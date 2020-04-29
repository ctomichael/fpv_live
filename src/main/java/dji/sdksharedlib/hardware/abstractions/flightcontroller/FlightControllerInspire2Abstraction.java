package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonRestartDevice;
import dji.midware.util.RepeatDataBase;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantInspire2Abstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager;

@EXClassNullAway
public class FlightControllerInspire2Abstraction extends FlightControllerInspire1Abstraction {
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        super.initFlightControllerSupportParameter();
        this.imuCount = 2;
        this.compassCount = 2;
        notifyValueChangeForKeyPath(Integer.valueOf(this.imuCount), convertKeyToPath(FlightControllerKeys.IMU_COUNT));
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
        notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_FLIGHT_ASSISTANT_SUPPORTED));
    }

    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistantInspire2Abstraction();
    }

    @Setter(FlightControllerKeys.QUICK_SPIN_ENABLED)
    public void setQuickSpinEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJISDKCacheProductConfigManager.getInstance().writeConfig(FlightControllerKeys.QUICK_SPIN_ENABLED, Integer.valueOf(enabled ? 1 : 0), new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire2Abstraction.AnonymousClass1 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(FlightControllerKeys.QUICK_SPIN_ENABLED)
    public void getQuickSpinEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.newMergeGetFlycParamInfo.getInfo(FlightControllerKeys.QUICK_SPIN_ENABLED, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire2Abstraction.AnonymousClass2 */

            public void onSuccess(Object object) {
                boolean z = true;
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                if (((Number) object).intValue() != 1) {
                    z = false;
                }
                CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Action(FlightControllerKeys.RESET_MOTOR)
    public void resetMotor(DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCommonRestartDevice restart = new DataCommonRestartDevice();
        restart.setReceiveType(DeviceType.FLYC).setReceiveId(0).setRestartType(1).setDelay(1000);
        new RepeatDataBase(restart, 3, 200, CallbackUtils.defaultCB(callback)).start();
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return true;
    }
}
