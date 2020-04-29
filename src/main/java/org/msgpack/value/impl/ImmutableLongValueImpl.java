package org.msgpack.value.impl;

import java.io.IOException;
import java.math.BigInteger;
import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessageIntegerOverflowException;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.ImmutableArrayValue;
import org.msgpack.value.ImmutableBinaryValue;
import org.msgpack.value.ImmutableBooleanValue;
import org.msgpack.value.ImmutableExtensionValue;
import org.msgpack.value.ImmutableFloatValue;
import org.msgpack.value.ImmutableIntegerValue;
import org.msgpack.value.ImmutableMapValue;
import org.msgpack.value.ImmutableNilValue;
import org.msgpack.value.ImmutableNumberValue;
import org.msgpack.value.ImmutableRawValue;
import org.msgpack.value.ImmutableStringValue;
import org.msgpack.value.IntegerValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;

public class ImmutableLongValueImpl extends AbstractImmutableValue implements ImmutableIntegerValue {
    private static final long BYTE_MAX = 127;
    private static final long BYTE_MIN = -128;
    private static final long INT_MAX = 2147483647L;
    private static final long INT_MIN = -2147483648L;
    private static final long SHORT_MAX = 32767;
    private static final long SHORT_MIN = -32768;
    private final long value;

    public /* bridge */ /* synthetic */ ImmutableArrayValue asArrayValue() {
        return super.asArrayValue();
    }

    public /* bridge */ /* synthetic */ ImmutableBinaryValue asBinaryValue() {
        return super.asBinaryValue();
    }

    public /* bridge */ /* synthetic */ ImmutableBooleanValue asBooleanValue() {
        return super.asBooleanValue();
    }

    public /* bridge */ /* synthetic */ ImmutableExtensionValue asExtensionValue() {
        return super.asExtensionValue();
    }

    public /* bridge */ /* synthetic */ ImmutableFloatValue asFloatValue() {
        return super.asFloatValue();
    }

    public /* bridge */ /* synthetic */ ImmutableMapValue asMapValue() {
        return super.asMapValue();
    }

    public /* bridge */ /* synthetic */ ImmutableNilValue asNilValue() {
        return super.asNilValue();
    }

    public /* bridge */ /* synthetic */ ImmutableRawValue asRawValue() {
        return super.asRawValue();
    }

    public /* bridge */ /* synthetic */ ImmutableStringValue asStringValue() {
        return super.asStringValue();
    }

    public /* bridge */ /* synthetic */ boolean isArrayValue() {
        return super.isArrayValue();
    }

    public /* bridge */ /* synthetic */ boolean isBinaryValue() {
        return super.isBinaryValue();
    }

    public /* bridge */ /* synthetic */ boolean isBooleanValue() {
        return super.isBooleanValue();
    }

    public /* bridge */ /* synthetic */ boolean isExtensionValue() {
        return super.isExtensionValue();
    }

    public /* bridge */ /* synthetic */ boolean isFloatValue() {
        return super.isFloatValue();
    }

    public /* bridge */ /* synthetic */ boolean isIntegerValue() {
        return super.isIntegerValue();
    }

    public /* bridge */ /* synthetic */ boolean isMapValue() {
        return super.isMapValue();
    }

    public /* bridge */ /* synthetic */ boolean isNilValue() {
        return super.isNilValue();
    }

    public /* bridge */ /* synthetic */ boolean isNumberValue() {
        return super.isNumberValue();
    }

    public /* bridge */ /* synthetic */ boolean isRawValue() {
        return super.isRawValue();
    }

    public /* bridge */ /* synthetic */ boolean isStringValue() {
        return super.isStringValue();
    }

    public ImmutableLongValueImpl(long j) {
        this.value = j;
    }

    public ValueType getValueType() {
        return ValueType.INTEGER;
    }

    public ImmutableIntegerValue immutableValue() {
        return this;
    }

    public ImmutableNumberValue asNumberValue() {
        return this;
    }

    public ImmutableIntegerValue asIntegerValue() {
        return this;
    }

    public byte toByte() {
        return (byte) ((int) this.value);
    }

    public short toShort() {
        return (short) ((int) this.value);
    }

    public int toInt() {
        return (int) this.value;
    }

    public long toLong() {
        return this.value;
    }

    public BigInteger toBigInteger() {
        return BigInteger.valueOf(this.value);
    }

    public float toFloat() {
        return (float) this.value;
    }

    public double toDouble() {
        return (double) this.value;
    }

    public boolean isInByteRange() {
        return BYTE_MIN <= this.value && this.value <= BYTE_MAX;
    }

    public boolean isInShortRange() {
        return SHORT_MIN <= this.value && this.value <= SHORT_MAX;
    }

    public boolean isInIntRange() {
        return INT_MIN <= this.value && this.value <= INT_MAX;
    }

    public boolean isInLongRange() {
        return true;
    }

    public MessageFormat mostSuccinctMessageFormat() {
        return ImmutableBigIntegerValueImpl.mostSuccinctMessageFormat(this);
    }

    public byte asByte() {
        if (isInByteRange()) {
            return (byte) ((int) this.value);
        }
        throw new MessageIntegerOverflowException(this.value);
    }

    public short asShort() {
        if (isInShortRange()) {
            return (short) ((int) this.value);
        }
        throw new MessageIntegerOverflowException(this.value);
    }

    public int asInt() {
        if (isInIntRange()) {
            return (int) this.value;
        }
        throw new MessageIntegerOverflowException(this.value);
    }

    public long asLong() {
        return this.value;
    }

    public BigInteger asBigInteger() {
        return BigInteger.valueOf(this.value);
    }

    public void writeTo(MessagePacker messagePacker) throws IOException {
        messagePacker.packLong(this.value);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Value)) {
            return false;
        }
        Value value2 = (Value) obj;
        if (!value2.isIntegerValue()) {
            return false;
        }
        IntegerValue asIntegerValue = value2.asIntegerValue();
        if (!asIntegerValue.isInLongRange()) {
            return false;
        }
        if (this.value != asIntegerValue.toLong()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (INT_MIN > this.value || this.value > INT_MAX) {
            return (int) (this.value ^ (this.value >>> 32));
        }
        return (int) this.value;
    }

    public String toJson() {
        return Long.toString(this.value);
    }

    public String toString() {
        return toJson();
    }
}
