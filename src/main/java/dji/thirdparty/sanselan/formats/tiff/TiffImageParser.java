package dji.thirdparty.sanselan.formats.tiff;

import dji.thirdparty.sanselan.FormatCompliance;
import dji.thirdparty.sanselan.ImageFormat;
import dji.thirdparty.sanselan.ImageInfo;
import dji.thirdparty.sanselan.ImageParser;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.common.IImageMetadata;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.formats.tiff.TiffDirectory;
import dji.thirdparty.sanselan.formats.tiff.TiffImageMetadata;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TiffImageParser extends ImageParser implements TiffConstants {
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION, ".tiff"};
    private static final String DEFAULT_EXTENSION = ".tif";

    public String getName() {
        return "Tiff-Custom";
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_TIFF};
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        TiffField field = ((TiffDirectory) new TiffReader(isStrict(params)).readFirstDirectory(byteSource, params, false, FormatCompliance.getDefault()).directories.get(0)).findField(EXIF_TAG_ICC_PROFILE);
        if (field == null) {
            return null;
        }
        return field.oversizeValue;
    }

    public int[] getImageSize(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        TiffDirectory directory = (TiffDirectory) new TiffReader(isStrict(params)).readFirstDirectory(byteSource, params, false, FormatCompliance.getDefault()).directories.get(0);
        return new int[]{directory.findField(TIFF_TAG_IMAGE_WIDTH).getIntValue(), directory.findField(TIFF_TAG_IMAGE_LENGTH).getIntValue()};
    }

    public byte[] embedICCProfile(byte[] image, byte[] profile) {
        return null;
    }

    public boolean embedICCProfile(File src, File dst, byte[] profile) {
        return false;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        TiffContents contents = new TiffReader(isStrict(params)).readContents(byteSource, params, FormatCompliance.getDefault());
        ArrayList directories = contents.directories;
        TiffImageMetadata result = new TiffImageMetadata(contents);
        for (int i = 0; i < directories.size(); i++) {
            TiffDirectory dir = (TiffDirectory) directories.get(i);
            TiffImageMetadata.Directory metadataDirectory = new TiffImageMetadata.Directory(dir);
            ArrayList entries = dir.getDirectoryEntrys();
            for (int j = 0; j < entries.size(); j++) {
                metadataDirectory.add((TiffField) entries.get(j));
            }
            result.add(metadataDirectory);
        }
        return result;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, boolean, dji.thirdparty.sanselan.FormatCompliance):dji.thirdparty.sanselan.formats.tiff.TiffContents
     arg types: [dji.thirdparty.sanselan.common.byteSources.ByteSource, int, dji.thirdparty.sanselan.FormatCompliance]
     candidates:
      dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, dji.thirdparty.sanselan.FormatCompliance, dji.thirdparty.sanselan.formats.tiff.TiffReader$Listener):void
      dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, boolean, dji.thirdparty.sanselan.FormatCompliance):dji.thirdparty.sanselan.formats.tiff.TiffContents */
    public ImageInfo getImageInfo(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        String compressionAlgorithm;
        TiffContents contents = new TiffReader(isStrict(params)).readDirectories(byteSource, false, FormatCompliance.getDefault());
        TiffDirectory directory = (TiffDirectory) contents.directories.get(0);
        TiffField widthField = directory.findField(TIFF_TAG_IMAGE_WIDTH, true);
        TiffField heightField = directory.findField(TIFF_TAG_IMAGE_LENGTH, true);
        if (widthField == null || heightField == null) {
            throw new ImageReadException("TIFF image missing size info.");
        }
        int height = heightField.getIntValue();
        int width = widthField.getIntValue();
        TiffField resolutionUnitField = directory.findField(TIFF_TAG_RESOLUTION_UNIT);
        int resolutionUnit = 2;
        if (!(resolutionUnitField == null || resolutionUnitField.getValue() == null)) {
            resolutionUnit = resolutionUnitField.getIntValue();
        }
        double unitsPerInch = -1.0d;
        switch (resolutionUnit) {
            case 2:
                unitsPerInch = 1.0d;
                break;
            case 3:
                unitsPerInch = 0.0254d;
                break;
        }
        TiffField xResolutionField = directory.findField(TIFF_TAG_XRESOLUTION);
        TiffField yResolutionField = directory.findField(TIFF_TAG_YRESOLUTION);
        int physicalWidthDpi = -1;
        float physicalWidthInch = -1.0f;
        int physicalHeightDpi = -1;
        float physicalHeightInch = -1.0f;
        if (unitsPerInch > 0.0d) {
            if (!(xResolutionField == null || xResolutionField.getValue() == null)) {
                double XResolutionPixelsPerUnit = xResolutionField.getDoubleValue();
                physicalWidthDpi = (int) (XResolutionPixelsPerUnit / unitsPerInch);
                physicalWidthInch = (float) (((double) width) / (XResolutionPixelsPerUnit * unitsPerInch));
            }
            if (!(yResolutionField == null || yResolutionField.getValue() == null)) {
                double YResolutionPixelsPerUnit = yResolutionField.getDoubleValue();
                physicalHeightDpi = (int) (YResolutionPixelsPerUnit / unitsPerInch);
                physicalHeightInch = (float) (((double) height) / (YResolutionPixelsPerUnit * unitsPerInch));
            }
        }
        TiffField bitsPerSampleField = directory.findField(TIFF_TAG_BITS_PER_SAMPLE);
        int bitsPerSample = -1;
        if (!(bitsPerSampleField == null || bitsPerSampleField.getValue() == null)) {
            bitsPerSample = bitsPerSampleField.getIntValueOrArraySum();
        }
        int bitsPerPixel = bitsPerSample;
        ArrayList comments = new ArrayList();
        ArrayList entries = directory.entries;
        for (int i = 0; i < entries.size(); i++) {
            comments.add(((TiffField) entries.get(i)).toString());
        }
        ImageFormat format = ImageFormat.IMAGE_FORMAT_TIFF;
        int numberOfImages = contents.directories.size();
        String formatDetails = "Tiff v." + contents.header.tiffVersion;
        boolean usesPalette = false;
        if (directory.findField(TIFF_TAG_COLOR_MAP) != null) {
            usesPalette = true;
        }
        switch (directory.findField(TIFF_TAG_COMPRESSION).getIntValue()) {
            case 1:
                compressionAlgorithm = "None";
                break;
            case 2:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_CCITT_1D;
                break;
            case 3:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_CCITT_GROUP_3;
                break;
            case 4:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_CCITT_GROUP_4;
                break;
            case 5:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_LZW;
                break;
            case 6:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_JPEG;
                break;
            case 32771:
                compressionAlgorithm = "None";
                break;
            case 32773:
                compressionAlgorithm = ImageInfo.COMPRESSION_ALGORITHM_PACKBITS;
                break;
            default:
                compressionAlgorithm = "Unknown";
                break;
        }
        return new ImageInfo(formatDetails, bitsPerPixel, comments, format, "TIFF Tag-based Image File Format", height, "image/tiff", numberOfImages, physicalHeightDpi, physicalHeightInch, physicalWidthDpi, physicalWidthInch, width, false, false, usesPalette, 2, compressionAlgorithm);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, boolean, dji.thirdparty.sanselan.FormatCompliance):dji.thirdparty.sanselan.formats.tiff.TiffContents
     arg types: [dji.thirdparty.sanselan.common.byteSources.ByteSource, int, dji.thirdparty.sanselan.FormatCompliance]
     candidates:
      dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, dji.thirdparty.sanselan.FormatCompliance, dji.thirdparty.sanselan.formats.tiff.TiffReader$Listener):void
      dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, boolean, dji.thirdparty.sanselan.FormatCompliance):dji.thirdparty.sanselan.formats.tiff.TiffContents */
    public String getXmpXml(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        TiffField xmpField = ((TiffDirectory) new TiffReader(isStrict(params)).readDirectories(byteSource, false, FormatCompliance.getDefault()).directories.get(0)).findField(TIFF_TAG_XMP, false);
        if (xmpField == null) {
            return null;
        }
        try {
            return new String(xmpField.getByteArrayValue(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ImageReadException("Invalid JPEG XMP Segment.");
        }
    }

    public boolean dumpImageFile(PrintWriter pw, ByteSource byteSource) throws ImageReadException, IOException {
        pw.println("tiff.dumpImageFile");
        ImageInfo imageData = getImageInfo(byteSource);
        if (imageData == null) {
            pw.println("");
            return false;
        }
        imageData.toString(pw, "");
        pw.println("");
        ArrayList directories = new TiffReader(true).readContents(byteSource, null, FormatCompliance.getDefault()).directories;
        if (directories == null) {
            pw.println("");
            return false;
        }
        for (int d = 0; d < directories.size(); d++) {
            ArrayList entries = ((TiffDirectory) directories.get(d)).entries;
            if (entries == null) {
                pw.println("");
                return false;
            }
            int i = 0;
            while (i < entries.size()) {
                try {
                    ((TiffField) entries.get(i)).dump(pw, d + "");
                    i++;
                } catch (Throwable th) {
                    pw.println("");
                    throw th;
                }
            }
        }
        pw.println("");
        pw.println("");
        return true;
    }

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        FormatCompliance formatCompliance = FormatCompliance.getDefault();
        new TiffReader(isStrict(null)).readContents(byteSource, null, formatCompliance);
        return formatCompliance;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, boolean, dji.thirdparty.sanselan.FormatCompliance):dji.thirdparty.sanselan.formats.tiff.TiffContents
     arg types: [dji.thirdparty.sanselan.common.byteSources.ByteSource, int, dji.thirdparty.sanselan.FormatCompliance]
     candidates:
      dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, dji.thirdparty.sanselan.FormatCompliance, dji.thirdparty.sanselan.formats.tiff.TiffReader$Listener):void
      dji.thirdparty.sanselan.formats.tiff.TiffReader.readDirectories(dji.thirdparty.sanselan.common.byteSources.ByteSource, boolean, dji.thirdparty.sanselan.FormatCompliance):dji.thirdparty.sanselan.formats.tiff.TiffContents */
    public List collectRawImageData(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        TiffContents contents = new TiffReader(isStrict(params)).readDirectories(byteSource, true, FormatCompliance.getDefault());
        List result = new ArrayList();
        for (int i = 0; i < contents.directories.size(); i++) {
            List dataElements = ((TiffDirectory) contents.directories.get(i)).getTiffRawImageDataElements();
            for (int j = 0; j < dataElements.size(); j++) {
                TiffDirectory.ImageDataElement element = (TiffDirectory.ImageDataElement) dataElements.get(j);
                result.add(byteSource.getBlock(element.offset, element.length));
            }
        }
        return result;
    }
}
