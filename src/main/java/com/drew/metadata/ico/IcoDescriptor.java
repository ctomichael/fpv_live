package com.drew.metadata.ico;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class IcoDescriptor extends TagDescriptor<IcoDirectory> {
    public IcoDescriptor(@NotNull IcoDirectory directory) {
        super(directory);
    }

    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getImageTypeDescription();
            case 2:
                return getImageWidthDescription();
            case 3:
                return getImageHeightDescription();
            case 4:
                return getColourPaletteSizeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getImageTypeDescription() {
        return getIndexedDescription(1, 1, "Icon", "Cursor");
    }

    @Nullable
    public String getImageWidthDescription() {
        Integer width = ((IcoDirectory) this._directory).getInteger(2);
        if (width == null) {
            return null;
        }
        return (width.intValue() == 0 ? 256 : width.intValue()) + " pixels";
    }

    @Nullable
    public String getImageHeightDescription() {
        Integer width = ((IcoDirectory) this._directory).getInteger(3);
        if (width == null) {
            return null;
        }
        return (width.intValue() == 0 ? 256 : width.intValue()) + " pixels";
    }

    @Nullable
    public String getColourPaletteSizeDescription() {
        Integer size = ((IcoDirectory) this._directory).getInteger(4);
        if (size == null) {
            return null;
        }
        if (size.intValue() == 0) {
            return "No palette";
        }
        return size + " colour" + (size.intValue() == 1 ? "" : "s");
    }
}
