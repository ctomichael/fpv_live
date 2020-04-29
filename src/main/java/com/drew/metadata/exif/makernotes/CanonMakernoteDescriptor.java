package com.drew.metadata.exif.makernotes;

import com.adobe.xmp.XMPError;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import dji.pilot.publics.model.ICameraResMode;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import dji.thirdparty.sanselan.ImageInfo;
import it.sauronsoftware.ftp4j.FTPCodes;
import java.text.DecimalFormat;
import java.util.HashMap;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.tls.CipherSuite;

public class CanonMakernoteDescriptor extends TagDescriptor<CanonMakernoteDirectory> {
    private static final HashMap<Integer, String> _lensTypeById = new HashMap<>();

    public CanonMakernoteDescriptor(@NotNull CanonMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 12:
                return getSerialNumberDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_MACRO_MODE /*49409*/:
                return getMacroModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_SELF_TIMER_DELAY /*49410*/:
                return getSelfTimerDelayDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_QUALITY /*49411*/:
                return getQualityDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FLASH_MODE /*49412*/:
                return getFlashModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_CONTINUOUS_DRIVE_MODE /*49413*/:
                return getContinuousDriveModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_1 /*49415*/:
                return getFocusMode1Description();
            case CanonMakernoteDirectory.CameraSettings.TAG_RECORD_MODE /*49417*/:
                return getRecordModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE /*49418*/:
                return getImageSizeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_EASY_SHOOTING_MODE /*49419*/:
                return getEasyShootingModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM /*49420*/:
                return getDigitalZoomDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_CONTRAST /*49421*/:
                return getContrastDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_SATURATION /*49422*/:
                return getSaturationDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_SHARPNESS /*49423*/:
                return getSharpnessDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_ISO /*49424*/:
                return getIsoDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_METERING_MODE /*49425*/:
                return getMeteringModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_TYPE /*49426*/:
                return getFocusTypeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_AF_POINT_SELECTED /*49427*/:
                return getAfPointSelectedDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE /*49428*/:
                return getExposureModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_LENS_TYPE /*49430*/:
                return getLensTypeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_LONG_FOCAL_LENGTH /*49431*/:
                return getLongFocalLengthDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_SHORT_FOCAL_LENGTH /*49432*/:
                return getShortFocalLengthDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FOCAL_UNITS_PER_MM /*49433*/:
                return getFocalUnitsPerMillimetreDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_MAX_APERTURE /*49434*/:
                return getMaxApertureDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_MIN_APERTURE /*49435*/:
                return getMinApertureDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FLASH_ACTIVITY /*49436*/:
                return getFlashActivityDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FLASH_DETAILS /*49437*/:
                return getFlashDetailsDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_CONTINUOUS /*49438*/:
                return getFocusContinuousDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_AE_SETTING /*49439*/:
                return getAESettingDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_2 /*49440*/:
                return getFocusMode2Description();
            case CanonMakernoteDirectory.CameraSettings.TAG_DISPLAY_APERTURE /*49441*/:
                return getDisplayApertureDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_SPOT_METERING_MODE /*49445*/:
                return getSpotMeteringModeDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_PHOTO_EFFECT /*49446*/:
                return getPhotoEffectDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_MANUAL_FLASH_OUTPUT /*49447*/:
                return getManualFlashOutputDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_COLOR_TONE /*49449*/:
                return getColorToneDescription();
            case CanonMakernoteDirectory.CameraSettings.TAG_SRAW_QUALITY /*49453*/:
                return getSRawQualityDescription();
            case CanonMakernoteDirectory.FocalLength.TAG_WHITE_BALANCE /*49671*/:
                return getWhiteBalanceDescription();
            case CanonMakernoteDirectory.FocalLength.TAG_AF_POINT_USED /*49678*/:
                return getAfPointUsedDescription();
            case CanonMakernoteDirectory.FocalLength.TAG_FLASH_BIAS /*49679*/:
                return getFlashBiasDescription();
            case CanonMakernoteDirectory.AFInfo.TAG_AF_POINTS_IN_FOCUS /*53770*/:
                return getTagAfPointsInFocus();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getSerialNumberDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(12);
        if (value == null) {
            return null;
        }
        return String.format("%04X%05d", Integer.valueOf((value.intValue() >> 8) & 255), Integer.valueOf(value.intValue() & 255));
    }

