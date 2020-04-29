package dji.publics.utils;

import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class RomUtil {
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    private static final String ROM_OPPO = "oppo";
    private static final String ROM_UNKNOWN = "unknown";
    private static final String ROM_VIVO = "vivo";
    private static volatile String sRomName = null;

    public static boolean isOppo() {
        return check(ROM_OPPO);
    }

    public static boolean isVivo() {
        return check(ROM_VIVO);
    }

    private static synchronized boolean check(String romName) {
        boolean equals;
        synchronized (RomUtil.class) {
            if (!TextUtils.isEmpty(sRomName)) {
                equals = sRomName.equals(romName);
            } else {
                if (!TextUtils.isEmpty(getProp(KEY_VERSION_OPPO))) {
                    sRomName = ROM_OPPO;
                } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_VIVO))) {
                    sRomName = ROM_VIVO;
                } else {
                    sRomName = "unknown";
                }
                equals = sRomName.equals(romName);
            }
        }
        return equals;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x005c A[SYNTHETIC, Splitter:B:25:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0061 A[SYNTHETIC, Splitter:B:28:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078 A[SYNTHETIC, Splitter:B:39:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007d A[SYNTHETIC, Splitter:B:42:0x007d] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0082  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getProp(java.lang.String r12) {
        /*
            r8 = 0
            r6 = 0
            r0 = 0
            r4 = 0
            java.lang.Runtime r9 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0055 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0055 }
            r10.<init>()     // Catch:{ IOException -> 0x0055 }
            java.lang.String r11 = "getprop "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ IOException -> 0x0055 }
            java.lang.StringBuilder r10 = r10.append(r12)     // Catch:{ IOException -> 0x0055 }
            java.lang.String r10 = r10.toString()     // Catch:{ IOException -> 0x0055 }
            java.lang.Process r8 = r9.exec(r10)     // Catch:{ IOException -> 0x0055 }
            java.io.InputStreamReader r5 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0055 }
            java.io.InputStream r9 = r8.getInputStream()     // Catch:{ IOException -> 0x0055 }
            java.lang.String r10 = "UTF-8"
            r5.<init>(r9, r10)     // Catch:{ IOException -> 0x0055 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0097, all -> 0x0090 }
            r9 = 1024(0x400, float:1.435E-42)
            r1.<init>(r5, r9)     // Catch:{ IOException -> 0x0097, all -> 0x0090 }
            java.lang.String r6 = r1.readLine()     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            if (r1 == 0) goto L_0x003c
            r1.close()     // Catch:{ IOException -> 0x004b }
        L_0x003c:
            if (r5 == 0) goto L_0x0041
            r5.close()     // Catch:{ IOException -> 0x0050 }
        L_0x0041:
            if (r8 == 0) goto L_0x0046
            r8.destroy()
        L_0x0046:
            r4 = r5
            r0 = r1
            r7 = r6
            r9 = r6
        L_0x004a:
            return r9
        L_0x004b:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x003c
        L_0x0050:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0041
        L_0x0055:
            r3 = move-exception
        L_0x0056:
            r3.printStackTrace()     // Catch:{ all -> 0x0075 }
            r9 = 0
            if (r0 == 0) goto L_0x005f
            r0.close()     // Catch:{ IOException -> 0x006b }
        L_0x005f:
            if (r4 == 0) goto L_0x0064
            r4.close()     // Catch:{ IOException -> 0x0070 }
        L_0x0064:
            if (r8 == 0) goto L_0x0069
            r8.destroy()
        L_0x0069:
            r7 = r6
            goto L_0x004a
        L_0x006b:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x005f
        L_0x0070:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0064
        L_0x0075:
            r9 = move-exception
        L_0x0076:
            if (r0 == 0) goto L_0x007b
            r0.close()     // Catch:{ IOException -> 0x0086 }
        L_0x007b:
            if (r4 == 0) goto L_0x0080
            r4.close()     // Catch:{ IOException -> 0x008b }
        L_0x0080:
            if (r8 == 0) goto L_0x0085
            r8.destroy()
        L_0x0085:
            throw r9
        L_0x0086:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x007b
        L_0x008b:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0080
        L_0x0090:
            r9 = move-exception
            r4 = r5
            goto L_0x0076
        L_0x0093:
            r9 = move-exception
            r4 = r5
            r0 = r1
            goto L_0x0076
        L_0x0097:
            r3 = move-exception
            r4 = r5
            goto L_0x0056
        L_0x009a:
            r3 = move-exception
            r4 = r5
            r0 = r1
            goto L_0x0056
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.publics.utils.RomUtil.getProp(java.lang.String):java.lang.String");
    }
}
