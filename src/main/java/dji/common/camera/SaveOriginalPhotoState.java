package dji.common.camera;

import dji.common.camera.SettingsDefinitions;

public class SaveOriginalPhotoState {
    /* access modifiers changed from: private */
    public SettingsDefinitions.PhotoFileFormat hyperLapseFileType;
    /* access modifiers changed from: private */
    public boolean hyperLapseState;
    /* access modifiers changed from: private */
    public SettingsDefinitions.PhotoFileFormat panoFileType;
    /* access modifiers changed from: private */
    public boolean panoState;

    private SaveOriginalPhotoState() {
        this.hyperLapseFileType = SettingsDefinitions.PhotoFileFormat.JPEG;
        this.panoFileType = SettingsDefinitions.PhotoFileFormat.JPEG;
    }

    private SaveOriginalPhotoState(boolean hyperLapseState2, SettingsDefinitions.PhotoFileFormat hyperLapseFileType2, boolean panoState2, SettingsDefinitions.PhotoFileFormat panoFileType2) {
        this.hyperLapseFileType = SettingsDefinitions.PhotoFileFormat.JPEG;
        this.panoFileType = SettingsDefinitions.PhotoFileFormat.JPEG;
        this.hyperLapseState = hyperLapseState2;
        this.hyperLapseFileType = hyperLapseFileType2;
        this.panoState = panoState2;
        this.panoFileType = panoFileType2;
    }

    private SaveOriginalPhotoState(SaveOriginalPhotoState rightObject) {
        this.hyperLapseFileType = SettingsDefinitions.PhotoFileFormat.JPEG;
        this.panoFileType = SettingsDefinitions.PhotoFileFormat.JPEG;
        this.hyperLapseState = rightObject.hyperLapseState;
        this.hyperLapseFileType = rightObject.hyperLapseFileType;
        this.panoState = rightObject.panoState;
        this.panoFileType = rightObject.panoFileType;
    }

    public boolean isHyperLapseState() {
        return this.hyperLapseState;
    }

    public SettingsDefinitions.PhotoFileFormat getHyperLapseFileType() {
        return this.hyperLapseFileType;
    }

    public boolean isPanoState() {
        return this.panoState;
    }

    public SettingsDefinitions.PhotoFileFormat getPanoFileType() {
        return this.panoFileType;
    }

    public int hashCode() {
        int result;
        int i = 1;
        if (this.hyperLapseState) {
            result = 1;
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (!this.panoState) {
            i = 0;
        }
        return ((((i2 + i) * 31) + this.hyperLapseFileType.value()) * 31) + this.panoFileType.value();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof SaveOriginalPhotoState)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        SaveOriginalPhotoState rightObject = (SaveOriginalPhotoState) o;
        if (this.hyperLapseState == rightObject.hyperLapseState && this.panoState == rightObject.panoState && this.hyperLapseFileType.value() == rightObject.hyperLapseFileType.value() && this.panoFileType.value() == rightObject.panoFileType.value()) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "{hyper=" + this.hyperLapseState + " pano=" + this.panoState + "}";
    }

    public static class Builder {
        private SaveOriginalPhotoState mSaveOriginalPhotoState = new SaveOriginalPhotoState();

        public Builder() {
        }

        public Builder(SaveOriginalPhotoState outerObject) {
            this.mSaveOriginalPhotoState = new SaveOriginalPhotoState();
        }

        public Builder setHyperLapseState(boolean hyperLapseState) {
            boolean unused = this.mSaveOriginalPhotoState.hyperLapseState = hyperLapseState;
            return this;
        }

        public Builder setPanoState(boolean panoState) {
            boolean unused = this.mSaveOriginalPhotoState.panoState = panoState;
            return this;
        }

        public Builder setHyperLapseFileType(SettingsDefinitions.PhotoFileFormat hyperLapseFileType) {
            SettingsDefinitions.PhotoFileFormat unused = this.mSaveOriginalPhotoState.hyperLapseFileType = hyperLapseFileType;
            return this;
        }

        public Builder setPanoFileType(SettingsDefinitions.PhotoFileFormat panoFileType) {
            SettingsDefinitions.PhotoFileFormat unused = this.mSaveOriginalPhotoState.panoFileType = panoFileType;
            return this;
        }

        public SaveOriginalPhotoState build() {
            return this.mSaveOriginalPhotoState;
        }
    }
}
