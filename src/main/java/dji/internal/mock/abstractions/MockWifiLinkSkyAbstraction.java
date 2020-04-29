package dji.internal.mock.abstractions;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkSkyAbstraction;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockWifiLinkSkyAbstraction extends DJIWifiLinkSkyAbstraction {
    boolean goingUp = true;
    int signalQuality = 0;

    public MockWifiLinkSkyAbstraction() {
        generateFakeSignalQuality();
    }

    private void generateFakeSignalQuality() {
        Observable.timer(100, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockWifiLinkSkyAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockWifiLinkSkyAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockWifiLinkSkyAbstraction.this.signalQuality), AirLinkKeys.DOWNLINK_SIGNAL_QUALITY);
                if (MockWifiLinkSkyAbstraction.this.goingUp) {
                    MockWifiLinkSkyAbstraction.this.signalQuality++;
                } else {
                    MockWifiLinkSkyAbstraction mockWifiLinkSkyAbstraction = MockWifiLinkSkyAbstraction.this;
                    mockWifiLinkSkyAbstraction.signalQuality--;
                }
                if (MockWifiLinkSkyAbstraction.this.goingUp && MockWifiLinkSkyAbstraction.this.signalQuality >= 100) {
                    MockWifiLinkSkyAbstraction.this.goingUp = false;
                } else if (!MockWifiLinkSkyAbstraction.this.goingUp && MockWifiLinkSkyAbstraction.this.signalQuality <= 0) {
                    MockWifiLinkSkyAbstraction.this.goingUp = true;
                }
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }
}
