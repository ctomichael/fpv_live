package dji.common.flightcontroller.accesslocker;

import android.support.annotation.NonNull;

public enum AccessLockerState {
    NOT_INITIALIZED,
    LOCKED,
    UNLOCKED,
    UNKNOWN;

    public interface Callback {
        void onUpdate(@NonNull AccessLockerState accessLockerState);
    }
}
