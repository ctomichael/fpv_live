package dji.sdksharedlib.keycatalog;

import dji.sdksharedlib.keycatalog.extension.Key;

public class SpotlightKeys extends DJISDKCacheKeys {
    @Key(accessType = 6, type = Integer.class)
    public static final String BRIGHTNESS = "SpotlightBrightness";
    public static final String COMPONENT_KEY = "SearchlightLED";
    @Key(accessType = 6, type = Boolean.class)
    public static final String ENABLED = "SpotlightEnabled";
    @Key(accessType = 4, type = Float.class)
    public static final String TEMPERATURE = "SpotlightTemperature";

    public SpotlightKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
