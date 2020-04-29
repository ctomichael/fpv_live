package org.msgpack.value.impl;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
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
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;

public class ImmutableMapValueImpl extends AbstractImmutableValue implements ImmutableMapValue {
    private static final ImmutableMapValueImpl EMPTY = new ImmutableMapValueImpl(new Value[0]);
    private final Value[] kvs;

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

    public /* bridge */ /* synthetic */ ImmutableIntegerValue asIntegerValue() {
        return super.asIntegerValue();
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

    public static ImmutableMapValue empty() {
        return EMPTY;
    }

    public ImmutableMapValueImpl(Value[] valueArr) {
        this.kvs = valueArr;
    }

    public ValueType getValueType() {
        return ValueType.MAP;
    }

    public ImmutableMapValue immutableValue() {
        return this;
    }

    public ImmutableMapValue asMapValue() {
        return this;
    }

    public Value[] getKeyValueArray() {
        return (Value[]) Arrays.copyOf(this.kvs, this.kvs.length);
    }

    public int size() {
        return this.kvs.length / 2;
    }

    public Set<Value> keySet() {
        return new KeySet(this.kvs);
    }

    public Set<Map.Entry<Value, Value>> entrySet() {
        return new EntrySet(this.kvs);
    }

    public Collection<Value> values() {
        return new ValueCollection(this.kvs);
    }

    public Map<Value, Value> map() {
        return new ImmutableMapValueMap(this.kvs);
    }

    public void writeTo(MessagePacker messagePacker) throws IOException {
        messagePacker.packMapHeader(this.kvs.length / 2);
        for (int i = 0; i < this.kvs.length; i++) {
            this.kvs[i].writeTo(messagePacker);
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
        if (!value.isMapValue()) {
            return false;
        }
        return map().equals(value.asMapValue().map());
    }

    public int hashCode() {
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i;
            if (i2 >= this.kvs.length) {
                return i3;
            }
            i = (this.kvs[i2].hashCode() ^ this.kvs[i2 + 1].hashCode()) + i3;
            i2 += 2;
        }
    }

    public String toJson() {
        if (this.kvs.length == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendJsonKey(sb, this.kvs[0]);
        sb.append(":");
        sb.append(this.kvs[1].toJson());
        for (int i = 2; i < this.kvs.length; i += 2) {
            sb.append(",");
            appendJsonKey(sb, this.kvs[i]);
            sb.append(":");
            sb.append(this.kvs[i + 1].toJson());
        }
        sb.append("}");
        return sb.toString();
    }

    private static void appendJsonKey(StringBuilder sb, Value value) {
        if (value.isRawValue()) {
            sb.append(value.toJson());
        } else {
            ImmutableStringValueImpl.appendJsonString(sb, value.toString());
        }
    }

    public String toString() {
        if (this.kvs.length == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendString(sb, this.kvs[0]);
        sb.append(":");
        appendString(sb, this.kvs[1]);
        for (int i = 2; i < this.kvs.length; i += 2) {
            sb.append(",");
            appendString(sb, this.kvs[i]);
            sb.append(":");
            appendString(sb, this.kvs[i + 1]);
        }
        sb.append("}");
        return sb.toString();
    }

    private static void appendString(StringBuilder sb, Value value) {
        if (value.isRawValue()) {
            sb.append(value.toJson());
        } else {
            sb.append(value.toString());
        }
    }

    private static class ImmutableMapValueMap extends AbstractMap<Value, Value> {
        private final Value[] kvs;

        public ImmutableMapValueMap(Value[] valueArr) {
            this.kvs = valueArr;
        }

        public Set<Map.Entry<Value, Value>> entrySet() {
            return new EntrySet(this.kvs);
        }
    }

    private static class EntrySet extends AbstractSet<Map.Entry<Value, Value>> {
        private final Value[] kvs;

        EntrySet(Value[] valueArr) {
            this.kvs = valueArr;
        }

        public int size() {
            return this.kvs.length / 2;
        }

        public Iterator<Map.Entry<Value, Value>> iterator() {
            return new EntrySetIterator(this.kvs);
        }
    }

    private static class EntrySetIterator implements Iterator<Map.Entry<Value, Value>> {
        private int index = 0;
        private final Value[] kvs;

        EntrySetIterator(Value[] valueArr) {
            this.kvs = valueArr;
        }

        public boolean hasNext() {
            return this.index < this.kvs.length;
        }

        public Map.Entry<Value, Value> next() {
            if (this.index >= this.kvs.length) {
                throw new NoSuchElementException();
            }
            AbstractMap.SimpleImmutableEntry simpleImmutableEntry = new AbstractMap.SimpleImmutableEntry(this.kvs[this.index], this.kvs[this.index + 1]);
            this.index += 2;
            return simpleImmutableEntry;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class KeySet extends AbstractSet<Value> {
        private Value[] kvs;

        KeySet(Value[] valueArr) {
            this.kvs = valueArr;
        }

        public int size() {
            return this.kvs.length / 2;
        }

        public Iterator<Value> iterator() {
            return new EntryIterator(this.kvs, 0);
        }
    }

    private static class ValueCollection extends AbstractCollection<Value> {
        private Value[] kvs;

        ValueCollection(Value[] valueArr) {
            this.kvs = valueArr;
        }

        public int size() {
            return this.kvs.length / 2;
        }

        public Iterator<Value> iterator() {
            return new EntryIterator(this.kvs, 1);
        }
    }

    private static class EntryIterator implements Iterator<Value> {
        private int index;
        private Value[] kvs;

        public EntryIterator(Value[] valueArr, int i) {
            this.kvs = valueArr;
            this.index = i;
        }

        public boolean hasNext() {
            return this.index < this.kvs.length;
        }

        public Value next() {
            int i = this.index;
            if (i >= this.kvs.length) {
                throw new NoSuchElementException();
            }
            this.index = i + 2;
            return this.kvs[i];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
