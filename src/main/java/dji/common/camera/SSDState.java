package dji.common.camera;

import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class SSDState {
    private int burstPhotoShootCount;
    private SSDCapacity capacity;
    private boolean isConnected;
    private long remainingSpaceInMB;
    private int remainingTime;
    private SSDOperationState ssdState;
    private SettingsDefinitions.VideoFrameRate videoFrameRate;
    private SettingsDefinitions.VideoResolution videoResolution;

    public interface Callback {
        void onUpdate(@NonNull SSDState sSDState);
    }

    private SSDState(Builder builder) {
        this.ssdState = builder.ssdState;
        this.isConnected = builder.isConnected;
        this.capacity = builder.capacity;
        this.remainingTime = builder.remainingTime;
        this.remainingSpaceInMB = builder.remainingSpaceInMB;
        this.videoResolution = builder.videoResolution;
        this.videoFrameRate = builder.videoFrameRate;
        this.burstPhotoShootCount = builder.burstPhotoShootCount;
    }

    public SSDOperationState getSSDOperationState() {
        return this.ssdState;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public SSDCapacity getCapacity() {
        return this.capacity;
    }

    public int getAvailableRecordingTimeInSeconds() {
        return this.remainingTime;
    }

    public long getRemainingSpaceInMB() {
        return this.remainingSpaceInMB;
    }

    public SettingsDefinitions.VideoResolution getVideoResolution() {
        return this.videoResolution;
    }

    public SettingsDefinitions.VideoFrameRate getVideoFrameRate() {
        return this.videoFrameRate;
    }

    public int getRAWPhotoBurstCount() {
        return this.burstPhotoShootCount;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        int hashCode = ((this.ssdState.hashCode() * 31) + (this.isConnected ? 1 : 0)) * 31;
        if (this.capacity != null) {
            i = this.capacity.hashCode();
        } else {
            i = 0;
        }
        int i4 = (((((hashCode + i) * 31) + this.remainingTime) * 31) + ((int) (this.remainingSpaceInMB ^ (this.remainingSpaceInMB >>> 32)))) * 31;
        if (this.videoResolution != null) {
            i2 = this.videoResolution.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 31;
        if (this.videoFrameRate != null) {
            i3 = this.videoFrameRate.hashCode();
        }
        return ((i5 + i3) * 31) + this.burstPhotoShootCount;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof SSDState)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.ssdState == ((SSDState) o).ssdState && this.isConnected == ((SSDState) o).isConnected && this.capacity == ((SSDState) o).capacity && this.remainingTime == ((SSDState) o).remainingTime && this.remainingSpaceInMB == ((SSDState) o).remainingSpaceInMB && this.videoResolution == ((SSDState) o).videoResolution && this.videoFrameRate == ((SSDState) o).videoFrameRate && this.burstPhotoShootCount == ((SSDState) o).burstPhotoShootCount;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int burstPhotoShootCount;
        /* access modifiers changed from: private */
        public SSDCapacity capacity;
        /* access modifiers changed from: private */
        public boolean isConnected;
        /* access modifiers changed from: private */
        public long remainingSpaceInMB;
        /* access modifiers changed from: private */
        public int remainingTime;
        /* access modifiers changed from: private */
        public SSDOperationState ssdState;
        /* access modifiers changed from: private */
        public SettingsDefinitions.VideoFrameRate videoFrameRate;
        /* access modifiers changed from: private */
        public SettingsDefinitions.VideoResolution videoResolution;

        public Builder ssdState(SSDOperationState ssdState2) {
            this.ssdState = ssdState2;
            return this;
        }

        public Builder isConnected(boolean isConnected2) {
            this.isConnected = isConnected2;
            return this;
        }

        public Builder capacity(SSDCapacity capacity2) {
            this.capacity = capacity2;
            return this;
        }

        public Builder remainingTime(int remainingTime2) {
            this.remainingTime = remainingTime2;
            return this;
        }

        public Builder remainingSpaceInMB(long remainingSpaceInMB2) {
            this.remainingSpaceInMB = remainingSpaceInMB2;
            return this;
        }

        public Builder videoResolution(SettingsDefinitions.VideoResolution videoResolution2) {
            this.videoResolution = videoResolution2;
            return this;
        }

        public Builder videoFrameRate(SettingsDefinitions.VideoFrameRate videoFrameRate2) {
            this.videoFrameRate = videoFrameRate2;
            return this;
        }

        public Builder burstPhotoShootCount(int burstPhotoShootCount2) {
            this.burstPhotoShootCount = burstPhotoShootCount2;
            return this;
        }

        public SSDState build() {
            return new SSDState(this);
        }
    }
}
