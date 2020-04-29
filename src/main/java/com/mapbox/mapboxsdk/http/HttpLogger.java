package com.mapbox.mapboxsdk.http;

import com.mapbox.mapboxsdk.log.Logger;

public class HttpLogger {
    private static final String TAG = "Mbgl-HttpRequest";
    public static boolean logEnabled = true;
    public static boolean logRequestUrl;

    private HttpLogger() {
    }

    public static void logFailure(int type, String errorMessage, String requestUrl) {
        int i = type == 1 ? 3 : type == 0 ? 4 : 5;
        Object[] objArr = new Object[3];
        objArr[0] = type == 1 ? "temporary" : type == 0 ? "connection" : "permanent";
        objArr[1] = errorMessage;
        if (!logRequestUrl) {
            requestUrl = "";
        }
        objArr[2] = requestUrl;
        log(i, String.format("Request failed due to a %s error: %s %s", objArr));
    }

    public static void log(int type, String errorMessage) {
        if (logEnabled) {
            Logger.log(type, TAG, errorMessage);
        }
    }
}
