package com.drew.lang;

import java.io.IOException;

public final class BufferBoundsException extends IOException {
    private static final long serialVersionUID = 2911102837808946396L;

    public BufferBoundsException(int index, int bytesRequested, long bufferLength) {
        super(getMessage(index, bytesRequested, bufferLength));
    }

    public BufferBoundsException(String message) {
        super(message);
    }

    private static String getMessage(int index, int bytesRequested, long bufferLength) {
        if (index < 0) {
            return String.format("Attempt to read from buffer using a negative index (%d)", Integer.valueOf(index));
        } else if (bytesRequested < 0) {
            return String.format("Number of requested bytes cannot be negative (%d)", Integer.valueOf(bytesRequested));
        } else if ((((long) index) + ((long) bytesRequested)) - 1 > 2147483647L) {
            return String.format("Number of requested bytes summed with starting index exceed maximum range of signed 32 bit integers (requested index: %d, requested count: %d)", Integer.valueOf(index), Integer.valueOf(bytesRequested));
        } else {
            return String.format("Attempt to read from beyond end of underlying data source (requested index: %d, requested count: %d, max index: %d)", Integer.valueOf(index), Integer.valueOf(bytesRequested), Long.valueOf(bufferLength - 1));
        }
    }
}
