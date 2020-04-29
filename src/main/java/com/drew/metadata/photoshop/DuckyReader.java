package com.drew.metadata.photoshop;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.Charsets;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.util.Collections;

public class DuckyReader implements JpegSegmentMetadataReader {
    @NotNull
    private static final String JPEG_SEGMENT_PREAMBLE = "Ducky";

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APPC);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        int preambleLength = JPEG_SEGMENT_PREAMBLE.length();
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= preambleLength && JPEG_SEGMENT_PREAMBLE.equals(new String(segmentBytes, 0, preambleLength))) {
                extract(new SequentialByteArrayReader(segmentBytes, preambleLength), metadata);
            }
        }
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        DuckyDirectory directory = new DuckyDirectory();
        metadata.addDirectory(directory);
        while (true) {
            try {
                int tag = reader.getUInt16();
                if (tag != 0) {
                    int length = reader.getUInt16();
                    switch (tag) {
                        case 1:
                            if (length == 4) {
                                directory.setInt(tag, reader.getInt32());
                                break;
                            } else {
                                directory.addError("Unexpected length for the quality tag");
                                return;
                            }
                        case 2:
                        case 3:
                            reader.skip(4);
                            directory.setStringValue(tag, reader.getStringValue(length - 4, Charsets.UTF_16BE));
                            break;
                        default:
                            directory.setByteArray(tag, reader.getBytes(length));
                            break;
                    }
                } else {
                    return;
                }
            } catch (IOException e) {
                directory.addError(e.getMessage());
                return;
            }
        }
    }
}
