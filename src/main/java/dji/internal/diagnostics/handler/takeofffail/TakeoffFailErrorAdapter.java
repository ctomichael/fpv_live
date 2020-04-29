package dji.internal.diagnostics.handler.takeofffail;

import dji.diagnostics.model.DJIDiagnosticsError;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import java.util.HashMap;

public class TakeoffFailErrorAdapter {
    private static HashMap<DataOsdGetPushCommon.MotorStartFailedCause, Integer> sTakeoffFailCauseCodeMap;

    private static synchronized void initCodeMap() {
        synchronized (TakeoffFailErrorAdapter.class) {
            if (sTakeoffFailCauseCodeMap == null) {
                sTakeoffFailCauseCodeMap = new HashMap<>();
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.AssistantProtected, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ASSISTANT_PROTECTED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.DeviceLocked, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_DEVICE_LOCKED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.IMUSNError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_IMU_SN_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.IMUWarning, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_IMU_WARMING));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.CompassCalibrating, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_COMPASS_CALIBRATING));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.NoviceProtected, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_NOVICE_PROTECTED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.AttiError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_NO_ATTI_DATA));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BatteryCellError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_CELL_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BatteryCommuniteError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_COMMUNITE_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.SeriouLowVoltage, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_SERIOUS_LOW_VOLTAGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.SeriouLowPower, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_SERIOUS_LOW_POWER));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.TempureVolLow, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_TEMPERATURE_LOW));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.SmartLowToLand, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_SMART_LOW_TO_LAND));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BatteryNotReady, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_NOT_READY));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.SimulatorMode, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_SIMULATOR_MODE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.AttitudeAbNormal, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ATTITUDE_LIMIT));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.FlyForbiddenError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_IN_FLY_LIMIT_ZONE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BiasError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_IMU_BIAS_TOO_LARGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.EscError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ESC_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.ImuInitError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_IMU_INITING));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.SimulatorStarted, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_SIMULATOR_RUN));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.AttiAngleOver, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_LARGE_TILT));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.GyroscopeError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_GYROSCOPE_DEAD));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.AcceletorError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ACCELEROMETER_DEAD));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.CompassFailed, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_COMPASS_DEAD));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BarometerError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BAROMETER_DEAD));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BarometerNegative, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BAROMETER_NEGATIVE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.CompassBig, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_COMPASS_MODE_TOO_LARGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.GyroscopeBiasBig, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_GYRO_BIAS_TOO_LARGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.AcceletorBiasBig, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ACCELEROMETER_BIAS_TOO_LARGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.CompassNoiseBig, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_COMPASS_NOISE_TOO_LARGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BarometerNoiseBig, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BAROMETER_NOISE_TOO_LARGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.GPS_DISCONNECT, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_GPS_DISCONNECT));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.SDCardException, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_SD_CARD_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.IMUNoconnection, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_NAVIGATION_SYSTEM_DISCONNECTED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RCCalibrationException, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_REMOTE_CONTROLLER_CALIBRATION_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RCCalibrationUnfinished, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_REMOTE_CONTROLLER_CALIBRATION_UNFINISHED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RCCalibrationException2, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_REMOTE_CONTROLLER_MIDDLE_LARGE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RCCalibrationException3, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_REMOTE_CONTROLLER_MAPPING_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.NS_ABNORMAL, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_NAVIGATION_SYSTEM_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.TOPOLOGY_ABNORMAL, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_DEVICE_TOPOLOGY_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RC_NEED_CALI, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_REMOTE_CONTROLLER_NEED_CALIBRATION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.INVALID_FLOAT, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_SOFTWARE_DATA_INVALID));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.IMUcCalibrationFinished, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_REBOOT_AIRCRAFT));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.TAKEOFF_ROLLOVER, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_TAKE_OFF_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.MOTOR_STUCK, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_STALL_NEAR_GROUND));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.MOTOR_UNBALANCED, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_UNBALANCE_ON_GROUND));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.MOTOR_LESS_PADDLE, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_PART_PADDLE_EMPTY));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.MOTOR_START_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ENGINE_START_FAILED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.MOTOR_AUTO_TAKEOFF_FAIL, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_AUTO_TAKE_OFF_FAILED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RollOverOnGround, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ROLL_OVER_ON_GROUND));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BatVersionError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_VERSION_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RTK_BAD_SIGNAL, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_RTK_SIGN_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RTK_DEVIATION_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_RTK_COMPASS_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.ESC_CALIBRATING, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ESC_CALIBRATING));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.GPS_SIGN_INVALID, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_GPS_SIGNATURE_INVALID));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.LOCK_BY_APP, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_FORCE_DISABLE));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.START_FLY_HEIGHT_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ALTITUDE_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.ESC_VERSION_NOT_MATCH, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ESC_VERSION_NOT_MATCH));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.IMU_ORI_NOT_MATCH, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_GYRO_DATA_NOT_MATCH));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.STOP_BY_APP, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_APP_NOT_ALLOW));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.COMPASS_IMU_ORI_NOT_MATCH, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_COMPASS_INSTALLATION_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.ESC_ECHOING, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ESC_SCREAMING));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.ESC_OVER_HEAT, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ESC_OVER_HEAT));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BATTERY_INSTALL_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_NOT_IN_POSITION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BE_IMPACT, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_IMPACT_DETECTED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.MODE_FAILUER, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_MODEL_FAILUER));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.CRASH, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_CRAFT_HAS_EXCEPTION_LATELY));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.LOW_VERSION_OF_BATTERY, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_VERSION_TOO_LOW));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.VOLTAGE_OF_BATTERY_IS_TOO_HIGH, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_DROP_OUT_VOLTAGE_TOO_BIG));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.COOLING_FAN_EXCEPTION, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_FANS_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.EAGEL_TEMPERATURE_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_EAGEL_TEMPERATURE_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.LOST_GPS_IN_POR_A_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_LOST_GPS_IN_P_OR_A_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.RC_THROTTLE_IS_NOT_IN_MIDDLE, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_STICK_NOT_IN_MIDDLE_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.FLIGHT_RESTRICTION_STRATEGY_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_RESTRICTION_STRATEGY_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BATTERY_ICO_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_ICO_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BAT_HW_VERSION_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_VERSION_NOT_MATCH));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.M600_BAT_AUTH_ERR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_AUTHEN_EXCEPTION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.ESC_SHORT_CUT_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_ESC_SHORT_CUT_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.POWER_SYSTEM_HARDWARE_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_POWER_SYSTEM_HARDWARE_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.GIMBAL_IS_CALIBRATING, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_GIMBAL_CALIBRATING));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.BATTERY_IN_LOADER, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_IN_LOADER));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.FLIGHT_BSP_ERROR, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_FLIGHT_BSP_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.CompassError, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_COMPASS_CONNECT_ERROR));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.IMUNeedCalibration, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_IMU_NEED_ADVANCED_CALIBRATION));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.UnActive, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_DRONE_NOT_ACTIVATED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.SystemUpgrade, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_DRONE_UPGRADING));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.INVALID_VERSION, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_FIRMWARE_VERSION_MISMATCHED));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.HEIGHT_CONTROL_ANOMALY, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_HEIGHT_CONTROL_ANOMALY));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.DARK_NEED_GPS, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_DARK_NEED_GPS));
                sTakeoffFailCauseCodeMap.put(DataOsdGetPushCommon.MotorStartFailedCause.REMOTE_USB_CONNECTED, Integer.valueOf((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_HARDWARE_LOCK_MOTOR));
            }
        }
    }

    public static HashMap<DataOsdGetPushCommon.MotorStartFailedCause, Integer> getTakeoffFailCauseCodeMap() {
        if (sTakeoffFailCauseCodeMap == null) {
            initCodeMap();
        }
        return sTakeoffFailCauseCodeMap;
    }
}
