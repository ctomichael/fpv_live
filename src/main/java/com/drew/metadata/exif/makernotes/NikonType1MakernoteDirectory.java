package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class NikonType1MakernoteDirectory extends Directory {
    public static final int TAG_CCD_SENSITIVITY = 6;
    public static final int TAG_COLOR_MODE = 4;
    public static final int TAG_CONVERTER = 11;
    public static final int TAG_DIGITAL_ZOOM = 10;
    public static final int TAG_FOCUS = 8;
    public static final int TAG_IMAGE_ADJUSTMENT = 5;
    public static final int TAG_QUALITY = 3;
    public static final int TAG_UNKNOWN_1 = 2;
    public static final int TAG_UNKNOWN_2 = 9;
    public static final int TAG_UNKNOWN_3 = 3840;
    public static final int TAG_WHITE_BALANCE = 7;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(6, "CCD Sensitivity");
        _tagNameMap.put(4, "Color Mode");
        _tagNameMap.put(10, "Digital Zoom");
        _tagNameMap.put(11, "Fisheye Converter");
        _tagNameMap.put(8, "Focus");
        _tagNameMap.put(5, "Image Adjustment");
        _tagNameMap.put(3, "Quality");
        _tagNameMap.put(2, "Makernote Unknown 1");
        _tagNameMap.put(9, "Makernote Unknown 2");
        _tagNameMap.put(3840, "Makernote Unknown 3");
        _tagNameMap.put(7, "White Balance");
    }

    public NikonType1MakernoteDirectory() {
        setDescriptor(new NikonType1MakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Nikon Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
