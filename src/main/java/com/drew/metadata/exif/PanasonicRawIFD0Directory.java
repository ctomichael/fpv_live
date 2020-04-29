package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class PanasonicRawIFD0Directory extends Directory {
    public static final int TagBlackLevel1 = 8;
    public static final int TagBlackLevel2 = 9;
    public static final int TagBlackLevel3 = 10;
    public static final int TagBlackLevelBlue = 30;
    public static final int TagBlackLevelGreen = 29;
    public static final int TagBlackLevelRed = 28;
    public static final int TagBlueBalance = 18;
    public static final int TagCropBottom = 49;
    public static final int TagCropLeft = 48;
    public static final int TagCropRight = 50;
    public static final int TagCropTop = 47;
    public static final int TagDistortionInfo = 281;
    public static final int TagHighIsoMultiplierBlue = 26;
    public static final int TagHighIsoMultiplierGreen = 25;
    public static final int TagHighIsoMultiplierRed = 24;
    public static final int TagIso = 23;
    public static final int TagJpgFromRaw = 46;
    public static final int TagLinearityLimitBlue = 16;
    public static final int TagLinearityLimitGreen = 15;
    public static final int TagLinearityLimitRed = 14;
    public static final int TagMake = 271;
    public static final int TagModel = 272;
    public static final int TagOrientation = 274;
    public static final int TagPanasonicRawVersion = 1;
    public static final int TagRawDataOffset = 280;
    public static final int TagRedBalance = 17;
    public static final int TagRowsPerStrip = 278;
    public static final int TagSensorBottomBorder = 6;
    public static final int TagSensorHeight = 3;
    public static final int TagSensorLeftBorder = 5;
    public static final int TagSensorRightBorder = 7;
    public static final int TagSensorTopBorder = 4;
    public static final int TagSensorWidth = 2;
    public static final int TagStripByteCounts = 279;
    public static final int TagStripOffsets = 273;
    public static final int TagWbBlueLevel = 38;
    public static final int TagWbGreenLevel = 37;
    public static final int TagWbInfo = 19;
    public static final int TagWbInfo2 = 39;
    public static final int TagWbRedLevel = 36;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    public PanasonicRawIFD0Directory() {
        setDescriptor(new PanasonicRawIFD0Descriptor(this));
    }

    static {
        _tagNameMap.put(1, "Panasonic Raw Version");
        _tagNameMap.put(2, "Sensor Width");
        _tagNameMap.put(3, "Sensor Height");
        _tagNameMap.put(4, "Sensor Top Border");
        _tagNameMap.put(5, "Sensor Left Border");
        _tagNameMap.put(6, "Sensor Bottom Border");
        _tagNameMap.put(7, "Sensor Right Border");
        _tagNameMap.put(8, "Black Level 1");
        _tagNameMap.put(9, "Black Level 2");
        _tagNameMap.put(10, "Black Level 3");
        _tagNameMap.put(14, "Linearity Limit Red");
        _tagNameMap.put(15, "Linearity Limit Green");
        _tagNameMap.put(16, "Linearity Limit Blue");
        _tagNameMap.put(17, "Red Balance");
        _tagNameMap.put(18, "Blue Balance");
        _tagNameMap.put(23, CameraKeys.ISO);
        _tagNameMap.put(24, "High ISO Multiplier Red");
        _tagNameMap.put(25, "High ISO Multiplier Green");
        _tagNameMap.put(26, "High ISO Multiplier Blue");
        _tagNameMap.put(28, "Black Level Red");
        _tagNameMap.put(29, "Black Level Green");
        _tagNameMap.put(30, "Black Level Blue");
        _tagNameMap.put(36, "WB Red Level");
        _tagNameMap.put(37, "WB Green Level");
        _tagNameMap.put(38, "WB Blue Level");
        _tagNameMap.put(46, "Jpg From Raw");
        _tagNameMap.put(47, "Crop Top");
        _tagNameMap.put(48, "Crop Left");
        _tagNameMap.put(49, "Crop Bottom");
        _tagNameMap.put(50, "Crop Right");
        _tagNameMap.put(271, "Make");
        _tagNameMap.put(272, "Model");
        _tagNameMap.put(273, "Strip Offsets");
        _tagNameMap.put(274, CameraKeys.ORIENTATION);
        _tagNameMap.put(278, "Rows Per Strip");
        _tagNameMap.put(279, "Strip Byte Counts");
        _tagNameMap.put(280, "Raw Data Offset");
    }

    @NotNull
    public String getName() {
        return "PanasonicRaw Exif IFD0";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
