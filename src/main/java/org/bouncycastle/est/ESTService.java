package org.bouncycastle.est;

import dji.component.accountcenter.IMemberProtocol;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cmc.CMCException;
import org.bouncycastle.cmc.SimplePKIResponse;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;

public class ESTService {
    protected static final String CACERTS = "/cacerts";
    protected static final String CSRATTRS = "/csrattrs";
    protected static final String FULLCMC = "/fullcmc";
    protected static final String SERVERGEN = "/serverkeygen";
    protected static final String SIMPLE_ENROLL = "/simpleenroll";
    protected static final String SIMPLE_REENROLL = "/simplereenroll";
    protected static final Set<String> illegalParts = new HashSet();
    private static final Pattern pathInvalid = Pattern.compile("^[0-9a-zA-Z_\\-.~!$&'()*+,;=]+");
    private final ESTClientProvider clientProvider;
    private final String server;

    static {
        illegalParts.add(CACERTS.substring(1));
        illegalParts.add(SIMPLE_ENROLL.substring(1));
        illegalParts.add(SIMPLE_REENROLL.substring(1));
        illegalParts.add(FULLCMC.substring(1));
        illegalParts.add(SERVERGEN.substring(1));
        illegalParts.add(CSRATTRS.substring(1));
    }

    ESTService(String str, String str2, ESTClientProvider eSTClientProvider) {
        String verifyServer = verifyServer(str);
        if (str2 != null) {
            this.server = "https://" + verifyServer + "/.well-known/est/" + verifyLabel(str2);
        } else {
            this.server = "https://" + verifyServer + "/.well-known/est";
        }
        this.clientProvider = eSTClientProvider;
    }

    /* access modifiers changed from: private */
    public String annotateRequest(byte[] bArr) {
        int i = 0;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        do {
            if (i + 48 < bArr.length) {
                printWriter.print(Base64.toBase64String(bArr, i, 48));
                i += 48;
            } else {
                printWriter.print(Base64.toBase64String(bArr, i, bArr.length - i));
                i = bArr.length;
            }
            printWriter.print(10);
        } while (i < bArr.length);
        printWriter.flush();
        return stringWriter.toString();
    }

    public static X509CertificateHolder[] storeToArray(Store<X509CertificateHolder> store) {
        return storeToArray(store, null);
    }

    public static X509CertificateHolder[] storeToArray(Store<X509CertificateHolder> store, Selector<X509CertificateHolder> selector) {
        Collection<X509CertificateHolder> matches = store.getMatches(selector);
        return (X509CertificateHolder[]) matches.toArray(new X509CertificateHolder[matches.size()]);
    }

