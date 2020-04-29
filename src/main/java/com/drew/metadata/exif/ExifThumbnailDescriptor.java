package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

public class ExifThumbnailDescriptor extends ExifDescriptorBase<ExifThumbnailDirectory> {
    public ExifThumbnailDescriptor(@NotNull ExifThumbnailDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 513:
                return getThumbnailOffsetDescription();
            case 514:
                return getThumbnailLengthDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getThumbnailLengthDescription() {
        String value = ((ExifThumbnailDirectory) this._directory).getString(514);
        if (value == null) {
            return null;
        }
        return value + " bytes";
    }

    @Nullable
    public String getThumbnailOffsetDescription() {
        String value = ((ExifThumbnailDirectory) this._directory).getString(513);
        if (value == null) {
            return null;
        }
        return value + " bytes";
    }
}
