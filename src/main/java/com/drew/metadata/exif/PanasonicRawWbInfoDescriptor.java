package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class PanasonicRawWbInfoDescriptor extends TagDescriptor<PanasonicRawWbInfoDirectory> {
    public PanasonicRawWbInfoDescriptor(@NotNull PanasonicRawWbInfoDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
            case 4:
            case 7:
            case 10:
            case 13:
            case 16:
            case 19:
                return getWbTypeDescription(tagType);
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getWbTypeDescription(int tagType) {
        Integer wbtype = ((PanasonicRawWbInfoDirectory) this._directory).getInteger(tagType);
        if (wbtype == null) {
            return null;
        }
        return super.getLightSourceDescription(wbtype.shortValue());
    }
}
