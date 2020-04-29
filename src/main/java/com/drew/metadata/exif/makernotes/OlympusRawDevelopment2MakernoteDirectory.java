package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class OlympusRawDevelopment2MakernoteDirectory extends Directory {
    public static final int TagRawDevArtFilter = 289;
    public static final int TagRawDevAutoGradation = 281;
    public static final int TagRawDevColorSpace = 265;
    public static final int TagRawDevContrastValue = 261;
    public static final int TagRawDevEngine = 267;
    public static final int TagRawDevExposureBiasValue = 256;
    public static final int TagRawDevGradation = 274;
    public static final int TagRawDevGrayPoint = 260;
    public static final int TagRawDevMemoryColorEmphasis = 264;
    public static final int TagRawDevNoiseReduction = 266;
    public static final int TagRawDevPictureMode = 268;
    public static final int TagRawDevPmBwFilter = 272;
    public static final int TagRawDevPmContrast = 270;
    public static final int TagRawDevPmNoiseFilter = 288;
    public static final int TagRawDevPmPictureTone = 273;
    public static final int TagRawDevPmSaturation = 269;
    public static final int TagRawDevPmSharpness = 271;
    public static final int TagRawDevSaturation3 = 275;
    public static final int TagRawDevSaturationEmphasis = 263;
    public static final int TagRawDevSharpnessValue = 262;
    public static final int TagRawDevVersion = 0;
    public static final int TagRawDevWbFineAdjustment = 259;
    public static final int TagRawDevWhiteBalance = 257;
    public static final int TagRawDevWhiteBalanceValue = 258;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Raw Dev Version");
        _tagNameMap.put(256, "Raw Dev Exposure Bias Value");
        _tagNameMap.put(257, "Raw Dev White Balance");
        _tagNameMap.put(258, "Raw Dev White Balance Value");
        _tagNameMap.put(259, "Raw Dev WB Fine Adjustment");
        _tagNameMap.put(260, "Raw Dev Gray Point");
        _tagNameMap.put(261, "Raw Dev Contrast Value");
        _tagNameMap.put(262, "Raw Dev Sharpness Value");
        _tagNameMap.put(263, "Raw Dev Saturation Emphasis");
        _tagNameMap.put(264, "Raw Dev Memory Color Emphasis");
        _tagNameMap.put(265, "Raw Dev Color Space");
        _tagNameMap.put(266, "Raw Dev Noise Reduction");
        _tagNameMap.put(267, "Raw Dev Engine");
        _tagNameMap.put(268, "Raw Dev Picture Mode");
        _tagNameMap.put(269, "Raw Dev PM Saturation");
        _tagNameMap.put(270, "Raw Dev PM Contrast");
        _tagNameMap.put(271, "Raw Dev PM Sharpness");
        _tagNameMap.put(272, "Raw Dev PM BW Filter");
        _tagNameMap.put(273, "Raw Dev PM Picture Tone");
        _tagNameMap.put(274, "Raw Dev Gradation");
        _tagNameMap.put(275, "Raw Dev Saturation 3");
        _tagNameMap.put(281, "Raw Dev Auto Gradation");
        _tagNameMap.put(288, "Raw Dev PM Noise Filter");
        _tagNameMap.put(289, "Raw Dev Art Filter");
    }

    public OlympusRawDevelopment2MakernoteDirectory() {
        setDescriptor(new OlympusRawDevelopment2MakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Olympus Raw Development 2";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
