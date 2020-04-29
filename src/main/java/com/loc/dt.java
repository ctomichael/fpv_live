package com.loc;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import java.io.UnsupportedEncodingException;
import org.xeustechnologies.jtar.TarHeader;

/* compiled from: Base64 */
public class dt {
    static final /* synthetic */ boolean a = (!dt.class.desiredAssertionStatus());

    /* compiled from: Base64 */
    static abstract class a {
        public byte[] a;
        public int b;

        a() {
        }
    }

    /* compiled from: Base64 */
    static class b extends a {
        private static final int[] c = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] d = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int e = 0;
        private int f = 0;
        private final int[] g = c;

        public b(byte[] bArr) {
            this.a = bArr;
        }

        /* JADX WARNING: Removed duplicated region for block: B:44:0x0102  */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x0107  */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x0110  */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x011f  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean a(byte[] r14, int r15) {
            /*
                r13 = this;
                r12 = -2
                r11 = -1
                r10 = 6
                r3 = 0
                int r0 = r13.e
                if (r0 != r10) goto L_0x000a
                r0 = r3
            L_0x0009:
                return r0
            L_0x000a:
                int r7 = r15 + 0
                int r4 = r13.e
                int r1 = r13.f
                byte[] r8 = r13.a
                int[] r9 = r13.g
                r0 = r3
                r2 = r3
                r6 = r4
            L_0x0017:
                if (r2 >= r7) goto L_0x00f7
                if (r6 != 0) goto L_0x0060
            L_0x001b:
                int r4 = r2 + 4
                if (r4 > r7) goto L_0x005e
                byte r1 = r14[r2]
                r1 = r1 & 255(0xff, float:3.57E-43)
                r1 = r9[r1]
                int r1 = r1 << 18
                int r4 = r2 + 1
                byte r4 = r14[r4]
                r4 = r4 & 255(0xff, float:3.57E-43)
                r4 = r9[r4]
                int r4 = r4 << 12
                r1 = r1 | r4
                int r4 = r2 + 2
                byte r4 = r14[r4]
                r4 = r4 & 255(0xff, float:3.57E-43)
                r4 = r9[r4]
                int r4 = r4 << 6
                r1 = r1 | r4
                int r4 = r2 + 3
                byte r4 = r14[r4]
                r4 = r4 & 255(0xff, float:3.57E-43)
                r4 = r9[r4]
                r1 = r1 | r4
                if (r1 < 0) goto L_0x005e
                int r4 = r0 + 2
                byte r5 = (byte) r1
                r8[r4] = r5
                int r4 = r0 + 1
                int r5 = r1 >> 8
                byte r5 = (byte) r5
                r8[r4] = r5
                int r4 = r1 >> 16
                byte r4 = (byte) r4
                r8[r0] = r4
                int r0 = r0 + 3
                int r2 = r2 + 4
                goto L_0x001b
            L_0x005e:
                if (r2 >= r7) goto L_0x00f7
            L_0x0060:
                int r5 = r2 + 1
                byte r2 = r14[r2]
                r2 = r2 & 255(0xff, float:3.57E-43)
                r4 = r9[r2]
                switch(r6) {
                    case 0: goto L_0x006d;
                    case 1: goto L_0x007a;
                    case 2: goto L_0x008a;
                    case 3: goto L_0x00a9;
                    case 4: goto L_0x00e1;
                    case 5: goto L_0x00f0;
                    default: goto L_0x006b;
                }
            L_0x006b:
                r2 = r5
                goto L_0x0017
            L_0x006d:
                if (r4 < 0) goto L_0x0074
                int r6 = r6 + 1
                r1 = r4
                r2 = r5
                goto L_0x0017
            L_0x0074:
                if (r4 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x007a:
                if (r4 < 0) goto L_0x0084
                int r1 = r1 << 6
                r1 = r1 | r4
                int r4 = r6 + 1
                r2 = r5
                r6 = r4
                goto L_0x0017
            L_0x0084:
                if (r4 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x008a:
                if (r4 < 0) goto L_0x0094
                int r1 = r1 << 6
                r1 = r1 | r4
                int r4 = r6 + 1
                r2 = r5
                r6 = r4
                goto L_0x0017
            L_0x0094:
                if (r4 != r12) goto L_0x00a2
                int r4 = r0 + 1
                int r2 = r1 >> 4
                byte r2 = (byte) r2
                r8[r0] = r2
                r6 = 4
                r0 = r4
                r2 = r5
                goto L_0x0017
            L_0x00a2:
                if (r4 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00a9:
                if (r4 < 0) goto L_0x00c5
                int r1 = r1 << 6
                r1 = r1 | r4
                int r2 = r0 + 2
                byte r4 = (byte) r1
                r8[r2] = r4
                int r2 = r0 + 1
                int r4 = r1 >> 8
                byte r4 = (byte) r4
                r8[r2] = r4
                int r2 = r1 >> 16
                byte r2 = (byte) r2
                r8[r0] = r2
                int r0 = r0 + 3
                r2 = r5
                r6 = r3
                goto L_0x0017
            L_0x00c5:
                if (r4 != r12) goto L_0x00da
                int r2 = r0 + 1
                int r4 = r1 >> 2
                byte r4 = (byte) r4
                r8[r2] = r4
                int r2 = r1 >> 10
                byte r2 = (byte) r2
                r8[r0] = r2
                int r0 = r0 + 2
                r4 = 5
                r2 = r5
                r6 = r4
                goto L_0x0017
            L_0x00da:
                if (r4 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00e1:
                if (r4 != r12) goto L_0x00e9
                int r4 = r6 + 1
                r2 = r5
                r6 = r4
                goto L_0x0017
            L_0x00e9:
                if (r4 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00f0:
                if (r4 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00f7:
                r2 = r1
                switch(r6) {
                    case 0: goto L_0x00fb;
                    case 1: goto L_0x0102;
                    case 2: goto L_0x0107;
                    case 3: goto L_0x0110;
                    case 4: goto L_0x011f;
                    default: goto L_0x00fb;
                }
            L_0x00fb:
                r13.e = r6
                r13.b = r0
                r0 = 1
                goto L_0x0009
            L_0x0102:
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x0107:
                int r1 = r0 + 1
                int r2 = r2 >> 4
                byte r2 = (byte) r2
                r8[r0] = r2
                r0 = r1
                goto L_0x00fb
            L_0x0110:
                int r1 = r0 + 1
                int r3 = r2 >> 10
                byte r3 = (byte) r3
                r8[r0] = r3
                int r0 = r1 + 1
                int r2 = r2 >> 2
                byte r2 = (byte) r2
                r8[r1] = r2
                goto L_0x00fb
            L_0x011f:
                r13.e = r10
                r0 = r3
                goto L_0x0009
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.dt.b.a(byte[], int):boolean");
        }
    }

    /* compiled from: Base64 */
    static class c extends a {
        static final /* synthetic */ boolean g = (!dt.class.desiredAssertionStatus());
        private static final byte[] h = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 43, 47};
        private static final byte[] i = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 45, 95};
        int c;
        public final boolean d;
        public final boolean e;
        public final boolean f;
        private final byte[] j;
        private int k;
        private final byte[] l;

