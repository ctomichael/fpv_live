package org.msgpack.core;

public class MessageSizeException extends MessagePackException {
    private final long size;

    public MessageSizeException(long j) {
        this.size = j;
    }

    public MessageSizeException(String str, long j) {
        super(str);
        this.size = j;
    }

    public long getSize() {
        return this.size;
    }
}
