package org.msgpack.value;

import java.util.Iterator;
import java.util.List;

public interface ArrayValue extends Value, Iterable<Value> {
    Value get(int i);

    Value getOrNilValue(int i);

    Iterator<Value> iterator();

    List<Value> list();

    int size();
}
