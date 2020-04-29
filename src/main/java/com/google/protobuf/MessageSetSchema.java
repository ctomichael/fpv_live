package com.google.protobuf;

import com.google.protobuf.FieldSet;
import com.google.protobuf.LazyField;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

final class MessageSetSchema<T> implements Schema<T> {
    private final MessageLite defaultInstance;
    private final ExtensionSchema<?> extensionSchema;
    private final boolean hasExtensions;
    private final UnknownFieldSchema<?, ?> unknownFieldSchema;

    private MessageSetSchema(UnknownFieldSchema<?, ?> unknownFieldSchema2, ExtensionSchema<?> extensionSchema2, MessageLite defaultInstance2) {
        this.unknownFieldSchema = unknownFieldSchema2;
        this.hasExtensions = extensionSchema2.hasExtensions(defaultInstance2);
        this.extensionSchema = extensionSchema2;
        this.defaultInstance = defaultInstance2;
    }

    static <T> MessageSetSchema<T> newSchema(UnknownFieldSchema<?, ?> unknownFieldSchema2, ExtensionSchema<?> extensionSchema2, MessageLite defaultInstance2) {
        return new MessageSetSchema<>(unknownFieldSchema2, extensionSchema2, defaultInstance2);
    }

    public T newInstance() {
        return this.defaultInstance.newBuilderForType().buildPartial();
    }

    public boolean equals(T message, T other) {
        if (!this.unknownFieldSchema.getFromMessage(message).equals(this.unknownFieldSchema.getFromMessage(other))) {
            return false;
        }
        if (this.hasExtensions) {
            return this.extensionSchema.getExtensions(message).equals(this.extensionSchema.getExtensions(other));
        }
        return true;
    }

    public int hashCode(T message) {
        int hashCode = this.unknownFieldSchema.getFromMessage(message).hashCode();
        if (!this.hasExtensions) {
            return hashCode;
        }
        return (hashCode * 53) + this.extensionSchema.getExtensions(message).hashCode();
    }

    public void mergeFrom(T message, T other) {
        SchemaUtil.mergeUnknownFields(this.unknownFieldSchema, message, other);
        if (this.hasExtensions) {
            SchemaUtil.mergeExtensions(this.extensionSchema, message, other);
        }
    }

