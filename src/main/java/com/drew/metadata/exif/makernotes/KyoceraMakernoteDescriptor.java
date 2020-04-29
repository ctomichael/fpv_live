package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class KyoceraMakernoteDescriptor extends TagDescriptor<KyoceraMakernoteDirectory> {
    public KyoceraMakernoteDescriptor(@NotNull KyoceraMakernoteDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getProprietaryThumbnailDataDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getProprietaryThumbnailDataDescription() {
        return getByteLengthDescription(1);
    }
}
