package com.dji.http;

import okhttp3.logging.HttpLoggingInterceptor;

final /* synthetic */ class OkHttpUtil$$Lambda$0 implements HttpLoggingInterceptor.Logger {
    static final HttpLoggingInterceptor.Logger $instance = new OkHttpUtil$$Lambda$0();

    private OkHttpUtil$$Lambda$0() {
    }

    public void log(String str) {
        OkHttpUtil.lambda$static$0$OkHttpUtil(str);
    }
}
