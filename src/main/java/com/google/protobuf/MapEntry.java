package com.google.protobuf;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.Message;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class MapEntry<K, V> extends AbstractMessage {
    private volatile int cachedSerializedSize;
    /* access modifiers changed from: private */
    public final K key;
    private final Metadata<K, V> metadata;
    /* access modifiers changed from: private */
    public final V value;

    private static final class Metadata<K, V> extends MapEntryLite.Metadata<K, V> {
        public final Descriptors.Descriptor descriptor;
        public final Parser<MapEntry<K, V>> parser = new AbstractParser<MapEntry<K, V>>() {
            /* class com.google.protobuf.MapEntry.Metadata.AnonymousClass1 */

            public MapEntry<K, V> parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new MapEntry<>(Metadata.this, input, extensionRegistry);
            }
        };

        public Metadata(Descriptors.Descriptor descriptor2, MapEntry<K, V> defaultInstance, WireFormat.FieldType keyType, WireFormat.FieldType valueType) {
            super(keyType, defaultInstance.key, valueType, defaultInstance.value);
            this.descriptor = descriptor2;
        }
    }

    private MapEntry(Descriptors.Descriptor descriptor, WireFormat.FieldType keyType, K defaultKey, WireFormat.FieldType valueType, V defaultValue) {
        this.cachedSerializedSize = -1;
        this.key = defaultKey;
        this.value = defaultValue;
        this.metadata = new Metadata<>(descriptor, this, keyType, valueType);
    }

    private MapEntry(Metadata metadata2, K key2, V value2) {
        this.cachedSerializedSize = -1;
        this.key = key2;
        this.value = value2;
        this.metadata = metadata2;
    }

    private MapEntry(Metadata<K, V> metadata2, CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        this.cachedSerializedSize = -1;
        try {
            this.metadata = metadata2;
            Map.Entry<K, V> entry = MapEntryLite.parseEntry(input, metadata2, extensionRegistry);
            this.key = entry.getKey();
            this.value = entry.getValue();
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (IOException e2) {
            throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
        }
    }

    public static <K, V> MapEntry<K, V> newDefaultInstance(Descriptors.Descriptor descriptor, WireFormat.FieldType keyType, K defaultKey, WireFormat.FieldType valueType, V defaultValue) {
        return new MapEntry<>(descriptor, keyType, defaultKey, valueType, defaultValue);
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public int getSerializedSize() {
        if (this.cachedSerializedSize != -1) {
            return this.cachedSerializedSize;
        }
        int size = MapEntryLite.computeSerializedSize(this.metadata, this.key, this.value);
        this.cachedSerializedSize = size;
        return size;
    }

    public void writeTo(CodedOutputStream output) throws IOException {
        MapEntryLite.writeTo(output, this.metadata, this.key, this.value);
    }

    public boolean isInitialized() {
        return isInitialized(this.metadata, this.value);
    }

    public Parser<MapEntry<K, V>> getParserForType() {
        return this.metadata.parser;
    }

    public Builder<K, V> newBuilderForType() {
        return new Builder<>(this.metadata);
    }

    public Builder<K, V> toBuilder() {
        return new Builder<>(this.metadata, this.key, this.value, true, true);
    }

    public MapEntry<K, V> getDefaultInstanceForType() {
        return new MapEntry<>(this.metadata, this.metadata.defaultKey, this.metadata.defaultValue);
    }

    public Descriptors.Descriptor getDescriptorForType() {
        return this.metadata.descriptor;
    }

    public Map<Descriptors.FieldDescriptor, Object> getAllFields() {
        TreeMap<Descriptors.FieldDescriptor, Object> result = new TreeMap<>();
        for (Descriptors.FieldDescriptor field : this.metadata.descriptor.getFields()) {
            if (hasField(field)) {
                result.put(field, getField(field));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private void checkFieldDescriptor(Descriptors.FieldDescriptor field) {
        if (field.getContainingType() != this.metadata.descriptor) {
            throw new RuntimeException("Wrong FieldDescriptor \"" + field.getFullName() + "\" used in message \"" + this.metadata.descriptor.getFullName());
        }
    }

    public boolean hasField(Descriptors.FieldDescriptor field) {
        checkFieldDescriptor(field);
        return true;
    }

    public Object getField(Descriptors.FieldDescriptor field) {
        checkFieldDescriptor(field);
        Object result = field.getNumber() == 1 ? getKey() : getValue();
        if (field.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
            return field.getEnumType().findValueByNumberCreatingIfUnknown(((Integer) result).intValue());
        }
        return result;
    }

    public int getRepeatedFieldCount(Descriptors.FieldDescriptor field) {
        throw new RuntimeException("There is no repeated field in a map entry message.");
    }

    public Object getRepeatedField(Descriptors.FieldDescriptor field, int index) {
        throw new RuntimeException("There is no repeated field in a map entry message.");
    }

    public UnknownFieldSet getUnknownFields() {
        return UnknownFieldSet.getDefaultInstance();
    }

    public static class Builder<K, V> extends AbstractMessage.Builder<Builder<K, V>> {
        private boolean hasKey;
        private boolean hasValue;
        private K key;
        private final Metadata<K, V> metadata;
        private V value;

        private Builder(Metadata<K, V> metadata2) {
            this(metadata2, metadata2.defaultKey, metadata2.defaultValue, false, false);
        }

        private Builder(Metadata<K, V> metadata2, K key2, V value2, boolean hasKey2, boolean hasValue2) {
            this.metadata = metadata2;
            this.key = key2;
            this.value = value2;
            this.hasKey = hasKey2;
            this.hasValue = hasValue2;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public Builder<K, V> setKey(K key2) {
            this.key = key2;
            this.hasKey = true;
            return this;
        }

        public Builder<K, V> clearKey() {
            this.key = this.metadata.defaultKey;
            this.hasKey = false;
            return this;
        }

        public Builder<K, V> setValue(V value2) {
            this.value = value2;
            this.hasValue = true;
            return this;
        }

        public Builder<K, V> clearValue() {
            this.value = this.metadata.defaultValue;
            this.hasValue = false;
            return this;
        }

        public MapEntry<K, V> build() {
            MapEntry<K, V> result = buildPartial();
            if (result.isInitialized()) {
                return result;
            }
            throw newUninitializedMessageException((Message) result);
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.google.protobuf.MapEntry.<init>(com.google.protobuf.MapEntry$Metadata, java.lang.Object, java.lang.Object, com.google.protobuf.MapEntry$1):void
         arg types: [com.google.protobuf.MapEntry$Metadata<K, V>, K, V, ?[OBJECT, ARRAY]]
         candidates:
          com.google.protobuf.MapEntry.<init>(com.google.protobuf.MapEntry$Metadata, com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite, com.google.protobuf.MapEntry$1):void
          com.google.protobuf.MapEntry.<init>(com.google.protobuf.MapEntry$Metadata, java.lang.Object, java.lang.Object, com.google.protobuf.MapEntry$1):void */
        public MapEntry<K, V> buildPartial() {
            return new MapEntry<>((Metadata) this.metadata, (Object) this.key, (Object) this.value);
        }

        public Descriptors.Descriptor getDescriptorForType() {
            return this.metadata.descriptor;
        }

        private void checkFieldDescriptor(Descriptors.FieldDescriptor field) {
            if (field.getContainingType() != this.metadata.descriptor) {
                throw new RuntimeException("Wrong FieldDescriptor \"" + field.getFullName() + "\" used in message \"" + this.metadata.descriptor.getFullName());
            }
        }

        public Message.Builder newBuilderForField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            if (field.getNumber() == 2 && field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                return ((Message) this.value).newBuilderForType();
            }
            throw new RuntimeException("\"" + field.getFullName() + "\" is not a message value field.");
        }

        public Builder<K, V> setField(Descriptors.FieldDescriptor field, Object value2) {
            checkFieldDescriptor(field);
            if (field.getNumber() == 1) {
                setKey(value2);
            } else {
                if (field.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
                    value2 = Integer.valueOf(((Descriptors.EnumValueDescriptor) value2).getNumber());
                } else if (field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE && value2 != null && !this.metadata.defaultValue.getClass().isInstance(value2)) {
                    value2 = ((Message) this.metadata.defaultValue).toBuilder().mergeFrom((Message) value2).build();
                }
                setValue(value2);
            }
            return this;
        }

        public Builder<K, V> clearField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            if (field.getNumber() == 1) {
                clearKey();
            } else {
                clearValue();
            }
            return this;
        }

        public Builder<K, V> setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value2) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        public Builder<K, V> addRepeatedField(Descriptors.FieldDescriptor field, Object value2) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        public Builder<K, V> setUnknownFields(UnknownFieldSet unknownFields) {
            return this;
        }

        public MapEntry<K, V> getDefaultInstanceForType() {
            return new MapEntry<>(this.metadata, this.metadata.defaultKey, this.metadata.defaultValue);
        }

        public boolean isInitialized() {
            return MapEntry.isInitialized(this.metadata, this.value);
        }

        public Map<Descriptors.FieldDescriptor, Object> getAllFields() {
            TreeMap<Descriptors.FieldDescriptor, Object> result = new TreeMap<>();
            for (Descriptors.FieldDescriptor field : this.metadata.descriptor.getFields()) {
                if (hasField(field)) {
                    result.put(field, getField(field));
                }
            }
            return Collections.unmodifiableMap(result);
        }

        public boolean hasField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            return field.getNumber() == 1 ? this.hasKey : this.hasValue;
        }

        public Object getField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            Object result = field.getNumber() == 1 ? getKey() : getValue();
            if (field.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
                return field.getEnumType().findValueByNumberCreatingIfUnknown(((Integer) result).intValue());
            }
            return result;
        }

        public int getRepeatedFieldCount(Descriptors.FieldDescriptor field) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        public Object getRepeatedField(Descriptors.FieldDescriptor field, int index) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        public UnknownFieldSet getUnknownFields() {
            return UnknownFieldSet.getDefaultInstance();
        }

        public Builder<K, V> clone() {
            return new Builder<>(this.metadata, this.key, this.value, this.hasKey, this.hasValue);
        }
    }

    /* access modifiers changed from: private */
    public static <V> boolean isInitialized(Metadata metadata2, V value2) {
        if (metadata2.valueType.getJavaType() == WireFormat.JavaType.MESSAGE) {
            return ((MessageLite) value2).isInitialized();
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public final Metadata<K, V> getMetadata() {
        return this.metadata;
    }
}
