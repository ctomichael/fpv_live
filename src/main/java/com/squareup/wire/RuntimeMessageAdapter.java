package com.squareup.wire;

import com.squareup.wire.Message;
import com.squareup.wire.Message.Builder;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class RuntimeMessageAdapter<M extends Message<M, B>, B extends Message.Builder<M, B>> extends ProtoAdapter<M> {
    private static final String REDACTED = "██";
    private final Class<B> builderType;
    private final Map<Integer, FieldBinding<M, B>> fieldBindings;
    private final Class<M> messageType;

    static <M extends Message<M, B>, B extends Message.Builder<M, B>> RuntimeMessageAdapter<M, B> create(Class<M> messageType2) {
        Class<B> builderType2 = getBuilderType(messageType2);
        Map<Integer, FieldBinding<M, B>> fieldBindings2 = new LinkedHashMap<>();
        Field[] declaredFields = messageType2.getDeclaredFields();
        for (Field messageField : declaredFields) {
            WireField wireField = (WireField) messageField.getAnnotation(WireField.class);
            if (wireField != null) {
                fieldBindings2.put(Integer.valueOf(wireField.tag()), new FieldBinding(wireField, messageField, builderType2));
            }
        }
        return new RuntimeMessageAdapter<>(messageType2, builderType2, Collections.unmodifiableMap(fieldBindings2));
    }

    RuntimeMessageAdapter(Class<M> messageType2, Class<B> builderType2, Map<Integer, FieldBinding<M, B>> fieldBindings2) {
        super(FieldEncoding.LENGTH_DELIMITED, messageType2);
        this.messageType = messageType2;
        this.builderType = builderType2;
        this.fieldBindings = fieldBindings2;
    }

    /* access modifiers changed from: package-private */
    public Map<Integer, FieldBinding<M, B>> fieldBindings() {
        return this.fieldBindings;
    }

    /* access modifiers changed from: package-private */
    public B newBuilder() {
        try {
            return (Message.Builder) this.builderType.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new AssertionError(e);
        }
    }

    private static <M extends Message<M, B>, B extends Message.Builder<M, B>> Class<B> getBuilderType(Class<M> messageType2) {
        try {
            return Class.forName(messageType2.getName() + "$Builder");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No builder class found for message type " + messageType2.getName());
        }
    }

    public int encodedSize(M message) {
        int cachedSerializedSize = message.cachedSerializedSize;
        if (cachedSerializedSize != 0) {
            return cachedSerializedSize;
        }
        int size = 0;
        for (FieldBinding<M, B> fieldBinding : this.fieldBindings.values()) {
            Object value = fieldBinding.get(message);
            if (value != null) {
                size += fieldBinding.adapter().encodedSizeWithTag(fieldBinding.tag, value);
            }
        }
        int size2 = size + message.unknownFields().size();
        message.cachedSerializedSize = size2;
        return size2;
    }

    public void encode(ProtoWriter writer, M message) throws IOException {
        for (FieldBinding<M, B> fieldBinding : this.fieldBindings.values()) {
            Object value = fieldBinding.get(message);
            if (value != null) {
                fieldBinding.adapter().encodeWithTag(writer, fieldBinding.tag, value);
            }
        }
        writer.writeBytes(message.unknownFields());
    }

    public M redact(M message) {
        B builder = message.newBuilder();
        for (FieldBinding<M, B> fieldBinding : this.fieldBindings.values()) {
            if (!fieldBinding.redacted || fieldBinding.label != WireField.Label.REQUIRED) {
                boolean isMessage = Message.class.isAssignableFrom(fieldBinding.singleAdapter().javaType);
                if (fieldBinding.redacted || (isMessage && !fieldBinding.label.isRepeated())) {
                    Object builderValue = fieldBinding.getFromBuilder(builder);
                    if (builderValue != null) {
                        fieldBinding.set(builder, fieldBinding.adapter().redact(builderValue));
                    }
                } else if (isMessage && fieldBinding.label.isRepeated()) {
                    Internal.redactElements((List) fieldBinding.getFromBuilder(builder), fieldBinding.singleAdapter());
                }
            } else {
                throw new UnsupportedOperationException(String.format("Field '%s' in %s is required and cannot be redacted.", fieldBinding.name, this.javaType.getName()));
            }
        }
        builder.clearUnknownFields();
        return builder.build();
    }

    public boolean equals(Object o) {
        return (o instanceof RuntimeMessageAdapter) && ((RuntimeMessageAdapter) o).messageType == this.messageType;
    }

    public int hashCode() {
        return this.messageType.hashCode();
    }

    public String toString(M message) {
        StringBuilder sb = new StringBuilder();
        for (FieldBinding<M, B> fieldBinding : this.fieldBindings.values()) {
            Object value = fieldBinding.get(message);
            if (value != null) {
                StringBuilder append = sb.append(", ").append(fieldBinding.name).append('=');
                if (fieldBinding.redacted) {
                    value = REDACTED;
                }
                append.append(value);
            }
        }
        sb.replace(0, 2, this.messageType.getSimpleName() + '{');
        return sb.append('}').toString();
    }

    public M decode(ProtoReader reader) throws IOException {
        ProtoAdapter<?> adapter;
        B builder = newBuilder();
        long token = reader.beginMessage();
        while (true) {
            int tag = reader.nextTag();
            if (tag != -1) {
                FieldBinding<M, B> fieldBinding = this.fieldBindings.get(Integer.valueOf(tag));
                if (fieldBinding != null) {
                    try {
                        if (fieldBinding.isMap()) {
                            adapter = fieldBinding.adapter();
                        } else {
                            adapter = fieldBinding.singleAdapter();
                        }
                        fieldBinding.value(builder, adapter.decode(reader));
                    } catch (ProtoAdapter.EnumConstantNotFoundException e) {
                        builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e.value));
                    }
                } else {
                    FieldEncoding fieldEncoding = reader.peekFieldEncoding();
                    builder.addUnknownField(tag, fieldEncoding, fieldEncoding.rawProtoAdapter().decode(reader));
                }
            } else {
                reader.endMessage(token);
                return builder.build();
            }
        }
    }
}
