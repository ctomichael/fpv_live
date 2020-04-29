package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IAaskyHttpApi extends IHttpApi {
    public static final String API_FLIGHT_CHECK_AREA = "config/%1$s/%2$s";
    public static final String API_FLIGHT_HISTORY = "history_record_upload/secret/gzip";
    public static final String API_FLIGHT_UPLOAD = "flying_record_upload/secret/dji";
    public static final String API_FLIGHT_UPLOAD_CONFIG = "config";
    public static final String DOMAIN = "https://fffdrone.aasky.net";
}
