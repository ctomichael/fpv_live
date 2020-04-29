package org.bouncycastle.pqc.asn1;

import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.pqc.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.crypto.gmss.Treehash;

public class GMSSPrivateKey extends ASN1Object {
    private ASN1Primitive primitive;

    private GMSSPrivateKey(ASN1Sequence aSN1Sequence) {
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence) aSN1Sequence.getObjectAt(0);
        int[] iArr = new int[aSN1Sequence2.size()];
        for (int i = 0; i < aSN1Sequence2.size(); i++) {
            iArr[i] = checkBigIntegerInIntRange(aSN1Sequence2.getObjectAt(i));
        }
        ASN1Sequence aSN1Sequence3 = (ASN1Sequence) aSN1Sequence.getObjectAt(1);
        byte[][] bArr = new byte[aSN1Sequence3.size()][];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = ((DEROctetString) aSN1Sequence3.getObjectAt(i2)).getOctets();
        }
        ASN1Sequence aSN1Sequence4 = (ASN1Sequence) aSN1Sequence.getObjectAt(2);
        byte[][] bArr2 = new byte[aSN1Sequence4.size()][];
        for (int i3 = 0; i3 < bArr2.length; i3++) {
            bArr2[i3] = ((DEROctetString) aSN1Sequence4.getObjectAt(i3)).getOctets();
        }
        ASN1Sequence aSN1Sequence5 = (ASN1Sequence) aSN1Sequence.getObjectAt(3);
        byte[][][] bArr3 = new byte[aSN1Sequence5.size()][][];
        for (int i4 = 0; i4 < bArr3.length; i4++) {
            ASN1Sequence aSN1Sequence6 = (ASN1Sequence) aSN1Sequence5.getObjectAt(i4);
            bArr3[i4] = new byte[aSN1Sequence6.size()][];
            for (int i5 = 0; i5 < bArr3[i4].length; i5++) {
                bArr3[i4][i5] = ((DEROctetString) aSN1Sequence6.getObjectAt(i5)).getOctets();
            }
        }
        ASN1Sequence aSN1Sequence7 = (ASN1Sequence) aSN1Sequence.getObjectAt(4);
        byte[][][] bArr4 = new byte[aSN1Sequence7.size()][][];
        for (int i6 = 0; i6 < bArr4.length; i6++) {
            ASN1Sequence aSN1Sequence8 = (ASN1Sequence) aSN1Sequence7.getObjectAt(i6);
            bArr4[i6] = new byte[aSN1Sequence8.size()][];
            for (int i7 = 0; i7 < bArr4[i6].length; i7++) {
                bArr4[i6][i7] = ((DEROctetString) aSN1Sequence8.getObjectAt(i7)).getOctets();
            }
        }
        Treehash[][] treehashArr = new Treehash[((ASN1Sequence) aSN1Sequence.getObjectAt(5)).size()][];
    }

    public GMSSPrivateKey(int[] iArr, byte[][] bArr, byte[][] bArr2, byte[][][] bArr3, byte[][][] bArr4, Treehash[][] treehashArr, Treehash[][] treehashArr2, Vector[] vectorArr, Vector[] vectorArr2, Vector[][] vectorArr3, Vector[][] vectorArr4, byte[][][] bArr5, GMSSLeaf[] gMSSLeafArr, GMSSLeaf[] gMSSLeafArr2, GMSSLeaf[] gMSSLeafArr3, int[] iArr2, byte[][] bArr6, GMSSRootCalc[] gMSSRootCalcArr, byte[][] bArr7, GMSSRootSig[] gMSSRootSigArr, GMSSParameters gMSSParameters, AlgorithmIdentifier algorithmIdentifier) {
        this.primitive = encode(iArr, bArr, bArr2, bArr3, bArr4, bArr5, treehashArr, treehashArr2, vectorArr, vectorArr2, vectorArr3, vectorArr4, gMSSLeafArr, gMSSLeafArr2, gMSSLeafArr3, iArr2, bArr6, gMSSRootCalcArr, bArr7, gMSSRootSigArr, gMSSParameters, new AlgorithmIdentifier[]{algorithmIdentifier});
    }

    private static int checkBigIntegerInIntRange(ASN1Encodable aSN1Encodable) {
        BigInteger value = ((ASN1Integer) aSN1Encodable).getValue();
        if (value.compareTo(BigInteger.valueOf(2147483647L)) <= 0 && value.compareTo(BigInteger.valueOf(-2147483648L)) >= 0) {
            return value.intValue();
        }
        throw new IllegalArgumentException("BigInteger not in Range: " + value.toString());
    }

    private ASN1Primitive encode(int[] iArr, byte[][] bArr, byte[][] bArr2, byte[][][] bArr3, byte[][][] bArr4, byte[][][] bArr5, Treehash[][] treehashArr, Treehash[][] treehashArr2, Vector[] vectorArr, Vector[] vectorArr2, Vector[][] vectorArr3, Vector[][] vectorArr4, GMSSLeaf[] gMSSLeafArr, GMSSLeaf[] gMSSLeafArr2, GMSSLeaf[] gMSSLeafArr3, int[] iArr2, byte[][] bArr6, GMSSRootCalc[] gMSSRootCalcArr, byte[][] bArr7, GMSSRootSig[] gMSSRootSigArr, GMSSParameters gMSSParameters, AlgorithmIdentifier[] algorithmIdentifierArr) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i : iArr) {
            aSN1EncodableVector2.add(new ASN1Integer((long) i));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
        for (byte[] bArr8 : bArr) {
            aSN1EncodableVector3.add(new DEROctetString(bArr8));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector3));
        ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
        for (byte[] bArr9 : bArr2) {
            aSN1EncodableVector4.add(new DEROctetString(bArr9));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector4));
        ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
        for (int i2 = 0; i2 < bArr3.length; i2++) {
            for (int i3 = 0; i3 < bArr3[i2].length; i3++) {
                aSN1EncodableVector5.add(new DEROctetString(bArr3[i2][i3]));
            }
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector5));
            aSN1EncodableVector5 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector6));
        ASN1EncodableVector aSN1EncodableVector7 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector8 = new ASN1EncodableVector();
        for (int i4 = 0; i4 < bArr4.length; i4++) {
            for (int i5 = 0; i5 < bArr4[i4].length; i5++) {
                aSN1EncodableVector7.add(new DEROctetString(bArr4[i4][i5]));
            }
            aSN1EncodableVector8.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector8));
        ASN1EncodableVector aSN1EncodableVector9 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector10 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector11 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector12 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector13 = new ASN1EncodableVector();
        for (int i6 = 0; i6 < treehashArr.length; i6++) {
            for (int i7 = 0; i7 < treehashArr[i6].length; i7++) {
                aSN1EncodableVector11.add(new DERSequence(algorithmIdentifierArr[0]));
                int i8 = treehashArr[i6][i7].getStatInt()[1];
                aSN1EncodableVector12.add(new DEROctetString(treehashArr[i6][i7].getStatByte()[0]));
                aSN1EncodableVector12.add(new DEROctetString(treehashArr[i6][i7].getStatByte()[1]));
                aSN1EncodableVector12.add(new DEROctetString(treehashArr[i6][i7].getStatByte()[2]));
                for (int i9 = 0; i9 < i8; i9++) {
                    aSN1EncodableVector12.add(new DEROctetString(treehashArr[i6][i7].getStatByte()[i9 + 3]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
                aSN1EncodableVector12 = new ASN1EncodableVector();
                aSN1EncodableVector13.add(new ASN1Integer((long) treehashArr[i6][i7].getStatInt()[0]));
                aSN1EncodableVector13.add(new ASN1Integer((long) i8));
                aSN1EncodableVector13.add(new ASN1Integer((long) treehashArr[i6][i7].getStatInt()[2]));
                aSN1EncodableVector13.add(new ASN1Integer((long) treehashArr[i6][i7].getStatInt()[3]));
                aSN1EncodableVector13.add(new ASN1Integer((long) treehashArr[i6][i7].getStatInt()[4]));
                aSN1EncodableVector13.add(new ASN1Integer((long) treehashArr[i6][i7].getStatInt()[5]));
                for (int i10 = 0; i10 < i8; i10++) {
                    aSN1EncodableVector13.add(new ASN1Integer((long) treehashArr[i6][i7].getStatInt()[i10 + 6]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
                aSN1EncodableVector13 = new ASN1EncodableVector();
                aSN1EncodableVector10.add(new DERSequence(aSN1EncodableVector11));
                aSN1EncodableVector11 = new ASN1EncodableVector();
            }
            aSN1EncodableVector9.add(new DERSequence(aSN1EncodableVector10));
            aSN1EncodableVector10 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector9));
        ASN1EncodableVector aSN1EncodableVector14 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector15 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector16 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector17 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector18 = new ASN1EncodableVector();
        for (int i11 = 0; i11 < treehashArr2.length; i11++) {
            for (int i12 = 0; i12 < treehashArr2[i11].length; i12++) {
                aSN1EncodableVector16.add(new DERSequence(algorithmIdentifierArr[0]));
                int i13 = treehashArr2[i11][i12].getStatInt()[1];
                aSN1EncodableVector17.add(new DEROctetString(treehashArr2[i11][i12].getStatByte()[0]));
                aSN1EncodableVector17.add(new DEROctetString(treehashArr2[i11][i12].getStatByte()[1]));
                aSN1EncodableVector17.add(new DEROctetString(treehashArr2[i11][i12].getStatByte()[2]));
                for (int i14 = 0; i14 < i13; i14++) {
                    aSN1EncodableVector17.add(new DEROctetString(treehashArr2[i11][i12].getStatByte()[i14 + 3]));
                }
                aSN1EncodableVector16.add(new DERSequence(aSN1EncodableVector17));
                aSN1EncodableVector17 = new ASN1EncodableVector();
                aSN1EncodableVector18.add(new ASN1Integer((long) treehashArr2[i11][i12].getStatInt()[0]));
                aSN1EncodableVector18.add(new ASN1Integer((long) i13));
                aSN1EncodableVector18.add(new ASN1Integer((long) treehashArr2[i11][i12].getStatInt()[2]));
                aSN1EncodableVector18.add(new ASN1Integer((long) treehashArr2[i11][i12].getStatInt()[3]));
                aSN1EncodableVector18.add(new ASN1Integer((long) treehashArr2[i11][i12].getStatInt()[4]));
                aSN1EncodableVector18.add(new ASN1Integer((long) treehashArr2[i11][i12].getStatInt()[5]));
                for (int i15 = 0; i15 < i13; i15++) {
                    aSN1EncodableVector18.add(new ASN1Integer((long) treehashArr2[i11][i12].getStatInt()[i15 + 6]));
                }
                aSN1EncodableVector16.add(new DERSequence(aSN1EncodableVector18));
                aSN1EncodableVector18 = new ASN1EncodableVector();
                aSN1EncodableVector15.add(new DERSequence(aSN1EncodableVector16));
                aSN1EncodableVector16 = new ASN1EncodableVector();
            }
            aSN1EncodableVector14.add(new DERSequence(new DERSequence(aSN1EncodableVector15)));
            aSN1EncodableVector15 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector14));
        ASN1EncodableVector aSN1EncodableVector19 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector20 = new ASN1EncodableVector();
        for (int i16 = 0; i16 < bArr5.length; i16++) {
            for (int i17 = 0; i17 < bArr5[i16].length; i17++) {
                aSN1EncodableVector19.add(new DEROctetString(bArr5[i16][i17]));
            }
            aSN1EncodableVector20.add(new DERSequence(aSN1EncodableVector19));
            aSN1EncodableVector19 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector20));
        ASN1EncodableVector aSN1EncodableVector21 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector22 = new ASN1EncodableVector();
        int i18 = 0;
        while (true) {
            int i19 = i18;
            if (i19 >= vectorArr.length) {
                break;
            }
            int i20 = 0;
            while (true) {
                int i21 = i20;
                if (i21 >= vectorArr[i19].size()) {
                    break;
                }
                aSN1EncodableVector21.add(new DEROctetString((byte[]) vectorArr[i19].elementAt(i21)));
                i20 = i21 + 1;
            }
            aSN1EncodableVector22.add(new DERSequence(aSN1EncodableVector21));
            aSN1EncodableVector21 = new ASN1EncodableVector();
            i18 = i19 + 1;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector22));
        ASN1EncodableVector aSN1EncodableVector23 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector24 = new ASN1EncodableVector();
        int i22 = 0;
        while (true) {
            int i23 = i22;
            if (i23 >= vectorArr2.length) {
                break;
            }
            int i24 = 0;
            while (true) {
                int i25 = i24;
                if (i25 >= vectorArr2[i23].size()) {
                    break;
                }
                aSN1EncodableVector23.add(new DEROctetString((byte[]) vectorArr2[i23].elementAt(i25)));
                i24 = i25 + 1;
            }
            aSN1EncodableVector24.add(new DERSequence(aSN1EncodableVector23));
            aSN1EncodableVector23 = new ASN1EncodableVector();
            i22 = i23 + 1;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector24));
        ASN1EncodableVector aSN1EncodableVector25 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector26 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector27 = new ASN1EncodableVector();
        int i26 = 0;
        while (true) {
            int i27 = i26;
            if (i27 >= vectorArr3.length) {
                break;
            }
            int i28 = 0;
            while (true) {
                int i29 = i28;
                if (i29 >= vectorArr3[i27].length) {
                    break;
                }
                int i30 = 0;
                while (true) {
                    int i31 = i30;
                    if (i31 >= vectorArr3[i27][i29].size()) {
                        break;
                    }
                    aSN1EncodableVector25.add(new DEROctetString((byte[]) vectorArr3[i27][i29].elementAt(i31)));
                    i30 = i31 + 1;
                }
                aSN1EncodableVector26.add(new DERSequence(aSN1EncodableVector25));
                aSN1EncodableVector25 = new ASN1EncodableVector();
                i28 = i29 + 1;
            }
            aSN1EncodableVector27.add(new DERSequence(aSN1EncodableVector26));
            aSN1EncodableVector26 = new ASN1EncodableVector();
            i26 = i27 + 1;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector27));
        ASN1EncodableVector aSN1EncodableVector28 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector29 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector30 = new ASN1EncodableVector();
        int i32 = 0;
        while (true) {
            int i33 = i32;
            if (i33 >= vectorArr4.length) {
                break;
            }
            int i34 = 0;
            while (true) {
                int i35 = i34;
                if (i35 >= vectorArr4[i33].length) {
                    break;
                }
                int i36 = 0;
                while (true) {
                    int i37 = i36;
                    if (i37 >= vectorArr4[i33][i35].size()) {
                        break;
                    }
                    aSN1EncodableVector28.add(new DEROctetString((byte[]) vectorArr4[i33][i35].elementAt(i37)));
                    i36 = i37 + 1;
                }
                aSN1EncodableVector29.add(new DERSequence(aSN1EncodableVector28));
                aSN1EncodableVector28 = new ASN1EncodableVector();
                i34 = i35 + 1;
            }
            aSN1EncodableVector30.add(new DERSequence(aSN1EncodableVector29));
            aSN1EncodableVector29 = new ASN1EncodableVector();
            i32 = i33 + 1;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector30));
        ASN1EncodableVector aSN1EncodableVector31 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector32 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector33 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector34 = new ASN1EncodableVector();
        for (int i38 = 0; i38 < gMSSLeafArr.length; i38++) {
            aSN1EncodableVector32.add(new DERSequence(algorithmIdentifierArr[0]));
            byte[][] statByte = gMSSLeafArr[i38].getStatByte();
            aSN1EncodableVector33.add(new DEROctetString(statByte[0]));
            aSN1EncodableVector33.add(new DEROctetString(statByte[1]));
            aSN1EncodableVector33.add(new DEROctetString(statByte[2]));
            aSN1EncodableVector33.add(new DEROctetString(statByte[3]));
            aSN1EncodableVector32.add(new DERSequence(aSN1EncodableVector33));
            aSN1EncodableVector33 = new ASN1EncodableVector();
            int[] statInt = gMSSLeafArr[i38].getStatInt();
            aSN1EncodableVector34.add(new ASN1Integer((long) statInt[0]));
            aSN1EncodableVector34.add(new ASN1Integer((long) statInt[1]));
            aSN1EncodableVector34.add(new ASN1Integer((long) statInt[2]));
            aSN1EncodableVector34.add(new ASN1Integer((long) statInt[3]));
            aSN1EncodableVector32.add(new DERSequence(aSN1EncodableVector34));
            aSN1EncodableVector34 = new ASN1EncodableVector();
            aSN1EncodableVector31.add(new DERSequence(aSN1EncodableVector32));
            aSN1EncodableVector32 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector31));
        ASN1EncodableVector aSN1EncodableVector35 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector36 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector37 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector38 = new ASN1EncodableVector();
        for (int i39 = 0; i39 < gMSSLeafArr2.length; i39++) {
            aSN1EncodableVector36.add(new DERSequence(algorithmIdentifierArr[0]));
            byte[][] statByte2 = gMSSLeafArr2[i39].getStatByte();
            aSN1EncodableVector37.add(new DEROctetString(statByte2[0]));
            aSN1EncodableVector37.add(new DEROctetString(statByte2[1]));
            aSN1EncodableVector37.add(new DEROctetString(statByte2[2]));
            aSN1EncodableVector37.add(new DEROctetString(statByte2[3]));
            aSN1EncodableVector36.add(new DERSequence(aSN1EncodableVector37));
            aSN1EncodableVector37 = new ASN1EncodableVector();
            int[] statInt2 = gMSSLeafArr2[i39].getStatInt();
            aSN1EncodableVector38.add(new ASN1Integer((long) statInt2[0]));
            aSN1EncodableVector38.add(new ASN1Integer((long) statInt2[1]));
            aSN1EncodableVector38.add(new ASN1Integer((long) statInt2[2]));
            aSN1EncodableVector38.add(new ASN1Integer((long) statInt2[3]));
            aSN1EncodableVector36.add(new DERSequence(aSN1EncodableVector38));
            aSN1EncodableVector38 = new ASN1EncodableVector();
            aSN1EncodableVector35.add(new DERSequence(aSN1EncodableVector36));
            aSN1EncodableVector36 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector35));
        ASN1EncodableVector aSN1EncodableVector39 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector40 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector41 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector42 = new ASN1EncodableVector();
        for (int i40 = 0; i40 < gMSSLeafArr3.length; i40++) {
            aSN1EncodableVector40.add(new DERSequence(algorithmIdentifierArr[0]));
            byte[][] statByte3 = gMSSLeafArr3[i40].getStatByte();
            aSN1EncodableVector41.add(new DEROctetString(statByte3[0]));
            aSN1EncodableVector41.add(new DEROctetString(statByte3[1]));
            aSN1EncodableVector41.add(new DEROctetString(statByte3[2]));
            aSN1EncodableVector41.add(new DEROctetString(statByte3[3]));
            aSN1EncodableVector40.add(new DERSequence(aSN1EncodableVector41));
            aSN1EncodableVector41 = new ASN1EncodableVector();
            int[] statInt3 = gMSSLeafArr3[i40].getStatInt();
            aSN1EncodableVector42.add(new ASN1Integer((long) statInt3[0]));
            aSN1EncodableVector42.add(new ASN1Integer((long) statInt3[1]));
            aSN1EncodableVector42.add(new ASN1Integer((long) statInt3[2]));
            aSN1EncodableVector42.add(new ASN1Integer((long) statInt3[3]));
            aSN1EncodableVector40.add(new DERSequence(aSN1EncodableVector42));
            aSN1EncodableVector42 = new ASN1EncodableVector();
            aSN1EncodableVector39.add(new DERSequence(aSN1EncodableVector40));
            aSN1EncodableVector40 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector39));
        ASN1EncodableVector aSN1EncodableVector43 = new ASN1EncodableVector();
        for (int i41 : iArr2) {
            aSN1EncodableVector43.add(new ASN1Integer((long) i41));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector43));
        ASN1EncodableVector aSN1EncodableVector44 = new ASN1EncodableVector();
        for (byte[] bArr10 : bArr6) {
            aSN1EncodableVector44.add(new DEROctetString(bArr10));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector44));
        ASN1EncodableVector aSN1EncodableVector45 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector46 = new ASN1EncodableVector();
        new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector47 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector48 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector49 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector50 = new ASN1EncodableVector();
        int i42 = 0;
        while (true) {
            int i43 = i42;
            if (i43 >= gMSSRootCalcArr.length) {
                break;
            }
            aSN1EncodableVector46.add(new DERSequence(algorithmIdentifierArr[0]));
            new ASN1EncodableVector();
            int i44 = gMSSRootCalcArr[i43].getStatInt()[0];
            int i45 = gMSSRootCalcArr[i43].getStatInt()[7];
            aSN1EncodableVector47.add(new DEROctetString(gMSSRootCalcArr[i43].getStatByte()[0]));
            for (int i46 = 0; i46 < i44; i46++) {
                aSN1EncodableVector47.add(new DEROctetString(gMSSRootCalcArr[i43].getStatByte()[i46 + 1]));
            }
            for (int i47 = 0; i47 < i45; i47++) {
                aSN1EncodableVector47.add(new DEROctetString(gMSSRootCalcArr[i43].getStatByte()[i44 + 1 + i47]));
            }
            aSN1EncodableVector46.add(new DERSequence(aSN1EncodableVector47));
            aSN1EncodableVector47 = new ASN1EncodableVector();
            aSN1EncodableVector48.add(new ASN1Integer((long) i44));
            aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[1]));
            aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[2]));
            aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[3]));
            aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[4]));
            aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[5]));
            aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[6]));
            aSN1EncodableVector48.add(new ASN1Integer((long) i45));
            for (int i48 = 0; i48 < i44; i48++) {
                aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[i48 + 8]));
            }
            for (int i49 = 0; i49 < i45; i49++) {
                aSN1EncodableVector48.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getStatInt()[i44 + 8 + i49]));
            }
            aSN1EncodableVector46.add(new DERSequence(aSN1EncodableVector48));
            aSN1EncodableVector48 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector51 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector52 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector53 = new ASN1EncodableVector();
            if (gMSSRootCalcArr[i43].getTreehash() != null) {
                for (int i50 = 0; i50 < gMSSRootCalcArr[i43].getTreehash().length; i50++) {
                    aSN1EncodableVector51.add(new DERSequence(algorithmIdentifierArr[0]));
                    int i51 = gMSSRootCalcArr[i43].getTreehash()[i50].getStatInt()[1];
                    aSN1EncodableVector52.add(new DEROctetString(gMSSRootCalcArr[i43].getTreehash()[i50].getStatByte()[0]));
                    aSN1EncodableVector52.add(new DEROctetString(gMSSRootCalcArr[i43].getTreehash()[i50].getStatByte()[1]));
                    aSN1EncodableVector52.add(new DEROctetString(gMSSRootCalcArr[i43].getTreehash()[i50].getStatByte()[2]));
                    for (int i52 = 0; i52 < i51; i52++) {
                        aSN1EncodableVector52.add(new DEROctetString(gMSSRootCalcArr[i43].getTreehash()[i50].getStatByte()[i52 + 3]));
                    }
                    aSN1EncodableVector51.add(new DERSequence(aSN1EncodableVector52));
                    aSN1EncodableVector52 = new ASN1EncodableVector();
                    aSN1EncodableVector53.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getTreehash()[i50].getStatInt()[0]));
                    aSN1EncodableVector53.add(new ASN1Integer((long) i51));
                    aSN1EncodableVector53.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getTreehash()[i50].getStatInt()[2]));
                    aSN1EncodableVector53.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getTreehash()[i50].getStatInt()[3]));
                    aSN1EncodableVector53.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getTreehash()[i50].getStatInt()[4]));
                    aSN1EncodableVector53.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getTreehash()[i50].getStatInt()[5]));
                    for (int i53 = 0; i53 < i51; i53++) {
                        aSN1EncodableVector53.add(new ASN1Integer((long) gMSSRootCalcArr[i43].getTreehash()[i50].getStatInt()[i53 + 6]));
                    }
                    aSN1EncodableVector51.add(new DERSequence(aSN1EncodableVector53));
                    aSN1EncodableVector53 = new ASN1EncodableVector();
                    aSN1EncodableVector49.add(new DERSequence(aSN1EncodableVector51));
                    aSN1EncodableVector51 = new ASN1EncodableVector();
                }
            }
            aSN1EncodableVector46.add(new DERSequence(aSN1EncodableVector49));
            aSN1EncodableVector49 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector54 = new ASN1EncodableVector();
            if (gMSSRootCalcArr[i43].getRetain() != null) {
                int i54 = 0;
                while (true) {
                    int i55 = i54;
                    if (i55 >= gMSSRootCalcArr[i43].getRetain().length) {
                        break;
                    }
                    int i56 = 0;
                    while (true) {
                        int i57 = i56;
                        if (i57 >= gMSSRootCalcArr[i43].getRetain()[i55].size()) {
                            break;
                        }
                        aSN1EncodableVector54.add(new DEROctetString((byte[]) gMSSRootCalcArr[i43].getRetain()[i55].elementAt(i57)));
                        i56 = i57 + 1;
                    }
                    aSN1EncodableVector50.add(new DERSequence(aSN1EncodableVector54));
                    aSN1EncodableVector54 = new ASN1EncodableVector();
                    i54 = i55 + 1;
                }
            }
            aSN1EncodableVector46.add(new DERSequence(aSN1EncodableVector50));
            aSN1EncodableVector50 = new ASN1EncodableVector();
            aSN1EncodableVector45.add(new DERSequence(aSN1EncodableVector46));
            aSN1EncodableVector46 = new ASN1EncodableVector();
            i42 = i43 + 1;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector45));
        ASN1EncodableVector aSN1EncodableVector55 = new ASN1EncodableVector();
        for (byte[] bArr11 : bArr7) {
            aSN1EncodableVector55.add(new DEROctetString(bArr11));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector55));
        ASN1EncodableVector aSN1EncodableVector56 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector57 = new ASN1EncodableVector();
        new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector58 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector59 = new ASN1EncodableVector();
        for (int i58 = 0; i58 < gMSSRootSigArr.length; i58++) {
            aSN1EncodableVector57.add(new DERSequence(algorithmIdentifierArr[0]));
            new ASN1EncodableVector();
            aSN1EncodableVector58.add(new DEROctetString(gMSSRootSigArr[i58].getStatByte()[0]));
            aSN1EncodableVector58.add(new DEROctetString(gMSSRootSigArr[i58].getStatByte()[1]));
            aSN1EncodableVector58.add(new DEROctetString(gMSSRootSigArr[i58].getStatByte()[2]));
            aSN1EncodableVector58.add(new DEROctetString(gMSSRootSigArr[i58].getStatByte()[3]));
            aSN1EncodableVector58.add(new DEROctetString(gMSSRootSigArr[i58].getStatByte()[4]));
            aSN1EncodableVector57.add(new DERSequence(aSN1EncodableVector58));
            aSN1EncodableVector58 = new ASN1EncodableVector();
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[0]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[1]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[2]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[3]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[4]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[5]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[6]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[7]));
            aSN1EncodableVector59.add(new ASN1Integer((long) gMSSRootSigArr[i58].getStatInt()[8]));
            aSN1EncodableVector57.add(new DERSequence(aSN1EncodableVector59));
            aSN1EncodableVector59 = new ASN1EncodableVector();
            aSN1EncodableVector56.add(new DERSequence(aSN1EncodableVector57));
            aSN1EncodableVector57 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector56));
        ASN1EncodableVector aSN1EncodableVector60 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector61 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector62 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector63 = new ASN1EncodableVector();
        for (int i59 = 0; i59 < gMSSParameters.getHeightOfTrees().length; i59++) {
            aSN1EncodableVector61.add(new ASN1Integer((long) gMSSParameters.getHeightOfTrees()[i59]));
            aSN1EncodableVector62.add(new ASN1Integer((long) gMSSParameters.getWinternitzParameter()[i59]));
            aSN1EncodableVector63.add(new ASN1Integer((long) gMSSParameters.getK()[i59]));
        }
        aSN1EncodableVector60.add(new ASN1Integer((long) gMSSParameters.getNumOfLayers()));
        aSN1EncodableVector60.add(new DERSequence(aSN1EncodableVector61));
        aSN1EncodableVector60.add(new DERSequence(aSN1EncodableVector62));
        aSN1EncodableVector60.add(new DERSequence(aSN1EncodableVector63));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector60));
        ASN1EncodableVector aSN1EncodableVector64 = new ASN1EncodableVector();
        for (AlgorithmIdentifier algorithmIdentifier : algorithmIdentifierArr) {
            aSN1EncodableVector64.add(algorithmIdentifier);
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector64));
        return new DERSequence(aSN1EncodableVector);
    }

    public ASN1Primitive toASN1Primitive() {
        return this.primitive;
    }
}