    private String verifyLabel(String str) {
        while (str.endsWith(IMemberProtocol.PARAM_SEPERATOR) && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        while (str.startsWith(IMemberProtocol.PARAM_SEPERATOR) && str.length() > 0) {
            str = str.substring(1);
        }
        if (str.length() == 0) {
            throw new IllegalArgumentException("Label set but after trimming '/' is not zero length string.");
        } else if (!pathInvalid.matcher(str).matches()) {
            throw new IllegalArgumentException("Server path " + str + " contains invalid characters");
        } else if (!illegalParts.contains(str)) {
            return str;
        } else {
            throw new IllegalArgumentException("Label " + str + " is a reserved path segment.");
        }
    }

    private String verifyServer(String str) {
        while (str.endsWith(IMemberProtocol.PARAM_SEPERATOR) && str.length() > 0) {
            try {
                str = str.substring(0, str.length() - 1);
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                    throw ((IllegalArgumentException) e);
                }
                throw new IllegalArgumentException("Scheme and host is invalid: " + e.getMessage(), e);
            }
        }
        if (str.contains("://")) {
            throw new IllegalArgumentException("Server contains scheme, must only be <dnsname/ipaddress>:port, https:// will be added arbitrarily.");
        }
        URL url = new URL("https://" + str);
        if (url.getPath().length() == 0 || url.getPath().equals(IMemberProtocol.PARAM_SEPERATOR)) {
            return str;
        }
        throw new IllegalArgumentException("Server contains path, must only be <dnsname/ipaddress>:port, a path of '/.well-known/est/<label>' will be added arbitrarily.");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x014c, code lost:
        r0 = th;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00bd A[SYNTHETIC, Splitter:B:23:0x00bd] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x014c A[Catch:{ Throwable -> 0x0116, all -> 0x014c, Throwable -> 0x00b0, all -> 0x014c }, ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x0036] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.bouncycastle.est.CACertsResponse getCACerts() throws java.lang.Exception {
        /*
            r9 = this;
            r6 = 0
            java.net.URL r8 = new java.net.URL     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            r0.<init>()     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            java.lang.String r1 = r9.server     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            java.lang.String r1 = "/cacerts"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            r8.<init>(r0)     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            org.bouncycastle.est.ESTClientProvider r0 = r9.clientProvider     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            org.bouncycastle.est.ESTClient r0 = r0.makeClient()     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            org.bouncycastle.est.ESTRequestBuilder r1 = new org.bouncycastle.est.ESTRequestBuilder     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            java.lang.String r2 = "GET"
            r1.<init>(r2, r8)     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            org.bouncycastle.est.ESTRequestBuilder r1 = r1.withClient(r0)     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            org.bouncycastle.est.ESTRequest r3 = r1.build()     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            org.bouncycastle.est.ESTResponse r7 = r0.doRequest(r3)     // Catch:{ Throwable -> 0x01b5, all -> 0x01b1 }
            int r0 = r7.getStatusCode()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r1 = 200(0xc8, float:2.8E-43)
            if (r0 != r1) goto L_0x014f
            java.lang.String r0 = "application/pkcs7-mime"
            org.bouncycastle.est.HttpUtil$Headers r1 = r7.getHeaders()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r2 = "Content-Type"
            java.lang.String r1 = r1.getFirstValue(r2)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            boolean r0 = r0.equals(r1)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            if (r0 != 0) goto L_0x00c5
            org.bouncycastle.est.HttpUtil$Headers r0 = r7.getHeaders()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r1 = "Content-Type"
            java.lang.String r0 = r0.getFirstValue(r1)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            if (r0 == 0) goto L_0x00c1
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r0.<init>()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r1 = " got "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            org.bouncycastle.est.HttpUtil$Headers r1 = r7.getHeaders()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r2 = "Content-Type"
            java.lang.String r1 = r1.getFirstValue(r2)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
        L_0x007e:
            org.bouncycastle.est.ESTException r1 = new org.bouncycastle.est.ESTException     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r2.<init>()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r3 = "Response : "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r3 = r8.toString()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r3 = "Expecting application/pkcs7-mime "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r2 = 0
            int r3 = r7.getStatusCode()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.io.InputStream r4 = r7.getInputStream()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r1.<init>(r0, r2, r3, r4)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            throw r1     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
        L_0x00b0:
            r0 = move-exception
            r6 = r7
        L_0x00b2:
            boolean r1 = r0 instanceof org.bouncycastle.est.ESTException     // Catch:{ all -> 0x00b9 }
            if (r1 == 0) goto L_0x017e
            org.bouncycastle.est.ESTException r0 = (org.bouncycastle.est.ESTException) r0     // Catch:{ all -> 0x00b9 }
            throw r0     // Catch:{ all -> 0x00b9 }
        L_0x00b9:
            r0 = move-exception
            r7 = r6
        L_0x00bb:
            if (r7 == 0) goto L_0x00c0
            r7.close()     // Catch:{ Exception -> 0x01ad }
        L_0x00c0:
            throw r0
        L_0x00c1:
            java.lang.String r0 = " but was not present."
            goto L_0x007e
        L_0x00c5:
            java.lang.Long r0 = r7.getContentLength()     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            if (r0 == 0) goto L_0x01bf
            java.lang.Long r0 = r7.getContentLength()     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            long r0 = r0.longValue()     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x01bf
            org.bouncycastle.asn1.ASN1InputStream r0 = new org.bouncycastle.asn1.ASN1InputStream     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            java.io.InputStream r1 = r7.getInputStream()     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            org.bouncycastle.cmc.SimplePKIResponse r2 = new org.bouncycastle.cmc.SimplePKIResponse     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            org.bouncycastle.asn1.ASN1Primitive r0 = r0.readObject()     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            org.bouncycastle.asn1.ASN1Sequence r0 = (org.bouncycastle.asn1.ASN1Sequence) r0     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            org.bouncycastle.asn1.cms.ContentInfo r0 = org.bouncycastle.asn1.cms.ContentInfo.getInstance(r0)     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            r2.<init>(r0)     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            org.bouncycastle.util.Store r1 = r2.getCertificates()     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
            org.bouncycastle.util.Store r0 = r2.getCRLs()     // Catch:{ Throwable -> 0x0116, all -> 0x014c }
        L_0x00f9:
            r2 = r0
        L_0x00fa:
            org.bouncycastle.est.CACertsResponse r0 = new org.bouncycastle.est.CACertsResponse     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            org.bouncycastle.est.Source r4 = r7.getSource()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            org.bouncycastle.est.ESTClientProvider r5 = r9.clientProvider     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            boolean r5 = r5.isTrusted()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r0.<init>(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            if (r7 == 0) goto L_0x01b8
            r7.close()     // Catch:{ Exception -> 0x01aa }
            r1 = r6
        L_0x010f:
            if (r1 == 0) goto L_0x01b0
            boolean r0 = r1 instanceof org.bouncycastle.est.ESTException
            if (r0 == 0) goto L_0x0188
            throw r1
        L_0x0116:
            r0 = move-exception
            org.bouncycastle.est.ESTException r1 = new org.bouncycastle.est.ESTException     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r2.<init>()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r3 = "Decoding CACerts: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r3 = r8.toString()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r3 = " "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r3 = r0.getMessage()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            int r3 = r7.getStatusCode()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.io.InputStream r4 = r7.getInputStream()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r1.<init>(r2, r0, r3, r4)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            throw r1     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
        L_0x014c:
            r0 = move-exception
            goto L_0x00bb
        L_0x014f:
            int r0 = r7.getStatusCode()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r1 = 204(0xcc, float:2.86E-43)
            if (r0 == r1) goto L_0x01bb
            org.bouncycastle.est.ESTException r0 = new org.bouncycastle.est.ESTException     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r1.<init>()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r2 = "Get CACerts: "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r2 = r8.toString()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r2 = 0
            int r3 = r7.getStatusCode()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            java.io.InputStream r4 = r7.getInputStream()     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            r0.<init>(r1, r2, r3, r4)     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
            throw r0     // Catch:{ Throwable -> 0x00b0, all -> 0x014c }
        L_0x017e:
            org.bouncycastle.est.ESTException r1 = new org.bouncycastle.est.ESTException     // Catch:{ all -> 0x00b9 }
            java.lang.String r2 = r0.getMessage()     // Catch:{ all -> 0x00b9 }
            r1.<init>(r2, r0)     // Catch:{ all -> 0x00b9 }
            throw r1     // Catch:{ all -> 0x00b9 }
        L_0x0188:
            org.bouncycastle.est.ESTException r0 = new org.bouncycastle.est.ESTException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Get CACerts: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r8.toString()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            int r3 = r7.getStatusCode()
            r0.<init>(r2, r1, r3, r6)
            throw r0
        L_0x01aa:
            r1 = move-exception
            goto L_0x010f
        L_0x01ad:
            r1 = move-exception
            goto L_0x00c0
        L_0x01b0:
            return r0
        L_0x01b1:
            r0 = move-exception
            r7 = r6
            goto L_0x00bb
        L_0x01b5:
            r0 = move-exception
            goto L_0x00b2
        L_0x01b8:
            r1 = r6
            goto L_0x010f
        L_0x01bb:
            r2 = r6
            r1 = r6
            goto L_0x00fa
        L_0x01bf:
            r0 = r6
            r1 = r6
            goto L_0x00f9
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.est.ESTService.getCACerts():org.bouncycastle.est.CACertsResponse");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00fd, code lost:
        r0 = th;
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0086 A[SYNTHETIC, Splitter:B:21:0x0086] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00fd A[ExcHandler: all (th java.lang.Throwable), Splitter:B:6:0x0047] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.bouncycastle.est.CSRRequestResponse getCSRAttributes() throws org.bouncycastle.est.ESTException {
        /*
            r8 = this;
            r2 = 0
            org.bouncycastle.est.ESTClientProvider r0 = r8.clientProvider
            boolean r0 = r0.isTrusted()
            if (r0 != 0) goto L_0x0012
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "No trust anchors."
            r0.<init>(r1)
            throw r0
        L_0x0012:
            java.net.URL r4 = new java.net.URL     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            r0.<init>()     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            java.lang.String r1 = r8.server     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            java.lang.String r1 = "/csrattrs"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            r4.<init>(r0)     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            org.bouncycastle.est.ESTClientProvider r0 = r8.clientProvider     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            org.bouncycastle.est.ESTClient r0 = r0.makeClient()     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            org.bouncycastle.est.ESTRequestBuilder r1 = new org.bouncycastle.est.ESTRequestBuilder     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            java.lang.String r3 = "GET"
            r1.<init>(r3, r4)     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            org.bouncycastle.est.ESTRequestBuilder r1 = r1.withClient(r0)     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            org.bouncycastle.est.ESTRequest r1 = r1.build()     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            org.bouncycastle.est.ESTResponse r3 = r0.doRequest(r1)     // Catch:{ Throwable -> 0x012e, all -> 0x012a }
            int r0 = r3.getStatusCode()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            switch(r0) {
                case 200: goto L_0x008a;
                case 204: goto L_0x00ff;
                case 404: goto L_0x0101;
                default: goto L_0x004e;
            }     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
        L_0x004e:
            org.bouncycastle.est.ESTException r0 = new org.bouncycastle.est.ESTException     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            r2.<init>()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r4 = "CSR Attribute request: "
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.net.URL r1 = r1.getURL()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            r2 = 0
            int r4 = r3.getStatusCode()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.io.InputStream r5 = r3.getInputStream()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            r0.<init>(r1, r2, r4, r5)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            throw r0     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
        L_0x0079:
            r0 = move-exception
            r2 = r3
        L_0x007b:
            boolean r1 = r0 instanceof org.bouncycastle.est.ESTException     // Catch:{ all -> 0x0082 }
            if (r1 == 0) goto L_0x0103
            org.bouncycastle.est.ESTException r0 = (org.bouncycastle.est.ESTException) r0     // Catch:{ all -> 0x0082 }
            throw r0     // Catch:{ all -> 0x0082 }
        L_0x0082:
            r0 = move-exception
            r3 = r2
        L_0x0084:
            if (r3 == 0) goto L_0x0089
            r3.close()     // Catch:{ Exception -> 0x0127 }
        L_0x0089:
            throw r0
        L_0x008a:
            java.lang.Long r0 = r3.getContentLength()     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            if (r0 == 0) goto L_0x0133
            java.lang.Long r0 = r3.getContentLength()     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            long r0 = r0.longValue()     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            r6 = 0
            int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x0133
            org.bouncycastle.asn1.ASN1InputStream r0 = new org.bouncycastle.asn1.ASN1InputStream     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            java.io.InputStream r1 = r3.getInputStream()     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            org.bouncycastle.asn1.ASN1Primitive r0 = r0.readObject()     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            org.bouncycastle.asn1.ASN1Sequence r0 = (org.bouncycastle.asn1.ASN1Sequence) r0     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            org.bouncycastle.est.CSRAttributesResponse r1 = new org.bouncycastle.est.CSRAttributesResponse     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            org.bouncycastle.asn1.est.CsrAttrs r0 = org.bouncycastle.asn1.est.CsrAttrs.getInstance(r0)     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            r1.<init>(r0)     // Catch:{ Throwable -> 0x00c7, all -> 0x00fd }
            r0 = r1
        L_0x00b7:
            r1 = r0
        L_0x00b8:
            if (r3 == 0) goto L_0x0131
            r3.close()     // Catch:{ Exception -> 0x0125 }
            r0 = r2
        L_0x00be:
            if (r0 == 0) goto L_0x011b
            boolean r1 = r0 instanceof org.bouncycastle.est.ESTException
            if (r1 == 0) goto L_0x010d
            org.bouncycastle.est.ESTException r0 = (org.bouncycastle.est.ESTException) r0
            throw r0
        L_0x00c7:
            r0 = move-exception
            org.bouncycastle.est.ESTException r1 = new org.bouncycastle.est.ESTException     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            r2.<init>()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r5 = "Decoding CACerts: "
            java.lang.StringBuilder r2 = r2.append(r5)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r4 = " "
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r4 = r0.getMessage()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            int r4 = r3.getStatusCode()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            java.io.InputStream r5 = r3.getInputStream()     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            r1.<init>(r2, r0, r4, r5)     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
            throw r1     // Catch:{ Throwable -> 0x0079, all -> 0x00fd }
        L_0x00fd:
            r0 = move-exception
            goto L_0x0084
        L_0x00ff:
            r1 = r2
            goto L_0x00b8
        L_0x0101:
            r1 = r2
            goto L_0x00b8
        L_0x0103:
            org.bouncycastle.est.ESTException r1 = new org.bouncycastle.est.ESTException     // Catch:{ all -> 0x0082 }
            java.lang.String r3 = r0.getMessage()     // Catch:{ all -> 0x0082 }
            r1.<init>(r3, r0)     // Catch:{ all -> 0x0082 }
            throw r1     // Catch:{ all -> 0x0082 }
        L_0x010d:
            org.bouncycastle.est.ESTException r1 = new org.bouncycastle.est.ESTException
            java.lang.String r4 = r0.getMessage()
            int r3 = r3.getStatusCode()
            r1.<init>(r4, r0, r3, r2)
            throw r1
        L_0x011b:
            org.bouncycastle.est.CSRRequestResponse r0 = new org.bouncycastle.est.CSRRequestResponse
            org.bouncycastle.est.Source r2 = r3.getSource()
            r0.<init>(r1, r2)
            return r0
        L_0x0125:
            r0 = move-exception
            goto L_0x00be
        L_0x0127:
            r1 = move-exception
            goto L_0x0089
        L_0x012a:
            r0 = move-exception
            r3 = r2
            goto L_0x0084
        L_0x012e:
            r0 = move-exception
            goto L_0x007b
        L_0x0131:
            r0 = r2
            goto L_0x00be
        L_0x0133:
            r0 = r2
            goto L_0x00b7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.est.ESTService.getCSRAttributes():org.bouncycastle.est.CSRRequestResponse");
    }

    /* access modifiers changed from: protected */
    public EnrollmentResponse handleEnrollResponse(ESTResponse eSTResponse) throws IOException {
        long time;
        ESTRequest originalRequest = eSTResponse.getOriginalRequest();
        if (eSTResponse.getStatusCode() == 202) {
            String header = eSTResponse.getHeader("Retry-After");
            if (header == null) {
                throw new ESTException("Got Status 202 but not Retry-After header from: " + originalRequest.getURL().toString());
            }
            try {
                time = System.currentTimeMillis() + (Long.parseLong(header) * 1000);
            } catch (NumberFormatException e) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    time = simpleDateFormat.parse(header).getTime();
                } catch (Exception e2) {
                    throw new ESTException("Unable to parse Retry-After header:" + originalRequest.getURL().toString() + " " + e2.getMessage(), null, eSTResponse.getStatusCode(), eSTResponse.getInputStream());
                }
            }
            return new EnrollmentResponse(null, time, originalRequest, eSTResponse.getSource());
        } else if (eSTResponse.getStatusCode() == 200) {
            try {
                return new EnrollmentResponse(new SimplePKIResponse(ContentInfo.getInstance(new ASN1InputStream(eSTResponse.getInputStream()).readObject())).getCertificates(), -1, null, eSTResponse.getSource());
            } catch (CMCException e3) {
                throw new ESTException(e3.getMessage(), e3.getCause());
            }
        } else {
            throw new ESTException("Simple Enroll: " + originalRequest.getURL().toString(), null, eSTResponse.getStatusCode(), eSTResponse.getInputStream());
        }
    }

