package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.TagDescriptor;

public class GifImageDescriptor extends TagDescriptor<GifImageDirectory> {
    public GifImageDescriptor(@NotNull GifImageDirectory directory) {
        super(directory);
    }
}
