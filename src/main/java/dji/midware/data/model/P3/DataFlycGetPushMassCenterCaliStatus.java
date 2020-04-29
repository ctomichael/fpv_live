package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataFlycGetPushMassCenterCaliStatus extends DataBase {
    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public static DataFlycGetPushMassCenterCaliStatus getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataFlycGetPushMassCenterCaliStatus INSTANCE = new DataFlycGetPushMassCenterCaliStatus();

        private Holder() {
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public MassCenterCaliState getCaliState() {
        if (isGetted()) {
            return MassCenterCaliState.find(((Integer) get(0, 1, Integer.class)).intValue());
        }
        return MassCenterCaliState.OTHER;
    }

    public MassCenterCaliExitReason getCaliExitReason() {
        if (isGetted()) {
            return MassCenterCaliExitReason.find(((Integer) get(1, 1, Integer.class)).intValue());
        }
        return MassCenterCaliExitReason.OTHER;
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

        public int value() {
            return this.mValue;
        }

        static MassCenterCaliExitReason find(int value) {
            MassCenterCaliExitReason reason = OTHER;
            MassCenterCaliExitReason[] values = values();
            for (MassCenterCaliExitReason state : values) {
                if (state.mValue == value) {
                    return state;
                }
            }
            return reason;
        }
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

        public int value() {
            return this.mValue;
        }

        static MassCenterCaliState find(int value) {
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
}
