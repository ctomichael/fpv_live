package org.bouncycastle.operator.jcajce;

import java.security.PrivateKey;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.util.DefaultJcaJceHelper;
import org.bouncycastle.jcajce.util.NamedJcaJceHelper;
import org.bouncycastle.jcajce.util.ProviderJcaJceHelper;
import org.bouncycastle.operator.AsymmetricKeyUnwrapper;

public class JceAsymmetricKeyUnwrapper extends AsymmetricKeyUnwrapper {
    private Map extraMappings = new HashMap();
    private OperatorHelper helper = new OperatorHelper(new DefaultJcaJceHelper());
    private PrivateKey privKey;
    private boolean unwrappedKeyMustBeEncodable;

    public JceAsymmetricKeyUnwrapper(AlgorithmIdentifier algorithmIdentifier, PrivateKey privateKey) {
        super(algorithmIdentifier);
        this.privKey = privateKey;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0042  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.bouncycastle.operator.GenericKey generateUnwrappedKey(org.bouncycastle.asn1.x509.AlgorithmIdentifier r6, byte[] r7) throws org.bouncycastle.operator.OperatorException {
        /*
            r5 = this;
            r1 = 0
            org.bouncycastle.operator.jcajce.OperatorHelper r0 = r5.helper     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r2 = r5.getAlgorithmIdentifier()     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = r2.getAlgorithm()     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            java.util.Map r3 = r5.extraMappings     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            javax.crypto.Cipher r2 = r0.createAsymmetricWrapper(r2, r3)     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            org.bouncycastle.operator.jcajce.OperatorHelper r0 = r5.helper     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r3 = r5.getAlgorithmIdentifier()     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            java.security.AlgorithmParameters r0 = r0.createAlgorithmParameters(r3)     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            if (r0 == 0) goto L_0x005f
            r3 = 4
            java.security.PrivateKey r4 = r5.privKey     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
            r2.init(r3, r4, r0)     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
        L_0x0023:
            org.bouncycastle.operator.jcajce.OperatorHelper r0 = r5.helper     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r3 = r6.getAlgorithm()     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
            java.lang.String r0 = r0.getKeyAlgorithmName(r3)     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
            r3 = 3
            java.security.Key r0 = r2.unwrap(r7, r0, r3)     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
            boolean r3 = r5.unwrappedKeyMustBeEncodable     // Catch:{ GeneralSecurityException -> 0x00db, IllegalStateException -> 0x00d8, UnsupportedOperationException -> 0x00d5, ProviderException -> 0x00d2 }
            if (r3 == 0) goto L_0x0040
            byte[] r3 = r0.getEncoded()     // Catch:{ Exception -> 0x0069 }
            if (r3 == 0) goto L_0x003f
            int r3 = r3.length     // Catch:{ Exception -> 0x0069 }
            if (r3 != 0) goto L_0x0040
        L_0x003f:
            r0 = r1
        L_0x0040:
            if (r0 != 0) goto L_0x0059
            r0 = 2
            java.security.PrivateKey r1 = r5.privKey     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            r2.init(r0, r1)     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            javax.crypto.spec.SecretKeySpec r0 = new javax.crypto.spec.SecretKeySpec     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            byte[] r1 = r2.doFinal(r7)     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = r6.getAlgorithm()     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            java.lang.String r2 = r2.getId()     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            r0.<init>(r1, r2)     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
        L_0x0059:
            org.bouncycastle.operator.jcajce.JceGenericKey r1 = new org.bouncycastle.operator.jcajce.JceGenericKey     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            r1.<init>(r6, r0)     // Catch:{ InvalidKeyException -> 0x0075, IllegalBlockSizeException -> 0x0094, BadPaddingException -> 0x00b3 }
            return r1
        L_0x005f:
            r0 = 4
            java.security.PrivateKey r3 = r5.privKey     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
            r2.init(r0, r3)     // Catch:{ GeneralSecurityException -> 0x0066, IllegalStateException -> 0x006c, UnsupportedOperationException -> 0x006f, ProviderException -> 0x0072 }
            goto L_0x0023
        L_0x0066:
            r0 = move-exception
        L_0x0067:
            r0 = r1
            goto L_0x0040
        L_0x0069:
            r0 = move-exception
            r0 = r1
            goto L_0x0040
        L_0x006c:
            r0 = move-exception
        L_0x006d:
            r0 = r1
            goto L_0x0040
        L_0x006f:
            r0 = move-exception
        L_0x0070:
            r0 = r1
            goto L_0x0040
        L_0x0072:
            r0 = move-exception
        L_0x0073:
            r0 = r1
            goto L_0x0040
        L_0x0075:
            r0 = move-exception
            org.bouncycastle.operator.OperatorException r1 = new org.bouncycastle.operator.OperatorException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "key invalid: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r0.getMessage()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x0094:
            r0 = move-exception
            org.bouncycastle.operator.OperatorException r1 = new org.bouncycastle.operator.OperatorException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "illegal blocksize: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r0.getMessage()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x00b3:
            r0 = move-exception
            org.bouncycastle.operator.OperatorException r1 = new org.bouncycastle.operator.OperatorException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "bad padding: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r0.getMessage()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x00d2:
            r1 = move-exception
            r1 = r0
            goto L_0x0073
        L_0x00d5:
            r1 = move-exception
            r1 = r0
            goto L_0x0070
        L_0x00d8:
            r1 = move-exception
            r1 = r0
            goto L_0x006d
        L_0x00db:
            r1 = move-exception
            r1 = r0
            goto L_0x0067
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.operator.jcajce.JceAsymmetricKeyUnwrapper.generateUnwrappedKey(org.bouncycastle.asn1.x509.AlgorithmIdentifier, byte[]):org.bouncycastle.operator.GenericKey");
    }

    public JceAsymmetricKeyUnwrapper setAlgorithmMapping(ASN1ObjectIdentifier aSN1ObjectIdentifier, String str) {
        this.extraMappings.put(aSN1ObjectIdentifier, str);
        return this;
    }

    public JceAsymmetricKeyUnwrapper setMustProduceEncodableUnwrappedKey(boolean z) {
        this.unwrappedKeyMustBeEncodable = z;
        return this;
    }

    public JceAsymmetricKeyUnwrapper setProvider(String str) {
        this.helper = new OperatorHelper(new NamedJcaJceHelper(str));
        return this;
    }

    public JceAsymmetricKeyUnwrapper setProvider(Provider provider) {
        this.helper = new OperatorHelper(new ProviderJcaJceHelper(provider));
        return this;
    }
}
