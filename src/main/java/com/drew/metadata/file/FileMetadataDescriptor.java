package com.drew.metadata.file;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class FileMetadataDescriptor extends TagDescriptor<FileMetadataDirectory> {
    public FileMetadataDescriptor(@NotNull FileMetadataDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 2:
                return getFileSizeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    private String getFileSizeDescription() {
        Long size = ((FileMetadataDirectory) this._directory).getLongObject(2);
        if (size == null) {
            return null;
        }
        return Long.toString(size.longValue()) + " bytes";
    }
}
