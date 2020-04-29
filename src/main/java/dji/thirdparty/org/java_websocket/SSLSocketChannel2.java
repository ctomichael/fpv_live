package dji.thirdparty.org.java_websocket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public class SSLSocketChannel2 implements ByteChannel, WrappedByteChannel {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected static ByteBuffer emptybuffer = ByteBuffer.allocate(0);
    protected SSLEngineResult engineResult;
    private SSLEngineResult.Status engineStatus = SSLEngineResult.Status.BUFFER_UNDERFLOW;
    protected ExecutorService exec;
    protected ByteBuffer inCrypt;
    protected ByteBuffer inData;
    protected ByteBuffer outCrypt;
    protected SelectionKey selectionKey;
    protected SocketChannel socketChannel;
    protected SSLEngine sslEngine;
    protected List<Future<?>> tasks;

    static {
        boolean z;
        if (!SSLSocketChannel2.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    public SSLSocketChannel2(SocketChannel channel, SSLEngine sslEngine2, ExecutorService exec2, SelectionKey key) throws IOException {
        if (channel == null || sslEngine2 == null || exec2 == null) {
            throw new IllegalArgumentException("parameter must not be null");
        }
        this.socketChannel = channel;
        this.sslEngine = sslEngine2;
        this.exec = exec2;
        this.tasks = new ArrayList(3);
        if (key != null) {
            key.interestOps(key.interestOps() | 4);
            this.selectionKey = key;
        }
        createBuffers(sslEngine2.getSession());
        this.socketChannel.write(wrap(emptybuffer));
        processHandshake();
    }

    private void consumeFutureUninterruptible(Future<?> f) {
        boolean interrupted = false;
        while (true) {
            try {
                f.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
        if (interrupted) {
            try {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
        if (isBlocking() == false) goto L_0x000b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
        consumeFutureUninterruptible(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void processHandshake() throws java.io.IOException {
        /*
            r5 = this;
            monitor-enter(r5)
            javax.net.ssl.SSLEngineResult r3 = r5.engineResult     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r3 = r3.getHandshakeStatus()     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING     // Catch:{ all -> 0x0031 }
            if (r3 != r4) goto L_0x000d
        L_0x000b:
            monitor-exit(r5)
            return
        L_0x000d:
            java.util.List<java.util.concurrent.Future<?>> r3 = r5.tasks     // Catch:{ all -> 0x0031 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0031 }
            if (r3 != 0) goto L_0x003e
            java.util.List<java.util.concurrent.Future<?>> r3 = r5.tasks     // Catch:{ all -> 0x0031 }
            java.util.Iterator r1 = r3.iterator()     // Catch:{ all -> 0x0031 }
        L_0x001b:
            boolean r3 = r1.hasNext()     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x003e
            java.lang.Object r0 = r1.next()     // Catch:{ all -> 0x0031 }
            java.util.concurrent.Future r0 = (java.util.concurrent.Future) r0     // Catch:{ all -> 0x0031 }
            boolean r3 = r0.isDone()     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x0034
            r1.remove()     // Catch:{ all -> 0x0031 }
            goto L_0x001b
        L_0x0031:
            r3 = move-exception
            monitor-exit(r5)
            throw r3
        L_0x0034:
            boolean r3 = r5.isBlocking()     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x000b
            r5.consumeFutureUninterruptible(r0)     // Catch:{ all -> 0x0031 }
            goto L_0x000b
        L_0x003e:
            javax.net.ssl.SSLEngineResult r3 = r5.engineResult     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r3 = r3.getHandshakeStatus()     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_UNWRAP     // Catch:{ all -> 0x0031 }
            if (r3 != r4) goto L_0x008f
            boolean r3 = r5.isBlocking()     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x0054
            javax.net.ssl.SSLEngineResult$Status r3 = r5.engineStatus     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$Status r4 = javax.net.ssl.SSLEngineResult.Status.BUFFER_UNDERFLOW     // Catch:{ all -> 0x0031 }
            if (r3 != r4) goto L_0x0072
        L_0x0054:
            java.nio.ByteBuffer r3 = r5.inCrypt     // Catch:{ all -> 0x0031 }
            r3.compact()     // Catch:{ all -> 0x0031 }
            java.nio.channels.SocketChannel r3 = r5.socketChannel     // Catch:{ all -> 0x0031 }
            java.nio.ByteBuffer r4 = r5.inCrypt     // Catch:{ all -> 0x0031 }
            int r2 = r3.read(r4)     // Catch:{ all -> 0x0031 }
            r3 = -1
            if (r2 != r3) goto L_0x006d
            java.io.IOException r3 = new java.io.IOException     // Catch:{ all -> 0x0031 }
            java.lang.String r4 = "connection closed unexpectedly by peer"
            r3.<init>(r4)     // Catch:{ all -> 0x0031 }
            throw r3     // Catch:{ all -> 0x0031 }
        L_0x006d:
            java.nio.ByteBuffer r3 = r5.inCrypt     // Catch:{ all -> 0x0031 }
            r3.flip()     // Catch:{ all -> 0x0031 }
        L_0x0072:
            java.nio.ByteBuffer r3 = r5.inData     // Catch:{ all -> 0x0031 }
            r3.compact()     // Catch:{ all -> 0x0031 }
            r5.unwrap()     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult r3 = r5.engineResult     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r3 = r3.getHandshakeStatus()     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED     // Catch:{ all -> 0x0031 }
            if (r3 != r4) goto L_0x008f
            javax.net.ssl.SSLEngine r3 = r5.sslEngine     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLSession r3 = r3.getSession()     // Catch:{ all -> 0x0031 }
            r5.createBuffers(r3)     // Catch:{ all -> 0x0031 }
            goto L_0x000b
        L_0x008f:
            r5.consumeDelegatedTasks()     // Catch:{ all -> 0x0031 }
            boolean r3 = dji.thirdparty.org.java_websocket.SSLSocketChannel2.$assertionsDisabled     // Catch:{ all -> 0x0031 }
            if (r3 != 0) goto L_0x00a6
            javax.net.ssl.SSLEngineResult r3 = r5.engineResult     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r3 = r3.getHandshakeStatus()     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING     // Catch:{ all -> 0x0031 }
            if (r3 != r4) goto L_0x00a6
            java.lang.AssertionError r3 = new java.lang.AssertionError     // Catch:{ all -> 0x0031 }
            r3.<init>()     // Catch:{ all -> 0x0031 }
            throw r3     // Catch:{ all -> 0x0031 }
        L_0x00a6:
            java.util.List<java.util.concurrent.Future<?>> r3 = r5.tasks     // Catch:{ all -> 0x0031 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0031 }
            if (r3 != 0) goto L_0x00b8
            javax.net.ssl.SSLEngineResult r3 = r5.engineResult     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r3 = r3.getHandshakeStatus()     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_WRAP     // Catch:{ all -> 0x0031 }
            if (r3 != r4) goto L_0x000b
        L_0x00b8:
            java.nio.channels.SocketChannel r3 = r5.socketChannel     // Catch:{ all -> 0x0031 }
            java.nio.ByteBuffer r4 = dji.thirdparty.org.java_websocket.SSLSocketChannel2.emptybuffer     // Catch:{ all -> 0x0031 }
            java.nio.ByteBuffer r4 = r5.wrap(r4)     // Catch:{ all -> 0x0031 }
            r3.write(r4)     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult r3 = r5.engineResult     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r3 = r3.getHandshakeStatus()     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLEngineResult$HandshakeStatus r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED     // Catch:{ all -> 0x0031 }
            if (r3 != r4) goto L_0x000b
            javax.net.ssl.SSLEngine r3 = r5.sslEngine     // Catch:{ all -> 0x0031 }
            javax.net.ssl.SSLSession r3 = r3.getSession()     // Catch:{ all -> 0x0031 }
            r5.createBuffers(r3)     // Catch:{ all -> 0x0031 }
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.org.java_websocket.SSLSocketChannel2.processHandshake():void");
    }

    private synchronized ByteBuffer wrap(ByteBuffer b) throws SSLException {
        this.outCrypt.compact();
        this.engineResult = this.sslEngine.wrap(b, this.outCrypt);
        this.outCrypt.flip();
        return this.outCrypt;
    }

    private synchronized ByteBuffer unwrap() throws SSLException {
        while (true) {
            int rem = this.inData.remaining();
            this.engineResult = this.sslEngine.unwrap(this.inCrypt, this.inData);
            this.engineStatus = this.engineResult.getStatus();
            if (this.engineStatus != SSLEngineResult.Status.OK || (rem == this.inData.remaining() && this.engineResult.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
                this.inData.flip();
            }
        }
        this.inData.flip();
        return this.inData;
    }

    /* access modifiers changed from: protected */
    public void consumeDelegatedTasks() {
        while (true) {
            Runnable task = this.sslEngine.getDelegatedTask();
            if (task != null) {
                this.tasks.add(this.exec.submit(task));
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void createBuffers(SSLSession session) {
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        if (this.inData == null) {
            this.inData = ByteBuffer.allocate(appBufferMax);
            this.outCrypt = ByteBuffer.allocate(netBufferMax);
            this.inCrypt = ByteBuffer.allocate(netBufferMax);
        } else {
            if (this.inData.capacity() != appBufferMax) {
                this.inData = ByteBuffer.allocate(appBufferMax);
            }
            if (this.outCrypt.capacity() != netBufferMax) {
                this.outCrypt = ByteBuffer.allocate(netBufferMax);
            }
            if (this.inCrypt.capacity() != netBufferMax) {
                this.inCrypt = ByteBuffer.allocate(netBufferMax);
            }
        }
        this.inData.rewind();
        this.inData.flip();
        this.inCrypt.rewind();
        this.inCrypt.flip();
        this.outCrypt.rewind();
        this.outCrypt.flip();
    }

    public int write(ByteBuffer src) throws IOException {
        if (isHandShakeComplete()) {
            return this.socketChannel.write(wrap(src));
        }
        processHandshake();
        return 0;
    }

    public int read(ByteBuffer dst) throws IOException {
        if (!dst.hasRemaining()) {
            return 0;
        }
        if (!isHandShakeComplete()) {
            if (isBlocking()) {
                while (!isHandShakeComplete()) {
                    processHandshake();
                }
            } else {
                processHandshake();
                if (!isHandShakeComplete()) {
                    return 0;
                }
            }
        }
        int purged = readRemaining(dst);
        if (purged != 0) {
            return purged;
        }
        if ($assertionsDisabled || this.inData.position() == 0) {
            this.inData.clear();
            if (!this.inCrypt.hasRemaining()) {
                this.inCrypt.clear();
            } else {
                this.inCrypt.compact();
            }
            if (((isBlocking() && this.inCrypt.position() == 0) || this.engineStatus == SSLEngineResult.Status.BUFFER_UNDERFLOW) && this.socketChannel.read(this.inCrypt) == -1) {
                return -1;
            }
            this.inCrypt.flip();
            unwrap();
            int transfered = transfereTo(this.inData, dst);
            return (transfered != 0 || !isBlocking()) ? transfered : read(dst);
        }
        throw new AssertionError();
    }

    private int readRemaining(ByteBuffer dst) throws SSLException {
        if (this.inData.hasRemaining()) {
            return transfereTo(this.inData, dst);
        }
        if (!this.inData.hasRemaining()) {
            this.inData.clear();
        }
        if (this.inCrypt.hasRemaining()) {
            unwrap();
            int amount = transfereTo(this.inData, dst);
            if (amount <= 0) {
                return 0;
            }
            return amount;
        }
        return 0;
    }

    public boolean isConnected() {
        return this.socketChannel.isConnected();
    }

    public void close() throws IOException {
        this.sslEngine.closeOutbound();
        this.sslEngine.getSession().invalidate();
        if (this.socketChannel.isOpen()) {
            this.socketChannel.write(wrap(emptybuffer));
        }
        this.socketChannel.close();
    }

    private boolean isHandShakeComplete() {
        SSLEngineResult.HandshakeStatus status = this.engineResult.getHandshakeStatus();
        return status == SSLEngineResult.HandshakeStatus.FINISHED || status == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }

    public SelectableChannel configureBlocking(boolean b) throws IOException {
        return this.socketChannel.configureBlocking(b);
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return this.socketChannel.connect(remote);
    }

    public boolean finishConnect() throws IOException {
        return this.socketChannel.finishConnect();
    }

    public Socket socket() {
        return this.socketChannel.socket();
    }

    public boolean isInboundDone() {
        return this.sslEngine.isInboundDone();
    }

    public boolean isOpen() {
        return this.socketChannel.isOpen();
    }

    public boolean isNeedWrite() {
        return this.outCrypt.hasRemaining() || !isHandShakeComplete();
    }

    public void writeMore() throws IOException {
        write(this.outCrypt);
    }

    public boolean isNeedRead() {
        return this.inData.hasRemaining() || (this.inCrypt.hasRemaining() && this.engineResult.getStatus() != SSLEngineResult.Status.BUFFER_UNDERFLOW);
    }

    public int readMore(ByteBuffer dst) throws SSLException {
        return readRemaining(dst);
    }

    private int transfereTo(ByteBuffer from, ByteBuffer to) {
        int fremain = from.remaining();
        int toremain = to.remaining();
        if (fremain > toremain) {
            int limit = Math.min(fremain, toremain);
            for (int i = 0; i < limit; i++) {
                to.put(from.get());
            }
            return limit;
        }
        to.put(from);
        return fremain;
    }

    public boolean isBlocking() {
        return this.socketChannel.isBlocking();
    }
}
