package com.loc;

import android.content.Context;
import dji.component.accountcenter.IMemberProtocol;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BinaryRequest */
public abstract class bh extends bj {
    protected Context a;
    protected ac b;

    public bh(Context context, ac acVar) {
        if (context != null) {
            this.a = context.getApplicationContext();
        }
        this.b = acVar;
    }

    protected static byte[] a(byte[] bArr) {
        int length = bArr.length;
        return new byte[]{(byte) (length / 256), (byte) (length % 256)};
    }

    private static byte[] m() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(ad.a("PANDORA$"));
            byteArrayOutputStream.write(new byte[]{1});
            byteArrayOutputStream.write(new byte[]{0});
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                return byteArray;
            } catch (Throwable th) {
                an.a(th, "bre", "gbh");
                return byteArray;
            }
        } catch (Throwable th2) {
            an.a(th2, "bre", "gbh");
        }
        return null;
    }

    private byte[] n() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(new byte[]{3});
            if (g()) {
                byte[] a2 = w.a(this.a, i());
                byteArrayOutputStream.write(a(a2));
                byteArrayOutputStream.write(a2);
            } else {
                byteArrayOutputStream.write(new byte[]{0, 0});
            }
            byte[] a3 = ad.a(f());
            if (a3 == null || a3.length <= 0) {
                byteArrayOutputStream.write(new byte[]{0, 0});
            } else {
                byteArrayOutputStream.write(a(a3));
                byteArrayOutputStream.write(a3);
            }
            byte[] a4 = ad.a(h());
            if (a4 == null || a4.length <= 0) {
                byteArrayOutputStream.write(new byte[]{0, 0});
            } else {
                byteArrayOutputStream.write(a(a4));
                byteArrayOutputStream.write(a4);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                return byteArray;
            } catch (Throwable th) {
                an.a(th, "bre", "gred");
                return byteArray;
            }
        } catch (Throwable th2) {
            an.a(th2, "bre", "gred");
        }
        return new byte[]{0};
    }

    private byte[] o() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] a_ = a_();
            if (a_ == null || a_.length == 0) {
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray;
                } catch (Throwable th) {
                    an.a(th, "bre", "grrd");
                    return byteArray;
                }
            } else {
                byteArrayOutputStream.write(new byte[]{1});
                byteArrayOutputStream.write(a(a_));
                byteArrayOutputStream.write(a_);
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray2;
                } catch (Throwable th2) {
                    an.a(th2, "bre", "grrd");
                    return byteArray2;
                }
            }
        } catch (Throwable th3) {
            an.a(th3, "bre", "grrd");
        }
        return new byte[]{0};
    }

    private byte[] p() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] e = e();
            if (e == null || e.length == 0) {
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray;
                } catch (Throwable th) {
                    an.a(th, "bre", "gred");
                    return byteArray;
                }
            } else {
                byteArrayOutputStream.write(new byte[]{1});
                byte[] a2 = y.a(e);
                byteArrayOutputStream.write(a(a2));
                byteArrayOutputStream.write(a2);
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray2;
                } catch (Throwable th2) {
                    an.a(th2, "bre", "gred");
                    return byteArray2;
                }
            }
        } catch (Throwable th3) {
            an.a(th3, "bre", "gred");
        }
        return new byte[]{0};
    }

    public abstract byte[] a_();

    public Map<String, String> b_() {
        String f = u.f(this.a);
        String a2 = w.a();
        String a3 = w.a(this.a, a2, "key=" + f);
        HashMap hashMap = new HashMap();
        hashMap.put("ts", a2);
        hashMap.put(IMemberProtocol.STRING_KEY, f);
        hashMap.put("scode", a3);
        return hashMap;
    }

    public final byte[] d() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(m());
            byteArrayOutputStream.write(n());
            byteArrayOutputStream.write(o());
            byteArrayOutputStream.write(p());
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                return byteArray;
            } catch (Throwable th) {
                an.a(th, "bre", "geb");
                return byteArray;
            }
        } catch (Throwable th2) {
            an.a(th2, "bre", "geb");
        }
        return null;
    }

    public abstract byte[] e();

    /* access modifiers changed from: protected */
    public String f() {
        return "2.1";
    }

    public boolean g() {
        return true;
    }

    public String h() {
        return String.format("platform=Android&sdkversion=%s&product=%s", this.b.c(), this.b.a());
    }

    /* access modifiers changed from: protected */
    public boolean i() {
        return false;
    }
}
