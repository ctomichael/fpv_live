package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import java.text.ParseException;
import org.bouncycastle.util.Pack;

public final class XMSSPrivateKeyParameters implements XMSSStoreableObjectInterface {
    private final BDS bdsState;
    private final int index;
    private final XMSSParameters params;
    private final byte[] publicSeed;
    private final byte[] root;
    private final byte[] secretKeyPRF;
    private final byte[] secretKeySeed;

    public static class Builder {
        /* access modifiers changed from: private */
        public BDS bdsState = null;
        /* access modifiers changed from: private */
        public int index = 0;
        /* access modifiers changed from: private */
        public final XMSSParameters params;
        /* access modifiers changed from: private */
        public byte[] privateKey = null;
        /* access modifiers changed from: private */
        public byte[] publicSeed = null;
        /* access modifiers changed from: private */
        public byte[] root = null;
        /* access modifiers changed from: private */
        public byte[] secretKeyPRF = null;
        /* access modifiers changed from: private */
        public byte[] secretKeySeed = null;
        /* access modifiers changed from: private */
        public XMSS xmss = null;

        public Builder(XMSSParameters xMSSParameters) {
            this.params = xMSSParameters;
        }

        public XMSSPrivateKeyParameters build() throws ParseException, ClassNotFoundException, IOException {
            return new XMSSPrivateKeyParameters(this);
        }

        public Builder withBDSState(BDS bds) {
            this.bdsState = bds;
            return this;
        }

        public Builder withIndex(int i) {
            this.index = i;
            return this;
        }

        public Builder withPrivateKey(byte[] bArr, XMSS xmss2) {
            this.privateKey = XMSSUtil.cloneArray(bArr);
            this.xmss = xmss2;
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

        public Builder withSecretKeyPRF(byte[] bArr) {
            this.secretKeyPRF = XMSSUtil.cloneArray(bArr);
            return this;
        }

        public Builder withSecretKeySeed(byte[] bArr) {
            this.secretKeySeed = XMSSUtil.cloneArray(bArr);
            return this;
        }
    }

    private XMSSPrivateKeyParameters(Builder builder) throws ParseException, ClassNotFoundException, IOException {
        this.params = builder.params;
        if (this.params == null) {
            throw new NullPointerException("params == null");
        }
        int digestSize = this.params.getDigestSize();
        byte[] access$100 = builder.privateKey;
        if (access$100 == null) {
            this.index = builder.index;
            byte[] access$400 = builder.secretKeySeed;
            if (access$400 == null) {
                this.secretKeySeed = new byte[digestSize];
            } else if (access$400.length != digestSize) {
                throw new IllegalArgumentException("size of secretKeySeed needs to be equal size of digest");
            } else {
                this.secretKeySeed = access$400;
            }
            byte[] access$500 = builder.secretKeyPRF;
            if (access$500 == null) {
                this.secretKeyPRF = new byte[digestSize];
            } else if (access$500.length != digestSize) {
                throw new IllegalArgumentException("size of secretKeyPRF needs to be equal size of digest");
            } else {
                this.secretKeyPRF = access$500;
            }
            byte[] access$600 = builder.publicSeed;
            if (access$600 == null) {
                this.publicSeed = new byte[digestSize];
            } else if (access$600.length != digestSize) {
                throw new IllegalArgumentException("size of publicSeed needs to be equal size of digest");
            } else {
                this.publicSeed = access$600;
            }
            byte[] access$700 = builder.root;
            if (access$700 == null) {
                this.root = new byte[digestSize];
            } else if (access$700.length != digestSize) {
                throw new IllegalArgumentException("size of root needs to be equal size of digest");
            } else {
                this.root = access$700;
            }
            BDS access$800 = builder.bdsState;
            if (access$800 != null) {
                this.bdsState = access$800;
            } else {
                this.bdsState = new BDS(new XMSS(this.params));
            }
        } else if (builder.xmss == null) {
            throw new NullPointerException("xmss == null");
        } else {
            int height = this.params.getHeight();
            this.index = Pack.bigEndianToInt(access$100, 0);
            if (!XMSSUtil.isIndexValid(height, (long) this.index)) {
                throw new ParseException("index out of bounds", 0);
            }
            this.secretKeySeed = XMSSUtil.extractBytesAtOffset(access$100, 4, digestSize);
            int i = digestSize + 4;
            this.secretKeyPRF = XMSSUtil.extractBytesAtOffset(access$100, i, digestSize);
            int i2 = i + digestSize;
            this.publicSeed = XMSSUtil.extractBytesAtOffset(access$100, i2, digestSize);
            int i3 = i2 + digestSize;
            this.root = XMSSUtil.extractBytesAtOffset(access$100, i3, digestSize);
            int i4 = digestSize + i3;
            BDS bds = (BDS) XMSSUtil.deserialize(XMSSUtil.extractBytesAtOffset(access$100, i4, access$100.length - i4));
            bds.setXMSS(builder.xmss);
            bds.validate();
            this.bdsState = bds;
        }
    }

    public BDS getBDSState() {
        return this.bdsState;
    }

    public int getIndex() {
        return this.index;
    }

    public byte[] getPublicSeed() {
        return XMSSUtil.cloneArray(this.publicSeed);
    }

    public byte[] getRoot() {
        return XMSSUtil.cloneArray(this.root);
    }

    public byte[] getSecretKeyPRF() {
        return XMSSUtil.cloneArray(this.secretKeyPRF);
    }

    public byte[] getSecretKeySeed() {
        return XMSSUtil.cloneArray(this.secretKeySeed);
    }

    public byte[] toByteArray() {
        int digestSize = this.params.getDigestSize();
        byte[] bArr = new byte[(4 + digestSize + digestSize + digestSize + digestSize)];
        XMSSUtil.intToBytesBigEndianOffset(bArr, this.index, 0);
        XMSSUtil.copyBytesAtOffset(bArr, this.secretKeySeed, 4);
        int i = digestSize + 4;
        XMSSUtil.copyBytesAtOffset(bArr, this.secretKeyPRF, i);
        int i2 = i + digestSize;
        XMSSUtil.copyBytesAtOffset(bArr, this.publicSeed, i2);
        XMSSUtil.copyBytesAtOffset(bArr, this.root, digestSize + i2);
        try {
            return XMSSUtil.concat(bArr, XMSSUtil.serialize(this.bdsState));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error serializing bds state");
        }
    }
}
