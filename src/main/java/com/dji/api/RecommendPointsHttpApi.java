package com.dji.api;

import com.dji.util.Util;

public class RecommendPointsHttpApi {
    private static String Domain = "https://www.skypixel.com/aerials";
    private static final String TAG = RecommendPointsHttpApi.class.getSimpleName();
    private static String sDomainTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static String getRecommendPointsUrl() {
        return Util.generateUrl(Domain, sDomainTest, null);
    }
}
