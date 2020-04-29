package com.drew.imaging.bmp;

import com.drew.lang.StreamReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.bmp.BmpReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BmpMetadataReader {
    @NotNull
    public static Metadata readMetadata(@NotNull File file) throws IOException {
        FileInputStream stream = null;
        try {
            FileInputStream stream2 = new FileInputStream(file);
            try {
                Metadata readMetadata = readMetadata(stream2);
                if (stream2 != null) {
                    stream2.close();
                }
                return readMetadata;
            } catch (Throwable th) {
                th = th;
                stream = stream2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (stream != null) {
                stream.close();
            }
            throw th;
        }
    }

    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream) {
        Metadata metadata = new Metadata();
        new BmpReader().extract(new StreamReader(inputStream), metadata);
        return metadata;
    }
}
