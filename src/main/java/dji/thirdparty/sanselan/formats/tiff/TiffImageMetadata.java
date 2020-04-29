package dji.thirdparty.sanselan.formats.tiff;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.IImageMetadata;
import dji.thirdparty.sanselan.common.ImageMetadata;
import dji.thirdparty.sanselan.common.RationalNumber;
import dji.thirdparty.sanselan.formats.tiff.constants.TagInfo;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffDirectoryConstants;
import dji.thirdparty.sanselan.formats.tiff.fieldtypes.FieldType;
import dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory;
import dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField;
import dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet;
import java.util.ArrayList;
import java.util.List;

public class TiffImageMetadata extends ImageMetadata implements TiffDirectoryConstants {
    public final TiffContents contents;

    public TiffImageMetadata(TiffContents contents2) {
        this.contents = contents2;
    }

    public static class Directory extends ImageMetadata implements IImageMetadata.IImageMetadataItem {
        /* access modifiers changed from: private */
        public final TiffDirectory directory;
        public final int type;

        public Directory(TiffDirectory directory2) {
            this.type = directory2.type;
            this.directory = directory2;
        }

        public void add(TiffField entry) {
            add(new Item(entry));
        }

        public TiffField findField(TagInfo tagInfo) throws ImageReadException {
            return this.directory.findField(tagInfo);
        }

        public List getAllFields() throws ImageReadException {
            return this.directory.getDirectoryEntrys();
        }

        public JpegImageData getJpegImageData() {
            return this.directory.getJpegImageData();
        }

        public String toString(String prefix) {
            return (prefix != null ? prefix : "") + this.directory.description() + ": " + (getJpegImageData() != null ? " (jpegImageData)" : "") + "\n" + super.toString(prefix) + "\n";
        }

        public TiffOutputDirectory getOutputDirectory(int byteOrder) throws ImageWriteException {
            try {
                TiffOutputDirectory dstDir = new TiffOutputDirectory(this.type);
                ArrayList entries = getItems();
                for (int i = 0; i < entries.size(); i++) {
                    TiffField srcField = ((Item) entries.get(i)).getTiffField();
                    if (dstDir.findField(srcField.tag) == null && !(srcField.tagInfo instanceof TagInfo.Offset)) {
                        TagInfo tagInfo = srcField.tagInfo;
                        FieldType fieldType = srcField.fieldType;
                        TiffOutputField dstField = new TiffOutputField(srcField.tag, tagInfo, fieldType, srcField.length, tagInfo.encodeValue(fieldType, srcField.getValue(), byteOrder));
                        dstField.setSortHint(srcField.getSortHint());
                        dstDir.add(dstField);
                    }
                }
                dstDir.setJpegImageData(getJpegImageData());
                return dstDir;
            } catch (ImageReadException e) {
                throw new ImageWriteException(e.getMessage(), e);
            }
        }
    }

    public ArrayList getDirectories() {
        return super.getItems();
    }

    public ArrayList getItems() {
        ArrayList result = new ArrayList();
        ArrayList items = super.getItems();
        for (int i = 0; i < items.size(); i++) {
            result.addAll(((Directory) items.get(i)).getItems());
        }
        return result;
    }

    public static class Item extends ImageMetadata.Item {
        private final TiffField entry;

        public Item(TiffField entry2) {
            super(entry2.getTagName(), entry2.getValueDescription());
            this.entry = entry2;
        }

        public TiffField getTiffField() {
            return this.entry;
        }
    }

    public TiffOutputSet getOutputSet() throws ImageWriteException {
        int byteOrder = this.contents.header.byteOrder;
        TiffOutputSet result = new TiffOutputSet(byteOrder);
        ArrayList srcDirs = getDirectories();
        for (int i = 0; i < srcDirs.size(); i++) {
            Directory srcDir = (Directory) srcDirs.get(i);
            if (result.findDirectory(srcDir.type) == null) {
                result.addDirectory(srcDir.getOutputDirectory(byteOrder));
            }
        }
        return result;
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        ArrayList directories = getDirectories();
        for (int i = 0; i < directories.size(); i++) {
            TiffField field = ((Directory) directories.get(i)).findField(tagInfo);
            if (field != null) {
                return field;
            }
        }
        return null;
    }

    public TiffDirectory findDirectory(int directoryType) {
        ArrayList directories = getDirectories();
        for (int i = 0; i < directories.size(); i++) {
            Directory directory = (Directory) directories.get(i);
            if (directory.type == directoryType) {
                return directory.directory;
            }
        }
        return null;
    }

