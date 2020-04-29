package org.bouncycastle.pqc.crypto.xmss;

import java.text.ParseException;

public final class XMSSMTPublicKeyParameters implements XMSSStoreableObjectInterface {
    private final XMSSMTParameters params;
    private final byte[] publicSeed;
    private final byte[] root;

    public static class Builder {
        /* access modifiers changed from: private */
        public final XMSSMTParameters params;
        /* access modifiers changed from: private */
        public byte[] publicKey = null;
        /* access modifiers changed from: private */
        public byte[] publicSeed = null;
        /* access modifiers changed from: private */
        public byte[] root = null;

        public Builder(XMSSMTParameters xMSSMTParameters) {
            this.params = xMSSMTParameters;
        }

        public XMSSMTPublicKeyParameters build() throws ParseException {
            return new XMSSMTPublicKeyParameters(this);
        }

        public Builder withPublicKey(byte[] bArr) {
            this.publicKey = XMSSUtil.cloneArray(bArr);
            return this;
        }

        public Builder withPublicSeed(byte[] bArr) {
            this.publicSeed = XMSSUtil.cloneArray(bArr);
            return this;
        }

        public Builder withRoot(byte[] bArr) {
            this.root = XMSSUtil.cloneArray(bArr);
            return this;
        }
    }

    private XMSSMTPublicKeyParameters(Builder builder) throws ParseException {
        this.params = builder.params;
        if (this.params == null) {
            throw new NullPointerException("params == null");
        }
        int digestSize = this.params.getDigestSize();
        byte[] access$100 = builder.publicKey;
        if (access$100 != null) {
            if (access$100.length != digestSize + digestSize) {
                throw new ParseException("public key has wrong size", 0);
            }
            this.root = XMSSUtil.extractBytesAtOffset(access$100, 0, digestSize);
            this.publicSeed = XMSSUtil.extractBytesAtOffset(access$100, 0 + digestSize, digestSize);
            return;
        }
        byte[] access$200 = builder.root;
        if (access$200 == null) {
            this.root = new byte[digestSize];
        } else if (access$200.length != digestSize) {
            throw new IllegalArgumentException("length of root must be equal to length of digest");
        } else {
            this.root = access$200;
        }
        byte[] access$300 = builder.publicSeed;
        if (access$300 == null) {
            this.publicSeed = new byte[digestSize];
        } else if (access$300.length != digestSize) {
            throw new IllegalArgumentException("length of publicSeed must be equal to length of digest");
        } else {
            this.publicSeed = access$300;
        }
    }

    public byte[] getPublicSeed() {
        return XMSSUtil.cloneArray(this.publicSeed);
    }

    public byte[] getRoot() {
        return XMSSUtil.cloneArray(this.root);
    }

    public byte[] toByteArray() {
        int digestSize = this.params.getDigestSize();
        byte[] bArr = new byte[(digestSize + digestSize)];
        XMSSUtil.copyBytesAtOffset(bArr, this.root, 0);
        XMSSUtil.copyBytesAtOffset(bArr, this.publicSeed, digestSize + 0);
        return bArr;
    }
}
