package org.bouncycastle.jcajce.provider.symmetric.util;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import org.bouncycastle.asn1.cms.GCMParameters;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CCMBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.CTSBlockCipher;
import org.bouncycastle.crypto.modes.EAXBlockCipher;
import org.bouncycastle.crypto.modes.GCFBBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.modes.GOFBBlockCipher;
import org.bouncycastle.crypto.modes.OCBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.OpenPGPCFBBlockCipher;
import org.bouncycastle.crypto.modes.PGPCFBBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.TBCPadding;
import org.bouncycastle.crypto.paddings.X923Padding;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.jcajce.spec.GOST28147ParameterSpec;
import org.bouncycastle.util.Strings;

public class BaseBlockCipher extends BaseWrapCipher implements PBE {
    private static final Class gcmSpecClass = lookup("javax.crypto.spec.GCMParameterSpec");
    private AEADParameters aeadParams;
    private Class[] availableSpecs = {RC2ParameterSpec.class, RC5ParameterSpec.class, gcmSpecClass, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class};
    private BlockCipher baseEngine;
    private GenericBlockCipher cipher;
    private int digest;
    private BlockCipherProvider engineProvider;
    private boolean fixedIv = true;
    private int ivLength = 0;
    private ParametersWithIV ivParam;
    private int keySizeInBits;
    private String modeName = null;
    private boolean padded;
    private String pbeAlgorithm = null;
    private PBEParameterSpec pbeSpec = null;
    private int scheme = -1;

    private static class AEADGenericBlockCipher implements GenericBlockCipher {
        private static final Constructor aeadBadTagConstructor;
        /* access modifiers changed from: private */
        public AEADBlockCipher cipher;

        static {
            Class access$100 = BaseBlockCipher.lookup("javax.crypto.AEADBadTagException");
            if (access$100 != null) {
                aeadBadTagConstructor = findExceptionConstructor(access$100);
            } else {
                aeadBadTagConstructor = null;
            }
        }

        AEADGenericBlockCipher(AEADBlockCipher aEADBlockCipher) {
            this.cipher = aEADBlockCipher;
        }

        private static Constructor findExceptionConstructor(Class cls) {
            try {
                return cls.getConstructor(String.class);
            } catch (Exception e) {
                return null;
            }
        }

        public int doFinal(byte[] bArr, int i) throws IllegalStateException, BadPaddingException {
            BadPaddingException badPaddingException;
            try {
                return this.cipher.doFinal(bArr, i);
            } catch (InvalidCipherTextException e) {
                InvalidCipherTextException invalidCipherTextException = e;
                if (aeadBadTagConstructor != null) {
                    try {
                        badPaddingException = (BadPaddingException) aeadBadTagConstructor.newInstance(invalidCipherTextException.getMessage());
                    } catch (Exception e2) {
                        badPaddingException = null;
                    }
                    if (badPaddingException != null) {
                        throw badPaddingException;
                    }
                }
                throw new BadPaddingException(invalidCipherTextException.getMessage());
            }
        }

        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }

