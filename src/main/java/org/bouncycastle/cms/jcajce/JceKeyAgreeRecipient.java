package org.bouncycastle.cms.jcajce;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cms.ecc.ECCCMSSharedInfo;
import org.bouncycastle.asn1.cms.ecc.MQVuserKeyingMaterial;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyAgreeRecipient;
import org.bouncycastle.jcajce.spec.MQVParameterSpec;
import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;
import org.bouncycastle.operator.DefaultSecretKeySizeProvider;
import org.bouncycastle.operator.SecretKeySizeProvider;
import org.bouncycastle.util.Pack;

public abstract class JceKeyAgreeRecipient implements KeyAgreeRecipient {
    private static KeyMaterialGenerator ecc_cms_Generator = new RFC5753KeyMaterialGenerator();
    private static KeyMaterialGenerator old_ecc_cms_Generator = new KeyMaterialGenerator() {
        /* class org.bouncycastle.cms.jcajce.JceKeyAgreeRecipient.AnonymousClass1 */

        public byte[] generateKDFMaterial(AlgorithmIdentifier algorithmIdentifier, int i, byte[] bArr) {
            try {
                return new ECCCMSSharedInfo(new AlgorithmIdentifier(algorithmIdentifier.getAlgorithm(), DERNull.INSTANCE), bArr, Pack.intToBigEndian(i)).getEncoded(ASN1Encoding.DER);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to create KDF material: " + e);
            }
        }
    };
    private static final Set possibleOldMessages = new HashSet();
    protected EnvelopedDataHelper contentHelper = this.helper;
    protected EnvelopedDataHelper helper = new EnvelopedDataHelper(new DefaultJcaJceExtHelper());
    private SecretKeySizeProvider keySizeProvider = new DefaultSecretKeySizeProvider();
    private PrivateKey recipientKey;

    static {
        possibleOldMessages.add(X9ObjectIdentifiers.dhSinglePass_stdDH_sha1kdf_scheme);
        possibleOldMessages.add(X9ObjectIdentifiers.mqvSinglePass_sha1kdf_scheme);
    }

    public JceKeyAgreeRecipient(PrivateKey privateKey) {
        this.recipientKey = privateKey;
    }

    private SecretKey calculateAgreedWrapKey(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2, PublicKey publicKey, ASN1OctetString aSN1OctetString, PrivateKey privateKey, KeyMaterialGenerator keyMaterialGenerator) throws CMSException, GeneralSecurityException, IOException {
        UserKeyingMaterialSpec userKeyingMaterialSpec = null;
        byte[] bArr = null;
        if (CMSUtils.isMQV(algorithmIdentifier.getAlgorithm())) {
            MQVuserKeyingMaterial instance = MQVuserKeyingMaterial.getInstance(aSN1OctetString.getOctets());
            PublicKey generatePublic = this.helper.createKeyFactory(algorithmIdentifier.getAlgorithm()).generatePublic(new X509EncodedKeySpec(new SubjectPublicKeyInfo(getPrivateKeyAlgorithmIdentifier(), instance.getEphemeralPublicKey().getPublicKey().getBytes()).getEncoded()));
            KeyAgreement createKeyAgreement = this.helper.createKeyAgreement(algorithmIdentifier.getAlgorithm());
            if (instance.getAddedukm() != null) {
                bArr = instance.getAddedukm().getOctets();
            }
            if (keyMaterialGenerator == old_ecc_cms_Generator) {
                bArr = old_ecc_cms_Generator.generateKDFMaterial(algorithmIdentifier2, this.keySizeProvider.getKeySize(algorithmIdentifier2), bArr);
            }
            createKeyAgreement.init(privateKey, new MQVParameterSpec(privateKey, generatePublic, bArr));
            createKeyAgreement.doPhase(publicKey, true);
            return createKeyAgreement.generateSecret(algorithmIdentifier2.getAlgorithm().getId());
        }
        KeyAgreement createKeyAgreement2 = this.helper.createKeyAgreement(algorithmIdentifier.getAlgorithm());
        if (CMSUtils.isEC(algorithmIdentifier.getAlgorithm())) {
            userKeyingMaterialSpec = aSN1OctetString != null ? new UserKeyingMaterialSpec(keyMaterialGenerator.generateKDFMaterial(algorithmIdentifier2, this.keySizeProvider.getKeySize(algorithmIdentifier2), aSN1OctetString.getOctets())) : new UserKeyingMaterialSpec(keyMaterialGenerator.generateKDFMaterial(algorithmIdentifier2, this.keySizeProvider.getKeySize(algorithmIdentifier2), null));
        } else if (!CMSUtils.isRFC2631(algorithmIdentifier.getAlgorithm())) {
            throw new CMSException("Unknown key agreement algorithm: " + algorithmIdentifier.getAlgorithm());
        } else if (aSN1OctetString != null) {
            userKeyingMaterialSpec = new UserKeyingMaterialSpec(aSN1OctetString.getOctets());
        }
        createKeyAgreement2.init(privateKey, userKeyingMaterialSpec);
        createKeyAgreement2.doPhase(publicKey, true);
        return createKeyAgreement2.generateSecret(algorithmIdentifier2.getAlgorithm().getId());
    }

