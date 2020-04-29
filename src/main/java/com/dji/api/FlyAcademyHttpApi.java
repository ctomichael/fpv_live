package com.dji.api;

import com.dji.util.Util;

public class FlyAcademyHttpApi {
    private static String Domain = "https://app-h5.dji.com/ZGppLWdvLT/index.html#/";
    private static final String TAG = FlyAcademyHttpApi.class.getSimpleName();
    private static String sDomainTest = "https://test-app-h5.dbeta.me/ZGppLWdvLT/index.html#/";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static String getFlyAcademyUrl() {
        return Util.generateUrl(Domain, sDomainTest, null);
    }
}
