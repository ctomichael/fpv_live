package dji.thirdparty.sanselan;

import dji.thirdparty.sanselan.common.BinaryFileParser;
import dji.thirdparty.sanselan.common.IImageMetadata;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.common.byteSources.ByteSourceArray;
import dji.thirdparty.sanselan.common.byteSources.ByteSourceFile;
import dji.thirdparty.sanselan.formats.jpeg.JpegImageParser;
import dji.thirdparty.sanselan.formats.tiff.TiffImageParser;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public abstract class ImageParser extends BinaryFileParser implements SanselanConstants {
    public abstract boolean embedICCProfile(File file, File file2, byte[] bArr);

    /* access modifiers changed from: protected */
    public abstract String[] getAcceptedExtensions();

    /* access modifiers changed from: protected */
    public abstract ImageFormat[] getAcceptedTypes();

    public abstract String getDefaultExtension();

    public abstract byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract String getName();

    public abstract String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public static final ImageParser[] getAllImageParsers() {
        return new ImageParser[]{new JpegImageParser(), new TiffImageParser()};
    }

    public final IImageMetadata getMetadata(ByteSource byteSource) throws ImageReadException, IOException {
        return getMetadata(byteSource, (Map) null);
    }

    public final IImageMetadata getMetadata(byte[] bytes, Map params) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceArray(bytes), params);
    }

    public final IImageMetadata getMetadata(File file) throws ImageReadException, IOException {
        return getMetadata(file, (Map) null);
    }

    public final IImageMetadata getMetadata(File file, Map params) throws ImageReadException, IOException {
        if (this.debug) {
            System.out.println(getName() + ".getMetadata: " + file.getName());
        }
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getMetadata(new ByteSourceFile(file), params);
    }

    public final ImageInfo getImageInfo(ByteSource byteSource) throws ImageReadException, IOException {
        return getImageInfo(byteSource, (Map) null);
    }

    public final ImageInfo getImageInfo(byte[] bytes, Map params) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceArray(bytes), params);
    }

    public final ImageInfo getImageInfo(File file, Map params) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getImageInfo(new ByteSourceFile(file), params);
    }

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        return null;
    }

    public final FormatCompliance getFormatCompliance(byte[] bytes) throws ImageReadException, IOException {
        return getFormatCompliance(new ByteSourceArray(bytes));
    }

    public final FormatCompliance getFormatCompliance(File file) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getFormatCompliance(new ByteSourceFile(file));
    }

    public final byte[] getICCProfileBytes(byte[] bytes) throws ImageReadException, IOException {
        return getICCProfileBytes(bytes, (Map) null);
    }

    public final byte[] getICCProfileBytes(byte[] bytes, Map params) throws ImageReadException, IOException {
        return getICCProfileBytes(new ByteSourceArray(bytes), params);
    }

    public final byte[] getICCProfileBytes(File file) throws ImageReadException, IOException {
        return getICCProfileBytes(file, (Map) null);
    }

    public final byte[] getICCProfileBytes(File file, Map params) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (this.debug) {
            System.out.println(getName() + ": " + file.getName());
        }
        return getICCProfileBytes(new ByteSourceFile(file), params);
    }

    public final String dumpImageFile(byte[] bytes) throws ImageReadException, IOException {
        return dumpImageFile(new ByteSourceArray(bytes));
    }

    public final String dumpImageFile(File file) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (this.debug) {
            System.out.println(getName() + ": " + file.getName());
        }
        return dumpImageFile(new ByteSourceFile(file));
    }

    public final String dumpImageFile(ByteSource byteSource) throws ImageReadException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        dumpImageFile(pw, byteSource);
        pw.flush();
        return sw.toString();
    }

    public boolean dumpImageFile(PrintWriter pw, ByteSource byteSource) throws ImageReadException, IOException {
        return false;
    }

    public boolean canAcceptType(ImageFormat type) {
        ImageFormat[] types;
        for (ImageFormat imageFormat : getAcceptedTypes()) {
            if (imageFormat.equals(type)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean canAcceptExtension(File file) {
        return canAcceptExtension(file.getName());
    }

    /* access modifiers changed from: protected */
    public final boolean canAcceptExtension(String filename) {
        String[] exts = getAcceptedExtensions();
        if (exts == null) {
            return true;
        }
        int index = filename.lastIndexOf(46);
        if (index >= 0) {
            String ext = filename.substring(index).toLowerCase();
            for (String str : exts) {
                if (str.toLowerCase().equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean isStrict(Map params) {
        if (params == null || !params.containsKey(SanselanConstants.PARAM_KEY_STRICT)) {
            return false;
        }
        return ((Boolean) params.get(SanselanConstants.PARAM_KEY_STRICT)).booleanValue();
    }
}
