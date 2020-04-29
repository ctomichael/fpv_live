package dji.thirdparty.sanselan.formats.tiff;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.formats.tiff.constants.TagInfo;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import java.io.IOException;
import java.util.ArrayList;

public class TiffDirectory extends TiffElement implements TiffConstants {
    public final ArrayList entries;
    private JpegImageData jpegImageData = null;
    public final int nextDirectoryOffset;
    public final int type;

    public String description() {
        return description(this.type);
    }

    public String getElementDescription(boolean verbose) {
        if (!verbose) {
            return "TIFF Directory (" + description() + ")";
        }
        int entryOffset = this.offset + 2;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < this.entries.size(); i++) {
            TiffField entry = (TiffField) this.entries.get(i);
            result.append("\t");
            result.append(IMemberProtocol.STRING_SEPERATOR_LEFT + entryOffset + "]: ");
            result.append(entry.tagInfo.name);
            result.append(" (" + entry.tag + ", 0x" + Integer.toHexString(entry.tag) + ")");
            result.append(", " + entry.fieldType.name);
            result.append(", " + entry.fieldType.getRawBytes(entry).length);
            result.append(": " + entry.getValueDescription());
            result.append("\n");
            entryOffset += 12;
        }
        return result.toString();
    }

    public static final String description(int type2) {
        switch (type2) {
            case -4:
                return "Interoperability";
            case -3:
                return "Gps";
            case -2:
                return "Exif";
            case -1:
                return "Unknown";
            case 0:
                return "Root";
            case 1:
                return "Sub";
            case 2:
                return "Thumbnail";
            default:
                return "Bad Type";
        }
    }

    public TiffDirectory(int type2, ArrayList entries2, int offset, int nextDirectoryOffset2) {
        super(offset, (entries2.size() * 12) + 2 + 4);
        this.type = type2;
        this.entries = entries2;
        this.nextDirectoryOffset = nextDirectoryOffset2;
    }

    public ArrayList getDirectoryEntrys() {
        return new ArrayList(this.entries);
    }

    /* access modifiers changed from: protected */
    public void fillInValues(ByteSource byteSource) throws ImageReadException, IOException {
        for (int i = 0; i < this.entries.size(); i++) {
            ((TiffField) this.entries.get(i)).fillInValue(byteSource);
        }
    }

    public void dump() {
        for (int i = 0; i < this.entries.size(); i++) {
            ((TiffField) this.entries.get(i)).dump();
        }
    }

    public boolean hasJpegImageData() throws ImageReadException {
        if (findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT) != null) {
            return true;
        }
        return false;
    }

    public boolean hasTiffImageData() throws ImageReadException {
        if (findField(TIFF_TAG_TILE_OFFSETS) == null && findField(TIFF_TAG_STRIP_OFFSETS) == null) {
            return false;
        }
        return true;
    }

    public TiffField findField(TagInfo tag) throws ImageReadException {
        return findField(tag, false);
    }

    public TiffField findField(TagInfo tag, boolean failIfMissing) throws ImageReadException {
        if (this.entries == null) {
            return null;
        }
        for (int i = 0; i < this.entries.size(); i++) {
            TiffField field = (TiffField) this.entries.get(i);
            if (field.tag == tag.tag) {
                return field;
            }
        }
        if (!failIfMissing) {
            return null;
        }
        throw new ImageReadException("Missing expected field: " + tag.getDescription());
    }

    public final class ImageDataElement extends TiffElement {
        public ImageDataElement(int offset, int length) {
            super(offset, length);
        }

        public String getElementDescription(boolean verbose) {
            if (verbose) {
                return null;
            }
            return "ImageDataElement";
        }
    }

    private ArrayList getRawImageDataElements(TiffField offsetsField, TiffField byteCountsField) throws ImageReadException {
        int[] offsets = offsetsField.getIntArrayValue();
        int[] byteCounts = byteCountsField.getIntArrayValue();
        if (offsets.length != byteCounts.length) {
            throw new ImageReadException("offsets.length(" + offsets.length + ") != byteCounts.length(" + byteCounts.length + ")");
        }
        ArrayList result = new ArrayList();
        for (int i = 0; i < offsets.length; i++) {
            result.add(new ImageDataElement(offsets[i], byteCounts[i]));
        }
        return result;
    }

    public ArrayList getTiffRawImageDataElements() throws ImageReadException {
        TiffField tileOffsets = findField(TIFF_TAG_TILE_OFFSETS);
        TiffField tileByteCounts = findField(TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField stripOffsets = findField(TIFF_TAG_STRIP_OFFSETS);
        TiffField stripByteCounts = findField(TIFF_TAG_STRIP_BYTE_COUNTS);
        if (tileOffsets != null && tileByteCounts != null) {
            return getRawImageDataElements(tileOffsets, tileByteCounts);
        }
        if (stripOffsets != null && stripByteCounts != null) {
            return getRawImageDataElements(stripOffsets, stripByteCounts);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public boolean imageDataInStrips() throws ImageReadException {
        TiffField tileOffsets = findField(TIFF_TAG_TILE_OFFSETS);
        TiffField tileByteCounts = findField(TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField stripOffsets = findField(TIFF_TAG_STRIP_OFFSETS);
        TiffField stripByteCounts = findField(TIFF_TAG_STRIP_BYTE_COUNTS);
        if (tileOffsets != null && tileByteCounts != null) {
            return false;
        }
        if (stripOffsets != null && stripByteCounts != null) {
            return true;
        }
        if (stripOffsets != null && stripByteCounts != null) {
            return true;
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public ImageDataElement getJpegRawImageDataElement() throws ImageReadException {
        TiffField jpegInterchangeFormat = findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        TiffField jpegInterchangeFormatLength = findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (jpegInterchangeFormat != null && jpegInterchangeFormatLength != null) {
            return new ImageDataElement(jpegInterchangeFormat.getIntArrayValue()[0], jpegInterchangeFormatLength.getIntArrayValue()[0]);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public void setJpegImageData(JpegImageData value) {
        this.jpegImageData = value;
    }

    public JpegImageData getJpegImageData() {
        return this.jpegImageData;
    }
}
