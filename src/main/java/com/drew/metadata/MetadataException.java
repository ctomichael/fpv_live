package com.drew.metadata;

import com.drew.lang.CompoundException;
import com.drew.lang.annotations.Nullable;

public class MetadataException extends CompoundException {
    private static final long serialVersionUID = 8612756143363919682L;

    public MetadataException(@Nullable String msg) {
        super(msg);
    }

    public MetadataException(@Nullable Throwable exception) {
        super(exception);
    }

    public MetadataException(@Nullable String msg, @Nullable Throwable innerException) {
        super(msg, innerException);
    }
}
