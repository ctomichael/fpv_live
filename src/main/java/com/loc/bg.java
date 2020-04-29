package com.loc;

import android.text.TextUtils;
import java.net.URLConnection;
import java.util.Map;

/* compiled from: BaseNetManager */
public final class bg {
    public static int a = 0;
    public static String b = "";
    private static bg c;

    /* compiled from: BaseNetManager */
    public interface a {
        URLConnection a();
    }

    public static bg a() {
        if (c == null) {
            c = new bg();
        }
        return c;
    }

    public static bk a(bj bjVar, boolean z) throws t {
        String str;
        if (bjVar == null) {
            try {
                throw new t("requeust is null");
            } catch (t e) {
                throw e;
            } catch (Throwable th) {
                th.printStackTrace();
                throw new t("未知的错误");
            }
        } else if (bjVar.c() == null || "".equals(bjVar.c())) {
            throw new t("request url is empty");
        } else {
            bi biVar = new bi(bjVar.c, bjVar.d, bjVar.e == null ? null : bjVar.e, z);
            byte[] d = bjVar.d();
            if (d == null || d.length == 0) {
                str = bjVar.c();
            } else {
                Map<String, String> b_ = bjVar.b_();
                if (b_ == null) {
                    str = bjVar.c();
                } else {
                    String a2 = bi.a(b_);
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(bjVar.c()).append("?").append(a2);
                    str = stringBuffer.toString();
                }
            }
            boolean k = bjVar.k();
            String j = bjVar.j();
            Map<String, String> b2 = bjVar.b();
            byte[] d2 = bjVar.d();
            if (d2 == null || d2.length == 0) {
                String a3 = bi.a(bjVar.b_());
                if (!TextUtils.isEmpty(a3)) {
                    d2 = ad.a(a3);
                }
            }
            return biVar.a(str, k, j, b2, d2, bjVar.l());
        }
    }

    public static byte[] a(bj bjVar) throws t {
        try {
            bk a2 = a(bjVar, true);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (t e) {
            throw e;
        } catch (Throwable th) {
            throw new t("未知的错误");
        }
    }

    public static byte[] b(bj bjVar) throws t {
        try {
            bk a2 = a(bjVar, false);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (t e) {
            throw e;
        } catch (Throwable th) {
            an.a(th, "bm", "msp");
            throw new t("未知的错误");
        }
    }
}
