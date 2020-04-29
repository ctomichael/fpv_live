package com.drew.metadata.pcx;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class PcxDirectory extends Directory {
    public static final int TAG_BITS_PER_PIXEL = 2;
    public static final int TAG_BYTES_PER_LINE = 11;
    public static final int TAG_COLOR_PLANES = 10;
    public static final int TAG_HORIZONTAL_DPI = 7;
    public static final int TAG_HSCR_SIZE = 13;
    public static final int TAG_PALETTE = 9;
    public static final int TAG_PALETTE_TYPE = 12;
    public static final int TAG_VERSION = 1;
    public static final int TAG_VERTICAL_DPI = 8;
    public static final int TAG_VSCR_SIZE = 14;
    public static final int TAG_XMAX = 5;
    public static final int TAG_XMIN = 3;
    public static final int TAG_YMAX = 6;
    public static final int TAG_YMIN = 4;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Version");
        _tagNameMap.put(2, "Bits Per Pixel");
        _tagNameMap.put(3, "X Min");
        _tagNameMap.put(4, "Y Min");
        _tagNameMap.put(5, "X Max");
        _tagNameMap.put(6, "Y Max");
        _tagNameMap.put(7, "Horizontal DPI");
        _tagNameMap.put(8, "Vertical DPI");
        _tagNameMap.put(9, "Palette");
        _tagNameMap.put(10, "Color Planes");
        _tagNameMap.put(11, "Bytes Per Line");
        _tagNameMap.put(12, "Palette Type");
        _tagNameMap.put(13, "H Scr Size");
        _tagNameMap.put(14, "V Scr Size");
    }

    public PcxDirectory() {
        setDescriptor(new PcxDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "PCX";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
