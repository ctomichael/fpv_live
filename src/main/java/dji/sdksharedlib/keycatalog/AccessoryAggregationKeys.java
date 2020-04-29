package dji.sdksharedlib.keycatalog;

public class AccessoryAggregationKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "AccessoryAggregation";

    public AccessoryAggregationKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
