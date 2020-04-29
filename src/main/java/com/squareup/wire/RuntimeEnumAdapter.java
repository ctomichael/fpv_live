package com.squareup.wire;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class RuntimeEnumAdapter<E extends WireEnum> extends ProtoAdapter<E> {
    private Method fromValueMethod;
    private final Class<E> type;

    RuntimeEnumAdapter(Class<E> type2) {
        super(FieldEncoding.VARINT, type2);
        this.type = type2;
    }

    private Method getFromValueMethod() {
        Method method = this.fromValueMethod;
        if (method != null) {
            return method;
        }
        try {
            Method method2 = this.type.getMethod("fromValue", Integer.TYPE);
            this.fromValueMethod = method2;
            return method2;
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    public int encodedSize(E value) {
        return ProtoWriter.varint32Size(value.getValue());
    }

    public void encode(ProtoWriter writer, E value) throws IOException {
        writer.writeVarint32(value.getValue());
    }

    public E decode(ProtoReader reader) throws IOException {
        int value = reader.readVarint32();
        try {
            E constant = (WireEnum) getFromValueMethod().invoke(null, Integer.valueOf(value));
            if (constant != null) {
                return constant;
            }
            throw new ProtoAdapter.EnumConstantNotFoundException(value, this.type);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    public boolean equals(Object o) {
        return (o instanceof RuntimeEnumAdapter) && ((RuntimeEnumAdapter) o).type == this.type;
    }

    public int hashCode() {
        return this.type.hashCode();
    }
}
