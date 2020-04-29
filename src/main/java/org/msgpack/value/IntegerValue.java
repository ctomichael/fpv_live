package org.msgpack.value;

import java.math.BigInteger;
import org.msgpack.core.MessageFormat;

public interface IntegerValue extends NumberValue {
    BigInteger asBigInteger();

    byte asByte();

    int asInt();

    long asLong();

    short asShort();

    boolean isInByteRange();

    boolean isInIntRange();

    boolean isInLongRange();

    boolean isInShortRange();

    MessageFormat mostSuccinctMessageFormat();
}
