package dji.sdksharedlib.keycatalog;

import dji.common.airlink.LteSignalStatus;
import dji.common.camera.SettingsDefinitions;
import dji.common.flightcontroller.BatteryThresholdBehavior;
import dji.common.flightcontroller.CompassCalibrationState;
import dji.common.flightcontroller.CompassState;
import dji.common.flightcontroller.ConnectionFailSafeBehavior;
import dji.common.flightcontroller.ControlGimbalBehavior;
import dji.common.flightcontroller.ControlMode;
import dji.common.flightcontroller.DJIMultiLEDControlMode;
import dji.common.flightcontroller.FlightAction;
import dji.common.flightcontroller.FlightMode;
import dji.common.flightcontroller.FlightOrientationMode;
import dji.common.flightcontroller.FlightWindWarning;
import dji.common.flightcontroller.GPSSignalLevel;
import dji.common.flightcontroller.GoHomeAssessment;
import dji.common.flightcontroller.GoHomeExecutionState;
import dji.common.flightcontroller.HomePointState;
import dji.common.flightcontroller.IOStateOnBoard;
import dji.common.flightcontroller.LEDsSettings;
import dji.common.flightcontroller.LandingGearMode;
import dji.common.flightcontroller.LandingGearState;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.MotorStartFailedState;
import dji.common.flightcontroller.NavigationSystemError;
import dji.common.flightcontroller.OSDKEnabledState;
import dji.common.flightcontroller.PowerStateOnBoard;
import dji.common.flightcontroller.RedundancySensorUsedState;
import dji.common.flightcontroller.RemoteControllerFlightMode;
import dji.common.flightcontroller.UrgentStopMotorMode;
import dji.common.flightcontroller.adsb.AirSenseAirplaneState;
import dji.common.flightcontroller.adsb.AirSenseAvoidanceAction;
import dji.common.flightcontroller.adsb.AirSenseWarningLevel;
import dji.common.flightcontroller.imu.CalibrationState;
import dji.common.flightcontroller.imu.IMUState;
import dji.common.flightcontroller.imu.SensorState;
import dji.common.flightcontroller.simulator.InitializationData;
import dji.common.flightcontroller.simulator.SimulatorState;
import dji.common.flightcontroller.simulator.SimulatorWindData;
import dji.common.flightcontroller.virtualstick.FlightControlData;
import dji.common.flightcontroller.virtualstick.FlightCoordinateSystem;
import dji.common.flightcontroller.virtualstick.RollPitchControlMode;
import dji.common.flightcontroller.virtualstick.VerticalControlMode;
import dji.common.flightcontroller.virtualstick.YawControlMode;
import dji.common.mission.waypoint.WaypointMissionInterruption;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.DJIParamMinMaxCapability;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.aeroscope.AeroScopeClientSwitch;
import dji.internal.logics.SalesStrategicLogic;
import dji.midware.data.model.P3.DataFlycGetPushDeformStatus;
import dji.midware.data.model.P3.DataFlycGetPushForbidStatus;
import dji.midware.data.model.P3.DataFlycGetPushGoHomeCountDown;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.DJIWM100FlightControllerAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA2Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerA3WithLb2Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire1Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM100Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerM200Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPM420Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom3Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM230Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM245Abstraction;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class FlightControllerKeys extends DJISDKCacheKeys {
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ACCESS_LOCKER_SUPPORTED = "AccessLockerSupported";
    @Key(accessType = 1, type = Long.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ACTIVATION_TIME = "ActivationTime";
    @Key(accessType = 2, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ADVANCED_FLIGHT_MODE_ENABLED = "AdvancedFlightModeEnabled";
    @Key(accessType = 3, type = AeroScopeClientSwitch.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AERO_SCOPE_CLIENT_SWITCH = "AeroScopeClientSwitch";
    @Key(accessType = 4, type = Integer.class)
    public static final String AIRCRAFT_GO_HOME_COUNT_DOWN = "AircraftGoHomeCountDown";
    @Key(accessType = 4, type = LocationCoordinate3D.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AIRCRAFT_LOCATION = "AircraftLocation";
    @Key(accessType = 4, type = Double.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String AIRCRAFT_LOCATION_LATITUDE = "AircraftLocationLatitude";
    @Key(accessType = 4, type = Double.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String AIRCRAFT_LOCATION_LONGITUDE = "AircraftLocationLongitude";
    @Key(accessType = 3, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String AIRCRAFT_NAME = "AircraftName";
    @Key(accessType = 4, type = Boolean.class)
    public static final String AIRCRAFT_SHOULD_GO_HOME = "AircraftShouldGoHome";
    @Key(accessType = 4, type = AirSenseAirplaneState[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AIR_SENSE_AIRPLANE_STATES = "AirSenseAirplaneStates";
    @Key(accessType = 4, includedAbstractions = {FlightControllerWM245Abstraction.class}, type = AirSenseAvoidanceAction.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AIR_SENSE_AVOIDANCE_ACTION = "AirSenseAvoidanceAction";
    @Key(accessType = 4, type = Boolean[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AIR_SENSE_SYSTEM_CONNECTED = "AirSenseSystemConnected";
    @Key(accessType = 4, type = AirSenseWarningLevel.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AIR_SENSE_SYSTEM_WARNING_LEVEL = "AirSenseSystemWarningLevel";
    @Key(accessType = 4, type = Float.class)
    public static final String ALTITUDE = "Altitude";
    @Key(accessType = 4, type = Boolean.class)
    public static final String ARE_MOTOR_ON = "AreMotorsOn";
    @Key(accessType = 4, type = Double.class)
    public static final String ATTITUDE_PITCH = "AttitudePitch";
    @Key(accessType = 4, type = Double.class)
    public static final String ATTITUDE_ROLL = "AttitudeRoll";
    @Key(accessType = 4, type = Double.class)
    public static final String ATTITUDE_YAW = "AttitudeYaw";
    @InternalKey
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String AUTO_LANDING_GEAR = "AutoLandingGear";
    @InternalKey
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String AUTO_LANDING_GEAR_GROUND_NOTIFY = "AutoLandingGearGroundNotify";
    @Key(accessType = 4, type = Integer.class)
    public static final String BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME = "BatteryPercentageNeededToGoHome";
    @Key(accessType = 4, type = BatteryThresholdBehavior.class)
    public static final String BATTERY_THRESHOLD_BEHAVIOR = "RemainingBattery";
    @Key(accessType = 3, type = Object[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String BINDING_STATE = "BindingState";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CANCEL_GO_HOME = "CancelGoHome";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CANCEL_LANDING = "CancelAutoLanding";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CANCEL_TAKE_OFF = "CancelTakeOff";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CINEMATIC_BRAKE_SENSITIVITY = "CinematicBrakeSensitivity";
    @Key(accessType = 1, type = DJIParamMinMaxCapability.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CINEMATIC_BRAKE_SENSITIVITY_RANGE = "CinematicBrakeSensitivityRange";
    @Key(accessType = 6, excludedAbstractions = {FlightControllerPhantom4PRTKAbstraction.class}, includedAbstractions = {FlightControllerFoldingDroneAbstraction.class, FlightControllerPhantom4PAbstraction.class, FlightControllerInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CINEMATIC_MODE_ENABLED = "CinematicModeEnabled";
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CINEMATIC_YAW_SPEED = "CinematicYawSpeed";
    @Key(accessType = 1, type = DJIParamMinMaxCapability.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CINEMATIC_YAW_SPEED_RANGE = "CinematicYawSpeedRange";
    @Key(accessType = 4, type = CompassCalibrationState.class)
    public static final String COMPASS_CALIBRATION_STATUS = "CompassCalibrationStatus";
    @Key(accessType = 1, protectDuration = 0, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String COMPASS_COUNT = "CompassCount";
    @Key(accessType = 4, type = Boolean.class)
    public static final String COMPASS_HAS_ERROR = "CompassHasError";
    @Key(accessType = 4, type = Float.class)
    public static final String COMPASS_HEADING = "CompassHeading";
    @Key(accessType = 4, type = Boolean.class)
    public static final String COMPASS_IS_CALIBRATING = "CompassIsCalibrating";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String COMPASS_START_CALIBRATION = "CompassStartCalibration";
    @Key(accessType = 1, type = CompassState.class)
    public static final String COMPASS_STATE = "CompassState";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String COMPASS_STOP_CALIBRATION = "CompassStopCalibration";
    public static final String COMPONENT_KEY = "FlightController";
    @InternalKey
    @Key(accessType = 2, includedAbstractions = {FlightControllerWM160Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONFIG_DARK_NEED_GPS = "ConfigDarkNeedGps";
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONFIG_RC_SCALE_IN_AVOIDANCE = "ConfigRcScaleInAvoidance";
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONFIG_RC_SCALE_IN_NORMAL = "ConfigRcScaleInNormal";
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONFIG_RC_SCALE_IN_SPORT = "ConfigRcScaleInSport";
    @Key(accessType = 3, excludedAbstractions = {FlightControllerWM160Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONFIG_RTH_IN_CURRENT_ALTITUDE = "ConfigRTHInCurrentAltitude";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONFIG_YAW_RATE_IN_AVOIDANCE = "ConfigYawRateInAvoidance";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONFIG_YAW_RATE_IN_NORMAL = "ConfigYawRateInNormal";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONFIG_YAW_RATE_IN_SPORT = "ConfigYawRateInSport";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONFIRM_LANDING = "confirmLanding";
    @Key(accessType = 8, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONFIRM_SMART_RETURN_TO_HOME_REQUEST = "ConfirmSmartReturnToHomeRequest";
    @Key(accessType = 3, type = ConnectionFailSafeBehavior.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONNECTION_FAIL_SAFE_BEHAVIOR = "FlightFailSafeOperation";
    @Deprecated
    @Key(accessType = 6, includedAbstractions = {FlightControllerM200Abstraction.class}, type = ControlGimbalBehavior.class)
    public static final String CONTROL_GIMBAL_BEHAVIOR = "control_gimbal_behavior";
    @Key(accessType = 3, includedAbstractions = {FlightControllerA3WithLb2Abstraction.class}, type = ControlMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONTROL_MODE = "ControlMode";
    @InternalKey
    @Key(accessType = 4, type = Short.class)
    public static final String COURSE_LOCK_ANGLE = "CourseLockAngle";
    @Key(accessType = 4, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = Integer.class)
    public static final String CURRENT_LAND_IMMEDIATELY_BATTERY = "CurrentLandImmediatelyBattery";
    @Key(accessType = 4, type = RemoteControllerFlightMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CURRENT_MODE = "CurrentMode";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {FlightControllerInspire1Abstraction.class}, type = DataFlycGetPushDeformStatus.DEFORM_MODE.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DEFORM_MODE = "DeformMode";
    @Key(accessType = 8, includedAbstractions = {FlightControllerInspire1Abstraction.class, FlightControllerInspire2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DEPLOY_LANDING_GEAR = "DeployLandingGear";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DEVICE_INSTALL_ERROR_HOVER_THRUST_LOW = "DeviceInstallErrorHoverThrustLow";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DEVICE_INSTALL_ERROR_MASS_CENTER = "DeviceInstallErrorMassCenter";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DEVICE_INSTALL_ERROR_VIBRATION = "DeviceInstallErrorVibration";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DEVICE_INSTALL_ERROR_YAW = "DeviceInstallErrorYaw";
    @InternalKey
    @Key(accessType = 4, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DRONE_PRECISE_DB_VERSION = "DronePreciseDBVersion";
    @Key(accessType = 3, includedAbstractions = {FlightControllerM200Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ENABLE_1860 = "Enable1860";
    @Key(accessType = 3, includedAbstractions = {FlightControllerPM420Abstraction.class}, type = OSDKEnabledState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ENABLE_OSDK = "EnableOsdk";
    @Key(accessType = 3, includedAbstractions = {FlightControllerPM420Abstraction.class}, type = OSDKEnabledState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ENABLE_PPS = "EnablePPS";
    @Key(accessType = 3, includedAbstractions = {FlightControllerPM420Abstraction.class}, type = OSDKEnabledState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ENABLE_VISION = "EnableVison";
    @Key(accessType = 8, includedAbstractions = {FlightControllerInspire1Abstraction.class, FlightControllerInspire2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ENTER_TRANSPORT_MODE = "EnterTransportMode";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ESC_BEEPING_ENABLED = "ESCBeepingEnabled";
    @Key(accessType = 8, includedAbstractions = {FlightControllerPM420Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String EXIT_MASS_CENTER_CALI = "exit_mass_center_cali";
    @Key(accessType = 8, includedAbstractions = {FlightControllerInspire1Abstraction.class, FlightControllerInspire2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String EXIT_TRANSPORT_MODE = "ExitTransportMode";
    @InternalKey
    public static final String FAN_ENABLED = "FanEnabled";
    @Key(accessType = 4, type = FlightAction.class)
    public static final String FLIGHT_ACTION = "FlightAction";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_ATTITUDE_RANGE = "FlightControllerConfigAttitudeRange";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_BASIC_PITCH = "FlightControllerConfigBasicPitch";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_BASIC_ROLL = "FlightControllerConfigBasicRoll";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_BASIC_TAIL = "FlightControllerConfigBasicTail";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_BASIC_YAW = "FlightControllerConfigBasicYaw";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_BRAKE_SENSITIVE = "FlightControllerConfigBrakeSensitive";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_FFG_SPEED_CTRL_PERC = "FlightControllerConfigFFGSpeedCtrlPerc";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_GENTLE_LIFT_EXP_MIDDLE_POINT = "FlightControllerConfigGentleLiftExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_GENTLE_TILT_EXP_MIDDLE_POINT = "FlightControllerConfigGentleTiltExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_GENTLE_TORSION_EXP_MIDDLE_POINT = "FlightControllerConfigGentleTorsionExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_GYRO_RANGE = "FlightControllerConfigTripodGyroRange";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_HIDE_GEAR = "FlightControllerConfigHideGear";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_IMU_TEMP_REAL_CTL_OUT_PER = "FlightControllerConfigImuTempRealCtlOutPer";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_LANDING_CHECK_SWITCH = "FlightControllerConfigLandingCheckSwitch";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_NORMAL_LIFT_EXP_MIDDLE_POINT = "FlightControllerConfigNormalLiftExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_NORMAL_TILT_EXP_MIDDLE_POINT = "FlightControllerConfigNormalTiltExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_NORMAL_TORSION_EXP_MIDDLE_POINT = "FlightControllerConfigNormalTorsionExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_ON_GROUND_HIDE_GEAR = "FlightControllerConfigOnGroundHideGear";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_RC_TILT_SENSITIVE = "FlightControllerConfigRcTiltSensitive";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_SPORT_LIFT_EXP_MIDDLE_POINT = "FlightControllerConfigSportLiftExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_SPORT_THROTTLE_EXPERIENCE_MID_POINT = "FlightControllerConfigSportThrottleExperienceMidPoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_SPORT_TILT_EXPERIENCE_MID_POINT = "FlightControllerConfigSportTiltExperienceMidPoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_SPORT_TILT_EXP_MIDDLE_POINT = "FlightControllerConfigSportTiltExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_SPORT_TORSION_EXP_MIDDLE_POINT = "FlightControllerConfigSportTorsionExpMiddlePoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_SPORT_YAW_EXPERIENCE_MID_POINT = "FlightControllerConfigSportYawExperienceMidPoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_THROTTLE_EXPERIENCE_MID_POINT = "FlightControllerConfigThrottleExperienceMidPoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_TILT_EXPERIENCE_MID_POINT = "FlightControllerConfigTiltExperienceMidPoint";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_TILT_SENSITIVE = "FlightControllerConfigTiltSensitive";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_TORSION_GYRO_RANGE = "FlightControllerConfigTorsionGyroRange";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_TORSION_RATE = "FlightControllerConfigTorsionRate";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_TRIPOD_RCSCALE = "FlightControllerConfigTripodRcScale";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_VERTICAL_ATTI = "FlightControllerConfigVerticalAtti";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_VERT_DOWN = "FlightControllerConfigTripodVertDown";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_VERT_UP = "FlightControllerConfigTripodVertUp";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_YAW_AT_SPORT = "FlightControllerConfigYawAtSport";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_CONFIG_YAW_EXPERIENCE_MID_POINT = "FlightControllerConfigYawExperienceMidPoint";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FLIGHT_CONTROLLER_MAX_DIVE_SPEED = "MaxDiveSpeed";
    @Key(accessType = 4, type = Integer.class)
    public static final String FLIGHT_LIMIT_SPACE_NUM = "FlightLimitSpaceNum";
    @Key(accessType = 4, type = DataFlycGetPushForbidStatus.DJIFlightLimitAreaState.class)
    public static final String FLIGHT_LIMIT_STATE = "FlightLimitState";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_LOG_INDEX = "FlycLogIndex";
    @Key(accessType = 4, type = FlightMode.class)
    public static final String FLIGHT_MODE = "FlightMode";
    @Key(accessType = 4, type = String.class)
    public static final String FLIGHT_MODE_STRING = "FlightModeString";
    @Key(accessType = 4, type = FlightWindWarning.class)
    public static final String FLIGHT_WIND_WARNING = "FlightWindWarning";
    @Key(accessType = 4, type = Integer.class)
    public static final String FLY_TIME_IN_SECONDS = "FlyTime";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FLY_ZONE_LIMITATION_ENABLED = "GeoFeatureInSimulatorEnabled";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FORCE_LANDING = "ForceLanding";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FORCE_LANDING_AFTER_FLAT_MODE = "forceLandingAfterFlatMode";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FORCE_LANDING_HEIGHT = "ForceLandingHeight";
    @InternalKey
    @Key(accessType = 1, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FULL_SERIAL_NUMBER_HASH = "FullSerialNumberHash";
    @Key(accessType = 8, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String GO_HOME_ACTION_COUNT_DOWN_CONFIRM = "GoHomeActionCountDownConfirm";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String GO_HOME_ACTION_COUNT_DOWN_TIME = "GoHomeActionCountDownTime";
    @Key(accessType = 4, type = DataFlycGetPushGoHomeCountDown.GoHomePushActionType.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String GO_HOME_ACTION_TYPE = "GoHomeActionType";
    @Key(accessType = 4, type = GoHomeAssessment.class)
    public static final String GO_HOME_ASSESSMENT = "GoHomeAssessment";
    @Key(accessType = 3, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String GO_HOME_HEIGHT_IN_METERS = "GoHomeAltitude";
    @Key(accessType = 1, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = DJIParamMinMaxCapability.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String GO_HOME_HEIGHT_RANGE = "GoHomeAltitudeRange";
    @Key(accessType = 4, type = GoHomeExecutionState.class)
    public static final String GO_HOME_STATUS = "GoHomeStatus";
    @Key(accessType = 4, type = GPSSignalLevel.class)
    public static final String GPS_SIGNAL_LEVEL = "GPSSignalLevel";
    @Key(accessType = 4, type = Boolean.class)
    public static final String HAS_REACHED_MAX_FLIGHT_HEIGHT = "HasReachedMaxFlightHeight";
    @Key(accessType = 4, type = Boolean.class)
    public static final String HAS_REACHED_MAX_FLIGHT_RADIUS = "HasReachedMaxFlightRadius";
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HAS_WRITTEN_UID = "RealNameSystemHasWrittenUID";
    @Key(accessType = 6, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = LocationCoordinate2D.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HOME_LOCATION = "HomeLocation";
    @Key(accessType = 4, type = Double.class)
    public static final String HOME_LOCATION_LATITUDE = "HomeLocationLatitude";
    @Key(accessType = 4, type = Double.class)
    public static final String HOME_LOCATION_LONGITUDE = "HomeLocationLongitude";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HOME_LOCATION_USING_CURRENT_AIRCRAFT_LOCATION = "HomeLocationUsingCurrentAircraftLocation";
    @Key(accessType = 4, type = Float.class)
    public static final String HOME_POINT_ALTITUDE = "HomePointAltitude";
    @Key(accessType = 4, type = HomePointState.class)
    public static final String HOME_POINT_STATE = "HomePointState";
    @Key(accessType = 1, excludedAbstractions = {FlightControllerPhantom3Abstraction.class, FlightControllerInspire1Abstraction.class, FlightControllerWM160Abstraction.class}, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HOTPOINT_MAX_ACCELERATION = "FlightControllerConfigHotpointMaxAcceleration";
    @Key(accessType = 1, excludedAbstractions = {FlightControllerPhantom3Abstraction.class, FlightControllerInspire1Abstraction.class, FlightControllerWM160Abstraction.class}, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HOTPOINT_MIN_RADIUS = "FlightControllerConfigHotpointMinRadius";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.EVENT)
    public static final String IMPACT_IN_AIR_DETECTED = "impactInAirDetected";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String IMU_ALLOW_INIT_FAIL_REASON = "AllowIMUInitFailReason";
    @Key(accessType = 1, protectDuration = 0, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IMU_COUNT = "ImuCount";
    @InternalKey
    @Key(accessType = 4, type = DataOsdGetPushCommon.IMU_INITFAIL_REASON.class)
    public static final String IMU_INIT_FAIL_REASON = "IMUInitFailReason";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String IMU_IS_INIT_ERROR = "IMUIsInitError";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {FlightControllerWM160Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IMU_NEED_CALIBRATION_TYPE = "IMUNeedCalibrationType";
    @Key(accessType = 1, type = IMUState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IMU_STATE = "IMUState";
    @Key(accessType = 4, type = SensorState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IMU_STATE_ACCELEROMETER_STATE = "IMUStateAcceleratorState";
    @Key(accessType = 4, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IMU_STATE_ACCELEROMETER_VALUE = "IMUStateAcceleratorValue";
    @Key(accessType = 4, type = Integer.class)
    public static final String IMU_STATE_CALIBRATION_PROGRESS = "IMUStateCalibrationProgress";
    @Key(accessType = 4, type = CalibrationState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IMU_STATE_CALIBRATION_STATE = "IMUStateCalibrationState";
    @Key(accessType = 4, type = SensorState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IMU_STATE_GYROSCOPE_STATE = "IMUStateGyroscopeState";
    @Key(accessType = 4, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IMU_STATE_GYROSCOPE_VALUE = "IMUStateGyroscopeValue";
    @Key(accessType = 8, includedAbstractions = {FlightControllerM200Abstraction.class, FlightControllerA3Abstraction.class}, types = {Integer.class, IOStateOnBoard.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String INIT_IO = "InitIo";
    @Key(accessType = 3, type = Boolean.class)
    public static final String INTELLIGENT_FLIGHT_ASSISTANT_HAND_GESTURE_ENABLED = "HandGestureEnabled";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INTERNAL_FLIGHT_CONTROLLER_VERSION = "InternalFlightControllerVersion";
    @InternalKey
    @Key(accessType = 1, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String INTERNAL_SERIAL_NUMBER = "InternalSerialNumber";
    @Key(accessType = 3, includedAbstractions = {FlightControllerM200Abstraction.class, FlightControllerA3Abstraction.class}, type = IOStateOnBoard.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IOSTATE_0 = "IOState_0";
    @Key(accessType = 3, includedAbstractions = {FlightControllerM200Abstraction.class, FlightControllerA3Abstraction.class}, type = IOStateOnBoard.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IOSTATE_1 = "IOState_1";
    @Key(accessType = 3, includedAbstractions = {FlightControllerM200Abstraction.class, FlightControllerA3Abstraction.class}, type = IOStateOnBoard.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IOSTATE_2 = "IOState_2";
    @Key(accessType = 3, includedAbstractions = {FlightControllerM200Abstraction.class, FlightControllerA3Abstraction.class}, type = IOStateOnBoard.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IOSTATE_3 = "IOState_3";
    @Key(accessType = 3, includedAbstractions = {FlightControllerM200Abstraction.class, FlightControllerA3Abstraction.class}, type = IOStateOnBoard.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IOSTATE_4 = "IOState_4";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_ABOVE_WATER = "IsAboveWater";
    @Key(accessType = 4, includedAbstractions = {FlightControllerInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_ASCENT_LIMITED_BY_OBSTACLE = "IsAscentLimitedByObstacle";
    @Key(accessType = 4, includedAbstractions = {FlightControllerFoldingDroneAbstraction.class, FlightControllerPhantom4PAbstraction.class, FlightControllerInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_AVOIDING_ACTIVE_OBSTACLE_COLLISION = "IsAvoidingActiveObstacleCollision";
    @InternalKey
    @Key(accessType = 4, type = boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_EXTRA_LOAD_DETECTED = "isExtraLoadDetected";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_FAIL_SAFE = "IsFailSafe";
    @Key(accessType = 1, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_FLIGHT_ASSISTANT_SUPPORTED = "IntelligentFlightAssistantSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_FLYING = "IsFlying";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_GOING_HOME = "IsGoingHome";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_GPS_BEING_USED = "IsGpsBeingUsed";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_GPS_VALID = "IsGpsValid";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_HOME_LOCATION_SET = "IsHomeLocationSet";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_IMU_PREHEATING = "IsIMUPreheating";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_IN_LANDING_MODE = "isInLandingMode";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_LANDING = "IsAutoLanding";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_LANDING_CONFIRMATION_NEEDED = "isLandingConfirmationNeeded";
    @Key(accessType = 4, protectDuration = 0, type = Boolean.class)
    public static final String IS_LANDING_GEAR_MOVABLE = "IsLandingGearMovable";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_LOWER_THAN_BATTERY_WARNING_THRESHOLD = "IsLowerThanBatteryWarningThreshold";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_LOWER_THAN_SERIOUS_BATTERY_WARNING_THRESHOLD = "IsLowerThanSeriousBatteryWarningThreshold";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MOTORS_LOCKED_BY_APP = "IsMotorsLockedByApp";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_NEAR_DISTANCE_LIMIT = "IsNearDistanceLimit";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_NEAR_HEIGHT_LIMIT = "IsNearHeightLimit";
    @Key(accessType = 4, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_ONBOARD_FCHANNEL_AVAILABLE = "isOnboardFChannelAvailable";
    @Key(accessType = 4, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_ON_BOARD_SDK_AVAILABLE = "IsOnBoardSDKAvailable";
    @Key(accessType = 8, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_PROPELLER_CALIBRATION_SUPPORTED = "IsPropellerCalibrationSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_REBOOT_IN_S_MODE = "isRebootInSMode";
    @InternalKey
    @Key(accessType = 4, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_RTK_SUPPORTED = "RtkSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SIMULATOR_ACTIVE = "IsSimulatorStarted";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_SUPPORT_AERO_SCOPE = "IsSupportAeroScope";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SYSTEM_UPGRADE_ABNORMAL = "IsSystemUpgradeAbnormal";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_ULTRASONIC_BEING_USED = "IsUltrasonicBeingUsed";
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_VIRTUAL_FENCE_SUPPORTED = "IsVirtualFenceSupported";
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_VIRTUAL_STICK_CONTROL_MODE_AVAILABLE = "isVirtualStickControlModeAvailable";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_VISION_POSITIONING_SENSOR_BEING_USED = "IsVisionPositioningSensorBeingUsed";
    @InternalKey
    public static final String IS_VISION_SENSOR_ENABLE = "IsVisionSensorEnable";
    @InternalKey
    public static final String IS_VISION_SENSOR_WORK = "IswaypointProtocol";
    @Key(accessType = 3, includedAbstractions = {FlightControllerInspire1Abstraction.class, FlightControllerInspire2Abstraction.class, FlightControllerA3Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LANDING_GEAR_AUTOMATIC_MOVEMENT_ENABLED = "LandingGearAutomaticMovementEnabled";
    @Key(accessType = 3, includedAbstractions = {FlightControllerA3Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LANDING_GEAR_HIDE_ENABLED = "LandingGearHideEnabled";
    @Key(accessType = 4, includedAbstractions = {FlightControllerInspire1Abstraction.class, FlightControllerInspire2Abstraction.class}, type = LandingGearMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LANDING_GEAR_MODE = "LandingGearMode";
    @Key(accessType = 4, includedAbstractions = {FlightControllerInspire1Abstraction.class, FlightControllerInspire2Abstraction.class}, type = LandingGearState.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LANDING_GEAR_STATUS = "LandingGearStatus";
    @Key(accessType = 3, excludedAbstractions = {FlightControllerWM160Abstraction.class}, type = LEDsSettings.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LEDS_ENABLED_SETTINGS = "LEDsEnabledSettings";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class, FlightControllerPhantom4PRTKAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LOCK_COURSE_USING_CURRENT_HEADING = "LockCourseUsingCurrentDirection";
    @Key(accessType = 2, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LOCK_MOTORS = "LockMotors";
    @Key(accessType = 6, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = Integer.class)
    public static final String LOW_BATTERY_WARNING_THRESHOLD = "LowBatteryWarningThreshold";
    @Key(accessType = 4, includedAbstractions = {FlightControllerPM420Abstraction.class}, type = LteSignalStatus.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LTE_STATUS = "LteStatus";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {FlightControllerM200Abstraction.class}, type = boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MAIN_GPS_SIGNAL_SHELTERED = "MainGPSSignalSheltered";
    @Key(accessType = 4, includedAbstractions = {FlightControllerPM420Abstraction.class}, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MASS_CENTER_STATE = "mass_center_state";
    @Key(accessType = 2, includedAbstractions = {FlightControllerPM420Abstraction.class}, type = SettingsDefinitions.CameraType.class)
    public static final String MASTER_LIVE_VIDEO_CAMERA = "pm420_master_live_video_camera";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MAX_FLIGHT_HEIGHT = "MaxFlightHeight";
    @Key(accessType = 1, type = DJIParamMinMaxCapability.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MAX_FLIGHT_HEIGHT_RANGE = "MaxFlightHeightRange";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MAX_FLIGHT_RADIUS = "MaxFlightRadius";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MAX_FLIGHT_RADIUS_LIMITATION_ENABLED = "MaxFlightRadiusEnabled";
    @Key(accessType = 4, type = DJIParamMinMaxCapability.class)
    public static final String MAX_FLIGHT_RADIUS_RANGE = "MaxFlightRadiusRange";
    @Key(accessType = 4, type = Float.class)
    public static final String MAX_RADIUS_AIRCRAFT_CAN_FLY_AND_GO_HOME = "MaxRadiusAircraftCanFlyAndGoHome";
    @Key(accessType = 1, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MAX_WAYPOINT_NUM = "MaxWaypointNum";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MISSION_TRIPOD_VELOCITY_CTRL = "MissionTripodVelocityCtrl";
    @Key(accessType = 1, type = RemoteControllerFlightMode[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MODE = "Mode";
    @InternalKey
    @Key(accessType = 4, type = DataOsdGetPushHome.MotorEscmState[].class)
    public static final String MOTOR_ESCM_STATE = "MotorEscmState";
    @InternalKey
    @Key(accessType = 4, type = MotorStartFailedState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MOTOR_START_FAILED_STATE = "MotorStartFailedState";
    @InternalKey
    @Key(accessType = 3, excludedAbstractions = {FlightControllerWM160Abstraction.class}, type = DJIMultiLEDControlMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MULTI_LEDS_ENABLED = "MultiLEDsEnabled";
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MULTI_MODE_OPEN = "MultiModeOpen";
    @Key(accessType = 1, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MULTI_ROTOR_TYPE = "multi_rotor_type";
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NAVIGATION_MODE_ENABLED = "NavigationModeEnabled";
    @Key(accessType = 4, type = NavigationSystemError.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NAVIGATION_SYSTEM_ERROR = "NavigationSystemError";
    @Key(accessType = 5, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NEED_LIMIT_FLIGHT_HEIGHT = "NeedLimitFlightHeight";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NFZ_MAX_HEIGHT = "NFZMaxHeight";
    @Key(accessType = 6, type = Boolean.class)
    public static final String NOVICE_MODE_ENABLED = "NoviceModeEnabled";
    @Key(accessType = 6, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = FlightOrientationMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ORIENTATION_MODE = "IocMode";
    @Key(accessType = 8, includedAbstractions = {FlightControllerFoldingDroneAbstraction.class, FlightControllerPhantom4PAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PAUSE_TERRAIN_FOLLOW_MODE = "PauseTerrainFollowMode";
    @Key(accessType = 3, includedAbstractions = {FlightControllerM200Abstraction.class}, type = PowerStateOnBoard.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POWER_ON_BOARD = "PowerOnBoard";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class, FlightControllerPM420Abstraction.class, FlightControllerM100Abstraction.class, FlightControllerWM240Abstraction.class, FlightControllerWM245Abstraction.class}, includedAbstractions = {FlightControllerPhantom4Abstraction.class, FlightControllerM200Abstraction.class, FlightControllerWM230Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PRECISION_TAKE_OFF = "PrecisionTakeOff";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {FlightControllerWM160Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PROP_COVER_LIMIT_ENABLED = "PropCoverLimitEnabled";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {FlightControllerWM160Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PROP_COVER_LIMIT_HEIGHT = "PropCoverLimitHeight";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {FlightControllerWM160Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PROP_COVER_LIMIT_RADIUS = "PropCoverLimitRadius";
    @Key(accessType = 3, includedAbstractions = {FlightControllerInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String QUICK_SPIN_ENABLED = "QuickSpin";
    @InternalKey
    @Key(accessType = 4, type = DataOsdGetPushCommon.RcModeChannel.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RC_MODE_CHANNEL = "RcModeChannel";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REAL_NAME_SUPPORTED = "RealNameSupported";
    @Key(accessType = 9, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REAL_NAME_SYSTEM_ENABLED = "RealNameSystemEnabled";
    @Key(accessType = 2, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String REAL_NAME_SYSTEM_UID = "RealNameSystemUID";
    @Key(accessType = 1, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REDUNDANCY_COMPASS_STATUS = "redundnacyCompassStatus";
    @Key(accessType = 1, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REDUNDANCY_IMU_STATUS = "redundancyImuStatus";
    @Key(accessType = 4, type = RedundancySensorUsedState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REDUNDANCY_SENSOR_USED_STATE = "RedundancySensorUsedState";
    @Key(accessType = 4, type = Integer.class)
    public static final String REMAINING_FLIGHT_TIME = "RemainingFlightTime";
    @Key(accessType = 8, includedAbstractions = {FlightControllerPM420Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESET_MASS_CENTER_CALI = "reset_mass_center_cali";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESET_MOTOR = "ResetMotor";
    @Key(accessType = 8, includedAbstractions = {FlightControllerFoldingDroneAbstraction.class, FlightControllerPhantom4PAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESUME_TERRAIN_FOLLOW_MODE = "ResumeTerrainFollowMode";
    @Key(accessType = 8, includedAbstractions = {FlightControllerInspire1Abstraction.class, FlightControllerInspire2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RETRACT_LANDING_GEAR = "RetractLandingGear";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {FlightControllerPhantom4PRTKAbstraction.class}, type = SalesStrategicLogic.SalesStrategy.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SALES_STRATEGY = "SalesStrategy";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SATELLITE_COUNT = "SatelliteCount";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = byte[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SEND_DATA_TO_ON_BOARD_SDK_DEVICE = "SendDataToOnboardSDKDevice";
    @Key(accessType = 8, types = {FlightControlData.class, VerticalControlMode.class, RollPitchControlMode.class, YawControlMode.class, FlightCoordinateSystem.class, Boolean.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SEND_VIRTUAL_STICK_FLIGHT_CONTROL_DATA = "SendVirtualStickFlightControlData";
    @Key(accessType = 6, excludedAbstractions = {FlightControllerA2Abstraction.class}, type = Integer.class)
    public static final String SERIOUS_LOW_BATTERY_WARNING_THRESHOLD = "SeriousLowBatteryWarningThreshold";
    @Key(accessType = 8, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SET_DATA_UPGRADE_SELF = "SetUpgradeSelf";
    @Key(accessType = 4, type = SimulatorState.class)
    public static final String SIMULATOR_STATE = "SimulatorState";
    @Key(accessType = 6, type = SimulatorWindData.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SIMULATOR_WIND_DATA = "SimulatorWindData";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SMART_RETURN_TO_HOME_ENABLED = "SmartReturnToHomeEnabled";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_GO_HOME = "GoHome";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_IMU_CALIBRATION = "StartIMUCalibration";
    @Key(accessType = 8, includedAbstractions = {FlightControllerA3Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_IMU_CALIBRATION_WITH_ID = "StartIMUCalibrationWithID";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_LANDING = "AutoLanding";
    @Key(accessType = 8, includedAbstractions = {FlightControllerPM420Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_MASS_CENTER_CALI = "start_mass_center_cali";
    @Key(accessType = 8, includedAbstractions = {FlightControllerM200Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_PROPELLER_CALIBRATION = "StartPropellerCalibration";
    @Key(accessType = 8, type = InitializationData.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_SIMULATOR = "StartSimulator";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP_IOC_MODE = "StopIOCMode";
    @Key(accessType = 8, includedAbstractions = {FlightControllerM200Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP_PROPELLER_CALIBRATION = "StopPropellerCalibration";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP_SIMULATOR = "StopSimulator";
    @Key(accessType = 1, type = Float[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String STRUCT_GAP_TIME = "structGapTime";
    public static final String SUBCOMPONENT_IMU_KEY = "Imu";
    @Key(accessType = 4, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TAKEOFF_LOCATION_ALTITUDE = "TakeoffLocationAltitude";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TAKE_OFF = "TakeOff";
    @Key(accessType = 3, excludedAbstractions = {FlightControllerPhantom4PRTKAbstraction.class}, includedAbstractions = {FlightControllerFoldingDroneAbstraction.class, FlightControllerPhantom4Abstraction.class, FlightControllerInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TERRAIN_FOLLOW_MODE_ENABLED = "TerrainFollowModeEnabled";
    @Key(accessType = 4, type = Integer.class)
    public static final String TIME_NEEDED_TO_GO_HOME = "TimeNeededToGoHome";
    @Key(accessType = 4, type = Integer.class)
    public static final String TIME_NEEDED_TO_LAND_FROM_CURRENT_HEIGHT = "TimeNeededToLandFromCurrentHeight";
    @InternalKey
    @Key(accessType = 6, includedAbstractions = {FlightControllerFoldingDroneAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TRACKING_MAXIMUM_SPEED = "TrackingMaximumSpeed";
    @Key(accessType = 6, excludedAbstractions = {FlightControllerPhantom4PRTKAbstraction.class}, includedAbstractions = {FlightControllerPhantom4Abstraction.class, FlightControllerInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TRIPOD_MODE_ENABLED = "TripodModeEnabled";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TURN_OFF_MOTORS = "TurnOffMotors";
    @Key(accessType = 8, excludedAbstractions = {FlightControllerA2Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TURN_ON_MOTORS = "TurnOnMotors";
    @Key(accessType = 4, type = Boolean.class)
    public static final String ULTRASONIC_ERROR = "UltrasonicError";
    @Key(accessType = 4, type = Float.class)
    public static final String ULTRASONIC_HEIGHT_IN_METERS = "UltrasonicHeightInMeters";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String UPGRADE_VOICE_OPEN = "upgrade_voice_open";
    @Key(accessType = 3, excludedAbstractions = {DJIWM100FlightControllerAbstraction.class}, includedAbstractions = {FlightControllerInspire2Abstraction.class, FlightControllerPhantom4Abstraction.class}, type = UrgentStopMotorMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String URGENT_STOP_MOTOR_MODE = "UrgentStopMotorMode";
    @Key(accessType = 4, type = Float.class)
    public static final String VELOCITY_X = "VelocityX";
    @Key(accessType = 4, type = Float.class)
    public static final String VELOCITY_Y = "VelocityY";
    @Key(accessType = 4, type = Float.class)
    public static final String VELOCITY_Z = "VelocityZ";
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String VIRTUAL_STICK_CONTROL_MODE_ENABLED = "VirtualStickControlModeEnabled";
    @Key(accessType = 1, includedAbstractions = {FlightControllerPhantom4PRTKAbstraction.class}, type = WaypointMissionInterruption.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String WAYPOINT_MISSION_INTERRUPTION = "WaypointMissionInterruption";
    @Key(accessType = 1, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String WAYPOINT_MISSION_SPEED = "WaypointMissionSpeed";
    @InternalKey
    @Key(accessType = 1, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String WAYPOINT_PROTOCOL_VERSION = "WaypointProtocolVersion";

    public FlightControllerKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
