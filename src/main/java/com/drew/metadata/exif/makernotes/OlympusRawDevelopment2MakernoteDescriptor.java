package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import java.util.HashMap;

public class OlympusRawDevelopment2MakernoteDescriptor extends TagDescriptor<OlympusRawDevelopment2MakernoteDirectory> {
    private static final HashMap<Integer, String> _filters = new HashMap<>();

    public OlympusRawDevelopment2MakernoteDescriptor(@NotNull OlympusRawDevelopment2MakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getRawDevVersionDescription();
            case 256:
                return getRawDevExposureBiasValueDescription();
            case 265:
                return getRawDevColorSpaceDescription();
            case 266:
                return getRawDevNoiseReductionDescription();
            case 267:
                return getRawDevEngineDescription();
            case 268:
                return getRawDevPictureModeDescription();
            case 272:
                return getRawDevPmBwFilterDescription();
            case 273:
                return getRawDevPmPictureToneDescription();
            case 289:
                return getRawDevArtFilterDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getRawDevVersionDescription() {
        return getVersionBytesDescription(0, 4);
    }

    @Nullable
    public String getRawDevExposureBiasValueDescription() {
        return getIndexedDescription(256, 1, "Color Temperature", "Gray Point");
    }

    @Nullable
    public String getRawDevColorSpaceDescription() {
        return getIndexedDescription(265, "sRGB", "Adobe RGB", "Pro Photo RGB");
    }

    @Nullable
    public String getRawDevNoiseReductionDescription() {
        Integer value = ((OlympusRawDevelopment2MakernoteDirectory) this._directory).getInteger(266);
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
    public String getRawDevEngineDescription() {
        return getIndexedDescription(267, "High Speed", "High Function", "Advanced High Speed", "Advanced High Function");
    }

    @Nullable
    public String getRawDevPictureModeDescription() {
        Integer value = ((OlympusRawDevelopment2MakernoteDirectory) this._directory).getInteger(268);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 1:
                return "Vivid";
            case 2:
                return "Natural";
            case 3:
                return "Muted";
            case 256:
                return "Monotone";
            case 512:
                return "Sepia";
            default:
                return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getRawDevPmBwFilterDescription() {
        return getIndexedDescription(272, "Neutral", "Yellow", "Orange", "Red", "Green");
    }

    @Nullable
    public String getRawDevPmPictureToneDescription() {
        return getIndexedDescription(273, "Neutral", "Sepia", "Blue", "Purple", "Green");
    }

    @Nullable
    public String getRawDevArtFilterDescription() {
        return getFilterDescription(289);
    }

    @Nullable
    public String getFilterDescription(int tag) {
        int[] values = ((OlympusRawDevelopment2MakernoteDirectory) this._directory).getIntArray(tag);
        if (values == null || values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                sb.append(_filters.containsKey(Integer.valueOf(values[i])) ? _filters.get(Integer.valueOf(values[i])) : "[unknown]");
            } else {
                sb.append(values[i]).append("; ");
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
    }
}
