package dji.sdksharedlib.keycatalog;

import com.dji.cmd.v1.protocol.TimeLapseWaypointInfo;
import com.dji.mapkit.core.models.DJILatLng;
import dji.common.camera.SettingsDefinitions;
import dji.common.flightcontroller.DJIVisionTrackHeadingMode;
import dji.common.flightcontroller.DJIVisionTrackMode;
import dji.common.flightcontroller.FixedWingControl;
import dji.common.flightcontroller.ObstacleDetectionSector;
import dji.common.flightcontroller.VisionDetectionState;
import dji.common.flightcontroller.VisionDrawHeadingMode;
import dji.common.flightcontroller.VisionDrawStatus;
import dji.common.flightcontroller.VisionLandingProtectionState;
import dji.common.flightcontroller.VisionSystemWarning;
import dji.common.flightcontroller.flightassistant.AdvancedPilotAssistantSystemState;
import dji.common.flightcontroller.flightassistant.BottomAuxiliaryLightMode;
import dji.common.flightcontroller.flightassistant.FaceAwareState;
import dji.common.flightcontroller.flightassistant.IntelligentHotpointMissionMode;
import dji.common.flightcontroller.flightassistant.PalmControlState;
import dji.common.flightcontroller.flightassistant.PalmDetectionState;
import dji.common.flightcontroller.flightassistant.PoiException;
import dji.common.flightcontroller.flightassistant.PoiTargetInformation;
import dji.common.flightcontroller.flightassistant.PointOfInterestExecutingState;
import dji.common.flightcontroller.flightassistant.QuickShotException;
import dji.common.flightcontroller.flightassistant.SmartCaptureCameraAction;
import dji.common.flightcontroller.flightassistant.SmartCaptureControlMode;
import dji.common.flightcontroller.flightassistant.SmartCaptureFanProtectionState;
import dji.common.flightcontroller.flightassistant.SmartCaptureFollowingMode;
import dji.common.flightcontroller.flightassistant.SmartCaptureSystemStatus;
import dji.common.flightcontroller.flightassistant.SmartCaptureTargetType;
import dji.common.flightcontroller.flightassistant.TimeLapseException;
import dji.common.flightcontroller.flightassistant.TimeLapseFramesOption;
import dji.common.flightcontroller.flightassistant.TimeLapseState;
import dji.common.flightcontroller.flightassistant.TimeLapseSubMode;
import dji.common.mission.activetrack.ActiveTrackMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mock.abstractions.MockIntelligentFlightAssistantAbstraction;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushFixedWingState;
import dji.midware.data.model.P3.DataEyeGetPushFlatCheck;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.P3.DataEyeGetSmartCaptureStatisticsData;
import dji.midware.data.model.P3.DataEyePushVisionTip;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantMavicProAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantPhantom4ProAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM230Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM245Abstraction;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class IntelligentFlightAssistantKeys extends DJISDKCacheKeys {
    @Key(accessType = 3, type = Boolean.class)
    public static final String ACTIVE_BACKWARD_FLYING_ENABLED = "ActiveBackwardFlyingEnabled";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ACTIVE_OBSTACLE_AVOIDANCE_ENABLED = "ActiveAvoidanceEnabled";
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ACTIVE_TRACK_CIRCULAR_SPEED = "ActiveTrackCircularSpeed";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ACTIVE_TRACK_GPS_ASSISTANT_ENABLED = "ActiveTrackGPSAssistantEnabled";
    @Key(accessType = 6, type = ActiveTrackMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ACTIVE_TRACK_MODE = "ActiveTrackMode";
    @Key(accessType = 2, type = Long.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ADD_TIME_LAPSE_KEY_FRAME = "AddTimeLapseKeyFrame";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ADVANCED_GESTURE_CONTROL_ENABLED = "AdvancedGestureControlEnabled";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ADVANCED_GO_HOME_ENABLED = "AdvancedGoHomeEnabled";
    @InternalKey
    public static final String ADVANCED_GO_HOME_STATE = "AdvancedGoHomeState";
    @InternalKey
    @Key(accessType = 4, type = AdvancedPilotAssistantSystemState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String APAS_STATE = "APASState";
    @Key(accessType = 3, includedAbstractions = {IntelligentFlightAssistantWM240Abstraction.class}, type = BottomAuxiliaryLightMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String BOTTOM_AUXILIARY_LIGHT_MODE = "BottomAuxiliaryLightMode";
    @Key(accessType = 6, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String COLLISION_AVOIDANCE_ENABLED = "CollisionAvoidanceEnabled";
    public static final String COMPONENT_KEY = "IntelligentFlightAssistant";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = ObstacleDetectionSector[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DETECTION_SECTORS = "DetectionSectors";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DOES_APAS_HAVE_TEMP_ERROR = "DoesAPASHasTempError";
    @InternalKey
    @Key(accessType = 4, type = DataEyePushVisionTip.DynamicHomeGpsStatus.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DYNAMIC_HOME_GPS_EXCEPTION = "DynamicHomeGpsException";
    @Key(accessType = 4, type = FaceAwareState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FACE_DETECTION_STATE = "FaceAwareState";
    @Key(accessType = 8, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class}, type = FixedWingControl.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FIXED_WING_CONTROL = "FixedWingControl";
    @Key(accessType = 3, type = VisionDrawHeadingMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_DRAW_HEADING_MODE = "FlightControllerDrawHeadingMode";
    @InternalKey
    @Key(accessType = 7, type = DataSingleVisualParam.DrawMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_DRAW_MODE = "FlightControllerDrawMode";
    @InternalKey
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_DRAW_SPEED = "FlightControllerDrawSpeed";
    @Key(accessType = 4, type = VisionDrawStatus.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_DRAW_STATUS = "FlightControllerDrawStatus";
    @InternalKey
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FLIGHT_CONTROLLER_DYNAMIC_HOME = "FlightControllerDynamicHome";
    @InternalKey
    @Key(accessType = 4, type = DataEyeGetPushFixedWingState.FixedWingState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_FIXEDWING_STATE = "FlightControllerFixedWingState";
    @InternalKey
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_FIXWING_GIMBALCTRL = "FlightControllerFixWingGimbalCtrl";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_IS_IN_ADVANCED_GO_HOME = "FlightControllerIsInAdvancedGoHome";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_IS_IN_DRAW = "FlightControllerIsInDraw";
    @InternalKey
    public static final String FLIGHT_CONTROLLER_IS_IN_PRECISE_LANDING = "IsInPreciseLanding";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_IS_IN_TAPFLY = "FlightControllerIsInTapFly";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_IS_IN_TRACKING = "FlightControllerIsInTracking";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_IS_MOVINGEOBJ_DETECT = "FlightControllerIsMovingObjDetect";
    @InternalKey
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_SELFIE_GPS = "FlightControllerSelfieGPS";
    @InternalKey
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_TAPFLY_RC_GIMBALCTRL = "FlightControllerTapFlyRcGimbalCtrl";
    @InternalKey
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_TAPFLY_SPEED = "FlightControllerTapFlySpeed";
    @InternalKey
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_TRACK_AUTO_FOCUS = "FlightControllerTrackAutoFocus";
    @InternalKey
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_TRACK_CIRCLEY = "FlightControllerTrackCircleY";
    @InternalKey
    @Key(accessType = 4, type = DataEyeGetPushException.TrackExceptionStatus.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_TRACK_EXCEPTION = "FlightControllerTrackException";
    @InternalKey
    @Key(accessType = 3, type = DJIVisionTrackHeadingMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_TRACK_HEADING_MODE = "FlightControllerTrackHeadingMode";
    @InternalKey
    @Key(accessType = 3, type = DJIVisionTrackMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_TRACK_MODE = "FlightControllerTrackMode";
    @InternalKey
    @Key(accessType = 4, type = Long.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FLIGHT_CONTROLLER_VISION_VERSION = "FlightControllerVisionVersion";
    @Key(accessType = 9, type = TimeLapseWaypointInfo.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String GET_KEY_FRAME_BY_INDEX = "GetKeyFrameByIndex";
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String INTELLIGENT_FLIGHT_ASSISTANT_IS_USERAVOID_ENABLE = "IsUserAvoidEnable";
    @InternalKey
    public static final String INTELLIGENT_FLIGHT_ASSISTANT_VISION_ASSISTANT_STATUS = "IntelligentFlightAssistantVisionAssistantStatus";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IN_ON_LIMITE_AREA_BOUNDARIES = "isOnLimitAreaBoundaries";
    @Key(accessType = 3, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, includedAbstractions = {IntelligentFlightAssistantWM230Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_ADVANCED_PILOT_ASSISTANT_SYSTEM_ENABLED = "IsAdvancedPilotAssistantSystemEnabled";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_APAS_ENABLED = "IsAPASEnabled";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_APAS_FUNCTIONING = "IsAPASFunctioning";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistantInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_ASCENT_LIMITED_BY_OBSTACLE = "IsAscentLimitedByObstacle";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistantPhantom4ProAbstraction.class, IntelligentFlightAssistantInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_AVOIDING_ACTIVE_OBSTACLE_COLLISION = "IsAvoidingActiveObstacleCollision";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_BACK_RADAR_OPEN = "IsBackRadarOpen";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_BRAKING = "IsBraking";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_EXECUTING_QUICK_MOVIE = "IsExecutingQuickMovie";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_FRONT_RADAR_OPEN = "IsFrontRadarOpen";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_LEFT_RADAR_OPEN = "IsLeftRadarOpen";
    @Key(accessType = 6, includedAbstractions = {IntelligentFlightAssistantWM230Abstraction.class, IntelligentFlightAssistantWM160Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MULTI_QUICK_SHOT_ENABLED = "IsMultiQuickShotEnabled";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MULTI_QUICK_SHOT_EXECUTING = "IsMultiQuickShotExecuting";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MULTI_QUICK_SHOT_WAITING_CONFIRMATION = "IsMultiQuickShotWaitingConfirmation";
    @Key(accessType = 6, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, includedAbstractions = {IntelligentFlightAssistantWM230Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MULTI_TRACKING_ENABLED = "IsMultiTrackingEnabled";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MULTI_TRACKING_EXECUTING = "IsMultiTrackingExecuting";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MULTI_TRACKING_WAITING_CONFIRMATION = "IsMultiTrackingWaitingConfirmation";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_OBSTACLE_DETECTED_FROM_LEFT = "IsObstacleDetectedFromLeft";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_OBSTACLE_DETECTED_FROM_RIGHT = "IsObstacleDetectedFromRight";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_PERFORMING_PRECISION_LANDING = "PreciseLandingState";
    @InternalKey
    @Key(accessType = 6, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, protectDuration = 50, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_POI2_ENABLED = "IsPOI2Enabled";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_RIGHT_RADAR_OPEN = "IsRightRadarOpen";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SENSOR_WORKING = "IsSensorWorking";
    @InternalKey
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SPEED_LOCK_AVAILABLE_IN_TIME_LAPSE = "IsSpeedLockAvailableInTimeLapse";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_TIME_LAPSE_EXECUTING = "IsTimeLapseExecuting";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_TIME_LAPSE_PAUSED = "IsTimeLapsePaused";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_TRACKING = "IsTracking";
    @InternalKey
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_TRACKING_HIGH_SPEED_ENABLED = "isTrackingHighSpeedEnabled";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LANDING_PROTECTION_ENABLED = "LandingProtectionEnabled";
    @Key(accessType = 4, type = DataEyeGetPushFlatCheck.FlatStatus.class)
    public static final String LANDING_PROTECTION_ORIGINAL_STATE = "LandingProtectionOriginalState";
    @Key(accessType = 4, type = VisionLandingProtectionState.class)
    public static final String LANDING_PROTECTION_STATE = "LandingProtectionState";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MAX_SUGGEST_TIME = "MaxSuggestTime";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MIN_SUGGEST_TIME = "MinSuggestTime";
    @InternalKey
    @Key(accessType = 4, type = DataEyeGetPushTrackStatus.TrackException.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MULTI_TRACKING_EXCEPTION = "MultiTrackingException";
    @InternalKey
    @Key(accessType = 4, type = DataSingleVisualParam.TrackingMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MULTI_TRACKING_SUB_MODE = "MultiTrackingSubMode";
    @InternalKey
    @Key(accessType = 4, type = DataEyeGetPushTrackStatus.TrackMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MULTI_TRACKING_TRACK_MODE = "MultiTrackingTrackMode";
    @InternalKey
    @Key(accessType = 4, type = SmartCaptureSystemStatus.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_ACTION_STATE = "PalmControlActionState";
    @Key(accessType = 3, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, type = Boolean.class)
    public static final String PALM_CONTROL_AWAY_INDOOR_ENABLED = "PalmControlAwayIndoorEnabled";
    @InternalKey
    @Key(accessType = 4, type = SmartCaptureCameraAction.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_CAMERA_ACTION = "PalmControlCameraAction";
    @InternalKey
    @Key(accessType = 4, type = SmartCaptureControlMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_CONTROL_MODE = "PalmControlControlMode";
    @InternalKey
    @Key(accessType = 4, type = SmartCaptureTargetType.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_DETECTION_LOGIC = "PalmControlDetectionLogic";
    @Key(accessType = 3, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, includedAbstractions = {IntelligentFlightAssistantSparkAbstraction.class}, type = Boolean.class)
    public static final String PALM_CONTROL_ENABLED = "HandGestureEnabled";
    @InternalKey
    @Key(accessType = 4, type = DataEyeGetPushTrackStatus.TrackException.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_EXCEPTION = "PalmControlException";
    @InternalKey
    @Key(accessType = 4, type = SmartCaptureFanProtectionState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_FAN_PROTECTION_DETECTING_STATE = "PalmControlFanProtectionDetectingState";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_LANDING_PROGRESS = "PalmControlLandingProgress";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_NUMBER_OF_DETECTED_HUMANS = "PalmControlNumberOfDetectedHumans";
    @Key(accessType = 4, type = PalmControlState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_CONTROL_STATE = "PalmControlState";
    @Key(accessType = 4, type = PalmDetectionState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PALM_DETECTION_STATE = "PalmDetectionState";
    @InternalKey
    @Key(accessType = 4, protectDuration = 10000, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_CURRENT_SPEED = "POICurrentSpeed";
    @InternalKey
    @Key(accessType = 4, type = PoiException.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_EXCEPTION = "POIException";
    @InternalKey
    @Key(accessType = 4, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_HEIGHT = "POIHeight";
    @InternalKey
    @Key(accessType = 4, type = DJILatLng.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_LOCATION = "POILatitude";
    @InternalKey
    @Key(accessType = 4, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_MAXIMUM_SPEED = "POIMaximumSpeed";
    @InternalKey
    @Key(accessType = 4, type = IntelligentHotpointMissionMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_MISSION_MODE = "POI_MISSION_MODE";
    @InternalKey
    @Key(accessType = 4, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_RADIUS = "POI_RADIUS";
    @InternalKey
    @Key(accessType = 4, type = PointOfInterestExecutingState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_STATE = "POIState";
    @InternalKey
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_TARGET_HEIGHT = "POITargetHeight";
    @InternalKey
    @Key(accessType = 4, type = PoiTargetInformation.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_TARGET_INFORMATION = "PoiTargetInformation";
    @InternalKey
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POI_TARGET_RADIUS = "POITargetRadius";
    @InternalKey
    public static final String PRECISE_LANDING_STATE = "PreciseLandingState";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PRECISION_LANDING_ENABLED = "PreciseLandingEnabled";
    @Key(accessType = 1, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String QUICK_MOVIE_CIRCLE_MAXIMUM_DISTANCE = "QuickMovieCircleMaximumDistance";
    @Key(accessType = 1, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String QUICK_MOVIE_DRONIE_MAXIMUM_DISTANCE = "QuickMovieDronieMaximumDistance";
    @Key(accessType = 1, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String QUICK_MOVIE_HELIX_MAXIMUM_DISTANCE = "QuickMovieHelixMaximumDistance";
    @Key(accessType = 1, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String QUICK_MOVIE_ROCKET_MAXIMUM_DISTANCE = "QuickMovieRocketMaximumDistance";
    @InternalKey
    @Key(accessType = 4, type = QuickShotException.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String QUICK_SHOT_EXCEPTION = "QuickShotException";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String QUICK_SHOT_PROGRESS = "QuickShotProgress";
    @Key(accessType = 2, type = Long.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String REMOVE_TIME_LAPSE_KEY_FRAME = "RemoveTimeLapseKeyFrame";
    @Key(accessType = 7, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RTH_OBSTACLE_AVOIDANCE_ENABLED = "RTHObstacleAvoidanceEnabled";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RTH_REMOTE_OBSTACLE_AVOIDANCE_ENABLED = "RTHRemoteObstacleAvoidanceEnabled";
    @Key(accessType = 2, type = SettingsDefinitions.CameraType.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SET_ACTIVE_TRACK_CAMERA = "setActiveTrackCamera";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SINGLE_VISION_SENSOR_ENABLED = "SingleVisionSensorEnabled";
    @InternalKey
    @Key(accessType = 8, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SMART_CAPTURE_CLEAR_CACHE = "SmartCaptureClearCache";
    @Key(accessType = 3, excludedAbstractions = {IntelligentFlightAssistantWM240Abstraction.class, IntelligentFlightAssistantWM245Abstraction.class, IntelligentFlightAssistantWM160Abstraction.class}, includedAbstractions = {IntelligentFlightAssistantWM230Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SMART_CAPTURE_ENABLED = "SMART_CAPTURE_ENABLED";
    @Key(accessType = 3, excludedAbstractions = {IntelligentFlightAssistantWM240Abstraction.class, IntelligentFlightAssistantWM245Abstraction.class, IntelligentFlightAssistantWM160Abstraction.class}, includedAbstractions = {IntelligentFlightAssistantWM230Abstraction.class}, type = SmartCaptureFollowingMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SMART_CAPTURE_FOLLOWING_MODE = "SmartCaptureFollowingMode";
    @InternalKey
    @Key(accessType = 1, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, type = DataEyeGetSmartCaptureStatisticsData.StatisticsData[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SMART_CAPTURE_STATISTICS_DATA = "SmartCaptureStatisticsData";
    @InternalKey
    @Key(accessType = 1, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SMART_CAPTURE_STATISTICS_DATA_COUNT = "SmartCaptureStatisticsDataCount";
    @InternalKey
    @Key(accessType = 1, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, type = Long.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SMART_CAPTURE_STATISTICS_DATA_TIME_STAMP = "SmartCaptureStatisticsDataTimeStamp";
    @InternalKey
    @Key(accessType = 2, excludedAbstractions = {IntelligentFlightAssistantWM160Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TAP_FLY_ENABLED = "TapFlyEnabled";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_CURRENT_NNUMBER_OF_SHOOTING_PHOTO = "TimeLapseTotalCurrentNumberaOfShootingPhoto";
    @InternalKey
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_CUR_GIMBAL_ATTI = "TimeLapseCurGimbalAtti";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_DURATION = "TimeLapseDuration";
    @InternalKey
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_ENABLED = "TimeLapseEnabled";
    @InternalKey
    @Key(accessType = 4, type = TimeLapseException.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_EXCEPTION = "TimeLapseException";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_EXECUTED_TIME = "TimeLapseExecutedTime";
    @InternalKey
    @Key(accessType = 4, type = TimeLapseFramesOption.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_FRAMES_OPTION = "TimeLapseFramesOption";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_IS_INFINITE_SUPPORTED = "isInfiniteSupported";
    @Key(accessType = 4, type = Long[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_KEY_FRAME_ARRAY = "TimeLapseKeyFrameArray";
    @InternalKey
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_MAX_GIMBAL_ATTI = "TimeLapseMaxGimbalAtti";
    @InternalKey
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_MAX_SPEED = "TimeLapseMaxSpeed";
    @InternalKey
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_MIN_GIMBAL_ATTI = "TimeLapseMinGimbalAtti";
    @InternalKey
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_MIN_SPEED = "TimeLapseMinSpeed";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_PROGRESS = "TimeLapseProgress";
    @InternalKey
    @Key(accessType = 3, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_SPEED = "TimeLapseSpeed";
    @InternalKey
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_SPEED_LOCKED = "TimeLapseSpeedLocked";
    @InternalKey
    @Key(accessType = 4, type = TimeLapseState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_STATE = "TimeLapseState";
    @InternalKey
    @Key(accessType = 3, type = TimeLapseSubMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_SUB_MODE = "TimeLapseSubMode";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_TOTAL_NUMBER_OF_SHOOTING_PHOTO = "TimeLapseTotalNumberOfShootingPhoto";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TIME_LAPSE_TOTAL_TIME = "TimeLapseTotalTime";
    @InternalKey
    @Key(accessType = 6, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TRACKING_MAXIMUM_SPEED = "TrackingMaximumSpeed";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TRACKING_SPEED_THRESHOLD = "TrackingSpeedThreshold";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {IntelligentFlightAssistantInspire2Abstraction.class, IntelligentFlightAssistantWM240Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String UPWARDS_AVOIDANCE_ENABLED = "RoofAvoidance";
    @Key(accessType = 3, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class, IntelligentFlightAssistantPhantom4ProAbstraction.class, IntelligentFlightAssistantInspire2Abstraction.class, MockIntelligentFlightAssistantAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String VISION_ASSISTED_POSITIONING_ENABLED = "VisionPositioningEnabled";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = VisionDetectionState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String VISION_DETECTION_STATE = "VisionDetectionState";
    @Key(accessType = 4, includedAbstractions = {IntelligentFlightAssistant1860Abstraction.class, IntelligentFlightAssistantMavicProAbstraction.class}, type = VisionSystemWarning.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String VISION_SYSTEM_WARNING = "VisionSystemWarning";

    public IntelligentFlightAssistantKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
