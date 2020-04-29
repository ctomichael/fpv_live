package com.drew.metadata.exif.makernotes;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import java.text.DecimalFormat;

public class NikonType2MakernoteDescriptor extends TagDescriptor<NikonType2MakernoteDirectory> {
    public NikonType2MakernoteDescriptor(@NotNull NikonType2MakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getFirmwareVersionDescription();
            case 2:
                return getIsoSettingDescription();
            case 13:
                return getProgramShiftDescription();
            case 14:
                return getExposureDifferenceDescription();
            case 18:
                return getAutoFlashCompensationDescription();
            case 23:
                return getFlashExposureCompensationDescription();
            case 24:
                return getFlashBracketCompensationDescription();
            case 28:
                return getExposureTuningDescription();
            case 30:
                return getColorSpaceDescription();
            case 34:
                return getActiveDLightingDescription();
            case 42:
                return getVignetteControlDescription();
            case 131:
                return getLensTypeDescription();
            case 132:
                return getLensDescription();
            case 134:
                return getDigitalZoomDescription();
            case 135:
                return getFlashUsedDescription();
            case 136:
                return getAutoFocusPositionDescription();
            case 137:
                return getShootingModeDescription();
            case 139:
                return getLensStopsDescription();
            case 141:
                return getColorModeDescription();
            case 146:
                return getHueAdjustmentDescription();
            case 147:
                return getNEFCompressionDescription();
            case 177:
                return getHighISONoiseReductionDescription();
            case 182:
                return getPowerUpTimeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getPowerUpTimeDescription() {
        return getEpochTimeDescription(182);
    }

    @Nullable
    public String getHighISONoiseReductionDescription() {
        return getIndexedDescription(177, "Off", "Minimal", "Low", null, "Normal", null, "High");
    }

    @Nullable
    public String getFlashUsedDescription() {
        return getIndexedDescription(135, "Flash Not Used", "Manual Flash", null, "Flash Not Ready", null, null, null, "External Flash", "Fired, Commander Mode", "Fired, TTL Mode");
    }

    @Nullable
    public String getNEFCompressionDescription() {
        return getIndexedDescription(147, 1, "Lossy (Type 1)", null, "Uncompressed", null, null, null, "Lossless", "Lossy (Type 2)");
    }

    @Nullable
    public String getShootingModeDescription() {
        return getBitFlagDescription(137, new String[]{"Single Frame", "Continuous"}, "Delay", null, "PC Control", "Exposure Bracketing", "Auto ISO", "White-Balance Bracketing", "IR Control");
    }

    @Nullable
    public String getLensTypeDescription() {
        return getBitFlagDescription(131, new String[]{"AF", "MF"}, "D", "G", "VR");
    }

    @Nullable
    public String getColorSpaceDescription() {
        return getIndexedDescription(30, 1, "sRGB", "Adobe RGB");
    }

    @Nullable
    public String getActiveDLightingDescription() {
        Integer value = ((NikonType2MakernoteDirectory) this._directory).getInteger(34);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Off";
            case 1:
                return "Light";
            case 3:
                return "Normal";
            case 5:
                return "High";
            case 7:
                return "Extra High";
            case 65535:
                return "Auto";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getVignetteControlDescription() {
        Integer value = ((NikonType2MakernoteDirectory) this._directory).getInteger(42);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Off";
            case 1:
                return "Low";
            case 2:
            case 4:
            default:
                return "Unknown (" + value + ")";
            case 3:
                return "Normal";
            case 5:
                return "High";
        }
    }

    @Nullable
    public String getAutoFocusPositionDescription() {
        int[] values = ((NikonType2MakernoteDirectory) this._directory).getIntArray(136);
        if (values == null) {
            return null;
        }
        if (values.length != 4 || values[0] != 0 || values[2] != 0 || values[3] != 0) {
            return "Unknown (" + ((NikonType2MakernoteDirectory) this._directory).getString(136) + ")";
        }
        switch (values[1]) {
            case 0:
                return "Centre";
            case 1:
                return "Top";
            case 2:
                return "Bottom";
            case 3:
                return "Left";
            case 4:
                return "Right";
            default:
                return "Unknown (" + values[1] + ")";
        }
    }

    @Nullable
    public String getDigitalZoomDescription() {
        Rational value = ((NikonType2MakernoteDirectory) this._directory).getRational(134);
        if (value == null) {
            return null;
        }
        return value.intValue() == 1 ? "No digital zoom" : value.toSimpleString(true) + "x digital zoom";
    }

    @Nullable
    public String getProgramShiftDescription() {
        return getEVDescription(13);
    }

    @Nullable
    public String getExposureDifferenceDescription() {
        return getEVDescription(14);
    }

    @Nullable
    public String getAutoFlashCompensationDescription() {
        return getEVDescription(18);
    }

    @Nullable
    public String getFlashExposureCompensationDescription() {
        return getEVDescription(23);
    }

    @Nullable
    public String getFlashBracketCompensationDescription() {
        return getEVDescription(24);
    }

    @Nullable
    public String getExposureTuningDescription() {
        return getEVDescription(28);
    }

    @Nullable
    public String getLensStopsDescription() {
        return getEVDescription(139);
    }

    @Nullable
    private String getEVDescription(int tagType) {
        int[] values = ((NikonType2MakernoteDirectory) this._directory).getIntArray(tagType);
        if (values == null || values.length < 2) {
            return null;
        }
        if (values.length < 3 || values[2] == 0) {
            return null;
        }
        return new DecimalFormat("0.##").format(((double) (values[0] * values[1])) / ((double) values[2])) + " EV";
    }

    @Nullable
    public String getIsoSettingDescription() {
        int[] values = ((NikonType2MakernoteDirectory) this._directory).getIntArray(2);
        if (values == null) {
            return null;
        }
        if (values[0] != 0 || values[1] == 0) {
            return "Unknown (" + ((NikonType2MakernoteDirectory) this._directory).getString(2) + ")";
        }
        return "ISO " + values[1];
    }

    @Nullable
    public String getLensDescription() {
        return getLensSpecificationDescription(132);
    }

    @Nullable
    public String getHueAdjustmentDescription() {
        return getFormattedString(146, "%s degrees");
    }

    @Nullable
    public String getColorModeDescription() {
        String value = ((NikonType2MakernoteDirectory) this._directory).getString(141);
        if (value == null) {
            return null;
        }
        return value.startsWith("MODE1") ? "Mode I (sRGB)" : value;
    }

    @Nullable
    public String getFirmwareVersionDescription() {
        return getVersionBytesDescription(1, 2);
    }
}
