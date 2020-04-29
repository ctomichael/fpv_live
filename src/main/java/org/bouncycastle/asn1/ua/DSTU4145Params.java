package org.bouncycastle.asn1.ua;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.Arrays;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public class DSTU4145Params extends ASN1Object {
    private static final byte[] DEFAULT_DKE = {-87, MessagePack.Code.FIXEXT4, -21, 69, -15, 60, 112, -126, Byte.MIN_VALUE, MessagePack.Code.BIN8, -106, 123, 35, 31, 94, -83, -10, 88, -21, -92, MessagePack.Code.NIL, TarHeader.LF_CONTIG, 41, 29, 56, MessagePack.Code.STR8, 107, -16, 37, MessagePack.Code.FLOAT32, 78, 23, -8, -23, 114, Draft_75.CR, MessagePack.Code.BIN32, 21, -76, 58, 40, -105, 95, 11, MessagePack.Code.NEVER_USED, MessagePack.Code.MAP16, -93, 100, 56, -75, 100, -22, 44, 23, -97, MessagePack.Code.INT8, 18, 62, 109, -72, -6, MessagePack.Code.BIN16, 121, 4};
    private byte[] dke = DEFAULT_DKE;
    private DSTU4145ECBinary ecbinary;
    private ASN1ObjectIdentifier namedCurve;

    public DSTU4145Params(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.namedCurve = aSN1ObjectIdentifier;
    }

    public DSTU4145Params(ASN1ObjectIdentifier aSN1ObjectIdentifier, byte[] bArr) {
        this.namedCurve = aSN1ObjectIdentifier;
        this.dke = Arrays.clone(bArr);
    }

    public DSTU4145Params(DSTU4145ECBinary dSTU4145ECBinary) {
        this.ecbinary = dSTU4145ECBinary;
    }

    public static byte[] getDefaultDKE() {
        return DEFAULT_DKE;
    }

    public static DSTU4145Params getInstance(Object obj) {
        if (obj instanceof DSTU4145Params) {
            return (DSTU4145Params) obj;
        }
        if (obj != null) {
            ASN1Sequence instance = ASN1Sequence.getInstance(obj);
            DSTU4145Params dSTU4145Params = instance.getObjectAt(0) instanceof ASN1ObjectIdentifier ? new DSTU4145Params(ASN1ObjectIdentifier.getInstance(instance.getObjectAt(0))) : new DSTU4145Params(DSTU4145ECBinary.getInstance(instance.getObjectAt(0)));
            if (instance.size() == 2) {
                dSTU4145Params.dke = ASN1OctetString.getInstance(instance.getObjectAt(1)).getOctets();
                if (dSTU4145Params.dke.length != DEFAULT_DKE.length) {
                    throw new IllegalArgumentException("object parse error");
                }
            }
            return dSTU4145Params;
        }
        throw new IllegalArgumentException("object parse error");
    }

    public byte[] getDKE() {
        return this.dke;
    }

    public DSTU4145ECBinary getECBinary() {
        return this.ecbinary;
    }

    public ASN1ObjectIdentifier getNamedCurve() {
        return this.namedCurve;
    }

    public boolean isNamedCurve() {
        return this.namedCurve != null;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.namedCurve != null) {
            aSN1EncodableVector.add(this.namedCurve);
        } else {
            aSN1EncodableVector.add(this.ecbinary);
        }
        if (!Arrays.areEqual(this.dke, DEFAULT_DKE)) {
            aSN1EncodableVector.add(new DEROctetString(this.dke));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
