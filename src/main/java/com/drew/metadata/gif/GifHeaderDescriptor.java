package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.TagDescriptor;

public class GifHeaderDescriptor extends TagDescriptor<GifHeaderDirectory> {
    public GifHeaderDescriptor(@NotNull GifHeaderDirectory directory) {
        super(directory);
    }
}
