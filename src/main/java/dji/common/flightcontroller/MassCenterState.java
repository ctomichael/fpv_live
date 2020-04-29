package dji.common.flightcontroller;

import java.util.Objects;

public final class MassCenterState {
    private final MassCenterCaliState mCaliState;
    private final MassCenterCaliExitReason mExitReason;

    public MassCenterState(MassCenterCaliState caliState) {
        this.mCaliState = caliState;
        this.mExitReason = MassCenterCaliExitReason.OTHER;
    }

    public MassCenterState(MassCenterCaliState caliState, MassCenterCaliExitReason reason) {
        this.mCaliState = caliState;
        this.mExitReason = reason;
    }

    public MassCenterCaliExitReason getExitReason() {
        return this.mExitReason;
    }

    public MassCenterCaliState getCaliState() {
        return this.mCaliState;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MassCenterState)) {
            return false;
        }
        MassCenterState state2 = (MassCenterState) obj;
        if (!Objects.equals(this.mCaliState, state2.mCaliState) || !Objects.equals(this.mExitReason, state2.mExitReason)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mCaliState, this.mExitReason);
    }

    public enum MassCenterCaliState {
        STANDBY(0),
        CALCULATING(1),
        FINISHED(2),
        FAILED(3),
        OTHER(255);
        
        private int mValue;

        private MassCenterCaliState(int value) {
            this.mValue = value;
        }

        public static MassCenterCaliState find(int value) {
            MassCenterCaliState target = OTHER;
            MassCenterCaliState[] values = values();
            for (MassCenterCaliState state : values) {
                if (state.mValue == value) {
                    return state;
                }
            }
            return target;
        }
    }

    public enum MassCenterCaliExitReason {
        SUCCESS(0),
        MANNUAL_EXIT(1),
        FAIL_NOT_IN_HOVER(2),
        FAIL_BIG_WIND(3),
        OTHER(255);
        
        private int mValue;

        private MassCenterCaliExitReason(int value) {
            this.mValue = value;
        }

        public static MassCenterCaliExitReason find(int value) {
            MassCenterCaliExitReason target = OTHER;
            MassCenterCaliExitReason[] values = values();
            for (MassCenterCaliExitReason reason : values) {
                if (reason.mValue == value) {
                    return reason;
                }
            }
            return target;
        }
    }
}
