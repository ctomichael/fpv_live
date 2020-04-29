package org.bouncycastle.x509;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.qualified.MonetaryValue;
import org.bouncycastle.asn1.x509.qualified.QCStatement;
import org.bouncycastle.i18n.ErrorBundle;
import org.bouncycastle.i18n.LocaleString;
import org.bouncycastle.i18n.filter.TrustedInput;
import org.bouncycastle.i18n.filter.UntrustedInput;
import org.bouncycastle.i18n.filter.UntrustedUrlInput;
import org.bouncycastle.jce.provider.AnnotatedException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidator;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidatorException;
import org.bouncycastle.util.Integers;

public class PKIXCertPathReviewer extends CertPathValidatorUtilities {
    private static final String AUTH_INFO_ACCESS = X509Extensions.AuthorityInfoAccess.getId();
    private static final String CRL_DIST_POINTS = X509Extensions.CRLDistributionPoints.getId();
    private static final String QC_STATEMENT = X509Extensions.QCStatements.getId();
    private static final String RESOURCE_NAME = "org.bouncycastle.x509.CertPathReviewerMessages";
    protected CertPath certPath;
    protected List certs;
    protected List[] errors;
    private boolean initialized;
    protected int n;
    protected List[] notifications;
    protected PKIXParameters pkixParams;
    protected PolicyNode policyTree;
    protected PublicKey subjectPublicKey;
    protected TrustAnchor trustAnchor;
    protected Date validDate;

    public PKIXCertPathReviewer() {
    }

    public PKIXCertPathReviewer(CertPath certPath2, PKIXParameters pKIXParameters) throws CertPathReviewerException {
        init(certPath2, pKIXParameters);
    }

