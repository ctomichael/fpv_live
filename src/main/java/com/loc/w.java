package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;

/* compiled from: ClientInfo */
public final class w {

    /* compiled from: ClientInfo */
    private static class a {
        String a;
        String b;
        String c;
        String d;
        String e;
        String f;
        String g;
        String h;
        String i;
        String j;
        String k;
        String l;
        String m;
        String n;
        String o;
        String p;
        String q;
        String r;
        String s;
        String t;
        String u;
        String v;
        String w;
        String x;
        String y;

        private a() {
        }

        /* synthetic */ a(byte b2) {
            this();
        }
    }

    public static String a() {
        try {
            String valueOf = String.valueOf(System.currentTimeMillis());
            String str = "1";
            if (!u.a()) {
                str = "0";
            }
            int length = valueOf.length();
            return valueOf.substring(0, length - 2) + str + valueOf.substring(length - 1);
        } catch (Throwable th) {
            an.a(th, "CI", "TS");
            return null;
        }
    }

    public static String a(Context context, String str, String str2) {
        try {
            return aa.b(u.e(context) + ":" + str.substring(0, str.length() - 3) + ":" + str2);
        } catch (Throwable th) {
            an.a(th, "CI", "Sco");
            return null;
        }
    }

    private static void a(ByteArrayOutputStream byteArrayOutputStream, String str) {
        if (!TextUtils.isEmpty(str)) {
            ad.a(byteArrayOutputStream, str.getBytes().length > 255 ? -1 : (byte) str.getBytes().length, ad.a(str));
        } else {
            ad.a(byteArrayOutputStream, (byte) 0, new byte[0]);
        }
    }

    public static byte[] a(Context context, boolean z) {
        try {
            a aVar = new a((byte) 0);
            aVar.a = x.u(context);
            aVar.b = x.l(context);
            String g = x.g(context);
            if (g == null) {
                g = "";
            }
            aVar.c = g;
            aVar.d = u.c(context);
            aVar.e = Build.MODEL;
            aVar.f = Build.MANUFACTURER;
            aVar.g = Build.DEVICE;
            aVar.h = u.b(context);
            aVar.i = u.d(context);
            aVar.j = String.valueOf(Build.VERSION.SDK_INT);
            aVar.k = x.x(context);
            aVar.l = x.t(context);
            aVar.m = new StringBuilder().append(x.q(context)).toString();
            aVar.n = new StringBuilder().append(x.p(context)).toString();
            aVar.o = x.z(context);
            aVar.p = x.o(context);
            if (z) {
                aVar.q = "";
            } else {
                aVar.q = x.k(context);
            }
            if (z) {
                aVar.r = "";
            } else {
                aVar.r = x.j(context);
            }
            if (z) {
                aVar.s = "";
                aVar.t = "";
            } else {
                String[] m = x.m(context);
                aVar.s = m[0];
                aVar.t = m[1];
            }
            aVar.w = x.a();
            String a2 = x.a(context);
            if (!TextUtils.isEmpty(a2)) {
                aVar.x = a2;
            } else {
                aVar.x = "";
            }
            aVar.y = "aid=" + x.i(context) + "|serial=" + x.h(context) + "|storage=" + x.d() + "|ram=" + x.y(context) + "|arch=" + x.e();
            String b = x.b();
            if (!TextUtils.isEmpty(b)) {
                aVar.y += "|adiuExtras=" + b;
            }
            String a3 = x.a(context, ",");
            if (!TextUtils.isEmpty(a3)) {
                aVar.y += "|multiImeis=" + a3;
            }
            String w = x.w(context);
            if (!TextUtils.isEmpty(w)) {
                aVar.y += "|meid=" + w;
            }
            return a(aVar);
        } catch (Throwable th) {
            an.a(th, "CI", "gz");
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00e4 A[SYNTHETIC, Splitter:B:27:0x00e4] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] a(com.loc.w.a r8) {
        /*
            r1 = 0
            r5 = 117(0x75, float:1.64E-43)
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x00c9, all -> 0x00e0 }
            r2.<init>()     // Catch:{ Throwable -> 0x00c9, all -> 0x00e0 }
            java.lang.String r0 = r8.a     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.b     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.c     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.d     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.e     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.f     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.g     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.h     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.i     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.j     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.k     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.l     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.m     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.n     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.o     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.p     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.q     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.r     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.s     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.t     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.u     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.v     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.w     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.x     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            java.lang.String r0 = r8.y     // Catch:{ Throwable -> 0x00ef }
            a(r2, r0)     // Catch:{ Throwable -> 0x00ef }
            byte[] r0 = r2.toByteArray()     // Catch:{ Throwable -> 0x00ef }
            byte[] r3 = com.loc.ad.b(r0)     // Catch:{ Throwable -> 0x00ef }
            java.security.PublicKey r0 = com.loc.ad.d()     // Catch:{ Throwable -> 0x00ef }
            int r4 = r3.length     // Catch:{ Throwable -> 0x00ef }
            if (r4 <= r5) goto L_0x00bf
            r4 = 117(0x75, float:1.64E-43)
            byte[] r4 = new byte[r4]     // Catch:{ Throwable -> 0x00ef }
            r5 = 0
            r6 = 0
            r7 = 117(0x75, float:1.64E-43)
            java.lang.System.arraycopy(r3, r5, r4, r6, r7)     // Catch:{ Throwable -> 0x00ef }
            byte[] r4 = com.loc.y.a(r4, r0)     // Catch:{ Throwable -> 0x00ef }
            int r0 = r3.length     // Catch:{ Throwable -> 0x00ef }
            int r0 = r0 + 128
            int r0 = r0 + -117
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x00ef }
            r5 = 0
            r6 = 0
            r7 = 128(0x80, float:1.794E-43)
            java.lang.System.arraycopy(r4, r5, r0, r6, r7)     // Catch:{ Throwable -> 0x00ef }
            r4 = 117(0x75, float:1.64E-43)
            r5 = 128(0x80, float:1.794E-43)
            int r6 = r3.length     // Catch:{ Throwable -> 0x00ef }
            int r6 = r6 + -117
            java.lang.System.arraycopy(r3, r4, r0, r5, r6)     // Catch:{ Throwable -> 0x00ef }
        L_0x00bb:
            r2.close()     // Catch:{ Throwable -> 0x00c4 }
        L_0x00be:
            return r0
        L_0x00bf:
            byte[] r0 = com.loc.y.a(r3, r0)     // Catch:{ Throwable -> 0x00ef }
            goto L_0x00bb
        L_0x00c4:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00be
        L_0x00c9:
            r0 = move-exception
            r2 = r1
        L_0x00cb:
            java.lang.String r3 = "CI"
            java.lang.String r4 = "gzx"
            com.loc.an.a(r0, r3, r4)     // Catch:{ all -> 0x00ed }
            if (r2 == 0) goto L_0x00d9
            r2.close()     // Catch:{ Throwable -> 0x00db }
        L_0x00d9:
            r0 = r1
            goto L_0x00be
        L_0x00db:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00d9
        L_0x00e0:
            r0 = move-exception
            r2 = r1
        L_0x00e2:
            if (r2 == 0) goto L_0x00e7
            r2.close()     // Catch:{ Throwable -> 0x00e8 }
        L_0x00e7:
            throw r0
        L_0x00e8:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00e7
        L_0x00ed:
            r0 = move-exception
            goto L_0x00e2
        L_0x00ef:
            r0 = move-exception
            goto L_0x00cb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.w.a(com.loc.w$a):byte[]");
    }
}
