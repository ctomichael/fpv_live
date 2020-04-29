package dji.internal.mock.abstractions;

import dji.common.remotecontroller.AircraftMappingStyle;
import dji.common.remotecontroller.ChargeRemaining;
import dji.common.remotecontroller.GPSData;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockRemoteControllerAbstraction extends DJIRCAbstraction {
    /* access modifiers changed from: private */
    public int counter = 0;
    /* access modifiers changed from: private */
    public GPSData gpsData = new GPSData.Builder().location(new GPSData.GPSLocation(-122.137387d, 37.421975d)).build();

    static /* synthetic */ int access$008(MockRemoteControllerAbstraction x0) {
        int i = x0.counter;
        x0.counter = i + 1;
        return i;
    }

    public MockRemoteControllerAbstraction() {
        generateFakeGPSData();
    }

    private void generateFakeGPSData() {
        Observable.timer(1000, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockRemoteControllerAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockRemoteControllerAbstraction.access$008(MockRemoteControllerAbstraction.this);
                MockRemoteControllerAbstraction.this.notifyValueChangeForKeyPath(MockRemoteControllerAbstraction.this.gpsData, RemoteControllerKeys.GPS_DATA);
                MockRemoteControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(AircraftMappingStyle.values()[MockRemoteControllerAbstraction.this.counter % (AircraftMappingStyle.values().length - 1)], RemoteControllerKeys.AIRCRAFT_MAPPING_STYLE);
                MockRemoteControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(new ChargeRemaining(10000, MockRemoteControllerAbstraction.this.counter % 101), "ChargeRemaining");
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }
}
