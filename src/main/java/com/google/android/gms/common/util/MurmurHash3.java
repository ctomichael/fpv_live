package com.google.android.gms.common.util;

import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public class MurmurHash3 {
    @KeepForSdk
    public static int murmurhash3_x86_32(byte[] bArr, int i, int i2, int i3) {
        int i4;
        int i5 = i + (i2 & -4);
        int i6 = i3;
        while (i < i5) {
            int i7 = ((bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << Tnaf.POW_2_WIDTH) | (bArr[i + 3] << 24)) * -862048943;
            int i8 = (((i7 >>> 17) | (i7 << 15)) * 461845907) ^ i6;
            i6 = -430675100 + (((i8 >>> 19) | (i8 << 13)) * 5);
            i += 4;
        }
        int i9 = 0;
        switch (i2 & 3) {
            case 3:
                i9 = (bArr[i5 + 2] & 255) << Tnaf.POW_2_WIDTH;
            case 2:
                i9 |= (bArr[i5 + 1] & 255) << 8;
            case 1:
                int i10 = (i9 | (bArr[i5] & 255)) * -862048943;
                i4 = (((i10 >>> 17) | (i10 << 15)) * 461845907) ^ i6;
                break;
            default:
                i4 = i6;
                break;
        }
        int i11 = i4 ^ i2;
        int i12 = (i11 ^ (i11 >>> 16)) * -2048144789;
        int i13 = (i12 ^ (i12 >>> 13)) * -1028477387;
        return i13 ^ (i13 >>> 16);
    }

    private MurmurHash3() {
    }
}