    public EnrollmentResponse simpleEnroll(EnrollmentResponse enrollmentResponse) throws Exception {
        if (!this.clientProvider.isTrusted()) {
            throw new IllegalStateException("No trust anchors.");
        }
        ESTResponse eSTResponse = null;
        try {
            ESTClient makeClient = this.clientProvider.makeClient();
            eSTResponse = makeClient.doRequest(new ESTRequestBuilder(enrollmentResponse.getRequestToRetry()).withClient(makeClient).build());
            EnrollmentResponse handleEnrollResponse = handleEnrollResponse(eSTResponse);
            if (eSTResponse != null) {
                eSTResponse.close();
            }
            return handleEnrollResponse;
        } catch (Throwable th) {
            if (eSTResponse != null) {
                eSTResponse.close();
            }
            throw th;
        }
    }

    public EnrollmentResponse simpleEnroll(boolean z, PKCS10CertificationRequest pKCS10CertificationRequest, ESTAuth eSTAuth) throws IOException {
        if (!this.clientProvider.isTrusted()) {
            throw new IllegalStateException("No trust anchors.");
        }
        ESTResponse eSTResponse = null;
        try {
            byte[] bytes = annotateRequest(pKCS10CertificationRequest.getEncoded()).getBytes();
            URL url = new URL(this.server + (z ? SIMPLE_REENROLL : SIMPLE_ENROLL));
            ESTClient makeClient = this.clientProvider.makeClient();
            ESTRequestBuilder withClient = new ESTRequestBuilder("POST", url).withData(bytes).withClient(makeClient);
            withClient.addHeader("Content-Type", "application/pkcs10");
            withClient.addHeader("Content-Length", "" + bytes.length);
            withClient.addHeader("Content-Transfer-Encoding", "base64");
            if (eSTAuth != null) {
                eSTAuth.applyAuth(withClient);
            }
            ESTResponse doRequest = makeClient.doRequest(withClient.build());
            EnrollmentResponse handleEnrollResponse = handleEnrollResponse(doRequest);
            if (doRequest != null) {
                doRequest.close();
            }
            return handleEnrollResponse;
        } catch (Throwable th) {
            if (eSTResponse != null) {
                eSTResponse.close();
            }
            throw th;
        }
    }

