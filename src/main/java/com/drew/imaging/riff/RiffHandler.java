package com.drew.imaging.riff;

import com.drew.lang.annotations.NotNull;

public interface RiffHandler {
    void processChunk(@NotNull String str, @NotNull byte[] bArr);

    boolean shouldAcceptChunk(@NotNull String str);

    boolean shouldAcceptRiffIdentifier(@NotNull String str);
}
