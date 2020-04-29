package org.msgpack.value;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.LongCompanionObject;
import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessageIntegerOverflowException;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageStringCodingException;
import org.msgpack.core.MessageTypeCastException;
import org.msgpack.value.impl.ImmutableBigIntegerValueImpl;

public class Variable implements Value {
    private static final long BYTE_MAX = 127;
    private static final long BYTE_MIN = -128;
    private static final long INT_MAX = 2147483647L;
    private static final long INT_MIN = -2147483648L;
    private static final BigInteger LONG_MAX = BigInteger.valueOf(LongCompanionObject.MAX_VALUE);
    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
    private static final long SHORT_MAX = 32767;
    private static final long SHORT_MIN = -32768;
    private AbstractValueAccessor accessor;
    private final ArrayValueAccessor arrayAccessor = new ArrayValueAccessor();
    private final BinaryValueAccessor binaryAccessor = new BinaryValueAccessor();
    private final BooleanValueAccessor booleanAccessor = new BooleanValueAccessor();
    /* access modifiers changed from: private */
    public double doubleValue;
    private final ExtensionValueAccessor extensionAccessor = new ExtensionValueAccessor();
    private final FloatValueAccessor floatAccessor = new FloatValueAccessor();
    private final IntegerValueAccessor integerAccessor = new IntegerValueAccessor();
    /* access modifiers changed from: private */
    public long longValue;
    private final MapValueAccessor mapAccessor = new MapValueAccessor();
    private final NilValueAccessor nilAccessor = new NilValueAccessor();
    /* access modifiers changed from: private */
    public Object objectValue;
    private final StringValueAccessor stringAccessor = new StringValueAccessor();
    /* access modifiers changed from: private */
    public Type type;

    private abstract class AbstractValueAccessor implements Value {
        private AbstractValueAccessor() {
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

        public NilValue asNilValue() {
            throw new MessageTypeCastException();
        }

        public BooleanValue asBooleanValue() {
            throw new MessageTypeCastException();
        }

        public NumberValue asNumberValue() {
            throw new MessageTypeCastException();
        }

        public IntegerValue asIntegerValue() {
            throw new MessageTypeCastException();
        }

        public FloatValue asFloatValue() {
            throw new MessageTypeCastException();
        }

        public RawValue asRawValue() {
            throw new MessageTypeCastException();
        }

        public BinaryValue asBinaryValue() {
            throw new MessageTypeCastException();
        }

        public StringValue asStringValue() {
            throw new MessageTypeCastException();
        }

        public ArrayValue asArrayValue() {
            throw new MessageTypeCastException();
        }

        public MapValue asMapValue() {
            throw new MessageTypeCastException();
        }

        public ExtensionValue asExtensionValue() {
            throw new MessageTypeCastException();
        }

        public boolean equals(Object obj) {
            return Variable.this.equals(obj);
        }

        public int hashCode() {
            return Variable.this.hashCode();
        }

        public String toJson() {
            return Variable.this.toJson();
        }

        public String toString() {
            return Variable.this.toString();
        }
    }

    public enum Type {
        NULL(ValueType.NIL),
        BOOLEAN(ValueType.BOOLEAN),
        LONG(ValueType.INTEGER),
        BIG_INTEGER(ValueType.INTEGER),
        DOUBLE(ValueType.FLOAT),
        BYTE_ARRAY(ValueType.BINARY),
        RAW_STRING(ValueType.STRING),
        LIST(ValueType.ARRAY),
        MAP(ValueType.MAP),
        EXTENSION(ValueType.EXTENSION);
        
        private final ValueType valueType;

        private Type(ValueType valueType2) {
            this.valueType = valueType2;
        }

        public ValueType getValueType() {
            return this.valueType;
        }
    }

    public Variable() {
        setNilValue();
    }

    public Variable setNilValue() {
        this.type = Type.NULL;
        this.accessor = this.nilAccessor;
        return this;
    }

    private class NilValueAccessor extends AbstractValueAccessor implements NilValue {
        private NilValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.NIL;
        }

        public NilValue asNilValue() {
            return this;
        }

        public ImmutableNilValue immutableValue() {
            return ValueFactory.newNil();
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            messagePacker.packNil();
        }
    }

    public Variable setBooleanValue(boolean z) {
        this.type = Type.BOOLEAN;
        this.accessor = this.booleanAccessor;
        this.longValue = z ? 1 : 0;
        return this;
    }

    private class BooleanValueAccessor extends AbstractValueAccessor implements BooleanValue {
        private BooleanValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.BOOLEAN;
        }

        public BooleanValue asBooleanValue() {
            return this;
        }

