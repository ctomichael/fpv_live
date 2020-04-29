package dji.sdksharedlib.listener;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJISDKCacheInteractionCallback {
    public static final String ACTION_EVENT_NAME = "PropertyStartListen";
    public static final String GETTER_EVENT_NAME = "PropertyGet";
    public static final String SETTER_EVENT_NAME = "PropertySet";
    public static final String START_EVENT_NAME = "Action";

    void onEvent(String str, int i, String str2, int i2, String str3, boolean z, EventType eventType);

    public enum EventType {
        START_LISTENING(DJISDKCacheInteractionCallback.START_EVENT_NAME),
        GET(DJISDKCacheInteractionCallback.GETTER_EVENT_NAME),
        SET(DJISDKCacheInteractionCallback.SETTER_EVENT_NAME),
        ACTION(DJISDKCacheInteractionCallback.ACTION_EVENT_NAME);
        
        public final String eventName;

        private EventType(String name) {
            this.eventName = name;
        }
    }
}
