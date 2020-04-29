package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.IFlyForbidHttpApi;
import com.dji.config.ApiConfig;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;
import dji.component.areacode.IAreaCode;
import java.util.Locale;

@EXClassNullAway
public class FlyForbidHttpApi implements IFlyForbidHttpApi {
    private static final String TAG = FlyForbidHttpApi.class.getSimpleName();
    private static String sDomainTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static String getUrlFetchPolygonTfrsAroundUrl() {
        return generateUrl(IFlyForbidHttpApi.API_FETCH_POLYGON_TFRS_AROUND);
    }

    public static String getUrlFetchOldCircleTfrs() {
        return generateUrl(IFlyForbidHttpApi.API_OLD_CIRCLE_TFRS);
    }

    public static String getUrlUserInfo() {
        return generateUrl(IFlyForbidHttpApi.API_USER_INFO);
    }

    public static String getUrlUnlockV3LicenseGroup() {
        return generateUrl(IFlyForbidHttpApi.API_UNLOCK_V3_LICENSE_GROUP);
    }

    public static String getUrlUnlockV3ApplyLicense() {
        return generateUrl(IFlyForbidHttpApi.API_UNLOCK_V3_APPLY_LICENSE);
    }

    public static String getUrlCircleStatic() {
        return generateUrl(IFlyForbidHttpApi.API_OLD_CIRCLE_STATIC_DATA);
    }

    public static String getUrlFetchAllUnlockLicenseUrl() {
        return generateUrl(IFlyForbidHttpApi.API_FETCH_ALL_UNLOCK_LICENSE);
    }

    public static String getUrlGeoUnlockLicenseApplyUrl() {
        return generateUrl(IFlyForbidHttpApi.API_GEO_UNLOCK_LICENSE_APPLY);
    }

    public static String getUrlFlyZoneParamsUrl() {
        if (ApiConfig.getConfig().isDebug() && !ApiConfig.getConfig().isFlyForbidBeta() && !TextUtils.isEmpty(sDomainTest)) {
            return Util.generateUrl(sDomainTest, IFlyForbidHttpApi.API_NO_FLY_ZONE_PARAMS);
        } else if (ApiConfig.getConfig().isDebug() && ApiConfig.getConfig().isFlyForbidBeta() && !TextUtils.isEmpty(sDomainTest)) {
            return Util.generateUrl(sDomainTest, IFlyForbidHttpApi.API_BETA_NO_FLY_ZONE_PARAMS);
        } else if (ApiConfig.getConfig().isFlyForbidBeta()) {
            return Util.generateUrl(IFlyForbidHttpApi.DOMAIN, IFlyForbidHttpApi.API_BETA_NO_FLY_ZONE_PARAMS);
        } else {
            return Util.generateUrl(IFlyForbidHttpApi.DOMAIN, IFlyForbidHttpApi.API_NO_FLY_ZONE_PARAMS);
        }
    }

    public static String getUrlUpdateUrl() {
        return generateUrl(IFlyForbidHttpApi.API_FLY_FORBID_UPDATE);
    }

    public static String getUrlTermsUrl() {
        return generateUrl(IFlyForbidHttpApi.API_FLY_SAFE_TERMS);
    }

    public static String getUrlListUrl() {
        return generateUrl(IFlyForbidHttpApi.API_UNLIMIT_LICENSE_LIST);
    }

    public static String getUrlUnlimitDataUrl() {
        return generateUrl(IFlyForbidHttpApi.API_UMLIMIT_LICENSE);
    }

    public static String getUrlUnlimitUserVerifyUrl() {
        return generateUrl(IFlyForbidHttpApi.API_UNLIMIT_USER_VERIFY);
    }

    public static String getUrlRedirectAirmapVerifyUrl() {
        return generateUrl(IFlyForbidHttpApi.API_REDIRECT_AIRMAP_VERIFY);
    }

    public static String getUrlMobileUnlockAreasUrl() {
        return generateUrl(IFlyForbidHttpApi.API_MOBILE_UNLOCK_AREAS);
    }

    public static String getUrlErrorReportUrl() {
        return generateUrl(IFlyForbidHttpApi.API_NFZ_ERROR_REPORT);
    }

    public static String getUrlUnlockAreaListUrl() {
        return generateUrl(IFlyForbidHttpApi.API_LICENSE_UNLOCK_LIST);
    }

    public static String getUrlUpdateAirmapDataUrl() {
        return generateUrl(IFlyForbidHttpApi.API_UPDATE_AIR_MAP_DATA);
    }

    public static String getNoFlyZoneQueryUrl() {
        return generateUrl(IFlyForbidHttpApi.API_NO_FLY_ZONE_QUERY);
    }

    public static String getAppStaticDataUrl() {
        return generateUrl(IFlyForbidHttpApi.API_APP_STATIC_DATA);
    }

    public static String getOnboardStaticDataUrl() {
        return generateUrl(IFlyForbidHttpApi.API_ONBOARD_STATIC_DATA);
    }

    public static String getWhiteListLicenseListUrl() {
        return generateUrl(IFlyForbidHttpApi.API_WHITE_LIST_LICENSE);
    }

    public static String getQueryUpdateForOnBoardStaticDataUrl() {
        return generateUrl(IFlyForbidHttpApi.API_QUERY_UPDATE_FOR_ONBOARD_STATIC_DATA);
    }

    public static String getSDKStaticDataUrl() {
        return generateUrl(IFlyForbidHttpApi.API_SDK_STATIC_DATA);
    }

    public static String getGetSyncFileFromServerUrl() {
        return generateUrl(IFlyForbidHttpApi.API_GET_SYNC_FILE_FROM_SERVER);
    }

    public static String getUnlockVerifyUrl() {
        return generateUrl(IFlyForbidHttpApi.API_UNLOCK_VERIFY);
    }

    public static String generateUrl(String... route) {
        return Util.generateUrl(IFlyForbidHttpApi.DOMAIN, sDomainTest, route);
    }

    public static String getDomain() {
        return Util.getDomain(IFlyForbidHttpApi.DOMAIN, sDomainTest);
    }

    public static String getRelockUrl() {
        return generateUrl(IFlyForbidHttpApi.API_RELOCK);
    }

    public static String getUnlockTermsByLang() {
        String countryStr = Locale.getDefault().getCountry();
        if (countryStr.equalsIgnoreCase(IAreaCode.STR_AREA_CODE_CHINA) || countryStr.equalsIgnoreCase("TW")) {
            return IFlyForbidHttpApi.URL_UNLOCK_TERMS_CN;
        }
        return IFlyForbidHttpApi.URL_UNLOCK_TERMS_EN;
    }
}
