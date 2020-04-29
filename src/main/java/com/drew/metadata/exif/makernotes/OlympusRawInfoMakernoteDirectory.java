package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class OlympusRawInfoMakernoteDirectory extends Directory {
    public static final int TagBlackLevel2 = 1536;
    public static final int TagCmContrast = 8226;
    public static final int TagCmExposureCompensation = 8192;
    public static final int TagCmHue = 8225;
    public static final int TagCmSaturation = 8224;
    public static final int TagCmSharpness = 8227;
    public static final int TagCmWhiteBalance = 8193;
    public static final int TagCmWhiteBalanceComp = 8194;
    public static final int TagCmWhiteBalanceGrayPoint = 8208;
    public static final int TagColorMatrix2 = 512;
    public static final int TagContrastSetting = 4114;
    public static final int TagCoringFilter = 784;
    public static final int TagCoringValues = 785;
    public static final int TagCropHeight = 1557;
    public static final int TagCropLeft = 1554;
    public static final int TagCropTop = 1555;
    public static final int TagCropWidth = 1556;
    public static final int TagHueSetting = 4113;
    public static final int TagLightSource = 4096;
    public static final int TagRawInfoVersion = 0;
    public static final int TagSaturationSetting = 4112;
    public static final int TagSharpnessSetting = 4115;
    public static final int TagValidPixelDepth = 1553;
    public static final int TagWbRbLevelsAuto = 272;
    public static final int TagWbRbLevelsCloudy = 289;
    public static final int TagWbRbLevelsCoolWhiteFluor = 306;
    public static final int TagWbRbLevelsDayWhiteFluor = 305;
    public static final int TagWbRbLevelsDaylightFluor = 304;
    public static final int TagWbRbLevelsEveningSunlight = 292;
    public static final int TagWbRbLevelsFineWeather = 290;
    public static final int TagWbRbLevelsShade = 288;
    public static final int TagWbRbLevelsTungsten = 291;
    public static final int TagWbRbLevelsUsed = 256;
    public static final int TagWbRbLevelsWhiteFluorescent = 307;
    public static final int TagWhiteBalanceComp = 4097;
    public static final int TagYCbCrCoefficients = 1537;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Raw Info Version");
        _tagNameMap.put(256, "WB RB Levels Used");
        _tagNameMap.put(272, "WB RB Levels Auto");
        _tagNameMap.put(288, "WB RB Levels Shade");
        _tagNameMap.put(289, "WB RB Levels Cloudy");
        _tagNameMap.put(Integer.valueOf((int) TagWbRbLevelsFineWeather), "WB RB Levels Fine Weather");
        _tagNameMap.put(291, "WB RB Levels Tungsten");
        _tagNameMap.put(Integer.valueOf((int) TagWbRbLevelsEveningSunlight), "WB RB Levels Evening Sunlight");
        _tagNameMap.put(Integer.valueOf((int) TagWbRbLevelsDaylightFluor), "WB RB Levels Daylight Fluor");
        _tagNameMap.put(305, "WB RB Levels Day White Fluor");
        _tagNameMap.put(306, "WB RB Levels Cool White Fluor");
        _tagNameMap.put(307, "WB RB Levels White Fluorescent");
        _tagNameMap.put(512, "Color Matrix 2");
        _tagNameMap.put(784, "Coring Filter");
        _tagNameMap.put(785, "Coring Values");
        _tagNameMap.put(1536, "Black Level 2");
        _tagNameMap.put(1537, "YCbCrCoefficients");
        _tagNameMap.put(1553, "Valid Pixel Depth");
        _tagNameMap.put(1554, "Crop Left");
        _tagNameMap.put(1555, "Crop Top");
        _tagNameMap.put(1556, "Crop Width");
        _tagNameMap.put(1557, "Crop Height");
        _tagNameMap.put(4096, "Light Source");
        _tagNameMap.put(4097, "White Balance Comp");
        _tagNameMap.put(4112, "Saturation Setting");
        _tagNameMap.put(4113, "Hue Setting");
        _tagNameMap.put(4114, "Contrast Setting");
        _tagNameMap.put(4115, "Sharpness Setting");
        _tagNameMap.put(8192, "CM Exposure Compensation");
        _tagNameMap.put(8193, "CM White Balance");
        _tagNameMap.put(8194, "CM White Balance Comp");
        _tagNameMap.put(8208, "CM White Balance Gray Point");
        _tagNameMap.put(8224, "CM Saturation");
        _tagNameMap.put(Integer.valueOf((int) TagCmHue), "CM Hue");
        _tagNameMap.put(8226, "CM Contrast");
        _tagNameMap.put(Integer.valueOf((int) TagCmSharpness), "CM Sharpness");
    }

    public OlympusRawInfoMakernoteDirectory() {
        setDescriptor(new OlympusRawInfoMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Olympus Raw Info";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
