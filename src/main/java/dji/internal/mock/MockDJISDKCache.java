package dji.internal.mock;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.listener.DJISDKCacheListenerLayer;

@EXClassNullAway
public class MockDJISDKCache extends DJISDKCache {
    private static final String TAG = "MockDJISDKCache";

    private MockDJISDKCache() {
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static MockDJISDKCache instance = new MockDJISDKCache();

        private SingletonHolder() {
        }
    }

    public static MockDJISDKCache getInstance() {
        return SingletonHolder.instance;
    }

    public void init() {
        if (!this.isInitialized) {
            this.isInitialized = true;
            this.listenerLayer = new DJISDKCacheListenerLayer();
            this.storeLayer = new MockDJISDKCacheStoreLayer();
            this.halLayer = new MockDJISDKCacheHWAbstractionLayer();
            this.listenerLayer.init();
            this.storeLayer.init(this.listenerLayer);
            this.halLayer.init(this.storeLayer, this.listenerLayer);
        }
    }

    public void notifyFakeComponentChanged() {
        ((MockDJISDKCacheHWAbstractionLayer) this.halLayer).notifyFakeComponentChanged();
    }
}
