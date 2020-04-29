package org.msgpack.core;

import org.msgpack.core.MessagePack;
import org.msgpack.core.annotations.VisibleForTesting;
import org.msgpack.value.ValueType;

public enum MessageFormat {
    POSFIXINT(ValueType.INTEGER),
    FIXMAP(ValueType.MAP),
    FIXARRAY(ValueType.ARRAY),
    FIXSTR(ValueType.STRING),
    NIL(ValueType.NIL),
    NEVER_USED(null),
    BOOLEAN(ValueType.BOOLEAN),
    BIN8(ValueType.BINARY),
    BIN16(ValueType.BINARY),
    BIN32(ValueType.BINARY),
    EXT8(ValueType.EXTENSION),
    EXT16(ValueType.EXTENSION),
    EXT32(ValueType.EXTENSION),
    FLOAT32(ValueType.FLOAT),
    FLOAT64(ValueType.FLOAT),
    UINT8(ValueType.INTEGER),
    UINT16(ValueType.INTEGER),
    UINT32(ValueType.INTEGER),
    UINT64(ValueType.INTEGER),
    INT8(ValueType.INTEGER),
    INT16(ValueType.INTEGER),
    INT32(ValueType.INTEGER),
    INT64(ValueType.INTEGER),
    FIXEXT1(ValueType.EXTENSION),
    FIXEXT2(ValueType.EXTENSION),
    FIXEXT4(ValueType.EXTENSION),
    FIXEXT8(ValueType.EXTENSION),
    FIXEXT16(ValueType.EXTENSION),
    STR8(ValueType.STRING),
    STR16(ValueType.STRING),
    STR32(ValueType.STRING),
    ARRAY16(ValueType.ARRAY),
    ARRAY32(ValueType.ARRAY),
    MAP16(ValueType.MAP),
    MAP32(ValueType.MAP),
    NEGFIXINT(ValueType.INTEGER);
    
    private static final MessageFormat[] formatTable = new MessageFormat[256];
    private final ValueType valueType;

    static {
        for (int i = 0; i <= 255; i++) {
            formatTable[i] = toMessageFormat((byte) i);
        }
    }

    private MessageFormat(ValueType valueType2) {
        this.valueType = valueType2;
    }

    public ValueType getValueType() throws MessageFormatException {
        if (this != NEVER_USED) {
            return this.valueType;
        }
        throw new MessageFormatException("Cannot convert NEVER_USED to ValueType");
    }

    public static MessageFormat valueOf(byte b) {
        return formatTable[b & 255];
    }

    @VisibleForTesting
    static MessageFormat toMessageFormat(byte b) {
        if (MessagePack.Code.isPosFixInt(b)) {
            return POSFIXINT;
        }
        if (MessagePack.Code.isNegFixInt(b)) {
            return NEGFIXINT;
        }
        if (MessagePack.Code.isFixStr(b)) {
            return FIXSTR;
        }
        if (MessagePack.Code.isFixedArray(b)) {
            return FIXARRAY;
        }
        if (MessagePack.Code.isFixedMap(b)) {
            return FIXMAP;
        }
        switch (b) {
            case -64:
                return NIL;
            case -63:
            default:
                return NEVER_USED;
            case -62:
            case -61:
                return BOOLEAN;
            case -60:
                return BIN8;
            case -59:
                return BIN16;
            case -58:
                return BIN32;
            case -57:
                return EXT8;
            case -56:
                return EXT16;
            case -55:
                return EXT32;
            case -54:
                return FLOAT32;
            case -53:
                return FLOAT64;
            case -52:
                return UINT8;
            case -51:
                return UINT16;
            case -50:
                return UINT32;
            case -49:
                return UINT64;
            case -48:
                return INT8;
            case -47:
                return INT16;
            case -46:
                return INT32;
            case -45:
                return INT64;
            case -44:
                return FIXEXT1;
            case -43:
                return FIXEXT2;
            case -42:
                return FIXEXT4;
            case -41:
                return FIXEXT8;
            case -40:
                return FIXEXT16;
            case -39:
                return STR8;
            case -38:
                return STR16;
            case -37:
                return STR32;
            case -36:
                return ARRAY16;
            case -35:
                return ARRAY32;
            case -34:
                return MAP16;
            case -33:
                return MAP32;
        }
    }
}
