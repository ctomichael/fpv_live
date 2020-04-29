package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IFlightHttpApi extends IHttpApi {
    public static final String API_ASSIST_NOW_OFFLINE_DATA = "api/gnss_service/assistnow_offline_data?timestamp=%s&signature=%s";
    public static final String API_AWS_STS_TOKEN = "api/obtain_sts_token";
    public static final String API_COUNTRY_CODE_URL = "api/v2/geocoder_service/";
    public static final String API_DJI_POPUP_V2 = "api/djigo/popupv2";
    public static final String API_FIRMWARE_RELEASE_NOTE = "api/v2/firmware/release_note";
    public static final String API_FIRMWARE_UPGRADE_LOG = "api/v2/firmware/upgrade_log";
    public static final String API_FLIGHT_DELETE = "flight/delete?token=%1$s";
    public static final String API_FLIGHT_DOWNLOAD = "flight/download?token=%1$s&filename=%2$s";
    public static final String API_FLIGHT_DOWNLOAD_ZIP = "flight/zipdownload?token=%1$s&filename=%2$s";
    public static final String API_FLIGHT_INFO = "flight/info/upload?token=%1$s&filename=%2$s";
    public static final String API_FLIGHT_LIST = "flight/list?token=%1$s&page=%2$d&pagesize=%3$d";
    public static final String API_FLIGHT_OVERVIEW = "flight/overview?token=%1$s";
    public static final String API_FLIGHT_OVERVIEW_INFO = "flight/query/userinfo?token=%s";
    public static final String API_FLIGHT_UPLOAD = "flight/upload?token=%1$s&filename=%2$s";
    public static final String API_FLIGHT_UPLOAD_ZIP_NEW = "api/v2/flight_log/zipupload";
    public static final String API_GEOCODER_SERVICE_GEO_IP = "api/v2/geocoder_service/geoip";
    public static final String API_LICENSES_STATUS_CHECK = "api/v2/licenses/status_check";
    public static final String API_LOAD_CONFIG_GET_URL = "loadconfig/geturl";
    public static final String API_REGISTER_DEVICE = "api/v2/register_device";
    public static final String API_USER_LEVEL = "api/v2/flight_log/profile?user_id=%1$s";
    public static final String DOMAIN = "https://mydjiflight.dji.com";
}
