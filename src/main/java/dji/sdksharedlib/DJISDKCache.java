package dji.sdksharedlib;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISDKCacheInteractionCallback;
import dji.sdksharedlib.listener.DJISDKCacheListenerLayer;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;

@EXClassNullAway
public class DJISDKCache {
    private static final String TAG = "DJISDKCache";
    public DJISDKCacheHWAbstractionLayer halLayer;
    private DJISDKCacheInteractionCallback interactionCallback = null;
    protected boolean isInitialized = false;
    public DJISDKCacheListenerLayer listenerLayer;
    public DJISDKCacheStoreLayer storeLayer;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static DJISDKCache instance = new DJISDKCache();

        private SingletonHolder() {
        }
    }

    public static DJISDKCache getInstance() {
        return SingletonHolder.instance;
    }

    protected DJISDKCache() {
    }

    public void setInteractionCallback(DJISDKCacheInteractionCallback callback) {
        this.interactionCallback = callback;
    }

    public void init() {
        if (!this.isInitialized) {
            this.isInitialized = true;
            this.listenerLayer = new DJISDKCacheListenerLayer();
            this.storeLayer = new DJISDKCacheStoreLayer();
            this.halLayer = new DJISDKCacheHWAbstractionLayer();
            this.listenerLayer.init();
            this.storeLayer.init(this.listenerLayer);
            this.halLayer.init(this.storeLayer, this.listenerLayer);
        }
    }

    public void destroy() {
        this.isInitialized = false;
        if (this.halLayer != null) {
            this.halLayer.destroy();
        }
        if (this.storeLayer != null) {
            this.storeLayer.destroy();
        }
        if (this.listenerLayer != null) {
            this.listenerLayer.destroy();
        }
        if (!this.subscriptions.isUnsubscribed()) {
            this.subscriptions.unsubscribe();
        }
        this.interactionCallback = null;
    }

    public boolean containsKey(DJISDKCacheKey keyPath, DJIParamAccessListener listener) {
        if (this.listenerLayer == null || keyPath == null || listener == null) {
            return false;
        }
        return this.listenerLayer.containsKey(keyPath, listener);
    }

    public void addMockAbstraction(int count, String mockComponentName, Class<? extends DJISDKCacheHWAbstraction> mockComponentClass) {
        if (this.halLayer != null) {
            this.halLayer.addMockAbstraction(count, mockComponentName, mockComponentClass);
            return;
        }
        throw new NullPointerException("DJISDKCacheHWAbstractionLayer is null, call init first");
    }

    public boolean startListeningForUpdates(DJISDKCacheKey keyPath, DJIParamAccessListener listener, boolean isRunInMainThread) {
        return startListeningForUpdates(keyPath, listener, isRunInMainThread, false);
    }

    public boolean startListeningForUpdates(DJISDKCacheKey keyPath, DJIParamAccessListener listener, boolean isRunInMainThread, boolean withKeyManager) {
        if (this.listenerLayer == null || keyPath == null) {
            return false;
        }
        sendEvent(keyPath, withKeyManager, DJISDKCacheInteractionCallback.EventType.START_LISTENING);
        return this.listenerLayer.startListeningForUpdates(keyPath, listener, isRunInMainThread);
    }

    public void stopListeningOnKey(DJISDKCacheKey keyPath, DJIParamAccessListener listener) {
        this.listenerLayer.stopListeningOnKeyPath(keyPath, listener);
    }

    public void stopListening(DJIParamAccessListener listener) {
        this.listenerLayer.stopListening(listener);
    }

    private /* synthetic */ void lambda$sendEvent$0(DJISDKCacheKey keyPath, boolean withKeyManager, DJISDKCacheInteractionCallback.EventType eventType, Boolean aBoolean) {
        if (this.interactionCallback != null) {
            this.interactionCallback.onEvent(keyPath.getComponent(), keyPath.getIndex(), keyPath.getSubComponent(), keyPath.getSubComponentIndex(), keyPath.getParamKey(), withKeyManager, eventType);
        }
    }

    private void sendEvent(DJISDKCacheKey keyPath, boolean withKeyManager, DJISDKCacheInteractionCallback.EventType eventType) {
    }

    public void setValue(DJISDKCacheKey keyPath, Object value, DJISetCallback callback) {
        setValue(keyPath, value, callback, false);
    }

    public void setValue(DJISDKCacheKey keyPath, Object value, DJISetCallback callback, boolean withKeyManager) {
        if (keyPath != null) {
            this.halLayer.setValue(keyPath, value, callback);
            sendEvent(keyPath, withKeyManager, DJISDKCacheInteractionCallback.EventType.SET);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void getValue(DJISDKCacheKey keyPath, DJIGetCallback callback) {
        getValue(keyPath, callback, false);
    }

    public void getValue(DJISDKCacheKey keyPath, DJIGetCallback callback, boolean withKeyManager) {
        DJISDKCacheParamValue value = getAvailableValue(keyPath, withKeyManager);
        if (value == null || !value.isValid()) {
            if (keyPath != null) {
                this.halLayer.getValue(keyPath, callback);
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_EXECUTION_FAILED);
            }
        } else if (callback != null) {
            callback.onSuccess(value);
        }
    }

    public DJISDKCacheParamValue getAvailableValue(DJISDKCacheKey keyPath) {
        return getAvailableValue(keyPath, false);
    }

    public DJISDKCacheParamValue getAvailableValue(DJISDKCacheKey keyPath, boolean withKeyManager) {
        if (keyPath == null || this.storeLayer == null) {
            return null;
        }
        sendEvent(keyPath, withKeyManager, DJISDKCacheInteractionCallback.EventType.GET);
        return this.storeLayer.getValue(keyPath);
    }

    public void performAction(DJISDKCacheKey keyPath, DJIActionCallback callback, Object... args) {
        performActionWithKeyManager(keyPath, callback, false, args);
    }

    public void performActionWithKeyManager(DJISDKCacheKey keyPath, DJIActionCallback callback, boolean withKeyManager, Object... args) {
        this.halLayer.performAction(keyPath, callback, args);
        sendEvent(keyPath, withKeyManager, DJISDKCacheInteractionCallback.EventType.ACTION);
    }

    public void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    public void removeSubscription(Subscription subscription) {
        this.subscriptions.remove(subscription);
    }

    public boolean isKeySupported(DJISDKCacheKey keyPath) {
        if (keyPath == null) {
            return false;
        }
        return this.halLayer.isKeySupported(keyPath);
    }
}
