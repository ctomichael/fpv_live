package com.drew.metadata.bmp;

import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;

public class BmpReader {
    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        BmpHeaderDirectory directory = new BmpHeaderDirectory();
        metadata.addDirectory(directory);
        reader.setMotorolaByteOrder(false);
        try {
            if (reader.getUInt16() != 19778) {
                directory.addError("Invalid BMP magic number");
                return;
            }
            reader.skip(12);
            int headerSize = reader.getInt32();
            directory.setInt(-1, headerSize);
            if (headerSize == 40) {
                directory.setInt(2, reader.getInt32());
                directory.setInt(1, reader.getInt32());
                directory.setInt(3, reader.getInt16());
                directory.setInt(4, reader.getInt16());
                directory.setInt(5, reader.getInt32());
                reader.skip(4);
                directory.setInt(6, reader.getInt32());
                directory.setInt(7, reader.getInt32());
                directory.setInt(8, reader.getInt32());
                directory.setInt(9, reader.getInt32());
            } else if (headerSize == 12) {
                directory.setInt(2, reader.getInt16());
                directory.setInt(1, reader.getInt16());
                directory.setInt(3, reader.getInt16());
                directory.setInt(4, reader.getInt16());
            } else {
                directory.addError("Unexpected DIB header size: " + headerSize);
            }
        } catch (IOException e) {
            directory.addError("Unable to read BMP header");
        }
    }
}
