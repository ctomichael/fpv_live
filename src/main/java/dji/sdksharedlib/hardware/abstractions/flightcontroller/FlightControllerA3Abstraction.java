package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.flightcontroller.IOMode;
import dji.common.flightcontroller.IOStateOnBoard;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataFlycGetFChannelOutputValue;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycGetPushParamsByHash;
import dji.midware.data.model.P3.DataFlycSetFChannelInitialization;
import dji.midware.data.model.P3.DataFlycSetFChannelOutputValue;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;

@EXClassNullAway
public class FlightControllerA3Abstraction extends FlightControllerAbstraction {
    private final String[] IMU_CALC_STAT_CONFIG = {ParamCfgName.GSTATUS_GYRACC_STATE_0, ParamCfgName.GSTATUS_GYRACC_STATE_1, ParamCfgName.GSTATUS_GYRACC_STATE_2, "g_status.acc_gyro[0].cali_cnt_0", "g_status.acc_gyro[1].cali_cnt_0", "g_status.acc_gyro[2].cali_cnt_0", ParamCfgName.GSTATUS_GRYACC_TEMP_READY_0, ParamCfgName.GSTATUS_GRYACC_TEMP_READY_1, ParamCfgName.GSTATUS_GRYACC_TEMP_READY_2};

