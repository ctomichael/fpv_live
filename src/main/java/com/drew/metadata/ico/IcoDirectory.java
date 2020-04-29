package com.drew.metadata.ico;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class IcoDirectory extends Directory {
    public static final int TAG_BITS_PER_PIXEL = 7;
    public static final int TAG_COLOUR_PALETTE_SIZE = 4;
    public static final int TAG_COLOUR_PLANES = 5;
    public static final int TAG_CURSOR_HOTSPOT_X = 6;
    public static final int TAG_CURSOR_HOTSPOT_Y = 8;
    public static final int TAG_IMAGE_HEIGHT = 3;
    public static final int TAG_IMAGE_OFFSET_BYTES = 10;
    public static final int TAG_IMAGE_SIZE_BYTES = 9;
    public static final int TAG_IMAGE_TYPE = 1;
    public static final int TAG_IMAGE_WIDTH = 2;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Image Type");
        _tagNameMap.put(2, "Image Width");
        _tagNameMap.put(3, "Image Height");
        _tagNameMap.put(4, "Colour Palette Size");
        _tagNameMap.put(5, "Colour Planes");
        _tagNameMap.put(6, "Hotspot X");
        _tagNameMap.put(7, "Bits Per Pixel");
        _tagNameMap.put(8, "Hotspot Y");
        _tagNameMap.put(9, "Image Size Bytes");
        _tagNameMap.put(10, "Image Offset Bytes");
    }

    public IcoDirectory() {
        setDescriptor(new IcoDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "ICO";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
