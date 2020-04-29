package com.squareup.wire;

import java.io.IOException;
import okio.BufferedSink;
import okio.ByteString;
import org.bouncycastle.asn1.eac.CertificateBody;

public final class ProtoWriter {
    private final BufferedSink sink;

    private static int makeTag(int fieldNumber, FieldEncoding fieldEncoding) {
        return (fieldNumber << 3) | fieldEncoding.value;
    }

    static int tagSize(int tag) {
        return varint32Size(makeTag(tag, FieldEncoding.VARINT));
    }

    static int utf8Length(String s) {
        int result = 0;
        int i = 0;
        int length = s.length();
        while (i < length) {
            char c = s.charAt(i);
            if (c < 128) {
                result++;
            } else if (c < 2048) {
                result += 2;
            } else if (c < 55296 || c > 57343) {
                result += 3;
            } else if (c > 56319 || i + 1 >= length || s.charAt(i + 1) < 56320 || s.charAt(i + 1) > 57343) {
                result++;
            } else {
                result += 4;
                i++;
            }
            i++;
        }
        return result;
    }

    static int int32Size(int value) {
        if (value >= 0) {
            return varint32Size(value);
        }
        return 10;
    }

    static int varint32Size(int value) {
        if ((value & -128) == 0) {
            return 1;
        }
        if ((value & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & value) == 0) {
            return 3;
        }
        if ((-268435456 & value) == 0) {
            return 4;
        }
        return 5;
    }

    static int varint64Size(long value) {
        if ((-128 & value) == 0) {
            return 1;
        }
        if ((-16384 & value) == 0) {
            return 2;
        }
        if ((-2097152 & value) == 0) {
            return 3;
        }
        if ((-268435456 & value) == 0) {
            return 4;
        }
        if ((-34359738368L & value) == 0) {
            return 5;
        }
        if ((-4398046511104L & value) == 0) {
            return 6;
        }
        if ((-562949953421312L & value) == 0) {
            return 7;
        }
        if ((-72057594037927936L & value) == 0) {
            return 8;
        }
        if ((Long.MIN_VALUE & value) == 0) {
            return 9;
        }
        return 10;
    }

    static int encodeZigZag32(int n) {
        return (n << 1) ^ (n >> 31);
    }

    static int decodeZigZag32(int n) {
        return (n >>> 1) ^ (-(n & 1));
    }

    static long encodeZigZag64(long n) {
        return (n << 1) ^ (n >> 63);
    }

    static long decodeZigZag64(long n) {
        return (n >>> 1) ^ (-(1 & n));
    }

    public ProtoWriter(BufferedSink sink2) {
        this.sink = sink2;
    }

    public void writeBytes(ByteString value) throws IOException {
        this.sink.write(value);
    }

    public void writeString(String value) throws IOException {
        this.sink.writeUtf8(value);
    }

    public void writeTag(int fieldNumber, FieldEncoding fieldEncoding) throws IOException {
        writeVarint32(makeTag(fieldNumber, fieldEncoding));
    }

    /* access modifiers changed from: package-private */
    public void writeSignedVarint32(int value) throws IOException {
        if (value >= 0) {
            writeVarint32(value);
        } else {
            writeVarint64((long) value);
        }
    }

    public void writeVarint32(int value) throws IOException {
        while ((value & -128) != 0) {
            this.sink.writeByte((value & CertificateBody.profileType) | 128);
            value >>>= 7;
        }
        this.sink.writeByte(value);
    }

    public void writeVarint64(long value) throws IOException {
        while ((-128 & value) != 0) {
            this.sink.writeByte((((int) value) & CertificateBody.profileType) | 128);
            value >>>= 7;
        }
        this.sink.writeByte((int) value);
    }

    public void writeFixed32(int value) throws IOException {
        this.sink.writeIntLe(value);
    }

    public void writeFixed64(long value) throws IOException {
        this.sink.writeLongLe(value);
    }
}
