package dji.internal.mock.mock;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class FakeComponentKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "FakeComponent";
    @Key(accessType = 3, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FAKE_VALUE = "FakeValue";

    public FakeComponentKeys(String name) {
        super(name);
    }
}
