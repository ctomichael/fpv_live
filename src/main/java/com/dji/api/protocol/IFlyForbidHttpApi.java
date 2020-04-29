package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IFlyForbidHttpApi extends IHttpApi {
    public static final String API_APP_STATIC_DATA = "api/v3/geofence/app_static_data";
    public static final String API_BETA_NO_FLY_ZONE_PARAMS = "api/beta/geo_fence/noflyzone_params/?";
    public static final String API_FETCH_ALL_UNLOCK_LICENSE = "api/v3/geofence_unlock/list_unlimited_areas";
    public static final String API_FETCH_POLYGON_TFRS_AROUND = "api/v3/geofence/tfrs_around";
    public static final String API_FLY_FORBID_UPDATE = "api/release_limitarea.json/?updated_at=";
    public static final String API_FLY_SAFE_TERMS = "api/v3/flysafe_terms/geo";
    public static final String API_GEO_UNLOCK_LICENSE_APPLY = "api/v3/geofence_unlock/mobile_unlock_areas";
    public static final String API_GET_SYNC_FILE_FROM_SERVER = "api/v1/geo_fence/get_geo_file";
    public static final String API_LICENSE_UNLOCK_LIST = "api/v1/geo_fence/list_unlimited_areas";
    public static final String API_MOBILE_UNLOCK_AREAS = "api/mobile_unlock_areas/?";
    public static final String API_NFZ_ERROR_REPORT = "api/v3/flysafe_feedback/nfz_error_report";
    public static final String API_NO_FLY_ZONE_PARAMS = "api/v1/geo_fence/noflyzone_params/?";
    public static final String API_NO_FLY_ZONE_QUERY = "api/v3/geo/query_page";
    public static final String API_OLD_CIRCLE_STATIC_DATA = "api/v3/circle/static_data";
    public static final String API_OLD_CIRCLE_TFRS = "api/v3/circle/tfrs";
    public static final String API_ONBOARD_STATIC_DATA = "api/v3/geofence/onboard_static_data";
    public static final String API_QUERY_UPDATE_FOR_ONBOARD_STATIC_DATA = "api/v3/geofence/query_update_for_onboard_static_data";
    public static final String API_REDIRECT_AIRMAP_VERIFY = "api/airmap_verify/?version=1.0";
    public static final String API_RELOCK = "api/v3/geofence_unlock/disable_unlock_license";
    public static final String API_SDK_STATIC_DATA = "api/v3/geofence/sdk_static_data";
    public static final String API_UMLIMIT_LICENSE = "api/unlimit_license";
    public static final String API_UNLIMIT_LICENSE_LIST = "api/unlimit_license_list";
    public static final String API_UNLIMIT_USER_VERIFY = "api/v3/flysafe_users/unlimit_user_verify/?";
    public static final String API_UNLOCK_V3_APPLY_LICENSE = "api/v4/mobile/unlock_license_groups/areas";
    public static final String API_UNLOCK_V3_LICENSE_GROUP = "api/v4/mobile/unlock_license_groups";
    public static final String API_UNLOCK_VERIFY = "api/unlock/v1/sms";
    public static final String API_UPDATE_AIR_MAP_DATA = "api/v1/geo_fence/fetch_corrections?updated_at=";
    public static final String API_USER_INFO = "api/v4/mobile/user";
    public static final String API_WHITE_LIST_LICENSE = "api/v3/geofence_unlock/whitelist_license";
    public static final String DOMAIN = "https://flysafe-api.dji.com";
    public static final String URL_UNLOCK_TERMS_CN = "https://flysafe.dji.com/cn/terms/unlock ";
    public static final String URL_UNLOCK_TERMS_EN = "https://flysafe.dji.com/en/terms/unlock";
}
