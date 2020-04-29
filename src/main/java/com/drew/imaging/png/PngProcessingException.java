package com.drew.imaging.png;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.annotations.Nullable;

public class PngProcessingException extends ImageProcessingException {
    private static final long serialVersionUID = -687991554932005033L;

    public PngProcessingException(@Nullable String message) {
        super(message);
    }

    public PngProcessingException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public PngProcessingException(@Nullable Throwable cause) {
        super(cause);
    }
}
