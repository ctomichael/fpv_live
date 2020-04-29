package dji.sdksharedlib.hardware.abstractions.airlink.ocusync;

import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.FrequencyInterference;
import dji.common.airlink.OcuSyncBandwidth;
import dji.common.airlink.OcuSyncFrequencyBand;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.common.util.SDRLinkHelper;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetPushSDRNfParams;
import dji.midware.data.model.P3.DataOsdGetPushSdrSweepFrequency;
import dji.midware.data.model.P3.DataOsdSetSdrAssitantRead;
import dji.midware.data.model.P3.DataOsdSetSdrAssitantWrite;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIOcuSyncDualBandLinkAbstraction extends DJIOcuSyncLinkAbstraction implements DJIParamAccessListener {
    private static final OcuSyncFrequencyBand[] ALL_BANDS_RANGE = {OcuSyncFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ, OcuSyncFrequencyBand.FREQUENCY_BAND_5_DOT_8_GHZ, OcuSyncFrequencyBand.FREQUENCY_BAND_DUAL};
    private static final OcuSyncFrequencyBand[] DUAL_BANDS_RANGE = {OcuSyncFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ, OcuSyncFrequencyBand.FREQUENCY_BAND_5_DOT_8_GHZ};
    private static final float RSSI_FREQUENCY_INTERVAL = 2.0f;
    private static final float RSSI_FREQUENCY_OFFSET_2dot4G = 2400.0f;
    private static final float RSSI_FREQUENCY_OFFSET_5dot8G = 5470.0f;
    private static final OcuSyncFrequencyBand[] SINGLE_BAND_RANGE = {OcuSyncFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ};

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        CacheHelper.addOcuSyncLinkListener(this, "ChannelSelectionMode", OcuSyncLinkKeys.IS_DUAL_BAND_SUPPORTED);
        CacheHelper.addAirlinkListener(this, "CountryCode");
    }

    @Getter(OcuSyncLinkKeys.FREQUENCY_POINT_RSSIS)
    public void getFrequencyPointRSSI(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!((DJISDKCacheHWAbstraction.GetterInnerCallback) callback).hasGetCallback()) {
            if (this.rssiEventHandler == null) {
                this.rssiEventHandler = new DualBandRSSIEventHandler();
            }
            if (!EventBus.getDefault().isRegistered(this.rssiEventHandler)) {
                EventBus.getDefault().register(this.rssiEventHandler);
            }
            this.ocuSyncRssiSubscription.start();
        }
    }

    public class DualBandRSSIEventHandler extends DJIOcuSyncLinkAbstraction.RSSIEventHandler {
        public DualBandRSSIEventHandler() {
            super();
        }

        @Subscribe(threadMode = ThreadMode.BACKGROUND)
        public void onEvent3BackgroundThread(DataOsdGetPushSdrSweepFrequency frequency) {
            frequency.getSignalList();
            FrequencyInterference[] rssi2dot4G = new FrequencyInterference[41];
            FrequencyInterference[] rssi5dot8G = new FrequencyInterference[62];
            DJIOcuSyncDualBandLinkAbstraction.this.getFrequencyPointIndexValidRange();
            int i = 0;
            while (i < frequency.getRecData().length) {
                try {
                    if (i < 41) {
                        rssi2dot4G[i] = new FrequencyInterference(DJIOcuSyncDualBandLinkAbstraction.RSSI_FREQUENCY_OFFSET_2dot4G + (((float) i) * 2.0f), DJIOcuSyncDualBandLinkAbstraction.RSSI_FREQUENCY_OFFSET_2dot4G + (((float) (i + 1)) * 2.0f), (byte) frequency.getSignalList()[i]);
                    } else if (i < 103) {
                        rssi5dot8G[i - 41] = new FrequencyInterference(DJIOcuSyncDualBandLinkAbstraction.RSSI_FREQUENCY_OFFSET_5dot8G + (((float) (i - 41)) * 2.0f), DJIOcuSyncDualBandLinkAbstraction.RSSI_FREQUENCY_OFFSET_5dot8G + (((float) ((i - 41) + 1)) * 2.0f), (byte) frequency.getSignalList()[i]);
                    }
                    i++;
                } catch (Exception e) {
                }
            }
            int freqIndex = ((Integer) CacheHelper.getValue(KeyHelper.getOcuSyncLinkKey(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX))).intValue();
            if (SDRLinkHelper.isFrequencyIndexIn2dot4G(freqIndex)) {
                DJIOcuSyncDualBandLinkAbstraction.this.notifyValueChangeForKeyPath(rssi2dot4G, DJIOcuSyncDualBandLinkAbstraction.this.convertKeyToPath(OcuSyncLinkKeys.FREQUENCY_POINT_RSSIS));
            } else if (SDRLinkHelper.isFrequencyIndexIn5dot8G(freqIndex)) {
                DJIOcuSyncDualBandLinkAbstraction.this.notifyValueChangeForKeyPath(rssi5dot8G, DJIOcuSyncDualBandLinkAbstraction.this.convertKeyToPath(OcuSyncLinkKeys.FREQUENCY_POINT_RSSIS));
            } else {
                DJIOcuSyncDualBandLinkAbstraction.this.notifyValueChangeForKeyPath(rssi2dot4G, DJIOcuSyncDualBandLinkAbstraction.this.convertKeyToPath(OcuSyncLinkKeys.FREQUENCY_POINT_RSSIS));
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.common.util.CallbackUtils.onSuccess(dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, java.lang.Object):void
     arg types: [dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, boolean]
     candidates:
      dji.common.util.CallbackUtils.onSuccess(dji.common.util.CommonCallbacks$CompletionCallbackWith, java.lang.Object):void
      dji.common.util.CallbackUtils.onSuccess(dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, java.lang.Object):void */
    @Getter(OcuSyncLinkKeys.IS_DUAL_BAND_SUPPORTED)
    public void isDualBandSupported(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback == null) {
            return;
        }
        if (!CommonUtil.supportDualBand()) {
            CallbackUtils.onSuccess(callback, (Object) false);
        } else {
            this.mergeGetOcuSyncInfo.getInfo(OcuSyncLinkKeys.IS_DUAL_BAND_SUPPORTED, new DJISDKCacheCommonMergeCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkAbstraction.AnonymousClass1 */

                public void onSuccess(Object object) {
                    CallbackUtils.onSuccess(callback, object);
                }

                public void onFailure(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Getter("FrequencyBand")
    public void getFrequencyBand(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOsdSetSdrAssitantRead.getInstance().setSdrDeviceType(DataOsdSetSdrAssitantRead.SdrDeviceType.Sky);
        DataOsdSetSdrAssitantRead.getInstance().setSdrCpuType(DataOsdSetSdrAssitantRead.SdrCpuType.CP_A7);
        DataOsdSetSdrAssitantRead.getInstance().setSdrDataType(DataOsdSetSdrAssitantRead.SdrDataType.Byte_Data);
        DataOsdSetSdrAssitantRead.getInstance().setAddress(-65437);
        DataOsdSetSdrAssitantRead.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, OcuSyncFrequencyBand.find(DataOsdSetSdrAssitantRead.getInstance().getIntValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter("FrequencyBand")
    public void setFrequencyBand(OcuSyncFrequencyBand ocuSyncFrequencyBand, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (ocuSyncFrequencyBand == null || ocuSyncFrequencyBand == OcuSyncFrequencyBand.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_PARAM_ILLEGAL);
            return;
        }
        boolean isSupported = false;
        OcuSyncFrequencyBand[] ocuSyncFrequencies = (OcuSyncFrequencyBand[]) CacheHelper.getOcuSyncLink("SupportedFrequencyBands");
        if (ocuSyncFrequencies != null) {
            int length = ocuSyncFrequencies.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (ocuSyncFrequencies[i] == ocuSyncFrequencyBand) {
                    isSupported = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        if (!isSupported) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataOsdSetSdrAssitantWrite.getInstance().setSdrDeviceType(DataOsdSetSdrAssitantRead.SdrDeviceType.Sky);
        DataOsdSetSdrAssitantWrite.getInstance().setSdrCpuType(DataOsdSetSdrAssitantRead.SdrCpuType.CP_A7);
        DataOsdSetSdrAssitantWrite.getInstance().setSdrDataType(DataOsdSetSdrAssitantRead.SdrDataType.Byte_Data);
        DataOsdSetSdrAssitantWrite.getInstance().setAddress(-65437);
        DataOsdSetSdrAssitantWrite.getInstance().setWriteValue(ocuSyncFrequencyBand.getValue());
        DataOsdSetSdrAssitantWrite.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncDualBandLinkAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* access modifiers changed from: protected */
    public Integer[] calculateFrequencyIndexValidRangeWithBandwidth(OcuSyncBandwidth bandwidth, ChannelSelectionMode selectionMode) {
        Integer[] range = new Integer[2];
        if (bandwidth == OcuSyncBandwidth.Bandwidth10MHz) {
            range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMinNf10M()));
            range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMaxNf10M()));
        } else {
            range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMinNf20M()));
            range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMaxNf20M()));
        }
        return range;
    }

    public void destroy() {
        super.destroy();
        CacheHelper.removeListener(this);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null) {
            notifyValueChangeForKeyPath(calculateSupportedFrequencyBands(), convertKeyToPath("SupportedFrequencyBands"));
            getFrequencyPointIndexValidRange();
            forceFrequencyBandTo2DOT4IfNeeded();
        }
    }

    private OcuSyncFrequencyBand[] calculateSupportedFrequencyBands() {
        boolean isDualBandSupported = CacheHelper.toBool(CacheHelper.getOcuSyncLink(OcuSyncLinkKeys.IS_DUAL_BAND_SUPPORTED));
        ChannelSelectionMode mode = (ChannelSelectionMode) CacheHelper.getValue(KeyHelper.getOcuSyncLinkKey("ChannelSelectionMode"), ChannelSelectionMode.UNKNOWN);
        if (isDualBandSupported) {
            switch (mode) {
                case AUTO:
                    return ALL_BANDS_RANGE;
                case MANUAL:
                    return DUAL_BANDS_RANGE;
            }
        } else {
            switch (mode) {
                case AUTO:
                case MANUAL:
                    return SINGLE_BAND_RANGE;
            }
        }
        return new OcuSyncFrequencyBand[0];
    }

    private void forceFrequencyBandTo2DOT4IfNeeded() {
        Boolean isDualBandSupported = (Boolean) CacheHelper.getOcuSyncLink(OcuSyncLinkKeys.IS_DUAL_BAND_SUPPORTED);
        ChannelSelectionMode currentChannelmode = (ChannelSelectionMode) CacheHelper.getOcuSyncLink("ChannelSelectionMode");
        OcuSyncFrequencyBand currentFrequencyBand = (OcuSyncFrequencyBand) CacheHelper.getOcuSyncLink("FrequencyBand");
        if (isDualBandSupported != null && currentChannelmode != null && currentFrequencyBand != null) {
            if (isDualBandSupported.booleanValue()) {
                if (currentChannelmode == ChannelSelectionMode.MANUAL && currentFrequencyBand == OcuSyncFrequencyBand.FREQUENCY_BAND_DUAL) {
                    setFrequencyBand(OcuSyncFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ, null);
                }
            } else if (currentFrequencyBand != OcuSyncFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ) {
                setFrequencyBand(OcuSyncFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ, null);
            }
        }
    }
}
