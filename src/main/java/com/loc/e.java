package com.loc;

import android.content.Context;
import android.os.storage.StorageManager;
import java.io.Closeable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/* compiled from: FileStorageModel */
public final class e {
    private static String a(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        try {
            Class<?> cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method method2 = cls.getMethod("getPath", new Class[0]);
            Method method3 = cls.getMethod("isRemovable", new Class[0]);
            Object invoke = method.invoke(storageManager, new Object[0]);
            int length = Array.getLength(invoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(invoke, i);
                String str = (String) method2.invoke(obj, new Object[0]);
                if (!((Boolean) method3.invoke(obj, new Object[0])).booleanValue()) {
                    return str;
                }
            }
        } catch (Throwable th) {
        }
        return null;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:28:0x0075=Splitter:B:28:0x0075, B:48:0x00be=Splitter:B:48:0x00be} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String a(android.content.Context r10, java.lang.String r11) {
        /*
            r2 = 0
            java.lang.Class<com.loc.e> r4 = com.loc.e.class
            monitor-enter(r4)
            java.lang.String r0 = a(r10)     // Catch:{ all -> 0x00bb }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x00bb }
            if (r1 == 0) goto L_0x0013
            java.lang.String r0 = ""
        L_0x0011:
            monitor-exit(r4)
            return r0
        L_0x0013:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bb }
            r1.<init>()     // Catch:{ all -> 0x00bb }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ all -> 0x00bb }
            java.lang.String r1 = java.io.File.separator     // Catch:{ all -> 0x00bb }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ all -> 0x00bb }
            java.lang.String r1 = "backups"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ all -> 0x00bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00bb }
            java.io.File r1 = new java.io.File     // Catch:{ all -> 0x00bb }
            java.lang.String r3 = ".adiu"
            r1.<init>(r0, r3)     // Catch:{ all -> 0x00bb }
            boolean r0 = r1.exists()     // Catch:{ all -> 0x00bb }
            if (r0 == 0) goto L_0x0041
            boolean r0 = r1.canRead()     // Catch:{ all -> 0x00bb }
            if (r0 != 0) goto L_0x0045
        L_0x0041:
            java.lang.String r0 = ""
            goto L_0x0011
        L_0x0045:
            long r6 = r1.length()     // Catch:{ all -> 0x00bb }
            r8 = 0
            int r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r0 != 0) goto L_0x0056
            r1.delete()     // Catch:{ all -> 0x00bb }
            java.lang.String r0 = ""
            goto L_0x0011
        L_0x0056:
            java.io.RandomAccessFile r3 = new java.io.RandomAccessFile     // Catch:{ Throwable -> 0x00d4, all -> 0x00c5 }
            java.lang.String r0 = "r"
            r3.<init>(r1, r0)     // Catch:{ Throwable -> 0x00d4, all -> 0x00c5 }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x00d8, all -> 0x00cf }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x00d8, all -> 0x00cf }
            r1.<init>()     // Catch:{ Throwable -> 0x00d8, all -> 0x00cf }
        L_0x0067:
            int r2 = r3.read(r0)     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            r5 = -1
            if (r2 == r5) goto L_0x007f
            r5 = 0
            r1.write(r0, r5, r2)     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            goto L_0x0067
        L_0x0073:
            r0 = move-exception
            r0 = r1
        L_0x0075:
            a(r0)     // Catch:{ all -> 0x00bb }
            a(r3)     // Catch:{ all -> 0x00bb }
        L_0x007b:
            java.lang.String r0 = ""
            goto L_0x0011
        L_0x007f:
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            byte[] r2 = r1.toByteArray()     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            java.lang.String r5 = "UTF-8"
            r0.<init>(r2, r5)     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            if (r2 != 0) goto L_0x00be
            java.lang.String r2 = "#"
            boolean r2 = r0.contains(r2)     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            if (r2 == 0) goto L_0x00be
            java.lang.String r2 = "#"
            java.lang.String[] r0 = r0.split(r2)     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            if (r0 == 0) goto L_0x00be
            int r2 = r0.length     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            r5 = 2
            if (r2 != r5) goto L_0x00be
            r2 = 0
            r2 = r0[r2]     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            boolean r2 = android.text.TextUtils.equals(r11, r2)     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            if (r2 == 0) goto L_0x00be
            r2 = 1
            r0 = r0[r2]     // Catch:{ Throwable -> 0x0073, all -> 0x00d2 }
            a(r1)     // Catch:{ all -> 0x00bb }
            a(r3)     // Catch:{ all -> 0x00bb }
            goto L_0x0011
        L_0x00bb:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        L_0x00be:
            a(r1)     // Catch:{ all -> 0x00bb }
            a(r3)     // Catch:{ all -> 0x00bb }
            goto L_0x007b
        L_0x00c5:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L_0x00c8:
            a(r1)     // Catch:{ all -> 0x00bb }
            a(r3)     // Catch:{ all -> 0x00bb }
            throw r0     // Catch:{ all -> 0x00bb }
        L_0x00cf:
            r0 = move-exception
            r1 = r2
            goto L_0x00c8
        L_0x00d2:
            r0 = move-exception
            goto L_0x00c8
        L_0x00d4:
            r0 = move-exception
            r0 = r2
            r3 = r2
            goto L_0x0075
        L_0x00d8:
            r0 = move-exception
            r0 = r2
            goto L_0x0075
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.e.a(android.content.Context, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0098 A[SYNTHETIC, Splitter:B:38:0x0098] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x009d A[SYNTHETIC, Splitter:B:41:0x009d] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00ac A[SYNTHETIC, Splitter:B:48:0x00ac] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00b1 A[SYNTHETIC, Splitter:B:51:0x00b1] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:43:0x00a0=Splitter:B:43:0x00a0, B:53:0x00b4=Splitter:B:53:0x00b4, B:30:0x008b=Splitter:B:30:0x008b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void a(android.content.Context r7, java.lang.String r8, java.lang.String r9) {
        /*
            r1 = 0
            java.lang.Class<com.loc.e> r6 = com.loc.e.class
            monitor-enter(r6)
            java.lang.String r0 = a(r7)     // Catch:{ all -> 0x008f }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x008f }
            if (r2 == 0) goto L_0x0010
        L_0x000e:
            monitor-exit(r6)
            return
        L_0x0010:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x008f }
            r2.<init>()     // Catch:{ all -> 0x008f }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ all -> 0x008f }
            java.lang.String r3 = "#"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x008f }
            java.lang.StringBuilder r2 = r2.append(r9)     // Catch:{ all -> 0x008f }
            java.lang.String r4 = r2.toString()     // Catch:{ all -> 0x008f }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x008f }
            r2.<init>()     // Catch:{ all -> 0x008f }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ all -> 0x008f }
            java.lang.String r2 = java.io.File.separator     // Catch:{ all -> 0x008f }
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x008f }
            java.lang.String r2 = "backups"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x008f }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x008f }
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x008f }
            r2.<init>(r0)     // Catch:{ all -> 0x008f }
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x008f }
            java.lang.String r3 = ".adiu"
            r0.<init>(r2, r3)     // Catch:{ all -> 0x008f }
            boolean r3 = r2.exists()     // Catch:{ Throwable -> 0x0092, all -> 0x00a5 }
            if (r3 == 0) goto L_0x005b
            boolean r3 = r2.isDirectory()     // Catch:{ Throwable -> 0x0092, all -> 0x00a5 }
            if (r3 == 0) goto L_0x005e
        L_0x005b:
            r2.mkdirs()     // Catch:{ Throwable -> 0x0092, all -> 0x00a5 }
        L_0x005e:
            r0.createNewFile()     // Catch:{ Throwable -> 0x0092, all -> 0x00a5 }
            java.io.RandomAccessFile r3 = new java.io.RandomAccessFile     // Catch:{ Throwable -> 0x0092, all -> 0x00a5 }
            java.lang.String r2 = "rws"
            r3.<init>(r0, r2)     // Catch:{ Throwable -> 0x0092, all -> 0x00a5 }
            java.nio.channels.FileChannel r2 = r3.getChannel()     // Catch:{ Throwable -> 0x00d1, all -> 0x00c4 }
            java.nio.channels.FileLock r0 = r2.tryLock()     // Catch:{ Throwable -> 0x00d5, all -> 0x00c9 }
            if (r0 == 0) goto L_0x0081
            java.lang.String r1 = "UTF-8"
            byte[] r1 = r4.getBytes(r1)     // Catch:{ Throwable -> 0x00d8, all -> 0x00cd }
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.wrap(r1)     // Catch:{ Throwable -> 0x00d8, all -> 0x00cd }
            r2.write(r1)     // Catch:{ Throwable -> 0x00d8, all -> 0x00cd }
        L_0x0081:
            if (r0 == 0) goto L_0x0086
            r0.release()     // Catch:{ IOException -> 0x00b8 }
        L_0x0086:
            if (r2 == 0) goto L_0x008b
            r2.close()     // Catch:{ IOException -> 0x00ba }
        L_0x008b:
            a(r3)     // Catch:{ all -> 0x008f }
            goto L_0x000e
        L_0x008f:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        L_0x0092:
            r0 = move-exception
            r0 = r1
            r2 = r1
            r3 = r1
        L_0x0096:
            if (r0 == 0) goto L_0x009b
            r0.release()     // Catch:{ IOException -> 0x00bc }
        L_0x009b:
            if (r2 == 0) goto L_0x00a0
            r2.close()     // Catch:{ IOException -> 0x00be }
        L_0x00a0:
            a(r3)     // Catch:{ all -> 0x008f }
            goto L_0x000e
        L_0x00a5:
            r0 = move-exception
            r4 = r0
            r5 = r1
            r2 = r1
            r3 = r1
        L_0x00aa:
            if (r5 == 0) goto L_0x00af
            r5.release()     // Catch:{ IOException -> 0x00c0 }
        L_0x00af:
            if (r2 == 0) goto L_0x00b4
            r2.close()     // Catch:{ IOException -> 0x00c2 }
        L_0x00b4:
            a(r3)     // Catch:{ all -> 0x008f }
            throw r4     // Catch:{ all -> 0x008f }
        L_0x00b8:
            r0 = move-exception
            goto L_0x0086
        L_0x00ba:
            r0 = move-exception
            goto L_0x008b
        L_0x00bc:
            r0 = move-exception
            goto L_0x009b
        L_0x00be:
            r0 = move-exception
            goto L_0x00a0
        L_0x00c0:
            r0 = move-exception
            goto L_0x00af
        L_0x00c2:
            r0 = move-exception
            goto L_0x00b4
        L_0x00c4:
            r0 = move-exception
            r4 = r0
            r5 = r1
            r2 = r1
            goto L_0x00aa
        L_0x00c9:
            r0 = move-exception
            r4 = r0
            r5 = r1
            goto L_0x00aa
        L_0x00cd:
            r1 = move-exception
            r4 = r1
            r5 = r0
            goto L_0x00aa
        L_0x00d1:
            r0 = move-exception
            r0 = r1
            r2 = r1
            goto L_0x0096
        L_0x00d5:
            r0 = move-exception
            r0 = r1
            goto L_0x0096
        L_0x00d8:
            r1 = move-exception
            goto L_0x0096
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.e.a(android.content.Context, java.lang.String, java.lang.String):void");
    }

    private static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }
}
