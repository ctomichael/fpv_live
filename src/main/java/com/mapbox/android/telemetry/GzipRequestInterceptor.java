package com.mapbox.android.telemetry;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

final class GzipRequestInterceptor implements Interceptor {
    private static final int THREAD_ID = 10000;

    GzipRequestInterceptor() {
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest);
        }
        return chain.proceed(originalRequest.newBuilder().header("Content-Encoding", "gzip").method(originalRequest.method(), gzip(originalRequest.body())).build());
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            /* class com.mapbox.android.telemetry.GzipRequestInterceptor.AnonymousClass1 */

            public MediaType contentType() {
                return body.contentType();
            }

            public long contentLength() {
                return -1;
            }

            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
