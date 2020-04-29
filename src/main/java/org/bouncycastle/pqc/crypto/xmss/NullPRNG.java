package org.bouncycastle.pqc.crypto.xmss;

import java.security.SecureRandom;

public final class NullPRNG extends SecureRandom {
    private static final long serialVersionUID = 1;

    public void nextBytes(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = 0;
        }
    }
}
