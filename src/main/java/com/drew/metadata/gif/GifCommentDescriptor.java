package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.TagDescriptor;

public class GifCommentDescriptor extends TagDescriptor<GifCommentDirectory> {
    public GifCommentDescriptor(@NotNull GifCommentDirectory directory) {
        super(directory);
    }
}
