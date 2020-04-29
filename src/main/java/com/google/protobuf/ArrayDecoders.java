package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.asn1.eac.CertificateBody;

final class ArrayDecoders {
    ArrayDecoders() {
    }

    static final class Registers {
        public final ExtensionRegistryLite extensionRegistry;
        public int int1;
        public long long1;
        public Object object1;

        Registers() {
            this.extensionRegistry = ExtensionRegistryLite.getEmptyRegistry();
        }

        Registers(ExtensionRegistryLite extensionRegistry2) {
            if (extensionRegistry2 == null) {
                throw new NullPointerException();
            }
            this.extensionRegistry = extensionRegistry2;
        }
    }

    static int decodeVarint32(byte[] data, int position, Registers registers) {
        int position2 = position + 1;
        byte b = data[position];
        if (b < 0) {
            return decodeVarint32(b, data, position2, registers);
        }
        registers.int1 = b;
        return position2;
    }

    static int decodeVarint32(int firstByte, byte[] data, int position, Registers registers) {
        int value = firstByte & CertificateBody.profileType;
        int position2 = position + 1;
        byte b2 = data[position];
        if (b2 >= 0) {
            registers.int1 = (b2 << 7) | value;
            return position2;
        }
        int value2 = value | ((b2 & ByteCompanionObject.MAX_VALUE) << 7);
        int position3 = position2 + 1;
        byte b3 = data[position2];
        if (b3 >= 0) {
            registers.int1 = (b3 << 14) | value2;
            return position3;
        }
        int value3 = value2 | ((b3 & ByteCompanionObject.MAX_VALUE) << 14);
        int position4 = position3 + 1;
        byte b4 = data[position3];
        if (b4 >= 0) {
            registers.int1 = (b4 << 21) | value3;
            return position4;
        }
        int value4 = value3 | ((b4 & ByteCompanionObject.MAX_VALUE) << 21);
        int position5 = position4 + 1;
        byte b5 = data[position4];
        if (b5 >= 0) {
            registers.int1 = (b5 << 28) | value4;
            return position5;
        }
        int value5 = value4 | ((b5 & ByteCompanionObject.MAX_VALUE) << 28);
        while (true) {
            int position6 = position5 + 1;
            if (data[position5] < 0) {
                position5 = position6;
            } else {
                registers.int1 = value5;
                return position6;
            }
        }
    }

    static int decodeVarint64(byte[] data, int position, Registers registers) {
        int position2 = position + 1;
        long value = (long) data[position];
        if (value < 0) {
            return decodeVarint64(value, data, position2, registers);
        }
        registers.long1 = value;
        return position2;
    }

    static int decodeVarint64(long firstByte, byte[] data, int position, Registers registers) {
        int position2 = position + 1;
        byte next = data[position];
        int shift = 7;
        long value = (firstByte & 127) | (((long) (next & ByteCompanionObject.MAX_VALUE)) << 7);
        while (next < 0) {
            next = data[position2];
            shift += 7;
            value |= ((long) (next & ByteCompanionObject.MAX_VALUE)) << shift;
            position2++;
        }
        registers.long1 = value;
        return position2;
    }

    static int decodeFixed32(byte[] data, int position) {
        return (data[position] & 255) | ((data[position + 1] & 255) << 8) | ((data[position + 2] & 255) << Tnaf.POW_2_WIDTH) | ((data[position + 3] & 255) << 24);
    }

    static long decodeFixed64(byte[] data, int position) {
        return (((long) data[position]) & 255) | ((((long) data[position + 1]) & 255) << 8) | ((((long) data[position + 2]) & 255) << 16) | ((((long) data[position + 3]) & 255) << 24) | ((((long) data[position + 4]) & 255) << 32) | ((((long) data[position + 5]) & 255) << 40) | ((((long) data[position + 6]) & 255) << 48) | ((((long) data[position + 7]) & 255) << 56);
    }

    static double decodeDouble(byte[] data, int position) {
        return Double.longBitsToDouble(decodeFixed64(data, position));
    }

    static float decodeFloat(byte[] data, int position) {
        return Float.intBitsToFloat(decodeFixed32(data, position));
    }

    static int decodeString(byte[] data, int position, Registers registers) throws InvalidProtocolBufferException {
        int position2 = decodeVarint32(data, position, registers);
        int length = registers.int1;
        if (length < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        } else if (length == 0) {
            registers.object1 = "";
            return position2;
        } else {
            registers.object1 = new String(data, position2, length, Internal.UTF_8);
            return position2 + length;
        }
    }

