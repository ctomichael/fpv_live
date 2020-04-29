package com.squareup.wire;

import com.squareup.wire.Message;
import com.squareup.wire.Message.Builder;
import com.squareup.wire.WireField;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

final class FieldBinding<M extends Message<M, B>, B extends Message.Builder<M, B>> {
    private ProtoAdapter<Object> adapter;
    private final String adapterString;
    private final Field builderField;
    private final Method builderMethod;
    private ProtoAdapter<?> keyAdapter;
    private final String keyAdapterString;
    public final WireField.Label label;
    private final Field messageField;
    public final String name;
    public final boolean redacted;
    private ProtoAdapter<?> singleAdapter;
    public final int tag;

    private static Field getBuilderField(Class<?> builderType, String name2) {
        try {
            return builderType.getField(name2);
        } catch (NoSuchFieldException e) {
            throw new AssertionError("No builder field " + builderType.getName() + "." + name2);
        }
    }

    private static Method getBuilderMethod(Class<?> builderType, String name2, Class<?> type) {
        try {
            return builderType.getMethod(name2, type);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("No builder method " + builderType.getName() + "." + name2 + "(" + type.getName() + ")");
        }
    }

    FieldBinding(WireField wireField, Field messageField2, Class<B> builderType) {
        this.label = wireField.label();
        this.name = messageField2.getName();
        this.tag = wireField.tag();
        this.keyAdapterString = wireField.keyAdapter();
        this.adapterString = wireField.adapter();
        this.redacted = wireField.redacted();
        this.messageField = messageField2;
        this.builderField = getBuilderField(builderType, this.name);
        this.builderMethod = getBuilderMethod(builderType, this.name, messageField2.getType());
    }

    /* access modifiers changed from: package-private */
    public boolean isMap() {
        return !this.keyAdapterString.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public ProtoAdapter<?> singleAdapter() {
        ProtoAdapter<?> result = this.singleAdapter;
        if (result != null) {
            return result;
        }
        ProtoAdapter<?> result2 = ProtoAdapter.get(this.adapterString);
        this.singleAdapter = result2;
        return result2;
    }

    /* access modifiers changed from: package-private */
    public ProtoAdapter<?> keyAdapter() {
        ProtoAdapter<?> result = this.keyAdapter;
        if (result != null) {
            return result;
        }
        ProtoAdapter<?> result2 = ProtoAdapter.get(this.keyAdapterString);
        this.keyAdapter = result2;
        return result2;
    }

    /* access modifiers changed from: package-private */
    public ProtoAdapter<Object> adapter() {
        ProtoAdapter<Object> result = this.adapter;
        if (result != null) {
            return result;
        }
        if (isMap()) {
            ProtoAdapter<Object> result2 = ProtoAdapter.newMapAdapter(keyAdapter(), singleAdapter());
            this.adapter = result2;
            return result2;
        }
        ProtoAdapter<Object> result3 = singleAdapter().withLabel(this.label);
        this.adapter = result3;
        return result3;
    }

    /* access modifiers changed from: package-private */
    public void value(B builder, Object value) {
        if (this.label.isRepeated()) {
            ((List) getFromBuilder(builder)).add(value);
        } else if (!this.keyAdapterString.isEmpty()) {
            ((Map) getFromBuilder(builder)).putAll((Map) value);
        } else {
            set(builder, value);
        }
    }

    /* access modifiers changed from: package-private */
    public void set(B builder, Object value) {
        try {
            if (this.label.isOneOf()) {
                this.builderMethod.invoke(builder, value);
                return;
            }
            this.builderField.set(builder, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    /* access modifiers changed from: package-private */
    public Object get(M message) {
        try {
            return this.messageField.get(message);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    /* access modifiers changed from: package-private */
    public Object getFromBuilder(B builder) {
        try {
            return this.builderField.get(builder);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }
}
