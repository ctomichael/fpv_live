package dji.thirdparty.rx.subjects;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.annotations.Beta;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.internal.operators.NotificationLite;
import dji.thirdparty.rx.subjects.SubjectSubscriptionManager;
import java.util.ArrayList;
import java.util.List;

public final class PublishSubject<T> extends Subject<T, T> {
    private final NotificationLite<T> nl = NotificationLite.instance();
    final SubjectSubscriptionManager<T> state;

    public static <T> PublishSubject<T> create() {
        final SubjectSubscriptionManager<T> state2 = new SubjectSubscriptionManager<>();
        state2.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            /* class dji.thirdparty.rx.subjects.PublishSubject.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((SubjectSubscriptionManager.SubjectObserver) ((SubjectSubscriptionManager.SubjectObserver) x0));
            }

            public void call(SubjectSubscriptionManager.SubjectObserver<T> o) {
                o.emitFirst(state2.getLatest(), state2.nl);
            }
        };
        return new PublishSubject<>(state2, state2);
    }

    protected PublishSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state2) {
        super(onSubscribe);
        this.state = state2;
    }

    public void onCompleted() {
        if (this.state.active) {
            Object n = this.nl.completed();
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : this.state.terminate(n)) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    public void onError(Throwable e) {
        if (this.state.active) {
            Object n = this.nl.error(e);
            List<Throwable> errors = null;
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : this.state.terminate(n)) {
                try {
                    bo.emitNext(n, this.state.nl);
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    public void onNext(T v) {
        for (SubjectSubscriptionManager.SubjectObserver<T> bo : this.state.observers()) {
            bo.onNext(v);
        }
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Beta
    public boolean hasThrowable() {
        return this.nl.isError(this.state.getLatest());
    }

    @Beta
    public boolean hasCompleted() {
        Object o = this.state.getLatest();
        return o != null && !this.nl.isError(o);
    }

    @Beta
    public Throwable getThrowable() {
        Object o = this.state.getLatest();
        if (this.nl.isError(o)) {
            return this.nl.getError(o);
        }
        return null;
    }
}
