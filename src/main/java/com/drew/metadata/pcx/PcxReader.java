package com.drew.metadata.pcx;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;

public class PcxReader {
    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        reader.setMotorolaByteOrder(false);
        PcxDirectory directory = new PcxDirectory();
        metadata.addDirectory(directory);
        try {
            if (reader.getInt8() != 10) {
                throw new ImageProcessingException("Invalid PCX identifier byte");
            }
            directory.setInt(1, reader.getInt8());
            if (reader.getInt8() != 1) {
                throw new ImageProcessingException("Invalid PCX encoding byte");
            }
            directory.setInt(2, reader.getUInt8());
            directory.setInt(3, reader.getUInt16());
            directory.setInt(4, reader.getUInt16());
            directory.setInt(5, reader.getUInt16());
            directory.setInt(6, reader.getUInt16());
            directory.setInt(7, reader.getUInt16());
            directory.setInt(8, reader.getUInt16());
            directory.setByteArray(9, reader.getBytes(48));
            reader.skip(1);
            directory.setInt(10, reader.getUInt8());
            directory.setInt(11, reader.getUInt16());
            int paletteType = reader.getUInt16();
            if (paletteType != 0) {
                directory.setInt(12, paletteType);
            }
            int hScrSize = reader.getUInt16();
            if (hScrSize != 0) {
                directory.setInt(13, hScrSize);
            }
            int vScrSize = reader.getUInt16();
            if (vScrSize != 0) {
                directory.setInt(14, vScrSize);
            }
        } catch (Exception ex) {
            directory.addError("Exception reading PCX file metadata: " + ex.getMessage());
        }
    }
}
