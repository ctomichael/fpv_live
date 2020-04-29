package com.drew.metadata;

import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;

public interface MetadataReader {
    void extract(@NotNull RandomAccessReader randomAccessReader, @NotNull Metadata metadata);
}
