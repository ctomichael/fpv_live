package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.IUpgradeHttpApi;
import com.dji.config.ApiConfig;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;
import java.util.Locale;

@EXClassNullAway
public class UpgradeHttpApi implements IUpgradeHttpApi {
    private static final String TAG = UpgradeHttpApi.class.getSimpleName();
    private static String sDomainTest = "";
    private static String sTestConfigUrl = "";
    private static String sTestInnerUrl = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static void setTestConfigUrl(String configUrl) {
        sTestConfigUrl = configUrl;
    }

    public static void setTestInnerUrl(String innerUrl) {
        sTestInnerUrl = innerUrl;
    }

    public static String getUpgradePilotV2Url(String buildNum) {
        ApiConfig config = ApiConfig.getConfig();
        if (config.isUpgradeBeta() && !TextUtils.isEmpty(sDomainTest)) {
            return getUrl(sDomainTest, IUpgradeHttpApi.API_PILOT_V2_TEST, new Object[0]);
        }
        if (config.isDebug() && !TextUtils.isEmpty(sDomainTest)) {
            return getUrl(sDomainTest, IUpgradeHttpApi.API_PILOT_V2_DEBUG, new Object[0]);
        }
        if (TextUtils.isEmpty(buildNum)) {
            return getUrl("https://mydjiflight.dji.com", IUpgradeHttpApi.API_PILOT_V2, new Object[0]);
        }
        return getUrl("https://mydjiflight.dji.com", IUpgradeHttpApi.API_PILOT_V2_WITH_PARAM, buildNum);
    }

    public static String getUpgradePilotBrUrl(String buildNum) {
        ApiConfig config = ApiConfig.getConfig();
        if ((config.isDebug() || config.isUpgradeBeta()) && !TextUtils.isEmpty(sDomainTest)) {
            return getUrl(sDomainTest, IUpgradeHttpApi.API_PILOT_BR_DEBUG, new Object[0]);
        }
        if (TextUtils.isEmpty(buildNum)) {
            return getUrl("https://mydjiflight.dji.com", IUpgradeHttpApi.API_PILOT_BR, new Object[0]);
        }
        return getUrl("https://mydjiflight.dji.com", IUpgradeHttpApi.API_PILOT_BR_WITH_PARAM, buildNum);
    }

    public static boolean isOfficial() {
        return TextUtils.isEmpty(sDomainTest) || (!ApiConfig.getConfig().isDebug() && !ApiConfig.getConfig().isUpgradeBeta());
    }

    public static boolean isDebug() {
        return !TextUtils.isEmpty(sDomainTest) && ApiConfig.getConfig().isDebug();
    }

    public static boolean isTestServer() {
        return !TextUtils.isEmpty(sTestConfigUrl) && !TextUtils.isEmpty(sTestInnerUrl) && ApiConfig.getConfig().isDebug();
    }

    public static String getTestConfigUrl() {
        return sTestConfigUrl;
    }

    public static String getTestInnerUrl() {
        return sTestInnerUrl;
    }

    public static String getDomain() {
        return "https://mydjiflight.dji.com";
    }

    public static String getInnerDomain() {
        return IUpgradeHttpApi.INNER_DOMAIN;
    }

    public static boolean isBeta() {
        return !TextUtils.isEmpty(sDomainTest) && ApiConfig.getConfig().isUpgradeBeta();
    }

    public static String getGetDateUrl() {
        return getUrl("https://mydjiflight.dji.com", IUpgradeHttpApi.API_GET_DATE, new Object[0]);
    }

    private static String getUrl(String domain, String api, Object... args) {
        if (args == null || args.length <= 0) {
            return generateUrl(domain, api);
        }
        return String.format(Locale.US, generateUrl(domain, api), args);
    }

    private static String generateUrl(String domain, String... route) {
        return Util.generateUrl(domain, route);
    }
}
