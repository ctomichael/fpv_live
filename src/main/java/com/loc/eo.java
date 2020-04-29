package com.loc;

import android.content.Context;
import android.text.TextUtils;
import java.util.Map;

/* compiled from: LocationRequest */
public final class eo extends bh {
    Map<String, String> f = null;
    String g = "";
    byte[] h = null;
    byte[] i = null;
    boolean j = false;
    String k = null;
    Map<String, String> l = null;
    boolean m = false;
    private String n = "";

    public eo(Context context, ac acVar) {
        super(context, acVar);
    }

    public final void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.n = str;
        } else {
            this.n = "";
        }
    }

    public final byte[] a_() {
        return this.h;
    }

    public final Map<String, String> b() {
        return this.f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0028 A[SYNTHETIC, Splitter:B:16:0x0028] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0035 A[SYNTHETIC, Splitter:B:23:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void b(byte[] r4) {
        /*
            r3 = this;
            r2 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0021, all -> 0x0031 }
            r1.<init>()     // Catch:{ Throwable -> 0x0021, all -> 0x0031 }
            if (r4 == 0) goto L_0x0012
            byte[] r0 = a(r4)     // Catch:{ Throwable -> 0x0040 }
            r1.write(r0)     // Catch:{ Throwable -> 0x0040 }
            r1.write(r4)     // Catch:{ Throwable -> 0x0040 }
        L_0x0012:
            byte[] r0 = r1.toByteArray()     // Catch:{ Throwable -> 0x0040 }
            r3.i = r0     // Catch:{ Throwable -> 0x0040 }
            r1.close()     // Catch:{ IOException -> 0x001c }
        L_0x001b:
            return
        L_0x001c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x001b
        L_0x0021:
            r0 = move-exception
            r1 = r2
        L_0x0023:
            r0.printStackTrace()     // Catch:{ all -> 0x003e }
            if (r1 == 0) goto L_0x001b
            r1.close()     // Catch:{ IOException -> 0x002c }
            goto L_0x001b
        L_0x002c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x001b
        L_0x0031:
            r0 = move-exception
            r1 = r2
        L_0x0033:
            if (r1 == 0) goto L_0x0038
            r1.close()     // Catch:{ IOException -> 0x0039 }
        L_0x0038:
            throw r0
        L_0x0039:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0038
        L_0x003e:
            r0 = move-exception
            goto L_0x0033
        L_0x0040:
            r0 = move-exception
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eo.b(byte[]):void");
    }

    public final Map<String, String> b_() {
        return this.l;
    }

    public final String c() {
        return this.g;
    }

    public final byte[] e() {
        return this.i;
    }

    public final boolean g() {
        return this.j;
    }

    public final String h() {
        return this.k;
    }

    /* access modifiers changed from: protected */
    public final boolean i() {
        return this.m;
    }

    /* access modifiers changed from: protected */
    public final String j() {
        return this.n;
    }
}
