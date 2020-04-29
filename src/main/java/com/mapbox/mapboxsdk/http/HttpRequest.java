package com.mapbox.mapboxsdk.http;

public interface HttpRequest {
    public static final int CONNECTION_ERROR = 0;
    public static final int PERMANENT_ERROR = 2;
    public static final int TEMPORARY_ERROR = 1;

    void cancelRequest();

    void executeRequest(HttpResponder httpResponder, long j, String str, String str2, String str3, boolean z);
}
