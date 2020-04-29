package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OnSubscribeJoin<TLeft, TRight, TLeftDuration, TRightDuration, R> implements Observable.OnSubscribe<R> {
    final Observable<TLeft> left;
    final Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector;
    final Func2<TLeft, TRight, R> resultSelector;
    final Observable<TRight> right;
    final Func1<TRight, Observable<TRightDuration>> rightDurationSelector;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public OnSubscribeJoin(Observable<TLeft> left2, Observable<TRight> right2, Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector2, Func1<TRight, Observable<TRightDuration>> rightDurationSelector2, Func2<TLeft, TRight, R> resultSelector2) {
        this.left = left2;
        this.right = right2;
        this.leftDurationSelector = leftDurationSelector2;
        this.rightDurationSelector = rightDurationSelector2;
        this.resultSelector = resultSelector2;
    }

    public void call(Subscriber<? super R> t1) {
        new ResultSink(new SerializedSubscriber(t1)).run();
    }

    final class ResultSink {
        final CompositeSubscription group;
        final Object guard = new Object();
        boolean leftDone;
        int leftId;
        final Map<Integer, TLeft> leftMap;
        boolean rightDone;
        int rightId;
        final Map<Integer, TRight> rightMap;
        final Subscriber<? super R> subscriber;

        public ResultSink(Subscriber<? super R> subscriber2) {
            this.subscriber = subscriber2;
            this.group = new CompositeSubscription();
            this.leftMap = new HashMap();
            this.rightMap = new HashMap();
        }

        public void run() {
            this.subscriber.add(this.group);
            Subscriber<TLeft> s1 = new LeftSubscriber();
            Subscriber<TRight> s2 = new RightSubscriber();
            this.group.add(s1);
            this.group.add(s2);
            OnSubscribeJoin.this.left.unsafeSubscribe(s1);
            OnSubscribeJoin.this.right.unsafeSubscribe(s2);
        }

        final class LeftSubscriber extends Subscriber<TLeft> {
            LeftSubscriber() {
            }

            /* access modifiers changed from: protected */
            public void expire(int id, Subscription resource) {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    if (ResultSink.this.leftMap.remove(Integer.valueOf(id)) != null && ResultSink.this.leftMap.isEmpty() && ResultSink.this.leftDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(resource);
            }

            public void onNext(TLeft args) {
                int id;
                int highRightId;
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    id = resultSink.leftId;
                    resultSink.leftId = id + 1;
                    ResultSink.this.leftMap.put(Integer.valueOf(id), args);
                    highRightId = ResultSink.this.rightId;
                }
                try {
                    Subscriber<TLeftDuration> d1 = new LeftDurationSubscriber(id);
                    ResultSink.this.group.add(d1);
                    OnSubscribeJoin.this.leftDurationSelector.call(args).unsafeSubscribe(d1);
                    List<TRight> rightValues = new ArrayList<>();
                    synchronized (ResultSink.this.guard) {
                        for (Map.Entry<Integer, TRight> entry : ResultSink.this.rightMap.entrySet()) {
                            if (entry.getKey().intValue() < highRightId) {
                                rightValues.add(entry.getValue());
                            }
                        }
                    }
                    for (TRight r : rightValues) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(args, r));
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onError(Throwable e) {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    ResultSink.this.leftDone = true;
                    if (ResultSink.this.rightDone || ResultSink.this.leftMap.isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }

            final class LeftDurationSubscriber extends Subscriber<TLeftDuration> {
                final int id;
                boolean once = true;

                public LeftDurationSubscriber(int id2) {
                    this.id = id2;
                }

                public void onNext(TLeftDuration tleftduration) {
                    onCompleted();
                }

                public void onError(Throwable e) {
                    LeftSubscriber.this.onError(e);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        LeftSubscriber.this.expire(this.id, this);
                    }
                }
            }
        }

        final class RightSubscriber extends Subscriber<TRight> {
            RightSubscriber() {
            }

            /* access modifiers changed from: package-private */
            public void expire(int id, Subscription resource) {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    if (ResultSink.this.rightMap.remove(Integer.valueOf(id)) != null && ResultSink.this.rightMap.isEmpty() && ResultSink.this.rightDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(resource);
            }

            public void onNext(TRight args) {
                int id;
                int highLeftId;
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    id = resultSink.rightId;
                    resultSink.rightId = id + 1;
                    ResultSink.this.rightMap.put(Integer.valueOf(id), args);
                    highLeftId = ResultSink.this.leftId;
                }
                ResultSink.this.group.add(new SerialSubscription());
                try {
                    Subscriber<TRightDuration> d2 = new RightDurationSubscriber(id);
                    ResultSink.this.group.add(d2);
                    OnSubscribeJoin.this.rightDurationSelector.call(args).unsafeSubscribe(d2);
                    List<TLeft> leftValues = new ArrayList<>();
                    synchronized (ResultSink.this.guard) {
                        for (Map.Entry<Integer, TLeft> entry : ResultSink.this.leftMap.entrySet()) {
                            if (entry.getKey().intValue() < highLeftId) {
                                leftValues.add(entry.getValue());
                            }
                        }
                    }
                    for (TLeft lv : leftValues) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(lv, args));
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onError(Throwable e) {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    ResultSink.this.rightDone = true;
                    if (ResultSink.this.leftDone || ResultSink.this.rightMap.isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }

            final class RightDurationSubscriber extends Subscriber<TRightDuration> {
                final int id;
                boolean once = true;

                public RightDurationSubscriber(int id2) {
                    this.id = id2;
                }

                public void onNext(TRightDuration trightduration) {
                    onCompleted();
                }

                public void onError(Throwable e) {
                    RightSubscriber.this.onError(e);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        RightSubscriber.this.expire(this.id, this);
                    }
                }
            }
        }
    }
}
