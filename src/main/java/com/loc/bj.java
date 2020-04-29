package com.loc;

import android.text.TextUtils;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.net.Proxy;
import java.util.Map;

/* compiled from: Request */
public abstract class bj {
    int c = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
    int d = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
    Proxy e = null;

    public final void a(int i) {
        this.c = i;
    }

    public final void a(Proxy proxy) {
        this.e = proxy;
    }

    public abstract Map<String, String> b();

    public final void b(int i) {
        this.d = i;
    }

    public abstract Map<String, String> b_();

    public abstract String c();

    public byte[] d() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String j() {
        return "";
    }

    /* access modifiers changed from: protected */
    public final boolean k() {
        return !TextUtils.isEmpty(j());
    }

    public boolean l() {
        return false;
    }
}
