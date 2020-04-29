package com.dji.rx.sharedlib;

import android.support.annotation.NonNull;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.utils.Optional;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SharedLibPushObservable<T> extends Observable<Optional<T>> {
    private MockableCacheHelper cacheHelper;
    private final T defaultValue;
    private final DJISDKCacheKey key;

    public SharedLibPushObservable(DJISDKCacheKey key2) {
        this.key = key2;
        this.defaultValue = null;
        this.cacheHelper = new MockableCacheHelper();
    }

    public SharedLibPushObservable(DJISDKCacheKey key2, @NonNull T defaultValue2) {
        this.key = key2;
        this.defaultValue = defaultValue2;
        this.cacheHelper = new MockableCacheHelper();
    }

    public SharedLibPushObservable(DJISDKCacheKey key2, @NonNull T defaultValue2, MockableCacheHelper cacheHelper2) {
        this.key = key2;
        this.defaultValue = defaultValue2;
        this.cacheHelper = cacheHelper2;
    }

    public void setCacheHelper(MockableCacheHelper cacheHelper2) {
        this.cacheHelper = cacheHelper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Optional<T>> observer) {
        Listener listener = new Listener(this.key, this.cacheHelper, observer, this.defaultValue);
        observer.onSubscribe(listener);
        T value = this.cacheHelper.getValue(this.key);
        if (value != null) {
            observer.onNext(Optional.of(value));
        } else {
            observer.onNext(Optional.ofNullable(this.defaultValue));
        }
        if (!listener.isDisposed()) {
            this.cacheHelper.addListener(listener, this.key);
        }
    }

    private static final class Listener<U> implements Disposable, DJIParamAccessListener {
        private final MockableCacheHelper cacheHelper;
        private final U defaultValue;
        private final DJISDKCacheKey key;
        private final Observer<? super Optional<U>> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        Listener(DJISDKCacheKey key2, MockableCacheHelper cacheHelper2, Observer<? super Optional<U>> observer2, U defaultValue2) {
            this.key = key2;
            this.cacheHelper = cacheHelper2;
            this.observer = observer2;
            this.defaultValue = defaultValue2;
        }

        public void onValueChange(DJISDKCacheKey key2, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
            if (!isDisposed() && key2.equals(this.key)) {
                U value = this.cacheHelper.getValue(key2);
                if (value != null) {
                    this.observer.onNext(Optional.of(value));
                } else {
                    this.observer.onNext(Optional.ofNullable(this.defaultValue));
                }
            }
        }

        public void dispose() {
            if (this.unsubscribed.compareAndSet(false, true)) {
                this.cacheHelper.removeListener(this);
            }
        }

        public boolean isDisposed() {
            return this.unsubscribed.get();
        }
    }
}
