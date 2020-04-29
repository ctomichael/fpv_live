package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.IAccountCenterHttpApi;
import com.dji.config.ApiConfig;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;

@EXClassNullAway
public class AccountCenterHttpApi implements IAccountCenterHttpApi {
    private static final String TAG = AccountCenterHttpApi.class.getSimpleName();
    private static String sAccountCenterKeyTest = "";
    private static String sDomainTest = "";

    public static void setTest(String domainTest, String accountCenterKeyTest) {
        if (TextUtils.isEmpty(domainTest) || TextUtils.isEmpty(accountCenterKeyTest)) {
            sDomainTest = "";
            sAccountCenterKeyTest = "";
            return;
        }
        sDomainTest = domainTest;
        sAccountCenterKeyTest = accountCenterKeyTest;
    }

    public static String getAccountCenterCheckAccount() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_CHECK_ACCOUNT);
    }

    public static String getAccountCenterPhoneRegister() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_PHONE_REGISTER);
    }

    public static String getAccountCenterPhoneRegisterAndLogin() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_PHONE_REGISTER_AND_LOGIN);
    }

    public static String getAccountCenterPhoneLogin() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_PHONE_LOGIN);
    }

    public static String getAccountCenterEmailRegister() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_EMAIL_REGISTER);
    }

    public static String getAccountCenterEmailRegisterAndLogin() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_EMAIL_REGISTER_AND_LOGIN);
    }

    public static String getAccountCenterEmailLogin() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_EMAIL_LOGIN);
    }

    public static String getAccountCenterPhoneCheckCode() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_PHONE_CHECK_CODE);
    }

    public static String getAccountCenterCaptcha() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_CAPTCHA);
    }

    public static String getAccountCenterPhoneSendCode() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_PHONE_SEND_CODE);
    }

    public static String getAccountCenterPhoneReset() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_PHONE_RESET);
    }

    public static String getAccountCenterPasswordWeakCheck() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_PW_WEAK_CHECK);
    }

    public static String getAccountCenterDeletePhone() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_DELETE_PHONE);
    }

    public static String getAccountCenterCheckToken() {
        return generateUrl("apis/apprest/v1/validate_token");
    }

    public static String getAccountCenterUserLogin() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_USER_LOGIN);
    }

    public static String getAccountCenterUserTokenInfo() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_CENTER_USER_TOKEN_INFO);
    }

    public static String getAccountSendResetEmailUrl() {
        return generateUrl(IAccountCenterHttpApi.API_SEND_RESET_EMAIL);
    }

    public static String getAccountLogoutUrl() {
        return generateUrl("apis/apprest/v1/logout");
    }

    private static String generateUrl(String... route) {
        return Util.generateUrl(IAccountCenterHttpApi.DOMAIN, sDomainTest, route);
    }

    private static String generateSDKUrl(String... route) {
        return Util.generateUrl(IAccountCenterHttpApi.SDK_DOMAIN, sDomainTest, route);
    }

    public static String getAccountCenterKey() {
        if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sAccountCenterKeyTest)) {
            return IAccountCenterHttpApi.KEY;
        }
        return sAccountCenterKeyTest;
    }

    public static String getSDKAccountCenterKey() {
        if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sAccountCenterKeyTest)) {
            return IAccountCenterHttpApi.SDK_ACCOUNT_SEC;
        }
        return sAccountCenterKeyTest;
    }

    public static String getEncryptIvString() {
        return IAccountCenterHttpApi.ENCRYPT_IV_STRING;
    }

    public static String getEncryptTransformation() {
        return IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION;
    }

    public static String getDomain() {
        return Util.getDomain(IAccountCenterHttpApi.DOMAIN, sDomainTest);
    }

    public static String getSDKDomain() {
        return Util.getDomain(IAccountCenterHttpApi.SDK_DOMAIN, sDomainTest);
    }

    public static String getSDKAccountCenterInfo() {
        return generateUrl(IAccountCenterHttpApi.SDK_REQUEST_ACCOUNT_CENTER_INFO);
    }

    public static String getSDKValicateToken() {
        return generateUrl("apis/apprest/v1/validate_token");
    }

    public static String getSDKLoginUrl() {
        return generateSDKUrl(IAccountCenterHttpApi.SDK_LOGIN_URL);
    }

    public static String getAccountLoginAuthUrl() {
        return generateUrl(IAccountCenterHttpApi.API_ACCOUNT_LOGIN_AUTH_URL);
    }

    public static String getSDKLogoutUrl() {
        return generateUrl("apis/apprest/v1/logout");
    }
}
