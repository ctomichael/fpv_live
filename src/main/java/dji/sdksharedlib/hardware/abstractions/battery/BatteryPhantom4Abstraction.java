package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.error.DJIBatteryError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataBatteryActiveStatus;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.model.common.DataCommonActiveGetVer;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

@EXClassNullAway
public class BatteryPhantom4Abstraction extends BatteryAbstraction {
    private static final String TAG = "BatteryPhantom4Abstraction";

    /* access modifiers changed from: protected */
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback, final int state) {
        new DataCommonActiveGetVer().setDevice(DeviceType.BATTERY).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryPhantom4Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                DataBatteryActiveStatus.getInstance().setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryPhantom4Abstraction.AnonymousClass1.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        String rawSN = DataBatteryActiveStatus.getInstance().getSN();
                        if (state != 3) {
                            rawSN = BatteryPhantom4Abstraction.this.getHashSerialNum(rawSN, state);
                        }
                        if (callback != null) {
                            callback.onSuccess(rawSN);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (callback != null) {
                            callback.onFails(DJIBatteryError.getDJIError(ccode));
                        }
                    }
                });
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIBatteryError.getDJIError(ccode));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isSelfDischargeInDaysValueValid(short days) {
        if (days < 1 || days > 10) {
            return false;
        }
        return true;
    }
}
