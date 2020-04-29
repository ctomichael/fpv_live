package org.bouncycastle.crypto.prng.drbg;

import dji.component.accountcenter.IMemberProtocol;
import java.util.Hashtable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2KeyGenParameterSpec;
import org.bouncycastle.util.Integers;

class Utils {
    static final Hashtable maxSecurityStrengths = new Hashtable();

    static {
        maxSecurityStrengths.put(McElieceCCA2KeyGenParameterSpec.SHA1, Integers.valueOf(128));
        maxSecurityStrengths.put(McElieceCCA2KeyGenParameterSpec.SHA224, Integers.valueOf(192));
        maxSecurityStrengths.put(McElieceCCA2KeyGenParameterSpec.SHA256, Integers.valueOf(256));
        maxSecurityStrengths.put(McElieceCCA2KeyGenParameterSpec.SHA384, Integers.valueOf(256));
        maxSecurityStrengths.put(McElieceCCA2KeyGenParameterSpec.SHA512, Integers.valueOf(256));
        maxSecurityStrengths.put("SHA-512/224", Integers.valueOf(192));
        maxSecurityStrengths.put("SHA-512/256", Integers.valueOf(256));
    }

    Utils() {
    }

    static int getMaxSecurityStrength(Digest digest) {
        return ((Integer) maxSecurityStrengths.get(digest.getAlgorithmName())).intValue();
    }

    static int getMaxSecurityStrength(Mac mac) {
        String algorithmName = mac.getAlgorithmName();
        return ((Integer) maxSecurityStrengths.get(algorithmName.substring(0, algorithmName.indexOf(IMemberProtocol.PARAM_SEPERATOR)))).intValue();
    }

    static byte[] hash_df(Digest digest, byte[] bArr, int i) {
        byte b = 0;
        byte[] bArr2 = new byte[((i + 7) / 8)];
        int length = bArr2.length / digest.getDigestSize();
        byte[] bArr3 = new byte[digest.getDigestSize()];
        int i2 = 1;
        for (int i3 = 0; i3 <= length; i3++) {
            digest.update((byte) i2);
            digest.update((byte) (i >> 24));
            digest.update((byte) (i >> 16));
            digest.update((byte) (i >> 8));
            digest.update((byte) i);
            digest.update(bArr, 0, bArr.length);
            digest.doFinal(bArr3, 0);
            System.arraycopy(bArr3, 0, bArr2, bArr3.length * i3, bArr2.length - (bArr3.length * i3) > bArr3.length ? bArr3.length : bArr2.length - (bArr3.length * i3));
            i2++;
        }
        if (i % 8 != 0) {
            int i4 = 8 - (i % 8);
            int i5 = 0;
            while (true) {
                byte b2 = b;
                if (i5 == bArr2.length) {
                    break;
                }
                b = bArr2[i5] & 255;
                bArr2[i5] = (byte) ((b2 << (8 - i4)) | (b >>> i4));
                i5++;
            }
        }
        return bArr2;
    }

    static boolean isTooLarge(byte[] bArr, int i) {
        return bArr != null && bArr.length > i;
    }
}
