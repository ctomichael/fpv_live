package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IAccountCenterHttpApi extends IHttpApi {
    public static final String API_ACCOUNT_CENTER_CAPTCHA = "apis/apprest/v1/vcode";
    public static final String API_ACCOUNT_CENTER_CHECK_ACCOUNT = "apis/apprest/v1/check_account_exist";
    public static final String API_ACCOUNT_CENTER_CHECK_TOKEN = "apis/apprest/v1/validate_token";
    public static final String API_ACCOUNT_CENTER_DELETE_PHONE = "apis/apprest/v1/delete_phone_test";
    public static final String API_ACCOUNT_CENTER_EMAIL_LOGIN = "apis/apprest/v1/email_login";
    public static final String API_ACCOUNT_CENTER_EMAIL_REGISTER = "apis/apprest/v1/email_register";
    public static final String API_ACCOUNT_CENTER_EMAIL_REGISTER_AND_LOGIN = "apis/apprest/v1/email_register_with_login";
    public static final String API_ACCOUNT_CENTER_PHONE_CHECK_CODE = "apis/apprest/v1/check_code";
    public static final String API_ACCOUNT_CENTER_PHONE_LOGIN = "apis/apprest/v1/phone_login";
    public static final String API_ACCOUNT_CENTER_PHONE_REGISTER = "apis/apprest/v1/phone_register";
    public static final String API_ACCOUNT_CENTER_PHONE_REGISTER_AND_LOGIN = "apis/apprest/v1/phone_register_with_login";
    public static final String API_ACCOUNT_CENTER_PHONE_RESET = "apis/apprest/v1/phone_reset";
    public static final String API_ACCOUNT_CENTER_PHONE_SEND_CODE = "apis/apprest/v1/send_code";
    public static final String API_ACCOUNT_CENTER_PREFIX = "apis/apprest/v1/";
    public static final String API_ACCOUNT_CENTER_PW_WEAK_CHECK = "apis/apprest/v1/check_weak_password";
    public static final String API_ACCOUNT_CENTER_USER_LOGIN = "apis/apprest/v1/user_login";
    public static final String API_ACCOUNT_CENTER_USER_TOKEN_INFO = "apis/apprest/v1/token";
    public static final String API_ACCOUNT_LOGIN_AUTH_URL = "apis/apprest/v1/tokenv2/generate/authurl";
    public static final String API_LOGOUT = "apis/apprest/v1/logout";
    public static final String API_SEND_RESET_EMAIL = "apis/apprest/v1/send_reset_email";
    public static final String DOMAIN = "https://account-api.dji.com";
    public static final String DOMAIN_TEST = "https://account.dbeta.me";
    public static final String ENCRYPT_IV_STRING = "1wdv$ESZ@QAX3rfc";
    public static final String ENCRYPT_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final String KEY = "221edf0e-edf7-42b5-a451-776a5872ae4c";
    public static final String SDK_ACCOUNT_SEC = "677f5381-234d-4591-80db-33cf3203cc06";
    public static final String SDK_DOMAIN = "https://account.dji.com";
    public static final String SDK_LOGIN_URL = "login?showHeader=false&showThirdPart=false&appId=mobile_sdk";
    public static final String SDK_LOGOUT_URL = "apis/apprest/v1/logout";
    public static final String SDK_REQUEST_ACCOUNT_CENTER_INFO = "apis/apprest/v1/result";
    public static final String SDK_REQUEST_VALIDATE_TOKEN = "apis/apprest/v1/validate_token";
}
