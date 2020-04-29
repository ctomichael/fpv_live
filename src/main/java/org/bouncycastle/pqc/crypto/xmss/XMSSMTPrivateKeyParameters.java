package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

public final class XMSSMTPrivateKeyParameters implements XMSSStoreableObjectInterface {
    private final Map<Integer, BDS> bdsState;
    private final long index;
    private final XMSSMTParameters params;
    private final byte[] publicSeed;
    private final byte[] root;
    private final byte[] secretKeyPRF;
    private final byte[] secretKeySeed;

    public static class Builder {
        /* access modifiers changed from: private */
        public Map<Integer, BDS> bdsState = null;
        /* access modifiers changed from: private */
        public long index = 0;
        /* access modifiers changed from: private */
        public final XMSSMTParameters params;
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

        public Builder(XMSSMTParameters xMSSMTParameters) {
            this.params = xMSSMTParameters;
        }

        public XMSSMTPrivateKeyParameters build() throws ParseException, ClassNotFoundException, IOException {
            return new XMSSMTPrivateKeyParameters(this);
        }

        public Builder withBDSState(Map<Integer, BDS> map) {
            this.bdsState = map;
            return this;
        }

        public Builder withIndex(long j) {
            this.index = j;
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

    private XMSSMTPrivateKeyParameters(Builder builder) throws ParseException, ClassNotFoundException, IOException {
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
            Map<Integer, BDS> access$800 = builder.bdsState;
            if (access$800 != null) {
                this.bdsState = access$800;
            } else {
                this.bdsState = new TreeMap();
            }
        } else if (builder.xmss == null) {
            throw new NullPointerException("xmss == null");
        } else {
            int height = this.params.getHeight();
            int ceil = (int) Math.ceil(((double) height) / 8.0d);
            this.index = XMSSUtil.bytesToXBigEndian(access$100, 0, ceil);
            if (!XMSSUtil.isIndexValid(height, this.index)) {
                throw new ParseException("index out of bounds", 0);
            }
            int i = 0 + ceil;
            this.secretKeySeed = XMSSUtil.extractBytesAtOffset(access$100, i, digestSize);
            int i2 = i + digestSize;
            this.secretKeyPRF = XMSSUtil.extractBytesAtOffset(access$100, i2, digestSize);
            int i3 = i2 + digestSize;
            this.publicSeed = XMSSUtil.extractBytesAtOffset(access$100, i3, digestSize);
            int i4 = i3 + digestSize;
            this.root = XMSSUtil.extractBytesAtOffset(access$100, i4, digestSize);
            int i5 = digestSize + i4;
            TreeMap treeMap = (TreeMap) XMSSUtil.deserialize(XMSSUtil.extractBytesAtOffset(access$100, i5, access$100.length - i5));
            for (Integer num : treeMap.keySet()) {
                BDS bds = (BDS) treeMap.get(num);
                bds.setXMSS(builder.xmss);
                bds.validate();
            }
            this.bdsState = treeMap;
        }
    }

    public Map<Integer, BDS> getBDSState() {
        return this.bdsState;
    }

    public long getIndex() {
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
        int ceil = (int) Math.ceil(((double) this.params.getHeight()) / 8.0d);
        byte[] bArr = new byte[(ceil + digestSize + digestSize + digestSize + digestSize)];
        XMSSUtil.copyBytesAtOffset(bArr, XMSSUtil.toBytesBigEndian(this.index, ceil), 0);
        int i = ceil + 0;
        XMSSUtil.copyBytesAtOffset(bArr, this.secretKeySeed, i);
        int i2 = i + digestSize;
        XMSSUtil.copyBytesAtOffset(bArr, this.secretKeyPRF, i2);
        int i3 = i2 + digestSize;
        XMSSUtil.copyBytesAtOffset(bArr, this.publicSeed, i3);
        XMSSUtil.copyBytesAtOffset(bArr, this.root, digestSize + i3);
        try {
            return XMSSUtil.concat(bArr, XMSSUtil.serialize(this.bdsState));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error serializing bds state");
        }
    }
}
