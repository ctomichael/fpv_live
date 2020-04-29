package dji.service;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DJIAppServiceManager {
    private Map<String, IDJIService> mServiceMap = new ConcurrentHashMap();

    private static class INSTANCE_HOLDER {
        /* access modifiers changed from: private */
        public static final DJIAppServiceManager S_INSTANCE = new DJIAppServiceManager();

        private INSTANCE_HOLDER() {
        }
    }

    public static DJIAppServiceManager getInstance() {
        return INSTANCE_HOLDER.S_INSTANCE;
    }

    public void register(String key, IDJIService service) {
        if (!TextUtils.isEmpty(key)) {
            this.mServiceMap.put(key, service);
        }
    }

    public void unregister(String key) {
        if (!TextUtils.isEmpty(key)) {
            this.mServiceMap.remove(key);
        }
    }

    @Nullable
    public IDJIService getService(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return this.mServiceMap.get(key);
    }

    @Nullable
    public <T> T getService(String key, Class<T> cls) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return this.mServiceMap.get(key);
    }

    public Collection<IDJIService> getServices() {
        return Collections.unmodifiableCollection(this.mServiceMap.values());
    }
}
