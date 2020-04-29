package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSet extends ASN1Set {
    public BERSet() {
    }

    public BERSet(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.asn1.ASN1Set.<init>(org.bouncycastle.asn1.ASN1EncodableVector, boolean):void
     arg types: [org.bouncycastle.asn1.ASN1EncodableVector, int]
     candidates:
      org.bouncycastle.asn1.ASN1Set.<init>(org.bouncycastle.asn1.ASN1Encodable[], boolean):void
      org.bouncycastle.asn1.ASN1Set.<init>(org.bouncycastle.asn1.ASN1EncodableVector, boolean):void */
    public BERSet(ASN1EncodableVector aSN1EncodableVector) {
        super(aSN1EncodableVector, false);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.asn1.ASN1Set.<init>(org.bouncycastle.asn1.ASN1Encodable[], boolean):void
     arg types: [org.bouncycastle.asn1.ASN1Encodable[], int]
     candidates:
      org.bouncycastle.asn1.ASN1Set.<init>(org.bouncycastle.asn1.ASN1EncodableVector, boolean):void
      org.bouncycastle.asn1.ASN1Set.<init>(org.bouncycastle.asn1.ASN1Encodable[], boolean):void */
    public BERSet(ASN1Encodable[] aSN1EncodableArr) {
        super(aSN1EncodableArr, false);
    }

    /* access modifiers changed from: package-private */
    public void encode(ASN1OutputStream aSN1OutputStream) throws IOException {
        aSN1OutputStream.write(49);
        aSN1OutputStream.write(128);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            aSN1OutputStream.writeObject((ASN1Encodable) objects.nextElement());
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }

    /* access modifiers changed from: package-private */
    public int encodedLength() throws IOException {
        int i = 0;
        Enumeration objects = getObjects();
        while (true) {
            int i2 = i;
            if (!objects.hasMoreElements()) {
                return i2 + 2 + 2;
            }
            i = ((ASN1Encodable) objects.nextElement()).toASN1Primitive().encodedLength() + i2;
        }
    }
}