    public void writeTo(T message, Writer writer) throws IOException {
        Iterator<?> iterator = this.extensionSchema.getExtensions(message).iterator();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> extension = iterator.next();
            FieldSet.FieldDescriptorLite<?> fd = (FieldSet.FieldDescriptorLite) extension.getKey();
            if (fd.getLiteJavaType() != WireFormat.JavaType.MESSAGE || fd.isRepeated() || fd.isPacked()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            } else if (extension instanceof LazyField.LazyEntry) {
                writer.writeMessageSetItem(fd.getNumber(), ((LazyField.LazyEntry) extension).getField().toByteString());
            } else {
                writer.writeMessageSetItem(fd.getNumber(), extension.getValue());
            }
        }
        writeUnknownFieldsHelper(this.unknownFieldSchema, message, writer);
    }

    private <UT, UB> void writeUnknownFieldsHelper(UnknownFieldSchema<UT, UB> unknownFieldSchema2, T message, Writer writer) throws IOException {
        unknownFieldSchema2.writeAsMessageSetTo(unknownFieldSchema2.getFromMessage(message), writer);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00bb  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x001d A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00b9 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void mergeFrom(T r18, byte[] r19, int r20, int r21, com.google.protobuf.ArrayDecoders.Registers r22) throws java.io.IOException {
        /*
            r17 = this;
            r5 = r18
            com.google.protobuf.GeneratedMessageLite r5 = (com.google.protobuf.GeneratedMessageLite) r5
            com.google.protobuf.UnknownFieldSetLite r8 = r5.unknownFields
            com.google.protobuf.UnknownFieldSetLite r5 = com.google.protobuf.UnknownFieldSetLite.getDefaultInstance()
            if (r8 != r5) goto L_0x0016
            com.google.protobuf.UnknownFieldSetLite r8 = com.google.protobuf.UnknownFieldSetLite.newInstance()
            r5 = r18
            com.google.protobuf.GeneratedMessageLite r5 = (com.google.protobuf.GeneratedMessageLite) r5
            r5.unknownFields = r8
        L_0x0016:
            com.google.protobuf.GeneratedMessageLite$ExtendableMessage r18 = (com.google.protobuf.GeneratedMessageLite.ExtendableMessage) r18
            com.google.protobuf.FieldSet r11 = r18.ensureExtensionsAreMutable()
            r10 = 0
        L_0x001d:
            r0 = r20
            r1 = r21
            if (r0 >= r1) goto L_0x0135
            r0 = r19
            r1 = r20
            r2 = r22
            int r20 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2)
            r0 = r22
            int r4 = r0.int1
            int r5 = com.google.protobuf.WireFormat.MESSAGE_SET_ITEM_TAG
            if (r4 == r5) goto L_0x0094
            int r5 = com.google.protobuf.WireFormat.getTagWireType(r4)
            r6 = 2
            if (r5 != r6) goto L_0x0087
            r0 = r17
            com.google.protobuf.ExtensionSchema<?> r5 = r0.extensionSchema
            r0 = r22
            com.google.protobuf.ExtensionRegistryLite r6 = r0.extensionRegistry
            r0 = r17
            com.google.protobuf.MessageLite r7 = r0.defaultInstance
            int r9 = com.google.protobuf.WireFormat.getTagFieldNumber(r4)
            java.lang.Object r10 = r5.findExtensionByNumber(r6, r7, r9)
            com.google.protobuf.GeneratedMessageLite$GeneratedExtension r10 = (com.google.protobuf.GeneratedMessageLite.GeneratedExtension) r10
            if (r10 == 0) goto L_0x007a
            com.google.protobuf.Protobuf r5 = com.google.protobuf.Protobuf.getInstance()
            com.google.protobuf.MessageLite r6 = r10.getMessageDefaultInstance()
            java.lang.Class r6 = r6.getClass()
            com.google.protobuf.Schema r5 = r5.schemaFor(r6)
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r22
            int r20 = com.google.protobuf.ArrayDecoders.decodeMessageField(r5, r0, r1, r2, r3)
            com.google.protobuf.GeneratedMessageLite$ExtensionDescriptor r5 = r10.descriptor
            r0 = r22
            java.lang.Object r6 = r0.object1
            r11.setField(r5, r6)
            goto L_0x001d
        L_0x007a:
            r5 = r19
            r6 = r20
            r7 = r21
            r9 = r22
            int r20 = com.google.protobuf.ArrayDecoders.decodeUnknownField(r4, r5, r6, r7, r8, r9)
            goto L_0x001d
        L_0x0087:
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r22
            int r20 = com.google.protobuf.ArrayDecoders.skipField(r4, r0, r1, r2, r3)
            goto L_0x001d
        L_0x0094:
            r15 = 0
            r13 = 0
        L_0x0096:
            r0 = r20
            r1 = r21
            if (r0 >= r1) goto L_0x00b9
            r0 = r19
            r1 = r20
            r2 = r22
            int r20 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2)
            r0 = r22
            int r14 = r0.int1
            int r12 = com.google.protobuf.WireFormat.getTagFieldNumber(r14)
            int r16 = com.google.protobuf.WireFormat.getTagWireType(r14)
            switch(r12) {
                case 2: goto L_0x00c5;
                case 3: goto L_0x00e8;
                default: goto L_0x00b5;
            }
        L_0x00b5:
            int r5 = com.google.protobuf.WireFormat.MESSAGE_SET_ITEM_END_TAG
            if (r14 != r5) goto L_0x0127
        L_0x00b9:
            if (r13 == 0) goto L_0x001d
            r5 = 2
            int r5 = com.google.protobuf.WireFormat.makeTag(r15, r5)
            r8.storeField(r5, r13)
            goto L_0x001d
        L_0x00c5:
            if (r16 != 0) goto L_0x00b5
            r0 = r19
            r1 = r20
            r2 = r22
            int r20 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2)
            r0 = r22
            int r15 = r0.int1
            r0 = r17
            com.google.protobuf.ExtensionSchema<?> r5 = r0.extensionSchema
            r0 = r22
            com.google.protobuf.ExtensionRegistryLite r6 = r0.extensionRegistry
            r0 = r17
            com.google.protobuf.MessageLite r7 = r0.defaultInstance
            java.lang.Object r10 = r5.findExtensionByNumber(r6, r7, r15)
            com.google.protobuf.GeneratedMessageLite$GeneratedExtension r10 = (com.google.protobuf.GeneratedMessageLite.GeneratedExtension) r10
            goto L_0x0096
        L_0x00e8:
            if (r10 == 0) goto L_0x0110
            com.google.protobuf.Protobuf r5 = com.google.protobuf.Protobuf.getInstance()
            com.google.protobuf.MessageLite r6 = r10.getMessageDefaultInstance()
            java.lang.Class r6 = r6.getClass()
            com.google.protobuf.Schema r5 = r5.schemaFor(r6)
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r22
            int r20 = com.google.protobuf.ArrayDecoders.decodeMessageField(r5, r0, r1, r2, r3)
            com.google.protobuf.GeneratedMessageLite$ExtensionDescriptor r5 = r10.descriptor
            r0 = r22
            java.lang.Object r6 = r0.object1
            r11.setField(r5, r6)
            goto L_0x0096
        L_0x0110:
            r5 = 2
            r0 = r16
            if (r0 != r5) goto L_0x00b5
            r0 = r19
            r1 = r20
            r2 = r22
            int r20 = com.google.protobuf.ArrayDecoders.decodeBytes(r0, r1, r2)
            r0 = r22
            java.lang.Object r13 = r0.object1
            com.google.protobuf.ByteString r13 = (com.google.protobuf.ByteString) r13
            goto L_0x0096
        L_0x0127:
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r22
            int r20 = com.google.protobuf.ArrayDecoders.skipField(r14, r0, r1, r2, r3)
            goto L_0x0096
        L_0x0135:
            r0 = r20
            r1 = r21
            if (r0 == r1) goto L_0x0140
            com.google.protobuf.InvalidProtocolBufferException r5 = com.google.protobuf.InvalidProtocolBufferException.parseFailure()
            throw r5
        L_0x0140:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSetSchema.mergeFrom(java.lang.Object, byte[], int, int, com.google.protobuf.ArrayDecoders$Registers):void");
    }

    public void mergeFrom(T message, Reader reader, ExtensionRegistryLite extensionRegistry) throws IOException {
        mergeFromHelper(this.unknownFieldSchema, this.extensionSchema, message, reader, extensionRegistry);
    }

    private <UT, UB, ET extends FieldSet.FieldDescriptorLite<ET>> void mergeFromHelper(UnknownFieldSchema<UT, UB> unknownFieldSchema2, ExtensionSchema<ET> extensionSchema2, T message, Reader reader, ExtensionRegistryLite extensionRegistry) throws IOException {
        UB unknownFields = unknownFieldSchema2.getBuilderFromMessage(message);
        FieldSet<ET> extensions = extensionSchema2.getMutableExtensions(message);
        do {
            try {
                if (reader.getFieldNumber() == Integer.MAX_VALUE) {
                    unknownFieldSchema2.setBuilderToMessage(message, unknownFields);
                    return;
                }
            } finally {
                unknownFieldSchema2.setBuilderToMessage(message, unknownFields);
            }
        } while (parseMessageSetItemOrUnknownField(reader, extensionRegistry, extensionSchema2, extensions, unknownFieldSchema2, unknownFields));
    }

    public void makeImmutable(T message) {
        this.unknownFieldSchema.makeImmutable(message);
        this.extensionSchema.makeImmutable(message);
    }

    private <UT, UB, ET extends FieldSet.FieldDescriptorLite<ET>> boolean parseMessageSetItemOrUnknownField(Reader reader, ExtensionRegistryLite extensionRegistry, ExtensionSchema<ET> extensionSchema2, FieldSet<ET> extensions, UnknownFieldSchema<UT, UB> unknownFieldSchema2, UB unknownFields) throws IOException {
        int startTag = reader.getTag();
        if (startTag == WireFormat.MESSAGE_SET_ITEM_TAG) {
            int typeId = 0;
            ByteString rawBytes = null;
            Object obj = null;
            while (reader.getFieldNumber() != Integer.MAX_VALUE) {
                int tag = reader.getTag();
                if (tag == WireFormat.MESSAGE_SET_TYPE_ID_TAG) {
                    typeId = reader.readUInt32();
                    obj = extensionSchema2.findExtensionByNumber(extensionRegistry, this.defaultInstance, typeId);
                } else if (tag == WireFormat.MESSAGE_SET_MESSAGE_TAG) {
                    if (obj != null) {
                        extensionSchema2.parseLengthPrefixedMessageSetItem(reader, obj, extensionRegistry, extensions);
                    } else {
                        rawBytes = reader.readBytes();
                    }
                } else if (!reader.skipField()) {
                    break;
                }
            }
            if (reader.getTag() != WireFormat.MESSAGE_SET_ITEM_END_TAG) {
                throw InvalidProtocolBufferException.invalidEndTag();
            } else if (rawBytes == null) {
                return true;
            } else {
                if (obj != null) {
                    extensionSchema2.parseMessageSetItem(rawBytes, obj, extensionRegistry, extensions);
                    return true;
                }
                unknownFieldSchema2.addLengthDelimited(unknownFields, typeId, rawBytes);
                return true;
            }
        } else if (WireFormat.getTagWireType(startTag) != 2) {
            return reader.skipField();
        } else {
            Object extension = extensionSchema2.findExtensionByNumber(extensionRegistry, this.defaultInstance, WireFormat.getTagFieldNumber(startTag));
            if (extension == null) {
                return unknownFieldSchema2.mergeOneFieldFrom(unknownFields, reader);
            }
            extensionSchema2.parseLengthPrefixedMessageSetItem(reader, extension, extensionRegistry, extensions);
            return true;
        }
    }

    public final boolean isInitialized(T message) {
        return this.extensionSchema.getExtensions(message).isInitialized();
    }

    public int getSerializedSize(T message) {
        int size = 0 + getUnknownFieldsSerializedSize(this.unknownFieldSchema, message);
        if (this.hasExtensions) {
            return size + this.extensionSchema.getExtensions(message).getMessageSetSerializedSize();
        }
        return size;
    }

    private <UT, UB> int getUnknownFieldsSerializedSize(UnknownFieldSchema<UT, UB> schema, T message) {
        return schema.getSerializedSizeAsMessageSet(schema.getFromMessage(message));
    }
}