    /* access modifiers changed from: protected */
    public boolean isLandingGearMovable() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isOnboardSDKAvailable() {
        return true;
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
        this.imuCount = 3;
        this.compassCount = 3;
        notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_ONBOARD_FCHANNEL_AVAILABLE));
        notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_RTK_SUPPORTED));
        notifyValueChangeForKeyPath(Integer.valueOf(this.imuCount), convertKeyToPath(FlightControllerKeys.IMU_COUNT));
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    /* access modifiers changed from: protected */
    public RTKAbstraction getRTKAbstractionIfSupported() {
        return new RTKAbstraction();
    }

    @Action(FlightControllerKeys.START_IMU_CALIBRATION_WITH_ID)
    public void startImuCalibrationWithID(final DJISDKCacheHWAbstraction.InnerCallback callback, int index) {
        int i;
        int i2 = 1;
        Integer imuCount = (Integer) DJISDKCache.getInstance().getAvailableValue(new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).paramKey(FlightControllerKeys.IMU_COUNT).build()).getData();
        if (index < 0 || index >= imuCount.intValue()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        String[] str = {ParamCfgName.GCONFIG_IMU_STARTCAL_0, ParamCfgName.GCONFIG_IMU_STARTCAL_1, ParamCfgName.GCONFIG_IMU_STARTCAL_2};
        Number[] nums = new Number[3];
        if (index == 0) {
            i = 1;
        } else {
            i = 0;
        }
        nums[0] = Integer.valueOf(i);
        nums[1] = Integer.valueOf(index == 1 ? 1 : 0);
        if (index != 2) {
            i2 = 0;
        }
        nums[2] = Integer.valueOf(i2);
        DataFlycSetParams params = new DataFlycSetParams();
        params.setIndexs(str);
        params.setValues(nums);
        params.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.LANDING_GEAR_HIDE_ENABLED)
    public void setHideGearEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        boolean isNewFlyVer;
        if (enable) {
            i = 1;
        } else {
            i = 0;
        }
        Number info = Integer.valueOf(i);
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 16) {
            isNewFlyVer = true;
        } else {
            isNewFlyVer = false;
        }
        this.mergeSetFlyParamInfo.getInfo(isNewFlyVer ? FlightControllerKeys.FLIGHT_CONTROLLER_CONFIG_ON_GROUND_HIDE_GEAR : FlightControllerKeys.FLIGHT_CONTROLLER_CONFIG_HIDE_GEAR, info, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass2 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(FlightControllerKeys.LANDING_GEAR_HIDE_ENABLED)
    public void getHideGearEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.newMergeGetFlycParamInfo.getInfo(DataOsdGetPushCommon.getInstance().getFlycVersion() < 16 ? FlightControllerKeys.FLIGHT_CONTROLLER_CONFIG_ON_GROUND_HIDE_GEAR : FlightControllerKeys.FLIGHT_CONTROLLER_CONFIG_HIDE_GEAR, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass3 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(((Number) object).intValue() != 0));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void updateIMUState(final DataFlycGetPushParamsByHash param) {
        if (DataOsdGetPushCommon.getInstance().groundOrSky() != 2) {
            new DataFlycGetParams().setInfos(this.IMU_CALC_STAT_CONFIG).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    FlightControllerA3Abstraction.super.updateIMUState(param);
                }

                public void onFailure(Ccode ccode) {
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return true;
    }

    @Action(FlightControllerKeys.INIT_IO)
    public void initIO(DJISDKCacheHWAbstraction.InnerCallback callback, Integer index, IOStateOnBoard ioStateOnBoard) {
        DataFlycSetFChannelInitialization setter = new DataFlycSetFChannelInitialization();
        setter.setPortIndex(index.intValue());
        if (ioStateOnBoard.getIoMode() == IOMode.PWM) {
            setter.setFrequency((short) ioStateOnBoard.getFrequencyOfPWM());
            setter.setInitValue(ioStateOnBoard.getDutyRatioOfPWM());
            if (ioStateOnBoard.isOutputMode()) {
                setter.setMode(DataFlycSetFChannelInitialization.FChannelMode.PWM_OUTPUT);
            } else {
                setter.setMode(DataFlycSetFChannelInitialization.FChannelMode.PWM_INPUT);
            }
        } else {
            setter.setInitValue(ioStateOnBoard.isInHighElectricLevel() ? 1 : 0);
            if (ioStateOnBoard.isOutputMode()) {
                setter.setMode(DataFlycSetFChannelInitialization.FChannelMode.GPIO_OUTPUT);
            } else {
                setter.setMode(DataFlycSetFChannelInitialization.FChannelMode.GPIO_INPUT);
            }
        }
        setter.start(CallbackUtils.getFChannelCallback(new FlightControllerA3Abstraction$$Lambda$0(setter), callback));
    }

    private void setIOParams(DJISDKCacheHWAbstraction.InnerCallback callback, Integer index, IOStateOnBoard ioStateOnBoard) {
        DataFlycSetFChannelOutputValue setter = new DataFlycSetFChannelOutputValue();
        setter.setPortIndex(index.intValue());
        if (ioStateOnBoard.getIoMode() == IOMode.PWM) {
            setter.setOutputValue(ioStateOnBoard.getDutyRatioOfPWM());
        } else {
            setter.setOutputValue(ioStateOnBoard.isInHighElectricLevel() ? 1 : 0);
        }
        setter.start(CallbackUtils.getFChannelCallback(new FlightControllerA3Abstraction$$Lambda$1(setter), callback));
    }

    @Getter(FlightControllerKeys.IOSTATE_1)
    public void getIOStateIndex1(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataFlycGetFChannelOutputValue getter = new DataFlycGetFChannelOutputValue();
        getter.setPortIndex(1);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                FlightControllerA3Abstraction.this.generateResult(getter, callback);
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
        final DataFlycGetFChannelOutputValue getter = new DataFlycGetFChannelOutputValue();
        getter.setPortIndex(2);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                FlightControllerA3Abstraction.this.generateResult(getter, callback);
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
        final DataFlycGetFChannelOutputValue getter = new DataFlycGetFChannelOutputValue();
        getter.setPortIndex(3);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                FlightControllerA3Abstraction.this.generateResult(getter, callback);
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
        final DataFlycGetFChannelOutputValue getter = new DataFlycGetFChannelOutputValue();
        getter.setPortIndex(4);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                FlightControllerA3Abstraction.this.generateResult(getter, callback);
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
        final DataFlycGetFChannelOutputValue getter = new DataFlycGetFChannelOutputValue();
        getter.setPortIndex(0);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction.AnonymousClass9 */

            public void onSuccess(Object model) {
                FlightControllerA3Abstraction.this.generateResult(getter, callback);
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
    public void generateResult(DataFlycGetFChannelOutputValue getter, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onSuccess(callback, IOStateOnBoard.Builder.createFChannelReturnValue(getter.getDutyRatio(), getter.isHighElectricLevel()));
    }
}
