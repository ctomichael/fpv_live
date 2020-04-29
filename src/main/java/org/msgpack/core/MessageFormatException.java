package org.msgpack.core;

public class MessageFormatException extends MessagePackException {
    public MessageFormatException(Throwable th) {
        super(th);
    }

    public MessageFormatException(String str) {
        super(str);
    }

    public MessageFormatException(String str, Throwable th) {
        super(str, th);
    }
}
