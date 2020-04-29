package com.mapbox.mapboxsdk.http;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.http.LocalRequestTask;
import java.util.concurrent.locks.ReentrantLock;

@Keep
public class NativeHttpRequest implements HttpResponder {
    private final HttpRequest httpRequest = Mapbox.getModuleProvider().createHttpRequest();
    /* access modifiers changed from: private */
    public final ReentrantLock lock = new ReentrantLock();
    /* access modifiers changed from: private */
    @Keep
    public long nativePtr;

    @Keep
    private native void nativeOnFailure(int i, String str);

    /* access modifiers changed from: private */
    @Keep
    public native void nativeOnResponse(int i, String str, String str2, String str3, String str4, String str5, String str6, byte[] bArr);

    @Keep
    private NativeHttpRequest(long nativePtr2, String resourceUrl, String etag, String modified, boolean offlineUsage) {
        this.nativePtr = nativePtr2;
        if (resourceUrl.startsWith("local://")) {
            executeLocalRequest(resourceUrl);
        } else {
            this.httpRequest.executeRequest(this, nativePtr2, resourceUrl, etag, modified, offlineUsage);
        }
    }

    public void cancel() {
        this.httpRequest.cancelRequest();
        this.lock.lock();
        this.nativePtr = 0;
        this.lock.unlock();
    }

    public void onResponse(int responseCode, String etag, String lastModified, String cacheControl, String expires, String retryAfter, String xRateLimitReset, byte[] body) {
        this.lock.lock();
        if (this.nativePtr != 0) {
            nativeOnResponse(responseCode, etag, lastModified, cacheControl, expires, retryAfter, xRateLimitReset, body);
        }
        this.lock.unlock();
    }

    private void executeLocalRequest(String resourceUrl) {
        new LocalRequestTask(new LocalRequestTask.OnLocalRequestResponse() {
            /* class com.mapbox.mapboxsdk.http.NativeHttpRequest.AnonymousClass1 */

            public void onResponse(@Nullable byte[] bytes) {
                if (bytes != null) {
                    NativeHttpRequest.this.lock.lock();
                    if (NativeHttpRequest.this.nativePtr != 0) {
                        NativeHttpRequest.this.nativeOnResponse(200, null, null, null, null, null, null, bytes);
                    }
                    NativeHttpRequest.this.lock.unlock();
                }
            }
        }).execute(resourceUrl);
    }

    public void handleFailure(int type, String errorMessage) {
        this.lock.lock();
        if (this.nativePtr != 0) {
            nativeOnFailure(type, errorMessage);
        }
        this.lock.unlock();
    }
}
