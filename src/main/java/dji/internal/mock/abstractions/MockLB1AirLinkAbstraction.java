package dji.internal.mock.abstractions;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockLB1AirLinkAbstraction extends Lightbridge1Abstraction {
    /* access modifiers changed from: private */
    public boolean goingUp = true;
    /* access modifiers changed from: private */
    public int videoSignalPercentage = 0;

    static /* synthetic */ int access$008(MockLB1AirLinkAbstraction x0) {
        int i = x0.videoSignalPercentage;
        x0.videoSignalPercentage = i + 1;
        return i;
    }

    static /* synthetic */ int access$010(MockLB1AirLinkAbstraction x0) {
        int i = x0.videoSignalPercentage;
        x0.videoSignalPercentage = i - 1;
        return i;
    }

    public MockLB1AirLinkAbstraction() {
        generateFakeVideoSignalPercentage();
    }

    private void generateFakeVideoSignalPercentage() {
        Observable.timer(100, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockLB1AirLinkAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockLB1AirLinkAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockLB1AirLinkAbstraction.this.videoSignalPercentage), AirLinkKeys.DOWNLINK_SIGNAL_QUALITY);
                MockLB1AirLinkAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockLB1AirLinkAbstraction.this.videoSignalPercentage / 2), AirLinkKeys.UPLINK_SIGNAL_QUALITY);
                if (MockLB1AirLinkAbstraction.this.goingUp) {
                    MockLB1AirLinkAbstraction.access$008(MockLB1AirLinkAbstraction.this);
                } else {
                    MockLB1AirLinkAbstraction.access$010(MockLB1AirLinkAbstraction.this);
                }
                if (MockLB1AirLinkAbstraction.this.goingUp && MockLB1AirLinkAbstraction.this.videoSignalPercentage >= 100) {
                    boolean unused = MockLB1AirLinkAbstraction.this.goingUp = false;
                } else if (!MockLB1AirLinkAbstraction.this.goingUp && MockLB1AirLinkAbstraction.this.videoSignalPercentage <= 0) {
                    boolean unused2 = MockLB1AirLinkAbstraction.this.goingUp = true;
                }
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }
}
