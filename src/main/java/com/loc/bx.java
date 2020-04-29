package com.loc;

import android.content.Context;
import android.os.Build;
import java.io.ByteArrayOutputStream;

/* compiled from: StatisticsHeaderDataStrategy */
public final class bx extends bz {
    public static int a = 13;
    public static int b = 6;
    private Context e;

    public bx(Context context, bz bzVar) {
        super(bzVar);
        this.e = context;
    }

    private static byte[] a(Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[0];
        try {
            ad.a(byteArrayOutputStream, "1.2." + a + "." + b);
            ad.a(byteArrayOutputStream, "Android");
            ad.a(byteArrayOutputStream, x.u(context));
            ad.a(byteArrayOutputStream, x.l(context));
            ad.a(byteArrayOutputStream, x.g(context));
            ad.a(byteArrayOutputStream, Build.MANUFACTURER);
            ad.a(byteArrayOutputStream, Build.MODEL);
            ad.a(byteArrayOutputStream, Build.DEVICE);
            ad.a(byteArrayOutputStream, x.x(context));
            ad.a(byteArrayOutputStream, u.c(context));
            ad.a(byteArrayOutputStream, u.d(context));
            ad.a(byteArrayOutputStream, u.f(context));
            byteArrayOutputStream.write(new byte[]{0});
            bArr = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th) {
                th = th;
                th.printStackTrace();
                return bArr;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] a(byte[] bArr) {
        byte[] a2 = a(this.e);
        byte[] bArr2 = new byte[(a2.length + bArr.length)];
        System.arraycopy(a2, 0, bArr2, 0, a2.length);
        System.arraycopy(bArr, 0, bArr2, a2.length, bArr.length);
        return bArr2;
    }
}
