package com.drew.metadata.exif;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class PanasonicRawDistortionDescriptor extends TagDescriptor<PanasonicRawDistortionDirectory> {
    public PanasonicRawDistortionDescriptor(@NotNull PanasonicRawDistortionDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 2:
                return getDistortionParam02Description();
            case 3:
            case 6:
            case 10:
            default:
                return super.getDescription(tagType);
            case 4:
                return getDistortionParam04Description();
            case 5:
                return getDistortionScaleDescription();
            case 7:
                return getDistortionCorrectionDescription();
            case 8:
                return getDistortionParam08Description();
            case 9:
                return getDistortionParam09Description();
            case 11:
                return getDistortionParam11Description();
        }
    }

    @Nullable
    public String getWbTypeDescription(int tagType) {
        Integer wbtype = ((PanasonicRawDistortionDirectory) this._directory).getInteger(tagType);
        if (wbtype == null) {
            return null;
        }
        return super.getLightSourceDescription(wbtype.shortValue());
    }

    @Nullable
    public String getDistortionParam02Description() {
        Integer value = ((PanasonicRawDistortionDirectory) this._directory).getInteger(2);
        if (value == null) {
            return null;
        }
        return new Rational((long) value.intValue(), 32678).toString();
    }

    @Nullable
    public String getDistortionParam04Description() {
        Integer value = ((PanasonicRawDistortionDirectory) this._directory).getInteger(4);
        if (value == null) {
            return null;
        }
        return new Rational((long) value.intValue(), 32678).toString();
    }

    @Nullable
    public String getDistortionScaleDescription() {
        Integer value = ((PanasonicRawDistortionDirectory) this._directory).getInteger(5);
        if (value == null) {
            return null;
        }
        return Integer.toString(1 / ((value.intValue() / 32768) + 1));
    }

    @Nullable
    public String getDistortionCorrectionDescription() {
        Integer value = ((PanasonicRawDistortionDirectory) this._directory).getInteger(7);
        if (value == null) {
            return null;
        }
        switch (value.intValue() & 15) {
            case 0:
                return "Off";
            case 1:
                return "On";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getDistortionParam08Description() {
        Integer value = ((PanasonicRawDistortionDirectory) this._directory).getInteger(8);
        if (value == null) {
            return null;
        }
        return new Rational((long) value.intValue(), 32678).toString();
    }

    @Nullable
    public String getDistortionParam09Description() {
        Integer value = ((PanasonicRawDistortionDirectory) this._directory).getInteger(9);
        if (value == null) {
            return null;
        }
        return new Rational((long) value.intValue(), 32678).toString();
    }

    @Nullable
    public String getDistortionParam11Description() {
        Integer value = ((PanasonicRawDistortionDirectory) this._directory).getInteger(11);
        if (value == null) {
            return null;
        }
        return new Rational((long) value.intValue(), 32678).toString();
    }
}
