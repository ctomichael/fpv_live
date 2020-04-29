package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.error.DJIBatteryError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;

@EXClassNullAway
public class BatteryHandheldAbstraction extends BatteryAbstraction {
    public BatteryHandheldAbstraction() {
        this.isSmartBattery = false;
    }

    @Getter(BatteryKeys.LATEST_WARNING_RECORD)
    public void getLatestWarningRecord(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Setter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void setSelfDischargeInDays(Integer value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Getter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void getSelfDischargeInDays(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getVersion(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.NOT_SUPPORT_CURRENT_STATE));
        }
    }

    /* access modifiers changed from: protected */
    public void getSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback, int state) {
        String rawSN = "" + DataCenterGetPushBatteryCommon.getInstance().getSerialNo();
        if (state != 3) {
            rawSN = getHashSerialNum(rawSN, state);
        }
        if (callback != null) {
            callback.onSuccess(rawSN);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isWarningInformationRecordsSupported() {
        return false;
    }
}
