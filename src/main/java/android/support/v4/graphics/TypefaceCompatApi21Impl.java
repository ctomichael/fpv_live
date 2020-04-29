package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;

@RequiresApi(21)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String TAG = "TypefaceCompatApi21Impl";

    TypefaceCompatApi21Impl() {
    }

    private File getFile(ParcelFileDescriptor fd) {
        try {
            String path = Os.readlink("/proc/self/fd/" + fd.getFd());
            if (OsConstants.S_ISREG(Os.stat(path).st_mode)) {
                return new File(path);
            }
            return null;
        } catch (ErrnoException e) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x005b, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x005c, code lost:
        r8 = r7;
        r9 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x006a, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x009c, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x009d, code lost:
        r8 = null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x005b A[ExcHandler: all (r7v2 'th' java.lang.Throwable A[CUSTOM_DECLARE])] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r12, android.os.CancellationSignal r13, @android.support.annotation.NonNull android.support.v4.provider.FontsContractCompat.FontInfo[] r14, int r15) {
        /*
            r11 = this;
            r6 = 0
            int r7 = r14.length
            r8 = 1
            if (r7 >= r8) goto L_0x0006
        L_0x0005:
            return r6
        L_0x0006:
            android.support.v4.provider.FontsContractCompat$FontInfo r0 = r11.findBestInfo(r14, r15)
            android.content.ContentResolver r5 = r12.getContentResolver()
            android.net.Uri r7 = r0.getUri()     // Catch:{ IOException -> 0x0055 }
            java.lang.String r8 = "r"
            android.os.ParcelFileDescriptor r4 = r5.openFileDescriptor(r7, r8, r13)     // Catch:{ IOException -> 0x0055 }
            r8 = 0
            java.io.File r2 = r11.getFile(r4)     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            if (r2 == 0) goto L_0x0026
            boolean r7 = r2.canRead()     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            if (r7 != 0) goto L_0x007c
        L_0x0026:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            java.io.FileDescriptor r7 = r4.getFileDescriptor()     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            r3.<init>(r7)     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            r9 = 0
            android.graphics.Typeface r7 = super.createFromInputStream(r12, r3)     // Catch:{ Throwable -> 0x0068, all -> 0x009c }
            if (r3 == 0) goto L_0x003b
            if (r6 == 0) goto L_0x0057
            r3.close()     // Catch:{ Throwable -> 0x0044, all -> 0x005b }
        L_0x003b:
            if (r4 == 0) goto L_0x0042
            if (r6 == 0) goto L_0x0064
            r4.close()     // Catch:{ Throwable -> 0x005f }
        L_0x0042:
            r6 = r7
            goto L_0x0005
        L_0x0044:
            r10 = move-exception
            r9.addSuppressed(r10)     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            goto L_0x003b
        L_0x0049:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x004b }
        L_0x004b:
            r8 = move-exception
            r9 = r7
        L_0x004d:
            if (r4 == 0) goto L_0x0054
            if (r9 == 0) goto L_0x0098
            r4.close()     // Catch:{ Throwable -> 0x0093 }
        L_0x0054:
            throw r8     // Catch:{ IOException -> 0x0055 }
        L_0x0055:
            r1 = move-exception
            goto L_0x0005
        L_0x0057:
            r3.close()     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            goto L_0x003b
        L_0x005b:
            r7 = move-exception
            r8 = r7
            r9 = r6
            goto L_0x004d
        L_0x005f:
            r9 = move-exception
            r8.addSuppressed(r9)     // Catch:{ IOException -> 0x0055 }
            goto L_0x0042
        L_0x0064:
            r4.close()     // Catch:{ IOException -> 0x0055 }
            goto L_0x0042
        L_0x0068:
            r8 = move-exception
            throw r8     // Catch:{ all -> 0x006a }
        L_0x006a:
            r7 = move-exception
        L_0x006b:
            if (r3 == 0) goto L_0x0072
            if (r8 == 0) goto L_0x0078
            r3.close()     // Catch:{ Throwable -> 0x0073, all -> 0x005b }
        L_0x0072:
            throw r7     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
        L_0x0073:
            r9 = move-exception
            r8.addSuppressed(r9)     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            goto L_0x0072
        L_0x0078:
            r3.close()     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            goto L_0x0072
        L_0x007c:
            android.graphics.Typeface r7 = android.graphics.Typeface.createFromFile(r2)     // Catch:{ Throwable -> 0x0049, all -> 0x005b }
            if (r4 == 0) goto L_0x0087
            if (r6 == 0) goto L_0x008f
            r4.close()     // Catch:{ Throwable -> 0x008a }
        L_0x0087:
            r6 = r7
            goto L_0x0005
        L_0x008a:
            r9 = move-exception
            r8.addSuppressed(r9)     // Catch:{ IOException -> 0x0055 }
            goto L_0x0087
        L_0x008f:
            r4.close()     // Catch:{ IOException -> 0x0055 }
            goto L_0x0087
        L_0x0093:
            r7 = move-exception
            r9.addSuppressed(r7)     // Catch:{ IOException -> 0x0055 }
            goto L_0x0054
        L_0x0098:
            r4.close()     // Catch:{ IOException -> 0x0055 }
            goto L_0x0054
        L_0x009c:
            r7 = move-exception
            r8 = r6
            goto L_0x006b
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatApi21Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, android.support.v4.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }
}
