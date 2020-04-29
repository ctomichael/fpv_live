package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class RicohMakernoteDescriptor extends TagDescriptor<RicohMakernoteDirectory> {
    public RicohMakernoteDescriptor(@NotNull RicohMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        return super.getDescription(tagType);
    }
}
