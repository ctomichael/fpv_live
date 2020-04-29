package dji.sdksharedlib.hardware.abstractions.airlink.ocusync;

import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.FrequencyInterference;
import dji.common.airlink.LightbridgeTransmissionMode;
import dji.common.airlink.OcuSyncBandwidth;
import dji.common.airlink.OcuSyncMagneticInterferenceLevel;
import dji.common.airlink.OcuSyncWarningMessage;
import dji.common.airlink.SDRHdOffsetParams;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.AirLinkUtils;
import dji.common.util.CallbackUtils;
import dji.common.util.SDRLinkHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataGetPushMultiRcPairingStatus;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatusV2;
import dji.midware.data.model.P3.DataOsdGetPushSDRNfParams;
import dji.midware.data.model.P3.DataOsdGetPushSdrConfigInfo;
import dji.midware.data.model.P3.DataOsdGetPushSdrSweepFrequency;
import dji.midware.data.model.P3.DataOsdGetPushSignalQuality;
import dji.midware.data.model.P3.DataOsdGetPushWirelessState;
import dji.midware.data.model.P3.DataOsdGetSDRImageTransmMode;
import dji.midware.data.model.P3.DataOsdGetSDRPushCustomCodeRate;
import dji.midware.data.model.P3.DataOsdSetSDRConfigInfo;
import dji.midware.data.model.P3.DataOsdSetSDRImageTransmMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.merge.MergeGetOcuSyncInfo;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.subscription.OcuSyncRSSISubscription;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.operators.OperatorWhileDoWhile;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIOcuSyncLinkAbstraction extends DJISubComponentHWAbstraction implements DJIParamAccessListener {
    private static final float RSSI_FREQUENCY_INTERVAL = 2.0f;
    private static final float RSSI_FREQUENCY_OFFSET = 2400.0f;
    private static final String TAG = "DJIOcuSyncLinkAbstraction";
    private static final long WARNING_MESSAGE_EXPIRATION_TIME = 3000;
    protected DJISDKCacheKey.Builder builder;
    protected Map<OcuSyncWarningMessage, Long> currentWarningMessage = new ConcurrentHashMap();
    protected boolean isTimerRunning = false;
    protected MergeGetOcuSyncInfo mergeGetOcuSyncInfo;
    protected OcuSyncRSSISubscription ocuSyncRssiSubscription;
    protected RSSIEventHandler rssiEventHandler;
    private Observable<Boolean> warningMessageTimer = Observable.create(new OperatorWhileDoWhile(Observable.just(true).delay(1, TimeUnit.SECONDS).map(new Func1<Boolean, Boolean>() {
        /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass3 */

        public Boolean call(Boolean aBoolean) {
            boolean unused = DJIOcuSyncLinkAbstraction.this.pruneWarningMessageMapAndCheckIfShouldNotify();
            return true;
        }
    }), new Func0<Boolean>() {
        /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass1 */

        public synchronized Boolean call() {
            DJIOcuSyncLinkAbstraction.this.isTimerRunning = true;
            return true;
        }
    }, new Func0<Boolean>() {
        /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass2 */

        public synchronized Boolean call() {
            boolean z;
            Boolean valueOf;
            boolean z2 = true;
            synchronized (this) {
                DJIOcuSyncLinkAbstraction dJIOcuSyncLinkAbstraction = DJIOcuSyncLinkAbstraction.this;
                if (!DJIOcuSyncLinkAbstraction.this.currentWarningMessage.isEmpty()) {
                    z = true;
                } else {
                    z = false;
                }
                dJIOcuSyncLinkAbstraction.isTimerRunning = z;
                if (DJIOcuSyncLinkAbstraction.this.currentWarningMessage.isEmpty()) {
                    z2 = false;
                }
                valueOf = Boolean.valueOf(z2);
            }
            return valueOf;
        }
    }));

    public boolean isSecondaryVideoOutputSupported() {
        return false;
    }

    private boolean isExpired(long lastTime, long currentTime) {
        return currentTime - lastTime > WARNING_MESSAGE_EXPIRATION_TIME;
    }

    /* access modifiers changed from: private */
    public synchronized boolean pruneWarningMessageMapAndCheckIfShouldNotify() {
        boolean mapIsChanged;
        long currentTime = System.currentTimeMillis();
        mapIsChanged = false;
        Iterator it2 = this.currentWarningMessage.entrySet().iterator();
        while (it2.hasNext()) {
            if (isExpired(((Long) it2.next().getValue()).longValue(), currentTime)) {
                it2.remove();
                mapIsChanged = true;
            }
        }
        return mapIsChanged;
    }

    /* access modifiers changed from: protected */
    public boolean updateWarningMessageAndCheckIfShouldNotify(OcuSyncWarningMessage newMessage) {
        long currentTime = System.currentTimeMillis();
        boolean mapIsChanged = false;
        if (this.currentWarningMessage.containsKey(newMessage)) {
            this.currentWarningMessage.put(newMessage, Long.valueOf(currentTime));
        } else {
            this.currentWarningMessage.put(newMessage, Long.valueOf(currentTime));
            mapIsChanged = true;
        }
        if (pruneWarningMessageMapAndCheckIfShouldNotify()) {
            mapIsChanged = true;
        }
        if (mapIsChanged) {
            checkAndStartTimer();
        }
        return mapIsChanged;
    }

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
        this.ocuSyncRssiSubscription = new OcuSyncRSSISubscription();
        this.mergeGetOcuSyncInfo = new MergeGetOcuSyncInfo();
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        if (EventBus.getDefault().isRegistered(this.rssiEventHandler)) {
            EventBus.getDefault().unregister(this.rssiEventHandler);
        }
        super.destroy();
        DJISDKCache.getInstance().stopListening(this);
        this.ocuSyncRssiSubscription.stop();
        this.ocuSyncRssiSubscription = null;
        this.mergeGetOcuSyncInfo = null;
        this.currentWarningMessage.clear();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(OcuSyncLinkKeys.class, getClass());
    }

    private OcuSyncBandwidth getBandwidth() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getOcuSyncLinkKey(OcuSyncLinkKeys.BANDWIDTH));
        if (value == null || value.getData() == null) {
            return OcuSyncBandwidth.Unknown;
        }
        return (OcuSyncBandwidth) value.getData();
    }

    private ChannelSelectionMode getChannelSelectionMode() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getOcuSyncLinkKey("ChannelSelectionMode"));
        if (value == null || !(value.getData() instanceof ChannelSelectionMode)) {
            return ChannelSelectionMode.UNKNOWN;
        }
        return (ChannelSelectionMode) value.getData();
    }

    /* access modifiers changed from: protected */
    public Integer[] calculateFrequencyIndexValidRangeWithBandwidth(OcuSyncBandwidth bandwidth, ChannelSelectionMode selectionMode) {
        Integer[] range = new Integer[2];
        if (ChannelSelectionMode.AUTO.equals(selectionMode)) {
            if (DataOsdGetPushSDRNfParams.getInstance().getMinNf10M() == 0) {
                if (bandwidth == OcuSyncBandwidth.Bandwidth10MHz) {
                    range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(1006));
                    range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(PhotoshopDirectory.TAG_ONION_SKINS));
                } else {
                    range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(1011));
                    range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(PhotoshopDirectory.TAG_COLOR_SAMPLERS));
                }
            } else if (bandwidth == OcuSyncBandwidth.Bandwidth10MHz) {
                range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMinNf10M()));
                range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMaxNf10M()));
            } else {
                range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMinNf20M()));
                range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMaxNf20M()));
            }
        } else if (DataOsdGetPushSDRNfParams.getInstance().getMinNf10M() == 0) {
            if (bandwidth == OcuSyncBandwidth.Bandwidth10MHz) {
                range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(1007));
                range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(PhotoshopDirectory.TAG_LAYER_GROUPS_ENABLED_ID));
            } else {
                range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(1012));
                range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(PhotoshopDirectory.TAG_ALTERNATE_SPOT_COLORS));
            }
        } else if (bandwidth == OcuSyncBandwidth.Bandwidth10MHz) {
            range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMinNf10M()));
            range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMaxNf10M()));
        } else {
            range[0] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMinNf20M()));
            range[1] = Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetPushSDRNfParams.getInstance().getMaxNf20M()));
        }
        return range;
    }

    /* access modifiers changed from: protected */
    public Integer[] getFrequencyPointIndexValidRange() {
        OcuSyncBandwidth bandwidth = getBandwidth();
        ChannelSelectionMode selectionMode = getChannelSelectionMode();
        if (bandwidth == OcuSyncBandwidth.Unknown || selectionMode == ChannelSelectionMode.UNKNOWN) {
            return null;
        }
        Integer[] range = calculateFrequencyIndexValidRangeWithBandwidth(bandwidth, selectionMode);
        notifyValueChangeForKeyPath(range, convertKeyToPath(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX_VALID_RANGE));
        return range;
    }

    @Getter(OcuSyncLinkKeys.BANDWIDTH)
    public void getBandwidth(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetOcuSyncInfo.getInfo(OcuSyncLinkKeys.BANDWIDTH, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass4 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(OcuSyncLinkKeys.BANDWIDTH)
    public void setBandwidth(OcuSyncBandwidth ocuSyncBandwidth, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (getChannelSelectionMode() != ChannelSelectionMode.MANUAL) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_EXECUTION_FAILED);
            }
        } else if (ocuSyncBandwidth != null && ocuSyncBandwidth != OcuSyncBandwidth.Unknown) {
            DataOsdSetSDRConfigInfo.getInstance().setSDRConfigInfo(new DataOsdSetSDRConfigInfo.SDRConfigInfo(DataOsdSetSDRConfigInfo.SDRConfigType.Bandwidth, ocuSyncBandwidth.value())).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass5 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(OcuSyncLinkKeys.FREQUENCY_POINT_RSSIS)
    public void getFrequencyPointRSSI(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!((DJISDKCacheHWAbstraction.GetterInnerCallback) callback).hasGetCallback()) {
            if (this.rssiEventHandler == null) {
                this.rssiEventHandler = new RSSIEventHandler();
            }
            if (!EventBus.getDefault().isRegistered(this.rssiEventHandler)) {
                EventBus.getDefault().register(this.rssiEventHandler);
            }
            this.ocuSyncRssiSubscription.start();
        }
    }

    public class RSSIEventHandler {
        public RSSIEventHandler() {
        }

        @Subscribe(threadMode = ThreadMode.BACKGROUND)
        public void onEvent3BackgroundThread(DataOsdGetPushSdrSweepFrequency frequency) {
            FrequencyInterference[] rssis = new FrequencyInterference[frequency.getRecData().length];
            for (byte i = 0; i < frequency.getRecData().length; i = (byte) (i + 1)) {
                rssis[i] = new FrequencyInterference(DJIOcuSyncLinkAbstraction.RSSI_FREQUENCY_OFFSET + (((float) i) * 2.0f), DJIOcuSyncLinkAbstraction.RSSI_FREQUENCY_OFFSET + (((float) (i + 1)) * 2.0f), (byte) frequency.getSignalList()[i]);
            }
            DJIOcuSyncLinkAbstraction.this.notifyValueChangeForKeyPath(rssis, DJIOcuSyncLinkAbstraction.this.convertKeyToPath(OcuSyncLinkKeys.FREQUENCY_POINT_RSSIS));
        }
    }

    @Setter(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX)
    public void setFrequencyPointIndex(Integer index, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        Integer[] integers = getFrequencyPointIndexValidRange();
        if (integers == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else if (integers[0].intValue() > index.intValue() || integers[1].intValue() < index.intValue()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (getChannelSelectionMode() == ChannelSelectionMode.MANUAL) {
            DataOsdSetSDRConfigInfo.getInstance().setSDRConfigInfo(new DataOsdSetSDRConfigInfo.SDRConfigInfo(DataOsdSetSDRConfigInfo.SDRConfigType.NFIndex, SDRLinkHelper.convertFrequencyPointFromFrequencyIndex(index.intValue()))).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_EXECUTION_FAILED);
        }
    }

    @Getter(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX)
    public void getFrequencyPointIndex(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetOcuSyncInfo.getInfo(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass7 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter("TransmissionMode")
    public void setTransmissionMode(LightbridgeTransmissionMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOsdSetSDRImageTransmMode.SDRImageTransmMode internalMode;
        if (mode != null && mode != LightbridgeTransmissionMode.UNKNOWN) {
            if (mode.equals(LightbridgeTransmissionMode.LOW_LATENCY)) {
                internalMode = DataOsdSetSDRImageTransmMode.SDRImageTransmMode.SMOOTH;
            } else if (mode.equals(LightbridgeTransmissionMode.HIGH_QUALITY)) {
                internalMode = DataOsdSetSDRImageTransmMode.SDRImageTransmMode.HD;
            } else {
                internalMode = DataOsdSetSDRImageTransmMode.SDRImageTransmMode.NONE;
            }
            DataOsdSetSDRImageTransmMode.getInstance().setMode(internalMode).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass8 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("TransmissionMode")
    public void getTransmissionMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOsdGetSDRImageTransmMode.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass9 */

            public void onSuccess(Object o) {
                LightbridgeTransmissionMode latency;
                DataOsdSetSDRImageTransmMode.SDRImageTransmMode mode = DataOsdGetSDRImageTransmMode.getInstance().getMode();
                if (mode.equals(DataOsdSetSDRImageTransmMode.SDRImageTransmMode.HD)) {
                    latency = LightbridgeTransmissionMode.HIGH_QUALITY;
                } else if (mode.equals(DataOsdSetSDRImageTransmMode.SDRImageTransmMode.SMOOTH)) {
                    latency = LightbridgeTransmissionMode.LOW_LATENCY;
                } else {
                    latency = LightbridgeTransmissionMode.UNKNOWN;
                }
                CallbackUtils.onSuccess(callback, latency);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter("ChannelSelectionMode")
    public void getChannelSelectionMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetOcuSyncInfo.getInfo("ChannelSelectionMode", new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass10 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter("ChannelSelectionMode")
    public void setChannelSelectionMode(ChannelSelectionMode selectionMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (selectionMode != null && selectionMode != ChannelSelectionMode.UNKNOWN) {
            DataOsdSetSDRConfigInfo.getInstance().setSDRConfigInfo(new DataOsdSetSDRConfigInfo.SDRConfigInfo(DataOsdSetSDRConfigInfo.SDRConfigType.SelectionMode, selectionMode.value() == 0 ? 1 : 0)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        getFrequencyPointIndexValidRange();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSdrConfigInfo info) {
        notifyValueChangeForKeyPath(OcuSyncBandwidth.find(info.getBand()), convertKeyToPath(OcuSyncLinkKeys.BANDWIDTH));
        notifyValueChangeForKeyPath(Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(info.getNF())), convertKeyToPath(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetSDRPushCustomCodeRate rate) {
        notifyValueChangeForKeyPath(Float.valueOf(rate.getCodeRate()), convertKeyToPath(OcuSyncLinkKeys.VIDEO_DATA_RATE));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSignalQuality params) {
        if (!params.isGetRcQuality()) {
            notifyValueChangeForKeyPath(Integer.valueOf(AirLinkUtils.convertOcuSyncSignalQuality(params.getDownSignalQuality())), convertKeyToPath(AirLinkKeys.DOWNLINK_SIGNAL_QUALITY));
        } else {
            notifyValueChangeForKeyPath(Integer.valueOf(AirLinkUtils.convertOcuSyncSignalQuality(params.getUpSignalQuality())), convertKeyToPath(AirLinkKeys.UPLINK_SIGNAL_QUALITY));
        }
    }

    public synchronized void onEvent3BackgroundThread(DataOsdGetPushWirelessState event) {
        OcuSyncWarningMessage warningMessage;
        switch (event.getEventCode()) {
            case STRONG_DISTURBANCE:
                warningMessage = OcuSyncWarningMessage.STRONG_TAKE_OFF_INTERFERENCE;
                break;
            case VIDEO_DISTURBANCE:
                warningMessage = OcuSyncWarningMessage.STRONG_DOWN_LINK_INTERFERENCE;
                break;
            case RC_DISTURBANCE:
                warningMessage = OcuSyncWarningMessage.STRONG_UP_LINK_INTERFERENCE;
                break;
            case LOW_SIGNAL_POWER:
                warningMessage = OcuSyncWarningMessage.WEAK_SIGNAL;
                break;
            case CUSTOM_SIGNAL_DISTURBANCE:
                warningMessage = OcuSyncWarningMessage.STRONG_INTERFERENCE_WITH_MANUAL_SETTING;
                break;
            case RC_TO_GLASS_DIST:
                warningMessage = OcuSyncWarningMessage.WEAK_SIGNAL_FROM_GLASS_TO_REMOTE_CONTROLLER;
                break;
            case UAV_HAL_RESTART:
                warningMessage = OcuSyncWarningMessage.AIRCRAFT_LINK_REBOOT;
                break;
            case GLASS_DIST_RC_ANTENNA:
                warningMessage = OcuSyncWarningMessage.WEAK_SIGNAL_FROM_REMOTE_CONTROLLER_TO_GLASS;
                break;
            case DISCONNECT_RC_DISTURB:
                warningMessage = OcuSyncWarningMessage.UP_LINK_BROKEN;
                break;
            case DISCONNECT_UAV_DISTURB:
                warningMessage = OcuSyncWarningMessage.DOWN_LINK_BROKEN;
                break;
            case DISCONNECT_WEEK_SIGNAL:
                warningMessage = OcuSyncWarningMessage.LINK_UNUSABLE;
                break;
            case INTERNAL_EVENT:
                warningMessage = OcuSyncWarningMessage.DEBUG;
                break;
            default:
                warningMessage = OcuSyncWarningMessage.LINK_UNUSABLE;
                break;
        }
        if (updateWarningMessageAndCheckIfShouldNotify(warningMessage)) {
            notifyValueChangeForKeyPath(getCurrentMessages(), convertKeyToPath(OcuSyncLinkKeys.WARNING_MESSAGES));
        }
    }

    /* access modifiers changed from: protected */
    public OcuSyncWarningMessage[] getCurrentMessages() {
        OcuSyncWarningMessage[] result = new OcuSyncWarningMessage[this.currentWarningMessage.size()];
        int index = 0;
        for (Map.Entry<OcuSyncWarningMessage, Long> pair : this.currentWarningMessage.entrySet()) {
            result[index] = (OcuSyncWarningMessage) pair.getKey();
            index++;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void checkAndStartTimer() {
        if (!this.isTimerRunning) {
            DJISDKCache.getInstance().addSubscription(this.warningMessageTimer.subscribe());
        }
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOsdGetPushSdrConfigInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushSdrConfigInfo.getInstance());
        }
        if (DataOsdGetPushSDRNfParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushSDRNfParams.getInstance());
        }
        if (DataOsdGetPushChannalStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushChannalStatus.getInstance());
        }
        if (DataGetPushMultiRcPairingStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGetPushMultiRcPairingStatus.getInstance());
        }
        DJISDKCacheKey channelKey = KeyHelper.getOcuSyncLinkKey("ChannelSelectionMode");
        DJISDKCacheKey bandwidthKey = KeyHelper.getOcuSyncLinkKey(OcuSyncLinkKeys.BANDWIDTH);
        DJISDKCache.getInstance().startListeningForUpdates(channelKey, this, false);
        DJISDKCache.getInstance().startListeningForUpdates(bandwidthKey, this, false);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSDRNfParams param) {
        notifyValueChangeForKeyPath(new SDRHdOffsetParams(param.getPathLossOffset(), param.getRcLinkOffset(), param.getTxPowerOffset()), convertKeyToPath(OcuSyncLinkKeys.HD_OFFSET_PARAM_VALUES));
        notifyValueChangeForKeyPath(Integer.valueOf(param.get1KmOffset()), convertKeyToPath(OcuSyncLinkKeys.HD_DIST_OFFSET));
        notifyValueChangeForKeyPath(param.getDisLossInd(), convertKeyToPath(OcuSyncLinkKeys.USR_CONFIG_EVENT));
        getSDRFrequencyPointIndexValidRange();
    }

    private void getSDRFrequencyPointIndexValidRange() {
        DJISDKCacheParamValue value;
        DJISDKCacheParamValue selectionModeValue;
        if (DataOsdGetPushSDRNfParams.getInstance().isGetted() && (value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getOcuSyncLinkKey(OcuSyncLinkKeys.BANDWIDTH))) != null && value.getData() != null && (selectionModeValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getOcuSyncLinkKey("ChannelSelectionMode"))) != null && selectionModeValue.getData() != null) {
            notifyValueChangeForKeyPath(calculateFrequencyIndexValidRangeWithBandwidth((OcuSyncBandwidth) value.getData(), (ChannelSelectionMode) selectionModeValue.getData()), convertKeyToPath(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX_VALID_RANGE));
        }
    }

    private OcuSyncMagneticInterferenceLevel getOcuSyncMagneticInterferenceLevel(DataOsdGetPushCheckStatusV2 status) {
        OcuSyncMagneticInterferenceLevel interferenceLevel = OcuSyncMagneticInterferenceLevel.NONE;
        if (status.isRCRadioQualityHigh()) {
            return OcuSyncMagneticInterferenceLevel.HIGH;
        }
        if (status.isRCRadioQualityMedium()) {
            return OcuSyncMagneticInterferenceLevel.MEDIUM;
        }
        if (status.isRCRadioQualityLow()) {
            return OcuSyncMagneticInterferenceLevel.LOW;
        }
        return interferenceLevel;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushChannalStatus params) {
        if (params != null) {
            notifyValueChangeForKeyPath(params.getChannelStatus(), convertKeyToPath(OcuSyncLinkKeys.OCU_CHANNEL_STATUS));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCheckStatusV2 status) {
        if (status.isGetted() && status.isSenderRC()) {
            notifyValueChangeForKeyPath(getOcuSyncMagneticInterferenceLevel(status), convertKeyToPath(OcuSyncLinkKeys.INTERFERENCE_LEVEL));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushMultiRcPairingStatus status) {
        if (status.isGetted()) {
            notifyValueChangeForKeyPath(Integer.valueOf(status.getBaseStationSignalQuality()), convertKeyToPath(AirLinkKeys.BASE_STATION_SIGNAL_QUALITY));
        }
    }
}
