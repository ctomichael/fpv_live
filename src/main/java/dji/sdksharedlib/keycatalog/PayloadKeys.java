package dji.sdksharedlib.keycatalog;

import dji.common.payload.PayloadWidget;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class PayloadKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "Payload";
    @Key(accessType = 8, types = {Integer.class, PayloadWidget.PayloadWidgetType.class, Integer.class}, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CONFIGURE_WIDGET_VALUE = "setValueForWidget";
    @Key(accessType = 4, type = byte[].class, updateType = DJISDKCacheUpdateType.EVENT)
    public static final String GET_DATA_FROM_PAYLOAD = "getDataFromPayload";
    @Key(accessType = 4, type = String.class, updateType = DJISDKCacheUpdateType.EVENT)
    public static final String GET_FLOAT_WINDOW_MSG_FROM_PAYLOAD = "getFloatWindowDataFromPayload";
    @Key(accessType = 1, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String GET_UPSTREAM_BANDWIDTH = "uploadLimit";
    @Key(accessType = 8, types = {Integer.class, PayloadWidget.PayloadWidgetType.class}, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String GET_WIDGET = "getValueOfWidget";
    @Key(accessType = 4, type = PayloadWidget[].class)
    public static final String GET_WIDGETS = "allWidgetStatus";
    @Key(accessType = 1, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SYNCING_TIME_BETWEEN_APP_AND_PAYLOAD = "syncTimeForPayload";
    @Key(accessType = 4, type = String.class)
    public static final String PAYLOAD_PRODUCT_NAME = "payloadName";
    @Key(accessType = 8, type = byte[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SEND_DATA_TO_PAYLOAD = "SendDataToPayload";

    public PayloadKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
