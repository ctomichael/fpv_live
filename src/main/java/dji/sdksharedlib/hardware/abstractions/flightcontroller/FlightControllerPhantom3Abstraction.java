package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.imu.CalibrationState;
import dji.common.flightcontroller.imu.SensorState;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetPushParamsByHash;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FlightControllerPhantom3Abstraction extends FlightControllerAbstraction {
    private boolean hasStartedCalibration = false;

    @Action(FlightControllerKeys.START_IMU_CALIBRATION)
    public void startImuCalibration(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ParamInfo calibInfo = DJIFlycParamInfoManager.read("imu_app_temp_cali.start_flag_0");
        if (!DataOsdGetPushCommon.getInstance().isMotorUp()) {
            new DataFlycSetParams().setInfo(calibInfo.name, 1).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom3Abstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.IMU_CALIBRATION_ERROR_IN_THE_AIR_OR_MOTORS_ON);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushParamsByHash pushParams) {
        super.onEvent3BackgroundThread(pushParams);
        updateIMUState(pushParams);
    }

    /* access modifiers changed from: protected */
    public void updateIMUState(DataFlycGetPushParamsByHash params) {
        SensorState gyroState;
        SensorState acceState;
        int progress = DJIFlycParamInfoManager.valueOf("imu_app_temp_cali.cali_cnt_0").intValue();
        CalibrationState imuCaliStatus = updateIMUCaliStatus("imu_app_temp_cali.state_0");
        if (imuCaliStatus.equals(CalibrationState.CALIBRATING)) {
            gyroState = SensorState.CALIBRATING;
            acceState = SensorState.CALIBRATING;
        } else if (DataOsdGetPushCommon.getInstance().isImuInitError()) {
            gyroState = SensorState.DATA_EXCEPTION;
            acceState = SensorState.DATA_EXCEPTION;
        } else if (!DataOsdGetPushCommon.getInstance().isImuPreheatd()) {
            gyroState = SensorState.WARMING_UP;
            acceState = SensorState.WARMING_UP;
        } else {
            gyroState = SensorState.NORMAL_BIAS;
            acceState = SensorState.NORMAL_BIAS;
        }
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(FlightControllerKeys.SUBCOMPONENT_IMU_KEY).subComponentIndex(0);
        notifyValueChangeForKeyPath(gyroState, builder.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_STATE).build());
        notifyValueChangeForKeyPath(acceState, builder.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_STATE).build());
        notifyValueChangeForKeyPath(Integer.valueOf(progress), builder.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_PROGRESS).build());
        notifyValueChangeForKeyPath(imuCaliStatus, builder.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_STATE).build());
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return false;
    }
}
