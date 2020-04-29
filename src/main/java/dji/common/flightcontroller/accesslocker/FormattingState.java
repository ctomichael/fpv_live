package dji.common.flightcontroller.accesslocker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;

public final class FormattingState {
    private final DJIError djiDataLockerError;
    private final FormattingProgressState progressState;

    public interface Callback {
        void onUpdate(@NonNull FormattingState formattingState);
    }

    public FormattingState(FormattingProgressState formattingState, DJIError djiDataLockerError2) {
        this.progressState = formattingState;
        this.djiDataLockerError = djiDataLockerError2;
    }

    public FormattingProgressState getProgressState() {
        return this.progressState;
    }

    @Nullable
    public DJIError getDJIDataLockerError() {
        return this.djiDataLockerError;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormattingState that = (FormattingState) o;
        if (getProgressState() != that.getProgressState()) {
            return false;
        }
        if (getDJIDataLockerError() != null) {
            return getDJIDataLockerError().equals(that.getDJIDataLockerError());
        }
        if (that.getDJIDataLockerError() != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (getProgressState() != null) {
            result = getProgressState().hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (getDJIDataLockerError() != null) {
            i = getDJIDataLockerError().hashCode();
        }
        return i2 + i;
    }
}
