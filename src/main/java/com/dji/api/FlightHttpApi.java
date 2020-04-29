package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.IFlightHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;
import java.util.Locale;

@EXClassNullAway
public class FlightHttpApi implements IFlightHttpApi {
    private static final String TAG = FlightHttpApi.class.getSimpleName();
    private static String sDomainTest = "";
    private static String sDomainUnencrypted = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static void setDomainUnencrypted(String domainUnencrypted) {
        sDomainUnencrypted = domainUnencrypted;
    }

    public static String getFlightUploadUrl(String token, String fileName) {
        return getUrl(IFlightHttpApi.API_FLIGHT_UPLOAD, token, fileName);
    }

    public static String getFlightDownloadUrl() {
        return getUrl(IFlightHttpApi.API_FLIGHT_DOWNLOAD, new Object[0]);
    }

    public static String getFlightDeleteUrl(String token) {
        return getUrl(IFlightHttpApi.API_FLIGHT_DELETE, token);
    }

    public static String getFlightListUrl(String token, int page, int pageSize) {
        return getUrl(IFlightHttpApi.API_FLIGHT_LIST, token, Integer.valueOf(page), Integer.valueOf(pageSize));
    }

    public static String getFlightOverviewUrl(String token) {
        return getUrl(IFlightHttpApi.API_FLIGHT_OVERVIEW, token);
    }

    public static String getFlightUploadZipNewUrl() {
        return getUrl(IFlightHttpApi.API_FLIGHT_UPLOAD_ZIP_NEW, new Object[0]);
    }

    public static String getFlightDownloadZipUrl(String token, String fileName) {
        return getUrl(IFlightHttpApi.API_FLIGHT_DOWNLOAD_ZIP, token, fileName);
    }

    public static String getFlightInfoUrl(String token, String fileName) {
        return getUrl(IFlightHttpApi.API_FLIGHT_INFO, token, fileName);
    }

    public static String getFlightOverviewInfoUrl(String token) {
        return getUrl(IFlightHttpApi.API_FLIGHT_OVERVIEW_INFO, token);
    }

    public static String getFirmwareUpgradeLogUrl() {
        return getUrl(IFlightHttpApi.API_FIRMWARE_UPGRADE_LOG, new Object[0]);
    }

    public static String getFirmwareReleaseNoteUrl() {
        return getUrl(IFlightHttpApi.API_FIRMWARE_RELEASE_NOTE, new Object[0]);
    }

    public static String getLoadConfigGetUrlUrl() {
        return getUrl(IFlightHttpApi.API_LOAD_CONFIG_GET_URL, new Object[0]);
    }

    public static String getAssistNowOfflineDataUrl(int timestamp, String signature) {
        return getUrl(IFlightHttpApi.API_ASSIST_NOW_OFFLINE_DATA, String.valueOf(timestamp), signature);
    }

    public static String getRegisterDeviceUrl() {
        return getUrl(IFlightHttpApi.API_REGISTER_DEVICE, new Object[0]);
    }

    public static String getLicensesStatusCheckUrl() {
        return getUrl(IFlightHttpApi.API_LICENSES_STATUS_CHECK, new Object[0]);
    }

    public static String getDJIPopupV2Url() {
        return getUrl(IFlightHttpApi.API_DJI_POPUP_V2, new Object[0]);
    }

    public static String getGeocoderServiceGeoIpUrl() {
        return getUrl(IFlightHttpApi.API_GEOCODER_SERVICE_GEO_IP, new Object[0]);
    }

    public static String getUserLevelUrl(String id) {
        return getUrl(IFlightHttpApi.API_USER_LEVEL, id);
    }

    public static String getCountryCodeUrl() {
        return getUrl(IFlightHttpApi.API_COUNTRY_CODE_URL, new Object[0]);
    }

    private static String getUrl(String api, Object... args) {
        if (args == null || args.length <= 0) {
            return generateUrl(api);
        }
        return String.format(Locale.US, generateUrl(api), args);
    }

    private static String generateUrl(String... route) {
        if (route == null || route.length <= 0 || !IFlightHttpApi.API_LOAD_CONFIG_GET_URL.equals(route[0]) || TextUtils.isEmpty(sDomainUnencrypted)) {
            return Util.generateUrl("https://mydjiflight.dji.com", sDomainTest, route);
        }
        return Util.generateUrl("https://mydjiflight.dji.com", sDomainUnencrypted, route);
    }

    public static String getDomain() {
        return Util.getDomain("https://mydjiflight.dji.com", sDomainTest);
    }
}
