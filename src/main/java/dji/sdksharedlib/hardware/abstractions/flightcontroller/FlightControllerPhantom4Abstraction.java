package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.flightcontroller.imu.CalibrationState;
import dji.common.flightcontroller.imu.SensorState;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycGetPushAvoidParam;
import dji.midware.data.model.P3.DataFlycGetPushParamsByHash;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FlightControllerPhantom4Abstraction extends FlightControllerAbstraction {
    private final String[] IMU_CALC_STAT_CONFIG = {ParamCfgName.GSTATUS_GYRACC_STATE_0, ParamCfgName.GSTATUS_GYRACC_STATE_1, "g_status.acc_gyro[0].cali_cnt_0", "g_status.acc_gyro[1].cali_cnt_0"};

    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistant1860Abstraction();
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

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushAvoidParam param) {
        super.onEvent3BackgroundThread(param);
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isAvoidOvershotAct()), convertKeyToPath("IsAvoidingActiveObstacleCollision"));
    }

    @Action(FlightControllerKeys.START_IMU_CALIBRATION)
    public void startImuCalibration(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        String[] str = {ParamCfgName.GCONFIG_IMU_STARTCAL_0, ParamCfgName.GCONFIG_IMU_STARTCAL_1};
        DataFlycSetParams params = new DataFlycSetParams();
        params.setIndexs(str);
        params.setValues(1, 1);
        params.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void updateIMUState(DataFlycGetPushParamsByHash param) {
        if (DataOsdGetPushCommon.getInstance().groundOrSky() != 2) {
            new DataFlycGetParams().setInfos(this.IMU_CALC_STAT_CONFIG).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4Abstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    SensorState gyroState0 = SensorState.find(DJIFlycParamInfoManager.valueOf("g_config.fdi_sensor[0].gyr_stat_0").intValue());
                    SensorState gyroState1 = SensorState.find(DJIFlycParamInfoManager.valueOf("g_config.fdi_sensor[1].gyr_stat_0").intValue());
                    SensorState acceState0 = SensorState.find(DJIFlycParamInfoManager.valueOf("g_config.fdi_sensor[0].acc_stat_0").intValue());
                    SensorState acceState1 = SensorState.find(DJIFlycParamInfoManager.valueOf("g_config.fdi_sensor[1].acc_stat_0").intValue());
                    int progress0 = DJIFlycParamInfoManager.read("g_status.acc_gyro[0].cali_cnt_0").value.byteValue();
                    int progress1 = DJIFlycParamInfoManager.read("g_status.acc_gyro[1].cali_cnt_0").value.byteValue();
                    CalibrationState imuCaliStatus0 = FlightControllerPhantom4Abstraction.this.updateIMUCaliStatus(ParamCfgName.GSTATUS_GYRACC_STATE_1);
                    CalibrationState imuCaliStatus1 = FlightControllerPhantom4Abstraction.this.updateIMUCaliStatus(ParamCfgName.GSTATUS_GYRACC_STATE_1);
                    DJISDKCacheKey.Builder imuBuilder0 = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(FlightControllerKeys.SUBCOMPONENT_IMU_KEY).subComponentIndex(0);
                    DJISDKCacheKey.Builder imuBuilder1 = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).index(0).subComponent(FlightControllerKeys.SUBCOMPONENT_IMU_KEY).subComponentIndex(1);
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(gyroState0, imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_STATE).build());
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(acceState0, imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_STATE).build());
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(progress0), imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_PROGRESS).build());
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(imuCaliStatus0, imuBuilder0.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_STATE).build());
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(gyroState1, imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_GYROSCOPE_STATE).build());
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(acceState1, imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_ACCELEROMETER_STATE).build());
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(progress1), imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_PROGRESS).build());
                    FlightControllerPhantom4Abstraction.this.notifyValueChangeForKeyPath(imuCaliStatus1, imuBuilder1.paramKey(FlightControllerKeys.IMU_STATE_CALIBRATION_STATE).build());
                }

                public void onFailure(Ccode ccode) {
                }
            });
        }
    }
}
