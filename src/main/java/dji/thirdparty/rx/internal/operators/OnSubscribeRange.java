package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OnSubscribeRange implements Observable.OnSubscribe<Integer> {
    private final int endIndex;
    private final int startIndex;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber<? super Integer>) ((Subscriber) x0));
    }

    public OnSubscribeRange(int start, int end) {
        this.startIndex = start;
        this.endIndex = end;
    }

    public void call(Subscriber<? super Integer> childSubscriber) {
        childSubscriber.setProducer(new RangeProducer(childSubscriber, this.startIndex, this.endIndex));
    }

    private static final class RangeProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = 4114392207069098388L;
        private final Subscriber<? super Integer> childSubscriber;
        private long currentIndex;
        private final int endOfRange;

        RangeProducer(Subscriber<? super Integer> childSubscriber2, int startIndex, int endIndex) {
            this.childSubscriber = childSubscriber2;
            this.currentIndex = (long) startIndex;
            this.endOfRange = endIndex;
        }

        public void request(long requestedAmount) {
            if (get() != LongCompanionObject.MAX_VALUE) {
                if (requestedAmount == LongCompanionObject.MAX_VALUE && compareAndSet(0, LongCompanionObject.MAX_VALUE)) {
                    fastpath();
                } else if (requestedAmount > 0 && BackpressureUtils.getAndAddRequest(this, requestedAmount) == 0) {
                    slowpath(requestedAmount);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void slowpath(long requestedAmount) {
            long emitted = 0;
            long endIndex = ((long) this.endOfRange) + 1;
            long index = this.currentIndex;
            Subscriber<? super Integer> childSubscriber2 = this.childSubscriber;
            while (true) {
                if (emitted == requestedAmount || index == endIndex) {
                    if (childSubscriber2.isUnsubscribed()) {
                        return;
                    }
                    if (index == endIndex) {
                        childSubscriber2.onCompleted();
                        return;
                    }
                    requestedAmount = get();
                    if (requestedAmount == emitted) {
                        this.currentIndex = index;
                        requestedAmount = addAndGet(-emitted);
                        if (requestedAmount != 0) {
                            emitted = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!childSubscriber2.isUnsubscribed()) {
                    childSubscriber2.onNext(Integer.valueOf((int) index));
                    index++;
                    emitted++;
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void fastpath() {
            long endIndex = ((long) this.endOfRange) + 1;
            Subscriber<? super Integer> childSubscriber2 = this.childSubscriber;
            long index = this.currentIndex;
            while (index != endIndex) {
                if (!childSubscriber2.isUnsubscribed()) {
                    childSubscriber2.onNext(Integer.valueOf((int) index));
                    index++;
                } else {
                    return;
                }
            }
            if (!childSubscriber2.isUnsubscribed()) {
                childSubscriber2.onCompleted();
            }
        }
    }
}
