package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;

public enum NullValue implements ProtocolMessageEnum {
    NULL_VALUE(0),
    UNRECOGNIZED(-1);
    
    public static final int NULL_VALUE_VALUE = 0;
    private static final NullValue[] VALUES = values();
    private static final Internal.EnumLiteMap<NullValue> internalValueMap = new Internal.EnumLiteMap<NullValue>() {
        /* class com.google.protobuf.NullValue.AnonymousClass1 */

        public NullValue findValueByNumber(int number) {
            return NullValue.forNumber(number);
        }
    };
    private final int value;

    public final int getNumber() {
        if (this != UNRECOGNIZED) {
            return this.value;
        }
        throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
    }

    @Deprecated
    public static NullValue valueOf(int value2) {
        return forNumber(value2);
    }

    public static NullValue forNumber(int value2) {
        switch (value2) {
            case 0:
                return NULL_VALUE;
            default:
                return null;
        }
    }

    public static Internal.EnumLiteMap<NullValue> internalGetValueMap() {
        return internalValueMap;
    }

    public final Descriptors.EnumValueDescriptor getValueDescriptor() {
        return getDescriptor().getValues().get(ordinal());
    }

    public final Descriptors.EnumDescriptor getDescriptorForType() {
        return getDescriptor();
    }

    public static final Descriptors.EnumDescriptor getDescriptor() {
        return StructProto.getDescriptor().getEnumTypes().get(0);
    }

    public static NullValue valueOf(Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
            throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
        } else if (desc.getIndex() == -1) {
            return UNRECOGNIZED;
        } else {
            return VALUES[desc.getIndex()];
        }
    }

    private NullValue(int value2) {
        this.value = value2;
    }
}
