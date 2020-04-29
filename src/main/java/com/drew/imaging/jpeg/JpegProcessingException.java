package com.drew.imaging.jpeg;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.annotations.Nullable;

public class JpegProcessingException extends ImageProcessingException {
    private static final long serialVersionUID = -7870179776125450158L;

    public JpegProcessingException(@Nullable String message) {
        super(message);
    }

    public JpegProcessingException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public JpegProcessingException(@Nullable Throwable cause) {
        super(cause);
    }
}
