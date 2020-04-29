package org.msgpack.value.impl;

import java.io.IOException;
import java.util.Arrays;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.ImmutableBinaryValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;

public class ImmutableBinaryValueImpl extends AbstractImmutableRawValue implements ImmutableBinaryValue {
    public ImmutableBinaryValueImpl(byte[] bArr) {
        super(bArr);
    }

    public ValueType getValueType() {
        return ValueType.BINARY;
    }

    public ImmutableBinaryValue immutableValue() {
        return this;
    }

    public ImmutableBinaryValue asBinaryValue() {
        return this;
    }

    public void writeTo(MessagePacker messagePacker) throws IOException {
        messagePacker.packBinaryHeader(this.data.length);
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
        if (!value.isBinaryValue()) {
            return false;
        }
        if (value instanceof ImmutableBinaryValueImpl) {
            return Arrays.equals(this.data, ((ImmutableBinaryValueImpl) value).data);
        }
        return Arrays.equals(this.data, value.asBinaryValue().asByteArray());
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
}
