package dji.component.appstate;

public interface IAppStateService {
    public static final String MMKV_KEY_SWITCH_DIRECT_ENTER_FPV = "mmkv_key_switch_direct_enter_fpv";
    public static final String NAME = "IAppStateService";

    String getAppVersion();

    String getAppVersionString();

    String getBuildNumber();

    String getFlavor();

    boolean getNetWorkStatus();

    String getUUID();

    boolean isDebugApp();

    boolean isForeGround();

    boolean isInFpv();

    boolean isInnerApp();

    void setFlavor(String str);

    void setIsDebugApp(boolean z);

    void setIsInnerApp(boolean z);
}
