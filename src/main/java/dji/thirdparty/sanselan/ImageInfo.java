package dji.thirdparty.sanselan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class ImageInfo {
    public static final int COLOR_TYPE_BW = 0;
    public static final int COLOR_TYPE_CMYK = 3;
    public static final int COLOR_TYPE_GRAYSCALE = 1;
    public static final int COLOR_TYPE_OTHER = -1;
    public static final int COLOR_TYPE_RGB = 2;
    public static final int COLOR_TYPE_UNKNOWN = -2;
    public static final String COMPRESSION_ALGORITHM_CCITT_1D = "CCITT 1D";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_3 = "CCITT Group 3 1-Dimensional Modified Huffman run-length encoding.";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_4 = "CCITT Group 4";
    public static final String COMPRESSION_ALGORITHM_JPEG = "JPEG";
    public static final String COMPRESSION_ALGORITHM_LZW = "LZW";
    public static final String COMPRESSION_ALGORITHM_NONE = "None";
    public static final String COMPRESSION_ALGORITHM_PACKBITS = "PackBits";
    public static final String COMPRESSION_ALGORITHM_PNG_FILTER = "PNG Filter";
    public static final String COMPRESSION_ALGORITHM_PSD = "Photoshop";
    public static final String COMPRESSION_ALGORITHM_RLE = "RLE: Run-Length Encoding";
    public static final String COMPRESSION_ALGORITHM_UNKNOWN = "Unknown";
    private final int bitsPerPixel;
    private final int colorType;
    private final ArrayList comments;
    private final String compressionAlgorithm;
    private final ImageFormat format;
    private final String formatDetails;
    private final String formatName;
    private final int height;
    private final boolean isProgressive;
    private final boolean isTransparent;
    private final String mimeType;
    private final int numberOfImages;
    private final int physicalHeightDpi;
    private final float physicalHeightInch;
    private final int physicalWidthDpi;
    private final float physicalWidthInch;
    private final boolean usesPalette;
    private final int width;

    public ImageInfo(String formatDetails2, int bitsPerPixel2, ArrayList comments2, ImageFormat format2, String formatName2, int height2, String mimeType2, int numberOfImages2, int physicalHeightDpi2, float physicalHeightInch2, int physicalWidthDpi2, float physicalWidthInch2, int width2, boolean isProgressive2, boolean isTransparent2, boolean usesPalette2, int colorType2, String compressionAlgorithm2) {
        this.formatDetails = formatDetails2;
        this.bitsPerPixel = bitsPerPixel2;
        this.comments = comments2;
        this.format = format2;
        this.formatName = formatName2;
        this.height = height2;
        this.mimeType = mimeType2;
        this.numberOfImages = numberOfImages2;
        this.physicalHeightDpi = physicalHeightDpi2;
        this.physicalHeightInch = physicalHeightInch2;
        this.physicalWidthDpi = physicalWidthDpi2;
        this.physicalWidthInch = physicalWidthInch2;
        this.width = width2;
        this.isProgressive = isProgressive2;
        this.isTransparent = isTransparent2;
        this.usesPalette = usesPalette2;
        this.colorType = colorType2;
        this.compressionAlgorithm = compressionAlgorithm2;
    }

    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }

    public ArrayList getComments() {
        return new ArrayList(this.comments);
    }

    public ImageFormat getFormat() {
        return this.format;
    }

    public String getFormatName() {
        return this.formatName;
    }

    public int getHeight() {
        return this.height;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public int getNumberOfImages() {
        return this.numberOfImages;
    }

    public int getPhysicalHeightDpi() {
        return this.physicalHeightDpi;
    }

    public float getPhysicalHeightInch() {
        return this.physicalHeightInch;
    }

    public int getPhysicalWidthDpi() {
        return this.physicalWidthDpi;
    }

    public float getPhysicalWidthInch() {
        return this.physicalWidthInch;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean getIsProgressive() {
        return this.isProgressive;
    }

    public int getColorType() {
        return this.colorType;
    }

    public String getColorTypeDescription() {
        switch (this.colorType) {
            case -2:
                return "Unknown";
            case -1:
                return "Other";
            case 0:
                return "Black and White";
            case 1:
                return "Grayscale";
            case 2:
                return "RGB";
            case 3:
                return "CMYK";
            default:
                return "Unknown";
        }
    }

    public void dump() {
        System.out.print(toString());
    }

    public String toString() {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            toString(pw, "");
            pw.flush();
            return sw.toString();
        } catch (Exception e) {
            return "Image Data: Error";
        }
    }

    public void toString(PrintWriter pw, String prefix) throws ImageReadException, IOException {
        pw.println("Format Details: " + this.formatDetails);
        pw.println("Bits Per Pixel: " + this.bitsPerPixel);
        pw.println("Comments: " + this.comments.size());
        for (int i = 0; i < this.comments.size(); i++) {
            pw.println("\t" + i + ": '" + ((String) this.comments.get(i)) + "'");
        }
        pw.println("Format: " + this.format.name);
        pw.println("Format Name: " + this.formatName);
        pw.println("Compression Algorithm: " + this.compressionAlgorithm);
        pw.println("Height: " + this.height);
        pw.println("MimeType: " + this.mimeType);
        pw.println("Number Of Images: " + this.numberOfImages);
        pw.println("Physical Height Dpi: " + this.physicalHeightDpi);
        pw.println("Physical Height Inch: " + this.physicalHeightInch);
        pw.println("Physical Width Dpi: " + this.physicalWidthDpi);
        pw.println("Physical Width Inch: " + this.physicalWidthInch);
        pw.println("Width: " + this.width);
        pw.println("Is Progressive: " + this.isProgressive);
        pw.println("Is Transparent: " + this.isTransparent);
        pw.println("Color Type: " + getColorTypeDescription());
        pw.println("Uses Palette: " + this.usesPalette);
        pw.flush();
    }
}
