package com.facebook.soloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcel;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public final class SysUtil {
    private static final byte APK_SIGNATURE_VERSION = 1;

    public static int findAbiScore(String[] supportedAbis, String abi) {
        for (int i = 0; i < supportedAbis.length; i++) {
            if (supportedAbis[i] != null && abi.equals(supportedAbis[i])) {
                return i;
            }
        }
        return -1;
    }

    public static void deleteOrThrow(File file) throws IOException {
        if (!file.delete()) {
            throw new IOException("could not delete file " + file);
        }
    }

    public static String[] getSupportedAbis() {
        if (Build.VERSION.SDK_INT >= 21) {
            return LollipopSysdeps.getSupportedAbis();
        }
        return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
    }

    public static void fallocateIfSupported(FileDescriptor fd, long length) throws IOException {
        if (Build.VERSION.SDK_INT >= 21) {
            LollipopSysdeps.fallocateIfSupported(fd, length);
        }
    }

    public static void dumbDeleteRecursive(File file) throws IOException {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File entry : fileList) {
                    dumbDeleteRecursive(entry);
                }
            } else {
                return;
            }
        }
        if (!file.delete() && file.exists()) {
            throw new IOException("could not delete: " + file);
        }
    }

    @DoNotOptimize
    @TargetApi(21)
    private static final class LollipopSysdeps {
        private LollipopSysdeps() {
        }

        @DoNotOptimize
        public static String[] getSupportedAbis() {
            return Build.SUPPORTED_ABIS;
        }

        @DoNotOptimize
        public static void fallocateIfSupported(FileDescriptor fd, long length) throws IOException {
            try {
                Os.posix_fallocate(fd, 0, length);
            } catch (ErrnoException ex) {
                if (ex.errno != OsConstants.EOPNOTSUPP && ex.errno != OsConstants.ENOSYS && ex.errno != OsConstants.EINVAL) {
                    throw new IOException(ex.toString(), ex);
                }
            }
        }
    }

    static void mkdirOrThrow(File dir) throws IOException {
        if (!dir.mkdirs() && !dir.isDirectory()) {
            throw new IOException("cannot mkdir: " + dir);
        }
    }

    static int copyBytes(RandomAccessFile os, InputStream is, int byteLimit, byte[] buffer) throws IOException {
        int bytesCopied = 0;
        while (bytesCopied < byteLimit) {
            int nrRead = is.read(buffer, 0, Math.min(buffer.length, byteLimit - bytesCopied));
            if (nrRead == -1) {
                break;
            }
            os.write(buffer, 0, nrRead);
            bytesCopied += nrRead;
        }
        return bytesCopied;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0063, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0064, code lost:
        r5 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0076, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0077, code lost:
        r4 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void fsyncRecursive(java.io.File r6) throws java.io.IOException {
        /*
            boolean r3 = r6.isDirectory()
            if (r3 == 0) goto L_0x0032
            java.io.File[] r1 = r6.listFiles()
            if (r1 != 0) goto L_0x0026
            java.io.IOException r3 = new java.io.IOException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "cannot list directory "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            throw r3
        L_0x0026:
            r2 = 0
        L_0x0027:
            int r3 = r1.length
            if (r2 >= r3) goto L_0x003f
            r3 = r1[r2]
            fsyncRecursive(r3)
            int r2 = r2 + 1
            goto L_0x0027
        L_0x0032:
            java.lang.String r3 = r6.getPath()
            java.lang.String r4 = "_lock"
            boolean r3 = r3.endsWith(r4)
            if (r3 == 0) goto L_0x0040
        L_0x003f:
            return
        L_0x0040:
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile
            java.lang.String r3 = "r"
            r0.<init>(r6, r3)
            r5 = 0
            java.io.FileDescriptor r3 = r0.getFD()     // Catch:{ Throwable -> 0x0061, all -> 0x0076 }
            r3.sync()     // Catch:{ Throwable -> 0x0061, all -> 0x0076 }
            if (r0 == 0) goto L_0x003f
            if (r5 == 0) goto L_0x005d
            r0.close()     // Catch:{ Throwable -> 0x0058 }
            goto L_0x003f
        L_0x0058:
            r3 = move-exception
            r5.addSuppressed(r3)
            goto L_0x003f
        L_0x005d:
            r0.close()
            goto L_0x003f
        L_0x0061:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0063 }
        L_0x0063:
            r4 = move-exception
            r5 = r3
        L_0x0065:
            if (r0 == 0) goto L_0x006c
            if (r5 == 0) goto L_0x0072
            r0.close()     // Catch:{ Throwable -> 0x006d }
        L_0x006c:
            throw r4
        L_0x006d:
            r3 = move-exception
            r5.addSuppressed(r3)
            goto L_0x006c
        L_0x0072:
            r0.close()
            goto L_0x006c
        L_0x0076:
            r3 = move-exception
            r4 = r3
            goto L_0x0065
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.SysUtil.fsyncRecursive(java.io.File):void");
    }

    public static byte[] makeApkDepBlock(File apkFile, Context context) throws IOException {
        File apkFile2 = apkFile.getCanonicalFile();
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeByte((byte) 1);
            parcel.writeString(apkFile2.getPath());
            parcel.writeLong(apkFile2.lastModified());
            parcel.writeInt(getAppVersionCode(context));
            return parcel.marshall();
        } finally {
            parcel.recycle();
        }
    }

    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return 0;
        }
        try {
            return pm.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException | RuntimeException e) {
            return 0;
        }
    }
}
