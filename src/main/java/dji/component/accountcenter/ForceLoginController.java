package dji.component.accountcenter;

public interface ForceLoginController {
    boolean isNeedForceLogin();

    void onTokenNotLogin();
}
