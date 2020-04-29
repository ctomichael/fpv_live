package com.drew.metadata.bmp;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class BmpHeaderDirectory extends Directory {
    public static final int TAG_BITS_PER_PIXEL = 4;
    public static final int TAG_COLOUR_PLANES = 3;
    public static final int TAG_COMPRESSION = 5;
    public static final int TAG_HEADER_SIZE = -1;
    public static final int TAG_IMAGE_HEIGHT = 1;
    public static final int TAG_IMAGE_WIDTH = 2;
    public static final int TAG_IMPORTANT_COLOUR_COUNT = 9;
    public static final int TAG_PALETTE_COLOUR_COUNT = 8;
    public static final int TAG_X_PIXELS_PER_METER = 6;
    public static final int TAG_Y_PIXELS_PER_METER = 7;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(-1, "Header Size");
        _tagNameMap.put(1, "Image Height");
        _tagNameMap.put(2, "Image Width");
        _tagNameMap.put(3, "Planes");
        _tagNameMap.put(4, "Bits Per Pixel");
        _tagNameMap.put(5, "Compression");
        _tagNameMap.put(6, "X Pixels per Meter");
        _tagNameMap.put(7, "Y Pixels per Meter");
        _tagNameMap.put(8, "Palette Colour Count");
        _tagNameMap.put(9, "Important Colour Count");
    }

    public BmpHeaderDirectory() {
        setDescriptor(new BmpHeaderDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "BMP Header";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
