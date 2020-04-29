package com.drew.imaging.riff;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.annotations.Nullable;

public class RiffProcessingException extends ImageProcessingException {
    private static final long serialVersionUID = -1658134596321487960L;

    public RiffProcessingException(@Nullable String message) {
        super(message);
    }

    public RiffProcessingException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public RiffProcessingException(@Nullable Throwable cause) {
        super(cause);
    }
}