    private String IPtoString(byte[] bArr) {
        try {
            return InetAddress.getByAddress(bArr).getHostAddress();
        } catch (Exception e) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i != bArr.length; i++) {
                stringBuffer.append(Integer.toHexString(bArr[i] & 255));
                stringBuffer.append(' ');
            }
            return stringBuffer.toString();
        }
    }

    private void checkCriticalExtensions() {
        int size;
        List<PKIXCertPathChecker> certPathCheckers = this.pkixParams.getCertPathCheckers();
        for (PKIXCertPathChecker pKIXCertPathChecker : certPathCheckers) {
            try {
                pKIXCertPathChecker.init(false);
            } catch (CertPathValidatorException e) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.criticalExtensionError", new Object[]{e.getMessage(), e, e.getClass().getName()}), e.getCause(), this.certPath, size);
            } catch (CertPathValidatorException e2) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certPathCheckerError", new Object[]{e2.getMessage(), e2, e2.getClass().getName()}), e2);
            } catch (CertPathReviewerException e3) {
                addError(e3.getErrorMessage(), e3.getIndex());
                return;
            }
        }
        size = this.certs.size() - 1;
        while (size >= 0) {
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
            Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty()) {
                criticalExtensionOIDs.remove(KEY_USAGE);
                criticalExtensionOIDs.remove(CERTIFICATE_POLICIES);
                criticalExtensionOIDs.remove(POLICY_MAPPINGS);
                criticalExtensionOIDs.remove(INHIBIT_ANY_POLICY);
                criticalExtensionOIDs.remove(ISSUING_DISTRIBUTION_POINT);
                criticalExtensionOIDs.remove(DELTA_CRL_INDICATOR);
                criticalExtensionOIDs.remove(POLICY_CONSTRAINTS);
                criticalExtensionOIDs.remove(BASIC_CONSTRAINTS);
                criticalExtensionOIDs.remove(SUBJECT_ALTERNATIVE_NAME);
                criticalExtensionOIDs.remove(NAME_CONSTRAINTS);
                if (criticalExtensionOIDs.contains(QC_STATEMENT) && processQcStatements(x509Certificate, size)) {
                    criticalExtensionOIDs.remove(QC_STATEMENT);
                }
                for (PKIXCertPathChecker pKIXCertPathChecker2 : certPathCheckers) {
                    pKIXCertPathChecker2.check(x509Certificate, criticalExtensionOIDs);
                }
                if (!criticalExtensionOIDs.isEmpty()) {
                    Iterator<String> it2 = criticalExtensionOIDs.iterator();
                    while (it2.hasNext()) {
                        addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.unknownCriticalExt", new Object[]{new ASN1ObjectIdentifier(it2.next())}), size);
                    }
                }
            }
            size--;
        }
    }

    private void checkNameConstraints() {
        GeneralName instance;
        PKIXNameConstraintValidator pKIXNameConstraintValidator = new PKIXNameConstraintValidator();
        for (int size = this.certs.size() - 1; size > 0; size--) {
            int i = this.n - size;
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
            if (!isSelfIssued(x509Certificate)) {
                X500Principal subjectPrincipal = getSubjectPrincipal(x509Certificate);
                try {
                    ASN1Sequence aSN1Sequence = (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(subjectPrincipal.getEncoded())).readObject();
                    pKIXNameConstraintValidator.checkPermittedDN(aSN1Sequence);
                    pKIXNameConstraintValidator.checkExcludedDN(aSN1Sequence);
                    ASN1Sequence aSN1Sequence2 = (ASN1Sequence) getExtensionValue(x509Certificate, SUBJECT_ALTERNATIVE_NAME);
                    if (aSN1Sequence2 != null) {
                        for (int i2 = 0; i2 < aSN1Sequence2.size(); i2++) {
                            instance = GeneralName.getInstance(aSN1Sequence2.getObjectAt(i2));
                            pKIXNameConstraintValidator.checkPermitted(instance);
                            pKIXNameConstraintValidator.checkExcluded(instance);
                        }
                    }
                } catch (AnnotatedException e) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ncExtError"), e, this.certPath, size);
                } catch (PKIXNameConstraintValidatorException e2) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notPermittedEmail", new Object[]{new UntrustedInput(instance)}), e2, this.certPath, size);
                } catch (AnnotatedException e3) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.subjAltNameExtError"), e3, this.certPath, size);
                } catch (PKIXNameConstraintValidatorException e4) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.excludedDN", new Object[]{new UntrustedInput(subjectPrincipal.getName())}), e4, this.certPath, size);
                } catch (PKIXNameConstraintValidatorException e5) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notPermittedDN", new Object[]{new UntrustedInput(subjectPrincipal.getName())}), e5, this.certPath, size);
                } catch (IOException e6) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.ncSubjectNameError", new Object[]{new UntrustedInput(subjectPrincipal)}), e6, this.certPath, size);
                } catch (CertPathReviewerException e7) {
                    addError(e7.getErrorMessage(), e7.getIndex());
                    return;
                }
            }
            ASN1Sequence aSN1Sequence3 = (ASN1Sequence) getExtensionValue(x509Certificate, NAME_CONSTRAINTS);
            if (aSN1Sequence3 != null) {
                NameConstraints instance2 = NameConstraints.getInstance(aSN1Sequence3);
                GeneralSubtree[] permittedSubtrees = instance2.getPermittedSubtrees();
                if (permittedSubtrees != null) {
                    pKIXNameConstraintValidator.intersectPermittedSubtree(permittedSubtrees);
                }
                GeneralSubtree[] excludedSubtrees = instance2.getExcludedSubtrees();
                if (excludedSubtrees != null) {
                    for (int i3 = 0; i3 != excludedSubtrees.length; i3++) {
                        pKIXNameConstraintValidator.addExcludedSubtree(excludedSubtrees[i3]);
                    }
                }
            }
        }
    }

    private void checkPathLength() {
        int i;
        BasicConstraints basicConstraints;
        int i2;
        BigInteger pathLenConstraint;
        int i3 = this.n;
        int size = this.certs.size() - 1;
        int i4 = 0;
        int i5 = i3;
        while (size > 0) {
            int i6 = this.n - size;
            X509Certificate x509Certificate = (X509Certificate) this.certs.get(size);
            if (!isSelfIssued(x509Certificate)) {
                if (i5 <= 0) {
                    addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.pathLengthExtended"));
                }
                i = i5 - 1;
                i4++;
            } else {
                i = i5;
            }
            try {
                basicConstraints = BasicConstraints.getInstance(getExtensionValue(x509Certificate, BASIC_CONSTRAINTS));
            } catch (AnnotatedException e) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.processLengthConstError"), size);
                basicConstraints = null;
            }
            if (basicConstraints == null || (pathLenConstraint = basicConstraints.getPathLenConstraint()) == null || (i2 = pathLenConstraint.intValue()) >= i) {
                i2 = i;
            }
            size--;
            i5 = i2;
        }
        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.totalPathLength", new Object[]{Integers.valueOf(i4)}));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v90, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v31, resolved type: java.security.cert.X509Certificate} */
    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkPolicy() {
        /*
            r23 = this;
            r0 = r23
            java.security.cert.PKIXParameters r2 = r0.pkixParams
            java.util.Set r19 = r2.getInitialPolicies()
            r0 = r23
            int r2 = r0.n
            int r2 = r2 + 1
            java.util.ArrayList[] r0 = new java.util.ArrayList[r2]
            r20 = r0
            r2 = 0
        L_0x0013:
            r0 = r20
            int r3 = r0.length
            if (r2 >= r3) goto L_0x0022
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r20[r2] = r3
            int r2 = r2 + 1
            goto L_0x0013
        L_0x0022:
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
            r3 = r20[r3]
            r3.add(r2)
            r0 = r23
            java.security.cert.PKIXParameters r3 = r0.pkixParams
            boolean r3 = r3.isExplicitPolicyRequired()
            if (r3 == 0) goto L_0x00f3
            r3 = 0
        L_0x0053:
            r0 = r23
            java.security.cert.PKIXParameters r4 = r0.pkixParams
            boolean r4 = r4.isAnyPolicyInhibited()
            if (r4 == 0) goto L_0x00fb
            r4 = 0
        L_0x005e:
            r0 = r23
            java.security.cert.PKIXParameters r5 = r0.pkixParams
            boolean r5 = r5.isPolicyMappingInhibited()
            if (r5 == 0) goto L_0x0103
            r5 = 0
        L_0x0069:
            r7 = 0
            r10 = 0
            r0 = r23
            java.util.List r6 = r0.certs     // Catch:{ CertPathReviewerException -> 0x00e4 }
            int r6 = r6.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            int r6 = r6 + -1
            r16 = r6
            r12 = r5
            r13 = r4
            r14 = r3
            r15 = r2
        L_0x007b:
            if (r16 < 0) goto L_0x0489
            r0 = r23
            int r2 = r0.n     // Catch:{ CertPathReviewerException -> 0x00e4 }
            int r4 = r2 - r16
            r0 = r23
            java.util.List r2 = r0.certs     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            java.lang.Object r2 = r2.get(r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r2
            java.security.cert.X509Certificate r0 = (java.security.cert.X509Certificate) r0     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r10 = r0
            java.lang.String r2 = org.bouncycastle.x509.PKIXCertPathReviewer.CERTIFICATE_POLICIES     // Catch:{ AnnotatedException -> 0x010b }
            org.bouncycastle.asn1.ASN1Primitive r2 = getExtensionValue(r10, r2)     // Catch:{ AnnotatedException -> 0x010b }
            r0 = r2
            org.bouncycastle.asn1.ASN1Sequence r0 = (org.bouncycastle.asn1.ASN1Sequence) r0     // Catch:{ AnnotatedException -> 0x010b }
            r11 = r0
            if (r11 == 0) goto L_0x0289
            if (r15 == 0) goto L_0x0289
            java.util.Enumeration r3 = r11.getObjects()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.HashSet r2 = new java.util.HashSet     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x00a8:
            boolean r5 = r3.hasMoreElements()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 == 0) goto L_0x013b
            java.lang.Object r5 = r3.nextElement()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.x509.PolicyInformation r5 = org.bouncycastle.asn1.x509.PolicyInformation.getInstance(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r6 = r5.getPolicyIdentifier()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r8 = r6.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2.add(r8)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r8 = "2.5.29.32.0"
            java.lang.String r9 = r6.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r8 = r8.equals(r9)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r8 != 0) goto L_0x00a8
            org.bouncycastle.asn1.ASN1Sequence r5 = r5.getPolicyQualifiers()     // Catch:{ CertPathValidatorException -> 0x0123 }
            java.util.Set r5 = getQualifierSet(r5)     // Catch:{ CertPathValidatorException -> 0x0123 }
            r0 = r20
            boolean r8 = processCertD1i(r4, r0, r6, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r8 != 0) goto L_0x00a8
            r0 = r20
            processCertD1ii(r4, r0, r6, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x00a8
        L_0x00e4:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r3 = r2.getErrorMessage()
            int r2 = r2.getIndex()
            r0 = r23
            r0.addError(r3, r2)
        L_0x00f2:
            return
        L_0x00f3:
            r0 = r23
            int r3 = r0.n
            int r3 = r3 + 1
            goto L_0x0053
        L_0x00fb:
            r0 = r23
            int r4 = r0.n
            int r4 = r4 + 1
            goto L_0x005e
        L_0x0103:
            r0 = r23
            int r5 = r0.n
            int r5 = r5 + 1
            goto L_0x0069
        L_0x010b:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r3 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.policyExtError"
            r3.<init>(r4, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r4 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r5 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r4.<init>(r3, r2, r5, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r4     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0123:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r3 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.policyQualifierError"
            r3.<init>(r4, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r4 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r5 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r4.<init>(r3, r2, r5, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r4     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x013b:
            if (r7 == 0) goto L_0x0146
            java.lang.String r3 = "2.5.29.32.0"
            boolean r3 = r7.contains(r3)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r3 == 0) goto L_0x01d1
        L_0x0146:
            r17 = r2
        L_0x0148:
            if (r13 > 0) goto L_0x0156
            r0 = r23
            int r2 = r0.n     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r4 >= r2) goto L_0x0238
            boolean r2 = isSelfIssued(r10)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x0238
        L_0x0156:
            java.util.Enumeration r2 = r11.getObjects()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x015a:
            boolean r3 = r2.hasMoreElements()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r3 == 0) goto L_0x0238
            java.lang.Object r3 = r2.nextElement()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.x509.PolicyInformation r3 = org.bouncycastle.asn1.x509.PolicyInformation.getInstance(r3)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r5 = "2.5.29.32.0"
            org.bouncycastle.asn1.ASN1ObjectIdentifier r6 = r3.getPolicyIdentifier()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r6 = r6.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r5 = r5.equals(r6)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 == 0) goto L_0x015a
            org.bouncycastle.asn1.ASN1Sequence r2 = r3.getPolicyQualifiers()     // Catch:{ CertPathValidatorException -> 0x01f0 }
            java.util.Set r7 = getQualifierSet(r2)     // Catch:{ CertPathValidatorException -> 0x01f0 }
            int r2 = r4 + -1
            r21 = r20[r2]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r18 = r2
        L_0x0188:
            int r2 = r21.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r18
            if (r0 >= r2) goto L_0x0238
            r0 = r21
            r1 = r18
            java.lang.Object r6 = r0.get(r1)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r6 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r6     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.Set r2 = r6.getExpectedPolicies()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.Iterator r22 = r2.iterator()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x01a2:
            boolean r2 = r22.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x0232
            java.lang.Object r2 = r22.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r3 = r2 instanceof java.lang.String     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r3 == 0) goto L_0x0208
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r8 = r2
        L_0x01b3:
            r3 = 0
            java.util.Iterator r5 = r6.getChildren()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x01b8:
            boolean r2 = r5.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x0213
            java.lang.Object r2 = r5.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r2 = r2.getValidPolicy()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r2 = r8.equals(r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x06b1
            r2 = 1
        L_0x01cf:
            r3 = r2
            goto L_0x01b8
        L_0x01d1:
            java.util.Iterator r3 = r7.iterator()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.HashSet r17 = new java.util.HashSet     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r17.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x01da:
            boolean r5 = r3.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 == 0) goto L_0x0148
            java.lang.Object r5 = r3.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r6 = r2.contains(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r6 == 0) goto L_0x01da
            r0 = r17
            r0.add(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x01da
        L_0x01f0:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r3 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.policyQualifierError"
            r3.<init>(r4, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r4 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r5 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r4.<init>(r3, r2, r5, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r4     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0208:
            boolean r3 = r2 instanceof org.bouncycastle.asn1.ASN1ObjectIdentifier     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r3 == 0) goto L_0x01a2
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = (org.bouncycastle.asn1.ASN1ObjectIdentifier) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r8 = r2.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x01b3
        L_0x0213:
            if (r3 != 0) goto L_0x01a2
            java.util.HashSet r5 = new java.util.HashSet     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r5.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r5.add(r8)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = new org.bouncycastle.jce.provider.PKIXPolicyNode     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r3.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r9 = 0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r6.addChild(r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r3 = r20[r4]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r3.add(r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x01a2
        L_0x0232:
            int r2 = r18 + 1
            r18 = r2
            goto L_0x0188
        L_0x0238:
            int r2 = r4 + -1
            r6 = r2
        L_0x023b:
            if (r6 < 0) goto L_0x0266
            r7 = r20[r6]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r5 = r2
            r3 = r15
        L_0x0242:
            int r2 = r7.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 >= r2) goto L_0x06ae
            java.lang.Object r2 = r7.get(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r8 = r2.hasChildren()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r8 != 0) goto L_0x0261
            r0 = r20
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = removePolicyNode(r3, r0, r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 != 0) goto L_0x0262
        L_0x025c:
            int r3 = r6 + -1
            r6 = r3
            r15 = r2
            goto L_0x023b
        L_0x0261:
            r2 = r3
        L_0x0262:
            int r5 = r5 + 1
            r3 = r2
            goto L_0x0242
        L_0x0266:
            java.util.Set r2 = r10.getCriticalExtensionOIDs()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x06a9
            java.lang.String r3 = org.bouncycastle.x509.PKIXCertPathReviewer.CERTIFICATE_POLICIES     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r5 = r2.contains(r3)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r6 = r20[r4]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r3 = r2
        L_0x0276:
            int r2 = r6.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r3 >= r2) goto L_0x06a9
            java.lang.Object r2 = r6.get(r3)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2.setCritical(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            int r2 = r3 + 1
            r3 = r2
            goto L_0x0276
        L_0x0289:
            r8 = r7
            r2 = r15
        L_0x028b:
            if (r11 != 0) goto L_0x06a6
            r7 = 0
        L_0x028e:
            if (r14 > 0) goto L_0x02a3
            if (r7 != 0) goto L_0x02a3
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.noValidPolicyTree"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r3.<init>(r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x02a3:
            r0 = r23
            int r2 = r0.n     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r4 == r2) goto L_0x06a0
            java.lang.String r2 = org.bouncycastle.x509.PKIXCertPathReviewer.POLICY_MAPPINGS     // Catch:{ AnnotatedException -> 0x02f5 }
            org.bouncycastle.asn1.ASN1Primitive r3 = getExtensionValue(r10, r2)     // Catch:{ AnnotatedException -> 0x02f5 }
            if (r3 == 0) goto L_0x0335
            r0 = r3
            org.bouncycastle.asn1.ASN1Sequence r0 = (org.bouncycastle.asn1.ASN1Sequence) r0     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = r0
            r5 = 0
            r9 = r5
        L_0x02b7:
            int r5 = r2.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r9 >= r5) goto L_0x0335
            org.bouncycastle.asn1.ASN1Encodable r5 = r2.getObjectAt(r9)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.ASN1Sequence r5 = (org.bouncycastle.asn1.ASN1Sequence) r5     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r6 = 0
            org.bouncycastle.asn1.ASN1Encodable r6 = r5.getObjectAt(r6)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r6 = (org.bouncycastle.asn1.ASN1ObjectIdentifier) r6     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r11 = 1
            org.bouncycastle.asn1.ASN1Encodable r5 = r5.getObjectAt(r11)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r5 = (org.bouncycastle.asn1.ASN1ObjectIdentifier) r5     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r11 = "2.5.29.32.0"
            java.lang.String r6 = r6.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r6 = r11.equals(r6)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r6 == 0) goto L_0x030d
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.invalidPolicyMapping"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r4 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r3.<init>(r2, r4, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x02f5:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r3 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.policyMapExtError"
            r3.<init>(r4, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r4 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r5 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r4.<init>(r3, r2, r5, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r4     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x030d:
            java.lang.String r6 = "2.5.29.32.0"
            java.lang.String r5 = r5.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r5 = r6.equals(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 == 0) goto L_0x0331
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.invalidPolicyMapping"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r4 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r3.<init>(r2, r4, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0331:
            int r5 = r9 + 1
            r9 = r5
            goto L_0x02b7
        L_0x0335:
            if (r3 == 0) goto L_0x03de
            org.bouncycastle.asn1.ASN1Sequence r3 = (org.bouncycastle.asn1.ASN1Sequence) r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.HashMap r9 = new java.util.HashMap     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r9.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.HashSet r11 = new java.util.HashSet     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r11.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r6 = r2
        L_0x0345:
            int r2 = r3.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r6 >= r2) goto L_0x0389
            org.bouncycastle.asn1.ASN1Encodable r2 = r3.getObjectAt(r6)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.ASN1Sequence r2 = (org.bouncycastle.asn1.ASN1Sequence) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r5 = 0
            org.bouncycastle.asn1.ASN1Encodable r5 = r2.getObjectAt(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r5 = (org.bouncycastle.asn1.ASN1ObjectIdentifier) r5     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r5 = r5.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r15 = 1
            org.bouncycastle.asn1.ASN1Encodable r2 = r2.getObjectAt(r15)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = (org.bouncycastle.asn1.ASN1ObjectIdentifier) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r15 = r2.getId()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r2 = r9.containsKey(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 != 0) goto L_0x037f
            java.util.HashSet r2 = new java.util.HashSet     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2.add(r15)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r9.put(r5, r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r11.add(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x037b:
            int r2 = r6 + 1
            r6 = r2
            goto L_0x0345
        L_0x037f:
            java.lang.Object r2 = r9.get(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.util.Set r2 = (java.util.Set) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2.add(r15)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x037b
        L_0x0389:
            java.util.Iterator r5 = r11.iterator()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r3 = r7
        L_0x038e:
            boolean r2 = r5.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x03dd
            java.lang.Object r2 = r5.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r12 <= 0) goto L_0x03d4
            r0 = r20
            prepareNextCertB1(r4, r0, r2, r9, r10)     // Catch:{ AnnotatedException -> 0x03a4, CertPathValidatorException -> 0x03bc }
            r2 = r3
        L_0x03a2:
            r3 = r2
            goto L_0x038e
        L_0x03a4:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r3 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.policyExtError"
            r3.<init>(r4, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r4 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r5 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r4.<init>(r3, r2, r5, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r4     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x03bc:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r3 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.policyQualifierError"
            r3.<init>(r4, r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r4 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r5 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r4.<init>(r3, r2, r5, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r4     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x03d4:
            if (r12 > 0) goto L_0x069d
            r0 = r20
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = prepareNextCertB2(r4, r0, r2, r3)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x03a2
        L_0x03dd:
            r7 = r3
        L_0x03de:
            boolean r2 = isSelfIssued(r10)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 != 0) goto L_0x0698
            if (r14 == 0) goto L_0x0695
            int r5 = r14 + -1
        L_0x03e8:
            if (r12 == 0) goto L_0x0692
            int r3 = r12 + -1
        L_0x03ec:
            if (r13 == 0) goto L_0x068f
            int r13 = r13 + -1
            r6 = r13
        L_0x03f1:
            java.lang.String r2 = org.bouncycastle.x509.PKIXCertPathReviewer.POLICY_CONSTRAINTS     // Catch:{ AnnotatedException -> 0x0439 }
            org.bouncycastle.asn1.ASN1Primitive r2 = getExtensionValue(r10, r2)     // Catch:{ AnnotatedException -> 0x0439 }
            org.bouncycastle.asn1.ASN1Sequence r2 = (org.bouncycastle.asn1.ASN1Sequence) r2     // Catch:{ AnnotatedException -> 0x0439 }
            if (r2 == 0) goto L_0x0451
            java.util.Enumeration r9 = r2.getObjects()     // Catch:{ AnnotatedException -> 0x0439 }
        L_0x03ff:
            boolean r2 = r9.hasMoreElements()     // Catch:{ AnnotatedException -> 0x0439 }
            if (r2 == 0) goto L_0x0451
            java.lang.Object r2 = r9.nextElement()     // Catch:{ AnnotatedException -> 0x0439 }
            org.bouncycastle.asn1.ASN1TaggedObject r2 = (org.bouncycastle.asn1.ASN1TaggedObject) r2     // Catch:{ AnnotatedException -> 0x0439 }
            int r4 = r2.getTagNo()     // Catch:{ AnnotatedException -> 0x0439 }
            switch(r4) {
                case 0: goto L_0x0417;
                case 1: goto L_0x0428;
                default: goto L_0x0412;
            }     // Catch:{ AnnotatedException -> 0x0439 }
        L_0x0412:
            r2 = r3
            r4 = r5
        L_0x0414:
            r3 = r2
            r5 = r4
            goto L_0x03ff
        L_0x0417:
            r4 = 0
            org.bouncycastle.asn1.ASN1Integer r2 = org.bouncycastle.asn1.ASN1Integer.getInstance(r2, r4)     // Catch:{ AnnotatedException -> 0x0439 }
            java.math.BigInteger r2 = r2.getValue()     // Catch:{ AnnotatedException -> 0x0439 }
            int r4 = r2.intValue()     // Catch:{ AnnotatedException -> 0x0439 }
            if (r4 >= r5) goto L_0x0412
            r2 = r3
            goto L_0x0414
        L_0x0428:
            r4 = 0
            org.bouncycastle.asn1.ASN1Integer r2 = org.bouncycastle.asn1.ASN1Integer.getInstance(r2, r4)     // Catch:{ AnnotatedException -> 0x0439 }
            java.math.BigInteger r2 = r2.getValue()     // Catch:{ AnnotatedException -> 0x0439 }
            int r2 = r2.intValue()     // Catch:{ AnnotatedException -> 0x0439 }
            if (r2 >= r3) goto L_0x0412
            r4 = r5
            goto L_0x0414
        L_0x0439:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.policyConstExtError"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r4 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r3.<init>(r2, r4, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0451:
            java.lang.String r2 = org.bouncycastle.x509.PKIXCertPathReviewer.INHIBIT_ANY_POLICY     // Catch:{ AnnotatedException -> 0x0471 }
            org.bouncycastle.asn1.ASN1Primitive r2 = getExtensionValue(r10, r2)     // Catch:{ AnnotatedException -> 0x0471 }
            org.bouncycastle.asn1.ASN1Integer r2 = (org.bouncycastle.asn1.ASN1Integer) r2     // Catch:{ AnnotatedException -> 0x0471 }
            if (r2 == 0) goto L_0x068c
            java.math.BigInteger r2 = r2.getValue()     // Catch:{ AnnotatedException -> 0x0471 }
            int r2 = r2.intValue()     // Catch:{ AnnotatedException -> 0x0471 }
            if (r2 >= r6) goto L_0x068c
        L_0x0465:
            r4 = r7
        L_0x0466:
            int r6 = r16 + -1
            r16 = r6
            r7 = r8
            r12 = r3
            r13 = r2
            r14 = r5
            r15 = r4
            goto L_0x007b
        L_0x0471:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.policyInhibitExtError"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r4 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r3.<init>(r2, r4, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0489:
            boolean r2 = isSelfIssued(r10)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 != 0) goto L_0x0689
            if (r14 <= 0) goto L_0x0689
            int r14 = r14 + -1
            r3 = r14
        L_0x0494:
            java.lang.String r2 = org.bouncycastle.x509.PKIXCertPathReviewer.POLICY_CONSTRAINTS     // Catch:{ AnnotatedException -> 0x04c9 }
            org.bouncycastle.asn1.ASN1Primitive r2 = getExtensionValue(r10, r2)     // Catch:{ AnnotatedException -> 0x04c9 }
            org.bouncycastle.asn1.ASN1Sequence r2 = (org.bouncycastle.asn1.ASN1Sequence) r2     // Catch:{ AnnotatedException -> 0x04c9 }
            if (r2 == 0) goto L_0x04e1
            java.util.Enumeration r4 = r2.getObjects()     // Catch:{ AnnotatedException -> 0x04c9 }
        L_0x04a2:
            boolean r2 = r4.hasMoreElements()     // Catch:{ AnnotatedException -> 0x04c9 }
            if (r2 == 0) goto L_0x04e1
            java.lang.Object r2 = r4.nextElement()     // Catch:{ AnnotatedException -> 0x04c9 }
            org.bouncycastle.asn1.ASN1TaggedObject r2 = (org.bouncycastle.asn1.ASN1TaggedObject) r2     // Catch:{ AnnotatedException -> 0x04c9 }
            int r5 = r2.getTagNo()     // Catch:{ AnnotatedException -> 0x04c9 }
            switch(r5) {
                case 0: goto L_0x04b8;
                default: goto L_0x04b5;
            }     // Catch:{ AnnotatedException -> 0x04c9 }
        L_0x04b5:
            r2 = r3
        L_0x04b6:
            r3 = r2
            goto L_0x04a2
        L_0x04b8:
            r5 = 0
            org.bouncycastle.asn1.ASN1Integer r2 = org.bouncycastle.asn1.ASN1Integer.getInstance(r2, r5)     // Catch:{ AnnotatedException -> 0x04c9 }
            java.math.BigInteger r2 = r2.getValue()     // Catch:{ AnnotatedException -> 0x04c9 }
            int r2 = r2.intValue()     // Catch:{ AnnotatedException -> 0x04c9 }
            if (r2 != 0) goto L_0x04b5
            r2 = 0
            goto L_0x04b6
        L_0x04c9:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.policyConstExtError"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r4 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r3.<init>(r2, r4, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x04e1:
            if (r15 != 0) goto L_0x051a
            r0 = r23
            java.security.cert.PKIXParameters r2 = r0.pkixParams     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r2 = r2.isExplicitPolicyRequired()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x0504
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.explicitPolicy"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r4 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r3.<init>(r2, r4, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0504:
            r2 = 0
        L_0x0505:
            if (r3 > 0) goto L_0x00f2
            if (r2 != 0) goto L_0x00f2
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.invalidPolicy"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r3.<init>(r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x051a:
            boolean r2 = isAnyPolicy(r19)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x05d6
            r0 = r23
            java.security.cert.PKIXParameters r2 = r0.pkixParams     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r2 = r2.isExplicitPolicyRequired()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x0686
            boolean r2 = r7.isEmpty()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x0547
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.explicitPolicy"
            r2.<init>(r3, r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.x509.CertPathReviewerException r3 = new org.bouncycastle.x509.CertPathReviewerException     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r23
            java.security.cert.CertPath r4 = r0.certPath     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r16
            r3.<init>(r2, r4, r0)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            throw r3     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0547:
            java.util.HashSet r6 = new java.util.HashSet     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r6.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r5 = r2
        L_0x054e:
            r0 = r20
            int r2 = r0.length     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 >= r2) goto L_0x058a
            r8 = r20[r5]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r4 = r2
        L_0x0557:
            int r2 = r8.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r4 >= r2) goto L_0x0586
            java.lang.Object r2 = r8.get(r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r9 = "2.5.29.32.0"
            java.lang.String r10 = r2.getValidPolicy()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r9 = r9.equals(r10)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r9 == 0) goto L_0x0582
            java.util.Iterator r2 = r2.getChildren()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0574:
            boolean r9 = r2.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r9 == 0) goto L_0x0582
            java.lang.Object r9 = r2.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r6.add(r9)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x0574
        L_0x0582:
            int r2 = r4 + 1
            r4 = r2
            goto L_0x0557
        L_0x0586:
            int r2 = r5 + 1
            r5 = r2
            goto L_0x054e
        L_0x058a:
            java.util.Iterator r4 = r6.iterator()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x058e:
            boolean r2 = r4.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x05a5
            java.lang.Object r2 = r4.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r2 = r2.getValidPolicy()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r2 = r7.contains(r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 != 0) goto L_0x058e
            goto L_0x058e
        L_0x05a5:
            if (r15 == 0) goto L_0x0686
            r0 = r23
            int r2 = r0.n     // Catch:{ CertPathReviewerException -> 0x00e4 }
            int r4 = r2 + -1
            r6 = r4
            r2 = r15
        L_0x05af:
            if (r6 < 0) goto L_0x0505
            r7 = r20[r6]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r5 = 0
            r4 = r2
        L_0x05b5:
            int r2 = r7.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 >= r2) goto L_0x05d1
            java.lang.Object r2 = r7.get(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r8 = r2.hasChildren()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r8 != 0) goto L_0x0683
            r0 = r20
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = removePolicyNode(r4, r0, r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x05cd:
            int r5 = r5 + 1
            r4 = r2
            goto L_0x05b5
        L_0x05d1:
            int r5 = r6 + -1
            r6 = r5
            r2 = r4
            goto L_0x05af
        L_0x05d6:
            java.util.HashSet r6 = new java.util.HashSet     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r6.<init>()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r5 = r2
        L_0x05dd:
            r0 = r20
            int r2 = r0.length     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 >= r2) goto L_0x0628
            r7 = r20[r5]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r4 = r2
        L_0x05e6:
            int r2 = r7.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r4 >= r2) goto L_0x0624
            java.lang.Object r2 = r7.get(r4)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r8 = "2.5.29.32.0"
            java.lang.String r9 = r2.getValidPolicy()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r8 = r8.equals(r9)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r8 == 0) goto L_0x0620
            java.util.Iterator r8 = r2.getChildren()     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0603:
            boolean r2 = r8.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x0620
            java.lang.Object r2 = r8.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r9 = "2.5.29.32.0"
            java.lang.String r10 = r2.getValidPolicy()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r9 = r9.equals(r10)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r9 != 0) goto L_0x0603
            r6.add(r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            goto L_0x0603
        L_0x0620:
            int r2 = r4 + 1
            r4 = r2
            goto L_0x05e6
        L_0x0624:
            int r2 = r5 + 1
            r5 = r2
            goto L_0x05dd
        L_0x0628:
            java.util.Iterator r5 = r6.iterator()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r4 = r15
        L_0x062d:
            boolean r2 = r5.hasNext()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r2 == 0) goto L_0x064d
            java.lang.Object r2 = r5.next()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            java.lang.String r6 = r2.getValidPolicy()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r0 = r19
            boolean r6 = r0.contains(r6)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r6 != 0) goto L_0x0681
            r0 = r20
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = removePolicyNode(r4, r0, r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x064b:
            r4 = r2
            goto L_0x062d
        L_0x064d:
            if (r4 == 0) goto L_0x067c
            r0 = r23
            int r2 = r0.n     // Catch:{ CertPathReviewerException -> 0x00e4 }
            int r2 = r2 + -1
            r6 = r2
        L_0x0656:
            if (r6 < 0) goto L_0x067c
            r7 = r20[r6]     // Catch:{ CertPathReviewerException -> 0x00e4 }
            r2 = 0
            r5 = r2
        L_0x065c:
            int r2 = r7.size()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r5 >= r2) goto L_0x0678
            java.lang.Object r2 = r7.get(r5)     // Catch:{ CertPathReviewerException -> 0x00e4 }
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = (org.bouncycastle.jce.provider.PKIXPolicyNode) r2     // Catch:{ CertPathReviewerException -> 0x00e4 }
            boolean r8 = r2.hasChildren()     // Catch:{ CertPathReviewerException -> 0x00e4 }
            if (r8 != 0) goto L_0x067f
            r0 = r20
            org.bouncycastle.jce.provider.PKIXPolicyNode r2 = removePolicyNode(r4, r0, r2)     // Catch:{ CertPathReviewerException -> 0x00e4 }
        L_0x0674:
            int r5 = r5 + 1
            r4 = r2
            goto L_0x065c
        L_0x0678:
            int r2 = r6 + -1
            r6 = r2
            goto L_0x0656
        L_0x067c:
            r2 = r4
            goto L_0x0505
        L_0x067f:
            r2 = r4
            goto L_0x0674
        L_0x0681:
            r2 = r4
            goto L_0x064b
        L_0x0683:
            r2 = r4
            goto L_0x05cd
        L_0x0686:
            r2 = r15
            goto L_0x0505
        L_0x0689:
            r3 = r14
            goto L_0x0494
        L_0x068c:
            r2 = r6
            goto L_0x0465
        L_0x068f:
            r6 = r13
            goto L_0x03f1
        L_0x0692:
            r3 = r12
            goto L_0x03ec
        L_0x0695:
            r5 = r14
            goto L_0x03e8
        L_0x0698:
            r3 = r12
            r6 = r13
            r5 = r14
            goto L_0x03f1
        L_0x069d:
            r2 = r3
            goto L_0x03a2
        L_0x06a0:
            r3 = r12
            r2 = r13
            r5 = r14
            r4 = r7
            goto L_0x0466
        L_0x06a6:
            r7 = r2
            goto L_0x028e
        L_0x06a9:
            r8 = r17
            r2 = r15
            goto L_0x028b
        L_0x06ae:
            r2 = r3
            goto L_0x025c
        L_0x06b1:
            r2 = r3
            goto L_0x01cf
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.x509.PKIXCertPathReviewer.checkPolicy():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:157:0x04df  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00d4  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0085  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkSignatures() {
        /*
            r18 = this;
            r3 = 0
            r4 = 0
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.certPathValidDate"
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r7 = 0
            org.bouncycastle.i18n.filter.TrustedInput r8 = new org.bouncycastle.i18n.filter.TrustedInput
            r0 = r18
            java.util.Date r9 = r0.validDate
            r8.<init>(r9)
            r6[r7] = r8
            r7 = 1
            org.bouncycastle.i18n.filter.TrustedInput r8 = new org.bouncycastle.i18n.filter.TrustedInput
            java.util.Date r9 = new java.util.Date
            r9.<init>()
            r8.<init>(r9)
            r6[r7] = r8
            r1.<init>(r2, r5, r6)
            r0 = r18
            r0.addNotification(r1)
            r0 = r18
            java.util.List r1 = r0.certs     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r0 = r18
            java.util.List r2 = r0.certs     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            int r2 = r2.size()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            int r2 = r2 + -1
            java.lang.Object r1 = r1.get(r2)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.security.cert.X509Certificate r1 = (java.security.cert.X509Certificate) r1     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r0 = r18
            java.security.cert.PKIXParameters r2 = r0.pkixParams     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.util.Set r2 = r2.getTrustAnchors()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r0 = r18
            java.util.Collection r2 = r0.getTrustAnchors(r1, r2)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            int r5 = r2.size()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r6 = 1
            if (r5 <= r6) goto L_0x0152
            org.bouncycastle.i18n.ErrorBundle r5 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.lang.String r6 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r7 = "CertPathReviewer.conflictingTrustAnchors"
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r9 = 0
            int r2 = r2.size()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.lang.Integer r2 = org.bouncycastle.util.Integers.valueOf(r2)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r8[r9] = r2     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r2 = 1
            org.bouncycastle.i18n.filter.UntrustedInput r9 = new org.bouncycastle.i18n.filter.UntrustedInput     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            javax.security.auth.x500.X500Principal r1 = r1.getIssuerX500Principal()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r9.<init>(r1)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r8[r2] = r9     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r5.<init>(r6, r7, r8)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r0 = r18
            r0.addError(r5)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r2 = r3
        L_0x0082:
            r11 = r2
        L_0x0083:
            if (r11 == 0) goto L_0x04df
            java.security.cert.X509Certificate r2 = r11.getTrustedCert()
            if (r2 == 0) goto L_0x0203
            javax.security.auth.x500.X500Principal r1 = getSubjectPrincipal(r2)     // Catch:{ IllegalArgumentException -> 0x020e }
        L_0x008f:
            r3 = r1
        L_0x0090:
            if (r2 == 0) goto L_0x00ad
            boolean[] r1 = r2.getKeyUsage()
            if (r1 == 0) goto L_0x00ad
            r2 = 5
            boolean r1 = r1[r2]
            if (r1 != 0) goto L_0x00ad
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.trustKeyUsage"
            r1.<init>(r2, r4)
            r0 = r18
            r0.addNotification(r1)
        L_0x00ad:
            r2 = 0
            r1 = 0
            if (r11 == 0) goto L_0x00c5
            java.security.cert.X509Certificate r1 = r11.getTrustedCert()
            if (r1 == 0) goto L_0x0231
            java.security.PublicKey r2 = r1.getPublicKey()
        L_0x00bb:
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r4 = getAlgorithmIdentifier(r2)     // Catch:{ CertPathValidatorException -> 0x0237 }
            r4.getAlgorithm()     // Catch:{ CertPathValidatorException -> 0x0237 }
            r4.getParameters()     // Catch:{ CertPathValidatorException -> 0x0237 }
        L_0x00c5:
            r0 = r18
            java.util.List r4 = r0.certs
            int r4 = r4.size()
            int r9 = r4 + -1
            r5 = r1
            r10 = r3
            r6 = r2
        L_0x00d2:
            if (r9 < 0) goto L_0x04c7
            r0 = r18
            int r1 = r0.n
            int r12 = r1 - r9
            r0 = r18
            java.util.List r1 = r0.certs
            java.lang.Object r3 = r1.get(r9)
            java.security.cert.X509Certificate r3 = (java.security.cert.X509Certificate) r3
            if (r6 == 0) goto L_0x0275
            r0 = r18
            java.security.cert.PKIXParameters r1 = r0.pkixParams     // Catch:{ GeneralSecurityException -> 0x024a }
            java.lang.String r1 = r1.getSigProvider()     // Catch:{ GeneralSecurityException -> 0x024a }
            org.bouncycastle.x509.CertPathValidatorUtilities.verifyX509Certificate(r3, r6, r1)     // Catch:{ GeneralSecurityException -> 0x024a }
        L_0x00f1:
            r0 = r18
            java.util.Date r1 = r0.validDate     // Catch:{ CertificateNotYetValidException -> 0x033a, CertificateExpiredException -> 0x035c }
            r3.checkValidity(r1)     // Catch:{ CertificateNotYetValidException -> 0x033a, CertificateExpiredException -> 0x035c }
        L_0x00f8:
            r0 = r18
            java.security.cert.PKIXParameters r1 = r0.pkixParams
            boolean r1 = r1.isRevocationEnabled()
            if (r1 == 0) goto L_0x03db
            r1 = 0
            java.lang.String r2 = org.bouncycastle.x509.PKIXCertPathReviewer.CRL_DIST_POINTS     // Catch:{ AnnotatedException -> 0x037e }
            org.bouncycastle.asn1.ASN1Primitive r2 = getExtensionValue(r3, r2)     // Catch:{ AnnotatedException -> 0x037e }
            if (r2 == 0) goto L_0x010f
            org.bouncycastle.asn1.x509.CRLDistPoint r1 = org.bouncycastle.asn1.x509.CRLDistPoint.getInstance(r2)     // Catch:{ AnnotatedException -> 0x037e }
        L_0x010f:
            r2 = 0
            java.lang.String r4 = org.bouncycastle.x509.PKIXCertPathReviewer.AUTH_INFO_ACCESS     // Catch:{ AnnotatedException -> 0x0391 }
            org.bouncycastle.asn1.ASN1Primitive r4 = getExtensionValue(r3, r4)     // Catch:{ AnnotatedException -> 0x0391 }
            if (r4 == 0) goto L_0x011c
            org.bouncycastle.asn1.x509.AuthorityInformationAccess r2 = org.bouncycastle.asn1.x509.AuthorityInformationAccess.getInstance(r4)     // Catch:{ AnnotatedException -> 0x0391 }
        L_0x011c:
            r0 = r18
            java.util.Vector r7 = r0.getCRLDistUrls(r1)
            r0 = r18
            java.util.Vector r8 = r0.getOCSPUrls(r2)
            java.util.Iterator r1 = r7.iterator()
        L_0x012c:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x03a4
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r13 = "CertPathReviewer.crlDistPoint"
            r14 = 1
            java.lang.Object[] r14 = new java.lang.Object[r14]
            r15 = 0
            org.bouncycastle.i18n.filter.UntrustedUrlInput r16 = new org.bouncycastle.i18n.filter.UntrustedUrlInput
            java.lang.Object r17 = r1.next()
            r16.<init>(r17)
            r14[r15] = r16
            r2.<init>(r4, r13, r14)
            r0 = r18
            r0.addNotification(r2, r9)
            goto L_0x012c
        L_0x0152:
            boolean r5 = r2.isEmpty()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            if (r5 == 0) goto L_0x018d
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.lang.String r5 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r6 = "CertPathReviewer.noTrustAnchorFound"
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r8 = 0
            org.bouncycastle.i18n.filter.UntrustedInput r9 = new org.bouncycastle.i18n.filter.UntrustedInput     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            javax.security.auth.x500.X500Principal r1 = r1.getIssuerX500Principal()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r9.<init>(r1)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r7[r8] = r9     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r1 = 1
            r0 = r18
            java.security.cert.PKIXParameters r8 = r0.pkixParams     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.util.Set r8 = r8.getTrustAnchors()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            int r8 = r8.size()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.lang.Integer r8 = org.bouncycastle.util.Integers.valueOf(r8)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r7[r1] = r8     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r2.<init>(r5, r6, r7)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r0 = r18
            r0.addError(r2)     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            r2 = r3
            goto L_0x0082
        L_0x018d:
            java.util.Iterator r2 = r2.iterator()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.lang.Object r2 = r2.next()     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.security.cert.TrustAnchor r2 = (java.security.cert.TrustAnchor) r2     // Catch:{ CertPathReviewerException -> 0x04d9, Throwable -> 0x01d8 }
            java.security.cert.X509Certificate r3 = r2.getTrustedCert()     // Catch:{ CertPathReviewerException -> 0x01c5, Throwable -> 0x04d5 }
            if (r3 == 0) goto L_0x01d3
            java.security.cert.X509Certificate r3 = r2.getTrustedCert()     // Catch:{ CertPathReviewerException -> 0x01c5, Throwable -> 0x04d5 }
            java.security.PublicKey r3 = r3.getPublicKey()     // Catch:{ CertPathReviewerException -> 0x01c5, Throwable -> 0x04d5 }
        L_0x01a5:
            r0 = r18
            java.security.cert.PKIXParameters r5 = r0.pkixParams     // Catch:{ SignatureException -> 0x01b2, Exception -> 0x04dc }
            java.lang.String r5 = r5.getSigProvider()     // Catch:{ SignatureException -> 0x01b2, Exception -> 0x04dc }
            org.bouncycastle.x509.CertPathValidatorUtilities.verifyX509Certificate(r1, r3, r5)     // Catch:{ SignatureException -> 0x01b2, Exception -> 0x04dc }
            goto L_0x0082
        L_0x01b2:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ CertPathReviewerException -> 0x01c5, Throwable -> 0x04d5 }
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.trustButInvalidCert"
            r1.<init>(r3, r5)     // Catch:{ CertPathReviewerException -> 0x01c5, Throwable -> 0x04d5 }
            r0 = r18
            r0.addError(r1)     // Catch:{ CertPathReviewerException -> 0x01c5, Throwable -> 0x04d5 }
            goto L_0x0082
        L_0x01c5:
            r1 = move-exception
            r3 = r2
        L_0x01c7:
            org.bouncycastle.i18n.ErrorBundle r1 = r1.getErrorMessage()
            r0 = r18
            r0.addError(r1)
            r11 = r3
            goto L_0x0083
        L_0x01d3:
            java.security.PublicKey r3 = r2.getCAPublicKey()     // Catch:{ CertPathReviewerException -> 0x01c5, Throwable -> 0x04d5 }
            goto L_0x01a5
        L_0x01d8:
            r1 = move-exception
        L_0x01d9:
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r5 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r6 = "CertPathReviewer.unknown"
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r8 = 0
            org.bouncycastle.i18n.filter.UntrustedInput r9 = new org.bouncycastle.i18n.filter.UntrustedInput
            java.lang.String r10 = r1.getMessage()
            r9.<init>(r10)
            r7[r8] = r9
            r8 = 1
            org.bouncycastle.i18n.filter.UntrustedInput r9 = new org.bouncycastle.i18n.filter.UntrustedInput
            r9.<init>(r1)
            r7[r8] = r9
            r2.<init>(r5, r6, r7)
            r0 = r18
            r0.addError(r2)
            r11 = r3
            goto L_0x0083
        L_0x0203:
            javax.security.auth.x500.X500Principal r1 = new javax.security.auth.x500.X500Principal     // Catch:{ IllegalArgumentException -> 0x020e }
            java.lang.String r3 = r11.getCAName()     // Catch:{ IllegalArgumentException -> 0x020e }
            r1.<init>(r3)     // Catch:{ IllegalArgumentException -> 0x020e }
            goto L_0x008f
        L_0x020e:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r3 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r5 = "CertPathReviewer.trustDNInvalid"
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r7 = 0
            org.bouncycastle.i18n.filter.UntrustedInput r8 = new org.bouncycastle.i18n.filter.UntrustedInput
            java.lang.String r9 = r11.getCAName()
            r8.<init>(r9)
            r6[r7] = r8
            r1.<init>(r3, r5, r6)
            r0 = r18
            r0.addError(r1)
            r3 = r4
            goto L_0x0090
        L_0x0231:
            java.security.PublicKey r2 = r11.getCAPublicKey()
            goto L_0x00bb
        L_0x0237:
            r4 = move-exception
            org.bouncycastle.i18n.ErrorBundle r4 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r5 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r6 = "CertPathReviewer.trustPubKeyError"
            r4.<init>(r5, r6)
            r0 = r18
            r0.addError(r4)
            goto L_0x00c5
        L_0x024a:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r7 = "CertPathReviewer.signatureNotVerified"
            r8 = 3
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r13 = 0
            java.lang.String r14 = r1.getMessage()
            r8[r13] = r14
            r13 = 1
            r8[r13] = r1
            r13 = 2
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            r8[r13] = r1
            r2.<init>(r4, r7, r8)
            r0 = r18
            r0.addError(r2, r9)
            goto L_0x00f1
        L_0x0275:
            boolean r1 = isSelfIssued(r3)
            if (r1 == 0) goto L_0x02c7
            java.security.PublicKey r1 = r3.getPublicKey()     // Catch:{ GeneralSecurityException -> 0x029c }
            r0 = r18
            java.security.cert.PKIXParameters r2 = r0.pkixParams     // Catch:{ GeneralSecurityException -> 0x029c }
            java.lang.String r2 = r2.getSigProvider()     // Catch:{ GeneralSecurityException -> 0x029c }
            org.bouncycastle.x509.CertPathValidatorUtilities.verifyX509Certificate(r3, r1, r2)     // Catch:{ GeneralSecurityException -> 0x029c }
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ GeneralSecurityException -> 0x029c }
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.rootKeyIsValidButNotATrustAnchor"
            r1.<init>(r2, r4)     // Catch:{ GeneralSecurityException -> 0x029c }
            r0 = r18
            r0.addError(r1, r9)     // Catch:{ GeneralSecurityException -> 0x029c }
            goto L_0x00f1
        L_0x029c:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r7 = "CertPathReviewer.signatureNotVerified"
            r8 = 3
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r13 = 0
            java.lang.String r14 = r1.getMessage()
            r8[r13] = r14
            r13 = 1
            r8[r13] = r1
            r13 = 2
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            r8[r13] = r1
            r2.<init>(r4, r7, r8)
            r0 = r18
            r0.addError(r2, r9)
            goto L_0x00f1
        L_0x02c7:
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.NoIssuerPublicKey"
            r1.<init>(r2, r4)
            org.bouncycastle.asn1.ASN1ObjectIdentifier r2 = org.bouncycastle.asn1.x509.X509Extensions.AuthorityKeyIdentifier
            java.lang.String r2 = r2.getId()
            byte[] r2 = r3.getExtensionValue(r2)
            if (r2 == 0) goto L_0x0333
            org.bouncycastle.asn1.ASN1Primitive r2 = org.bouncycastle.x509.extension.X509ExtensionUtil.fromExtensionValue(r2)     // Catch:{ IOException -> 0x04d2 }
            org.bouncycastle.asn1.x509.AuthorityKeyIdentifier r2 = org.bouncycastle.asn1.x509.AuthorityKeyIdentifier.getInstance(r2)     // Catch:{ IOException -> 0x04d2 }
            org.bouncycastle.asn1.x509.GeneralNames r4 = r2.getAuthorityCertIssuer()     // Catch:{ IOException -> 0x04d2 }
            if (r4 == 0) goto L_0x0333
            org.bouncycastle.asn1.x509.GeneralName[] r4 = r4.getNames()     // Catch:{ IOException -> 0x04d2 }
            r7 = 0
            r4 = r4[r7]     // Catch:{ IOException -> 0x04d2 }
            java.math.BigInteger r2 = r2.getAuthorityCertSerialNumber()     // Catch:{ IOException -> 0x04d2 }
            if (r2 == 0) goto L_0x0333
            r7 = 7
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ IOException -> 0x04d2 }
            r8 = 0
            org.bouncycastle.i18n.LocaleString r13 = new org.bouncycastle.i18n.LocaleString     // Catch:{ IOException -> 0x04d2 }
            java.lang.String r14 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r15 = "missingIssuer"
            r13.<init>(r14, r15)     // Catch:{ IOException -> 0x04d2 }
            r7[r8] = r13     // Catch:{ IOException -> 0x04d2 }
            r8 = 1
            java.lang.String r13 = " \""
            r7[r8] = r13     // Catch:{ IOException -> 0x04d2 }
            r8 = 2
            r7[r8] = r4     // Catch:{ IOException -> 0x04d2 }
            r4 = 3
            java.lang.String r8 = "\" "
            r7[r4] = r8     // Catch:{ IOException -> 0x04d2 }
            r4 = 4
            org.bouncycastle.i18n.LocaleString r8 = new org.bouncycastle.i18n.LocaleString     // Catch:{ IOException -> 0x04d2 }
            java.lang.String r13 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r14 = "missingSerial"
            r8.<init>(r13, r14)     // Catch:{ IOException -> 0x04d2 }
            r7[r4] = r8     // Catch:{ IOException -> 0x04d2 }
            r4 = 5
            java.lang.String r8 = " "
            r7[r4] = r8     // Catch:{ IOException -> 0x04d2 }
            r4 = 6
            r7[r4] = r2     // Catch:{ IOException -> 0x04d2 }
            r1.setExtraArguments(r7)     // Catch:{ IOException -> 0x04d2 }
        L_0x0333:
            r0 = r18
            r0.addError(r1, r9)
            goto L_0x00f1
        L_0x033a:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.certificateNotYetValid"
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r8 = 0
            org.bouncycastle.i18n.filter.TrustedInput r13 = new org.bouncycastle.i18n.filter.TrustedInput
            java.util.Date r14 = r3.getNotBefore()
            r13.<init>(r14)
            r7[r8] = r13
            r1.<init>(r2, r4, r7)
            r0 = r18
            r0.addError(r1, r9)
            goto L_0x00f8
        L_0x035c:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.certificateExpired"
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r8 = 0
            org.bouncycastle.i18n.filter.TrustedInput r13 = new org.bouncycastle.i18n.filter.TrustedInput
            java.util.Date r14 = r3.getNotAfter()
            r13.<init>(r14)
            r7[r8] = r13
            r1.<init>(r2, r4, r7)
            r0 = r18
            r0.addError(r1, r9)
            goto L_0x00f8
        L_0x037e:
            r2 = move-exception
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r7 = "CertPathReviewer.crlDistPtExtError"
            r2.<init>(r4, r7)
            r0 = r18
            r0.addError(r2, r9)
            goto L_0x010f
        L_0x0391:
            r4 = move-exception
            org.bouncycastle.i18n.ErrorBundle r4 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r7 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r8 = "CertPathReviewer.crlAuthInfoAccError"
            r4.<init>(r7, r8)
            r0 = r18
            r0.addError(r4, r9)
            goto L_0x011c
        L_0x03a4:
            java.util.Iterator r1 = r8.iterator()
        L_0x03a8:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x03ce
            org.bouncycastle.i18n.ErrorBundle r2 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r4 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r13 = "CertPathReviewer.ocspLocation"
            r14 = 1
            java.lang.Object[] r14 = new java.lang.Object[r14]
            r15 = 0
            org.bouncycastle.i18n.filter.UntrustedUrlInput r16 = new org.bouncycastle.i18n.filter.UntrustedUrlInput
            java.lang.Object r17 = r1.next()
            r16.<init>(r17)
            r14[r15] = r16
            r2.<init>(r4, r13, r14)
            r0 = r18
            r0.addNotification(r2, r9)
            goto L_0x03a8
        L_0x03ce:
            r0 = r18
            java.security.cert.PKIXParameters r2 = r0.pkixParams     // Catch:{ CertPathReviewerException -> 0x0485 }
            r0 = r18
            java.util.Date r4 = r0.validDate     // Catch:{ CertPathReviewerException -> 0x0485 }
            r1 = r18
            r1.checkRevocation(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ CertPathReviewerException -> 0x0485 }
        L_0x03db:
            if (r10 == 0) goto L_0x040c
            javax.security.auth.x500.X500Principal r1 = r3.getIssuerX500Principal()
            boolean r1 = r1.equals(r10)
            if (r1 != 0) goto L_0x040c
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.certWrongIssuer"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r7 = 0
            java.lang.String r8 = r10.getName()
            r5[r7] = r8
            r7 = 1
            javax.security.auth.x500.X500Principal r8 = r3.getIssuerX500Principal()
            java.lang.String r8 = r8.getName()
            r5[r7] = r8
            r1.<init>(r2, r4, r5)
            r0 = r18
            r0.addError(r1, r9)
        L_0x040c:
            r0 = r18
            int r1 = r0.n
            if (r12 == r1) goto L_0x0468
            if (r3 == 0) goto L_0x042b
            int r1 = r3.getVersion()
            r2 = 1
            if (r1 != r2) goto L_0x042b
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.noCACert"
            r1.<init>(r2, r4)
            r0 = r18
            r0.addError(r1, r9)
        L_0x042b:
            java.lang.String r1 = org.bouncycastle.x509.PKIXCertPathReviewer.BASIC_CONSTRAINTS     // Catch:{ AnnotatedException -> 0x04a2 }
            org.bouncycastle.asn1.ASN1Primitive r1 = getExtensionValue(r3, r1)     // Catch:{ AnnotatedException -> 0x04a2 }
            org.bouncycastle.asn1.x509.BasicConstraints r1 = org.bouncycastle.asn1.x509.BasicConstraints.getInstance(r1)     // Catch:{ AnnotatedException -> 0x04a2 }
            if (r1 == 0) goto L_0x0491
            boolean r1 = r1.isCA()     // Catch:{ AnnotatedException -> 0x04a2 }
            if (r1 != 0) goto L_0x044d
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ AnnotatedException -> 0x04a2 }
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.noCACert"
            r1.<init>(r2, r4)     // Catch:{ AnnotatedException -> 0x04a2 }
            r0 = r18
            r0.addError(r1, r9)     // Catch:{ AnnotatedException -> 0x04a2 }
        L_0x044d:
            boolean[] r1 = r3.getKeyUsage()
            if (r1 == 0) goto L_0x0468
            r2 = 5
            boolean r1 = r1[r2]
            if (r1 != 0) goto L_0x0468
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.noCertSign"
            r1.<init>(r2, r4)
            r0 = r18
            r0.addError(r1, r9)
        L_0x0468:
            javax.security.auth.x500.X500Principal r2 = r3.getSubjectX500Principal()
            r0 = r18
            java.util.List r1 = r0.certs     // Catch:{ CertPathValidatorException -> 0x04b4 }
            java.security.PublicKey r1 = getNextWorkingKey(r1, r9)     // Catch:{ CertPathValidatorException -> 0x04b4 }
            org.bouncycastle.asn1.x509.AlgorithmIdentifier r4 = getAlgorithmIdentifier(r1)     // Catch:{ CertPathValidatorException -> 0x04d0 }
            r4.getAlgorithm()     // Catch:{ CertPathValidatorException -> 0x04d0 }
            r4.getParameters()     // Catch:{ CertPathValidatorException -> 0x04d0 }
        L_0x047e:
            int r9 = r9 + -1
            r5 = r3
            r10 = r2
            r6 = r1
            goto L_0x00d2
        L_0x0485:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r1 = r1.getErrorMessage()
            r0 = r18
            r0.addError(r1, r9)
            goto L_0x03db
        L_0x0491:
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle     // Catch:{ AnnotatedException -> 0x04a2 }
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.noBasicConstraints"
            r1.<init>(r2, r4)     // Catch:{ AnnotatedException -> 0x04a2 }
            r0 = r18
            r0.addError(r1, r9)     // Catch:{ AnnotatedException -> 0x04a2 }
            goto L_0x044d
        L_0x04a2:
            r1 = move-exception
            org.bouncycastle.i18n.ErrorBundle r1 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r2 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r4 = "CertPathReviewer.errorProcesingBC"
            r1.<init>(r2, r4)
            r0 = r18
            r0.addError(r1, r9)
            goto L_0x044d
        L_0x04b4:
            r1 = move-exception
            r1 = r6
        L_0x04b6:
            org.bouncycastle.i18n.ErrorBundle r4 = new org.bouncycastle.i18n.ErrorBundle
            java.lang.String r5 = "org.bouncycastle.x509.CertPathReviewerMessages"
            java.lang.String r6 = "CertPathReviewer.pubKeyError"
            r4.<init>(r5, r6)
            r0 = r18
            r0.addError(r4, r9)
            goto L_0x047e
        L_0x04c7:
            r0 = r18
            r0.trustAnchor = r11
            r0 = r18
            r0.subjectPublicKey = r6
            return
        L_0x04d0:
            r4 = move-exception
            goto L_0x04b6
        L_0x04d2:
            r2 = move-exception
            goto L_0x0333
        L_0x04d5:
            r1 = move-exception
            r3 = r2
            goto L_0x01d9
        L_0x04d9:
            r1 = move-exception
            goto L_0x01c7
        L_0x04dc:
            r1 = move-exception
            goto L_0x0082
        L_0x04df:
            r3 = r4
            goto L_0x00ad
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.x509.PKIXCertPathReviewer.checkSignatures():void");
    }

    private X509CRL getCRL(String str) throws CertPathReviewerException {
        try {
            URL url = new URL(str);
            if (!url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
                return null;
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                return (X509CRL) CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME).generateCRL(httpURLConnection.getInputStream());
            }
            throw new Exception(httpURLConnection.getResponseMessage());
        } catch (Exception e) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.loadCrlDistPointError", new Object[]{new UntrustedInput(str), e.getMessage(), e, e.getClass().getName()}));
        }
    }

    private boolean processQcStatements(X509Certificate x509Certificate, int i) {
        boolean z = false;
        try {
            ASN1Sequence aSN1Sequence = (ASN1Sequence) getExtensionValue(x509Certificate, QC_STATEMENT);
            for (int i2 = 0; i2 < aSN1Sequence.size(); i2++) {
                QCStatement instance = QCStatement.getInstance(aSN1Sequence.getObjectAt(i2));
                if (QCStatement.id_etsi_qcs_QcCompliance.equals(instance.getStatementId())) {
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcEuCompliance"), i);
                } else if (!QCStatement.id_qcs_pkixQCSyntax_v1.equals(instance.getStatementId())) {
                    if (QCStatement.id_etsi_qcs_QcSSCD.equals(instance.getStatementId())) {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcSSCD"), i);
                    } else if (QCStatement.id_etsi_qcs_LimiteValue.equals(instance.getStatementId())) {
                        MonetaryValue instance2 = MonetaryValue.getInstance(instance.getStatementInfo());
                        instance2.getCurrency();
                        double doubleValue = instance2.getAmount().doubleValue() * Math.pow(10.0d, instance2.getExponent().doubleValue());
                        addNotification(instance2.getCurrency().isAlphabetic() ? new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcLimitValueAlpha", new Object[]{instance2.getCurrency().getAlphabetic(), new TrustedInput(new Double(doubleValue)), instance2}) : new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcLimitValueNum", new Object[]{Integers.valueOf(instance2.getCurrency().getNumeric()), new TrustedInput(new Double(doubleValue)), instance2}), i);
                    } else {
                        addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcUnknownStatement", new Object[]{instance.getStatementId(), new UntrustedInput(instance)}), i);
                        z = true;
                    }
                }
            }
            return !z;
        } catch (AnnotatedException e) {
            addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.QcStatementExtError"), i);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void addError(ErrorBundle errorBundle) {
        this.errors[0].add(errorBundle);
    }

    /* access modifiers changed from: protected */
    public void addError(ErrorBundle errorBundle, int i) {
        if (i < -1 || i >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.errors[i + 1].add(errorBundle);
    }

    /* access modifiers changed from: protected */
    public void addNotification(ErrorBundle errorBundle) {
        this.notifications[0].add(errorBundle);
    }

    /* access modifiers changed from: protected */
    public void addNotification(ErrorBundle errorBundle, int i) {
        if (i < -1 || i >= this.n) {
            throw new IndexOutOfBoundsException();
        }
        this.notifications[i + 1].add(errorBundle);
    }

    /* access modifiers changed from: protected */
    public void checkCRLs(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, int i) throws CertPathReviewerException {
        Iterator it2;
        X509CRL x509crl;
        boolean z;
        X509CRL x509crl2;
        boolean z2;
        boolean z3;
        boolean[] keyUsage;
        boolean z4;
        X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            x509CRLStoreSelector.addIssuerName(getEncodedIssuerPrincipal(x509Certificate).getEncoded());
            x509CRLStoreSelector.setCertificateChecking(x509Certificate);
            try {
                Set findCRLs = CRL_UTIL.findCRLs(x509CRLStoreSelector, pKIXParameters);
                it2 = findCRLs.iterator();
                if (findCRLs.isEmpty()) {
                    ArrayList arrayList = new ArrayList();
                    for (X509CRL x509crl3 : CRL_UTIL.findCRLs(new X509CRLStoreSelector(), pKIXParameters)) {
                        arrayList.add(x509crl3.getIssuerX500Principal());
                    }
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlInCertstore", new Object[]{new UntrustedInput(x509CRLStoreSelector.getIssuerNames()), new UntrustedInput(arrayList), Integers.valueOf(arrayList.size())}), i);
                }
            } catch (AnnotatedException e) {
                addError(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError", new Object[]{e.getCause().getMessage(), e.getCause(), e.getCause().getClass().getName()}), i);
                it2 = new ArrayList().iterator();
            }
            X509CRL x509crl4 = null;
            while (true) {
                if (!it2.hasNext()) {
                    x509crl = x509crl4;
                    z = false;
                    break;
                }
                x509crl4 = (X509CRL) it2.next();
                if (x509crl4.getNextUpdate() == null || pKIXParameters.getDate().before(x509crl4.getNextUpdate())) {
                    z = true;
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localValidCRL", new Object[]{new TrustedInput(x509crl4.getThisUpdate()), new TrustedInput(x509crl4.getNextUpdate())}), i);
                    x509crl = x509crl4;
                } else {
                    addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localInvalidCRL", new Object[]{new TrustedInput(x509crl4.getThisUpdate()), new TrustedInput(x509crl4.getNextUpdate())}), i);
                }
            }
            z = true;
            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.localValidCRL", new Object[]{new TrustedInput(x509crl4.getThisUpdate()), new TrustedInput(x509crl4.getNextUpdate())}), i);
            x509crl = x509crl4;
            if (!z) {
                Iterator it3 = vector.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    try {
                        String str = (String) it3.next();
                        x509crl2 = getCRL(str);
                        if (x509crl2 == null) {
                            continue;
                        } else if (!x509Certificate.getIssuerX500Principal().equals(x509crl2.getIssuerX500Principal())) {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineCRLWrongCA", new Object[]{new UntrustedInput(x509crl2.getIssuerX500Principal().getName()), new UntrustedInput(x509Certificate.getIssuerX500Principal().getName()), new UntrustedUrlInput(str)}), i);
                        } else if (x509crl2.getNextUpdate() == null || this.pkixParams.getDate().before(x509crl2.getNextUpdate())) {
                            z2 = true;
                            try {
                                addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineValidCRL", new Object[]{new TrustedInput(x509crl2.getThisUpdate()), new TrustedInput(x509crl2.getNextUpdate()), new UntrustedUrlInput(str)}), i);
                                break;
                            } catch (CertPathReviewerException e2) {
                                e = e2;
                                z4 = true;
                            }
                        } else {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.onlineInvalidCRL", new Object[]{new TrustedInput(x509crl2.getThisUpdate()), new TrustedInput(x509crl2.getNextUpdate()), new UntrustedUrlInput(str)}), i);
                        }
                    } catch (CertPathReviewerException e3) {
                        e = e3;
                        z4 = z;
                    }
                }
            }
            x509crl2 = x509crl;
            z2 = z;
            if (x509crl2 != null) {
                if (x509Certificate2 != null && (keyUsage = x509Certificate2.getKeyUsage()) != null && (keyUsage.length < 7 || !keyUsage[6])) {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noCrlSigningPermited"));
                } else if (publicKey != null) {
                    try {
                        x509crl2.verify(publicKey, BouncyCastleProvider.PROVIDER_NAME);
                        X509CRLEntry revokedCertificate = x509crl2.getRevokedCertificate(x509Certificate.getSerialNumber());
                        if (revokedCertificate != null) {
                            String str2 = null;
                            if (revokedCertificate.hasExtensions()) {
                                try {
                                    ASN1Enumerated instance = ASN1Enumerated.getInstance(getExtensionValue(revokedCertificate, X509Extensions.ReasonCode.getId()));
                                    if (instance != null) {
                                        str2 = crlReasons[instance.getValue().intValue()];
                                    }
                                } catch (AnnotatedException e4) {
                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlReasonExtError"), e4);
                                }
                            }
                            if (str2 == null) {
                                str2 = crlReasons[7];
                            }
                            LocaleString localeString = new LocaleString(RESOURCE_NAME, str2);
                            if (!date.before(revokedCertificate.getRevocationDate())) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.certRevoked", new Object[]{new TrustedInput(revokedCertificate.getRevocationDate()), localeString}));
                            }
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.revokedAfterValidation", new Object[]{new TrustedInput(revokedCertificate.getRevocationDate()), localeString}), i);
                        } else {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.notRevoked"), i);
                        }
                        if (x509crl2.getNextUpdate() != null && x509crl2.getNextUpdate().before(this.pkixParams.getDate())) {
                            addNotification(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlUpdateAvailable", new Object[]{new TrustedInput(x509crl2.getNextUpdate())}), i);
                        }
                        try {
                            ASN1Primitive extensionValue = getExtensionValue(x509crl2, ISSUING_DISTRIBUTION_POINT);
                            try {
                                ASN1Primitive extensionValue2 = getExtensionValue(x509crl2, DELTA_CRL_INDICATOR);
                                if (extensionValue2 != null) {
                                    X509CRLStoreSelector x509CRLStoreSelector2 = new X509CRLStoreSelector();
                                    try {
                                        x509CRLStoreSelector2.addIssuerName(getIssuerPrincipal(x509crl2).getEncoded());
                                        x509CRLStoreSelector2.setMinCRLNumber(((ASN1Integer) extensionValue2).getPositiveValue());
                                        try {
                                            x509CRLStoreSelector2.setMaxCRLNumber(((ASN1Integer) getExtensionValue(x509crl2, CRL_NUMBER)).getPositiveValue().subtract(BigInteger.valueOf(1)));
                                            try {
                                                Iterator it4 = CRL_UTIL.findCRLs(x509CRLStoreSelector2, pKIXParameters).iterator();
                                                while (true) {
                                                    if (!it4.hasNext()) {
                                                        z3 = false;
                                                        break;
                                                    }
                                                    try {
                                                        ASN1Primitive extensionValue3 = getExtensionValue((X509CRL) it4.next(), ISSUING_DISTRIBUTION_POINT);
                                                        if (extensionValue == null) {
                                                            if (extensionValue3 == null) {
                                                                z3 = true;
                                                                break;
                                                            }
                                                        } else if (extensionValue.equals(extensionValue3)) {
                                                            z3 = true;
                                                            break;
                                                        }
                                                    } catch (AnnotatedException e5) {
                                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"), e5);
                                                    }
                                                }
                                                if (!z3) {
                                                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noBaseCRL"));
                                                }
                                            } catch (AnnotatedException e6) {
                                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlExtractionError"), e6);
                                            }
                                        } catch (AnnotatedException e7) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNbrExtError"), e7);
                                        }
                                    } catch (IOException e8) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), e8);
                                    }
                                }
                                if (extensionValue != null) {
                                    IssuingDistributionPoint instance2 = IssuingDistributionPoint.getInstance(extensionValue);
                                    try {
                                        BasicConstraints instance3 = BasicConstraints.getInstance(getExtensionValue(x509Certificate, BASIC_CONSTRAINTS));
                                        if (instance2.onlyContainsUserCerts() && instance3 != null && instance3.isCA()) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyUserCert"));
                                        } else if (instance2.onlyContainsCACerts() && (instance3 == null || !instance3.isCA())) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyCaCert"));
                                        } else if (instance2.onlyContainsAttributeCerts()) {
                                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlOnlyAttrCert"));
                                        }
                                    } catch (AnnotatedException e9) {
                                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlBCExtError"), e9);
                                    }
                                }
                            } catch (AnnotatedException e10) {
                                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.deltaCrlExtError"));
                            }
                        } catch (AnnotatedException e11) {
                            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.distrPtExtError"));
                        }
                    } catch (Exception e12) {
                        throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlVerifyFailed"), e12);
                    }
                } else {
                    throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlNoIssuerPublicKey"));
                }
            }
            if (!z2) {
                throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.noValidCrlFound"));
            }
            return;
            addNotification(e.getErrorMessage(), i);
            z = z4;
        } catch (IOException e13) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.crlIssuerException"), e13);
        }
    }

    /* access modifiers changed from: protected */
    public void checkRevocation(PKIXParameters pKIXParameters, X509Certificate x509Certificate, Date date, X509Certificate x509Certificate2, PublicKey publicKey, Vector vector, Vector vector2, int i) throws CertPathReviewerException {
        checkCRLs(pKIXParameters, x509Certificate, date, x509Certificate2, publicKey, vector, i);
    }

    /* access modifiers changed from: protected */
    public void doChecks() {
        if (!this.initialized) {
            throw new IllegalStateException("Object not initialized. Call init() first.");
        } else if (this.notifications == null) {
            this.notifications = new List[(this.n + 1)];
            this.errors = new List[(this.n + 1)];
            for (int i = 0; i < this.notifications.length; i++) {
                this.notifications[i] = new ArrayList();
                this.errors[i] = new ArrayList();
            }
            checkSignatures();
            checkNameConstraints();
            checkPathLength();
            checkPolicy();
            checkCriticalExtensions();
        }
    }

    /* access modifiers changed from: protected */
    public Vector getCRLDistUrls(CRLDistPoint cRLDistPoint) {
        DistributionPoint[] distributionPoints;
        Vector vector = new Vector();
        if (cRLDistPoint != null) {
            for (DistributionPoint distributionPoint : cRLDistPoint.getDistributionPoints()) {
                DistributionPointName distributionPoint2 = distributionPoint.getDistributionPoint();
                if (distributionPoint2.getType() == 0) {
                    GeneralName[] names = GeneralNames.getInstance(distributionPoint2.getName()).getNames();
                    for (int i = 0; i < names.length; i++) {
                        if (names[i].getTagNo() == 6) {
                            vector.add(((DERIA5String) names[i].getName()).getString());
                        }
                    }
                }
            }
        }
        return vector;
    }

    public CertPath getCertPath() {
        return this.certPath;
    }

    public int getCertPathSize() {
        return this.n;
    }

    public List getErrors(int i) {
        doChecks();
        return this.errors[i + 1];
    }

    public List[] getErrors() {
        doChecks();
        return this.errors;
    }

    public List getNotifications(int i) {
        doChecks();
        return this.notifications[i + 1];
    }

    public List[] getNotifications() {
        doChecks();
        return this.notifications;
    }

    /* access modifiers changed from: protected */
    public Vector getOCSPUrls(AuthorityInformationAccess authorityInformationAccess) {
        Vector vector = new Vector();
        if (authorityInformationAccess != null) {
            AccessDescription[] accessDescriptions = authorityInformationAccess.getAccessDescriptions();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= accessDescriptions.length) {
                    break;
                }
                if (accessDescriptions[i2].getAccessMethod().equals(AccessDescription.id_ad_ocsp)) {
                    GeneralName accessLocation = accessDescriptions[i2].getAccessLocation();
                    if (accessLocation.getTagNo() == 6) {
                        vector.add(((DERIA5String) accessLocation.getName()).getString());
                    }
                }
                i = i2 + 1;
            }
        }
        return vector;
    }

    public PolicyNode getPolicyTree() {
        doChecks();
        return this.policyTree;
    }

    public PublicKey getSubjectPublicKey() {
        doChecks();
        return this.subjectPublicKey;
    }

    public TrustAnchor getTrustAnchor() {
        doChecks();
        return this.trustAnchor;
    }

    /* access modifiers changed from: protected */
    public Collection getTrustAnchors(X509Certificate x509Certificate, Set set) throws CertPathReviewerException {
        ArrayList arrayList = new ArrayList();
        Iterator it2 = set.iterator();
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(getEncodedIssuerPrincipal(x509Certificate).getEncoded());
            byte[] extensionValue = x509Certificate.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
            if (extensionValue != null) {
                AuthorityKeyIdentifier instance = AuthorityKeyIdentifier.getInstance(ASN1Primitive.fromByteArray(((ASN1OctetString) ASN1Primitive.fromByteArray(extensionValue)).getOctets()));
                x509CertSelector.setSerialNumber(instance.getAuthorityCertSerialNumber());
                byte[] keyIdentifier = instance.getKeyIdentifier();
                if (keyIdentifier != null) {
                    x509CertSelector.setSubjectKeyIdentifier(new DEROctetString(keyIdentifier).getEncoded());
                }
            }
            while (it2.hasNext()) {
                TrustAnchor trustAnchor2 = (TrustAnchor) it2.next();
                if (trustAnchor2.getTrustedCert() != null) {
                    if (x509CertSelector.match(trustAnchor2.getTrustedCert())) {
                        arrayList.add(trustAnchor2);
                    }
                } else if (!(trustAnchor2.getCAName() == null || trustAnchor2.getCAPublicKey() == null || !getEncodedIssuerPrincipal(x509Certificate).equals(new X500Principal(trustAnchor2.getCAName())))) {
                    arrayList.add(trustAnchor2);
                }
            }
            return arrayList;
        } catch (IOException e) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.trustAnchorIssuerError"));
        }
    }

    public void init(CertPath certPath2, PKIXParameters pKIXParameters) throws CertPathReviewerException {
        if (this.initialized) {
            throw new IllegalStateException("object is already initialized!");
        }
        this.initialized = true;
        if (certPath2 == null) {
            throw new NullPointerException("certPath was null");
        }
        this.certPath = certPath2;
        this.certs = certPath2.getCertificates();
        this.n = this.certs.size();
        if (this.certs.isEmpty()) {
            throw new CertPathReviewerException(new ErrorBundle(RESOURCE_NAME, "CertPathReviewer.emptyCertPath"));
        }
        this.pkixParams = (PKIXParameters) pKIXParameters.clone();
        this.validDate = getValidDate(this.pkixParams);
        this.notifications = null;
        this.errors = null;
        this.trustAnchor = null;
        this.subjectPublicKey = null;
        this.policyTree = null;
    }

    public boolean isValidCertPath() {
        doChecks();
        for (int i = 0; i < this.errors.length; i++) {
            if (!this.errors[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
