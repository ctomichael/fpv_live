package dji.sdksharedlib.keycatalog;

import dji.common.accessory.SettingsDefinitions;
import dji.common.accessory.SpeakerState;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.extension.Key;

public class SpeakerKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "Speaker";
    @Key(accessType = 8, types = {Integer.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PLAY = "Play";
    @Key(accessType = 6, type = SettingsDefinitions.PlayMode.class)
    public static final String PLAY_MODE = "PlayMode";
    @Key(accessType = 4, type = SpeakerState.class)
    public static final String SPEAKER_STATE = "SpeakerState";
    @Key(accessType = 6, type = Integer.class)
    public static final String SPEAKER_VOLUME = "SpeakerVolume";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP = "Stop";

    public SpeakerKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
