package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class ExtensionSchemaLite extends ExtensionSchema<GeneratedMessageLite.ExtensionDescriptor> {
    ExtensionSchemaLite() {
    }

    /* access modifiers changed from: package-private */
    public boolean hasExtensions(MessageLite prototype) {
        return prototype instanceof GeneratedMessageLite.ExtendableMessage;
    }

    /* access modifiers changed from: package-private */
    public FieldSet<GeneratedMessageLite.ExtensionDescriptor> getExtensions(Object message) {
        return ((GeneratedMessageLite.ExtendableMessage) message).extensions;
    }

    /* access modifiers changed from: package-private */
    public void setExtensions(Object message, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions) {
        ((GeneratedMessageLite.ExtendableMessage) message).extensions = extensions;
    }

    /* access modifiers changed from: package-private */
    public FieldSet<GeneratedMessageLite.ExtensionDescriptor> getMutableExtensions(Object message) {
        return ((GeneratedMessageLite.ExtendableMessage) message).ensureExtensionsAreMutable();
    }

    /* access modifiers changed from: package-private */
    public void makeImmutable(Object message) {
        getExtensions(message).makeImmutable();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.SchemaUtil.filterUnknownEnumList(int, java.util.List<java.lang.Integer>, com.google.protobuf.Internal$EnumLiteMap<?>, java.lang.Object, com.google.protobuf.UnknownFieldSchema):UB
     arg types: [int, java.util.List<java.lang.Double>, com.google.protobuf.Internal$EnumLiteMap<?>, UB, com.google.protobuf.UnknownFieldSchema<UT, UB>]
     candidates:
      com.google.protobuf.SchemaUtil.filterUnknownEnumList(int, java.util.List<java.lang.Integer>, com.google.protobuf.Internal$EnumVerifier, java.lang.Object, com.google.protobuf.UnknownFieldSchema):UB
      com.google.protobuf.SchemaUtil.filterUnknownEnumList(int, java.util.List<java.lang.Integer>, com.google.protobuf.Internal$EnumLiteMap<?>, java.lang.Object, com.google.protobuf.UnknownFieldSchema):UB */
    /* access modifiers changed from: package-private */
    public <UT, UB> UB parseExtension(Reader reader, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions, UB unknownFields, UnknownFieldSchema<UT, UB> unknownFieldSchema) throws IOException {
        Object value;
        GeneratedMessageLite.GeneratedExtension<?, ?> extension = (GeneratedMessageLite.GeneratedExtension) extensionObject;
        int fieldNumber = extension.getNumber();
        if (!extension.descriptor.isRepeated() || !extension.descriptor.isPacked()) {
            Object value2 = null;
            if (extension.getLiteType() != WireFormat.FieldType.ENUM) {
                switch (extension.getLiteType()) {
                    case DOUBLE:
                        value2 = Double.valueOf(reader.readDouble());
                        break;
                    case FLOAT:
                        value2 = Float.valueOf(reader.readFloat());
                        break;
                    case INT64:
                        value2 = Long.valueOf(reader.readInt64());
                        break;
                    case UINT64:
                        value2 = Long.valueOf(reader.readUInt64());
                        break;
                    case INT32:
                        value2 = Integer.valueOf(reader.readInt32());
                        break;
                    case FIXED64:
                        value2 = Long.valueOf(reader.readFixed64());
                        break;
                    case FIXED32:
                        value2 = Integer.valueOf(reader.readFixed32());
                        break;
                    case BOOL:
                        value2 = Boolean.valueOf(reader.readBool());
                        break;
                    case UINT32:
                        value2 = Integer.valueOf(reader.readUInt32());
                        break;
                    case SFIXED32:
                        value2 = Integer.valueOf(reader.readSFixed32());
                        break;
                    case SFIXED64:
                        value2 = Long.valueOf(reader.readSFixed64());
                        break;
                    case SINT32:
                        value2 = Integer.valueOf(reader.readSInt32());
                        break;
                    case SINT64:
                        value2 = Long.valueOf(reader.readSInt64());
                        break;
                    case ENUM:
                        throw new IllegalStateException("Shouldn't reach here.");
                    case BYTES:
                        value2 = reader.readBytes();
                        break;
                    case STRING:
                        value2 = reader.readString();
                        break;
                    case GROUP:
                        value2 = reader.readGroup(extension.getMessageDefaultInstance().getClass(), extensionRegistry);
                        break;
                    case MESSAGE:
                        value2 = reader.readMessage(extension.getMessageDefaultInstance().getClass(), extensionRegistry);
                        break;
                }
            } else {
                int number = reader.readInt32();
                if (extension.descriptor.getEnumType().findValueByNumber(number) == null) {
                    return SchemaUtil.storeUnknownEnum(fieldNumber, number, unknownFields, unknownFieldSchema);
                }
                value2 = Integer.valueOf(number);
            }
            if (extension.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, value2);
            } else {
                switch (extension.getLiteType()) {
                    case GROUP:
                    case MESSAGE:
                        Object oldValue = extensions.getField(extension.descriptor);
                        if (oldValue != null) {
                            value2 = Internal.mergeMessage(oldValue, value2);
                            break;
                        }
                        break;
                }
                extensions.setField(extension.descriptor, value2);
            }
        } else {
            switch (extension.getLiteType()) {
                case DOUBLE:
                    List<Double> list = new ArrayList<>();
                    reader.readDoubleList(list);
                    value = list;
                    break;
                case FLOAT:
                    List list2 = new ArrayList();
                    reader.readFloatList(list2);
                    value = list2;
                    break;
                case INT64:
                    List list3 = new ArrayList();
                    reader.readInt64List(list3);
                    value = list3;
                    break;
                case UINT64:
                    List list4 = new ArrayList();
                    reader.readUInt64List(list4);
                    value = list4;
                    break;
                case INT32:
                    List list5 = new ArrayList();
                    reader.readInt32List(list5);
                    value = list5;
                    break;
                case FIXED64:
                    List list6 = new ArrayList();
                    reader.readFixed64List(list6);
                    value = list6;
                    break;
                case FIXED32:
                    List list7 = new ArrayList();
                    reader.readFixed32List(list7);
                    value = list7;
                    break;
                case BOOL:
                    List list8 = new ArrayList();
                    reader.readBoolList(list8);
                    value = list8;
                    break;
                case UINT32:
                    List list9 = new ArrayList();
                    reader.readUInt32List(list9);
                    value = list9;
                    break;
                case SFIXED32:
                    List list10 = new ArrayList();
                    reader.readSFixed32List(list10);
                    value = list10;
                    break;
                case SFIXED64:
                    List list11 = new ArrayList();
                    reader.readSFixed64List(list11);
                    value = list11;
                    break;
                case SINT32:
                    List list12 = new ArrayList();
                    reader.readSInt32List(list12);
                    value = list12;
                    break;
                case SINT64:
                    List list13 = new ArrayList();
                    reader.readSInt64List(list13);
                    value = list13;
                    break;
                case ENUM:
                    List<Double> list14 = new ArrayList<>();
                    reader.readEnumList(list14);
                    unknownFields = SchemaUtil.filterUnknownEnumList(fieldNumber, (List<Integer>) list14, extension.descriptor.getEnumType(), (Object) unknownFields, (UnknownFieldSchema) unknownFieldSchema);
                    value = list14;
                    break;
                default:
                    throw new IllegalStateException("Type cannot be packed: " + extension.descriptor.getLiteType());
            }
            extensions.setField(extension.descriptor, value);
        }
        return unknownFields;
    }

    /* access modifiers changed from: package-private */
    public int extensionNumber(Map.Entry<?, ?> extension) {
        return ((GeneratedMessageLite.ExtensionDescriptor) extension.getKey()).getNumber();
    }

    /* access modifiers changed from: package-private */
    public void serializeExtension(Writer writer, Map.Entry<?, ?> extension) throws IOException {
        GeneratedMessageLite.ExtensionDescriptor descriptor = (GeneratedMessageLite.ExtensionDescriptor) extension.getKey();
        if (descriptor.isRepeated()) {
            switch (descriptor.getLiteType()) {
                case DOUBLE:
                    SchemaUtil.writeDoubleList(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case FLOAT:
                    SchemaUtil.writeFloatList(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case INT64:
                    SchemaUtil.writeInt64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case UINT64:
                    SchemaUtil.writeUInt64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case INT32:
                    SchemaUtil.writeInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case FIXED64:
                    SchemaUtil.writeFixed64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case FIXED32:
                    SchemaUtil.writeFixed32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case BOOL:
                    SchemaUtil.writeBoolList(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case UINT32:
                    SchemaUtil.writeUInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SFIXED32:
                    SchemaUtil.writeSFixed32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SFIXED64:
                    SchemaUtil.writeSFixed64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SINT32:
                    SchemaUtil.writeSInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SINT64:
                    SchemaUtil.writeSInt64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case ENUM:
                    SchemaUtil.writeInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case BYTES:
                    SchemaUtil.writeBytesList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case STRING:
                    SchemaUtil.writeStringList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case GROUP:
                    List<?> data = (List) extension.getValue();
                    if (data != null && !data.isEmpty()) {
                        SchemaUtil.writeGroupList(descriptor.getNumber(), (List) extension.getValue(), writer, Protobuf.getInstance().schemaFor((Class) data.get(0).getClass()));
                        return;
                    }
                    return;
                case MESSAGE:
                    List<?> data2 = (List) extension.getValue();
                    if (data2 != null && !data2.isEmpty()) {
                        SchemaUtil.writeMessageList(descriptor.getNumber(), (List) extension.getValue(), writer, Protobuf.getInstance().schemaFor((Class) data2.get(0).getClass()));
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else {
            switch (descriptor.getLiteType()) {
                case DOUBLE:
                    writer.writeDouble(descriptor.getNumber(), ((Double) extension.getValue()).doubleValue());
                    return;
                case FLOAT:
                    writer.writeFloat(descriptor.getNumber(), ((Float) extension.getValue()).floatValue());
                    return;
                case INT64:
                    writer.writeInt64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                    return;
                case UINT64:
                    writer.writeUInt64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                    return;
                case INT32:
                    writer.writeInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                    return;
                case FIXED64:
                    writer.writeFixed64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                    return;
                case FIXED32:
                    writer.writeFixed32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                    return;
                case BOOL:
                    writer.writeBool(descriptor.getNumber(), ((Boolean) extension.getValue()).booleanValue());
                    return;
                case UINT32:
                    writer.writeUInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                    return;
                case SFIXED32:
                    writer.writeSFixed32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                    return;
                case SFIXED64:
                    writer.writeSFixed64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                    return;
                case SINT32:
                    writer.writeSInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                    return;
                case SINT64:
                    writer.writeSInt64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                    return;
                case ENUM:
                    writer.writeInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                    return;
                case BYTES:
                    writer.writeBytes(descriptor.getNumber(), (ByteString) extension.getValue());
                    return;
                case STRING:
                    writer.writeString(descriptor.getNumber(), (String) extension.getValue());
                    return;
                case GROUP:
                    writer.writeGroup(descriptor.getNumber(), extension.getValue(), Protobuf.getInstance().schemaFor((Class) extension.getValue().getClass()));
                    return;
                case MESSAGE:
                    writer.writeMessage(descriptor.getNumber(), extension.getValue(), Protobuf.getInstance().schemaFor((Class) extension.getValue().getClass()));
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Object findExtensionByNumber(ExtensionRegistryLite extensionRegistry, MessageLite defaultInstance, int number) {
        return extensionRegistry.findLiteExtensionByNumber(defaultInstance, number);
    }

    /* access modifiers changed from: package-private */
    public void parseLengthPrefixedMessageSetItem(Reader reader, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions) throws IOException {
        GeneratedMessageLite.GeneratedExtension<?, ?> extension = (GeneratedMessageLite.GeneratedExtension) extensionObject;
        extensions.setField(extension.descriptor, reader.readMessage(extension.getMessageDefaultInstance().getClass(), extensionRegistry));
    }

    /* access modifiers changed from: package-private */
    public void parseMessageSetItem(ByteString data, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions) throws IOException {
        GeneratedMessageLite.GeneratedExtension<?, ?> extension = (GeneratedMessageLite.GeneratedExtension) extensionObject;
        MessageLite value = extension.getMessageDefaultInstance().newBuilderForType().buildPartial();
        Reader reader = BinaryReader.newInstance(ByteBuffer.wrap(data.toByteArray()), true);
        Protobuf.getInstance().mergeFrom(value, reader, extensionRegistry);
        extensions.setField(extension.descriptor, value);
        if (reader.getFieldNumber() != Integer.MAX_VALUE) {
            throw InvalidProtocolBufferException.invalidEndTag();
        }
    }
}
