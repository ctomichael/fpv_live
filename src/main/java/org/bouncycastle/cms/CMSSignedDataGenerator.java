package org.bouncycastle.cms;

import java.util.ArrayList;
import java.util.List;

public class CMSSignedDataGenerator extends CMSSignedGenerator {
    private List signerInfs = new ArrayList();

    public CMSSignedData generate(CMSTypedData cMSTypedData) throws CMSException {
        return generate(cMSTypedData, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00d4  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0109  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x010b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.bouncycastle.cms.CMSSignedData generate(org.bouncycastle.cms.CMSTypedData r10, boolean r11) throws org.bouncycastle.cms.CMSException {
        /*
            r9 = this;
            r1 = 0
            java.util.List r0 = r9.signerInfs
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0012
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "this method can only be used with SignerInfoGenerator"
            r0.<init>(r1)
            throw r0
        L_0x0012:
            org.bouncycastle.asn1.ASN1EncodableVector r6 = new org.bouncycastle.asn1.ASN1EncodableVector
            r6.<init>()
            org.bouncycastle.asn1.ASN1EncodableVector r7 = new org.bouncycastle.asn1.ASN1EncodableVector
            r7.<init>()
            java.util.Map r0 = r9.digests
            r0.clear()
            java.util.List r0 = r9._signers
            java.util.Iterator r2 = r0.iterator()
        L_0x0027:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x0048
            java.lang.Object r0 = r2.next()
            org.bouncycastle.cms.SignerInformation r0 = (org.bouncycastle.cms.SignerInformation) r0
            org.bouncycastle.cms.CMSSignedHelper r3 = org.bouncycastle.cms.CMSSignedHelper.INSTANCE
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r4 = r0.getDigestAlgorithmID()
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r3 = r3.fixAlgID(r4)
            r6.add(r3)
            org.bouncycastle.asn1.cms.SignerInfo r0 = r0.toASN1Structure()
            r7.add(r0)
            goto L_0x0027
        L_0x0048:
            org.bouncycastle.asn1.ASN1ObjectIdentifier r8 = r10.getContentType()
            java.lang.Object r0 = r10.getContent()
            if (r0 == 0) goto L_0x010d
            if (r11 == 0) goto L_0x0110
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
        L_0x0059:
            java.util.List r2 = r9.signerGens
            java.io.OutputStream r2 = org.bouncycastle.cms.CMSUtils.attachSignersToOutputStream(r2, r0)
            java.io.OutputStream r2 = org.bouncycastle.cms.CMSUtils.getSafeOutputStream(r2)
            r10.write(r2)     // Catch:{ IOException -> 0x00ad }
            r2.close()     // Catch:{ IOException -> 0x00ad }
            if (r11 == 0) goto L_0x010d
            org.bouncycastle.asn1.BEROctetString r2 = new org.bouncycastle.asn1.BEROctetString
            byte[] r0 = r0.toByteArray()
            r2.<init>(r0)
            r5 = r2
        L_0x0075:
            java.util.List r0 = r9.signerGens
            java.util.Iterator r2 = r0.iterator()
        L_0x007b:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x00cc
            java.lang.Object r0 = r2.next()
            org.bouncycastle.cms.SignerInfoGenerator r0 = (org.bouncycastle.cms.SignerInfoGenerator) r0
            org.bouncycastle.asn1.cms.SignerInfo r3 = r0.generate(r8)
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r4 = r3.getDigestAlgorithm()
            r6.add(r4)
            r7.add(r3)
            byte[] r0 = r0.getCalculatedDigest()
            if (r0 == 0) goto L_0x007b
            java.util.Map r4 = r9.digests
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r3 = r3.getDigestAlgorithm()
            org.bouncycastle.asn1.ASN1ObjectIdentifier r3 = r3.getAlgorithm()
            java.lang.String r3 = r3.getId()
            r4.put(r3, r0)
            goto L_0x007b
        L_0x00ad:
            r0 = move-exception
            org.bouncycastle.cms.CMSException r1 = new org.bouncycastle.cms.CMSException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "data processing exception: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r0.getMessage()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x00cc:
            java.util.List r0 = r9.certs
            int r0 = r0.size()
            if (r0 == 0) goto L_0x010b
            java.util.List r0 = r9.certs
            org.bouncycastle.asn1.ASN1Set r3 = org.bouncycastle.cms.CMSUtils.createBerSetFromList(r0)
        L_0x00da:
            java.util.List r0 = r9.crls
            int r0 = r0.size()
            if (r0 == 0) goto L_0x0109
            java.util.List r0 = r9.crls
            org.bouncycastle.asn1.ASN1Set r4 = org.bouncycastle.cms.CMSUtils.createBerSetFromList(r0)
        L_0x00e8:
            org.bouncycastle.asn1.cms.ContentInfo r2 = new org.bouncycastle.asn1.cms.ContentInfo
            r2.<init>(r8, r5)
            org.bouncycastle.asn1.cms.SignedData r0 = new org.bouncycastle.asn1.cms.SignedData
            org.bouncycastle.asn1.DERSet r1 = new org.bouncycastle.asn1.DERSet
            r1.<init>(r6)
            org.bouncycastle.asn1.DERSet r5 = new org.bouncycastle.asn1.DERSet
            r5.<init>(r7)
            r0.<init>(r1, r2, r3, r4, r5)
            org.bouncycastle.asn1.cms.ContentInfo r1 = new org.bouncycastle.asn1.cms.ContentInfo
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = org.bouncycastle.asn1.cms.CMSObjectIdentifiers.signedData
            r1.<init>(r2, r0)
            org.bouncycastle.cms.CMSSignedData r0 = new org.bouncycastle.cms.CMSSignedData
            r0.<init>(r10, r1)
            return r0
        L_0x0109:
            r4 = r1
            goto L_0x00e8
        L_0x010b:
            r3 = r1
            goto L_0x00da
        L_0x010d:
            r5 = r1
            goto L_0x0075
        L_0x0110:
            r0 = r1
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.cms.CMSSignedDataGenerator.generate(org.bouncycastle.cms.CMSTypedData, boolean):org.bouncycastle.cms.CMSSignedData");
    }

    public SignerInformationStore generateCounterSigners(SignerInformation signerInformation) throws CMSException {
        return generate(new CMSProcessableByteArray(null, signerInformation.getSignature()), false).getSignerInfos();
    }
}
