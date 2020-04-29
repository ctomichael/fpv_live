package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.HashMap;

public class CasioType1MakernoteDirectory extends Directory {
    public static final int TAG_CCD_SENSITIVITY = 20;
    public static final int TAG_CONTRAST = 12;
    public static final int TAG_DIGITAL_ZOOM = 10;
    public static final int TAG_FLASH_INTENSITY = 5;
    public static final int TAG_FLASH_MODE = 4;
    public static final int TAG_FOCUSING_MODE = 3;
    public static final int TAG_OBJECT_DISTANCE = 6;
    public static final int TAG_QUALITY = 2;
    public static final int TAG_RECORDING_MODE = 1;
    public static final int TAG_SATURATION = 13;
    public static final int TAG_SHARPNESS = 11;
    public static final int TAG_UNKNOWN_1 = 8;
    public static final int TAG_UNKNOWN_2 = 9;
    public static final int TAG_UNKNOWN_3 = 14;
    public static final int TAG_UNKNOWN_4 = 15;
    public static final int TAG_UNKNOWN_5 = 16;
    public static final int TAG_UNKNOWN_6 = 17;
    public static final int TAG_UNKNOWN_7 = 18;
    public static final int TAG_UNKNOWN_8 = 19;
    public static final int TAG_WHITE_BALANCE = 7;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(20, "CCD Sensitivity");
        _tagNameMap.put(12, CameraKeys.CONTRAST);
        _tagNameMap.put(10, "Digital Zoom");
        _tagNameMap.put(5, "Flash Intensity");
        _tagNameMap.put(4, "Flash Mode");
        _tagNameMap.put(3, "Focusing Mode");
        _tagNameMap.put(6, "Object Distance");
        _tagNameMap.put(2, "Quality");
        _tagNameMap.put(1, "Recording Mode");
        _tagNameMap.put(13, CameraKeys.SATURATION);
        _tagNameMap.put(11, CameraKeys.SHARPNESS);
        _tagNameMap.put(8, "Makernote Unknown 1");
        _tagNameMap.put(9, "Makernote Unknown 2");
        _tagNameMap.put(14, "Makernote Unknown 3");
        _tagNameMap.put(15, "Makernote Unknown 4");
        _tagNameMap.put(16, "Makernote Unknown 5");
        _tagNameMap.put(17, "Makernote Unknown 6");
        _tagNameMap.put(18, "Makernote Unknown 7");
        _tagNameMap.put(19, "Makernote Unknown 8");
        _tagNameMap.put(7, "White Balance");
    }

    public CasioType1MakernoteDirectory() {
        setDescriptor(new CasioType1MakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Casio Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
