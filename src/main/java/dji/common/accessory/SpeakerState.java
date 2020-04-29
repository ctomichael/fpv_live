package dji.common.accessory;

import dji.common.accessory.SettingsDefinitions;

public final class SpeakerState {
    private final int index;
    private final SettingsDefinitions.PlayMode playingMode;
    private final SettingsDefinitions.SpeakerPlayingState playingState;
    private final SettingsDefinitions.AudioStorageLocation storageLocation;
    private final int volume;

    public interface Callback {
        void onUpdate(SpeakerState speakerState);
    }

    public SpeakerState(Builder builder) {
        this.playingState = builder.playingState;
        this.index = builder.index;
        this.playingMode = builder.playingMode;
        this.storageLocation = builder.storageLocation;
        this.volume = builder.volume;
    }

    public SettingsDefinitions.SpeakerPlayingState getPlayingState() {
        return this.playingState;
    }

    public int getPlayingIndex() {
        return this.index;
    }

    public SettingsDefinitions.PlayMode getPlayingMode() {
        return this.playingMode;
    }

    public SettingsDefinitions.AudioStorageLocation getStorageLocation() {
        return this.storageLocation;
    }

    public int getVolume() {
        return this.volume;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpeakerState that = (SpeakerState) o;
        if (getPlayingIndex() != that.getPlayingIndex() || getVolume() != that.getVolume() || getPlayingState() != that.getPlayingState() || getPlayingMode() != that.getPlayingMode()) {
            return false;
        }
        if (getStorageLocation() != that.getStorageLocation()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (getPlayingState() != null) {
            result = getPlayingState().hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (getPlayingMode() != null) {
            i = getPlayingMode().hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (getStorageLocation() != null) {
            i2 = getStorageLocation().hashCode();
        }
        return ((((i4 + i2) * 31) + getPlayingIndex()) * 31) + getVolume();
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int index;
        /* access modifiers changed from: private */
        public SettingsDefinitions.PlayMode playingMode;
        /* access modifiers changed from: private */
        public SettingsDefinitions.SpeakerPlayingState playingState;
        /* access modifiers changed from: private */
        public SettingsDefinitions.AudioStorageLocation storageLocation;
        /* access modifiers changed from: private */
        public int volume;

        public Builder playingState(SettingsDefinitions.SpeakerPlayingState playingState2) {
            this.playingState = playingState2;
            return this;
        }

        public Builder playingMode(SettingsDefinitions.PlayMode playingMode2) {
            this.playingMode = playingMode2;
            return this;
        }

        public Builder storageLocation(SettingsDefinitions.AudioStorageLocation storageLocation2) {
            this.storageLocation = storageLocation2;
            return this;
        }

        public Builder index(int index2) {
            this.index = index2;
            return this;
        }

        public Builder volume(int volume2) {
            this.volume = volume2;
            return this;
        }

        public SpeakerState build() {
            return new SpeakerState(this);
        }
    }
}