        public c(int i2) {
            boolean z = true;
            this.a = null;
            this.d = (i2 & 1) == 0;
            this.e = (i2 & 2) == 0;
            this.f = (i2 & 4) == 0 ? false : z;
            this.l = (i2 & 8) == 0 ? h : i;
            this.j = new byte[2];
            this.c = 0;
            this.k = this.e ? 19 : -1;
        }

        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        public final boolean a(byte[] bArr, int i2) {
            int i3;
            int i4;
            int i5;
            int i6;
            byte b;
            byte b2;
            int i7;
            byte b3;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12 = 0;
            byte[] bArr2 = this.l;
            byte[] bArr3 = this.a;
            int i13 = this.k;
            int i14 = i2 + 0;
            byte b4 = -1;
            switch (this.c) {
                case 0:
                    i3 = 0;
                    break;
                case 1:
                    if (2 <= i14) {
                        b4 = (bArr[1] & 255) | ((this.j[0] & 255) << Tnaf.POW_2_WIDTH) | ((bArr[0] & 255) << 8);
                        this.c = 0;
                        i3 = 2;
                        break;
                    }
                    i3 = 0;
                    break;
                case 2:
                    if (i14 > 0) {
                        b4 = (bArr[0] & 255) | ((this.j[0] & 255) << Tnaf.POW_2_WIDTH) | ((this.j[1] & 255) << 8);
                        this.c = 0;
                        i3 = 1;
                        break;
                    }
                    i3 = 0;
                    break;
                default:
                    i3 = 0;
                    break;
            }
            if (b4 != -1) {
                bArr3[0] = bArr2[(b4 >> 18) & 63];
                bArr3[1] = bArr2[(b4 >> 12) & 63];
                bArr3[2] = bArr2[(b4 >> 6) & 63];
                int i15 = 4;
                bArr3[3] = bArr2[b4 & 63];
                int i16 = i13 - 1;
                if (i16 == 0) {
                    if (this.f) {
                        i15 = 5;
                        bArr3[4] = Draft_75.CR;
                    }
                    i5 = i15 + 1;
                    bArr3[i15] = 10;
                    i4 = 19;
                } else {
                    i4 = i16;
                    i5 = 4;
                }
            } else {
                i4 = i13;
                i5 = 0;
            }
            while (i3 + 3 <= i14) {
                byte b5 = ((bArr[i3] & 255) << Tnaf.POW_2_WIDTH) | ((bArr[i3 + 1] & 255) << 8) | (bArr[i3 + 2] & 255);
                bArr3[i5] = bArr2[(b5 >> 18) & 63];
                bArr3[i5 + 1] = bArr2[(b5 >> 12) & 63];
                bArr3[i5 + 2] = bArr2[(b5 >> 6) & 63];
                bArr3[i5 + 3] = bArr2[b5 & 63];
                i3 += 3;
                int i17 = i5 + 4;
                int i18 = i4 - 1;
                if (i18 == 0) {
                    if (this.f) {
                        i11 = i17 + 1;
                        bArr3[i17] = Draft_75.CR;
                    } else {
                        i11 = i17;
                    }
                    i10 = i11 + 1;
                    bArr3[i11] = 10;
                    i9 = 19;
                } else {
                    i9 = i18;
                    i10 = i17;
                }
            }
            if (i3 - this.c == i14 - 1) {
                if (this.c > 0) {
                    b3 = this.j[0];
                    i8 = 1;
                } else {
                    b3 = bArr[i3];
                    i8 = 0;
                    i3++;
                }
                int i19 = (b3 & 255) << 4;
                this.c -= i8;
                int i20 = i5 + 1;
                bArr3[i5] = bArr2[(i19 >> 6) & 63];
                int i21 = i20 + 1;
                bArr3[i20] = bArr2[i19 & 63];
                if (this.d) {
                    int i22 = i21 + 1;
                    bArr3[i21] = 61;
                    i21 = i22 + 1;
                    bArr3[i22] = 61;
                }
                if (this.e) {
                    if (this.f) {
                        bArr3[i21] = Draft_75.CR;
                        i21++;
                    }
                    bArr3[i21] = 10;
                    i21++;
                }
                i5 = i21;
            } else if (i3 - this.c == i14 - 2) {
                if (this.c > 1) {
                    b = this.j[0];
                    i12 = 1;
                } else {
                    b = bArr[i3];
                    i3++;
                }
                int i23 = (b & 255) << 10;
                if (this.c > 0) {
                    b2 = this.j[i12];
                    i12++;
                } else {
                    b2 = bArr[i3];
                    i3++;
                }
                int i24 = ((b2 & 255) << 2) | i23;
                this.c -= i12;
                int i25 = i5 + 1;
                bArr3[i5] = bArr2[(i24 >> 12) & 63];
                int i26 = i25 + 1;
                bArr3[i25] = bArr2[(i24 >> 6) & 63];
                int i27 = i26 + 1;
                bArr3[i26] = bArr2[i24 & 63];
                if (this.d) {
                    i7 = i27 + 1;
                    bArr3[i27] = 61;
                } else {
                    i7 = i27;
                }
                if (this.e) {
                    if (this.f) {
                        bArr3[i7] = Draft_75.CR;
                        i7++;
                    }
                    bArr3[i7] = 10;
                    i7++;
                }
                i5 = i7;
            } else if (this.e && i5 > 0 && i4 != 19) {
                if (this.f) {
                    i6 = i5 + 1;
                    bArr3[i5] = Draft_75.CR;
                } else {
                    i6 = i5;
                }
                i5 = i6 + 1;
                bArr3[i6] = 10;
            }
            if (!g && this.c != 0) {
                throw new AssertionError();
            } else if (g || i3 == i14) {
                this.b = i5;
                this.k = i4;
                return true;
            } else {
                throw new AssertionError();
            }
        }
    }

    private dt() {
    }

    public static String a(byte[] bArr, int i) {
        int i2;
        try {
            int length = bArr.length;
            c cVar = new c(i);
            int i3 = (length / 3) * 4;
            if (!cVar.d) {
                switch (length % 3) {
                    case 1:
                        i3 += 2;
                        break;
                    case 2:
                        i3 += 3;
                        break;
                }
            } else if (length % 3 > 0) {
                i3 += 4;
            }
            if (!cVar.e || length <= 0) {
                i2 = i3;
            } else {
                i2 = ((cVar.f ? 2 : 1) * (((length - 1) / 57) + 1)) + i3;
            }
            cVar.a = new byte[i2];
            cVar.a(bArr, length);
            if (a || cVar.b == i2) {
                return new String(cVar.a, "US-ASCII");
            }
            throw new AssertionError();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] a(String str) {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        b bVar = new b(new byte[((length * 3) / 4)]);
        if (!bVar.a(bytes, length)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (bVar.b == bVar.a.length) {
            return bVar.a;
        } else {
            byte[] bArr = new byte[bVar.b];
            System.arraycopy(bVar.a, 0, bArr, 0, bVar.b);
            return bArr;
        }
    }
}
