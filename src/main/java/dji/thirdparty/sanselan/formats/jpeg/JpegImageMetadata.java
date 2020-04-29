package dji.thirdparty.sanselan.formats.jpeg;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.common.IImageMetadata;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.formats.tiff.TiffImageMetadata;
import dji.thirdparty.sanselan.formats.tiff.constants.TagInfo;
import dji.thirdparty.sanselan.util.Debug;
import java.io.IOException;
import java.util.ArrayList;

public class JpegImageMetadata implements IImageMetadata {
    private static final String newline = System.getProperty("line.separator");
    private final TiffImageMetadata exif;

    public JpegImageMetadata(Object photoshop, TiffImageMetadata exif2) {
        this.exif = exif2;
    }

    public TiffImageMetadata getExif() {
        return this.exif;
    }

    public TiffField findEXIFValue(TagInfo tagInfo) {
        ArrayList items = getItems();
        for (int i = 0; i < items.size(); i++) {
            Object o = items.get(i);
            if (o instanceof TiffImageMetadata.Item) {
                TiffField field = ((TiffImageMetadata.Item) o).getTiffField();
                if (field.tag == tagInfo.tag) {
                    return field;
                }
            }
        }
        return null;
    }

    public Object getEXIFThumbnail() throws ImageReadException, IOException {
        return null;
    }

    public ArrayList getItems() {
        ArrayList result = new ArrayList();
        if (this.exif != null) {
            result.addAll(this.exif.getItems());
        }
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
        if (this.exif == null) {
            result.append("No Exif metadata.");
        } else {
            result.append("Exif metadata:");
            result.append(newline);
            result.append(this.exif.toString("\t"));
        }
        result.append(newline);
        result.append(prefix);
        result.append("No Photoshop (IPTC) metadata.");
        return result.toString();
    }

    public void dump() {
        Debug.debug(toString());
    }
}
