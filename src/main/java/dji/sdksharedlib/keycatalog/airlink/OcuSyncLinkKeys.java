package dji.sdksharedlib.keycatalog.airlink;

import dji.common.OcuSyncPIPPosition;
import dji.common.OcuSyncSecondaryVideoFormat;
import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.FrequencyInterference;
import dji.common.airlink.LightbridgeTransmissionMode;
import dji.common.airlink.OcuSyncBandwidth;
import dji.common.airlink.OcuSyncFrequencyBand;
import dji.common.airlink.OcuSyncMagneticInterferenceLevel;
import dji.common.airlink.OcuSyncSecondaryVideoDisplayMode;
import dji.common.airlink.OcuSyncSecondaryVideoOutputPort;
import dji.common.airlink.OcuSyncUnit;
import dji.common.airlink.OcuSyncWarningMessage;
import dji.common.airlink.SDRHdOffsetParams;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushSDRNfParams;
import dji.midware.media.DJIVideoDecoder;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkPM420Abstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class OcuSyncLinkKeys extends AirLinkKeys {
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = OcuSyncBandwidth.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String BANDWIDTH = "Bandwidth";
    @Key(accessType = 6, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = Float.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String BANDWIDTH_ALLOCATION_FOR_MAIN_CAMERA = "BandwidthAllocationForMainCamera";
    @Key(accessType = 3, type = ChannelSelectionMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CHANNEL_SELECTION_MODE = "ChannelSelectionMode";
    public static final String COMPONENT_KEY = "OcuSyncLink";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkAbstraction.class}, type = OcuSyncFrequencyBand.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FREQUENCY_BAND = "FrequencyBand";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FREQUENCY_POINT_INDEX = "FrequencyPointIndex";
    @Key(accessType = 4, expirationDuration = 2100, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = Integer[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FREQUENCY_POINT_INDEX_VALID_RANGE = "FrequencyPointIndexValidRange";
    @Key(accessType = 1, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = FrequencyInterference[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FREQUENCY_POINT_RSSIS = "FrequencyPointRSSIs";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = OcuSyncSecondaryVideoFormat.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HDMI_OUTPUT_FORMAT = "HDMIOutputFormat";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HD_DIST_OFFSET = "HdDistOffset";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = SDRHdOffsetParams.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HD_OFFSET_PARAM_VALUES = "HdOffsetParamValues";
    @InternalKey
    @Key(accessType = 4, type = OcuSyncMagneticInterferenceLevel.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INTERFERENCE_LEVEL = "InterferenceLevel";
    @Key(accessType = 1, expirationDuration = DJIVideoDecoder.connectLosedelay, includedAbstractions = {DJIOcuSyncDualBandLinkAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_DUAL_BAND_SUPPORTED = "IsDualBandSupported";
    @InternalKey
    @Key(accessType = 4, type = DataOsdGetPushChannalStatus.CHANNEL_STATUS.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String OCU_CHANNEL_STATUS = "OcuChannelStatus";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = OcuSyncPIPPosition.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PIP_POSITION = "PIPPosition";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = OcuSyncSecondaryVideoFormat.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SDI_OUTPUT_FORMAT = "SDIOutputFormat";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = OcuSyncSecondaryVideoDisplayMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_DISPLAY_MODE = "SecondaryVideoDisplayMode";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_OSD_BOTTOM_MARGIN = "SecondaryVideoOSDBottomMargin";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_OSD_ENABLED = "SecondaryVideoOSDEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_OSD_LEFT_MARGIN = "SecondaryVideoOSDLeftMargin";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_OSD_RIGHT_MARGIN = "SecondaryVideoOSDRightMargin";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_OSD_TOP_MARGIN = "SecondaryVideoOSDTopMargin";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = OcuSyncUnit.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_OSD_UNIT = "SecondaryVideoOSDUnits";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SECONDARY_VIDEO_OUTPUT_ENABLED = "SecondaryVideoOutputEnabled";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = OcuSyncSecondaryVideoOutputPort.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SECONDARY_VIDEO_OUTPUT_PORT = "SecondaryVideoOutputPort";
    @Key(accessType = 4, type = OcuSyncFrequencyBand[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SUPPORTED_FREQUENCY_BANDS = "SupportedFrequencyBands";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {Lightbridge2Abstraction.class, DJIOcuSyncLinkAbstraction.class}, type = LightbridgeTransmissionMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TRANSMISSION_MODE = "TransmissionMode";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = DataOsdGetPushSDRNfParams.DisLossEvent.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String USR_CONFIG_EVENT = "UsrConfigEvent";
    @Key(accessType = 4, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String VIDEO_DATA_RATE = "VideoDataRate";
    @Key(accessType = 3, includedAbstractions = {DJIOcuSyncDualBandLinkPM420Abstraction.class}, type = SettingsDefinitions.CameraType[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String VIDEO_STREAM_CAMERAS = "SdrVideoStreamCameras";
    @Key(accessType = 4, includedAbstractions = {DJIOcuSyncLinkAbstraction.class}, type = OcuSyncWarningMessage[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String WARNING_MESSAGES = "WarningMessages";

    public OcuSyncLinkKeys(String name) {
        super(name);
    }
}
