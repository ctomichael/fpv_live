package org.bouncycastle.jce.provider;

import java.security.cert.CertPathValidatorSpi;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;

public class PKIXCertPathValidatorSpi extends CertPathValidatorSpi {
    private final JcaJceHelper helper = new BCJcaJceHelper();

    /* JADX WARN: Type inference failed for: r2v26, types: [java.lang.Object], assign insn: 0x01da: INVOKE  (r2v26 ? I:java.lang.Object) = (r0v14 java.util.List<? extends java.security.cert.Certificate>), (r4v12 int) type: INTERFACE call: java.util.List.get(int):java.lang.Object */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.security.cert.CertPathValidatorResult engineValidate(java.security.cert.CertPath r28, java.security.cert.CertPathParameters r29) throws java.security.cert.CertPathValidatorException, java.security.InvalidAlgorithmParameterException {
        /*
            r27 = this;
            r0 = r29
            boolean r2 = r0 instanceof java.security.cert.PKIXParameters
            if (r2 == 0) goto L_0x0038
            org.bouncycastle.jcajce.PKIXExtendedParameters$Builder r3 = new org.bouncycastle.jcajce.PKIXExtendedParameters$Builder
            r2 = r29
            java.security.cert.PKIXParameters r2 = (java.security.cert.PKIXParameters) r2
            r3.<init>(r2)
            r0 = r29
            boolean r2 = r0 instanceof org.bouncycastle.x509.ExtendedPKIXParameters
            if (r2 == 0) goto L_0x0025
            org.bouncycastle.x509.ExtendedPKIXParameters r29 = (org.bouncycastle.x509.ExtendedPKIXParameters) r29
            boolean r2 = r29.isUseDeltasEnabled()
            r3.setUseDeltasEnabled(r2)
            int r2 = r29.getValidityModel()
            r3.setValidityModel(r2)
        L_0x0025:
            org.bouncycastle.jcajce.PKIXExtendedParameters r29 = r3.build()
        L_0x0029:
            java.util.Set r2 = r29.getTrustAnchors()
            if (r2 != 0) goto L_0x0075
            java.security.InvalidAlgorithmParameterException r2 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r3 = "trustAnchors is null, this is not allowed for certification path validation."
            r2.<init>(r3)
            throw r2
        L_0x0038:
            r0 = r29
            boolean r2 = r0 instanceof org.bouncycastle.jcajce.PKIXExtendedBuilderParameters
            if (r2 == 0) goto L_0x0045
            org.bouncycastle.jcajce.PKIXExtendedBuilderParameters r29 = (org.bouncycastle.jcajce.PKIXExtendedBuilderParameters) r29
            org.bouncycastle.jcajce.PKIXExtendedParameters r29 = r29.getBaseParameters()
            goto L_0x0029
        L_0x0045:
            r0 = r29
            boolean r2 = r0 instanceof org.bouncycastle.jcajce.PKIXExtendedParameters
            if (r2 == 0) goto L_0x004e
            org.bouncycastle.jcajce.PKIXExtendedParameters r29 = (org.bouncycastle.jcajce.PKIXExtendedParameters) r29
            goto L_0x0029
        L_0x004e:
            java.security.InvalidAlgorithmParameterException r2 = new java.security.InvalidAlgorithmParameterException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Parameters must be a "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.Class<java.security.cert.PKIXParameters> r4 = java.security.cert.PKIXParameters.class
            java.lang.String r4 = r4.getName()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = " instance."
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        L_0x0075:
            java.util.List r20 = r28.getCertificates()
            int r19 = r20.size()
            boolean r2 = r20.isEmpty()
            if (r2 == 0) goto L_0x0090
            java.security.cert.CertPathValidatorException r2 = new java.security.cert.CertPathValidatorException
            java.lang.String r3 = "Certification path is empty."
            r4 = 0
            r5 = -1
            r0 = r28
            r2.<init>(r3, r4, r0, r5)
            throw r2
        L_0x0090:
            java.util.Set r21 = r29.getInitialPolicies()
            int r2 = r20.size()     // Catch:{ AnnotatedException -> 0x00bd }
            int r2 = r2 + -1
            r0 = r20
            java.lang.Object r2 = r0.get(r2)     // Catch:{ AnnotatedException -> 0x00bd }
            java.security.cert.X509Certificate r2 = (java.security.cert.X509Certificate) r2     // Catch:{ AnnotatedException -> 0x00bd }
            java.util.Set r3 = r29.getTrustAnchors()     // Catch:{ AnnotatedException -> 0x00bd }
            java.lang.String r4 = r29.getSigProvider()     // Catch:{ AnnotatedException -> 0x00bd }
            java.security.cert.TrustAnchor r22 = org.bouncycastle.jce.provider.CertPathValidatorUtilities.findTrustAnchor(r2, r3, r4)     // Catch:{ AnnotatedException -> 0x00bd }
            if (r22 != 0) goto L_0x00d0
            java.security.cert.CertPathValidatorException r2 = new java.security.cert.CertPathValidatorException
            java.lang.String r3 = "Trust anchor for certification path not found."
            r4 = 0
            r5 = -1
            r0 = r28
            r2.<init>(r3, r4, r0, r5)
            throw r2
        L_0x00bd:
            r2 = move-exception
            java.security.cert.CertPathValidatorException r3 = new java.security.cert.CertPathValidatorException
            java.lang.String r4 = r2.getMessage()
            int r5 = r20.size()
            int r5 = r5 + -1
            r0 = r28
            r3.<init>(r4, r2, r0, r5)
            throw r3
        L_0x00d0:
            org.bouncycastle.jcajce.PKIXExtendedParameters$Builder r2 = new org.bouncycastle.jcajce.PKIXExtendedParameters$Builder
            r0 = r29
            r2.<init>(r0)
            r0 = r22
            org.bouncycastle.jcajce.PKIXExtendedParameters$Builder r2 = r2.setTrustAnchor(r0)
            org.bouncycastle.jcajce.PKIXExtendedParameters r23 = r2.build()
            int r2 = r19 + 1
            java.util.ArrayList[] r13 = new java.util.ArrayList[r2]
            r2 = 0
        L_0x00e6:
            int r3 = r13.length
            if (r2 >= r3) goto L_0x00f3
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r13[r2] = r3
            int r2 = r2 + 1
            goto L_0x00e6
        L_0x00f3:
            java.util.HashSet r5 = new java.util.HashSet
            r5.<init>()
            java.lang.String r2 = "2.5.29.32.0"
            r5.add(r2)
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = new org.bouncycastle.jce.provider.PKIXPolicyNode
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = 0
            r6 = 0
            java.util.HashSet r7 = new java.util.HashSet
            r7.<init>()
            java.lang.String r8 = "2.5.29.32.0"
            r9 = 0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)
            r3 = 0
            r3 = r13[r3]
            r3.add(r2)
            org.bouncycastle.jce.provider.PKIXNameConstraintValidator r24 = new org.bouncycastle.jce.provider.PKIXNameConstraintValidator
            r24.<init>()
            java.util.HashSet r11 = new java.util.HashSet
            r11.<init>()
            boolean r3 = r23.isExplicitPolicyRequired()
            if (r3 == 0) goto L_0x017b
            r3 = 0
            r6 = r3
        L_0x012b:
            boolean r3 = r23.isAnyPolicyInhibited()
            if (r3 == 0) goto L_0x017f
            r3 = 0
            r9 = r3
        L_0x0133:
            boolean r3 = r23.isPolicyMappingInhibited()
            if (r3 == 0) goto L_0x0183
            r3 = 0
            r10 = r3
        L_0x013b:
            java.security.cert.X509Certificate r8 = r22.getTrustedCert()
            if (r8 == 0) goto L_0x0187
            org.bouncycastle.asn1.x500.X500Name r3 = org.bouncycastle.jce.provider.PrincipalUtils.getSubjectPrincipal(r8)     // Catch:{ IllegalArgumentException -> 0x0192 }
            java.security.PublicKey r4 = r8.getPublicKey()     // Catch:{ IllegalArgumentException -> 0x0192 }
            r7 = r3
            r5 = r4
        L_0x014b:
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r3 = org.bouncycastle.jce.provider.CertPathValidatorUtilities.getAlgorithmIdentifier(r5)     // Catch:{ CertPathValidatorException -> 0x019f }
            r3.getAlgorithm()
            r3.getParameters()
            org.bouncycastle.jcajce.PKIXCertStoreSelector r3 = r23.getTargetConstraints()
            if (r3 == 0) goto L_0x01ac
            org.bouncycastle.jcajce.PKIXCertStoreSelector r4 = r23.getTargetConstraints()
            r3 = 0
            r0 = r20
            java.lang.Object r3 = r0.get(r3)
            java.security.cert.X509Certificate r3 = (java.security.cert.X509Certificate) r3
            boolean r3 = r4.match(r3)
            if (r3 != 0) goto L_0x01ac
            org.bouncycastle.jce.exception.ExtCertPathValidatorException r2 = new org.bouncycastle.jce.exception.ExtCertPathValidatorException
            java.lang.String r3 = "Target certificate in certification path does not match targetConstraints."
            r4 = 0
            r5 = 0
            r0 = r28
            r2.<init>(r3, r4, r0, r5)
            throw r2
        L_0x017b:
            int r3 = r19 + 1
            r6 = r3
            goto L_0x012b
        L_0x017f:
            int r3 = r19 + 1
            r9 = r3
            goto L_0x0133
        L_0x0183:
            int r3 = r19 + 1
            r10 = r3
            goto L_0x013b
        L_0x0187:
            org.bouncycastle.asn1.x500.X500Name r3 = org.bouncycastle.jce.provider.PrincipalUtils.getCA(r22)     // Catch:{ IllegalArgumentException -> 0x0192 }
            java.security.PublicKey r4 = r22.getCAPublicKey()     // Catch:{ IllegalArgumentException -> 0x0192 }
            r7 = r3
            r5 = r4
            goto L_0x014b
        L_0x0192:
            r2 = move-exception
            org.bouncycastle.jce.exception.ExtCertPathValidatorException r3 = new org.bouncycastle.jce.exception.ExtCertPathValidatorException
            java.lang.String r4 = "Subject of trust anchor could not be (re)encoded."
            r5 = -1
            r0 = r28
            r3.<init>(r4, r2, r0, r5)
            throw r3
        L_0x019f:
            r2 = move-exception
            org.bouncycastle.jce.exception.ExtCertPathValidatorException r3 = new org.bouncycastle.jce.exception.ExtCertPathValidatorException
            java.lang.String r4 = "Algorithm identifier of public key of trust anchor could not be read."
            r5 = -1
            r0 = r28
            r3.<init>(r4, r2, r0, r5)
            throw r3
        L_0x01ac:
            java.util.List r25 = r23.getCertPathCheckers()
            java.util.Iterator r4 = r25.iterator()
        L_0x01b4:
            boolean r3 = r4.hasNext()
            if (r3 == 0) goto L_0x01c5
            java.lang.Object r3 = r4.next()
            java.security.cert.PKIXCertPathChecker r3 = (java.security.cert.PKIXCertPathChecker) r3
            r12 = 0
            r3.init(r12)
            goto L_0x01b4
        L_0x01c5:
            r15 = 0
            int r3 = r20.size()
            int r4 = r3 + -1
            r16 = r19
            r17 = r10
            r14 = r9
            r18 = r6
            r12 = r2
        L_0x01d4:
            if (r4 < 0) goto L_0x02fe
            int r26 = r19 - r4
            r0 = r20
            java.lang.Object r2 = r0.get(r4)
            r15 = r2
            java.security.cert.X509Certificate r15 = (java.security.cert.X509Certificate) r15
            int r2 = r20.size()
            int r2 = r2 + -1
            if (r4 != r2) goto L_0x022b
            r6 = 1
        L_0x01ea:
            r0 = r27
            org.bouncycastle.jcajce.util.JcaJceHelper r9 = r0.helper
            r2 = r28
            r3 = r23
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.processCertA(r2, r3, r4, r5, r6, r7, r8, r9)
            r0 = r28
            r1 = r24
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.processCertBC(r0, r4, r1)
            r9 = r28
            r10 = r4
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.processCertD(r9, r10, r11, r12, r13, r14)
            r0 = r28
            org.bouncycastle.jce.provider.PKIXPolicyNode r3 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.processCertE(r0, r4, r2)
            r0 = r28
            r1 = r18
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.processCertF(r0, r4, r3, r1)
            r0 = r26
            r1 = r19
            if (r0 == r1) goto L_0x0390
            if (r15 == 0) goto L_0x022d
            int r2 = r15.getVersion()
            r5 = 1
            if (r2 != r5) goto L_0x022d
            java.security.cert.CertPathValidatorException r2 = new java.security.cert.CertPathValidatorException
            java.lang.String r3 = "Version 1 certificates can't be used as CA ones."
            r5 = 0
            r0 = r28
            r2.<init>(r3, r5, r0, r4)
            throw r2
        L_0x022b:
            r6 = 0
            goto L_0x01ea
        L_0x022d:
            r0 = r28
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertA(r0, r4)
            r0 = r28
            r1 = r17
            org.bouncycastle.jce.provider.PKIXPolicyNode r10 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareCertB(r0, r4, r13, r3, r1)
            r0 = r28
            r1 = r24
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertG(r0, r4, r1)
            r0 = r28
            r1 = r18
            int r2 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertH1(r0, r4, r1)
            r0 = r28
            r1 = r17
            int r3 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertH2(r0, r4, r1)
            r0 = r28
            int r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertH3(r0, r4, r14)
            r0 = r28
            int r9 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertI1(r0, r4, r2)
            r0 = r28
            int r6 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertI2(r0, r4, r3)
            r0 = r28
            int r14 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertJ(r0, r4, r5)
            r0 = r28
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertK(r0, r4)
            r0 = r28
            r1 = r16
            int r2 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertL(r0, r4, r1)
            r0 = r28
            int r3 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertM(r0, r4, r2)
            r0 = r28
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertN(r0, r4)
            java.util.Set r5 = r15.getCriticalExtensionOIDs()
            if (r5 == 0) goto L_0x02ec
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.KEY_USAGE
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.CERTIFICATE_POLICIES
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.POLICY_MAPPINGS
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.INHIBIT_ANY_POLICY
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.DELTA_CRL_INDICATOR
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.POLICY_CONSTRAINTS
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.BASIC_CONSTRAINTS
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.NAME_CONSTRAINTS
            r2.remove(r5)
        L_0x02be:
            r0 = r28
            r1 = r25
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.prepareNextCertO(r0, r4, r2, r1)
            org.bouncycastle.asn1.x500.X500Name r7 = org.bouncycastle.jce.provider.PrincipalUtils.getSubjectPrincipal(r15)
            java.util.List r2 = r28.getCertificates()     // Catch:{ CertPathValidatorException -> 0x02f2 }
            r0 = r27
            org.bouncycastle.jcajce.util.JcaJceHelper r5 = r0.helper     // Catch:{ CertPathValidatorException -> 0x02f2 }
            java.security.PublicKey r5 = org.bouncycastle.jce.provider.CertPathValidatorUtilities.getNextWorkingKey(r2, r4, r5)     // Catch:{ CertPathValidatorException -> 0x02f2 }
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r2 = org.bouncycastle.jce.provider.CertPathValidatorUtilities.getAlgorithmIdentifier(r5)
            r2.getAlgorithm()
            r2.getParameters()
            r2 = r3
            r8 = r15
        L_0x02e1:
            int r4 = r4 + -1
            r16 = r2
            r17 = r6
            r18 = r9
            r12 = r10
            goto L_0x01d4
        L_0x02ec:
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            goto L_0x02be
        L_0x02f2:
            r2 = move-exception
            java.security.cert.CertPathValidatorException r3 = new java.security.cert.CertPathValidatorException
            java.lang.String r5 = "Next working key could not be retrieved."
            r0 = r28
            r3.<init>(r5, r2, r0, r4)
            throw r3
        L_0x02fe:
            r0 = r18
            int r2 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.wrapupCertA(r0, r15)
            int r3 = r4 + 1
            r0 = r28
            int r3 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.wrapupCertB(r0, r3, r2)
            java.util.Set r5 = r15.getCriticalExtensionOIDs()
            if (r5 == 0) goto L_0x037e
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.KEY_USAGE
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.CERTIFICATE_POLICIES
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.POLICY_MAPPINGS
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.INHIBIT_ANY_POLICY
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.DELTA_CRL_INDICATOR
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.POLICY_CONSTRAINTS
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.BASIC_CONSTRAINTS
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.NAME_CONSTRAINTS
            r2.remove(r5)
            java.lang.String r5 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS
            r2.remove(r5)
            org.bouncycastle.asn1.ASN1ObjectIdentifier r5 = org.bouncycastle.asn1.x509.Extension.extendedKeyUsage
            java.lang.String r5 = r5.getId()
            r2.remove(r5)
        L_0x0357:
            int r5 = r4 + 1
            r0 = r28
            r1 = r25
            org.bouncycastle.jce.provider.RFC3280CertPathUtilities.wrapupCertF(r0, r5, r1, r2)
            int r8 = r4 + 1
            r5 = r28
            r6 = r23
            r7 = r21
            r9 = r13
            r10 = r12
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = org.bouncycastle.jce.provider.RFC3280CertPathUtilities.wrapupCertG(r5, r6, r7, r8, r9, r10, r11)
            if (r3 > 0) goto L_0x0372
            if (r2 == 0) goto L_0x0384
        L_0x0372:
            java.security.cert.PKIXCertPathValidatorResult r3 = new java.security.cert.PKIXCertPathValidatorResult
            java.security.PublicKey r4 = r15.getPublicKey()
            r0 = r22
            r3.<init>(r0, r2, r4)
            return r3
        L_0x037e:
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            goto L_0x0357
        L_0x0384:
            java.security.cert.CertPathValidatorException r2 = new java.security.cert.CertPathValidatorException
            java.lang.String r3 = "Path processing failed on policy."
            r5 = 0
            r0 = r28
            r2.<init>(r3, r5, r0, r4)
            throw r2
        L_0x0390:
            r2 = r16
            r6 = r17
            r9 = r18
            r10 = r3
            goto L_0x02e1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi.engineValidate(java.security.cert.CertPath, java.security.cert.CertPathParameters):java.security.cert.CertPathValidatorResult");
    }
}
