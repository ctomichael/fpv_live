package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public final class StringValue {
    @NotNull
    private final byte[] _bytes;
    @Nullable
    private final Charset _charset;

    public StringValue(@NotNull byte[] bytes, @Nullable Charset charset) {
        this._bytes = bytes;
        this._charset = charset;
    }

    @NotNull
    public byte[] getBytes() {
        return this._bytes;
    }

    @Nullable
    public Charset getCharset() {
        return this._charset;
    }

    public String toString() {
        return toString(this._charset);
    }

    public String toString(@Nullable Charset charset) {
        if (charset != null) {
            try {
                return new String(this._bytes, charset.name());
            } catch (UnsupportedEncodingException e) {
            }
        }
        return new String(this._bytes);
    }
}
