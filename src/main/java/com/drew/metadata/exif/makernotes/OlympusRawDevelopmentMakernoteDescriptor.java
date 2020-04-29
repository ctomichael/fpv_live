package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class OlympusRawDevelopmentMakernoteDescriptor extends TagDescriptor<OlympusRawDevelopmentMakernoteDirectory> {
    public OlympusRawDevelopmentMakernoteDescriptor(@NotNull OlympusRawDevelopmentMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getRawDevVersionDescription();
            case 264:
                return getRawDevColorSpaceDescription();
            case 265:
                return getRawDevEngineDescription();
            case 266:
                return getRawDevNoiseReductionDescription();
            case 267:
                return getRawDevEditStatusDescription();
            case 268:
                return getRawDevSettingsDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getRawDevVersionDescription() {
        return getVersionBytesDescription(0, 4);
    }

    @Nullable
    public String getRawDevColorSpaceDescription() {
        return getIndexedDescription(264, "sRGB", "Adobe RGB", "Pro Photo RGB");
    }

    @Nullable
    public String getRawDevEngineDescription() {
        return getIndexedDescription(265, "High Speed", "High Function", "Advanced High Speed", "Advanced High Function");
    }

    @Nullable
    public String getRawDevNoiseReductionDescription() {
        Integer value = ((OlympusRawDevelopmentMakernoteDirectory) this._directory).getInteger(266);
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
        return sb.substring(0, sb.length() - 2);
    }

    @Nullable
    public String getRawDevEditStatusDescription() {
        Integer value = ((OlympusRawDevelopmentMakernoteDirectory) this._directory).getInteger(267);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "Original";
            case 1:
                return "Edited (Landscape)";
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            default:
                return "Unknown (" + value + ")";
            case 6:
            case 8:
                return "Edited (Portrait)";
        }
    }

    @Nullable
    public String getRawDevSettingsDescription() {
        Integer value = ((OlympusRawDevelopmentMakernoteDirectory) this._directory).getInteger(268);
        if (value == null) {
            return null;
        }
        if (value.intValue() == 0) {
            return "(none)";
        }
        StringBuilder sb = new StringBuilder();
        int v = value.intValue();
        if ((v & 1) != 0) {
            sb.append("WB Color Temp, ");
        }
        if (((v >> 1) & 1) != 0) {
            sb.append("WB Gray Point, ");
        }
        if (((v >> 2) & 1) != 0) {
            sb.append("Saturation, ");
        }
        if (((v >> 3) & 1) != 0) {
            sb.append("Contrast, ");
        }
        if (((v >> 4) & 1) != 0) {
            sb.append("Sharpness, ");
        }
        if (((v >> 5) & 1) != 0) {
            sb.append("Color Space, ");
        }
        if (((v >> 6) & 1) != 0) {
            sb.append("High Function, ");
        }
        if (((v >> 7) & 1) != 0) {
            sb.append("Noise Reduction, ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
