package org.msgpack.value;

import java.util.Iterator;
import java.util.List;

public interface ImmutableArrayValue extends ArrayValue, ImmutableValue {
    Iterator<Value> iterator();

    List<Value> list();
}
