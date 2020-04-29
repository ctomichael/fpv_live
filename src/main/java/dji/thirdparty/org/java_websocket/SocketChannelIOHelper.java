package dji.thirdparty.org.java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

public class SocketChannelIOHelper {
    static final /* synthetic */ boolean $assertionsDisabled = (!SocketChannelIOHelper.class.desiredAssertionStatus());

    public static boolean read(ByteBuffer buf, WebSocketImpl ws, ByteChannel channel) throws IOException {
        buf.clear();
        int read = channel.read(buf);
        buf.flip();
        if (read == -1) {
            ws.eot();
            return false;
        } else if (read != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean readMore(ByteBuffer buf, WebSocketImpl ws, WrappedByteChannel channel) throws IOException {
        buf.clear();
        int read = channel.readMore(buf);
        buf.flip();
        if (read != -1) {
            return channel.isNeedRead();
        }
        ws.eot();
        return false;
    }

    public static boolean batch(WebSocketImpl ws, ByteChannel sockchannel) throws IOException {
        ByteBuffer buffer = ws.outQueue.peek();
        WrappedByteChannel c = null;
        if (buffer != null) {
            do {
                sockchannel.write(buffer);
                if (buffer.remaining() > 0) {
                    return false;
                }
                ws.outQueue.poll();
                buffer = ws.outQueue.peek();
            } while (buffer != null);
        } else if (sockchannel instanceof WrappedByteChannel) {
            c = (WrappedByteChannel) sockchannel;
            if (c.isNeedWrite()) {
                c.writeMore();
            }
        }
        if (ws.outQueue.isEmpty() && ws.isFlushAndClose()) {
            synchronized (ws) {
                ws.closeConnection();
            }
        }
        if (c == null || !((WrappedByteChannel) sockchannel).isNeedWrite()) {
            return true;
        }
        return false;
    }

    public static void writeBlocking(WebSocketImpl ws, ByteChannel channel) throws InterruptedException, IOException {
        if (!$assertionsDisabled && (channel instanceof AbstractSelectableChannel) && !((AbstractSelectableChannel) channel).isBlocking()) {
            throw new AssertionError();
        } else if ($assertionsDisabled || !(channel instanceof WrappedByteChannel) || ((WrappedByteChannel) channel).isBlocking()) {
            ByteBuffer buf = ws.outQueue.take();
            while (buf.hasRemaining()) {
                channel.write(buf);
            }
        } else {
            throw new AssertionError();
        }
    }
}