    private Key unwrapSessionKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, SecretKey secretKey, ASN1ObjectIdentifier aSN1ObjectIdentifier2, byte[] bArr) throws CMSException, InvalidKeyException, NoSuchAlgorithmException {
        Cipher createCipher = this.helper.createCipher(aSN1ObjectIdentifier);
        createCipher.init(4, secretKey);
        return createCipher.unwrap(bArr, this.helper.getBaseCipherName(aSN1ObjectIdentifier2), 3);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0061, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x006a, code lost:
        throw new org.bouncycastle.cms.CMSException("can't find algorithm.", r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0075, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x007e, code lost:
        throw new org.bouncycastle.cms.CMSException("originator key spec invalid.", r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x007f, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0088, code lost:
        throw new org.bouncycastle.cms.CMSException("required padding not supported.", r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0089, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0092, code lost:
        throw new org.bouncycastle.cms.CMSException("originator key invalid.", r0);
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0061 A[ExcHandler: NoSuchAlgorithmException (r0v4 'e' java.security.NoSuchAlgorithmException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0075 A[ExcHandler: InvalidKeySpecException (r0v2 'e' java.security.spec.InvalidKeySpecException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x007f A[ExcHandler: NoSuchPaddingException (r0v1 'e' javax.crypto.NoSuchPaddingException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0089 A[ExcHandler: Exception (r0v0 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.security.Key extractSecretKey(org.bouncycastle.asn1.x509.AlgorithmIdentifier r8, org.bouncycastle.asn1.x509.AlgorithmIdentifier r9, org.bouncycastle.asn1.x509.SubjectPublicKeyInfo r10, org.bouncycastle.asn1.ASN1OctetString r11, byte[] r12) throws org.bouncycastle.cms.CMSException {
        /*
            r7 = this;
            org.bouncycastle.asn1.ASN1Encodable r0 = r8.getParameters()     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r2 = org.bouncycastle.asn1.x509.AlgorithmIdentifier.getInstance(r0)     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            java.security.spec.X509EncodedKeySpec r0 = new java.security.spec.X509EncodedKeySpec     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            byte[] r1 = r10.getEncoded()     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            r0.<init>(r1)     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.cms.jcajce.EnvelopedDataHelper r1 = r7.helper     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r3 = r10.getAlgorithm()     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r3 = r3.getAlgorithm()     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            java.security.KeyFactory r1 = r1.createKeyFactory(r3)     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            java.security.PublicKey r3 = r1.generatePublic(r0)     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            java.security.PrivateKey r5 = r7.recipientKey     // Catch:{ InvalidKeyException -> 0x003b, NoSuchAlgorithmException -> 0x0061, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.cms.jcajce.KeyMaterialGenerator r6 = org.bouncycastle.cms.jcajce.JceKeyAgreeRecipient.ecc_cms_Generator     // Catch:{ InvalidKeyException -> 0x003b, NoSuchAlgorithmException -> 0x0061, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            r0 = r7
            r1 = r8
            r4 = r11
            javax.crypto.SecretKey r0 = r0.calculateAgreedWrapKey(r1, r2, r3, r4, r5, r6)     // Catch:{ InvalidKeyException -> 0x003b, NoSuchAlgorithmException -> 0x0061, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r1 = r2.getAlgorithm()     // Catch:{ InvalidKeyException -> 0x003b, NoSuchAlgorithmException -> 0x0061, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r4 = r9.getAlgorithm()     // Catch:{ InvalidKeyException -> 0x003b, NoSuchAlgorithmException -> 0x0061, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            java.security.Key r0 = r7.unwrapSessionKey(r1, r0, r4, r12)     // Catch:{ InvalidKeyException -> 0x003b, NoSuchAlgorithmException -> 0x0061, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
        L_0x003a:
            return r0
        L_0x003b:
            r0 = move-exception
            java.util.Set r1 = org.bouncycastle.cms.jcajce.JceKeyAgreeRecipient.possibleOldMessages     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r4 = r8.getAlgorithm()     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            boolean r1 = r1.contains(r4)     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            if (r1 == 0) goto L_0x0060
            java.security.PrivateKey r5 = r7.recipientKey     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.cms.jcajce.KeyMaterialGenerator r6 = org.bouncycastle.cms.jcajce.JceKeyAgreeRecipient.old_ecc_cms_Generator     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            r0 = r7
            r1 = r8
            r4 = r11
            javax.crypto.SecretKey r0 = r0.calculateAgreedWrapKey(r1, r2, r3, r4, r5, r6)     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r1 = r2.getAlgorithm()     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = r9.getAlgorithm()     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            java.security.Key r0 = r7.unwrapSessionKey(r1, r0, r2, r12)     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
            goto L_0x003a
        L_0x0060:
            throw r0     // Catch:{ NoSuchAlgorithmException -> 0x0061, InvalidKeyException -> 0x006b, InvalidKeySpecException -> 0x0075, NoSuchPaddingException -> 0x007f, Exception -> 0x0089 }
        L_0x0061:
            r0 = move-exception
            org.bouncycastle.cms.CMSException r1 = new org.bouncycastle.cms.CMSException
            java.lang.String r2 = "can't find algorithm."
            r1.<init>(r2, r0)
            throw r1
        L_0x006b:
            r0 = move-exception
            org.bouncycastle.cms.CMSException r1 = new org.bouncycastle.cms.CMSException
            java.lang.String r2 = "key invalid in message."
            r1.<init>(r2, r0)
            throw r1
        L_0x0075:
            r0 = move-exception
            org.bouncycastle.cms.CMSException r1 = new org.bouncycastle.cms.CMSException
            java.lang.String r2 = "originator key spec invalid."
            r1.<init>(r2, r0)
            throw r1
        L_0x007f:
            r0 = move-exception
            org.bouncycastle.cms.CMSException r1 = new org.bouncycastle.cms.CMSException
            java.lang.String r2 = "required padding not supported."
            r1.<init>(r2, r0)
            throw r1
        L_0x0089:
            r0 = move-exception
            org.bouncycastle.cms.CMSException r1 = new org.bouncycastle.cms.CMSException
            java.lang.String r2 = "originator key invalid."
            r1.<init>(r2, r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.cms.jcajce.JceKeyAgreeRecipient.extractSecretKey(org.bouncycastle.asn1.x509.AlgorithmIdentifier, org.bouncycastle.asn1.x509.AlgorithmIdentifier, org.bouncycastle.asn1.x509.SubjectPublicKeyInfo, org.bouncycastle.asn1.ASN1OctetString, byte[]):java.security.Key");
    }

    public AlgorithmIdentifier getPrivateKeyAlgorithmIdentifier() {
        return PrivateKeyInfo.getInstance(this.recipientKey.getEncoded()).getPrivateKeyAlgorithm();
    }

    public JceKeyAgreeRecipient setContentProvider(String str) {
        this.contentHelper = CMSUtils.createContentHelper(str);
        return this;
    }

    public JceKeyAgreeRecipient setContentProvider(Provider provider) {
        this.contentHelper = CMSUtils.createContentHelper(provider);
        return this;
    }

    public JceKeyAgreeRecipient setProvider(String str) {
        this.helper = new EnvelopedDataHelper(new NamedJcaJceExtHelper(str));
        this.contentHelper = this.helper;
        return this;
    }

    public JceKeyAgreeRecipient setProvider(Provider provider) {
        this.helper = new EnvelopedDataHelper(new ProviderJcaJceExtHelper(provider));
        this.contentHelper = this.helper;
        return this;
    }
}
