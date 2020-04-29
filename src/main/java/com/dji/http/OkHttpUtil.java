package com.dji.http;

import com.dji.frame.util.V_AppUtils;
import dji.log.DJILog;
import dji.thirdparty.afinal.utils.HttpsHelper;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static HttpLoggingInterceptor mDebugLogInterceptor = new HttpLoggingInterceptor(OkHttpUtil$$Lambda$0.$instance).setLevel(HttpLoggingInterceptor.Level.NONE);
    private static OkHttpClient sOkHttpClientWithSSL;
    private static OkHttpClient sOkHttpClientWithoutSSL;

    static final /* synthetic */ void lambda$static$0$OkHttpUtil(String message) {
        while (message.length() > 2001) {
            DJILog.logWriteD(TAG, message.substring(0, 2001), new Object[0]);
            message = message.substring(2001);
        }
        DJILog.logWriteD(TAG, message, new Object[0]);
    }

    public static synchronized OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClientWithoutSSL;
        synchronized (OkHttpUtil.class) {
            if (V_AppUtils.sSSLSwitch) {
                okHttpClientWithoutSSL = getOkHttpClientWithSSL();
            } else {
                okHttpClientWithoutSSL = getOkHttpClientWithoutSSL();
            }
        }
        return okHttpClientWithoutSSL;
    }

    private static synchronized OkHttpClient getOkHttpClientWithoutSSL() {
        OkHttpClient okHttpClient;
        synchronized (OkHttpUtil.class) {
            if (sOkHttpClientWithoutSSL == null) {
                sOkHttpClientWithoutSSL = new OkHttpClient.Builder().addInterceptor(mDebugLogInterceptor).connectTimeout(30, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
            }
            okHttpClient = sOkHttpClientWithoutSSL;
        }
        return okHttpClient;
    }

    private static synchronized OkHttpClient getOkHttpClientWithSSL() {
        OkHttpClient okHttpClient;
        synchronized (OkHttpUtil.class) {
            if (sOkHttpClientWithSSL == null) {
                sOkHttpClientWithSSL = new OkHttpClient.Builder().addInterceptor(mDebugLogInterceptor).connectTimeout(30, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true).sslSocketFactory(HttpsHelper.getDJISSLSocketFactoryForJavax(), HttpsHelper.getTrustManager()).build();
            }
            okHttpClient = sOkHttpClientWithSSL;
        }
        return okHttpClient;
    }

    public static void setDebug(boolean debug) {
        if (debug) {
            mDebugLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            mDebugLogInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    public static boolean isDebug() {
        return mDebugLogInterceptor.getLevel() != HttpLoggingInterceptor.Level.NONE;
    }
}
