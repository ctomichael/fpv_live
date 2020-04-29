package com.google.protobuf;

import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import kotlin.jvm.internal.ByteCompanionObject;

abstract class BinaryReader implements Reader {
    private static final int FIXED32_MULTIPLE_MASK = 3;
    private static final int FIXED64_MULTIPLE_MASK = 7;

    public abstract int getTotalBytesRead();

    public static BinaryReader newInstance(ByteBuffer buffer, boolean bufferIsImmutable) {
        if (buffer.hasArray()) {
            return new SafeHeapReader(buffer, bufferIsImmutable);
        }
        throw new IllegalArgumentException("Direct buffers not yet supported");
    }

    private BinaryReader() {
    }

    public boolean shouldDiscardUnknownFields() {
        return false;
    }

    private static final class SafeHeapReader extends BinaryReader {
        private final byte[] buffer;
        private final boolean bufferIsImmutable;
        private int endGroupTag;
        private final int initialPos;
        private int limit;
        private int pos;
        private int tag;

        public SafeHeapReader(ByteBuffer bytebuf, boolean bufferIsImmutable2) {
            super();
            this.bufferIsImmutable = bufferIsImmutable2;
            this.buffer = bytebuf.array();
            int arrayOffset = bytebuf.arrayOffset() + bytebuf.position();
            this.pos = arrayOffset;
            this.initialPos = arrayOffset;
            this.limit = bytebuf.arrayOffset() + bytebuf.limit();
        }

        private boolean isAtEnd() {
            return this.pos == this.limit;
        }

        public int getTotalBytesRead() {
            return this.pos - this.initialPos;
        }

        public int getFieldNumber() throws IOException {
            if (isAtEnd()) {
                return Integer.MAX_VALUE;
            }
            this.tag = readVarint32();
            if (this.tag != this.endGroupTag) {
                return WireFormat.getTagFieldNumber(this.tag);
            }
            return Integer.MAX_VALUE;
        }

        public int getTag() {
            return this.tag;
        }

        public boolean skipField() throws IOException {
            if (isAtEnd() || this.tag == this.endGroupTag) {
                return false;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    skipVarint();
                    return true;
                case 1:
                    skipBytes(8);
                    return true;
                case 2:
                    skipBytes(readVarint32());
                    return true;
                case 3:
                    skipGroup();
                    return true;
                case 4:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 5:
                    skipBytes(4);
                    return true;
            }
        }

        public double readDouble() throws IOException {
            requireWireType(1);
            return Double.longBitsToDouble(readLittleEndian64());
        }

        public float readFloat() throws IOException {
            requireWireType(5);
            return Float.intBitsToFloat(readLittleEndian32());
        }

        public long readUInt64() throws IOException {
            requireWireType(0);
            return readVarint64();
        }

        public long readInt64() throws IOException {
            requireWireType(0);
            return readVarint64();
        }

        public int readInt32() throws IOException {
            requireWireType(0);
            return readVarint32();
        }

        public long readFixed64() throws IOException {
            requireWireType(1);
            return readLittleEndian64();
        }

        public int readFixed32() throws IOException {
            requireWireType(5);
            return readLittleEndian32();
        }

        public boolean readBool() throws IOException {
            requireWireType(0);
            if (readVarint32() != 0) {
                return true;
            }
            return false;
        }

        public String readString() throws IOException {
            return readStringInternal(false);
        }

        public String readStringRequireUtf8() throws IOException {
            return readStringInternal(true);
        }

        public String readStringInternal(boolean requireUtf8) throws IOException {
            requireWireType(2);
            int size = readVarint32();
            if (size == 0) {
                return "";
            }
            requireBytes(size);
            if (!requireUtf8 || Utf8.isValidUtf8(this.buffer, this.pos, this.pos + size)) {
                String str = new String(this.buffer, this.pos, size, Internal.UTF_8);
                this.pos += size;
                return str;
            }
            throw InvalidProtocolBufferException.invalidUtf8();
        }

