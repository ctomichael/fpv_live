package org.msgpack.value;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MapValue extends Value {
    Set<Map.Entry<Value, Value>> entrySet();

    Value[] getKeyValueArray();

    Set<Value> keySet();

    Map<Value, Value> map();

    int size();

    Collection<Value> values();
}
