package com.drew.metadata.ico;

import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;

public class IcoReader {
    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        reader.setMotorolaByteOrder(false);
        try {
            if (reader.getUInt16() != 0) {
                IcoDirectory directory = new IcoDirectory();
                directory.addError("Invalid header bytes");
                metadata.addDirectory(directory);
                return;
            }
            int type = reader.getUInt16();
            if (type == 1 || type == 2) {
                int imageCount = reader.getUInt16();
                if (imageCount == 0) {
                    IcoDirectory directory2 = new IcoDirectory();
                    directory2.addError("Image count cannot be zero");
                    metadata.addDirectory(directory2);
                    return;
                }
                for (int imageIndex = 0; imageIndex < imageCount; imageIndex++) {
                    IcoDirectory directory3 = new IcoDirectory();
                    try {
                        directory3.setInt(1, type);
                        directory3.setInt(2, reader.getUInt8());
                        directory3.setInt(3, reader.getUInt8());
                        directory3.setInt(4, reader.getUInt8());
                        reader.getUInt8();
                        if (type == 1) {
                            directory3.setInt(5, reader.getUInt16());
                            directory3.setInt(7, reader.getUInt16());
                        } else {
                            directory3.setInt(6, reader.getUInt16());
                            directory3.setInt(8, reader.getUInt16());
                        }
                        directory3.setLong(9, reader.getUInt32());
                        directory3.setLong(10, reader.getUInt32());
                    } catch (IOException ex) {
                        directory3.addError("Exception reading ICO file metadata: " + ex.getMessage());
                    }
                    metadata.addDirectory(directory3);
                }
                return;
            }
            IcoDirectory directory4 = new IcoDirectory();
            directory4.addError("Invalid type " + type + " -- expecting 1 or 2");
            metadata.addDirectory(directory4);
        } catch (IOException ex2) {
            IcoDirectory directory5 = new IcoDirectory();
            directory5.addError("Exception reading ICO file metadata: " + ex2.getMessage());
            metadata.addDirectory(directory5);
        }
    }
}
