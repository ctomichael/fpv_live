package com.dji.rx.sharedlib;

import android.support.annotation.NonNull;
import dji.common.error.DJIError;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.atomic.AtomicBoolean;

public class SharedLibGetObservable<T> extends Observable<T> {
    private final boolean isDynamic;
    private final MockableCacheHelper mCacheHelper;
    private final T mDefaultValue;
    private final Consumer<SharedLibException> mErrorConsumer;
    private final DJISDKCacheKey mKey;

    public SharedLibGetObservable(@NonNull DJISDKCacheKey key, @NonNull Consumer<SharedLibException> errorConsumer) {
        this.mCacheHelper = new MockableCacheHelper();
        this.isDynamic = false;
        this.mKey = key;
        this.mErrorConsumer = errorConsumer;
        this.mDefaultValue = null;
    }

    public SharedLibGetObservable(@NonNull DJISDKCacheKey key, @NonNull Consumer<SharedLibException> errorConsumer, T defaultValue) {
        this.mCacheHelper = new MockableCacheHelper();
        this.isDynamic = false;
        this.mKey = key;
        this.mErrorConsumer = errorConsumer;
        this.mDefaultValue = defaultValue;
    }

    public SharedLibGetObservable(@NonNull DJISDKCacheKey key, @NonNull Consumer<SharedLibException> errorConsumer, T defaultValue, boolean dynamic) {
        this.mCacheHelper = new MockableCacheHelper();
        this.isDynamic = dynamic;
        this.mKey = key;
        this.mErrorConsumer = errorConsumer;
        this.mDefaultValue = defaultValue;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        Listener listener = new Listener(this.mKey, this.mCacheHelper, observer, this.mErrorConsumer);
        observer.onSubscribe(listener);
        if (this.isDynamic) {
            if (this.mDefaultValue != null) {
                observer.onNext(this.mDefaultValue);
            }
            this.mCacheHelper.get(this.mKey, listener);
            return;
        }
        T value = this.mCacheHelper.getValue(this.mKey);
        if (value == null) {
            if (this.mDefaultValue != null) {
                observer.onNext(this.mDefaultValue);
            }
            this.mCacheHelper.get(this.mKey, listener);
            return;
        }
        observer.onNext(value);
        observer.onComplete();
    }

    static final class Listener<T> implements Disposable, DJIGetCallback {
        private final MockableCacheHelper mCacheHelper;
        private final Consumer<SharedLibException> mErrorConsumer;
        private final DJISDKCacheKey mKey;
        private final Observer<? super T> mObserver;
        private final AtomicBoolean mUnsubscribed = new AtomicBoolean();

        Listener(DJISDKCacheKey key, MockableCacheHelper cacheHelper, Observer<? super T> observer, Consumer<SharedLibException> errorConsumer) {
            this.mKey = key;
            this.mCacheHelper = cacheHelper;
            this.mObserver = observer;
            this.mErrorConsumer = errorConsumer;
        }

        public void onSuccess(DJISDKCacheParamValue value) {
            if (!isDisposed()) {
                this.mObserver.onNext(value.getData());
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
