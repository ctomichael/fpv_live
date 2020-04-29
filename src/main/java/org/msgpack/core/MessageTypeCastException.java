package org.msgpack.core;

public class MessageTypeCastException extends MessageTypeException {
    public MessageTypeCastException() {
    }

    public MessageTypeCastException(String str) {
        super(str);
    }

    public MessageTypeCastException(String str, Throwable th) {
        super(str, th);
    }

    public MessageTypeCastException(Throwable th) {
        super(th);
    }
}
