package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class ExtensionSchemaFull extends ExtensionSchema<Descriptors.FieldDescriptor> {
    private static final long EXTENSION_FIELD_OFFSET = getExtensionsFieldOffset();

    ExtensionSchemaFull() {
    }

    private static <T> long getExtensionsFieldOffset() {
        try {
            return UnsafeUtil.objectFieldOffset(GeneratedMessageV3.ExtendableMessage.class.getDeclaredField("extensions"));
        } catch (Throwable th) {
            throw new IllegalStateException("Unable to lookup extension field offset");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasExtensions(MessageLite prototype) {
        return prototype instanceof GeneratedMessageV3.ExtendableMessage;
    }

    public FieldSet<Descriptors.FieldDescriptor> getExtensions(Object message) {
        return (FieldSet) UnsafeUtil.getObject(message, EXTENSION_FIELD_OFFSET);
    }

    /* access modifiers changed from: package-private */
    public void setExtensions(Object message, FieldSet<Descriptors.FieldDescriptor> extensions) {
        UnsafeUtil.putObject(message, EXTENSION_FIELD_OFFSET, extensions);
    }

    /* access modifiers changed from: package-private */
    public FieldSet<Descriptors.FieldDescriptor> getMutableExtensions(Object message) {
        FieldSet<Descriptors.FieldDescriptor> extensions = getExtensions(message);
        if (!extensions.isImmutable()) {
            return extensions;
        }
        FieldSet<Descriptors.FieldDescriptor> extensions2 = extensions.clone();
        setExtensions(message, extensions2);
        return extensions2;
    }

    /* access modifiers changed from: package-private */
    public void makeImmutable(Object message) {
        getExtensions(message).makeImmutable();
    }

    /* access modifiers changed from: package-private */
    public <UT, UB> UB parseExtension(Reader reader, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<Descriptors.FieldDescriptor> extensions, UB unknownFields, UnknownFieldSchema<UT, UB> unknownFieldSchema) throws IOException {
        List<Double> value;
        ExtensionRegistry.ExtensionInfo extension = (ExtensionRegistry.ExtensionInfo) extensionObject;
        int fieldNumber = extension.descriptor.getNumber();
        if (!extension.descriptor.isRepeated() || !extension.descriptor.isPacked()) {
            Object value2 = null;
            if (extension.descriptor.getLiteType() != WireFormat.FieldType.ENUM) {
                switch (extension.descriptor.getLiteType()) {
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
                        value2 = reader.readGroup(extension.defaultInstance.getClass(), extensionRegistry);
                        break;
                    case MESSAGE:
                        value2 = reader.readMessage(extension.defaultInstance.getClass(), extensionRegistry);
                        break;
                }
            } else {
                int number = reader.readInt32();
                Descriptors.EnumValueDescriptor enumValue = extension.descriptor.getEnumType().findValueByNumber(number);
                if (enumValue == null) {
                    return SchemaUtil.storeUnknownEnum(fieldNumber, number, unknownFields, unknownFieldSchema);
                }
                value2 = enumValue;
            }
            if (extension.descriptor.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, value2);
            } else {
                switch (extension.descriptor.getLiteType()) {
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
            switch (extension.descriptor.getLiteType()) {
                case DOUBLE:
                    List<Double> list = new ArrayList<>();
                    reader.readDoubleList(list);
                    value = list;
                    break;
                case FLOAT:
                    List arrayList = new ArrayList();
                    reader.readFloatList(arrayList);
                    value = arrayList;
                    break;
                case INT64:
                    List arrayList2 = new ArrayList();
                    reader.readInt64List(arrayList2);
                    value = arrayList2;
                    break;
                case UINT64:
                    List arrayList3 = new ArrayList();
                    reader.readUInt64List(arrayList3);
                    value = arrayList3;
                    break;
                case INT32:
                    List arrayList4 = new ArrayList();
                    reader.readInt32List(arrayList4);
                    value = arrayList4;
                    break;
                case FIXED64:
                    List arrayList5 = new ArrayList();
                    reader.readFixed64List(arrayList5);
                    value = arrayList5;
                    break;
                case FIXED32:
                    List arrayList6 = new ArrayList();
                    reader.readFixed32List(arrayList6);
                    value = arrayList6;
                    break;
                case BOOL:
                    List arrayList7 = new ArrayList();
                    reader.readBoolList(arrayList7);
                    value = arrayList7;
                    break;
                case UINT32:
                    List arrayList8 = new ArrayList();
                    reader.readUInt32List(arrayList8);
                    value = arrayList8;
                    break;
                case SFIXED32:
                    List arrayList9 = new ArrayList();
                    reader.readSFixed32List(arrayList9);
                    value = arrayList9;
                    break;
                case SFIXED64:
                    List arrayList10 = new ArrayList();
                    reader.readSFixed64List(arrayList10);
                    value = arrayList10;
                    break;
                case SINT32:
                    List arrayList11 = new ArrayList();
                    reader.readSInt32List(arrayList11);
                    value = arrayList11;
                    break;
                case SINT64:
                    List arrayList12 = new ArrayList();
                    reader.readSInt64List(arrayList12);
                    value = arrayList12;
                    break;
                case ENUM:
                    List<Integer> list2 = new ArrayList<>();
                    reader.readEnumList(list2);
                    List<Double> enumList = new ArrayList<>();
                    for (Integer num : list2) {
                        int number2 = num.intValue();
                        Descriptors.EnumValueDescriptor enumDescriptor = extension.descriptor.getEnumType().findValueByNumber(number2);
                        if (enumDescriptor != null) {
                            enumList.add(enumDescriptor);
                        } else {
                            unknownFields = SchemaUtil.storeUnknownEnum(fieldNumber, number2, unknownFields, unknownFieldSchema);
                        }
                    }
                    value = enumList;
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
        return ((Descriptors.FieldDescriptor) extension.getKey()).getNumber();
    }

    /* access modifiers changed from: package-private */
    public void serializeExtension(Writer writer, Map.Entry<?, ?> extension) throws IOException {
        Descriptors.FieldDescriptor descriptor = (Descriptors.FieldDescriptor) extension.getKey();
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
                    List<Integer> list = new ArrayList<>();
                    for (Descriptors.EnumValueDescriptor d : (List) extension.getValue()) {
                        list.add(Integer.valueOf(d.getNumber()));
                    }
                    SchemaUtil.writeInt32List(descriptor.getNumber(), list, writer, descriptor.isPacked());
                    return;
                case BYTES:
                    SchemaUtil.writeBytesList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case STRING:
                    SchemaUtil.writeStringList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case GROUP:
                    SchemaUtil.writeGroupList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case MESSAGE:
                    SchemaUtil.writeMessageList(descriptor.getNumber(), (List) extension.getValue(), writer);
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
                    writer.writeInt32(descriptor.getNumber(), ((Descriptors.EnumValueDescriptor) extension.getValue()).getNumber());
                    return;
                case BYTES:
                    writer.writeBytes(descriptor.getNumber(), (ByteString) extension.getValue());
                    return;
                case STRING:
                    writer.writeString(descriptor.getNumber(), (String) extension.getValue());
                    return;
                case GROUP:
                    writer.writeGroup(descriptor.getNumber(), extension.getValue());
                    return;
                case MESSAGE:
                    writer.writeMessage(descriptor.getNumber(), extension.getValue());
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Object findExtensionByNumber(ExtensionRegistryLite extensionRegistry, MessageLite defaultInstance, int number) {
        return ((ExtensionRegistry) extensionRegistry).findExtensionByNumber(((Message) defaultInstance).getDescriptorForType(), number);
    }

    /* access modifiers changed from: package-private */
    public void parseLengthPrefixedMessageSetItem(Reader reader, Object extension, ExtensionRegistryLite extensionRegistry, FieldSet<Descriptors.FieldDescriptor> extensions) throws IOException {
        ExtensionRegistry.ExtensionInfo extensionInfo = (ExtensionRegistry.ExtensionInfo) extension;
        if (ExtensionRegistryLite.isEagerlyParseMessageSets()) {
            extensions.setField(extensionInfo.descriptor, reader.readMessage(extensionInfo.defaultInstance.getClass(), extensionRegistry));
            return;
        }
        extensions.setField(extensionInfo.descriptor, new LazyField(extensionInfo.defaultInstance, extensionRegistry, reader.readBytes()));
    }

    /* access modifiers changed from: package-private */
    public void parseMessageSetItem(ByteString data, Object extension, ExtensionRegistryLite extensionRegistry, FieldSet<Descriptors.FieldDescriptor> extensions) throws IOException {
        ExtensionRegistry.ExtensionInfo extensionInfo = (ExtensionRegistry.ExtensionInfo) extension;
        Message value = extensionInfo.defaultInstance.newBuilderForType().buildPartial();
        if (ExtensionRegistryLite.isEagerlyParseMessageSets()) {
            Reader reader = BinaryReader.newInstance(ByteBuffer.wrap(data.toByteArray()), true);
            Protobuf.getInstance().mergeFrom(value, reader, extensionRegistry);
            extensions.setField(extensionInfo.descriptor, value);
            if (reader.getFieldNumber() != Integer.MAX_VALUE) {
                throw InvalidProtocolBufferException.invalidEndTag();
            }
            return;
        }
        extensions.setField(extensionInfo.descriptor, new LazyField(extensionInfo.defaultInstance, extensionRegistry, data));
    }
}
