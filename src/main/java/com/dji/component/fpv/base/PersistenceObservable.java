package com.dji.component.fpv.base;

import com.dji.component.persistence.DJIPersistenceDataListener;
import com.dji.component.persistence.DJIPersistenceStorage;
import dji.utils.Optional;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PersistenceObservable extends Observable<Optional<Object>> {
    private final Class dataType;
    private Object defaultValue;
    private final String key;

    public PersistenceObservable(String key2, Class dataType2) {
        this.key = key2;
        this.dataType = dataType2;
    }

    public PersistenceObservable(String key2, Class dataType2, Object defaultValue2) {
        this.key = key2;
        this.dataType = dataType2;
        this.defaultValue = defaultValue2;
    }

    public Object getValue(Object defaultValue2) {
        return getValue(this.key, this.dataType, defaultValue2);
    }

    public Object getValue() {
        return getValue(this.defaultValue);
    }

    /* access modifiers changed from: private */
    public static Object getValue(String key2, Class dataType2, Object defaultValue2) {
        if (defaultValue2 != null && defaultValue2.getClass() != dataType2) {
            throw new RuntimeException("the type of default value is not equals to data type.");
        } else if (!DJIPersistenceStorage.containKey(key2)) {
            return defaultValue2;
        } else {
            if (dataType2 == Integer.class) {
                if (defaultValue2 == null) {
                    return Integer.valueOf(DJIPersistenceStorage.getInt(key2));
                }
                return Integer.valueOf(DJIPersistenceStorage.getInt(key2, ((Integer) defaultValue2).intValue()));
            } else if (dataType2 == Float.class) {
                if (defaultValue2 == null) {
                    return Float.valueOf(DJIPersistenceStorage.getFloat(key2));
                }
                return Float.valueOf(DJIPersistenceStorage.getFloat(key2, ((Float) defaultValue2).floatValue()));
            } else if (dataType2 == Double.class) {
                if (defaultValue2 == null) {
                    return Double.valueOf(DJIPersistenceStorage.getDouble(key2));
                }
                return Double.valueOf(DJIPersistenceStorage.getDouble(key2, ((Double) defaultValue2).doubleValue()));
            } else if (dataType2 == String.class) {
                if (defaultValue2 == null) {
                    return DJIPersistenceStorage.getString(key2);
                }
                return DJIPersistenceStorage.getString(key2, (String) defaultValue2);
            } else if (dataType2 == Boolean.class) {
                if (defaultValue2 == null) {
                    return Boolean.valueOf(DJIPersistenceStorage.getBoolean(key2));
                }
                return Boolean.valueOf(DJIPersistenceStorage.getBoolean(key2, ((Boolean) defaultValue2).booleanValue()));
            } else if (dataType2 != Long.class) {
                throw new RuntimeException("not supported type");
            } else if (defaultValue2 == null) {
                return Long.valueOf(DJIPersistenceStorage.getLong(key2));
            } else {
                return Long.valueOf(DJIPersistenceStorage.getLong(key2, ((Long) defaultValue2).longValue()));
            }
        }
    }

    public String getKey() {
        return this.key;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Optional<Object>> observer) {
        Listener listener = new Listener(this.key, observer, this.defaultValue, this.dataType);
        observer.onSubscribe(listener);
        observer.onNext(Optional.ofNullable(getValue()));
        DJIPersistenceStorage.addListener(listener, this.key);
    }

    private static final class Listener implements Disposable, DJIPersistenceDataListener {
        private final Class dataType;
        private final Object defaultValue;
        private final String key;
        private final Observer<? super Optional<Object>> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        Listener(String key2, Observer<? super Optional<Object>> observer2, Object defaultValue2, Class dataType2) {
            this.key = key2;
            this.observer = observer2;
            this.defaultValue = defaultValue2;
            this.dataType = dataType2;
        }

        public void dispose() {
            if (this.unsubscribed.compareAndSet(false, true)) {
                DJIPersistenceStorage.removeListener(this);
            }
        }

        public boolean isDisposed() {
            return this.unsubscribed.get();
        }

        public void onValueUpdate(String id, String key2) {
            if (!isDisposed() && key2.equals(this.key)) {
                this.observer.onNext(Optional.ofNullable(PersistenceObservable.getValue(key2, this.dataType, this.defaultValue)));
            }
        }
    }
}
