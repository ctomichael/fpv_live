package org.msgpack.core.buffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayBufferOutput implements MessageBufferOutput {
    private int bufferSize;
    private MessageBuffer lastBuffer;
    private List<MessageBuffer> list;

    public ArrayBufferOutput() {
        this(8192);
    }

    public ArrayBufferOutput(int i) {
        this.bufferSize = i;
        this.list = new ArrayList();
    }

    public int getSize() {
        int i = 0;
        Iterator<MessageBuffer> it2 = this.list.iterator();
        while (true) {
            int i2 = i;
            if (!it2.hasNext()) {
                return i2;
            }
            i = it2.next().size() + i2;
        }
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[getSize()];
        int i = 0;
        for (MessageBuffer messageBuffer : this.list) {
            messageBuffer.getBytes(0, bArr, i, messageBuffer.size());
            i = messageBuffer.size() + i;
        }
        return bArr;
    }

    public MessageBuffer toMessageBuffer() {
        if (this.list.size() == 1) {
            return this.list.get(0);
        }
        if (this.list.isEmpty()) {
            return MessageBuffer.allocate(0);
        }
        return MessageBuffer.wrap(toByteArray());
    }

    public List<MessageBuffer> toBufferList() {
        return new ArrayList(this.list);
    }

    public void clear() {
        this.list.clear();
    }

    public MessageBuffer next(int i) {
        if (this.lastBuffer != null && this.lastBuffer.size() > i) {
            return this.lastBuffer;
        }
        MessageBuffer allocate = MessageBuffer.allocate(Math.max(this.bufferSize, i));
        this.lastBuffer = allocate;
        return allocate;
    }

    public void writeBuffer(int i) {
        this.list.add(this.lastBuffer.slice(0, i));
        if (this.lastBuffer.size() - i > this.bufferSize / 4) {
            this.lastBuffer = this.lastBuffer.slice(i, this.lastBuffer.size() - i);
        } else {
            this.lastBuffer = null;
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        MessageBuffer allocate = MessageBuffer.allocate(i2);
        allocate.putBytes(0, bArr, i, i2);
        this.list.add(allocate);
    }

    public void add(byte[] bArr, int i, int i2) {
        this.list.add(MessageBuffer.wrap(bArr, i, i2));
    }

    public void close() {
    }

    public void flush() {
    }
}
