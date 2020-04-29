package org.msgpack.value.impl;

import com.adobe.xmp.XMPConst;
import dji.component.accountcenter.IMemberProtocol;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.ArrayValue;
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
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;

public class ImmutableArrayValueImpl extends AbstractImmutableValue implements ImmutableArrayValue {
    private static final ImmutableArrayValueImpl EMPTY = new ImmutableArrayValueImpl(new Value[0]);
    private final Value[] array;

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

    public /* bridge */ /* synthetic */ ImmutableIntegerValue asIntegerValue() {
        return super.asIntegerValue();
    }

    public /* bridge */ /* synthetic */ ImmutableMapValue asMapValue() {
        return super.asMapValue();
    }

    public /* bridge */ /* synthetic */ ImmutableNilValue asNilValue() {
        return super.asNilValue();
    }

    public /* bridge */ /* synthetic */ ImmutableNumberValue asNumberValue() {
        return super.asNumberValue();
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

    public static ImmutableArrayValue empty() {
        return EMPTY;
    }

    public ImmutableArrayValueImpl(Value[] valueArr) {
        this.array = valueArr;
    }

    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    public ImmutableArrayValue immutableValue() {
        return this;
    }

    public ImmutableArrayValue asArrayValue() {
        return this;
    }

    public int size() {
        return this.array.length;
    }

    public Value get(int i) {
        return this.array[i];
    }

    public Value getOrNilValue(int i) {
        if (i >= this.array.length || i < 0) {
            return ImmutableNilValueImpl.get();
        }
        return this.array[i];
    }

    public Iterator<Value> iterator() {
        return new Ite(this.array);
    }

    public List<Value> list() {
        return new ImmutableArrayValueList(this.array);
    }

    public void writeTo(MessagePacker messagePacker) throws IOException {
        messagePacker.packArrayHeader(this.array.length);
        for (int i = 0; i < this.array.length; i++) {
            this.array[i].writeTo(messagePacker);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Value)) {
            return false;
        }
        Value value = (Value) obj;
        if (value instanceof ImmutableArrayValueImpl) {
            return Arrays.equals(this.array, ((ImmutableArrayValueImpl) value).array);
        }
        if (!value.isArrayValue()) {
            return false;
        }
        ArrayValue asArrayValue = value.asArrayValue();
        if (size() != asArrayValue.size()) {
            return false;
        }
        Iterator<Value> it2 = asArrayValue.iterator();
        for (int i = 0; i < this.array.length; i++) {
            if (!it2.hasNext() || !this.array[i].equals(it2.next())) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.array.length; i2++) {
            i = (i * 31) + this.array[i2].hashCode();
        }
        return i;
    }

    public String toJson() {
        if (this.array.length == 0) {
            return XMPConst.ARRAY_ITEM_NAME;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(IMemberProtocol.STRING_SEPERATOR_LEFT);
        sb.append(this.array[0].toJson());
        for (int i = 1; i < this.array.length; i++) {
            sb.append(",");
            sb.append(this.array[i].toJson());
        }
        sb.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }

    public String toString() {
        if (this.array.length == 0) {
            return XMPConst.ARRAY_ITEM_NAME;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(IMemberProtocol.STRING_SEPERATOR_LEFT);
        appendString(sb, this.array[0]);
        for (int i = 1; i < this.array.length; i++) {
            sb.append(",");
            appendString(sb, this.array[i]);
        }
        sb.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }

    private static void appendString(StringBuilder sb, Value value) {
        if (value.isRawValue()) {
            sb.append(value.toJson());
        } else {
            sb.append(value.toString());
        }
    }

    private static class ImmutableArrayValueList extends AbstractList<Value> {
        private final Value[] array;

        public ImmutableArrayValueList(Value[] valueArr) {
            this.array = valueArr;
        }

        public Value get(int i) {
            return this.array[i];
        }

        public int size() {
            return this.array.length;
        }
    }

    private static class Ite implements Iterator<Value> {
        private final Value[] array;
        private int index = 0;

        public Ite(Value[] valueArr) {
            this.array = valueArr;
        }

        public boolean hasNext() {
            return this.index != this.array.length;
        }

        public Value next() {
            int i = this.index;
            if (i >= this.array.length) {
                throw new NoSuchElementException();
            }
            this.index = i + 1;
            return this.array[i];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
