package org.bouncycastle.crypto.prng;

import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.util.Pack;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

public class VMPCRandomGenerator implements RandomGenerator {
    private byte[] P = {-69, 44, 98, ByteCompanionObject.MAX_VALUE, -75, -86, MessagePack.Code.FIXEXT1, Draft_75.CR, -127, -2, -78, -126, MessagePack.Code.FLOAT64, MessagePack.Code.FIXSTR_PREFIX, -95, 8, 24, 113, 86, -24, 73, 2, Tnaf.POW_2_WIDTH, MessagePack.Code.BIN8, MessagePack.Code.MAP16, TarHeader.LF_DIR, -91, -20, Byte.MIN_VALUE, 18, -72, 105, MessagePack.Code.STR16, 47, 117, MessagePack.Code.UINT8, -94, 9, TarHeader.LF_FIFO, 3, 97, 45, -3, MessagePack.Code.NEGFIXINT_PREFIX, MessagePack.Code.ARRAY32, 5, 67, MessagePack.Code.FIXARRAY_PREFIX, -83, MessagePack.Code.EXT16, -31, -81, 87, -101, 76, MessagePack.Code.FIXEXT16, 81, -82, 80, -123, 60, 10, -28, -13, -100, 38, 35, 83, MessagePack.Code.EXT32, -125, -105, 70, -79, -103, 100, TarHeader.LF_LINK, 119, MessagePack.Code.FIXEXT2, 29, MessagePack.Code.FIXEXT4, 120, -67, 94, -80, -118, 34, 56, -8, 104, 43, 42, MessagePack.Code.BIN16, MessagePack.Code.INT64, -9, PSSSigner.TRAILER_IMPLICIT, 111, MessagePack.Code.MAP32, 4, -27, -107, 62, 37, -122, -90, 11, -113, -15, 36, 14, MessagePack.Code.FIXEXT8, 64, -77, MessagePack.Code.UINT64, 126, 6, 21, -102, 77, 28, -93, MessagePack.Code.STR32, TarHeader.LF_SYMLINK, -110, 88, 17, 39, -12, 89, MessagePack.Code.INT8, 78, 106, 23, 91, -84, -1, 7, MessagePack.Code.NIL, 101, 121, -4, MessagePack.Code.EXT8, MessagePack.Code.UINT16, 118, 66, 93, -25, 58, TarHeader.LF_BLK, 122, TarHeader.LF_NORMAL, 40, 15, 115, 1, -7, MessagePack.Code.INT16, MessagePack.Code.INT32, 25, -23, -111, -71, 90, -19, 65, 109, -76, MessagePack.Code.TRUE, -98, -65, 99, -6, 31, TarHeader.LF_CHR, 96, 71, -119, -16, -106, 26, 95, -109, 61, TarHeader.LF_CONTIG, 75, MessagePack.Code.STR8, -88, MessagePack.Code.NEVER_USED, 27, -10, 57, -117, -73, 12, 32, MessagePack.Code.UINT32, -120, 110, -74, 116, -114, -115, 22, 41, -14, -121, -11, -21, 112, -29, -5, 85, -97, MessagePack.Code.BIN32, 68, 74, 69, 125, -30, 107, 92, 108, 102, -87, -116, -18, -124, 19, -89, 30, -99, MessagePack.Code.ARRAY16, 103, 72, -70, 46, -26, -92, -85, 124, -108, 0, 33, -17, -22, -66, MessagePack.Code.FLOAT32, 114, 79, 82, -104, 63, MessagePack.Code.FALSE, 20, 123, 59, 84};
    private byte n = 0;
    private byte s = -66;

    public void addSeedMaterial(long j) {
        addSeedMaterial(Pack.longToBigEndian(j));
    }

    public void addSeedMaterial(byte[] bArr) {
        for (byte b : bArr) {
            this.s = this.P[(this.s + this.P[this.n & 255] + b) & 255];
            byte b2 = this.P[this.n & 255];
            this.P[this.n & 255] = this.P[this.s & 255];
            this.P[this.s & 255] = b2;
            this.n = (byte) ((this.n + 1) & 255);
        }
    }

    public void nextBytes(byte[] bArr) {
        nextBytes(bArr, 0, bArr.length);
    }

    public void nextBytes(byte[] bArr, int i, int i2) {
        synchronized (this.P) {
            int i3 = i + i2;
            while (i != i3) {
                this.s = this.P[(this.s + this.P[this.n & 255]) & 255];
                bArr[i] = this.P[(this.P[this.P[this.s & 255] & 255] + 1) & 255];
                byte b = this.P[this.n & 255];
                this.P[this.n & 255] = this.P[this.s & 255];
                this.P[this.s & 255] = b;
                this.n = (byte) ((this.n + 1) & 255);
                i++;
            }
        }
    }
}
