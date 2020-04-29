package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;

public class ExifSubIFDDescriptor extends ExifDescriptorBase<ExifSubIFDDirectory> {
    public ExifSubIFDDescriptor(@NotNull ExifSubIFDDirectory directory) {
        super(directory);
    }
}
