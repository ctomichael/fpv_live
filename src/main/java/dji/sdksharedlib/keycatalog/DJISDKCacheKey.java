package dji.sdksharedlib.keycatalog;

import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public final class DJISDKCacheKey {
    /* access modifiers changed from: private */
    public static final int DEFAULT_INDEX = DJISDKCacheKeyIndexType.Default.value;
    /* access modifiers changed from: private */
    public static final String DEFAULT_STRING = null;
    private static Map<String, DJISDKCacheKey> keysCacheMap = new ConcurrentHashMap();
    private final String component;
    private final int index;
    private boolean isValid;
    private final String paramKey;
    private final String path;
    private final String pathWithoutParamKey;
    private final int subCompIndex;
    private final String subComponent;

    public static final class Builder {
        /* access modifiers changed from: private */
        public String component;
        /* access modifiers changed from: private */
        public int index = DJISDKCacheKey.DEFAULT_INDEX;
        /* access modifiers changed from: private */
        public String paramKey;
        /* access modifiers changed from: private */
        public String path = DJISDKCacheKey.DEFAULT_STRING;
        /* access modifiers changed from: private */
        public int subCompIndex = DJISDKCacheKey.DEFAULT_INDEX;
        /* access modifiers changed from: private */
        public String subComponent = DJISDKCacheKey.DEFAULT_STRING;

        public Builder path(String path2) {
            if (DJISDKCacheKey.validatePath(path2)) {
                this.path = path2;
                return this;
            }
            throw new RuntimeException("Invalid path");
        }

        public Builder component(String component2) {
            this.component = component2;
            return this;
        }

        public Builder index(int index2) {
            this.index = index2;
            return this;
        }

        public Builder subComponent(String subComponent2) {
            this.subComponent = subComponent2;
            return this;
        }

        public Builder subComponentIndex(int subCompIndex2) {
            this.subCompIndex = subCompIndex2;
            return this;
        }

        public Builder paramKey(String paramKey2) {
            this.paramKey = paramKey2;
            return this;
        }

        public DJISDKCacheKey build() {
            String keyStr;
            if (this.component != null || this.path == null) {
                keyStr = DJISDKCacheKey.producePathFromElements(this.component, this.index, this.subComponent, this.subCompIndex, this.paramKey);
            } else {
                keyStr = this.path;
            }
            DJISDKCacheKey key = DJISDKCacheKey.getCache(keyStr);
            if (key != null) {
                return key;
            }
            DJISDKCacheKey key2 = new DJISDKCacheKey(this);
            DJISDKCacheKey.putCache(keyStr, key2);
            return key2;
        }
    }

    private DJISDKCacheKey(Builder builder) {
        if (validatePath(builder.path)) {
            this.component = componentInPath(builder.path);
            this.index = indexInPath(builder.path);
            this.subComponent = subComponentInPath(builder.path);
            this.subCompIndex = subCompIndexInPath(builder.path);
            this.paramKey = paramKeyInPath(builder.path);
            this.path = producePathFromElements(this.component, this.index, this.subComponent, this.subCompIndex, this.paramKey);
            this.pathWithoutParamKey = producePathFromElements(this.component, this.index, this.subComponent, this.subCompIndex, "");
        } else {
            this.component = builder.component;
            this.index = builder.index;
            this.subComponent = builder.subComponent;
            this.subCompIndex = builder.subCompIndex;
            this.paramKey = builder.paramKey;
            this.path = producePathFromElements(this.component, this.index, this.subComponent, this.subCompIndex, this.paramKey);
            this.pathWithoutParamKey = producePathFromElements(this.component, this.index, this.subComponent, this.subCompIndex, "");
        }
        this.isValid = validatePath(this.path);
    }

    public enum DJISDKCacheKeyIndexType {
        Default(0),
        All(Integer.MAX_VALUE);
        
        /* access modifiers changed from: private */
        public int value;

        private DJISDKCacheKeyIndexType(int value2) {
            this.value = value2;
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getComponent() {
        return this.component;
    }

    public int getIndex() {
        return this.index;
    }

    public String getSubComponent() {
        return this.subComponent;
    }

    public int getSubComponentIndex() {
        return this.subCompIndex;
    }

    public String getParamKey() {
        return this.paramKey;
    }

    public boolean isPathEmpty() {
        return this.path == null || this.path.isEmpty();
    }

    public boolean isValid() {
        return this.isValid;
    }

    public static boolean validatePath(String path2) {
        if (path2 == null || path2.length() == 0) {
            return false;
        }
        return path2.matches("^\\w+[/](\\d+|\\*)[/]\\w+([/](\\d+)[/]\\w+)?$");
    }

    private static String componentInPath(String path2) {
        String[] items = keyPathSplitHelper(path2);
        if (items.length > 0) {
            return items[0];
        }
        return null;
    }

    private static int indexInPath(String path2) {
        String[] items = keyPathSplitHelper(path2);
        if (items.length > 1 && items[1].matches("\\d+")) {
            return Integer.parseInt(items[1]);
        }
        if (items[1].equals("*")) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    private static String subComponentInPath(String path2) {
        String[] items = keyPathSplitHelper(path2);
        if (items.length > 3) {
            return items[2];
        }
        return null;
    }

    private static int subCompIndexInPath(String path2) {
        String[] items = keyPathSplitHelper(path2);
        if (items.length <= 3 || !items[3].matches("\\d+")) {
            return 0;
        }
        return Integer.parseInt(items[3]);
    }

    private static String paramKeyInPath(String path2) {
        String[] items = keyPathSplitHelper(path2);
        if (items.length > 1) {
            return items[items.length - 1];
        }
        return null;
    }

    private static String componentIndexString(int index2) {
        if (index2 == Integer.MAX_VALUE) {
            return "*";
        }
        return Integer.toString(index2);
    }

    private static String subComponentIndexString(int subCompIndex2) {
        return componentIndexString(subCompIndex2);
    }

    /* access modifiers changed from: private */
    public static String producePathFromElements(String component2, int index2, String subComponent2, int subCompIndex2, String paramKey2) {
        StringBuilder sb = new StringBuilder();
        if (index2 == 0 && subComponent2 == null) {
            sb.append(component2).append("/0/").append(paramKey2);
        } else if (subComponent2 == null) {
            sb.append(component2).append(IMemberProtocol.PARAM_SEPERATOR).append(componentIndexString(index2)).append(IMemberProtocol.PARAM_SEPERATOR).append(paramKey2);
        } else if (subCompIndex2 == 0) {
            sb.append(component2).append(IMemberProtocol.PARAM_SEPERATOR).append(componentIndexString(index2)).append(IMemberProtocol.PARAM_SEPERATOR).append(subComponent2).append("/0/").append(paramKey2);
        } else {
            sb.append(component2).append(IMemberProtocol.PARAM_SEPERATOR).append(componentIndexString(index2)).append(IMemberProtocol.PARAM_SEPERATOR).append(subComponent2).append(IMemberProtocol.PARAM_SEPERATOR).append(subComponentIndexString(subCompIndex2)).append(IMemberProtocol.PARAM_SEPERATOR).append(paramKey2);
        }
        return sb.toString();
    }

    public static String produceKeyForComponent(String component2, int index2, String paramKey2) {
        return produceKeyForComponent(component2, index2, null, 0, paramKey2);
    }

    private static String produceKeyForComponent(String component2, int index2, String subComponent2, int subCompIndex2, String paramKey2) {
        return producePathFromElements(component2, index2, subComponent2, subCompIndex2, paramKey2);
    }

    public DJISDKCacheKey clone(String paramKey2) {
        if (paramKey2 == null) {
            return null;
        }
        DJISDKCacheKey key = getCache(this.pathWithoutParamKey + paramKey2);
        if (key == null) {
            return new Builder().component(this.component).index(this.index).subComponent(this.subComponent).subComponentIndex(this.subCompIndex).paramKey(paramKey2).build();
        }
        return key;
    }

    public DJISDKCacheKey extractSubComponentKeyPath() {
        return new Builder().component(this.subComponent).index(this.subCompIndex).paramKey(this.paramKey).build();
    }

    private static String[] keyPathSplitHelper(String keyPath) {
        return keyPath.split(IMemberProtocol.PARAM_SEPERATOR);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DJISDKCacheKey) {
            return this.path.equals(((DJISDKCacheKey) obj).path);
        }
        throw new RuntimeException("Can't compare CacheKey to a " + obj.getClass().getSimpleName());
    }

    public int hashCode() {
        if (this.path != null) {
            return this.path.hashCode();
        }
        return 0;
    }

    public String toString() {
        if (this.path == null) {
            return "";
        }
        return this.path;
    }

    public static DJISDKCacheKey getCache(String keyStr) {
        if (keyStr != null) {
            return keysCacheMap.get(keyStr);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static void putCache(String keyStr, DJISDKCacheKey cacheKey) {
        if (keyStr != null && cacheKey != null && !keysCacheMap.containsKey(keyStr)) {
            keysCacheMap.put(keyStr, cacheKey);
        }
    }
}
