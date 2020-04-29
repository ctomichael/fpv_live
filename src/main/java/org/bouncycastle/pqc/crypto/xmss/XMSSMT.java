package org.bouncycastle.pqc.crypto.xmss;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Map;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTPublicKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTSignature;
import org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSReducedSignature;

public final class XMSSMT {
    private KeyedHashFunctions khf;
    private XMSSMTParameters params;
    private XMSSMTPrivateKeyParameters privateKey;
    private SecureRandom prng;
    private XMSSMTPublicKeyParameters publicKey;
    private XMSS xmss;

    public XMSSMT(XMSSMTParameters xMSSMTParameters) {
        if (xMSSMTParameters == null) {
            throw new NullPointerException("params == null");
        }
        this.params = xMSSMTParameters;
        this.xmss = xMSSMTParameters.getXMSS();
        this.prng = xMSSMTParameters.getXMSS().getParams().getPRNG();
        this.khf = this.xmss.getKhf();
        try {
            this.privateKey = new XMSSMTPrivateKeyParameters.Builder(xMSSMTParameters).build();
            this.publicKey = new XMSSMTPublicKeyParameters.Builder(xMSSMTParameters).build();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private XMSSMTPrivateKeyParameters generatePrivateKey() {
        int digestSize = this.params.getDigestSize();
        byte[] bArr = new byte[digestSize];
        this.prng.nextBytes(bArr);
        byte[] bArr2 = new byte[digestSize];
        this.prng.nextBytes(bArr2);
        byte[] bArr3 = new byte[digestSize];
        this.prng.nextBytes(bArr3);
        try {
            return new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(bArr).withSecretKeyPRF(bArr2).withPublicSeed(bArr3).withBDSState(this.privateKey.getBDSState()).build();
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
        XMSSPrivateKeyParameters xMSSPrivateKeyParameters;
        XMSSPublicKeyParameters xMSSPublicKeyParameters = null;
        this.privateKey = generatePrivateKey();
        try {
            xMSSPrivateKeyParameters = new XMSSPrivateKeyParameters.Builder(this.xmss.getParams()).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withBDSState(new BDS(this.xmss)).build();
            try {
                xMSSPublicKeyParameters = new XMSSPublicKeyParameters.Builder(this.xmss.getParams()).withPublicSeed(getPublicSeed()).build();
            } catch (ParseException e) {
                e = e;
            } catch (ClassNotFoundException e2) {
                e = e2;
                e.printStackTrace();
                this.xmss.importState(xMSSPrivateKeyParameters.toByteArray(), xMSSPublicKeyParameters.toByteArray());
                int layers = this.params.getLayers() - 1;
                BDS bds = new BDS(this.xmss);
                XMSSNode initialize = bds.initialize((OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(layers)).build());
                getBDSState().put(Integer.valueOf(layers), bds);
                this.xmss.setRoot(initialize.getValue());
                this.privateKey = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.xmss.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
                this.publicKey = new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(initialize.getValue()).withPublicSeed(getPublicSeed()).build();
            } catch (IOException e3) {
                e = e3;
                e.printStackTrace();
                this.xmss.importState(xMSSPrivateKeyParameters.toByteArray(), xMSSPublicKeyParameters.toByteArray());
                int layers2 = this.params.getLayers() - 1;
                BDS bds2 = new BDS(this.xmss);
                XMSSNode initialize2 = bds2.initialize((OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(layers2)).build());
                getBDSState().put(Integer.valueOf(layers2), bds2);
                this.xmss.setRoot(initialize2.getValue());
                this.privateKey = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.xmss.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
                this.publicKey = new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(initialize2.getValue()).withPublicSeed(getPublicSeed()).build();
            }
        } catch (ParseException e4) {
            e = e4;
            xMSSPrivateKeyParameters = null;
        } catch (ClassNotFoundException e5) {
            e = e5;
            xMSSPrivateKeyParameters = null;
            e.printStackTrace();
            this.xmss.importState(xMSSPrivateKeyParameters.toByteArray(), xMSSPublicKeyParameters.toByteArray());
            int layers22 = this.params.getLayers() - 1;
            BDS bds22 = new BDS(this.xmss);
            XMSSNode initialize22 = bds22.initialize((OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(layers22)).build());
            getBDSState().put(Integer.valueOf(layers22), bds22);
            this.xmss.setRoot(initialize22.getValue());
            this.privateKey = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.xmss.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
            this.publicKey = new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(initialize22.getValue()).withPublicSeed(getPublicSeed()).build();
        } catch (IOException e6) {
            e = e6;
            xMSSPrivateKeyParameters = null;
            e.printStackTrace();
            this.xmss.importState(xMSSPrivateKeyParameters.toByteArray(), xMSSPublicKeyParameters.toByteArray());
            int layers222 = this.params.getLayers() - 1;
            BDS bds222 = new BDS(this.xmss);
            XMSSNode initialize222 = bds222.initialize((OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(layers222)).build());
            getBDSState().put(Integer.valueOf(layers222), bds222);
            this.xmss.setRoot(initialize222.getValue());
            this.privateKey = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.xmss.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
            this.publicKey = new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(initialize222.getValue()).withPublicSeed(getPublicSeed()).build();
        }
        try {
            this.xmss.importState(xMSSPrivateKeyParameters.toByteArray(), xMSSPublicKeyParameters.toByteArray());
        } catch (ParseException e7) {
            e7.printStackTrace();
        } catch (ClassNotFoundException e8) {
            e8.printStackTrace();
        } catch (IOException e9) {
            e9.printStackTrace();
        }
        int layers2222 = this.params.getLayers() - 1;
        BDS bds2222 = new BDS(this.xmss);
        XMSSNode initialize2222 = bds2222.initialize((OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(layers2222)).build());
        getBDSState().put(Integer.valueOf(layers2222), bds2222);
        this.xmss.setRoot(initialize2222.getValue());
        try {
            this.privateKey = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.xmss.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
            this.publicKey = new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(initialize2222.getValue()).withPublicSeed(getPublicSeed()).build();
        } catch (ParseException e10) {
            e10.printStackTrace();
            return;
        } catch (ClassNotFoundException e11) {
            e11.printStackTrace();
            return;
        } catch (IOException e12) {
            e12.printStackTrace();
            return;
        }
        e.printStackTrace();
        this.xmss.importState(xMSSPrivateKeyParameters.toByteArray(), xMSSPublicKeyParameters.toByteArray());
        int layers22222 = this.params.getLayers() - 1;
        BDS bds22222 = new BDS(this.xmss);
        XMSSNode initialize22222 = bds22222.initialize((OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(layers22222)).build());
        getBDSState().put(Integer.valueOf(layers22222), bds22222);
        this.xmss.setRoot(initialize22222.getValue());
        this.privateKey = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.xmss.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
        this.publicKey = new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(initialize22222.getValue()).withPublicSeed(getPublicSeed()).build();
    }

    /* access modifiers changed from: protected */
    public Map<Integer, BDS> getBDSState() {
        return this.privateKey.getBDSState();
    }

    public long getIndex() {
        return this.privateKey.getIndex();
    }

    public XMSSMTParameters getParams() {
        return this.params;
    }

    public byte[] getPublicSeed() {
        return this.privateKey.getPublicSeed();
    }

    /* access modifiers changed from: protected */
    public XMSS getXMSS() {
        return this.xmss;
    }

    public void importState(byte[] bArr, byte[] bArr2) throws ParseException, ClassNotFoundException, IOException {
        if (bArr == null) {
            throw new NullPointerException("privateKey == null");
        } else if (bArr2 == null) {
            throw new NullPointerException("publicKey == null");
        } else {
            XMSSMTPrivateKeyParameters build = new XMSSMTPrivateKeyParameters.Builder(this.params).withPrivateKey(bArr, this.xmss).build();
            XMSSMTPublicKeyParameters build2 = new XMSSMTPublicKeyParameters.Builder(this.params).withPublicKey(bArr2).build();
            if (!XMSSUtil.compareByteArray(build.getRoot(), build2.getRoot())) {
                throw new IllegalStateException("root of private key and public key do not match");
            } else if (!XMSSUtil.compareByteArray(build.getPublicSeed(), build2.getPublicSeed())) {
                throw new IllegalStateException("public seed of private key and public key do not match");
            } else {
                this.xmss.importState(new XMSSPrivateKeyParameters.Builder(this.xmss.getParams()).withSecretKeySeed(build.getSecretKeySeed()).withSecretKeyPRF(build.getSecretKeyPRF()).withPublicSeed(build.getPublicSeed()).withRoot(build.getRoot()).withBDSState(new BDS(this.xmss)).build().toByteArray(), new XMSSPublicKeyParameters.Builder(this.xmss.getParams()).withRoot(build.getRoot()).withPublicSeed(getPublicSeed()).build().toByteArray());
                this.privateKey = build;
                this.publicKey = build2;
            }
        }
    }

    public byte[] sign(byte[] bArr) {
        XMSSMTSignature xMSSMTSignature;
        if (bArr == null) {
            throw new NullPointerException("message == null");
        } else if (getBDSState().isEmpty()) {
            throw new IllegalStateException("not initialized");
        } else {
            long index = getIndex();
            int height = this.params.getHeight();
            int height2 = this.xmss.getParams().getHeight();
            if (!XMSSUtil.isIndexValid(height, index)) {
                throw new IllegalArgumentException("index out of bounds");
            }
            byte[] PRF = this.khf.PRF(this.privateKey.getSecretKeyPRF(), XMSSUtil.toBytesBigEndian(index, 32));
            byte[] HMsg = this.khf.HMsg(XMSSUtil.concat(PRF, this.privateKey.getRoot(), XMSSUtil.toBytesBigEndian(index, this.params.getDigestSize())), bArr);
            try {
                xMSSMTSignature = new XMSSMTSignature.Builder(this.params).withIndex(index).withRandom(PRF).build();
            } catch (ParseException e) {
                e.printStackTrace();
                xMSSMTSignature = null;
            }
            long treeIndex = XMSSUtil.getTreeIndex(index, height2);
            int leafIndex = XMSSUtil.getLeafIndex(index, height2);
            this.xmss.setIndex(leafIndex);
            this.xmss.setPublicSeed(getPublicSeed());
            OTSHashAddress oTSHashAddress = (OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withTreeAddress(treeIndex)).withOTSAddress(leafIndex).build();
            WOTSPlusSignature wotsSign = this.xmss.wotsSign(HMsg, oTSHashAddress);
            if (getBDSState().get(0) == null || leafIndex == 0) {
                getBDSState().put(0, new BDS(this.xmss));
                getBDSState().get(0).initialize(oTSHashAddress);
            }
            XMSSReducedSignature xMSSReducedSignature = null;
            try {
                xMSSReducedSignature = new XMSSReducedSignature.Builder(this.xmss.getParams()).withWOTSPlusSignature(wotsSign).withAuthPath(getBDSState().get(0).getAuthenticationPath()).build();
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            xMSSMTSignature.getReducedSignatures().add(xMSSReducedSignature);
            if (leafIndex < (1 << height2) - 1) {
                getBDSState().get(0).nextAuthenticationPath(oTSHashAddress);
            }
            int i = 1;
            while (true) {
                int i2 = i;
                if (i2 < this.params.getLayers()) {
                    XMSSNode root = getBDSState().get(Integer.valueOf(i2 - 1)).getRoot();
                    int leafIndex2 = XMSSUtil.getLeafIndex(treeIndex, height2);
                    treeIndex = XMSSUtil.getTreeIndex(treeIndex, height2);
                    this.xmss.setIndex(leafIndex2);
                    OTSHashAddress oTSHashAddress2 = (OTSHashAddress) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(i2)).withTreeAddress(treeIndex)).withOTSAddress(leafIndex2).build();
                    WOTSPlusSignature wotsSign2 = this.xmss.wotsSign(root.getValue(), oTSHashAddress2);
                    if (getBDSState().get(Integer.valueOf(i2)) == null || XMSSUtil.isNewBDSInitNeeded(index, height2, i2)) {
                        getBDSState().put(Integer.valueOf(i2), new BDS(this.xmss));
                        getBDSState().get(Integer.valueOf(i2)).initialize(oTSHashAddress2);
                    }
                    try {
                        xMSSReducedSignature = new XMSSReducedSignature.Builder(this.xmss.getParams()).withWOTSPlusSignature(wotsSign2).withAuthPath(getBDSState().get(Integer.valueOf(i2)).getAuthenticationPath()).build();
                    } catch (ParseException e3) {
                        e3.printStackTrace();
                    }
                    xMSSMTSignature.getReducedSignatures().add(xMSSReducedSignature);
                    if (leafIndex2 < (1 << height2) - 1 && XMSSUtil.isNewAuthenticationPathNeeded(index, height2, i2)) {
                        getBDSState().get(Integer.valueOf(i2)).nextAuthenticationPath(oTSHashAddress2);
                    }
                    i = i2 + 1;
                } else {
                    try {
                        break;
                    } catch (ParseException e4) {
                        e4.printStackTrace();
                    } catch (ClassNotFoundException e5) {
                        e5.printStackTrace();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
            }
            this.privateKey = new XMSSMTPrivateKeyParameters.Builder(this.params).withIndex(1 + index).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.privateKey.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
            return xMSSMTSignature.toByteArray();
        }
    }

    public boolean verifySignature(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ParseException {
        int i = 1;
        if (bArr == null) {
            throw new NullPointerException("message == null");
        } else if (bArr2 == null) {
            throw new NullPointerException("signature == null");
        } else if (bArr3 == null) {
            throw new NullPointerException("publicKey == null");
        } else {
            XMSSMTSignature build = new XMSSMTSignature.Builder(this.params).withSignature(bArr2).build();
            XMSSMTPublicKeyParameters build2 = new XMSSMTPublicKeyParameters.Builder(this.params).withPublicKey(bArr3).build();
            byte[] HMsg = this.khf.HMsg(XMSSUtil.concat(build.getRandom(), build2.getRoot(), XMSSUtil.toBytesBigEndian(build.getIndex(), this.params.getDigestSize())), bArr);
            long index = build.getIndex();
            int height = this.xmss.getParams().getHeight();
            long treeIndex = XMSSUtil.getTreeIndex(index, height);
            int leafIndex = XMSSUtil.getLeafIndex(index, height);
            this.xmss.setIndex(leafIndex);
            this.xmss.setPublicSeed(build2.getPublicSeed());
            XMSSReducedSignature xMSSReducedSignature = build.getReducedSignatures().get(0);
            XMSSNode rootNodeFromSignature = this.xmss.getRootNodeFromSignature(HMsg, xMSSReducedSignature, (OTSHashAddress) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withTreeAddress(treeIndex)).withOTSAddress(leafIndex).build());
            while (i < this.params.getLayers()) {
                int leafIndex2 = XMSSUtil.getLeafIndex(treeIndex, height);
                treeIndex = XMSSUtil.getTreeIndex(treeIndex, height);
                this.xmss.setIndex(leafIndex2);
                XMSSNode rootNodeFromSignature2 = this.xmss.getRootNodeFromSignature(rootNodeFromSignature.getValue(), build.getReducedSignatures().get(i), (OTSHashAddress) ((OTSHashAddress.Builder) ((OTSHashAddress.Builder) new OTSHashAddress.Builder().withLayerAddress(i)).withTreeAddress(treeIndex)).withOTSAddress(leafIndex2).build());
                i++;
                rootNodeFromSignature = rootNodeFromSignature2;
            }
            return XMSSUtil.compareByteArray(rootNodeFromSignature.getValue(), build2.getRoot());
        }
    }
}
