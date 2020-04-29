package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Metadata {
    @NotNull
    private final List<Directory> _directories = new ArrayList();

    @NotNull
    public Iterable<Directory> getDirectories() {
        return this._directories;
    }

    @NotNull
    public <T extends Directory> Collection<T> getDirectoriesOfType(Class<T> type) {
        List<T> directories = new ArrayList<>();
        for (Directory dir : this._directories) {
            if (type.isAssignableFrom(dir.getClass())) {
                directories.add(dir);
            }
        }
        return directories;
    }

    public int getDirectoryCount() {
        return this._directories.size();
    }

    public <T extends Directory> void addDirectory(@NotNull T directory) {
        this._directories.add(directory);
    }

    @Nullable
    public <T extends Directory> T getFirstDirectoryOfType(@NotNull Class<T> type) {
        for (Directory dir : this._directories) {
            if (type.isAssignableFrom(dir.getClass())) {
                return dir;
            }
        }
        return null;
    }

    public boolean containsDirectoryOfType(Class<? extends Directory> type) {
        for (Directory dir : this._directories) {
            if (type.isAssignableFrom(dir.getClass())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasErrors() {
        for (Directory directory : getDirectories()) {
            if (directory.hasErrors()) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        int count = getDirectoryCount();
        Object[] objArr = new Object[2];
        objArr[0] = Integer.valueOf(count);
        objArr[1] = count == 1 ? "directory" : "directories";
        return String.format("Metadata (%d %s)", objArr);
    }
}
