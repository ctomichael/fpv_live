package dji.sdksharedlib.hardware.abstractions.airlink.lb;

import dji.common.LightbridgePIPPosition;
import dji.common.LightbridgeSecondaryVideoFormat;
import dji.common.airlink.ChannelInterference;
import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.LightbridgeAntennaRSSI;
import dji.common.airlink.LightbridgeDataRate;
import dji.common.airlink.LightbridgeFrequencyBand;
import dji.common.airlink.LightbridgeSecondaryVideoDisplayMode;
import dji.common.airlink.LightbridgeSecondaryVideoOutputPort;
import dji.common.airlink.LightbridgeTransmissionMode;
import dji.common.airlink.LightbridgeUnit;
import dji.common.error.DJIAirLinkError;
import dji.common.util.AirLinkUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.airlink.WorkingFrequency;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataDm368SetGParams;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushSignalQuality;
import dji.midware.data.model.P3.DataOsdGetPushSweepFrequency;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class LightbridgeAbstraction extends DJISubComponentHWAbstraction implements DJIParamAccessListener {
    private static final int CHANNEL_OFFSET_2_DOT_4_G = 12;
    private static final int SUPPORTED_CHANNEL_COUNT_2_DOT_4_G = 8;
    private static final int SUPPORTED_CHANNEL_COUNT_5_DOT_8_G = 29;
    private static final String TAG = "DJISDKCacheLightBridgeSeriesAirLinkAbstraction";
    protected final Integer[] DEFAULT_RANGE = {13, 14, 15, 16, 17, 18, 19, 20};
    protected LightbridgeFrequencyBand currentFrequencyBand = null;
    protected Integer[] currentRange = null;
    protected ChannelSelectionMode mode = null;
    protected LightbridgeFrequencyBand[] supportBand = {LightbridgeFrequencyBand.UNKNOWN};

    @Getter(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_HDMI_VIDEO_INPUT_PORT)
    public abstract void getBandwidthAllocationForHDMIVideoInputPort(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(LightbridgeLinkKeys.CHANNEL)
    public abstract void getChannel(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(LightbridgeLinkKeys.CHANNEL_RANGE)
    public abstract void getChannelRange(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("ChannelSelectionMode")
    public abstract void getChannelSelectionMode(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("DataRate")
    public abstract void getDataRate(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOSDEnabled")
    public abstract void getDisplayOSDEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(LightbridgeLinkKeys.IS_EXT_VIDEO_INPUT_PORT_ENABLED)
    public abstract void getEXTVideoInputPortEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("HDMIOutputFormat")
    public abstract void getHDMIOutputFormat(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOSDBottomMargin")
    public abstract void getOSDBottomMargin(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOSDLeftMargin")
    public abstract void getOSDLeftMargin(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOSDRightMargin")
    public abstract void getOSDRightMargin(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOSDTopMargin")
    public abstract void getOSDTopMargin(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOSDUnits")
    public abstract void getOSDUnit(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("PIPPosition")
    public abstract void getPIPPosition(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SDIOutputFormat")
    public abstract void getSDIOutputFormat(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoDisplayMode")
    public abstract void getSecondaryVideoDisplayMode(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOutputEnabled")
    public abstract void getSecondaryVideoOutputEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SecondaryVideoOutputPort")
    public abstract void getSecondaryVideoOutputPort(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("SupportedFrequencyBands")
    public abstract void getSupportedFrequencyBands(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("TransmissionMode")
    public abstract void getTransmissionMode(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(LightbridgeLinkKeys.WORKING_FREQUENCY)
    public abstract void getWorkingFrequency(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    public abstract boolean isSecondaryVideoOutputSupported();

    @Setter(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_HDMI_VIDEO_INPUT_PORT)
    public abstract void setBandwidthAllocationForHDMIVideoInputPort(float f, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_LEFT_CAMERA)
    public abstract void setBandwidthAllocationForLeftCamera(float f, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("BandwidthAllocationForMainCamera")
    public abstract void setBandwidthAllocationForMainCamera(float f, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(LightbridgeLinkKeys.CHANNEL)
    public abstract void setChannel(int i, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("ChannelSelectionMode")
    public abstract void setChannelSelectionMode(ChannelSelectionMode channelSelectionMode, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("DataRate")
    public abstract void setDataRate(LightbridgeDataRate lightbridgeDataRate, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOSDEnabled")
    public abstract void setDisplayOSDEnabled(boolean z, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(LightbridgeLinkKeys.IS_EXT_VIDEO_INPUT_PORT_ENABLED)
    public abstract void setEXTVideoInputPortEnabled(boolean z, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_LB_VIDEO_INPUT_PORT)
    public abstract void setFPVVideoBandwidthPercent(float f, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("HDMIOutputFormat")
    public abstract void setHDMIOutputFormat(LightbridgeSecondaryVideoFormat lightbridgeSecondaryVideoFormat, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOSDBottomMargin")
    public abstract void setOSDBottomMargin(int i, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOSDLeftMargin")
    public abstract void setOSDLeftMargin(int i, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOSDRightMargin")
    public abstract void setOSDRightMargin(int i, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOSDTopMargin")
    public abstract void setOSDTopMargin(int i, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOSDUnits")
    public abstract void setOSDUnit(LightbridgeUnit lightbridgeUnit, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("PIPPosition")
    public abstract void setPIPPosition(LightbridgePIPPosition lightbridgePIPPosition, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SDIOutputFormat")
    public abstract void setSDIOutputFormat(LightbridgeSecondaryVideoFormat lightbridgeSecondaryVideoFormat, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoDisplayMode")
    public abstract void setSecondaryVideoDisplayMode(LightbridgeSecondaryVideoDisplayMode lightbridgeSecondaryVideoDisplayMode, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOutputEnabled")
    public abstract void setSecondaryVideoOutputEnabled(boolean z, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("SecondaryVideoOutputPort")
    public abstract void setSecondaryVideoOutputPort(LightbridgeSecondaryVideoOutputPort lightbridgeSecondaryVideoOutputPort, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("TransmissionMode")
    public abstract void setTransmissionMode(LightbridgeTransmissionMode lightbridgeTransmissionMode, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(LightbridgeLinkKeys.WORKING_FREQUENCY)
    public abstract void setWorkingFrequency(WorkingFrequency workingFrequency, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
        new DJISDKCacheKey.Builder().component(AirLinkKeys.COMPONENT_KEY).index(0).subComponent(LightbridgeLinkKeys.COMPONENT_KEY).subComponentIndex(0);
        CacheHelper.addLightbridgeLinkListener(this, "ChannelSelectionMode");
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null && newValue.getData() != null) {
            this.mode = (ChannelSelectionMode) CacheHelper.getLightbridgeLink("ChannelSelectionMode");
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
        CacheHelper.removeListener(this);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(LightbridgeLinkKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOsdGetPushSignalQuality.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushSignalQuality.getInstance());
        }
        if (DataRcGetPushParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushParams.getInstance());
        }
        if (DataOsdGetPushSweepFrequency.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushSweepFrequency.getInstance());
        }
        if (DataOsdGetPushChannalStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushChannalStatus.getInstance());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSignalQuality mParams) {
        DJISDKCacheKey signalQualityKey;
        int signalQuality;
        DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
        builder.component(AirLinkKeys.COMPONENT_KEY).index(0).subComponent(LightbridgeLinkKeys.COMPONENT_KEY).subComponentIndex(0);
        DJISDKCacheKey antennaKey = null;
        int energy1 = 0;
        int energy2 = 0;
        if (!mParams.isGetRcQuality()) {
            signalQualityKey = builder.paramKey(AirLinkKeys.DOWNLINK_SIGNAL_QUALITY).build();
            signalQuality = AirLinkUtils.transformRadioSignal(mParams.getDownSignalQuality());
            if (isSecondaryVideoOutputSupported()) {
                antennaKey = builder.paramKey(LightbridgeLinkKeys.REMOTE_CONTROLLER_ANTENNA_RSSI).build();
                if (mParams.getRecData().length > 1) {
                    energy1 = mParams.getAerial1DownSignalQuality();
                    if (mParams.getRecData().length > 2) {
                        energy2 = mParams.getAerial2DownSignalQuality();
                    }
                }
            }
        } else {
            signalQualityKey = builder.paramKey(AirLinkKeys.UPLINK_SIGNAL_QUALITY).build();
            signalQuality = mParams.getUpSignalQuality();
            antennaKey = builder.paramKey(LightbridgeLinkKeys.AIRCRAFT_ANTENNA_RSSI).build();
            if (mParams.getRecData().length > 1) {
                energy1 = mParams.getAerial1UpSignalQuality();
                if (mParams.getRecData().length > 2) {
                    energy2 = mParams.getAerial2UpSignalQuality();
                }
            }
        }
        if (antennaKey != null) {
            notifyValueChangeForKeyPath(new LightbridgeAntennaRSSI(energy1, energy2), antennaKey);
        }
        notifyValueChangeForKeyPath(Integer.valueOf(signalQuality), signalQualityKey);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        notifyValueChangeForKeyPath(Float.valueOf(((float) params.getBandWidth()) / 10.0f), convertKeyToPath(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_LB_VIDEO_INPUT_PORT));
        notifyValueChangeForKeyPath(Float.valueOf(((float) params.getBandWidth()) / 10.0f), convertKeyToPath("BandwidthAllocationForMainCamera"));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSweepFrequency params) {
        if (this.currentFrequencyBand == null || this.currentRange == null) {
            ChannelInterference[] interference = new ChannelInterference[8];
            int[] tmp = params.getSignalList();
            for (int i = 0; i < 8; i++) {
                interference[i] = new ChannelInterference();
                interference[i].setChannel(i);
                interference[i].setPower(transformPower(tmp[i + 12]));
            }
            notifyValueChangeForKeyPath(interference, convertKeyToPath(LightbridgeLinkKeys.FREQUENCY_POINT_RSSIS));
            return;
        }
        int[] tmp2 = params.getSignalList();
        synchronized (this) {
            int startIndex = this.currentRange[0].intValue();
            ChannelInterference[] interference2 = new ChannelInterference[this.currentRange.length];
            for (int i2 = 0; i2 < this.currentRange.length; i2++) {
                interference2[i2] = new ChannelInterference();
                interference2[i2].setChannel(this.currentRange[i2].intValue());
                interference2[i2].setPower(transformPower(tmp2[(startIndex - 1) + i2]));
            }
            notifyValueChangeForKeyPath(interference2, convertKeyToPath(LightbridgeLinkKeys.FREQUENCY_POINT_RSSIS));
        }
    }

    private int transformPower(int power) {
        if (power < -100) {
            return -100;
        }
        if (power > -60) {
            return -60;
        }
        return power;
    }

    public boolean isDualEncodeModeSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void setter(DataDm368SetGParams.CmdId id, int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataDm368SetGParams setter = new DataDm368SetGParams();
        setter.set(id, value);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.LightbridgeAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isUsingLightBridge2(DJIComponentManager.PlatformType platformType) {
        return platformType == DJIComponentManager.PlatformType.M600 || platformType == DJIComponentManager.PlatformType.M600Pro || platformType == DJIComponentManager.PlatformType.A3 || platformType == DJIComponentManager.PlatformType.N3 || platformType == DJIComponentManager.PlatformType.A2;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushChannalStatus params) {
        if (params != null) {
            DJILog.d("LightbridgeLinkKeys.OCU_CHANNEL_STATUS", new Object[0]);
            notifyValueChangeForKeyPath(params.getChannelStatus(), LightbridgeLinkKeys.LIGHTBRIDGE_CHANNEL_STATUS);
        }
    }
}
