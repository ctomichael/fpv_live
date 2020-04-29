package com.squareup.wire;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.LongCompanionObject;
import okio.BufferedSource;
import okio.ByteString;
import org.bouncycastle.asn1.eac.CertificateBody;

public final class ProtoReader {
    private static final int FIELD_ENCODING_MASK = 7;
    private static final int RECURSION_LIMIT = 65;
    private static final int STATE_END_GROUP = 4;
    private static final int STATE_FIXED32 = 5;
    private static final int STATE_FIXED64 = 1;
    private static final int STATE_LENGTH_DELIMITED = 2;
    private static final int STATE_PACKED_TAG = 7;
    private static final int STATE_START_GROUP = 3;
    private static final int STATE_TAG = 6;
    private static final int STATE_VARINT = 0;
    static final int TAG_FIELD_ENCODING_BITS = 3;
    private long limit = LongCompanionObject.MAX_VALUE;
    private FieldEncoding nextFieldEncoding;
    private long pos = 0;
    private long pushedLimit = -1;
    private int recursionDepth;
    private final BufferedSource source;
    private int state = 2;
    private int tag = -1;

    public ProtoReader(BufferedSource source2) {
        this.source = source2;
    }

    public long beginMessage() throws IOException {
        if (this.state != 2) {
            throw new IllegalStateException("Unexpected call to beginMessage()");
        }
        int i = this.recursionDepth + 1;
        this.recursionDepth = i;
        if (i > 65) {
            throw new IOException("Wire recursion limit exceeded");
        }
        long token = this.pushedLimit;
        this.pushedLimit = -1;
        this.state = 6;
        return token;
    }

    public void endMessage(long token) throws IOException {
        if (this.state != 6) {
            throw new IllegalStateException("Unexpected call to endMessage()");
        }
        int i = this.recursionDepth - 1;
        this.recursionDepth = i;
        if (i < 0 || this.pushedLimit != -1) {
            throw new IllegalStateException("No corresponding call to beginMessage()");
        } else if (this.pos == this.limit || this.recursionDepth == 0) {
            this.limit = token;
        } else {
            throw new IOException("Expected to end at " + this.limit + " but was " + this.pos);
        }
    }

    public int nextTag() throws IOException {
        if (this.state == 7) {
            this.state = 2;
            return this.tag;
        } else if (this.state != 6) {
            throw new IllegalStateException("Unexpected call to nextTag()");
        } else {
            while (this.pos < this.limit && !this.source.exhausted()) {
                int tagAndFieldEncoding = internalReadVarint32();
                if (tagAndFieldEncoding == 0) {
                    throw new ProtocolException("Unexpected tag 0");
                }
                this.tag = tagAndFieldEncoding >> 3;
                int groupOrFieldEncoding = tagAndFieldEncoding & 7;
                switch (groupOrFieldEncoding) {
                    case 0:
                        this.nextFieldEncoding = FieldEncoding.VARINT;
                        this.state = 0;
                        return this.tag;
                    case 1:
                        this.nextFieldEncoding = FieldEncoding.FIXED64;
                        this.state = 1;
                        return this.tag;
                    case 2:
                        this.nextFieldEncoding = FieldEncoding.LENGTH_DELIMITED;
                        this.state = 2;
                        int length = internalReadVarint32();
                        if (length < 0) {
                            throw new ProtocolException("Negative length: " + length);
                        } else if (this.pushedLimit != -1) {
                            throw new IllegalStateException();
                        } else {
                            this.pushedLimit = this.limit;
                            this.limit = this.pos + ((long) length);
                            if (this.limit <= this.pushedLimit) {
                                return this.tag;
                            }
                            throw new EOFException();
                        }
                    case 3:
                        skipGroup(this.tag);
                    case 4:
                        throw new ProtocolException("Unexpected end group");
                    case 5:
                        this.nextFieldEncoding = FieldEncoding.FIXED32;
                        this.state = 5;
                        return this.tag;
                    default:
                        throw new ProtocolException("Unexpected field encoding: " + groupOrFieldEncoding);
                }
            }
            return -1;
        }
    }

    public FieldEncoding peekFieldEncoding() {
        return this.nextFieldEncoding;
    }

    public void skip() throws IOException {
        switch (this.state) {
            case 0:
                readVarint64();
                return;
            case 1:
                readFixed64();
                return;
            case 2:
                this.source.skip(beforeLengthDelimitedScalar());
                return;
            case 3:
            case 4:
            default:
                throw new IllegalStateException("Unexpected call to skip()");
            case 5:
                readFixed32();
                return;
        }
    }

