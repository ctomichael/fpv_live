package org.bouncycastle.pqc.crypto.xmss;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;

public final class XMSSMTParameters {
    private final int height;
    private final int layers;
    private final XMSSOid oid;
    private final XMSS xmss;

    public XMSSMTParameters(int i, int i2, Digest digest, SecureRandom secureRandom) {
        this.height = i;
        this.layers = i2;
        this.xmss = new XMSS(new XMSSParameters(xmssTreeHeight(i, i2), digest, secureRandom));
        this.oid = DefaultXMSSMTOid.lookup(getDigest().getAlgorithmName(), getDigestSize(), getWinternitzParameter(), getLen(), getHeight(), i2);
    }

    private static int xmssTreeHeight(int i, int i2) throws IllegalArgumentException {
        if (i < 2) {
            throw new IllegalArgumentException("totalHeight must be > 1");
        } else if (i % i2 != 0) {
            throw new IllegalArgumentException("layers must divide totalHeight without remainder");
        } else if (i / i2 != 1) {
            return i / i2;
        } else {
            throw new IllegalArgumentException("height / layers must be greater than 1");
        }
    }

    /* access modifiers changed from: protected */
    public Digest getDigest() {
        return this.xmss.getParams().getDigest();
    }

    public int getDigestSize() {
        return this.xmss.getParams().getDigestSize();
    }

    public int getHeight() {
        return this.height;
    }

    public int getLayers() {
        return this.layers;
    }

    /* access modifiers changed from: protected */
    public int getLen() {
        return this.xmss.getWOTSPlus().getParams().getLen();
    }

    /* access modifiers changed from: protected */
    public WOTSPlus getWOTSPlus() {
        return this.xmss.getWOTSPlus();
    }

    public int getWinternitzParameter() {
        return this.xmss.getParams().getWinternitzParameter();
    }

    /* access modifiers changed from: protected */
    public XMSS getXMSS() {
        return this.xmss;
    }
}
