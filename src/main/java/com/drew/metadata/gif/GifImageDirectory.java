package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class GifImageDirectory extends Directory {
    public static final int TAG_HAS_LOCAL_COLOUR_TABLE = 5;
    public static final int TAG_HEIGHT = 4;
    public static final int TAG_IS_COLOR_TABLE_SORTED = 7;
    public static final int TAG_IS_INTERLACED = 6;
    public static final int TAG_LEFT = 1;
    public static final int TAG_LOCAL_COLOUR_TABLE_BITS_PER_PIXEL = 8;
    public static final int TAG_TOP = 2;
    public static final int TAG_WIDTH = 3;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Left");
        _tagNameMap.put(2, "Top");
        _tagNameMap.put(3, "Width");
        _tagNameMap.put(4, "Height");
        _tagNameMap.put(5, "Has Local Colour Table");
        _tagNameMap.put(6, "Is Interlaced");
        _tagNameMap.put(7, "Is Local Colour Table Sorted");
        _tagNameMap.put(8, "Local Colour Table Bits Per Pixel");
    }

    public GifImageDirectory() {
        setDescriptor(new GifImageDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "GIF Image";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
