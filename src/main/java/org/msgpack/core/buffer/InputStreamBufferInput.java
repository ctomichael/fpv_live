package org.msgpack.core.buffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import org.msgpack.core.Preconditions;

public class InputStreamBufferInput implements MessageBufferInput {
    private final byte[] buffer;

    /* renamed from: in  reason: collision with root package name */
    private InputStream f28in;

    public static MessageBufferInput newBufferInput(InputStream inputStream) {
        FileChannel channel;
        Preconditions.checkNotNull(inputStream, "InputStream is null");
        if (!(inputStream instanceof FileInputStream) || (channel = ((FileInputStream) inputStream).getChannel()) == null) {
            return new InputStreamBufferInput(inputStream);
        }
        return new ChannelBufferInput(channel);
    }

    public InputStreamBufferInput(InputStream inputStream) {
        this(inputStream, 8192);
    }

    public InputStreamBufferInput(InputStream inputStream, int i) {
        this.f28in = (InputStream) Preconditions.checkNotNull(inputStream, "input is null");
        this.buffer = new byte[i];
    }

    public InputStream reset(InputStream inputStream) throws IOException {
        InputStream inputStream2 = this.f28in;
        this.f28in = inputStream;
        return inputStream2;
    }

    public MessageBuffer next() throws IOException {
        int read = this.f28in.read(this.buffer);
        if (read == -1) {
            return null;
        }
        return MessageBuffer.wrap(this.buffer, 0, read);
    }

    public void close() throws IOException {
        this.f28in.close();
    }
}
