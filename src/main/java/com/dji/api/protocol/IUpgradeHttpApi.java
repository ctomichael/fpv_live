package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IUpgradeHttpApi extends IHttpApi {
    public static final String API_GET_DATE = "getdayv3";
    public static final String API_PARAM_PILOT = "?os=android&build=%1$s";
    public static final String API_PILOT_BR = "links/links/pilot_br";
    public static final String API_PILOT_BR_DEBUG = "redirect/links/pliot_br_DEBUG";
    public static final String API_PILOT_BR_WITH_PARAM = "links/links/pilot_br?os=android&build=%1$s";
    public static final String API_PILOT_V2 = "links/links/pilot_v2";
    public static final String API_PILOT_V2_DEBUG = "redirect/links/GO_Debug";
    public static final String API_PILOT_V2_TEST = "redirect/links/GO_Test";
    public static final String API_PILOT_V2_WITH_PARAM = "links/links/pilot_v2?os=android&build=%1$s";
    public static final String DOMAIN = "https://mydjiflight.dji.com";
    public static final String INNER_DOMAIN = "https://firmware-dl.djicorp.com";
}
