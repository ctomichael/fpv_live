package dji.sdksharedlib.keycatalog;

import dji.common.Stick;
import dji.common.airlink.LteSignalStatus;
import dji.common.remotecontroller.AircraftMapping;
import dji.common.remotecontroller.AircraftMappingStyle;
import dji.common.remotecontroller.AuthorizationInfo;
import dji.common.remotecontroller.CalibrationState;
import dji.common.remotecontroller.ChargeMobileMode;
import dji.common.remotecontroller.ChargeRemaining;
import dji.common.remotecontroller.Credentials;
import dji.common.remotecontroller.CustomButtonTags;
import dji.common.remotecontroller.FocusControllerState;
import dji.common.remotecontroller.GPSData;
import dji.common.remotecontroller.GimbalAxis;
import dji.common.remotecontroller.GimbalControlSpeedCoefficient;
import dji.common.remotecontroller.GimbalMapping;
import dji.common.remotecontroller.GimbalMappingStyle;
import dji.common.remotecontroller.HardwareState;
import dji.common.remotecontroller.Information;
import dji.common.remotecontroller.MasterSlaveState;
import dji.common.remotecontroller.MultiDeviceAggregationState;
import dji.common.remotecontroller.PairingDevice;
import dji.common.remotecontroller.PairingState;
import dji.common.remotecontroller.ProfessionalRC;
import dji.common.remotecontroller.RCMode;
import dji.common.remotecontroller.RequestGimbalControlResult;
import dji.common.remotecontroller.ResponseForGimbalControl;
import dji.common.remotecontroller.SoftSwitchJoyStickMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.narrowband.NarrowBandExchangeEvent;
import dji.internal.narrowband.NarrowBandSlaveMode;
import dji.internal.narrowband.SlaveChannelState;
import dji.midware.data.model.P3.DataRcSetAppSpecialControl;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCC3SDRAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCFoldingDroneAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4PSDRAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCRM500Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM160Abstaction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM240Abstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM245Abstraction;
import dji.sdksharedlib.keycatalog.extension.ComplexKey;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;
import java.util.ArrayList;

