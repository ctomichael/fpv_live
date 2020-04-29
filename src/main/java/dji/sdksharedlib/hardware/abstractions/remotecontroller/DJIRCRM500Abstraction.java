package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import dji.common.Stick;
import dji.common.error.DJIRemoteControllerError;
import dji.common.remotecontroller.ChargeRemaining;
import dji.common.remotecontroller.CustomButtonTags;
import dji.common.remotecontroller.HardwareState;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataRcGetCustomFuction;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.ContextUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRCRM500Abstraction extends DJIRCFoldingDroneAbstraction {
    private static final int maxmAh = 12400;
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCRM500Abstraction.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                DJIRCRM500Abstraction.this.onBatteryInfoChange(intent);
            }
        }
    };
    private int oldPercent = -1;

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return "RM500 General SDR Controller";
    }

    public DJIRCRM500Abstraction() {
        this.hardwareState.getC1Button().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getC3Button().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(false);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getPlaybackButton().setPresent(false);
        this.hardwareState.getPauseButton().setPresent(false);
        this.hardwareState.getFunctionButton().setPresent(false);
        this.hardwareState.getFiveDButton().setPresent(true);
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.hardwareState.getC1Button().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getC3Button().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(false);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getPlaybackButton().setPresent(false);
        this.hardwareState.getPauseButton().setPresent(false);
        this.hardwareState.getFunctionButton().setPresent(false);
        this.hardwareState.getFiveDButton().setPresent(true);
    }

    public void destroy() {
        ContextUtil.getContext().unregisterReceiver(this.mBatteryReceiver);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
        addCharacteristics(RemoteControllerKeys.class, getClass());
    }

    /* access modifiers changed from: private */
    public void onBatteryInfoChange(Intent intent) {
        int percent;
        if (intent != null && (percent = (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 1)) != this.oldPercent) {
            notifyValueChangeForKeyPath(new ChargeRemaining(percent * maxmAh, percent), genKeyPath("ChargeRemaining"));
            DJILog.logWriteE("LowRcBatteryReason", "ShareLib Update = " + percent + "%, old = " + this.oldPercent + "%", "LowRcBatteryReason", new Object[0]);
            this.oldPercent = percent;
        }
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        onBatteryInfoChange(ContextUtil.getContext().registerReceiver(this.mBatteryReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED")));
    }

    @Setter(RemoteControllerKeys.CUSTOM_BUTTON_TAGS)
    public void setRCCustomButtonTag(CustomButtonTags tag, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.hardwareState.getC1Button().isPresent() || this.hardwareState.getC2Button().isPresent() || this.hardwareState.getC3Button().isPresent()) {
            CustomButtonTags param = tag;
            DataRcSetCustomFuction.getInstance().setC1(param.getC1ButtonTag()).setC2(param.getC2ButtonTag()).setC3(param.getC3ButtonTag()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCRM500Abstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
            return;
        }
        CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
    }

    @Getter(RemoteControllerKeys.CUSTOM_BUTTON_TAGS)
    public void getRCCustomButtonTag(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.hardwareState.getC1Button().isPresent() || this.hardwareState.getC2Button().isPresent() || this.hardwareState.getC3Button().isPresent()) {
            DataRcGetCustomFuction.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCRM500Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, new CustomButtonTags.Builder().c1ButtonTag((short) DataRcGetCustomFuction.getInstance().getC1()).c2ButtonTag((short) DataRcGetCustomFuction.getInstance().getC2()).c3ButtonTag((short) DataRcGetCustomFuction.getInstance().getC3()).build());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        int i;
        int i2;
        boolean z;
        boolean z2 = true;
        notifyValueChangeForKeyPath(new Stick(params.getAileron() - 1024, params.getElevator() - 1024), genKeyPath(RemoteControllerKeys.RIGHT_STICK_VALUE));
        notifyValueChangeForKeyPath(new Stick(params.getRudder() - 1024, params.getThrottle() - 1024), genKeyPath(RemoteControllerKeys.LEFT_STICK_VALUE));
        notifyValueChangeForKeyPath(Integer.valueOf(params.getGyroValue() - 1024), genKeyPath(RemoteControllerKeys.LEFT_WHEEL));
        boolean isPresent = this.hardwareState.getRightWheel().isPresent();
        boolean isWheelChanged = params.isWheelChanged();
        boolean isWheelBtnDown = params.isWheelBtnDown();
        if (params.isWheelPositive()) {
            i = params.getWheelOffset();
        } else {
            i = -params.getWheelOffset();
        }
        notifyValueChangeForKeyPath(new HardwareState.RightWheel(isPresent, isWheelChanged, isWheelBtnDown, i), genKeyPath(RemoteControllerKeys.RIGHT_WHEEL));
        boolean isPresent2 = this.hardwareState.getTransformationSwitch().isPresent();
        if (params.getFootStool()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        notifyValueChangeForKeyPath(new HardwareState.TransformationSwitch(isPresent2, HardwareState.TransformationSwitch.State.find(i2)), genKeyPath(RemoteControllerKeys.TRANSFORMATION_SWITCH));
        if (isPhantom4RC() || isFoldingDrone()) {
            switch (params.getMode()) {
                case 0:
                    notifyValueChangeForKeyPath(HardwareState.FlightModeSwitch.POSITION_TWO, genKeyPath(RemoteControllerKeys.FLIGHT_MODE_SWITCH_POSITION));
                    break;
                case 1:
                    notifyValueChangeForKeyPath(HardwareState.FlightModeSwitch.POSITION_THREE, genKeyPath(RemoteControllerKeys.FLIGHT_MODE_SWITCH_POSITION));
                    break;
                case 2:
                    notifyValueChangeForKeyPath(HardwareState.FlightModeSwitch.POSITION_ONE, genKeyPath(RemoteControllerKeys.FLIGHT_MODE_SWITCH_POSITION));
                    break;
            }
        } else {
            notifyValueChangeForKeyPath(HardwareState.FlightModeSwitch.find(DJIProductManager.getInstance().getType(), params.getMode()), genKeyPath(RemoteControllerKeys.FLIGHT_MODE_SWITCH_POSITION));
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getGoHomeButton().isPresent(), params.isGoHomeButtonPressed()), genKeyPath(RemoteControllerKeys.GO_HOME_BUTTON));
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getGoHomeButton().isPresent(), params.getRecordStatus()), genKeyPath(RemoteControllerKeys.RECORD_BUTTON));
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getShutterButton().isPresent(), params.getShutterStatus()), genKeyPath(RemoteControllerKeys.SHUTTER_BUTTON));
        notifyValueChangeForKeyPath(new HardwareState.Button(this.hardwareState.getC1Button().isPresent(), params.getCustom1() == 1), genKeyPath(RemoteControllerKeys.CUSTOM_BUTTON_1));
        boolean isPresent3 = this.hardwareState.getC2Button().isPresent();
        if (params.getCustom2() == 1) {
            z = true;
        } else {
            z = false;
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(isPresent3, z), genKeyPath(RemoteControllerKeys.CUSTOM_BUTTON_2));
        boolean isPresent4 = this.hardwareState.getC3Button().isPresent();
        if (params.getCustom3() != 1) {
            z2 = false;
        }
        notifyValueChangeForKeyPath(new HardwareState.Button(isPresent4, z2), genKeyPath(RemoteControllerKeys.CUSTOM_BUTTON_3));
    }
}
