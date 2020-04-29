package dji.internal.mock.abstractions;

import dji.common.airlink.WifiChannelInterference;
import dji.common.airlink.WifiDataRate;
import dji.common.error.DJIAirLinkError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockWifiLinkFoldingDroneAbstraction extends DJIWifiLinkFoldingDroneAbstraction {
    private int channel;
    private WifiDataRate dataRate;
    /* access modifiers changed from: private */
    public boolean goingUp = true;
    /* access modifiers changed from: private */
    public int signalQuality = 0;

    static /* synthetic */ int access$108(MockWifiLinkFoldingDroneAbstraction x0) {
        int i = x0.signalQuality;
        x0.signalQuality = i + 1;
        return i;
    }

    static /* synthetic */ int access$110(MockWifiLinkFoldingDroneAbstraction x0) {
        int i = x0.signalQuality;
        x0.signalQuality = i - 1;
        return i;
    }

    public MockWifiLinkFoldingDroneAbstraction() {
        generateFakeData();
    }

    private void generateFakeData() {
        Observable.timer(100, TimeUnit.MILLISECONDS, Schedulers.computation()).map(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockWifiLinkFoldingDroneAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockWifiLinkFoldingDroneAbstraction.this.notifyValueChangeForKeyPath(new WifiChannelInterference[3], WifiLinkKeys.CHANNEL_INTERFERENCE);
                MockWifiLinkFoldingDroneAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockWifiLinkFoldingDroneAbstraction.this.signalQuality), AirLinkKeys.DOWNLINK_SIGNAL_QUALITY);
                MockWifiLinkFoldingDroneAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockWifiLinkFoldingDroneAbstraction.this.signalQuality), AirLinkKeys.UPLINK_SIGNAL_QUALITY);
                if (MockWifiLinkFoldingDroneAbstraction.this.goingUp) {
                    MockWifiLinkFoldingDroneAbstraction.access$108(MockWifiLinkFoldingDroneAbstraction.this);
                } else {
                    MockWifiLinkFoldingDroneAbstraction.access$110(MockWifiLinkFoldingDroneAbstraction.this);
                }
                if (MockWifiLinkFoldingDroneAbstraction.this.goingUp && MockWifiLinkFoldingDroneAbstraction.this.signalQuality >= 100) {
                    boolean unused = MockWifiLinkFoldingDroneAbstraction.this.goingUp = false;
                } else if (!MockWifiLinkFoldingDroneAbstraction.this.goingUp && MockWifiLinkFoldingDroneAbstraction.this.signalQuality <= 0) {
                    boolean unused2 = MockWifiLinkFoldingDroneAbstraction.this.goingUp = true;
                }
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    @Setter("DataRate")
    public void setDataRate(WifiDataRate rate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (rate != null && !rate.equals(WifiDataRate.UNKNOWN)) {
            this.dataRate = rate;
            if (callback != null) {
                callback.onSuccess(null);
            }
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter("DataRate")
    public void getDataRate(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(this.dataRate);
        }
    }

    @Setter(WifiLinkKeys.CHANNEL_NUMBER)
    public void setChannel(int channelNumber, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (channelNumber > 0) {
            this.channel = channelNumber;
            if (callback != null) {
                callback.onSuccess(null);
            }
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(WifiLinkKeys.CHANNEL_NUMBER)
    public void getChannel(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(Integer.valueOf(this.channel));
        }
    }
}