    public List getAllFields() throws ImageReadException {
        List result = new ArrayList();
        ArrayList directories = getDirectories();
        for (int i = 0; i < directories.size(); i++) {
            result.addAll(((Directory) directories.get(i)).getAllFields());
        }
        return result;
    }

    public GPSInfo getGPS() throws ImageReadException {
        TiffDirectory gpsDirectory = findDirectory(-3);
        if (gpsDirectory == null) {
            return null;
        }
        TiffField latitudeRefField = gpsDirectory.findField(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
        TiffField latitudeField = gpsDirectory.findField(TiffConstants.GPS_TAG_GPS_LATITUDE);
        TiffField longitudeRefField = gpsDirectory.findField(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
        TiffField longitudeField = gpsDirectory.findField(TiffConstants.GPS_TAG_GPS_LONGITUDE);
        if (latitudeRefField == null || latitudeField == null || longitudeRefField == null || longitudeField == null) {
            return null;
        }
        String latitudeRef = latitudeRefField.getStringValue();
        RationalNumber[] latitude = (RationalNumber[]) latitudeField.getValue();
        String longitudeRef = longitudeRefField.getStringValue();
        RationalNumber[] longitude = (RationalNumber[]) longitudeField.getValue();
        if (latitude.length == 3 && longitude.length == 3) {
            return new GPSInfo(latitudeRef, longitudeRef, latitude[0], latitude[1], latitude[2], longitude[0], longitude[1], longitude[2]);
        }
        throw new ImageReadException("Expected three values for latitude and longitude.");
    }

    public static class GPSInfo {
        public final RationalNumber latitudeDegrees;
        public final RationalNumber latitudeMinutes;
        public final String latitudeRef;
        public final RationalNumber latitudeSeconds;
        public final RationalNumber longitudeDegrees;
        public final RationalNumber longitudeMinutes;
        public final String longitudeRef;
        public final RationalNumber longitudeSeconds;

        public GPSInfo(String latitudeRef2, String longitudeRef2, RationalNumber latitudeDegrees2, RationalNumber latitudeMinutes2, RationalNumber latitudeSeconds2, RationalNumber longitudeDegrees2, RationalNumber longitudeMinutes2, RationalNumber longitudeSeconds2) {
            this.latitudeRef = latitudeRef2;
            this.longitudeRef = longitudeRef2;
            this.latitudeDegrees = latitudeDegrees2;
            this.latitudeMinutes = latitudeMinutes2;
            this.latitudeSeconds = latitudeSeconds2;
            this.longitudeDegrees = longitudeDegrees2;
            this.longitudeMinutes = longitudeMinutes2;
            this.longitudeSeconds = longitudeSeconds2;
        }

        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append("[GPS. ");
            result.append("Latitude: " + this.latitudeDegrees.toDisplayString() + " degrees, " + this.latitudeMinutes.toDisplayString() + " minutes, " + this.latitudeSeconds.toDisplayString() + " seconds " + this.latitudeRef);
            result.append(", Longitude: " + this.longitudeDegrees.toDisplayString() + " degrees, " + this.longitudeMinutes.toDisplayString() + " minutes, " + this.longitudeSeconds.toDisplayString() + " seconds " + this.longitudeRef);
            result.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            return result.toString();
        }

        public double getLongitudeAsDegreesEast() throws ImageReadException {
            double result = this.longitudeDegrees.doubleValue() + (this.longitudeMinutes.doubleValue() / 60.0d) + (this.longitudeSeconds.doubleValue() / 3600.0d);
            if (this.longitudeRef.trim().equalsIgnoreCase("e")) {
                return result;
            }
            if (this.longitudeRef.trim().equalsIgnoreCase("w")) {
                return -result;
            }
            throw new ImageReadException("Unknown longitude ref: \"" + this.longitudeRef + "\"");
        }

        public double getLatitudeAsDegreesNorth() throws ImageReadException {
            double result = this.latitudeDegrees.doubleValue() + (this.latitudeMinutes.doubleValue() / 60.0d) + (this.latitudeSeconds.doubleValue() / 3600.0d);
            if (this.latitudeRef.trim().equalsIgnoreCase("n")) {
                return result;
            }
            if (this.latitudeRef.trim().equalsIgnoreCase("s")) {
                return -result;
            }
            throw new ImageReadException("Unknown latitude ref: \"" + this.latitudeRef + "\"");
        }
    }
}