    @Nullable
    public String getFlashBiasDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.FocalLength.TAG_FLASH_BIAS);
        if (value == null) {
            return null;
        }
        boolean isNegative = false;
        if (value.intValue() > 61440) {
            isNegative = true;
            value = Integer.valueOf(Integer.valueOf(65535 - value.intValue()).intValue() + 1);
        }
        return (isNegative ? "-" : "") + Float.toString(((float) value.intValue()) / 32.0f) + " EV";
    }

    @Nullable
    public String getAfPointUsedDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.FocalLength.TAG_AF_POINT_USED);
        if (value == null) {
            return null;
        }
        if ((value.intValue() & 7) == 0) {
            return "Right";
        }
        if ((value.intValue() & 7) == 1) {
            return "Centre";
        }
        if ((value.intValue() & 7) == 2) {
            return "Left";
        }
        return "Unknown (" + value + ")";
    }

    @Nullable
    public String getTagAfPointsInFocus() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.AFInfo.TAG_AF_POINTS_IN_FOCUS);
        if (value == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if ((value.intValue() & (1 << i)) != 0) {
                if (sb.length() != 0) {
                    sb.append(',');
                }
                sb.append(i);
            }
        }
        return sb.length() == 0 ? "None" : sb.toString();
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.FocalLength.TAG_WHITE_BALANCE, "Auto", "Sunny", "Cloudy", "Tungsten", "Florescent", "Flash", "Custom");
    }

    @Nullable
    public String getFocusMode2Description() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_2, "Single", "Continuous");
    }

    @Nullable
    public String getFlashDetailsDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FLASH_DETAILS);
        if (value == null) {
            return null;
        }
        if (((value.intValue() >> 14) & 1) != 0) {
            return "External E-TTL";
        }
        if (((value.intValue() >> 13) & 1) != 0) {
            return "Internal flash";
        }
        if (((value.intValue() >> 11) & 1) != 0) {
            return "FP sync used";
        }
        if (((value.intValue() >> 4) & 1) != 0) {
            return "FP sync enabled";
        }
        return "Unknown (" + value + ")";
    }

    @Nullable
    public String getFocalUnitsPerMillimetreDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FOCAL_UNITS_PER_MM);
        if (value == null) {
            return null;
        }
        if (value.intValue() != 0) {
            return Integer.toString(value.intValue());
        }
        return "";
    }

    @Nullable
    public String getShortFocalLengthDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SHORT_FOCAL_LENGTH);
        if (value == null) {
            return null;
        }
        return Integer.toString(value.intValue()) + " " + getFocalUnitsPerMillimetreDescription();
    }

    @Nullable
    public String getLongFocalLengthDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_LONG_FOCAL_LENGTH);
        if (value == null) {
            return null;
        }
        return Integer.toString(value.intValue()) + " " + getFocalUnitsPerMillimetreDescription();
    }

    @Nullable
    public String getExposureModeDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE, "Easy shooting", "Program", "Tv-priority", "Av-priority", "Manual", "A-DEP");
    }

    @Nullable
    public String getLensTypeDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_LENS_TYPE);
        if (value == null) {
            return null;
        }
        if (_lensTypeById.containsKey(value)) {
            return _lensTypeById.get(value);
        }
        return String.format("Unknown (%d)", value);
    }

    @Nullable
    public String getMaxApertureDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_MAX_APERTURE);
        if (value == null) {
            return null;
        }
        if (value.intValue() <= 512) {
            return getFStopDescription(Math.exp((decodeCanonEv(value.intValue()) * Math.log(2.0d)) / 2.0d));
        }
        return String.format("Unknown (%d)", value);
    }

    @Nullable
    public String getMinApertureDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_MIN_APERTURE);
        if (value == null) {
            return null;
        }
        if (value.intValue() <= 512) {
            return getFStopDescription(Math.exp((decodeCanonEv(value.intValue()) * Math.log(2.0d)) / 2.0d));
        }
        return String.format("Unknown (%d)", value);
    }

    @Nullable
    public String getAfPointSelectedDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_AF_POINT_SELECTED, 12288, "None (MF)", "Auto selected", "Right", "Centre", "Left");
    }

    @Nullable
    public String getMeteringModeDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_METERING_MODE, 3, "Evaluative", "Partial", "Centre weighted");
    }

    @Nullable
    public String getIsoDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_ISO);
        if (value == null) {
            return null;
        }
        if ((value.intValue() & 16384) != 0) {
            return "" + (value.intValue() & -16385);
        }
        switch (value.intValue()) {
            case 0:
                return "Not specified (see ISOSpeedRatings tag)";
            case 15:
                return "Auto";
            case 16:
                return "50";
            case 17:
                return "100";
            case 18:
                return "200";
            case 19:
                return "400";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getSharpnessDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SHARPNESS);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Normal";
            case 1:
                return "High";
            case 65535:
                return "Low";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getSaturationDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SATURATION);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Normal";
            case 1:
                return "High";
            case 65535:
                return "Low";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getContrastDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_CONTRAST);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Normal";
            case 1:
                return "High";
            case 65535:
                return "Low";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getEasyShootingModeDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_EASY_SHOOTING_MODE, "Full auto", "Manual", "Landscape", "Fast shutter", "Slow shutter", "Night", "B&W", "Sepia", "Portrait", "Sports", "Macro / Closeup", "Pan focus");
    }

    @Nullable
    public String getImageSizeDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE, "Large", "Medium", "Small");
    }

    @Nullable
    public String getFocusMode1Description() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_1, "One-shot", "AI Servo", "AI Focus", "Manual Focus", "Single", "Continuous", "Manual Focus");
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Nullable
    public String getContinuousDriveModeDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_CONTINUOUS_DRIVE_MODE);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                Integer delay = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SELF_TIMER_DELAY);
                if (delay != null) {
                    return delay.intValue() == 0 ? "Single shot" : "Single shot with self-timer";
                }
                break;
            case 1:
                break;
            default:
                return "Unknown (" + value + ")";
        }
        return "Continuous";
    }

    @Nullable
    public String getFlashModeDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FLASH_MODE);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "No flash fired";
            case 1:
                return "Auto";
            case 2:
                return "On";
            case 3:
                return "Red-eye reduction";
            case 4:
                return "Slow-synchro";
            case 5:
                return "Auto and red-eye reduction";
            case 6:
                return "On and red-eye reduction";
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                return "Unknown (" + value + ")";
            case 16:
                return "External flash";
        }
    }

    @Nullable
    public String getSelfTimerDelayDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SELF_TIMER_DELAY);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 0) {
            return "Self timer not used";
        }
        return new DecimalFormat("0.##").format(((double) value.intValue()) * 0.1d) + " sec";
    }

    @Nullable
    public String getMacroModeDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_MACRO_MODE, 1, "Macro", "Normal");
    }

    @Nullable
    public String getQualityDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_QUALITY, 2, "Normal", "Fine", null, "Superfine");
    }

    @Nullable
    public String getDigitalZoomDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM, "No digital zoom", "2x", "4x");
    }

    @Nullable
    public String getRecordModeDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_RECORD_MODE, 1, ImageInfo.COMPRESSION_ALGORITHM_JPEG, "CRW+THM", "AVI+THM", "TIF", "TIF+JPEG", "CR2", "CR2+JPEG", null, "MOV", "MP4");
    }

    @Nullable
    public String getFocusTypeDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_TYPE);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Manual";
            case 1:
                return "Auto";
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                return "Unknown (" + value + ")";
            case 3:
                return "Close-up (Macro)";
            case 8:
                return "Locked (Pan Mode)";
        }
    }

    @Nullable
    public String getFlashActivityDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_FLASH_ACTIVITY, "Flash did not fire", "Flash fired");
    }

    @Nullable
    public String getFocusContinuousDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_CONTINUOUS, 0, "Single", "Continuous", null, null, null, null, null, null, "Manual");
    }

    @Nullable
    public String getAESettingDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_AE_SETTING, 0, "Normal AE", "Exposure Compensation", "AE Lock", "AE Lock + Exposure Comp.", "No AE");
    }

    @Nullable
    public String getDisplayApertureDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_DISPLAY_APERTURE);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 65535) {
            return value.toString();
        }
        return getFStopDescription((double) (((float) value.intValue()) / 10.0f));
    }

    @Nullable
    public String getSpotMeteringModeDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_SPOT_METERING_MODE, 0, ICameraResMode.ICameraVideoResolutionRes.VRSTR_MAX, "AF Point");
    }

    @Nullable
    public String getPhotoEffectDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_PHOTO_EFFECT);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Off";
            case 1:
                return "Vivid";
            case 2:
                return "Neutral";
            case 3:
                return "Smooth";
            case 4:
                return "Sepia";
            case 5:
                return "B&W";
            case 6:
                return "Custom";
            case 100:
                return "My Color Data";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getManualFlashOutputDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_MANUAL_FLASH_OUTPUT);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "n/a";
            case 1280:
                return "Full";
            case OlympusCameraSettingsMakernoteDirectory.TagWhiteBalanceBracket /*1282*/:
                return "Medium";
            case OlympusCameraSettingsMakernoteDirectory.TagModifiedSaturation /*1284*/:
                return "Low";
            case 32767:
                return "n/a";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getColorToneDescription() {
        Integer value = ((CanonMakernoteDirectory) this._directory).getInteger(CanonMakernoteDirectory.CameraSettings.TAG_COLOR_TONE);
        if (value == null) {
            return null;
        }
        return value.intValue() == 32767 ? "n/a" : value.toString();
    }

    @Nullable
    public String getSRawQualityDescription() {
        return getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_SRAW_QUALITY, 0, "n/a", "sRAW1 (mRAW)", "sRAW2 (sRAW)");
    }

    private double decodeCanonEv(int val) {
        int sign = 1;
        if (val < 0) {
            val = -val;
            sign = -1;
        }
        int frac = val & 31;
        int val2 = val - frac;
        if (frac == 12) {
            frac = 10;
        } else if (frac == 20) {
            frac = 21;
        }
        return ((double) ((val2 + frac) * sign)) / 32.0d;
    }

    static {
        _lensTypeById.put(1, "Canon EF 50mm f/1.8");
        _lensTypeById.put(2, "Canon EF 28mm f/2.8");
        _lensTypeById.put(3, "Canon EF 135mm f/2.8 Soft");
        _lensTypeById.put(4, "Canon EF 35-105mm f/3.5-4.5 or Sigma Lens");
        _lensTypeById.put(5, "Canon EF 35-70mm f/3.5-4.5");
        _lensTypeById.put(6, "Canon EF 28-70mm f/3.5-4.5 or Sigma or Tokina Lens");
        _lensTypeById.put(7, "Canon EF 100-300mm f/5.6L");
        _lensTypeById.put(8, "Canon EF 100-300mm f/5.6 or Sigma or Tokina Lens");
        _lensTypeById.put(9, "Canon EF 70-210mm f/4");
        _lensTypeById.put(10, "Canon EF 50mm f/2.5 Macro or Sigma Lens");
        _lensTypeById.put(11, "Canon EF 35mm f/2");
        _lensTypeById.put(13, "Canon EF 15mm f/2.8 Fisheye");
        _lensTypeById.put(14, "Canon EF 50-200mm f/3.5-4.5L");
        _lensTypeById.put(15, "Canon EF 50-200mm f/3.5-4.5");
        _lensTypeById.put(16, "Canon EF 35-135mm f/3.5-4.5");
        _lensTypeById.put(17, "Canon EF 35-70mm f/3.5-4.5A");
        _lensTypeById.put(18, "Canon EF 28-70mm f/3.5-4.5");
        _lensTypeById.put(20, "Canon EF 100-200mm f/4.5A");
        _lensTypeById.put(21, "Canon EF 80-200mm f/2.8L");
        _lensTypeById.put(22, "Canon EF 20-35mm f/2.8L or Tokina Lens");
        _lensTypeById.put(23, "Canon EF 35-105mm f/3.5-4.5");
        _lensTypeById.put(24, "Canon EF 35-80mm f/4-5.6 Power Zoom");
        _lensTypeById.put(25, "Canon EF 35-80mm f/4-5.6 Power Zoom");
        _lensTypeById.put(26, "Canon EF 100mm f/2.8 Macro or Other Lens");
        _lensTypeById.put(27, "Canon EF 35-80mm f/4-5.6");
        _lensTypeById.put(28, "Canon EF 80-200mm f/4.5-5.6 or Tamron Lens");
        _lensTypeById.put(29, "Canon EF 50mm f/1.8 II");
        _lensTypeById.put(30, "Canon EF 35-105mm f/4.5-5.6");
        _lensTypeById.put(31, "Canon EF 75-300mm f/4-5.6 or Tamron Lens");
        _lensTypeById.put(32, "Canon EF 24mm f/2.8 or Sigma Lens");
        _lensTypeById.put(33, "Voigtlander or Carl Zeiss Lens");
        _lensTypeById.put(35, "Canon EF 35-80mm f/4-5.6");
        _lensTypeById.put(36, "Canon EF 38-76mm f/4.5-5.6");
        _lensTypeById.put(37, "Canon EF 35-80mm f/4-5.6 or Tamron Lens");
        _lensTypeById.put(38, "Canon EF 80-200mm f/4.5-5.6");
        _lensTypeById.put(39, "Canon EF 75-300mm f/4-5.6");
        _lensTypeById.put(40, "Canon EF 28-80mm f/3.5-5.6");
        _lensTypeById.put(41, "Canon EF 28-90mm f/4-5.6");
        _lensTypeById.put(42, "Canon EF 28-200mm f/3.5-5.6 or Tamron Lens");
        _lensTypeById.put(43, "Canon EF 28-105mm f/4-5.6");
        _lensTypeById.put(44, "Canon EF 90-300mm f/4.5-5.6");
        _lensTypeById.put(45, "Canon EF-S 18-55mm f/3.5-5.6 [II]");
        _lensTypeById.put(46, "Canon EF 28-90mm f/4-5.6");
        _lensTypeById.put(47, "Zeiss Milvus 35mm f/2 or 50mm f/2");
        _lensTypeById.put(48, "Canon EF-S 18-55mm f/3.5-5.6 IS");
        _lensTypeById.put(49, "Canon EF-S 55-250mm f/4-5.6 IS");
        _lensTypeById.put(50, "Canon EF-S 18-200mm f/3.5-5.6 IS");
        _lensTypeById.put(51, "Canon EF-S 18-135mm f/3.5-5.6 IS");
        _lensTypeById.put(52, "Canon EF-S 18-55mm f/3.5-5.6 IS II");
        _lensTypeById.put(53, "Canon EF-S 18-55mm f/3.5-5.6 III");
        _lensTypeById.put(54, "Canon EF-S 55-250mm f/4-5.6 IS II");
        _lensTypeById.put(94, "Canon TS-E 17mm f/4L");
        _lensTypeById.put(95, "Canon TS-E 24.0mm f/3.5 L II");
        _lensTypeById.put(Integer.valueOf((int) PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH), "Canon MP-E 65mm f/2.8 1-5x Macro Photo");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.DATA_CONNECTION_ALREADY_OPEN), "Canon TS-E 24mm f/3.5L");
        _lensTypeById.put(126, "Canon TS-E 45mm f/2.8");
        _lensTypeById.put(Integer.valueOf((int) CertificateBody.profileType), "Canon TS-E 90mm f/2.8");
        _lensTypeById.put(129, "Canon EF 300mm f/2.8L");
        _lensTypeById.put(Integer.valueOf((int) NikonType2MakernoteDirectory.TAG_ADAPTER), "Canon EF 50mm f/1.0L");
        _lensTypeById.put(131, "Canon EF 28-80mm f/2.8-4L or Sigma Lens");
        _lensTypeById.put(132, "Canon EF 1200mm f/5.6L");
        _lensTypeById.put(134, "Canon EF 600mm f/4L IS");
        _lensTypeById.put(135, "Canon EF 200mm f/1.8L");
        _lensTypeById.put(136, "Canon EF 300mm f/2.8L");
        _lensTypeById.put(137, "Canon EF 85mm f/1.2L or Sigma or Tamron Lens");
        _lensTypeById.put(138, "Canon EF 28-80mm f/2.8-4L");
        _lensTypeById.put(139, "Canon EF 400mm f/2.8L");
        _lensTypeById.put(140, "Canon EF 500mm f/4.5L");
        _lensTypeById.put(141, "Canon EF 500mm f/4.5L");
        _lensTypeById.put(142, "Canon EF 300mm f/2.8L IS");
        _lensTypeById.put(143, "Canon EF 500mm f/4L IS or Sigma Lens");
        _lensTypeById.put(144, "Canon EF 35-135mm f/4-5.6 USM");
        _lensTypeById.put(145, "Canon EF 100-300mm f/4.5-5.6 USM");
        _lensTypeById.put(146, "Canon EF 70-210mm f/3.5-4.5 USM");
        _lensTypeById.put(147, "Canon EF 35-135mm f/4-5.6 USM");
        _lensTypeById.put(148, "Canon EF 28-80mm f/3.5-5.6 USM");
        _lensTypeById.put(149, "Canon EF 100mm f/2 USM");
        _lensTypeById.put(150, "Canon EF 14mm f/2.8L or Sigma Lens");
        _lensTypeById.put(151, "Canon EF 200mm f/2.8L");
        _lensTypeById.put(152, "Canon EF 300mm f/4L IS or Sigma Lens");
        _lensTypeById.put(153, "Canon EF 35-350mm f/3.5-5.6L or Sigma or Tamron Lens");
        _lensTypeById.put(154, "Canon EF 20mm f/2.8 USM or Zeiss Lens");
        _lensTypeById.put(155, "Canon EF 85mm f/1.8 USM");
        _lensTypeById.put(156, "Canon EF 28-105mm f/3.5-4.5 USM or Tamron Lens");
        _lensTypeById.put(160, "Canon EF 20-35mm f/3.5-4.5 USM or Tamron or Tokina Lens");
        _lensTypeById.put(161, "Canon EF 28-70mm f/2.8L or Sigma or Tamron Lens");
        _lensTypeById.put(162, "Canon EF 200mm f/2.8L");
        _lensTypeById.put(163, "Canon EF 300mm f/4L");
        _lensTypeById.put(164, "Canon EF 400mm f/5.6L");
        _lensTypeById.put(165, "Canon EF 70-200mm f/2.8 L");
        _lensTypeById.put(166, "Canon EF 70-200mm f/2.8 L + 1.4x");
        _lensTypeById.put(167, "Canon EF 70-200mm f/2.8 L + 2x");
        _lensTypeById.put(168, "Canon EF 28mm f/1.8 USM or Sigma Lens");
        _lensTypeById.put(169, "Canon EF 17-35mm f/2.8L or Sigma Lens");
        _lensTypeById.put(170, "Canon EF 200mm f/2.8L II");
        _lensTypeById.put(171, "Canon EF 300mm f/4L");
        _lensTypeById.put(172, "Canon EF 400mm f/5.6L or Sigma Lens");
        _lensTypeById.put(173, "Canon EF 180mm Macro f/3.5L or Sigma Lens");
        _lensTypeById.put(174, "Canon EF 135mm f/2L or Other Lens");
        _lensTypeById.put(175, "Canon EF 400mm f/2.8L");
        _lensTypeById.put(176, "Canon EF 24-85mm f/3.5-4.5 USM");
        _lensTypeById.put(177, "Canon EF 300mm f/4L IS");
        _lensTypeById.put(178, "Canon EF 28-135mm f/3.5-5.6 IS");
        _lensTypeById.put(179, "Canon EF 24mm f/1.4L");
        _lensTypeById.put(180, "Canon EF 35mm f/1.4L or Other Lens");
        _lensTypeById.put(181, "Canon EF 100-400mm f/4.5-5.6L IS + 1.4x or Sigma Lens");
        _lensTypeById.put(182, "Canon EF 100-400mm f/4.5-5.6L IS + 2x or Sigma Lens");
        _lensTypeById.put(183, "Canon EF 100-400mm f/4.5-5.6L IS or Sigma Lens");
        _lensTypeById.put(184, "Canon EF 400mm f/2.8L + 2x");
        _lensTypeById.put(185, "Canon EF 600mm f/4L IS");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256), "Canon EF 70-200mm f/4L");
        _lensTypeById.put(187, "Canon EF 70-200mm f/4L + 1.4x");
        _lensTypeById.put(188, "Canon EF 70-200mm f/4L + 2x");
        _lensTypeById.put(189, "Canon EF 70-200mm f/4L + 2.8x");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256), "Canon EF 100mm f/2.8 Macro USM");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256), "Canon EF 400mm f/4 DO IS");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256), "Canon EF 35-80mm f/4-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256), "Canon EF 80-200mm f/4.5-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256), "Canon EF 35-105mm f/4.5-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256), "Canon EF 75-300mm f/4-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256), "Canon EF 75-300mm f/4-5.6 IS USM");
        _lensTypeById.put(198, "Canon EF 50mm f/1.4 USM or Zeiss Lens");
        _lensTypeById.put(199, "Canon EF 28-80mm f/3.5-5.6 USM");
        _lensTypeById.put(200, "Canon EF 75-300mm f/4-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) XMPError.BADXML), "Canon EF 28-80mm f/3.5-5.6 USM");
        _lensTypeById.put(202, "Canon EF 28-80mm f/3.5-5.6 USM IV");
        _lensTypeById.put(Integer.valueOf((int) CanonMakernoteDirectory.TAG_VRD_OFFSET), "Canon EF 22-55mm f/4-5.6 USM");
        _lensTypeById.put(209, "Canon EF 55-200mm f/4.5-5.6");
        _lensTypeById.put(210, "Canon EF 28-90mm f/4-5.6 USM");
        _lensTypeById.put(211, "Canon EF 28-200mm f/3.5-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.DIRECTORY_STATUS), "Canon EF 28-105mm f/4-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.FILE_STATUS), "Canon EF 90-300mm f/4.5-5.6 USM or Tamron Lens");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.HELP_MESSAGE), "Canon EF-S 18-55mm f/3.5-5.6 USM");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.NAME_SYSTEM_TIME), "Canon EF 55-200mm f/4.5-5.6 II USM");
        _lensTypeById.put(217, "Tamron AF 18-270mm f/3.5-6.3 Di II VC PZD");
        _lensTypeById.put(224, "Canon EF 70-200mm f/2.8L IS");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.DATA_CONNECTION_OPEN), "Canon EF 70-200mm f/2.8L IS + 1.4x");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.DATA_CONNECTION_CLOSING), "Canon EF 70-200mm f/2.8L IS + 2x");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.ENTER_PASSIVE_MODE), "Canon EF 70-200mm f/2.8L IS + 2.8x");
        _lensTypeById.put(228, "Canon EF 28-105mm f/3.5-4.5 USM");
        _lensTypeById.put(229, "Canon EF 16-35mm f/2.8L");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.USER_LOGGED_IN), "Canon EF 24-70mm f/2.8L");
        _lensTypeById.put(231, "Canon EF 17-40mm f/4L");
        _lensTypeById.put(232, "Canon EF 70-300mm f/4.5-5.6 DO IS USM");
        _lensTypeById.put(233, "Canon EF 28-300mm f/3.5-5.6L IS");
        _lensTypeById.put(234, "Canon EF-S 17-85mm f/4-5.6 IS USM or Tokina Lens");
        _lensTypeById.put(235, "Canon EF-S 10-22mm f/3.5-4.5 USM");
        _lensTypeById.put(236, "Canon EF-S 60mm f/2.8 Macro USM");
        _lensTypeById.put(237, "Canon EF 24-105mm f/4L IS");
        _lensTypeById.put(238, "Canon EF 70-300mm f/4-5.6 IS USM");
        _lensTypeById.put(239, "Canon EF 85mm f/1.2L II");
        _lensTypeById.put(240, "Canon EF-S 17-55mm f/2.8 IS USM");
        _lensTypeById.put(241, "Canon EF 50mm f/1.2L");
        _lensTypeById.put(242, "Canon EF 70-200mm f/4L IS");
        _lensTypeById.put(243, "Canon EF 70-200mm f/4L IS + 1.4x");
        _lensTypeById.put(244, "Canon EF 70-200mm f/4L IS + 2x");
        _lensTypeById.put(245, "Canon EF 70-200mm f/4L IS + 2.8x");
        _lensTypeById.put(246, "Canon EF 16-35mm f/2.8L II");
        _lensTypeById.put(247, "Canon EF 14mm f/2.8L II USM");
        _lensTypeById.put(248, "Canon EF 200mm f/2L IS or Sigma Lens");
        _lensTypeById.put(249, "Canon EF 800mm f/5.6L IS");
        _lensTypeById.put(250, "Canon EF 24mm f/1.4L II or Sigma Lens");
        _lensTypeById.put(251, "Canon EF 70-200mm f/2.8L IS II USM");
        _lensTypeById.put(252, "Canon EF 70-200mm f/2.8L IS II USM + 1.4x");
        _lensTypeById.put(Integer.valueOf((int) ICameraResMode.ICameraVideoResolutionRes.VR_MAX), "Canon EF 70-200mm f/2.8L IS II USM + 2x");
        _lensTypeById.put(254, "Canon EF 100mm f/2.8L Macro IS USM");
        _lensTypeById.put(255, "Sigma 24-105mm f/4 DG OS HSM | A or Other Sigma Lens");
        _lensTypeById.put(488, "Canon EF-S 15-85mm f/3.5-5.6 IS USM");
        _lensTypeById.put(489, "Canon EF 70-300mm f/4-5.6L IS USM");
        _lensTypeById.put(490, "Canon EF 8-15mm f/4L Fisheye USM");
        _lensTypeById.put(491, "Canon EF 300mm f/2.8L IS II USM");
        _lensTypeById.put(492, "Canon EF 400mm f/2.8L IS II USM");
        _lensTypeById.put(493, "Canon EF 500mm f/4L IS II USM or EF 24-105mm f4L IS USM");
        _lensTypeById.put(494, "Canon EF 600mm f/4.0L IS II USM");
        _lensTypeById.put(495, "Canon EF 24-70mm f/2.8L II USM");
        _lensTypeById.put(496, "Canon EF 200-400mm f/4L IS USM");
        _lensTypeById.put(499, "Canon EF 200-400mm f/4L IS USM + 1.4x");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.COMMAND_NOT_IMPLEMENTED), "Canon EF 28mm f/2.8 IS USM");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.BAD_SEQUENCE_OF_COMMANDS), "Canon EF 24mm f/2.8 IS USM");
        _lensTypeById.put(Integer.valueOf((int) FTPCodes.COMMAND_PARAMETER_NOT_IMPLEMENTED), "Canon EF 24-70mm f/4L IS USM");
        _lensTypeById.put(505, "Canon EF 35mm f/2 IS USM");
        _lensTypeById.put(506, "Canon EF 400mm f/4 DO IS II USM");
        _lensTypeById.put(507, "Canon EF 16-35mm f/4L IS USM");
        _lensTypeById.put(508, "Canon EF 11-24mm f/4L USM");
        _lensTypeById.put(747, "Canon EF 100-400mm f/4.5-5.6L IS II USM");
        _lensTypeById.put(750, "Canon EF 35mm f/1.4L II USM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_OLYMPUS_IMAGE_WIDTH), "Canon EF-S 18-135mm f/3.5-5.6 IS STM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_OLYMPUS_IMAGE_HEIGHT), "Canon EF-M 18-55mm f/3.5-5.6 IS STM or Tamron Lens");
        _lensTypeById.put(4144, "Canon EF 40mm f/2.8 STM");
        _lensTypeById.put(4145, "Canon EF-M 22mm f/2 STM");
        _lensTypeById.put(4146, "Canon EF-S 18-55mm f/3.5-5.6 IS STM");
        _lensTypeById.put(4147, "Canon EF-M 11-22mm f/4-5.6 IS STM");
        _lensTypeById.put(4148, "Canon EF-S 55-250mm f/4-5.6 IS STM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE_VALID), "Canon EF-M 55-200mm f/4.5-6.3 IS STM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE_START), "Canon EF-S 10-18mm f/4.5-5.6 IS STM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_AF_RESULT), "Canon EF 24-105mm f/3.5-5.6 IS STM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_CCD_SCAN_MODE), "Canon EF-M 15-45mm f/3.5-6.3 IS STM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_NOISE_REDUCTION), "Canon EF-S 24mm f/2.8 STM");
        _lensTypeById.put(Integer.valueOf((int) OlympusMakernoteDirectory.TAG_NEAR_LENS_STEP), "Canon EF 50mm f/1.8 STM");
        _lensTypeById.put(36912, "Canon EF-S 18-135mm f/3.5-5.6 IS USM");
        _lensTypeById.put(65535, DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION);
    }
}
