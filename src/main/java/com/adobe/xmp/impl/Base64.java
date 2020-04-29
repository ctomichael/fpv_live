package com.adobe.xmp.impl;

import org.xeustechnologies.jtar.TarHeader;

public class Base64 {
    private static final byte EQUAL = -3;
    private static final byte INVALID = -1;
    private static final byte WHITESPACE = -2;
    private static byte[] ascii = new byte[255];
    private static byte[] base64 = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 43, 47};

    static {
        for (int i = 0; i < 255; i++) {
            ascii[i] = -1;
        }
        for (int i2 = 0; i2 < base64.length; i2++) {
            ascii[base64[i2]] = (byte) i2;
        }
        ascii[9] = WHITESPACE;
        ascii[10] = WHITESPACE;
        ascii[13] = WHITESPACE;
        ascii[32] = WHITESPACE;
        ascii[61] = EQUAL;
    }

    public static final String decode(String str) {
        return new String(decode(str.getBytes()));
    }

    public static final byte[] decode(byte[] bArr) throws IllegalArgumentException {
        int i = 0;
        for (byte b : bArr) {
            byte b2 = ascii[b];
            if (b2 >= 0) {
                bArr[i] = b2;
                i++;
            } else if (b2 == -1) {
                throw new IllegalArgumentException("Invalid base 64 string");
            }
        }
        while (i > 0 && bArr[i - 1] == -3) {
            i--;
        }
        byte[] bArr2 = new byte[((i * 3) / 4)];
        int i2 = 0;
        int i3 = 0;
        while (i2 < bArr2.length - 2) {
            bArr2[i2] = (byte) (((bArr[i3] << 2) & 255) | ((bArr[i3 + 1] >>> 4) & 3));
            bArr2[i2 + 1] = (byte) (((bArr[i3 + 1] << 4) & 255) | ((bArr[i3 + 2] >>> 2) & 15));
            bArr2[i2 + 2] = (byte) (((bArr[i3 + 2] << 6) & 255) | (bArr[i3 + 3] & 63));
            i3 += 4;
            i2 += 3;
        }
        if (i2 < bArr2.length) {
            bArr2[i2] = (byte) (((bArr[i3] << 2) & 255) | ((bArr[i3 + 1] >>> 4) & 3));
        }
        int i4 = i2 + 1;
        if (i4 < bArr2.length) {
            bArr2[i4] = (byte) (((bArr[i3 + 1] << 4) & 255) | ((bArr[i3 + 2] >>> 2) & 15));
        }
        return bArr2;
    }

    public static final String encode(String str) {
        return new String(encode(str.getBytes()));
    }

    public static final byte[] encode(byte[] bArr) {
        return encode(bArr, 0);
    }

    public static final byte[] encode(byte[] bArr, int i) {
        int i2 = (i / 4) * 4;
        if (i2 < 0) {
            i2 = 0;
        }
        int length = ((bArr.length + 2) / 3) * 4;
        if (i2 > 0) {
            length += (length - 1) / i2;
        }
        byte[] bArr2 = new byte[length];
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i4 + 3 <= bArr.length) {
            int i6 = i4 + 1;
            int i7 = i6 + 1;
            int i8 = ((bArr[i6] & 255) << 8) | ((bArr[i4] & 255) << Tnaf.POW_2_WIDTH);
            i4 = i7 + 1;
            int i9 = i8 | ((bArr[i7] & 255) << 0);
            int i10 = i5 + 1;
            bArr2[i5] = base64[(i9 & 16515072) >> 18];
            int i11 = i10 + 1;
            bArr2[i10] = base64[(i9 & 258048) >> 12];
            int i12 = i11 + 1;
            bArr2[i11] = base64[(i9 & 4032) >> 6];
            int i13 = i12 + 1;
            bArr2[i12] = base64[i9 & 63];
            int i14 = i3 + 4;
            if (i13 >= length || i2 <= 0 || i14 % i2 != 0) {
                i3 = i14;
                i5 = i13;
            } else {
                i5 = i13 + 1;
                bArr2[i13] = 10;
                i3 = i14;
            }
        }
        if (bArr.length - i4 == 2) {
            int i15 = ((bArr[i4] & 255) << Tnaf.POW_2_WIDTH) | ((bArr[i4 + 1] & 255) << 8);
            int i16 = i5 + 1;
            bArr2[i5] = base64[(i15 & 16515072) >> 18];
            int i17 = i16 + 1;
            bArr2[i16] = base64[(i15 & 258048) >> 12];
            int i18 = i17 + 1;
            bArr2[i17] = base64[(i15 & 4032) >> 6];
            int i19 = i18 + 1;
            bArr2[i18] = 61;
        } else if (bArr.length - i4 == 1) {
            int i20 = (bArr[i4] & 255) << Tnaf.POW_2_WIDTH;
            int i21 = i5 + 1;
            bArr2[i5] = base64[(i20 & 16515072) >> 18];
            int i22 = i21 + 1;
            bArr2[i21] = base64[(i20 & 258048) >> 12];
            int i23 = i22 + 1;
            bArr2[i22] = 61;
            int i24 = i23 + 1;
            bArr2[i23] = 61;
        }
        return bArr2;
    }
}
