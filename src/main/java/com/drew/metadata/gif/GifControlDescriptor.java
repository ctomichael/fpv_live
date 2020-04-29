package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.TagDescriptor;

public class GifControlDescriptor extends TagDescriptor<GifControlDirectory> {
    public GifControlDescriptor(@NotNull GifControlDirectory directory) {
        super(directory);
    }
}
