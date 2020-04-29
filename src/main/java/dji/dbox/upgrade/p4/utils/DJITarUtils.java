package dji.dbox.upgrade.p4.utils;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJITarUtils {

    public interface Callback {
        void onTarException(Exception exc);

        void onTarSuccess();
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0057 A[SYNTHETIC, Splitter:B:21:0x0057] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005c A[SYNTHETIC, Splitter:B:24:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0096 A[SYNTHETIC, Splitter:B:48:0x0096] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x009b A[SYNTHETIC, Splitter:B:51:0x009b] */
    /* JADX WARNING: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void tarDir(java.lang.String r16, java.lang.String r17, dji.dbox.upgrade.p4.utils.DJITarUtils.Callback r18) {
        /*
            r9 = 0
            r7 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00b0 }
            r0 = r16
            r3.<init>(r0)     // Catch:{ IOException -> 0x00b0 }
            org.xeustechnologies.jtar.TarOutputStream r10 = new org.xeustechnologies.jtar.TarOutputStream     // Catch:{ IOException -> 0x00b0 }
            java.io.BufferedOutputStream r11 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x00b0 }
            r11.<init>(r3)     // Catch:{ IOException -> 0x00b0 }
            r10.<init>(r11)     // Catch:{ IOException -> 0x00b0 }
            java.io.File r4 = new java.io.File     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            r0 = r17
            r4.<init>(r0)     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            java.io.File[] r12 = r4.listFiles()     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            int r13 = r12.length     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            r11 = 0
            r8 = r7
        L_0x0021:
            if (r11 >= r13) goto L_0x006a
            r6 = r12[r11]     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            org.xeustechnologies.jtar.TarEntry r14 = new org.xeustechnologies.jtar.TarEntry     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            java.lang.String r15 = r6.getName()     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            r14.<init>(r6, r15)     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            r10.putNextEntry(r14)     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            java.io.BufferedInputStream r7 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            java.io.FileInputStream r14 = new java.io.FileInputStream     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            r14.<init>(r6)     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            r7.<init>(r14)     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            r14 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r14]     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
        L_0x003f:
            int r1 = r7.read(r2)     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            r14 = -1
            if (r1 == r14) goto L_0x0060
            r14 = 0
            r10.write(r2, r14, r1)     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            goto L_0x003f
        L_0x004b:
            r5 = move-exception
            r9 = r10
        L_0x004d:
            r0 = r18
            r0.onTarException(r5)     // Catch:{ all -> 0x0093 }
            r5.printStackTrace()     // Catch:{ all -> 0x0093 }
            if (r9 == 0) goto L_0x005a
            r9.close()     // Catch:{ IOException -> 0x0089 }
        L_0x005a:
            if (r7 == 0) goto L_0x005f
            r7.close()     // Catch:{ IOException -> 0x008e }
        L_0x005f:
            return
        L_0x0060:
            r10.flush()     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            r7.close()     // Catch:{ IOException -> 0x004b, all -> 0x00a9 }
            int r11 = r11 + 1
            r8 = r7
            goto L_0x0021
        L_0x006a:
            r10.close()     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            r18.onTarSuccess()     // Catch:{ IOException -> 0x00b2, all -> 0x00ac }
            if (r10 == 0) goto L_0x0075
            r10.close()     // Catch:{ IOException -> 0x007d }
        L_0x0075:
            if (r8 == 0) goto L_0x00b6
            r8.close()     // Catch:{ IOException -> 0x0082 }
            r7 = r8
            r9 = r10
            goto L_0x005f
        L_0x007d:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0075
        L_0x0082:
            r5 = move-exception
            r5.printStackTrace()
            r7 = r8
            r9 = r10
            goto L_0x005f
        L_0x0089:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x005a
        L_0x008e:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x005f
        L_0x0093:
            r11 = move-exception
        L_0x0094:
            if (r9 == 0) goto L_0x0099
            r9.close()     // Catch:{ IOException -> 0x009f }
        L_0x0099:
            if (r7 == 0) goto L_0x009e
            r7.close()     // Catch:{ IOException -> 0x00a4 }
        L_0x009e:
            throw r11
        L_0x009f:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0099
        L_0x00a4:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x009e
        L_0x00a9:
            r11 = move-exception
            r9 = r10
            goto L_0x0094
        L_0x00ac:
            r11 = move-exception
            r7 = r8
            r9 = r10
            goto L_0x0094
        L_0x00b0:
            r5 = move-exception
            goto L_0x004d
        L_0x00b2:
            r5 = move-exception
            r7 = r8
            r9 = r10
            goto L_0x004d
        L_0x00b6:
            r7 = r8
            r9 = r10
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.dbox.upgrade.p4.utils.DJITarUtils.tarDir(java.lang.String, java.lang.String, dji.dbox.upgrade.p4.utils.DJITarUtils$Callback):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0230 A[SYNTHETIC, Splitter:B:52:0x0230] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0235 A[SYNTHETIC, Splitter:B:55:0x0235] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void tarFiles(java.lang.String r32, java.lang.String r33, java.util.ArrayList<java.lang.String> r34, dji.dbox.upgrade.p4.server.DJIUpServerManager.TarCallBack r35, dji.dbox.upgrade.p4.utils.DJITarUtils.Callback r36) {
        /*
            r13 = 0
            r16 = 0
            r15 = r32
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x02cf }
            r21.<init>()     // Catch:{ IOException -> 0x02cf }
            r0 = r21
            r1 = r32
            java.lang.StringBuilder r21 = r0.append(r1)     // Catch:{ IOException -> 0x02cf }
            java.lang.String r28 = ".b"
            r0 = r21
            r1 = r28
            java.lang.StringBuilder r21 = r0.append(r1)     // Catch:{ IOException -> 0x02cf }
            java.lang.String r32 = r21.toString()     // Catch:{ IOException -> 0x02cf }
            java.lang.String r21 = "tar"
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x02cf }
            r28.<init>()     // Catch:{ IOException -> 0x02cf }
            java.lang.String r29 = "tarFiles outpath="
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ IOException -> 0x02cf }
            r0 = r28
            r1 = r32
            java.lang.StringBuilder r28 = r0.append(r1)     // Catch:{ IOException -> 0x02cf }
            java.lang.String r28 = r28.toString()     // Catch:{ IOException -> 0x02cf }
            r0 = r21
            r1 = r28
            dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r0, r1)     // Catch:{ IOException -> 0x02cf }
            java.io.File r10 = new java.io.File     // Catch:{ IOException -> 0x02cf }
            r0 = r32
            r10.<init>(r0)     // Catch:{ IOException -> 0x02cf }
            boolean r21 = r10.exists()     // Catch:{ IOException -> 0x02cf }
            if (r21 != 0) goto L_0x0053
            r10.createNewFile()     // Catch:{ IOException -> 0x02cf }
        L_0x0053:
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x02cf }
            r0 = r32
            r6.<init>(r0)     // Catch:{ IOException -> 0x02cf }
            org.xeustechnologies.jtar.TarOutputStream r17 = new org.xeustechnologies.jtar.TarOutputStream     // Catch:{ IOException -> 0x02cf }
            java.io.BufferedOutputStream r21 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x02cf }
            r0 = r21
            r0.<init>(r6)     // Catch:{ IOException -> 0x02cf }
            r0 = r17
            r1 = r21
            r0.<init>(r1)     // Catch:{ IOException -> 0x02cf }
            java.lang.String r21 = "tar"
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r28.<init>()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r29 = "tarFiles dirpath="
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r28
            r1 = r33
            java.lang.StringBuilder r28 = r0.append(r1)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r28 = r28.toString()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r21
            r1 = r28
            dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r0, r1)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.io.File r7 = new java.io.File     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r33
            r7.<init>(r0)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            boolean r21 = r7.exists()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            if (r21 != 0) goto L_0x009c
            r7.createNewFile()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
        L_0x009c:
            r24 = 0
            r26 = 0
            java.util.Iterator r21 = r34.iterator()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
        L_0x00a4:
            boolean r28 = r21.hasNext()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            if (r28 == 0) goto L_0x0101
            java.lang.Object r12 = r21.next()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r28 = "tar"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r29.<init>()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r30 = "tarFiles name="
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r29
            java.lang.StringBuilder r29 = r0.append(r12)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r29 = r29.toString()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r28, r29)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r28 = "/"
            r0 = r28
            boolean r28 = r12.contains(r0)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            if (r28 == 0) goto L_0x00e3
            java.io.File r9 = new java.io.File     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r9.<init>(r12)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
        L_0x00dc:
            long r22 = r9.length()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            long r24 = r24 + r22
            goto L_0x00a4
        L_0x00e3:
            java.io.File r9 = new java.io.File     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r33
            r9.<init>(r0, r12)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            goto L_0x00dc
        L_0x00eb:
            r8 = move-exception
            r16 = r17
        L_0x00ee:
            r0 = r36
            r0.onTarException(r8)     // Catch:{ all -> 0x02c6 }
            r8.printStackTrace()     // Catch:{ all -> 0x02c6 }
            if (r16 == 0) goto L_0x00fb
            r16.close()     // Catch:{ IOException -> 0x02ae }
        L_0x00fb:
            if (r13 == 0) goto L_0x0100
            r13.close()     // Catch:{ IOException -> 0x02b4 }
        L_0x0100:
            return
        L_0x0101:
            java.lang.String r21 = "tar"
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r28.<init>()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r29 = "tar totalSize="
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r28
            r1 = r24
            java.lang.StringBuilder r28 = r0.append(r1)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r28 = r28.toString()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r21
            r1 = r28
            android.util.Log.d(r0, r1)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r11 = 0
            java.util.Iterator r21 = r34.iterator()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r14 = r13
        L_0x0129:
            boolean r28 = r21.hasNext()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            if (r28 == 0) goto L_0x0251
            java.lang.Object r12 = r21.next()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r28 = "tar"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r29.<init>()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r30 = "tarFiles name="
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r29
            java.lang.StringBuilder r29 = r0.append(r12)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r29 = r29.toString()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            dji.dbox.upgrade.p4.constants.DJIUpConstants.LOGD(r28, r29)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r28 = "/"
            r0 = r28
            boolean r28 = r12.contains(r0)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            if (r28 == 0) goto L_0x0239
            java.io.File r9 = new java.io.File     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r9.<init>(r12)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
        L_0x0161:
            r18 = 2097152(0x200000, float:2.938736E-39)
            org.xeustechnologies.jtar.TarEntry r28 = new org.xeustechnologies.jtar.TarEntry     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r29 = r9.getName()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r28
            r1 = r29
            r0.<init>(r9, r1)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r17
            r1 = r28
            r0.putNextEntry(r1)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.io.BufferedInputStream r13 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.io.FileInputStream r28 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r28
            r0.<init>(r9)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r28
            r13.<init>(r0)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r18
            byte[] r5 = new byte[r0]     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
        L_0x0189:
            int r4 = r13.read(r5)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r28 = -1
            r0 = r28
            if (r4 == r0) goto L_0x0248
            java.lang.String r28 = "tar"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r29.<init>()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r30 = "tar read="
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            long r30 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r29 = r29.toString()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            android.util.Log.d(r28, r29)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            int r11 = r11 + 1
            r28 = 0
            r0 = r17
            r1 = r28
            r0.write(r5, r1, r4)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r28 = "tar"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r29.<init>()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r30 = "tar write="
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            long r30 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r29 = r29.toString()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            android.util.Log.d(r28, r29)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            long r0 = (long) r4     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r28 = r0
            long r26 = r26 + r28
            r0 = r26
            float r0 = (float) r0     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r28 = r0
            r29 = 1120403456(0x42c80000, float:100.0)
            float r28 = r28 * r29
            r0 = r24
            float r0 = (float) r0     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r29 = r0
            float r28 = r28 / r29
            int r19 = java.lang.Math.round(r28)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            int r28 = r11 % 5
            if (r28 != 0) goto L_0x01fe
            r0 = r35
            r1 = r19
            r0.onZipProgress(r1)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
        L_0x01fe:
            java.lang.String r28 = "tar"
            java.lang.StringBuilder r29 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r29.<init>()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r30 = "tar count="
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r0 = r29
            java.lang.StringBuilder r29 = r0.append(r4)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r30 = " "
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            long r30 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.StringBuilder r29 = r29.append(r30)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            java.lang.String r29 = r29.toString()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            android.util.Log.d(r28, r29)     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            goto L_0x0189
        L_0x022b:
            r21 = move-exception
            r16 = r17
        L_0x022e:
            if (r16 == 0) goto L_0x0233
            r16.close()     // Catch:{ IOException -> 0x02ba }
        L_0x0233:
            if (r13 == 0) goto L_0x0238
            r13.close()     // Catch:{ IOException -> 0x02c0 }
        L_0x0238:
            throw r21
        L_0x0239:
            java.io.File r9 = new java.io.File     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r33
            r9.<init>(r0, r12)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            goto L_0x0161
        L_0x0242:
            r8 = move-exception
            r16 = r17
            r13 = r14
            goto L_0x00ee
        L_0x0248:
            r17.flush()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r13.close()     // Catch:{ IOException -> 0x00eb, all -> 0x022b }
            r14 = r13
            goto L_0x0129
        L_0x0251:
            r17.close()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.io.File r21 = new java.io.File     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r21
            r1 = r32
            r0.<init>(r1)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.io.File r28 = new java.io.File     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r28
            r0.<init>(r15)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r21
            r1 = r28
            boolean r20 = r0.renameTo(r1)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r21 = "tar"
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r28.<init>()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r29 = "renameOK "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r28
            r1 = r20
            java.lang.StringBuilder r28 = r0.append(r1)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            java.lang.String r28 = r28.toString()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r0 = r21
            r1 = r28
            android.util.Log.d(r0, r1)     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            r36.onTarSuccess()     // Catch:{ IOException -> 0x0242, all -> 0x02c9 }
            if (r17 == 0) goto L_0x0296
            r17.close()     // Catch:{ IOException -> 0x02a0 }
        L_0x0296:
            if (r14 == 0) goto L_0x02d2
            r14.close()     // Catch:{ IOException -> 0x02a5 }
            r16 = r17
            r13 = r14
            goto L_0x0100
        L_0x02a0:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0296
        L_0x02a5:
            r8 = move-exception
            r8.printStackTrace()
            r16 = r17
            r13 = r14
            goto L_0x0100
        L_0x02ae:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x00fb
        L_0x02b4:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0100
        L_0x02ba:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0233
        L_0x02c0:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0238
        L_0x02c6:
            r21 = move-exception
            goto L_0x022e
        L_0x02c9:
            r21 = move-exception
            r16 = r17
            r13 = r14
            goto L_0x022e
        L_0x02cf:
            r8 = move-exception
            goto L_0x00ee
        L_0x02d2:
            r16 = r17
            r13 = r14
            goto L_0x0100
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.dbox.upgrade.p4.utils.DJITarUtils.tarFiles(java.lang.String, java.lang.String, java.util.ArrayList, dji.dbox.upgrade.p4.server.DJIUpServerManager$TarCallBack, dji.dbox.upgrade.p4.utils.DJITarUtils$Callback):void");
    }
}
