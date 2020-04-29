package org.msgpack.core;

import java.math.BigInteger;

public class MessageIntegerOverflowException extends MessageTypeException {
    private final BigInteger bigInteger;

    public MessageIntegerOverflowException(BigInteger bigInteger2) {
        this.bigInteger = bigInteger2;
    }

    public MessageIntegerOverflowException(long j) {
        this(BigInteger.valueOf(j));
    }

    public MessageIntegerOverflowException(String str, BigInteger bigInteger2) {
        super(str);
        this.bigInteger = bigInteger2;
    }

    public BigInteger getBigInteger() {
        return this.bigInteger;
    }

    public String getMessage() {
        return this.bigInteger.toString();
    }
}
