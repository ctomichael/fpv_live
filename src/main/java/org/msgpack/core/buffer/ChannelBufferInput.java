package org.msgpack.core.buffer;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import org.msgpack.core.Preconditions;

public class ChannelBufferInput implements MessageBufferInput {
    private final MessageBuffer buffer;
    private ReadableByteChannel channel;

    public ChannelBufferInput(ReadableByteChannel readableByteChannel) {
        this(readableByteChannel, 8192);
    }

    public ChannelBufferInput(ReadableByteChannel readableByteChannel, int i) {
        this.channel = (ReadableByteChannel) Preconditions.checkNotNull(readableByteChannel, "input channel is null");
        Preconditions.checkArgument(i > 0, "buffer size must be > 0: " + i);
        this.buffer = MessageBuffer.allocate(i);
    }

    public ReadableByteChannel reset(ReadableByteChannel readableByteChannel) throws IOException {
        ReadableByteChannel readableByteChannel2 = this.channel;
        this.channel = readableByteChannel;
        return readableByteChannel2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x000c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.msgpack.core.buffer.MessageBuffer next() throws java.io.IOException {
        /*
            r3 = this;
            org.msgpack.core.buffer.MessageBuffer r0 = r3.buffer
            java.nio.ByteBuffer r0 = r0.sliceAsByteBuffer()
        L_0x0006:
            int r1 = r0.remaining()
            if (r1 <= 0) goto L_0x0015
            java.nio.channels.ReadableByteChannel r1 = r3.channel
            int r1 = r1.read(r0)
            r2 = -1
            if (r1 != r2) goto L_0x0006
        L_0x0015:
            r0.flip()
            int r1 = r0.remaining()
            if (r1 != 0) goto L_0x0020
            r0 = 0
        L_0x001f:
            return r0
        L_0x0020:
            org.msgpack.core.buffer.MessageBuffer r1 = r3.buffer
            r2 = 0
            int r0 = r0.limit()
            org.msgpack.core.buffer.MessageBuffer r0 = r1.slice(r2, r0)
            goto L_0x001f
        */
        throw new UnsupportedOperationException("Method not decompiled: org.msgpack.core.buffer.ChannelBufferInput.next():org.msgpack.core.buffer.MessageBuffer");
    }

    public void close() throws IOException {
        this.channel.close();
    }
}
