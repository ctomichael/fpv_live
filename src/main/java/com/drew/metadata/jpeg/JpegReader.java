package com.drew.metadata.jpeg;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.util.Arrays;

public class JpegReader implements JpegSegmentMetadataReader {
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Arrays.asList(JpegSegmentType.SOF0, JpegSegmentType.SOF1, JpegSegmentType.SOF2, JpegSegmentType.SOF3, JpegSegmentType.SOF5, JpegSegmentType.SOF6, JpegSegmentType.SOF7, JpegSegmentType.SOF9, JpegSegmentType.SOF10, JpegSegmentType.SOF11, JpegSegmentType.SOF13, JpegSegmentType.SOF14, JpegSegmentType.SOF15);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] segmentBytes : segments) {
            extract(segmentBytes, metadata, segmentType);
        }
    }

    public void extract(byte[] segmentBytes, Metadata metadata, JpegSegmentType segmentType) {
        JpegDirectory directory = new JpegDirectory();
        metadata.addDirectory(directory);
        directory.setInt(-3, segmentType.byteValue - JpegSegmentType.SOF0.byteValue);
        SequentialReader reader = new SequentialByteArrayReader(segmentBytes);
        try {
            directory.setInt(0, reader.getUInt8());
            directory.setInt(1, reader.getUInt16());
            directory.setInt(3, reader.getUInt16());
            short componentCount = reader.getUInt8();
            directory.setInt(5, componentCount);
            for (int i = 0; i < componentCount; i++) {
                directory.setObject(i + 6, new JpegComponent(reader.getUInt8(), reader.getUInt8(), reader.getUInt8()));
            }
        } catch (IOException ex) {
            directory.addError(ex.getMessage());
        }
    }
}
