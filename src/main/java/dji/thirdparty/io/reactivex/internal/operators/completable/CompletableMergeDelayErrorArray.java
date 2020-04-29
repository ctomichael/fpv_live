package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.util.AtomicThrowable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableMergeDelayErrorArray extends Completable {
    final CompletableSource[] sources;

    public CompletableMergeDelayErrorArray(CompletableSource[] sources2) {
        this.sources = sources2;
    }

    public void subscribeActual(CompletableObserver s) {
        CompositeDisposable set = new CompositeDisposable();
        AtomicInteger wip = new AtomicInteger(this.sources.length + 1);
        AtomicThrowable error = new AtomicThrowable();
        s.onSubscribe(set);
        CompletableSource[] arr$ = this.sources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            CompletableSource c = arr$[i$];
            if (!set.isDisposed()) {
                if (c == null) {
                    error.addThrowable(new NullPointerException("A completable source is null"));
                    wip.decrementAndGet();
                } else {
                    c.subscribe(new MergeInnerCompletableObserver(s, set, error, wip));
                }
                i$++;
            } else {
                return;
            }
        }
        if (wip.decrementAndGet() == 0) {
            Throwable ex = error.terminate();
            if (ex == null) {
                s.onComplete();
            } else {
                s.onError(ex);
            }
        }
    }

    static final class MergeInnerCompletableObserver implements CompletableObserver {
        final CompletableObserver actual;
        final AtomicThrowable error;
        final CompositeDisposable set;
        final AtomicInteger wip;

        MergeInnerCompletableObserver(CompletableObserver s, CompositeDisposable set2, AtomicThrowable error2, AtomicInteger wip2) {
            this.actual = s;
            this.set = set2;
            this.error = error2;
            this.wip = wip2;
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onError(Throwable e) {
            if (this.error.addThrowable(e)) {
                tryTerminate();
            } else {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            tryTerminate();
        }

        /* access modifiers changed from: package-private */
        public void tryTerminate() {
            if (this.wip.decrementAndGet() == 0) {
                Throwable ex = this.error.terminate();
                if (ex == null) {
                    this.actual.onComplete();
                } else {
                    this.actual.onError(ex);
                }
            }
        }
    }
}
