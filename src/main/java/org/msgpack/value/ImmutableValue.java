package org.msgpack.value;

public interface ImmutableValue extends Value {
    ImmutableArrayValue asArrayValue();

    ImmutableBinaryValue asBinaryValue();

    ImmutableBooleanValue asBooleanValue();

    ImmutableFloatValue asFloatValue();

    ImmutableIntegerValue asIntegerValue();

    ImmutableMapValue asMapValue();

    ImmutableNilValue asNilValue();

    ImmutableRawValue asRawValue();

    ImmutableStringValue asStringValue();
}
