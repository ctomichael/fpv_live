package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class PentaxMakernoteDirectory extends Directory {
    public static final int TAG_CAPTURE_MODE = 1;
    public static final int TAG_COLOUR = 23;
    public static final int TAG_CONTRAST = 12;
    public static final int TAG_DAYLIGHT_SAVINGS = 4097;
    public static final int TAG_DIGITAL_ZOOM = 10;
    public static final int TAG_FLASH_MODE = 4;
    public static final int TAG_FOCUS_MODE = 3;
    public static final int TAG_ISO_SPEED = 20;
    public static final int TAG_PRINT_IMAGE_MATCHING_INFO = 3584;
    public static final int TAG_QUALITY_LEVEL = 2;
    public static final int TAG_SATURATION = 13;
    public static final int TAG_SHARPNESS = 11;
    public static final int TAG_TIME_ZONE = 4096;
    public static final int TAG_WHITE_BALANCE = 7;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Capture Mode");
        _tagNameMap.put(2, "Quality Level");
        _tagNameMap.put(3, "Focus Mode");
        _tagNameMap.put(4, "Flash Mode");
        _tagNameMap.put(7, "White Balance");
        _tagNameMap.put(10, "Digital Zoom");
        _tagNameMap.put(11, CameraKeys.SHARPNESS);
        _tagNameMap.put(12, CameraKeys.CONTRAST);
        _tagNameMap.put(13, CameraKeys.SATURATION);
        _tagNameMap.put(20, "ISO Speed");
        _tagNameMap.put(23, "Colour");
        _tagNameMap.put(3584, "Print Image Matching (PIM) Info");
        _tagNameMap.put(4096, "Time Zone");
        _tagNameMap.put(4097, "Daylight Savings");
    }

    public PentaxMakernoteDirectory() {
        setDescriptor(new PentaxMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Pentax Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
