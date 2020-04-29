package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class ReconyxUltraFireMakernoteDirectory extends Directory {
    public static final int MAKERNOTE_ID = 65536;
    public static final int MAKERNOTE_PUBLIC_ID = 133234689;
    public static final int TAG_AMBIENT_TEMPERATURE = 70;
    public static final int TAG_AMBIENT_TEMPERATURE_FAHRENHEIT = 68;
    public static final int TAG_BATTERY_VOLTAGE = 73;
    public static final int TAG_BTL_VERSION = 38;
    public static final int TAG_CAMERA_VERSION = 24;
    public static final int TAG_DATE_TIME_ORIGINAL = 59;
    public static final int TAG_DAY_OF_WEEK = 66;
    public static final int TAG_EVENT_NUMBER = 55;
    public static final int TAG_EVENT_TYPE = 52;
    public static final int TAG_FLASH = 72;
    public static final int TAG_LABEL = 0;
    public static final int TAG_MAKERNOTE_ID = 10;
    public static final int TAG_MAKERNOTE_PUBLIC_ID = 18;
    public static final int TAG_MAKERNOTE_PUBLIC_SIZE = 22;
    public static final int TAG_MAKERNOTE_SIZE = 14;
    public static final int TAG_MOON_PHASE = 67;
    public static final int TAG_PEX_VERSION = 45;
    public static final int TAG_SEQUENCE = 53;
    public static final int TAG_SERIAL_NUMBER = 75;
    public static final int TAG_UIB_VERSION = 31;
    public static final int TAG_USER_LABEL = 80;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Makernote Label");
        _tagNameMap.put(10, "Makernote ID");
        _tagNameMap.put(14, "Makernote Size");
        _tagNameMap.put(18, "Makernote Public ID");
        _tagNameMap.put(22, "Makernote Public Size");
        _tagNameMap.put(24, "Camera Version");
        _tagNameMap.put(31, "Uib Version");
        _tagNameMap.put(38, "Btl Version");
        _tagNameMap.put(45, "Pex Version");
        _tagNameMap.put(52, "Event Type");
        _tagNameMap.put(53, "Sequence");
        _tagNameMap.put(55, "Event Number");
        _tagNameMap.put(59, "Date/Time Original");
        _tagNameMap.put(66, "Day of Week");
        _tagNameMap.put(67, "Moon Phase");
        _tagNameMap.put(68, "Ambient Temperature Fahrenheit");
        _tagNameMap.put(70, "Ambient Temperature");
        _tagNameMap.put(72, "Flash");
        _tagNameMap.put(73, "Battery Voltage");
        _tagNameMap.put(75, "Serial Number");
        _tagNameMap.put(80, "User Label");
    }

    public ReconyxUltraFireMakernoteDirectory() {
        setDescriptor(new ReconyxUltraFireMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Reconyx UltraFire Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
