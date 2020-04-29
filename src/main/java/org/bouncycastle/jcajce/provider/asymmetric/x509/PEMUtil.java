package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.util.encoders.Base64;

class PEMUtil {
    private final String _footer1;
    private final String _footer2;
    private final String _footer3;
    private final String _header1;
    private final String _header2;
    private final String _header3 = "-----BEGIN PKCS7-----";

    PEMUtil(String str) {
        this._header1 = "-----BEGIN " + str + "-----";
        this._header2 = "-----BEGIN X509 " + str + "-----";
        this._footer1 = "-----END " + str + "-----";
        this._footer2 = "-----END X509 " + str + "-----";
        this._footer3 = "-----END PKCS7-----";
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0025  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String readLine(java.io.InputStream r7) throws java.io.IOException {
        /*
            r6 = this;
            r5 = 13
            r4 = 10
            r3 = 1
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
        L_0x000a:
            int r1 = r7.read()
            if (r1 == r5) goto L_0x0019
            if (r1 == r4) goto L_0x0019
            if (r1 < 0) goto L_0x0019
            char r1 = (char) r1
            r0.append(r1)
            goto L_0x000a
        L_0x0019:
            if (r1 < 0) goto L_0x0021
            int r2 = r0.length()
            if (r2 == 0) goto L_0x000a
        L_0x0021:
            if (r1 >= 0) goto L_0x0025
            r0 = 0
        L_0x0024:
            return r0
        L_0x0025:
            if (r1 != r5) goto L_0x0038
            r7.mark(r3)
            int r1 = r7.read()
            if (r1 != r4) goto L_0x0033
            r7.mark(r3)
        L_0x0033:
            if (r1 <= 0) goto L_0x0038
            r7.reset()
        L_0x0038:
            java.lang.String r0 = r0.toString()
            goto L_0x0024
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.jcajce.provider.asymmetric.x509.PEMUtil.readLine(java.io.InputStream):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    public ASN1Sequence readPEMObject(InputStream inputStream) throws IOException {
        String readLine;
        StringBuffer stringBuffer = new StringBuffer();
        do {
            readLine = readLine(inputStream);
            if (readLine == null || readLine.startsWith(this._header1) || readLine.startsWith(this._header2)) {
            }
        } while (!readLine.startsWith(this._header3));
        while (true) {
            String readLine2 = readLine(inputStream);
            if (readLine2 != null && !readLine2.startsWith(this._footer1) && !readLine2.startsWith(this._footer2) && !readLine2.startsWith(this._footer3)) {
                stringBuffer.append(readLine2);
            }
        }
        if (stringBuffer.length() == 0) {
            return null;
        }
        try {
            return ASN1Sequence.getInstance(Base64.decode(stringBuffer.toString()));
        } catch (Exception e) {
            throw new IOException("malformed PEM data encountered");
        }
    }
}
