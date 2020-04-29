package org.msgpack.value;

import java.math.BigInteger;

public interface NumberValue extends Value {
    BigInteger toBigInteger();

    byte toByte();

    double toDouble();

    float toFloat();

    int toInt();

    long toLong();

    short toShort();
}
