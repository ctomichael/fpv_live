package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class KodakMakernoteDescriptor extends TagDescriptor<KodakMakernoteDirectory> {
    public KodakMakernoteDescriptor(@NotNull KodakMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 9:
                return getQualityDescription();
            case 10:
                return getBurstModeDescription();
            case 27:
                return getShutterModeDescription();
            case 56:
                return getFocusModeDescription();
            case 64:
                return getWhiteBalanceDescription();
            case 92:
                return getFlashModeDescription();
            case 93:
                return getFlashFiredDescription();
            case 102:
                return getColorModeDescription();
            case 107:
                return getSharpnessDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getSharpnessDescription() {
        return getIndexedDescription(107, "Normal");
    }

    @Nullable
    public String getColorModeDescription() {
        Integer value = ((KodakMakernoteDirectory) this._directory).getInteger(102);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 1:
            case 8192:
                return "B&W";
            case 2:
            case 16384:
                return "Sepia";
            case 3:
                return "B&W Yellow Filter";
            case 4:
                return "B&W Red Filter";
            case 32:
                return "Saturated Color";
            case 64:
            case 512:
                return "Neutral Color";
            case 256:
                return "Saturated Color";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getFlashFiredDescription() {
        return getIndexedDescription(93, "No", "Yes");
    }

    @Nullable
    public String getFlashModeDescription() {
        Integer value = ((KodakMakernoteDirectory) this._directory).getInteger(92);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Auto";
            case 1:
            case 16:
                return "Fill Flash";
            case 2:
            case 32:
                return "Off";
            case 3:
            case 64:
                return "Red Eye";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        return getIndexedDescription(64, "Auto", "Flash", "Tungsten", "Daylight");
    }

    @Nullable
    public String getFocusModeDescription() {
        return getIndexedDescription(56, "Normal", null, "Macro");
    }

    @Nullable
    public String getShutterModeDescription() {
        Integer value = ((KodakMakernoteDirectory) this._directory).getInteger(27);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Auto";
            case 8:
                return "Aperture Priority";
            case 32:
                return "Manual";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getBurstModeDescription() {
        return getIndexedDescription(10, "Off", "On");
    }

    @Nullable
    public String getQualityDescription() {
        return getIndexedDescription(9, 1, "Fine", "Normal");
    }
}
