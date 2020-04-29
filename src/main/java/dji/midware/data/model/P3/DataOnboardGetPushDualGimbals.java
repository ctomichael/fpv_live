package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataOnboardGetPushDualGimbals extends DataBase {
    public static DataOnboardGetPushDualGimbals getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataOnboardGetPushDualGimbals INSTANCE = new DataOnboardGetPushDualGimbals();

        private Holder() {
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public boolean isGimbalBehaviorBoth() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 8) == 8;
    }

    public boolean isMainGimbalConnect() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public boolean isSecondGimbalConnect() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 2) == 2;
    }

    public boolean isGimbalLocateError() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 4) == 4;
    }

    public int getMainCameraType() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getSecondCameraType() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getMainGimbalType() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getSecondGimbalType() {
        return ((Integer) get(4, 1, Integer.class)).intValue();
    }
}
