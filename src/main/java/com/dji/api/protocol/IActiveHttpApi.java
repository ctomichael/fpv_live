package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IActiveHttpApi extends IHttpApi {
    public static final String API_ACTIVATION_TERM = "api/v2/activation/term";
    public static final String API_ACTIVE_VERIFY = "verify";
    public static final String API_EAGLE_ACTIVE = "api/v3/eagle/activation";
    public static final String API_GET_SERVER_LOCATION = "api/v2/user/server_location";
    public static final String API_PHONE_ASSOCIATED_BINDING = "bind_service/associated_binding";
    public static final String API_PHONE_AUTHENTICATION = "bind_service/authentication";
    public static final String API_PHONE_BIND_SERVICE = "bind_service";
    public static final String API_PHONE_DIRECT_BINDING = "bind_service/direct_binding";
    public static final String API_PHONE_OBTAIN_BINDED_MOBILE = "bind_service/obtain_binded_mobile";
    public static final String API_PHONE_VERIFICATION = "bind_service/verification";
    public static final String API_POST_ERROR_LOG = "errorlog";
    public static final String API_SCAN_CODE_ACTIVATION = "api/v2/scan_code/activation";
    public static final String API_SCAN_CODE_DECRYPT_SN = "api/v2/scan_code/decrypt_sn";
    public static final String DOMAIN = "https://active.dji.com";
}
