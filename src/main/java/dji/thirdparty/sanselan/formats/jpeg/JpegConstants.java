package dji.thirdparty.sanselan.formats.jpeg;

import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public interface JpegConstants {
    public static final byte[] CONST_8BIM = {56, 66, 73, 77};
    public static final byte[] EOI = {-1, MessagePack.Code.STR8};
    public static final byte[] EXIF_IDENTIFIER_CODE = {69, 120, 105, 102};
    public static final byte[] JFIF0_SIGNATURE = {74, 70, 73, 70, 0};
    public static final byte[] JFIF0_SIGNATURE_ALTERNATIVE = {74, 70, 73, 70, 32};
    public static final int JFIFMarker = 65504;
    public static final int JPEG_APP0 = 224;
    public static final int JPEG_APP0_Marker = 65504;
    public static final int JPEG_APP13_Marker = 65517;
    public static final int JPEG_APP14_Marker = 65518;
    public static final int JPEG_APP15_Marker = 65519;
    public static final int JPEG_APP1_Marker = 65505;
    public static final int JPEG_APP2_Marker = 65506;
    public static final int[] MARKERS = {SOS_Marker, 224, 65504, JPEG_APP1_Marker, JPEG_APP2_Marker, JPEG_APP13_Marker, JPEG_APP14_Marker, JPEG_APP15_Marker, 65504, SOF0Marker, SOF1Marker, SOF2Marker, SOF3Marker, SOF4Marker, SOF5Marker, SOF6Marker, SOF7Marker, SOF8Marker, SOF9Marker, SOF10Marker, SOF11Marker, SOF12Marker, SOF13Marker, SOF14Marker, SOF15Marker};
    public static final int MAX_SEGMENT_SIZE = 65535;
    public static final byte[] PHOTOSHOP_IDENTIFICATION_STRING = {80, 104, 111, 116, 111, 115, 104, 111, 112, 32, TarHeader.LF_CHR, 46, TarHeader.LF_NORMAL, 0};
    public static final int SOF0Marker = 65472;
    public static final int SOF10Marker = 65482;
    public static final int SOF11Marker = 65483;
    public static final int SOF12Marker = 65484;
    public static final int SOF13Marker = 65485;
    public static final int SOF14Marker = 65486;
    public static final int SOF15Marker = 65487;
    public static final int SOF1Marker = 65473;
    public static final int SOF2Marker = 65474;
    public static final int SOF3Marker = 65475;
    public static final int SOF4Marker = 65476;
    public static final int SOF5Marker = 65477;
    public static final int SOF6Marker = 65478;
    public static final int SOF7Marker = 65479;
    public static final int SOF8Marker = 65480;
    public static final int SOF9Marker = 65481;
    public static final byte[] SOI = {-1, MessagePack.Code.FIXEXT16};
    public static final int SOS_Marker = 65498;
    public static final byte[] XMP_IDENTIFIER = {104, 116, 116, 112, 58, 47, 47, 110, 115, 46, 97, 100, 111, 98, 101, 46, 99, 111, 109, 47, 120, 97, 112, 47, TarHeader.LF_LINK, 46, TarHeader.LF_NORMAL, 47, 0};
    public static final byte[] icc_profile_label = {73, 67, 67, 95, 80, 82, 79, 70, 73, 76, 69, 0};
}
