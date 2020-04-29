package com.drew.tools;

import com.drew.lang.annotations.NotNull;
import java.io.File;
import java.io.IOException;

public class FileUtil {
    static final /* synthetic */ boolean $assertionsDisabled = (!FileUtil.class.desiredAssertionStatus());

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0012  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveBytes(@com.drew.lang.annotations.NotNull java.io.File r3, @com.drew.lang.annotations.NotNull byte[] r4) throws java.io.IOException {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x000f }
            r1.<init>(r3)     // Catch:{ all -> 0x000f }
            r1.write(r4)     // Catch:{ all -> 0x0016 }
            if (r1 == 0) goto L_0x000e
            r1.close()
        L_0x000e:
            return
        L_0x000f:
            r2 = move-exception
        L_0x0010:
            if (r0 == 0) goto L_0x0015
            r0.close()
        L_0x0015:
            throw r2
        L_0x0016:
            r2 = move-exception
            r0 = r1
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: com.drew.tools.FileUtil.saveBytes(java.io.File, byte[]):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0030  */
    @com.drew.lang.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readBytes(@com.drew.lang.annotations.NotNull java.io.File r8) throws java.io.IOException {
        /*
            long r6 = r8.length()
            int r4 = (int) r6
            boolean r6 = com.drew.tools.FileUtil.$assertionsDisabled
            if (r6 != 0) goto L_0x0011
            if (r4 != 0) goto L_0x0011
            java.lang.AssertionError r6 = new java.lang.AssertionError
            r6.<init>()
            throw r6
        L_0x0011:
            byte[] r0 = new byte[r4]
            r5 = 0
            r2 = 0
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ all -> 0x002d }
            r3.<init>(r8)     // Catch:{ all -> 0x002d }
        L_0x001a:
            if (r5 == r4) goto L_0x0025
            int r6 = r4 - r5
            int r1 = r3.read(r0, r5, r6)     // Catch:{ all -> 0x0034 }
            r6 = -1
            if (r1 != r6) goto L_0x002b
        L_0x0025:
            if (r3 == 0) goto L_0x002a
            r3.close()
        L_0x002a:
            return r0
        L_0x002b:
            int r5 = r5 + r1
            goto L_0x001a
        L_0x002d:
            r6 = move-exception
        L_0x002e:
            if (r2 == 0) goto L_0x0033
            r2.close()
        L_0x0033:
            throw r6
        L_0x0034:
            r6 = move-exception
            r2 = r3
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.drew.tools.FileUtil.readBytes(java.io.File):byte[]");
    }

    @NotNull
    public static byte[] readBytes(@NotNull String filePath) throws IOException {
        return readBytes(new File(filePath));
    }
}
