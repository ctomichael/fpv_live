package dji.sdksharedlib.hardware.abstractions;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public abstract class DJISubComponentHWAbstraction extends DJISDKCacheHWAbstraction {
    private static final String TAG = "DJISubComponentHWAbstraction";

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        DJILog.d(TAG, String.format("init, abstraction : %s, component : %s, index : %d", getClass().getSimpleName(), component, Integer.valueOf(index)), new Object[0]);
        this.index = index;
        this.onValueChangeListener = onValueChangeListener;
        DJILog.d("DJISDKMergeHandler", "init", new Object[0]);
        this.defaultKeyPath = new DJISDKCacheKey.Builder().component(component).index(index).subComponent(subComponent).subComponentIndex(subComponentIndex).build();
        this.keyMap = new HashMap();
        initializeAllCharacteristics();
        this.subComponentMap = new ConcurrentHashMap();
        initializeSubComponents(storeLayer);
        createMethodMap();
    }

    public void refreshTheSubComponentCacheData() {
    }

    public boolean isKeySupported(DJISDKCacheKey keyPath) {
        if (this.characteristicsMap.containsKey(keyPath.getParamKey())) {
            return true;
        }
        return false;
    }
}
