package com.drew.metadata.photoshop;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class PsdHeaderDescriptor extends TagDescriptor<PsdHeaderDirectory> {
    public PsdHeaderDescriptor(@NotNull PsdHeaderDirectory directory) {
        super(directory);
    }

    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getChannelCountDescription();
            case 2:
                return getImageHeightDescription();
            case 3:
                return getImageWidthDescription();
            case 4:
                return getBitsPerChannelDescription();
            case 5:
                return getColorModeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getChannelCountDescription() {
        Integer value = ((PsdHeaderDirectory) this._directory).getInteger(1);
        if (value == null) {
            return null;
        }
        return value + " channel" + (value.intValue() == 1 ? "" : "s");
    }

    @Nullable
    public String getBitsPerChannelDescription() {
        Integer value = ((PsdHeaderDirectory) this._directory).getInteger(4);
        if (value == null) {
            return null;
        }
        return value + " bit" + (value.intValue() == 1 ? "" : "s") + " per channel";
    }

    @Nullable
    public String getColorModeDescription() {
        return getIndexedDescription(5, "Bitmap", "Grayscale", "Indexed", "RGB", "CMYK", null, null, "Multichannel", "Duotone", "Lab");
    }

    @Nullable
    public String getImageHeightDescription() {
        Integer value = ((PsdHeaderDirectory) this._directory).getInteger(2);
        if (value == null) {
            return null;
        }
        return value + " pixel" + (value.intValue() == 1 ? "" : "s");
    }

    @Nullable
    public String getImageWidthDescription() {
        try {
            Integer value = ((PsdHeaderDirectory) this._directory).getInteger(3);
            if (value == null) {
                return null;
            }
            return value + " pixel" + (value.intValue() == 1 ? "" : "s");
        } catch (Exception e) {
            return null;
        }
    }
}
