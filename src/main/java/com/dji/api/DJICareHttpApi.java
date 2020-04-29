package com.dji.api;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;

@EXClassNullAway
public class DJICareHttpApi implements IHttpApi {
    private static final String API_DJI_CARE_GEN_TASK = "api/v1/djicare/gen_task";
    private static final String API_DJI_CARE_H5_BUY_URL = "product/";
    private static final String API_DJI_CARE_TASK_RESULT = "api/v1/djicare/result";
    private static final String DOMAIN = "https://apigateway.djiservice.org";
    private static final String DOMAIN_FOR_ACTIVE_CARD = "https://support.dji.com/care/active";
    private static final String DOMAIN_FOR_AUTH = "djiservice.org";
    private static final String DOMAIN_FOR_H5 = "https://m.dji.com";
    private static final String KEY_FOR_AUTH = "5C4AtJjmSif5LePjWHpAhQeMFMbhld8F";
    private static String sActiveCardTest = "";
    private static String sDomainTest = "";
    private static String sH5DomainTest = "";
    private static String sHostTest = "";
    private static String sSignKeyTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static void setHostTest(String hostTest) {
        sHostTest = hostTest;
    }

    public static void setActiveCardTest(String hostTest) {
        sActiveCardTest = hostTest;
    }

    public static void setSignKeyTest(String signKeyTest) {
        sSignKeyTest = signKeyTest;
    }

    public static void setH5DomainTest(String debugH5Key) {
        sH5DomainTest = debugH5Key;
    }

    public static String getHost() {
        return Util.getDomain("djiservice.org", sHostTest);
    }

    public static String getSignKey() {
        return Util.getDomain("5C4AtJjmSif5LePjWHpAhQeMFMbhld8F", sSignKeyTest);
    }

    public static String getDJICareGenTaskUrl() {
        return generateUrl("api/v1/djicare/gen_task");
    }

    public static String getDJICareTaskResultUrl() {
        return generateUrl("api/v1/djicare/result");
    }

    public static String getDJICareBuyUrl() {
        return Util.generateUrl(DOMAIN_FOR_H5, sH5DomainTest, API_DJI_CARE_H5_BUY_URL);
    }

    public static String getActiveCardUrl() {
        return Util.generateUrl(DOMAIN_FOR_ACTIVE_CARD, sActiveCardTest, null);
    }

    private static String generateUrl(String... route) {
        return Util.generateUrl("https://apigateway.djiservice.org", sDomainTest, route);
    }
}
