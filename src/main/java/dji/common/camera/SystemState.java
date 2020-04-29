package dji.common.camera;

import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class SystemState {
    private int currentPanoramaPhotoCount;
    private int currentVideoRecordingTimeInSeconds;
    private boolean hasError;
    private boolean isOverHeating;
    private boolean isPhotoStoring;
    private boolean isRecording;
    private boolean isShootingBurstPhoto;
    private boolean isShootingIntervalPhoto;
    private boolean isShootingPanoramaPhoto;
    private boolean isShootingRAWBurstPhoto;
    private boolean isShootingRawPhoto;
    private boolean isShootingShallowFocusPhoto;
    private boolean isShootingSinglePhoto;
    private SettingsDefinitions.CameraMode mode;
    private int panoramaProgress;

    public interface Callback {
        void onUpdate(@NonNull SystemState systemState);
    }

    private SystemState(Builder builder) {
        this.isPhotoStoring = builder.isPhotoStoring;
        this.currentVideoRecordingTimeInSeconds = builder.currentVideoRecordingTimeInSeconds;
        this.mode = builder.mode;
        this.isShootingIntervalPhoto = builder.isShootingIntervalPhoto;
        this.isShootingBurstPhoto = builder.isShootingBurstPhoto;
        this.isShootingSinglePhoto = builder.isShootingSinglePhoto;
        this.isShootingRawPhoto = builder.isShootingRawPhoto;
        this.isRecording = builder.isRecording;
        this.isOverHeating = builder.isOverHeating;
        this.hasError = builder.hasError;
        this.isShootingRAWBurstPhoto = builder.isShootingRAWBurstPhoto;
        this.isShootingShallowFocusPhoto = builder.isShootingShallowFocusPhoto;
        this.isShootingPanoramaPhoto = builder.isShootingPanoramaPhoto;
        this.currentPanoramaPhotoCount = builder.currentPanoramaPhotoCount;
        this.panoramaProgress = builder.panoramaProgress;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10 = 1;
        if (this.isPhotoStoring) {
            result = 1;
        } else {
            result = 0;
        }
        int i11 = ((result * 31) + this.currentVideoRecordingTimeInSeconds) * 31;
        if (this.mode != null) {
            i = this.mode.hashCode();
        } else {
            i = 0;
        }
        int i12 = (i11 + i) * 31;
        if (this.isShootingIntervalPhoto) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i13 = (i12 + i2) * 31;
        if (this.isShootingBurstPhoto) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i14 = (i13 + i3) * 31;
        if (this.isShootingSinglePhoto) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i15 = (i14 + i4) * 31;
        if (this.isShootingRawPhoto) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i16 = (i15 + i5) * 31;
        if (this.isRecording) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int i17 = (i16 + i6) * 31;
        if (this.hasError) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        int i18 = (i17 + i7) * 31;
        if (this.isShootingRAWBurstPhoto) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        int i19 = (i18 + i8) * 31;
        if (this.isShootingShallowFocusPhoto) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        int i20 = (i19 + i9) * 31;
        if (!this.isShootingPanoramaPhoto) {
            i10 = 0;
        }
        return ((((i20 + i10) * 31) + this.currentPanoramaPhotoCount) * 31) + this.panoramaProgress;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof SystemState)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.isPhotoStoring == ((SystemState) o).isPhotoStoring && this.currentVideoRecordingTimeInSeconds == ((SystemState) o).currentVideoRecordingTimeInSeconds && this.mode == ((SystemState) o).mode && this.isShootingIntervalPhoto == ((SystemState) o).isShootingIntervalPhoto && this.isShootingBurstPhoto == ((SystemState) o).isShootingBurstPhoto && this.isShootingSinglePhoto == ((SystemState) o).isShootingSinglePhoto && this.isShootingRawPhoto == ((SystemState) o).isShootingRawPhoto && this.isRecording == ((SystemState) o).isRecording && this.isOverHeating == ((SystemState) o).isOverHeating && this.hasError == ((SystemState) o).hasError && this.isShootingRAWBurstPhoto == ((SystemState) o).isShootingRAWBurstPhoto && this.isShootingShallowFocusPhoto == ((SystemState) o).isShootingShallowFocusPhoto && this.isShootingPanoramaPhoto == ((SystemState) o).isShootingPanoramaPhoto && this.currentPanoramaPhotoCount == ((SystemState) o).currentPanoramaPhotoCount && this.panoramaProgress == ((SystemState) o).panoramaProgress;
    }

    public int getCurrentPanoramaPhotoCount() {
        return this.currentPanoramaPhotoCount;
    }

    public boolean isShootingShallowFocusPhoto() {
        return this.isShootingShallowFocusPhoto;
    }

    public boolean isShootingPanoramaPhoto() {
        return this.isShootingPanoramaPhoto;
    }

    public boolean isShootingRAWBurstPhoto() {
        return this.isShootingRAWBurstPhoto;
    }

    public boolean isShootingSinglePhoto() {
        return this.isShootingSinglePhoto;
    }

    public boolean isShootingSinglePhotoInRAWFormat() {
        return this.isShootingRawPhoto;
    }

    public boolean isShootingIntervalPhoto() {
        return this.isShootingIntervalPhoto;
    }

    public boolean isShootingBurstPhoto() {
        return this.isShootingBurstPhoto;
    }

    public boolean isStoringPhoto() {
        return this.isPhotoStoring;
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public boolean isOverheating() {
        return this.isOverHeating;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public SettingsDefinitions.CameraMode getMode() {
        return this.mode;
    }

    public int getCurrentVideoRecordingTimeInSeconds() {
        return this.currentVideoRecordingTimeInSeconds;
    }

    public int getPanoramaProgress() {
        return this.panoramaProgress;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int currentPanoramaPhotoCount;
        /* access modifiers changed from: private */
        public int currentVideoRecordingTimeInSeconds;
        /* access modifiers changed from: private */
        public boolean hasError;
        /* access modifiers changed from: private */
        public boolean isOverHeating;
        /* access modifiers changed from: private */
        public boolean isPhotoStoring;
        /* access modifiers changed from: private */
        public boolean isRecording;
        /* access modifiers changed from: private */
        public boolean isShootingBurstPhoto;
        /* access modifiers changed from: private */
        public boolean isShootingIntervalPhoto;
        /* access modifiers changed from: private */
        public boolean isShootingPanoramaPhoto;
        /* access modifiers changed from: private */
        public boolean isShootingRAWBurstPhoto;
        /* access modifiers changed from: private */
        public boolean isShootingRawPhoto;
        /* access modifiers changed from: private */
        public boolean isShootingShallowFocusPhoto;
        /* access modifiers changed from: private */
        public boolean isShootingSinglePhoto;
        /* access modifiers changed from: private */
        public SettingsDefinitions.CameraMode mode;
        /* access modifiers changed from: private */
        public int panoramaProgress;

        public Builder currentPanoramaPhotoCount(int currentPanoramaPhotoCount2) {
            this.currentPanoramaPhotoCount = currentPanoramaPhotoCount2;
            return this;
        }

        public Builder isShootingShallowFocusPhoto(boolean isShootingShallowFocusPhoto2) {
            this.isShootingShallowFocusPhoto = isShootingShallowFocusPhoto2;
            return this;
        }

        public Builder isShootingPanoramaPhoto(boolean isShootingPanoramaPhoto2) {
            this.isShootingPanoramaPhoto = isShootingPanoramaPhoto2;
            return this;
        }

        public Builder isPhotoStoring(boolean isPhotoStoring2) {
            this.isPhotoStoring = isPhotoStoring2;
            return this;
        }

        public Builder currentVideoRecordingTimeInSeconds(int currentVideoRecordingTimeInSeconds2) {
            this.currentVideoRecordingTimeInSeconds = currentVideoRecordingTimeInSeconds2;
            return this;
        }

        public Builder panoramaProgress(int panoramaProgress2) {
            this.panoramaProgress = panoramaProgress2;
            return this;
        }

        public Builder mode(SettingsDefinitions.CameraMode mode2) {
            this.mode = mode2;
            return this;
        }

        public Builder isShootingIntervalPhoto(boolean isShootingIntervalPhoto2) {
            this.isShootingIntervalPhoto = isShootingIntervalPhoto2;
            return this;
        }

        public Builder isShootingBurstPhoto(boolean isShootingBurstPhoto2) {
            this.isShootingBurstPhoto = isShootingBurstPhoto2;
            return this;
        }

        public Builder isShootingSinglePhoto(boolean isShootingSinglePhoto2) {
            this.isShootingSinglePhoto = isShootingSinglePhoto2;
            return this;
        }

        public Builder isShootingRawPhoto(boolean isShootingRawPhoto2) {
            this.isShootingRawPhoto = isShootingRawPhoto2;
            return this;
        }

        public Builder isRecording(boolean isRecording2) {
            this.isRecording = isRecording2;
            return this;
        }

        public Builder isOverHeating(boolean isOverHeating2) {
            this.isOverHeating = isOverHeating2;
            return this;
        }

        public Builder hasError(boolean hasError2) {
            this.hasError = hasError2;
            return this;
        }

        public Builder isShootingRAWBurstPhoto(boolean isShootingRAWBurstPhoto2) {
            this.isShootingRAWBurstPhoto = isShootingRAWBurstPhoto2;
            return this;
        }

        public SystemState build() {
            return new SystemState(this);
        }
    }
}
