package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ISDKHttpApi extends IHttpApi {
    public static final String API_PREFIX = "api/v1/";
    public static final String DOMAIN = "https://api.dji-services.com";
    public static final String URL_SDK_SERVER = "https://dev.dji.com/sdk?";
    public static final String USER_NAME = "4961b54ae8813317d5b094a336de371f";
}
