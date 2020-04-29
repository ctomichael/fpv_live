package dji.thirdparty.sanselan.util;

import dji.thirdparty.sanselan.SanselanConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class IOUtils implements SanselanConstants {
    private IOUtils() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0024 A[SYNTHETIC, Splitter:B:14:0x0024] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getInputStreamBytes(java.io.InputStream r8) throws java.io.IOException {
        /*
            r4 = 0
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x003f }
            r6 = 4096(0x1000, float:5.74E-42)
            r5.<init>(r6)     // Catch:{ all -> 0x003f }
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0041 }
            r3.<init>(r8)     // Catch:{ all -> 0x0041 }
            r6 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r6]     // Catch:{ all -> 0x001f }
        L_0x0011:
            r6 = 0
            r7 = 4096(0x1000, float:5.74E-42)
            int r1 = r3.read(r0, r6, r7)     // Catch:{ all -> 0x001f }
            if (r1 <= 0) goto L_0x0028
            r6 = 0
            r5.write(r0, r6, r1)     // Catch:{ all -> 0x001f }
            goto L_0x0011
        L_0x001f:
            r6 = move-exception
            r4 = r5
            r8 = r3
        L_0x0022:
            if (r4 == 0) goto L_0x0027
            r4.close()     // Catch:{ IOException -> 0x003a }
        L_0x0027:
            throw r6
        L_0x0028:
            r5.flush()     // Catch:{ all -> 0x001f }
            byte[] r6 = r5.toByteArray()     // Catch:{ all -> 0x001f }
            if (r5 == 0) goto L_0x0034
            r5.close()     // Catch:{ IOException -> 0x0035 }
        L_0x0034:
            return r6
        L_0x0035:
            r2 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r2)
            goto L_0x0034
        L_0x003a:
            r2 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r2)
            goto L_0x0027
        L_0x003f:
            r6 = move-exception
            goto L_0x0022
        L_0x0041:
            r6 = move-exception
            r4 = r5
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.sanselan.util.IOUtils.getInputStreamBytes(java.io.InputStream):byte[]");
    }

    public static byte[] getFileBytes(File file) throws IOException {
        InputStream is = null;
        try {
            InputStream is2 = new FileInputStream(file);
            try {
                byte[] inputStreamBytes = getInputStreamBytes(is2);
                if (is2 != null) {
                    try {
                        is2.close();
                    } catch (IOException e) {
                        Debug.debug((Throwable) e);
                    }
                }
                return inputStreamBytes;
            } catch (Throwable th) {
                th = th;
                is = is2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2) {
                    Debug.debug((Throwable) e2);
                }
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0017 A[SYNTHETIC, Splitter:B:12:0x0017] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeToFile(byte[] r4, java.io.File r5) throws java.io.IOException {
        /*
            r1 = 0
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x0014 }
            r2.<init>(r4)     // Catch:{ all -> 0x0014 }
            putInputStreamToFile(r2, r5)     // Catch:{ all -> 0x0020 }
            if (r2 == 0) goto L_0x000e
            r2.close()     // Catch:{ Exception -> 0x000f }
        L_0x000e:
            return
        L_0x000f:
            r0 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r0)
            goto L_0x000e
        L_0x0014:
            r3 = move-exception
        L_0x0015:
            if (r1 == 0) goto L_0x001a
            r1.close()     // Catch:{ Exception -> 0x001b }
        L_0x001a:
            throw r3
        L_0x001b:
            r0 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r0)
            goto L_0x001a
        L_0x0020:
            r3 = move-exception
            r1 = r2
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.sanselan.util.IOUtils.writeToFile(byte[], java.io.File):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0024 A[SYNTHETIC, Splitter:B:15:0x0024] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void putInputStreamToFile(java.io.InputStream r4, java.io.File r5) throws java.io.IOException {
        /*
            r1 = 0
            java.io.File r3 = r5.getParentFile()     // Catch:{ all -> 0x0021 }
            if (r3 == 0) goto L_0x000e
            java.io.File r3 = r5.getParentFile()     // Catch:{ all -> 0x0021 }
            r3.mkdirs()     // Catch:{ all -> 0x0021 }
        L_0x000e:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ all -> 0x0021 }
            r2.<init>(r5)     // Catch:{ all -> 0x0021 }
            copyStreamToStream(r4, r2)     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x001b
            r2.close()     // Catch:{ Exception -> 0x001c }
        L_0x001b:
            return
        L_0x001c:
            r0 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r0)
            goto L_0x001b
        L_0x0021:
            r3 = move-exception
        L_0x0022:
            if (r1 == 0) goto L_0x0027
            r1.close()     // Catch:{ Exception -> 0x0028 }
        L_0x0027:
            throw r3
        L_0x0028:
            r0 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r0)
            goto L_0x0027
        L_0x002d:
            r3 = move-exception
            r1 = r2
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.sanselan.util.IOUtils.putInputStreamToFile(java.io.InputStream, java.io.File):void");
    }

    public static void copyStreamToStream(InputStream src, OutputStream dst) throws IOException {
        copyStreamToStream(src, dst, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0022  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyStreamToStream(java.io.InputStream r9, java.io.OutputStream r10, boolean r11) throws java.io.IOException {
        /*
            r0 = 0
            r2 = 0
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0051 }
            r1.<init>(r9)     // Catch:{ all -> 0x0051 }
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x0053 }
            r3.<init>(r10)     // Catch:{ all -> 0x0053 }
            r7 = 4096(0x1000, float:5.74E-42)
            byte[] r4 = new byte[r7]     // Catch:{ all -> 0x001d }
        L_0x0010:
            r7 = 0
            int r8 = r4.length     // Catch:{ all -> 0x001d }
            int r5 = r1.read(r4, r7, r8)     // Catch:{ all -> 0x001d }
            if (r5 <= 0) goto L_0x002d
            r7 = 0
            r10.write(r4, r7, r5)     // Catch:{ all -> 0x001d }
            goto L_0x0010
        L_0x001d:
            r7 = move-exception
            r2 = r3
            r0 = r1
        L_0x0020:
            if (r11 == 0) goto L_0x002c
            if (r0 == 0) goto L_0x0027
            r0.close()     // Catch:{ IOException -> 0x0047 }
        L_0x0027:
            if (r2 == 0) goto L_0x002c
            r2.close()     // Catch:{ IOException -> 0x004c }
        L_0x002c:
            throw r7
        L_0x002d:
            r3.flush()     // Catch:{ all -> 0x001d }
            if (r11 == 0) goto L_0x003c
            if (r1 == 0) goto L_0x0037
            r1.close()     // Catch:{ IOException -> 0x003d }
        L_0x0037:
            if (r3 == 0) goto L_0x003c
            r3.close()     // Catch:{ IOException -> 0x0042 }
        L_0x003c:
            return
        L_0x003d:
            r6 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r6)
            goto L_0x0037
        L_0x0042:
            r6 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r6)
            goto L_0x003c
        L_0x0047:
            r6 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r6)
            goto L_0x0027
        L_0x004c:
            r6 = move-exception
            dji.thirdparty.sanselan.util.Debug.debug(r6)
            goto L_0x002c
        L_0x0051:
            r7 = move-exception
            goto L_0x0020
        L_0x0053:
            r7 = move-exception
            r0 = r1
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.sanselan.util.IOUtils.copyStreamToStream(java.io.InputStream, java.io.OutputStream, boolean):void");
    }

    public static final boolean copyFileNio(File src, File dst) throws IOException {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            FileChannel srcChannel2 = new FileInputStream(src).getChannel();
            FileChannel dstChannel2 = new FileOutputStream(dst).getChannel();
            long size = srcChannel2.size();
            for (long position = 0; position < size; position += srcChannel2.transferTo(position, (long) 16777216, dstChannel2)) {
            }
            srcChannel2.close();
            srcChannel = null;
            dstChannel2.close();
            dstChannel = null;
            if (srcChannel != null) {
                try {
                } catch (IOException e) {
                    Debug.debug((Throwable) e);
                }
            }
            if (dstChannel != null) {
                try {
                } catch (IOException e2) {
                    Debug.debug((Throwable) e2);
                }
            }
            return true;
        } finally {
            if (srcChannel != null) {
                try {
                    srcChannel.close();
                } catch (IOException e3) {
                    Debug.debug((Throwable) e3);
                }
            }
            if (dstChannel != null) {
                try {
                    dstChannel.close();
                } catch (IOException e4) {
                    Debug.debug((Throwable) e4);
                }
            }
        }
    }
}
