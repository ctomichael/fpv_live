package dji.sdksharedlib.hardware.extension;

import android.os.Handler;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheKeyCharacteristics;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.util.ArrayList;
import java.util.HashMap;

@EXClassNullAway
public class DJISDKCacheCollectorCharacteristics extends DJISDKCacheKeyCharacteristics {
    private static final String TAG = "DJISDKCacheCollectorCharacteristics";
    private static Handler operationHandler = new Handler(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
    public String defaultTimerScheduleTag;
    /* access modifiers changed from: private */
    public HashMap<DJISDKCacheKeyCharacteristics, ArrayList<DJISDKCacheHWAbstraction.InnerCallback>> operations = new HashMap<>();

    public DJISDKCacheCollectorCharacteristics(String collectorKey, int accessTypeMask, DJISDKCacheUpdateType updateType, Class valueType) {
        super(collectorKey, accessTypeMask, updateType, valueType);
    }

    public void requestGet(DJISDKCacheKeyCharacteristics cha, DJISDKCacheHWAbstraction.InnerCallback callback) {
        operationHandler.post(new RequestGet(cha, callback));
    }

    public void removeAllOperation() {
        if (this.operations != null) {
            this.operations.clear();
        }
    }

    private class RequestGet implements Runnable {
        private DJISDKCacheHWAbstraction.InnerCallback callback;
        private DJISDKCacheKeyCharacteristics cha;

        public RequestGet(DJISDKCacheKeyCharacteristics cha2, DJISDKCacheHWAbstraction.InnerCallback callback2) {
            this.cha = cha2;
            this.callback = callback2;
        }

        public void run() {
            if (DJISDKCacheCollectorCharacteristics.this.operations == null) {
                return;
            }
            if (DJISDKCacheCollectorCharacteristics.this.operations.containsKey(this.cha)) {
                ((ArrayList) DJISDKCacheCollectorCharacteristics.this.operations.get(this.cha)).add(this.callback);
                return;
            }
            ArrayList<DJISDKCacheHWAbstraction.InnerCallback> newInnerCallbackList = new ArrayList<>();
            newInnerCallbackList.add(this.callback);
            DJISDKCacheCollectorCharacteristics.this.operations.put(this.cha, newInnerCallbackList);
        }
    }

    public HashMap<DJISDKCacheKeyCharacteristics, ArrayList<DJISDKCacheHWAbstraction.InnerCallback>> getOperations() {
        return this.operations;
    }
}
