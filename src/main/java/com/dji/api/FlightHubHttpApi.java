package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.IFlightHubHttpApi;
import com.dji.config.ApiConfig;
import com.dji.util.Util;

public class FlightHubHttpApi implements IFlightHubHttpApi {
    private static final String TAG = FlightHubHttpApi.class.getSimpleName();
    private static String sAppKeyTest = "";
    private static String sDomainTest = "";

    public static void setTest(String domainTest, String appKeyTest) {
        if (TextUtils.isEmpty(domainTest) || TextUtils.isEmpty(appKeyTest)) {
            sDomainTest = "";
            sAppKeyTest = "";
            return;
        }
        sDomainTest = domainTest;
        sAppKeyTest = appKeyTest;
    }

    public static String getDomain() {
        return Util.getDomain(IFlightHubHttpApi.DOMAIN, sDomainTest);
    }

    public static String getSDKTestAppKey() {
        if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sAppKeyTest)) {
            return null;
        }
        return sAppKeyTest;
    }
}
