package dji.internal.mock.abstractions;

import dji.common.airlink.OcuSyncWarningMessage;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockOcuSyncLinkAbstraction extends DJIOcuSyncLinkAbstraction {
    /* access modifiers changed from: private */
    public boolean goingUp = true;
    /* access modifiers changed from: private */
    public int videoSignalPercentage = 0;

    static /* synthetic */ int access$208(MockOcuSyncLinkAbstraction x0) {
        int i = x0.videoSignalPercentage;
        x0.videoSignalPercentage = i + 1;
        return i;
    }

    static /* synthetic */ int access$210(MockOcuSyncLinkAbstraction x0) {
        int i = x0.videoSignalPercentage;
        x0.videoSignalPercentage = i - 1;
        return i;
    }

    public MockOcuSyncLinkAbstraction() {
        generateFakeVideoSignalPercentage();
        if (this.currentWarningMessage == null) {
            this.currentWarningMessage = new ConcurrentHashMap();
        }
        generateFakeWarningMessage();
    }

    private void generateFakeWarningMessage() {
        Observable.timer(1, TimeUnit.SECONDS).repeat().subscribe(new Action1<Long>() {
            /* class dji.internal.mock.abstractions.MockOcuSyncLinkAbstraction.AnonymousClass1 */

            public void call(Long aLong) {
                OcuSyncWarningMessage warningMessage = OcuSyncWarningMessage.values()[(int) (System.currentTimeMillis() % ((long) OcuSyncWarningMessage.values().length))];
                if (MockOcuSyncLinkAbstraction.this.updateWarningMessageAndCheckIfShouldNotify(warningMessage)) {
                    DJILog.d("HAIHAI", "Notifying! " + warningMessage, new Object[0]);
                    MockOcuSyncLinkAbstraction.this.notifyValueChangeForKeyPath(MockOcuSyncLinkAbstraction.this.getCurrentMessages(), OcuSyncLinkKeys.WARNING_MESSAGES);
                    return;
                }
                DJILog.d("HAIHAI", "OLD! " + warningMessage, new Object[0]);
            }
        });
    }

    private void generateFakeVideoSignalPercentage() {
        Observable.timer(100, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockOcuSyncLinkAbstraction.AnonymousClass2 */

            public Observable<Boolean> call(Long aLong) {
                MockOcuSyncLinkAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockOcuSyncLinkAbstraction.this.videoSignalPercentage), AirLinkKeys.DOWNLINK_SIGNAL_QUALITY);
                MockOcuSyncLinkAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockOcuSyncLinkAbstraction.this.videoSignalPercentage / 2), AirLinkKeys.UPLINK_SIGNAL_QUALITY);
                if (MockOcuSyncLinkAbstraction.this.goingUp) {
                    MockOcuSyncLinkAbstraction.access$208(MockOcuSyncLinkAbstraction.this);
                } else {
                    MockOcuSyncLinkAbstraction.access$210(MockOcuSyncLinkAbstraction.this);
                }
                if (MockOcuSyncLinkAbstraction.this.goingUp && MockOcuSyncLinkAbstraction.this.videoSignalPercentage >= 100) {
                    boolean unused = MockOcuSyncLinkAbstraction.this.goingUp = false;
                } else if (!MockOcuSyncLinkAbstraction.this.goingUp && MockOcuSyncLinkAbstraction.this.videoSignalPercentage <= 0) {
                    boolean unused2 = MockOcuSyncLinkAbstraction.this.goingUp = true;
                }
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    public boolean updateWarningMessageAndCheckIfShouldNotify(OcuSyncWarningMessage newMessage) {
        return super.updateWarningMessageAndCheckIfShouldNotify(newMessage);
    }

    public int getTotalWarningMessage() {
        return this.currentWarningMessage.size();
    }

    public boolean isTimerRunning() {
        return this.isTimerRunning;
    }
}
