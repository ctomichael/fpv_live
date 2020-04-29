package com.google.protobuf;

import com.google.protobuf.MessageLite;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class MapEntryLite<K, V> {
    private static final int KEY_FIELD_NUMBER = 1;
    private static final int VALUE_FIELD_NUMBER = 2;
    private final K key;
    private final Metadata<K, V> metadata;
    private final V value;

    static class Metadata<K, V> {
        public final K defaultKey;
        public final V defaultValue;
        public final WireFormat.FieldType keyType;
        public final WireFormat.FieldType valueType;

        public Metadata(WireFormat.FieldType keyType2, K defaultKey2, WireFormat.FieldType valueType2, V defaultValue2) {
            this.keyType = keyType2;
            this.defaultKey = defaultKey2;
            this.valueType = valueType2;
            this.defaultValue = defaultValue2;
        }
    }

    private MapEntryLite(WireFormat.FieldType keyType, K defaultKey, WireFormat.FieldType valueType, V defaultValue) {
        this.metadata = new Metadata<>(keyType, defaultKey, valueType, defaultValue);
        this.key = defaultKey;
        this.value = defaultValue;
    }

    private MapEntryLite(Metadata<K, V> metadata2, K key2, V value2) {
        this.metadata = metadata2;
        this.key = key2;
        this.value = value2;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public static <K, V> MapEntryLite<K, V> newDefaultInstance(WireFormat.FieldType keyType, K defaultKey, WireFormat.FieldType valueType, V defaultValue) {
        return new MapEntryLite<>(keyType, defaultKey, valueType, defaultValue);
    }

    static <K, V> void writeTo(CodedOutputStream output, Metadata<K, V> metadata2, K key2, V value2) throws IOException {
        FieldSet.writeElement(output, metadata2.keyType, 1, key2);
        FieldSet.writeElement(output, metadata2.valueType, 2, value2);
    }

    static <K, V> int computeSerializedSize(Metadata<K, V> metadata2, K key2, V value2) {
        return FieldSet.computeElementSize(metadata2.keyType, 1, key2) + FieldSet.computeElementSize(metadata2.valueType, 2, value2);
    }

    static <T> T parseField(CodedInputStream input, ExtensionRegistryLite extensionRegistry, WireFormat.FieldType type, T value2) throws IOException {
        switch (type) {
            case MESSAGE:
                MessageLite.Builder subBuilder = ((MessageLite) value2).toBuilder();
                input.readMessage(subBuilder, extensionRegistry);
                return subBuilder.buildPartial();
            case ENUM:
                return Integer.valueOf(input.readEnum());
            case GROUP:
                throw new RuntimeException("Groups are not allowed in maps.");
            default:
                return FieldSet.readPrimitiveField(input, type, true);
        }
    }

    public void serializeTo(CodedOutputStream output, int fieldNumber, K key2, V value2) throws IOException {
        output.writeTag(fieldNumber, 2);
        output.writeUInt32NoTag(computeSerializedSize(this.metadata, key2, value2));
        writeTo(output, this.metadata, key2, value2);
    }

    public int computeMessageSize(int fieldNumber, K key2, V value2) {
        return CodedOutputStream.computeTagSize(fieldNumber) + CodedOutputStream.computeLengthDelimitedFieldSize(computeSerializedSize(this.metadata, key2, value2));
    }

    public Map.Entry<K, V> parseEntry(ByteString bytes, ExtensionRegistryLite extensionRegistry) throws IOException {
        return parseEntry(bytes.newCodedInput(), this.metadata, extensionRegistry);
    }

    static <K, V> Map.Entry<K, V> parseEntry(CodedInputStream input, Metadata<K, V> metadata2, ExtensionRegistryLite extensionRegistry) throws IOException {
        K key2 = metadata2.defaultKey;
        V value2 = metadata2.defaultValue;
        while (true) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            } else if (tag == WireFormat.makeTag(1, metadata2.keyType.getWireType())) {
                key2 = parseField(input, extensionRegistry, metadata2.keyType, key2);
            } else if (tag == WireFormat.makeTag(2, metadata2.valueType.getWireType())) {
                value2 = parseField(input, extensionRegistry, metadata2.valueType, value2);
            } else if (!input.skipField(tag)) {
                break;
            }
        }
        return new AbstractMap.SimpleImmutableEntry(key2, value2);
    }

    public void parseInto(MapFieldLite<K, V> map, CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        int oldLimit = input.pushLimit(input.readRawVarint32());
        K key2 = this.metadata.defaultKey;
        V value2 = this.metadata.defaultValue;
        while (true) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            } else if (tag == WireFormat.makeTag(1, this.metadata.keyType.getWireType())) {
                key2 = parseField(input, extensionRegistry, this.metadata.keyType, key2);
            } else if (tag == WireFormat.makeTag(2, this.metadata.valueType.getWireType())) {
                value2 = parseField(input, extensionRegistry, this.metadata.valueType, value2);
            } else if (!input.skipField(tag)) {
                break;
            }
        }
        input.checkLastTagWas(0);
        input.popLimit(oldLimit);
        map.put(key2, value2);
    }

    /* access modifiers changed from: package-private */
    public Metadata<K, V> getMetadata() {
        return this.metadata;
    }
}
