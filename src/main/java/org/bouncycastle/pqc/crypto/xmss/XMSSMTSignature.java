package org.bouncycastle.pqc.crypto.xmss;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.pqc.crypto.xmss.XMSSReducedSignature;

public final class XMSSMTSignature implements XMSSStoreableObjectInterface {
    private final long index;
    private final XMSSMTParameters params;
    private final byte[] random;
    private final List<XMSSReducedSignature> reducedSignatures;

    public static class Builder {
        /* access modifiers changed from: private */
        public long index = 0;
        /* access modifiers changed from: private */
        public final XMSSMTParameters params;
        /* access modifiers changed from: private */
        public byte[] random = null;
        /* access modifiers changed from: private */
        public List<XMSSReducedSignature> reducedSignatures = null;
        /* access modifiers changed from: private */
        public byte[] signature = null;

        public Builder(XMSSMTParameters xMSSMTParameters) {
            this.params = xMSSMTParameters;
        }

        public XMSSMTSignature build() throws ParseException {
            return new XMSSMTSignature(this);
        }

        public Builder withIndex(long j) {
            this.index = j;
            return this;
        }

        public Builder withRandom(byte[] bArr) {
            this.random = XMSSUtil.cloneArray(bArr);
            return this;
        }

        public Builder withReducedSignatures(List<XMSSReducedSignature> list) {
            this.reducedSignatures = list;
            return this;
        }

        public Builder withSignature(byte[] bArr) {
            this.signature = bArr;
            return this;
        }
    }

    private XMSSMTSignature(Builder builder) throws ParseException {
        this.params = builder.params;
        if (this.params == null) {
            throw new NullPointerException("params == null");
        }
        int digestSize = this.params.getDigestSize();
        byte[] access$100 = builder.signature;
        if (access$100 != null) {
            int len = this.params.getWOTSPlus().getParams().getLen();
            int ceil = (int) Math.ceil(((double) this.params.getHeight()) / 8.0d);
            int height = (len + (this.params.getHeight() / this.params.getLayers())) * digestSize;
            if (access$100.length != (this.params.getLayers() * height) + ceil + digestSize) {
                throw new ParseException("signature has wrong size", 0);
            }
            this.index = XMSSUtil.bytesToXBigEndian(access$100, 0, ceil);
            if (!XMSSUtil.isIndexValid(this.params.getHeight(), this.index)) {
                throw new ParseException("index out of bounds", 0);
            }
            int i = ceil + 0;
            this.random = XMSSUtil.extractBytesAtOffset(access$100, i, digestSize);
            this.reducedSignatures = new ArrayList();
            for (int i2 = digestSize + i; i2 < access$100.length; i2 += height) {
                this.reducedSignatures.add(new XMSSReducedSignature.Builder(this.params.getXMSS().getParams()).withReducedSignature(XMSSUtil.extractBytesAtOffset(access$100, i2, height)).build());
            }
            return;
        }
        this.index = builder.index;
        byte[] access$300 = builder.random;
        if (access$300 == null) {
            this.random = new byte[digestSize];
        } else if (access$300.length != digestSize) {
            throw new IllegalArgumentException("size of random needs to be equal to size of digest");
        } else {
            this.random = access$300;
        }
        List<XMSSReducedSignature> access$400 = builder.reducedSignatures;
        if (access$400 != null) {
            this.reducedSignatures = access$400;
        } else {
            this.reducedSignatures = new ArrayList();
        }
    }

    public long getIndex() {
        return this.index;
    }

    public byte[] getRandom() {
        return XMSSUtil.cloneArray(this.random);
    }

    public List<XMSSReducedSignature> getReducedSignatures() {
        return this.reducedSignatures;
    }

    public byte[] toByteArray() {
        int digestSize = this.params.getDigestSize();
        int len = this.params.getWOTSPlus().getParams().getLen();
        int ceil = (int) Math.ceil(((double) this.params.getHeight()) / 8.0d);
        int height = (len + (this.params.getHeight() / this.params.getLayers())) * digestSize;
        byte[] bArr = new byte[((this.params.getLayers() * height) + ceil + digestSize)];
        XMSSUtil.copyBytesAtOffset(bArr, XMSSUtil.toBytesBigEndian(this.index, ceil), 0);
        int i = 0 + ceil;
        XMSSUtil.copyBytesAtOffset(bArr, this.random, i);
        int i2 = digestSize + i;
        Iterator<XMSSReducedSignature> it2 = this.reducedSignatures.iterator();
        while (true) {
            int i3 = i2;
            if (!it2.hasNext()) {
                return bArr;
            }
            XMSSUtil.copyBytesAtOffset(bArr, it2.next().toByteArray(), i3);
            i2 = i3 + height;
        }
    }
}
