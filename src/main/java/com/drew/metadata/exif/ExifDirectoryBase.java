package com.drew.metadata.exif;

import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public abstract class ExifDirectoryBase extends Directory {
    public static final int TAG_35MM_FILM_EQUIV_FOCAL_LENGTH = 41989;
    public static final int TAG_APERTURE = 37378;
    public static final int TAG_APPLICATION_NOTES = 700;
    public static final int TAG_ARTIST = 315;
    public static final int TAG_BATTERY_LEVEL = 33423;
    public static final int TAG_BITS_PER_SAMPLE = 258;
    public static final int TAG_BODY_SERIAL_NUMBER = 42033;
    public static final int TAG_BRIGHTNESS_VALUE = 37379;
    public static final int TAG_CAMERA_OWNER_NAME = 42032;
    public static final int TAG_CFA_PATTERN = 41730;
    public static final int TAG_CFA_PATTERN_2 = 33422;
    public static final int TAG_CFA_REPEAT_PATTERN_DIM = 33421;
    public static final int TAG_COLOR_SPACE = 40961;
    public static final int TAG_COMPONENTS_CONFIGURATION = 37121;
    public static final int TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL = 37122;
    public static final int TAG_COMPRESSION = 259;
    public static final int TAG_CONTRAST = 41992;
    public static final int TAG_COPYRIGHT = 33432;
    public static final int TAG_CUSTOM_RENDERED = 41985;
    public static final int TAG_DATETIME = 306;
    public static final int TAG_DATETIME_DIGITIZED = 36868;
    public static final int TAG_DATETIME_ORIGINAL = 36867;
    public static final int TAG_DEVICE_SETTING_DESCRIPTION = 41995;
    public static final int TAG_DIGITAL_ZOOM_RATIO = 41988;
    public static final int TAG_DOCUMENT_NAME = 269;
    public static final int TAG_EXIF_IMAGE_HEIGHT = 40963;
    public static final int TAG_EXIF_IMAGE_WIDTH = 40962;
    public static final int TAG_EXIF_VERSION = 36864;
    public static final int TAG_EXPOSURE_BIAS = 37380;
    public static final int TAG_EXPOSURE_INDEX = 41493;
    public static final int TAG_EXPOSURE_INDEX_TIFF_EP = 37397;
    public static final int TAG_EXPOSURE_MODE = 41986;
    public static final int TAG_EXPOSURE_PROGRAM = 34850;
    public static final int TAG_EXPOSURE_TIME = 33434;
    public static final int TAG_FILE_SOURCE = 41728;
    public static final int TAG_FILL_ORDER = 266;
    public static final int TAG_FLASH = 37385;
    public static final int TAG_FLASHPIX_VERSION = 40960;
    public static final int TAG_FLASH_ENERGY = 41483;
    public static final int TAG_FLASH_ENERGY_TIFF_EP = 37387;
    public static final int TAG_FNUMBER = 33437;
    public static final int TAG_FOCAL_LENGTH = 37386;
    public static final int TAG_FOCAL_PLANE_RESOLUTION_UNIT = 41488;
    public static final int TAG_FOCAL_PLANE_X_RESOLUTION = 41486;
    public static final int TAG_FOCAL_PLANE_X_RESOLUTION_TIFF_EP = 37390;
    public static final int TAG_FOCAL_PLANE_Y_RESOLUTION = 41487;
    public static final int TAG_FOCAL_PLANE_Y_RESOLUTION_TIFF_EP = 37391;
    public static final int TAG_GAIN_CONTROL = 41991;
    public static final int TAG_GAMMA = 42240;
    public static final int TAG_HOST_COMPUTER = 316;
    public static final int TAG_IMAGE_DESCRIPTION = 270;
    public static final int TAG_IMAGE_HEIGHT = 257;
    public static final int TAG_IMAGE_HISTORY = 37395;
    public static final int TAG_IMAGE_NUMBER = 37393;
    public static final int TAG_IMAGE_UNIQUE_ID = 42016;
    public static final int TAG_IMAGE_WIDTH = 256;
    public static final int TAG_INTERLACE = 34857;
    public static final int TAG_INTEROP_INDEX = 1;
    public static final int TAG_INTEROP_VERSION = 2;
    public static final int TAG_INTER_COLOR_PROFILE = 34675;
    public static final int TAG_IPTC_NAA = 33723;
    public static final int TAG_ISO_EQUIVALENT = 34855;
    public static final int TAG_JPEG_AC_TABLES = 521;
    public static final int TAG_JPEG_DC_TABLES = 520;
    public static final int TAG_JPEG_LOSSLESS_PREDICTORS = 517;
    public static final int TAG_JPEG_POINT_TRANSFORMS = 518;
    public static final int TAG_JPEG_PROC = 512;
    public static final int TAG_JPEG_Q_TABLES = 519;
    public static final int TAG_JPEG_RESTART_INTERVAL = 515;
    public static final int TAG_JPEG_TABLES = 347;
    public static final int TAG_LENS = 65002;
    public static final int TAG_LENS_MAKE = 42035;
    public static final int TAG_LENS_MODEL = 42036;
    public static final int TAG_LENS_SERIAL_NUMBER = 42037;
    public static final int TAG_LENS_SPECIFICATION = 42034;
    @Deprecated
    public static final int TAG_LIGHT_SOURCE = 37384;
    public static final int TAG_MAKE = 271;
    public static final int TAG_MAKERNOTE = 37500;
    public static final int TAG_MAX_APERTURE = 37381;
    public static final int TAG_MAX_SAMPLE_VALUE = 281;
    public static final int TAG_METERING_MODE = 37383;
    public static final int TAG_MIN_SAMPLE_VALUE = 280;
    public static final int TAG_MODEL = 272;
    public static final int TAG_NEW_SUBFILE_TYPE = 254;
    public static final int TAG_NOISE = 37389;
    public static final int TAG_OPTO_ELECTRIC_CONVERSION_FUNCTION = 34856;
    public static final int TAG_ORIENTATION = 274;
    public static final int TAG_PADDING = 59932;
    public static final int TAG_PAGE_NAME = 285;
    public static final int TAG_PAGE_NUMBER = 297;
    public static final int TAG_PANASONIC_TITLE = 50898;
    public static final int TAG_PANASONIC_TITLE_2 = 50899;
    public static final int TAG_PHOTOMETRIC_INTERPRETATION = 262;
    public static final int TAG_PLANAR_CONFIGURATION = 284;
    public static final int TAG_PREDICTOR = 317;
    public static final int TAG_PRIMARY_CHROMATICITIES = 319;
    public static final int TAG_PRINT_IMAGE_MATCHING_INFO = 50341;
    public static final int TAG_RATING = 18246;
    public static final int TAG_RECOMMENDED_EXPOSURE_INDEX = 34866;
    public static final int TAG_REFERENCE_BLACK_WHITE = 532;
    public static final int TAG_RELATED_IMAGE_FILE_FORMAT = 4096;
    public static final int TAG_RELATED_IMAGE_HEIGHT = 4098;
    public static final int TAG_RELATED_IMAGE_WIDTH = 4097;
    public static final int TAG_RELATED_SOUND_FILE = 40964;
    public static final int TAG_RESOLUTION_UNIT = 296;
    public static final int TAG_ROWS_PER_STRIP = 278;
    public static final int TAG_SAMPLES_PER_PIXEL = 277;
    public static final int TAG_SATURATION = 41993;
    public static final int TAG_SCENE_CAPTURE_TYPE = 41990;
    public static final int TAG_SCENE_TYPE = 41729;
    public static final int TAG_SECURITY_CLASSIFICATION = 37394;
    public static final int TAG_SELF_TIMER_MODE = 34859;
    public static final int TAG_SELF_TIMER_MODE_TIFF_EP = 34859;
    public static final int TAG_SENSING_METHOD = 41495;
    public static final int TAG_SENSITIVITY_TYPE = 34864;
    public static final int TAG_SHARPNESS = 41994;
    public static final int TAG_SHUTTER_SPEED = 37377;
    public static final int TAG_SOFTWARE = 305;
    public static final int TAG_SPATIAL_FREQ_RESPONSE = 41484;
    public static final int TAG_SPATIAL_FREQ_RESPONSE_TIFF_EP = 37388;
    public static final int TAG_SPECTRAL_SENSITIVITY = 34852;
    public static final int TAG_STANDARD_ID_TIFF_EP = 37398;
    public static final int TAG_STANDARD_OUTPUT_SENSITIVITY = 34865;
    public static final int TAG_STRIP_BYTE_COUNTS = 279;
    public static final int TAG_STRIP_OFFSETS = 273;
    public static final int TAG_STRIP_ROW_COUNTS = 559;
    public static final int TAG_SUBFILE_TYPE = 255;
    public static final int TAG_SUBJECT_DISTANCE = 37382;
    public static final int TAG_SUBJECT_DISTANCE_RANGE = 41996;
    public static final int TAG_SUBJECT_LOCATION = 41492;
    public static final int TAG_SUBJECT_LOCATION_TIFF_EP = 37396;
    public static final int TAG_SUBSECOND_TIME = 37520;
    public static final int TAG_SUBSECOND_TIME_DIGITIZED = 37522;
    public static final int TAG_SUBSECOND_TIME_ORIGINAL = 37521;
    public static final int TAG_SUB_IFD_OFFSET = 330;
    public static final int TAG_THRESHOLDING = 263;
    public static final int TAG_TILE_BYTE_COUNTS = 325;
    public static final int TAG_TILE_LENGTH = 323;
    public static final int TAG_TILE_OFFSETS = 324;
    public static final int TAG_TILE_WIDTH = 322;
    public static final int TAG_TIME_ZONE_OFFSET = 34858;
    public static final int TAG_TIME_ZONE_OFFSET_TIFF_EP = 34858;
    public static final int TAG_TRANSFER_FUNCTION = 301;
    public static final int TAG_TRANSFER_RANGE = 342;
    public static final int TAG_USER_COMMENT = 37510;
    public static final int TAG_WHITE_BALANCE = 37384;
    public static final int TAG_WHITE_BALANCE_MODE = 41987;
    public static final int TAG_WHITE_POINT = 318;
    public static final int TAG_WIN_AUTHOR = 40093;
    public static final int TAG_WIN_COMMENT = 40092;
    public static final int TAG_WIN_KEYWORDS = 40094;
    public static final int TAG_WIN_SUBJECT = 40095;
    public static final int TAG_WIN_TITLE = 40091;
    public static final int TAG_X_RESOLUTION = 282;
    public static final int TAG_YCBCR_COEFFICIENTS = 529;
    public static final int TAG_YCBCR_POSITIONING = 531;
    public static final int TAG_YCBCR_SUBSAMPLING = 530;
    public static final int TAG_Y_RESOLUTION = 283;

    protected static void addExifTagNames(HashMap<Integer, String> map) {
        map.put(1, "Interoperability Index");
        map.put(2, "Interoperability Version");
        map.put(254, "New Subfile Type");
        map.put(255, "Subfile Type");
        map.put(256, "Image Width");
        map.put(257, "Image Height");
        map.put(258, "Bits Per Sample");
        map.put(259, "Compression");
        map.put(262, "Photometric Interpretation");
        map.put(263, "Thresholding");
        map.put(266, "Fill Order");
        map.put(269, "Document Name");
        map.put(270, "Image Description");
        map.put(271, "Make");
        map.put(272, "Model");
        map.put(273, "Strip Offsets");
        map.put(274, CameraKeys.ORIENTATION);
        map.put(277, "Samples Per Pixel");
        map.put(278, "Rows Per Strip");
        map.put(279, "Strip Byte Counts");
        map.put(280, "Minimum Sample Value");
        map.put(281, "Maximum Sample Value");
        map.put(282, "X Resolution");
        map.put(283, "Y Resolution");
        map.put(284, "Planar Configuration");
        map.put(285, "Page Name");
        map.put(296, "Resolution Unit");
        map.put(Integer.valueOf((int) TAG_PAGE_NUMBER), "Page Number");
        map.put(Integer.valueOf((int) TAG_TRANSFER_FUNCTION), "Transfer Function");
        map.put(305, "Software");
        map.put(306, "Date/Time");
        map.put(315, "Artist");
        map.put(Integer.valueOf((int) TAG_PREDICTOR), "Predictor");
        map.put(316, "Host Computer");
        map.put(Integer.valueOf((int) TAG_WHITE_POINT), "White Point");
        map.put(Integer.valueOf((int) TAG_PRIMARY_CHROMATICITIES), "Primary Chromaticities");
        map.put(Integer.valueOf((int) TAG_TILE_WIDTH), "Tile Width");
        map.put(Integer.valueOf((int) TAG_TILE_LENGTH), "Tile Length");
        map.put(Integer.valueOf((int) TAG_TILE_OFFSETS), "Tile Offsets");
        map.put(Integer.valueOf((int) TAG_TILE_BYTE_COUNTS), "Tile Byte Counts");
        map.put(Integer.valueOf((int) TAG_SUB_IFD_OFFSET), "Sub IFD Pointer(s)");
        map.put(Integer.valueOf((int) TAG_TRANSFER_RANGE), "Transfer Range");
        map.put(Integer.valueOf((int) TAG_JPEG_TABLES), "JPEG Tables");
        map.put(512, "JPEG Proc");
        map.put(515, "JPEG Restart Interval");
        map.put(517, "JPEG Lossless Predictors");
        map.put(518, "JPEG Point Transforms");
        map.put(519, "JPEGQ Tables");
        map.put(520, "JPEGDC Tables");
        map.put(521, "JPEGAC Tables");
        map.put(529, "YCbCr Coefficients");
        map.put(530, "YCbCr Sub-Sampling");
        map.put(531, "YCbCr Positioning");
        map.put(532, "Reference Black/White");
        map.put(559, "Strip Row Counts");
        map.put(700, "Application Notes");
        map.put(4096, "Related Image File Format");
        map.put(4097, "Related Image Width");
        map.put(4098, "Related Image Height");
        map.put(Integer.valueOf((int) TAG_RATING), "Rating");
        map.put(Integer.valueOf((int) TAG_CFA_REPEAT_PATTERN_DIM), "CFA Repeat Pattern Dim");
        map.put(Integer.valueOf((int) TAG_CFA_PATTERN_2), "CFA Pattern");
        map.put(Integer.valueOf((int) TAG_BATTERY_LEVEL), "Battery Level");
        map.put(Integer.valueOf((int) TAG_COPYRIGHT), "Copyright");
        map.put(Integer.valueOf((int) TAG_EXPOSURE_TIME), "Exposure Time");
        map.put(Integer.valueOf((int) TAG_FNUMBER), "F-Number");
        map.put(Integer.valueOf((int) TAG_IPTC_NAA), "IPTC/NAA");
        map.put(Integer.valueOf((int) TAG_INTER_COLOR_PROFILE), "Inter Color Profile");
        map.put(Integer.valueOf((int) TAG_EXPOSURE_PROGRAM), "Exposure Program");
        map.put(Integer.valueOf((int) TAG_SPECTRAL_SENSITIVITY), "Spectral Sensitivity");
        map.put(Integer.valueOf((int) TAG_ISO_EQUIVALENT), "ISO Speed Ratings");
        map.put(Integer.valueOf((int) TAG_OPTO_ELECTRIC_CONVERSION_FUNCTION), "Opto-electric Conversion Function (OECF)");
        map.put(Integer.valueOf((int) TAG_INTERLACE), "Interlace");
        map.put(34858, "Time Zone Offset");
        map.put(34859, "Self Timer Mode");
        map.put(Integer.valueOf((int) TAG_SENSITIVITY_TYPE), "Sensitivity Type");
        map.put(Integer.valueOf((int) TAG_STANDARD_OUTPUT_SENSITIVITY), "Standard Output Sensitivity");
        map.put(Integer.valueOf((int) TAG_RECOMMENDED_EXPOSURE_INDEX), "Recommended Exposure Index");
        map.put(34858, "Time Zone Offset");
        map.put(34859, "Self Timer Mode");
        map.put(Integer.valueOf((int) TAG_EXIF_VERSION), "Exif Version");
        map.put(Integer.valueOf((int) TAG_DATETIME_ORIGINAL), "Date/Time Original");
        map.put(Integer.valueOf((int) TAG_DATETIME_DIGITIZED), "Date/Time Digitized");
        map.put(Integer.valueOf((int) TAG_COMPONENTS_CONFIGURATION), "Components Configuration");
        map.put(Integer.valueOf((int) TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL), "Compressed Bits Per Pixel");
        map.put(Integer.valueOf((int) TAG_SHUTTER_SPEED), "Shutter Speed Value");
        map.put(Integer.valueOf((int) TAG_APERTURE), "Aperture Value");
        map.put(Integer.valueOf((int) TAG_BRIGHTNESS_VALUE), "Brightness Value");
        map.put(Integer.valueOf((int) TAG_EXPOSURE_BIAS), "Exposure Bias Value");
        map.put(Integer.valueOf((int) TAG_MAX_APERTURE), "Max Aperture Value");
        map.put(Integer.valueOf((int) TAG_SUBJECT_DISTANCE), "Subject Distance");
        map.put(Integer.valueOf((int) TAG_METERING_MODE), "Metering Mode");
        map.put(37384, "White Balance");
        map.put(Integer.valueOf((int) TAG_FLASH), "Flash");
        map.put(Integer.valueOf((int) TAG_FOCAL_LENGTH), "Focal Length");
        map.put(Integer.valueOf((int) TAG_FLASH_ENERGY_TIFF_EP), "Flash Energy");
        map.put(Integer.valueOf((int) TAG_SPATIAL_FREQ_RESPONSE_TIFF_EP), "Spatial Frequency Response");
        map.put(Integer.valueOf((int) TAG_NOISE), "Noise");
        map.put(Integer.valueOf((int) TAG_FOCAL_PLANE_X_RESOLUTION_TIFF_EP), "Focal Plane X Resolution");
        map.put(Integer.valueOf((int) TAG_FOCAL_PLANE_Y_RESOLUTION_TIFF_EP), "Focal Plane Y Resolution");
        map.put(Integer.valueOf((int) TAG_IMAGE_NUMBER), "Image Number");
        map.put(Integer.valueOf((int) TAG_SECURITY_CLASSIFICATION), "Security Classification");
        map.put(Integer.valueOf((int) TAG_IMAGE_HISTORY), "Image History");
        map.put(Integer.valueOf((int) TAG_SUBJECT_LOCATION_TIFF_EP), "Subject Location");
        map.put(Integer.valueOf((int) TAG_EXPOSURE_INDEX_TIFF_EP), "Exposure Index");
        map.put(Integer.valueOf((int) TAG_STANDARD_ID_TIFF_EP), "TIFF/EP Standard ID");
        map.put(Integer.valueOf((int) TAG_MAKERNOTE), "Makernote");
        map.put(Integer.valueOf((int) TAG_USER_COMMENT), "User Comment");
        map.put(Integer.valueOf((int) TAG_SUBSECOND_TIME), "Sub-Sec Time");
        map.put(Integer.valueOf((int) TAG_SUBSECOND_TIME_ORIGINAL), "Sub-Sec Time Original");
        map.put(Integer.valueOf((int) TAG_SUBSECOND_TIME_DIGITIZED), "Sub-Sec Time Digitized");
        map.put(Integer.valueOf((int) TAG_WIN_TITLE), "Windows XP Title");
        map.put(Integer.valueOf((int) TAG_WIN_COMMENT), "Windows XP Comment");
        map.put(Integer.valueOf((int) TAG_WIN_AUTHOR), "Windows XP Author");
        map.put(Integer.valueOf((int) TAG_WIN_KEYWORDS), "Windows XP Keywords");
        map.put(Integer.valueOf((int) TAG_WIN_SUBJECT), "Windows XP Subject");
        map.put(Integer.valueOf((int) TAG_FLASHPIX_VERSION), "FlashPix Version");
        map.put(40961, "Color Space");
        map.put(Integer.valueOf((int) TAG_EXIF_IMAGE_WIDTH), "Exif Image Width");
        map.put(Integer.valueOf((int) TAG_EXIF_IMAGE_HEIGHT), "Exif Image Height");
        map.put(Integer.valueOf((int) TAG_RELATED_SOUND_FILE), "Related Sound File");
        map.put(Integer.valueOf((int) TAG_FLASH_ENERGY), "Flash Energy");
        map.put(Integer.valueOf((int) TAG_SPATIAL_FREQ_RESPONSE), "Spatial Frequency Response");
        map.put(Integer.valueOf((int) TAG_FOCAL_PLANE_X_RESOLUTION), "Focal Plane X Resolution");
        map.put(Integer.valueOf((int) TAG_FOCAL_PLANE_Y_RESOLUTION), "Focal Plane Y Resolution");
        map.put(Integer.valueOf((int) TAG_FOCAL_PLANE_RESOLUTION_UNIT), "Focal Plane Resolution Unit");
        map.put(Integer.valueOf((int) TAG_SUBJECT_LOCATION), "Subject Location");
        map.put(Integer.valueOf((int) TAG_EXPOSURE_INDEX), "Exposure Index");
        map.put(Integer.valueOf((int) TAG_SENSING_METHOD), "Sensing Method");
        map.put(Integer.valueOf((int) TAG_FILE_SOURCE), "File Source");
        map.put(Integer.valueOf((int) TAG_SCENE_TYPE), "Scene Type");
        map.put(Integer.valueOf((int) TAG_CFA_PATTERN), "CFA Pattern");
        map.put(Integer.valueOf((int) TAG_CUSTOM_RENDERED), "Custom Rendered");
        map.put(Integer.valueOf((int) TAG_EXPOSURE_MODE), "Exposure Mode");
        map.put(Integer.valueOf((int) TAG_WHITE_BALANCE_MODE), "White Balance Mode");
        map.put(Integer.valueOf((int) TAG_DIGITAL_ZOOM_RATIO), "Digital Zoom Ratio");
        map.put(Integer.valueOf((int) TAG_35MM_FILM_EQUIV_FOCAL_LENGTH), "Focal Length 35");
        map.put(Integer.valueOf((int) TAG_SCENE_CAPTURE_TYPE), "Scene Capture Type");
        map.put(Integer.valueOf((int) TAG_GAIN_CONTROL), "Gain Control");
        map.put(Integer.valueOf((int) TAG_CONTRAST), CameraKeys.CONTRAST);
        map.put(Integer.valueOf((int) TAG_SATURATION), CameraKeys.SATURATION);
        map.put(Integer.valueOf((int) TAG_SHARPNESS), CameraKeys.SHARPNESS);
        map.put(Integer.valueOf((int) TAG_DEVICE_SETTING_DESCRIPTION), "Device Setting Description");
        map.put(Integer.valueOf((int) TAG_SUBJECT_DISTANCE_RANGE), "Subject Distance Range");
        map.put(Integer.valueOf((int) TAG_IMAGE_UNIQUE_ID), "Unique Image ID");
        map.put(Integer.valueOf((int) TAG_CAMERA_OWNER_NAME), "Camera Owner Name");
        map.put(Integer.valueOf((int) TAG_BODY_SERIAL_NUMBER), "Body Serial Number");
        map.put(Integer.valueOf((int) TAG_LENS_SPECIFICATION), "Lens Specification");
        map.put(Integer.valueOf((int) TAG_LENS_MAKE), "Lens Make");
        map.put(Integer.valueOf((int) TAG_LENS_MODEL), "Lens Model");
        map.put(Integer.valueOf((int) TAG_LENS_SERIAL_NUMBER), "Lens Serial Number");
        map.put(Integer.valueOf((int) TAG_GAMMA), "Gamma");
        map.put(Integer.valueOf((int) TAG_PRINT_IMAGE_MATCHING_INFO), "Print Image Matching (PIM) Info");
        map.put(Integer.valueOf((int) TAG_PANASONIC_TITLE), "Panasonic Title");
        map.put(Integer.valueOf((int) TAG_PANASONIC_TITLE_2), "Panasonic Title (2)");
        map.put(Integer.valueOf((int) TAG_PADDING), "Padding");
        map.put(Integer.valueOf((int) TAG_LENS), "Lens");
    }
}