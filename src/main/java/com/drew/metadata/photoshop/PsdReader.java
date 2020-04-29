package com.drew.metadata.photoshop;

import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;

public class PsdReader {
    static final /* synthetic */ boolean $assertionsDisabled = (!PsdReader.class.desiredAssertionStatus());

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        PsdHeaderDirectory directory = new PsdHeaderDirectory();
        metadata.addDirectory(directory);
        try {
            if (reader.getInt32() != 943870035) {
                directory.addError("Invalid PSD file signature");
                return;
            }
            int version = reader.getUInt16();
            if (version == 1 || version == 2) {
                reader.skip(6);
                directory.setInt(1, reader.getUInt16());
                directory.setInt(2, reader.getInt32());
                directory.setInt(3, reader.getInt32());
                directory.setInt(4, reader.getUInt16());
                directory.setInt(5, reader.getUInt16());
                try {
                    reader.skip(reader.getUInt32());
                    try {
                        long sectionLength = reader.getUInt32();
                        if ($assertionsDisabled || sectionLength <= 2147483647L) {
                            new PhotoshopReader().extract(reader, (int) sectionLength, metadata);
                            return;
                        }
                        throw new AssertionError();
                    } catch (IOException e) {
                    }
                } catch (IOException e2) {
                }
            } else {
                directory.addError("Invalid PSD file version (must be 1 or 2)");
            }
        } catch (IOException e3) {
            directory.addError("Unable to read PSD header");
        }
    }
}
