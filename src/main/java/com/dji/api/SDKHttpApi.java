package com.dji.api;

import android.text.TextUtils;
import com.dji.api.protocol.ISDKHttpApi;
import com.dji.config.ApiConfig;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;

@EXClassNullAway
public class SDKHttpApi implements ISDKHttpApi {
    private static String sDomainDev = "";
    private static String sDomainStage = "";
    private static String sUserNameDev = "";
    private static String sUserNameStage = "";

    public static void setDev(String domainDev, String userNameDev) {
        if (TextUtils.isEmpty(domainDev) || TextUtils.isEmpty(userNameDev)) {
            sDomainStage = "";
            sUserNameStage = "";
            return;
        }
        sDomainDev = domainDev;
        sUserNameDev = userNameDev;
    }

    public static void setStage(String domainStage, String userNameStage) {
        if (TextUtils.isEmpty(domainStage) || TextUtils.isEmpty(userNameStage)) {
            sDomainStage = "";
            sUserNameStage = "";
            return;
        }
        sDomainStage = domainStage;
        sUserNameStage = userNameStage;
    }

    public static String getSDKRemoteServerUrl() {
        ApiConfig config = ApiConfig.getConfig();
        if (config.isSDKBeta() && !TextUtils.isEmpty(sDomainDev)) {
            return Util.generateUrl(sDomainDev, ISDKHttpApi.API_PREFIX);
        } else if (!config.isDebug() || TextUtils.isEmpty(sDomainStage)) {
            return Util.generateUrl(ISDKHttpApi.DOMAIN, ISDKHttpApi.API_PREFIX);
        } else {
            return Util.generateUrl(sDomainStage, ISDKHttpApi.API_PREFIX);
        }
    }

    public static String getSDKRemoteServerUserName() {
        ApiConfig config = ApiConfig.getConfig();
        if (config.isSDKBeta() && !TextUtils.isEmpty(sUserNameDev)) {
            return sUserNameDev;
        }
        if (!config.isDebug() || TextUtils.isEmpty(sUserNameStage)) {
            return ISDKHttpApi.USER_NAME;
        }
        return sUserNameStage;
    }

    public static String getSDKServerUrl() {
        return ISDKHttpApi.URL_SDK_SERVER;
    }
}
