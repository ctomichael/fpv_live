package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    @Nullable
    public static File getTempFile(Context context) {
        String prefix = CACHE_FILE_PREFIX + Process.myPid() + "-" + Process.myTid() + "-";
        int i = 0;
        while (i < 100) {
            File file = new File(context.getCacheDir(), prefix + i);
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException e) {
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002d, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002e, code lost:
        r3 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0040, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0041, code lost:
        r2 = r1;
        r3 = null;
     */
    @android.support.annotation.Nullable
    @android.support.annotation.RequiresApi(19)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.nio.ByteBuffer mmap(java.io.File r10) {
        /*
            r8 = 0
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0024 }
            r7.<init>(r10)     // Catch:{ IOException -> 0x0024 }
            r9 = 0
            java.nio.channels.FileChannel r0 = r7.getChannel()     // Catch:{ Throwable -> 0x002b, all -> 0x0040 }
            long r4 = r0.size()     // Catch:{ Throwable -> 0x002b, all -> 0x0040 }
            java.nio.channels.FileChannel$MapMode r1 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ Throwable -> 0x002b, all -> 0x0040 }
            r2 = 0
            java.nio.MappedByteBuffer r1 = r0.map(r1, r2, r4)     // Catch:{ Throwable -> 0x002b, all -> 0x0040 }
            if (r7 == 0) goto L_0x001e
            if (r8 == 0) goto L_0x0027
            r7.close()     // Catch:{ Throwable -> 0x001f }
        L_0x001e:
            return r1
        L_0x001f:
            r2 = move-exception
            r9.addSuppressed(r2)     // Catch:{ IOException -> 0x0024 }
            goto L_0x001e
        L_0x0024:
            r6 = move-exception
            r1 = r8
            goto L_0x001e
        L_0x0027:
            r7.close()     // Catch:{ IOException -> 0x0024 }
            goto L_0x001e
        L_0x002b:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x002d }
        L_0x002d:
            r2 = move-exception
            r3 = r1
        L_0x002f:
            if (r7 == 0) goto L_0x0036
            if (r3 == 0) goto L_0x003c
            r7.close()     // Catch:{ Throwable -> 0x0037 }
        L_0x0036:
            throw r2     // Catch:{ IOException -> 0x0024 }
        L_0x0037:
            r1 = move-exception
            r3.addSuppressed(r1)     // Catch:{ IOException -> 0x0024 }
            goto L_0x0036
        L_0x003c:
            r7.close()     // Catch:{ IOException -> 0x0024 }
            goto L_0x0036
        L_0x0040:
            r1 = move-exception
            r2 = r1
            r3 = r8
            goto L_0x002f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(java.io.File):java.nio.ByteBuffer");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0067, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0068, code lost:
        r2 = r1;
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0071, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0072, code lost:
        r3 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x008d, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x008e, code lost:
        r2 = r1;
        r3 = null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0067 A[ExcHandler: all (r1v2 'th' java.lang.Throwable A[CUSTOM_DECLARE])] */
    @android.support.annotation.Nullable
    @android.support.annotation.RequiresApi(19)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.nio.ByteBuffer mmap(android.content.Context r13, android.os.CancellationSignal r14, android.net.Uri r15) {
        /*
            r10 = 0
            android.content.ContentResolver r9 = r13.getContentResolver()
            java.lang.String r1 = "r"
            android.os.ParcelFileDescriptor r8 = r9.openFileDescriptor(r15, r1, r14)     // Catch:{ IOException -> 0x001d }
            r11 = 0
            if (r8 != 0) goto L_0x0024
            if (r8 == 0) goto L_0x0016
            if (r10 == 0) goto L_0x0020
            r8.close()     // Catch:{ Throwable -> 0x0018 }
        L_0x0016:
            r1 = r10
        L_0x0017:
            return r1
        L_0x0018:
            r1 = move-exception
            r11.addSuppressed(r1)     // Catch:{ IOException -> 0x001d }
            goto L_0x0016
        L_0x001d:
            r6 = move-exception
            r1 = r10
            goto L_0x0017
        L_0x0020:
            r8.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0016
        L_0x0024:
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
            java.io.FileDescriptor r1 = r8.getFileDescriptor()     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
            r7.<init>(r1)     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
            r12 = 0
            java.nio.channels.FileChannel r0 = r7.getChannel()     // Catch:{ Throwable -> 0x006f, all -> 0x008d }
            long r4 = r0.size()     // Catch:{ Throwable -> 0x006f, all -> 0x008d }
            java.nio.channels.FileChannel$MapMode r1 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ Throwable -> 0x006f, all -> 0x008d }
            r2 = 0
            java.nio.MappedByteBuffer r1 = r0.map(r1, r2, r4)     // Catch:{ Throwable -> 0x006f, all -> 0x008d }
            if (r7 == 0) goto L_0x0045
            if (r10 == 0) goto L_0x0063
            r7.close()     // Catch:{ Throwable -> 0x0052, all -> 0x0067 }
        L_0x0045:
            if (r8 == 0) goto L_0x0017
            if (r10 == 0) goto L_0x006b
            r8.close()     // Catch:{ Throwable -> 0x004d }
            goto L_0x0017
        L_0x004d:
            r2 = move-exception
            r11.addSuppressed(r2)     // Catch:{ IOException -> 0x001d }
            goto L_0x0017
        L_0x0052:
            r2 = move-exception
            r12.addSuppressed(r2)     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
            goto L_0x0045
        L_0x0057:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0059 }
        L_0x0059:
            r2 = move-exception
            r3 = r1
        L_0x005b:
            if (r8 == 0) goto L_0x0062
            if (r3 == 0) goto L_0x0089
            r8.close()     // Catch:{ Throwable -> 0x0084 }
        L_0x0062:
            throw r2     // Catch:{ IOException -> 0x001d }
        L_0x0063:
            r7.close()     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
            goto L_0x0045
        L_0x0067:
            r1 = move-exception
            r2 = r1
            r3 = r10
            goto L_0x005b
        L_0x006b:
            r8.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0017
        L_0x006f:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0071 }
        L_0x0071:
            r2 = move-exception
            r3 = r1
        L_0x0073:
            if (r7 == 0) goto L_0x007a
            if (r3 == 0) goto L_0x0080
            r7.close()     // Catch:{ Throwable -> 0x007b, all -> 0x0067 }
        L_0x007a:
            throw r2     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
        L_0x007b:
            r1 = move-exception
            r3.addSuppressed(r1)     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
            goto L_0x007a
        L_0x0080:
            r7.close()     // Catch:{ Throwable -> 0x0057, all -> 0x0067 }
            goto L_0x007a
        L_0x0084:
            r1 = move-exception
            r3.addSuppressed(r1)     // Catch:{ IOException -> 0x001d }
            goto L_0x0062
        L_0x0089:
            r8.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0062
        L_0x008d:
            r1 = move-exception
            r2 = r1
            r3 = r10
            goto L_0x0073
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    @Nullable
    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context context, Resources res, int id) {
        ByteBuffer byteBuffer = null;
        File tmpFile = getTempFile(context);
        if (tmpFile != null) {
            try {
                if (copyToFile(tmpFile, res, id)) {
                    byteBuffer = mmap(tmpFile);
                    tmpFile.delete();
                }
            } finally {
                tmpFile.delete();
            }
        }
        return byteBuffer;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException} */
    public static boolean copyToFile(File file, InputStream is) {
        FileOutputStream os = null;
        StrictMode.ThreadPolicy old = StrictMode.allowThreadDiskWrites();
        try {
            FileOutputStream os2 = new FileOutputStream(file, false);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int readLen = is.read(buffer);
                    if (readLen != -1) {
                        os2.write(buffer, 0, readLen);
                    } else {
                        closeQuietly(os2);
                        StrictMode.setThreadPolicy(old);
                        return true;
                    }
                }
            } catch (IOException e) {
                e = e;
                os = os2;
                try {
                    Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
                    closeQuietly(os);
                    StrictMode.setThreadPolicy(old);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(os);
                    StrictMode.setThreadPolicy(old);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                os = os2;
                closeQuietly(os);
                StrictMode.setThreadPolicy(old);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
            closeQuietly(os);
            StrictMode.setThreadPolicy(old);
            return false;
        }
    }

    public static boolean copyToFile(File file, Resources res, int id) {
        InputStream is = null;
        try {
            is = res.openRawResource(id);
            return copyToFile(file, is);
        } finally {
            closeQuietly(is);
        }
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
