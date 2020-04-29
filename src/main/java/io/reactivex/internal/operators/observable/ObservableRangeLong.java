package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.observers.BasicIntQueueDisposable;

public final class ObservableRangeLong extends Observable<Long> {
    private final long count;
    private final long start;

    public ObservableRangeLong(long start2, long count2) {
        this.start = start2;
        this.count = count2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Long> o) {
        RangeDisposable parent = new RangeDisposable(o, this.start, this.start + this.count);
        o.onSubscribe(parent);
        parent.run();
    }

    static final class RangeDisposable extends BasicIntQueueDisposable<Long> {
        private static final long serialVersionUID = 396518478098735504L;
        final Observer<? super Long> downstream;
        final long end;
        boolean fused;
        long index;

        RangeDisposable(Observer<? super Long> actual, long start, long end2) {
            this.downstream = actual;
            this.index = start;
            this.end = end2;
        }

        /* access modifiers changed from: package-private */
        public void run() {
            if (!this.fused) {
                Observer<? super Long> actual = this.downstream;
                long e = this.end;
                for (long i = this.index; i != e && get() == 0; i++) {
                    actual.onNext(Long.valueOf(i));
                }
                if (get() == 0) {
                    lazySet(1);
                    actual.onComplete();
                }
            }
        }

        @Nullable
        public Long poll() throws Exception {
            long i = this.index;
            if (i != this.end) {
                this.index = 1 + i;
                return Long.valueOf(i);
            }
            lazySet(1);
            return null;
        }

        public boolean isEmpty() {
            return this.index == this.end;
        }

        public void clear() {
            this.index = this.end;
            lazySet(1);
        }

        public void dispose() {
            set(1);
        }

        public boolean isDisposed() {
            return get() != 0;
        }

        public int requestFusion(int mode) {
            if ((mode & 1) == 0) {
                return 0;
            }
            this.fused = true;
            return 1;
        }
    }
}
