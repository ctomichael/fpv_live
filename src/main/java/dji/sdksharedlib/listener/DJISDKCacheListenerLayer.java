package dji.sdksharedlib.listener;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@EXClassNullAway
public class DJISDKCacheListenerLayer {
    private static final String TAG = "DJISDKCacheListenerLayer";
    private ConcurrentHashMap<DJISDKCacheKey, ConcurrentHashMap<DJIParamAccessListener, Boolean>> keyListenersMap = null;
    private Lock lock = new ReentrantLock();
    private ArrayList<OnChangeListener> onChangeListeners;
    /* access modifiers changed from: private */
    public ReentrantLock recyleLock = new ReentrantLock();
    /* access modifiers changed from: private */
    public List<ValueChangeRunnable> recyleRunnable;

    public interface OnChangeListener {
        void onAdd(DJISDKCacheKey dJISDKCacheKey);

        void onRemove(DJISDKCacheKey dJISDKCacheKey);
    }

    public void addOnChangeListener(OnChangeListener onChangeListener) {
        if (this.onChangeListeners == null) {
            this.onChangeListeners = new ArrayList<>();
        }
        this.onChangeListeners.add(onChangeListener);
    }

    public void init() {
        this.keyListenersMap = new ConcurrentHashMap<>(300);
        this.recyleRunnable = new ArrayList();
    }

    public void destroy() {
        this.keyListenersMap = null;
        this.recyleRunnable = null;
    }

    /* JADX INFO: finally extract failed */
    public boolean startListeningForUpdates(DJISDKCacheKey keyPath, DJIParamAccessListener listener, boolean isRunInMainThread) {
        if (keyPath == null || !keyPath.isValid() || listener == null || this.keyListenersMap == null) {
            return false;
        }
        this.lock.lock();
        try {
            ConcurrentHashMap<DJIParamAccessListener, Boolean> listeners = this.keyListenersMap.get(keyPath);
            if (listeners == null) {
                listeners = new ConcurrentHashMap<>();
                this.keyListenersMap.put(keyPath, listeners);
            }
            listeners.put(listener, Boolean.valueOf(isRunInMainThread));
            notifyAddEvent(keyPath);
            this.lock.unlock();
            return true;
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    public boolean startListeningForUpdates(DJISDKCacheKey keyPath, DJIParamAccessListener listener) {
        return startListeningForUpdates(keyPath, listener, true);
    }

    public void stopListeningOnKeyPath(DJISDKCacheKey keyPath, DJIParamAccessListener listener) {
        if (keyPath != null && keyPath.isValid() && listener != null && this.keyListenersMap != null) {
            this.lock.lock();
            try {
                ConcurrentHashMap<DJIParamAccessListener, Boolean> listeners = this.keyListenersMap.get(keyPath);
                if (!(listeners == null || listeners.remove(listener) == null)) {
                    notifyRemoveListenerEvent(keyPath);
                }
            } finally {
                this.lock.unlock();
            }
        }
    }

    public void stopListening(DJIParamAccessListener listener) {
        if (listener != null && this.keyListenersMap != null) {
            this.lock.lock();
            try {
                for (DJISDKCacheKey key : this.keyListenersMap.keySet()) {
                    if (this.keyListenersMap.containsKey(key) && this.keyListenersMap.get(key).remove(listener) != null) {
                        notifyRemoveListenerEvent(key);
                    }
                }
            } finally {
                this.lock.unlock();
            }
        }
    }

    public int numberOfListenersOfKeyPath(DJISDKCacheKey keyPath) {
        if (this.keyListenersMap == null || !this.keyListenersMap.containsKey(keyPath)) {
            return 0;
        }
        return this.keyListenersMap.get(keyPath).size();
    }

    public boolean containsKey(DJISDKCacheKey keyPath, DJIParamAccessListener listener) {
        return this.keyListenersMap != null && this.keyListenersMap.containsKey(keyPath) && this.keyListenersMap.get(keyPath).containsKey(listener);
    }

    public void valueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        Map<DJIParamAccessListener, Boolean> listeners;
        if (key != null && key.isValid()) {
            this.lock.lock();
            try {
                if (!(this.keyListenersMap == null || (listeners = this.keyListenersMap.get(key)) == null)) {
                    for (DJIParamAccessListener listener : listeners.keySet()) {
                        DJISDKCacheThreadManager.invokeInSingletonThread(getRunnable(listener, key, oldValue, newValue), ((Boolean) listeners.get(listener)).booleanValue());
                    }
                }
            } finally {
                this.lock.unlock();
            }
        }
    }

    private void notifyAddEvent(DJISDKCacheKey key) {
        if (this.onChangeListeners != null && this.onChangeListeners.size() != 0) {
            Iterator<OnChangeListener> it2 = this.onChangeListeners.iterator();
            while (it2.hasNext()) {
                OnChangeListener listener = it2.next();
                if (listener != null) {
                    listener.onAdd(key);
                }
            }
        }
    }

    private void notifyRemoveListenerEvent(DJISDKCacheKey key) {
        if (this.onChangeListeners != null && this.onChangeListeners.size() != 0) {
            Iterator<OnChangeListener> it2 = this.onChangeListeners.iterator();
            while (it2.hasNext()) {
                OnChangeListener listener = it2.next();
                if (listener != null) {
                    listener.onRemove(key);
                }
            }
        }
    }

    /* JADX INFO: finally extract failed */
    private Runnable getRunnable(DJIParamAccessListener listener, DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        ValueChangeRunnable runnable;
        try {
            this.recyleLock.lock();
            if (this.recyleRunnable == null || this.recyleRunnable.size() <= 0) {
                runnable = new ValueChangeRunnable();
            } else {
                runnable = this.recyleRunnable.remove(0);
            }
            this.recyleLock.unlock();
            runnable.listener = listener;
            runnable.key = key;
            runnable.oldValue = oldValue;
            runnable.newValue = newValue;
            return runnable;
        } catch (Throwable th) {
            this.recyleLock.unlock();
            throw th;
        }
    }

    private class ValueChangeRunnable implements Runnable {
        public DJISDKCacheKey key;
        public DJIParamAccessListener listener;
        public DJISDKCacheParamValue newValue;
        public DJISDKCacheParamValue oldValue;

        public ValueChangeRunnable() {
        }

        public void reset() {
            this.listener = null;
            this.key = null;
            this.oldValue = null;
            this.newValue = null;
        }

        public void run() {
            if (this.listener != null) {
                this.listener.onValueChange(this.key, this.oldValue, this.newValue);
                try {
                    DJISDKCacheListenerLayer.this.recyleLock.lock();
                    if (DJISDKCacheListenerLayer.this.recyleRunnable != null) {
                        reset();
                        DJISDKCacheListenerLayer.this.recyleRunnable.add(this);
                    }
                } finally {
                    DJISDKCacheListenerLayer.this.recyleLock.unlock();
                }
            }
        }
    }
}
