package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.flightcontroller.ControlMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;

@EXClassNullAway
public class FlightControllerA3WithLb2Abstraction extends FlightControllerA3Abstraction {
    private static final String PARAM_NAME_FMODE_CONFIG = "g_config.control.control_mode[0]_0";

    /* access modifiers changed from: protected */
    public boolean isOnboardSDKAvailable() {
        return true;
    }

    @Getter(FlightControllerKeys.CONTROL_MODE)
    public void getControlMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{"g_config.control.control_mode[0]_0"}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3WithLb2Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, ControlMode.find(DJIFlycParamInfoManager.read("g_config.control.control_mode[0]_0").value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.CONTROL_MODE)
    public void setControlMode(ControlMode controlMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (controlMode == null || controlMode == ControlMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            new DataFlycSetParams().setInfo("g_config.control.control_mode[0]_0", Integer.valueOf(controlMode.value())).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3WithLb2Abstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }
}
