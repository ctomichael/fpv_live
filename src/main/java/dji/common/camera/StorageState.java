package dji.common.camera;

import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class StorageState {
    private long availableCaptureCount;
    private int availableRecordingTime;
    private boolean hasError;
    private boolean isFormatted;
    private boolean isFormatting;
    private boolean isFull;
    private boolean isInitializing;
    private boolean isInserted;
    private boolean isInvalidFormat;
    private boolean isReadOnly;
    private boolean isVerified;
    private int remainingSpace;
    private SettingsDefinitions.StorageLocation storageLocation;
    private int totalSpace;

    public interface Callback {
        void onUpdate(@NonNull StorageState storageState);
    }

    private StorageState(Builder builder) {
        this.isInitializing = builder.isInitializing;
        this.hasError = builder.hasError;
        this.isReadOnly = builder.isReadOnly;
        this.isInvalidFormat = builder.isInvalidFormat;
        this.isFormatting = builder.isFormatting;
        this.isFormatted = builder.isFormatted;
        this.isFull = builder.isFull;
        this.availableCaptureCount = builder.availableCaptureCount;
        this.isVerified = builder.isVerified;
        this.isInserted = builder.isInserted;
        this.totalSpace = builder.totalSpace;
        this.availableRecordingTime = builder.availableRecordingTime;
        this.remainingSpace = builder.remainingSpace;
        this.storageLocation = builder.storageLocation;
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
        int i8 = 1;
        int i9 = 0;
        if (this.isInitializing) {
            result = 1;
        } else {
            result = 0;
        }
        int i10 = result * 31;
        if (this.hasError) {
            i = 1;
        } else {
            i = 0;
        }
        int i11 = (i10 + i) * 31;
        if (this.isReadOnly) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i12 = (i11 + i2) * 31;
        if (this.isInvalidFormat) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i13 = (i12 + i3) * 31;
        if (this.isFormatting) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i14 = (i13 + i4) * 31;
        if (this.isFormatted) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i15 = (i14 + i5) * 31;
        if (this.isFull) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int i16 = (((i15 + i6) * 31) + ((int) (this.availableCaptureCount ^ (this.availableCaptureCount >>> 32)))) * 31;
        if (this.isVerified) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        int i17 = (i16 + i7) * 31;
        if (!this.isInserted) {
            i8 = 0;
        }
        int i18 = (((((((i17 + i8) * 31) + this.totalSpace) * 31) + this.availableRecordingTime) * 31) + this.remainingSpace) * 31;
        if (this.storageLocation != null) {
            i9 = this.storageLocation.hashCode();
        }
        return i18 + i9;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof StorageState)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.isInitializing == ((StorageState) o).isInitializing && this.hasError == ((StorageState) o).hasError && this.isReadOnly == ((StorageState) o).isReadOnly && this.isInvalidFormat == ((StorageState) o).isInvalidFormat && this.isFormatting == ((StorageState) o).isFormatting && this.isFormatted == ((StorageState) o).isFormatted && this.isFull == ((StorageState) o).isFull && this.availableCaptureCount == ((StorageState) o).availableCaptureCount && this.isVerified == ((StorageState) o).isVerified && this.isInserted == ((StorageState) o).isInserted && this.totalSpace == ((StorageState) o).totalSpace && this.availableRecordingTime == ((StorageState) o).availableRecordingTime && this.remainingSpace == ((StorageState) o).remainingSpace && this.storageLocation == ((StorageState) o).storageLocation;
    }

    public boolean isInitializing() {
        return this.isInitializing;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public boolean isInvalidFormat() {
        return this.isInvalidFormat;
    }

    public boolean isFormatted() {
        return this.isFormatted;
    }

    public boolean isFormatting() {
        return this.isFormatting;
    }

    public boolean isFull() {
        return this.isFull;
    }

    public boolean isVerified() {
        return this.isVerified;
    }

    public boolean isInserted() {
        return this.isInserted;
    }

    public int getTotalSpaceInMB() {
        return this.totalSpace;
    }

    public void totalSpaceInMB(int totalSpace2) {
        this.totalSpace = totalSpace2;
    }

    public int getRemainingSpaceInMB() {
        return this.remainingSpace;
    }

    public void remainingSpaceInMB(int remainSpace) {
        this.remainingSpace = remainSpace;
    }

    public long getAvailableCaptureCount() {
        return this.availableCaptureCount;
    }

    public int getAvailableRecordingTimeInSeconds() {
        return this.availableRecordingTime;
    }

    public SettingsDefinitions.StorageLocation getStorageLocation() {
        return this.storageLocation;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public long availableCaptureCount;
        /* access modifiers changed from: private */
        public int availableRecordingTime;
        /* access modifiers changed from: private */
        public boolean hasError;
        /* access modifiers changed from: private */
        public boolean isFormatted;
        /* access modifiers changed from: private */
        public boolean isFormatting;
        /* access modifiers changed from: private */
        public boolean isFull;
        /* access modifiers changed from: private */
        public boolean isInitializing;
        /* access modifiers changed from: private */
        public boolean isInserted;
        /* access modifiers changed from: private */
        public boolean isInvalidFormat;
        /* access modifiers changed from: private */
        public boolean isReadOnly;
        /* access modifiers changed from: private */
        public boolean isVerified;
        /* access modifiers changed from: private */
        public int remainingSpace;
        /* access modifiers changed from: private */
        public SettingsDefinitions.StorageLocation storageLocation;
        /* access modifiers changed from: private */
        public int totalSpace;

        public Builder isInitializing(boolean isInitializing2) {
            this.isInitializing = isInitializing2;
            return this;
        }

        public Builder hasError(boolean hasError2) {
            this.hasError = hasError2;
            return this;
        }

        public Builder isReadOnly(boolean isReadOnly2) {
            this.isReadOnly = isReadOnly2;
            return this;
        }

        public Builder isInvalidFormat(boolean isInvalidFormat2) {
            this.isInvalidFormat = isInvalidFormat2;
            return this;
        }

        public Builder isFormatted(boolean isFormatted2) {
            this.isFormatted = isFormatted2;
            return this;
        }

        public Builder isFormatting(boolean isFormatting2) {
            this.isFormatting = isFormatting2;
            return this;
        }

        public Builder isFull(boolean isFull2) {
            this.isFull = isFull2;
            return this;
        }

        public Builder isVerified(boolean isVerified2) {
            this.isVerified = isVerified2;
            return this;
        }

        public Builder isInserted(boolean isInserted2) {
            this.isInserted = isInserted2;
            return this;
        }

        public Builder totalSpace(int totalSpace2) {
            this.totalSpace = totalSpace2;
            return this;
        }

        public Builder remainingSpace(int remainingSpace2) {
            this.remainingSpace = remainingSpace2;
            return this;
        }

        public Builder availableCaptureCount(long availableCaptureCount2) {
            this.availableCaptureCount = availableCaptureCount2;
            return this;
        }

        public Builder availableRecordingTime(int availableRecordingTime2) {
            this.availableRecordingTime = availableRecordingTime2;
            return this;
        }

        public Builder storageLocation(SettingsDefinitions.StorageLocation location) {
            this.storageLocation = location;
            return this;
        }

        public StorageState build() {
            return new StorageState(this);
        }
    }
}
