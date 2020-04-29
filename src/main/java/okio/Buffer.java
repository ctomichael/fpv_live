package okio;

import android.support.v4.media.session.PlaybackStateCompat;
import com.mapzen.android.lost.internal.Clock;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.text.Typography;
import org.bouncycastle.asn1.cmc.BodyPartID;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2KeyGenParameterSpec;
import org.xeustechnologies.jtar.TarHeader;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    private static final byte[] DIGITS = {TarHeader.LF_NORMAL, TarHeader.LF_LINK, TarHeader.LF_SYMLINK, TarHeader.LF_CHR, TarHeader.LF_BLK, TarHeader.LF_DIR, TarHeader.LF_FIFO, TarHeader.LF_CONTIG, 56, 57, 97, 98, 99, 100, 101, 102};
    static final int REPLACEMENT_CHARACTER = 65533;
    @Nullable
    Segment head;
    long size;

    public final long size() {
        return this.size;
    }

    public Buffer buffer() {
        return this;
    }

    public OutputStream outputStream() {
        return new OutputStream() {
            /* class okio.Buffer.AnonymousClass1 */

            public void write(int b) {
                Buffer.this.writeByte((int) ((byte) b));
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: okio.Buffer.write(byte[], int, int):okio.Buffer
             arg types: [byte[], int, int]
             candidates:
              okio.Buffer.write(byte[], int, int):okio.BufferedSink
              okio.BufferedSink.write(byte[], int, int):okio.BufferedSink
              okio.Buffer.write(byte[], int, int):okio.Buffer */
            public void write(byte[] data, int offset, int byteCount) {
                Buffer.this.write(data, offset, byteCount);
            }

            public void flush() {
            }

            public void close() {
            }

            public String toString() {
                return Buffer.this + ".outputStream()";
            }
        };
    }

    public Buffer emitCompleteSegments() {
        return this;
    }

    public BufferedSink emit() {
        return this;
    }

    public boolean exhausted() {
        return this.size == 0;
    }

    public void require(long byteCount) throws EOFException {
        if (this.size < byteCount) {
            throw new EOFException();
        }
    }

    public boolean request(long byteCount) {
        return this.size >= byteCount;
    }

    public InputStream inputStream() {
        return new InputStream() {
            /* class okio.Buffer.AnonymousClass2 */

            public int read() {
                if (Buffer.this.size > 0) {
                    return Buffer.this.readByte() & 255;
                }
                return -1;
            }

            public int read(byte[] sink, int offset, int byteCount) {
                return Buffer.this.read(sink, offset, byteCount);
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: ClspMth{java.lang.Math.min(long, long):long}
             arg types: [long, int]
             candidates:
              ClspMth{java.lang.Math.min(double, double):double}
              ClspMth{java.lang.Math.min(float, float):float}
              ClspMth{java.lang.Math.min(int, int):int}
              ClspMth{java.lang.Math.min(long, long):long} */
            public int available() {
                return (int) Math.min(Buffer.this.size, 2147483647L);
            }

            public void close() {
            }

            public String toString() {
                return Buffer.this + ".inputStream()";
            }
        };
    }

    public final Buffer copyTo(OutputStream out) throws IOException {
        return copyTo(out, 0, this.size);
    }

    public final Buffer copyTo(OutputStream out, long offset, long byteCount) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, offset, byteCount);
        if (byteCount != 0) {
            Segment s = this.head;
            while (offset >= ((long) (s.limit - s.pos))) {
                offset -= (long) (s.limit - s.pos);
                s = s.next;
            }
            while (byteCount > 0) {
                int pos = (int) (((long) s.pos) + offset);
                int toCopy = (int) Math.min((long) (s.limit - pos), byteCount);
                out.write(s.data, pos, toCopy);
                byteCount -= (long) toCopy;
                offset = 0;
                s = s.next;
            }
        }
        return this;
    }

    public final Buffer copyTo(Buffer out, long offset, long byteCount) {
        if (out == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, offset, byteCount);
        if (byteCount != 0) {
            out.size += byteCount;
            Segment s = this.head;
            while (offset >= ((long) (s.limit - s.pos))) {
                offset -= (long) (s.limit - s.pos);
                s = s.next;
            }
            while (byteCount > 0) {
                Segment copy = s.sharedCopy();
                copy.pos = (int) (((long) copy.pos) + offset);
                copy.limit = Math.min(copy.pos + ((int) byteCount), copy.limit);
                if (out.head == null) {
                    copy.prev = copy;
                    copy.next = copy;
                    out.head = copy;
                } else {
                    out.head.prev.push(copy);
                }
                byteCount -= (long) (copy.limit - copy.pos);
                offset = 0;
                s = s.next;
            }
        }
        return this;
    }

    public final Buffer writeTo(OutputStream out) throws IOException {
        return writeTo(out, this.size);
    }

    public final Buffer writeTo(OutputStream out, long byteCount) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, 0, byteCount);
        Segment s = this.head;
        while (byteCount > 0) {
            int toCopy = (int) Math.min(byteCount, (long) (s.limit - s.pos));
            out.write(s.data, s.pos, toCopy);
            s.pos += toCopy;
            this.size -= (long) toCopy;
            byteCount -= (long) toCopy;
            if (s.pos == s.limit) {
                Segment toRecycle = s;
                s = toRecycle.pop();
                this.head = s;
                SegmentPool.recycle(toRecycle);
            }
        }
        return this;
    }

    public final Buffer readFrom(InputStream in2) throws IOException {
        readFrom(in2, LongCompanionObject.MAX_VALUE, true);
        return this;
    }

    public final Buffer readFrom(InputStream in2, long byteCount) throws IOException {
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        readFrom(in2, byteCount, false);
        return this;
    }

    private void readFrom(InputStream in2, long byteCount, boolean forever) throws IOException {
        if (in2 == null) {
            throw new IllegalArgumentException("in == null");
        }
        while (true) {
            if (byteCount > 0 || forever) {
                Segment tail = writableSegment(1);
                int bytesRead = in2.read(tail.data, tail.limit, (int) Math.min(byteCount, (long) (8192 - tail.limit)));
                if (bytesRead != -1) {
                    tail.limit += bytesRead;
                    this.size += (long) bytesRead;
                    byteCount -= (long) bytesRead;
                } else if (!forever) {
                    throw new EOFException();
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public final long completeSegmentByteCount() {
        long result = this.size;
        if (result == 0) {
            return 0;
        }
        Segment tail = this.head.prev;
        if (tail.limit < 8192 && tail.owner) {
            result -= (long) (tail.limit - tail.pos);
        }
        return result;
    }

    public byte readByte() {
        if (this.size == 0) {
            throw new IllegalStateException("size == 0");
        }
        Segment segment = this.head;
        int pos = segment.pos;
        int limit = segment.limit;
        int pos2 = pos + 1;
        byte b = segment.data[pos];
        this.size--;
        if (pos2 == limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = pos2;
        }
        return b;
    }

    public final byte getByte(long pos) {
        Util.checkOffsetAndCount(this.size, pos, 1);
        if (this.size - pos > pos) {
            Segment s = this.head;
            while (true) {
                int segmentByteCount = s.limit - s.pos;
                if (pos < ((long) segmentByteCount)) {
                    return s.data[s.pos + ((int) pos)];
                }
                pos -= (long) segmentByteCount;
                s = s.next;
            }
        } else {
            long pos2 = pos - this.size;
            Segment s2 = this.head.prev;
            while (true) {
                pos2 += (long) (s2.limit - s2.pos);
                if (pos2 >= 0) {
                    return s2.data[s2.pos + ((int) pos2)];
                }
                s2 = s2.prev;
            }
        }
    }

    public short readShort() {
        if (this.size < 2) {
            throw new IllegalStateException("size < 2: " + this.size);
        }
        Segment segment = this.head;
        int pos = segment.pos;
        int limit = segment.limit;
        if (limit - pos < 2) {
            return (short) (((readByte() & 255) << 8) | (readByte() & 255));
        }
        byte[] data = segment.data;
        int pos2 = pos + 1;
        int pos3 = pos2 + 1;
        int s = ((data[pos] & 255) << 8) | (data[pos2] & 255);
        this.size -= 2;
        if (pos3 == limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = pos3;
        }
        return (short) s;
    }

    public int readInt() {
        if (this.size < 4) {
            throw new IllegalStateException("size < 4: " + this.size);
        }
        Segment segment = this.head;
        int pos = segment.pos;
        int limit = segment.limit;
        if (limit - pos < 4) {
            return ((readByte() & 255) << 24) | ((readByte() & 255) << Tnaf.POW_2_WIDTH) | ((readByte() & 255) << 8) | (readByte() & 255);
        }
        byte[] data = segment.data;
        int pos2 = pos + 1;
        int pos3 = pos2 + 1;
        int pos4 = pos3 + 1;
        int pos5 = pos4 + 1;
        byte b = ((data[pos] & 255) << 24) | ((data[pos2] & 255) << Tnaf.POW_2_WIDTH) | ((data[pos3] & 255) << 8) | (data[pos4] & 255);
        this.size -= 4;
        if (pos5 == limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
            return b;
        }
        segment.pos = pos5;
        return b;
    }

    public long readLong() {
        if (this.size < 8) {
            throw new IllegalStateException("size < 8: " + this.size);
        }
        Segment segment = this.head;
        int pos = segment.pos;
        int limit = segment.limit;
        if (limit - pos < 8) {
            return ((((long) readInt()) & BodyPartID.bodyIdMax) << 32) | (((long) readInt()) & BodyPartID.bodyIdMax);
        }
        byte[] data = segment.data;
        int pos2 = pos + 1;
        int pos3 = pos2 + 1;
        int pos4 = pos3 + 1;
        int pos5 = pos4 + 1;
        int pos6 = pos5 + 1;
        int pos7 = pos6 + 1;
        int pos8 = pos7 + 1;
        int pos9 = pos8 + 1;
        long j = ((((long) data[pos]) & 255) << 56) | ((((long) data[pos2]) & 255) << 48) | ((((long) data[pos3]) & 255) << 40) | ((((long) data[pos4]) & 255) << 32) | ((((long) data[pos5]) & 255) << 24) | ((((long) data[pos6]) & 255) << 16) | ((((long) data[pos7]) & 255) << 8) | (((long) data[pos8]) & 255);
        this.size -= 8;
        if (pos9 == limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
            return j;
        }
        segment.pos = pos9;
        return j;
    }

    public short readShortLe() {
        return Util.reverseBytesShort(readShort());
    }

    public int readIntLe() {
        return Util.reverseBytesInt(readInt());
    }

    public long readLongLe() {
        return Util.reverseBytesLong(readLong());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0068, code lost:
        if (r10 != false) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006a, code lost:
        r5.readByte();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x008a, code lost:
        throw new java.lang.NumberFormatException("Number too large: " + r5.readUtf8());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00c8, code lost:
        if (r11 != r9) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00ca, code lost:
        r24.head = r17.pop();
        okio.SegmentPool.recycle(r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d7, code lost:
        if (r8 != false) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00f7, code lost:
        r17.pos = r11;
     */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00a9 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readDecimalLong() {
        /*
            r24 = this;
            r0 = r24
            long r0 = r0.size
            r20 = r0
            r22 = 0
            int r20 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1))
            if (r20 != 0) goto L_0x0015
            java.lang.IllegalStateException r20 = new java.lang.IllegalStateException
            java.lang.String r21 = "size == 0"
            r20.<init>(r21)
            throw r20
        L_0x0015:
            r18 = 0
            r16 = 0
            r10 = 0
            r8 = 0
            r14 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            r12 = -7
        L_0x0022:
            r0 = r24
            okio.Segment r0 = r0.head
            r17 = r0
            r0 = r17
            byte[] r6 = r0.data
            r0 = r17
            int r11 = r0.pos
            r0 = r17
            int r9 = r0.limit
        L_0x0034:
            if (r11 >= r9) goto L_0x00c8
            byte r4 = r6[r11]
            r20 = 48
            r0 = r20
            if (r4 < r0) goto L_0x0099
            r20 = 57
            r0 = r20
            if (r4 > r0) goto L_0x0099
            int r7 = 48 - r4
            int r20 = (r18 > r14 ? 1 : (r18 == r14 ? 0 : -1))
            if (r20 < 0) goto L_0x0055
            int r20 = (r18 > r14 ? 1 : (r18 == r14 ? 0 : -1))
            if (r20 != 0) goto L_0x008b
            long r0 = (long) r7
            r20 = r0
            int r20 = (r20 > r12 ? 1 : (r20 == r12 ? 0 : -1))
            if (r20 >= 0) goto L_0x008b
        L_0x0055:
            okio.Buffer r20 = new okio.Buffer
            r20.<init>()
            r0 = r20
            r1 = r18
            okio.Buffer r20 = r0.writeDecimalLong(r1)
            r0 = r20
            okio.Buffer r5 = r0.writeByte(r4)
            if (r10 != 0) goto L_0x006d
            r5.readByte()
        L_0x006d:
            java.lang.NumberFormatException r20 = new java.lang.NumberFormatException
            java.lang.StringBuilder r21 = new java.lang.StringBuilder
            r21.<init>()
            java.lang.String r22 = "Number too large: "
            java.lang.StringBuilder r21 = r21.append(r22)
            java.lang.String r22 = r5.readUtf8()
            java.lang.StringBuilder r21 = r21.append(r22)
            java.lang.String r21 = r21.toString()
            r20.<init>(r21)
            throw r20
        L_0x008b:
            r20 = 10
            long r18 = r18 * r20
            long r0 = (long) r7
            r20 = r0
            long r18 = r18 + r20
        L_0x0094:
            int r11 = r11 + 1
            int r16 = r16 + 1
            goto L_0x0034
        L_0x0099:
            r20 = 45
            r0 = r20
            if (r4 != r0) goto L_0x00a7
            if (r16 != 0) goto L_0x00a7
            r10 = 1
            r20 = 1
            long r12 = r12 - r20
            goto L_0x0094
        L_0x00a7:
            if (r16 != 0) goto L_0x00c7
            java.lang.NumberFormatException r20 = new java.lang.NumberFormatException
            java.lang.StringBuilder r21 = new java.lang.StringBuilder
            r21.<init>()
            java.lang.String r22 = "Expected leading [0-9] or '-' character but was 0x"
            java.lang.StringBuilder r21 = r21.append(r22)
            java.lang.String r22 = java.lang.Integer.toHexString(r4)
            java.lang.StringBuilder r21 = r21.append(r22)
            java.lang.String r21 = r21.toString()
            r20.<init>(r21)
            throw r20
        L_0x00c7:
            r8 = 1
        L_0x00c8:
            if (r11 != r9) goto L_0x00f7
            okio.Segment r20 = r17.pop()
            r0 = r20
            r1 = r24
            r1.head = r0
            okio.SegmentPool.recycle(r17)
        L_0x00d7:
            if (r8 != 0) goto L_0x00e1
            r0 = r24
            okio.Segment r0 = r0.head
            r20 = r0
            if (r20 != 0) goto L_0x0022
        L_0x00e1:
            r0 = r24
            long r0 = r0.size
            r20 = r0
            r0 = r16
            long r0 = (long) r0
            r22 = r0
            long r20 = r20 - r22
            r0 = r20
            r2 = r24
            r2.size = r0
            if (r10 == 0) goto L_0x00fc
        L_0x00f6:
            return r18
        L_0x00f7:
            r0 = r17
            r0.pos = r11
            goto L_0x00d7
        L_0x00fc:
            r0 = r18
            long r0 = -r0
            r18 = r0
            goto L_0x00f6
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.readDecimalLong():long");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x009e, code lost:
        if (r8 != r7) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a0, code lost:
        r18.head = r10.pop();
        okio.SegmentPool.recycle(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00ab, code lost:
        if (r6 != false) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00cb, code lost:
        r10.pos = r8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readHexadecimalUnsignedLong() {
        /*
            r18 = this;
            r0 = r18
            long r14 = r0.size
            r16 = 0
            int r11 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r11 != 0) goto L_0x0013
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException
            java.lang.String r14 = "size == 0"
            r11.<init>(r14)
            throw r11
        L_0x0013:
            r12 = 0
            r9 = 0
            r6 = 0
        L_0x0017:
            r0 = r18
            okio.Segment r10 = r0.head
            byte[] r4 = r10.data
            int r8 = r10.pos
            int r7 = r10.limit
        L_0x0021:
            if (r8 >= r7) goto L_0x009e
            byte r2 = r4[r8]
            r11 = 48
            if (r2 < r11) goto L_0x0063
            r11 = 57
            if (r2 > r11) goto L_0x0063
            int r5 = r2 + -48
        L_0x002f:
            r14 = -1152921504606846976(0xf000000000000000, double:-3.105036184601418E231)
            long r14 = r14 & r12
            r16 = 0
            int r11 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r11 == 0) goto L_0x00c1
            okio.Buffer r11 = new okio.Buffer
            r11.<init>()
            okio.Buffer r11 = r11.writeHexadecimalUnsignedLong(r12)
            okio.Buffer r3 = r11.writeByte(r2)
            java.lang.NumberFormatException r11 = new java.lang.NumberFormatException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "Number too large: "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = r3.readUtf8()
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r14 = r14.toString()
            r11.<init>(r14)
            throw r11
        L_0x0063:
            r11 = 97
            if (r2 < r11) goto L_0x0070
            r11 = 102(0x66, float:1.43E-43)
            if (r2 > r11) goto L_0x0070
            int r11 = r2 + -97
            int r5 = r11 + 10
            goto L_0x002f
        L_0x0070:
            r11 = 65
            if (r2 < r11) goto L_0x007d
            r11 = 70
            if (r2 > r11) goto L_0x007d
            int r11 = r2 + -65
            int r5 = r11 + 10
            goto L_0x002f
        L_0x007d:
            if (r9 != 0) goto L_0x009d
            java.lang.NumberFormatException r11 = new java.lang.NumberFormatException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "Expected leading [0-9a-fA-F] character but was 0x"
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = java.lang.Integer.toHexString(r2)
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r14 = r14.toString()
            r11.<init>(r14)
            throw r11
        L_0x009d:
            r6 = 1
        L_0x009e:
            if (r8 != r7) goto L_0x00cb
            okio.Segment r11 = r10.pop()
            r0 = r18
            r0.head = r11
            okio.SegmentPool.recycle(r10)
        L_0x00ab:
            if (r6 != 0) goto L_0x00b3
            r0 = r18
            okio.Segment r11 = r0.head
            if (r11 != 0) goto L_0x0017
        L_0x00b3:
            r0 = r18
            long r14 = r0.size
            long r0 = (long) r9
            r16 = r0
            long r14 = r14 - r16
            r0 = r18
            r0.size = r14
            return r12
        L_0x00c1:
            r11 = 4
            long r12 = r12 << r11
            long r14 = (long) r5
            long r12 = r12 | r14
            int r8 = r8 + 1
            int r9 = r9 + 1
            goto L_0x0021
        L_0x00cb:
            r10.pos = r8
            goto L_0x00ab
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.readHexadecimalUnsignedLong():long");
    }

    public ByteString readByteString() {
        return new ByteString(readByteArray());
    }

    public ByteString readByteString(long byteCount) throws EOFException {
        return new ByteString(readByteArray(byteCount));
    }

    public int select(Options options) {
        int index = selectPrefix(options, false);
        if (index == -1) {
            return -1;
        }
        try {
            skip((long) options.byteStrings[index].size());
            return index;
        } catch (EOFException e) {
            throw new AssertionError();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int selectPrefix(okio.Options r23, boolean r24) {
        /*
            r22 = this;
            r0 = r22
            okio.Segment r4 = r0.head
            if (r4 != 0) goto L_0x0015
            if (r24 == 0) goto L_0x000a
            r10 = -2
        L_0x0009:
            return r10
        L_0x000a:
            okio.ByteString r21 = okio.ByteString.EMPTY
            r0 = r23
            r1 = r21
            int r10 = r0.indexOf(r1)
            goto L_0x0009
        L_0x0015:
            r11 = r4
            byte[] r3 = r4.data
            int r7 = r4.pos
            int r5 = r4.limit
            r0 = r23
            int[] r0 = r0.trie
            r17 = r0
            r19 = 0
            r10 = -1
        L_0x0025:
            int r20 = r19 + 1
            r14 = r17[r19]
            int r19 = r20 + 1
            r9 = r17[r20]
            r21 = -1
            r0 = r21
            if (r9 == r0) goto L_0x0034
            r10 = r9
        L_0x0034:
            if (r11 != 0) goto L_0x003a
        L_0x0036:
            if (r24 == 0) goto L_0x0009
            r10 = -2
            goto L_0x0009
        L_0x003a:
            if (r14 >= 0) goto L_0x0078
            int r12 = r14 * -1
            int r18 = r19 + r12
        L_0x0040:
            int r8 = r7 + 1
            byte r21 = r3[r7]
            r0 = r21
            r2 = r0 & 255(0xff, float:3.57E-43)
            int r20 = r19 + 1
            r21 = r17[r19]
            r0 = r21
            if (r2 != r0) goto L_0x0009
            r0 = r20
            r1 = r18
            if (r0 != r1) goto L_0x0068
            r13 = 1
        L_0x0057:
            if (r8 != r5) goto L_0x00ab
            okio.Segment r11 = r11.next
            int r7 = r11.pos
            byte[] r3 = r11.data
            int r5 = r11.limit
            if (r11 != r4) goto L_0x006b
            if (r13 != 0) goto L_0x006a
            r19 = r20
            goto L_0x0036
        L_0x0068:
            r13 = 0
            goto L_0x0057
        L_0x006a:
            r11 = 0
        L_0x006b:
            if (r13 == 0) goto L_0x0075
            r6 = r17[r20]
            r19 = r20
        L_0x0071:
            if (r6 < 0) goto L_0x00a4
            r10 = r6
            goto L_0x0009
        L_0x0075:
            r19 = r20
            goto L_0x0040
        L_0x0078:
            r15 = r14
            int r8 = r7 + 1
            byte r21 = r3[r7]
            r0 = r21
            r2 = r0 & 255(0xff, float:3.57E-43)
            int r16 = r19 + r15
        L_0x0083:
            r0 = r19
            r1 = r16
            if (r0 == r1) goto L_0x0009
            r21 = r17[r19]
            r0 = r21
            if (r2 != r0) goto L_0x00a1
            int r21 = r19 + r15
            r6 = r17[r21]
            if (r8 != r5) goto L_0x00a9
            okio.Segment r11 = r11.next
            int r7 = r11.pos
            byte[] r3 = r11.data
            int r5 = r11.limit
            if (r11 != r4) goto L_0x0071
            r11 = 0
            goto L_0x0071
        L_0x00a1:
            int r19 = r19 + 1
            goto L_0x0083
        L_0x00a4:
            int r0 = -r6
            r19 = r0
            goto L_0x0025
        L_0x00a9:
            r7 = r8
            goto L_0x0071
        L_0x00ab:
            r7 = r8
            goto L_0x006b
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.selectPrefix(okio.Options, boolean):int");
    }

    public void readFully(Buffer sink, long byteCount) throws EOFException {
        if (this.size < byteCount) {
            sink.write(this, this.size);
            throw new EOFException();
        } else {
            sink.write(this, byteCount);
        }
    }

    public long readAll(Sink sink) throws IOException {
        long byteCount = this.size;
        if (byteCount > 0) {
            sink.write(this, byteCount);
        }
        return byteCount;
    }

    public String readUtf8() {
        try {
            return readString(this.size, Util.UTF_8);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public String readUtf8(long byteCount) throws EOFException {
        return readString(byteCount, Util.UTF_8);
    }

    public String readString(Charset charset) {
        try {
            return readString(this.size, charset);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public String readString(long byteCount, Charset charset) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0, byteCount);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        } else if (byteCount > 2147483647L) {
            throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + byteCount);
        } else if (byteCount == 0) {
            return "";
        } else {
            Segment s = this.head;
            if (((long) s.pos) + byteCount > ((long) s.limit)) {
                return new String(readByteArray(byteCount), charset);
            }
            String str = new String(s.data, s.pos, (int) byteCount, charset);
            s.pos = (int) (((long) s.pos) + byteCount);
            this.size -= byteCount;
            if (s.pos != s.limit) {
                return str;
            }
            this.head = s.pop();
            SegmentPool.recycle(s);
            return str;
        }
    }

    @Nullable
    public String readUtf8Line() throws EOFException {
        long newline = indexOf((byte) 10);
        if (newline != -1) {
            return readUtf8Line(newline);
        }
        if (this.size != 0) {
            return readUtf8(this.size);
        }
        return null;
    }

    public String readUtf8LineStrict() throws EOFException {
        return readUtf8LineStrict(LongCompanionObject.MAX_VALUE);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.min(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.min(double, double):double}
      ClspMth{java.lang.Math.min(float, float):float}
      ClspMth{java.lang.Math.min(int, int):int}
      ClspMth{java.lang.Math.min(long, long):long} */
    public String readUtf8LineStrict(long limit) throws EOFException {
        if (limit < 0) {
            throw new IllegalArgumentException("limit < 0: " + limit);
        }
        long scanLength = limit == LongCompanionObject.MAX_VALUE ? LongCompanionObject.MAX_VALUE : limit + 1;
        long newline = indexOf((byte) 10, 0, scanLength);
        if (newline != -1) {
            return readUtf8Line(newline);
        }
        if (scanLength < size()) {
            if (getByte(scanLength - 1) == 13 && getByte(scanLength) == 10) {
                return readUtf8Line(scanLength);
            }
        }
        Buffer data = new Buffer();
        copyTo(data, 0, Math.min(32L, size()));
        throw new EOFException("\\n not found: limit=" + Math.min(size(), limit) + " content=" + data.readByteString().hex() + ((char) Typography.ellipsis));
    }

    /* access modifiers changed from: package-private */
    public String readUtf8Line(long newline) throws EOFException {
        if (newline <= 0 || getByte(newline - 1) != 13) {
            String result = readUtf8(newline);
            skip(1);
            return result;
        }
        String result2 = readUtf8(newline - 1);
        skip(2);
        return result2;
    }

    public int readUtf8CodePoint() throws EOFException {
        int codePoint;
        int byteCount;
        int min;
        if (this.size == 0) {
            throw new EOFException();
        }
        int b0 = getByte(0);
        if ((b0 & 128) == 0) {
            codePoint = b0 & CertificateBody.profileType;
            byteCount = 1;
            min = 0;
        } else if ((b0 & 224) == 192) {
            codePoint = b0 & 31;
            byteCount = 2;
            min = 128;
        } else if ((b0 & 240) == 224) {
            codePoint = b0 & 15;
            byteCount = 3;
            min = 2048;
        } else if ((b0 & 248) == 240) {
            codePoint = b0 & 7;
            byteCount = 4;
            min = 65536;
        } else {
            skip(1);
            return REPLACEMENT_CHARACTER;
        }
        if (this.size < ((long) byteCount)) {
            throw new EOFException("size < " + byteCount + ": " + this.size + " (to read code point prefixed 0x" + Integer.toHexString(b0) + ")");
        }
        int i = 1;
        while (i < byteCount) {
            int b = getByte((long) i);
            if ((b & 192) == 128) {
                codePoint = (codePoint << 6) | (b & 63);
                i++;
            } else {
                skip((long) i);
                return REPLACEMENT_CHARACTER;
            }
        }
        skip((long) byteCount);
        return codePoint > 1114111 ? REPLACEMENT_CHARACTER : (codePoint < 55296 || codePoint > 57343) ? codePoint < min ? REPLACEMENT_CHARACTER : codePoint : REPLACEMENT_CHARACTER;
    }

    public byte[] readByteArray() {
        try {
            return readByteArray(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public byte[] readByteArray(long byteCount) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0, byteCount);
        if (byteCount > 2147483647L) {
            throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + byteCount);
        }
        byte[] result = new byte[((int) byteCount)];
        readFully(result);
        return result;
    }

    public int read(byte[] sink) {
        return read(sink, 0, sink.length);
    }

    public void readFully(byte[] sink) throws EOFException {
        int offset = 0;
        while (offset < sink.length) {
            int read = read(sink, offset, sink.length - offset);
            if (read == -1) {
                throw new EOFException();
            }
            offset += read;
        }
    }

    public int read(byte[] sink, int offset, int byteCount) {
        Util.checkOffsetAndCount((long) sink.length, (long) offset, (long) byteCount);
        Segment s = this.head;
        if (s == null) {
            return -1;
        }
        int toCopy = Math.min(byteCount, s.limit - s.pos);
        System.arraycopy(s.data, s.pos, sink, offset, toCopy);
        s.pos += toCopy;
        this.size -= (long) toCopy;
        if (s.pos != s.limit) {
            return toCopy;
        }
        this.head = s.pop();
        SegmentPool.recycle(s);
        return toCopy;
    }

    public int read(ByteBuffer sink) throws IOException {
        Segment s = this.head;
        if (s == null) {
            return -1;
        }
        int toCopy = Math.min(sink.remaining(), s.limit - s.pos);
        sink.put(s.data, s.pos, toCopy);
        s.pos += toCopy;
        this.size -= (long) toCopy;
        if (s.pos != s.limit) {
            return toCopy;
        }
        this.head = s.pop();
        SegmentPool.recycle(s);
        return toCopy;
    }

    public final void clear() {
        try {
            skip(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public void skip(long byteCount) throws EOFException {
        while (byteCount > 0) {
            if (this.head == null) {
                throw new EOFException();
            }
            int toSkip = (int) Math.min(byteCount, (long) (this.head.limit - this.head.pos));
            this.size -= (long) toSkip;
            byteCount -= (long) toSkip;
            this.head.pos += toSkip;
            if (this.head.pos == this.head.limit) {
                Segment toRecycle = this.head;
                this.head = toRecycle.pop();
                SegmentPool.recycle(toRecycle);
            }
        }
    }

    public Buffer write(ByteString byteString) {
        if (byteString == null) {
            throw new IllegalArgumentException("byteString == null");
        }
        byteString.write(this);
        return this;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer
     arg types: [java.lang.String, int, int]
     candidates:
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.BufferedSink.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer */
    public Buffer writeUtf8(String string) {
        return writeUtf8(string, 0, string.length());
    }

    public Buffer writeUtf8(String string, int beginIndex, int endIndex) {
        int low;
        if (string == null) {
            throw new IllegalArgumentException("string == null");
        } else if (beginIndex < 0) {
            throw new IllegalArgumentException("beginIndex < 0: " + beginIndex);
        } else if (endIndex < beginIndex) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + endIndex + " < " + beginIndex);
        } else if (endIndex > string.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + endIndex + " > " + string.length());
        } else {
            int i = beginIndex;
            while (true) {
                int i2 = i;
                if (i2 >= endIndex) {
                    return this;
                }
                int c = string.charAt(i2);
                if (c < 128) {
                    Segment tail = writableSegment(1);
                    byte[] data = tail.data;
                    int segmentOffset = tail.limit - i2;
                    int runLimit = Math.min(endIndex, 8192 - segmentOffset);
                    data[segmentOffset + i2] = (byte) c;
                    int i3 = i2 + 1;
                    while (i3 < runLimit) {
                        int c2 = string.charAt(i3);
                        if (c2 >= 128) {
                            break;
                        }
                        data[segmentOffset + i3] = (byte) c2;
                        i3++;
                    }
                    int runSize = (i3 + segmentOffset) - tail.limit;
                    tail.limit += runSize;
                    this.size += (long) runSize;
                    i = i3;
                } else if (c < 2048) {
                    writeByte((c >> 6) | 192);
                    writeByte((c & 63) | 128);
                    i = i2 + 1;
                } else if (c < 55296 || c > 57343) {
                    writeByte((c >> 12) | 224);
                    writeByte(((c >> 6) & 63) | 128);
                    writeByte((c & 63) | 128);
                    i = i2 + 1;
                } else {
                    if (i2 + 1 < endIndex) {
                        low = string.charAt(i2 + 1);
                    } else {
                        low = 0;
                    }
                    if (c > 56319 || low < 56320 || low > 57343) {
                        writeByte(63);
                        i = i2 + 1;
                    } else {
                        int codePoint = 65536 + (((-55297 & c) << 10) | (-56321 & low));
                        writeByte((codePoint >> 18) | 240);
                        writeByte(((codePoint >> 12) & 63) | 128);
                        writeByte(((codePoint >> 6) & 63) | 128);
                        writeByte((codePoint & 63) | 128);
                        i = i2 + 2;
                    }
                }
            }
        }
    }

    public Buffer writeUtf8CodePoint(int codePoint) {
        if (codePoint < 128) {
            writeByte(codePoint);
        } else if (codePoint < 2048) {
            writeByte((codePoint >> 6) | 192);
            writeByte((codePoint & 63) | 128);
        } else if (codePoint < 65536) {
            if (codePoint < 55296 || codePoint > 57343) {
                writeByte((codePoint >> 12) | 224);
                writeByte(((codePoint >> 6) & 63) | 128);
                writeByte((codePoint & 63) | 128);
            } else {
                writeByte(63);
            }
        } else if (codePoint <= 1114111) {
            writeByte((codePoint >> 18) | 240);
            writeByte(((codePoint >> 12) & 63) | 128);
            writeByte(((codePoint >> 6) & 63) | 128);
            writeByte((codePoint & 63) | 128);
        } else {
            throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(codePoint));
        }
        return this;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.Buffer
     arg types: [java.lang.String, int, int, java.nio.charset.Charset]
     candidates:
      okio.Buffer.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.BufferedSink
      okio.BufferedSink.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.BufferedSink
      okio.Buffer.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.Buffer */
    public Buffer writeString(String string, Charset charset) {
        return writeString(string, 0, string.length(), charset);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer
     arg types: [java.lang.String, int, int]
     candidates:
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.BufferedSink.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.write(byte[], int, int):okio.Buffer
     arg types: [byte[], int, int]
     candidates:
      okio.Buffer.write(byte[], int, int):okio.BufferedSink
      okio.BufferedSink.write(byte[], int, int):okio.BufferedSink
      okio.Buffer.write(byte[], int, int):okio.Buffer */
    public Buffer writeString(String string, int beginIndex, int endIndex, Charset charset) {
        if (string == null) {
            throw new IllegalArgumentException("string == null");
        } else if (beginIndex < 0) {
            throw new IllegalAccessError("beginIndex < 0: " + beginIndex);
        } else if (endIndex < beginIndex) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + endIndex + " < " + beginIndex);
        } else if (endIndex > string.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + endIndex + " > " + string.length());
        } else if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        } else if (charset.equals(Util.UTF_8)) {
            return writeUtf8(string, beginIndex, endIndex);
        } else {
            byte[] data = string.substring(beginIndex, endIndex).getBytes(charset);
            return write(data, 0, data.length);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.write(byte[], int, int):okio.Buffer
     arg types: [byte[], int, int]
     candidates:
      okio.Buffer.write(byte[], int, int):okio.BufferedSink
      okio.BufferedSink.write(byte[], int, int):okio.BufferedSink
      okio.Buffer.write(byte[], int, int):okio.Buffer */
    public Buffer write(byte[] source) {
        if (source != null) {
            return write(source, 0, source.length);
        }
        throw new IllegalArgumentException("source == null");
    }

    public Buffer write(byte[] source, int offset, int byteCount) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        Util.checkOffsetAndCount((long) source.length, (long) offset, (long) byteCount);
        int limit = offset + byteCount;
        while (offset < limit) {
            Segment tail = writableSegment(1);
            int toCopy = Math.min(limit - offset, 8192 - tail.limit);
            System.arraycopy(source, offset, tail.data, tail.limit, toCopy);
            offset += toCopy;
            tail.limit += toCopy;
        }
        this.size += (long) byteCount;
        return this;
    }

    public int write(ByteBuffer source) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        int byteCount = source.remaining();
        int remaining = byteCount;
        while (remaining > 0) {
            Segment tail = writableSegment(1);
            int toCopy = Math.min(remaining, 8192 - tail.limit);
            source.get(tail.data, tail.limit, toCopy);
            remaining -= toCopy;
            tail.limit += toCopy;
        }
        this.size += (long) byteCount;
        return byteCount;
    }

    public long writeAll(Source source) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long totalBytesRead = 0;
        while (true) {
            long readCount = source.read(this, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
            if (readCount == -1) {
                return totalBytesRead;
            }
            totalBytesRead += readCount;
        }
    }

    public BufferedSink write(Source source, long byteCount) throws IOException {
        while (byteCount > 0) {
            long read = source.read(this, byteCount);
            if (read == -1) {
                throw new EOFException();
            }
            byteCount -= read;
        }
        return this;
    }

    public Buffer writeByte(int b) {
        Segment tail = writableSegment(1);
        byte[] bArr = tail.data;
        int i = tail.limit;
        tail.limit = i + 1;
        bArr[i] = (byte) b;
        this.size++;
        return this;
    }

    public Buffer writeShort(int s) {
        Segment tail = writableSegment(2);
        byte[] data = tail.data;
        int limit = tail.limit;
        int limit2 = limit + 1;
        data[limit] = (byte) ((s >>> 8) & 255);
        data[limit2] = (byte) (s & 255);
        tail.limit = limit2 + 1;
        this.size += 2;
        return this;
    }

    public Buffer writeShortLe(int s) {
        return writeShort((int) Util.reverseBytesShort((short) s));
    }

    public Buffer writeInt(int i) {
        Segment tail = writableSegment(4);
        byte[] data = tail.data;
        int limit = tail.limit;
        int limit2 = limit + 1;
        data[limit] = (byte) ((i >>> 24) & 255);
        int limit3 = limit2 + 1;
        data[limit2] = (byte) ((i >>> 16) & 255);
        int limit4 = limit3 + 1;
        data[limit3] = (byte) ((i >>> 8) & 255);
        data[limit4] = (byte) (i & 255);
        tail.limit = limit4 + 1;
        this.size += 4;
        return this;
    }

    public Buffer writeIntLe(int i) {
        return writeInt(Util.reverseBytesInt(i));
    }

    public Buffer writeLong(long v) {
        Segment tail = writableSegment(8);
        byte[] data = tail.data;
        int limit = tail.limit;
        int limit2 = limit + 1;
        data[limit] = (byte) ((int) ((v >>> 56) & 255));
        int limit3 = limit2 + 1;
        data[limit2] = (byte) ((int) ((v >>> 48) & 255));
        int limit4 = limit3 + 1;
        data[limit3] = (byte) ((int) ((v >>> 40) & 255));
        int limit5 = limit4 + 1;
        data[limit4] = (byte) ((int) ((v >>> 32) & 255));
        int limit6 = limit5 + 1;
        data[limit5] = (byte) ((int) ((v >>> 24) & 255));
        int limit7 = limit6 + 1;
        data[limit6] = (byte) ((int) ((v >>> 16) & 255));
        int limit8 = limit7 + 1;
        data[limit7] = (byte) ((int) ((v >>> 8) & 255));
        data[limit8] = (byte) ((int) (v & 255));
        tail.limit = limit8 + 1;
        this.size += 8;
        return this;
    }

    public Buffer writeLongLe(long v) {
        return writeLong(Util.reverseBytesLong(v));
    }

    /* Debug info: failed to restart local var, previous not found, register: 13 */
    public Buffer writeDecimalLong(long v) {
        int width;
        if (v == 0) {
            return writeByte(48);
        }
        boolean negative = false;
        if (v < 0) {
            v = -v;
            if (v < 0) {
                return writeUtf8("-9223372036854775808");
            }
            negative = true;
        }
        if (v < 100000000) {
            width = v < 10000 ? v < 100 ? v < 10 ? 1 : 2 : v < 1000 ? 3 : 4 : v < Clock.MS_TO_NS ? v < 100000 ? 5 : 6 : v < 10000000 ? 7 : 8;
        } else if (v < 1000000000000L) {
            width = v < 10000000000L ? v < 1000000000 ? 9 : 10 : v < 100000000000L ? 11 : 12;
        } else if (v < 1000000000000000L) {
            width = v < 10000000000000L ? 13 : v < 100000000000000L ? 14 : 15;
        } else if (v < 100000000000000000L) {
            width = v < 10000000000000000L ? 16 : 17;
        } else {
            width = v < 1000000000000000000L ? 18 : 19;
        }
        if (negative) {
            width++;
        }
        Segment tail = writableSegment(width);
        byte[] data = tail.data;
        int pos = tail.limit + width;
        while (v != 0) {
            pos--;
            data[pos] = DIGITS[(int) (v % 10)];
            v /= 10;
        }
        if (negative) {
            data[pos - 1] = 45;
        }
        tail.limit += width;
        this.size += (long) width;
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 11 */
    public Buffer writeHexadecimalUnsignedLong(long v) {
        if (v == 0) {
            return writeByte(48);
        }
        int width = (Long.numberOfTrailingZeros(Long.highestOneBit(v)) / 4) + 1;
        Segment tail = writableSegment(width);
        byte[] data = tail.data;
        int start = tail.limit;
        for (int pos = (tail.limit + width) - 1; pos >= start; pos--) {
            data[pos] = DIGITS[(int) (15 & v)];
            v >>>= 4;
        }
        tail.limit += width;
        this.size += (long) width;
        return this;
    }

    /* access modifiers changed from: package-private */
    public Segment writableSegment(int minimumCapacity) {
        if (minimumCapacity < 1 || minimumCapacity > 8192) {
            throw new IllegalArgumentException();
        } else if (this.head == null) {
            this.head = SegmentPool.take();
            Segment segment = this.head;
            Segment segment2 = this.head;
            Segment segment3 = this.head;
            segment2.prev = segment3;
            segment.next = segment3;
            return segment3;
        } else {
            Segment tail = this.head.prev;
            if (tail.limit + minimumCapacity > 8192 || !tail.owner) {
                return tail.push(SegmentPool.take());
            }
            return tail;
        }
    }

    public void write(Buffer source, long byteCount) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        } else if (source == this) {
            throw new IllegalArgumentException("source == this");
        } else {
            Util.checkOffsetAndCount(source.size, 0, byteCount);
            while (byteCount > 0) {
                if (byteCount < ((long) (source.head.limit - source.head.pos))) {
                    Segment tail = this.head != null ? this.head.prev : null;
                    if (tail != null && tail.owner) {
                        if ((byteCount + ((long) tail.limit)) - ((long) (tail.shared ? 0 : tail.pos)) <= PlaybackStateCompat.ACTION_PLAY_FROM_URI) {
                            source.head.writeTo(tail, (int) byteCount);
                            source.size -= byteCount;
                            this.size += byteCount;
                            return;
                        }
                    }
                    source.head = source.head.split((int) byteCount);
                }
                Segment segmentToMove = source.head;
                long movedByteCount = (long) (segmentToMove.limit - segmentToMove.pos);
                source.head = segmentToMove.pop();
                if (this.head == null) {
                    this.head = segmentToMove;
                    Segment segment = this.head;
                    Segment segment2 = this.head;
                    Segment segment3 = this.head;
                    segment2.prev = segment3;
                    segment.next = segment3;
                } else {
                    this.head.prev.push(segmentToMove).compact();
                }
                source.size -= movedByteCount;
                this.size += movedByteCount;
                byteCount -= movedByteCount;
            }
        }
    }

    public long read(Buffer sink, long byteCount) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        } else if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        } else if (this.size == 0) {
            return -1;
        } else {
            if (byteCount > this.size) {
                byteCount = this.size;
            }
            sink.write(this, byteCount);
            return byteCount;
        }
    }

    public long indexOf(byte b) {
        return indexOf(b, 0, LongCompanionObject.MAX_VALUE);
    }

    public long indexOf(byte b, long fromIndex) {
        return indexOf(b, fromIndex, LongCompanionObject.MAX_VALUE);
    }

    public long indexOf(byte b, long fromIndex, long toIndex) {
        Segment s;
        long offset;
        if (fromIndex < 0 || toIndex < fromIndex) {
            throw new IllegalArgumentException(String.format("size=%s fromIndex=%s toIndex=%s", Long.valueOf(this.size), Long.valueOf(fromIndex), Long.valueOf(toIndex)));
        }
        if (toIndex > this.size) {
            toIndex = this.size;
        }
        if (fromIndex == toIndex || (s = this.head) == null) {
            return -1;
        }
        if (this.size - fromIndex >= fromIndex) {
            long offset2 = 0;
            while (true) {
                long nextOffset = offset + ((long) (s.limit - s.pos));
                if (nextOffset >= fromIndex) {
                    break;
                }
                s = s.next;
                offset2 = nextOffset;
            }
        } else {
            offset = this.size;
            while (offset > fromIndex) {
                s = s.prev;
                offset -= (long) (s.limit - s.pos);
            }
        }
        while (offset < toIndex) {
            byte[] data = s.data;
            int limit = (int) Math.min((long) s.limit, (((long) s.pos) + toIndex) - offset);
            for (int pos = (int) ((((long) s.pos) + fromIndex) - offset); pos < limit; pos++) {
                if (data[pos] == b) {
                    return ((long) (pos - s.pos)) + offset;
                }
            }
            offset += (long) (s.limit - s.pos);
            fromIndex = offset;
            s = s.next;
        }
        return -1;
    }

    public long indexOf(ByteString bytes) throws IOException {
        return indexOf(bytes, 0);
    }

    public long indexOf(ByteString bytes, long fromIndex) throws IOException {
        long offset;
        if (bytes.size() == 0) {
            throw new IllegalArgumentException("bytes is empty");
        } else if (fromIndex < 0) {
            throw new IllegalArgumentException("fromIndex < 0");
        } else {
            Segment s = this.head;
            if (s == null) {
                return -1;
            }
            if (this.size - fromIndex >= fromIndex) {
                long offset2 = 0;
                while (true) {
                    long nextOffset = offset + ((long) (s.limit - s.pos));
                    if (nextOffset >= fromIndex) {
                        break;
                    }
                    s = s.next;
                    offset2 = nextOffset;
                }
            } else {
                offset = this.size;
                while (offset > fromIndex) {
                    s = s.prev;
                    offset -= (long) (s.limit - s.pos);
                }
            }
            byte b0 = bytes.getByte(0);
            int bytesSize = bytes.size();
            long resultLimit = (this.size - ((long) bytesSize)) + 1;
            while (offset < resultLimit) {
                byte[] data = s.data;
                int segmentLimit = (int) Math.min((long) s.limit, (((long) s.pos) + resultLimit) - offset);
                for (int pos = (int) ((((long) s.pos) + fromIndex) - offset); pos < segmentLimit; pos++) {
                    if (data[pos] == b0) {
                        if (rangeEquals(s, pos + 1, bytes, 1, bytesSize)) {
                            return ((long) (pos - s.pos)) + offset;
                        }
                    }
                }
                offset += (long) (s.limit - s.pos);
                fromIndex = offset;
                s = s.next;
            }
            return -1;
        }
    }

    public long indexOfElement(ByteString targetBytes) {
        return indexOfElement(targetBytes, 0);
    }

    public long indexOfElement(ByteString targetBytes, long fromIndex) {
        long offset;
        if (fromIndex < 0) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment s = this.head;
        if (s == null) {
            return -1;
        }
        if (this.size - fromIndex >= fromIndex) {
            long offset2 = 0;
            while (true) {
                long nextOffset = offset + ((long) (s.limit - s.pos));
                if (nextOffset >= fromIndex) {
                    break;
                }
                s = s.next;
                offset2 = nextOffset;
            }
        } else {
            offset = this.size;
            while (offset > fromIndex) {
                s = s.prev;
                offset -= (long) (s.limit - s.pos);
            }
        }
        if (targetBytes.size() == 2) {
            byte b0 = targetBytes.getByte(0);
            byte b1 = targetBytes.getByte(1);
            while (offset < this.size) {
                byte[] data = s.data;
                int limit = s.limit;
                for (int pos = (int) ((((long) s.pos) + fromIndex) - offset); pos < limit; pos++) {
                    byte b = data[pos];
                    if (b == b0 || b == b1) {
                        return ((long) (pos - s.pos)) + offset;
                    }
                }
                offset += (long) (s.limit - s.pos);
                fromIndex = offset;
                s = s.next;
            }
        } else {
            byte[] targetByteArray = targetBytes.internalArray();
            while (offset < this.size) {
                byte[] data2 = s.data;
                int limit2 = s.limit;
                for (int pos2 = (int) ((((long) s.pos) + fromIndex) - offset); pos2 < limit2; pos2++) {
                    byte b2 = data2[pos2];
                    for (byte t : targetByteArray) {
                        if (b2 == t) {
                            return ((long) (pos2 - s.pos)) + offset;
                        }
                    }
                }
                offset += (long) (s.limit - s.pos);
                fromIndex = offset;
                s = s.next;
            }
        }
        return -1;
    }

    public boolean rangeEquals(long offset, ByteString bytes) {
        return rangeEquals(offset, bytes, 0, bytes.size());
    }

    public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount) {
        if (offset < 0 || bytesOffset < 0 || byteCount < 0 || this.size - offset < ((long) byteCount) || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i = 0; i < byteCount; i++) {
            if (getByte(((long) i) + offset) != bytes.getByte(bytesOffset + i)) {
                return false;
            }
        }
        return true;
    }

    private boolean rangeEquals(Segment segment, int segmentPos, ByteString bytes, int bytesOffset, int bytesLimit) {
        int segmentLimit = segment.limit;
        byte[] data = segment.data;
        for (int i = bytesOffset; i < bytesLimit; i++) {
            if (segmentPos == segmentLimit) {
                segment = segment.next;
                data = segment.data;
                segmentPos = segment.pos;
                segmentLimit = segment.limit;
            }
            if (data[segmentPos] != bytes.getByte(i)) {
                return false;
            }
            segmentPos++;
        }
        return true;
    }

    public void flush() {
    }

    public boolean isOpen() {
        return true;
    }

    public void close() {
    }

    public Timeout timeout() {
        return Timeout.NONE;
    }

    /* access modifiers changed from: package-private */
    public List<Integer> segmentSizes() {
        if (this.head == null) {
            return Collections.emptyList();
        }
        List<Integer> result = new ArrayList<>();
        result.add(Integer.valueOf(this.head.limit - this.head.pos));
        for (Segment s = this.head.next; s != this.head; s = s.next) {
            result.add(Integer.valueOf(s.limit - s.pos));
        }
        return result;
    }

    public final ByteString md5() {
        return digest("MD5");
    }

    public final ByteString sha1() {
        return digest(McElieceCCA2KeyGenParameterSpec.SHA1);
    }

    public final ByteString sha256() {
        return digest(McElieceCCA2KeyGenParameterSpec.SHA256);
    }

    public final ByteString sha512() {
        return digest(McElieceCCA2KeyGenParameterSpec.SHA512);
    }

    private ByteString digest(String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            if (this.head != null) {
                messageDigest.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
                for (Segment s = this.head.next; s != this.head; s = s.next) {
                    messageDigest.update(s.data, s.pos, s.limit - s.pos);
                }
            }
            return ByteString.of(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
    }

    public final ByteString hmacSha1(ByteString key) {
        return hmac("HmacSHA1", key);
    }

    public final ByteString hmacSha256(ByteString key) {
        return hmac("HmacSHA256", key);
    }

    public final ByteString hmacSha512(ByteString key) {
        return hmac("HmacSHA512", key);
    }

    private ByteString hmac(String algorithm, ByteString key) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.toByteArray(), algorithm));
            if (this.head != null) {
                mac.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
                for (Segment s = this.head.next; s != this.head; s = s.next) {
                    mac.update(s.data, s.pos, s.limit - s.pos);
                }
            }
            return ByteString.of(mac.doFinal());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        } catch (InvalidKeyException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006c, code lost:
        if (r8 != r11.limit) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006e, code lost:
        r11 = r11.next;
        r5 = r11.pos;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0074, code lost:
        if (r10 != r12.limit) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0076, code lost:
        r12 = r12.next;
        r9 = r12.pos;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007a, code lost:
        r6 = r6 + r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007e, code lost:
        r9 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0080, code lost:
        r5 = r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0008
            r14 = 1
        L_0x0007:
            return r14
        L_0x0008:
            r0 = r19
            boolean r14 = r0 instanceof okio.Buffer
            if (r14 != 0) goto L_0x0010
            r14 = 0
            goto L_0x0007
        L_0x0010:
            r13 = r19
            okio.Buffer r13 = (okio.Buffer) r13
            r0 = r18
            long r14 = r0.size
            long r0 = r13.size
            r16 = r0
            int r14 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r14 == 0) goto L_0x0022
            r14 = 0
            goto L_0x0007
        L_0x0022:
            r0 = r18
            long r14 = r0.size
            r16 = 0
            int r14 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r14 != 0) goto L_0x002e
            r14 = 1
            goto L_0x0007
        L_0x002e:
            r0 = r18
            okio.Segment r11 = r0.head
            okio.Segment r12 = r13.head
            int r5 = r11.pos
            int r9 = r12.pos
            r6 = 0
        L_0x003a:
            r0 = r18
            long r14 = r0.size
            int r14 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r14 >= 0) goto L_0x007c
            int r14 = r11.limit
            int r14 = r14 - r5
            int r15 = r12.limit
            int r15 = r15 - r9
            int r14 = java.lang.Math.min(r14, r15)
            long r2 = (long) r14
            r4 = 0
            r10 = r9
            r8 = r5
        L_0x0050:
            long r14 = (long) r4
            int r14 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1))
            if (r14 >= 0) goto L_0x006a
            byte[] r14 = r11.data
            int r5 = r8 + 1
            byte r14 = r14[r8]
            byte[] r15 = r12.data
            int r9 = r10 + 1
            byte r15 = r15[r10]
            if (r14 == r15) goto L_0x0065
            r14 = 0
            goto L_0x0007
        L_0x0065:
            int r4 = r4 + 1
            r10 = r9
            r8 = r5
            goto L_0x0050
        L_0x006a:
            int r14 = r11.limit
            if (r8 != r14) goto L_0x0080
            okio.Segment r11 = r11.next
            int r5 = r11.pos
        L_0x0072:
            int r14 = r12.limit
            if (r10 != r14) goto L_0x007e
            okio.Segment r12 = r12.next
            int r9 = r12.pos
        L_0x007a:
            long r6 = r6 + r2
            goto L_0x003a
        L_0x007c:
            r14 = 1
            goto L_0x0007
        L_0x007e:
            r9 = r10
            goto L_0x007a
        L_0x0080:
            r5 = r8
            goto L_0x0072
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        Segment s = this.head;
        if (s == null) {
            return 0;
        }
        int result = 1;
        do {
            int limit = s.limit;
            for (int pos = s.pos; pos < limit; pos++) {
                result = (result * 31) + s.data[pos];
            }
            s = s.next;
        } while (s != this.head);
        return result;
    }

    public String toString() {
        return snapshot().toString();
    }

    public Buffer clone() {
        Buffer result = new Buffer();
        if (this.size != 0) {
            result.head = this.head.sharedCopy();
            Segment segment = result.head;
            Segment segment2 = result.head;
            Segment segment3 = result.head;
            segment2.prev = segment3;
            segment.next = segment3;
            for (Segment s = this.head.next; s != this.head; s = s.next) {
                result.head.prev.push(s.sharedCopy());
            }
            result.size = this.size;
        }
        return result;
    }

    public final ByteString snapshot() {
        if (this.size <= 2147483647L) {
            return snapshot((int) this.size);
        }
        throw new IllegalArgumentException("size > Integer.MAX_VALUE: " + this.size);
    }

    public final ByteString snapshot(int byteCount) {
        if (byteCount == 0) {
            return ByteString.EMPTY;
        }
        return new SegmentedByteString(this, byteCount);
    }

    public final UnsafeCursor readUnsafe() {
        return readUnsafe(new UnsafeCursor());
    }

    public final UnsafeCursor readUnsafe(UnsafeCursor unsafeCursor) {
        if (unsafeCursor.buffer != null) {
            throw new IllegalStateException("already attached to a buffer");
        }
        unsafeCursor.buffer = this;
        unsafeCursor.readWrite = false;
        return unsafeCursor;
    }

    public final UnsafeCursor readAndWriteUnsafe() {
        return readAndWriteUnsafe(new UnsafeCursor());
    }

    public final UnsafeCursor readAndWriteUnsafe(UnsafeCursor unsafeCursor) {
        if (unsafeCursor.buffer != null) {
            throw new IllegalStateException("already attached to a buffer");
        }
        unsafeCursor.buffer = this;
        unsafeCursor.readWrite = true;
        return unsafeCursor;
    }

    public static final class UnsafeCursor implements Closeable {
        public Buffer buffer;
        public byte[] data;
        public int end = -1;
        public long offset = -1;
        public boolean readWrite;
        private Segment segment;
        public int start = -1;

        public final int next() {
            if (this.offset == this.buffer.size) {
                throw new IllegalStateException();
            } else if (this.offset == -1) {
                return seek(0);
            } else {
                return seek(this.offset + ((long) (this.end - this.start)));
            }
        }

        public final int seek(long offset2) {
            long nextOffset;
            Segment next;
            if (offset2 < -1 || offset2 > this.buffer.size) {
                throw new ArrayIndexOutOfBoundsException(String.format("offset=%s > size=%s", Long.valueOf(offset2), Long.valueOf(this.buffer.size)));
            } else if (offset2 == -1 || offset2 == this.buffer.size) {
                this.segment = null;
                this.offset = offset2;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return -1;
            } else {
                long min = 0;
                long max = this.buffer.size;
                Segment head = this.buffer.head;
                Segment tail = this.buffer.head;
                if (this.segment != null) {
                    long segmentOffset = this.offset - ((long) (this.start - this.segment.pos));
                    if (segmentOffset > offset2) {
                        max = segmentOffset;
                        tail = this.segment;
                    } else {
                        min = segmentOffset;
                        head = this.segment;
                    }
                }
                if (max - offset2 > offset2 - min) {
                    next = head;
                    nextOffset = min;
                    while (offset2 >= ((long) (next.limit - next.pos)) + nextOffset) {
                        nextOffset += (long) (next.limit - next.pos);
                        next = next.next;
                    }
                } else {
                    Segment next2 = tail;
                    long nextOffset2 = max;
                    while (nextOffset > offset2) {
                        next2 = next.prev;
                        nextOffset2 = nextOffset - ((long) (next2.limit - next2.pos));
                    }
                }
                if (this.readWrite && next.shared) {
                    Segment unsharedNext = next.unsharedCopy();
                    if (this.buffer.head == next) {
                        this.buffer.head = unsharedNext;
                    }
                    next = next.push(unsharedNext);
                    next.prev.pop();
                }
                this.segment = next;
                this.offset = offset2;
                this.data = next.data;
                this.start = next.pos + ((int) (offset2 - nextOffset));
                this.end = next.limit;
                return this.end - this.start;
            }
        }

        public final long resizeBuffer(long newSize) {
            if (this.buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            } else if (!this.readWrite) {
                throw new IllegalStateException("resizeBuffer() only permitted for read/write buffers");
            } else {
                long oldSize = this.buffer.size;
                if (newSize <= oldSize) {
                    if (newSize < 0) {
                        throw new IllegalArgumentException("newSize < 0: " + newSize);
                    }
                    long bytesToSubtract = oldSize - newSize;
                    while (true) {
                        if (bytesToSubtract <= 0) {
                            break;
                        }
                        Segment tail = this.buffer.head.prev;
                        int tailSize = tail.limit - tail.pos;
                        if (((long) tailSize) > bytesToSubtract) {
                            tail.limit = (int) (((long) tail.limit) - bytesToSubtract);
                            break;
                        }
                        this.buffer.head = tail.pop();
                        SegmentPool.recycle(tail);
                        bytesToSubtract -= (long) tailSize;
                    }
                    this.segment = null;
                    this.offset = newSize;
                    this.data = null;
                    this.start = -1;
                    this.end = -1;
                } else if (newSize > oldSize) {
                    boolean needsToSeek = true;
                    long bytesToAdd = newSize - oldSize;
                    while (bytesToAdd > 0) {
                        Segment tail2 = this.buffer.writableSegment(1);
                        int segmentBytesToAdd = (int) Math.min(bytesToAdd, (long) (8192 - tail2.limit));
                        tail2.limit += segmentBytesToAdd;
                        bytesToAdd -= (long) segmentBytesToAdd;
                        if (needsToSeek) {
                            this.segment = tail2;
                            this.offset = oldSize;
                            this.data = tail2.data;
                            this.start = tail2.limit - segmentBytesToAdd;
                            this.end = tail2.limit;
                            needsToSeek = false;
                        }
                    }
                }
                this.buffer.size = newSize;
                return oldSize;
            }
        }

        public final long expandBuffer(int minByteCount) {
            if (minByteCount <= 0) {
                throw new IllegalArgumentException("minByteCount <= 0: " + minByteCount);
            } else if (minByteCount > 8192) {
                throw new IllegalArgumentException("minByteCount > Segment.SIZE: " + minByteCount);
            } else if (this.buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            } else if (!this.readWrite) {
                throw new IllegalStateException("expandBuffer() only permitted for read/write buffers");
            } else {
                long oldSize = this.buffer.size;
                Segment tail = this.buffer.writableSegment(minByteCount);
                int result = 8192 - tail.limit;
                tail.limit = 8192;
                this.buffer.size = ((long) result) + oldSize;
                this.segment = tail;
                this.offset = oldSize;
                this.data = tail.data;
                this.start = 8192 - result;
                this.end = 8192;
                return (long) result;
            }
        }

        public void close() {
            if (this.buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            }
            this.buffer = null;
            this.segment = null;
            this.offset = -1;
            this.data = null;
            this.start = -1;
            this.end = -1;
        }
    }
}
