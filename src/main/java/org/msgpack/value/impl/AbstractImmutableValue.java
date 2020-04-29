package org.msgpack.value.impl;

import org.msgpack.core.MessageTypeCastException;
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
import org.msgpack.value.ImmutableValue;

abstract class AbstractImmutableValue implements ImmutableValue {
    AbstractImmutableValue() {
    }

    public boolean isNilValue() {
        return getValueType().isNilType();
    }

    public boolean isBooleanValue() {
        return getValueType().isBooleanType();
    }

    public boolean isNumberValue() {
        return getValueType().isNumberType();
    }

    public boolean isIntegerValue() {
        return getValueType().isIntegerType();
    }

    public boolean isFloatValue() {
        return getValueType().isFloatType();
    }

    public boolean isRawValue() {
        return getValueType().isRawType();
    }

    public boolean isBinaryValue() {
        return getValueType().isBinaryType();
    }

    public boolean isStringValue() {
        return getValueType().isStringType();
    }

    public boolean isArrayValue() {
        return getValueType().isArrayType();
    }

    public boolean isMapValue() {
        return getValueType().isMapType();
    }

    public boolean isExtensionValue() {
        return getValueType().isExtensionType();
    }

    public ImmutableNilValue asNilValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableBooleanValue asBooleanValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableNumberValue asNumberValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableIntegerValue asIntegerValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableFloatValue asFloatValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableRawValue asRawValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableBinaryValue asBinaryValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableStringValue asStringValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableArrayValue asArrayValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableMapValue asMapValue() {
        throw new MessageTypeCastException();
    }

    public ImmutableExtensionValue asExtensionValue() {
        throw new MessageTypeCastException();
    }
}
