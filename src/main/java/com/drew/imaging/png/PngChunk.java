package com.drew.imaging.png;

import com.drew.lang.annotations.NotNull;

public class PngChunk {
    @NotNull
    private final byte[] _bytes;
    @NotNull
    private final PngChunkType _chunkType;

    public PngChunk(@NotNull PngChunkType chunkType, @NotNull byte[] bytes) {
        this._chunkType = chunkType;
        this._bytes = bytes;
    }

    @NotNull
    public PngChunkType getType() {
        return this._chunkType;
    }

    @NotNull
    public byte[] getBytes() {
        return this._bytes;
    }
}
