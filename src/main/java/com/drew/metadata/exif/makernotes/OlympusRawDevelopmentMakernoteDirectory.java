package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class OlympusRawDevelopmentMakernoteDirectory extends Directory {
    public static final int TagRawDevColorSpace = 264;
    public static final int TagRawDevContrastValue = 262;
    public static final int TagRawDevEditStatus = 267;
    public static final int TagRawDevEngine = 265;
    public static final int TagRawDevExposureBiasValue = 256;
    public static final int TagRawDevGrayPoint = 259;
    public static final int TagRawDevMemoryColorEmphasis = 261;
    public static final int TagRawDevNoiseReduction = 266;
    public static final int TagRawDevSaturationEmphasis = 260;
    public static final int TagRawDevSettings = 268;
    public static final int TagRawDevSharpnessValue = 263;
    public static final int TagRawDevVersion = 0;
    public static final int TagRawDevWbFineAdjustment = 258;
    public static final int TagRawDevWhiteBalanceValue = 257;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Raw Dev Version");
        _tagNameMap.put(256, "Raw Dev Exposure Bias Value");
        _tagNameMap.put(257, "Raw Dev White Balance Value");
        _tagNameMap.put(258, "Raw Dev WB Fine Adjustment");
        _tagNameMap.put(259, "Raw Dev Gray Point");
        _tagNameMap.put(260, "Raw Dev Saturation Emphasis");
        _tagNameMap.put(261, "Raw Dev Memory Color Emphasis");
        _tagNameMap.put(262, "Raw Dev Contrast Value");
        _tagNameMap.put(263, "Raw Dev Sharpness Value");
        _tagNameMap.put(264, "Raw Dev Color Space");
        _tagNameMap.put(265, "Raw Dev Engine");
        _tagNameMap.put(266, "Raw Dev Noise Reduction");
        _tagNameMap.put(267, "Raw Dev Edit Status");
        _tagNameMap.put(268, "Raw Dev Settings");
    }

    public OlympusRawDevelopmentMakernoteDirectory() {
        setDescriptor(new OlympusRawDevelopmentMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Olympus Raw Development";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
