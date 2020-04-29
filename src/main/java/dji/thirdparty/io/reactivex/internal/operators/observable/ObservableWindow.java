package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.subjects.UnicastSubject;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableWindow<T> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int capacityHint;
    final long count;
    final long skip;

    public ObservableWindow(ObservableSource<T> source, long count2, long skip2, int capacityHint2) {
        super(source);
        this.count = count2;
        this.skip = skip2;
        this.capacityHint = capacityHint2;
    }

    public void subscribeActual(Observer<? super Observable<T>> t) {
        if (this.count == this.skip) {
            this.source.subscribe(new WindowExactObserver(t, this.count, this.capacityHint));
            return;
        }
        this.source.subscribe(new WindowSkipObserver(t, this.count, this.skip, this.capacityHint));
    }

    static final class WindowExactObserver<T> extends AtomicInteger implements Observer<T>, Disposable, Runnable {
        private static final long serialVersionUID = -7481782523886138128L;
        final Observer<? super Observable<T>> actual;
        volatile boolean cancelled;
        final int capacityHint;
        final long count;
        Disposable s;
        long size;
        UnicastSubject<T> window;

        WindowExactObserver(Observer<? super Observable<T>> actual2, long count2, int capacityHint2) {
            this.actual = actual2;
            this.count = count2;
            this.capacityHint = capacityHint2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            UnicastSubject<T> w = this.window;
            if (w == null && !this.cancelled) {
                w = UnicastSubject.create(this.capacityHint, this);
                this.window = w;
                this.actual.onNext(w);
            }
            if (w != null) {
                w.onNext(t);
                long j = this.size + 1;
                this.size = j;
                if (j >= this.count) {
                    this.size = 0;
                    this.window = null;
                    w.onComplete();
                    if (this.cancelled) {
                        this.s.dispose();
                    }
                }
            }
        }

        public void onError(Throwable t) {
            UnicastSubject<T> w = this.window;
            if (w != null) {
                this.window = null;
                w.onError(t);
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            UnicastSubject<T> w = this.window;
            if (w != null) {
                this.window = null;
                w.onComplete();
            }
            this.actual.onComplete();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void run() {
            if (this.cancelled) {
                this.s.dispose();
            }
        }
    }

    static final class WindowSkipObserver<T> extends AtomicBoolean implements Observer<T>, Disposable, Runnable {
        private static final long serialVersionUID = 3366976432059579510L;
        final Observer<? super Observable<T>> actual;
        volatile boolean cancelled;
        final int capacityHint;
        final long count;
        long firstEmission;
        long index;
        Disposable s;
        final long skip;
        final ArrayDeque<UnicastSubject<T>> windows;
        final AtomicInteger wip = new AtomicInteger();

        WindowSkipObserver(Observer<? super Observable<T>> actual2, long count2, long skip2, int capacityHint2) {
            this.actual = actual2;
            this.count = count2;
            this.skip = skip2;
            this.capacityHint = capacityHint2;
            this.windows = new ArrayDeque<>();
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            ArrayDeque<UnicastSubject<T>> ws = this.windows;
            long i = this.index;
            long s2 = this.skip;
            if (i % s2 == 0 && !this.cancelled) {
                this.wip.getAndIncrement();
                UnicastSubject<T> w = UnicastSubject.create(this.capacityHint, this);
                ws.offer(w);
                this.actual.onNext(w);
            }
            long c = this.firstEmission + 1;
            Iterator i$ = ws.iterator();
            while (i$.hasNext()) {
                i$.next().onNext(t);
            }
            if (c >= this.count) {
                ws.poll().onComplete();
                if (!ws.isEmpty() || !this.cancelled) {
                    this.firstEmission = c - s2;
                } else {
                    this.s.dispose();
                    return;
                }
            } else {
                this.firstEmission = c;
            }
            this.index = 1 + i;
        }

        public void onError(Throwable t) {
            ArrayDeque<UnicastSubject<T>> ws = this.windows;
            while (!ws.isEmpty()) {
                ws.poll().onError(t);
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            ArrayDeque<UnicastSubject<T>> ws = this.windows;
            while (!ws.isEmpty()) {
                ws.poll().onComplete();
            }
            this.actual.onComplete();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void run() {
            if (this.wip.decrementAndGet() == 0 && this.cancelled) {
                this.s.dispose();
            }
        }
    }
}
