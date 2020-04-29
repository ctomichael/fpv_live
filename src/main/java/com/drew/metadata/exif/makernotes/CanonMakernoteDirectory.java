package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class CanonMakernoteDirectory extends Directory {
    private static final int TAG_AF_INFO_ARRAY = 18;
    public static final int TAG_AF_INFO_ARRAY_2 = 38;
    public static final int TAG_AF_POINTS_IN_FOCUS_1D = 148;
    public static final int TAG_AMBIANCE_INFO_ARRAY = 16416;
    public static final int TAG_ASPECT_INFO_ARRAY = 154;
    public static final int TAG_BLACK_LEVEL = 16392;
    public static final int TAG_CAMERA_INFO_ARRAY = 13;
    private static final int TAG_CAMERA_SETTINGS_ARRAY = 1;
    public static final int TAG_CANON_CUSTOM_FUNCTIONS_ARRAY = 15;
    public static final int TAG_CANON_FILE_LENGTH = 14;
    public static final int TAG_CANON_FIRMWARE_VERSION = 7;
    public static final int TAG_CANON_FLAGS_ARRAY = 176;
    public static final int TAG_CANON_IMAGE_NUMBER = 8;
    public static final int TAG_CANON_IMAGE_TYPE = 6;
    public static final int TAG_CANON_OWNER_NAME = 9;
    public static final int TAG_CANON_SERIAL_NUMBER = 12;
    public static final int TAG_CATEGORIES = 35;
    public static final int TAG_COLOR_BALANCE_ARRAY = 169;
    public static final int TAG_COLOR_DATA_ARRAY_2 = 16385;
    public static final int TAG_COLOR_INFO_ARRAY = 16403;
    public static final int TAG_COLOR_INFO_ARRAY_2 = 16387;
    public static final int TAG_COLOR_SPACE = 180;
    public static final int TAG_COLOR_TEMPERATURE = 174;
    public static final int TAG_CROP_INFO = 152;
    public static final int TAG_CRW_PARAM = 16386;
    public static final int TAG_CUSTOM_FUNCTIONS_1D_ARRAY = 144;
    public static final int TAG_CUSTOM_FUNCTIONS_ARRAY_2 = 153;
    public static final int TAG_CUSTOM_PICTURE_STYLE_FILE_NAME = 16400;
    public static final int TAG_DATE_STAMP_MODE = 28;
    public static final int TAG_DUST_REMOVAL_DATA = 151;
    public static final int TAG_FACE_DETECT_ARRAY_1 = 36;
    public static final int TAG_FACE_DETECT_ARRAY_2 = 37;
    public static final int TAG_FILE_INFO_ARRAY = 147;
    public static final int TAG_FILTER_INFO_ARRAY = 16420;
    public static final int TAG_FIRMWARE_REVISION = 30;
    private static final int TAG_FOCAL_LENGTH_ARRAY = 2;
    public static final int TAG_IMAGE_UNIQUE_ID = 40;
    public static final int TAG_LENS_INFO_ARRAY = 16409;
    public static final int TAG_LENS_MODEL = 149;
    public static final int TAG_LIGHTING_OPTIMIZER_ARRAY = 16408;
    public static final int TAG_MEASURED_COLOR_ARRAY = 170;
    public static final int TAG_MODEL_ID = 16;
    public static final int TAG_MODIFIED_INFO_ARRAY = 177;
    public static final int TAG_MOVIE_INFO_ARRAY = 17;
    public static final int TAG_MY_COLORS = 29;
    public static final int TAG_ORIGINAL_DECISION_DATA_OFFSET = 131;
    private static final int TAG_PANORAMA_ARRAY = 5;
    public static final int TAG_PERSONAL_FUNCTIONS_ARRAY = 145;
    public static final int TAG_PERSONAL_FUNCTION_VALUES_ARRAY = 146;
    public static final int TAG_PREVIEW_IMAGE_INFO_ARRAY = 182;
    public static final int TAG_PROCESSING_INFO_ARRAY = 160;
    public static final int TAG_RAW_DATA_OFFSET = 129;
    public static final int TAG_SENSOR_INFO_ARRAY = 224;
    public static final int TAG_SERIAL_INFO_ARRAY = 150;
    public static final int TAG_SERIAL_NUMBER_FORMAT = 21;
    public static final int TAG_SHARPNESS_FREQ_TABLE = 163;
    public static final int TAG_SHARPNESS_TABLE = 162;
    private static final int TAG_SHOT_INFO_ARRAY = 4;
    public static final int TAG_SUPER_MACRO = 26;
    public static final int TAG_THUMBNAIL_IMAGE_VALID_AREA = 19;
    public static final int TAG_TONE_CURVE_MATCHING = 178;
    public static final int TAG_TONE_CURVE_TABLE = 161;
    public static final int TAG_VIGNETTING_CORRECTION_ARRAY_1 = 16405;
    public static final int TAG_VIGNETTING_CORRECTION_ARRAY_2 = 16406;
    public static final int TAG_VRD_OFFSET = 208;
    public static final int TAG_WHITE_BALANCE_MATCHING = 179;
    public static final int TAG_WHITE_BALANCE_TABLE = 164;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    public static final class AFInfo {
        private static final int OFFSET = 53760;
        public static final int TAG_AF_AREA_HEIGHT = 53767;
        public static final int TAG_AF_AREA_WIDTH = 53766;
        public static final int TAG_AF_AREA_X_POSITIONS = 53768;
        public static final int TAG_AF_AREA_Y_POSITIONS = 53769;
        public static final int TAG_AF_IMAGE_HEIGHT = 53765;
        public static final int TAG_AF_IMAGE_WIDTH = 53764;
        public static final int TAG_AF_POINTS_IN_FOCUS = 53770;
        public static final int TAG_IMAGE_HEIGHT = 53763;
        public static final int TAG_IMAGE_WIDTH = 53762;
        public static final int TAG_NUM_AF_POINTS = 53760;
        public static final int TAG_PRIMARY_AF_POINT_1 = 53771;
        public static final int TAG_PRIMARY_AF_POINT_2 = 53772;
        public static final int TAG_VALID_AF_POINTS = 53761;
    }

    public static final class CameraSettings {
        private static final int OFFSET = 49408;
        public static final int TAG_AE_SETTING = 49439;
        public static final int TAG_AF_POINT_SELECTED = 49427;
        public static final int TAG_COLOR_TONE = 49449;
        public static final int TAG_CONTINUOUS_DRIVE_MODE = 49413;
        public static final int TAG_CONTRAST = 49421;
        public static final int TAG_DIGITAL_ZOOM = 49420;
        public static final int TAG_DISPLAY_APERTURE = 49441;
        public static final int TAG_EASY_SHOOTING_MODE = 49419;
        public static final int TAG_EXPOSURE_MODE = 49428;
        public static final int TAG_FLASH_ACTIVITY = 49436;
        public static final int TAG_FLASH_DETAILS = 49437;
        public static final int TAG_FLASH_MODE = 49412;
        public static final int TAG_FOCAL_UNITS_PER_MM = 49433;
        public static final int TAG_FOCUS_CONTINUOUS = 49438;
        public static final int TAG_FOCUS_MODE_1 = 49415;
        public static final int TAG_FOCUS_MODE_2 = 49440;
        public static final int TAG_FOCUS_TYPE = 49426;
        public static final int TAG_IMAGE_SIZE = 49418;
        public static final int TAG_ISO = 49424;
        public static final int TAG_LENS_TYPE = 49430;
        public static final int TAG_LONG_FOCAL_LENGTH = 49431;
        public static final int TAG_MACRO_MODE = 49409;
        public static final int TAG_MANUAL_FLASH_OUTPUT = 49447;
        public static final int TAG_MAX_APERTURE = 49434;
        public static final int TAG_METERING_MODE = 49425;
        public static final int TAG_MIN_APERTURE = 49435;
        public static final int TAG_PHOTO_EFFECT = 49446;
        public static final int TAG_QUALITY = 49411;
        public static final int TAG_RECORD_MODE = 49417;
        public static final int TAG_SATURATION = 49422;
        public static final int TAG_SELF_TIMER_DELAY = 49410;
        public static final int TAG_SHARPNESS = 49423;
        public static final int TAG_SHORT_FOCAL_LENGTH = 49432;
        public static final int TAG_SPOT_METERING_MODE = 49445;
        public static final int TAG_SRAW_QUALITY = 49453;
        public static final int TAG_UNKNOWN_2 = 49414;
        public static final int TAG_UNKNOWN_3 = 49416;
        public static final int TAG_UNKNOWN_7 = 49429;
        public static final int TAG_ZOOM_SOURCE_WIDTH = 49442;
        public static final int TAG_ZOOM_TARGET_WIDTH = 49443;
    }

    public static final class FocalLength {
        private static final int OFFSET = 49664;
        public static final int TAG_AEB_BRACKET_VALUE = 49681;
        public static final int TAG_AF_POINT_USED = 49678;
        public static final int TAG_AUTO_EXPOSURE_BRACKETING = 49680;
        public static final int TAG_FLASH_BIAS = 49679;
        public static final int TAG_SEQUENCE_NUMBER = 49673;
        public static final int TAG_SUBJECT_DISTANCE = 49683;
        public static final int TAG_WHITE_BALANCE = 49671;
    }

    public static final class Panorama {
        private static final int OFFSET = 50432;
        public static final int TAG_PANORAMA_DIRECTION = 50437;
        public static final int TAG_PANORAMA_FRAME_NUMBER = 50434;
    }

    public static final class ShotInfo {
        private static final int OFFSET = 50176;
        public static final int TAG_AEB_BRACKET_VALUE = 50193;
        public static final int TAG_AF_POINTS_IN_FOCUS = 50190;
        public static final int TAG_AUTO_EXPOSURE_BRACKETING = 50192;
        public static final int TAG_AUTO_ISO = 50177;
        public static final int TAG_AUTO_ROTATE = 50203;
        public static final int TAG_BASE_ISO = 50178;
        public static final int TAG_BULB_DURATION = 50200;
        public static final int TAG_CAMERA_TEMPERATURE = 50188;
        public static final int TAG_CAMERA_TYPE = 50202;
        public static final int TAG_CONTROL_MODE = 50194;
        public static final int TAG_EXPOSURE_COMPENSATION = 50182;
        public static final int TAG_EXPOSURE_TIME = 50198;
        public static final int TAG_FLASH_EXPOSURE_BRACKETING = 50191;
        public static final int TAG_FLASH_GUIDE_NUMBER = 50189;
        public static final int TAG_FLASH_OUTPUT = 50209;
        public static final int TAG_FOCUS_DISTANCE_LOWER = 50196;
        public static final int TAG_FOCUS_DISTANCE_UPPER = 50195;
        public static final int TAG_F_NUMBER = 50197;
        public static final int TAG_MEASURED_EV = 50179;
        public static final int TAG_MEASURED_EV_2 = 50199;
        public static final int TAG_ND_FILTER = 50204;
        public static final int TAG_OPTICAL_ZOOM_CODE = 50186;
        public static final int TAG_SELF_TIMER_2 = 50205;
        public static final int TAG_SEQUENCE_NUMBER = 50185;
        public static final int TAG_SLOW_SHUTTER = 50184;
        public static final int TAG_TARGET_APERTURE = 50180;
        public static final int TAG_TARGET_EXPOSURE_TIME = 50181;
        public static final int TAG_WHITE_BALANCE = 50183;
    }

    static {
        _tagNameMap.put(7, "Firmware Version");
        _tagNameMap.put(8, "Image Number");
        _tagNameMap.put(6, "Image Type");
        _tagNameMap.put(9, "Owner Name");
        _tagNameMap.put(12, "Camera Serial Number");
        _tagNameMap.put(13, "Camera Info Array");
        _tagNameMap.put(14, "File Length");
        _tagNameMap.put(15, "Custom Functions");
        _tagNameMap.put(16, "Canon Model ID");
        _tagNameMap.put(17, "Movie Info Array");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_AF_POINT_SELECTED), "AF Point Selected");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_CONTINUOUS_DRIVE_MODE), "Continuous Drive Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_CONTRAST), CameraKeys.CONTRAST);
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_EASY_SHOOTING_MODE), "Easy Shooting Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_EXPOSURE_MODE), "Exposure Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FLASH_DETAILS), "Flash Details");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FLASH_MODE), "Flash Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FOCAL_UNITS_PER_MM), "Focal Units per mm");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FOCUS_MODE_1), "Focus Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FOCUS_MODE_2), "Focus Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_IMAGE_SIZE), "Image Size");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_ISO), "Iso");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_LONG_FOCAL_LENGTH), "Long Focal Length");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_MACRO_MODE), "Macro Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_METERING_MODE), "Metering Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_SATURATION), CameraKeys.SATURATION);
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_SELF_TIMER_DELAY), "Self Timer Delay");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_SHARPNESS), CameraKeys.SHARPNESS);
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_SHORT_FOCAL_LENGTH), "Short Focal Length");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_QUALITY), "Quality");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_UNKNOWN_2), "Unknown Camera Setting 2");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_UNKNOWN_3), "Unknown Camera Setting 3");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_RECORD_MODE), "Record Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_DIGITAL_ZOOM), "Digital Zoom");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FOCUS_TYPE), "Focus Type");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_UNKNOWN_7), "Unknown Camera Setting 7");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_LENS_TYPE), "Lens Type");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_MAX_APERTURE), "Max Aperture");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_MIN_APERTURE), "Min Aperture");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FLASH_ACTIVITY), "Flash Activity");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_FOCUS_CONTINUOUS), "Focus Continuous");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_AE_SETTING), "AE Setting");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_DISPLAY_APERTURE), "Display Aperture");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_ZOOM_SOURCE_WIDTH), "Zoom Source Width");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_ZOOM_TARGET_WIDTH), "Zoom Target Width");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_SPOT_METERING_MODE), "Spot Metering Mode");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_PHOTO_EFFECT), "Photo Effect");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_MANUAL_FLASH_OUTPUT), "Manual Flash Output");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_COLOR_TONE), "Color Tone");
        _tagNameMap.put(Integer.valueOf((int) CameraSettings.TAG_SRAW_QUALITY), "SRAW Quality");
        _tagNameMap.put(Integer.valueOf((int) FocalLength.TAG_WHITE_BALANCE), "White Balance");
        _tagNameMap.put(Integer.valueOf((int) FocalLength.TAG_SEQUENCE_NUMBER), "Sequence Number");
        _tagNameMap.put(Integer.valueOf((int) FocalLength.TAG_AF_POINT_USED), "AF Point Used");
        _tagNameMap.put(Integer.valueOf((int) FocalLength.TAG_FLASH_BIAS), "Flash Bias");
        _tagNameMap.put(Integer.valueOf((int) FocalLength.TAG_AUTO_EXPOSURE_BRACKETING), "Auto Exposure Bracketing");
        _tagNameMap.put(Integer.valueOf((int) FocalLength.TAG_AEB_BRACKET_VALUE), "AEB Bracket Value");
        _tagNameMap.put(Integer.valueOf((int) FocalLength.TAG_SUBJECT_DISTANCE), "Subject Distance");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_AUTO_ISO), "Auto ISO");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_BASE_ISO), "Base ISO");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_MEASURED_EV), "Measured EV");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_TARGET_APERTURE), "Target Aperture");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_TARGET_EXPOSURE_TIME), "Target Exposure Time");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_EXPOSURE_COMPENSATION), "Exposure Compensation");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_WHITE_BALANCE), "White Balance");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_SLOW_SHUTTER), "Slow Shutter");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_SEQUENCE_NUMBER), "Sequence Number");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_OPTICAL_ZOOM_CODE), "Optical Zoom Code");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_CAMERA_TEMPERATURE), "Camera Temperature");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_FLASH_GUIDE_NUMBER), "Flash Guide Number");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_AF_POINTS_IN_FOCUS), "AF Points in Focus");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_FLASH_EXPOSURE_BRACKETING), "Flash Exposure Compensation");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_AUTO_EXPOSURE_BRACKETING), "Auto Exposure Bracketing");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_AEB_BRACKET_VALUE), "AEB Bracket Value");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_CONTROL_MODE), "Control Mode");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_FOCUS_DISTANCE_UPPER), "Focus Distance Upper");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_FOCUS_DISTANCE_LOWER), "Focus Distance Lower");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_F_NUMBER), "F Number");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_EXPOSURE_TIME), "Exposure Time");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_MEASURED_EV_2), "Measured EV 2");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_BULB_DURATION), "Bulb Duration");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_CAMERA_TYPE), "Camera Type");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_AUTO_ROTATE), "Auto Rotate");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_ND_FILTER), "ND Filter");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_SELF_TIMER_2), "Self Timer 2");
        _tagNameMap.put(Integer.valueOf((int) ShotInfo.TAG_FLASH_OUTPUT), "Flash Output");
        _tagNameMap.put(Integer.valueOf((int) Panorama.TAG_PANORAMA_FRAME_NUMBER), "Panorama Frame Number");
        _tagNameMap.put(Integer.valueOf((int) Panorama.TAG_PANORAMA_DIRECTION), "Panorama Direction");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_NUM_AF_POINTS), "AF Point Count");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_VALID_AF_POINTS), "Valid AF Point Count");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_IMAGE_WIDTH), "Image Width");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_IMAGE_HEIGHT), "Image Height");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_AF_IMAGE_WIDTH), "AF Image Width");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_AF_IMAGE_HEIGHT), "AF Image Height");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_AF_AREA_WIDTH), "AF Area Width");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_AF_AREA_HEIGHT), "AF Area Height");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_AF_AREA_X_POSITIONS), "AF Area X Positions");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_AF_AREA_Y_POSITIONS), "AF Area Y Positions");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_AF_POINTS_IN_FOCUS), "AF Points in Focus");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_PRIMARY_AF_POINT_1), "Primary AF Point 1");
        _tagNameMap.put(Integer.valueOf((int) AFInfo.TAG_PRIMARY_AF_POINT_2), "Primary AF Point 2");
        _tagNameMap.put(19, "Thumbnail Image Valid Area");
        _tagNameMap.put(21, "Serial Number Format");
        _tagNameMap.put(26, "Super Macro");
        _tagNameMap.put(28, "Date Stamp Mode");
        _tagNameMap.put(29, "My Colors");
        _tagNameMap.put(30, "Firmware Revision");
        _tagNameMap.put(35, "Categories");
        _tagNameMap.put(36, "Face Detect Array 1");
        _tagNameMap.put(37, "Face Detect Array 2");
        _tagNameMap.put(38, "AF Info Array 2");
        _tagNameMap.put(40, "Image Unique ID");
        _tagNameMap.put(129, "Raw Data Offset");
        _tagNameMap.put(131, "Original Decision Data Offset");
        _tagNameMap.put(144, "Custom Functions (1D) Array");
        _tagNameMap.put(145, "Personal Functions Array");
        _tagNameMap.put(146, "Personal Function Values Array");
        _tagNameMap.put(147, "File Info Array");
        _tagNameMap.put(148, "AF Points in Focus (1D)");
        _tagNameMap.put(149, "Lens Model");
        _tagNameMap.put(150, "Serial Info Array");
        _tagNameMap.put(151, "Dust Removal Data");
        _tagNameMap.put(152, "Crop Info");
        _tagNameMap.put(153, "Custom Functions Array 2");
        _tagNameMap.put(154, "Aspect Information Array");
        _tagNameMap.put(160, "Processing Information Array");
        _tagNameMap.put(161, "Tone Curve Table");
        _tagNameMap.put(162, "Sharpness Table");
        _tagNameMap.put(163, "Sharpness Frequency Table");
        _tagNameMap.put(164, "White Balance Table");
        _tagNameMap.put(169, "Color Balance Array");
        _tagNameMap.put(170, "Measured Color Array");
        _tagNameMap.put(174, "Color Temperature");
        _tagNameMap.put(176, "Canon Flags Array");
        _tagNameMap.put(177, "Modified Information Array");
        _tagNameMap.put(178, "Tone Curve Matching");
        _tagNameMap.put(179, "White Balance Matching");
        _tagNameMap.put(180, "Color Space");
        _tagNameMap.put(182, "Preview Image Info Array");
        _tagNameMap.put(Integer.valueOf((int) TAG_VRD_OFFSET), "VRD Offset");
        _tagNameMap.put(224, "Sensor Information Array");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_DATA_ARRAY_2), "Color Data Array 1");
        _tagNameMap.put(16386, "CRW Parameters");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_INFO_ARRAY_2), "Color Data Array 2");
        _tagNameMap.put(Integer.valueOf((int) TAG_BLACK_LEVEL), "Black Level");
        _tagNameMap.put(Integer.valueOf((int) TAG_CUSTOM_PICTURE_STYLE_FILE_NAME), "Custom Picture Style File Name");
        _tagNameMap.put(Integer.valueOf((int) TAG_COLOR_INFO_ARRAY), "Color Info Array");
        _tagNameMap.put(Integer.valueOf((int) TAG_VIGNETTING_CORRECTION_ARRAY_1), "Vignetting Correction Array 1");
        _tagNameMap.put(Integer.valueOf((int) TAG_VIGNETTING_CORRECTION_ARRAY_2), "Vignetting Correction Array 2");
        _tagNameMap.put(Integer.valueOf((int) TAG_LIGHTING_OPTIMIZER_ARRAY), "Lighting Optimizer Array");
        _tagNameMap.put(Integer.valueOf((int) TAG_LENS_INFO_ARRAY), "Lens Info Array");
        _tagNameMap.put(Integer.valueOf((int) TAG_AMBIANCE_INFO_ARRAY), "Ambiance Info Array");
        _tagNameMap.put(Integer.valueOf((int) TAG_FILTER_INFO_ARRAY), "Filter Info Array");
    }

    public CanonMakernoteDirectory() {
        setDescriptor(new CanonMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Canon Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    public void setObjectArray(int tagType, @NotNull Object array) {
        if (!(array instanceof int[])) {
            super.setObjectArray(tagType, array);
            return;
        }
        switch (tagType) {
            case 1:
                int[] ints = (int[]) array;
                for (int i = 0; i < ints.length; i++) {
                    setInt(49408 + i, ints[i]);
                }
                return;
            case 2:
                int[] ints2 = (int[]) array;
                for (int i2 = 0; i2 < ints2.length; i2++) {
                    setInt(49664 + i2, ints2[i2]);
                }
                return;
            case 4:
                int[] ints3 = (int[]) array;
                for (int i3 = 0; i3 < ints3.length; i3++) {
                    setInt(50176 + i3, ints3[i3]);
                }
                return;
            case 5:
                int[] ints4 = (int[]) array;
                for (int i4 = 0; i4 < ints4.length; i4++) {
                    setInt(50432 + i4, ints4[i4]);
                }
                return;
            case 18:
                int[] values = (int[]) array;
                int numafpoints = values[0];
                int tagnumber = 0;
                int i5 = 0;
                while (i5 < values.length) {
                    if (AFInfo.TAG_NUM_AF_POINTS + tagnumber == 53768 || AFInfo.TAG_NUM_AF_POINTS + tagnumber == 53769) {
                        if (values.length - 1 >= i5 + numafpoints) {
                            short[] areaPositions = new short[numafpoints];
                            for (int j = 0; j < areaPositions.length; j++) {
                                areaPositions[j] = (short) values[i5 + j];
                            }
                            super.setObjectArray(AFInfo.TAG_NUM_AF_POINTS + tagnumber, areaPositions);
                        }
                        i5 += numafpoints - 1;
                    } else if (AFInfo.TAG_NUM_AF_POINTS + tagnumber == 53770) {
                        short[] pointsInFocus = new short[((numafpoints + 15) / 16)];
                        if (values.length - 1 >= pointsInFocus.length + i5) {
                            for (int j2 = 0; j2 < pointsInFocus.length; j2++) {
                                pointsInFocus[j2] = (short) values[i5 + j2];
                            }
                            super.setObjectArray(AFInfo.TAG_NUM_AF_POINTS + tagnumber, pointsInFocus);
                        }
                        i5 += pointsInFocus.length - 1;
                    } else {
                        super.setObjectArray(AFInfo.TAG_NUM_AF_POINTS + tagnumber, Integer.valueOf(values[i5]));
                    }
                    tagnumber++;
                    i5++;
                }
                return;
            default:
                super.setObjectArray(tagType, array);
                return;
        }
    }
}
