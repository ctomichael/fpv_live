package org.msgpack.core;

public class MessageNeverUsedFormatException extends MessageFormatException {
    public MessageNeverUsedFormatException(Throwable th) {
        super(th);
    }

    public MessageNeverUsedFormatException(String str) {
        super(str);
    }

    public MessageNeverUsedFormatException(String str, Throwable th) {
        super(str, th);
    }
}
