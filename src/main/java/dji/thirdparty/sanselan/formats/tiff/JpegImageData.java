package dji.thirdparty.sanselan.formats.tiff;

import dji.thirdparty.sanselan.formats.tiff.TiffElement;

public class JpegImageData extends TiffElement.DataElement {
    public JpegImageData(int offset, int length, byte[] data) {
        super(offset, length, data);
    }

    public String getElementDescription(boolean verbose) {
        return "Jpeg image data: " + this.data.length + " bytes";
    }
}
