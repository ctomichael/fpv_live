package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.gimbal.BalanceState;
import dji.common.gimbal.CapabilityKey;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataGimbalGetPushAbnormalStatus;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIGimbalMobileHandheldAbstraction extends DJIGimbalHandheldAbstraction {
    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_YAW, -140, 140);
    }

    public void destroy() {
        super.destroy();
        DJIEventBusUtil.unRegister(this);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataGimbalGetPushAbnormalStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGimbalGetPushAbnormalStatus.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushAbnormalStatus param) {
        notifyValueChangeForKeyPath(Boolean.valueOf(!param.isPhoneOutGimbal()), GimbalKeys.IS_MOBILE_DEVICE_MOUNTED);
        notifyValueChangeForKeyPath(BalanceState.values()[param.getGimbalGravity()], GimbalKeys.BALANCE_STATE);
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isMotorProtected()), GimbalKeys.IS_MOTOR_OVER_LOADED);
    }
}
