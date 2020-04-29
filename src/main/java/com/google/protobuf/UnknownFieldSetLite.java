package com.google.protobuf;

import com.google.protobuf.Writer;
import java.io.IOException;
import java.util.Arrays;

public final class UnknownFieldSetLite {
    private static final UnknownFieldSetLite DEFAULT_INSTANCE = new UnknownFieldSetLite(0, new int[0], new Object[0], false);
    private static final int MIN_CAPACITY = 8;
    private int count;
    private boolean isMutable;
    private int memoizedSerializedSize;
    private Object[] objects;
    private int[] tags;

    public static UnknownFieldSetLite getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    static UnknownFieldSetLite newInstance() {
        return new UnknownFieldSetLite();
    }

    static UnknownFieldSetLite mutableCopyOf(UnknownFieldSetLite first, UnknownFieldSetLite second) {
        int count2 = first.count + second.count;
        int[] tags2 = Arrays.copyOf(first.tags, count2);
        System.arraycopy(second.tags, 0, tags2, first.count, second.count);
        Object[] objects2 = Arrays.copyOf(first.objects, count2);
        System.arraycopy(second.objects, 0, objects2, first.count, second.count);
        return new UnknownFieldSetLite(count2, tags2, objects2, true);
    }

    private UnknownFieldSetLite() {
        this(0, new int[8], new Object[8], true);
    }

    private UnknownFieldSetLite(int count2, int[] tags2, Object[] objects2, boolean isMutable2) {
        this.memoizedSerializedSize = -1;
        this.count = count2;
        this.tags = tags2;
        this.objects = objects2;
        this.isMutable = isMutable2;
    }

    public void makeImmutable() {
        this.isMutable = false;
    }

    /* access modifiers changed from: package-private */
    public void checkMutable() {
        if (!this.isMutable) {
            throw new UnsupportedOperationException();
        }
    }

