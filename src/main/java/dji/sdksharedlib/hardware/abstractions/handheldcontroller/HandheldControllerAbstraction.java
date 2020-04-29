package dji.sdksharedlib.hardware.abstractions.handheldcontroller;

import dji.common.error.DJIError;
import dji.common.error.DJISDKCacheError;
import dji.common.handheld.PowerMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdActiveStatus;
import dji.midware.data.model.P3.DataOsdSetPower;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.HandheldControllerKeys;

@EXClassNullAway
public class HandheldControllerAbstraction extends HandheldControllerBaseAbstraction {
    @Setter(HandheldControllerKeys.POWER_MODE)
    public void setHandheldPowerMode(final PowerMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOsdSetPower.POWER_TYPE type = DataOsdSetPower.POWER_TYPE.OTHER;
        if (mode.equals(PowerMode.ON)) {
            type = DataOsdSetPower.POWER_TYPE.AWEAK;
        }
        if (mode.equals(PowerMode.SLEEPING)) {
            type = DataOsdSetPower.POWER_TYPE.SLEEP;
        }
        if (mode.equals(PowerMode.OFF)) {
            type = DataOsdSetPower.POWER_TYPE.POWER_OFF;
        }
        DataOsdSetPower.getInstance().setPowerType(type).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.handheldcontroller.HandheldControllerAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(mode);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJISDKCacheError.DISCONNECTED);
                }
            }
        });
    }

    @Getter("FullSerialNumberHash")
    public void getFullSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getSerialNumber(callback, 2);
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(DJISDKCacheHWAbstraction.InnerCallback callback) {
        getSerialNumber(callback, 0);
    }

    /* access modifiers changed from: protected */
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback, final int state) {
        DataOsdActiveStatus.getInstance().setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.handheldcontroller.HandheldControllerAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                String MD5SN = HandheldControllerAbstraction.this.getHashSerialNum(DataOsdActiveStatus.getInstance().getSN(), state);
                if (callback != null) {
                    callback.onSuccess(MD5SN);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }
}
