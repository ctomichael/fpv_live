package dji.sdksharedlib.keycatalog;

import dji.common.flightcontroller.accesslocker.AccessLockerState;
import dji.common.flightcontroller.accesslocker.FormattingState;
import dji.common.flightcontroller.accesslocker.UserAccountInfo;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.keycatalog.extension.Key;

public class AccessLockerKeys extends DJISDKCacheKeys {
    @Key(accessType = 4, type = AccessLockerState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ACCESS_LOCKER_STATE = "AccessLockerState";
    @Key(accessType = 1, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ACCESS_LOCKER_USERNAME = "AccessLockerUsername";
    @Key(accessType = 1, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ACCESS_LOCKER_VERSION = "AccessLockerVersion";
    public static final String COMPONENT_KEY = "AccessLocker";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FORMAT = "Format";
    @Key(accessType = 4, type = FormattingState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String FORMATTING_STATE = "FormattingState";
    @Key(accessType = 8, type = UserAccountInfo.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LOGIN = "login";
    @Key(accessType = 8, types = {UserAccountInfo.class, UserAccountInfo.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MODIFY_USER_ACCOUNT = "modifyUserAccount";
    @Key(accessType = 8, type = UserAccountInfo.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESET_USER_ACCOUNT = "resetUserAccount";
    @Key(accessType = 8, type = UserAccountInfo.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SET_UP_USER = "SetUpUser";

    public AccessLockerKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
