package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import org.bouncycastle.pqc.crypto.xmss.HashTreeAddress;
import org.bouncycastle.pqc.crypto.xmss.LTreeAddress;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
import org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSSignature;

public class XMSS {
    private KeyedHashFunctions khf;
    private XMSSParameters params;
    private XMSSPrivateKeyParameters privateKey;
    private SecureRandom prng;
    private XMSSPublicKeyParameters publicKey;
    private WOTSPlus wotsPlus;

    public XMSS(XMSSParameters xMSSParameters) {
        if (xMSSParameters == null) {
            throw new NullPointerException("params == null");
        }
        this.params = xMSSParameters;
        this.wotsPlus = xMSSParameters.getWOTSPlus();
        this.prng = xMSSParameters.getPRNG();
        this.khf = this.wotsPlus.getKhf();
        try {
            this.privateKey = new XMSSPrivateKeyParameters.Builder(xMSSParameters).withBDSState(new BDS(this)).build();
            this.publicKey = new XMSSPublicKeyParameters.Builder(xMSSParameters).build();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private XMSSPrivateKeyParameters generatePrivateKey() {
        int digestSize = this.params.getDigestSize();
        byte[] bArr = new byte[digestSize];
        this.prng.nextBytes(bArr);
        byte[] bArr2 = new byte[digestSize];
        this.prng.nextBytes(bArr2);
        byte[] bArr3 = new byte[digestSize];
        this.prng.nextBytes(bArr3);
        try {
            return new XMSSPrivateKeyParameters.Builder(this.params).withSecretKeySeed(bArr).withSecretKeyPRF(bArr2).withPublicSeed(bArr3).withBDSState(this.privateKey.getBDSState()).build();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            return null;
        } catch (IOException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public byte[] exportPrivateKey() {
        return this.privateKey.toByteArray();
    }

    public byte[] exportPublicKey() {
        return this.publicKey.toByteArray();
    }

    public void generateKeys() {
        this.privateKey = generatePrivateKey();
        XMSSNode initialize = getBDSState().initialize((OTSHashAddress) new OTSHashAddress.Builder().build());
        try {
            this.privateKey = new XMSSPrivateKeyParameters.Builder(this.params).withIndex(this.privateKey.getIndex()).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(initialize.getValue()).withBDSState(this.privateKey.getBDSState()).build();
            this.publicKey = new XMSSPublicKeyParameters.Builder(this.params).withRoot(initialize.getValue()).withPublicSeed(getPublicSeed()).build();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public BDS getBDSState() {
        return this.privateKey.getBDSState();
    }

    public int getIndex() {
        return this.privateKey.getIndex();
    }

    /* access modifiers changed from: protected */
    public KeyedHashFunctions getKhf() {
        return this.khf;
    }

    public XMSSParameters getParams() {
        return this.params;
    }

    public byte[] getPublicSeed() {
        return this.privateKey.getPublicSeed();
    }

    public byte[] getRoot() {
        return this.privateKey.getRoot();
    }

    /* access modifiers changed from: protected */
    public XMSSNode getRootNodeFromSignature(byte[] bArr, XMSSReducedSignature xMSSReducedSignature, OTSHashAddress oTSHashAddress) {
        HashTreeAddress hashTreeAddress;
        if (bArr.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        } else if (xMSSReducedSignature == null) {
            throw new NullPointerException("signature == null");
        } else if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        } else {
            HashTreeAddress hashTreeAddress2 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withTreeIndex(oTSHashAddress.getOTSAddress()).build();
            XMSSNode[] xMSSNodeArr = new XMSSNode[2];
            xMSSNodeArr[0] = lTree(this.wotsPlus.getPublicKeyFromSignature(bArr, xMSSReducedSignature.getWOTSPlusSignature(), oTSHashAddress), (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withLTreeAddress(oTSHashAddress.getOTSAddress()).build());
            for (int i = 0; i < this.params.getHeight(); i++) {
                HashTreeAddress hashTreeAddress3 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(i).withTreeIndex(hashTreeAddress2.getTreeIndex()).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
                if (Math.floor((double) (this.privateKey.getIndex() / (1 << i))) % 2.0d == 0.0d) {
                    hashTreeAddress = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight()).withTreeIndex(hashTreeAddress3.getTreeIndex() / 2).withKeyAndMask(hashTreeAddress3.getKeyAndMask())).build();
                    xMSSNodeArr[1] = randomizeHash(xMSSNodeArr[0], xMSSReducedSignature.getAuthPath().get(i), hashTreeAddress);
                    xMSSNodeArr[1] = new XMSSNode(xMSSNodeArr[1].getHeight() + 1, xMSSNodeArr[1].getValue());
                } else {
                    hashTreeAddress = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight()).withTreeIndex((hashTreeAddress3.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress3.getKeyAndMask())).build();
                    xMSSNodeArr[1] = randomizeHash(xMSSReducedSignature.getAuthPath().get(i), xMSSNodeArr[0], hashTreeAddress);
                    xMSSNodeArr[1] = new XMSSNode(xMSSNodeArr[1].getHeight() + 1, xMSSNodeArr[1].getValue());
                }
                hashTreeAddress2 = hashTreeAddress;
                xMSSNodeArr[0] = xMSSNodeArr[1];
            }
            return xMSSNodeArr[0];
        }
    }

    /* access modifiers changed from: protected */
    public WOTSPlus getWOTSPlus() {
        return this.wotsPlus;
    }

    /* access modifiers changed from: protected */
    public byte[] getWOTSPlusSecretKey(OTSHashAddress oTSHashAddress) {
        return this.khf.PRF(this.privateKey.getSecretKeySeed(), ((OTSHashAddress) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(oTSHashAddress.getLayerAddress())).withTreeAddress(oTSHashAddress.getTreeAddress())).withOTSAddress(oTSHashAddress.getOTSAddress()).build()).toByteArray());
    }

    public void importState(byte[] bArr, byte[] bArr2) throws ParseException, ClassNotFoundException, IOException {
        if (bArr == null) {
            throw new NullPointerException("privateKey == null");
        } else if (bArr2 == null) {
            throw new NullPointerException("publicKey == null");
        } else {
            XMSSPrivateKeyParameters build = new XMSSPrivateKeyParameters.Builder(this.params).withPrivateKey(bArr, this).build();
            XMSSPublicKeyParameters build2 = new XMSSPublicKeyParameters.Builder(this.params).withPublicKey(bArr2).build();
            if (!XMSSUtil.compareByteArray(build.getRoot(), build2.getRoot())) {
                throw new IllegalStateException("root of private key and public key do not match");
            } else if (!XMSSUtil.compareByteArray(build.getPublicSeed(), build2.getPublicSeed())) {
                throw new IllegalStateException("public seed of private key and public key do not match");
            } else {
                this.privateKey = build;
                this.publicKey = build2;
                this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], this.privateKey.getPublicSeed());
            }
        }
    }

    /* access modifiers changed from: protected */
    public XMSSNode lTree(WOTSPlusPublicKeyParameters wOTSPlusPublicKeyParameters, LTreeAddress lTreeAddress) {
        LTreeAddress lTreeAddress2;
        if (wOTSPlusPublicKeyParameters == null) {
            throw new NullPointerException("publicKey == null");
        } else if (lTreeAddress == null) {
            throw new NullPointerException("address == null");
        } else {
            int len = this.wotsPlus.getParams().getLen();
            byte[][] byteArray = wOTSPlusPublicKeyParameters.toByteArray();
            XMSSNode[] xMSSNodeArr = new XMSSNode[byteArray.length];
            for (int i = 0; i < byteArray.length; i++) {
                xMSSNodeArr[i] = new XMSSNode(0, byteArray[i]);
            }
            LTreeAddress lTreeAddress3 = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(0).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
            int i2 = len;
            while (i2 > 1) {
                int i3 = 0;
                while (true) {
                    lTreeAddress2 = lTreeAddress3;
                    if (i3 >= ((int) Math.floor((double) (i2 / 2)))) {
                        break;
                    }
                    lTreeAddress3 = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress2.getLayerAddress())).withTreeAddress(lTreeAddress2.getTreeAddress())).withLTreeAddress(lTreeAddress2.getLTreeAddress()).withTreeHeight(lTreeAddress2.getTreeHeight()).withTreeIndex(i3).withKeyAndMask(lTreeAddress2.getKeyAndMask())).build();
                    xMSSNodeArr[i3] = randomizeHash(xMSSNodeArr[i3 * 2], xMSSNodeArr[(i3 * 2) + 1], lTreeAddress3);
                    i3++;
                }
                if (i2 % 2 == 1) {
                    xMSSNodeArr[(int) Math.floor((double) (i2 / 2))] = xMSSNodeArr[i2 - 1];
                }
                int ceil = (int) Math.ceil(((double) i2) / 2.0d);
                lTreeAddress3 = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress2.getLayerAddress())).withTreeAddress(lTreeAddress2.getTreeAddress())).withLTreeAddress(lTreeAddress2.getLTreeAddress()).withTreeHeight(lTreeAddress2.getTreeHeight() + 1).withTreeIndex(lTreeAddress2.getTreeIndex()).withKeyAndMask(lTreeAddress2.getKeyAndMask())).build();
                i2 = ceil;
            }
            return xMSSNodeArr[0];
        }
    }

    /* access modifiers changed from: protected */
    public XMSSNode randomizeHash(XMSSNode xMSSNode, XMSSNode xMSSNode2, XMSSAddress xMSSAddress) {
        XMSSAddress xMSSAddress2;
        if (xMSSNode == null) {
            throw new NullPointerException("left == null");
        } else if (xMSSNode2 == null) {
            throw new NullPointerException("right == null");
        } else if (xMSSNode.getHeight() != xMSSNode2.getHeight()) {
            throw new IllegalStateException("height of both nodes must be equal");
        } else if (xMSSAddress == null) {
            throw new NullPointerException("address == null");
        } else {
            byte[] publicSeed = getPublicSeed();
            if (xMSSAddress instanceof LTreeAddress) {
                LTreeAddress lTreeAddress = (LTreeAddress) xMSSAddress;
                xMSSAddress2 = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(0)).build();
            } else if (xMSSAddress instanceof HashTreeAddress) {
                HashTreeAddress hashTreeAddress = (HashTreeAddress) xMSSAddress;
                xMSSAddress2 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(0)).build();
            } else {
                xMSSAddress2 = xMSSAddress;
            }
            byte[] PRF = this.khf.PRF(publicSeed, xMSSAddress2.toByteArray());
            if (xMSSAddress2 instanceof LTreeAddress) {
                LTreeAddress lTreeAddress2 = (LTreeAddress) xMSSAddress2;
                xMSSAddress2 = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress2.getLayerAddress())).withTreeAddress(lTreeAddress2.getTreeAddress())).withLTreeAddress(lTreeAddress2.getLTreeAddress()).withTreeHeight(lTreeAddress2.getTreeHeight()).withTreeIndex(lTreeAddress2.getTreeIndex()).withKeyAndMask(1)).build();
            } else if (xMSSAddress2 instanceof HashTreeAddress) {
                HashTreeAddress hashTreeAddress2 = (HashTreeAddress) xMSSAddress2;
                xMSSAddress2 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(hashTreeAddress2.getTreeHeight()).withTreeIndex(hashTreeAddress2.getTreeIndex()).withKeyAndMask(1)).build();
            }
            byte[] PRF2 = this.khf.PRF(publicSeed, xMSSAddress2.toByteArray());
            if (xMSSAddress2 instanceof LTreeAddress) {
                LTreeAddress lTreeAddress3 = (LTreeAddress) xMSSAddress2;
                xMSSAddress2 = (LTreeAddress) ((LTreeAddress.Builder) ((LTreeAddress.Builder) ((LTreeAddress.Builder) new LTreeAddress.Builder().withLayerAddress(lTreeAddress3.getLayerAddress())).withTreeAddress(lTreeAddress3.getTreeAddress())).withLTreeAddress(lTreeAddress3.getLTreeAddress()).withTreeHeight(lTreeAddress3.getTreeHeight()).withTreeIndex(lTreeAddress3.getTreeIndex()).withKeyAndMask(2)).build();
            } else if (xMSSAddress2 instanceof HashTreeAddress) {
                HashTreeAddress hashTreeAddress3 = (HashTreeAddress) xMSSAddress2;
                xMSSAddress2 = (HashTreeAddress) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) ((HashTreeAddress.Builder) new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight()).withTreeIndex(hashTreeAddress3.getTreeIndex()).withKeyAndMask(2)).build();
            }
            byte[] PRF3 = this.khf.PRF(publicSeed, xMSSAddress2.toByteArray());
            int digestSize = this.params.getDigestSize();
            byte[] bArr = new byte[(digestSize * 2)];
            for (int i = 0; i < digestSize; i++) {
                bArr[i] = (byte) (xMSSNode.getValue()[i] ^ PRF2[i]);
            }
            for (int i2 = 0; i2 < digestSize; i2++) {
                bArr[i2 + digestSize] = (byte) (xMSSNode2.getValue()[i2] ^ PRF3[i2]);
            }
            return new XMSSNode(xMSSNode.getHeight(), this.khf.H(PRF, bArr));
        }
    }

    /* access modifiers changed from: protected */
    public void setIndex(int i) {
        try {
            this.privateKey = new XMSSPrivateKeyParameters.Builder(this.params).withIndex(i).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.privateKey.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void setPublicSeed(byte[] bArr) {
        try {
            this.privateKey = new XMSSPrivateKeyParameters.Builder(this.params).withIndex(this.privateKey.getIndex()).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(bArr).withRoot(getRoot()).withBDSState(this.privateKey.getBDSState()).build();
            this.publicKey = new XMSSPublicKeyParameters.Builder(this.params).withRoot(getRoot()).withPublicSeed(bArr).build();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], bArr);
    }

    /* access modifiers changed from: protected */
    public void setRoot(byte[] bArr) {
        try {
            this.privateKey = new XMSSPrivateKeyParameters.Builder(this.params).withIndex(this.privateKey.getIndex()).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(getPublicSeed()).withRoot(bArr).withBDSState(this.privateKey.getBDSState()).build();
            this.publicKey = new XMSSPublicKeyParameters.Builder(this.params).withRoot(bArr).withPublicSeed(getPublicSeed()).build();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public byte[] sign(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("message == null");
        } else if (getBDSState().getAuthenticationPath().isEmpty()) {
            throw new IllegalStateException("not initialized");
        } else {
            int index = this.privateKey.getIndex();
            if (!XMSSUtil.isIndexValid(getParams().getHeight(), (long) index)) {
                throw new IllegalArgumentException("index out of bounds");
            }
            byte[] PRF = this.khf.PRF(this.privateKey.getSecretKeyPRF(), XMSSUtil.toBytesBigEndian((long) index, 32));
            XMSSSignature xMSSSignature = null;
            try {
                xMSSSignature = (XMSSSignature) new XMSSSignature.Builder(this.params).withIndex(index).withRandom(PRF).withWOTSPlusSignature(wotsSign(this.khf.HMsg(XMSSUtil.concat(PRF, this.privateKey.getRoot(), XMSSUtil.toBytesBigEndian((long) index, this.params.getDigestSize())), bArr), (OTSHashAddress) new OTSHashAddress.Builder().withOTSAddress(index).build())).withAuthPath(getBDSState().getAuthenticationPath()).build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (index < (1 << getParams().getHeight()) - 1) {
                getBDSState().nextAuthenticationPath((OTSHashAddress) new OTSHashAddress.Builder().build());
            }
            setIndex(index + 1);
            return xMSSSignature.toByteArray();
        }
    }

    public boolean verifySignature(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ParseException {
        if (bArr == null) {
            throw new NullPointerException("message == null");
        } else if (bArr2 == null) {
            throw new NullPointerException("signature == null");
        } else if (bArr3 == null) {
            throw new NullPointerException("publicKey == null");
        } else {
            XMSSSignature build = new XMSSSignature.Builder(this.params).withSignature(bArr2).build();
            XMSSPublicKeyParameters build2 = new XMSSPublicKeyParameters.Builder(this.params).withPublicKey(bArr3).build();
            int index = this.privateKey.getIndex();
            byte[] publicSeed = this.privateKey.getPublicSeed();
            int index2 = build.getIndex();
            setIndex(index2);
            setPublicSeed(build2.getPublicSeed());
            this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], getPublicSeed());
            XMSSNode rootNodeFromSignature = getRootNodeFromSignature(this.khf.HMsg(XMSSUtil.concat(build.getRandom(), build2.getRoot(), XMSSUtil.toBytesBigEndian((long) index2, this.params.getDigestSize())), bArr), build, (OTSHashAddress) new OTSHashAddress.Builder().withOTSAddress(index2).build());
            setIndex(index);
            setPublicSeed(publicSeed);
            return XMSSUtil.compareByteArray(rootNodeFromSignature.getValue(), build2.getRoot());
        }
    }

    /* access modifiers changed from: protected */
    public WOTSPlusSignature wotsSign(byte[] bArr, OTSHashAddress oTSHashAddress) {
        if (bArr.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        } else if (oTSHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        } else {
            this.wotsPlus.importKeys(getWOTSPlusSecretKey(oTSHashAddress), getPublicSeed());
            return this.wotsPlus.sign(bArr, oTSHashAddress);
        }
    }
}
