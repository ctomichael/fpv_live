package dji.component.accountcenter;

import android.content.Context;
import dji.component.accountcenter.listener.OnDataResultListener;
import dji.component.accountcenter.response.AccountCenterSmsType;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public interface IAccountCenterService {
    public static final String ACCOUNT_CENTER_SMS_TYPE_BIND = "account_center_sms_type_bind";
    public static final String ACCOUNT_CENTER_SMS_TYPE_REGISTER = "account_center_sms_type_register";
    public static final String ACCOUNT_CENTER_SMS_TYPE_RESET_PW = "account_center_sms_type_reset_pw";
    public static final String KEY_ACCOUNT_CENTER_SMS_TYPE = "keyAccountCenterSmsType";
    public static final String KEY_AREA_CODE = "keyAreaCode";
    public static final String KEY_IS_CAN_BACK = "keyIsCanBack";
    public static final String KEY_IS_CAN_SEND_AD_EMAIL = "keyIsCanSendEmail";
    public static final String KEY_IS_EXIT_APP = "keyIsExitApp";
    public static final String KEY_REGISTER_CAPTCHA = "keyRegisterCaptcha";
    public static final String KEY_REGISTER_CAPTCHA_KEY = "keyRegisterCaptchaKey";
    public static final String KEY_REGISTER_EMAIL = "keyRegisterEmail";
    public static final String KEY_REGISTER_PASSWORD = "keyRegisterPassword";
    public static final String KEY_REGISTER_PHONE = "keyRegisterPhone";
    public static final String KEY_REGISTER_PHONE_CODE = "keyRegisterPhoneCode";
    public static final String KEY_SIGN_UP_TYPE = "keySignupType";
    public static final String NAME = "AccountCenterService";
    public static final String SIGN_UP_TYPE_EMAIL = "sign_up_type_email";
    public static final String SIGN_UP_TYPE_PHONE = "sign_up_type_phone";

    public interface VerifyPhoneCallBack {
        void onFail(int i);

        void onSuccess(JSONObject jSONObject);
    }

    public interface GetRegionResultListener {
        void onResult(List<Region> list, Region region);
    }

    void accountCenterCheckSms(String str, String str2, String str3, AccountCenterSmsType accountCenterSmsType, OnDataResultListener onDataResultListener);

    void accountCenterLoginByEmailWithListener(String str, String str2, Callback callback);

    void accountCenterLoginByEmailWithVerificationListener(String str, String str2, String str3, String str4, Callback callback);

    void accountCenterLoginByPhoneWithListener(String str, String str2, Callback callback);

    void accountCenterLoginByPhoneWithVerificationListener(String str, String str2, String str3, String str4, Callback callback);

    void accountCenterPhoneReset(String str, String str2, String str3, String str4, String str5, OnDataResultListener onDataResultListener);

    void accountCenterSendSms(String str, String str2, AccountCenterSmsType accountCenterSmsType, OnDataResultListener onDataResultListener);

    void checkAccountEmailExist(String str, OnDataResultListener onDataResultListener);

    void checkAccountEmailExist(String str, String str2, String str3, OnDataResultListener onDataResultListener);

    void checkAccountPhoneExist(String str, String str2, OnDataResultListener onDataResultListener);

    void checkAccountPhoneExist(String str, String str2, String str3, String str4, OnDataResultListener onDataResultListener);

    void clearCountry();

    void convertRequestUrlForLogin(String str, String str2, String str3, Context context, OnConvertRequestListener onConvertRequestListener);

    boolean finalizeDecoder();

    void forgetpasswordWithListener(String str, Callback callback);

    String getAccountString();

    String getDecodeLocale();

    String getEmail();

    String getInputEmail();

    void getLocation(VerifyPhoneCallBack verifyPhoneCallBack);

    MemberInfo getMemberInfo();

    String getName();

    String getPhone();

    MemberInfo getProfileInfo();

    void getProfiles(Callback callback);

    void getRegion(Region region, GetRegionResultListener getRegionResultListener);

    <T> T getTempDataItem(String str, T t);

    Map<String, Object> getTempDataMap();

    String getToken();

    String getUid();

    String getUserId();

    String getUserName();

    void initRegionDecoder(Context context);

    boolean isLogin();

    boolean isPhoneRegistered();

    void logout(Callback callback);

    void registerByEmail(String str, String str2, boolean z);

    void registerByEmailAndLoginWithListener(String str, String str2, Callback callback);

    void registerByEmailAndLoginWithListener(String str, String str2, String str3, String str4, String str5, Callback callback);

    void registerByEmailWithListener(String str, String str2, Callback callback);

    void registerByEmailWithListener(String str, String str2, String str3, String str4, String str5, Callback callback);

    void registerByPhoneAndLoginWithListener(String str, String str2, String str3, String str4, String str5, Callback callback);

    void registerByPhoneWithListener(String str, String str2, String str3, String str4, String str5, Callback callback);

    void registerForceLoginLogic(ForceLoginController forceLoginController);

    void registerLoginStateListener(OnLoginStateListener onLoginStateListener);

    void registerProfileChangeListener(OnProfileChangeListener onProfileChangeListener);

    void registerTokenListener(OnTokenInvalidListener onTokenInvalidListener);

    void setIsCanSendAdEmail(boolean z);

    void setProfiles(Map<String, String> map, Callback callback);

    void setTempDataItem(String str, Object obj);

    void syncProfileInfo();

    void unregisterLoginStateListener(OnLoginStateListener onLoginStateListener);

    void unregisterProfileChangeListener(OnProfileChangeListener onProfileChangeListener);

    void unregisterTokenListener();

    void updateAvatar(String str, Callback callback);
}
