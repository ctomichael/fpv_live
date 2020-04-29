package dji.sdksharedlib.hardware.extension;

import android.os.Handler;
import dji.common.error.DJISDKCacheError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheKeyCharacteristics;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@EXClassNullAway
public class DJISDKMergeHandler {
    private static final String TAG = "DJISDKMergeHandler";
    private Handler handler = new Handler(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
    private HashMap<String, DJISDKMergeSignal> signalHashMap = new HashMap<>();

    public static class DJISDKMergeSignal implements Runnable {
        public DJISDKCacheHWAbstraction abstraction;
        public DJISDKCacheCollectorCharacteristics characteristics;

        public DJISDKMergeSignal(DJISDKCacheHWAbstraction abstraction2, DJISDKCacheCollectorCharacteristics characteristics2) {
            this.abstraction = abstraction2;
            this.characteristics = characteristics2;
        }

        public void run() {
            if (this.abstraction == null) {
                DJILog.d(DJISDKMergeHandler.TAG, "abstraction is null", new Object[0]);
            } else if (this.characteristics == null) {
                DJILog.d(DJISDKMergeHandler.TAG, "characteristics is null.", new Object[0]);
            } else {
                try {
                    HashMap<DJISDKCacheKeyCharacteristics, ArrayList<DJISDKCacheHWAbstraction.InnerCallback>> hashMap = new HashMap<>();
                    for (Map.Entry<DJISDKCacheKeyCharacteristics, ArrayList<DJISDKCacheHWAbstraction.InnerCallback>> entry : this.characteristics.getOperations().entrySet()) {
                        hashMap.put(entry.getKey(), entry.getValue());
                    }
                    this.characteristics.removeAllOperation();
                    this.abstraction.getGetterMethodMap().get(this.characteristics.key).invoke(this.abstraction, hashMap);
                } catch (Exception e) {
                    handleOnFailureCallback(DJISDKCacheError.INVALID_KEY_FOR_COMPONENT);
                }
            }
        }

        private void handleOnFailureCallback(DJISDKCacheError error) {
            for (Map.Entry<DJISDKCacheKeyCharacteristics, ArrayList<DJISDKCacheHWAbstraction.InnerCallback>> entry : this.characteristics.getOperations().entrySet()) {
                if (entry.getValue() != null) {
                    Iterator it2 = ((ArrayList) entry.getValue()).iterator();
                    while (it2.hasNext()) {
                        DJISDKCacheHWAbstraction.InnerCallback innerCallback = (DJISDKCacheHWAbstraction.InnerCallback) it2.next();
                        if (innerCallback != null) {
                            innerCallback.onFails(error);
                        }
                    }
                }
            }
        }
    }

    public synchronized void addSignal(final DJISDKMergeSignal signal, int interval) {
        if (this.handler == null) {
            DJILog.e(TAG, "Handler is terminated by exception.", new Object[0]);
            throw new RuntimeException("Merge logic breaks: handler is null.");
        } else if (this.signalHashMap == null) {
            DJILog.e(TAG, "Signal hash map is terminated by exception", new Object[0]);
            throw new RuntimeException("Merge logic breaks: signal hash map is null.");
        } else if (signal.abstraction == null) {
            DJILog.e(TAG, "Do not insert null for abstraction in signal", new Object[0]);
            throw new RuntimeException("Requirement exception, abstraction in signal is null.");
        } else if (signal.characteristics == null) {
            DJILog.e(TAG, "Do not insert null for characteristics in signal", new Object[0]);
            throw new RuntimeException("Requirement exception, characteristics in signal is null.");
        } else {
            String key = signal.characteristics.key;
            if (!this.signalHashMap.containsKey(key)) {
                this.signalHashMap.put(key, signal);
                this.handler.postDelayed(new Runnable() {
                    /* class dji.sdksharedlib.hardware.extension.DJISDKMergeHandler.AnonymousClass1 */

                    public void run() {
                        signal.run();
                        DJISDKMergeHandler.this.removeSignal(signal);
                    }
                }, (long) signal.characteristics.getAutoGetInterval());
            }
        }
    }

    /* access modifiers changed from: private */
    public void removeSignal(DJISDKMergeSignal signal) {
        this.signalHashMap.remove(signal.characteristics.key);
    }

    public void destroy() {
        this.handler.removeCallbacksAndMessages(null);
        this.handler = null;
        this.signalHashMap = null;
    }
}
