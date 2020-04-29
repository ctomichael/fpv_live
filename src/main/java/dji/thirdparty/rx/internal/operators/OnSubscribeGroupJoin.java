package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.observers.SerializedObserver;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.subjects.PublishSubject;
import dji.thirdparty.rx.subjects.Subject;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import dji.thirdparty.rx.subscriptions.RefCountSubscription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OnSubscribeGroupJoin<T1, T2, D1, D2, R> implements Observable.OnSubscribe<R> {
    protected final Observable<T1> left;
    protected final Func1<? super T1, ? extends Observable<D1>> leftDuration;
    protected final Func2<? super T1, ? super Observable<T2>, ? extends R> resultSelector;
    protected final Observable<T2> right;
    protected final Func1<? super T2, ? extends Observable<D2>> rightDuration;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OnSubscribeGroupJoin(Observable<T1> left2, Observable<T2> right2, Func1<? super T1, ? extends Observable<D1>> leftDuration2, Func1<? super T2, ? extends Observable<D2>> rightDuration2, Func2<? super T1, ? super Observable<T2>, ? extends R> resultSelector2) {
        this.left = left2;
        this.right = right2;
        this.leftDuration = leftDuration2;
        this.rightDuration = rightDuration2;
        this.resultSelector = resultSelector2;
    }

    public void call(Subscriber<? super R> child) {
        OnSubscribeGroupJoin<T1, T2, D1, D2, R>.ResultManager ro = new ResultManager(new SerializedSubscriber(child));
        child.add(ro);
        ro.init();
    }

    final class ResultManager implements Subscription {
        final RefCountSubscription cancel;
        final CompositeSubscription group;
        final Object guard = new Object();
        boolean leftDone;
        int leftIds;
        final Map<Integer, Observer<T2>> leftMap = new HashMap();
        boolean rightDone;
        int rightIds;
        final Map<Integer, T2> rightMap = new HashMap();
        final Subscriber<? super R> subscriber;

        public ResultManager(Subscriber<? super R> subscriber2) {
            this.subscriber = subscriber2;
            this.group = new CompositeSubscription();
            this.cancel = new RefCountSubscription(this.group);
        }

        public void init() {
            Subscriber<T1> s1 = new LeftObserver();
            Subscriber<T2> s2 = new RightObserver();
            this.group.add(s1);
            this.group.add(s2);
            OnSubscribeGroupJoin.this.left.unsafeSubscribe(s1);
            OnSubscribeGroupJoin.this.right.unsafeSubscribe(s2);
        }

        public void unsubscribe() {
            this.cancel.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.cancel.isUnsubscribed();
        }

        /* access modifiers changed from: package-private */
        public void errorAll(Throwable e) {
            List<Observer<T2>> list;
            synchronized (this.guard) {
                list = new ArrayList<>(this.leftMap.values());
                this.leftMap.clear();
                this.rightMap.clear();
            }
            for (Observer<T2> o : list) {
                o.onError(e);
            }
            this.subscriber.onError(e);
            this.cancel.unsubscribe();
        }

        /* access modifiers changed from: package-private */
        public void errorMain(Throwable e) {
            synchronized (this.guard) {
                this.leftMap.clear();
                this.rightMap.clear();
            }
            this.subscriber.onError(e);
            this.cancel.unsubscribe();
        }

        /* access modifiers changed from: package-private */
        public void complete(List<Observer<T2>> list) {
            if (list != null) {
                for (Observer<T2> o : list) {
                    o.onCompleted();
                }
                this.subscriber.onCompleted();
                this.cancel.unsubscribe();
            }
        }

        final class LeftObserver extends Subscriber<T1> {
            LeftObserver() {
            }

            public void onNext(T1 args) {
                int id;
                List<T2> rightMapValues;
                try {
                    Subject<T2, T2> subj = PublishSubject.create();
                    Observer<T2> subjSerial = new SerializedObserver<>(subj);
                    synchronized (ResultManager.this.guard) {
                        ResultManager resultManager = ResultManager.this;
                        id = resultManager.leftIds;
                        resultManager.leftIds = id + 1;
                        ResultManager.this.leftMap.put(Integer.valueOf(id), subjSerial);
                    }
                    Observable<T2> window = Observable.create(new WindowObservableFunc(subj, ResultManager.this.cancel));
                    Subscriber<D1> d1 = new LeftDurationObserver(id);
                    ResultManager.this.group.add(d1);
                    ((Observable) OnSubscribeGroupJoin.this.leftDuration.call(args)).unsafeSubscribe(d1);
                    R result = OnSubscribeGroupJoin.this.resultSelector.call(args, window);
                    synchronized (ResultManager.this.guard) {
                        rightMapValues = new ArrayList<>(ResultManager.this.rightMap.values());
                    }
                    ResultManager.this.subscriber.onNext(result);
                    for (T2 t2 : rightMapValues) {
                        subjSerial.onNext(t2);
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onCompleted() {
                List<Observer<T2>> list = null;
                synchronized (ResultManager.this.guard) {
                    ResultManager.this.leftDone = true;
                    if (ResultManager.this.rightDone) {
                        List<Observer<T2>> list2 = new ArrayList<>(ResultManager.this.leftMap.values());
                        try {
                            ResultManager.this.leftMap.clear();
                            ResultManager.this.rightMap.clear();
                            list = list2;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    try {
                        ResultManager.this.complete(list);
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorAll(e);
            }
        }

        final class RightObserver extends Subscriber<T2> {
            RightObserver() {
            }

            public void onNext(T2 args) {
                int id;
                List<Observer<T2>> list;
                try {
                    synchronized (ResultManager.this.guard) {
                        ResultManager resultManager = ResultManager.this;
                        id = resultManager.rightIds;
                        resultManager.rightIds = id + 1;
                        ResultManager.this.rightMap.put(Integer.valueOf(id), args);
                    }
                    Subscriber<D2> d2 = new RightDurationObserver(id);
                    ResultManager.this.group.add(d2);
                    ((Observable) OnSubscribeGroupJoin.this.rightDuration.call(args)).unsafeSubscribe(d2);
                    synchronized (ResultManager.this.guard) {
                        list = new ArrayList<>(ResultManager.this.leftMap.values());
                    }
                    for (Observer<T2> o : list) {
                        o.onNext(args);
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onCompleted() {
                List<Observer<T2>> list = null;
                synchronized (ResultManager.this.guard) {
                    ResultManager.this.rightDone = true;
                    if (ResultManager.this.leftDone) {
                        List<Observer<T2>> list2 = new ArrayList<>(ResultManager.this.leftMap.values());
                        try {
                            ResultManager.this.leftMap.clear();
                            ResultManager.this.rightMap.clear();
                            list = list2;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    try {
                        ResultManager.this.complete(list);
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorAll(e);
            }
        }

        final class LeftDurationObserver extends Subscriber<D1> {
            final int id;
            boolean once = true;

            public LeftDurationObserver(int id2) {
                this.id = id2;
            }

            public void onCompleted() {
                Observer<T2> gr;
                if (this.once) {
                    this.once = false;
                    synchronized (ResultManager.this.guard) {
                        gr = ResultManager.this.leftMap.remove(Integer.valueOf(this.id));
                    }
                    if (gr != null) {
                        gr.onCompleted();
                    }
                    ResultManager.this.group.remove(this);
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorMain(e);
            }

            public void onNext(D1 d1) {
                onCompleted();
            }
        }

        final class RightDurationObserver extends Subscriber<D2> {
            final int id;
            boolean once = true;

            public RightDurationObserver(int id2) {
                this.id = id2;
            }

            public void onCompleted() {
                if (this.once) {
                    this.once = false;
                    synchronized (ResultManager.this.guard) {
                        ResultManager.this.rightMap.remove(Integer.valueOf(this.id));
                    }
                    ResultManager.this.group.remove(this);
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorMain(e);
            }

            public void onNext(D2 d2) {
                onCompleted();
            }
        }
    }

    static final class WindowObservableFunc<T> implements Observable.OnSubscribe<T> {
        final RefCountSubscription refCount;
        final Observable<T> underlying;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            call((Subscriber) ((Subscriber) x0));
        }

        public WindowObservableFunc(Observable<T> underlying2, RefCountSubscription refCount2) {
            this.refCount = refCount2;
            this.underlying = underlying2;
        }

        public void call(Subscriber<? super T> t1) {
            Subscription ref = this.refCount.get();
            WindowObservableFunc<T>.WindowSubscriber wo = new WindowSubscriber(t1, ref);
            wo.add(ref);
            this.underlying.unsafeSubscribe(wo);
        }

        final class WindowSubscriber extends Subscriber<T> {
            private final Subscription ref;
            final Subscriber<? super T> subscriber;

            public WindowSubscriber(Subscriber<? super T> subscriber2, Subscription ref2) {
                super(subscriber2);
                this.subscriber = subscriber2;
                this.ref = ref2;
            }

            public void onNext(T args) {
                this.subscriber.onNext(args);
            }

            public void onError(Throwable e) {
                this.subscriber.onError(e);
                this.ref.unsubscribe();
            }

            public void onCompleted() {
                this.subscriber.onCompleted();
                this.ref.unsubscribe();
            }
        }
    }
}
