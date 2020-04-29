package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class KodakMakernoteDirectory extends Directory {
    public static final int TAG_BURST_MODE = 10;
    public static final int TAG_BURST_MODE_2 = 24;
    public static final int TAG_COLOR_MODE = 102;
    public static final int TAG_DATE_TIME_STAMP = 100;
    public static final int TAG_DIGITAL_ZOOM = 104;
    public static final int TAG_EXPOSURE_COMPENSATION = 36;
    public static final int TAG_EXPOSURE_TIME = 32;
    public static final int TAG_FLASH_FIRED = 93;
    public static final int TAG_FLASH_MODE = 92;
    public static final int TAG_FOCUS_MODE = 56;
    public static final int TAG_F_NUMBER = 30;
    public static final int TAG_IMAGE_HEIGHT = 14;
    public static final int TAG_IMAGE_WIDTH = 12;
    public static final int TAG_ISO = 96;
    public static final int TAG_ISO_SETTING = 94;
    public static final int TAG_KODAK_MODEL = 0;
    public static final int TAG_METERING_MODE = 28;
    public static final int TAG_MONTH_DAY_CREATED = 18;
    public static final int TAG_QUALITY = 9;
    public static final int TAG_SEQUENCE_NUMBER = 29;
    public static final int TAG_SHARPNESS = 107;
    public static final int TAG_SHUTTER_MODE = 27;
    public static final int TAG_TIME_CREATED = 20;
    public static final int TAG_TOTAL_ZOOM = 98;
    public static final int TAG_WHITE_BALANCE = 64;
    public static final int TAG_YEAR_CREATED = 16;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Kodak Model");
        _tagNameMap.put(9, "Quality");
        _tagNameMap.put(10, "Burst Mode");
        _tagNameMap.put(12, "Image Width");
        _tagNameMap.put(14, "Image Height");
        _tagNameMap.put(16, "Year Created");
        _tagNameMap.put(18, "Month/Day Created");
        _tagNameMap.put(20, "Time Created");
        _tagNameMap.put(24, "Burst Mode 2");
        _tagNameMap.put(27, "Shutter Speed");
        _tagNameMap.put(28, "Metering Mode");
        _tagNameMap.put(29, "Sequence Number");
        _tagNameMap.put(30, "F Number");
        _tagNameMap.put(32, "Exposure Time");
        _tagNameMap.put(36, "Exposure Compensation");
        _tagNameMap.put(56, "Focus Mode");
        _tagNameMap.put(64, "White Balance");
        _tagNameMap.put(92, "Flash Mode");
        _tagNameMap.put(93, "Flash Fired");
        _tagNameMap.put(94, "ISO Setting");
        _tagNameMap.put(96, CameraKeys.ISO);
        _tagNameMap.put(98, "Total Zoom");
        _tagNameMap.put(100, "Date/Time Stamp");
        _tagNameMap.put(102, "Color Mode");
        _tagNameMap.put(104, "Digital Zoom");
        _tagNameMap.put(107, CameraKeys.SHARPNESS);
    }

    public KodakMakernoteDirectory() {
        setDescriptor(new KodakMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Kodak Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