@EXClassNullAway
public class RemoteControllerKeys extends DJISDKCacheKeys {
    @Key(accessType = 8, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ADD_BUTTON_PROFILE_GROUP = "add_user_for_professional_rc";
    @Key(accessType = 3, type = AircraftMapping.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AIRCRAFT_CUSTOM_MAPPING = "AircraftCustomMapping";
    @Key(accessType = 3, type = AircraftMappingStyle.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String AIRCRAFT_MAPPING_STYLE = "AircraftMappingStyle";
    @Key(accessType = 1, type = Information[].class)
    public static final String AVAILABLE_MASTERS = "AvailableMasters";
    @Key(accessType = 3, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = ProfessionalRC.ButtonConfiguration.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String BUTTON_CONFIG = "full_config_of_professional_rc";
    @Key(accessType = 4, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = ProfessionalRC.Event.class, updateType = DJISDKCacheUpdateType.EVENT)
    public static final String BUTTON_EVENT_OF_PROFESSIONAL_RC = "buttonEventOfProfessionalRc";
    @Key(accessType = 1, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = String[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String BUTTON_PROFILE_GROUPS = "get_user_list_of_professional_rc";
    @Key(accessType = 2, type = CalibrationState.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CALIBRATION_STATE = "CalibrationState";
    @Key(accessType = 3, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = ChargeMobileMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CHARGE_MOBILE_MODE = "ChargeMobileMode";
    @Key(accessType = 4, type = ChargeRemaining.class)
    public static final String CHARGE_REMAINING = "ChargeRemaining";
    @Deprecated
    @Key(accessType = 8, includedAbstractions = {DJIRCProfessionalAbstraction.class}, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String COMMIT_CHANGES_FOR_PROFESSIONAL_RC = "commit_changes";
    public static final String COMPONENT_KEY = "RemoteController";
    @Key(accessType = 1, type = Credentials.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONNECTED_MASTER_CREDENTIALS = "ConnectedMasterCredentials";
    @Key(accessType = 8, type = Credentials.class)
    public static final String CONNECT_TO_MASTER = "connectToMaster";
    @Key(accessType = 8, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = AuthorizationInfo.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONNECT_TO_MASTER_WITH_ID = "connectToMasterWithID";
    @Key(accessType = 6, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = Integer.class)
    public static final String CONTROLLING_GIMBAL_INDEX = "controllingGimbalIndex";
    @Key(accessType = 1, excludedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String COUNTRY_CODE = "CountryCode";
    @Key(accessType = 8, includedAbstractions = {DJIRCProfessionalAbstraction.class}, types = {ProfessionalRC.CustomizableButton.class, ProfessionalRC.ButtonAction.class}, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CUSTOMIZE_BUTTON = "set_single_config_of_professional_rc";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String CUSTOM_BUTTON_1 = "CustomButton1";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String CUSTOM_BUTTON_2 = "CustomButton2";
    @Key(accessType = 4, includedAbstractions = {DJIRCRM500Abstraction.class}, type = HardwareState.Button.class)
    public static final String CUSTOM_BUTTON_3 = "CustomButton3";
    @Key(accessType = 3, type = CustomButtonTags.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CUSTOM_BUTTON_TAGS = "CustomButtonTags";
    @Key(accessType = 3, type = GimbalMapping.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CUSTOM_GIMBAL_MAPPING = "CustomGimbalMapping";
    @Key(accessType = 4, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DISPLAY_NAME = "DisplayName";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ENTER_RC_TO_AIRCRAFT_PAIRING_MODE = "EnterRCToAircraftPairingMode";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String EXIT_RC_TO_AIRCRAFT_PAIRING_MODE = "ExitRCToAircraftPairingMode";
    @Key(accessType = 8, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = ProfessionalRC.CustomizableButton.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FETCH_CUSTOMIZED_ACTION_OF_BUTTON = "get_single_config_of_professional_rc";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = Boolean.class)
    public static final String FIVE_DIMENS_BUTTON_PUSH_DOWN = "FiveDimensButtonPushDown";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = Boolean.class)
    public static final String FIVE_DIMENS_BUTTON_PUSH_LEFT = "FiveDimensButtonPushLeft";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = Boolean.class)
    public static final String FIVE_DIMENS_BUTTON_PUSH_PRESSED = "FiveDimensButtonPushPressed";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = Boolean.class)
    public static final String FIVE_DIMENS_BUTTON_PUSH_RIGHT = "FiveDimensButtonPushRight";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = Boolean.class)
    public static final String FIVE_DIMENS_BUTTON_PUSH_UP = "FiveDimensButtonPushUp";
    @Key(accessType = 4, type = HardwareState.FiveDButton.class)
    public static final String FIVE_D_BUTTON = "FiveDButton";
    @Key(accessType = 6, type = HardwareState.FlightModeSwitch.class)
    public static final String FLIGHT_MODE_SWITCH_POSITION = "FlightModeSwitchPosition";
    @Key(accessType = 4, type = FocusControllerState.ControlType.class)
    public static final String FOCUS_CONTROLLER_CONTROL_TYPE = "FocusControllerControlType";
    @Key(accessType = 4, type = FocusControllerState.Direction.class)
    public static final String FOCUS_CONTROLLER_DIRECTION = "FocusControllerDirection";
    @Key(accessType = 4, type = Boolean.class)
    public static final String FOCUS_CONTROLLER_IS_WORKING = "FocusControllerIsWorking";
    @InternalKey
    @Key(accessType = 1, excludedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FULL_SERIAL_NUMBER_HASH = "FullSerialNumberHash";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String FUNCTION_BUTTON = "FunctionButton";
    @Key(accessType = 8, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String GET_MASTER_AUTH_CODE = "GetMasterAuthCode";
    @Key(accessType = 3, type = GimbalControlSpeedCoefficient.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String GIMBAL_CONTROL_SPEED_COEFFICIENT = "GimbalControlSpeedCoefficient";
    @Key(accessType = 3, type = GimbalMappingStyle.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String GIMBAL_MAPPING_STYLE = "GimbalMappingStyle";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String GO_HOME_BUTTON = "GoHomeButton";
    @Key(accessType = 4, type = GPSData.class)
    public static final String GPS_DATA = "GPSData";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_CHARGE_REMAINING_LOW = "IsChargeRemainingLow";
    @Key(accessType = 4, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_FOCUS_CONTROLLER_SUPPORTED = "IsFocusControllerSupported";
    @Key(accessType = 4, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_MASTER_SLAVE_MODE_SUPPORTED = "IsMasterSlaveModeSupported";
    @Key(accessType = 1, includedAbstractions = {DJIRCInspire2Abstraction.class}, protectDuration = 0, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_MASTER_SLAVE_MODE_V2_SUPPORTED = "IsMasterSlaveModeV2Supported";
    @Key(accessType = 4, includedAbstractions = {DJIRCPhantom4RTKAbstraction.class}, type = Boolean.class)
    public static final String IS_MULTI_DEVICE_PAIRING_SUPPORTED = "isMultiDevicePairingSupported";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_NARROW_BAND_MODULE_CONNECTED = "IsNarrowBandModuleConnected";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIRCWM160Abstaction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_STILL_LINKED = "IsStillLinked";
    @Key(accessType = 4, type = Stick.class)
    public static final String LEFT_STICK_VALUE = "LeftStickValue";
    @Key(accessType = 4, type = Integer.class)
    public static final String LEFT_WHEEL = "LeftWheel";
    @Key(accessType = 3, excludedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCPhantom4PSDRAbstraction.class, DJIRCInspire2Abstraction.class, DJIRCProfessionalAbstraction.class, DJIRCWM240Abstraction.class, DJIRCWM245Abstraction.class}, type = GimbalAxis.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LEFT_WHEEL_GIMBAL_CONTROL_AXIS = "LeftWheelGimbalControlAxis";
    @Key(accessType = 3, excludedAbstractions = {DJIRCPhantom4RTKAbstraction.class, DJIRCPhantom4PSDRAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LEFT_WHEEL_GIMBAL_CONTROL_SPEED_COEFFICIENT = "LeftWheelGimbalControlSpeedCoefficient";
    @Key(accessType = 4, includedAbstractions = {DJIRCC3SDRAbstraction.class}, type = LteSignalStatus.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LTE_STATUS = "LteStatus";
    @Key(accessType = 1, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = String[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MASTER_LIST = "MasterList";
    @Key(accessType = 1, type = Boolean.class)
    public static final String MASTER_SEARCHING_STATE = "MasterSearchingState";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MASTER_SLAVE_ID = "MasterSlaveID";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = MasterSlaveState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MASTER_SLAVE_STATE = "MasterSlaveState";
    @ComplexKey({@Key(accessType = 3, type = RCMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC), @Key(accessType = 6, includedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = RCMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)})
    public static final String MODE = "Mode";
    @Key(accessType = 4, includedAbstractions = {DJIRCPhantom4RTKAbstraction.class}, type = MultiDeviceAggregationState.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MULTI_DEVICES_PAIRING_STATE = "MultiDeviceAggregationState";
    @Key(accessType = 3, excludedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCWM240Abstraction.class, DJIRCWM245Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String NAME = "Name";
    @Key(accessType = 6, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_AUTHCODE = "NarrowBandAuthCode";
    @Key(accessType = 8, includedAbstractions = {DJIRCInspire2Abstraction.class}, types = {String.class, String.class, NarrowBandSlaveMode.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String NARROW_BAND_CONNECT = "NarrowBandConnect";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_CONNECTION_STATE = "NarrowBandConnectionState";
    @Key(accessType = 8, includedAbstractions = {DJIRCInspire2Abstraction.class}, types = {String.class, String.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String NARROW_BAND_DISCONNECT = "NarrowBandDisConnect";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = NarrowBandExchangeEvent.class, updateType = DJISDKCacheUpdateType.EVENT)
    public static final String NARROW_BAND_EXCHANGE_EVENT = "NarrowBandExchangeEvent";
    @Key(accessType = 8, includedAbstractions = {DJIRCInspire2Abstraction.class}, types = {NarrowBandSlaveMode.class, NarrowBandSlaveMode.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String NARROW_BAND_EXCHANGE_MODE = "NarrowBandExchangeMode";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_ID = "NarrowBandID";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_MASTER_ID = "NarrowBandMasterID";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_PAD_ANTENNA_CONNECT_STATE = "NarrowBandPadAntennaConnectState";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = ArrayList.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_SCAN_MASTER_LIST = "NarrowBandScanMasterList";
    @InternalKey
    @Key(accessType = 6, includedAbstractions = {DJIRCInspire2Abstraction.class}, types = {Boolean.class}, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_SHIELD_UP_CMD = "NarrowBandShieldUpCmd";
    @Key(accessType = 6, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = NarrowBandSlaveMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String NARROW_BAND_SLAVE_MODE = "NarrowBandSlaveMode";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = SlaveChannelState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NARROW_BAND_SLAVE_STATE = "NarrowBandSlaveState";
    @Key(accessType = 1, type = PairingState.class)
    public static final String PAIRING_STATE = "PairingState";
    @Key(accessType = 3, excludedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCWM240Abstraction.class, DJIRCWM245Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PASSWORD = "Password";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String PAUSE_BUTTON = "PauseButton";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String PLAYBACK_BUTTON = "PlaybackButton";
    @InternalKey
    @Key(accessType = 3, type = DataRcSetAppSpecialControl.RcAircraftType.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RC_AIRCRAFT_TYPE = "RcAircraftType";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {DJIRCFoldingDroneAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RC_CALIBRATION = "RcCalibration";
    @Key(accessType = 4, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RC_HAS_GIMBAL_CONTROL = "RcHasGimbalControl";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RC_MASTER_SLAVE_OPEN = "RcMasterSlaveOpen";
    @Key(accessType = 3, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RC_UPGRADE_VOICE_DISABLE = "remote_controller_voice_disable";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String RECORD_BUTTON = "RecordButton";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RELEASE_GIMBAL_CONTROL = "ReleaseGimbalControl";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {DJIRCWM240Abstraction.class}, type = Integer[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REMAIN_BUFFER_SIZE = "RemainBufferSize";
    @InternalKey
    @Key(accessType = 8, includedAbstractions = {DJIRCFoldingDroneAbstraction.class})
    public static final String REMOTE_CONTROLLER_CALIBRATION = "RemoteControllerCalibration";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_A_AXIS_STATUS = "RemoteControllerAAxisStatus";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_B_AXIS_STATUS = "RemoteControllerBAxisStatus";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_C_AXIS_STATUS = "RemoteControllerCAxisStatus";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_D_AXIS_STATUS = "RemoteControllerDAxisStatus";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_E_AXIS_STATUS = "RemoteControllerEAxisStatus";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_F_AXIS_STATUS = "RemoteControllerFAxisStatus";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_G_AXIS_STATUS = "RemoteControllerGAxisStatus";
    @InternalKey
    @Key(accessType = 1, autoGetInterval = 200, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_H_AXIS_STATUS = "RemoteControllerHAxisStatus";
    @InternalKey
    @Key(accessType = 1, includedAbstractions = {DJIRCFoldingDroneAbstraction.class, DJIRCSparkAbstraction.class}, type = Integer.class)
    public static final String REMOTE_CONTROLLER_CALIBRATION_NUMBER_OF_SEGMENT = "RemoteControllerCalibrationNumberOfFragment";
    @Key(accessType = 2, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REMOTE_CONTROLLER_UNIT = "remote_controller_unit_rc";
    @Key(accessType = 8, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String REMOVE_BUTTON_PROFILE_GROUP = "delete_user_for_professional_rc";
    @Key(accessType = 8, includedAbstractions = {DJIRCProfessionalAbstraction.class}, types = {String.class, String.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RENAME_BUTTON_PROFILE_GROUP = "change_user_name_of_professional_rc";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String REQUEST_GIMBAL_CONTROL = "RequestGimbalControl";
    @Key(accessType = 1, type = RequestGimbalControlResult.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String REQUEST_LEGACY_GIMBAL_CONTROL = "RequestLegacyGimbalControl";
    @Key(accessType = 8, includedAbstractions = {DJIRCProfessionalAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESET_BUTTON_CONFIG = "reset_professional_rc";
    @Key(accessType = 8, type = ResponseForGimbalControl.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESPONSE_TO_REQUEST_FOR_GIMBAL_CONTROL = "ResponseToRequestForGimbalControl";
    @Key(accessType = 4, type = Stick.class)
    public static final String RIGHT_STICK_VALUE = "RightStickValue";
    @Key(accessType = 4, type = HardwareState.RightWheel.class)
    public static final String RIGHT_WHEEL = "RightWheel";
    @Key(accessType = 6, includedAbstractions = {DJIRCPhantom4RTKAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RTK_CHANNEL_ENABLE = "RTKChannelEnable";
    @Key(accessType = 3, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SELECT_BUTTON_PROFILE = "current_config_index_of_current_user";
    @Key(accessType = 3, includedAbstractions = {DJIRCProfessionalAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SELECT_BUTTON_PROFILE_GROUP = "current_user_of_professional_rc";
    @Key(accessType = 2, includedAbstractions = {DJIRCInspire2Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SET_MASTER_AUTH_CODE = "SetMasterAuthCode";
    @Key(accessType = 4, type = HardwareState.Button.class)
    public static final String SHUTTER_BUTTON = "ShutterButton";
    @Key(accessType = 1, type = Information[].class)
    public static final String SLAVE_LIST = "SlaveList";
    @Key(accessType = 6, includedAbstractions = {DJIRCWM160Abstaction.class}, type = SoftSwitchJoyStickMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SOFT_SWITCH_STICK_MODE = "SoftSwitchStickMode";
    @Key(accessType = 8, includedAbstractions = {DJIRCPhantom4RTKAbstraction.class}, type = PairingDevice.class)
    public static final String START_MULTI_DEVICE_PAIRING = "StartMultiDevicePairing";
    @Key(accessType = 8, excludedAbstractions = {DJIRCPhantom4RTKAbstraction.class})
    public static final String START_PAIRING = "StartPairing";
    @Key(accessType = 8)
    public static final String START_SEARCH_MASTER = "StartSearchMaster";
    @InternalKey
    @Key(accessType = 1, type = ArrayList.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STICK_MAPPING = "StickMapping";
    @Key(accessType = 8)
    public static final String STOP_MASTER_SEARCHING = "StopMasterSearching";
    @Key(accessType = 8, includedAbstractions = {DJIRCPhantom4RTKAbstraction.class})
    public static final String STOP_MULTI_DEVICE_PAIRING = "StopMultiDevicePairing";
    @Key(accessType = 8, excludedAbstractions = {DJIRCPhantom4RTKAbstraction.class})
    public static final String STOP_PAIRING = "StopPairing";
    @Key(accessType = 4, type = HardwareState.TransformationSwitch.class)
    public static final String TRANSFORMATION_SWITCH = "TransformationSwitch";

    public RemoteControllerKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
