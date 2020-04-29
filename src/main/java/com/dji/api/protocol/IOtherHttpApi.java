package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IOtherHttpApi extends IHttpApi {
    public static final String URL_AIRMAP_ERROR_REPORT = "https://api.airmap.io/data/v1/error-report";
    public static final String URL_DEVICE_CHECK = "https://lmsz.djicorp.com/WebApiGo/SN/GetSnStatusInfoByGo";
    public static final String URL_NET_MUSIC = "https://sp-webfront.skypixel.com/djigo/music/template/config_music_android.json";
    public static final String URL_NET_MUSIC_V2 = "https://djigo.aasky.net/api/v1/music/list";
    public static final String URL_ONBOARD_SDK_ACTIVATE = "https://dev.dji.com/api/onboardsdk/activate";
    public static final String URL_REPAIR_UPLOAD_ATTACHMENT = "https://djigoaftersale.djiservice.org/addon/";
    public static final String URL_UPGRADE_INFO = "https://upgrade.dj2006.net/upgrade/inspireinfo";
}
