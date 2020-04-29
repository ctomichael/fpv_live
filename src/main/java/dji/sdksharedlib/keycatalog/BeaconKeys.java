package dji.sdksharedlib.keycatalog;

import dji.sdksharedlib.keycatalog.extension.Key;

public class BeaconKeys extends DJISDKCacheKeys {
    @Key(accessType = 6, type = Integer.class)
    public static final String BRIGHTNESS = "BeaconBrightness";
    public static final String COMPONENT_KEY = "NavigationLED";
    @Key(accessType = 6, type = Boolean.class)
    public static final String ENABLED = "BeaconEnabled";
    @Key(accessType = 6, type = Integer.class)
    public static final String FLICKER_FREQUENCY = "BeaconFlickerFrequency";

    public BeaconKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