    private void skipGroup(int expectedEndTag) throws IOException {
        while (this.pos < this.limit && !this.source.exhausted()) {
            int tagAndFieldEncoding = internalReadVarint32();
            if (tagAndFieldEncoding == 0) {
                throw new ProtocolException("Unexpected tag 0");
            }
            int tag2 = tagAndFieldEncoding >> 3;
            int groupOrFieldEncoding = tagAndFieldEncoding & 7;
            switch (groupOrFieldEncoding) {
                case 0:
                    this.state = 0;
                    readVarint64();
                    break;
                case 1:
                    this.state = 1;
                    readFixed64();
                    break;
                case 2:
                    int length = internalReadVarint32();
                    this.pos += (long) length;
                    this.source.skip((long) length);
                    break;
                case 3:
                    skipGroup(tag2);
                    break;
                case 4:
                    if (tag2 != expectedEndTag) {
                        throw new ProtocolException("Unexpected end group");
                    }
                    return;
                case 5:
                    this.state = 5;
                    readFixed32();
                    break;
                default:
                    throw new ProtocolException("Unexpected field encoding: " + groupOrFieldEncoding);
            }
        }
        throw new EOFException();
    }

    public ByteString readBytes() throws IOException {
        return this.source.readByteString(beforeLengthDelimitedScalar());
    }

    public String readString() throws IOException {
        return this.source.readUtf8(beforeLengthDelimitedScalar());
    }

    public int readVarint32() throws IOException {
        if (this.state == 0 || this.state == 2) {
            int result = internalReadVarint32();
            afterPackableScalar(0);
            return result;
        }
        throw new ProtocolException("Expected VARINT or LENGTH_DELIMITED but was " + this.state);
    }

    private int internalReadVarint32() throws IOException {
        int result;
        this.pos++;
        int tmp = this.source.readByte();
        if (tmp >= 0) {
            return tmp;
        }
        int result2 = tmp & CertificateBody.profileType;
        this.pos++;
        byte tmp2 = this.source.readByte();
        if (tmp2 >= 0) {
            result = result2 | (tmp2 << 7);
        } else {
            int result3 = result2 | ((tmp2 & ByteCompanionObject.MAX_VALUE) << 7);
            this.pos++;
            tmp2 = this.source.readByte();
            if (tmp2 >= 0) {
                result = result3 | (tmp2 << 14);
            } else {
                int result4 = result3 | ((tmp2 & ByteCompanionObject.MAX_VALUE) << 14);
                this.pos++;
                tmp2 = this.source.readByte();
                if (tmp2 >= 0) {
                    result = result4 | (tmp2 << 21);
                } else {
                    int result5 = result4 | ((tmp2 & ByteCompanionObject.MAX_VALUE) << 21);
                    this.pos++;
                    tmp2 = this.source.readByte();
                    result = result5 | (tmp2 << 28);
                    if (tmp2 < 0) {
                        for (int i = 0; i < 5; i++) {
                            this.pos++;
                            if (this.source.readByte() >= 0) {
                                return result;
                            }
                        }
                        throw new ProtocolException("Malformed VARINT");
                    }
                }
            }
        }
        return result;
    }

    public long readVarint64() throws IOException {
        if (this.state == 0 || this.state == 2) {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                this.pos++;
                byte b = this.source.readByte();
                result |= ((long) (b & ByteCompanionObject.MAX_VALUE)) << shift;
                if ((b & 128) == 0) {
                    afterPackableScalar(0);
                    return result;
                }
            }
            throw new ProtocolException("WireInput encountered a malformed varint");
        }
        throw new ProtocolException("Expected VARINT or LENGTH_DELIMITED but was " + this.state);
    }

    public int readFixed32() throws IOException {
        if (this.state == 5 || this.state == 2) {
            this.source.require(4);
            this.pos += 4;
            int result = this.source.readIntLe();
            afterPackableScalar(5);
            return result;
        }
        throw new ProtocolException("Expected FIXED32 or LENGTH_DELIMITED but was " + this.state);
    }

    public long readFixed64() throws IOException {
        if (this.state == 1 || this.state == 2) {
            this.source.require(8);
            this.pos += 8;
            long result = this.source.readLongLe();
            afterPackableScalar(1);
            return result;
        }
        throw new ProtocolException("Expected FIXED64 or LENGTH_DELIMITED but was " + this.state);
    }

    private void afterPackableScalar(int fieldEncoding) throws IOException {
        if (this.state == fieldEncoding) {
            this.state = 6;
        } else if (this.pos > this.limit) {
            throw new IOException("Expected to end at " + this.limit + " but was " + this.pos);
        } else if (this.pos == this.limit) {
            this.limit = this.pushedLimit;
            this.pushedLimit = -1;
            this.state = 6;
        } else {
            this.state = 7;
        }
    }

    private long beforeLengthDelimitedScalar() throws IOException {
        if (this.state != 2) {
            throw new ProtocolException("Expected LENGTH_DELIMITED but was " + this.state);
        }
        long byteCount = this.limit - this.pos;
        this.source.require(byteCount);
        this.state = 6;
        this.pos = this.limit;
        this.limit = this.pushedLimit;
        this.pushedLimit = -1;
        return byteCount;
    }
}
