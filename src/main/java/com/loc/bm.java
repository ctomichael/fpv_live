package com.loc;

import com.loc.bc;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: LogEngine */
public final class bm {
    private static void a(bc bcVar, List<String> list) {
        if (bcVar != null) {
            try {
                for (String str : list) {
                    bcVar.c(str);
                }
                bcVar.close();
            } catch (Throwable th) {
                aq.b(th, "ofm", "dlo");
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a5 A[SYNTHETIC, Splitter:B:47:0x00a5] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(com.loc.bl r8) {
        /*
            r2 = 0
            r7 = 1
            com.loc.cf r0 = r8.f     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            boolean r0 = r0.c()     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            if (r0 == 0) goto L_0x0075
            com.loc.cf r0 = r8.f     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            r1 = 1
            r0.a(r1)     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            java.lang.String r1 = r8.a     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            long r4 = r8.b     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            com.loc.bc r1 = com.loc.bc.a(r0, r4)     // Catch:{ Throwable -> 0x00b2, all -> 0x00a1 }
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Throwable -> 0x008f }
            r0.<init>()     // Catch:{ Throwable -> 0x008f }
            byte[] r3 = a(r1, r8, r0)     // Catch:{ Throwable -> 0x008f }
            if (r3 == 0) goto L_0x002b
            int r4 = r3.length     // Catch:{ Throwable -> 0x008f }
            if (r4 != 0) goto L_0x0031
        L_0x002b:
            if (r1 == 0) goto L_0x0030
            r1.close()     // Catch:{ Throwable -> 0x00ae }
        L_0x0030:
            return
        L_0x0031:
            com.loc.ap r4 = new com.loc.ap     // Catch:{ Throwable -> 0x008f }
            java.lang.String r5 = r8.c     // Catch:{ Throwable -> 0x008f }
            r4.<init>(r3, r5)     // Catch:{ Throwable -> 0x008f }
            com.loc.bg.a()     // Catch:{ Throwable -> 0x008f }
            byte[] r4 = com.loc.bg.b(r4)     // Catch:{ Throwable -> 0x008f }
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Throwable -> 0x008f }
            java.lang.String r6 = new java.lang.String     // Catch:{ Throwable -> 0x008f }
            r6.<init>(r4)     // Catch:{ Throwable -> 0x008f }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x008f }
            java.lang.String r4 = "code"
            boolean r4 = r5.has(r4)     // Catch:{ Throwable -> 0x008f }
            if (r4 == 0) goto L_0x00b5
            java.lang.String r4 = "code"
            int r4 = r5.getInt(r4)     // Catch:{ Throwable -> 0x008f }
            if (r4 != r7) goto L_0x00b5
            com.loc.cf r4 = r8.f     // Catch:{ Throwable -> 0x008f }
            if (r4 == 0) goto L_0x0067
            if (r3 == 0) goto L_0x0067
            com.loc.cf r4 = r8.f     // Catch:{ Throwable -> 0x008f }
            int r3 = r3.length     // Catch:{ Throwable -> 0x008f }
            r4.a(r3)     // Catch:{ Throwable -> 0x008f }
        L_0x0067:
            com.loc.cf r3 = r8.f     // Catch:{ Throwable -> 0x008f }
            int r3 = r3.b()     // Catch:{ Throwable -> 0x008f }
            r4 = 2147483647(0x7fffffff, float:NaN)
            if (r3 >= r4) goto L_0x0080
            a(r1, r0)     // Catch:{ Throwable -> 0x008f }
        L_0x0075:
            if (r2 == 0) goto L_0x0030
            r2.close()     // Catch:{ Throwable -> 0x007b }
            goto L_0x0030
        L_0x007b:
            r0 = move-exception
        L_0x007c:
            r0.printStackTrace()
            goto L_0x0030
        L_0x0080:
            r1.d()     // Catch:{ Throwable -> 0x0084 }
            goto L_0x0075
        L_0x0084:
            r0 = move-exception
            java.lang.String r3 = "ofm"
            java.lang.String r4 = "dlo"
            com.loc.aq.b(r0, r3, r4)     // Catch:{ Throwable -> 0x008f }
            goto L_0x0075
        L_0x008f:
            r0 = move-exception
        L_0x0090:
            java.lang.String r2 = "leg"
            java.lang.String r3 = "uts"
            com.loc.aq.b(r0, r2, r3)     // Catch:{ all -> 0x00b0 }
            if (r1 == 0) goto L_0x0030
            r1.close()     // Catch:{ Throwable -> 0x009f }
            goto L_0x0030
        L_0x009f:
            r0 = move-exception
            goto L_0x007c
        L_0x00a1:
            r0 = move-exception
            r1 = r2
        L_0x00a3:
            if (r1 == 0) goto L_0x00a8
            r1.close()     // Catch:{ Throwable -> 0x00a9 }
        L_0x00a8:
            throw r0
        L_0x00a9:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00a8
        L_0x00ae:
            r0 = move-exception
            goto L_0x007c
        L_0x00b0:
            r0 = move-exception
            goto L_0x00a3
        L_0x00b2:
            r0 = move-exception
            r1 = r2
            goto L_0x0090
        L_0x00b5:
            r2 = r1
            goto L_0x0075
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bm.a(com.loc.bl):void");
    }

    public static void a(String str, byte[] bArr, bl blVar) throws IOException, CertificateException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        bc bcVar;
        OutputStream outputStream;
        bc bcVar2 = null;
        OutputStream outputStream2 = null;
        try {
            if (a(blVar.a, str)) {
                if (0 != 0) {
                    try {
                        outputStream2.close();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                if (0 != 0) {
                    try {
                        bcVar2.close();
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } else {
                    return;
                }
            } else {
                File file = new File(blVar.a);
                if (!file.exists()) {
                    file.mkdirs();
                }
                bcVar = bc.a(file, blVar.b);
                try {
                    bcVar.a(blVar.d);
                    byte[] b = blVar.e.b(bArr);
                    bc.a b2 = bcVar.b(str);
                    outputStream = b2.a();
                    try {
                        outputStream.write(b);
                        b2.b();
                        bcVar.c();
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (Throwable th3) {
                                th3.printStackTrace();
                            }
                        }
                        if (bcVar != null) {
                            try {
                                bcVar.close();
                                return;
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (Throwable th6) {
                                th6.printStackTrace();
                            }
                        }
                        if (bcVar != null) {
                            try {
                                bcVar.close();
                            } catch (Throwable th7) {
                                th7.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    outputStream = null;
                }
            }
            th.printStackTrace();
        } catch (Throwable th9) {
            th = th9;
            outputStream = null;
            bcVar = null;
        }
    }

    private static boolean a(String str, String str2) {
        try {
            return new File(str, str2 + ".0").exists();
        } catch (Throwable th) {
            aq.b(th, "leg", "fet");
            return false;
        }
    }

    private static byte[] a(bc bcVar, bl blVar, List<String> list) {
        try {
            File b = bcVar.b();
            if (b != null && b.exists()) {
                String[] list2 = b.list();
                int i = 0;
                for (String str : list2) {
                    if (str.contains(".0")) {
                        String str2 = str.split("\\.")[0];
                        byte[] a = bs.a(bcVar, str2);
                        i += a.length;
                        list.add(str2);
                        if (i > blVar.f.b()) {
                            break;
                        }
                        blVar.g.b(a);
                    }
                }
                return blVar.g.a();
            }
        } catch (Throwable th) {
            aq.b(th, "leg", "gCo");
        }
        return new byte[0];
    }
}
