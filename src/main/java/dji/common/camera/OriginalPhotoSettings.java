package dji.common.camera;

import dji.common.camera.SettingsDefinitions;

public class OriginalPhotoSettings {
    private final SettingsDefinitions.PhotoFileFormat format;
    private final boolean shouldSaveOriginalPhotos;

    public OriginalPhotoSettings(boolean shouldSaveOriginalPhotos2) {
        this.shouldSaveOriginalPhotos = shouldSaveOriginalPhotos2;
        this.format = SettingsDefinitions.PhotoFileFormat.JPEG;
    }

    public OriginalPhotoSettings(boolean shouldSaveOriginalPhotos2, SettingsDefinitions.PhotoFileFormat format2) {
        this.shouldSaveOriginalPhotos = shouldSaveOriginalPhotos2;
        this.format = format2;
    }

    public boolean shouldSaveOriginalPhotos() {
        return this.shouldSaveOriginalPhotos;
    }

    public SettingsDefinitions.PhotoFileFormat getFormat() {
        return this.format;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OriginalPhotoSettings that = (OriginalPhotoSettings) o;
        if (shouldSaveOriginalPhotos() != that.shouldSaveOriginalPhotos()) {
            return false;
        }
        if (getFormat() != that.getFormat()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (shouldSaveOriginalPhotos()) {
            result = 1;
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (getFormat() != null) {
            i = getFormat().hashCode();
        }
        return i2 + i;
    }
}
