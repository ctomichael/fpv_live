package dji.thirdparty.rx.subjects;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.annotations.Beta;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.internal.operators.NotificationLite;
import dji.thirdparty.rx.subjects.SubjectSubscriptionManager;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public final class BehaviorSubject<T> extends Subject<T, T> {
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private final NotificationLite<T> nl = NotificationLite.instance();
    private final SubjectSubscriptionManager<T> state;

    public static <T> BehaviorSubject<T> create() {
        return create(null, false);
    }

    public static <T> BehaviorSubject<T> create(T defaultValue) {
        return create(defaultValue, true);
    }

    private static <T> BehaviorSubject<T> create(T defaultValue, boolean hasDefault) {
        final SubjectSubscriptionManager<T> state2 = new SubjectSubscriptionManager<>();
        if (hasDefault) {
            state2.setLatest(NotificationLite.instance().next(defaultValue));
        }
        state2.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            /* class dji.thirdparty.rx.subjects.BehaviorSubject.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((SubjectSubscriptionManager.SubjectObserver) ((SubjectSubscriptionManager.SubjectObserver) x0));
            }

            public void call(SubjectSubscriptionManager.SubjectObserver<T> o) {
                o.emitFirst(state2.getLatest(), state2.nl);
            }
        };
        state2.onTerminated = state2.onAdded;
        return new BehaviorSubject<>(state2, state2);
    }

    protected BehaviorSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state2) {
        super(onSubscribe);
        this.state = state2;
    }

    public void onCompleted() {
        if (this.state.getLatest() == null || this.state.active) {
            Object n = this.nl.completed();
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : this.state.terminate(n)) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    public void onError(Throwable e) {
        if (this.state.getLatest() == null || this.state.active) {
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
        if (this.state.getLatest() == null || this.state.active) {
            Object n = this.nl.next(v);
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : this.state.next(n)) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int subscriberCount() {
        return this.state.observers().length;
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Beta
    public boolean hasValue() {
        return this.nl.isNext(this.state.getLatest());
    }

    @Beta
    public boolean hasThrowable() {
        return this.nl.isError(this.state.getLatest());
    }

    @Beta
    public boolean hasCompleted() {
        return this.nl.isCompleted(this.state.getLatest());
    }

    @Beta
    public T getValue() {
        Object o = this.state.getLatest();
        if (this.nl.isNext(o)) {
            return this.nl.getValue(o);
        }
        return null;
    }

    @Beta
    public Throwable getThrowable() {
        Object o = this.state.getLatest();
        if (this.nl.isError(o)) {
            return this.nl.getError(o);
        }
        return null;
    }

    @Beta
    public T[] getValues(T[] a) {
        Object o = this.state.getLatest();
        if (this.nl.isNext(o)) {
            if (a.length == 0) {
                a = (Object[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), 1));
            }
            a[0] = this.nl.getValue(o);
            if (a.length > 1) {
                a[1] = null;
            }
        } else if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }

    @Beta
    public Object[] getValues() {
        T[] r = getValues(EMPTY_ARRAY);
        if (r == EMPTY_ARRAY) {
            return new Object[0];
        }
        return r;
    }
}
