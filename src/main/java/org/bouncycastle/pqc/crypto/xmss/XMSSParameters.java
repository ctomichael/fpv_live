package org.bouncycastle.pqc.crypto.xmss;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;

public final class XMSSParameters {
    private final int height;
    private final int k;
    private final XMSSOid oid;
    private final SecureRandom prng;
    private final WOTSPlus wotsPlus;

    public XMSSParameters(int i, Digest digest, SecureRandom secureRandom) {
        if (i < 2) {
            throw new IllegalArgumentException("height must be >= 2");
        } else if (digest == null) {
            throw new NullPointerException("digest == null");
        } else if (secureRandom == null) {
            throw new NullPointerException("prng == null");
        } else {
            this.wotsPlus = new WOTSPlus(new WOTSPlusParameters(digest));
            this.prng = secureRandom;
            this.height = i;
            this.k = determineMinK();
            this.oid = DefaultXMSSOid.lookup(getDigest().getAlgorithmName(), getDigestSize(), getWinternitzParameter(), this.wotsPlus.getParams().getLen(), i);
        }
    }

    private int determineMinK() {
        for (int i = 2; i <= this.height; i++) {
            if ((this.height - i) % 2 == 0) {
                return i;
            }
        }
        throw new IllegalStateException("should never happen...");
    }

    /* access modifiers changed from: protected */
    public Digest getDigest() {
        return this.wotsPlus.getParams().getDigest();
    }

    public int getDigestSize() {
        return this.wotsPlus.getParams().getDigestSize();
    }

    public int getHeight() {
        return this.height;
    }

    /* access modifiers changed from: protected */
    public int getK() {
        return this.k;
    }

    /* access modifiers changed from: protected */
    public SecureRandom getPRNG() {
        return this.prng;
    }

    /* access modifiers changed from: protected */
    public WOTSPlus getWOTSPlus() {
        return this.wotsPlus;
    }

    public int getWinternitzParameter() {
        return this.wotsPlus.getParams().getWinternitzParameter();
    }
}
