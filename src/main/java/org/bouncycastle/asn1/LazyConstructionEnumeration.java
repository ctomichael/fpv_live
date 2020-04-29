package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

class LazyConstructionEnumeration implements Enumeration {
    private ASN1InputStream aIn;
    private Object nextObj = readObject();

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.asn1.ASN1InputStream.<init>(byte[], boolean):void
     arg types: [byte[], int]
     candidates:
      org.bouncycastle.asn1.ASN1InputStream.<init>(java.io.InputStream, int):void
      org.bouncycastle.asn1.ASN1InputStream.<init>(java.io.InputStream, boolean):void
      org.bouncycastle.asn1.ASN1InputStream.<init>(byte[], boolean):void */
    public LazyConstructionEnumeration(byte[] bArr) {
        this.aIn = new ASN1InputStream(bArr, true);
    }

    private Object readObject() {
        try {
            return this.aIn.readObject();
        } catch (IOException e) {
            throw new ASN1ParsingException("malformed DER construction: " + e, e);
        }
    }

    public boolean hasMoreElements() {
        return this.nextObj != null;
    }

    public Object nextElement() {
        Object obj = this.nextObj;
        this.nextObj = readObject();
        return obj;
    }
}
