package com.loc;

import android.content.Context;
import android.os.Build;
import com.drew.metadata.exif.ExifDirectoryBase;
import dji.utils.TimeUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ErrorLogManager */
public class ar {
    /* access modifiers changed from: private */
    public static WeakReference<bl> a;
    private static boolean b = true;
    private static WeakReference<cf> c;
    private static WeakReference<cf> d;
    private static String[] e = new String[10];
    private static int f = 0;
    private static boolean g = false;
    private static int h = 0;
    private static ac i;

    private static ac a(Context context, String str) {
        List<ac> c2 = ao.c(context);
        if (c2 == null) {
            c2 = new ArrayList();
        }
        if (str == null || "".equals(str)) {
            return null;
        }
        for (ac acVar : c2) {
            if (ao.a(acVar.f(), str)) {
                return acVar;
            }
        }
        if (str.contains("com.amap.api.col")) {
            try {
                return ad.a();
            } catch (t e2) {
                e2.printStackTrace();
            }
        }
        if (str.contains("com.amap.co") || str.contains("com.amap.opensdk.co") || str.contains("com.amap.location")) {
            try {
                ac b2 = ad.b();
                b2.a(true);
                return b2;
            } catch (t e3) {
                e3.printStackTrace();
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00b8, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00b9, code lost:
        com.loc.aq.b(r0, "alg", "aDa");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00c4, code lost:
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00d0, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00d1, code lost:
        r2 = "alg";
        r3 = "getA";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0112, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0113, code lost:
        com.loc.aq.b(r0, "alg", "getA");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00b6 A[Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }, ExcHandler: EOFException (e java.io.EOFException), Splitter:B:18:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00c3 A[ExcHandler: FileNotFoundException (e java.io.FileNotFoundException), Splitter:B:18:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00c7 A[SYNTHETIC, Splitter:B:61:0x00c7] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00cc A[SYNTHETIC, Splitter:B:64:0x00cc] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00ef A[SYNTHETIC, Splitter:B:76:0x00ef] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x00f4 A[SYNTHETIC, Splitter:B:79:0x00f4] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0106 A[SYNTHETIC, Splitter:B:86:0x0106] */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x010b A[SYNTHETIC, Splitter:B:89:0x010b] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x010f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String a(java.util.List<com.loc.ac> r8) {
        /*
            r6 = 1024000(0xfa000, float:1.43493E-39)
            r5 = 0
            r1 = 0
            r0 = 0
            r2 = 0
            java.io.File r4 = new java.io.File     // Catch:{ FileNotFoundException -> 0x0179, Throwable -> 0x0170, all -> 0x0101 }
            java.lang.String r3 = "/data/anr/traces.txt"
            r4.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0179, Throwable -> 0x0170, all -> 0x0101 }
            boolean r3 = r4.exists()     // Catch:{ FileNotFoundException -> 0x0179, Throwable -> 0x0170, all -> 0x0101 }
            if (r3 != 0) goto L_0x0021
            if (r1 == 0) goto L_0x001a
            r2.close()     // Catch:{ Throwable -> 0x013e }
        L_0x001a:
            if (r1 == 0) goto L_0x001f
            r0.close()     // Catch:{ Throwable -> 0x014a }
        L_0x001f:
            r0 = r1
        L_0x0020:
            return r0
        L_0x0021:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0179, Throwable -> 0x0170, all -> 0x0101 }
            r3.<init>(r4)     // Catch:{ FileNotFoundException -> 0x0179, Throwable -> 0x0170, all -> 0x0101 }
            int r0 = r3.available()     // Catch:{ FileNotFoundException -> 0x017e, Throwable -> 0x0175, all -> 0x016b }
            if (r0 <= r6) goto L_0x0031
            int r0 = r0 - r6
            long r6 = (long) r0     // Catch:{ FileNotFoundException -> 0x017e, Throwable -> 0x0175, all -> 0x016b }
            r3.skip(r6)     // Catch:{ FileNotFoundException -> 0x017e, Throwable -> 0x0175, all -> 0x016b }
        L_0x0031:
            com.loc.bd r2 = new com.loc.bd     // Catch:{ FileNotFoundException -> 0x017e, Throwable -> 0x0175, all -> 0x016b }
            java.nio.charset.Charset r0 = com.loc.be.a     // Catch:{ FileNotFoundException -> 0x017e, Throwable -> 0x0175, all -> 0x016b }
            r2.<init>(r3, r0)     // Catch:{ FileNotFoundException -> 0x017e, Throwable -> 0x0175, all -> 0x016b }
        L_0x0038:
            java.lang.String r0 = r2.a()     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            java.lang.String r0 = r0.trim()     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            java.lang.String r4 = "pid"
            boolean r4 = r0.contains(r4)     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            if (r4 == 0) goto L_0x0182
        L_0x0049:
            java.lang.String r4 = "\"main\""
            boolean r4 = r0.startsWith(r4)     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            if (r4 != 0) goto L_0x0057
            java.lang.String r0 = r2.a()     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            goto L_0x0049
        L_0x0057:
            r5 = 1
            r4 = r0
        L_0x0059:
            java.lang.String r0 = ""
            boolean r0 = r4.equals(r0)     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            if (r0 == 0) goto L_0x0077
            if (r5 == 0) goto L_0x0077
        L_0x0064:
            if (r2 == 0) goto L_0x0069
            r2.close()     // Catch:{ Throwable -> 0x0156 }
        L_0x0069:
            if (r3 == 0) goto L_0x006e
            r3.close()     // Catch:{ Throwable -> 0x0162 }
        L_0x006e:
            boolean r0 = com.loc.ar.g
            if (r0 == 0) goto L_0x010f
            java.lang.String r0 = b()
            goto L_0x0020
        L_0x0077:
            if (r5 == 0) goto L_0x0038
            int r0 = com.loc.ar.f     // Catch:{ Throwable -> 0x00b8, EOFException -> 0x00b6, FileNotFoundException -> 0x00c3 }
            r6 = 9
            if (r0 <= r6) goto L_0x0082
            r0 = 0
            com.loc.ar.f = r0     // Catch:{ Throwable -> 0x00b8, EOFException -> 0x00b6, FileNotFoundException -> 0x00c3 }
        L_0x0082:
            java.lang.String[] r0 = com.loc.ar.e     // Catch:{ Throwable -> 0x00b8, EOFException -> 0x00b6, FileNotFoundException -> 0x00c3 }
            int r6 = com.loc.ar.f     // Catch:{ Throwable -> 0x00b8, EOFException -> 0x00b6, FileNotFoundException -> 0x00c3 }
            r0[r6] = r4     // Catch:{ Throwable -> 0x00b8, EOFException -> 0x00b6, FileNotFoundException -> 0x00c3 }
            int r0 = com.loc.ar.f     // Catch:{ Throwable -> 0x00b8, EOFException -> 0x00b6, FileNotFoundException -> 0x00c3 }
            int r0 = r0 + 1
            com.loc.ar.f = r0     // Catch:{ Throwable -> 0x00b8, EOFException -> 0x00b6, FileNotFoundException -> 0x00c3 }
        L_0x008e:
            int r0 = com.loc.ar.h     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            r6 = 5
            if (r0 == r6) goto L_0x0064
            boolean r0 = com.loc.ar.g     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            if (r0 != 0) goto L_0x00db
            java.util.Iterator r6 = r8.iterator()     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
        L_0x009b:
            boolean r0 = r6.hasNext()     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            if (r0 == 0) goto L_0x0038
            java.lang.Object r0 = r6.next()     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            com.loc.ac r0 = (com.loc.ac) r0     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            java.lang.String[] r7 = r0.f()     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            boolean r7 = com.loc.ao.b(r7, r4)     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            com.loc.ar.g = r7     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            if (r7 == 0) goto L_0x009b
            com.loc.ar.i = r0     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            goto L_0x0038
        L_0x00b6:
            r0 = move-exception
            goto L_0x0064
        L_0x00b8:
            r0 = move-exception
            java.lang.String r6 = "alg"
            java.lang.String r7 = "aDa"
            com.loc.aq.b(r0, r6, r7)     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            goto L_0x008e
        L_0x00c3:
            r0 = move-exception
            r0 = r2
        L_0x00c5:
            if (r0 == 0) goto L_0x00ca
            r0.close()     // Catch:{ Throwable -> 0x0112 }
        L_0x00ca:
            if (r3 == 0) goto L_0x006e
            r3.close()     // Catch:{ Throwable -> 0x00d0 }
            goto L_0x006e
        L_0x00d0:
            r0 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r3 = "getA"
        L_0x00d7:
            com.loc.aq.b(r0, r2, r3)
            goto L_0x006e
        L_0x00db:
            int r0 = com.loc.ar.h     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            int r0 = r0 + 1
            com.loc.ar.h = r0     // Catch:{ EOFException -> 0x00b6, FileNotFoundException -> 0x00c3, Throwable -> 0x00e3 }
            goto L_0x0038
        L_0x00e3:
            r0 = move-exception
        L_0x00e4:
            java.lang.String r4 = "alg"
            java.lang.String r5 = "getA"
            com.loc.aq.b(r0, r4, r5)     // Catch:{ all -> 0x016e }
            if (r2 == 0) goto L_0x00f2
            r2.close()     // Catch:{ Throwable -> 0x0133 }
        L_0x00f2:
            if (r3 == 0) goto L_0x006e
            r3.close()     // Catch:{ Throwable -> 0x00f9 }
            goto L_0x006e
        L_0x00f9:
            r0 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r3 = "getA"
            goto L_0x00d7
        L_0x0101:
            r0 = move-exception
            r2 = r1
            r3 = r1
        L_0x0104:
            if (r2 == 0) goto L_0x0109
            r2.close()     // Catch:{ Throwable -> 0x011d }
        L_0x0109:
            if (r3 == 0) goto L_0x010e
            r3.close()     // Catch:{ Throwable -> 0x0128 }
        L_0x010e:
            throw r0
        L_0x010f:
            r0 = r1
            goto L_0x0020
        L_0x0112:
            r0 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r4 = "getA"
            com.loc.aq.b(r0, r2, r4)
            goto L_0x00ca
        L_0x011d:
            r1 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r4 = "getA"
            com.loc.aq.b(r1, r2, r4)
            goto L_0x0109
        L_0x0128:
            r1 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r3 = "getA"
            com.loc.aq.b(r1, r2, r3)
            goto L_0x010e
        L_0x0133:
            r0 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r4 = "getA"
            com.loc.aq.b(r0, r2, r4)
            goto L_0x00f2
        L_0x013e:
            r2 = move-exception
            java.lang.String r3 = "alg"
            java.lang.String r4 = "getA"
            com.loc.aq.b(r2, r3, r4)
            goto L_0x001a
        L_0x014a:
            r0 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r3 = "getA"
            com.loc.aq.b(r0, r2, r3)
            goto L_0x001f
        L_0x0156:
            r0 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r4 = "getA"
            com.loc.aq.b(r0, r2, r4)
            goto L_0x0069
        L_0x0162:
            r0 = move-exception
            java.lang.String r2 = "alg"
            java.lang.String r3 = "getA"
            goto L_0x00d7
        L_0x016b:
            r0 = move-exception
            r2 = r1
            goto L_0x0104
        L_0x016e:
            r0 = move-exception
            goto L_0x0104
        L_0x0170:
            r0 = move-exception
            r2 = r1
            r3 = r1
            goto L_0x00e4
        L_0x0175:
            r0 = move-exception
            r2 = r1
            goto L_0x00e4
        L_0x0179:
            r0 = move-exception
            r0 = r1
            r3 = r1
            goto L_0x00c5
        L_0x017e:
            r0 = move-exception
            r0 = r1
            goto L_0x00c5
        L_0x0182:
            r4 = r0
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ar.a(java.util.List):java.lang.String");
    }

    static void a(Context context) {
        String a2;
        List<ac> c2 = ao.c(context);
        if (c2 != null && c2.size() != 0 && (a2 = a(c2)) != null && !"".equals(a2) && i != null) {
            a(context, i, 2, "ANR", a2);
        }
    }

    private static void a(Context context, ac acVar, int i2, String str, String str2) {
        String str3;
        String a2 = ad.a(System.currentTimeMillis());
        String a3 = bs.a(context, acVar);
        u.a(context);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(a3).append(",\"timestamp\":\"");
        stringBuffer.append(a2);
        stringBuffer.append("\",\"et\":\"");
        stringBuffer.append(i2);
        stringBuffer.append("\",\"classname\":\"");
        stringBuffer.append(str);
        stringBuffer.append("\",");
        stringBuffer.append("\"detail\":\"");
        stringBuffer.append(str2);
        stringBuffer.append("\"");
        String stringBuffer2 = stringBuffer.toString();
        if (stringBuffer2 != null && !"".equals(stringBuffer2)) {
            String c2 = aa.c(str2);
            if (i2 == 1) {
                str3 = ao.b;
            } else if (i2 == 2) {
                str3 = ao.d;
            } else if (i2 == 0) {
                str3 = ao.c;
            } else {
                return;
            }
            bl a4 = bs.a(a);
            bs.a(context, a4, str3, 1000, ExifDirectoryBase.TAG_FLASHPIX_VERSION, "1");
            if (a4.e == null) {
                a4.e = new af(new ag(new ai(new ak())));
            }
            try {
                bm.a(c2, ad.a(stringBuffer2.replaceAll("\n", "<br/>")), a4);
            } catch (Throwable th) {
            }
        }
    }

    private static void a(final Context context, final cf cfVar, final String str) {
        aq.d().submit(new Runnable() {
            /* class com.loc.ar.AnonymousClass1 */

            public final void run() {
                try {
                    synchronized (ar.class) {
                        bl a2 = bs.a(ar.a);
                        bs.a(context, a2, str, 1000, ExifDirectoryBase.TAG_FLASHPIX_VERSION, "1");
                        a2.f = cfVar;
                        if (a2.g == null) {
                            a2.g = new bw(new bv(context, new ca(), new ag(new ai(new ak())), "EImtleSI6IiVzIiwicGxhdGZvcm0iOiJhbmRyb2lkIiwiZGl1IjoiJXMiLCJwa2ciOiIlcyIsIm1vZGVsIjoiJXMiLCJhcHBuYW1lIjoiJXMiLCJhcHB2ZXJzaW9uIjoiJXMiLCJzeXN2ZXJzaW9uIjoiJXMiLA=", u.f(context), x.u(context), u.c(context), Build.MODEL, u.b(context), u.d(context), Build.VERSION.RELEASE));
                        }
                        a2.h = TimeUtils.TIMECONSTANT_HOUR;
                        bm.a(a2);
                    }
                } catch (Throwable th) {
                    aq.b(th, "lg", "pul");
                }
            }
        });
    }

    public static void a(Context context, Throwable th, int i2, String str, String str2) {
        String a2 = ad.a(th);
        ac a3 = a(context, a2);
        if (a(a3)) {
            String replaceAll = a2.replaceAll("\n", "<br/>");
            String th2 = th.toString();
            if (th2 != null && !"".equals(th2)) {
                StringBuilder sb = new StringBuilder();
                if (str != null) {
                    sb.append("class:").append(str);
                }
                if (str2 != null) {
                    sb.append(" method:").append(str2).append("$<br/>");
                }
                sb.append(replaceAll);
                a(context, a3, i2, th2, sb.toString());
            }
        }
    }

    static void a(ac acVar, Context context, String str, String str2) {
        if (a(acVar) && str != null && !"".equals(str)) {
            a(context, acVar, 0, str, str2);
        }
    }

    private static boolean a(ac acVar) {
        return acVar != null && acVar.e();
    }

    private static String b() {
        StringBuilder sb = new StringBuilder();
        try {
            int i2 = f;
            while (i2 < 10 && i2 <= 9) {
                sb.append(e[i2]);
                i2++;
            }
            for (int i3 = 0; i3 < f; i3++) {
                sb.append(e[i3]);
            }
        } catch (Throwable th) {
            aq.b(th, "alg", "gLI");
        }
        return sb.toString();
    }

    static void b(Context context) {
        cd cdVar = new cd(b);
        b = false;
        a(context, cdVar, ao.c);
    }

    static void b(ac acVar, Context context, String str, String str2) {
        if (a(acVar) && str != null && !"".equals(str)) {
            a(context, acVar, 1, str, str2);
        }
    }

    static void c(Context context) {
        if (c == null || c.get() == null) {
            c = new WeakReference<>(new ce(context, TimeUtils.TIMECONSTANT_HOUR, "hKey", new cg(context)));
        }
        a(context, c.get(), ao.d);
    }

    static void d(Context context) {
        if (d == null || d.get() == null) {
            d = new WeakReference<>(new ce(context, TimeUtils.TIMECONSTANT_HOUR, "gKey", new cg(context)));
        }
        a(context, d.get(), ao.b);
    }
}
