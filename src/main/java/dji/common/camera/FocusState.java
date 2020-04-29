package dji.common.camera;

import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FocusState {
    private SettingsDefinitions.FocusMode focusMode;
    private SettingsDefinitions.FocusStatus focusStatus;
    private boolean isAFSwitchOn;
    private boolean isFocusAssistantEnabledForAF;
    private boolean isFocusAssistantEnabledForMF;
    private boolean isFocusAssistantWorking;
    private boolean isLensMounted;
    private SettingsDefinitions.SensorCleaningState sensorCleaningState;

    public interface Callback {
        void onUpdate(@NonNull FocusState focusState);
    }

    private FocusState(Builder builder) {
        this.isLensMounted = builder.isLensMounted;
        this.isAFSwitchOn = builder.isAFSwitchOn;
        this.focusStatus = builder.focusStatus;
        this.focusMode = builder.focusMode;
        this.isFocusAssistantWorking = builder.isFocusAssistantWorking;
        this.isFocusAssistantEnabledForAF = builder.isFocusAssistantEnabledForAF;
        this.isFocusAssistantEnabledForMF = builder.isFocusAssistantEnabledForMF;
        this.sensorCleaningState = builder.cleaningState;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FocusState that = (FocusState) o;
        if (this.isLensMounted != that.isLensMounted || this.isAFSwitchOn != that.isAFSwitchOn || this.isFocusAssistantWorking != that.isFocusAssistantWorking || this.isFocusAssistantEnabledForMF != that.isFocusAssistantEnabledForMF || this.isFocusAssistantEnabledForAF != that.isFocusAssistantEnabledForAF || this.focusStatus != that.focusStatus || this.focusMode != that.focusMode) {
            return false;
        }
        if (this.sensorCleaningState != that.sensorCleaningState) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = 1;
        int i7 = 0;
        if (this.isLensMounted) {
            result = 1;
        } else {
            result = 0;
        }
        int i8 = result * 31;
        if (this.isAFSwitchOn) {
            i = 1;
        } else {
            i = 0;
        }
        int i9 = (i8 + i) * 31;
        if (this.focusStatus != null) {
            i2 = this.focusStatus.hashCode();
        } else {
            i2 = 0;
        }
        int i10 = (i9 + i2) * 31;
        if (this.focusMode != null) {
            i3 = this.focusMode.hashCode();
        } else {
            i3 = 0;
        }
        int i11 = (i10 + i3) * 31;
        if (this.isFocusAssistantWorking) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i12 = (i11 + i4) * 31;
        if (this.isFocusAssistantEnabledForMF) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i13 = (i12 + i5) * 31;
        if (!this.isFocusAssistantEnabledForAF) {
            i6 = 0;
        }
        int i14 = (i13 + i6) * 31;
        if (this.sensorCleaningState != null) {
            i7 = this.sensorCleaningState.hashCode();
        }
        return i14 + i7;
    }

    public boolean isFocusAssistantEnabledForMF() {
        return this.isFocusAssistantEnabledForMF;
    }

    public boolean isFocusAssistantEnabledForAF() {
        return this.isFocusAssistantEnabledForAF;
    }

    public boolean isFocusAssistantWorking() {
        return this.isFocusAssistantWorking;
    }

    public boolean isLensMounted() {
        return this.isLensMounted;
    }

    public boolean isAFSwitchOn() {
        return this.isAFSwitchOn;
    }

    public SettingsDefinitions.FocusStatus getFocusStatus() {
        return this.focusStatus;
    }

    public SettingsDefinitions.FocusMode getFocusMode() {
        return this.focusMode;
    }

    public SettingsDefinitions.SensorCleaningState getSensorCleaningState() {
        return this.sensorCleaningState;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public SettingsDefinitions.SensorCleaningState cleaningState = SettingsDefinitions.SensorCleaningState.UNKNOWN;
        /* access modifiers changed from: private */
        public SettingsDefinitions.FocusMode focusMode = SettingsDefinitions.FocusMode.UNKNOWN;
        /* access modifiers changed from: private */
        public SettingsDefinitions.FocusStatus focusStatus = SettingsDefinitions.FocusStatus.UNKNOWN;
        /* access modifiers changed from: private */
        public boolean isAFSwitchOn;
        /* access modifiers changed from: private */
        public boolean isFocusAssistantEnabledForAF;
        /* access modifiers changed from: private */
        public boolean isFocusAssistantEnabledForMF;
        /* access modifiers changed from: private */
        public boolean isFocusAssistantWorking;
        /* access modifiers changed from: private */
        public boolean isLensMounted;

        public Builder isLensMounted(boolean isLensMounted2) {
            this.isLensMounted = isLensMounted2;
            return this;
        }

        public Builder isAFSwitchOn(boolean isAFSwitchOn2) {
            this.isAFSwitchOn = isAFSwitchOn2;
            return this;
        }

        public Builder focusStatus(SettingsDefinitions.FocusStatus focusStatus2) {
            this.focusStatus = focusStatus2;
            return this;
        }

        public Builder focusMode(SettingsDefinitions.FocusMode focusMode2) {
            this.focusMode = focusMode2;
            return this;
        }

        public Builder sensorCleaningState(SettingsDefinitions.SensorCleaningState dedustingState) {
            this.cleaningState = dedustingState;
            return this;
        }

        public Builder isFocusAssistantWorking(boolean isFocusAssistantWorking2) {
            this.isFocusAssistantWorking = isFocusAssistantWorking2;
            return this;
        }

        public Builder isFocusAssistantEnabledForMF(boolean isFocusAssistantEnabledForMF2) {
            this.isFocusAssistantEnabledForMF = isFocusAssistantEnabledForMF2;
            return this;
        }

        public Builder isFocusAssistantEnabledForAF(boolean isFocusAssistantEnabledForAF2) {
            this.isFocusAssistantEnabledForAF = isFocusAssistantEnabledForAF2;
            return this;
        }

        public FocusState build() {
            return new FocusState(this);
        }
    }
}
