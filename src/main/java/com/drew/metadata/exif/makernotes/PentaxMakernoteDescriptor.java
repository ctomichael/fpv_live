package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class PentaxMakernoteDescriptor extends TagDescriptor<PentaxMakernoteDirectory> {
    public PentaxMakernoteDescriptor(@NotNull PentaxMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getCaptureModeDescription();
            case 2:
                return getQualityLevelDescription();
            case 3:
                return getFocusModeDescription();
            case 4:
                return getFlashModeDescription();
            case 5:
            case 6:
            case 8:
            case 9:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 22:
            default:
                return super.getDescription(tagType);
            case 7:
                return getWhiteBalanceDescription();
            case 10:
                return getDigitalZoomDescription();
            case 11:
                return getSharpnessDescription();
            case 12:
                return getContrastDescription();
            case 13:
                return getSaturationDescription();
            case 20:
                return getIsoSpeedDescription();
            case 23:
                return getColourDescription();
        }
    }

    @Nullable
    public String getColourDescription() {
        return getIndexedDescription(23, 1, "Normal", "Black & White", "Sepia");
    }

    @Nullable
    public String getIsoSpeedDescription() {
        Integer value = ((PentaxMakernoteDirectory) this._directory).getInteger(20);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 10:
                return "ISO 100";
            case 16:
                return "ISO 200";
            case 100:
                return "ISO 100";
            case 200:
                return "ISO 200";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getSaturationDescription() {
        return getIndexedDescription(13, "Normal", "Low", "High");
    }

    @Nullable
    public String getContrastDescription() {
        return getIndexedDescription(12, "Normal", "Low", "High");
    }

    @Nullable
    public String getSharpnessDescription() {
        return getIndexedDescription(11, "Normal", "Soft", "Hard");
    }

    @Nullable
    public String getDigitalZoomDescription() {
        Float value = ((PentaxMakernoteDirectory) this._directory).getFloatObject(10);
        if (value == null) {
            return null;
        }
        if (value.floatValue() == 0.0f) {
            return "Off";
        }
        return Float.toString(value.floatValue());
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        return getIndexedDescription(7, "Auto", "Daylight", "Shade", "Tungsten", "Fluorescent", "Manual");
    }

    @Nullable
    public String getFlashModeDescription() {
        return getIndexedDescription(4, 1, "Auto", "Flash On", null, "Flash Off", null, "Red-eye Reduction");
    }

    @Nullable
    public String getFocusModeDescription() {
        return getIndexedDescription(3, 2, "Custom", "Auto");
    }

    @Nullable
    public String getQualityLevelDescription() {
        return getIndexedDescription(2, "Good", "Better", "Best");
    }

    @Nullable
    public String getCaptureModeDescription() {
        return getIndexedDescription(1, "Auto", "Night-scene", "Manual", null, "Multiple");
    }
}
