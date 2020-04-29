package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.flightcontroller.ControlGimbalBehavior;
import dji.common.flightcontroller.GPIOWorkModeOnBoard;
import dji.common.flightcontroller.IOMode;
import dji.common.flightcontroller.IOStateOnBoard;
import dji.common.flightcontroller.PowerStateOnBoard;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOnBoardControlGimbalsSimully;
import dji.midware.data.model.P3.DataOnBoardSdkGetIOState;
import dji.midware.data.model.P3.DataOnBoardSdkGetPowerState;
import dji.midware.data.model.P3.DataOnBoardSdkSetIOState;
import dji.midware.data.model.P3.DataOnBoardSdkSetPowerState;
import dji.midware.data.model.P3.DataOnBoardSetMappedGimbal;
import dji.midware.data.model.P3.DataOnboardGetPushMixInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FlightControllerM200Abstraction extends FlightControllerInspire2Abstraction {
    /* access modifiers changed from: protected */
    public RTKAbstraction getRTKAbstractionIfSupported() {
        return new RTKAbstraction();
    }

    /* access modifiers changed from: protected */
    public boolean isOnboardSDKAvailable() {
        if (DJIComponentManager.PlatformType.M210RTK == DJIComponentManager.getInstance().getPlatformType()) {
            return true;
        }
        return false;
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
        notifyValueChangeForKeyPath(ControlGimbalBehavior.ONLY_LEFT_MOVE, convertKeyToPath(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
        if (DJIComponentManager.PlatformType.M210RTK == platformType || DJIComponentManager.PlatformType.M210 == platformType) {
            this.compassCount = 2;
        } else {
            this.compassCount = 1;
        }
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
        if (DJIComponentManager.PlatformType.M210RTK == platformType) {
            notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_RTK_SUPPORTED));
        }
    }

    @Action(FlightControllerKeys.INIT_IO)
    public void initIO(DJISDKCacheHWAbstraction.InnerCallback callback, Integer index, IOStateOnBoard ioStateOnBoard) {
        DataOnBoardSdkSetIOState setter = new DataOnBoardSdkSetIOState();
        setter.setAction(DataOnBoardSdkSetIOState.CtrlAction.Initiate);
        setter.setIOIndex(index.intValue());
        if (ioStateOnBoard.getIoMode() == IOMode.GPIO) {
            setter.setProperty(DataOnBoardSdkSetIOState.IOProperty.GPIO);
            if (ioStateOnBoard.getGpioWorkModeOnBoard() == GPIOWorkModeOnBoard.PUSH_PULL_OUTPUT) {
                setter.setHighElectricLevel(ioStateOnBoard.isInHighElectricLevel());
            }
            setter.setGPIOMode(DataOnBoardSdkSetIOState.GPIOMode.find(ioStateOnBoard.getGpioWorkModeOnBoard().value()));
            setter.start(CallbackUtils.defaultCB(callback));
        } else if (ioStateOnBoard.getIoMode() == IOMode.PWM) {
            setter.setProperty(DataOnBoardSdkSetIOState.IOProperty.PWM);
            setter.setInitDutyRatio(ioStateOnBoard.getDutyRatioOfPWM());
            setter.setFrequency(ioStateOnBoard.getFrequencyOfPWM());
            setter.start(CallbackUtils.defaultCB(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    private void setIOParams(DJISDKCacheHWAbstraction.InnerCallback callback, Integer index, IOStateOnBoard ioStateOnBoard) {
        DataOnBoardSdkSetIOState setter = new DataOnBoardSdkSetIOState();
        setter.setAction(DataOnBoardSdkSetIOState.CtrlAction.SetParams);
        setter.setIOIndex(index.intValue());
        if (ioStateOnBoard.getIoMode() == IOMode.GPIO) {
            setter.setProperty(DataOnBoardSdkSetIOState.IOProperty.GPIO);
            setter.setHighElectricLevel(ioStateOnBoard.isInHighElectricLevel());
            setter.start(CallbackUtils.defaultCB(callback));
        } else if (ioStateOnBoard.getIoMode() == IOMode.PWM) {
            setter.setProperty(DataOnBoardSdkSetIOState.IOProperty.PWM);
            setter.setDutyRatio(ioStateOnBoard.getDutyRatioOfPWM());
            setter.start(CallbackUtils.defaultCB(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(FlightControllerKeys.POWER_ON_BOARD)
    public void setPowerState(PowerStateOnBoard stateOnBoard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOnBoardSdkSetPowerState setter = new DataOnBoardSdkSetPowerState();
        setter.setPowerState(stateOnBoard.isPowerOn());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(FlightControllerKeys.POWER_ON_BOARD)
    public void getPowerState(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetPowerState getter = DataOnBoardSdkGetPowerState.getInstance();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                PowerStateOnBoard powerStateOnBoard = new PowerStateOnBoard();
                powerStateOnBoard.setPowerOn(getter.isPowerOn());
                powerStateOnBoard.setOverLoaded(getter.isOverLoaded());
                powerStateOnBoard.setElectricCurrent(getter.getElectricCurrent());
                CallbackUtils.onSuccess(callback, powerStateOnBoard);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(FlightControllerKeys.IOSTATE_1)
    public void getIOStateIndex1(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetIOState getter = new DataOnBoardSdkGetIOState();
        getter.setIOIndex(1);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                FlightControllerM200Abstraction.this.generateResult(getter, callback);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.IOSTATE_1)
    public void setIOStateIndex1(IOStateOnBoard stateOnBoard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setIOParams(callback, 1, stateOnBoard);
    }

    @Getter(FlightControllerKeys.IOSTATE_2)
    public void getIOStateIndex2(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetIOState getter = new DataOnBoardSdkGetIOState();
        getter.setIOIndex(2);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                FlightControllerM200Abstraction.this.generateResult(getter, callback);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.IOSTATE_2)
    public void setIOStateIndex2(IOStateOnBoard stateOnBoard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setIOParams(callback, 2, stateOnBoard);
    }

    @Getter(FlightControllerKeys.IOSTATE_3)
    public void getIOStateIndex3(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetIOState getter = new DataOnBoardSdkGetIOState();
        getter.setIOIndex(3);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                FlightControllerM200Abstraction.this.generateResult(getter, callback);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.IOSTATE_3)
    public void setIOStateIndex3(IOStateOnBoard stateOnBoard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setIOParams(callback, 3, stateOnBoard);
    }

    @Getter(FlightControllerKeys.IOSTATE_4)
    public void getIOStateIndex4(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetIOState getter = new DataOnBoardSdkGetIOState();
        getter.setIOIndex(4);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                FlightControllerM200Abstraction.this.generateResult(getter, callback);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.IOSTATE_4)
    public void setIOStateIndex4(IOStateOnBoard stateOnBoard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setIOParams(callback, 4, stateOnBoard);
    }

    @Getter(FlightControllerKeys.IOSTATE_0)
    public void getIOStateIndex0(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOnBoardSdkGetIOState getter = new DataOnBoardSdkGetIOState();
        getter.setIOIndex(0);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                FlightControllerM200Abstraction.this.generateResult(getter, callback);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.IOSTATE_0)
    public void setIOStateIndex0(IOStateOnBoard stateOnBoard, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setIOParams(callback, 0, stateOnBoard);
    }

    /* access modifiers changed from: private */
    public void generateResult(DataOnBoardSdkGetIOState getter, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOnBoardSdkSetIOState.IOProperty ioProperty = getter.getProperty();
        if (ioProperty == DataOnBoardSdkSetIOState.IOProperty.GPIO) {
            CallbackUtils.onSuccess(callback, IOStateOnBoard.Builder.createReturnValue(getter.isHighElectricLevel(), getter.getGPIOMode(), getter.hasInitialized()));
        } else if (ioProperty == DataOnBoardSdkSetIOState.IOProperty.PWM) {
            CallbackUtils.onSuccess(callback, IOStateOnBoard.Builder.createReturnValue(getter.getDutyRatio(), getter.getFrequency(), getter.hasInitialized()));
        }
    }

    private void setMappedGimbal(int gimbal, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (gimbal == 0 || gimbal == 1) {
            DataOnBoardSetMappedGimbal setter = new DataOnBoardSetMappedGimbal();
            setter.setMappedGimbalID(gimbal);
            setter.start(CallbackUtils.defaultCB(callback));
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
    }

    @Setter(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR)
    public void setControlGimbalBehavior(ControlGimbalBehavior behavior, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (behavior == ControlGimbalBehavior.BOTH_MOVE) {
            new DataOnBoardControlGimbalsSimully().enable(true).start(CallbackUtils.defaultCB(callback));
            return;
        }
        new DataOnBoardControlGimbalsSimully().enable(false).start((DJIDataCallBack) null);
        if (behavior == ControlGimbalBehavior.ONLY_LEFT_MOVE) {
            setMappedGimbal(0, callback);
        } else {
            setMappedGimbal(1, callback);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnboardGetPushMixInfo params) {
        if (params.isGetted()) {
            if (params.isSimultaneousControlGimbal()) {
                notifyValueChangeForKeyPath(ControlGimbalBehavior.BOTH_MOVE, convertKeyToPath(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
            } else {
                notifyValueChangeForKeyPath(ControlGimbalBehavior.find(params.getMappedGimbal()), convertKeyToPath(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isPropellerCalibrationSupported() {
        return true;
    }

    @Action(FlightControllerKeys.START_PROPELLER_CALIBRATION)
    public void startPropellerCalibration(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlycSetParams params = new DataFlycSetParams();
        params.setIndexs("imu_proprller", "g_config_factory_test_cfg_enale");
        params.setValues(1, 1);
        params.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Action(FlightControllerKeys.STOP_PROPELLER_CALIBRATION)
    public void stopPropellerCalibration(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlycSetParams params = new DataFlycSetParams();
        params.setIndexs("imu_proprller", "g_config_factory_test_cfg_enale");
        params.setValues(2, 0);
        params.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }
}
