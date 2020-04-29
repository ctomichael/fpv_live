package com.drew.metadata.exif.makernotes;

import com.drew.imaging.PhotographicConversions;
import com.drew.lang.DateUtil;
import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class OlympusMakernoteDescriptor extends TagDescriptor<OlympusMakernoteDirectory> {
    public OlympusMakernoteDescriptor(@NotNull OlympusMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getMakernoteVersionDescription();
            case 257:
                return getColorModeDescription();
            case 258:
                return getImageQuality1Description();
            case 259:
                return getImageQuality2Description();
            case 512:
                return getSpecialModeDescription();
            case 513:
                return getJpegQualityDescription();
            case 514:
                return getMacroModeDescription();
            case 515:
                return getBWModeDescription();
            case 516:
                return getDigitalZoomDescription();
            case 517:
                return getFocalPlaneDiagonalDescription();
            case 519:
                return getCameraTypeDescription();
            case 521:
                return getCameraIdDescription();
            case 770:
                return getOneTouchWbDescription();
            case 4096:
                return getShutterSpeedDescription();
            case 4097:
                return getIsoValueDescription();
            case 4098:
                return getApertureValueDescription();
            case 4100:
                return getFlashModeDescription();
            case 4106:
                return getFocusRangeDescription();
            case 4107:
                return getFocusModeDescription();
            case OlympusMakernoteDirectory.TAG_SHARPNESS /*4111*/:
                return getSharpnessDescription();
            case 4113:
                return getColorMatrixDescription();
            case OlympusMakernoteDirectory.TAG_WB_MODE /*4117*/:
                return getWbModeDescription();
            case OlympusMakernoteDirectory.TAG_RED_BALANCE /*4119*/:
                return getRedBalanceDescription();
            case OlympusMakernoteDirectory.TAG_BLUE_BALANCE /*4120*/:
                return getBlueBalanceDescription();
            case OlympusMakernoteDirectory.TAG_CONTRAST /*4137*/:
                return getContrastDescription();
            case OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE_VALID /*4149*/:
                return getPreviewImageValidDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE /*61442*/:
                return getExposureModeDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_MODE /*61443*/:
                return getFlashModeCameraSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE /*61444*/:
                return getWhiteBalanceDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE /*61445*/:
                return getImageSizeDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_QUALITY /*61446*/:
                return getImageQualityDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_SHOOTING_MODE /*61447*/:
                return getShootingModeDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_METERING_MODE /*61448*/:
                return getMeteringModeDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_APEX_FILM_SPEED_VALUE /*61449*/:
                return getApexFilmSpeedDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_APEX_SHUTTER_SPEED_TIME_VALUE /*61450*/:
                return getApexShutterSpeedTimeDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_APEX_APERTURE_VALUE /*61451*/:
                return getApexApertureDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_MACRO_MODE /*61452*/:
                return getMacroModeCameraSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM /*61453*/:
                return getDigitalZoomCameraSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_COMPENSATION /*61454*/:
                return getExposureCompensationDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_BRACKET_STEP /*61455*/:
                return getBracketStepDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_LENGTH /*61457*/:
                return getIntervalLengthDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_NUMBER /*61458*/:
                return getIntervalNumberDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FOCAL_LENGTH /*61459*/:
                return getFocalLengthDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_DISTANCE /*61460*/:
                return getFocusDistanceDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_FIRED /*61461*/:
                return getFlashFiredDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_DATE /*61462*/:
                return getDateDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_TIME /*61463*/:
                return getTimeDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_MAX_APERTURE_AT_FOCAL_LENGTH /*61464*/:
                return getMaxApertureAtFocalLengthDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FILE_NUMBER_MEMORY /*61467*/:
                return getFileNumberMemoryDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_LAST_FILE_NUMBER /*61468*/:
                return getLastFileNumberDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_RED /*61469*/:
                return getWhiteBalanceRedDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_GREEN /*61470*/:
                return getWhiteBalanceGreenDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_BLUE /*61471*/:
                return getWhiteBalanceBlueDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_SATURATION /*61472*/:
                return getSaturationDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_CONTRAST /*61473*/:
                return getContrastCameraSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_SHARPNESS /*61474*/:
                return getSharpnessCameraSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_SUBJECT_PROGRAM /*61475*/:
                return getSubjectProgramDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_COMPENSATION /*61476*/:
                return getFlashCompensationDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_ISO_SETTING /*61477*/:
                return getIsoSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_CAMERA_MODEL /*61478*/:
                return getCameraModelDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_MODE /*61479*/:
                return getIntervalModeDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FOLDER_NAME /*61480*/:
                return getFolderNameDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_MODE /*61481*/:
                return getColorModeCameraSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_FILTER /*61482*/:
                return getColorFilterDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_BLACK_AND_WHITE_FILTER /*61483*/:
                return getBlackAndWhiteFilterDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_INTERNAL_FLASH /*61484*/:
                return getInternalFlashDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_APEX_BRIGHTNESS_VALUE /*61485*/:
                return getApexBrightnessDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_X_COORDINATE /*61486*/:
                return getSpotFocusPointXCoordinateDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_Y_COORDINATE /*61487*/:
                return getSpotFocusPointYCoordinateDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_WIDE_FOCUS_ZONE /*61488*/:
                return getWideFocusZoneDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE /*61489*/:
                return getFocusModeCameraSettingDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_AREA /*61490*/:
                return getFocusAreaDescription();
            case OlympusMakernoteDirectory.CameraSettings.TAG_DEC_SWITCH_POSITION /*61491*/:
                return getDecSwitchPositionDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getExposureModeDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE, "P", GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS, "S", "M");
    }

    @Nullable
    public String getFlashModeCameraSettingDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_MODE, "Normal", "Red-eye reduction", "Rear flash sync", "Wireless");
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE, "Auto", "Daylight", "Cloudy", "Tungsten", null, "Custom", null, "Fluorescent", "Fluorescent 2", null, null, "Custom 2", "Custom 3");
    }

    @Nullable
    public String getImageSizeDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE, "2560 x 1920", "1600 x 1200", "1280 x 960", "640 x 480");
    }

    @Nullable
    public String getImageQualityDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_QUALITY, "Raw", "Super Fine", "Fine", "Standard", "Economy", "Extra Fine");
    }

    @Nullable
    public String getShootingModeDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SHOOTING_MODE, "Single", "Continuous", "Self Timer", null, "Bracketing", "Interval", "UHS Continuous", "HS Continuous");
    }

    @Nullable
    public String getMeteringModeDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_METERING_MODE, "Multi-Segment", "Centre Weighted", "Spot");
    }

    @Nullable
    public String getApexFilmSpeedDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_FILM_SPEED_VALUE);
        if (value == null) {
            return null;
        }
        double iso = Math.pow((((double) value.longValue()) / 8.0d) - 1.0d, 2.0d) * 3.125d;
        DecimalFormat format = new DecimalFormat("0.##");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(iso);
    }

    @Nullable
    public String getApexShutterSpeedTimeDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_SHUTTER_SPEED_TIME_VALUE);
        if (value == null) {
            return null;
        }
        double shutterSpeed = Math.pow(((double) (49 - value.longValue())) / 8.0d, 2.0d);
        DecimalFormat format = new DecimalFormat("0.###");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(shutterSpeed) + " sec";
    }

    @Nullable
    public String getApexApertureDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_APERTURE_VALUE);
        if (value == null) {
            return null;
        }
        return getFStopDescription(Math.pow((((double) value.longValue()) / 16.0d) - 0.5d, 2.0d));
    }

    @Nullable
    public String getMacroModeCameraSettingDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_MACRO_MODE, "Off", "On");
    }

    @Nullable
    public String getDigitalZoomCameraSettingDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM, "Off", "Electronic magnification", "Digital zoom 2x");
    }

    @Nullable
    public String getExposureCompensationDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_COMPENSATION);
        DecimalFormat format = new DecimalFormat("0.##");
        if (value == null) {
            return null;
        }
        return format.format((((double) value.longValue()) / 3.0d) - 2.0d) + " EV";
    }

    @Nullable
    public String getBracketStepDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_BRACKET_STEP, "1/3 EV", "2/3 EV", "1 EV");
    }

    @Nullable
    public String getIntervalLengthDescription() {
        if (!((OlympusMakernoteDirectory) this._directory).isIntervalMode()) {
            return DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        }
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_LENGTH);
        if (value == null) {
            return null;
        }
        return value + " min";
    }

    @Nullable
    public String getIntervalNumberDescription() {
        if (!((OlympusMakernoteDirectory) this._directory).isIntervalMode()) {
            return DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        }
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_NUMBER);
        if (value == null) {
            return null;
        }
        return Long.toString(value.longValue());
    }

    @Nullable
    public String getFocalLengthDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_FOCAL_LENGTH);
        if (value == null) {
            return null;
        }
        return getFocalLengthDescription(((double) value.longValue()) / 256.0d);
    }

    @Nullable
    public String getFocusDistanceDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_DISTANCE);
        if (value == null) {
            return null;
        }
        return value.longValue() == 0 ? "Infinity" : value + " mm";
    }

    @Nullable
    public String getFlashFiredDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_FIRED, "No", "Yes");
    }

    @Nullable
    public String getDateDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_DATE);
        if (value == null) {
            return null;
        }
        int day = (int) (value.longValue() & 255);
        int month = (int) ((value.longValue() >> 16) & 255);
        int year = ((int) ((value.longValue() >> 8) & 255)) + 1970;
        if (!DateUtil.isValidDate(year, month, day)) {
            return "Invalid date";
        }
        return String.format("%04d-%02d-%02d", Integer.valueOf(year), Integer.valueOf(month + 1), Integer.valueOf(day));
    }

    @Nullable
    public String getTimeDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_TIME);
        if (value == null) {
            return null;
        }
        int hours = (int) ((value.longValue() >> 8) & 255);
        int minutes = (int) ((value.longValue() >> 16) & 255);
        int seconds = (int) (value.longValue() & 255);
        if (!DateUtil.isValidTime(hours, minutes, seconds)) {
            return "Invalid time";
        }
        return String.format("%02d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds));
    }

    @Nullable
    public String getMaxApertureAtFocalLengthDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_TIME);
        if (value == null) {
            return null;
        }
        return getFStopDescription(Math.pow((((double) value.longValue()) / 16.0d) - 0.5d, 2.0d));
    }

    @Nullable
    public String getFileNumberMemoryDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FILE_NUMBER_MEMORY, "Off", "On");
    }

    @Nullable
    public String getLastFileNumberDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_LAST_FILE_NUMBER);
        if (value == null) {
            return null;
        }
        return value.longValue() == 0 ? "File Number Memory Off" : Long.toString(value.longValue());
    }

    @Nullable
    public String getWhiteBalanceRedDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_RED);
        DecimalFormat format = new DecimalFormat("0.##");
        if (value == null) {
            return null;
        }
        return format.format(((double) value.longValue()) / 256.0d);
    }

    @Nullable
    public String getWhiteBalanceGreenDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_GREEN);
        DecimalFormat format = new DecimalFormat("0.##");
        if (value == null) {
            return null;
        }
        return format.format(((double) value.longValue()) / 256.0d);
    }

    @Nullable
    public String getWhiteBalanceBlueDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_BLUE);
        DecimalFormat format = new DecimalFormat("0.##");
        if (value == null) {
            return null;
        }
        return format.format(((double) value.longValue()) / 256.0d);
    }

    @Nullable
    public String getSaturationDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_SATURATION);
        if (value == null) {
            return null;
        }
        return Long.toString(value.longValue() - 3);
    }

    @Nullable
    public String getContrastCameraSettingDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_CONTRAST);
        if (value == null) {
            return null;
        }
        return Long.toString(value.longValue() - 3);
    }

    @Nullable
    public String getSharpnessCameraSettingDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SHARPNESS, "Hard", "Normal", "Soft");
    }

    @Nullable
    public String getSubjectProgramDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SUBJECT_PROGRAM, "None", "Portrait", "Text", "Night Portrait", "Sunset", "Sports Action");
    }

    @Nullable
    public String getFlashCompensationDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_COMPENSATION);
        DecimalFormat format = new DecimalFormat("0.##");
        if (value == null) {
            return null;
        }
        return format.format(((double) (value.longValue() - 6)) / 3.0d) + " EV";
    }

    @Nullable
    public String getIsoSettingDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_ISO_SETTING, "100", "200", "400", "800", "Auto", "64");
    }

    @Nullable
    public String getCameraModelDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_CAMERA_MODEL, "DiMAGE 7", "DiMAGE 5", "DiMAGE S304", "DiMAGE S404", "DiMAGE 7i", "DiMAGE 7Hi", "DiMAGE A1", "DiMAGE S414");
    }

    @Nullable
    public String getIntervalModeDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_MODE, "Still Image", "Time Lapse Movie");
    }

    @Nullable
    public String getFolderNameDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FOLDER_NAME, "Standard Form", "Data Form");
    }

    @Nullable
    public String getColorModeCameraSettingDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_MODE, "Natural Color", "Black & White", "Vivid Color", "Solarization", "AdobeRGB");
    }

    @Nullable
    public String getColorFilterDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_FILTER);
        if (value == null) {
            return null;
        }
        return Long.toString(value.longValue() - 3);
    }

    @Nullable
    public String getBlackAndWhiteFilterDescription() {
        return super.getDescription(OlympusMakernoteDirectory.CameraSettings.TAG_BLACK_AND_WHITE_FILTER);
    }

    @Nullable
    public String getInternalFlashDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_INTERNAL_FLASH, "Did Not Fire", "Fired");
    }

    @Nullable
    public String getApexBrightnessDescription() {
        Long value = ((OlympusMakernoteDirectory) this._directory).getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_BRIGHTNESS_VALUE);
        DecimalFormat format = new DecimalFormat("0.##");
        if (value == null) {
            return null;
        }
        return format.format((((double) value.longValue()) / 8.0d) - 6.0d);
    }

    @Nullable
    public String getSpotFocusPointXCoordinateDescription() {
        return super.getDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_X_COORDINATE);
    }

    @Nullable
    public String getSpotFocusPointYCoordinateDescription() {
        return super.getDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_Y_COORDINATE);
    }

    @Nullable
    public String getWideFocusZoneDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_WIDE_FOCUS_ZONE, "No Zone or AF Failed", "Center Zone (Horizontal Orientation)", "Center Zone (Vertical Orientation)", "Left Zone", "Right Zone");
    }

    @Nullable
    public String getFocusModeCameraSettingDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE, "Auto Focus", "Manual Focus");
    }

    @Nullable
    public String getFocusAreaDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_AREA, "Wide Focus (Normal)", "Spot Focus");
    }

    @Nullable
    public String getDecSwitchPositionDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_DEC_SWITCH_POSITION, "Exposure", CameraKeys.CONTRAST, CameraKeys.SATURATION, CameraKeys.FILTER);
    }

    @Nullable
    public String getMakernoteVersionDescription() {
        return getVersionBytesDescription(0, 2);
    }

    @Nullable
    public String getImageQuality2Description() {
        return getIndexedDescription(259, "Raw", "Super Fine", "Fine", "Standard", "Extra Fine");
    }

    @Nullable
    public String getImageQuality1Description() {
        return getIndexedDescription(258, "Raw", "Super Fine", "Fine", "Standard", "Extra Fine");
    }

    @Nullable
    public String getColorModeDescription() {
        return getIndexedDescription(257, "Natural Colour", "Black & White", "Vivid Colour", "Solarization", "AdobeRGB");
    }

    @Nullable
    public String getSharpnessDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.TAG_SHARPNESS, "Normal", "Hard", "Soft");
    }

    @Nullable
    public String getColorMatrixDescription() {
        int[] obj = ((OlympusMakernoteDirectory) this._directory).getIntArray(4113);
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obj.length; i++) {
            sb.append((int) ((short) obj[i]));
            if (i < obj.length - 1) {
                sb.append(" ");
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    @Nullable
    public String getWbModeDescription() {
        int[] obj = ((OlympusMakernoteDirectory) this._directory).getIntArray(OlympusMakernoteDirectory.TAG_WB_MODE);
        if (obj == null) {
            return null;
        }
        String val = String.format("%d %d", Integer.valueOf(obj[0]), Integer.valueOf(obj[1]));
        if (val.equals("1 0")) {
            return "Auto";
        }
        if (val.equals("1 2")) {
            return "Auto (2)";
        }
        if (val.equals("1 4")) {
            return "Auto (4)";
        }
        if (val.equals("2 2")) {
            return "3000 Kelvin";
        }
        if (val.equals("2 3")) {
            return "3700 Kelvin";
        }
        if (val.equals("2 4")) {
            return "4000 Kelvin";
        }
        if (val.equals("2 5")) {
            return "4500 Kelvin";
        }
        if (val.equals("2 6")) {
            return "5500 Kelvin";
        }
        if (val.equals("2 7")) {
            return "6500 Kelvin";
        }
        if (val.equals("2 8")) {
            return "7500 Kelvin";
        }
        if (val.equals("3 0")) {
            return "One-touch";
        }
        return "Unknown " + val;
    }

    @Nullable
    public String getRedBalanceDescription() {
        int[] values = ((OlympusMakernoteDirectory) this._directory).getIntArray(OlympusMakernoteDirectory.TAG_RED_BALANCE);
        if (values == null) {
            return null;
        }
        return String.valueOf(((double) ((short) values[0])) / 256.0d);
    }

    @Nullable
    public String getBlueBalanceDescription() {
        int[] values = ((OlympusMakernoteDirectory) this._directory).getIntArray(OlympusMakernoteDirectory.TAG_BLUE_BALANCE);
        if (values == null) {
            return null;
        }
        return String.valueOf(((double) ((short) values[0])) / 256.0d);
    }

    @Nullable
    public String getContrastDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.TAG_CONTRAST, "High", "Normal", "Low");
    }

    @Nullable
    public String getPreviewImageValidDescription() {
        return getIndexedDescription(OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE_VALID, "No", "Yes");
    }

    @Nullable
    public String getFocusModeDescription() {
        return getIndexedDescription(4107, "Auto", "Manual");
    }

    @Nullable
    public String getFocusRangeDescription() {
        return getIndexedDescription(4106, "Normal", "Macro");
    }

    @Nullable
    public String getFlashModeDescription() {
        return getIndexedDescription(4100, null, null, "On", "Off");
    }

    @Nullable
    public String getDigitalZoomDescription() {
        Rational value = ((OlympusMakernoteDirectory) this._directory).getRational(516);
        if (value == null) {
            return null;
        }
        return value.toSimpleString(false);
    }

    @Nullable
    public String getFocalPlaneDiagonalDescription() {
        Rational value = ((OlympusMakernoteDirectory) this._directory).getRational(517);
        if (value == null) {
            return null;
        }
        return new DecimalFormat("0.###").format(value.doubleValue()) + " mm";
    }

    @Nullable
    public String getCameraTypeDescription() {
        String cameratype = ((OlympusMakernoteDirectory) this._directory).getString(519);
        if (cameratype == null) {
            return null;
        }
        return OlympusMakernoteDirectory.OlympusCameraTypes.containsKey(cameratype) ? OlympusMakernoteDirectory.OlympusCameraTypes.get(cameratype) : cameratype;
    }

    @Nullable
    public String getCameraIdDescription() {
        byte[] bytes = ((OlympusMakernoteDirectory) this._directory).getByteArray(521);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    @Nullable
    public String getOneTouchWbDescription() {
        return getIndexedDescription(770, "Off", "On", "On (Preset)");
    }

    @Nullable
    public String getShutterSpeedDescription() {
        return super.getShutterSpeedDescription(4096);
    }

    @Nullable
    public String getIsoValueDescription() {
        Rational value = ((OlympusMakernoteDirectory) this._directory).getRational(4097);
        if (value == null) {
            return null;
        }
        return String.valueOf(Math.round(Math.pow(2.0d, value.doubleValue() - 5.0d) * 100.0d));
    }

    @Nullable
    public String getApertureValueDescription() {
        Double aperture = ((OlympusMakernoteDirectory) this._directory).getDoubleObject(4098);
        if (aperture == null) {
            return null;
        }
        return getFStopDescription(PhotographicConversions.apertureToFStop(aperture.doubleValue()));
    }

    @Nullable
    public String getMacroModeDescription() {
        return getIndexedDescription(514, "Normal (no macro)", "Macro");
    }

    @Nullable
    public String getBWModeDescription() {
        return getIndexedDescription(515, "Off", "On");
    }

    @Nullable
    public String getJpegQualityDescription() {
        String cameratype = ((OlympusMakernoteDirectory) this._directory).getString(519);
        if (cameratype != null) {
            Integer value = ((OlympusMakernoteDirectory) this._directory).getInteger(513);
            if (value == null) {
                return null;
            }
            if ((!cameratype.startsWith("SX") || cameratype.startsWith("SX151")) && !cameratype.startsWith("D4322")) {
                switch (value.intValue()) {
                    case 0:
                        return "Standard Quality (Low)";
                    case 1:
                        return "High Quality (Normal)";
                    case 2:
                        return "Super High Quality (Fine)";
                    case 4:
                        return "RAW";
                    case 5:
                        return "Medium-Fine";
                    case 6:
                        return "Small-Fine";
                    case 33:
                        return "Uncompressed";
                    default:
                        return "Unknown (" + value.toString() + ")";
                }
            } else {
                switch (value.intValue()) {
                    case 0:
                        return "Standard Quality (Low)";
                    case 1:
                        return "High Quality (Normal)";
                    case 2:
                        return "Super High Quality (Fine)";
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return "Unknown (" + value.toString() + ")";
                    case 6:
                        return "RAW";
                }
            }
        } else {
            return getIndexedDescription(513, 1, "Standard Quality", "High Quality", "Super High Quality");
        }
    }

    @Nullable
    public String getSpecialModeDescription() {
        long[] values = (long[]) ((OlympusMakernoteDirectory) this._directory).getObject(512);
        if (values == null) {
            return null;
        }
        if (values.length < 1) {
            return "";
        }
        StringBuilder desc = new StringBuilder();
        switch ((int) values[0]) {
            case 0:
                desc.append("Normal picture taking mode");
                break;
            case 1:
                desc.append("Unknown picture taking mode");
                break;
            case 2:
                desc.append("Fast picture taking mode");
                break;
            case 3:
                desc.append("Panorama picture taking mode");
                break;
            default:
                desc.append("Unknown picture taking mode");
                break;
        }
        if (values.length >= 2) {
            switch ((int) values[1]) {
                case 0:
                    break;
                case 1:
                    desc.append(" / 1st in a sequence");
                    break;
                case 2:
                    desc.append(" / 2nd in a sequence");
                    break;
                case 3:
                    desc.append(" / 3rd in a sequence");
                    break;
                default:
                    desc.append(" / ");
                    desc.append(values[1]);
                    desc.append("th in a sequence");
                    break;
            }
        }
        if (values.length >= 3) {
            switch ((int) values[2]) {
                case 1:
                    desc.append(" / Left to right panorama direction");
                    break;
                case 2:
                    desc.append(" / Right to left panorama direction");
                    break;
                case 3:
                    desc.append(" / Bottom to top panorama direction");
                    break;
                case 4:
                    desc.append(" / Top to bottom panorama direction");
                    break;
            }
        }
        return desc.toString();
    }
}
