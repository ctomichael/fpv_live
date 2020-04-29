package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.error.DJIBatteryError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCenterGetSelfDischarge;
import dji.midware.data.model.P3.DataCenterSetSelfDischarge;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.BatteryKeys;

@EXClassNullAway
public class BatteryPhantom3Abstraction extends BatteryAbstraction {
    @Setter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void setSelfDischargeInDays(Integer value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int day = value.intValue();
        if ((day < 1 || day > 10) && callback != null) {
            callback.onFails(DJIBatteryError.getDJIError(Ccode.INVALID_PARAM));
            return;
        }
        DataCenterSetSelfDischarge dataLitchiSelfSetter = new DataCenterSetSelfDischarge();
        dataLitchiSelfSetter.setEncrypt(0);
        dataLitchiSelfSetter.setDays(day).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryPhantom3Abstraction.AnonymousClass1 */

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

    @Getter(BatteryKeys.SELF_DISCHARGE_IN_DAYS)
    public void getSelfDischargeInDays(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCenterGetSelfDischarge dataLitchiSelfGetter = new DataCenterGetSelfDischarge();
        dataLitchiSelfGetter.setEncrypt(0);
        dataLitchiSelfGetter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryPhantom3Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                int result = dataLitchiSelfGetter.getDay();
                if (result < 1 || result > 10) {
                    result = 7;
                }
                callback.onSuccess(Integer.valueOf(result));
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIBatteryError.getDJIError(ccode));
                }
            }
        });
    }
}