    public EnrollmentResponse simpleEnrollPoP(boolean z, final PKCS10CertificationRequestBuilder pKCS10CertificationRequestBuilder, final ContentSigner contentSigner, ESTAuth eSTAuth) throws IOException {
        if (!this.clientProvider.isTrusted()) {
            throw new IllegalStateException("No trust anchors.");
        }
        ESTResponse eSTResponse = null;
        try {
            URL url = new URL(this.server + (z ? SIMPLE_REENROLL : SIMPLE_ENROLL));
            ESTClient makeClient = this.clientProvider.makeClient();
            ESTRequestBuilder withConnectionListener = new ESTRequestBuilder("POST", url).withClient(makeClient).withConnectionListener(new ESTSourceConnectionListener() {
                /* class org.bouncycastle.est.ESTService.AnonymousClass1 */

                public ESTRequest onConnection(Source source, ESTRequest eSTRequest) throws IOException {
                    if (!(source instanceof TLSUniqueProvider) || !((TLSUniqueProvider) source).isTLSUniqueAvailable()) {
                        throw new IOException("Source does not supply TLS unique.");
                    }
                    PKCS10CertificationRequestBuilder pKCS10CertificationRequestBuilder = new PKCS10CertificationRequestBuilder(pKCS10CertificationRequestBuilder);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    pKCS10CertificationRequestBuilder.setAttribute(PKCSObjectIdentifiers.pkcs_9_at_challengePassword, new DERPrintableString(Base64.toBase64String(((TLSUniqueProvider) source).getTLSUnique())));
                    byteArrayOutputStream.write(ESTService.this.annotateRequest(pKCS10CertificationRequestBuilder.build(contentSigner).getEncoded()).getBytes());
                    byteArrayOutputStream.flush();
                    ESTRequestBuilder withData = new ESTRequestBuilder(eSTRequest).withData(byteArrayOutputStream.toByteArray());
                    withData.setHeader("Content-Type", "application/pkcs10");
                    withData.setHeader("Content-Transfer-Encoding", "base64");
                    withData.setHeader("Content-Length", Long.toString((long) byteArrayOutputStream.size()));
                    return withData.build();
                }
            });
            if (eSTAuth != null) {
                eSTAuth.applyAuth(withConnectionListener);
            }
            ESTResponse doRequest = makeClient.doRequest(withConnectionListener.build());
            EnrollmentResponse handleEnrollResponse = handleEnrollResponse(doRequest);
            if (doRequest != null) {
                doRequest.close();
            }
            return handleEnrollResponse;
        } catch (Throwable th) {
            if (eSTResponse != null) {
                eSTResponse.close();
            }
            throw th;
        }
    }
}
