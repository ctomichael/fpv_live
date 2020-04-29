package com.dji.api;

import com.dji.api.protocol.IActiveHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;

@EXClassNullAway
public class ActiveHttpApi implements IActiveHttpApi {
    private static final String TAG = ActiveHttpApi.class.getSimpleName();
    private static String UASUrl;
    private static String sDeleteRouteUrl = "";
    private static String sDomainTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static void setDeleteRouteUrl(String deleteRouteUrl) {
        sDeleteRouteUrl = deleteRouteUrl;
    }

    public static String getScanCodeActivation() {
        return generateUrl(IActiveHttpApi.API_SCAN_CODE_ACTIVATION);
    }

    public static String getScanCodeDecryptSN() {
        return generateUrl(IActiveHttpApi.API_SCAN_CODE_DECRYPT_SN);
    }

    public static String getPostErrorLogUrl() {
        return generateUrl(IActiveHttpApi.API_POST_ERROR_LOG);
    }

    public static String getActiveVerifyUrl() {
        return generateUrl(IActiveHttpApi.API_ACTIVE_VERIFY);
    }

    public static String getEagleActiveUrl() {
        return generateUrl(IActiveHttpApi.API_EAGLE_ACTIVE);
    }

    public static String getActivationTermUrl() {
        return generateUrl(IActiveHttpApi.API_ACTIVATION_TERM);
    }

    public static String getPhoneVerificationUrl() {
        return generateUrl(IActiveHttpApi.API_PHONE_VERIFICATION);
    }

    public static String getPhoneAuthenticationUrl() {
        return generateUrl(IActiveHttpApi.API_PHONE_AUTHENTICATION);
    }

    public static String getPhoneDirectBindingUrl() {
        return generateUrl(IActiveHttpApi.API_PHONE_DIRECT_BINDING);
    }

    public static String getPhoneObtainBindedMobileUrl() {
        return generateUrl(IActiveHttpApi.API_PHONE_OBTAIN_BINDED_MOBILE);
    }

    public static String getPhoneAssociatedBindingUrl() {
        return generateUrl(IActiveHttpApi.API_PHONE_ASSOCIATED_BINDING);
    }

    public static String getPhoneBindServiceUrl() {
        return generateUrl("bind_service");
    }

    public static String getBindServiceBaseUrl() {
        return generateUrl("");
    }

    public static String getDeleteBindServiceUrl() {
        return generateUrl(sDeleteRouteUrl, "");
    }

    public static String getServerLocationUrl() {
        return generateUrl(IActiveHttpApi.API_GET_SERVER_LOCATION);
    }

    private static String generateUrl(String... route) {
        return Util.generateUrl(IActiveHttpApi.DOMAIN, sDomainTest, route);
    }

    public static String getDomain() {
        return Util.getDomain(IActiveHttpApi.DOMAIN, sDomainTest);
    }
}
