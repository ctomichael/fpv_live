package com.drew.metadata.exif.makernotes;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class NikonType1MakernoteDescriptor extends TagDescriptor<NikonType1MakernoteDirectory> {
    public NikonType1MakernoteDescriptor(@NotNull NikonType1MakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 3:
                return getQualityDescription();
            case 4:
                return getColorModeDescription();
            case 5:
                return getImageAdjustmentDescription();
            case 6:
                return getCcdSensitivityDescription();
            case 7:
                return getWhiteBalanceDescription();
            case 8:
                return getFocusDescription();
            case 9:
            default:
                return super.getDescription(tagType);
            case 10:
                return getDigitalZoomDescription();
            case 11:
                return getConverterDescription();
        }
    }

    @Nullable
    public String getConverterDescription() {
        return getIndexedDescription(11, "None", "Fisheye converter");
    }

    @Nullable
    public String getDigitalZoomDescription() {
        Rational value = ((NikonType1MakernoteDirectory) this._directory).getRational(10);
        if (value == null) {
            return null;
        }
        return value.getNumerator() == 0 ? "No digital zoom" : value.toSimpleString(true) + "x digital zoom";
    }

    @Nullable
    public String getFocusDescription() {
        Rational value = ((NikonType1MakernoteDirectory) this._directory).getRational(8);
        if (value == null) {
            return null;
        }
        return (value.getNumerator() == 1 && value.getDenominator() == 0) ? "Infinite" : value.toSimpleString(true);
    }

    @Nullable
    public String getWhiteBalanceDescription() {
        return getIndexedDescription(7, "Auto", "Preset", "Daylight", "Incandescence", "Florescence", "Cloudy", "SpeedLight");
    }

    @Nullable
    public String getCcdSensitivityDescription() {
        return getIndexedDescription(6, "ISO80", null, "ISO160", null, "ISO320", "ISO100");
    }

    @Nullable
    public String getImageAdjustmentDescription() {
        return getIndexedDescription(5, "Normal", "Bright +", "Bright -", "Contrast +", "Contrast -");
    }

    @Nullable
    public String getColorModeDescription() {
        return getIndexedDescription(4, 1, "Color", "Monochrome");
    }

    @Nullable
    public String getQualityDescription() {
        return getIndexedDescription(3, 1, "VGA Basic", "VGA Normal", "VGA Fine", "SXGA Basic", "SXGA Normal", "SXGA Fine");
    }
}
