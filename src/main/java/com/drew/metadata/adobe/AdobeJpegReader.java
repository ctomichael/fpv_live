package com.drew.metadata.adobe;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.util.Collections;

public class AdobeJpegReader implements JpegSegmentMetadataReader {
    public static final String PREAMBLE = "Adobe";

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APPE);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] bytes : segments) {
            if (bytes.length == 12 && PREAMBLE.equalsIgnoreCase(new String(bytes, 0, PREAMBLE.length()))) {
                extract(new SequentialByteArrayReader(bytes), metadata);
            }
        }
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        Directory directory = new AdobeJpegDirectory();
        metadata.addDirectory(directory);
        try {
            reader.setMotorolaByteOrder(false);
            if (!reader.getString(PREAMBLE.length()).equals(PREAMBLE)) {
                directory.addError("Invalid Adobe JPEG data header.");
                return;
            }
            directory.setInt(0, reader.getUInt16());
            directory.setInt(1, reader.getUInt16());
            directory.setInt(2, reader.getUInt16());
            directory.setInt(3, reader.getInt8());
        } catch (IOException ex) {
            directory.addError("IO exception processing data: " + ex.getMessage());
        }
    }
}
