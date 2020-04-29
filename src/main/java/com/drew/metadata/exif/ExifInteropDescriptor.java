package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;

public class ExifInteropDescriptor extends ExifDescriptorBase<ExifInteropDirectory> {
    public ExifInteropDescriptor(@NotNull ExifInteropDirectory directory) {
        super(directory);
    }
}