    static int decodeStringRequireUtf8(byte[] data, int position, Registers registers) throws InvalidProtocolBufferException {
        int position2 = decodeVarint32(data, position, registers);
        int length = registers.int1;
        if (length < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        } else if (length == 0) {
            registers.object1 = "";
            return position2;
        } else {
            registers.object1 = Utf8.decodeUtf8(data, position2, length);
            return position2 + length;
        }
    }

    static int decodeBytes(byte[] data, int position, Registers registers) throws InvalidProtocolBufferException {
        int position2 = decodeVarint32(data, position, registers);
        int length = registers.int1;
        if (length < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        } else if (length > data.length - position2) {
            throw InvalidProtocolBufferException.truncatedMessage();
        } else if (length == 0) {
            registers.object1 = ByteString.EMPTY;
            return position2;
        } else {
            registers.object1 = ByteString.copyFrom(data, position2, length);
            return position2 + length;
        }
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int decodeMessageField(com.google.protobuf.Schema r8, byte[] r9, int r10, int r11, com.google.protobuf.ArrayDecoders.Registers r12) throws java.io.IOException {
        /*
            int r7 = r10 + 1
            byte r6 = r9[r10]
            if (r6 >= 0) goto L_0x002c
            int r10 = decodeVarint32(r6, r9, r7, r12)
            int r6 = r12.int1
        L_0x000c:
            if (r6 < 0) goto L_0x0012
            int r0 = r11 - r10
            if (r6 <= r0) goto L_0x0017
        L_0x0012:
            com.google.protobuf.InvalidProtocolBufferException r0 = com.google.protobuf.InvalidProtocolBufferException.truncatedMessage()
            throw r0
        L_0x0017:
            java.lang.Object r1 = r8.newInstance()
            int r4 = r10 + r6
            r0 = r8
            r2 = r9
            r3 = r10
            r5 = r12
            r0.mergeFrom(r1, r2, r3, r4, r5)
            r8.makeImmutable(r1)
            r12.object1 = r1
            int r0 = r10 + r6
            return r0
        L_0x002c:
            r10 = r7
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.ArrayDecoders.decodeMessageField(com.google.protobuf.Schema, byte[], int, int, com.google.protobuf.ArrayDecoders$Registers):int");
    }

    static int decodeGroupField(Schema schema, byte[] data, int position, int limit, int endGroup, Registers registers) throws IOException {
        MessageSchema messageSchema = (MessageSchema) schema;
        Object result = messageSchema.newInstance();
        int endPosition = messageSchema.parseProto2Message(result, data, position, limit, endGroup, registers);
        messageSchema.makeImmutable(result);
        registers.object1 = result;
        return endPosition;
    }

    static int decodeVarint32List(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        IntArrayList output = (IntArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        output.addInt(registers.int1);
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeVarint32(data, nextPosition, registers);
            output.addInt(registers.int1);
        }
        return position2;
    }

    static int decodeVarint64List(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        LongArrayList output = (LongArrayList) list;
        int position2 = decodeVarint64(data, position, registers);
        output.addLong(registers.long1);
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeVarint64(data, nextPosition, registers);
            output.addLong(registers.long1);
        }
        return position2;
    }

    static int decodeFixed32List(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        IntArrayList output = (IntArrayList) list;
        output.addInt(decodeFixed32(data, position));
        int position2 = position + 4;
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            output.addInt(decodeFixed32(data, nextPosition));
            position2 = nextPosition + 4;
        }
        return position2;
    }

    static int decodeFixed64List(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        LongArrayList output = (LongArrayList) list;
        output.addLong(decodeFixed64(data, position));
        int position2 = position + 8;
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            output.addLong(decodeFixed64(data, nextPosition));
            position2 = nextPosition + 8;
        }
        return position2;
    }

    static int decodeFloatList(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        FloatArrayList output = (FloatArrayList) list;
        output.addFloat(decodeFloat(data, position));
        int position2 = position + 4;
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            output.addFloat(decodeFloat(data, nextPosition));
            position2 = nextPosition + 4;
        }
        return position2;
    }

    static int decodeDoubleList(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        DoubleArrayList output = (DoubleArrayList) list;
        output.addDouble(decodeDouble(data, position));
        int position2 = position + 8;
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            output.addDouble(decodeDouble(data, nextPosition));
            position2 = nextPosition + 8;
        }
        return position2;
    }

    static int decodeBoolList(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        BooleanArrayList output = (BooleanArrayList) list;
        int position2 = decodeVarint64(data, position, registers);
        output.addBoolean(registers.long1 != 0);
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeVarint64(data, nextPosition, registers);
            output.addBoolean(registers.long1 != 0);
        }
        return position2;
    }

    static int decodeSInt32List(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        IntArrayList output = (IntArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        output.addInt(CodedInputStream.decodeZigZag32(registers.int1));
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeVarint32(data, nextPosition, registers);
            output.addInt(CodedInputStream.decodeZigZag32(registers.int1));
        }
        return position2;
    }

    static int decodeSInt64List(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) {
        LongArrayList output = (LongArrayList) list;
        int position2 = decodeVarint64(data, position, registers);
        output.addLong(CodedInputStream.decodeZigZag64(registers.long1));
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeVarint64(data, nextPosition, registers);
            output.addLong(CodedInputStream.decodeZigZag64(registers.long1));
        }
        return position2;
    }

    static int decodePackedVarint32List(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        IntArrayList output = (IntArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            position2 = decodeVarint32(data, position2, registers);
            output.addInt(registers.int1);
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedVarint64List(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        LongArrayList output = (LongArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            position2 = decodeVarint64(data, position2, registers);
            output.addLong(registers.long1);
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedFixed32List(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        IntArrayList output = (IntArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            output.addInt(decodeFixed32(data, position2));
            position2 += 4;
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedFixed64List(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        LongArrayList output = (LongArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            output.addLong(decodeFixed64(data, position2));
            position2 += 8;
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedFloatList(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        FloatArrayList output = (FloatArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            output.addFloat(decodeFloat(data, position2));
            position2 += 4;
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedDoubleList(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        DoubleArrayList output = (DoubleArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            output.addDouble(decodeDouble(data, position2));
            position2 += 8;
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedBoolList(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        BooleanArrayList output = (BooleanArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            position2 = decodeVarint64(data, position2, registers);
            output.addBoolean(registers.long1 != 0);
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedSInt32List(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        IntArrayList output = (IntArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            position2 = decodeVarint32(data, position2, registers);
            output.addInt(CodedInputStream.decodeZigZag32(registers.int1));
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodePackedSInt64List(byte[] data, int position, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        LongArrayList output = (LongArrayList) list;
        int position2 = decodeVarint32(data, position, registers);
        int fieldLimit = position2 + registers.int1;
        while (position2 < fieldLimit) {
            position2 = decodeVarint64(data, position2, registers);
            output.addLong(CodedInputStream.decodeZigZag64(registers.long1));
        }
        if (position2 == fieldLimit) {
            return position2;
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    static int decodeStringList(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) throws InvalidProtocolBufferException {
        int position2;
        Internal.ProtobufList<?> protobufList = list;
        int position3 = decodeVarint32(data, position, registers);
        int length = registers.int1;
        if (length < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        }
        if (length == 0) {
            protobufList.add("");
        } else {
            protobufList.add(new String(data, position3, length, Internal.UTF_8));
            position3 += length;
        }
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeVarint32(data, nextPosition, registers);
            int nextLength = registers.int1;
            if (nextLength < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            } else if (nextLength == 0) {
                protobufList.add("");
            } else {
                protobufList.add(new String(data, position2, nextLength, Internal.UTF_8));
                position2 += nextLength;
            }
        }
        return position2;
    }

    static int decodeStringListRequireUtf8(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) throws InvalidProtocolBufferException {
        int position2;
        Internal.ProtobufList<?> protobufList = list;
        int position3 = decodeVarint32(data, position, registers);
        int length = registers.int1;
        if (length < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        }
        if (length == 0) {
            protobufList.add("");
        } else if (!Utf8.isValidUtf8(data, position3, position3 + length)) {
            throw InvalidProtocolBufferException.invalidUtf8();
        } else {
            protobufList.add(new String(data, position3, length, Internal.UTF_8));
            position3 += length;
        }
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeVarint32(data, nextPosition, registers);
            int nextLength = registers.int1;
            if (nextLength < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            } else if (nextLength == 0) {
                protobufList.add("");
            } else if (!Utf8.isValidUtf8(data, position2, position2 + nextLength)) {
                throw InvalidProtocolBufferException.invalidUtf8();
            } else {
                protobufList.add(new String(data, position2, nextLength, Internal.UTF_8));
                position2 += nextLength;
            }
        }
        return position2;
    }

    static int decodeBytesList(int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) throws InvalidProtocolBufferException {
        int position2;
        Internal.ProtobufList<?> protobufList = list;
        int position3 = decodeVarint32(data, position, registers);
        int length = registers.int1;
        if (length < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        } else if (length > data.length - position3) {
            throw InvalidProtocolBufferException.truncatedMessage();
        } else {
            if (length == 0) {
                protobufList.add(ByteString.EMPTY);
            } else {
                protobufList.add(ByteString.copyFrom(data, position3, length));
                position3 += length;
            }
            while (position2 < limit) {
                int nextPosition = decodeVarint32(data, position2, registers);
                if (tag != registers.int1) {
                    break;
                }
                position2 = decodeVarint32(data, nextPosition, registers);
                int nextLength = registers.int1;
                if (nextLength < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                } else if (nextLength > data.length - position2) {
                    throw InvalidProtocolBufferException.truncatedMessage();
                } else if (nextLength == 0) {
                    protobufList.add(ByteString.EMPTY);
                } else {
                    protobufList.add(ByteString.copyFrom(data, position2, nextLength));
                    position2 += nextLength;
                }
            }
            return position2;
        }
    }

    static int decodeMessageList(Schema<?> schema, int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        Internal.ProtobufList<Object> output = list;
        int position2 = decodeMessageField(schema, data, position, limit, registers);
        output.add(registers.object1);
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeMessageField(schema, data, nextPosition, limit, registers);
            output.add(registers.object1);
        }
        return position2;
    }

    static int decodeGroupList(Schema schema, int tag, byte[] data, int position, int limit, Internal.ProtobufList<?> list, Registers registers) throws IOException {
        Internal.ProtobufList<Object> output = list;
        int endgroup = (tag & -8) | 4;
        int position2 = decodeGroupField(schema, data, position, limit, endgroup, registers);
        output.add(registers.object1);
        while (position2 < limit) {
            int nextPosition = decodeVarint32(data, position2, registers);
            if (tag != registers.int1) {
                break;
            }
            position2 = decodeGroupField(schema, data, nextPosition, limit, endgroup, registers);
            output.add(registers.object1);
        }
        return position2;
    }

    static int decodeExtensionOrUnknownField(int tag, byte[] data, int position, int limit, Object message, MessageLite defaultInstance, UnknownFieldSchema<UnknownFieldSetLite, UnknownFieldSetLite> unknownFieldSchema, Registers registers) throws IOException {
        GeneratedMessageLite.GeneratedExtension extension = registers.extensionRegistry.findLiteExtensionByNumber(defaultInstance, tag >>> 3);
        if (extension == null) {
            return decodeUnknownField(tag, data, position, limit, MessageSchema.getMutableUnknownFields(message), registers);
        }
        ((GeneratedMessageLite.ExtendableMessage) message).ensureExtensionsAreMutable();
        return decodeExtension(tag, data, position, limit, (GeneratedMessageLite.ExtendableMessage) message, extension, unknownFieldSchema, registers);
    }

    static int decodeExtension(int tag, byte[] data, int position, int limit, GeneratedMessageLite.ExtendableMessage<?, ?> message, GeneratedMessageLite.GeneratedExtension<?, ?> extension, UnknownFieldSchema<UnknownFieldSetLite, UnknownFieldSetLite> unknownFieldSchema, Registers registers) throws IOException {
        FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions = message.extensions;
        int fieldNumber = tag >>> 3;
        if (!extension.descriptor.isRepeated() || !extension.descriptor.isPacked()) {
            Object value = null;
            if (extension.getLiteType() != WireFormat.FieldType.ENUM) {
                switch (extension.getLiteType()) {
                    case DOUBLE:
                        value = Double.valueOf(decodeDouble(data, position));
                        position += 8;
                        break;
                    case FLOAT:
                        value = Float.valueOf(decodeFloat(data, position));
                        position += 4;
                        break;
                    case INT64:
                    case UINT64:
                        position = decodeVarint64(data, position, registers);
                        value = Long.valueOf(registers.long1);
                        break;
                    case INT32:
                    case UINT32:
                        position = decodeVarint32(data, position, registers);
                        value = Integer.valueOf(registers.int1);
                        break;
                    case FIXED64:
                    case SFIXED64:
                        value = Long.valueOf(decodeFixed64(data, position));
                        position += 8;
                        break;
                    case FIXED32:
                    case SFIXED32:
                        value = Integer.valueOf(decodeFixed32(data, position));
                        position += 4;
                        break;
                    case BOOL:
                        position = decodeVarint64(data, position, registers);
                        value = Boolean.valueOf(registers.long1 != 0);
                        break;
                    case SINT32:
                        position = decodeVarint32(data, position, registers);
                        value = Integer.valueOf(CodedInputStream.decodeZigZag32(registers.int1));
                        break;
                    case SINT64:
                        position = decodeVarint64(data, position, registers);
                        value = Long.valueOf(CodedInputStream.decodeZigZag64(registers.long1));
                        break;
                    case ENUM:
                        throw new IllegalStateException("Shouldn't reach here.");
                    case BYTES:
                        position = decodeBytes(data, position, registers);
                        value = registers.object1;
                        break;
                    case STRING:
                        position = decodeString(data, position, registers);
                        value = registers.object1;
                        break;
                    case GROUP:
                        position = decodeGroupField(Protobuf.getInstance().schemaFor((Class) extension.getMessageDefaultInstance().getClass()), data, position, limit, (fieldNumber << 3) | 4, registers);
                        value = registers.object1;
                        break;
                    case MESSAGE:
                        position = decodeMessageField(Protobuf.getInstance().schemaFor((Class) extension.getMessageDefaultInstance().getClass()), data, position, limit, registers);
                        value = registers.object1;
                        break;
                }
            } else {
                position = decodeVarint32(data, position, registers);
                if (extension.descriptor.getEnumType().findValueByNumber(registers.int1) == null) {
                    UnknownFieldSetLite unknownFields = message.unknownFields;
                    if (unknownFields == UnknownFieldSetLite.getDefaultInstance()) {
                        unknownFields = UnknownFieldSetLite.newInstance();
                        message.unknownFields = unknownFields;
                    }
                    SchemaUtil.storeUnknownEnum(fieldNumber, registers.int1, unknownFields, unknownFieldSchema);
                    return position;
                }
                value = Integer.valueOf(registers.int1);
            }
            if (extension.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, value);
            } else {
                switch (extension.getLiteType()) {
                    case GROUP:
                    case MESSAGE:
                        Object oldValue = extensions.getField(extension.descriptor);
                        if (oldValue != null) {
                            value = Internal.mergeMessage(oldValue, value);
                            break;
                        }
                        break;
                }
                extensions.setField(extension.descriptor, value);
            }
        } else {
            switch (extension.getLiteType()) {
                case DOUBLE:
                    DoubleArrayList list = new DoubleArrayList();
                    position = decodePackedDoubleList(data, position, list, registers);
                    extensions.setField(extension.descriptor, list);
                    break;
                case FLOAT:
                    FloatArrayList list2 = new FloatArrayList();
                    position = decodePackedFloatList(data, position, list2, registers);
                    extensions.setField(extension.descriptor, list2);
                    break;
                case INT64:
                case UINT64:
                    LongArrayList list3 = new LongArrayList();
                    position = decodePackedVarint64List(data, position, list3, registers);
                    extensions.setField(extension.descriptor, list3);
                    break;
                case INT32:
                case UINT32:
                    IntArrayList list4 = new IntArrayList();
                    position = decodePackedVarint32List(data, position, list4, registers);
                    extensions.setField(extension.descriptor, list4);
                    break;
                case FIXED64:
                case SFIXED64:
                    LongArrayList list5 = new LongArrayList();
                    position = decodePackedFixed64List(data, position, list5, registers);
                    extensions.setField(extension.descriptor, list5);
                    break;
                case FIXED32:
                case SFIXED32:
                    IntArrayList list6 = new IntArrayList();
                    position = decodePackedFixed32List(data, position, list6, registers);
                    extensions.setField(extension.descriptor, list6);
                    break;
                case BOOL:
                    BooleanArrayList list7 = new BooleanArrayList();
                    position = decodePackedBoolList(data, position, list7, registers);
                    extensions.setField(extension.descriptor, list7);
                    break;
                case SINT32:
                    IntArrayList list8 = new IntArrayList();
                    position = decodePackedSInt32List(data, position, list8, registers);
                    extensions.setField(extension.descriptor, list8);
                    break;
                case SINT64:
                    LongArrayList list9 = new LongArrayList();
                    position = decodePackedSInt64List(data, position, list9, registers);
                    extensions.setField(extension.descriptor, list9);
                    break;
                case ENUM:
                    IntArrayList list10 = new IntArrayList();
                    position = decodePackedVarint32List(data, position, list10, registers);
                    UnknownFieldSetLite unknownFields2 = message.unknownFields;
                    if (unknownFields2 == UnknownFieldSetLite.getDefaultInstance()) {
                        unknownFields2 = null;
                    }
                    UnknownFieldSetLite unknownFields3 = (UnknownFieldSetLite) SchemaUtil.filterUnknownEnumList(fieldNumber, list10, extension.descriptor.getEnumType(), unknownFields2, unknownFieldSchema);
                    if (unknownFields3 != null) {
                        message.unknownFields = unknownFields3;
                    }
                    extensions.setField(extension.descriptor, list10);
                    break;
                default:
                    throw new IllegalStateException("Type cannot be packed: " + extension.descriptor.getLiteType());
            }
        }
        return position;
    }

    static int decodeUnknownField(int tag, byte[] data, int position, int limit, UnknownFieldSetLite unknownFields, Registers registers) throws InvalidProtocolBufferException {
        if (WireFormat.getTagFieldNumber(tag) == 0) {
            throw InvalidProtocolBufferException.invalidTag();
        }
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                int position2 = decodeVarint64(data, position, registers);
                unknownFields.storeField(tag, Long.valueOf(registers.long1));
                return position2;
            case 1:
                unknownFields.storeField(tag, Long.valueOf(decodeFixed64(data, position)));
                return position + 8;
            case 2:
                int position3 = decodeVarint32(data, position, registers);
                int length = registers.int1;
                if (length < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                } else if (length > data.length - position3) {
                    throw InvalidProtocolBufferException.truncatedMessage();
                } else {
                    if (length == 0) {
                        unknownFields.storeField(tag, ByteString.EMPTY);
                    } else {
                        unknownFields.storeField(tag, ByteString.copyFrom(data, position3, length));
                    }
                    return position3 + length;
                }
            case 3:
                UnknownFieldSetLite child = UnknownFieldSetLite.newInstance();
                int endGroup = (tag & -8) | 4;
                int lastTag = 0;
                while (position < limit) {
                    position = decodeVarint32(data, position, registers);
                    lastTag = registers.int1;
                    if (lastTag != endGroup) {
                        position = decodeUnknownField(lastTag, data, position, limit, child, registers);
                    } else if (position <= limit || lastTag != endGroup) {
                        throw InvalidProtocolBufferException.parseFailure();
                    } else {
                        unknownFields.storeField(tag, child);
                        return position;
                    }
                }
                if (position <= limit) {
                }
                throw InvalidProtocolBufferException.parseFailure();
            case 4:
            default:
                throw InvalidProtocolBufferException.invalidTag();
            case 5:
                unknownFields.storeField(tag, Integer.valueOf(decodeFixed32(data, position)));
                return position + 4;
        }
    }

    static int skipField(int tag, byte[] data, int position, int limit, Registers registers) throws InvalidProtocolBufferException {
        if (WireFormat.getTagFieldNumber(tag) == 0) {
            throw InvalidProtocolBufferException.invalidTag();
        }
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                return decodeVarint64(data, position, registers);
            case 1:
                return position + 8;
            case 2:
                return registers.int1 + decodeVarint32(data, position, registers);
            case 3:
                int endGroup = (tag & -8) | 4;
                int lastTag = 0;
                while (position < limit) {
                    position = decodeVarint32(data, position, registers);
                    lastTag = registers.int1;
                    if (lastTag != endGroup) {
                        position = skipField(lastTag, data, position, limit, registers);
                    } else if (position > limit && lastTag == endGroup) {
                        return position;
                    } else {
                        throw InvalidProtocolBufferException.parseFailure();
                    }
                }
                if (position > limit) {
                }
                throw InvalidProtocolBufferException.parseFailure();
            case 4:
            default:
                throw InvalidProtocolBufferException.invalidTag();
            case 5:
                return position + 4;
        }
    }
}
