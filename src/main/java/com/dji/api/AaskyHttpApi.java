package com.dji.api;

import com.dji.api.protocol.IAaskyHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;
import java.util.Locale;

@EXClassNullAway
public class AaskyHttpApi implements IAaskyHttpApi {
    private static final String TAG = AaskyHttpApi.class.getSimpleName();
    private static String sDomainTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static String getFlightCheckAreaUrl(double latitude, double longitude) {
        return getUrl(IAaskyHttpApi.API_FLIGHT_CHECK_AREA, String.valueOf(latitude), String.valueOf(longitude));
    }

    public static String getFlightUploadConfigUrl() {
        return getUrl(IAaskyHttpApi.API_FLIGHT_UPLOAD_CONFIG, new Object[0]);
    }

    public static String getFlightUploadUrl() {
        return getUrl(IAaskyHttpApi.API_FLIGHT_UPLOAD, new Object[0]);
    }

    public static String getFlightHistoryUrl() {
        return getUrl(IAaskyHttpApi.API_FLIGHT_HISTORY, new Object[0]);
    }

    private static String getUrl(String api, Object... args) {
        if (args == null || args.length <= 0) {
            return generateUrl(api);
        }
        return String.format(Locale.US, generateUrl(api), args);
    }

    private static String generateUrl(String... route) {
        return Util.generateUrl(IAaskyHttpApi.DOMAIN, sDomainTest, route);
    }

    public static String getDomain() {
        return Util.getDomain(IAaskyHttpApi.DOMAIN, sDomainTest);
    }
}
