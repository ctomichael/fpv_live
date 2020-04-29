package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.remotecontroller.RCMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetSNOfMavicRC;
import dji.midware.data.model.P3.DataRcGetFDPushConnectStatus;
import dji.midware.data.model.P3.DataRcGetPushRcCustomButtonsStatus;
import dji.midware.data.model.P3.DataRcSetCalibration;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import java.util.Iterator;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRCFoldingDroneAbstraction extends DJIRCAbstraction {
    private static final int CALIBRATION_INTERVAL = 500;

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameMavicPro;
    }

    public DJIRCFoldingDroneAbstraction() {
        this.hardwareState.getC1Button().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getPauseButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(true);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(false);
        this.hardwareState.getFiveDButton().setPresent(true);
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.hardwareState.getC1Button().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(true);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(false);
        this.hardwareState.getFiveDButton().setPresent(true);
    }

    public void destroy() {
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
        addCharacteristics(RemoteControllerKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataRcGetPushRcCustomButtonsStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushRcCustomButtonsStatus.getInstance());
        }
        if (DataRcGetFDPushConnectStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetFDPushConnectStatus.getInstance());
        }
    }

    @Action(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION)
    public void remoteControllerCalibration(DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcSetCalibration.getInstance().setMode(DataRcSetCalibration.MODE.Middle).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCFoldingDroneAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
    }

    private void notifyAllCallbacks(ArrayList<DJISDKCacheHWAbstraction.InnerCallback> callbacks, Object object) {
        Iterator<DJISDKCacheHWAbstraction.InnerCallback> it2 = callbacks.iterator();
        while (it2.hasNext()) {
            CallbackUtils.onSuccess(it2.next(), object);
        }
    }

    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetSNOfMavicRC getSNOfMavicRC = new DataCommonGetSNOfMavicRC();
        ((DataCommonGetSNOfMavicRC) getSNOfMavicRC.setDeviceType(DeviceType.DM368_G).setReceiverId(1, DataCommonGetSNOfMavicRC.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCFoldingDroneAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                if (TextUtils.isEmpty(getSNOfMavicRC.getSN())) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, getSNOfMavicRC.getSN());
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushRcCustomButtonsStatus status) {
        super.onEvent3BackgroundThread(status);
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isUp()), genKeyPath(RemoteControllerKeys.FIVE_DIMENS_BUTTON_PUSH_UP));
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isDown()), genKeyPath(RemoteControllerKeys.FIVE_DIMENS_BUTTON_PUSH_DOWN));
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isPressed()), genKeyPath(RemoteControllerKeys.FIVE_DIMENS_BUTTON_PUSH_PRESSED));
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isLeft()), genKeyPath(RemoteControllerKeys.FIVE_DIMENS_BUTTON_PUSH_LEFT));
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isRight()), genKeyPath(RemoteControllerKeys.FIVE_DIMENS_BUTTON_PUSH_RIGHT));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetFDPushConnectStatus status) {
        notifyValueChangeForKeyPath(RCMode.find(status.getRole().value()), genKeyPath("Mode"));
    }
}
