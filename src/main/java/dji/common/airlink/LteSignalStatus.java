package dji.common.airlink;

import dji.midware.data.model.P3.DataModule4GGetPushSignal;
import java.util.Objects;

public class LteSignalStatus {
    public static final LteSignalStatus NONE = new LteSignalStatus();
    private String mManufacturer;
    private DataModule4GGetPushSignal.NetworkType mNetworkType = DataModule4GGetPushSignal.NetworkType.UNKNOWN;
    private String mOperatorName = "";
    private int mSignal = 0;

    public String getOperatorName() {
        return this.mOperatorName;
    }

    public void setOperatorName(String operatorName) {
        this.mOperatorName = operatorName;
    }

    public String getNetworkType() {
        return this.mNetworkType.toString();
    }

    public void setNetworkType(DataModule4GGetPushSignal.NetworkType networkType) {
        this.mNetworkType = networkType;
    }

    public int getSignal() {
        return this.mSignal;
    }

    public void setSignal(int signal) {
        this.mSignal = signal;
    }

    public String getManufacturer() {
        return this.mManufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.mManufacturer = manufacturer;
    }

    public boolean isConnect() {
        return this.mNetworkType != DataModule4GGetPushSignal.NetworkType.UNKNOWN;
    }

    public LteSignalStatus clone() {
        LteSignalStatus copy = new LteSignalStatus();
        copy.mNetworkType = this.mNetworkType;
        copy.mOperatorName = this.mOperatorName;
        copy.mSignal = this.mSignal;
        return copy;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof LteSignalStatus)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        LteSignalStatus state = (LteSignalStatus) obj;
        if (!Objects.equals(this.mOperatorName, state.mOperatorName) || !Objects.equals(this.mNetworkType, state.mNetworkType) || !Objects.equals(this.mManufacturer, state.mManufacturer) || this.mSignal != state.mSignal) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mOperatorName, this.mNetworkType, this.mManufacturer, Integer.valueOf(this.mSignal));
    }
}
