package dji.internal.mock.abstractions;

import dji.common.bus.LogicEventBus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.GimbalLogic;
import dji.internal.logics.Message;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockGimbalAbstraction extends DJIGimbalAbstraction {
    Integer gimbalHeading = 0;

    public MockGimbalAbstraction() {
        generateFakeGimbalHeading();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataGimbalGetPushParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGimbalGetPushParams.getInstance());
        }
    }

    private void generateFakeGimbalHeading() {
        Observable.timer(1000, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockGimbalAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockGimbalAbstraction.this.notifyValueChangeForKeyPath(MockGimbalAbstraction.this.gimbalHeading, GimbalKeys.YAW_ANGLE_WITH_AIRCRAFT_IN_DEGREE);
                Integer num = MockGimbalAbstraction.this.gimbalHeading;
                MockGimbalAbstraction.this.gimbalHeading = Integer.valueOf(MockGimbalAbstraction.this.gimbalHeading.intValue() + 1);
                if (MockGimbalAbstraction.this.gimbalHeading.intValue() >= 360) {
                    MockGimbalAbstraction.this.gimbalHeading = 0;
                }
                LogicEventBus.getInstance().post(new GimbalLogic.GimbalEvent(new Message(MockGimbalAbstraction.this.gimbalHeading.intValue() % 2 == 0 ? Message.Type.GOOD : Message.Type.ERROR, MockGimbalAbstraction.this.gimbalHeading.intValue() % 2 == 0 ? "Normal" : "Abnormal", "")));
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }
}
