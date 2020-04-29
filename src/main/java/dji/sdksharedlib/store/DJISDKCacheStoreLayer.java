package dji.sdksharedlib.store;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.GlobalConfig;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.RandomUtils;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJISDKCacheListenerLayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJISDKCacheStoreLayer {
    private static final String TAG = "DJISDKCacheStoreLayer";
    private DJISDKCacheListenerLayer listener = null;
    private Lock lock = new ReentrantLock();
    protected final ConcurrentHashMap<DJISDKCacheKey, DJISDKCacheParamValue> stores = new ConcurrentHashMap<>(300);

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DumpKeys dumpKeys) {
        dumpKeysInfo(dumpKeys.getComponentName(), dumpKeys.getIndex());
    }

    public void dumpKeysInfo(final String componentName, final int index) {
        if (GlobalConfig.LOG_DEBUGGABLE) {
            new Thread() {
                /* class dji.sdksharedlib.store.DJISDKCacheStoreLayer.AnonymousClass1 */

                public void run() {
                    StringBuilder sb = new StringBuilder("start the dumping...").append("\n");
                    DJILog.d(DJISDKCacheStoreLayer.TAG, sb.toString(), new Object[0]);
                    for (Map.Entry<DJISDKCacheKey, DJISDKCacheParamValue> valueEntry : DJISDKCacheStoreLayer.this.stores.entrySet()) {
                        if (((DJISDKCacheKey) valueEntry.getKey()).getComponent().equals(componentName) && ((DJISDKCacheKey) valueEntry.getKey()).getIndex() == index) {
                            sb.setLength(0);
                            sb.append("Key:").append(((DJISDKCacheKey) valueEntry.getKey()).toString()).append(" Value:").append(((DJISDKCacheParamValue) valueEntry.getValue()).getData()).append("\n");
                            DJILog.d(DJISDKCacheStoreLayer.TAG, sb.toString(), new Object[0]);
                        }
                    }
                }
            }.start();
        } else {
            DJILog.e(TAG, "not debuggable can't dump the keys", new Object[0]);
        }
    }

    public DJISDKCacheStoreLayer() {
        if (GlobalConfig.LOG_DEBUGGABLE) {
            DJIEventBusUtil.register(this);
        }
    }

    public void init(DJISDKCacheListenerLayer listener2) {
        this.listener = listener2;
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    public DJISDKCacheParamValue getValue(DJISDKCacheKey keyPath) {
        if (keyPath == null || keyPath.isPathEmpty()) {
            return null;
        }
        return this.stores.get(keyPath);
    }

    public List<DJISDKCacheParamValue> getValues(DJISDKCacheKey[] keyPaths) {
        if (keyPaths == null) {
            return null;
        }
        List<DJISDKCacheParamValue> list = new ArrayList<>();
        for (DJISDKCacheKey keyPath : keyPaths) {
            DJISDKCacheParamValue value = getValue(keyPath);
            if (value == null) {
                return null;
            }
            list.add(value);
        }
        return list;
    }

    public boolean replaceValue(DJISDKCacheParamValue value, DJISDKCacheKey keyPath) {
        this.lock.lock();
        boolean ret = false;
        if (!(value == null || keyPath == null)) {
            try {
                if (!keyPath.isPathEmpty()) {
                    DJISDKCacheParamValue oldValue = getValue(keyPath);
                    this.stores.put(keyPath, value);
                    if (this.listener != null) {
                        this.listener.valueChange(keyPath, oldValue, value);
                    }
                    ret = true;
                }
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }
        this.lock.unlock();
        return ret;
    }

    public void sendEvent(DJISDKCacheParamValue value, DJISDKCacheKey keyPath) {
        if (this.listener != null) {
            this.listener.valueChange(keyPath, null, value);
        }
    }

    public boolean removeValue(DJISDKCacheKey keyPath) {
        this.lock.lock();
        boolean ret = false;
        if (keyPath != null) {
            try {
                if (this.stores.containsKey(keyPath)) {
                    DJISDKCacheParamValue value = this.stores.get(keyPath);
                    this.stores.remove(keyPath);
                    if (this.listener != null) {
                        this.listener.valueChange(keyPath, value, null);
                    }
                    ret = true;
                }
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }
        this.lock.unlock();
        return ret;
    }

    public boolean removeAllAbstractionValues(String componentName, int index) {
        this.lock.lock();
        boolean ret = false;
        try {
            Set<DJISDKCacheKey> keys = this.stores.keySet();
            List<DJISDKCacheKey> removeKeys = new ArrayList<>();
            for (DJISDKCacheKey key : keys) {
                if (key.getComponent().equals(componentName) && key.getIndex() == index) {
                    removeKeys.add(key);
                }
            }
            for (DJISDKCacheKey key2 : removeKeys) {
                DJISDKCacheParamValue value = this.stores.get(key2);
                this.stores.remove(key2);
                if (this.listener != null) {
                    this.listener.valueChange(key2, value, null);
                }
                ret = true;
            }
            return ret;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean removeAllSubAbstractionValues(String componentName, int index, String subComponentName, int subIndex) {
        this.lock.lock();
        boolean ret = false;
        try {
            Set<DJISDKCacheKey> keys = this.stores.keySet();
            List<DJISDKCacheKey> removeKeys = new ArrayList<>();
            for (DJISDKCacheKey key : keys) {
                if (key.getComponent() != null && key.getComponent().equals(componentName) && key.getIndex() == index && key.getSubComponent() != null && key.getSubComponent().equals(subComponentName) && key.getSubComponentIndex() == subIndex) {
                    removeKeys.add(key);
                }
            }
            for (DJISDKCacheKey key2 : removeKeys) {
                DJISDKCacheParamValue value = this.stores.get(key2);
                this.stores.remove(key2);
                if (this.listener != null) {
                    this.listener.valueChange(key2, value, null);
                }
                ret = true;
            }
            return ret;
        } finally {
            this.lock.unlock();
        }
    }

    public void test() {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            /* class dji.sdksharedlib.store.DJISDKCacheStoreLayer.AnonymousClass2 */

            public void run() {
                int data = (RandomUtils.createRandom().nextInt(5) + 1) * 100;
                DJISDKCacheStoreLayer.this.replaceValue(new DJISDKCacheParamValue(Integer.valueOf(data)), new DJISDKCacheKey.Builder().path("battery/0/EnergyRemainingPercentage").build());
            }
        }, 0, 2000);
    }
}
