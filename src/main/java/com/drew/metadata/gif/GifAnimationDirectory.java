package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class GifAnimationDirectory extends Directory {
    public static final int TAG_ITERATION_COUNT = 1;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Iteration Count");
    }

    public GifAnimationDirectory() {
        setDescriptor(new GifAnimationDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "GIF Animation";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
