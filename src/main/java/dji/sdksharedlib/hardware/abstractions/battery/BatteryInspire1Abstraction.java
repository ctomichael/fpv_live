package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.error.DJIBatteryError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCenterSelfDischarge;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BatteryKeys;

@EXClassNullAway
public class BatteryInspire1Abstraction extends BatteryAbstraction {
    @Setter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void setSelfDischargeInDays(Integer value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        short day = value.shortValue();
        if ((day < 1 || day > 10) && callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.INVALID_PARAM));
        } else {
            DataCenterSelfDischarge.getInstance().setDays(day).setFlag(false).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire1Abstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIBatteryError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Getter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void getSelfDischargeInDays(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCenterSelfDischarge instance = DataCenterSelfDischarge.getInstance();
        instance.setFlag(true).setDays(0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire1Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                callback.onSuccess(Integer.valueOf(instance.getDay()));
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIBatteryError.getDJIError(ccode));
                }
            }
        });
    }
}
