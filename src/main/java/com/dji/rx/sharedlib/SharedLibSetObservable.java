package com.dji.rx.sharedlib;

import android.support.annotation.NonNull;
import dji.common.error.DJIError;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJISetCallback;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.atomic.AtomicBoolean;

public class SharedLibSetObservable<T> extends Observable<T> {
    private final MockableCacheHelper mCacheHelper = new MockableCacheHelper();
    private final Consumer<SharedLibException> mErrorConsumer;
    private final DJISDKCacheKey mKey;
    private final T mValue;

    public SharedLibSetObservable(@NonNull DJISDKCacheKey key, @NonNull T value, @NonNull Consumer<SharedLibException> errorConsumer) {
        this.mKey = key;
        this.mErrorConsumer = errorConsumer;
        this.mValue = value;
    }

    public SharedLibSetObservable(@NonNull DJISDKCacheKey key, @NonNull T value) {
        this.mKey = key;
        this.mErrorConsumer = SharedLibSetObservable$$Lambda$0.$instance;
        this.mValue = value;
    }

    static final /* synthetic */ void lambda$new$0$SharedLibSetObservable(SharedLibException o) throws Exception {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        Listener listener = new Listener(this.mKey, this.mCacheHelper, observer, this.mValue, this.mErrorConsumer);
        observer.onSubscribe(listener);
        this.mCacheHelper.setValue(this.mKey, this.mValue, listener);
    }

    static final class Listener<U> implements Disposable, DJISetCallback {
        private final DJISDKCacheKey key;
        private final MockableCacheHelper mCacheHelper;
        private final Consumer<SharedLibException> mErrorConsumer;
        private final Observer<? super U> mObserver;
        private final AtomicBoolean mUnsubscribed = new AtomicBoolean();
        private final U mValue;

        public Listener(DJISDKCacheKey key2, MockableCacheHelper cacheHelper, Observer<? super U> observer, U value, Consumer<SharedLibException> errorConsumer) {
            this.key = key2;
            this.mCacheHelper = cacheHelper;
            this.mObserver = observer;
            this.mValue = value;
            this.mErrorConsumer = errorConsumer;
        }

        public void onSuccess() {
            if (!isDisposed()) {
                this.mObserver.onNext(this.mValue);
                this.mObserver.onComplete();
            }
        }

        public void onFails(DJIError error) {
            try {
                this.mErrorConsumer.accept(new SharedLibException(error));
                this.mObserver.onComplete();
            } catch (Exception e) {
            }
        }

        public void dispose() {
            this.mUnsubscribed.compareAndSet(false, true);
        }

        public boolean isDisposed() {
            return this.mUnsubscribed.get();
        }
    }
}
