package com.drew.metadata.exif.makernotes;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import java.text.DecimalFormat;
import java.util.HashMap;

public class OlympusCameraSettingsMakernoteDescriptor extends TagDescriptor<OlympusCameraSettingsMakernoteDirectory> {
    private static final HashMap<Integer, String> _filters = new HashMap<>();
    private static final HashMap<Integer, String> _toneLevelType = new HashMap<>();

    public OlympusCameraSettingsMakernoteDescriptor(@NotNull OlympusCameraSettingsMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getCameraSettingsVersionDescription();
            case 256:
                return getPreviewImageValidDescription();
            case 512:
                return getExposureModeDescription();
            case 513:
                return getAeLockDescription();
            case 514:
                return getMeteringModeDescription();
            case 515:
                return getExposureShiftDescription();
            case 516:
                return getNdFilterDescription();
            case 768:
                return getMacroModeDescription();
            case 769:
                return getFocusModeDescription();
            case 770:
                return getFocusProcessDescription();
            case 771:
                return getAfSearchDescription();
            case 772:
                return getAfAreasDescription();
            case 773:
                return getAfPointSelectedDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagAfFineTune /*774*/:
                return getAfFineTuneDescription();
            case 1024:
                return getFlashModeDescription();
            case 1027:
                return getFlashRemoteControlDescription();
            case 1028:
                return getFlashControlModeDescription();
            case 1029:
                return getFlashIntensityDescription();
            case 1030:
                return getManualFlashStrengthDescription();
            case 1280:
                return getWhiteBalance2Description();
            case OlympusCameraSettingsMakernoteDirectory.TagWhiteBalanceTemperature /*1281*/:
                return getWhiteBalanceTemperatureDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagCustomSaturation /*1283*/:
                return getCustomSaturationDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagModifiedSaturation /*1284*/:
                return getModifiedSaturationDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagContrastSetting /*1285*/:
                return getContrastSettingDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagSharpnessSetting /*1286*/:
                return getSharpnessSettingDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagColorSpace /*1287*/:
                return getColorSpaceDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagSceneMode /*1289*/:
                return getSceneModeDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagNoiseReduction /*1290*/:
                return getNoiseReductionDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagDistortionCorrection /*1291*/:
                return getDistortionCorrectionDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagShadingCompensation /*1292*/:
                return getShadingCompensationDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagGradation /*1295*/:
                return getGradationDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPictureMode /*1312*/:
                return getPictureModeDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPictureModeSaturation /*1313*/:
                return getPictureModeSaturationDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPictureModeContrast /*1315*/:
                return getPictureModeContrastDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPictureModeSharpness /*1316*/:
                return getPictureModeSharpnessDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPictureModeBWFilter /*1317*/:
                return getPictureModeBWFilterDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPictureModeTone /*1318*/:
                return getPictureModeToneDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagNoiseFilter /*1319*/:
                return getNoiseFilterDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagArtFilter /*1321*/:
                return getArtFilterDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagMagicFilter /*1324*/:
                return getMagicFilterDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPictureModeEffect /*1325*/:
                return getPictureModeEffectDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagToneLevel /*1326*/:
                return getToneLevelDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagArtFilterEffect /*1327*/:
                return getArtFilterEffectDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagColorCreatorEffect /*1330*/:
                return getColorCreatorEffectDescription();
            case 1536:
                return getDriveModeDescription();
            case 1537:
                return getPanoramaModeDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagImageQuality2 /*1539*/:
                return getImageQuality2Description();
            case OlympusCameraSettingsMakernoteDirectory.TagImageStabilization /*1540*/:
                return getImageStabilizationDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagStackedImage /*2052*/:
                return getStackedImageDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagManometerPressure /*2304*/:
                return getManometerPressureDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagManometerReading /*2305*/:
                return getManometerReadingDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagExtendedWBDetect /*2306*/:
                return getExtendedWBDetectDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagRollAngle /*2307*/:
                return getRollAngleDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagPitchAngle /*2308*/:
                return getPitchAngleDescription();
            case OlympusCameraSettingsMakernoteDirectory.TagDateTimeUtc /*2312*/:
                return getDateTimeUTCDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getCameraSettingsVersionDescription() {
        return getVersionBytesDescription(0, 4);
    }

    @Nullable
    public String getPreviewImageValidDescription() {
        return getIndexedDescription(256, "No", "Yes");
    }

    @Nullable
    public String getExposureModeDescription() {
        return getIndexedDescription(512, 1, "Manual", "Program", "Aperture-priority AE", "Shutter speed priority", "Program-shift");
    }

    @Nullable
    public String getAeLockDescription() {
        return getIndexedDescription(513, "Off", "On");
    }

    @Nullable
    public String getMeteringModeDescription() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(514);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 2:
                return "Center-weighted average";
            case 3:
                return "Spot";
            case 5:
                return "ESP";
            case 261:
                return "Pattern+AF";
            case 515:
                return "Spot+Highlight control";
            case 1027:
                return "Spot+Shadow control";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getExposureShiftDescription() {
        return getRationalOrDoubleString(515);
    }

    @Nullable
    public String getNdFilterDescription() {
        return getIndexedDescription(516, "Off", "On");
    }

    @Nullable
    public String getMacroModeDescription() {
        return getIndexedDescription(768, "Off", "On", "Super Macro");
    }

    @Nullable
    public String getFocusModeDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(769);
        if (values == null) {
            Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(769);
            if (value == null) {
                return null;
            }
            values = new int[]{value.intValue()};
        }
        if (values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        switch (values[0]) {
            case 0:
                sb.append("Single AF");
                break;
            case 1:
                sb.append("Sequential shooting AF");
                break;
            case 2:
                sb.append("Continuous AF");
                break;
            case 3:
                sb.append("Multi AF");
                break;
            case 4:
                sb.append("Face detect");
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                sb.append("Unknown (" + values[0] + ")");
                break;
            case 10:
                sb.append("MF");
                break;
        }
        if (values.length > 1) {
            sb.append("; ");
            int value1 = values[1];
            if (value1 == 0) {
                sb.append("(none)");
            } else {
                if ((value1 & 1) > 0) {
                    sb.append("S-AF, ");
                }
                if (((value1 >> 2) & 1) > 0) {
                    sb.append("C-AF, ");
                }
                if (((value1 >> 4) & 1) > 0) {
                    sb.append("MF, ");
                }
                if (((value1 >> 5) & 1) > 0) {
                    sb.append("Face detect, ");
                }
                if (((value1 >> 6) & 1) > 0) {
                    sb.append("Imager AF, ");
                }
                if (((value1 >> 7) & 1) > 0) {
                    sb.append("Live View Magnification Frame, ");
                }
                if (((value1 >> 8) & 1) > 0) {
                    sb.append("AF sensor, ");
                }
                sb.setLength(sb.length() - 2);
            }
        }
        return sb.toString();
    }

    @Nullable
    public String getFocusProcessDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(770);
        if (values == null) {
            Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(770);
            if (value == null) {
                return null;
            }
            values = new int[]{value.intValue()};
        }
        if (values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        switch (values[0]) {
            case 0:
                sb.append("AF not used");
                break;
            case 1:
                sb.append("AF used");
                break;
            default:
                sb.append("Unknown (" + values[0] + ")");
                break;
        }
        if (values.length > 1) {
            sb.append("; " + values[1]);
        }
        return sb.toString();
    }

    @Nullable
    public String getAfSearchDescription() {
        return getIndexedDescription(771, "Not Ready", "Ready");
    }

    @Nullable
    public String getAfAreasDescription() {
        Object obj = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getObject(772);
        if (obj == null || !(obj instanceof long[])) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        long[] arr$ = (long[]) obj;
        for (long point : arr$) {
            if (point != 0) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                if (point == 913916549) {
                    sb.append("Left ");
                } else if (point == 2038007173) {
                    sb.append("Center ");
                } else if (point == 3178875269L) {
                    sb.append("Right ");
                }
                sb.append(String.format("(%d/255,%d/255)-(%d/255,%d/255)", Long.valueOf((point >> 24) & 255), Long.valueOf((point >> 16) & 255), Long.valueOf((point >> 8) & 255), Long.valueOf(255 & point)));
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    @Nullable
    public String getAfPointSelectedDescription() {
        Rational[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getRationalArray(773);
        if (values == null) {
            return "n/a";
        }
        if (values.length < 4) {
            return null;
        }
        int index = 0;
        if (values.length == 5 && values[0].longValue() == 0) {
            index = 1;
        }
        int p1 = (int) (values[index].doubleValue() * 100.0d);
        int p2 = (int) (values[index + 1].doubleValue() * 100.0d);
        int p3 = (int) (values[index + 2].doubleValue() * 100.0d);
        int p4 = (int) (values[index + 3].doubleValue() * 100.0d);
        if (p1 + p2 + p3 + p4 == 0) {
            return "n/a";
        }
        return String.format("(%d%%,%d%%) (%d%%,%d%%)", Integer.valueOf(p1), Integer.valueOf(p2), Integer.valueOf(p3), Integer.valueOf(p4));
    }

    @Nullable
    public String getAfFineTuneDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagAfFineTune, "Off", "On");
    }

    @Nullable
    public String getFlashModeDescription() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(1024);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 0) {
            return "Off";
        }
        StringBuilder sb = new StringBuilder();
        int v = value.intValue();
        if ((v & 1) != 0) {
            sb.append("On, ");
        }
        if (((v >> 1) & 1) != 0) {
            sb.append("Fill-in, ");
        }
        if (((v >> 2) & 1) != 0) {
            sb.append("Red-eye, ");
        }
        if (((v >> 3) & 1) != 0) {
            sb.append("Slow-sync, ");
        }
        if (((v >> 4) & 1) != 0) {
            sb.append("Forced On, ");
        }
        if (((v >> 5) & 1) != 0) {
            sb.append("2nd Curtain, ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getFlashRemoteControlDescription() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(1027);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Off";
            case 1:
                return "Channel 1, Low";
            case 2:
                return "Channel 2, Low";
            case 3:
                return "Channel 3, Low";
            case 4:
                return "Channel 4, Low";
            case 5:
            case 6:
            case 7:
            case 8:
            case 13:
            case 14:
            case 15:
            case 16:
            default:
                return "Unknown (" + value + ")";
            case 9:
                return "Channel 1, Mid";
            case 10:
                return "Channel 2, Mid";
            case 11:
                return "Channel 3, Mid";
            case 12:
                return "Channel 4, Mid";
            case 17:
                return "Channel 1, High";
            case 18:
                return "Channel 2, High";
            case 19:
                return "Channel 3, High";
            case 20:
                return "Channel 4, High";
        }
    }

    @Nullable
    public String getFlashControlModeDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(1028);
        if (values == null) {
            return null;
        }
        if (values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        switch (values[0]) {
            case 0:
                sb.append("Off");
                break;
            case 1:
            case 2:
            default:
                sb.append("Unknown (").append(values[0]).append(")");
                break;
            case 3:
                sb.append("TTL");
                break;
            case 4:
                sb.append("Auto");
                break;
            case 5:
                sb.append("Manual");
                break;
        }
        for (int i = 1; i < values.length; i++) {
            sb.append("; ").append(values[i]);
        }
        return sb.toString();
    }

    @Nullable
    public String getFlashIntensityDescription() {
        Rational[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getRationalArray(1029);
        if (values == null || values.length == 0) {
            return null;
        }
        if (values.length == 3) {
            if (values[0].getDenominator() == 0 && values[1].getDenominator() == 0 && values[2].getDenominator() == 0) {
                return "n/a";
            }
        } else if (values.length == 4 && values[0].getDenominator() == 0 && values[1].getDenominator() == 0 && values[2].getDenominator() == 0 && values[3].getDenominator() == 0) {
            return "n/a (x4)";
        }
        StringBuilder sb = new StringBuilder();
        for (Rational t : values) {
            sb.append(t).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getManualFlashStrengthDescription() {
        Rational[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getRationalArray(1030);
        if (values == null || values.length == 0) {
            return "n/a";
        }
        if (values.length == 3) {
            if (values[0].getDenominator() == 0 && values[1].getDenominator() == 0 && values[2].getDenominator() == 0) {
                return "n/a";
            }
        } else if (values.length == 4 && values[0].getDenominator() == 0 && values[1].getDenominator() == 0 && values[2].getDenominator() == 0 && values[3].getDenominator() == 0) {
            return "n/a (x4)";
        }
        StringBuilder sb = new StringBuilder();
        for (Rational t : values) {
            sb.append(t).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getWhiteBalance2Description() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(1280);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Auto";
            case 1:
                return "Auto (Keep Warm Color Off)";
            case 16:
                return "7500K (Fine Weather with Shade)";
            case 17:
                return "6000K (Cloudy)";
            case 18:
                return "5300K (Fine Weather)";
            case 20:
                return "3000K (Tungsten light)";
            case 21:
                return "3600K (Tungsten light-like)";
            case 22:
                return "Auto Setup";
            case 23:
                return "5500K (Flash)";
            case 33:
                return "6600K (Daylight fluorescent)";
            case 34:
                return "4500K (Neutral white fluorescent)";
            case 35:
                return "4000K (Cool white fluorescent)";
            case 36:
                return "White Fluorescent";
            case 48:
                return "3600K (Tungsten light-like)";
            case 67:
                return "Underwater";
            case 256:
                return "One Touch WB 1";
            case 257:
                return "One Touch WB 2";
            case 258:
                return "One Touch WB 3";
            case 259:
                return "One Touch WB 4";
            case 512:
                return "Custom WB 1";
            case 513:
                return "Custom WB 2";
            case 514:
                return "Custom WB 3";
            case 515:
                return "Custom WB 4";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getWhiteBalanceTemperatureDescription() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(OlympusCameraSettingsMakernoteDirectory.TagWhiteBalanceTemperature);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 0) {
            return "Auto";
        }
        return value.toString();
    }

    @Nullable
    public String getCustomSaturationDescription() {
        return getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagCustomSaturation);
    }

    @Nullable
    public String getModifiedSaturationDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagModifiedSaturation, "Off", "CM1 (Red Enhance)", "CM2 (Green Enhance)", "CM3 (Blue Enhance)", "CM4 (Skin Tones)");
    }

    @Nullable
    public String getContrastSettingDescription() {
        return getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagContrastSetting);
    }

    @Nullable
    public String getSharpnessSettingDescription() {
        return getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagSharpnessSetting);
    }

