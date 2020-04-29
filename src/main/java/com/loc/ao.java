package com.loc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.text.TextUtils;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/* compiled from: Log */
public final class ao {
    public static final String a = "/a/";
    static final String b = "b";
    static final String c = "c";
    static final String d = "d";
    static final String e = "i";
    public static final String f = "g";
    public static final String g = "h";
    public static final String h = "e";
    public static final String i = "f";
    public static final String j = "j";

    public static String a(Context context) {
        return c(context, e);
    }

    public static String a(Context context, String str) {
        return context.getSharedPreferences("AMSKLG_CFG", 0).getString(str, "");
    }

    @TargetApi(9)
    public static void a(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("AMSKLG_CFG", 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    static boolean a(String[] strArr, String str) {
        if (strArr == null || str == null) {
            return false;
        }
        try {
            for (String str2 : str.split("\n")) {
                String trim = str2.trim();
                if (!TextUtils.isEmpty(trim) && trim.contains("uncaughtException")) {
                    return false;
                }
                if (b(strArr, trim)) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public static void b(final Context context) {
        try {
            ExecutorService d2 = aq.d();
            if (d2 != null && !d2.isShutdown()) {
                d2.submit(new Runnable() {
                    /* class com.loc.ao.AnonymousClass1 */

                    public final void run() {
                        try {
                            bn.a(context);
                            ar.b(context);
                            ar.d(context);
                            ar.c(context);
                            br.a(context);
                            bp.a(context);
                        } catch (RejectedExecutionException e) {
                        } catch (Throwable th) {
                            aq.b(th, "Lg", "proL");
                        }
                    }
                });
            }
        } catch (Throwable th) {
            aq.b(th, "Lg", "proL");
        }
    }

    public static void b(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("AMSKLG_CFG", 0).edit();
        edit.remove(str);
        edit.apply();
    }

    static boolean b(String[] strArr, String str) {
        if (strArr == null || str == null) {
            return false;
        }
        try {
            for (String str2 : strArr) {
                str = str.trim();
                if (str.startsWith("at ") && str.contains(str2 + ".") && str.endsWith(")") && !str.contains("uncaughtException")) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public static String c(Context context, String str) {
        return context.getFilesDir().getAbsolutePath() + a + str;
    }

    static List<ac> c(Context context) {
        List<ac> list;
        Throwable th;
        List<ac> list2 = null;
        try {
            synchronized (Looper.getMainLooper()) {
                try {
                    list = new ba(context, false).a();
                    try {
                    } catch (Throwable th2) {
                        th = th2;
                        list2 = list;
                        try {
                            throw th;
                        } catch (Throwable th3) {
                            th = th3;
                            list = list2;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    throw th;
                }
            }
        } catch (Throwable th5) {
            th = th5;
            list = null;
            th.printStackTrace();
            return list;
        }
    }
}