        public <T> T readMessage(Class<T> clazz, ExtensionRegistryLite extensionRegistry) throws IOException {
            requireWireType(2);
            return readMessage(Protobuf.getInstance().schemaFor((Class) clazz), extensionRegistry);
        }

        public <T> T readMessageBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
            requireWireType(2);
            return readMessage(schema, extensionRegistry);
        }

        private <T> T readMessage(Schema<T> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
            int size = readVarint32();
            requireBytes(size);
            int prevLimit = this.limit;
            int newLimit = this.pos + size;
            this.limit = newLimit;
            try {
                T message = schema.newInstance();
                schema.mergeFrom(message, this, extensionRegistry);
                schema.makeImmutable(message);
                if (this.pos == newLimit) {
                    return message;
                }
                throw InvalidProtocolBufferException.parseFailure();
            } finally {
                this.limit = prevLimit;
            }
        }

        public <T> T readGroup(Class<T> clazz, ExtensionRegistryLite extensionRegistry) throws IOException {
            requireWireType(3);
            return readGroup(Protobuf.getInstance().schemaFor((Class) clazz), extensionRegistry);
        }

        public <T> T readGroupBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
            requireWireType(3);
            return readGroup(schema, extensionRegistry);
        }

        private <T> T readGroup(Schema<T> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
            int prevEndGroupTag = this.endGroupTag;
            this.endGroupTag = WireFormat.makeTag(WireFormat.getTagFieldNumber(this.tag), 4);
            try {
                T message = schema.newInstance();
                schema.mergeFrom(message, this, extensionRegistry);
                schema.makeImmutable(message);
                if (this.tag == this.endGroupTag) {
                    return message;
                }
                throw InvalidProtocolBufferException.parseFailure();
            } finally {
                this.endGroupTag = prevEndGroupTag;
            }
        }

        public ByteString readBytes() throws IOException {
            ByteString bytes;
            requireWireType(2);
            int size = readVarint32();
            if (size == 0) {
                return ByteString.EMPTY;
            }
            requireBytes(size);
            if (this.bufferIsImmutable) {
                bytes = ByteString.wrap(this.buffer, this.pos, size);
            } else {
                bytes = ByteString.copyFrom(this.buffer, this.pos, size);
            }
            this.pos += size;
            return bytes;
        }

        public int readUInt32() throws IOException {
            requireWireType(0);
            return readVarint32();
        }

        public int readEnum() throws IOException {
            requireWireType(0);
            return readVarint32();
        }

        public int readSFixed32() throws IOException {
            requireWireType(5);
            return readLittleEndian32();
        }

        public long readSFixed64() throws IOException {
            requireWireType(1);
            return readLittleEndian64();
        }

        public int readSInt32() throws IOException {
            requireWireType(0);
            return CodedInputStream.decodeZigZag32(readVarint32());
        }

        public long readSInt64() throws IOException {
            requireWireType(0);
            return CodedInputStream.decodeZigZag64(readVarint64());
        }

        public void readDoubleList(List<Double> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof DoubleArrayList) {
                DoubleArrayList plist = (DoubleArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 1:
                        break;
                    case 2:
                        int bytes = readVarint32();
                        verifyPackedFixed64Length(bytes);
                        int fieldEndPos = this.pos + bytes;
                        while (this.pos < fieldEndPos) {
                            plist.addDouble(Double.longBitsToDouble(readLittleEndian64_NoCheck()));
                        }
                        return;
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                }
                do {
                    plist.addDouble(readDouble());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 1:
                    break;
                case 2:
                    int bytes2 = readVarint32();
                    verifyPackedFixed64Length(bytes2);
                    int fieldEndPos2 = this.pos + bytes2;
                    while (this.pos < fieldEndPos2) {
                        target.add(Double.valueOf(Double.longBitsToDouble(readLittleEndian64_NoCheck())));
                    }
                    return;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
            do {
                target.add(Double.valueOf(readDouble()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readFloatList(List<Float> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof FloatArrayList) {
                FloatArrayList plist = (FloatArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 2:
                        int bytes = readVarint32();
                        verifyPackedFixed32Length(bytes);
                        int fieldEndPos = this.pos + bytes;
                        while (this.pos < fieldEndPos) {
                            plist.addFloat(Float.intBitsToFloat(readLittleEndian32_NoCheck()));
                        }
                        return;
                    case 3:
                    case 4:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 5:
                        break;
                }
                do {
                    plist.addFloat(readFloat());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 2:
                    int bytes2 = readVarint32();
                    verifyPackedFixed32Length(bytes2);
                    int fieldEndPos2 = this.pos + bytes2;
                    while (this.pos < fieldEndPos2) {
                        target.add(Float.valueOf(Float.intBitsToFloat(readLittleEndian32_NoCheck())));
                    }
                    return;
                case 3:
                case 4:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 5:
                    break;
            }
            do {
                target.add(Float.valueOf(readFloat()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readUInt64List(List<Long> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof LongArrayList) {
                LongArrayList plist = (LongArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addLong(readVarint64());
                        }
                        requirePosition(fieldEndPos);
                        return;
                }
                do {
                    plist.addLong(readUInt64());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Long.valueOf(readVarint64()));
                    }
                    requirePosition(fieldEndPos2);
                    return;
            }
            do {
                target.add(Long.valueOf(readUInt64()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readInt64List(List<Long> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof LongArrayList) {
                LongArrayList plist = (LongArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addLong(readVarint64());
                        }
                        requirePosition(fieldEndPos);
                        return;
                }
                do {
                    plist.addLong(readInt64());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Long.valueOf(readVarint64()));
                    }
                    requirePosition(fieldEndPos2);
                    return;
            }
            do {
                target.add(Long.valueOf(readInt64()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readInt32List(List<Integer> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof IntArrayList) {
                IntArrayList plist = (IntArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addInt(readVarint32());
                        }
                        requirePosition(fieldEndPos);
                        return;
                }
                do {
                    plist.addInt(readInt32());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Integer.valueOf(readVarint32()));
                    }
                    requirePosition(fieldEndPos2);
                    return;
            }
            do {
                target.add(Integer.valueOf(readInt32()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readFixed64List(List<Long> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof LongArrayList) {
                LongArrayList plist = (LongArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 1:
                        break;
                    case 2:
                        int bytes = readVarint32();
                        verifyPackedFixed64Length(bytes);
                        int fieldEndPos = this.pos + bytes;
                        while (this.pos < fieldEndPos) {
                            plist.addLong(readLittleEndian64_NoCheck());
                        }
                        return;
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                }
                do {
                    plist.addLong(readFixed64());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 1:
                    break;
                case 2:
                    int bytes2 = readVarint32();
                    verifyPackedFixed64Length(bytes2);
                    int fieldEndPos2 = this.pos + bytes2;
                    while (this.pos < fieldEndPos2) {
                        target.add(Long.valueOf(readLittleEndian64_NoCheck()));
                    }
                    return;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
            do {
                target.add(Long.valueOf(readFixed64()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readFixed32List(List<Integer> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof IntArrayList) {
                IntArrayList plist = (IntArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 2:
                        int bytes = readVarint32();
                        verifyPackedFixed32Length(bytes);
                        int fieldEndPos = this.pos + bytes;
                        while (this.pos < fieldEndPos) {
                            plist.addInt(readLittleEndian32_NoCheck());
                        }
                        return;
                    case 3:
                    case 4:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 5:
                        break;
                }
                do {
                    plist.addInt(readFixed32());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 2:
                    int bytes2 = readVarint32();
                    verifyPackedFixed32Length(bytes2);
                    int fieldEndPos2 = this.pos + bytes2;
                    while (this.pos < fieldEndPos2) {
                        target.add(Integer.valueOf(readLittleEndian32_NoCheck()));
                    }
                    return;
                case 3:
                case 4:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 5:
                    break;
            }
            do {
                target.add(Integer.valueOf(readFixed32()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readBoolList(List<Boolean> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof BooleanArrayList) {
                BooleanArrayList plist = (BooleanArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addBoolean(readVarint32() != 0);
                        }
                        requirePosition(fieldEndPos);
                        return;
                }
                do {
                    plist.addBoolean(readBool());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Boolean.valueOf(readVarint32() != 0));
                    }
                    requirePosition(fieldEndPos2);
                    return;
            }
            do {
                target.add(Boolean.valueOf(readBool()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readStringList(List<String> target) throws IOException {
            readStringListInternal(target, false);
        }

        public void readStringListRequireUtf8(List<String> target) throws IOException {
            readStringListInternal(target, true);
        }

        public void readStringListInternal(List<String> target, boolean requireUtf8) throws IOException {
            int prevPos;
            int prevPos2;
            if (WireFormat.getTagWireType(this.tag) != 2) {
                throw InvalidProtocolBufferException.invalidWireType();
            } else if (!(target instanceof LazyStringList) || requireUtf8) {
                do {
                    target.add(readStringInternal(requireUtf8));
                    if (!isAtEnd()) {
                        prevPos = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos;
            } else {
                LazyStringList lazyList = (LazyStringList) target;
                do {
                    lazyList.add(readBytes());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
            }
        }

        public <T> void readMessageList(List<T> target, Class<T> targetType, ExtensionRegistryLite extensionRegistry) throws IOException {
            readMessageList(target, Protobuf.getInstance().schemaFor((Class) targetType), extensionRegistry);
        }

        public <T> void readMessageList(List<T> target, Schema<T> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
            int prevPos;
            if (WireFormat.getTagWireType(this.tag) != 2) {
                throw InvalidProtocolBufferException.invalidWireType();
            }
            int listTag = this.tag;
            do {
                target.add(readMessage(schema, extensionRegistry));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == listTag);
            this.pos = prevPos;
        }

        public <T> void readGroupList(List<T> target, Class<T> targetType, ExtensionRegistryLite extensionRegistry) throws IOException {
            readGroupList(target, Protobuf.getInstance().schemaFor((Class) targetType), extensionRegistry);
        }

        public <T> void readGroupList(List<T> target, Schema<T> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
            int prevPos;
            if (WireFormat.getTagWireType(this.tag) != 3) {
                throw InvalidProtocolBufferException.invalidWireType();
            }
            int listTag = this.tag;
            do {
                target.add(readGroup(schema, extensionRegistry));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == listTag);
            this.pos = prevPos;
        }

        public void readBytesList(List<ByteString> target) throws IOException {
            int prevPos;
            if (WireFormat.getTagWireType(this.tag) != 2) {
                throw InvalidProtocolBufferException.invalidWireType();
            }
            do {
                target.add(readBytes());
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readUInt32List(List<Integer> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof IntArrayList) {
                IntArrayList plist = (IntArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addInt(readVarint32());
                        }
                        return;
                }
                do {
                    plist.addInt(readUInt32());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Integer.valueOf(readVarint32()));
                    }
                    return;
            }
            do {
                target.add(Integer.valueOf(readUInt32()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readEnumList(List<Integer> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof IntArrayList) {
                IntArrayList plist = (IntArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addInt(readVarint32());
                        }
                        return;
                }
                do {
                    plist.addInt(readEnum());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Integer.valueOf(readVarint32()));
                    }
                    return;
            }
            do {
                target.add(Integer.valueOf(readEnum()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readSFixed32List(List<Integer> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof IntArrayList) {
                IntArrayList plist = (IntArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 2:
                        int bytes = readVarint32();
                        verifyPackedFixed32Length(bytes);
                        int fieldEndPos = this.pos + bytes;
                        while (this.pos < fieldEndPos) {
                            plist.addInt(readLittleEndian32_NoCheck());
                        }
                        return;
                    case 3:
                    case 4:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 5:
                        break;
                }
                do {
                    plist.addInt(readSFixed32());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 2:
                    int bytes2 = readVarint32();
                    verifyPackedFixed32Length(bytes2);
                    int fieldEndPos2 = this.pos + bytes2;
                    while (this.pos < fieldEndPos2) {
                        target.add(Integer.valueOf(readLittleEndian32_NoCheck()));
                    }
                    return;
                case 3:
                case 4:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 5:
                    break;
            }
            do {
                target.add(Integer.valueOf(readSFixed32()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readSFixed64List(List<Long> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof LongArrayList) {
                LongArrayList plist = (LongArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 1:
                        break;
                    case 2:
                        int bytes = readVarint32();
                        verifyPackedFixed64Length(bytes);
                        int fieldEndPos = this.pos + bytes;
                        while (this.pos < fieldEndPos) {
                            plist.addLong(readLittleEndian64_NoCheck());
                        }
                        return;
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                }
                do {
                    plist.addLong(readSFixed64());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 1:
                    break;
                case 2:
                    int bytes2 = readVarint32();
                    verifyPackedFixed64Length(bytes2);
                    int fieldEndPos2 = this.pos + bytes2;
                    while (this.pos < fieldEndPos2) {
                        target.add(Long.valueOf(readLittleEndian64_NoCheck()));
                    }
                    return;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
            do {
                target.add(Long.valueOf(readSFixed64()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readSInt32List(List<Integer> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof IntArrayList) {
                IntArrayList plist = (IntArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addInt(CodedInputStream.decodeZigZag32(readVarint32()));
                        }
                        return;
                }
                do {
                    plist.addInt(readSInt32());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Integer.valueOf(CodedInputStream.decodeZigZag32(readVarint32())));
                    }
                    return;
            }
            do {
                target.add(Integer.valueOf(readSInt32()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        public void readSInt64List(List<Long> target) throws IOException {
            int prevPos;
            int prevPos2;
            if (target instanceof LongArrayList) {
                LongArrayList plist = (LongArrayList) target;
                switch (WireFormat.getTagWireType(this.tag)) {
                    case 0:
                        break;
                    case 1:
                    default:
                        throw InvalidProtocolBufferException.invalidWireType();
                    case 2:
                        int fieldEndPos = this.pos + readVarint32();
                        while (this.pos < fieldEndPos) {
                            plist.addLong(CodedInputStream.decodeZigZag64(readVarint64()));
                        }
                        return;
                }
                do {
                    plist.addLong(readSInt64());
                    if (!isAtEnd()) {
                        prevPos2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = prevPos2;
                return;
            }
            switch (WireFormat.getTagWireType(this.tag)) {
                case 0:
                    break;
                case 1:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 2:
                    int fieldEndPos2 = this.pos + readVarint32();
                    while (this.pos < fieldEndPos2) {
                        target.add(Long.valueOf(CodedInputStream.decodeZigZag64(readVarint64())));
                    }
                    return;
            }
            do {
                target.add(Long.valueOf(readSInt64()));
                if (!isAtEnd()) {
                    prevPos = this.pos;
                } else {
                    return;
                }
            } while (readVarint32() == this.tag);
            this.pos = prevPos;
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x003f A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x0017 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public <K, V> void readMap(java.util.Map<K, V> r11, com.google.protobuf.MapEntryLite.Metadata<K, V> r12, com.google.protobuf.ExtensionRegistryLite r13) throws java.io.IOException {
            /*
                r10 = this;
                r7 = 2
                r10.requireWireType(r7)
                int r5 = r10.readVarint32()
                r10.requireBytes(r5)
                int r4 = r10.limit
                int r7 = r10.pos
                int r2 = r7 + r5
                r10.limit = r2
                K r1 = r12.defaultKey     // Catch:{ all -> 0x0048 }
                V r6 = r12.defaultValue     // Catch:{ all -> 0x0048 }
            L_0x0017:
                int r3 = r10.getFieldNumber()     // Catch:{ all -> 0x0048 }
                r7 = 2147483647(0x7fffffff, float:NaN)
                if (r3 != r7) goto L_0x0026
                r11.put(r1, r6)     // Catch:{ all -> 0x0048 }
                r10.limit = r4
                return
            L_0x0026:
                switch(r3) {
                    case 1: goto L_0x004c;
                    case 2: goto L_0x0055;
                    default: goto L_0x0029;
                }
            L_0x0029:
                boolean r7 = r10.skipField()     // Catch:{ InvalidWireTypeException -> 0x0038 }
                if (r7 != 0) goto L_0x0017
                com.google.protobuf.InvalidProtocolBufferException r7 = new com.google.protobuf.InvalidProtocolBufferException     // Catch:{ InvalidWireTypeException -> 0x0038 }
                java.lang.String r8 = "Unable to parse map entry."
                r7.<init>(r8)     // Catch:{ InvalidWireTypeException -> 0x0038 }
                throw r7     // Catch:{ InvalidWireTypeException -> 0x0038 }
            L_0x0038:
                r0 = move-exception
                boolean r7 = r10.skipField()     // Catch:{ all -> 0x0048 }
                if (r7 != 0) goto L_0x0017
                com.google.protobuf.InvalidProtocolBufferException r7 = new com.google.protobuf.InvalidProtocolBufferException     // Catch:{ all -> 0x0048 }
                java.lang.String r8 = "Unable to parse map entry."
                r7.<init>(r8)     // Catch:{ all -> 0x0048 }
                throw r7     // Catch:{ all -> 0x0048 }
            L_0x0048:
                r7 = move-exception
                r10.limit = r4
                throw r7
            L_0x004c:
                com.google.protobuf.WireFormat$FieldType r7 = r12.keyType     // Catch:{ InvalidWireTypeException -> 0x0038 }
                r8 = 0
                r9 = 0
                java.lang.Object r1 = r10.readField(r7, r8, r9)     // Catch:{ InvalidWireTypeException -> 0x0038 }
                goto L_0x0017
            L_0x0055:
                com.google.protobuf.WireFormat$FieldType r7 = r12.valueType     // Catch:{ InvalidWireTypeException -> 0x0038 }
                V r8 = r12.defaultValue     // Catch:{ InvalidWireTypeException -> 0x0038 }
                java.lang.Class r8 = r8.getClass()     // Catch:{ InvalidWireTypeException -> 0x0038 }
                java.lang.Object r6 = r10.readField(r7, r8, r13)     // Catch:{ InvalidWireTypeException -> 0x0038 }
                goto L_0x0017
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.BinaryReader.SafeHeapReader.readMap(java.util.Map, com.google.protobuf.MapEntryLite$Metadata, com.google.protobuf.ExtensionRegistryLite):void");
        }

        private Object readField(WireFormat.FieldType fieldType, Class<?> messageType, ExtensionRegistryLite extensionRegistry) throws IOException {
            switch (fieldType) {
                case BOOL:
                    return Boolean.valueOf(readBool());
                case BYTES:
                    return readBytes();
                case DOUBLE:
                    return Double.valueOf(readDouble());
                case ENUM:
                    return Integer.valueOf(readEnum());
                case FIXED32:
                    return Integer.valueOf(readFixed32());
                case FIXED64:
                    return Long.valueOf(readFixed64());
                case FLOAT:
                    return Float.valueOf(readFloat());
                case INT32:
                    return Integer.valueOf(readInt32());
                case INT64:
                    return Long.valueOf(readInt64());
                case MESSAGE:
                    return readMessage(messageType, extensionRegistry);
                case SFIXED32:
                    return Integer.valueOf(readSFixed32());
                case SFIXED64:
                    return Long.valueOf(readSFixed64());
                case SINT32:
                    return Integer.valueOf(readSInt32());
                case SINT64:
                    return Long.valueOf(readSInt64());
                case STRING:
                    return readStringRequireUtf8();
                case UINT32:
                    return Integer.valueOf(readUInt32());
                case UINT64:
                    return Long.valueOf(readUInt64());
                default:
                    throw new RuntimeException("unsupported field type.");
            }
        }

        private int readVarint32() throws IOException {
            int x;
            int i = this.pos;
            if (this.limit == this.pos) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            int i2 = i + 1;
            byte b = this.buffer[i];
            if (b >= 0) {
                this.pos = i2;
                return b;
            } else if (this.limit - i2 < 9) {
                return (int) readVarint64SlowPath();
            } else {
                int i3 = i2 + 1;
                int x2 = b ^ (this.buffer[i2] << 7);
                if (x2 < 0) {
                    x = x2 ^ -128;
                } else {
                    int i4 = i3 + 1;
                    int x3 = x2 ^ (this.buffer[i3] << 14);
                    if (x3 >= 0) {
                        x = x3 ^ 16256;
                        i3 = i4;
                    } else {
                        i3 = i4 + 1;
                        int x4 = x3 ^ (this.buffer[i4] << 21);
                        if (x4 < 0) {
                            x = x4 ^ -2080896;
                        } else {
                            int i5 = i3 + 1;
                            byte b2 = this.buffer[i3];
                            x = (x4 ^ (b2 << 28)) ^ 266354560;
                            if (b2 < 0) {
                                i3 = i5 + 1;
                                if (this.buffer[i5] < 0) {
                                    i5 = i3 + 1;
                                    if (this.buffer[i3] < 0) {
                                        i3 = i5 + 1;
                                        if (this.buffer[i5] < 0) {
                                            i5 = i3 + 1;
                                            if (this.buffer[i3] < 0) {
                                                i3 = i5 + 1;
                                                if (this.buffer[i5] < 0) {
                                                    throw InvalidProtocolBufferException.malformedVarint();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            i3 = i5;
                        }
                    }
                }
                this.pos = i3;
                return x;
            }
        }

        public long readVarint64() throws IOException {
            long x;
            int i = this.pos;
            if (this.limit == i) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            byte[] buffer2 = this.buffer;
            int i2 = i + 1;
            byte b = buffer2[i];
            if (b >= 0) {
                this.pos = i2;
                return (long) b;
            } else if (this.limit - i2 < 9) {
                return readVarint64SlowPath();
            } else {
                int i3 = i2 + 1;
                int y = b ^ (buffer2[i2] << 7);
                if (y < 0) {
                    x = (long) (y ^ -128);
                } else {
                    int i4 = i3 + 1;
                    int y2 = y ^ (buffer2[i3] << 14);
                    if (y2 >= 0) {
                        x = (long) (y2 ^ 16256);
                        i3 = i4;
                    } else {
                        i3 = i4 + 1;
                        int y3 = y2 ^ (buffer2[i4] << 21);
                        if (y3 < 0) {
                            x = (long) (-2080896 ^ y3);
                        } else {
                            int i5 = i3 + 1;
                            long x2 = ((long) y3) ^ (((long) buffer2[i3]) << 28);
                            if (x2 >= 0) {
                                x = x2 ^ 266354560;
                                i3 = i5;
                            } else {
                                i3 = i5 + 1;
                                long x3 = x2 ^ (((long) buffer2[i5]) << 35);
                                if (x3 < 0) {
                                    x = x3 ^ -34093383808L;
                                } else {
                                    int i6 = i3 + 1;
                                    long x4 = x3 ^ (((long) buffer2[i3]) << 42);
                                    if (x4 >= 0) {
                                        x = x4 ^ 4363953127296L;
                                        i3 = i6;
                                    } else {
                                        i3 = i6 + 1;
                                        long x5 = x4 ^ (((long) buffer2[i6]) << 49);
                                        if (x5 < 0) {
                                            x = x5 ^ -558586000294016L;
                                        } else {
                                            int i7 = i3 + 1;
                                            x = (x5 ^ (((long) buffer2[i3]) << 56)) ^ 71499008037633920L;
                                            if (x < 0) {
                                                i3 = i7 + 1;
                                                if (((long) buffer2[i7]) < 0) {
                                                    throw InvalidProtocolBufferException.malformedVarint();
                                                }
                                            } else {
                                                i3 = i7;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.pos = i3;
                return x;
            }
        }

        private long readVarint64SlowPath() throws IOException {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                byte b = readByte();
                result |= ((long) (b & ByteCompanionObject.MAX_VALUE)) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private byte readByte() throws IOException {
            if (this.pos == this.limit) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            byte[] bArr = this.buffer;
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i];
        }

        private int readLittleEndian32() throws IOException {
            requireBytes(4);
            return readLittleEndian32_NoCheck();
        }

        private long readLittleEndian64() throws IOException {
            requireBytes(8);
            return readLittleEndian64_NoCheck();
        }

        private int readLittleEndian32_NoCheck() {
            int p = this.pos;
            byte[] buffer2 = this.buffer;
            this.pos = p + 4;
            return (buffer2[p] & 255) | ((buffer2[p + 1] & 255) << 8) | ((buffer2[p + 2] & 255) << Tnaf.POW_2_WIDTH) | ((buffer2[p + 3] & 255) << 24);
        }

        private long readLittleEndian64_NoCheck() {
            int p = this.pos;
            byte[] buffer2 = this.buffer;
            this.pos = p + 8;
            return (((long) buffer2[p]) & 255) | ((((long) buffer2[p + 1]) & 255) << 8) | ((((long) buffer2[p + 2]) & 255) << 16) | ((((long) buffer2[p + 3]) & 255) << 24) | ((((long) buffer2[p + 4]) & 255) << 32) | ((((long) buffer2[p + 5]) & 255) << 40) | ((((long) buffer2[p + 6]) & 255) << 48) | ((((long) buffer2[p + 7]) & 255) << 56);
        }

        private void skipVarint() throws IOException {
            if (this.limit - this.pos >= 10) {
                byte[] buffer2 = this.buffer;
                int i = 0;
                int p = this.pos;
                while (i < 10) {
                    int p2 = p + 1;
                    if (buffer2[p] >= 0) {
                        this.pos = p2;
                        return;
                    } else {
                        i++;
                        p = p2;
                    }
                }
            }
            skipVarintSlowPath();
        }

        private void skipVarintSlowPath() throws IOException {
            int i = 0;
            while (i < 10) {
                if (readByte() < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private void skipBytes(int size) throws IOException {
            requireBytes(size);
            this.pos += size;
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0018  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void skipGroup() throws java.io.IOException {
            /*
                r3 = this;
                int r0 = r3.endGroupTag
                int r1 = r3.tag
                int r1 = com.google.protobuf.WireFormat.getTagFieldNumber(r1)
                r2 = 4
                int r1 = com.google.protobuf.WireFormat.makeTag(r1, r2)
                r3.endGroupTag = r1
            L_0x000f:
                int r1 = r3.getFieldNumber()
                r2 = 2147483647(0x7fffffff, float:NaN)
                if (r1 == r2) goto L_0x001e
                boolean r1 = r3.skipField()
                if (r1 != 0) goto L_0x000f
            L_0x001e:
                int r1 = r3.tag
                int r2 = r3.endGroupTag
                if (r1 == r2) goto L_0x0029
                com.google.protobuf.InvalidProtocolBufferException r1 = com.google.protobuf.InvalidProtocolBufferException.parseFailure()
                throw r1
            L_0x0029:
                r3.endGroupTag = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.BinaryReader.SafeHeapReader.skipGroup():void");
        }

        private void requireBytes(int size) throws IOException {
            if (size < 0 || size > this.limit - this.pos) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        private void requireWireType(int requiredWireType) throws IOException {
            if (WireFormat.getTagWireType(this.tag) != requiredWireType) {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        private void verifyPackedFixed64Length(int bytes) throws IOException {
            requireBytes(bytes);
            if ((bytes & 7) != 0) {
                throw InvalidProtocolBufferException.parseFailure();
            }
        }

        private void verifyPackedFixed32Length(int bytes) throws IOException {
            requireBytes(bytes);
            if ((bytes & 3) != 0) {
                throw InvalidProtocolBufferException.parseFailure();
            }
        }

        private void requirePosition(int expectedPosition) throws IOException {
            if (this.pos != expectedPosition) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }
    }
}
