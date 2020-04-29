package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class PanasonicRawWbInfo2Descriptor extends TagDescriptor<PanasonicRawWbInfo2Directory> {
    public PanasonicRawWbInfo2Descriptor(@NotNull PanasonicRawWbInfo2Directory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
            case 5:
            case 9:
            case 13:
            case 17:
            case 21:
            case 25:
                return getWbTypeDescription(tagType);
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getWbTypeDescription(int tagType) {
        Integer wbtype = ((PanasonicRawWbInfo2Directory) this._directory).getInteger(tagType);
        if (wbtype == null) {
            return null;
        }
        return super.getLightSourceDescription(wbtype.shortValue());
    }
}
