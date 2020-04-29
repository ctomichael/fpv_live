package com.loc;

import android.content.Context;
import android.provider.Settings;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: UTUtdid */
public final class cz {
    private static final Object b = new Object();
    private static cz c = null;
    private static final String j = (".UTSystemConfig" + File.separator + "Global");
    private Context a = null;
    private String d = null;
    private da e = null;
    private String f = "xx_utdid_key";
    private String g = "xx_utdid_domain";
    private dz h = null;
    private dz i = null;
    private Pattern k = Pattern.compile("[^0-9a-zA-Z=/+]+");

    private cz(Context context) {
        this.a = context;
        this.i = new dz(context, j, "Alvin2");
        this.h = new dz(context, ".DataStorage", "ContextData");
        this.e = new da();
        this.f = String.format("K_%d", Integer.valueOf(dw.b(this.f)));
        this.g = String.format("D_%d", Integer.valueOf(dw.b(this.g)));
    }

    public static cz a(Context context) {
        if (context != null && c == null) {
            synchronized (b) {
                if (c == null) {
                    c = new cz(context);
                }
            }
        }
        return c;
    }

    private void a(String str) {
        long j2;
        if (e(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.length() == 24 && this.i != null) {
                String a2 = this.i.a("UTDID");
                String a3 = this.i.a("EI");
                if (dw.a(a3)) {
                    a3 = dv.a(this.a);
                }
                String a4 = this.i.a("SI");
                if (dw.a(a4)) {
                    a4 = dv.b(this.a);
                }
                String a5 = this.i.a("DID");
                if (dw.a(a5)) {
                    a5 = a3;
                }
                if (a2 == null || !a2.equals(str)) {
                    cx cxVar = new cx();
                    cxVar.a(a3);
                    cxVar.b(a4);
                    cxVar.d(str);
                    cxVar.c(a5);
                    cxVar.b(System.currentTimeMillis());
                    this.i.a("UTDID", str);
                    this.i.a("EI", cxVar.b());
                    this.i.a("SI", cxVar.c());
                    this.i.a("DID", cxVar.d());
                    this.i.a("timestamp", cxVar.a());
                    dz dzVar = this.i;
                    String format = String.format("%s%s%s%s%s", cxVar.e(), cxVar.d(), Long.valueOf(cxVar.a()), cxVar.c(), cxVar.b());
                    if (!dw.a(format)) {
                        Adler32 adler32 = new Adler32();
                        adler32.reset();
                        adler32.update(format.getBytes());
                        j2 = adler32.getValue();
                    } else {
                        j2 = 0;
                    }
                    dzVar.a("S", j2);
                    this.i.a();
                }
            }
        }
    }

    private void b(String str) {
        if (str != null && this.h != null && !str.equals(this.h.a(this.f))) {
            this.h.a(this.f, str);
            this.h.a();
        }
    }

    private final byte[] b() throws Exception {
        String sb;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        int nextInt = new Random().nextInt();
        byte[] a2 = du.a(currentTimeMillis);
        byte[] a3 = du.a(nextInt);
        byteArrayOutputStream.write(a2, 0, 4);
        byteArrayOutputStream.write(a3, 0, 4);
        byteArrayOutputStream.write(3);
        byteArrayOutputStream.write(0);
        try {
            sb = dv.a(this.a);
        } catch (Exception e2) {
            sb = new StringBuilder().append(new Random().nextInt()).toString();
        }
        byteArrayOutputStream.write(du.a(dw.b(sb)), 0, 4);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Mac instance = Mac.getInstance("HmacSHA1");
        instance.init(new SecretKeySpec("d6fc3a4a06adbde89223bvefedc24fecde188aaa9161".getBytes(), instance.getAlgorithm()));
        byteArrayOutputStream.write(du.a(dw.b(dt.a(instance.doFinal(byteArray), 2))));
        return byteArrayOutputStream.toByteArray();
    }

