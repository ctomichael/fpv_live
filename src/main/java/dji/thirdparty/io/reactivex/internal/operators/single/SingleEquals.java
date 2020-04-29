package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class SingleEquals<T> extends Single<Boolean> {
    final SingleSource<? extends T> first;
    final SingleSource<? extends T> second;

    public SingleEquals(SingleSource<? extends T> first2, SingleSource<? extends T> second2) {
        this.first = first2;
        this.second = second2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> s) {
        AtomicInteger count = new AtomicInteger();
        Object[] values = {null, null};
        CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);
        this.first.subscribe(new SingleObserver<T>(0, set, values, count, s) {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleEquals.AnonymousClass1InnerObserver */
            final int index;
            final /* synthetic */ AtomicInteger val$count;
            final /* synthetic */ SingleObserver val$s;
            final /* synthetic */ CompositeDisposable val$set;
            final /* synthetic */ Object[] val$values;

            /* Incorrect method signature, types: dji.thirdparty.io.reactivex.internal.operators.single.SingleEquals, int */
            {
                this.val$set = r3;
                this.val$values = r4;
                this.val$count = r5;
                this.val$s = r6;
                this.index = index;
            }

            public void onSubscribe(Disposable d) {
                this.val$set.add(d);
            }

            public void onSuccess(T value) {
                this.val$values[this.index] = value;
                if (this.val$count.incrementAndGet() == 2) {
                    this.val$s.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.val$values[0], this.val$values[1])));
                }
            }

            public void onError(Throwable e) {
                int state;
                do {
                    state = this.val$count.get();
                    if (state >= 2) {
                        RxJavaPlugins.onError(e);
                        return;
                    }
                } while (!this.val$count.compareAndSet(state, 2));
                this.val$set.dispose();
                this.val$s.onError(e);
            }
        });
        this.second.subscribe(new SingleObserver<T>(1, set, values, count, s) {
            /* class dji.thirdparty.io.reactivex.internal.operators.single.SingleEquals.AnonymousClass1InnerObserver */
            final int index;
            final /* synthetic */ AtomicInteger val$count;
            final /* synthetic */ SingleObserver val$s;
            final /* synthetic */ CompositeDisposable val$set;
            final /* synthetic */ Object[] val$values;

            /* Incorrect method signature, types: dji.thirdparty.io.reactivex.internal.operators.single.SingleEquals, int */
            {
                this.val$set = r3;
                this.val$values = r4;
                this.val$count = r5;
                this.val$s = r6;
                this.index = index;
            }

            public void onSubscribe(Disposable d) {
                this.val$set.add(d);
            }

            public void onSuccess(T value) {
                this.val$values[this.index] = value;
                if (this.val$count.incrementAndGet() == 2) {
                    this.val$s.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.val$values[0], this.val$values[1])));
                }
            }

            public void onError(Throwable e) {
                int state;
                do {
                    state = this.val$count.get();
                    if (state >= 2) {
                        RxJavaPlugins.onError(e);
                        return;
                    }
                } while (!this.val$count.compareAndSet(state, 2));
                this.val$set.dispose();
                this.val$s.onError(e);
            }
        });
    }
}
