package com.loc;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import java.security.MessageDigest;
import java.util.Locale;

/* compiled from: AppInfo */
public final class u {
    static String a = null;
    static boolean b = false;
    private static String c = "";
    private static String d = "";
    private static String e = "";
    private static String f = "";

    public static String a(Context context) {
        try {
            return h(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return f;
        }
    }

    static void a(final Context context, final String str) {
        if (!TextUtils.isEmpty(str)) {
            f = str;
            if (context != null) {
                aq.d().submit(new Runnable() {
                    /* class com.loc.u.AnonymousClass1 */

                    /* JADX WARNING: Removed duplicated region for block: B:17:0x0043 A[SYNTHETIC, Splitter:B:17:0x0043] */
                    /* JADX WARNING: Removed duplicated region for block: B:23:0x004f A[SYNTHETIC, Splitter:B:23:0x004f] */
                    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public final void run() {
                        /*
                            r4 = this;
                            r1 = 0
                            android.content.Context r0 = r2     // Catch:{ Throwable -> 0x0037 }
                            java.lang.String r2 = "k.store"
                            java.lang.String r0 = com.loc.ao.c(r0, r2)     // Catch:{ Throwable -> 0x0037 }
                            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0037 }
                            r3.<init>(r0)     // Catch:{ Throwable -> 0x0037 }
                            java.io.File r0 = r3.getParentFile()     // Catch:{ Throwable -> 0x0037 }
                            boolean r0 = r0.exists()     // Catch:{ Throwable -> 0x0037 }
                            if (r0 != 0) goto L_0x0020
                            java.io.File r0 = r3.getParentFile()     // Catch:{ Throwable -> 0x0037 }
                            r0.mkdirs()     // Catch:{ Throwable -> 0x0037 }
                        L_0x0020:
                            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0037 }
                            r2.<init>(r3)     // Catch:{ Throwable -> 0x0037 }
                            java.lang.String r0 = r3     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
                            byte[] r0 = com.loc.ad.a(r0)     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
                            r2.write(r0)     // Catch:{ Throwable -> 0x005b, all -> 0x0058 }
                            r2.close()     // Catch:{ Throwable -> 0x0032 }
                        L_0x0031:
                            return
                        L_0x0032:
                            r0 = move-exception
                            r0.printStackTrace()
                            goto L_0x0031
                        L_0x0037:
                            r0 = move-exception
                        L_0x0038:
                            java.lang.String r2 = "AI"
                            java.lang.String r3 = "stf"
                            com.loc.an.a(r0, r2, r3)     // Catch:{ all -> 0x004c }
                            if (r1 == 0) goto L_0x0031
                            r1.close()     // Catch:{ Throwable -> 0x0047 }
                            goto L_0x0031
                        L_0x0047:
                            r0 = move-exception
                            r0.printStackTrace()
                            goto L_0x0031
                        L_0x004c:
                            r0 = move-exception
                        L_0x004d:
                            if (r1 == 0) goto L_0x0052
                            r1.close()     // Catch:{ Throwable -> 0x0053 }
                        L_0x0052:
                            throw r0
                        L_0x0053:
                            r1 = move-exception
                            r1.printStackTrace()
                            goto L_0x0052
                        L_0x0058:
                            r0 = move-exception
                            r1 = r2
                            goto L_0x004d
                        L_0x005b:
                            r0 = move-exception
                            r1 = r2
                            goto L_0x0038
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.loc.u.AnonymousClass1.run():void");
                    }
                });
            }
        }
    }

    public static void a(String str) {
        d = str;
    }

    static boolean a() {
        try {
            if (b) {
                return true;
            }
            if (b(a)) {
                b = true;
                return true;
            } else if (!TextUtils.isEmpty(a)) {
                b = false;
                a = null;
                return false;
            } else if (b(d)) {
                b = true;
                return true;
            } else if (TextUtils.isEmpty(d)) {
                return true;
            } else {
                b = false;
                d = null;
                return false;
            }
        } catch (Throwable th) {
            return true;
        }
    }

    public static String b(Context context) {
        try {
            if (!"".equals(c)) {
                return c;
            }
            PackageManager packageManager = context.getPackageManager();
            c = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
            return c;
        } catch (Throwable th) {
            an.a(th, "AI", "gAN");
        }
    }

    private static boolean b(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        str.toCharArray();
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        int i = 0;
        while (i < length) {
            char c2 = charArray[i];
            if (('A' > c2 || c2 > 'z') && (('0' > c2 || c2 > ':') && c2 != '.')) {
                try {
                    aq.b(ad.a(), str, "errorPackage");
                    return false;
                } catch (Throwable th) {
                    return false;
                }
            } else {
                i++;
            }
        }
        return true;
    }

    public static String c(Context context) {
        try {
            if (d != null && !"".equals(d)) {
                return d;
            }
            String packageName = context.getPackageName();
            d = packageName;
            if (!b(packageName)) {
                d = context.getPackageName();
            }
            return d;
        } catch (Throwable th) {
            an.a(th, "AI", "gpck");
        }
    }

    public static String d(Context context) {
        try {
            if (!"".equals(e)) {
                return e;
            }
            e = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return e == null ? "" : e;
        } catch (Throwable th) {
            an.a(th, "AI", "gAV");
        }
    }

    public static String e(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
            byte[] digest = MessageDigest.getInstance("SHA1").digest(packageInfo.signatures[0].toByteArray());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                String upperCase = Integer.toHexString(b2 & 255).toUpperCase(Locale.US);
                if (upperCase.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(upperCase);
                stringBuffer.append(":");
            }
            String str = packageInfo.packageName;
            if (b(str)) {
                str = packageInfo.packageName;
            }
            if (!TextUtils.isEmpty(d)) {
                str = c(context);
            }
            stringBuffer.append(str);
            String stringBuffer2 = stringBuffer.toString();
            a = stringBuffer2;
            return stringBuffer2;
        } catch (Throwable th) {
            an.a(th, "AI", "gsp");
            return a;
        }
    }

    public static String f(Context context) {
        try {
            return h(context);
        } catch (Throwable th) {
            an.a(th, "AI", "gKy");
            return f;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x004f A[Catch:{ Throwable -> 0x005b, all -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0054 A[SYNTHETIC, Splitter:B:25:0x0054] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String g(android.content.Context r5) {
        /*
            java.lang.String r0 = "k.store"
            java.lang.String r0 = com.loc.ao.c(r5, r0)
            java.io.File r3 = new java.io.File
            r3.<init>(r0)
            boolean r0 = r3.exists()
            if (r0 != 0) goto L_0x0016
            java.lang.String r0 = ""
        L_0x0015:
            return r0
        L_0x0016:
            r2 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x003e, all -> 0x0071 }
            r1.<init>(r3)     // Catch:{ Throwable -> 0x003e, all -> 0x0071 }
            int r0 = r1.available()     // Catch:{ Throwable -> 0x0074 }
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x0074 }
            r1.read(r0)     // Catch:{ Throwable -> 0x0074 }
            java.lang.String r0 = com.loc.ad.a(r0)     // Catch:{ Throwable -> 0x0074 }
            int r2 = r0.length()     // Catch:{ Throwable -> 0x0074 }
            r4 = 32
            if (r2 != r4) goto L_0x003a
        L_0x0031:
            r1.close()     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0015
        L_0x0035:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0015
        L_0x003a:
            java.lang.String r0 = ""
            goto L_0x0031
        L_0x003e:
            r0 = move-exception
            r1 = r2
        L_0x0040:
            java.lang.String r2 = "AI"
            java.lang.String r4 = "gKe"
            com.loc.an.a(r0, r2, r4)     // Catch:{ all -> 0x0060 }
            boolean r0 = r3.exists()     // Catch:{ Throwable -> 0x005b }
            if (r0 == 0) goto L_0x0052
            r3.delete()     // Catch:{ Throwable -> 0x005b }
        L_0x0052:
            if (r1 == 0) goto L_0x0057
            r1.close()     // Catch:{ Throwable -> 0x0067 }
        L_0x0057:
            java.lang.String r0 = ""
            goto L_0x0015
        L_0x005b:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0060 }
            goto L_0x0052
        L_0x0060:
            r0 = move-exception
        L_0x0061:
            if (r1 == 0) goto L_0x0066
            r1.close()     // Catch:{ Throwable -> 0x006c }
        L_0x0066:
            throw r0
        L_0x0067:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0057
        L_0x006c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0066
        L_0x0071:
            r0 = move-exception
            r1 = r2
            goto L_0x0061
        L_0x0074:
            r0 = move-exception
            goto L_0x0040
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.u.g(android.content.Context):java.lang.String");
    }

    private static String h(Context context) throws PackageManager.NameNotFoundException {
        if (f == null || f.equals("")) {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                return f;
            }
            String string = applicationInfo.metaData.getString("com.amap.api.v2.apikey");
            f = string;
            if (string == null) {
                f = g(context);
            }
        }
        return f;
    }
}
