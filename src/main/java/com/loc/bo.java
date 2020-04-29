package com.loc;

import android.content.Context;

/* compiled from: OfflineLocEntity */
public final class bo {
    private Context a;
    private ac b;
    private String c;

    public bo(Context context, ac acVar, String str) {
        this.a = context.getApplicationContext();
        this.b = acVar;
        this.c = str;
    }

    private static String a(Context context, ac acVar, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sdkversion\":\"").append(acVar.c()).append("\",\"product\":\"").append(acVar.a()).append("\",\"nt\":\"").append(x.d(context)).append("\",\"details\":").append(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public final byte[] a() {
        return ad.a(a(this.a, this.b, this.c));
    }
}
