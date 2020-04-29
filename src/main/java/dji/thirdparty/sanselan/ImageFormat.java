package dji.thirdparty.sanselan;

import com.billy.cc.core.component.CCUtil;

public class ImageFormat {
    public static final ImageFormat IMAGE_FORMAT_BMP = new ImageFormat("BMP");
    public static final ImageFormat IMAGE_FORMAT_GIF = new ImageFormat("GIF");
    public static final ImageFormat IMAGE_FORMAT_ICO = new ImageFormat("ICO");
    public static final ImageFormat IMAGE_FORMAT_JBIG2 = new ImageFormat("JBig2");
    public static final ImageFormat IMAGE_FORMAT_JPEG = new ImageFormat(ImageInfo.COMPRESSION_ALGORITHM_JPEG);
    public static final ImageFormat IMAGE_FORMAT_PBM = new ImageFormat("PBM");
    public static final ImageFormat IMAGE_FORMAT_PGM = new ImageFormat("PGM");
    public static final ImageFormat IMAGE_FORMAT_PNG = new ImageFormat("PNG");
    public static final ImageFormat IMAGE_FORMAT_PNM = new ImageFormat("PNM");
    public static final ImageFormat IMAGE_FORMAT_PPM = new ImageFormat("PPM");
    public static final ImageFormat IMAGE_FORMAT_PSD = new ImageFormat("PSD");
    public static final ImageFormat IMAGE_FORMAT_TGA = new ImageFormat("TGA");
    public static final ImageFormat IMAGE_FORMAT_TIFF = new ImageFormat("TIFF");
    public static final ImageFormat IMAGE_FORMAT_UNKNOWN = new ImageFormat(CCUtil.PROCESS_UNKNOWN, false);
    public final boolean actual;
    public final String extension;
    public final String name;

    private ImageFormat(String name2, boolean actual2) {
        this.name = name2;
        this.extension = name2;
        this.actual = actual2;
    }

    private ImageFormat(String name2) {
        this.name = name2;
        this.extension = name2;
        this.actual = true;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ImageFormat)) {
            return false;
        }
        return ((ImageFormat) o).name.equals(this.name);
    }

    public String toString() {
        return "{" + this.name + ": " + this.extension + "}";
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public static final ImageFormat[] getAllFormats() {
        return new ImageFormat[]{IMAGE_FORMAT_UNKNOWN, IMAGE_FORMAT_PNG, IMAGE_FORMAT_GIF, IMAGE_FORMAT_TIFF, IMAGE_FORMAT_JPEG, IMAGE_FORMAT_BMP, IMAGE_FORMAT_PSD, IMAGE_FORMAT_PBM, IMAGE_FORMAT_PGM, IMAGE_FORMAT_PPM, IMAGE_FORMAT_PNM, IMAGE_FORMAT_TGA, IMAGE_FORMAT_JBIG2};
    }
}
