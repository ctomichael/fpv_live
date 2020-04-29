package org.bouncycastle.pqc.crypto.sphincs;

import android.support.v4.media.session.PlaybackStateCompat;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;

class Horst {
    static final int HORST_K = 32;
    static final int HORST_LOGT = 16;
    static final int HORST_SIGBYTES = 13312;
    static final int HORST_SKBYTES = 32;
    static final int HORST_T = 65536;
    static final int N_MASKS = 32;

    Horst() {
    }

    static void expand_seed(byte[] bArr, byte[] bArr2) {
        Seed.prg(bArr, 0, PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE, bArr2, 0);
    }

    static int horst_sign(HashFunctions hashFunctions, byte[] bArr, int i, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5) {
        int i2;
        byte[] bArr6 = new byte[2097152];
        byte[] bArr7 = new byte[4194272];
        expand_seed(bArr6, bArr3);
        for (int i3 = 0; i3 < 65536; i3++) {
            hashFunctions.hash_n_n(bArr7, (65535 + i3) * 32, bArr6, i3 * 32);
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= 16) {
                break;
            }
            long j = (long) ((1 << (16 - i5)) - 1);
            long j2 = (long) ((1 << ((16 - i5) - 1)) - 1);
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 >= (1 << ((16 - i5) - 1))) {
                    break;
                }
                hashFunctions.hash_2n_n_mask(bArr7, (int) ((((long) i7) + j2) * 32), bArr7, (int) ((((long) (i7 * 2)) + j) * 32), bArr4, i5 * 2 * 32);
                i6 = i7 + 1;
            }
            i4 = i5 + 1;
        }
        int i8 = 2016;
        while (true) {
            i2 = i;
            if (i8 >= 4064) {
                break;
            }
            i = i2 + 1;
            bArr[i2] = bArr7[i8];
            i8++;
        }
        int i9 = 0;
        while (true) {
            int i10 = i9;
            if (i10 >= 32) {
                break;
            }
            int i11 = (bArr5[i10 * 2] & 255) + ((bArr5[(i10 * 2) + 1] & 255) << 8);
            int i12 = 0;
            while (i12 < 32) {
                bArr[i2] = bArr6[(i11 * 32) + i12];
                i12++;
                i2++;
            }
            int i13 = i11 + 65535;
            int i14 = 0;
            while (true) {
                int i15 = i14;
                if (i15 >= 10) {
                    break;
                }
                int i16 = (i13 & 1) != 0 ? i13 + 1 : i13 - 1;
                int i17 = 0;
                while (i17 < 32) {
                    bArr[i2] = bArr7[(i16 * 32) + i17];
                    i17++;
                    i2++;
                }
                i13 = (i16 - 1) / 2;
                i14 = i15 + 1;
            }
            i9 = i10 + 1;
        }
        for (int i18 = 0; i18 < 32; i18++) {
            bArr2[i18] = bArr7[i18];
        }
        return HORST_SIGBYTES;
    }

    static int horst_verify(HashFunctions hashFunctions, byte[] bArr, byte[] bArr2, int i, byte[] bArr3, byte[] bArr4) {
        byte[] bArr5 = new byte[1024];
        int i2 = i + 2048;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < 32) {
                int i5 = (bArr4[i4 * 2] & 255) + ((bArr4[(i4 * 2) + 1] & 255) << 8);
                if ((i5 & 1) == 0) {
                    hashFunctions.hash_n_n(bArr5, 0, bArr2, i2);
                    for (int i6 = 0; i6 < 32; i6++) {
                        bArr5[i6 + 32] = bArr2[i2 + 32 + i6];
                    }
                } else {
                    hashFunctions.hash_n_n(bArr5, 32, bArr2, i2);
                    for (int i7 = 0; i7 < 32; i7++) {
                        bArr5[i7] = bArr2[i2 + 32 + i7];
                    }
                }
                i2 += 64;
                int i8 = 1;
                while (i8 < 10) {
                    int i9 = i5 >>> 1;
                    if ((i9 & 1) == 0) {
                        hashFunctions.hash_2n_n_mask(bArr5, 0, bArr5, 0, bArr3, (i8 - 1) * 2 * 32);
                        for (int i10 = 0; i10 < 32; i10++) {
                            bArr5[i10 + 32] = bArr2[i2 + i10];
                        }
                    } else {
                        hashFunctions.hash_2n_n_mask(bArr5, 32, bArr5, 0, bArr3, (i8 - 1) * 2 * 32);
                        for (int i11 = 0; i11 < 32; i11++) {
                            bArr5[i11] = bArr2[i2 + i11];
                        }
                    }
                    i2 += 32;
                    i8++;
                    i5 = i9;
                }
                int i12 = i5 >>> 1;
                hashFunctions.hash_2n_n_mask(bArr5, 0, bArr5, 0, bArr3, 576);
                for (int i13 = 0; i13 < 32; i13++) {
                    if (bArr2[(i12 * 32) + i + i13] != bArr5[i13]) {
                        for (int i14 = 0; i14 < 32; i14++) {
                            bArr[i14] = 0;
                        }
                        return -1;
                    }
                }
                i3 = i4 + 1;
            } else {
                int i15 = 0;
                while (true) {
                    int i16 = i15;
                    if (i16 >= 32) {
                        break;
                    }
                    hashFunctions.hash_2n_n_mask(bArr5, i16 * 32, bArr2, i + (i16 * 2 * 32), bArr3, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE);
                    i15 = i16 + 1;
                }
                int i17 = 0;
                while (true) {
                    int i18 = i17;
                    if (i18 >= 16) {
                        break;
                    }
                    hashFunctions.hash_2n_n_mask(bArr5, i18 * 32, bArr5, i18 * 2 * 32, bArr3, 704);
                    i17 = i18 + 1;
                }
                int i19 = 0;
                while (true) {
                    int i20 = i19;
                    if (i20 >= 8) {
                        break;
                    }
                    hashFunctions.hash_2n_n_mask(bArr5, i20 * 32, bArr5, i20 * 2 * 32, bArr3, 768);
                    i19 = i20 + 1;
                }
                int i21 = 0;
                while (true) {
                    int i22 = i21;
                    if (i22 >= 4) {
                        break;
                    }
                    hashFunctions.hash_2n_n_mask(bArr5, i22 * 32, bArr5, i22 * 2 * 32, bArr3, LeicaMakernoteDirectory.TAG_IMAGE_ID_NUMBER);
                    i21 = i22 + 1;
                }
                int i23 = 0;
                while (true) {
                    int i24 = i23;
                    if (i24 < 2) {
                        hashFunctions.hash_2n_n_mask(bArr5, i24 * 32, bArr5, i24 * 2 * 32, bArr3, 896);
                        i23 = i24 + 1;
                    } else {
                        hashFunctions.hash_2n_n_mask(bArr, 0, bArr5, 0, bArr3, 960);
                        return 0;
                    }
                }
            }
        }
    }
}
