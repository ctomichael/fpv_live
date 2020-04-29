package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.IDJIServiceHttpApi;
import com.dji.config.ApiConfig;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

@EXClassNullAway
public class DJIServiceHttpApi implements IDJIServiceHttpApi {
    public static String SIGN_KEY = (isDJIServiceSignInDebug ? "TEST_KEY" : IDJIServiceHttpApi.KEY_FOR_AUTH);
    private static final String TAG = DJIServiceHttpApi.class.getSimpleName();
    public static boolean isDJIServiceSignInDebug = false;
    private static String sDomainTest = "";
    private static String sHostTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static void setHostTest(String hostTest) {
        sHostTest = hostTest;
    }

    public static String getDeviceCheckUrl() {
        return generateUrl(IDJIServiceHttpApi.API_DJI_DEVICE_CHECK);
    }

    public static String getAbFuncsQueryUrl() {
        return generateUrl(IDJIServiceHttpApi.API_ABFUNCS_QUERY);
    }

    public static String getAbFuncsConfirmUrl() {
        return generateUrl(IDJIServiceHttpApi.API_ABFUNC_CONFIRMATION);
    }

    public static String getDJIServiceAuthDomain() {
        return Util.getDomain(IDJIServiceHttpApi.DOMAIN_FOR_AUTH, sHostTest);
    }

    public static String getDJIServiceAuthKey() {
        return IDJIServiceHttpApi.KEY_FOR_AUTH;
    }

    public static String getCountryCodeReverseOnlineUrl() {
        return generateUrl(IDJIServiceHttpApi.API_COUNTRY_CODE_REVERSE_ONLINE);
    }

    private static String generateUrl(String... route) {
        return Util.generateUrl(IDJIServiceHttpApi.DOMAIN, sDomainTest, route);
    }

    public static String getHost() {
        if (ApiConfig.getConfig().isDebug()) {
            return Util.findHost(sDomainTest);
        }
        return Util.findHost(IDJIServiceHttpApi.DOMAIN);
    }

    public static String getDomain() {
        return Util.getDomain(IDJIServiceHttpApi.DOMAIN, sDomainTest);
    }

    public static class Push implements IDJIServiceHttpApi.IPush {
        private static String sBRServerAppIdTest = "";
        private static String sBRServerAppKeyTest = "";
        private static String sDomainTest = "";
        private static String sPushAppIdTest = "";
        private static String sPushAppKeyTest = "";