    public void writeTo(CodedOutputStream output) throws IOException {
        for (int i = 0; i < this.count; i++) {
            int tag = this.tags[i];
            int fieldNumber = WireFormat.getTagFieldNumber(tag);
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    output.writeUInt64(fieldNumber, ((Long) this.objects[i]).longValue());
                    break;
                case 1:
                    output.writeFixed64(fieldNumber, ((Long) this.objects[i]).longValue());
                    break;
                case 2:
                    output.writeBytes(fieldNumber, (ByteString) this.objects[i]);
                    break;
                case 3:
                    output.writeTag(fieldNumber, 3);
                    ((UnknownFieldSetLite) this.objects[i]).writeTo(output);
                    output.writeTag(fieldNumber, 4);
                    break;
                case 4:
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
                case 5:
                    output.writeFixed32(fieldNumber, ((Integer) this.objects[i]).intValue());
                    break;
            }
        }
    }

    public void writeAsMessageSetTo(CodedOutputStream output) throws IOException {
        for (int i = 0; i < this.count; i++) {
            output.writeRawMessageSetExtension(WireFormat.getTagFieldNumber(this.tags[i]), (ByteString) this.objects[i]);
        }
    }

    /* access modifiers changed from: package-private */
    public void writeAsMessageSetTo(Writer writer) throws IOException {
        if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
            for (int i = this.count - 1; i >= 0; i--) {
                writer.writeMessageSetItem(WireFormat.getTagFieldNumber(this.tags[i]), this.objects[i]);
            }
            return;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            writer.writeMessageSetItem(WireFormat.getTagFieldNumber(this.tags[i2]), this.objects[i2]);
        }
    }

    public void writeTo(Writer writer) throws IOException {
        if (this.count != 0) {
            if (writer.fieldOrder() == Writer.FieldOrder.ASCENDING) {
                for (int i = 0; i < this.count; i++) {
                    writeField(this.tags[i], this.objects[i], writer);
                }
                return;
            }
            for (int i2 = this.count - 1; i2 >= 0; i2--) {
                writeField(this.tags[i2], this.objects[i2], writer);
            }
        }
    }

    private static void writeField(int tag, Object object, Writer writer) throws IOException {
        int fieldNumber = WireFormat.getTagFieldNumber(tag);
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                writer.writeInt64(fieldNumber, ((Long) object).longValue());
                return;
            case 1:
                writer.writeFixed64(fieldNumber, ((Long) object).longValue());
                return;
            case 2:
                writer.writeBytes(fieldNumber, (ByteString) object);
                return;
            case 3:
                if (writer.fieldOrder() == Writer.FieldOrder.ASCENDING) {
                    writer.writeStartGroup(fieldNumber);
                    ((UnknownFieldSetLite) object).writeTo(writer);
                    writer.writeEndGroup(fieldNumber);
                    return;
                }
                writer.writeEndGroup(fieldNumber);
                ((UnknownFieldSetLite) object).writeTo(writer);
                writer.writeStartGroup(fieldNumber);
                return;
            case 4:
            default:
                throw new RuntimeException(InvalidProtocolBufferException.invalidWireType());
            case 5:
                writer.writeFixed32(fieldNumber, ((Integer) object).intValue());
                return;
        }
    }

    public int getSerializedSizeAsMessageSet() {
        int size = this.memoizedSerializedSize;
        if (size != -1) {
            return size;
        }
        int size2 = 0;
        for (int i = 0; i < this.count; i++) {
            size2 += CodedOutputStream.computeRawMessageSetExtensionSize(WireFormat.getTagFieldNumber(this.tags[i]), (ByteString) this.objects[i]);
        }
        this.memoizedSerializedSize = size2;
        return size2;
    }

    public int getSerializedSize() {
        int serializedSize;
        int size = this.memoizedSerializedSize;
        if (size != -1) {
            return size;
        }
        int size2 = 0;
        for (int i = 0; i < this.count; i++) {
            int tag = this.tags[i];
            int fieldNumber = WireFormat.getTagFieldNumber(tag);
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    serializedSize = CodedOutputStream.computeUInt64Size(fieldNumber, ((Long) this.objects[i]).longValue());
                    break;
                case 1:
                    serializedSize = CodedOutputStream.computeFixed64Size(fieldNumber, ((Long) this.objects[i]).longValue());
                    break;
                case 2:
                    serializedSize = CodedOutputStream.computeBytesSize(fieldNumber, (ByteString) this.objects[i]);
                    break;
                case 3:
                    serializedSize = ((UnknownFieldSetLite) this.objects[i]).getSerializedSize() + (CodedOutputStream.computeTagSize(fieldNumber) * 2);
                    break;
                case 4:
                default:
                    throw new IllegalStateException(InvalidProtocolBufferException.invalidWireType());
                case 5:
                    serializedSize = CodedOutputStream.computeFixed32Size(fieldNumber, ((Integer) this.objects[i]).intValue());
                    break;
            }
            size2 += serializedSize;
        }
        this.memoizedSerializedSize = size2;
        return size2;
    }

    private static boolean equals(int[] tags1, int[] tags2, int count2) {
        for (int i = 0; i < count2; i++) {
            if (tags1[i] != tags2[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(Object[] objects1, Object[] objects2, int count2) {
        for (int i = 0; i < count2; i++) {
            if (!objects1[i].equals(objects2[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UnknownFieldSetLite)) {
            return false;
        }
        UnknownFieldSetLite other = (UnknownFieldSetLite) obj;
        if (this.count != other.count || !equals(this.tags, other.tags, this.count) || !equals(this.objects, other.objects, this.count)) {
            return false;
        }
        return true;
    }

    private static int hashCode(int[] tags2, int count2) {
        int hashCode = 17;
        for (int i = 0; i < count2; i++) {
            hashCode = (hashCode * 31) + tags2[i];
        }
        return hashCode;
    }

    private static int hashCode(Object[] objects2, int count2) {
        int hashCode = 17;
        for (int i = 0; i < count2; i++) {
            hashCode = (hashCode * 31) + objects2[i].hashCode();
        }
        return hashCode;
    }

    public int hashCode() {
        return ((((this.count + 527) * 31) + hashCode(this.tags, this.count)) * 31) + hashCode(this.objects, this.count);
    }

    /* access modifiers changed from: package-private */
    public final void printWithIndent(StringBuilder buffer, int indent) {
        for (int i = 0; i < this.count; i++) {
            MessageLiteToString.printField(buffer, indent, String.valueOf(WireFormat.getTagFieldNumber(this.tags[i])), this.objects[i]);
        }
    }

    /* access modifiers changed from: package-private */
    public void storeField(int tag, Object value) {
        checkMutable();
        ensureCapacity();
        this.tags[this.count] = tag;
        this.objects[this.count] = value;
        this.count++;
    }

    private void ensureCapacity() {
        if (this.count == this.tags.length) {
            int newLength = this.count + (this.count < 4 ? 8 : this.count >> 1);
            this.tags = Arrays.copyOf(this.tags, newLength);
            this.objects = Arrays.copyOf(this.objects, newLength);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean mergeFieldFrom(int tag, CodedInputStream input) throws IOException {
        checkMutable();
        int fieldNumber = WireFormat.getTagFieldNumber(tag);
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                storeField(tag, Long.valueOf(input.readInt64()));
                return true;
            case 1:
                storeField(tag, Long.valueOf(input.readFixed64()));
                return true;
            case 2:
                storeField(tag, input.readBytes());
                return true;
            case 3:
                UnknownFieldSetLite subFieldSet = new UnknownFieldSetLite();
                subFieldSet.mergeFrom(input);
                input.checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
                storeField(tag, subFieldSet);
                return true;
            case 4:
                return false;
            case 5:
                storeField(tag, Integer.valueOf(input.readFixed32()));
                return true;
            default:
                throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSetLite mergeVarintField(int fieldNumber, int value) {
        checkMutable();
        if (fieldNumber == 0) {
            throw new IllegalArgumentException("Zero is not a valid field number.");
        }
        storeField(WireFormat.makeTag(fieldNumber, 0), Long.valueOf((long) value));
        return this;
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSetLite mergeLengthDelimitedField(int fieldNumber, ByteString value) {
        checkMutable();
        if (fieldNumber == 0) {
            throw new IllegalArgumentException("Zero is not a valid field number.");
        }
        storeField(WireFormat.makeTag(fieldNumber, 2), value);
        return this;
    }

    private UnknownFieldSetLite mergeFrom(CodedInputStream input) throws IOException {
        int tag;
        do {
            tag = input.readTag();
            if (tag == 0) {
                break;
            }
        } while (mergeFieldFrom(tag, input));
        return this;
    }
}
