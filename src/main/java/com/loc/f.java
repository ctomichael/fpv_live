package com.loc;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import org.xeustechnologies.jtar.TarHeader;

/* compiled from: Base64Util */
public class f {
    static final /* synthetic */ boolean a = (!f.class.desiredAssertionStatus());
    private static final byte[] b = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 43, 47};
    private static final byte[] c = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, Draft_75.CR, 14, 15, Tnaf.POW_2_WIDTH, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
    private static final byte[] d = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 45, 95};
    private static final byte[] e = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, Draft_75.CR, 14, 15, Tnaf.POW_2_WIDTH, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
    private static final byte[] f = {45, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
    private static final byte[] g = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, Draft_75.CR, 14, 15, Tnaf.POW_2_WIDTH, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};

    private f() {
    }

    public static String a(byte[] bArr) {
        String str = null;
        try {
            str = a(bArr, bArr.length);
        } catch (IOException e2) {
            if (!a) {
                throw new AssertionError(e2.getMessage());
            }
        }
        if (a || str != null) {
            return str;
        }
        throw new AssertionError();
    }

    private static String a(byte[] bArr, int i) throws IOException {
        byte[] bArr2;
        if (bArr == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        } else if (i < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + i);
        } else if (i + 0 > bArr.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", 0, Integer.valueOf(i), Integer.valueOf(bArr.length)));
        } else {
            byte[] bArr3 = new byte[((i % 3 > 0 ? 4 : 0) + ((i / 3) * 4))];
            int i2 = i - 2;
            int i3 = 0;
            int i4 = 0;
            while (i4 < i2) {
                a(bArr, i4 + 0, 3, bArr3, i3);
                i4 += 3;
                i3 += 4;
            }
            if (i4 < i) {
                a(bArr, i4 + 0, i - i4, bArr3, i3);
                i3 += 4;
            }
            if (i3 <= bArr3.length - 1) {
                byte[] bArr4 = new byte[i3];
                System.arraycopy(bArr3, 0, bArr4, 0, i3);
                bArr2 = bArr4;
            } else {
                bArr2 = bArr3;
            }
            try {
                return new String(bArr2, "US-ASCII");
            } catch (UnsupportedEncodingException e2) {
                return new String(bArr2);
            }
        }
    }

    public static byte[] a(String str) throws IOException {
        return b(str);
    }

    private static byte[] a(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int i4 = 0;
        byte[] bArr3 = b;
        int i5 = (i2 > 1 ? (bArr[i + 1] << 24) >>> 16 : 0) | (i2 > 0 ? (bArr[i] << 24) >>> 8 : 0);
        if (i2 > 2) {
            i4 = (bArr[i + 2] << 24) >>> 24;
        }
        int i6 = i4 | i5;
        switch (i2) {
            case 1:
                bArr2[i3] = bArr3[i6 >>> 18];
                bArr2[i3 + 1] = bArr3[(i6 >>> 12) & 63];
                bArr2[i3 + 2] = 61;
                bArr2[i3 + 3] = 61;
                break;
            case 2:
                bArr2[i3] = bArr3[i6 >>> 18];
                bArr2[i3 + 1] = bArr3[(i6 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i6 >>> 6) & 63];
                bArr2[i3 + 3] = 61;
                break;
            case 3:
                bArr2[i3] = bArr3[i6 >>> 18];
                bArr2[i3 + 1] = bArr3[(i6 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i6 >>> 6) & 63];
                bArr2[i3 + 3] = bArr3[i6 & 63];
                break;
        }
        return bArr2;
    }

    private static byte[] b(String str) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream byteArrayOutputStream;
        GZIPInputStream gZIPInputStream;
        ByteArrayInputStream byteArrayInputStream;
        if (str == null) {
            throw new NullPointerException("Input string was null.");
        }
        try {
            bytes = str.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e2) {
            bytes = str.getBytes();
        }
        byte[] b2 = b(bytes, bytes.length);
        if (b2.length >= 4 && 35615 == ((b2[0] & 255) | ((b2[1] << 8) & 65280))) {
            byte[] bArr = new byte[2048];
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    byteArrayInputStream = new ByteArrayInputStream(b2);
                    try {
                        gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
                        while (true) {
                            try {
                                int read = gZIPInputStream.read(bArr);
                                if (read < 0) {
                                    break;
                                }
                                byteArrayOutputStream.write(bArr, 0, read);
                            } catch (IOException e3) {
                                e = e3;
                            }
                        }
                        b2 = byteArrayOutputStream.toByteArray();
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e4) {
                        }
                        try {
                            gZIPInputStream.close();
                        } catch (Exception e5) {
                        }
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e6) {
                        }
                    } catch (IOException e7) {
                        e = e7;
                        gZIPInputStream = null;
                        try {
                            e.printStackTrace();
                            try {
                                byteArrayOutputStream.close();
                            } catch (Exception e8) {
                            }
                            try {
                                gZIPInputStream.close();
                            } catch (Exception e9) {
                            }
                            try {
                                byteArrayInputStream.close();
                            } catch (Exception e10) {
                            }
                            return b2;
                        } catch (Throwable th) {
                            th = th;
                            try {
                                byteArrayOutputStream.close();
                            } catch (Exception e11) {
                            }
                            try {
                                gZIPInputStream.close();
                            } catch (Exception e12) {
                            }
                            try {
                                byteArrayInputStream.close();
                            } catch (Exception e13) {
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        gZIPInputStream = null;
                        byteArrayOutputStream.close();
                        gZIPInputStream.close();
                        byteArrayInputStream.close();
                        throw th;
                    }
                } catch (IOException e14) {
                    e = e14;
                    gZIPInputStream = null;
                    byteArrayInputStream = null;
                    e.printStackTrace();
                    byteArrayOutputStream.close();
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return b2;
                } catch (Throwable th3) {
                    th = th3;
                    gZIPInputStream = null;
                    byteArrayInputStream = null;
                    byteArrayOutputStream.close();
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    throw th;
                }
            } catch (IOException e15) {
                e = e15;
                byteArrayOutputStream = null;
                gZIPInputStream = null;
                byteArrayInputStream = null;
                e.printStackTrace();
                byteArrayOutputStream.close();
                gZIPInputStream.close();
                byteArrayInputStream.close();
                return b2;
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream = null;
                gZIPInputStream = null;
                byteArrayInputStream = null;
                byteArrayOutputStream.close();
                gZIPInputStream.close();
                byteArrayInputStream.close();
                throw th;
            }
        }
        return b2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00c2, code lost:
        r0 = new byte[r5];
        java.lang.System.arraycopy(r9, 0, r0, 0, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] b(byte[] r13, int r14) throws java.io.IOException {
        /*
            r12 = 61
            r3 = 3
            r2 = 2
            r1 = 1
            r4 = 0
            if (r13 != 0) goto L_0x0011
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Cannot decode null source array."
            r0.<init>(r1)
            throw r0
        L_0x0011:
            int r0 = r14 + 0
            int r5 = r13.length
            if (r0 <= r5) goto L_0x0038
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Source array with length %d cannot have offset of %d and process %d bytes."
            java.lang.Object[] r3 = new java.lang.Object[r3]
            int r6 = r13.length
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r3[r4] = r6
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r3[r1] = r4
            java.lang.Integer r1 = java.lang.Integer.valueOf(r14)
            r3[r2] = r1
            java.lang.String r1 = java.lang.String.format(r5, r3)
            r0.<init>(r1)
            throw r0
        L_0x0038:
            if (r14 != 0) goto L_0x003d
            byte[] r0 = new byte[r4]
        L_0x003c:
            return r0
        L_0x003d:
            r0 = 4
            if (r14 >= r0) goto L_0x0056
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Base64Util-encoded string must have at least four characters, but length specified was "
            r1.<init>(r2)
            java.lang.StringBuilder r1 = r1.append(r14)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0056:
            byte[] r8 = com.loc.f.c
            int r0 = r14 * 3
            int r0 = r0 / 4
            byte[] r9 = new byte[r0]
            r0 = 4
            byte[] r10 = new byte[r0]
            r7 = r4
            r5 = r4
            r6 = r4
        L_0x0064:
            int r0 = r14 + 0
            if (r7 >= r0) goto L_0x0151
            byte r0 = r13[r7]
            r0 = r0 & 255(0xff, float:3.57E-43)
            byte r0 = r8[r0]
            r11 = -5
            if (r0 < r11) goto L_0x0129
            r11 = -1
            if (r0 < r11) goto L_0x014f
            int r0 = r5 + 1
            byte r11 = r13[r7]
            r10[r5] = r11
            if (r0 <= r3) goto L_0x014a
            if (r6 < 0) goto L_0x0083
            int r0 = r6 + 2
            int r5 = r9.length
            if (r0 < r5) goto L_0x009f
        L_0x0083:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r3 = "Destination array with length %d cannot have offset of %d and still store three bytes."
            java.lang.Object[] r2 = new java.lang.Object[r2]
            int r5 = r9.length
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r2[r4] = r5
            java.lang.Integer r4 = java.lang.Integer.valueOf(r6)
            r2[r1] = r4
            java.lang.String r1 = java.lang.String.format(r3, r2)
            r0.<init>(r1)
            throw r0
        L_0x009f:
            byte[] r0 = com.loc.f.c
            byte r5 = r10[r2]
            if (r5 != r12) goto L_0x00c9
            byte r5 = r10[r4]
            byte r5 = r0[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r5 = r5 << 18
            byte r11 = r10[r1]
            byte r0 = r0[r11]
            r0 = r0 & 255(0xff, float:3.57E-43)
            int r0 = r0 << 12
            r0 = r0 | r5
            int r0 = r0 >>> 16
            byte r0 = (byte) r0
            r9[r6] = r0
            r0 = r1
        L_0x00bc:
            int r5 = r6 + r0
            byte r0 = r13[r7]
            if (r0 != r12) goto L_0x0148
        L_0x00c2:
            byte[] r0 = new byte[r5]
            java.lang.System.arraycopy(r9, r4, r0, r4, r5)
            goto L_0x003c
        L_0x00c9:
            byte r5 = r10[r3]
            if (r5 != r12) goto L_0x00f5
            byte r5 = r10[r4]
            byte r5 = r0[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r5 = r5 << 18
            byte r11 = r10[r1]
            byte r11 = r0[r11]
            r11 = r11 & 255(0xff, float:3.57E-43)
            int r11 = r11 << 12
            r5 = r5 | r11
            byte r11 = r10[r2]
            byte r0 = r0[r11]
            r0 = r0 & 255(0xff, float:3.57E-43)
            int r0 = r0 << 6
            r0 = r0 | r5
            int r5 = r0 >>> 16
            byte r5 = (byte) r5
            r9[r6] = r5
            int r5 = r6 + 1
            int r0 = r0 >>> 8
            byte r0 = (byte) r0
            r9[r5] = r0
            r0 = r2
            goto L_0x00bc
        L_0x00f5:
            byte r5 = r10[r4]
            byte r5 = r0[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r5 = r5 << 18
            byte r11 = r10[r1]
            byte r11 = r0[r11]
            r11 = r11 & 255(0xff, float:3.57E-43)
            int r11 = r11 << 12
            r5 = r5 | r11
            byte r11 = r10[r2]
            byte r11 = r0[r11]
            r11 = r11 & 255(0xff, float:3.57E-43)
            int r11 = r11 << 6
            r5 = r5 | r11
            byte r11 = r10[r3]
            byte r0 = r0[r11]
            r0 = r0 & 255(0xff, float:3.57E-43)
            r0 = r0 | r5
            int r5 = r0 >> 16
            byte r5 = (byte) r5
            r9[r6] = r5
            int r5 = r6 + 1
            int r11 = r0 >> 8
            byte r11 = (byte) r11
            r9[r5] = r11
            int r5 = r6 + 2
            byte r0 = (byte) r0
            r9[r5] = r0
            r0 = r3
            goto L_0x00bc
        L_0x0129:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r3 = "Bad Base64Util input character decimal %d in array position %d"
            java.lang.Object[] r2 = new java.lang.Object[r2]
            byte r5 = r13[r7]
            r5 = r5 & 255(0xff, float:3.57E-43)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r2[r4] = r5
            java.lang.Integer r4 = java.lang.Integer.valueOf(r7)
            r2[r1] = r4
            java.lang.String r1 = java.lang.String.format(r3, r2)
            r0.<init>(r1)
            throw r0
        L_0x0148:
            r0 = r4
            r6 = r5
        L_0x014a:
            int r7 = r7 + 1
            r5 = r0
            goto L_0x0064
        L_0x014f:
            r0 = r5
            goto L_0x014a
        L_0x0151:
            r5 = r6
            goto L_0x00c2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.f.b(byte[], int):byte[]");
    }
}