        public static String getPushAppId() {
            if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sPushAppIdTest)) {
                return "123814";
            }
            return sPushAppIdTest;
        }

        public static String getPushAppKey() {
            if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sPushAppKeyTest)) {
                return "XynyNa6I1tUZAEi0xRvd49J";
            }
            return sPushAppKeyTest;
        }

        public static String getBRServerAppId() {
            if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sBRServerAppIdTest)) {
                return IDJIServiceHttpApi.IPush.BR_SERVER_APP_ID;
            }
            return sBRServerAppIdTest;
        }

        public static String getBRServerAppKey() {
            if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sBRServerAppKeyTest)) {
                return IDJIServiceHttpApi.IPush.BR_SERVER_APP_KEY;
            }
            return sBRServerAppKeyTest;
        }

        public static void setTest(String domainTest, String pushAppIdTest, String pushAppKeyTest, String brServerAppIdTest, String brServerAppKeyTest) {
            if (TextUtils.isEmpty(domainTest) || TextUtils.isEmpty(pushAppIdTest) || TextUtils.isEmpty(pushAppKeyTest) || TextUtils.isEmpty(brServerAppIdTest) || TextUtils.isEmpty(brServerAppKeyTest)) {
                sDomainTest = "";
                sBRServerAppIdTest = "";
                sBRServerAppKeyTest = "";
                sPushAppIdTest = "";
                sPushAppKeyTest = "";
                return;
            }
            sDomainTest = domainTest;
            sBRServerAppIdTest = brServerAppIdTest;
            sBRServerAppKeyTest = brServerAppKeyTest;
            sPushAppIdTest = pushAppIdTest;
            sPushAppKeyTest = pushAppKeyTest;
        }

        public static String getPushAuthenticateUrl() {
            return generateUrl(IDJIServiceHttpApi.IPush.API_AUTHENTICATE);
        }

        private static String generateUrl(String... route) {
            return Util.generateUrl(IDJIServiceHttpApi.IPush.DOMAIN, sDomainTest, route);
        }

        public static String getDomain() {
            return Util.getDomain(IDJIServiceHttpApi.IPush.DOMAIN, sDomainTest);
        }
    }

    public static class Geo implements IDJIServiceHttpApi.IGeo {
        public static String getGeoIp() {
            return "https://dict.djiservice.org/geo/ip";
        }

        public static String getGeoGps() {
            return "https://dict.djiservice.org/geo/gps";
        }

        public static String getGeoGpsWithIp() {
            return "https://dict.djiservice.org/geo/gpsWithIP";
        }
    }

    public static class DJIGOApi implements IDJIServiceHttpApi.IDJIGOApi {
        public static String getAcademyVideoUrl(String lang, int type, String appId) {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_ACADEMY_VIDEO, lang, Integer.valueOf(type), appId);
        }

        public static String getAcademyBookUrl(String lang, String appId) {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_ACADEMY_BOOK, lang, appId);
        }

        public static String getAcademyFaqUrl(String lang, int type, String appId) {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_ACADEMY_FAQ, lang, Integer.valueOf(type), appId);
        }

        public static String getAcademyBannerUrl(String lang) {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_ACADEMY_BANNER, lang);
        }

        public static String getAcademyFaqSearchUrl() {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_ACADEMY_FAQ_SEARCH, new Object[0]);
        }

        public static String getAcademyVideoSearchUrl() {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_ACADEMY_VIDEO_SEARCH, new Object[0]);
        }

        public static String getAcademySearchUrl() {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_ACADEMY_SEARCH, new Object[0]);
        }

        public static String getFeedBackUrl(String lang, String content, String contact) {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_FEED_BACK, lang, content, contact);
        }

        public static String getLearnMoreUrl(String lang, String deviceType) {
            return getUrl(IDJIServiceHttpApi.IDJIGOApi.API_LEARN_MORE, lang, deviceType);
        }

        private static String getUrl(String api, Object... args) {
            if (args == null || args.length <= 0) {
                return generateUrl(api);
            }
            return String.format(Locale.US, generateUrl(api), args);
        }

        private static String generateUrl(String... route) {
            return Util.generateUrl(IDJIServiceHttpApi.IDJIGOApi.DOMAIN, IDJIServiceHttpApi.IDJIGOApi.DOMAIN, route);
        }

        public static String getDomain() {
            return Util.getDomain(IDJIServiceHttpApi.IDJIGOApi.DOMAIN, IDJIServiceHttpApi.IDJIGOApi.DOMAIN);
        }
    }

    public static class FAQ implements IDJIServiceHttpApi.IFAQ {
        public static String getFaqFrequentlyFaqUrl(String lang, String type) {
            return String.format(Locale.US, generateUrl(IDJIServiceHttpApi.IFAQ.API_FREQUENTLY_FAQ), lang, type);
        }

        private static String generateUrl(String... route) {
            return Util.generateUrl(IDJIServiceHttpApi.IFAQ.DOMAIN, IDJIServiceHttpApi.IFAQ.DOMAIN, route);
        }

        public static String getDomain() {
            return Util.getDomain(IDJIServiceHttpApi.IFAQ.DOMAIN, IDJIServiceHttpApi.IFAQ.DOMAIN);
        }
    }

    public static class DeviceStore implements IDJIServiceHttpApi.IDeviceStore {
        public static String checkAllUrl() {
            return generateUrl(IDJIServiceHttpApi.IDeviceStore.API_DEVICE_ALL);
        }

        public static String checkUrl(String slug) {
            return getUrl(IDJIServiceHttpApi.IDeviceStore.API_DEVICE, slug);
        }

        private static String getUrl(String api, Object... args) {
            if (args == null || args.length <= 0) {
                return generateUrl(api);
            }
            return String.format(Locale.US, generateUrl(api), args);
        }

        private static String generateUrl(String... route) {
            return Util.generateUrl(IDJIServiceHttpApi.IDeviceStore.DOMAIN, IDJIServiceHttpApi.IDeviceStore.DOMAIN, route);
        }

        public static String getDomain() {
            return Util.getDomain(IDJIServiceHttpApi.IDeviceStore.DOMAIN, IDJIServiceHttpApi.IDeviceStore.DOMAIN);
        }
    }

    public static Header[] getFinalHttpHeader() {
        return new Header[]{new Header() {
            /* class com.dji.api.DJIServiceHttpApi.AnonymousClass1 */

            public String getName() {
                return "apikey";
            }

            public String getValue() {
                return DJIServiceHttpApi.getDJIServiceAuthKey();
            }

            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        }, new Header() {
            /* class com.dji.api.DJIServiceHttpApi.AnonymousClass2 */

            public String getName() {
                return "Host";
            }

            public String getValue() {
                return DJIServiceHttpApi.getDJIServiceAuthDomain();
            }

            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        }};
    }

    public static Header[] getAbTestHttpHeader() {
        return new Header[]{new Header() {
            /* class com.dji.api.DJIServiceHttpApi.AnonymousClass3 */

            public String getName() {
                return "Host";
            }

            public String getValue() {
                return DJIServiceHttpApi.getDJIServiceAuthDomain();
            }

            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        }};
    }

    public static class KongDjiService {
        private static String sDomainTest = "";

        public static String getUASUrl() {
            if (!ApiConfig.getConfig().isDebug() || TextUtils.isEmpty(sDomainTest)) {
                return generateUrl(IDJIServiceHttpApi.API_GET_CAAC_UAS);
            }
            return generateUrl(IDJIServiceHttpApi.API_GET_CAAC_UAS_DEBUG);
        }

        private static String generateUrl(String... route) {
            return Util.generateUrl(IDJIServiceHttpApi.DOMAIN, sDomainTest, route);
        }

        public static String getDomain() {
            return Util.getDomain(IDJIServiceHttpApi.DOMAIN, sDomainTest);
        }

        public static void setTest(String debugUrl) {
            sDomainTest = debugUrl;
        }
    }

    public static String getDSDomainUrl() {
        return generateUrl(new String[0]);
    }
}
