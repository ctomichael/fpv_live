package dji.thirdparty.afinal.bitmap.download;

import android.util.Log;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class SimpleDownloader implements Downloader {
    private static final int IO_BUFFER_SIZE = 8192;
    private static final String TAG = SimpleDownloader.class.getSimpleName();

    public byte[] download(String urlString) {
        if (urlString == null) {
            return null;
        }
        if (urlString.trim().toLowerCase().startsWith("http")) {
            return getFromHttp(urlString);
        }
        if (urlString.trim().toLowerCase().startsWith("file:")) {
            try {
                File f = new File(new URI(urlString));
                if (!f.exists() || !f.canRead()) {
                    return null;
                }
                return getFromFile(f);
            } catch (URISyntaxException e) {
                Log.e(TAG, "Error in read from file - " + urlString + " : " + e);
                return null;
            }
        } else {
            File f2 = new File(urlString);
            if (!f2.exists() || !f2.canRead()) {
                return null;
            }
            return getFromFile(f2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x005e A[SYNTHETIC, Splitter:B:29:0x005e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] getFromFile(java.io.File r11) {
        /*
            r10 = this;
            r6 = 0
            if (r11 != 0) goto L_0x0004
        L_0x0003:
            return r6
        L_0x0004:
            r3 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x006a }
            r4.<init>(r11)     // Catch:{ Exception -> 0x006a }
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0020, all -> 0x0067 }
            r0.<init>()     // Catch:{ Exception -> 0x0020, all -> 0x0067 }
            r5 = 0
            r7 = 1024(0x400, float:1.435E-42)
            byte[] r1 = new byte[r7]     // Catch:{ Exception -> 0x0020, all -> 0x0067 }
        L_0x0014:
            int r5 = r4.read(r1)     // Catch:{ Exception -> 0x0020, all -> 0x0067 }
            r7 = -1
            if (r5 == r7) goto L_0x004d
            r7 = 0
            r0.write(r1, r7, r5)     // Catch:{ Exception -> 0x0020, all -> 0x0067 }
            goto L_0x0014
        L_0x0020:
            r2 = move-exception
            r3 = r4
        L_0x0022:
            java.lang.String r7 = dji.thirdparty.afinal.bitmap.download.SimpleDownloader.TAG     // Catch:{ all -> 0x005b }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x005b }
            r8.<init>()     // Catch:{ all -> 0x005b }
            java.lang.String r9 = "Error in read from file - "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x005b }
            java.lang.StringBuilder r8 = r8.append(r11)     // Catch:{ all -> 0x005b }
            java.lang.String r9 = " : "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x005b }
            java.lang.StringBuilder r8 = r8.append(r2)     // Catch:{ all -> 0x005b }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x005b }
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x005b }
            if (r3 == 0) goto L_0x0003
            r3.close()     // Catch:{ IOException -> 0x0063 }
            r3 = 0
            goto L_0x0003
        L_0x004d:
            byte[] r6 = r0.toByteArray()     // Catch:{ Exception -> 0x0020, all -> 0x0067 }
            if (r4 == 0) goto L_0x006c
            r4.close()     // Catch:{ IOException -> 0x0058 }
            r3 = 0
            goto L_0x0003
        L_0x0058:
            r7 = move-exception
            r3 = r4
            goto L_0x0003
        L_0x005b:
            r6 = move-exception
        L_0x005c:
            if (r3 == 0) goto L_0x0062
            r3.close()     // Catch:{ IOException -> 0x0065 }
            r3 = 0
        L_0x0062:
            throw r6
        L_0x0063:
            r7 = move-exception
            goto L_0x0003
        L_0x0065:
            r7 = move-exception
            goto L_0x0062
        L_0x0067:
            r6 = move-exception
            r3 = r4
            goto L_0x005c
        L_0x006a:
            r2 = move-exception
            goto L_0x0022
        L_0x006c:
            r3 = r4
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.afinal.bitmap.download.SimpleDownloader.getFromFile(java.io.File):byte[]");
    }

    /* JADX WARN: Type inference failed for: r9v5, types: [java.net.URLConnection], assign insn: 0x0008: INVOKE  (r9v5 ? I:java.net.URLConnection) = (r7v0 'url' java.net.URL A[D('url' java.net.URL)]) type: VIRTUAL call: java.net.URL.openConnection():java.net.URLConnection */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0089 A[SYNTHETIC, Splitter:B:34:0x0089] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x008e A[Catch:{ IOException -> 0x0092 }] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] getFromHttp(java.lang.String r13) {
        /*
            r12 = this;
            r8 = 0
            r6 = 0
            r4 = 0
            java.net.URL r7 = new java.net.URL     // Catch:{ IOException -> 0x0099 }
            r7.<init>(r13)     // Catch:{ IOException -> 0x0099 }
            java.net.URLConnection r9 = r7.openConnection()     // Catch:{ IOException -> 0x0099 }
            r0 = r9
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ IOException -> 0x0099 }
            r8 = r0
            r9 = 6000(0x1770, float:8.408E-42)
            r8.setConnectTimeout(r9)     // Catch:{ IOException -> 0x0099 }
            dji.thirdparty.afinal.bitmap.download.SimpleDownloader$FlushedInputStream r5 = new dji.thirdparty.afinal.bitmap.download.SimpleDownloader$FlushedInputStream     // Catch:{ IOException -> 0x0099 }
            java.io.BufferedInputStream r9 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0099 }
            java.io.InputStream r10 = r8.getInputStream()     // Catch:{ IOException -> 0x0099 }
            r11 = 8192(0x2000, float:1.14794E-41)
            r9.<init>(r10, r11)     // Catch:{ IOException -> 0x0099 }
            r5.<init>(r9)     // Catch:{ IOException -> 0x0099 }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0035, all -> 0x0094 }
            r2.<init>()     // Catch:{ IOException -> 0x0035, all -> 0x0094 }
        L_0x002a:
            int r1 = r5.read()     // Catch:{ IOException -> 0x0035, all -> 0x0094 }
            r9 = -1
            if (r1 == r9) goto L_0x006c
            r2.write(r1)     // Catch:{ IOException -> 0x0035, all -> 0x0094 }
            goto L_0x002a
        L_0x0035:
            r3 = move-exception
            r4 = r5
        L_0x0037:
            java.lang.String r9 = dji.thirdparty.afinal.bitmap.download.SimpleDownloader.TAG     // Catch:{ all -> 0x0081 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0081 }
            r10.<init>()     // Catch:{ all -> 0x0081 }
            java.lang.String r11 = "Error in downloadBitmap - "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0081 }
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch:{ all -> 0x0081 }
            java.lang.String r11 = " : "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x0081 }
            java.lang.StringBuilder r10 = r10.append(r3)     // Catch:{ all -> 0x0081 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0081 }
            android.util.Log.e(r9, r10)     // Catch:{ all -> 0x0081 }
            if (r8 == 0) goto L_0x0060
            r8.disconnect()
        L_0x0060:
            if (r6 == 0) goto L_0x0065
            r6.close()     // Catch:{ IOException -> 0x0097 }
        L_0x0065:
            if (r4 == 0) goto L_0x006a
            r4.close()     // Catch:{ IOException -> 0x0097 }
        L_0x006a:
            r9 = 0
        L_0x006b:
            return r9
        L_0x006c:
            byte[] r9 = r2.toByteArray()     // Catch:{ IOException -> 0x0035, all -> 0x0094 }
            if (r8 == 0) goto L_0x0075
            r8.disconnect()
        L_0x0075:
            if (r6 == 0) goto L_0x007a
            r6.close()     // Catch:{ IOException -> 0x009b }
        L_0x007a:
            if (r5 == 0) goto L_0x007f
            r5.close()     // Catch:{ IOException -> 0x009b }
        L_0x007f:
            r4 = r5
            goto L_0x006b
        L_0x0081:
            r9 = move-exception
        L_0x0082:
            if (r8 == 0) goto L_0x0087
            r8.disconnect()
        L_0x0087:
            if (r6 == 0) goto L_0x008c
            r6.close()     // Catch:{ IOException -> 0x0092 }
        L_0x008c:
            if (r4 == 0) goto L_0x0091
            r4.close()     // Catch:{ IOException -> 0x0092 }
        L_0x0091:
            throw r9
        L_0x0092:
            r10 = move-exception
            goto L_0x0091
        L_0x0094:
            r9 = move-exception
            r4 = r5
            goto L_0x0082
        L_0x0097:
            r9 = move-exception
            goto L_0x006a
        L_0x0099:
            r3 = move-exception
            goto L_0x0037
        L_0x009b:
            r10 = move-exception
            goto L_0x007f
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.afinal.bitmap.download.SimpleDownloader.getFromHttp(java.lang.String):byte[]");
    }

    public class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0;
            while (totalBytesSkipped < n) {
                long bytesSkipped = this.in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0) {
                    if (read() < 0) {
                        break;
                    }
                    bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
