package com.dji.video.framing.utils;

public class FileUtils {
    private static final String TAG = "FileUtils";

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006a A[SYNTHETIC, Splitter:B:19:0x006a] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00af A[SYNTHETIC, Splitter:B:27:0x00af] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00d8 A[SYNTHETIC, Splitter:B:33:0x00d8] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:16:0x004a=Splitter:B:16:0x004a, B:24:0x008f=Splitter:B:24:0x008f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File saveBitmap(android.graphics.Bitmap r9, java.lang.String r10) {
        /*
            java.io.File r1 = new java.io.File
            r1.<init>(r10)
            boolean r4 = r1.exists()
            if (r4 == 0) goto L_0x000e
            r1.delete()
        L_0x000e:
            r2 = 0
            r1.createNewFile()     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x008e }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x008e }
            r3.<init>(r1)     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x008e }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x0102, IOException -> 0x00ff, all -> 0x00fc }
            r5 = 100
            r9.compress(r4, r5, r3)     // Catch:{ FileNotFoundException -> 0x0102, IOException -> 0x00ff, all -> 0x00fc }
            r3.flush()     // Catch:{ FileNotFoundException -> 0x0102, IOException -> 0x00ff, all -> 0x00fc }
            if (r3 == 0) goto L_0x0106
            r3.close()     // Catch:{ IOException -> 0x0028 }
            r2 = r3
        L_0x0027:
            return r1
        L_0x0028:
            r0 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()
            java.lang.String r5 = "FileUtils"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "saveBitmap: finally: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r4.LOGE(r5, r6)
            r2 = r3
            goto L_0x0027
        L_0x0049:
            r0 = move-exception
        L_0x004a:
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x00d5 }
            java.lang.String r5 = "FileUtils"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d5 }
            r6.<init>()     // Catch:{ all -> 0x00d5 }
            java.lang.String r7 = "saveBitmap: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00d5 }
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x00d5 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00d5 }
            r4.LOGE(r5, r6)     // Catch:{ all -> 0x00d5 }
            if (r2 == 0) goto L_0x0027
            r2.close()     // Catch:{ IOException -> 0x006e }
            goto L_0x0027
        L_0x006e:
            r0 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()
            java.lang.String r5 = "FileUtils"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "saveBitmap: finally: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r4.LOGE(r5, r6)
            goto L_0x0027
        L_0x008e:
            r0 = move-exception
        L_0x008f:
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x00d5 }
            java.lang.String r5 = "FileUtils"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d5 }
            r6.<init>()     // Catch:{ all -> 0x00d5 }
            java.lang.String r7 = "saveBitmap: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00d5 }
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x00d5 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00d5 }
            r4.LOGE(r5, r6)     // Catch:{ all -> 0x00d5 }
            if (r2 == 0) goto L_0x0027
            r2.close()     // Catch:{ IOException -> 0x00b4 }
            goto L_0x0027
        L_0x00b4:
            r0 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()
            java.lang.String r5 = "FileUtils"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "saveBitmap: finally: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r4.LOGE(r5, r6)
            goto L_0x0027
        L_0x00d5:
            r4 = move-exception
        L_0x00d6:
            if (r2 == 0) goto L_0x00db
            r2.close()     // Catch:{ IOException -> 0x00dc }
        L_0x00db:
            throw r4
        L_0x00dc:
            r0 = move-exception
            dji.log.DJILogHelper r5 = dji.log.DJILogHelper.getInstance()
            java.lang.String r6 = "FileUtils"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "saveBitmap: finally: "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r7 = r7.toString()
            r5.LOGE(r6, r7)
            goto L_0x00db
        L_0x00fc:
            r4 = move-exception
            r2 = r3
            goto L_0x00d6
        L_0x00ff:
            r0 = move-exception
            r2 = r3
            goto L_0x008f
        L_0x0102:
            r0 = move-exception
            r2 = r3
            goto L_0x004a
        L_0x0106:
            r2 = r3
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.video.framing.utils.FileUtils.saveBitmap(android.graphics.Bitmap, java.lang.String):java.io.File");
    }
}
