package com.drew.metadata.jfif;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import java.io.IOException;
import java.util.Collections;

public class JfifReader implements JpegSegmentMetadataReader, MetadataReader {
    public static final String PREAMBLE = "JFIF";

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APP0);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= PREAMBLE.length() && PREAMBLE.equals(new String(segmentBytes, 0, PREAMBLE.length()))) {
                extract(new ByteArrayReader(segmentBytes), metadata);
            }
        }
    }

    public void extract(@NotNull RandomAccessReader reader, @NotNull Metadata metadata) {
        JfifDirectory directory = new JfifDirectory();
        metadata.addDirectory(directory);
        try {
            directory.setInt(5, reader.getUInt16(5));
            directory.setInt(7, reader.getUInt8(7));
            directory.setInt(8, reader.getUInt16(8));
            directory.setInt(10, reader.getUInt16(10));
            directory.setInt(12, reader.getUInt8(12));
            directory.setInt(13, reader.getUInt8(13));
        } catch (IOException me) {
            directory.addError(me.getMessage());
        }
    }
}
