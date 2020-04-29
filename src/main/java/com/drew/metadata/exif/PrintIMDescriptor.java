package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class PrintIMDescriptor extends TagDescriptor<PrintIMDirectory> {
    public PrintIMDescriptor(@NotNull PrintIMDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return super.getDescription(tagType);
            default:
                Integer value = ((PrintIMDirectory) this._directory).getInteger(tagType);
                if (value == null) {
                    return null;
                }
                return String.format("0x%08x", value);
        }
    }
}
