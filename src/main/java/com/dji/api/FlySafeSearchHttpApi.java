package com.dji.api;

import com.dji.util.Util;

public class FlySafeSearchHttpApi {
    private static String Domain = "https://flysafe-api.dji.com/api/geo/fe/geo_map";
    private static final String TAG = FlySafeSearchHttpApi.class.getSimpleName();
    private static String sDomainTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static String getFlySafeSearchUrl() {
        return Util.generateUrl(Domain, sDomainTest, null);
    }
}
