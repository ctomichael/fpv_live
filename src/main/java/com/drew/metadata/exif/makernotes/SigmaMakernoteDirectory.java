package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class SigmaMakernoteDirectory extends Directory {
    public static final int TAG_ADJUSTMENT_MODE = 21;
    public static final int TAG_AUTO_BRACKET = 25;
    public static final int TAG_AUTO_FOCUS_MODE = 5;
    public static final int TAG_COLOR_ADJUSTMENT = 20;
    public static final int TAG_COLOR_SPACE = 11;
    public static final int TAG_CONTRAST = 13;
    public static final int TAG_DRIVE_MODE = 3;
    public static final int TAG_EXPOSURE = 12;
    public static final int TAG_EXPOSURE_MODE = 8;
    public static final int TAG_FILL_LIGHT = 18;
    public static final int TAG_FIRMWARE = 23;
    public static final int TAG_FOCUS_SETTING = 6;
    public static final int TAG_HIGHLIGHT = 15;
    public static final int TAG_LENS_RANGE = 10;
    public static final int TAG_METERING_MODE = 9;
    public static final int TAG_QUALITY = 22;
    public static final int TAG_RESOLUTION_MODE = 4;
    public static final int TAG_SATURATION = 16;
    public static final int TAG_SERIAL_NUMBER = 2;
    public static final int TAG_SHADOW = 14;
    public static final int TAG_SHARPNESS = 17;
    public static final int TAG_SOFTWARE = 24;
    public static final int TAG_WHITE_BALANCE = 7;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(2, "Serial Number");
        _tagNameMap.put(3, "Drive Mode");
        _tagNameMap.put(4, "Resolution Mode");
        _tagNameMap.put(5, "Auto Focus Mode");
        _tagNameMap.put(6, "Focus Setting");
        _tagNameMap.put(7, "White Balance");
        _tagNameMap.put(8, "Exposure Mode");
        _tagNameMap.put(9, "Metering Mode");
        _tagNameMap.put(10, "Lens Range");
        _tagNameMap.put(11, "Color Space");
        _tagNameMap.put(12, "Exposure");
        _tagNameMap.put(13, CameraKeys.CONTRAST);
        _tagNameMap.put(14, "Shadow");
        _tagNameMap.put(15, "Highlight");
        _tagNameMap.put(16, CameraKeys.SATURATION);
        _tagNameMap.put(17, CameraKeys.SHARPNESS);
        _tagNameMap.put(18, "Fill Light");
        _tagNameMap.put(20, "Color Adjustment");
        _tagNameMap.put(21, "Adjustment Mode");
        _tagNameMap.put(22, "Quality");
        _tagNameMap.put(23, "Firmware");
        _tagNameMap.put(24, "Software");
        _tagNameMap.put(25, "Auto Bracket");
    }

    public SigmaMakernoteDirectory() {
        setDescriptor(new SigmaMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Sigma Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
