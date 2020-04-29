package com.mapbox.mapboxsdk.module.http;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import com.mapbox.mapboxsdk.BuildConfig;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.http.HttpIdentifier;
import com.mapbox.mapboxsdk.http.HttpLogger;
import com.mapbox.mapboxsdk.http.HttpRequest;
import com.mapbox.mapboxsdk.http.HttpRequestUrl;
import com.mapbox.mapboxsdk.http.HttpResponder;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequestImpl implements HttpRequest {
    @VisibleForTesting
    static final OkHttpClient DEFAULT_CLIENT = new OkHttpClient.Builder().dispatcher(getDispatcher()).build();
    @VisibleForTesting
    static OkHttpClient client = DEFAULT_CLIENT;
    private static final String userAgentString = HttpRequestUtil.toHumanReadableAscii(String.format("%s %s (%s) Android/%s (%s)", HttpIdentifier.getIdentifier(), BuildConfig.MAPBOX_VERSION_STRING, BuildConfig.GIT_REVISION_SHORT, Integer.valueOf(Build.VERSION.SDK_INT), Build.CPU_ABI));
    private Call call;

    public void executeRequest(HttpResponder httpRequest, long nativePtr, @NonNull String resourceUrl, @NonNull String etag, @NonNull String modified, boolean offlineUsage) {
        OkHttpCallback callback = new OkHttpCallback(httpRequest);
        try {
            HttpUrl httpUrl = HttpUrl.parse(resourceUrl);
            if (httpUrl == null) {
                HttpLogger.log(6, String.format("[HTTP] Unable to parse resourceUrl %s", resourceUrl));
                return;
            }
            String resourceUrl2 = HttpRequestUrl.buildResourceUrl(httpUrl.host().toLowerCase(MapboxConstants.MAPBOX_LOCALE), resourceUrl, httpUrl.querySize(), offlineUsage);
            Request.Builder builder = new Request.Builder().url(resourceUrl2).tag(resourceUrl2.toLowerCase(MapboxConstants.MAPBOX_LOCALE)).addHeader("User-Agent", userAgentString);
            if (etag.length() > 0) {
                builder.addHeader("If-None-Match", etag);
            } else if (modified.length() > 0) {
                builder.addHeader("If-Modified-Since", modified);
            }
            this.call = client.newCall(builder.build());
            this.call.enqueue(callback);
        } catch (Exception exception) {
            callback.handleFailure(this.call, exception);
        }
    }

    public void cancelRequest() {
        if (this.call != null) {
            this.call.cancel();
        }
    }

    public static void enablePrintRequestUrlOnFailure(boolean enabled) {
        HttpLogger.logRequestUrl = enabled;
    }

    public static void enableLog(boolean enabled) {
        HttpLogger.logEnabled = enabled;
    }

    public static void setOkHttpClient(@Nullable OkHttpClient okHttpClient) {
        if (okHttpClient != null) {
            client = okHttpClient;
        } else {
            client = DEFAULT_CLIENT;
        }
    }

    private static class OkHttpCallback implements Callback {
        private HttpResponder httpRequest;

        OkHttpCallback(HttpResponder httpRequest2) {
            this.httpRequest = httpRequest2;
        }

        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            handleFailure(call, e);
        }

        public void onResponse(@NonNull Call call, @NonNull Response response) {
            if (response.isSuccessful()) {
                HttpLogger.log(2, String.format("[HTTP] Request was successful (code = %s).", Integer.valueOf(response.code())));
            } else {
                HttpLogger.log(3, String.format("[HTTP] Request with response = %s: %s", Integer.valueOf(response.code()), !TextUtils.isEmpty(response.message()) ? response.message() : "No additional information"));
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                HttpLogger.log(6, "[HTTP] Received empty response body");
                return;
            }
            try {
                byte[] body = responseBody.bytes();
                response.close();
                this.httpRequest.onResponse(response.code(), response.header("ETag"), response.header("Last-Modified"), response.header("Cache-Control"), response.header("Expires"), response.header("Retry-After"), response.header("x-rate-limit-reset"), body);
            } catch (IOException ioException) {
                onFailure(call, ioException);
                response.close();
            } catch (Throwable th) {
                response.close();
                throw th;
            }
        }

        /* access modifiers changed from: private */
        public void handleFailure(@Nullable Call call, Exception e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Error processing the request";
            int type = getFailureType(e);
            if (!(!HttpLogger.logEnabled || call == null || call.request() == null)) {
                HttpLogger.logFailure(type, errorMessage, call.request().url().toString());
            }
            this.httpRequest.handleFailure(type, errorMessage);
        }

        private int getFailureType(Exception e) {
            if ((e instanceof NoRouteToHostException) || (e instanceof UnknownHostException) || (e instanceof SocketException) || (e instanceof ProtocolException) || (e instanceof SSLException)) {
                return 0;
            }
            if (e instanceof InterruptedIOException) {
                return 1;
            }
            return 2;
        }
    }

    @NonNull
    private static Dispatcher getDispatcher() {
        Dispatcher dispatcher = new Dispatcher();
        if (Build.VERSION.SDK_INT >= 21) {
            dispatcher.setMaxRequestsPerHost(20);
        } else {
            dispatcher.setMaxRequestsPerHost(10);
        }
        return dispatcher;
    }
}
