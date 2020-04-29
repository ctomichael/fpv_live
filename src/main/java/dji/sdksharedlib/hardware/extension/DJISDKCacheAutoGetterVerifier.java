package dji.sdksharedlib.hardware.extension;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.DJISDKCacheHWAbstractionLayer;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJISDKCacheListenerLayer;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.util.concurrent.ConcurrentHashMap;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJISDKCacheAutoGetterVerifier {
    private static final int MSG_ADD = 1;
    private static final int MSG_COMPONENT_CHANGED = 3;
    private static final int MSG_REMOVE = 2;
    private static final int ONE_SECOND = 1000;
    private static final int PUSH_DATA_AUTOGET_INTERVAL = 0;
    private static final String TAG = "DJISDKCacheAutoGetterVerifier";
    /* access modifiers changed from: private */
    public AutoGetterHandler autoGetterHandler;
    /* access modifiers changed from: private */
    public DJISDKCacheHWAbstractionLayer hal;
    private ConcurrentHashMap<DJISDKCacheKey, Signal> keyPathSignalMap;
    private DJISDKCacheListenerLayer listenerLayer;
    private DJISDKCacheListenerLayer.OnChangeListener onChangeListener;
    private DJISDKCacheCenterTimer timer;

    public class Signal implements Runnable {
        private int interval = 0;
        private DJISDKCacheKey keyPath;

        public Signal(DJISDKCacheKey keyPath2) {
            this.keyPath = keyPath2;
        }

        public void setInterval(int interval2) {
            this.interval = interval2;
        }

        public int getInterval() {
            return this.interval;
        }

        public void run() {
            DJISDKCacheAutoGetterVerifier.this.hal.getValue(this.keyPath, null);
        }

        public int hashCode() {
            if (this.keyPath != null) {
                return this.keyPath.hashCode();
            }
            return 0;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (obj instanceof Signal) {
                return this.keyPath.equals(((Signal) obj).keyPath);
            }
            return super.equals(obj);
        }
    }

    public void init(DJISDKCacheHWAbstractionLayer hwAbstractionLayer, DJISDKCacheListenerLayer listenerLayer2) {
        this.hal = hwAbstractionLayer;
        this.listenerLayer = listenerLayer2;
        this.keyPathSignalMap = new ConcurrentHashMap<>();
        this.autoGetterHandler = new AutoGetterHandler(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
        this.timer = new DJISDKCacheCenterTimer();
        this.timer.init(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
        this.onChangeListener = new DJISDKCacheListenerLayer.OnChangeListener() {
            /* class dji.sdksharedlib.hardware.extension.DJISDKCacheAutoGetterVerifier.AnonymousClass1 */

            public void onAdd(DJISDKCacheKey keyPath) {
                if (keyPath != null) {
                    Message msg = DJISDKCacheAutoGetterVerifier.this.autoGetterHandler.obtainMessage(1);
                    msg.obj = keyPath;
                    DJISDKCacheAutoGetterVerifier.this.autoGetterHandler.sendMessage(msg);
                }
            }

            public void onRemove(DJISDKCacheKey keyPath) {
                if (keyPath != null) {
                    Message msg = DJISDKCacheAutoGetterVerifier.this.autoGetterHandler.obtainMessage(2);
                    msg.obj = keyPath;
                    DJISDKCacheAutoGetterVerifier.this.autoGetterHandler.sendMessage(msg);
                }
            }
        };
        listenerLayer2.addOnChangeListener(this.onChangeListener);
        DJIEventBusUtil.register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJISDKCacheHWAbstractionLayer.AbstractionChangeEvent event) {
        Message msg = this.autoGetterHandler.obtainMessage(3);
        this.autoGetterHandler.removeMessages(3);
        this.autoGetterHandler.sendMessageDelayed(msg, 1000);
    }

    public void destroy() {
        if (this.timer != null) {
            this.timer.destroy();
            this.timer = null;
        }
        this.autoGetterHandler = null;
        DJIEventBusUtil.unRegister(this);
    }

    /* access modifiers changed from: private */
    public void addSignal(DJISDKCacheKey keyPath) {
        if (!this.keyPathSignalMap.containsKey(keyPath)) {
            Signal signal = new Signal(keyPath);
            this.keyPathSignalMap.put(keyPath, signal);
            signal.setInterval(this.hal.getInterval(keyPath));
            if (signal.getInterval() != 0 && this.timer != null) {
                this.timer.add(signal, signal.getInterval());
            }
        }
    }

    /* access modifiers changed from: private */
    public void removeSignal(DJISDKCacheKey keyPath) {
        if (this.listenerLayer.numberOfListenersOfKeyPath(keyPath) == 0 && this.keyPathSignalMap.containsKey(keyPath)) {
            Signal signal = this.keyPathSignalMap.get(keyPath);
            if (this.timer != null) {
                this.timer.remove(signal, signal.getInterval());
            }
            this.keyPathSignalMap.remove(keyPath);
        }
    }

    /* access modifiers changed from: private */
    public void refreshSignal() {
        if (this.timer != null) {
            this.timer.clearList();
        }
        for (DJISDKCacheKey keyPath : this.keyPathSignalMap.keySet()) {
            Signal s = this.keyPathSignalMap.get(keyPath);
            s.setInterval(this.hal.getInterval(keyPath));
            if (!(s.getInterval() == 0 || this.timer == null)) {
                this.timer.add(s, s.getInterval());
            }
        }
    }

    private class AutoGetterHandler extends Handler {
        public AutoGetterHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DJISDKCacheAutoGetterVerifier.this.addSignal((DJISDKCacheKey) msg.obj);
                    return;
                case 2:
                    DJISDKCacheAutoGetterVerifier.this.removeSignal((DJISDKCacheKey) msg.obj);
                    return;
                case 3:
                    DJISDKCacheAutoGetterVerifier.this.refreshSignal();
                    return;
                default:
                    return;
            }
        }
    }

    public int getSignalMapSize() {
        return this.keyPathSignalMap.size();
    }

    public int getCenterTimerMapSize() {
        if (this.timer != null) {
            return this.timer.getCenterTimerMapSize();
        }
        return 0;
    }

    public int getCenterTimerIntervalListSize(int interval) {
        if (this.timer != null) {
            return this.timer.getCenterTimerIntervalSize(interval);
        }
        return 0;
    }
}
