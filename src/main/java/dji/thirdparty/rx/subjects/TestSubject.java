package dji.thirdparty.rx.subjects;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.internal.operators.NotificationLite;
import dji.thirdparty.rx.schedulers.TestScheduler;
import dji.thirdparty.rx.subjects.SubjectSubscriptionManager;
import java.util.concurrent.TimeUnit;

public final class TestSubject<T> extends Subject<T, T> {
    private final Scheduler.Worker innerScheduler;
    private final SubjectSubscriptionManager<T> state;

    public static <T> TestSubject<T> create(TestScheduler scheduler) {
        final SubjectSubscriptionManager<T> state2 = new SubjectSubscriptionManager<>();
        state2.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            /* class dji.thirdparty.rx.subjects.TestSubject.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((SubjectSubscriptionManager.SubjectObserver) ((SubjectSubscriptionManager.SubjectObserver) x0));
            }

            public void call(SubjectSubscriptionManager.SubjectObserver<T> o) {
                o.emitFirst(state2.getLatest(), state2.nl);
            }
        };
        state2.onTerminated = state2.onAdded;
        return new TestSubject<>(state2, state2, scheduler);
    }

    protected TestSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state2, TestScheduler scheduler) {
        super(onSubscribe);
        this.state = state2;
        this.innerScheduler = scheduler.createWorker();
    }

    public void onCompleted() {
        onCompleted(0);
    }

    /* access modifiers changed from: package-private */
    public void _onCompleted() {
        if (this.state.active) {
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : this.state.terminate(NotificationLite.instance().completed())) {
                bo.onCompleted();
            }
        }
    }

    public void onCompleted(long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            /* class dji.thirdparty.rx.subjects.TestSubject.AnonymousClass2 */

            public void call() {
                TestSubject.this._onCompleted();
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public void onError(Throwable e) {
        onError(e, 0);
    }

    /* access modifiers changed from: package-private */
    public void _onError(Throwable e) {
        if (this.state.active) {
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : this.state.terminate(NotificationLite.instance().error(e))) {
                bo.onError(e);
            }
        }
    }

    public void onError(final Throwable e, long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            /* class dji.thirdparty.rx.subjects.TestSubject.AnonymousClass3 */

            public void call() {
                TestSubject.this._onError(e);
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public void onNext(T v) {
        onNext(v, 0);
    }

    /* access modifiers changed from: package-private */
    public void _onNext(T v) {
        for (Observer<? super T> o : this.state.observers()) {
            o.onNext(v);
        }
    }

    public void onNext(final T v, long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            /* class dji.thirdparty.rx.subjects.TestSubject.AnonymousClass4 */

            public void call() {
                TestSubject.this._onNext(v);
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }
}
