package dji.service;

import android.content.Context;
import android.support.annotation.IntRange;

public interface IDJIService {
    public static final int PRIORITY_ACTIVATE_SERVICE = 33;
    public static final int PRIORITY_ANALYTICS_SERVICE = 99;
    public static final int PRIORITY_APP_STATE_SERVICE = 32;
    public static final int PRIORITY_DJI_NOTIFICATION_SERVICE = 200;
    public static final int PRIORITY_FLY_SAFE = 94;
    public static final int PRIORITY_HOST_DEVICE_SERVICE = 255;
    public static final int PRIORITY_PRIVACY_SERVICE_SERVICE = 0;
    public static final int PRIORITY_UPGRADE_SERVICE = 88;

    String getName();

    void init(Context context);

    @IntRange(from = 0, to = 255)
    int priority();
}
