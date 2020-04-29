package org.bouncycastle.pqc.crypto.xmss;

import java.text.ParseException;
import org.bouncycastle.pqc.crypto.xmss.XMSSReducedSignature;
import org.bouncycastle.util.Pack;

public final class XMSSSignature extends XMSSReducedSignature implements XMSSStoreableObjectInterface {
    private final int index;
    private final byte[] random;

    public static class Builder extends XMSSReducedSignature.Builder {
        /* access modifiers changed from: private */
        public int index = 0;
        private final XMSSParameters params;
        /* access modifiers changed from: private */
        public byte[] random = null;

        public Builder(XMSSParameters xMSSParameters) {
            super(xMSSParameters);
            this.params = xMSSParameters;
        }

        public XMSSSignature build() throws ParseException {
            return new XMSSSignature(this);
        }

        public Builder withIndex(int i) {
            this.index = i;
            return this;
        }

        public Builder withRandom(byte[] bArr) {
            this.random = XMSSUtil.cloneArray(bArr);
            return this;
        }

        public Builder withSignature(byte[] bArr) {
            if (bArr == null) {
                throw new NullPointerException("signature == null");
            }
            int digestSize = this.params.getDigestSize();
            int height = this.params.getHeight() * digestSize;
            this.index = Pack.bigEndianToInt(bArr, 0);
            this.random = XMSSUtil.extractBytesAtOffset(bArr, 4, digestSize);
            withReducedSignature(XMSSUtil.extractBytesAtOffset(bArr, digestSize + 4, (this.params.getWOTSPlus().getParams().getLen() * digestSize) + height));
            return this;
        }
    }

    private XMSSSignature(Builder builder) throws ParseException {
        super(builder);
        this.index = builder.index;
        int digestSize = getParams().getDigestSize();
        byte[] access$100 = builder.random;
        if (access$100 == null) {
            this.random = new byte[digestSize];
        } else if (access$100.length != digestSize) {
            throw new IllegalArgumentException("size of random needs to be equal to size of digest");
        } else {
            this.random = access$100;
        }
    }

    public int getIndex() {
        return this.index;
    }

    public byte[] getRandom() {
        return XMSSUtil.cloneArray(this.random);
    }

    public byte[] toByteArray() {
        byte[][] byteArray;
        int digestSize = getParams().getDigestSize();
        byte[] bArr = new byte[((getParams().getWOTSPlus().getParams().getLen() * digestSize) + 4 + digestSize + (getParams().getHeight() * digestSize))];
        XMSSUtil.intToBytesBigEndianOffset(bArr, this.index, 0);
        XMSSUtil.copyBytesAtOffset(bArr, this.random, 4);
        int i = digestSize + 4;
        for (byte[] bArr2 : getWOTSPlusSignature().toByteArray()) {
            XMSSUtil.copyBytesAtOffset(bArr, bArr2, i);
            i += digestSize;
        }
        for (int i2 = 0; i2 < getAuthPath().size(); i2++) {
            XMSSUtil.copyBytesAtOffset(bArr, getAuthPath().get(i2).getValue(), i);
            i += digestSize;
        }
        return bArr;
    }
}
