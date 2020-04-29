package dji.sdksharedlib.hardware.abstractions;

import android.text.TextUtils;
import com.dji.frame.util.MD5;
import dji.common.error.DJIError;
import dji.common.error.DJISDKCacheError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCollectorCharacteristics;
import dji.sdksharedlib.hardware.extension.DJISDKMergeHandler;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.extension.Key;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public abstract class DJISDKCacheHWAbstraction {
    protected static final int FULL_SERIAL_NUM = 2;
    protected static final int INTERNAL_SERIAL_NUM = 3;
    protected static final int LEGACY_SERIAL_NUM = 1;
    protected static final int SHORT_SERIAL_NUM = 0;
    private static final String TAG = "DJISDKCacheHWAbstraction";
    protected static final DJISDKMergeHandler mergeHandler = new DJISDKMergeHandler();
    private Map<String, Method> actionMethodMap;
    private DJISDKCacheKey.Builder builder = new DJISDKCacheKey.Builder();
    protected Map<String, DJISDKCacheKeyCharacteristics> characteristicsMap;
    protected DJISDKCacheKey defaultKeyPath;
    private Map<String, Method> getterMethodMap;
    protected String hwComponentName;
    /* access modifiers changed from: protected */
    public int index = 0;
    protected Map<String, DJISDKCacheKeyCharacteristics> keyMap;
    protected OnValueChangeListener onValueChangeListener;
    private Map<String, Method> setterMethodMap;
    protected Map<String, Map<Integer, DJISDKCacheHWAbstraction>> subComponentMap;

    public interface InnerCallback {
        void onFails(DJIError dJIError);

        void onSuccess(Object obj);
    }

    protected interface GetterInnerCallback extends InnerCallback {
        boolean hasGetCallback();
    }

    public interface OnValueChangeListener {
        void onNewValue(Object obj, DJISDKCacheKey dJISDKCacheKey);

        void onNewValueFromGetter(Object obj, DJISDKCacheKey dJISDKCacheKey, UpdateStoreForGetterCallback updateStoreForGetterCallback);

        void onNewValueFromSetter(Object obj, DJISDKCacheKey dJISDKCacheKey);
    }

    /* access modifiers changed from: protected */
    public abstract void initializeComponentCharacteristics();

    public void init(String component, int index2, DJISDKCacheStoreLayer storeLayer, OnValueChangeListener onValueChangeListener2) {
        this.hwComponentName = component;
        this.index = index2;
        this.onValueChangeListener = onValueChangeListener2;
        this.defaultKeyPath = this.builder.component(component).index(index2).build();
        this.keyMap = new HashMap();
        initializeAllCharacteristics();
        this.subComponentMap = new ConcurrentHashMap();
        initializeSubComponents(storeLayer);
        createMethodMap();
    }

    public int getAbstrationIndex() {
        return this.index;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    public synchronized void destroy() {
        notifyValueChangeForKeyPath((Object) false, convertKeyToPath(DJISDKCacheKeys.CONNECTION));
        if (this.subComponentMap != null && !this.subComponentMap.entrySet().isEmpty()) {
            for (Map.Entry<String, Map<Integer, DJISDKCacheHWAbstraction>> entry : this.subComponentMap.entrySet()) {
                for (Map.Entry entry2 : ((Map) entry.getValue()).entrySet()) {
                    DJISDKCacheHWAbstraction eachSubComponent = (DJISDKCacheHWAbstraction) entry2.getValue();
                    if (eachSubComponent != null) {
                        eachSubComponent.destroy();
                    }
                }
            }
        }
        this.onValueChangeListener = null;
    }

    /* access modifiers changed from: protected */
    public void createMethodMap() {
        this.getterMethodMap = new HashMap();
        this.setterMethodMap = new HashMap();
        this.actionMethodMap = new HashMap();
        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(getClass().getMethods()));
        methods.addAll(getMethodsFromSuperClass(getClass()));
        for (Method method : methods) {
            Getter getter = (Getter) method.getAnnotation(Getter.class);
            if (getter != null) {
                this.getterMethodMap.put(getter.value(), method);
            } else {
                Setter setter = (Setter) method.getAnnotation(Setter.class);
                if (setter != null) {
                    this.setterMethodMap.put(setter.value(), method);
                } else {
                    Action action = (Action) method.getAnnotation(Action.class);
                    if (action != null) {
                        this.actionMethodMap.put(action.value(), method);
                    }
                }
            }
        }
    }

    public boolean isPushKey(DJISDKCacheKey keyPath) {
        String key = keyPath.getParamKey();
        if (key == null) {
            return false;
        }
        return this.keyMap.containsKey(key) && this.keyMap.get(key).isPushAccessType();
    }

    public DJISDKCacheUpdateType getUpdateType(DJISDKCacheKey keyPath) {
        if (keyPath != null && keyPath.getParamKey() != null && this.keyMap.containsKey(keyPath.getParamKey())) {
            return this.keyMap.get(keyPath.getParamKey()).updateType;
        }
        DJILog.d(TAG, "getUpdatType is null! keyPath=" + keyPath.toString(), new Object[0]);
        return null;
    }

    /* access modifiers changed from: protected */
    public DJISDKCacheKey convertKeyToPath(String key) {
        return this.defaultKeyPath.clone(key);
    }

    /* access modifiers changed from: protected */
    public synchronized DJISDKCacheKey convertKeyToPath(DJISDKCacheKey key) {
        DJISDKCacheKey clone;
        if (!this.defaultKeyPath.getComponent().equals(key.getComponent())) {
            clone = this.builder.component(this.defaultKeyPath.getComponent()).index(this.defaultKeyPath.getIndex()).subComponent(key.getComponent()).subComponentIndex(key.getIndex()).paramKey(key.getParamKey()).build();
        } else {
            clone = this.defaultKeyPath.clone(key.getParamKey());
        }
        return clone;
    }

    private ArrayList<String> getGettableKeys() {
        ArrayList<String> result = new ArrayList<>();
        for (DJISDKCacheKeyCharacteristics characteristics : this.characteristicsMap.values()) {
            if (characteristics.isGettable()) {
                result.add(characteristics.key);
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void notifyValueChangeForKeyPathFromSetter(Object newValue, String key) {
        if (this.onValueChangeListener != null) {
            this.onValueChangeListener.onNewValueFromSetter(newValue, convertKeyToPath(key));
        }
    }

    /* access modifiers changed from: protected */
    public void notifyValueChangeForKeyPathFromSetter(Object newValue, DJISDKCacheKey keyPath) {
        if (this.onValueChangeListener != null) {
            this.onValueChangeListener.onNewValueFromSetter(newValue, convertKeyToPath(keyPath));
        }
    }

    /* access modifiers changed from: protected */
    public void notifyValueChangeForKeyPathFromGetter(Object newValue, DJISDKCacheKey keyPath, UpdateStoreForGetterCallback callback) {
        if (this.onValueChangeListener != null) {
            this.onValueChangeListener.onNewValueFromGetter(newValue, convertKeyToPath(keyPath), callback);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyValueChangeForKeyPath(Object newValue, String paramKey) {
        if (this.defaultKeyPath != null) {
            notifyValueChangeForKeyPath(newValue, convertKeyToPath(paramKey));
        }
    }

    /* access modifiers changed from: protected */
    public void notifyValueChangeForKeyPath(Object newValue, DJISDKCacheKey keyPath) {
        if (this.onValueChangeListener != null) {
            this.onValueChangeListener.onNewValue(newValue, keyPath);
        }
    }

    private <T> T[] prepend(T[] arr, T firstElement) {
        if (arr == null) {
            return (Object[]) Array.newInstance(firstElement.getClass().getComponentType(), new int[0]);
        }
        int N = arr.length;
        T[] arr2 = Arrays.copyOf(arr, N + 1);
        System.arraycopy(arr2, 0, arr2, 1, N);
        arr2[0] = firstElement;
        return arr2;
    }

    private boolean handleGetCustom(DJISDKCacheKey keyPath, DJIGetCallback callback) {
        return false;
    }

    private boolean handleSetCustom(DJISDKCacheKey keyPath, Object value, DJISetCallback callback) {
        return false;
    }

    private boolean handleActionCustom(DJISDKCacheKey keyPath, DJIActionCallback callback, Object... value) {
        return false;
    }

    public void getValue(DJISDKCacheKey keyPath, DJIGetCallback callback) {
        if (TextUtils.isEmpty(keyPath.getSubComponent())) {
            String paramKey = keyPath.getParamKey();
            if (!TextUtils.isEmpty(paramKey) && this.characteristicsMap.containsKey(paramKey) && this.getterMethodMap.containsKey(paramKey)) {
                try {
                    this.getterMethodMap.get(paramKey).invoke(this, new DefaultGetInnerCallback(keyPath, callback));
                } catch (InvocationTargetException e) {
                    DJILog.exceptionToString(e.getTargetException());
                    if (callback != null) {
                        callback.onFails(DJISDKCacheError.INVALID_KEY_FORMAT);
                    }
                } catch (Exception e2) {
                    DJILog.e(TAG, DJILog.exceptionToString(e2), new Object[0]);
                    if (callback != null) {
                        callback.onFails(DJISDKCacheError.INVALID_KEY_FOR_COMPONENT);
                    }
                }
            } else if (!handleGetCustom(keyPath, callback) && callback != null) {
                callback.onFails(DJISDKCacheError.KEY_UNSUPPORTED);
            }
        } else {
            throw new RuntimeException("still have the subComponentKey!");
        }
    }

    public void setValue(DJISDKCacheKey keyPath, Object value, DJISetCallback callback) {
        String subComponentKey = keyPath.getSubComponent();
        if (TextUtils.isEmpty(subComponentKey)) {
            String paramKey = keyPath.getParamKey();
            if (!TextUtils.isEmpty(paramKey) && this.characteristicsMap.containsKey(paramKey) && this.setterMethodMap.containsKey(paramKey)) {
                if (this.characteristicsMap.get(keyPath.getParamKey()).isParamTypeCorrect(value)) {
                    try {
                        this.setterMethodMap.get(paramKey).invoke(this, value, new DefaultSetInnerCallback(keyPath, value, callback));
                    } catch (InvocationTargetException e) {
                        DJILog.exceptionToString(e.getTargetException());
                        if (callback != null) {
                            callback.onFails(DJISDKCacheError.INVALID_KEY_FORMAT);
                        }
                    } catch (Exception e2) {
                        DJILog.d(TAG, "catch set value exception: " + e2.getMessage(), new Object[0]);
                        if (callback != null) {
                            callback.onFails(DJISDKCacheError.INVALID_KEY_FORMAT);
                        }
                    }
                } else if (callback != null) {
                    callback.onFails(DJISDKCacheError.SETTER_VALUE_TYPE_MISMATCH);
                }
            } else if (!handleSetCustom(keyPath, value, callback) && callback != null) {
                callback.onFails(DJISDKCacheError.KEY_UNSUPPORTED);
            }
        } else if (this.subComponentMap.containsKey(subComponentKey)) {
            setValueFromSubComponent(keyPath, value, callback);
        } else if (callback != null) {
            callback.onFails(DJISDKCacheError.KEY_UNSUPPORTED);
        }
    }

    public void performAction(DJISDKCacheKey keyPath, DJIActionCallback callback, Object... args) {
        String subComponentKey = keyPath.getSubComponent();
        if (TextUtils.isEmpty(keyPath.getSubComponent())) {
            String paramKey = keyPath.getParamKey();
            if (TextUtils.isEmpty(paramKey) || !this.characteristicsMap.containsKey(paramKey) || !this.actionMethodMap.containsKey(paramKey)) {
                if (!handleActionCustom(keyPath, callback, new Object[0]) && callback != null) {
                    callback.onFails(DJISDKCacheError.KEY_UNSUPPORTED);
                }
            } else if (this.characteristicsMap.get(paramKey).isParamTypeCorrect(args)) {
                try {
                    this.actionMethodMap.get(paramKey).invoke(this, prepend(args, new DefaultActionInnerCallback(paramKey, callback, args)));
                } catch (InvocationTargetException e) {
                    DJILog.exceptionToString(e.getTargetException());
                    if (callback != null) {
                        callback.onFails(DJISDKCacheError.INVALID_KEY_FORMAT);
                    }
                } catch (Exception e2) {
                    DJILog.e(TAG, "invoke action method failed: " + e2.getMessage(), new Object[0]);
                    if (callback != null) {
                        callback.onFails(DJISDKCacheError.INVALID_KEY_FOR_COMPONENT);
                    }
                }
            } else if (callback != null) {
                callback.onFails(DJISDKCacheError.SETTER_VALUE_TYPE_MISMATCH);
            }
        } else if (this.subComponentMap.containsKey(subComponentKey)) {
            performActionFromSubComponent(keyPath, callback, args);
        } else if (callback != null) {
            callback.onFails(DJISDKCacheError.KEY_UNSUPPORTED);
        }
    }

    private String getMD5Legacy(String rawString) {
        return MD5.getMD5For16(rawString).substring(0, 8);
    }

    private String getMD5Full(String rawString) {
        return MD5.getMD5(rawString);
    }

    /* access modifiers changed from: protected */
    public String getHashSerialNum(String sn, int state) {
        if (state == 1) {
            return getMD5Legacy(sn);
        }
        if (state == 0) {
            return sn;
        }
        return getMD5Full(sn);
    }

    /* access modifiers changed from: protected */
    public void addCharacteristics(String key, int accessType, DJISDKCacheUpdateType updateType, int protectDuration, int expirationDuration, int autoGetInterval, Class... paramTypes) {
        if (!this.characteristicsMap.containsKey(key)) {
            DJISDKCacheKeyCharacteristics characteristics = new DJISDKCacheKeyCharacteristics(key, accessType, updateType, protectDuration, expirationDuration, autoGetInterval, paramTypes);
            this.characteristicsMap.put(key, characteristics);
            this.keyMap.put(key, characteristics);
        }
    }

    /* access modifiers changed from: protected */
    public void addCharacteristics(Class<? extends DJISDKCacheKeys> clazz, Class abstractionClazz) {
        Key key;
        Map<String, DJISDKCacheKeys.KeyInfo> keyInfoMap = DJISDKCacheKeys.getKeyInfoMap(clazz);
        if (keyInfoMap != null) {
            for (String eachKey : keyInfoMap.keySet()) {
                DJISDKCacheKeys.KeyInfo info = keyInfoMap.get(eachKey);
                if (info.support(abstractionClazz) && (key = info.getKey(abstractionClazz)) != null) {
                    if (key.types() == null || key.types().length <= 0) {
                        addCharacteristics(eachKey, key.accessType(), key.updateType(), key.protectDuration(), key.expirationDuration(), key.autoGetInterval(), key.type());
                    } else {
                        addCharacteristics(eachKey, key.accessType(), key.updateType(), key.protectDuration(), key.expirationDuration(), key.autoGetInterval(), key.types());
                    }
                }
            }
        }
    }

    private void removeCollectorCharacteristics(String key) {
        this.characteristicsMap.remove(key);
        this.keyMap.remove(key);
    }

    private void removeCharacteristics(String key) {
        this.characteristicsMap.remove(key);
        this.keyMap.remove(key);
    }

    private void addSetterMethod(String key, Method setter) {
        this.setterMethodMap.put(key, setter);
    }

    private void addGetterMethod(String key, Method getter) {
        this.getterMethodMap.put(key, getter);
    }

    private void addActionMethod(String key, Method action) {
        this.actionMethodMap.put(key, action);
    }

    /* access modifiers changed from: protected */
    public void initializeAllCharacteristics() {
        initializeCommonCharacteristics();
        initializeComponentCharacteristics();
    }

    private void initializeCommonCharacteristics() {
        this.characteristicsMap = new HashMap();
    }

    public class UpdateStoreForGetterCallback implements GetterInnerCallback {
        /* access modifiers changed from: private */
        public DJIGetCallback cb;
        private DJISDKCacheKey keyPath;

        public UpdateStoreForGetterCallback(DJIGetCallback cb2) {
            this.cb = cb2;
        }

        public UpdateStoreForGetterCallback(DJISDKCacheKey keyPath2, DJIGetCallback cb2) {
            this.keyPath = keyPath2;
            this.cb = cb2;
        }

        public void onNoChange(final DJISDKCacheParamValue value) {
            if (this.cb != null) {
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.UpdateStoreForGetterCallback.AnonymousClass1 */

                    public void run() {
                        UpdateStoreForGetterCallback.this.cb.onSuccess(value);
                    }
                }, true);
            }
        }

        public void onSuccess(Object o) {
            if (this.cb != null) {
                final DJISDKCacheParamValue value = new DJISDKCacheParamValue(o, DJISDKCacheParamValue.Status.Valid, DJISDKCacheParamValue.Source.Get, (long) DJISDKCacheHWAbstraction.this.getCharacteristics(this.keyPath).getExpirationDuation());
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.UpdateStoreForGetterCallback.AnonymousClass2 */

                    public void run() {
                        UpdateStoreForGetterCallback.this.cb.onSuccess(value);
                    }
                }, true);
            }
        }

        public void onFails(final DJIError error) {
            if (this.cb != null) {
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.UpdateStoreForGetterCallback.AnonymousClass3 */

                    public void run() {
                        UpdateStoreForGetterCallback.this.cb.onFails(error);
                    }
                }, true);
            }
        }

        public boolean hasGetCallback() {
            return this.cb != null;
        }
    }

    public class DefaultGetInnerCallback implements GetterInnerCallback {
        /* access modifiers changed from: private */
        public DJIGetCallback cb;
        private DJISDKCacheKey keyPath;

        public DefaultGetInnerCallback(DJISDKCacheKey keyPath2, DJIGetCallback cb2) {
            this.keyPath = keyPath2;
            this.cb = cb2;
        }

        public void onSuccess(Object o) {
            DJISDKCacheHWAbstraction.this.notifyValueChangeForKeyPathFromGetter(o, this.keyPath, new UpdateStoreForGetterCallback(this.keyPath, this.cb));
        }

        public void onFails(final DJIError error) {
            if (this.cb != null) {
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.DefaultGetInnerCallback.AnonymousClass1 */

                    public void run() {
                        DefaultGetInnerCallback.this.cb.onFails(error);
                    }
                }, true);
            }
        }

        public boolean hasGetCallback() {
            return this.cb != null;
        }
    }

    public class DefaultSetInnerCallback implements InnerCallback {
        /* access modifiers changed from: private */
        public DJISetCallback cb;
        private DJISDKCacheKey keyPath;
        private Object value;

        public DefaultSetInnerCallback(DJISDKCacheKey keyPath2, Object value2, DJISetCallback cb2) {
            this.keyPath = keyPath2;
            this.cb = cb2;
            this.value = value2;
        }

        public void onSuccess(Object o) {
            DJISDKCacheHWAbstraction.this.notifyValueChangeForKeyPathFromSetter(this.value, this.keyPath);
            if (this.cb != null) {
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.DefaultSetInnerCallback.AnonymousClass2 */

                    public void run() {
                        DefaultSetInnerCallback.this.cb.onSuccess();
                    }
                }, true);
            }
        }

        public void onFails(final DJIError error) {
            if (this.cb != null) {
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.DefaultSetInnerCallback.AnonymousClass3 */

                    public void run() {
                        DefaultSetInnerCallback.this.cb.onFails(error);
                    }
                }, true);
            }
        }
    }

    public class DefaultActionInnerCallback implements InnerCallback {
        /* access modifiers changed from: private */
        public DJIActionCallback cb;
        private String key;
        private Object[] value;

        public DefaultActionInnerCallback(String key2, DJIActionCallback cb2, Object... args) {
            this.key = key2;
            this.cb = cb2;
            this.value = args;
        }

        public void onSuccess(final Object o) {
            if (o != null) {
                DJISDKCacheHWAbstraction.this.notifyValueChangeForKeyPathFromSetter(o, this.key);
            }
            if (this.cb != null) {
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.DefaultActionInnerCallback.AnonymousClass1 */

                    public void run() {
                        DefaultActionInnerCallback.this.cb.onSuccess(o);
                    }
                }, true);
            }
        }

        public void onFails(final DJIError error) {
            if (this.cb != null) {
                DJISDKCacheThreadManager.invoke(new Runnable() {
                    /* class dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.DefaultActionInnerCallback.AnonymousClass2 */

                    public void run() {
                        DefaultActionInnerCallback.this.cb.onFails(error);
                    }
                }, true);
            }
        }
    }

    private void setValueFromSubComponent(DJISDKCacheKey keyPath, Object value, DJISetCallback callback) {
        String subComponentKey = keyPath.getSubComponent();
        ((DJISDKCacheHWAbstraction) this.subComponentMap.get(subComponentKey).get(Integer.valueOf(keyPath.getSubComponentIndex()))).setValue(keyPath.extractSubComponentKeyPath(), value, callback);
    }

    private void performActionFromSubComponent(DJISDKCacheKey keyPath, DJIActionCallback callback, Object... args) {
        String subComponentKey = keyPath.getSubComponent();
        ((DJISDKCacheHWAbstraction) this.subComponentMap.get(subComponentKey).get(Integer.valueOf(keyPath.getSubComponentIndex()))).performAction(keyPath.extractSubComponentKeyPath(), callback, args);
    }

    /* access modifiers changed from: protected */
    public void initializeSubComponents(DJISDKCacheStoreLayer storeLayer) {
    }

    /* access modifiers changed from: protected */
    public synchronized void addSubComponents(DJISDKCacheHWAbstraction subComponent, String componentKey, int index2, DJISDKCacheStoreLayer storeLayer) {
        Map<Integer, DJISDKCacheHWAbstraction> currentSubComponentMap;
        if (subComponent != null) {
            if (subComponent instanceof DJISubComponentHWAbstraction) {
                ((DJISubComponentHWAbstraction) subComponent).init(this.defaultKeyPath.getComponent(), this.defaultKeyPath.getIndex(), componentKey, index2, storeLayer, this.onValueChangeListener);
            } else {
                subComponent.init(componentKey, index2, storeLayer, this.onValueChangeListener);
            }
            if (this.subComponentMap.containsKey(componentKey)) {
                currentSubComponentMap = this.subComponentMap.get(componentKey);
            } else {
                currentSubComponentMap = new ConcurrentHashMap<>();
            }
            currentSubComponentMap.put(Integer.valueOf(index2), subComponent);
            this.subComponentMap.put(componentKey, currentSubComponentMap);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void removeSubComponents(String subHWName, DJISDKCacheStoreLayer storeLayer) {
        if (this.subComponentMap != null && this.subComponentMap.size() > 0 && this.subComponentMap.containsKey(subHWName)) {
            for (Map.Entry<Integer, DJISDKCacheHWAbstraction> entry : this.subComponentMap.get(subHWName).entrySet()) {
                DJISDKCacheHWAbstraction eachSubComponent = (DJISDKCacheHWAbstraction) entry.getValue();
                if (eachSubComponent != null) {
                    eachSubComponent.destroy();
                    if (storeLayer != null) {
                        storeLayer.removeAllSubAbstractionValues(this.hwComponentName, this.index, subHWName, ((Integer) entry.getKey()).intValue());
                    }
                }
            }
            this.subComponentMap.remove(subHWName);
        }
    }

    private void performGetOnCollector(DJISDKCacheKeyCharacteristics collector, DJISDKCacheError error) {
    }

    private void mergeGetRequest(String collectorKey, DJISDKCacheKeyCharacteristics cha, DJIGetCallback callback, int timeInterval) {
    }

    private DJISDKCacheKeyCharacteristics createGetRequestCollector() {
        return null;
    }

    private ArrayList<String> getSupportedKeys() {
        return null;
    }

    private List<Method> getMethodsFromSuperClass(Class<?> aClass) {
        List<Method> methods = new ArrayList<>();
        Class<?> aClass2 = aClass.getSuperclass();
        while (aClass2 != null && aClass2 != DJISDKCacheHWAbstraction.class) {
            methods.addAll(Arrays.asList(aClass2.getMethods()));
            aClass2 = aClass2.getSuperclass();
        }
        return methods;
    }

    public Map<String, Method> getGetterMethodMap() {
        return this.getterMethodMap;
    }

    private Map<String, Method> getSetterMethodMap() {
        return this.setterMethodMap;
    }

    public boolean isUserDrivenKey(DJISDKCacheKey keyPath) {
        if (this.characteristicsMap.containsKey(keyPath.getParamKey())) {
            return this.characteristicsMap.get(keyPath.getParamKey()).isUserDrivenUpdateType();
        }
        return false;
    }

    public boolean isContainKey(DJISDKCacheKey keyPath) {
        return this.characteristicsMap.containsKey(keyPath.getParamKey());
    }

    public int getAutoGetInterval(DJISDKCacheKey keyPath) {
        String subComponentKey = keyPath.getSubComponent();
        if (!TextUtils.isEmpty(subComponentKey)) {
            this.subComponentMap.containsKey(subComponentKey);
            return getAutoGetIntervalFromSubComponent(keyPath);
        } else if (this.characteristicsMap.containsKey(keyPath.getParamKey())) {
            return this.characteristicsMap.get(keyPath.getParamKey()).getAutoGetInterval();
        } else {
            return 0;
        }
    }

    private int getAutoGetIntervalFromSubComponent(DJISDKCacheKey keyPath) {
        String subComponentKey = keyPath.getSubComponent();
        DJISDKCacheKey subComponentKeyPath = keyPath.extractSubComponentKeyPath();
        if (this.subComponentMap.containsKey(subComponentKey)) {
            Map<Integer, DJISDKCacheHWAbstraction> subCompoent = this.subComponentMap.get(subComponentKey);
            if (subCompoent.containsKey(Integer.valueOf(keyPath.getSubComponentIndex()))) {
                return ((DJISDKCacheHWAbstraction) subCompoent.get(Integer.valueOf(keyPath.getSubComponentIndex()))).getAutoGetInterval(subComponentKeyPath);
            }
        }
        return 0;
    }

    private void addKeyToCollector(String collectorKey, String paramKey, InnerCallback callback, int interval) {
        if (collectorKey == null || paramKey == null) {
            throw new RuntimeException("Logic error");
        }
        ((DJISDKCacheCollectorCharacteristics) this.characteristicsMap.get(collectorKey)).requestGet(this.characteristicsMap.get(paramKey), callback);
        mergeHandler.addSignal(new DJISDKMergeHandler.DJISDKMergeSignal(this, (DJISDKCacheCollectorCharacteristics) this.characteristicsMap.get(collectorKey)), interval);
    }

    public DJISDKCacheKeyCharacteristics getCharacteristics(DJISDKCacheKey cacheKey) {
        if (cacheKey == null || cacheKey.getParamKey() != null) {
            return this.characteristicsMap.get(cacheKey.getParamKey());
        }
        return null;
    }

    public DJISDKCacheHWAbstraction getSubComponent(DJISDKCacheKey keyPath) {
        Map<Integer, DJISDKCacheHWAbstraction> map = this.subComponentMap.get(keyPath.getSubComponent());
        if (map != null) {
            return (DJISDKCacheHWAbstraction) map.get(Integer.valueOf(keyPath.getSubComponentIndex()));
        }
        return null;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    public void syncPushDataFromMidware() {
        notifyValueChangeForKeyPath((Object) true, convertKeyToPath(DJISDKCacheKeys.CONNECTION));
    }

    public Map<String, Map<Integer, DJISDKCacheHWAbstraction>> getSubComponents() {
        return this.subComponentMap;
    }

    public boolean isKeySupported(DJISDKCacheKey keyPath) {
        DJISDKCacheHWAbstraction subComponentAbstraction;
        String subComponentKey = keyPath.getSubComponent();
        if (TextUtils.isEmpty(subComponentKey)) {
            if (this.characteristicsMap.containsKey(keyPath.getParamKey())) {
                return true;
            }
        } else if (this.subComponentMap.containsKey(subComponentKey) && (subComponentAbstraction = (DJISDKCacheHWAbstraction) this.subComponentMap.get(subComponentKey).get(Integer.valueOf(keyPath.getSubComponentIndex()))) != null) {
            return subComponentAbstraction.isKeySupported(keyPath);
        }
        return false;
    }
}
