package dji.thirdparty.sanselan.formats.tiff;

public class TiffHeader extends TiffElement {
    public final int byteOrder;
    public final int offsetToFirstIFD;
    public final int tiffVersion;

    public TiffHeader(int byteOrder2, int tiffVersion2, int offsetToFirstIFD2) {
        super(0, 8);
        this.byteOrder = byteOrder2;
        this.tiffVersion = tiffVersion2;
        this.offsetToFirstIFD = offsetToFirstIFD2;
    }

    public String getElementDescription(boolean verbose) {
        if (verbose) {
            return null;
        }
        return "TIFF Header";
    }
}
