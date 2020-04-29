package com.drew.metadata.jpeg;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.jpeg.HuffmanTablesDirectory;
import java.io.IOException;
import java.util.Collections;

public class JpegDhtReader implements JpegSegmentMetadataReader {
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.DHT);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] segmentBytes : segments) {
            extract(new SequentialByteArrayReader(segmentBytes), metadata);
        }
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        HuffmanTablesDirectory directory = (HuffmanTablesDirectory) metadata.getFirstDirectoryOfType(HuffmanTablesDirectory.class);
        if (directory == null) {
            directory = new HuffmanTablesDirectory();
            metadata.addDirectory(directory);
        }
        while (reader.available() > 0) {
            try {
                int header = reader.getByte();
                HuffmanTablesDirectory.HuffmanTable.HuffmanTableClass tableClass = HuffmanTablesDirectory.HuffmanTable.HuffmanTableClass.typeOf((header & 240) >> 4);
                int tableDestinationId = header & 15;
                byte[] lBytes = getBytes(reader, 16);
                int vCount = 0;
                for (byte b : lBytes) {
                    vCount += b & 255;
                }
                directory.getTables().add(new HuffmanTablesDirectory.HuffmanTable(tableClass, tableDestinationId, lBytes, getBytes(reader, vCount)));
            } catch (IOException me) {
                directory.addError(me.getMessage());
            }
        }
        directory.setInt(1, directory.getTables().size());
    }

    private byte[] getBytes(@NotNull SequentialReader reader, int count) throws IOException {
        byte stuffing;
        byte[] bytes = new byte[count];
        int i = 0;
        while (i < count) {
            byte b = reader.getByte();
            if ((b & 255) != 255 || (stuffing = reader.getByte()) == 0) {
                bytes[i] = b;
                i++;
            } else {
                throw new IOException("Marker " + JpegSegmentType.fromByte(stuffing) + " found inside DHT segment");
            }
        }
        return bytes;
    }
}
