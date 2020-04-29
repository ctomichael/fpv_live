package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICcodeError;
import dji.common.error.DJIError;
import dji.common.error.DJIGimbalBehaviorError;
import dji.common.flightcontroller.ControlGimbalBehavior;
import dji.common.flightcontroller.MassCenterState;
import dji.common.flightcontroller.OSDKEnabledState;
import dji.common.util.CallbackUtils;
import dji.internal.logics.CommonUtil;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataDm368SetMainLiveVideoCamera;
import dji.midware.data.model.P3.DataFlycFunctionControl;
import dji.midware.data.model.P3.DataFlycGetPushMassCenterCaliStatus;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataModule4GGetPushOperator;
import dji.midware.data.model.P3.DataModule4GGetPushSignal;
import dji.midware.data.model.P3.DataOnBoardControlGimbalsSimully;
import dji.midware.data.model.P3.DataOnBoardSdkGetEnabledState;
import dji.midware.data.model.P3.DataOnBoardSdkGetPPSEnabledState;
import dji.midware.data.model.P3.DataOnBoardSdkGetVisionEnabledState;
import dji.midware.data.model.P3.DataOnBoardSdkSetEnabledState;
import dji.midware.data.model.P3.DataOnBoardSdkSetPPSEnabledState;
import dji.midware.data.model.P3.DataOnBoardSdkSetVisionEnabledState;
import dji.midware.data.model.P3.DataOnboardGetPushDualGimbals;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPM420RTKAbstraction;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.util.LteSignalHelper;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FlightControllerPM420Abstraction extends FlightControllerM200Abstraction {
    private LteSignalHelper mLteSignalHelper = new LteSignalHelper(new FlightControllerPM420Abstraction$$Lambda$0(this), DeviceType.OFDM.value());

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$FlightControllerPM420Abstraction(Object newValue) {
        notifyValueChangeForKeyPath(newValue, convertKeyToPath("LteStatus"));
    }

    /* access modifiers changed from: protected */
    public RTKAbstraction getRTKAbstractionIfSupported() {
        return new RTKPM420RTKAbstraction();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        super.initFlightControllerSupportParameter();
        if (DJIComponentManager.PlatformType.PM420PRO_RTK == DJIComponentManager.getInstance().getPlatformType()) {
            notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_RTK_SUPPORTED));
        } else {
            notifyValueChangeForKeyPath((Object) false, convertKeyToPath(FlightControllerKeys.IS_RTK_SUPPORTED));
        }
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOnboardGetPushDualGimbals.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOnboardGetPushDualGimbals.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public boolean isOnboardSDKAvailable() {
        DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
        if (DJIComponentManager.PlatformType.PM420PRO_RTK == platformType || DJIComponentManager.PlatformType.PM420PRO == platformType) {
            return true;
        }
        return super.isOnboardSDKAvailable();
    }

    @Setter(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR)
    public void setControlGimbalBehavior(ControlGimbalBehavior behavior, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (behavior == ControlGimbalBehavior.BOTH_MOVE) {
            new DataOnBoardControlGimbalsSimully().enable(true).start(CallbackUtils.defaultCB(callback, DJIGimbalBehaviorError.class));
        } else {
            new DataOnBoardControlGimbalsSimully().enable(false).start(CallbackUtils.defaultCB(callback));
        }
    }

    @Setter(FlightControllerKeys.MASTER_LIVE_VIDEO_CAMERA)
    public void setMasterLiveVideoCamera(SettingsDefinitions.CameraType type, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (type == SettingsDefinitions.CameraType.OTHER) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataDm368SetMainLiveVideoCamera.getInstance().setCameraType(DataCameraGetPushStateInfo.CameraType.find(type.value())).setRcModel(!CommonUtil.isSlaveRc(null) ? DataRcSetMaster.MODE.Master : DataRcSetMaster.MODE.Slave).start(CallbackUtils.defaultCB(callback));
    }

    @Action(FlightControllerKeys.START_MASS_CENTER_CALI)
    public void startMassCenterCali(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.MASS_CENTER_CALI).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPM420Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
                FlightControllerPM420Abstraction.this.notifyValueChangeForKeyPath(new MassCenterState(MassCenterState.MassCenterCaliState.CALCULATING), FlightControllerPM420Abstraction.this.convertKeyToPath(FlightControllerKeys.MASS_CENTER_STATE));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICcodeError.getDJIError(ccode));
                FlightControllerPM420Abstraction.this.notifyValueChangeForKeyPath(new MassCenterState(MassCenterState.MassCenterCaliState.STANDBY), FlightControllerPM420Abstraction.this.convertKeyToPath(FlightControllerKeys.MASS_CENTER_STATE));
            }
        });
    }

    @Action(FlightControllerKeys.EXIT_MASS_CENTER_CALI)
    public void cancelMassCenterCali(DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlycFunctionControl.getInstance().setCommand(DataFlycFunctionControl.FLYC_COMMAND.EXIT_MASS_CENTER_CALI).start(CallbackUtils.defaultCB(callback));
    }

    @Setter(FlightControllerKeys.ENABLE_OSDK)
    public void setEnabledStateOsdk(OSDKEnabledState state, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOnBoardSdkSetEnabledState setter = new DataOnBoardSdkSetEnabledState();
        setter.setEnabledState(state.isPowerOn());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(FlightControllerKeys.ENABLE_OSDK)
    public void getEnabledStateOsdk(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetEnabledState getter = DataOnBoardSdkGetEnabledState.getInstance();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPM420Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                OSDKEnabledState powerStateOnBoard = new OSDKEnabledState();
                powerStateOnBoard.setPowerOn(getter.isPowerOn());
                CallbackUtils.onSuccess(callback, powerStateOnBoard);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.ENABLE_PPS)
    public void setEnabledStatePPS(OSDKEnabledState state, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOnBoardSdkSetPPSEnabledState setter = new DataOnBoardSdkSetPPSEnabledState();
        setter.setEnabledState(state.isPowerOn());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(FlightControllerKeys.ENABLE_PPS)
    public void getEnabledStatePPS(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetPPSEnabledState getter = DataOnBoardSdkGetPPSEnabledState.getInstance();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPM420Abstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                OSDKEnabledState powerStateOnBoard = new OSDKEnabledState();
                powerStateOnBoard.setPowerOn(getter.isPowerOn());
                CallbackUtils.onSuccess(callback, powerStateOnBoard);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.ENABLE_VISION)
    public void setEnabledStateVision(OSDKEnabledState state, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOnBoardSdkSetVisionEnabledState setter = new DataOnBoardSdkSetVisionEnabledState();
        setter.setEnabledState(state.isPowerOn());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(FlightControllerKeys.ENABLE_VISION)
    public void getEnabledStateVision(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetVisionEnabledState getter = DataOnBoardSdkGetVisionEnabledState.getInstance();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPM420Abstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                OSDKEnabledState powerStateOnBoard = new OSDKEnabledState();
                powerStateOnBoard.setPowerOn(getter.isPowerOn());
                CallbackUtils.onSuccess(callback, powerStateOnBoard);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Action(FlightControllerKeys.RESET_MASS_CENTER_CALI)
    public void resetMassCenterCali(DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycSetParams().setInfo("mass_center_calibrated_0", 0).start(CallbackUtils.defaultCB(callback));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnboardGetPushDualGimbals dualGimbals) {
        if (dualGimbals.isGetted()) {
            if (dualGimbals.isGimbalBehaviorBoth()) {
                notifyValueChangeForKeyPath(ControlGimbalBehavior.BOTH_MOVE, convertKeyToPath(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
            } else {
                notifyValueChangeForKeyPath(ControlGimbalBehavior.UNKNOWN, convertKeyToPath(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushMassCenterCaliStatus massCenterCaliStatus) {
        MassCenterState.MassCenterCaliState caliState;
        MassCenterState.MassCenterCaliExitReason exitReason = null;
        if (!massCenterCaliStatus.isGetted()) {
            caliState = MassCenterState.MassCenterCaliState.STANDBY;
        } else {
            caliState = MassCenterState.MassCenterCaliState.find(massCenterCaliStatus.getCaliState().value());
        }
        if (caliState == MassCenterState.MassCenterCaliState.FAILED || caliState == MassCenterState.MassCenterCaliState.FINISHED) {
            exitReason = MassCenterState.MassCenterCaliExitReason.find(massCenterCaliStatus.getCaliExitReason().value());
        }
        notifyValueChangeForKeyPath(new MassCenterState(caliState, exitReason), convertKeyToPath(FlightControllerKeys.MASS_CENTER_STATE));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataModule4GGetPushOperator operator) {
        this.mLteSignalHelper.operatorChange(operator);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataModule4GGetPushSignal signal) {
        this.mLteSignalHelper.signalChange(signal);
    }
}
