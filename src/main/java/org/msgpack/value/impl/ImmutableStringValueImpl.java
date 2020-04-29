package org.msgpack.value.impl;

import java.io.IOException;
import java.util.Arrays;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.ImmutableStringValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;

public class ImmutableStringValueImpl extends AbstractImmutableRawValue implements ImmutableStringValue {
    public ImmutableStringValueImpl(byte[] bArr) {
        super(bArr);
    }

    public ImmutableStringValueImpl(String str) {
        super(str);
    }

    public ValueType getValueType() {
        return ValueType.STRING;
    }

    public ImmutableStringValue immutableValue() {
        return this;
    }

    public ImmutableStringValue asStringValue() {
        return this;
    }

    public void writeTo(MessagePacker messagePacker) throws IOException {
        messagePacker.packRawStringHeader(this.data.length);
        messagePacker.writePayload(this.data);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Value)) {
            return false;
        }
        Value value = (Value) obj;
        if (!value.isStringValue()) {
            return false;
        }
        if (value instanceof ImmutableStringValueImpl) {
            return Arrays.equals(this.data, ((ImmutableStringValueImpl) value).data);
        }
        return Arrays.equals(this.data, value.asStringValue().asByteArray());
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
}
