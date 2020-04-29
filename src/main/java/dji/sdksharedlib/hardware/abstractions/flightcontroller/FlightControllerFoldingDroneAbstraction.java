package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.UrgentStopMotorMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantMavicProAbstraction;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;

@EXClassNullAway
public class FlightControllerFoldingDroneAbstraction extends FlightControllerPhantom4Abstraction {
    private static final String TAG = "DJIFlightControllerFoldingDroneAbstraction";

    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistantMavicProAbstraction();
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
        this.imuCount = 2;
        this.compassCount = 2;
        notifyValueChangeForKeyPath(Integer.valueOf(this.imuCount), convertKeyToPath(FlightControllerKeys.IMU_COUNT));
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
        notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_FLIGHT_ASSISTANT_SUPPORTED));
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    @Setter("TrackingMaximumSpeed")
    public void setTrackingMaximumSpeed(int trackingMaximumSpeed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setTrackingMaximumSpeed(trackingMaximumSpeed).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_MAXIMUM_SPEED).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerFoldingDroneAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(FlightControllerKeys.URGENT_STOP_MOTOR_MODE)
    public void setStopMotorMode(UrgentStopMotorMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (UrgentStopMotorMode.CSC == mode || UrgentStopMotorMode.NEVER == mode || UrgentStopMotorMode.UNKNOWN == mode) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        int flycVersion = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION));
        int cmdNo = -1;
        if (flycVersion < 16 || flycVersion >= 21) {
            if (flycVersion < 21) {
                CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
                return;
            } else if (UrgentStopMotorMode.IN_OUT_WHEN_BREAKDOWN == mode) {
                cmdNo = 6;
            } else if (UrgentStopMotorMode.IN_OUT_ALWAYS == mode) {
                cmdNo = 2;
            }
        } else if (UrgentStopMotorMode.IN_OUT_ALWAYS == mode) {
            cmdNo = 1;
        } else if (UrgentStopMotorMode.IN_OUT_WHEN_BREAKDOWN == mode) {
            cmdNo = 2;
        }
        DataFlycSetParams setParams = new DataFlycSetParams();
        setParams.setInfo(ParamCfgName.GSTATUS_RC_STOP_MOTOR_TYPE, Integer.valueOf(cmdNo));
        setParams.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(FlightControllerKeys.URGENT_STOP_MOTOR_MODE)
    public void getStopMotorMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        String[] indexes = {ParamCfgName.GSTATUS_RC_STOP_MOTOR_TYPE};
        final int flycVersion = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION));
        new DataFlycGetParams().setInfos(indexes).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerFoldingDroneAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                int cmdIndex = DJIFlycParamInfoManager.read(ParamCfgName.GSTATUS_RC_STOP_MOTOR_TYPE).value.intValue();
                if (flycVersion < 16 || flycVersion >= 21) {
                    if (flycVersion < 21) {
                        CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
                    } else if (6 == cmdIndex) {
                        CallbackUtils.onSuccess(callback, UrgentStopMotorMode.IN_OUT_WHEN_BREAKDOWN);
                    } else if (2 == cmdIndex) {
                        CallbackUtils.onSuccess(callback, UrgentStopMotorMode.IN_OUT_ALWAYS);
                    } else {
                        CallbackUtils.onFailure(callback, DJIError.COMMON_UNKNOWN);
                    }
                } else if (1 == cmdIndex) {
                    CallbackUtils.onSuccess(callback, UrgentStopMotorMode.IN_OUT_ALWAYS);
                } else if (2 == cmdIndex) {
                    CallbackUtils.onSuccess(callback, UrgentStopMotorMode.IN_OUT_WHEN_BREAKDOWN);
                } else {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_UNKNOWN);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }
}
