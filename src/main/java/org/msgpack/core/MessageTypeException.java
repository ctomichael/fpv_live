package org.msgpack.core;

public class MessageTypeException extends MessagePackException {
    public MessageTypeException() {
    }

    public MessageTypeException(String str) {
        super(str);
    }

    public MessageTypeException(String str, Throwable th) {
        super(str, th);
    }

    public MessageTypeException(Throwable th) {
        super(th);
    }
}