        public ImmutableBooleanValue immutableValue() {
            return ValueFactory.newBoolean(getBoolean());
        }

        public boolean getBoolean() {
            return Variable.this.longValue == 1;
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            messagePacker.packBoolean(Variable.this.longValue == 1);
        }
    }

    private abstract class AbstractNumberValueAccessor extends AbstractValueAccessor implements NumberValue {
        private AbstractNumberValueAccessor() {
            super();
        }

        public NumberValue asNumberValue() {
            return this;
        }

        public byte toByte() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return ((BigInteger) Variable.this.objectValue).byteValue();
            }
            return (byte) ((int) Variable.this.longValue);
        }

        public short toShort() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return ((BigInteger) Variable.this.objectValue).shortValue();
            }
            return (short) ((int) Variable.this.longValue);
        }

        public int toInt() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return ((BigInteger) Variable.this.objectValue).intValue();
            }
            return (int) Variable.this.longValue;
        }

        public long toLong() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return ((BigInteger) Variable.this.objectValue).longValue();
            }
            return Variable.this.longValue;
        }

        public BigInteger toBigInteger() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return (BigInteger) Variable.this.objectValue;
            }
            if (Variable.this.type == Type.DOUBLE) {
                return new BigDecimal(Variable.this.doubleValue).toBigInteger();
            }
            return BigInteger.valueOf(Variable.this.longValue);
        }

        public float toFloat() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return ((BigInteger) Variable.this.objectValue).floatValue();
            }
            if (Variable.this.type == Type.DOUBLE) {
                return (float) Variable.this.doubleValue;
            }
            return (float) Variable.this.longValue;
        }

        public double toDouble() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return ((BigInteger) Variable.this.objectValue).doubleValue();
            }
            if (Variable.this.type == Type.DOUBLE) {
                return Variable.this.doubleValue;
            }
            return (double) Variable.this.longValue;
        }
    }

    public Variable setIntegerValue(long j) {
        this.type = Type.LONG;
        this.accessor = this.integerAccessor;
        this.longValue = j;
        return this;
    }

    public Variable setIntegerValue(BigInteger bigInteger) {
        if (bigInteger.compareTo(LONG_MIN) < 0 || bigInteger.compareTo(LONG_MAX) > 0) {
            this.type = Type.BIG_INTEGER;
            this.accessor = this.integerAccessor;
            this.objectValue = bigInteger;
        } else {
            this.type = Type.LONG;
            this.accessor = this.integerAccessor;
            this.longValue = bigInteger.longValue();
        }
        return this;
    }

    private class IntegerValueAccessor extends AbstractNumberValueAccessor implements IntegerValue {
        private IntegerValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.INTEGER;
        }

        public IntegerValue asIntegerValue() {
            return this;
        }

        public ImmutableIntegerValue immutableValue() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return ValueFactory.newInteger((BigInteger) Variable.this.objectValue);
            }
            return ValueFactory.newInteger(Variable.this.longValue);
        }

        public boolean isInByteRange() {
            if (Variable.this.type != Type.BIG_INTEGER && Variable.BYTE_MIN <= Variable.this.longValue && Variable.this.longValue <= Variable.BYTE_MAX) {
                return true;
            }
            return false;
        }

        public boolean isInShortRange() {
            if (Variable.this.type != Type.BIG_INTEGER && Variable.SHORT_MIN <= Variable.this.longValue && Variable.this.longValue <= Variable.SHORT_MAX) {
                return true;
            }
            return false;
        }

        public boolean isInIntRange() {
            if (Variable.this.type != Type.BIG_INTEGER && Variable.INT_MIN <= Variable.this.longValue && Variable.this.longValue <= Variable.INT_MAX) {
                return true;
            }
            return false;
        }

        public boolean isInLongRange() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return false;
            }
            return true;
        }

        public MessageFormat mostSuccinctMessageFormat() {
            return ImmutableBigIntegerValueImpl.mostSuccinctMessageFormat(this);
        }

        public byte asByte() {
            if (isInByteRange()) {
                return (byte) ((int) Variable.this.longValue);
            }
            throw new MessageIntegerOverflowException(Variable.this.longValue);
        }

        public short asShort() {
            if (isInByteRange()) {
                return (short) ((int) Variable.this.longValue);
            }
            throw new MessageIntegerOverflowException(Variable.this.longValue);
        }

        public int asInt() {
            if (isInIntRange()) {
                return (int) Variable.this.longValue;
            }
            throw new MessageIntegerOverflowException(Variable.this.longValue);
        }

        public long asLong() {
            if (isInLongRange()) {
                return Variable.this.longValue;
            }
            throw new MessageIntegerOverflowException(Variable.this.longValue);
        }

        public BigInteger asBigInteger() {
            if (Variable.this.type == Type.BIG_INTEGER) {
                return (BigInteger) Variable.this.objectValue;
            }
            return BigInteger.valueOf(Variable.this.longValue);
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            if (Variable.this.type == Type.BIG_INTEGER) {
                messagePacker.packBigInteger((BigInteger) Variable.this.objectValue);
            } else {
                messagePacker.packLong(Variable.this.longValue);
            }
        }
    }

    public Variable setFloatValue(double d) {
        this.type = Type.DOUBLE;
        this.accessor = this.floatAccessor;
        this.doubleValue = d;
        this.longValue = (long) d;
        return this;
    }

    public Variable setFloatValue(float f) {
        this.type = Type.DOUBLE;
        this.accessor = this.floatAccessor;
        this.longValue = (long) f;
        return this;
    }

    private class FloatValueAccessor extends AbstractNumberValueAccessor implements FloatValue {
        private FloatValueAccessor() {
            super();
        }

        public FloatValue asFloatValue() {
            return this;
        }

        public ImmutableFloatValue immutableValue() {
            return ValueFactory.newFloat(Variable.this.doubleValue);
        }

        public ValueType getValueType() {
            return ValueType.FLOAT;
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            messagePacker.packDouble(Variable.this.doubleValue);
        }
    }

    private abstract class AbstractRawValueAccessor extends AbstractValueAccessor implements RawValue {
        private AbstractRawValueAccessor() {
            super();
        }

        public RawValue asRawValue() {
            return this;
        }

        public byte[] asByteArray() {
            return (byte[]) Variable.this.objectValue;
        }

        public ByteBuffer asByteBuffer() {
            return ByteBuffer.wrap(asByteArray());
        }

        public String asString() {
            try {
                return MessagePack.UTF8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT).decode(ByteBuffer.wrap((byte[]) Variable.this.objectValue)).toString();
            } catch (CharacterCodingException e) {
                throw new MessageStringCodingException(e);
            }
        }

        public String toString() {
            try {
                return MessagePack.UTF8.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(ByteBuffer.wrap((byte[]) Variable.this.objectValue)).toString();
            } catch (CharacterCodingException e) {
                throw new MessageStringCodingException(e);
            }
        }
    }

    public Variable setBinaryValue(byte[] bArr) {
        this.type = Type.BYTE_ARRAY;
        this.accessor = this.binaryAccessor;
        this.objectValue = bArr;
        return this;
    }

    private class BinaryValueAccessor extends AbstractRawValueAccessor implements BinaryValue {
        private BinaryValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.BINARY;
        }

        public BinaryValue asBinaryValue() {
            return this;
        }

        public ImmutableBinaryValue immutableValue() {
            return ValueFactory.newBinary(asByteArray());
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            byte[] bArr = (byte[]) Variable.this.objectValue;
            messagePacker.packBinaryHeader(bArr.length);
            messagePacker.writePayload(bArr);
        }
    }

    public Variable setStringValue(String str) {
        return setStringValue(str.getBytes(MessagePack.UTF8));
    }

    public Variable setStringValue(byte[] bArr) {
        this.type = Type.RAW_STRING;
        this.accessor = this.stringAccessor;
        this.objectValue = bArr;
        return this;
    }

    private class StringValueAccessor extends AbstractRawValueAccessor implements StringValue {
        private StringValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.STRING;
        }

        public StringValue asStringValue() {
            return this;
        }

        public ImmutableStringValue immutableValue() {
            return ValueFactory.newString((byte[]) Variable.this.objectValue);
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            byte[] bArr = (byte[]) Variable.this.objectValue;
            messagePacker.packRawStringHeader(bArr.length);
            messagePacker.writePayload(bArr);
        }
    }

    public Variable setArrayValue(List<Value> list) {
        this.type = Type.LIST;
        this.accessor = this.arrayAccessor;
        this.objectValue = list;
        return this;
    }

    private class ArrayValueAccessor extends AbstractValueAccessor implements ArrayValue {
        private ArrayValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.ARRAY;
        }

        public ArrayValue asArrayValue() {
            return this;
        }

        public ImmutableArrayValue immutableValue() {
            return ValueFactory.newArray(list());
        }

        public int size() {
            return list().size();
        }

        public Value get(int i) {
            return list().get(i);
        }

        public Value getOrNilValue(int i) {
            List<Value> list = list();
            if (list.size() >= i || i < 0) {
                return list.get(i);
            }
            return ValueFactory.newNil();
        }

        public Iterator<Value> iterator() {
            return list().iterator();
        }

        public List<Value> list() {
            return (List) Variable.this.objectValue;
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            List<Value> list = list();
            messagePacker.packArrayHeader(list.size());
            for (Value value : list) {
                value.writeTo(messagePacker);
            }
        }
    }

    public Variable setMapValue(Map<Value, Value> map) {
        this.type = Type.MAP;
        this.accessor = this.mapAccessor;
        this.objectValue = map;
        return this;
    }

    private class MapValueAccessor extends AbstractValueAccessor implements MapValue {
        private MapValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.MAP;
        }

        public MapValue asMapValue() {
            return this;
        }

        public ImmutableMapValue immutableValue() {
            return ValueFactory.newMap(map());
        }

        public int size() {
            return map().size();
        }

        public Set<Value> keySet() {
            return map().keySet();
        }

        public Set<Map.Entry<Value, Value>> entrySet() {
            return map().entrySet();
        }

        public Collection<Value> values() {
            return map().values();
        }

        public Value[] getKeyValueArray() {
            Map<Value, Value> map = map();
            Value[] valueArr = new Value[(map.size() * 2)];
            Iterator<Map.Entry<Value, Value>> it2 = map.entrySet().iterator();
            int i = 0;
            while (true) {
                int i2 = i;
                if (!it2.hasNext()) {
                    return valueArr;
                }
                Map.Entry next = it2.next();
                valueArr[i2] = (Value) next.getKey();
                int i3 = i2 + 1;
                valueArr[i3] = (Value) next.getValue();
                i = i3 + 1;
            }
        }

        public Map<Value, Value> map() {
            return (Map) Variable.this.objectValue;
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            Map<Value, Value> map = map();
            messagePacker.packArrayHeader(map.size());
            for (Map.Entry entry : map.entrySet()) {
                ((Value) entry.getKey()).writeTo(messagePacker);
                ((Value) entry.getValue()).writeTo(messagePacker);
            }
        }
    }

    public Variable setExtensionValue(byte b, byte[] bArr) {
        this.type = Type.EXTENSION;
        this.accessor = this.extensionAccessor;
        this.objectValue = ValueFactory.newExtension(b, bArr);
        return this;
    }

    private class ExtensionValueAccessor extends AbstractValueAccessor implements ExtensionValue {
        private ExtensionValueAccessor() {
            super();
        }

        public ValueType getValueType() {
            return ValueType.EXTENSION;
        }

        public ExtensionValue asExtensionValue() {
            return this;
        }

        public ImmutableExtensionValue immutableValue() {
            return (ImmutableExtensionValue) Variable.this.objectValue;
        }

        public byte getType() {
            return ((ImmutableExtensionValue) Variable.this.objectValue).getType();
        }

        public byte[] getData() {
            return ((ImmutableExtensionValue) Variable.this.objectValue).getData();
        }

        public void writeTo(MessagePacker messagePacker) throws IOException {
            ((ImmutableExtensionValue) Variable.this.objectValue).writeTo(messagePacker);
        }
    }

    public ImmutableValue immutableValue() {
        return this.accessor.immutableValue();
    }

    public void writeTo(MessagePacker messagePacker) throws IOException {
        this.accessor.writeTo(messagePacker);
    }

    public int hashCode() {
        return immutableValue().hashCode();
    }

    public boolean equals(Object obj) {
        return immutableValue().equals(obj);
    }

    public String toJson() {
        return immutableValue().toJson();
    }

    public String toString() {
        return immutableValue().toString();
    }

    public ValueType getValueType() {
        return this.type.getValueType();
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

    public NilValue asNilValue() {
        if (isNilValue()) {
            return (NilValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public BooleanValue asBooleanValue() {
        if (isBooleanValue()) {
            return (BooleanValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public NumberValue asNumberValue() {
        if (isNumberValue()) {
            return (NumberValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public IntegerValue asIntegerValue() {
        if (isIntegerValue()) {
            return (IntegerValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public FloatValue asFloatValue() {
        if (isFloatValue()) {
            return (FloatValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public RawValue asRawValue() {
        if (isRawValue()) {
            return (RawValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public BinaryValue asBinaryValue() {
        if (isBinaryValue()) {
            return (BinaryValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public StringValue asStringValue() {
        if (isStringValue()) {
            return (StringValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public ArrayValue asArrayValue() {
        if (isArrayValue()) {
            return (ArrayValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public MapValue asMapValue() {
        if (isMapValue()) {
            return (MapValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }

    public ExtensionValue asExtensionValue() {
        if (isExtensionValue()) {
            return (ExtensionValue) this.accessor;
        }
        throw new MessageTypeCastException();
    }
}
