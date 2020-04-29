package com.drew.metadata.xmp;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.TagDescriptor;

public class XmpDescriptor extends TagDescriptor<XmpDirectory> {
    public XmpDescriptor(@NotNull XmpDirectory directory) {
        super(directory);
    }
}
