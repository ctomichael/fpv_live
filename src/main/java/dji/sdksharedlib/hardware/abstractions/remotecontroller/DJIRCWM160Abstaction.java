package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.common.error.DJIError;
import dji.common.remotecontroller.RCMode;
import dji.common.util.CallbackUtils;
import dji.midware.data.model.P3.DataWifi_gGetPushCheckStatus;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIRCWM160Abstaction extends DJIRCMavicAirAbstraction {
    private static final String TAG = "DJIRCWM160Abstaction";

    public DJIRCWM160Abstaction() {
        this.hardwareState.getFunctionButton().setPresent(true);
        this.hardwareState.getC1Button().setPresent(false);
        this.hardwareState.getC2Button().setPresent(false);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getPauseButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(false);
        this.hardwareState.getShutterButton().setPresent(true);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataWifi_gGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifi_gGetPushCheckStatus.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameWM160;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isRemoteControllerModeSupported(RCMode mode) {
        return mode == RCMode.NORMAL;
    }

    public void setRemoteControllerMode(RCMode value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isRemoteControllerModeSupported(value)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            super.setRemoteControllerMode(value, callback);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifi_gGetPushCheckStatus status) {
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isLinked()), RemoteControllerKeys.IS_STILL_LINKED);
    }
}
