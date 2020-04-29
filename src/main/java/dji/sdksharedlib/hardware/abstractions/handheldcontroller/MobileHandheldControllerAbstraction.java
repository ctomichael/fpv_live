package dji.sdksharedlib.hardware.abstractions.handheldcontroller;

import android.os.Handler;
import dji.common.Stick;
import dji.common.error.DJIError;
import dji.common.handheld.HandheldButtonClickEvent;
import dji.common.handheld.LEDColorPattern;
import dji.common.handheld.LEDCommand;
import dji.common.handheld.RecordAndShutterButtons;
import dji.common.handheld.StickHorizontalDirection;
import dji.common.handheld.StickVerticalDirection;
import dji.common.util.AirLinkUtils;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ble.BluetoothLeService;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushShutterCmd;
import dji.midware.data.model.P3.DataGimbalGetPushHandheldStickState;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.data.model.P3.DataGimbalSetHandheldStickControlEnabled;
import dji.midware.data.model.P3.DataOsdSetLED;
import dji.midware.data.model.P3.DataWifiSetSSID;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.HandheldControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.util.ArrayList;
import java.util.Iterator;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class MobileHandheldControllerAbstraction extends HandheldControllerAbstraction {
    private Handler changeButtonStatusHandler = new Handler(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
    private Runnable changeButtonStatusToIdle = new Runnable() {
        /* class dji.sdksharedlib.hardware.abstractions.handheldcontroller.MobileHandheldControllerAbstraction.AnonymousClass1 */

        public void run() {
            MobileHandheldControllerAbstraction.this.notifyValueChangeForKeyPath(RecordAndShutterButtons.IDLE, HandheldControllerKeys.RECORD_AND_SHUTTER_BUTTONS);
        }
    };
    private long lastDoubleClickTime = 0;
    private long lastSingleClickTime = 0;
    private long lastTripleClickTime = 0;

    /* access modifiers changed from: protected */
    public boolean supportTriggerButton() {
        return true;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        super.destroy();
        DJIEventBusUtil.unRegister(this);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataGimbalGetPushHandheldStickState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGimbalGetPushHandheldStickState.getInstance());
        }
        if (DataGimbalGetPushParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGimbalGetPushParams.getInstance());
        }
        if (DataCameraGetPushShutterCmd.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushShutterCmd.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
     arg types: [boolean, java.lang.String]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent3BackgroundThread(DataGimbalGetPushParams gimbalGetPushParams) {
        if (gimbalGetPushParams != null) {
            if (supportTriggerButton()) {
                if (gimbalGetPushParams.getMode().value() == 0) {
                    notifyValueChangeForKeyPath((Object) true, HandheldControllerKeys.IS_TRIGGER_BEING_PRESSED);
                } else {
                    notifyValueChangeForKeyPath((Object) false, HandheldControllerKeys.IS_TRIGGER_BEING_PRESSED);
                }
            }
            if (gimbalGetPushParams.isSingleClick() && (this.lastSingleClickTime == 0 || System.currentTimeMillis() - this.lastSingleClickTime >= 1000)) {
                if (supportTriggerButton()) {
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.SINGLE_CLICK, HandheldControllerKeys.TRIGGER_BUTTON);
                    this.lastSingleClickTime = System.currentTimeMillis();
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.IDLE, HandheldControllerKeys.TRIGGER_BUTTON);
                } else {
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.SINGLE_CLICK, HandheldControllerKeys.MODE_BUTTON);
                    this.lastSingleClickTime = System.currentTimeMillis();
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.IDLE, HandheldControllerKeys.MODE_BUTTON);
                }
            }
            if (gimbalGetPushParams.isDoubleClick() && (this.lastDoubleClickTime == 0 || System.currentTimeMillis() - this.lastDoubleClickTime >= 1000)) {
                if (supportTriggerButton()) {
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.DOUBLE_CLICK, HandheldControllerKeys.TRIGGER_BUTTON);
                    this.lastDoubleClickTime = System.currentTimeMillis();
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.IDLE, HandheldControllerKeys.TRIGGER_BUTTON);
                } else {
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.DOUBLE_CLICK, HandheldControllerKeys.MODE_BUTTON);
                    this.lastDoubleClickTime = System.currentTimeMillis();
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.IDLE, HandheldControllerKeys.MODE_BUTTON);
                }
            }
            if (gimbalGetPushParams.isTripleClick() && (this.lastTripleClickTime == 0 || System.currentTimeMillis() - this.lastTripleClickTime >= 1000)) {
                if (supportTriggerButton()) {
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.TRIPLE_CLICK, HandheldControllerKeys.TRIGGER_BUTTON);
                    this.lastTripleClickTime = System.currentTimeMillis();
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.IDLE, HandheldControllerKeys.TRIGGER_BUTTON);
                } else {
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.TRIPLE_CLICK, HandheldControllerKeys.MODE_BUTTON);
                    this.lastTripleClickTime = System.currentTimeMillis();
                    notifyValueChangeForKeyPath(HandheldButtonClickEvent.IDLE, HandheldControllerKeys.MODE_BUTTON);
                }
            }
            notifyValueChangeForKeyPath(StickHorizontalDirection.values()[gimbalGetPushParams.getJoystickHorDirection()], HandheldControllerKeys.STICK_HORIZONTAL_DIRECTION);
            notifyValueChangeForKeyPath(StickVerticalDirection.values()[gimbalGetPushParams.getJoystickVerDirection()], HandheldControllerKeys.STICK_VERTICAL_DIRECTION);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShutterCmd cameraShutterCmd) {
        if (cameraShutterCmd.isGetted()) {
            int shutterType = cameraShutterCmd.getShutterType();
            if (shutterType == 4) {
                shutterType = 0;
            }
            RecordAndShutterButtons buttonStatus = RecordAndShutterButtons.values()[shutterType];
            notifyValueChangeForKeyPath(buttonStatus, HandheldControllerKeys.RECORD_AND_SHUTTER_BUTTONS);
            if (buttonStatus == RecordAndShutterButtons.SHUTTER_CLICK || buttonStatus == RecordAndShutterButtons.RECORD_CLICK) {
                if (this.changeButtonStatusHandler != null) {
                    this.changeButtonStatusHandler.removeCallbacks(this.changeButtonStatusToIdle);
                    this.changeButtonStatusHandler.postDelayed(this.changeButtonStatusToIdle, 600);
                }
            } else if (this.changeButtonStatusHandler != null) {
                this.changeButtonStatusHandler.removeCallbacks(this.changeButtonStatusToIdle);
            }
        }
    }

    @Setter(HandheldControllerKeys.HANDHELD_NAME)
    public void setHandheldName(String name, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!AirLinkUtils.verifySSID(name) || callback == null) {
            DataWifiSetSSID setter = new DataWifiSetSSID();
            setter.setFromLongan(true);
            setter.setSSID(name.getBytes()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.handheldcontroller.MobileHandheldControllerAbstraction.AnonymousClass2 */

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
            return;
        }
        callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
    }

    @Getter(HandheldControllerKeys.HANDHELD_NAME)
    public void getHandheldName(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(BluetoothLeService.getInstance().getBLE().getCurConnectedName());
        }
    }

    @Setter(HandheldControllerKeys.STICK_GIMBAL_CONTROL_ENABLED)
    public void setIsInternalAndExternalMicroPhonesEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataGimbalSetHandheldStickControlEnabled.getInstance().setHandheldStickControlEnabled(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.handheldcontroller.MobileHandheldControllerAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushHandheldStickState state) {
        if (state != null) {
            notifyValueChangeForKeyPath(new Stick(state.getStickX(), state.getStickY()), HandheldControllerKeys.STICK);
            notifyValueChangeForKeyPath(Boolean.valueOf(state.isStickGimbalControlEnabled()), HandheldControllerKeys.STICK_GIMBAL_CONTROL_ENABLED);
            if (supportTriggerButton()) {
                notifyValueChangeForKeyPath(Boolean.valueOf(state.isTriggerPressed()), HandheldControllerKeys.IS_TRIGGER_BEING_PRESSED);
            } else {
                notifyValueChangeForKeyPath(Boolean.valueOf(state.isTriggerPressed()), HandheldControllerKeys.IS_MODE_BUTTON_BEING_PRESSED);
            }
        }
    }

    @Action(HandheldControllerKeys.LED_COMMAND)
    public void performControlLED(final DJISDKCacheHWAbstraction.InnerCallback callback, LEDCommand command) {
        if (command == null || !verifyLEDCommand(command)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataOsdSetLED cmd = new DataOsdSetLED();
        cmd.reset();
        cmd.setRedUnit(1, getSequence(command.red.pattern), command.red.pattern.size(), command.red.repeatTimes);
        cmd.setBlueUnit(1, getSequence(command.blue.pattern), command.blue.pattern.size(), command.blue.repeatTimes);
        cmd.setGreenUnit(1, getSequence(command.green.pattern), command.green.pattern.size(), command.green.repeatTimes);
        cmd.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.handheldcontroller.MobileHandheldControllerAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    private boolean verifyLEDCommand(LEDCommand command) {
        return parseCommand(command.red) && parseCommand(command.blue) && parseCommand(command.green);
    }

    private boolean parseCommand(LEDColorPattern pattern) {
        if (pattern != null && pattern.repeatTimes > 0 && pattern.repeatTimes <= 255 && pattern.pattern != null && pattern.pattern.size() <= 32) {
            return true;
        }
        return false;
    }

    private int getSequence(ArrayList<Boolean> pattern) {
        int resultSequence = 0;
        Iterator<Boolean> it2 = pattern.iterator();
        while (it2.hasNext()) {
            resultSequence = (resultSequence << 1) | (it2.next().booleanValue() ? 1 : 0);
        }
        return resultSequence;
    }
}
