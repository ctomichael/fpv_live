package dji.component.accountcenter;

public interface OnLoginStateListener {
    void onLogin(MemberInfo memberInfo);

    void onLogout();
}
