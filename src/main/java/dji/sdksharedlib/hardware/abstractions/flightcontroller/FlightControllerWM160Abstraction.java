package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.error.DJIMissionError;
import dji.common.flightcontroller.ConnectionFailSafeBehavior;
import dji.common.flightcontroller.FlightOrientationMode;
import dji.common.util.CallbackUtils;
import dji.common.util.DJIParamMinMaxCapability;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCommonGetDeviceSerialNumber;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.ParamInfoCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FlightControllerWM160Abstraction extends FlightControllerWM230Abstraction {
    private static final String TAG = "FlightControllerWM160Abstraction";
    private DJISDKCacheProductConfigManager mFlycParamConfigManager = DJISDKCacheProductConfigManager.getInstance();

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        CacheHelper.addFlightControllerListener(this, FlightControllerKeys.IMU_NEED_CALIBRATION_TYPE);
    }

    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistantWM160Abstraction();
    }

    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        super.initFlightControllerSupportParameter();
        this.imuCount = 1;
        notifyValueChangeForKeyPath(Integer.valueOf(this.imuCount), convertKeyToPath(FlightControllerKeys.IMU_COUNT));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome information) {
        super.onEvent3BackgroundThread(information);
    }

    /* access modifiers changed from: protected */
    public boolean isFlightFailSafeOperationSupported(ConnectionFailSafeBehavior operation) {
        return operation == ConnectionFailSafeBehavior.GO_HOME;
    }

    @Setter(FlightControllerKeys.CONNECTION_FAIL_SAFE_BEHAVIOR)
    public void setFlightFailSafeOperation(ConnectionFailSafeBehavior operation, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isFlightFailSafeOperationSupported(operation)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            super.setFlightFailSafeOperation(operation, callback);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isFlightOrientationModeSupported(FlightOrientationMode mode) {
        return mode == FlightOrientationMode.AIRCRAFT_HEADING;
    }

    @Setter(FlightControllerKeys.ORIENTATION_MODE)
    public void setFlightOrientationMode(FlightOrientationMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isFlightOrientationModeSupported(mode)) {
            CallbackUtils.onFailure(callback, DJIMissionError.COMMON_PARAM_ILLEGAL);
        } else {
            super.setFlightOrientationMode(mode, callback);
        }
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetDeviceSerialNumber deviceSerialNumber = new DataCommonGetDeviceSerialNumber();
        deviceSerialNumber.setReceiveId(DataCommonGetDeviceSerialNumber.DeviceIndex.BoardNum);
        deviceSerialNumber.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, deviceSerialNumber.getSerialNum());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.IMU_NEED_CALIBRATION_TYPE)
    public void getImuPostCalibrationParam(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetFlycParamInfo.getInfo(ParamCfgName.GSTATUS_GYRACC_NEED_CALTYPE, new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass2 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(callback, Integer.valueOf(info.value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Action(FlightControllerKeys.START_IMU_CALIBRATION)
    public void startImuCalibration(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DataOsdGetPushCommon.getInstance().isMotorUp()) {
            String[] str_1 = {ParamCfgName.GCONFIG_IMU_STARTCAL_0};
            int param = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.IMU_NEED_CALIBRATION_TYPE));
            if (param != 1) {
                param = 3;
            }
            sendImuCalibrationCmd(str_1, new Number[]{Integer.valueOf(param)}, callback);
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.IMU_CALIBRATION_ERROR_IN_THE_AIR_OR_MOTORS_ON);
        }
    }

    @Setter(FlightControllerKeys.MAX_FLIGHT_RADIUS)
    public void setMaxFlightRadius(final int maxRadius, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJIParamMinMaxCapability capability = (DJIParamMinMaxCapability) CacheHelper.getFlightController(FlightControllerKeys.MAX_FLIGHT_RADIUS_RANGE);
        int min = capability.getMin().intValue();
        int max = capability.getMax().intValue();
        if (maxRadius < min || maxRadius > max) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setIndexs(ParamCfgName.GCONFIG_FLY_LIMIT);
        setParams.setValues(Integer.valueOf(maxRadius));
        setParams.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJILog.logWriteI(FlightControllerWM160Abstraction.TAG, "set max radius success " + maxRadius, new Object[0]);
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                DJILog.logWriteE(FlightControllerWM160Abstraction.TAG, "set max radius fail " + ccode, new Object[0]);
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.PROP_COVER_LIMIT_ENABLED)
    public void isPropCoverLimitEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetFlycParamInfo.getInfo(this.mFlycParamConfigManager.getSupportedConfigHashKey(FlightControllerKeys.PROP_COVER_LIMIT_ENABLED), new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass4 */

            public void onSuccess(ParamInfo info) {
                boolean isEnabled = true;
                if (info.value.intValue() != 1) {
                    isEnabled = false;
                }
                CallbackUtils.onSuccess(callback, Boolean.valueOf(isEnabled));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(FlightControllerKeys.PROP_COVER_LIMIT_ENABLED)
    public void setPropCoverLimitEnabled(Boolean isEnabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.PROP_COVER_LIMIT_ENABLED, Integer.valueOf(isEnabled.booleanValue() ? 1 : 0), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass5 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(FlightControllerKeys.CONFIG_DARK_NEED_GPS)
    public void setIsDarkNeedGps(Boolean isNeed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeSetFlyParamInfo.getInfo(FlightControllerKeys.CONFIG_DARK_NEED_GPS, Integer.valueOf(isNeed.booleanValue() ? 1 : 0), new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass6 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(FlightControllerKeys.PROP_COVER_LIMIT_HEIGHT)
    public void getPropCoverLimitHeight(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetFlycParamInfo.getInfo(this.mFlycParamConfigManager.getSupportedConfigHashKey(FlightControllerKeys.PROP_COVER_LIMIT_HEIGHT), new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass7 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(callback, Integer.valueOf(info.value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.PROP_COVER_LIMIT_RADIUS)
    public void getPropCoverLimitRadius(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetFlycParamInfo.getInfo(this.mFlycParamConfigManager.getSupportedConfigHashKey(FlightControllerKeys.PROP_COVER_LIMIT_RADIUS), new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction.AnonymousClass8 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(callback, Integer.valueOf(info.value.intValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public DJIParamMinMaxCapability getMaxFlightRadiusRange() {
        return new DJIParamMinMaxCapability(true, 20, 5000);
    }
}
