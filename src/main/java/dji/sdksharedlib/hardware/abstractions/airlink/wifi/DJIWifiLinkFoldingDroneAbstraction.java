package dji.sdksharedlib.hardware.abstractions.airlink.wifi;

import dji.common.airlink.WiFiFrequencyBand;
import dji.common.airlink.WifiDataRate;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.common.util.AirLinkUtils;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataWifiGetChannelList;
import dji.midware.data.model.P3.DataWifiGetPushSignal;
import dji.midware.data.model.P3.DataWifiGetPushSweepFrequency;
import dji.midware.data.model.P3.DataWifiGetWifiCurCodeRate;
import dji.midware.data.model.P3.DataWifiGetWifiFreqMode;
import dji.midware.data.model.P3.DataWifiSetModeChannel;
import dji.midware.data.model.P3.DataWifiSetWifiCodeRate;
import dji.midware.data.model.P3.DataWifiSetWifiFreq5GMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.subscription.WifiFoldingDroneRSSISubscription;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIWifiLinkFoldingDroneAbstraction extends DJIWifiLinkGroundAbstraction {
    protected boolean isDroneConnectedToPhone = false;
    protected RSSIEventHandler rssiEventHandler;
    protected WifiFoldingDroneRSSISubscription subscription;

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        DJIEventBusUtil.register(this);
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJISDKCache.getInstance().startListeningForUpdates(KeyHelper.getWiFiAirLinkKey("FrequencyBand"), new DJIParamAccessListener() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass1 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
            }
        }, false);
        this.isDroneConnectedToPhone = DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null);
        this.subscription = new WifiFoldingDroneRSSISubscription();
    }

    public void destroy() {
        super.destroy();
        this.isDroneConnectedToPhone = false;
        this.subscription.stop();
        this.subscription = null;
    }

    public void setFrequencyBand(WiFiFrequencyBand frequency, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (frequency != null && !frequency.equals(WiFiFrequencyBand.UNKNOWN)) {
            new DataWifiSetWifiFreq5GMode().setFrequencyMode(frequency.value()).start(CallbackUtils.getSetterDJIDataCallback(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass2 */

                public void onSuccess(Object o) {
                    DJIWifiLinkFoldingDroneAbstraction.this.getAvailableChannelNumbers(null);
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFails(DJIError error) {
                    DJIWifiLinkFoldingDroneAbstraction.this.getAvailableChannelNumbers(null);
                    if (callback != null) {
                        callback.onFails(error);
                    }
                }
            }));
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getFrequencyBand(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataWifiGetWifiFreqMode getter = new DataWifiGetWifiFreqMode();
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    callback.onSuccess(WiFiFrequencyBand.find(getter.getFreqMode()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter(WifiLinkKeys.CHANNEL_NUMBER)
    public void setChannel(int channelNumber, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getWiFiAirLinkKey("FrequencyBand"));
        WiFiFrequencyBand band = WiFiFrequencyBand.UNKNOWN;
        if (value != null) {
            band = (WiFiFrequencyBand) value.getData();
        }
        if (band == WiFiFrequencyBand.UNKNOWN) {
            if (callback != null) {
                callback.onFails(DJIAirLinkError.COMMON_EXECUTION_FAILED);
            }
        } else if (AirLinkUtils.getValidChannelsForFrequencyBand(band).contains(Integer.valueOf(channelNumber))) {
            WiFiFrequencyBand validBand = AirLinkUtils.getValidFrequencyBandForChannel(channelNumber);
            if (validBand != WiFiFrequencyBand.UNKNOWN) {
                DataWifiSetModeChannel.getInstance().setWifiMode(validBand.value()).setWifiChannel(channelNumber).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass4 */

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
            } else if (callback != null) {
                callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
            }
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(WifiLinkKeys.AVAILABLE_CHANNEL_NUMBERS)
    public void getAvailableChannelNumbers(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataWifiGetChannelList.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass5 */

                public void onSuccess(Object model) {
                    DJIWifiLinkFoldingDroneAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(DataWifiGetChannelList.getInstance().getCurChannel()), KeyHelper.getWiFiAirLinkKey(WifiLinkKeys.CHANNEL_NUMBER));
                    CallbackUtils.onSuccess(callback, DJIWifiLinkFoldingDroneAbstraction.this.getAvailableChannelNumbers());
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public Integer[] getAvailableChannelNumbers() {
        int[] c24GArray = DataWifiGetChannelList.getInstance().get24GChannelList();
        notifyValueChangeForKeyPath(c24GArray, KeyHelper.getWiFiAirLinkKey(WifiLinkKeys.CHANNEL_LIST_24G));
        int[] c5GArray = DataWifiGetChannelList.getInstance().get5GChannelList();
        notifyValueChangeForKeyPath(c5GArray, KeyHelper.getWiFiAirLinkKey(WifiLinkKeys.CHANNEL_LIST_5G));
        return concat(c24GArray, c5GArray);
    }

    @Getter(WifiLinkKeys.CHANNEL_NUMBER)
    public void getChannel(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataWifiGetChannelList.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    DJIWifiLinkFoldingDroneAbstraction.this.notifyValueChangeForKeyPath(DJIWifiLinkFoldingDroneAbstraction.this.getAvailableChannelNumbers(), KeyHelper.getWiFiAirLinkKey(WifiLinkKeys.AVAILABLE_CHANNEL_NUMBERS));
                    CallbackUtils.onSuccess(callback, Integer.valueOf(DataWifiGetChannelList.getInstance().getCurChannel()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter("DataRate")
    public void setDataRate(WifiDataRate rate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (rate != null && !rate.equals(WifiDataRate.UNKNOWN)) {
            new DataWifiSetWifiCodeRate().setCodeRate(rate.value()).start(CallbackUtils.getSetterDJIDataCallback(callback));
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("DataRate")
    public void getDataRate(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataWifiGetWifiCurCodeRate getter = new DataWifiGetWifiCurCodeRate();
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass7 */

                public void onSuccess(Object model) {
                    callback.onSuccess(WifiDataRate.find(getter.getCurCodeRate()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushSignal params) {
        if (params != null) {
            notifyValueChangeForKeyPath(Integer.valueOf(convertWifiSignalQualityToRightValue(params.getSignal())), convertKeyToPath(AirLinkKeys.DOWNLINK_SIGNAL_QUALITY));
            notifyValueChangeForKeyPath(Integer.valueOf(convertWifiSignalQualityToRightValue(params.getSignal())), convertKeyToPath(AirLinkKeys.UPLINK_SIGNAL_QUALITY));
        }
    }

    /* access modifiers changed from: protected */
    public int convertWifiSignalQualityToRightValue(int originValue) {
        if (!this.isDroneConnectedToPhone) {
            return originValue;
        }
        if (originValue <= 40) {
            return 0;
        }
        if (originValue < 136) {
            return originValue - 40;
        }
        return 100;
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataWifiGetPushSignal.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifiGetPushSignal.getInstance());
        }
    }

    @Getter(WifiLinkKeys.CHANNEL_INTERFERENCE)
    public void getFrequencyPointRSSI(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!((DJISDKCacheHWAbstraction.GetterInnerCallback) callback).hasGetCallback()) {
            getAvailableChannelNumbers(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.AnonymousClass8 */

                public void onSuccess(Object o) {
                    if (DJIWifiLinkFoldingDroneAbstraction.this.rssiEventHandler == null) {
                        DJIWifiLinkFoldingDroneAbstraction.this.rssiEventHandler = new RSSIEventHandler();
                    }
                    DJIWifiLinkFoldingDroneAbstraction.this.rssiEventHandler.onEvent3BackgroundThread(DataWifiGetPushSweepFrequency.getInstance());
                    if (!EventBus.getDefault().isRegistered(DJIWifiLinkFoldingDroneAbstraction.this.rssiEventHandler)) {
                        EventBus.getDefault().register(DJIWifiLinkFoldingDroneAbstraction.this.rssiEventHandler);
                    }
                    if (DJIWifiLinkFoldingDroneAbstraction.this.subscription != null) {
                        DJIWifiLinkFoldingDroneAbstraction.this.subscription.start();
                    }
                }

                public void onFails(DJIError error) {
                    callback.onFails(DJIError.COMMON_EXECUTION_FAILED);
                }
            });
        }
    }

    public class RSSIEventHandler {
        public RSSIEventHandler() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x005f, code lost:
            r16 = r11.length;
            r17 = r6.length;
         */
        @org.greenrobot.eventbus.Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.BACKGROUND)
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onEvent3BackgroundThread(dji.midware.data.model.P3.DataWifiGetPushSweepFrequency r22) {
            /*
                r21 = this;
                if (r22 == 0) goto L_0x0123
                int[] r11 = r22.get24GRssiList()
                r0 = r21
                dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction r0 = dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.this
                r16 = r0
                java.lang.String r17 = "RssiList24G"
                dji.sdksharedlib.keycatalog.DJISDKCacheKey r17 = dji.sdksharedlib.extension.KeyHelper.getWiFiAirLinkKey(r17)
                r0 = r16
                r1 = r17
                r0.notifyValueChangeForKeyPath(r11, r1)
                int[] r12 = r22.get5GRssiList()
                r0 = r21
                dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction r0 = dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.this
                r16 = r0
                java.lang.String r17 = "RssiList5G"
                dji.sdksharedlib.keycatalog.DJISDKCacheKey r17 = dji.sdksharedlib.extension.KeyHelper.getWiFiAirLinkKey(r17)
                r0 = r16
                r1 = r17
                r0.notifyValueChangeForKeyPath(r12, r1)
                java.lang.String r16 = "ChannelList24G"
                java.lang.Object r5 = dji.sdksharedlib.extension.CacheHelper.getWiFiAirLink(r16)
                int[] r5 = (int[]) r5
                java.lang.String r16 = "ChannelList5G"
                java.lang.Object r6 = dji.sdksharedlib.extension.CacheHelper.getWiFiAirLink(r16)
                int[] r6 = (int[]) r6
                long r14 = java.lang.System.currentTimeMillis()
                java.util.Calendar r4 = java.util.Calendar.getInstance()
                r4.setTimeInMillis(r14)
                dji.midware.data.config.P3.ProductType r16 = dji.midware.data.config.P3.ProductType.KumquatX
                dji.midware.data.manager.P3.DJIProductManager r17 = dji.midware.data.manager.P3.DJIProductManager.getInstance()
                dji.midware.data.config.P3.ProductType r17 = r17.getType()
                boolean r16 = r16.equals(r17)
                if (r16 == 0) goto L_0x00bb
                int r0 = r11.length
                r16 = r0
                int r0 = r6.length
                r17 = r0
                r0 = r16
                r1 = r17
                if (r0 != r1) goto L_0x00bb
                r9 = 1
            L_0x006c:
                if (r9 != 0) goto L_0x0086
                int r0 = r11.length
                r16 = r0
                int r0 = r5.length
                r17 = r0
                r0 = r16
                r1 = r17
                if (r0 == r1) goto L_0x0086
                int r0 = r12.length
                r16 = r0
                int r0 = r6.length
                r17 = r0
                r0 = r16
                r1 = r17
                if (r0 != r1) goto L_0x0123
            L_0x0086:
                int r0 = r11.length
                r16 = r0
                int r0 = r12.length
                r17 = r0
                int r16 = r16 + r17
                r0 = r16
                dji.common.airlink.WifiChannelInterference[] r13 = new dji.common.airlink.WifiChannelInterference[r0]
                r10 = 0
                r8 = 0
                if (r9 != 0) goto L_0x00e3
                int r0 = r11.length
                r17 = r0
                r16 = 0
            L_0x009b:
                r0 = r16
                r1 = r17
                if (r0 >= r1) goto L_0x00bd
                r7 = r11[r16]
                dji.common.airlink.WifiChannelInterference r18 = new dji.common.airlink.WifiChannelInterference
                dji.common.airlink.WiFiFrequencyBand r19 = dji.common.airlink.WiFiFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ
                r20 = r5[r8]
                r0 = r18
                r1 = r19
                r2 = r20
                r0.<init>(r1, r7, r2)
                r13[r10] = r18
                int r10 = r10 + 1
                int r8 = r8 + 1
                int r16 = r16 + 1
                goto L_0x009b
            L_0x00bb:
                r9 = 0
                goto L_0x006c
            L_0x00bd:
                r8 = 0
                int r0 = r12.length
                r17 = r0
                r16 = 0
            L_0x00c3:
                r0 = r16
                r1 = r17
                if (r0 >= r1) goto L_0x0109
                r7 = r12[r16]
                dji.common.airlink.WifiChannelInterference r18 = new dji.common.airlink.WifiChannelInterference
                dji.common.airlink.WiFiFrequencyBand r19 = dji.common.airlink.WiFiFrequencyBand.FREQUENCY_BAND_5_GHZ
                r20 = r6[r8]
                r0 = r18
                r1 = r19
                r2 = r20
                r0.<init>(r1, r7, r2)
                r13[r10] = r18
                int r10 = r10 + 1
                int r8 = r8 + 1
                int r16 = r16 + 1
                goto L_0x00c3
            L_0x00e3:
                r8 = 0
                int r0 = r11.length
                r17 = r0
                r16 = 0
            L_0x00e9:
                r0 = r16
                r1 = r17
                if (r0 >= r1) goto L_0x0109
                r7 = r11[r16]
                dji.common.airlink.WifiChannelInterference r18 = new dji.common.airlink.WifiChannelInterference
                dji.common.airlink.WiFiFrequencyBand r19 = dji.common.airlink.WiFiFrequencyBand.FREQUENCY_BAND_5_GHZ
                r20 = r6[r8]
                r0 = r18
                r1 = r19
                r2 = r20
                r0.<init>(r1, r7, r2)
                r13[r10] = r18
                int r10 = r10 + 1
                int r8 = r8 + 1
                int r16 = r16 + 1
                goto L_0x00e9
            L_0x0109:
                r0 = r21
                dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction r0 = dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.this
                r16 = r0
                r0 = r21
                dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction r0 = dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.this
                r17 = r0
                java.lang.String r18 = "ChannelInterference"
                dji.sdksharedlib.keycatalog.DJISDKCacheKey r17 = r17.convertKeyToPath(r18)
                r0 = r16
                r1 = r17
                r0.notifyValueChangeForKeyPath(r13, r1)
            L_0x0123:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction.RSSIEventHandler.onEvent3BackgroundThread(dji.midware.data.model.P3.DataWifiGetPushSweepFrequency):void");
        }
    }

    /* access modifiers changed from: protected */
    public Integer[] concat(int[] a, int[] b) {
        Integer[] resultArray = new Integer[(a.length + b.length)];
        int j = 0;
        int i = 0;
        while (i < a.length) {
            resultArray[j] = Integer.valueOf(a[i]);
            i++;
            j++;
        }
        int i2 = 0;
        while (i2 < b.length) {
            resultArray[j] = Integer.valueOf(b[i2]);
            i2++;
            j++;
        }
        return resultArray;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(WifiLinkKeys.class, getClass());
    }
}
