package dji.thirdparty.sanselan.formats.tiff.write;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.constants.TagInfo;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import dji.thirdparty.sanselan.util.Debug;
import java.util.ArrayList;
import java.util.List;

public final class TiffOutputSet implements TiffConstants {
    private static final String newline = System.getProperty("line.separator");
    public final int byteOrder;
    private final ArrayList directories;

    public TiffOutputSet() {
        this(73);
    }

    public TiffOutputSet(int byteOrder2) {
        this.directories = new ArrayList();
        this.byteOrder = byteOrder2;
    }

    /* access modifiers changed from: protected */
    public List getOutputItems(TiffOutputSummary outputSummary) throws ImageWriteException {
        List result = new ArrayList();
        for (int i = 0; i < this.directories.size(); i++) {
            result.addAll(((TiffOutputDirectory) this.directories.get(i)).getOutputItems(outputSummary));
        }
        return result;
    }

    public void addDirectory(TiffOutputDirectory directory) throws ImageWriteException {
        if (findDirectory(directory.type) != null) {
            throw new ImageWriteException("Output set already contains a directory of that type.");
        }
        this.directories.add(directory);
    }

    public List getDirectories() {
        return new ArrayList(this.directories);
    }

    public TiffOutputDirectory getRootDirectory() {
        return findDirectory(0);
    }

    public TiffOutputDirectory getExifDirectory() {
        return findDirectory(-2);
    }

    public TiffOutputDirectory getOrCreateRootDirectory() throws ImageWriteException {
        TiffOutputDirectory result = findDirectory(0);
        return result != null ? result : addRootDirectory();
    }

    public TiffOutputDirectory getOrCreateExifDirectory() throws ImageWriteException {
        getOrCreateRootDirectory();
        TiffOutputDirectory result = findDirectory(-2);
        return result != null ? result : addExifDirectory();
    }

    public TiffOutputDirectory getOrCreateGPSDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory result = findDirectory(-3);
        return result != null ? result : addGPSDirectory();
    }

    public TiffOutputDirectory getGPSDirectory() {
        return findDirectory(-3);
    }

    public TiffOutputDirectory getInteroperabilityDirectory() {
        return findDirectory(-4);
    }

    public TiffOutputDirectory findDirectory(int directoryType) {
        for (int i = 0; i < this.directories.size(); i++) {
            TiffOutputDirectory directory = (TiffOutputDirectory) this.directories.get(i);
            if (directory.type == directoryType) {
                return directory;
            }
        }
        return null;
    }

    public void setGPSInDegrees(double longitude, double latitude) throws ImageWriteException {
        TiffOutputDirectory gpsDirectory = getOrCreateGPSDirectory();
        String longitudeRef = longitude < 0.0d ? "W" : "E";
        double longitude2 = Math.abs(longitude);
        String latitudeRef = latitude < 0.0d ? "S" : "N";
        double latitude2 = Math.abs(latitude);
        TiffOutputField longitudeRefField = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF, this.byteOrder, longitudeRef);
        gpsDirectory.removeField(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
        gpsDirectory.add(longitudeRefField);
        TiffOutputField latitudeRefField = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LATITUDE_REF, this.byteOrder, latitudeRef);
        gpsDirectory.removeField(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
        gpsDirectory.add(latitudeRefField);
        double value = longitude2;
        double longitudeDegrees = (double) ((long) value);
        double value2 = (value % 1.0d) * 60.0d;
        TiffOutputField longitudeField = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LONGITUDE, this.byteOrder, new Double[]{new Double(longitudeDegrees), new Double((double) ((long) value2)), new Double((value2 % 1.0d) * 60.0d)});
        gpsDirectory.removeField(TiffConstants.GPS_TAG_GPS_LONGITUDE);
        gpsDirectory.add(longitudeField);
        double value3 = latitude2;
        double latitudeDegrees = (double) ((long) value3);
        double value4 = (value3 % 1.0d) * 60.0d;
        TiffOutputField latitudeField = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LATITUDE, this.byteOrder, new Double[]{new Double(latitudeDegrees), new Double((double) ((long) value4)), new Double((value4 % 1.0d) * 60.0d)});
        gpsDirectory.removeField(TiffConstants.GPS_TAG_GPS_LATITUDE);
        gpsDirectory.add(latitudeField);
    }

    public void removeField(TagInfo tagInfo) {
        removeField(tagInfo.tag);
    }

    public void removeField(int tag) {
        for (int i = 0; i < this.directories.size(); i++) {
            ((TiffOutputDirectory) this.directories.get(i)).removeField(tag);
        }
    }

    public TiffOutputField findField(TagInfo tagInfo) {
        return findField(tagInfo.tag);
    }

    public TiffOutputField findField(int tag) {
        for (int i = 0; i < this.directories.size(); i++) {
            TiffOutputField field = ((TiffOutputDirectory) this.directories.get(i)).findField(tag);
            if (field != null) {
                return field;
            }
        }
        return null;
    }

    public TiffOutputDirectory addRootDirectory() throws ImageWriteException {
        TiffOutputDirectory result = new TiffOutputDirectory(0);
        addDirectory(result);
        return result;
    }

    public TiffOutputDirectory addExifDirectory() throws ImageWriteException {
        TiffOutputDirectory result = new TiffOutputDirectory(-2);
        addDirectory(result);
        return result;
    }

    public TiffOutputDirectory addGPSDirectory() throws ImageWriteException {
        TiffOutputDirectory result = new TiffOutputDirectory(-3);
        addDirectory(result);
        return result;
    }

    public TiffOutputDirectory addInteroperabilityDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory result = new TiffOutputDirectory(-4);
        addDirectory(result);
        return result;
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        StringBuffer result = new StringBuffer();
        result.append(prefix);
        result.append("TiffOutputSet {");
        result.append(newline);
        result.append(prefix);
        result.append("byteOrder: " + this.byteOrder);
        result.append(newline);
        for (int i = 0; i < this.directories.size(); i++) {
            TiffOutputDirectory directory = (TiffOutputDirectory) this.directories.get(i);
            result.append(prefix);
            result.append("\tdirectory " + i + ": " + directory.description() + " (" + directory.type + ")");
            result.append(newline);
            ArrayList fields = directory.getFields();
            for (int j = 0; j < fields.size(); j++) {
                result.append(prefix);
                result.append("\t\tfield " + i + ": " + ((TiffOutputField) fields.get(j)).tagInfo);
                result.append(newline);
            }
        }
        result.append(prefix);
        result.append("}");
        result.append(newline);
        return result.toString();
    }

    public void dump() {
        Debug.debug(toString());
    }
}
