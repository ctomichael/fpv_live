package dji.internal.mission.fsm;

import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class FSMTempStateTimer {
    private MissionState currentState;
    private Observable<Long> observable;
    private Subscription subscription;
    private Runnable successOperation;

    public void startTimer(MissionState currentState2, boolean isRunningInMainThread, double timeoutInSeconds, Runnable timeoutAction) {
        startTimer(currentState2, isRunningInMainThread, null, timeoutInSeconds, timeoutAction);
    }

    public void startTimer(MissionState state, boolean isRunningInMainThread, Runnable successOperation2, double timeoutInSeconds, Runnable failureOperation) {
        if (state == null) {
            throw new RuntimeException("FSMTempStateTimer error.");
        }
        this.currentState = state;
        this.successOperation = successOperation2;
        long timeoutInMilliseconds = (long) (1000.0d * timeoutInSeconds);
        if (isRunningInMainThread) {
            this.observable = Observable.timer(timeoutInMilliseconds, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
        } else {
            this.observable = Observable.timer(timeoutInMilliseconds, TimeUnit.MILLISECONDS, Schedulers.computation());
        }
        if (this.subscription != null && !this.subscription.isUnsubscribed()) {
            this.subscription.unsubscribe();
        }
        this.subscription = this.observable.subscribe(new FSMTempStateTimer$$Lambda$0(failureOperation));
    }

    public void notifyStateChange(Object state) {
        if (this.subscription != null && !this.subscription.isUnsubscribed() && !this.currentState.equals(state)) {
            if (this.successOperation != null) {
                this.successOperation.run();
            }
            this.subscription.unsubscribe();
            this.observable = null;
        }
    }
}
