package dji.internal.aeroscope;

import dji.midware.data.model.P3.DataFlycDetection;

public class AeroScopeClientSwitch {
    private boolean[] mSwitchArray;

    public AeroScopeClientSwitch() {
        this.mSwitchArray = new boolean[DataFlycDetection.Switch.values().length];
        this.mSwitchArray = getDefaultState();
    }

    public AeroScopeClientSwitch(AeroScopeClientSwitch other) {
        this.mSwitchArray = new boolean[DataFlycDetection.Switch.values().length];
        boolean[] src = other.toArray();
        this.mSwitchArray = new boolean[src.length];
        System.arraycopy(src, 0, this.mSwitchArray, 0, src.length);
    }

    public AeroScopeClientSwitch(boolean[] array) {
        this.mSwitchArray = new boolean[DataFlycDetection.Switch.values().length];
        if (array.length >= 8) {
            this.mSwitchArray = new boolean[array.length];
            System.arraycopy(array, 0, this.mSwitchArray, 0, array.length);
            return;
        }
        this.mSwitchArray = getDefaultState();
    }

    private boolean[] getDefaultState() {
        this.mSwitchArray = new boolean[DataFlycDetection.Switch.values().length];
        this.mSwitchArray[DataFlycDetection.Switch.Sn.ordinal()] = true;
        this.mSwitchArray[DataFlycDetection.Switch.GPS.ordinal()] = true;
        this.mSwitchArray[DataFlycDetection.Switch.HomeGPS.ordinal()] = true;
        this.mSwitchArray[DataFlycDetection.Switch.DroneID.ordinal()] = true;
        this.mSwitchArray[DataFlycDetection.Switch.FlyPlan.ordinal()] = true;
        this.mSwitchArray[DataFlycDetection.Switch.APPGPS.ordinal()] = true;
        return this.mSwitchArray;
    }

    public boolean isEnable(DataFlycDetection.Switch switchType) {
        return this.mSwitchArray[switchType.ordinal()];
    }

    public AeroScopeClientSwitch setEnable(DataFlycDetection.Switch switchType, boolean isEnable) {
        this.mSwitchArray[switchType.ordinal()] = isEnable;
        return this;
    }

    public boolean[] toArray() {
        boolean[] tmpCopy = new boolean[this.mSwitchArray.length];
        System.arraycopy(this.mSwitchArray, 0, tmpCopy, 0, this.mSwitchArray.length);
        return tmpCopy;
    }
}
