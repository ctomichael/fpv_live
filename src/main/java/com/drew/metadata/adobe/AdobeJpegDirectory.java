package com.drew.metadata.adobe;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class AdobeJpegDirectory extends Directory {
    public static final int TAG_APP14_FLAGS0 = 1;
    public static final int TAG_APP14_FLAGS1 = 2;
    public static final int TAG_COLOR_TRANSFORM = 3;
    public static final int TAG_DCT_ENCODE_VERSION = 0;
    private static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "DCT Encode Version");
        _tagNameMap.put(1, "Flags 0");
        _tagNameMap.put(2, "Flags 1");
        _tagNameMap.put(3, "Color Transform");
    }

    public AdobeJpegDirectory() {
        setDescriptor(new AdobeJpegDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Adobe JPEG";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
