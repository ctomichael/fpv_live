package dji.thirdparty.sanselan.formats.jpeg;

import dji.thirdparty.sanselan.ImageFormat;
import dji.thirdparty.sanselan.ImageInfo;
import dji.thirdparty.sanselan.ImageParser;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.SanselanConstants;
import dji.thirdparty.sanselan.common.BinaryFileParser;
import dji.thirdparty.sanselan.common.IImageMetadata;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.formats.jpeg.JpegUtils;
import dji.thirdparty.sanselan.formats.jpeg.segments.App2Segment;
import dji.thirdparty.sanselan.formats.jpeg.segments.GenericSegment;
import dji.thirdparty.sanselan.formats.jpeg.segments.JFIFSegment;
import dji.thirdparty.sanselan.formats.jpeg.segments.SOFNSegment;
import dji.thirdparty.sanselan.formats.jpeg.segments.Segment;
import dji.thirdparty.sanselan.formats.jpeg.segments.UnknownSegment;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.formats.tiff.TiffImageMetadata;
import dji.thirdparty.sanselan.formats.tiff.TiffImageParser;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffTagConstants;
import dji.thirdparty.sanselan.util.Debug;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JpegImageParser extends ImageParser implements JpegConstants, TiffTagConstants {
    public static final String[] AcceptedExtensions = {DEFAULT_EXTENSION, ".jpeg"};
    private static final String DEFAULT_EXTENSION = ".jpg";
    public static final boolean permissive = true;

    public JpegImageParser() {
        setByteOrder(77);
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_JPEG};
    }

    public String getName() {
        return "Jpeg-Custom";
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return AcceptedExtensions;
    }

    /* access modifiers changed from: private */
    public boolean keepMarker(int marker, int[] markers) {
        if (markers == null) {
            return true;
        }
        for (int i : markers) {
            if (i == marker) {
                return true;
            }
        }
        return false;
    }

    public ArrayList readSegments(ByteSource byteSource, final int[] markers, final boolean returnAfterFirst, boolean readEverything) throws ImageReadException, IOException {
        final ArrayList result = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() {
            /* class dji.thirdparty.sanselan.formats.jpeg.JpegImageParser.AnonymousClass1 */

            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int marker, byte[] markerBytes, byte[] imageData) {
            }

            public boolean visitSOS(int marker, byte[] markerBytes, InputStream is) {
                return false;
            }

            public boolean visitSegment(int marker, byte[] markerBytes, int markerLength, byte[] markerLengthBytes, byte[] segmentData) throws ImageReadException, IOException {
                if (marker == 65497) {
                    return false;
                }
                if (!JpegImageParser.this.keepMarker(marker, markers)) {
                    return true;
                }
                if (marker != 65517) {
                    if (marker == 65506) {
                        result.add(new App2Segment(marker, segmentData));
                    } else if (marker == 65504) {
                        result.add(new JFIFSegment(marker, segmentData));
                    } else if (marker >= 65472 && marker <= 65487) {
                        result.add(new SOFNSegment(marker, segmentData));
                    } else if (marker >= 65505 && marker <= 65519) {
                        result.add(new UnknownSegment(marker, segmentData));
                    }
                }
                if (!returnAfterFirst) {
                    return true;
                }
                return false;
            }
        });
        return result;
    }

    private byte[] assembleSegments(ArrayList v) throws ImageReadException, IOException {
        try {
            return assembleSegments(v, false);
        } catch (ImageReadException e) {
            return assembleSegments(v, true);
        }
    }

    private byte[] assembleSegments(ArrayList v, boolean start_with_zero) throws ImageReadException, IOException {
        int offset = 1;
        if (v.size() < 1) {
            throw new ImageReadException("No App2 Segments Found.");
        }
        int markerCount = ((App2Segment) v.get(0)).num_markers;
        if (v.size() != markerCount) {
            throw new ImageReadException("App2 Segments Missing.  Found: " + v.size() + ", Expected: " + markerCount + ".");
        }
        Collections.sort(v);
        if (start_with_zero) {
            offset = 0;
        }
        int total = 0;
        int i = 0;
        while (i < v.size()) {
            App2Segment segment = (App2Segment) v.get(i);
            if (i + offset != segment.cur_marker) {
                dumpSegments(v);
                throw new ImageReadException("Incoherent App2 Segment Ordering.  i: " + i + ", segment[" + i + "].cur_marker: " + segment.cur_marker + ".");
            } else if (markerCount != segment.num_markers) {
                dumpSegments(v);
                throw new ImageReadException("Inconsistent App2 Segment Count info.  markerCount: " + markerCount + ", segment[" + i + "].num_markers: " + segment.num_markers + ".");
            } else {
                total += segment.icc_bytes.length;
                i++;
            }
        }
        byte[] result = new byte[total];
        int progress = 0;
        for (int i2 = 0; i2 < v.size(); i2++) {
            App2Segment segment2 = (App2Segment) v.get(i2);
            System.arraycopy(segment2.icc_bytes, 0, result, progress, segment2.icc_bytes.length);
            progress += segment2.icc_bytes.length;
        }
        return result;
    }

    private void dumpSegments(ArrayList v) {
        Debug.debug();
        Debug.debug("dumpSegments", v.size());
        for (int i = 0; i < v.size(); i++) {
            App2Segment segment = (App2Segment) v.get(i);
            Debug.debug(i + ": " + segment.cur_marker + " / " + segment.num_markers);
        }
        Debug.debug();
    }

    public ArrayList readSegments(ByteSource byteSource, int[] markers, boolean returnAfterFirst) throws ImageReadException, IOException {
        return readSegments(byteSource, markers, returnAfterFirst, false);
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        String str = null;
        ArrayList segments = readSegments(byteSource, new int[]{65506}, false);
        if (segments != null) {
            ArrayList filtered = new ArrayList();
            for (int i = 0; i < segments.size(); i++) {
                App2Segment segment = (App2Segment) segments.get(i);
                if (segment.icc_bytes != null) {
                    filtered.add(segment);
                }
            }
            segments = filtered;
        }
        if (segments == null || segments.size() < 1) {
            return null;
        }
        byte[] bytes = assembleSegments(segments);
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuilder append = new StringBuilder().append("bytes: ");
            if (bytes != null) {
                str = "" + bytes.length;
            }
            printStream.println(append.append(str).toString());
        }
        if (!this.debug) {
            return bytes;
        }
        System.out.println("");
        return bytes;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        TiffImageMetadata exif = getExifMetadata(byteSource, params);
        if (exif == null && 0 == 0) {
            return null;
        }
        return new JpegImageMetadata(null, exif);
    }

    public static boolean isExifAPP1Segment(GenericSegment segment) {
        return byteArrayHasPrefix(segment.bytes, EXIF_IDENTIFIER_CODE);
    }

    private ArrayList filterAPP1Segments(ArrayList v) {
        ArrayList result = new ArrayList();
        for (int i = 0; i < v.size(); i++) {
            GenericSegment segment = (GenericSegment) v.get(i);
            if (isExifAPP1Segment(segment)) {
                result.add(segment);
            }
        }
        return result;
    }

    private ArrayList filterSegments(ArrayList v, List markers) {
        ArrayList result = new ArrayList();
        for (int i = 0; i < v.size(); i++) {
            Segment segment = (Segment) v.get(i);
            if (markers.contains(new Integer(segment.marker))) {
                result.add(segment);
            }
        }
        return result;
    }

    public TiffImageMetadata getExifMetadata(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        byte[] bytes = getExifRawData(byteSource);
        if (bytes == null) {
            return null;
        }
        if (params == null) {
            params = new HashMap();
        }
        if (!params.containsKey(SanselanConstants.PARAM_KEY_READ_THUMBNAILS)) {
            params.put(SanselanConstants.PARAM_KEY_READ_THUMBNAILS, Boolean.TRUE);
        }
        return (TiffImageMetadata) new TiffImageParser().getMetadata(bytes, params);
    }

    public byte[] getExifRawData(ByteSource byteSource) throws ImageReadException, IOException {
        ArrayList segments = readSegments(byteSource, new int[]{65505}, false);
        if (segments == null || segments.size() < 1) {
            return null;
        }
        ArrayList exifSegments = filterAPP1Segments(segments);
        if (this.debug) {
            System.out.println("exif_segments.size: " + exifSegments.size());
        }
        if (exifSegments.size() < 1) {
            return null;
        }
        if (exifSegments.size() <= 1) {
            return getByteArrayTail("trimmed exif bytes", ((GenericSegment) exifSegments.get(0)).bytes, 6);
        }
        throw new ImageReadException("Sanselan currently can't parse EXIF metadata split across multiple APP1 segments.  Please send this image to the Sanselan project.");
    }

    public boolean hasExifSegment(ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] result = {false};
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() {
            /* class dji.thirdparty.sanselan.formats.jpeg.JpegImageParser.AnonymousClass2 */

            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int marker, byte[] markerBytes, byte[] imageData) {
            }

            public boolean visitSOS(int marker, byte[] markerBytes, InputStream is) {
                return false;
            }

            public boolean visitSegment(int marker, byte[] markerBytes, int markerLength, byte[] markerLengthBytes, byte[] segmentData) throws ImageReadException, IOException {
                if (marker == 65497) {
                    return false;
                }
                if (marker != 65505 || !BinaryFileParser.byteArrayHasPrefix(segmentData, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                    return true;
                }
                result[0] = true;
                return false;
            }
        });
        return result[0];
    }

    public boolean hasIptcSegment(ByteSource byteSource) throws ImageReadException, IOException {
        return new boolean[]{false}[0];
    }

    public boolean hasXmpSegment(ByteSource byteSource) throws ImageReadException, IOException {
        return new boolean[]{false}[0];
    }

    public String getXmpXml(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        return null;
    }

    public Object getPhotoshopMetadata(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        return null;
    }

    public int[] getImageSize(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        ArrayList segments = readSegments(byteSource, new int[]{JpegConstants.SOF0Marker, JpegConstants.SOF1Marker, JpegConstants.SOF2Marker, JpegConstants.SOF3Marker, JpegConstants.SOF5Marker, JpegConstants.SOF6Marker, JpegConstants.SOF7Marker, JpegConstants.SOF9Marker, JpegConstants.SOF10Marker, JpegConstants.SOF11Marker, JpegConstants.SOF13Marker, JpegConstants.SOF14Marker, JpegConstants.SOF15Marker}, true);
        if (segments == null || segments.size() < 1) {
            throw new ImageReadException("No JFIF Data Found.");
        } else if (segments.size() > 1) {
            throw new ImageReadException("Redundant JFIF Data Found.");
        } else {
            SOFNSegment fSOFNSegment = (SOFNSegment) segments.get(0);
            return new int[]{fSOFNSegment.width, fSOFNSegment.height};
        }
    }

    public byte[] embedICCProfile(byte[] image, byte[] profile) {
        return null;
    }

    public boolean embedICCProfile(File src, File dst, byte[] profile) {
        return false;
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        String FormatDetails;
        int ColorType;
        ArrayList SOF_segments = readSegments(byteSource, new int[]{JpegConstants.SOF0Marker, JpegConstants.SOF1Marker, JpegConstants.SOF2Marker, JpegConstants.SOF3Marker, JpegConstants.SOF5Marker, JpegConstants.SOF6Marker, JpegConstants.SOF7Marker, JpegConstants.SOF9Marker, JpegConstants.SOF10Marker, JpegConstants.SOF11Marker, JpegConstants.SOF13Marker, JpegConstants.SOF14Marker, JpegConstants.SOF15Marker}, false);
        if (SOF_segments == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        ArrayList jfifSegments = readSegments(byteSource, new int[]{65504}, true);
        SOFNSegment fSOFNSegment = (SOFNSegment) SOF_segments.get(0);
        if (fSOFNSegment == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        int Width = fSOFNSegment.width;
        int Height = fSOFNSegment.height;
        JFIFSegment jfifSegment = null;
        if (jfifSegments != null && jfifSegments.size() > 0) {
            jfifSegment = (JFIFSegment) jfifSegments.get(0);
        }
        double x_density = -1.0d;
        double y_density = -1.0d;
        double units_per_inch = -1.0d;
        if (jfifSegment != null) {
            x_density = (double) jfifSegment.xDensity;
            y_density = (double) jfifSegment.yDensity;
            FormatDetails = "Jpeg/JFIF v." + jfifSegment.jfifMajorVersion + "." + jfifSegment.jfifMinorVersion;
            switch (jfifSegment.densityUnits) {
                case 1:
                    units_per_inch = 1.0d;
                    break;
                case 2:
                    units_per_inch = 2.54d;
                    break;
            }
        } else {
            JpegImageMetadata metadata = (JpegImageMetadata) getMetadata(byteSource, params);
            if (metadata != null) {
                TiffField field = metadata.findEXIFValue(TIFF_TAG_XRESOLUTION);
                if (field != null) {
                    x_density = ((Number) field.getValue()).doubleValue();
                }
                TiffField field2 = metadata.findEXIFValue(TIFF_TAG_YRESOLUTION);
                if (field2 != null) {
                    y_density = ((Number) field2.getValue()).doubleValue();
                }
                TiffField field3 = metadata.findEXIFValue(TIFF_TAG_RESOLUTION_UNIT);
                if (field3 != null) {
                    switch (((Number) field3.getValue()).intValue()) {
                        case 2:
                            units_per_inch = 1.0d;
                            break;
                        case 3:
                            units_per_inch = 2.54d;
                            break;
                    }
                }
            }
            FormatDetails = "Jpeg/DCM";
        }
        int PhysicalHeightDpi = -1;
        float PhysicalHeightInch = -1.0f;
        int PhysicalWidthDpi = -1;
        float PhysicalWidthInch = -1.0f;
        if (units_per_inch > 0.0d) {
            PhysicalWidthDpi = (int) Math.round(x_density / units_per_inch);
            PhysicalWidthInch = (float) (((double) Width) / (x_density * units_per_inch));
            PhysicalHeightDpi = (int) Math.round(y_density * units_per_inch);
            PhysicalHeightInch = (float) (((double) Height) / (y_density * units_per_inch));
        }
        ArrayList Comments = new ArrayList();
        int Number_of_components = fSOFNSegment.numberOfComponents;
        int BitsPerPixel = Number_of_components * fSOFNSegment.precision;
        ImageFormat Format = ImageFormat.IMAGE_FORMAT_JPEG;
        boolean isProgressive = fSOFNSegment.marker == 65474;
        if (Number_of_components == 1) {
            ColorType = 0;
        } else if (Number_of_components == 3) {
            ColorType = 2;
        } else if (Number_of_components == 4) {
            ColorType = 3;
        } else {
            ColorType = -2;
        }
        return new ImageInfo(FormatDetails, BitsPerPixel, Comments, Format, "JPEG (Joint Photographic Experts Group) Format", Height, "image/jpeg", 1, PhysicalHeightDpi, PhysicalHeightInch, PhysicalWidthDpi, PhysicalWidthInch, Width, isProgressive, false, false, ColorType, ImageInfo.COMPRESSION_ALGORITHM_JPEG);
    }

    public boolean dumpImageFile(PrintWriter pw, ByteSource byteSource) throws ImageReadException, IOException {
        pw.println("tiff.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(pw, "");
        pw.println("");
        ArrayList segments = readSegments(byteSource, null, false);
        if (segments == null) {
            throw new ImageReadException("No Segments Found.");
        }
        for (int d = 0; d < segments.size(); d++) {
            Segment segment = (Segment) segments.get(d);
            pw.println(d + ": marker: " + Integer.toHexString(segment.marker) + ", " + segment.getDescription() + " (length: " + NumberFormat.getIntegerInstance().format((long) segment.length) + ")");
            segment.dump(pw);
        }
        pw.println("");
        return true;
    }
}