    @Nullable
    public String getColorSpaceDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagColorSpace, "sRGB", "Adobe RGB", "Pro Photo RGB");
    }

    @Nullable
    public String getSceneModeDescription() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(OlympusCameraSettingsMakernoteDirectory.TagSceneMode);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Standard";
            case 6:
                return "Auto";
            case 7:
                return "Sport";
            case 8:
                return "Portrait";
            case 9:
                return "Landscape+Portrait";
            case 10:
                return "Landscape";
            case 11:
                return "Night Scene";
            case 12:
                return "Self Portrait";
            case 13:
                return "Panorama";
            case 14:
                return "2 in 1";
            case 15:
                return "Movie";
            case 16:
                return "Landscape+Portrait";
            case 17:
                return "Night+Portrait";
            case 18:
                return "Indoor";
            case 19:
                return "Fireworks";
            case 20:
                return "Sunset";
            case 21:
                return "Beauty Skin";
            case 22:
                return "Macro";
            case 23:
                return "Super Macro";
            case 24:
                return "Food";
            case 25:
                return "Documents";
            case 26:
                return "Museum";
            case 27:
                return "Shoot & Select";
            case 28:
                return "Beach & Snow";
            case 29:
                return "Self Portrait+Timer";
            case 30:
                return "Candle";
            case 31:
                return "Available Light";
            case 32:
                return "Behind Glass";
            case 33:
                return "My Mode";
            case 34:
                return "Pet";
            case 35:
                return "Underwater Wide1";
            case 36:
                return "Underwater Macro";
            case 37:
                return "Shoot & Select1";
            case 38:
                return "Shoot & Select2";
            case 39:
                return "High Key";
            case 40:
                return "Digital Image Stabilization";
            case 41:
                return "Auction";
            case 42:
                return "Beach";
            case 43:
                return "Snow";
            case 44:
                return "Underwater Wide2";
            case 45:
                return "Low Key";
            case 46:
                return "Children";
            case 47:
                return "Vivid";
            case 48:
                return "Nature Macro";
            case 49:
                return "Underwater Snapshot";
            case 50:
                return "Shooting Guide";
            case 54:
                return "Face Portrait";
            case 57:
                return "Bulb";
            case 59:
                return "Smile Shot";
            case 60:
                return "Quick Shutter";
            case 63:
                return "Slow Shutter";
            case 64:
                return "Bird Watching";
            case 65:
                return "Multiple Exposure";
            case 66:
                return "e-Portrait";
            case 67:
                return "Soft Background Shot";
            case 142:
                return "Hand-held Starlight";
            case 154:
                return "HDR";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getNoiseReductionDescription() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(OlympusCameraSettingsMakernoteDirectory.TagNoiseReduction);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 0) {
            return "(none)";
        }
        StringBuilder sb = new StringBuilder();
        int v = value.intValue();
        if ((v & 1) != 0) {
            sb.append("Noise Reduction, ");
        }
        if (((v >> 1) & 1) != 0) {
            sb.append("Noise Filter, ");
        }
        if (((v >> 2) & 1) != 0) {
            sb.append("Noise Filter (ISO Boost), ");
        }
        if (((v >> 3) & 1) != 0) {
            sb.append("Auto, ");
        }
        return sb.length() != 0 ? sb.substring(0, sb.length() - 2) : "(none)";
    }

    @Nullable
    public String getDistortionCorrectionDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagDistortionCorrection, "Off", "On");
    }

    @Nullable
    public String getShadingCompensationDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagShadingCompensation, "Off", "On");
    }

    @Nullable
    public String getGradationDescription() {
        String ret;
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagGradation);
        if (values == null || values.length < 3) {
            return null;
        }
        String join = String.format("%d %d %d", Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
        if (join.equals("0 0 0")) {
            ret = "n/a";
        } else if (join.equals("-1 -1 1")) {
            ret = "Low Key";
        } else if (join.equals("0 -1 1")) {
            ret = "Normal";
        } else if (join.equals("1 -1 1")) {
            ret = "High Key";
        } else {
            ret = "Unknown (" + join + ")";
        }
        if (values.length <= 3) {
            return ret;
        }
        if (values[3] == 0) {
            return ret + "; User-Selected";
        }
        if (values[3] == 1) {
            return ret + "; Auto-Override";
        }
        return ret;
    }

    @Nullable
    public String getPictureModeDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagPictureMode);
        if (values == null) {
            Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(OlympusCameraSettingsMakernoteDirectory.TagNoiseReduction);
            if (value == null) {
                return null;
            }
            values = new int[]{value.intValue()};
        }
        if (values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        switch (values[0]) {
            case 1:
                sb.append("Vivid");
                break;
            case 2:
                sb.append("Natural");
                break;
            case 3:
                sb.append("Muted");
                break;
            case 4:
                sb.append("Portrait");
                break;
            case 5:
                sb.append("i-Enhance");
                break;
            case 256:
                sb.append("Monotone");
                break;
            case 512:
                sb.append("Sepia");
                break;
            default:
                sb.append("Unknown (").append(values[0]).append(")");
                break;
        }
        if (values.length > 1) {
            sb.append("; ").append(values[1]);
        }
        return sb.toString();
    }

    @Nullable
    public String getPictureModeSaturationDescription() {
        return getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeSaturation);
    }

    @Nullable
    public String getPictureModeContrastDescription() {
        return getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeContrast);
    }

    @Nullable
    public String getPictureModeSharpnessDescription() {
        return getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeSharpness);
    }

    @Nullable
    public String getPictureModeBWFilterDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeBWFilter, "n/a", "Neutral", "Yellow", "Orange", "Red", "Green");
    }

    @Nullable
    public String getPictureModeToneDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeTone, "n/a", "Neutral", "Sepia", "Blue", "Purple", "Green");
    }

    @Nullable
    public String getNoiseFilterDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagNoiseFilter);
        if (values == null) {
            return null;
        }
        String join = String.format("%d %d %d", Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
        if (join.equals("0 0 0")) {
            return "n/a";
        }
        if (join.equals("-2 -2 1")) {
            return "Off";
        }
        if (join.equals("-1 -2 1")) {
            return "Low";
        }
        if (join.equals("0 -2 1")) {
            return "Standard";
        }
        if (join.equals("1 -2 1")) {
            return "High";
        }
        return "Unknown (" + join + ")";
    }

    @Nullable
    public String getArtFilterDescription() {
        return getFiltersDescription(OlympusCameraSettingsMakernoteDirectory.TagArtFilter);
    }

    @Nullable
    public String getMagicFilterDescription() {
        return getFiltersDescription(OlympusCameraSettingsMakernoteDirectory.TagMagicFilter);
    }

    @Nullable
    public String getPictureModeEffectDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagPictureModeEffect);
        if (values == null) {
            return null;
        }
        String key = String.format("%d %d %d", Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
        if (key.equals("0 0 0")) {
            return "n/a";
        }
        if (key.equals("-1 -1 1")) {
            return "Low";
        }
        if (key.equals("0 -1 1")) {
            return "Standard";
        }
        if (key.equals("1 -1 1")) {
            return "High";
        }
        return "Unknown (" + key + ")";
    }

    @Nullable
    public String getToneLevelDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagToneLevel);
        if (values == null || values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == 0 || i == 4 || i == 8 || i == 12 || i == 16 || i == 20 || i == 24) {
                sb.append(_toneLevelType.get(Integer.valueOf(values[i]))).append("; ");
            } else {
                sb.append(values[i]).append("; ");
            }
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getArtFilterEffectDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagArtFilterEffect);
        if (values == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                sb.append(_filters.containsKey(Integer.valueOf(values[i])) ? _filters.get(Integer.valueOf(values[i])) : "[unknown]").append("; ");
            } else if (i == 3) {
                sb.append("Partial Color ").append(values[i]).append("; ");
            } else if (i == 4) {
                switch (values[i]) {
                    case 0:
                        sb.append("No Effect");
                        break;
                    case PanasonicMakernoteDirectory.TAG_BABY_AGE_1 /*32784*/:
                        sb.append("Star Light");
                        break;
                    case 32800:
                        sb.append("Pin Hole");
                        break;
                    case 32816:
                        sb.append("Frame");
                        break;
                    case 32832:
                        sb.append("Soft Focus");
                        break;
                    case 32848:
                        sb.append("White Edge");
                        break;
                    case 32864:
                        sb.append("B&W");
                        break;
                    default:
                        sb.append("Unknown (").append(values[i]).append(")");
                        break;
                }
                sb.append("; ");
            } else if (i == 6) {
                switch (values[i]) {
                    case 0:
                        sb.append("No Color Filter");
                        break;
                    case 1:
                        sb.append("Yellow Color Filter");
                        break;
                    case 2:
                        sb.append("Orange Color Filter");
                        break;
                    case 3:
                        sb.append("Red Color Filter");
                        break;
                    case 4:
                        sb.append("Green Color Filter");
                        break;
                    default:
                        sb.append("Unknown (").append(values[i]).append(")");
                        break;
                }
                sb.append("; ");
            } else {
                sb.append(values[i]).append("; ");
            }
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getColorCreatorEffectDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagColorCreatorEffect);
        if (values == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                sb.append("Color ").append(values[i]).append("; ");
            } else if (i == 3) {
                sb.append("Strength ").append(values[i]).append("; ");
            } else {
                sb.append(values[i]).append("; ");
            }
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getDriveModeDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(1536);
        if (values == null) {
            return null;
        }
        if (values.length == 0 || values[0] == 0) {
            return "Single Shot";
        }
        StringBuilder a = new StringBuilder();
        if (values[0] != 5 || values.length < 3) {
            switch (values[0]) {
                case 1:
                    a.append("Continuous Shooting");
                    break;
                case 2:
                    a.append("Exposure Bracketing");
                    break;
                case 3:
                    a.append("White Balance Bracketing");
                    break;
                case 4:
                    a.append("Exposure+WB Bracketing");
                    break;
                default:
                    a.append("Unknown (").append(values[0]).append(")");
                    break;
            }
        } else {
            int c = values[2];
            if ((c & 1) > 0) {
                a.append("AE");
            }
            if (((c >> 1) & 1) > 0) {
                a.append("WB");
            }
            if (((c >> 2) & 1) > 0) {
                a.append("FL");
            }
            if (((c >> 3) & 1) > 0) {
                a.append("MF");
            }
            if (((c >> 6) & 1) > 0) {
                a.append("Focus");
            }
            a.append(" Bracketing");
        }
        a.append(", Shot ").append(values[1]);
        return a.toString();
    }

    @Nullable
    public String getPanoramaModeDescription() {
        String a;
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(1537);
        if (values == null) {
            return null;
        }
        if (values.length == 0 || values[0] == 0) {
            return "Off";
        }
        switch (values[0]) {
            case 1:
                a = "Left to Right";
                break;
            case 2:
                a = "Right to Left";
                break;
            case 3:
                a = "Bottom to Top";
                break;
            case 4:
                a = "Top to Bottom";
                break;
            default:
                a = "Unknown (" + values[0] + ")";
                break;
        }
        return String.format("%s, Shot %d", a, Integer.valueOf(values[1]));
    }

    @Nullable
    public String getImageQuality2Description() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagImageQuality2, 1, "SQ", "HQ", "SHQ", "RAW", "SQ (5)");
    }

    @Nullable
    public String getImageStabilizationDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagImageStabilization, "Off", "On, Mode 1", "On, Mode 2", "On, Mode 3", "On, Mode 4");
    }

    @Nullable
    public String getStackedImageDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagStackedImage);
        if (values == null || values.length < 2) {
            return null;
        }
        int v1 = values[0];
        int v2 = values[1];
        if (v1 == 0 && v2 == 0) {
            return "No";
        }
        if (v1 == 9 && v2 == 8) {
            return "Focus-stacked (8 images)";
        }
        return String.format("Unknown (%d %d)", Integer.valueOf(v1), Integer.valueOf(v2));
    }

    @Nullable
    public String getManometerPressureDescription() {
        Integer value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getInteger(OlympusCameraSettingsMakernoteDirectory.TagManometerPressure);
        if (value == null) {
            return null;
        }
        return String.format("%s kPa", new DecimalFormat("#.##").format(((double) value.intValue()) / 10.0d));
    }

    @Nullable
    public String getManometerReadingDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagManometerReading);
        if (values == null || values.length < 2) {
            return null;
        }
        DecimalFormat format = new DecimalFormat("#.##");
        return String.format("%s m, %s ft", format.format(((double) values[0]) / 10.0d), format.format(((double) values[1]) / 10.0d));
    }

    @Nullable
    public String getExtendedWBDetectDescription() {
        return getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagExtendedWBDetect, "Off", "On");
    }

    @Nullable
    public String getRollAngleDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagRollAngle);
        if (values == null || values.length < 2) {
            return null;
        }
        return String.format("%s %d", values[0] != 0 ? Double.toString(((double) (-values[0])) / 10.0d) : "n/a", Integer.valueOf(values[1]));
    }

    @Nullable
    public String getPitchAngleDescription() {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(OlympusCameraSettingsMakernoteDirectory.TagPitchAngle);
        if (values == null || values.length < 2) {
            return null;
        }
        return String.format("%s %d", values[0] != 0 ? Double.toString(((double) values[0]) / 10.0d) : "n/a", Integer.valueOf(values[1]));
    }

    @Nullable
    public String getDateTimeUTCDescription() {
        Object value = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getObject(OlympusCameraSettingsMakernoteDirectory.TagDateTimeUtc);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Nullable
    private String getValueMinMaxDescription(int tagId) {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(tagId);
        if (values == null || values.length < 3) {
            return null;
        }
        return String.format("%d (min %d, max %d)", Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
    }

    @Nullable
    private String getFiltersDescription(int tagId) {
        int[] values = ((OlympusCameraSettingsMakernoteDirectory) this._directory).getIntArray(tagId);
        if (values == null || values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                sb.append(_filters.containsKey(Integer.valueOf(values[i])) ? _filters.get(Integer.valueOf(values[i])) : "[unknown]");
            } else {
                sb.append(values[i]);
            }
            sb.append("; ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    static {
        _filters.put(0, "Off");
        _filters.put(1, "Soft Focus");
        _filters.put(2, "Pop Art");
        _filters.put(3, "Pale & Light Color");
        _filters.put(4, "Light Tone");
        _filters.put(5, "Pin Hole");
        _filters.put(6, "Grainy Film");
        _filters.put(9, "Diorama");
        _filters.put(10, "Cross Process");
        _filters.put(12, "Fish Eye");
        _filters.put(13, "Drawing");
        _filters.put(14, "Gentle Sepia");
        _filters.put(15, "Pale & Light Color II");
        _filters.put(16, "Pop Art II");
        _filters.put(17, "Pin Hole II");
        _filters.put(18, "Pin Hole III");
        _filters.put(19, "Grainy Film II");
        _filters.put(20, "Dramatic Tone");
        _filters.put(21, "Punk");
        _filters.put(22, "Soft Focus 2");
        _filters.put(23, "Sparkle");
        _filters.put(24, "Watercolor");
        _filters.put(25, "Key Line");
        _filters.put(26, "Key Line II");
        _filters.put(27, "Miniature");
        _filters.put(28, "Reflection");
        _filters.put(29, "Fragmented");
        _filters.put(31, "Cross Process II");
        _filters.put(32, "Dramatic Tone II");
        _filters.put(33, "Watercolor I");
        _filters.put(34, "Watercolor II");
        _filters.put(35, "Diorama II");
        _filters.put(36, "Vintage");
        _filters.put(37, "Vintage II");
        _filters.put(38, "Vintage III");
        _filters.put(39, "Partial Color");
        _filters.put(40, "Partial Color II");
        _filters.put(41, "Partial Color III");
        _toneLevelType.put(0, "0");
        _toneLevelType.put(-31999, "Highlights ");
        _toneLevelType.put(-31998, "Shadows ");
        _toneLevelType.put(-31997, "Midtones ");
    }
}
