package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class GifAnimationDescriptor extends TagDescriptor<GifAnimationDirectory> {
    public GifAnimationDescriptor(@NotNull GifAnimationDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getIterationCountDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getIterationCountDescription() {
        Integer count = ((GifAnimationDirectory) this._directory).getInteger(1);
        if (count == null) {
            return null;
        }
        if (count.intValue() == 0) {
            return "Infinite";
        }
        if (count.intValue() == 1) {
            return "Once";
        }
        return count.intValue() == 2 ? "Twice" : count.toString() + " times";
    }
}
