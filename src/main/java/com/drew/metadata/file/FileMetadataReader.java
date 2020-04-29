package com.drew.metadata.file;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileMetadataReader {
    public void read(@NotNull File file, @NotNull Metadata metadata) throws IOException {
        if (!file.isFile()) {
            throw new IOException("File object must reference a file");
        } else if (!file.exists()) {
            throw new IOException("File does not exist");
        } else if (!file.canRead()) {
            throw new IOException("File is not readable");
        } else {
            FileMetadataDirectory directory = new FileMetadataDirectory();
            directory.setString(1, file.getName());
            directory.setLong(2, file.length());
            directory.setDate(3, new Date(file.lastModified()));
            metadata.addDirectory(directory);
        }
    }
}