    private void c(String str) {
        if (this.a.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && e(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (24 == str.length() && !e(Settings.System.getString(this.a.getContentResolver(), "mqBRboGZkQPcAkyk"))) {
                Settings.System.putString(this.a.getContentResolver(), "mqBRboGZkQPcAkyk", str);
            }
        }
    }

    private void d(String str) {
        if (this.a.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && str != null && !str.equals(Settings.System.getString(this.a.getContentResolver(), "dxCRMxhQkdGePGnp"))) {
            Settings.System.putString(this.a.getContentResolver(), "dxCRMxhQkdGePGnp", str);
        }
    }

    private boolean e(String str) {
        if (str == null) {
            return false;
        }
        if (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return 24 == str.length() && !this.k.matcher(str).find();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a4, code lost:
        if (r6.e.a(r0) != null) goto L_0x00a6;
     */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0079 A[Catch:{ Exception -> 0x0129 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized java.lang.String a() {
        /*
            r6 = this;
            r1 = 0
            monitor-enter(r6)
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0044 }
            if (r0 == 0) goto L_0x000a
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0044 }
        L_0x0008:
            monitor-exit(r6)
            return r0
        L_0x000a:
            android.content.Context r0 = r6.a     // Catch:{ all -> 0x0044 }
            android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ all -> 0x0044 }
            java.lang.String r2 = "mqBRboGZkQPcAkyk"
            java.lang.String r0 = android.provider.Settings.System.getString(r0, r2)     // Catch:{ all -> 0x0044 }
            boolean r2 = r6.e(r0)     // Catch:{ all -> 0x0044 }
            if (r2 != 0) goto L_0x0008
            com.loc.db r4 = new com.loc.db     // Catch:{ all -> 0x0044 }
            r4.<init>()     // Catch:{ all -> 0x0044 }
            r2 = 0
            android.content.Context r0 = r6.a     // Catch:{ all -> 0x0044 }
            android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = "dxCRMxhQkdGePGnp"
            java.lang.String r3 = android.provider.Settings.System.getString(r0, r3)     // Catch:{ all -> 0x0044 }
            boolean r0 = com.loc.dw.a(r3)     // Catch:{ all -> 0x0044 }
            if (r0 != 0) goto L_0x0089
            java.lang.String r0 = r4.b(r3)     // Catch:{ all -> 0x0044 }
            boolean r5 = r6.e(r0)     // Catch:{ all -> 0x0044 }
            if (r5 == 0) goto L_0x0047
            r6.c(r0)     // Catch:{ all -> 0x0044 }
            goto L_0x0008
        L_0x0044:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        L_0x0047:
            java.lang.String r0 = r4.a(r3)     // Catch:{ all -> 0x0044 }
            boolean r5 = r6.e(r0)     // Catch:{ all -> 0x0044 }
            if (r5 == 0) goto L_0x0130
            com.loc.da r5 = r6.e     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = r5.a(r0)     // Catch:{ all -> 0x0044 }
            boolean r5 = com.loc.dw.a(r0)     // Catch:{ all -> 0x0044 }
            if (r5 != 0) goto L_0x0130
            r6.d(r0)     // Catch:{ all -> 0x0044 }
            android.content.Context r0 = r6.a     // Catch:{ all -> 0x0044 }
            android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = "dxCRMxhQkdGePGnp"
            java.lang.String r0 = android.provider.Settings.System.getString(r0, r3)     // Catch:{ all -> 0x0044 }
        L_0x006d:
            com.loc.da r3 = r6.e     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = r3.b(r0)     // Catch:{ all -> 0x0044 }
            boolean r5 = r6.e(r3)     // Catch:{ all -> 0x0044 }
            if (r5 == 0) goto L_0x008b
            r6.d = r3     // Catch:{ all -> 0x0044 }
            r6.a(r3)     // Catch:{ all -> 0x0044 }
            r6.b(r0)     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0044 }
            r6.c(r0)     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0044 }
            goto L_0x0008
        L_0x0089:
            r0 = 1
            r2 = r0
        L_0x008b:
            com.loc.dz r0 = r6.i     // Catch:{ all -> 0x0044 }
            if (r0 == 0) goto L_0x00c1
            com.loc.dz r0 = r6.i     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = "UTDID"
            java.lang.String r0 = r0.a(r3)     // Catch:{ all -> 0x0044 }
            boolean r3 = com.loc.dw.a(r0)     // Catch:{ all -> 0x0044 }
            if (r3 != 0) goto L_0x00c1
            com.loc.da r3 = r6.e     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = r3.a(r0)     // Catch:{ all -> 0x0044 }
            if (r3 == 0) goto L_0x00c1
        L_0x00a6:
            boolean r3 = r6.e(r0)     // Catch:{ all -> 0x0044 }
            if (r3 == 0) goto L_0x00c3
            com.loc.da r1 = r6.e     // Catch:{ all -> 0x0044 }
            java.lang.String r1 = r1.a(r0)     // Catch:{ all -> 0x0044 }
            if (r2 == 0) goto L_0x00b7
            r6.d(r1)     // Catch:{ all -> 0x0044 }
        L_0x00b7:
            r6.c(r0)     // Catch:{ all -> 0x0044 }
            r6.b(r1)     // Catch:{ all -> 0x0044 }
            r6.d = r0     // Catch:{ all -> 0x0044 }
            goto L_0x0008
        L_0x00c1:
            r0 = r1
            goto L_0x00a6
        L_0x00c3:
            com.loc.dz r0 = r6.h     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = r6.f     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = r0.a(r3)     // Catch:{ all -> 0x0044 }
            boolean r0 = com.loc.dw.a(r3)     // Catch:{ all -> 0x0044 }
            if (r0 != 0) goto L_0x0103
            java.lang.String r0 = r4.a(r3)     // Catch:{ all -> 0x0044 }
            boolean r4 = r6.e(r0)     // Catch:{ all -> 0x0044 }
            if (r4 != 0) goto L_0x00e1
            com.loc.da r0 = r6.e     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = r0.b(r3)     // Catch:{ all -> 0x0044 }
        L_0x00e1:
            boolean r3 = r6.e(r0)     // Catch:{ all -> 0x0044 }
            if (r3 == 0) goto L_0x0103
            com.loc.da r3 = r6.e     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = r3.a(r0)     // Catch:{ all -> 0x0044 }
            boolean r4 = com.loc.dw.a(r0)     // Catch:{ all -> 0x0044 }
            if (r4 != 0) goto L_0x0103
            r6.d = r0     // Catch:{ all -> 0x0044 }
            if (r2 == 0) goto L_0x00fa
            r6.d(r3)     // Catch:{ all -> 0x0044 }
        L_0x00fa:
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0044 }
            r6.a(r0)     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = r6.d     // Catch:{ all -> 0x0044 }
            goto L_0x0008
        L_0x0103:
            byte[] r0 = r6.b()     // Catch:{ Exception -> 0x0129 }
            if (r0 == 0) goto L_0x012d
            r3 = 2
            java.lang.String r3 = com.loc.dt.a(r0, r3)     // Catch:{ Exception -> 0x0129 }
            r6.d = r3     // Catch:{ Exception -> 0x0129 }
            java.lang.String r3 = r6.d     // Catch:{ Exception -> 0x0129 }
            r6.a(r3)     // Catch:{ Exception -> 0x0129 }
            com.loc.da r3 = r6.e     // Catch:{ Exception -> 0x0129 }
            java.lang.String r0 = r3.a(r0)     // Catch:{ Exception -> 0x0129 }
            if (r0 == 0) goto L_0x0125
            if (r2 == 0) goto L_0x0122
            r6.d(r0)     // Catch:{ Exception -> 0x0129 }
        L_0x0122:
            r6.b(r0)     // Catch:{ Exception -> 0x0129 }
        L_0x0125:
            java.lang.String r0 = r6.d     // Catch:{ Exception -> 0x0129 }
            goto L_0x0008
        L_0x0129:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0044 }
        L_0x012d:
            r0 = r1
            goto L_0x0008
        L_0x0130:
            r0 = r3
            goto L_0x006d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cz.a():java.lang.String");
    }
}
