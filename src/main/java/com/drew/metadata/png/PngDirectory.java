package com.drew.metadata.png;

import com.drew.imaging.png.PngChunkType;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class PngDirectory extends Directory {
    public static final int TAG_BACKGROUND_COLOR = 15;
    public static final int TAG_BITS_PER_SAMPLE = 3;
    public static final int TAG_COLOR_TYPE = 4;
    public static final int TAG_COMPRESSION_TYPE = 5;
    public static final int TAG_FILTER_METHOD = 6;
    public static final int TAG_GAMMA = 11;
    public static final int TAG_ICC_PROFILE_NAME = 12;
    public static final int TAG_IMAGE_HEIGHT = 2;
    public static final int TAG_IMAGE_WIDTH = 1;
    public static final int TAG_INTERLACE_METHOD = 7;
    public static final int TAG_LAST_MODIFICATION_TIME = 14;
    public static final int TAG_PALETTE_HAS_TRANSPARENCY = 9;
    public static final int TAG_PALETTE_SIZE = 8;
    public static final int TAG_PIXELS_PER_UNIT_X = 16;
    public static final int TAG_PIXELS_PER_UNIT_Y = 17;
    public static final int TAG_SIGNIFICANT_BITS = 19;
    public static final int TAG_SRGB_RENDERING_INTENT = 10;
    public static final int TAG_TEXTUAL_DATA = 13;
    public static final int TAG_UNIT_SPECIFIER = 18;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();
    private final PngChunkType _pngChunkType;

    static {
        _tagNameMap.put(2, "Image Height");
        _tagNameMap.put(1, "Image Width");
        _tagNameMap.put(3, "Bits Per Sample");
        _tagNameMap.put(4, "Color Type");
        _tagNameMap.put(5, "Compression Type");
        _tagNameMap.put(6, "Filter Method");
        _tagNameMap.put(7, "Interlace Method");
        _tagNameMap.put(8, "Palette Size");
        _tagNameMap.put(9, "Palette Has Transparency");
        _tagNameMap.put(10, "sRGB Rendering Intent");
        _tagNameMap.put(11, "Image Gamma");
        _tagNameMap.put(12, "ICC Profile Name");
        _tagNameMap.put(13, "Textual Data");
        _tagNameMap.put(14, "Last Modification Time");
        _tagNameMap.put(15, "Background Color");
        _tagNameMap.put(16, "Pixels Per Unit X");
        _tagNameMap.put(17, "Pixels Per Unit Y");
        _tagNameMap.put(18, "Unit Specifier");
        _tagNameMap.put(19, "Significant Bits");
    }

    public PngDirectory(@NotNull PngChunkType pngChunkType) {
        this._pngChunkType = pngChunkType;
        setDescriptor(new PngDescriptor(this));
    }

    @NotNull
    public PngChunkType getPngChunkType() {
        return this._pngChunkType;
    }

    @NotNull
    public String getName() {
        return "PNG-" + this._pngChunkType.getIdentifier();
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