        public int getOutputSize(int i) {
            return this.cipher.getOutputSize(i);
        }

        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }

        public int getUpdateOutputSize(int i) {
            return this.cipher.getUpdateOutputSize(i);
        }

        public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
            this.cipher.init(z, cipherParameters);
        }

        public int processByte(byte b, byte[] bArr, int i) throws DataLengthException {
            return this.cipher.processByte(b, bArr, i);
        }

        public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws DataLengthException {
            return this.cipher.processBytes(bArr, i, i2, bArr2, i3);
        }

        public void updateAAD(byte[] bArr, int i, int i2) {
            this.cipher.processAADBytes(bArr, i, i2);
        }

        public boolean wrapOnNoPadding() {
            return false;
        }
    }

    private static class BufferedGenericBlockCipher implements GenericBlockCipher {
        private BufferedBlockCipher cipher;

        BufferedGenericBlockCipher(BlockCipher blockCipher) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher);
        }

        BufferedGenericBlockCipher(BlockCipher blockCipher, BlockCipherPadding blockCipherPadding) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher, blockCipherPadding);
        }

        BufferedGenericBlockCipher(BufferedBlockCipher bufferedBlockCipher) {
            this.cipher = bufferedBlockCipher;
        }

        public int doFinal(byte[] bArr, int i) throws IllegalStateException, BadPaddingException {
            try {
                return this.cipher.doFinal(bArr, i);
            } catch (InvalidCipherTextException e) {
                throw new BadPaddingException(e.getMessage());
            }
        }

        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }

        public int getOutputSize(int i) {
            return this.cipher.getOutputSize(i);
        }

        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }

        public int getUpdateOutputSize(int i) {
            return this.cipher.getUpdateOutputSize(i);
        }

        public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
            this.cipher.init(z, cipherParameters);
        }

        public int processByte(byte b, byte[] bArr, int i) throws DataLengthException {
            return this.cipher.processByte(b, bArr, i);
        }

        public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws DataLengthException {
            return this.cipher.processBytes(bArr, i, i2, bArr2, i3);
        }

        public void updateAAD(byte[] bArr, int i, int i2) {
            throw new UnsupportedOperationException("AAD is not supported in the current mode.");
        }

        public boolean wrapOnNoPadding() {
            return !(this.cipher instanceof CTSBlockCipher);
        }
    }

    private interface GenericBlockCipher {
        int doFinal(byte[] bArr, int i) throws IllegalStateException, BadPaddingException;

        String getAlgorithmName();

        int getOutputSize(int i);

        BlockCipher getUnderlyingCipher();

        int getUpdateOutputSize(int i);

        void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException;

        int processByte(byte b, byte[] bArr, int i) throws DataLengthException;

        int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws DataLengthException;

        void updateAAD(byte[] bArr, int i, int i2);

        boolean wrapOnNoPadding();
    }

    private static class InvalidKeyOrParametersException extends InvalidKeyException {
        private final Throwable cause;

        InvalidKeyOrParametersException(String str, Throwable th) {
            super(str);
            this.cause = th;
        }

        public Throwable getCause() {
            return this.cause;
        }
    }

    protected BaseBlockCipher(BlockCipher blockCipher) {
        this.baseEngine = blockCipher;
        this.cipher = new BufferedGenericBlockCipher(blockCipher);
    }

    protected BaseBlockCipher(BlockCipher blockCipher, int i) {
        this.baseEngine = blockCipher;
        this.cipher = new BufferedGenericBlockCipher(blockCipher);
        this.ivLength = i / 8;
    }

    protected BaseBlockCipher(BlockCipher blockCipher, int i, int i2, int i3, int i4) {
        this.baseEngine = blockCipher;
        this.scheme = i;
        this.digest = i2;
        this.keySizeInBits = i3;
        this.ivLength = i4;
        this.cipher = new BufferedGenericBlockCipher(blockCipher);
    }

    protected BaseBlockCipher(BufferedBlockCipher bufferedBlockCipher, int i) {
        this.baseEngine = bufferedBlockCipher.getUnderlyingCipher();
        this.cipher = new BufferedGenericBlockCipher(bufferedBlockCipher);
        this.ivLength = i / 8;
    }

    protected BaseBlockCipher(AEADBlockCipher aEADBlockCipher) {
        this.baseEngine = aEADBlockCipher.getUnderlyingCipher();
        this.ivLength = this.baseEngine.getBlockSize();
        this.cipher = new AEADGenericBlockCipher(aEADBlockCipher);
    }

    protected BaseBlockCipher(AEADBlockCipher aEADBlockCipher, boolean z, int i) {
        this.baseEngine = aEADBlockCipher.getUnderlyingCipher();
        this.fixedIv = z;
        this.ivLength = i;
        this.cipher = new AEADGenericBlockCipher(aEADBlockCipher);
    }

    protected BaseBlockCipher(BlockCipherProvider blockCipherProvider) {
        this.baseEngine = blockCipherProvider.get();
        this.engineProvider = blockCipherProvider;
        this.cipher = new BufferedGenericBlockCipher(blockCipherProvider.get());
    }

    private CipherParameters adjustParameters(AlgorithmParameterSpec algorithmParameterSpec, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            CipherParameters parameters = ((ParametersWithIV) cipherParameters).getParameters();
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                this.ivParam = new ParametersWithIV(parameters, ((IvParameterSpec) algorithmParameterSpec).getIV());
                return this.ivParam;
            } else if (!(algorithmParameterSpec instanceof GOST28147ParameterSpec)) {
                return cipherParameters;
            } else {
                GOST28147ParameterSpec gOST28147ParameterSpec = (GOST28147ParameterSpec) algorithmParameterSpec;
                CipherParameters parametersWithSBox = new ParametersWithSBox(cipherParameters, gOST28147ParameterSpec.getSbox());
                if (!(gOST28147ParameterSpec.getIV() == null || this.ivLength == 0)) {
                    this.ivParam = new ParametersWithIV(parameters, gOST28147ParameterSpec.getIV());
                    parametersWithSBox = this.ivParam;
                }
                return parametersWithSBox;
            }
        } else if (algorithmParameterSpec instanceof IvParameterSpec) {
            this.ivParam = new ParametersWithIV(cipherParameters, ((IvParameterSpec) algorithmParameterSpec).getIV());
            return this.ivParam;
        } else if (!(algorithmParameterSpec instanceof GOST28147ParameterSpec)) {
            return cipherParameters;
        } else {
            GOST28147ParameterSpec gOST28147ParameterSpec2 = (GOST28147ParameterSpec) algorithmParameterSpec;
            ParametersWithSBox parametersWithSBox2 = new ParametersWithSBox(cipherParameters, gOST28147ParameterSpec2.getSbox());
            return (gOST28147ParameterSpec2.getIV() == null || this.ivLength == 0) ? parametersWithSBox2 : new ParametersWithIV(parametersWithSBox2, gOST28147ParameterSpec2.getIV());
        }
    }

    private boolean isAEADModeName(String str) {
        return "CCM".equals(str) || "EAX".equals(str) || "GCM".equals(str) || "OCB".equals(str);
    }

    /* access modifiers changed from: private */
    public static Class lookup(String str) {
        try {
            return BaseBlockCipher.class.getClassLoader().loadClass(str);
        } catch (Exception e) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public int engineDoFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        int i4 = 0;
        if (engineGetOutputSize(i2) + i3 > bArr2.length) {
            throw new ShortBufferException("output buffer too short for input.");
        }
        if (i2 != 0) {
            try {
                i4 = this.cipher.processBytes(bArr, i, i2, bArr2, i3);
            } catch (OutputLengthException e) {
                throw new IllegalBlockSizeException(e.getMessage());
            } catch (DataLengthException e2) {
                throw new IllegalBlockSizeException(e2.getMessage());
            }
        }
        return i4 + this.cipher.doFinal(bArr2, i3 + i4);
    }

    /* access modifiers changed from: protected */
    public byte[] engineDoFinal(byte[] bArr, int i, int i2) throws IllegalBlockSizeException, BadPaddingException {
        byte[] bArr2 = new byte[engineGetOutputSize(i2)];
        int processBytes = i2 != 0 ? this.cipher.processBytes(bArr, i, i2, bArr2, 0) : 0;
        try {
            int doFinal = this.cipher.doFinal(bArr2, processBytes) + processBytes;
            if (doFinal == bArr2.length) {
                return bArr2;
            }
            byte[] bArr3 = new byte[doFinal];
            System.arraycopy(bArr2, 0, bArr3, 0, doFinal);
            return bArr3;
        } catch (DataLengthException e) {
            throw new IllegalBlockSizeException(e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public int engineGetBlockSize() {
        return this.baseEngine.getBlockSize();
    }

    /* access modifiers changed from: protected */
    public byte[] engineGetIV() {
        if (this.aeadParams != null) {
            return this.aeadParams.getNonce();
        }
        if (this.ivParam != null) {
            return this.ivParam.getIV();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public int engineGetKeySize(Key key) {
        return key.getEncoded().length * 8;
    }

    /* access modifiers changed from: protected */
    public int engineGetOutputSize(int i) {
        return this.cipher.getOutputSize(i);
    }

    /* access modifiers changed from: protected */
    public AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null) {
            if (this.pbeSpec != null) {
                try {
                    this.engineParams = createParametersInstance(this.pbeAlgorithm);
                    this.engineParams.init(this.pbeSpec);
                } catch (Exception e) {
                    return null;
                }
            } else if (this.aeadParams != null) {
                try {
                    this.engineParams = createParametersInstance("GCM");
                    this.engineParams.init(new GCMParameters(this.aeadParams.getNonce(), this.aeadParams.getMacSize() / 8).getEncoded());
                } catch (Exception e2) {
                    throw new RuntimeException(e2.toString());
                }
            } else if (this.ivParam != null) {
                String algorithmName = this.cipher.getUnderlyingCipher().getAlgorithmName();
                if (algorithmName.indexOf(47) >= 0) {
                    algorithmName = algorithmName.substring(0, algorithmName.indexOf(47));
                }
                try {
                    this.engineParams = createParametersInstance(algorithmName);
                    this.engineParams.init(this.ivParam.getIV());
                } catch (Exception e3) {
                    throw new RuntimeException(e3.toString());
                }
            }
        }
        return this.engineParams;
    }

    /* access modifiers changed from: protected */
    public void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec algorithmParameterSpec;
        if (algorithmParameters != null) {
            int i2 = 0;
            while (true) {
                if (i2 == this.availableSpecs.length) {
                    algorithmParameterSpec = null;
                    break;
                }
                if (this.availableSpecs[i2] != null) {
                    try {
                        algorithmParameterSpec = algorithmParameters.getParameterSpec(this.availableSpecs[i2]);
                        break;
                    } catch (Exception e) {
                    }
                }
                i2++;
            }
            if (algorithmParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + algorithmParameters.toString());
            }
        } else {
            algorithmParameterSpec = null;
        }
        engineInit(i, key, algorithmParameterSpec, secureRandom);
        this.engineParams = algorithmParameters;
    }

    /* access modifiers changed from: protected */
    public void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            engineInit(i, key, (AlgorithmParameterSpec) null, secureRandom);
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidKeyException(e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0295 A[SYNTHETIC, Splitter:B:140:0x0295] */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x054e A[SYNTHETIC, Splitter:B:251:0x054e] */
    /* JADX WARNING: Removed duplicated region for block: B:258:0x0583 A[Catch:{ Exception -> 0x02b6 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void engineInit(int r11, java.security.Key r12, java.security.spec.AlgorithmParameterSpec r13, java.security.SecureRandom r14) throws java.security.InvalidKeyException, java.security.InvalidAlgorithmParameterException {
        /*
            r10 = this;
            r2 = 2
            r9 = 1
            r1 = 0
            r8 = 0
            r10.pbeSpec = r1
            r10.pbeAlgorithm = r1
            r10.engineParams = r1
            r10.aeadParams = r1
            boolean r3 = r12 instanceof javax.crypto.SecretKey
            if (r3 != 0) goto L_0x0037
            java.security.InvalidKeyException r2 = new java.security.InvalidKeyException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Key for algorithm "
            java.lang.StringBuilder r3 = r3.append(r4)
            if (r12 == 0) goto L_0x0024
            java.lang.String r1 = r12.getAlgorithm()
        L_0x0024:
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r3 = " not suitable for symmetric enryption."
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r2.<init>(r1)
            throw r2
        L_0x0037:
            if (r13 != 0) goto L_0x0051
            org.bouncycastle.crypto.BlockCipher r3 = r10.baseEngine
            java.lang.String r3 = r3.getAlgorithmName()
            java.lang.String r4 = "RC5-64"
            boolean r3 = r3.startsWith(r4)
            if (r3 == 0) goto L_0x0051
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "RC5 requires an RC5ParametersSpec to be passed in."
            r1.<init>(r2)
            throw r1
        L_0x0051:
            int r3 = r10.scheme
            if (r3 == r2) goto L_0x0059
            boolean r3 = r12 instanceof org.bouncycastle.jcajce.PKCS12Key
            if (r3 == 0) goto L_0x011d
        L_0x0059:
            r0 = r12
            javax.crypto.SecretKey r0 = (javax.crypto.SecretKey) r0     // Catch:{ Exception -> 0x0080 }
            r1 = r0
            boolean r3 = r13 instanceof javax.crypto.spec.PBEParameterSpec
            if (r3 == 0) goto L_0x0066
            r3 = r13
            javax.crypto.spec.PBEParameterSpec r3 = (javax.crypto.spec.PBEParameterSpec) r3
            r10.pbeSpec = r3
        L_0x0066:
            boolean r3 = r1 instanceof javax.crypto.interfaces.PBEKey
            if (r3 == 0) goto L_0x0099
            javax.crypto.spec.PBEParameterSpec r3 = r10.pbeSpec
            if (r3 != 0) goto L_0x0099
            r3 = r1
            javax.crypto.interfaces.PBEKey r3 = (javax.crypto.interfaces.PBEKey) r3
            byte[] r4 = r3.getSalt()
            if (r4 != 0) goto L_0x008a
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "PBEKey requires parameters to specify salt"
            r1.<init>(r2)
            throw r1
        L_0x0080:
            r1 = move-exception
            java.security.InvalidKeyException r1 = new java.security.InvalidKeyException
            java.lang.String r2 = "PKCS12 requires a SecretKey/PBEKey"
            r1.<init>(r2)
            throw r1
        L_0x008a:
            javax.crypto.spec.PBEParameterSpec r4 = new javax.crypto.spec.PBEParameterSpec
            byte[] r5 = r3.getSalt()
            int r3 = r3.getIterationCount()
            r4.<init>(r5, r3)
            r10.pbeSpec = r4
        L_0x0099:
            javax.crypto.spec.PBEParameterSpec r3 = r10.pbeSpec
            if (r3 != 0) goto L_0x00aa
            boolean r3 = r1 instanceof javax.crypto.interfaces.PBEKey
            if (r3 != 0) goto L_0x00aa
            java.security.InvalidKeyException r1 = new java.security.InvalidKeyException
            java.lang.String r2 = "Algorithm requires a PBE key"
            r1.<init>(r2)
            throw r1
        L_0x00aa:
            boolean r3 = r12 instanceof org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey
            if (r3 == 0) goto L_0x0104
            r3 = r12
            org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey r3 = (org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey) r3
            org.bouncycastle.crypto.CipherParameters r3 = r3.getParam()
            boolean r4 = r3 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r4 == 0) goto L_0x00e0
            r1 = r3
        L_0x00ba:
            r2 = r1
        L_0x00bb:
            boolean r1 = r2 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r1 == 0) goto L_0x00c4
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x00c4:
            r1 = r2
        L_0x00c5:
            boolean r2 = r13 instanceof org.bouncycastle.jcajce.spec.AEADParameterSpec
            if (r2 == 0) goto L_0x02c4
            java.lang.String r2 = r10.modeName
            boolean r2 = r10.isAEADModeName(r2)
            if (r2 != 0) goto L_0x023c
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher
            boolean r2 = r2 instanceof org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.AEADGenericBlockCipher
            if (r2 != 0) goto L_0x023c
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "AEADParameterSpec can only be used with AEAD modes."
            r1.<init>(r2)
            throw r1
        L_0x00e0:
            if (r3 != 0) goto L_0x00fb
            byte[] r1 = r1.getEncoded()
            int r3 = r10.digest
            int r4 = r10.keySizeInBits
            int r5 = r10.ivLength
            int r5 = r5 * 8
            javax.crypto.spec.PBEParameterSpec r6 = r10.pbeSpec
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r7 = r10.cipher
            java.lang.String r7 = r7.getAlgorithmName()
            org.bouncycastle.crypto.CipherParameters r1 = org.bouncycastle.jcajce.provider.symmetric.util.PBE.Util.makePBEParameters(r1, r2, r3, r4, r5, r6, r7)
            goto L_0x00ba
        L_0x00fb:
            java.security.InvalidKeyException r1 = new java.security.InvalidKeyException
            java.lang.String r2 = "Algorithm requires a PBE key suitable for PKCS12"
            r1.<init>(r2)
            throw r1
        L_0x0104:
            byte[] r1 = r1.getEncoded()
            int r3 = r10.digest
            int r4 = r10.keySizeInBits
            int r5 = r10.ivLength
            int r5 = r5 * 8
            javax.crypto.spec.PBEParameterSpec r6 = r10.pbeSpec
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r7 = r10.cipher
            java.lang.String r7 = r7.getAlgorithmName()
            org.bouncycastle.crypto.CipherParameters r2 = org.bouncycastle.jcajce.provider.symmetric.util.PBE.Util.makePBEParameters(r1, r2, r3, r4, r5, r6, r7)
            goto L_0x00bb
        L_0x011d:
            boolean r2 = r12 instanceof org.bouncycastle.jcajce.PBKDF1Key
            if (r2 == 0) goto L_0x016f
            r1 = r12
            org.bouncycastle.jcajce.PBKDF1Key r1 = (org.bouncycastle.jcajce.PBKDF1Key) r1
            boolean r2 = r13 instanceof javax.crypto.spec.PBEParameterSpec
            if (r2 == 0) goto L_0x012d
            r2 = r13
            javax.crypto.spec.PBEParameterSpec r2 = (javax.crypto.spec.PBEParameterSpec) r2
            r10.pbeSpec = r2
        L_0x012d:
            boolean r2 = r1 instanceof org.bouncycastle.jcajce.PBKDF1KeyWithParameters
            if (r2 == 0) goto L_0x014a
            javax.crypto.spec.PBEParameterSpec r2 = r10.pbeSpec
            if (r2 != 0) goto L_0x014a
            javax.crypto.spec.PBEParameterSpec r3 = new javax.crypto.spec.PBEParameterSpec
            r2 = r1
            org.bouncycastle.jcajce.PBKDF1KeyWithParameters r2 = (org.bouncycastle.jcajce.PBKDF1KeyWithParameters) r2
            byte[] r4 = r2.getSalt()
            r2 = r1
            org.bouncycastle.jcajce.PBKDF1KeyWithParameters r2 = (org.bouncycastle.jcajce.PBKDF1KeyWithParameters) r2
            int r2 = r2.getIterationCount()
            r3.<init>(r4, r2)
            r10.pbeSpec = r3
        L_0x014a:
            byte[] r1 = r1.getEncoded()
            int r3 = r10.digest
            int r4 = r10.keySizeInBits
            int r2 = r10.ivLength
            int r5 = r2 * 8
            javax.crypto.spec.PBEParameterSpec r6 = r10.pbeSpec
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher
            java.lang.String r7 = r2.getAlgorithmName()
            r2 = r8
            org.bouncycastle.crypto.CipherParameters r2 = org.bouncycastle.jcajce.provider.symmetric.util.PBE.Util.makePBEParameters(r1, r2, r3, r4, r5, r6, r7)
            boolean r1 = r2 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r1 == 0) goto L_0x016c
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x016c:
            r1 = r2
            goto L_0x00c5
        L_0x016f:
            boolean r2 = r12 instanceof org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey
            if (r2 == 0) goto L_0x01c8
            r1 = r12
            org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey r1 = (org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey) r1
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = r1.getOID()
            if (r2 == 0) goto L_0x01a0
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = r1.getOID()
            java.lang.String r2 = r2.getId()
            r10.pbeAlgorithm = r2
        L_0x0186:
            org.bouncycastle.crypto.CipherParameters r2 = r1.getParam()
            if (r2 == 0) goto L_0x01a7
            org.bouncycastle.crypto.CipherParameters r1 = r1.getParam()
            org.bouncycastle.crypto.CipherParameters r2 = r10.adjustParameters(r13, r1)
        L_0x0194:
            boolean r1 = r2 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r1 == 0) goto L_0x019d
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x019d:
            r1 = r2
            goto L_0x00c5
        L_0x01a0:
            java.lang.String r2 = r1.getAlgorithm()
            r10.pbeAlgorithm = r2
            goto L_0x0186
        L_0x01a7:
            boolean r2 = r13 instanceof javax.crypto.spec.PBEParameterSpec
            if (r2 == 0) goto L_0x01bf
            r2 = r13
            javax.crypto.spec.PBEParameterSpec r2 = (javax.crypto.spec.PBEParameterSpec) r2
            r10.pbeSpec = r2
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher
            org.bouncycastle.crypto.BlockCipher r2 = r2.getUnderlyingCipher()
            java.lang.String r2 = r2.getAlgorithmName()
            org.bouncycastle.crypto.CipherParameters r2 = org.bouncycastle.jcajce.provider.symmetric.util.PBE.Util.makePBEParameters(r1, r13, r2)
            goto L_0x0194
        L_0x01bf:
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "PBE requires PBE parameters to be set."
            r1.<init>(r2)
            throw r1
        L_0x01c8:
            boolean r2 = r12 instanceof javax.crypto.interfaces.PBEKey
            if (r2 == 0) goto L_0x0211
            r1 = r12
            javax.crypto.interfaces.PBEKey r1 = (javax.crypto.interfaces.PBEKey) r1
            r2 = r13
            javax.crypto.spec.PBEParameterSpec r2 = (javax.crypto.spec.PBEParameterSpec) r2
            r10.pbeSpec = r2
            boolean r2 = r1 instanceof org.bouncycastle.jcajce.PKCS12KeyWithParameters
            if (r2 == 0) goto L_0x01eb
            javax.crypto.spec.PBEParameterSpec r2 = r10.pbeSpec
            if (r2 != 0) goto L_0x01eb
            javax.crypto.spec.PBEParameterSpec r2 = new javax.crypto.spec.PBEParameterSpec
            byte[] r3 = r1.getSalt()
            int r4 = r1.getIterationCount()
            r2.<init>(r3, r4)
            r10.pbeSpec = r2
        L_0x01eb:
            byte[] r1 = r1.getEncoded()
            int r2 = r10.scheme
            int r3 = r10.digest
            int r4 = r10.keySizeInBits
            int r5 = r10.ivLength
            int r5 = r5 * 8
            javax.crypto.spec.PBEParameterSpec r6 = r10.pbeSpec
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r7 = r10.cipher
            java.lang.String r7 = r7.getAlgorithmName()
            org.bouncycastle.crypto.CipherParameters r2 = org.bouncycastle.jcajce.provider.symmetric.util.PBE.Util.makePBEParameters(r1, r2, r3, r4, r5, r6, r7)
            boolean r1 = r2 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r1 == 0) goto L_0x020e
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x020e:
            r1 = r2
            goto L_0x00c5
        L_0x0211:
            boolean r2 = r12 instanceof org.bouncycastle.jcajce.spec.RepeatedSecretKeySpec
            if (r2 != 0) goto L_0x00c5
            int r1 = r10.scheme
            if (r1 == 0) goto L_0x0227
            int r1 = r10.scheme
            r2 = 4
            if (r1 == r2) goto L_0x0227
            int r1 = r10.scheme
            if (r1 == r9) goto L_0x0227
            int r1 = r10.scheme
            r2 = 5
            if (r1 != r2) goto L_0x0230
        L_0x0227:
            java.security.InvalidKeyException r1 = new java.security.InvalidKeyException
            java.lang.String r2 = "Algorithm requires a PBE key"
            r1.<init>(r2)
            throw r1
        L_0x0230:
            org.bouncycastle.crypto.params.KeyParameter r2 = new org.bouncycastle.crypto.params.KeyParameter
            byte[] r1 = r12.getEncoded()
            r2.<init>(r1)
            r1 = r2
            goto L_0x00c5
        L_0x023c:
            org.bouncycastle.jcajce.spec.AEADParameterSpec r13 = (org.bouncycastle.jcajce.spec.AEADParameterSpec) r13
            boolean r2 = r1 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r2 == 0) goto L_0x02c1
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            org.bouncycastle.crypto.CipherParameters r1 = r1.getParameters()
            org.bouncycastle.crypto.params.KeyParameter r1 = (org.bouncycastle.crypto.params.KeyParameter) r1
        L_0x024a:
            org.bouncycastle.crypto.params.AEADParameters r2 = new org.bouncycastle.crypto.params.AEADParameters
            int r3 = r13.getMacSizeInBits()
            byte[] r4 = r13.getNonce()
            byte[] r5 = r13.getAssociatedData()
            r2.<init>(r1, r3, r4, r5)
            r10.aeadParams = r2
            r1 = r2
        L_0x025e:
            int r2 = r10.ivLength
            if (r2 == 0) goto L_0x058d
            boolean r2 = r1 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r2 != 0) goto L_0x058d
            boolean r2 = r1 instanceof org.bouncycastle.crypto.params.AEADParameters
            if (r2 != 0) goto L_0x058d
            if (r14 != 0) goto L_0x0590
            java.security.SecureRandom r2 = new java.security.SecureRandom
            r2.<init>()
        L_0x0271:
            if (r11 == r9) goto L_0x0276
            r3 = 3
            if (r11 != r3) goto L_0x0532
        L_0x0276:
            int r3 = r10.ivLength
            byte[] r3 = new byte[r3]
            r2.nextBytes(r3)
            org.bouncycastle.crypto.params.ParametersWithIV r2 = new org.bouncycastle.crypto.params.ParametersWithIV
            r2.<init>(r1, r3)
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x0287:
            if (r14 == 0) goto L_0x058a
            boolean r1 = r10.padded
            if (r1 == 0) goto L_0x058a
            org.bouncycastle.crypto.params.ParametersWithRandom r1 = new org.bouncycastle.crypto.params.ParametersWithRandom
            r1.<init>(r2, r14)
        L_0x0292:
            switch(r11) {
                case 1: goto L_0x054e;
                case 2: goto L_0x0583;
                case 3: goto L_0x054e;
                case 4: goto L_0x0583;
                default: goto L_0x0295;
            }
        L_0x0295:
            java.security.InvalidParameterException r1 = new java.security.InvalidParameterException     // Catch:{ Exception -> 0x02b6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02b6 }
            r2.<init>()     // Catch:{ Exception -> 0x02b6 }
            java.lang.String r3 = "unknown opmode "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02b6 }
            java.lang.StringBuilder r2 = r2.append(r11)     // Catch:{ Exception -> 0x02b6 }
            java.lang.String r3 = " passed"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02b6 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x02b6 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x02b6 }
            throw r1     // Catch:{ Exception -> 0x02b6 }
        L_0x02b6:
            r1 = move-exception
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$InvalidKeyOrParametersException r2 = new org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$InvalidKeyOrParametersException
            java.lang.String r3 = r1.getMessage()
            r2.<init>(r3, r1)
            throw r2
        L_0x02c1:
            org.bouncycastle.crypto.params.KeyParameter r1 = (org.bouncycastle.crypto.params.KeyParameter) r1
            goto L_0x024a
        L_0x02c4:
            boolean r2 = r13 instanceof javax.crypto.spec.IvParameterSpec
            if (r2 == 0) goto L_0x0341
            int r2 = r10.ivLength
            if (r2 == 0) goto L_0x0329
            javax.crypto.spec.IvParameterSpec r13 = (javax.crypto.spec.IvParameterSpec) r13
            byte[] r2 = r13.getIV()
            int r2 = r2.length
            int r3 = r10.ivLength
            if (r2 == r3) goto L_0x0304
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher
            boolean r2 = r2 instanceof org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.AEADGenericBlockCipher
            if (r2 != 0) goto L_0x0304
            boolean r2 = r10.fixedIv
            if (r2 == 0) goto L_0x0304
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "IV must be "
            java.lang.StringBuilder r2 = r2.append(r3)
            int r3 = r10.ivLength
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = " bytes long."
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0304:
            boolean r2 = r1 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r2 == 0) goto L_0x031f
            org.bouncycastle.crypto.params.ParametersWithIV r2 = new org.bouncycastle.crypto.params.ParametersWithIV
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            org.bouncycastle.crypto.CipherParameters r1 = r1.getParameters()
            byte[] r3 = r13.getIV()
            r2.<init>(r1, r3)
        L_0x0317:
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
            r1 = r2
            goto L_0x025e
        L_0x031f:
            org.bouncycastle.crypto.params.ParametersWithIV r2 = new org.bouncycastle.crypto.params.ParametersWithIV
            byte[] r3 = r13.getIV()
            r2.<init>(r1, r3)
            goto L_0x0317
        L_0x0329:
            java.lang.String r2 = r10.modeName
            if (r2 == 0) goto L_0x025e
            java.lang.String r2 = r10.modeName
            java.lang.String r3 = "ECB"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x025e
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "ECB mode does not use an IV"
            r1.<init>(r2)
            throw r1
        L_0x0341:
            boolean r2 = r13 instanceof org.bouncycastle.jcajce.spec.GOST28147ParameterSpec
            if (r2 == 0) goto L_0x038d
            r1 = r13
            org.bouncycastle.jcajce.spec.GOST28147ParameterSpec r1 = (org.bouncycastle.jcajce.spec.GOST28147ParameterSpec) r1
            org.bouncycastle.crypto.params.ParametersWithSBox r2 = new org.bouncycastle.crypto.params.ParametersWithSBox
            org.bouncycastle.crypto.params.KeyParameter r3 = new org.bouncycastle.crypto.params.KeyParameter
            byte[] r4 = r12.getEncoded()
            r3.<init>(r4)
            org.bouncycastle.jcajce.spec.GOST28147ParameterSpec r13 = (org.bouncycastle.jcajce.spec.GOST28147ParameterSpec) r13
            byte[] r4 = r13.getSbox()
            r2.<init>(r3, r4)
            byte[] r3 = r1.getIV()
            if (r3 == 0) goto L_0x037f
            int r3 = r10.ivLength
            if (r3 == 0) goto L_0x037f
            boolean r3 = r2 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r3 == 0) goto L_0x0382
            org.bouncycastle.crypto.params.ParametersWithIV r3 = new org.bouncycastle.crypto.params.ParametersWithIV
            org.bouncycastle.crypto.params.ParametersWithIV r2 = (org.bouncycastle.crypto.params.ParametersWithIV) r2
            org.bouncycastle.crypto.CipherParameters r2 = r2.getParameters()
            byte[] r1 = r1.getIV()
            r3.<init>(r2, r1)
            r2 = r3
        L_0x037a:
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x037f:
            r1 = r2
            goto L_0x025e
        L_0x0382:
            org.bouncycastle.crypto.params.ParametersWithIV r3 = new org.bouncycastle.crypto.params.ParametersWithIV
            byte[] r1 = r1.getIV()
            r3.<init>(r2, r1)
            r2 = r3
            goto L_0x037a
        L_0x038d:
            boolean r2 = r13 instanceof javax.crypto.spec.RC2ParameterSpec
            if (r2 == 0) goto L_0x03d4
            r1 = r13
            javax.crypto.spec.RC2ParameterSpec r1 = (javax.crypto.spec.RC2ParameterSpec) r1
            org.bouncycastle.crypto.params.RC2Parameters r2 = new org.bouncycastle.crypto.params.RC2Parameters
            byte[] r3 = r12.getEncoded()
            javax.crypto.spec.RC2ParameterSpec r13 = (javax.crypto.spec.RC2ParameterSpec) r13
            int r4 = r13.getEffectiveKeyBits()
            r2.<init>(r3, r4)
            byte[] r3 = r1.getIV()
            if (r3 == 0) goto L_0x03c6
            int r3 = r10.ivLength
            if (r3 == 0) goto L_0x03c6
            boolean r3 = r2 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r3 == 0) goto L_0x03c9
            org.bouncycastle.crypto.params.ParametersWithIV r3 = new org.bouncycastle.crypto.params.ParametersWithIV
            org.bouncycastle.crypto.params.ParametersWithIV r2 = (org.bouncycastle.crypto.params.ParametersWithIV) r2
            org.bouncycastle.crypto.CipherParameters r2 = r2.getParameters()
            byte[] r1 = r1.getIV()
            r3.<init>(r2, r1)
            r2 = r3
        L_0x03c1:
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x03c6:
            r1 = r2
            goto L_0x025e
        L_0x03c9:
            org.bouncycastle.crypto.params.ParametersWithIV r3 = new org.bouncycastle.crypto.params.ParametersWithIV
            byte[] r1 = r1.getIV()
            r3.<init>(r2, r1)
            r2 = r3
            goto L_0x03c1
        L_0x03d4:
            boolean r2 = r13 instanceof javax.crypto.spec.RC5ParameterSpec
            if (r2 == 0) goto L_0x04ab
            r1 = r13
            javax.crypto.spec.RC5ParameterSpec r1 = (javax.crypto.spec.RC5ParameterSpec) r1
            org.bouncycastle.crypto.params.RC5Parameters r2 = new org.bouncycastle.crypto.params.RC5Parameters
            byte[] r3 = r12.getEncoded()
            javax.crypto.spec.RC5ParameterSpec r13 = (javax.crypto.spec.RC5ParameterSpec) r13
            int r4 = r13.getRounds()
            r2.<init>(r3, r4)
            org.bouncycastle.crypto.BlockCipher r3 = r10.baseEngine
            java.lang.String r3 = r3.getAlgorithmName()
            java.lang.String r4 = "RC5"
            boolean r3 = r3.startsWith(r4)
            if (r3 == 0) goto L_0x0471
            org.bouncycastle.crypto.BlockCipher r3 = r10.baseEngine
            java.lang.String r3 = r3.getAlgorithmName()
            java.lang.String r4 = "RC5-32"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x0435
            int r3 = r1.getWordSize()
            r4 = 32
            if (r3 == r4) goto L_0x047a
            java.security.InvalidAlgorithmParameterException r2 = new java.security.InvalidAlgorithmParameterException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "RC5 already set up for a word size of 32 not "
            java.lang.StringBuilder r3 = r3.append(r4)
            int r1 = r1.getWordSize()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r3 = "."
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r2.<init>(r1)
            throw r2
        L_0x0435:
            org.bouncycastle.crypto.BlockCipher r3 = r10.baseEngine
            java.lang.String r3 = r3.getAlgorithmName()
            java.lang.String r4 = "RC5-64"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x047a
            int r3 = r1.getWordSize()
            r4 = 64
            if (r3 == r4) goto L_0x047a
            java.security.InvalidAlgorithmParameterException r2 = new java.security.InvalidAlgorithmParameterException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "RC5 already set up for a word size of 64 not "
            java.lang.StringBuilder r3 = r3.append(r4)
            int r1 = r1.getWordSize()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r3 = "."
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r2.<init>(r1)
            throw r2
        L_0x0471:
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "RC5 parameters passed to a cipher that is not RC5."
            r1.<init>(r2)
            throw r1
        L_0x047a:
            byte[] r3 = r1.getIV()
            if (r3 == 0) goto L_0x049d
            int r3 = r10.ivLength
            if (r3 == 0) goto L_0x049d
            boolean r3 = r2 instanceof org.bouncycastle.crypto.params.ParametersWithIV
            if (r3 == 0) goto L_0x04a0
            org.bouncycastle.crypto.params.ParametersWithIV r3 = new org.bouncycastle.crypto.params.ParametersWithIV
            org.bouncycastle.crypto.params.ParametersWithIV r2 = (org.bouncycastle.crypto.params.ParametersWithIV) r2
            org.bouncycastle.crypto.CipherParameters r2 = r2.getParameters()
            byte[] r1 = r1.getIV()
            r3.<init>(r2, r1)
            r2 = r3
        L_0x0498:
            r1 = r2
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1
            r10.ivParam = r1
        L_0x049d:
            r1 = r2
            goto L_0x025e
        L_0x04a0:
            org.bouncycastle.crypto.params.ParametersWithIV r3 = new org.bouncycastle.crypto.params.ParametersWithIV
            byte[] r1 = r1.getIV()
            r3.<init>(r2, r1)
            r2 = r3
            goto L_0x0498
        L_0x04ab:
            java.lang.Class r2 = org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.gcmSpecClass
            if (r2 == 0) goto L_0x0523
            java.lang.Class r2 = org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.gcmSpecClass
            boolean r2 = r2.isInstance(r13)
            if (r2 == 0) goto L_0x0523
            java.lang.String r2 = r10.modeName
            boolean r2 = r10.isAEADModeName(r2)
            if (r2 != 0) goto L_0x04ce
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher
            boolean r2 = r2 instanceof org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.AEADGenericBlockCipher
            if (r2 != 0) goto L_0x04ce
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "GCMParameterSpec can only be used with AEAD modes."
            r1.<init>(r2)
            throw r1
        L_0x04ce:
            java.lang.Class r2 = org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.gcmSpecClass     // Catch:{ Exception -> 0x0519 }
            java.lang.String r3 = "getTLen"
            r4 = 0
            java.lang.Class[] r4 = new java.lang.Class[r4]     // Catch:{ Exception -> 0x0519 }
            java.lang.reflect.Method r4 = r2.getDeclaredMethod(r3, r4)     // Catch:{ Exception -> 0x0519 }
            java.lang.Class r2 = org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.gcmSpecClass     // Catch:{ Exception -> 0x0519 }
            java.lang.String r3 = "getIV"
            r5 = 0
            java.lang.Class[] r5 = new java.lang.Class[r5]     // Catch:{ Exception -> 0x0519 }
            java.lang.reflect.Method r5 = r2.getDeclaredMethod(r3, r5)     // Catch:{ Exception -> 0x0519 }
            boolean r2 = r1 instanceof org.bouncycastle.crypto.params.ParametersWithIV     // Catch:{ Exception -> 0x0519 }
            if (r2 == 0) goto L_0x0515
            org.bouncycastle.crypto.params.ParametersWithIV r1 = (org.bouncycastle.crypto.params.ParametersWithIV) r1     // Catch:{ Exception -> 0x0519 }
            org.bouncycastle.crypto.CipherParameters r1 = r1.getParameters()     // Catch:{ Exception -> 0x0519 }
            org.bouncycastle.crypto.params.KeyParameter r1 = (org.bouncycastle.crypto.params.KeyParameter) r1     // Catch:{ Exception -> 0x0519 }
            r3 = r1
        L_0x04f3:
            org.bouncycastle.crypto.params.AEADParameters r2 = new org.bouncycastle.crypto.params.AEADParameters     // Catch:{ Exception -> 0x0519 }
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x0519 }
            java.lang.Object r1 = r4.invoke(r13, r1)     // Catch:{ Exception -> 0x0519 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Exception -> 0x0519 }
            int r4 = r1.intValue()     // Catch:{ Exception -> 0x0519 }
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x0519 }
            java.lang.Object r1 = r5.invoke(r13, r1)     // Catch:{ Exception -> 0x0519 }
            byte[] r1 = (byte[]) r1     // Catch:{ Exception -> 0x0519 }
            byte[] r1 = (byte[]) r1     // Catch:{ Exception -> 0x0519 }
            r2.<init>(r3, r4, r1)     // Catch:{ Exception -> 0x0519 }
            r10.aeadParams = r2     // Catch:{ Exception -> 0x0519 }
            r1 = r2
            goto L_0x025e
        L_0x0515:
            org.bouncycastle.crypto.params.KeyParameter r1 = (org.bouncycastle.crypto.params.KeyParameter) r1     // Catch:{ Exception -> 0x0519 }
            r3 = r1
            goto L_0x04f3
        L_0x0519:
            r1 = move-exception
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "Cannot process GCMParameterSpec."
            r1.<init>(r2)
            throw r1
        L_0x0523:
            if (r13 == 0) goto L_0x025e
            boolean r2 = r13 instanceof javax.crypto.spec.PBEParameterSpec
            if (r2 != 0) goto L_0x025e
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "unknown parameter type."
            r1.<init>(r2)
            throw r1
        L_0x0532:
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher
            org.bouncycastle.crypto.BlockCipher r2 = r2.getUnderlyingCipher()
            java.lang.String r2 = r2.getAlgorithmName()
            java.lang.String r3 = "PGPCFB"
            int r2 = r2.indexOf(r3)
            if (r2 >= 0) goto L_0x058d
            java.security.InvalidAlgorithmParameterException r1 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r2 = "no IV set when one expected"
            r1.<init>(r2)
            throw r1
        L_0x054e:
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher     // Catch:{ Exception -> 0x02b6 }
            r3 = 1
            r2.init(r3, r1)     // Catch:{ Exception -> 0x02b6 }
        L_0x0554:
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r1 = r10.cipher     // Catch:{ Exception -> 0x02b6 }
            boolean r1 = r1 instanceof org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.AEADGenericBlockCipher     // Catch:{ Exception -> 0x02b6 }
            if (r1 == 0) goto L_0x0582
            org.bouncycastle.crypto.params.AEADParameters r1 = r10.aeadParams     // Catch:{ Exception -> 0x02b6 }
            if (r1 != 0) goto L_0x0582
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r1 = r10.cipher     // Catch:{ Exception -> 0x02b6 }
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$AEADGenericBlockCipher r1 = (org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.AEADGenericBlockCipher) r1     // Catch:{ Exception -> 0x02b6 }
            org.bouncycastle.crypto.modes.AEADBlockCipher r2 = r1.cipher     // Catch:{ Exception -> 0x02b6 }
            org.bouncycastle.crypto.params.AEADParameters r3 = new org.bouncycastle.crypto.params.AEADParameters     // Catch:{ Exception -> 0x02b6 }
            org.bouncycastle.crypto.params.ParametersWithIV r1 = r10.ivParam     // Catch:{ Exception -> 0x02b6 }
            org.bouncycastle.crypto.CipherParameters r1 = r1.getParameters()     // Catch:{ Exception -> 0x02b6 }
            org.bouncycastle.crypto.params.KeyParameter r1 = (org.bouncycastle.crypto.params.KeyParameter) r1     // Catch:{ Exception -> 0x02b6 }
            byte[] r2 = r2.getMac()     // Catch:{ Exception -> 0x02b6 }
            int r2 = r2.length     // Catch:{ Exception -> 0x02b6 }
            int r2 = r2 * 8
            org.bouncycastle.crypto.params.ParametersWithIV r4 = r10.ivParam     // Catch:{ Exception -> 0x02b6 }
            byte[] r4 = r4.getIV()     // Catch:{ Exception -> 0x02b6 }
            r3.<init>(r1, r2, r4)     // Catch:{ Exception -> 0x02b6 }
            r10.aeadParams = r3     // Catch:{ Exception -> 0x02b6 }
        L_0x0582:
            return
        L_0x0583:
            org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$GenericBlockCipher r2 = r10.cipher     // Catch:{ Exception -> 0x02b6 }
            r3 = 0
            r2.init(r3, r1)     // Catch:{ Exception -> 0x02b6 }
            goto L_0x0554
        L_0x058a:
            r1 = r2
            goto L_0x0292
        L_0x058d:
            r2 = r1
            goto L_0x0287
        L_0x0590:
            r2 = r14
            goto L_0x0271
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher.engineInit(int, java.security.Key, java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void");
    }

    /* access modifiers changed from: protected */
    public void engineSetMode(String str) throws NoSuchAlgorithmException {
        this.modeName = Strings.toUpperCase(str);
        if (this.modeName.equals("ECB")) {
            this.ivLength = 0;
            this.cipher = new BufferedGenericBlockCipher(this.baseEngine);
        } else if (this.modeName.equals("CBC")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new CBCBlockCipher(this.baseEngine));
        } else if (this.modeName.startsWith("OFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, Integer.parseInt(this.modeName.substring(3))));
                return;
            }
            this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, this.baseEngine.getBlockSize() * 8));
        } else if (this.modeName.startsWith("CFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, Integer.parseInt(this.modeName.substring(3))));
                return;
            }
            this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, this.baseEngine.getBlockSize() * 8));
        } else if (this.modeName.startsWith("PGP")) {
            boolean equalsIgnoreCase = this.modeName.equalsIgnoreCase("PGPCFBwithIV");
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new PGPCFBBlockCipher(this.baseEngine, equalsIgnoreCase));
        } else if (this.modeName.equalsIgnoreCase("OpenPGPCFB")) {
            this.ivLength = 0;
            this.cipher = new BufferedGenericBlockCipher(new OpenPGPCFBBlockCipher(this.baseEngine));
        } else if (this.modeName.startsWith("SIC")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.ivLength < 16) {
                throw new IllegalArgumentException("Warning: SIC-Mode can become a twotime-pad if the blocksize of the cipher is too small. Use a cipher with a block size of at least 128 bits (e.g. AES)");
            }
            this.fixedIv = false;
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
        } else if (this.modeName.startsWith("CTR")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.fixedIv = false;
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
        } else if (this.modeName.startsWith("GOFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new GOFBBlockCipher(this.baseEngine)));
        } else if (this.modeName.startsWith("GCFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new GCFBBlockCipher(this.baseEngine)));
        } else if (this.modeName.startsWith("CTS")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(new CBCBlockCipher(this.baseEngine)));
        } else if (this.modeName.startsWith("CCM")) {
            this.ivLength = 13;
            this.cipher = new AEADGenericBlockCipher(new CCMBlockCipher(this.baseEngine));
        } else if (this.modeName.startsWith("OCB")) {
            if (this.engineProvider != null) {
                this.ivLength = 15;
                this.cipher = new AEADGenericBlockCipher(new OCBBlockCipher(this.baseEngine, this.engineProvider.get()));
                return;
            }
            throw new NoSuchAlgorithmException("can't support mode " + str);
        } else if (this.modeName.startsWith("EAX")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new AEADGenericBlockCipher(new EAXBlockCipher(this.baseEngine));
        } else if (this.modeName.startsWith("GCM")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new AEADGenericBlockCipher(new GCMBlockCipher(this.baseEngine));
        } else {
            throw new NoSuchAlgorithmException("can't support mode " + str);
        }
    }

    /* access modifiers changed from: protected */
    public void engineSetPadding(String str) throws NoSuchPaddingException {
        String upperCase = Strings.toUpperCase(str);
        if (upperCase.equals("NOPADDING")) {
            if (this.cipher.wrapOnNoPadding()) {
                this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(this.cipher.getUnderlyingCipher()));
            }
        } else if (upperCase.equals("WITHCTS")) {
            this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(this.cipher.getUnderlyingCipher()));
        } else {
            this.padded = true;
            if (isAEADModeName(this.modeName)) {
                throw new NoSuchPaddingException("Only NoPadding can be used with AEAD modes.");
            } else if (upperCase.equals("PKCS5PADDING") || upperCase.equals("PKCS7PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher());
            } else if (upperCase.equals("ZEROBYTEPADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ZeroBytePadding());
            } else if (upperCase.equals("ISO10126PADDING") || upperCase.equals("ISO10126-2PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO10126d2Padding());
            } else if (upperCase.equals("X9.23PADDING") || upperCase.equals("X923PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new X923Padding());
            } else if (upperCase.equals("ISO7816-4PADDING") || upperCase.equals("ISO9797-1PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO7816d4Padding());
            } else if (upperCase.equals("TBCPADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new TBCPadding());
            } else {
                throw new NoSuchPaddingException("Padding " + str + " unknown.");
            }
        }
    }

    /* access modifiers changed from: protected */
    public int engineUpdate(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws ShortBufferException {
        if (this.cipher.getUpdateOutputSize(i2) + i3 > bArr2.length) {
            throw new ShortBufferException("output buffer too short for input.");
        }
        try {
            return this.cipher.processBytes(bArr, i, i2, bArr2, i3);
        } catch (DataLengthException e) {
            throw new IllegalStateException(e.toString());
        }
    }

    /* access modifiers changed from: protected */
    public byte[] engineUpdate(byte[] bArr, int i, int i2) {
        int updateOutputSize = this.cipher.getUpdateOutputSize(i2);
        if (updateOutputSize > 0) {
            byte[] bArr2 = new byte[updateOutputSize];
            int processBytes = this.cipher.processBytes(bArr, i, i2, bArr2, 0);
            if (processBytes == 0) {
                return null;
            }
            if (processBytes == bArr2.length) {
                return bArr2;
            }
            byte[] bArr3 = new byte[processBytes];
            System.arraycopy(bArr2, 0, bArr3, 0, processBytes);
            return bArr3;
        }
        this.cipher.processBytes(bArr, i, i2, null, 0);
        return null;
    }

    /* access modifiers changed from: protected */
    public void engineUpdateAAD(ByteBuffer byteBuffer) {
        engineUpdateAAD(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.limit() - byteBuffer.position());
    }

    /* access modifiers changed from: protected */
    public void engineUpdateAAD(byte[] bArr, int i, int i2) {
        this.cipher.updateAAD(bArr, i, i2);
    }
}
