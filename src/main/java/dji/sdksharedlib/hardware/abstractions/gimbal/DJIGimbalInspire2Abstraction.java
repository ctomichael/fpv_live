package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIError;
import dji.common.gimbal.CapabilityKey;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataGimbalGetSerialParams;
import dji.midware.data.model.P3.DataGimbalNewResetAndSetMode;
import dji.midware.data.model.P3.DataOnBoardControlGimbalsSimully;
import dji.midware.data.model.P3.DataOnboardGetPushMixInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIGimbalInspire2Abstraction extends DJIGimbalX3Abstraction {
    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_MAX_SPEED, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_MAX_SPEED, 0, 100);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
     arg types: [boolean, java.lang.String]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void */
    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOnboardGetPushMixInfo.getInstance().isGetted()) {
            notifyValueChangeForKeyPath(Boolean.valueOf(DataOnboardGetPushMixInfo.getInstance().isSimultaneousControlGimbal()), GimbalKeys.GIMBAL_ATTITUDE_SYNCHRONIZATION_ENABLED);
        } else {
            notifyValueChangeForKeyPath((Object) false, GimbalKeys.GIMBAL_ATTITUDE_SYNCHRONIZATION_ENABLED);
        }
        DataOnboardGetPushMixInfo.getInstance().swapValidData(getReceiverIdByIndex());
        onEvent3BackgroundThread(DataOnboardGetPushMixInfo.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnboardGetPushMixInfo params) {
        if (params.isGetted()) {
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isSimultaneousControlGimbal()), convertKeyToPath(GimbalKeys.GIMBAL_ATTITUDE_SYNCHRONIZATION_ENABLED));
        }
    }

    @Setter(GimbalKeys.GIMBAL_ATTITUDE_SYNCHRONIZATION_ENABLED)
    public void setAttitudeSynchronizationEnabled(boolean enable, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataOnBoardControlGimbalsSimully().enable(enable).start(CallbackUtils.defaultCB(callback));
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalGetSerialParams) DataGimbalGetSerialParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataGimbalGetSerialParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalInspire2Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, DataGimbalGetSerialParams.getInstance().getGimbalSerial());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void resetGimbal(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalNewResetAndSetMode) new DataGimbalNewResetAndSetMode().setReset(true).setValidBothYawAndPitch(true).setReceiverId(getReceiverIdByIndex(), DataGimbalNewResetAndSetMode.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalInspire2Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
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
