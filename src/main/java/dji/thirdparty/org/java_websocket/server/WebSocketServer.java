package dji.thirdparty.org.java_websocket.server;

import dji.thirdparty.org.java_websocket.WebSocket;
import dji.thirdparty.org.java_websocket.WebSocketAdapter;
import dji.thirdparty.org.java_websocket.WebSocketFactory;
import dji.thirdparty.org.java_websocket.WebSocketImpl;
import dji.thirdparty.org.java_websocket.drafts.Draft;
import dji.thirdparty.org.java_websocket.handshake.ClientHandshake;
import dji.thirdparty.org.java_websocket.handshake.Handshakedata;
import java.io.IOException;
import java.lang.Thread;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class WebSocketServer extends WebSocketAdapter implements Runnable {
    public static int DECODERS = Runtime.getRuntime().availableProcessors();
    private final InetSocketAddress address;
    private BlockingQueue<ByteBuffer> buffers;
    private final Collection<WebSocket> connections;
    private List<WebSocketWorker> decoders;
    private List<Draft> drafts;
    private List<WebSocketImpl> iqueue;
    private volatile AtomicBoolean isclosed;
    private int queueinvokes;
    private AtomicInteger queuesize;
    private Selector selector;
    private Thread selectorthread;
    private ServerSocketChannel server;
    private WebSocketServerFactory wsf;

    public interface WebSocketServerFactory extends WebSocketFactory {
        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, Draft draft, Socket socket);

        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, List<Draft> list, Socket socket);

        ByteChannel wrapChannel(SocketChannel socketChannel, SelectionKey selectionKey) throws IOException;
    }

    public abstract void onClose(WebSocket webSocket, int i, String str, boolean z);

    public abstract void onError(WebSocket webSocket, Exception exc);

    public abstract void onMessage(WebSocket webSocket, String str);

    public abstract void onOpen(WebSocket webSocket, ClientHandshake clientHandshake);

    public WebSocketServer() throws UnknownHostException {
        this(new InetSocketAddress(80), DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address2) {
        this(address2, DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address2, int decoders2) {
        this(address2, decoders2, null);
    }

    public WebSocketServer(InetSocketAddress address2, List<Draft> drafts2) {
        this(address2, DECODERS, drafts2);
    }

    public WebSocketServer(InetSocketAddress address2, int decodercount, List<Draft> drafts2) {
        this(address2, decodercount, drafts2, new HashSet());
    }

    public WebSocketServer(InetSocketAddress address2, int decodercount, List<Draft> drafts2, Collection<WebSocket> connectionscontainer) {
        this.isclosed = new AtomicBoolean(false);
        this.queueinvokes = 0;
        this.queuesize = new AtomicInteger(0);
        this.wsf = new DefaultWebSocketServerFactory();
        if (address2 == null || decodercount < 1 || connectionscontainer == null) {
            throw new IllegalArgumentException("address and connectionscontainer must not be null and you need at least 1 decoder");
        }
        if (drafts2 == null) {
            this.drafts = Collections.emptyList();
        } else {
            this.drafts = drafts2;
        }
        this.address = address2;
        this.connections = connectionscontainer;
        this.iqueue = new LinkedList();
        this.decoders = new ArrayList(decodercount);
        this.buffers = new LinkedBlockingQueue();
        for (int i = 0; i < decodercount; i++) {
            WebSocketWorker ex = new WebSocketWorker();
            this.decoders.add(ex);
            ex.start();
        }
    }

    public void start() {
        if (this.selectorthread != null) {
            throw new IllegalStateException(getClass().getName() + " can only be started once.");
        }
        new Thread(this).start();
    }

    public void stop(int timeout) throws IOException, InterruptedException {
        if (this.isclosed.compareAndSet(false, true)) {
            synchronized (this.connections) {
                for (WebSocket ws : this.connections) {
                    ws.close(1001);
                }
            }
            synchronized (this) {
                if (this.selectorthread != null) {
                    if (Thread.currentThread() != this.selectorthread) {
                    }
                    if (this.selectorthread != Thread.currentThread()) {
                        this.selectorthread.interrupt();
                        this.selectorthread.join();
                    }
                }
                if (this.decoders != null) {
                    for (WebSocketWorker w : this.decoders) {
                        w.interrupt();
                    }
                }
                if (this.server != null) {
                    this.server.close();
                }
            }
        }
    }

    public void stop() throws IOException, InterruptedException {
        stop(0);
    }

    public Collection<WebSocket> connections() {
        return this.connections;
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        int port = getAddress().getPort();
        if (port != 0 || this.server == null) {
            return port;
        }
        return this.server.socket().getLocalPort();
    }

    public List<Draft> getDraft() {
        return Collections.unmodifiableList(this.drafts);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v34, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v30, resolved type: dji.thirdparty.org.java_websocket.WebSocketImpl} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v39, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v32, resolved type: dji.thirdparty.org.java_websocket.WebSocketImpl} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.org.java_websocket.server.WebSocketServer.WebSocketServerFactory.createWebSocket(dji.thirdparty.org.java_websocket.WebSocketAdapter, java.util.List<dji.thirdparty.org.java_websocket.drafts.Draft>, java.net.Socket):dji.thirdparty.org.java_websocket.WebSocketImpl
     arg types: [dji.thirdparty.org.java_websocket.server.WebSocketServer, java.util.List<dji.thirdparty.org.java_websocket.drafts.Draft>, java.net.Socket]
     candidates:
      dji.thirdparty.org.java_websocket.server.WebSocketServer.WebSocketServerFactory.createWebSocket(dji.thirdparty.org.java_websocket.WebSocketAdapter, dji.thirdparty.org.java_websocket.drafts.Draft, java.net.Socket):dji.thirdparty.org.java_websocket.WebSocketImpl
      dji.thirdparty.org.java_websocket.WebSocketFactory.createWebSocket(dji.thirdparty.org.java_websocket.WebSocketAdapter, dji.thirdparty.org.java_websocket.drafts.Draft, java.net.Socket):dji.thirdparty.org.java_websocket.WebSocket
      dji.thirdparty.org.java_websocket.WebSocketFactory.createWebSocket(dji.thirdparty.org.java_websocket.WebSocketAdapter, java.util.List<dji.thirdparty.org.java_websocket.drafts.Draft>, java.net.Socket):dji.thirdparty.org.java_websocket.WebSocket
      dji.thirdparty.org.java_websocket.server.WebSocketServer.WebSocketServerFactory.createWebSocket(dji.thirdparty.org.java_websocket.WebSocketAdapter, java.util.List<dji.thirdparty.org.java_websocket.drafts.Draft>, java.net.Socket):dji.thirdparty.org.java_websocket.WebSocketImpl */
    /* JADX WARNING: Code restructure failed: missing block: B:108:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0041, code lost:
        r18.selectorthread.setName("WebsocketSelector" + r18.selectorthread.getId());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r18.server = java.nio.channels.ServerSocketChannel.open();
        r18.server.configureBlocking(false);
        r11 = r18.server.socket();
        r11.setReceiveBufferSize(dji.thirdparty.org.java_websocket.WebSocketImpl.RCVBUF);
        r11.bind(r18.address);
        r18.selector = java.nio.channels.Selector.open();
        r18.server.register(r18.selector, r18.server.validOps());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00ad, code lost:
        if (r18.selectorthread.isInterrupted() != false) goto L_0x003f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00af, code lost:
        r9 = null;
        r9 = null;
        r5 = null;
        r5 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r18.selector.select();
        r8 = r18.selector.selectedKeys().iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00c8, code lost:
        if (r8.hasNext() == false) goto L_0x01c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ca, code lost:
        r9 = r8.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00d6, code lost:
        if (r9.isValid() == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00dc, code lost:
        if (r9.isAcceptable() == false) goto L_0x0149;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00e4, code lost:
        if (onConnect(r9) != false) goto L_0x00f5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00e6, code lost:
        r9.cancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ec, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00ed, code lost:
        handleFatal(null, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        r4 = r18.server.accept();
        r4.configureBlocking(false);
        r12 = r18.wsf.createWebSocket((dji.thirdparty.org.java_websocket.WebSocketAdapter) r18, r18.drafts, r4.socket());
        r12.key = r4.register(r18.selector, 1, r12);
        r12.channel = r18.wsf.wrapChannel(r4, r12.key);
        r8.remove();
        allocateBuffers(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0133, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0134, code lost:
        if (r9 != null) goto L_0x0136;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r9.cancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0139, code lost:
        handleIOException(r9, r5, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0140, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0141, code lost:
        handleFatal(null, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x014d, code lost:
        if (r9.isReadable() == false) goto L_0x0187;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x014f, code lost:
        r5 = r9.attachment();
        r2 = takeBuffer();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0161, code lost:
        if (dji.thirdparty.org.java_websocket.SocketChannelIOHelper.read(r2, r5, r5.channel) == false) goto L_0x01ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0163, code lost:
        r5.inQueue.put(r2);
        queue(r5);
        r8.remove();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0174, code lost:
        if ((r5.channel instanceof dji.thirdparty.org.java_websocket.WrappedByteChannel) == false) goto L_0x0187;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x017e, code lost:
        if (((dji.thirdparty.org.java_websocket.WrappedByteChannel) r5.channel).isNeedRead() == false) goto L_0x0187;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0180, code lost:
        r18.iqueue.add(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x018b, code lost:
        if (r9.isWritable() == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x018d, code lost:
        r5 = r9.attachment();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x019b, code lost:
        if (dji.thirdparty.org.java_websocket.SocketChannelIOHelper.batch(r5, r5.channel) == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x01a1, code lost:
        if (r9.isValid() == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01a3, code lost:
        r9.interestOps(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
        pushBuffer(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01b2, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:?, code lost:
        pushBuffer(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x01b8, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x01b9, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01ba, code lost:
        pushBuffer(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01bf, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x01c8, code lost:
        if (r18.iqueue.isEmpty() != false) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01ca, code lost:
        r5 = r18.iqueue.remove(0);
        r3 = (dji.thirdparty.org.java_websocket.WrappedByteChannel) r5.channel;
        r2 = takeBuffer();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01e3, code lost:
        if (dji.thirdparty.org.java_websocket.SocketChannelIOHelper.readMore(r2, r5, r3) == false) goto L_0x01ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01e5, code lost:
        r18.iqueue.add(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01ec, code lost:
        r5.inQueue.put(r2);
        queue(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:?, code lost:
        pushBuffer(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01fc, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x01fd, code lost:
        pushBuffer(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0202, code lost:
        throw r13;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ea A[ExcHandler: CancelledKeyException (e java.nio.channels.CancelledKeyException), Splitter:B:23:0x00b3] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01a9 A[ExcHandler: InterruptedException (e java.lang.InterruptedException), Splitter:B:23:0x00b3] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r18 = this;
            monitor-enter(r18)
            r0 = r18
            java.lang.Thread r13 = r0.selectorthread     // Catch:{ all -> 0x0029 }
            if (r13 == 0) goto L_0x002c
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0029 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x0029 }
            r14.<init>()     // Catch:{ all -> 0x0029 }
            java.lang.Class r15 = r18.getClass()     // Catch:{ all -> 0x0029 }
            java.lang.String r15 = r15.getName()     // Catch:{ all -> 0x0029 }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ all -> 0x0029 }
            java.lang.String r15 = " can only be started once."
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ all -> 0x0029 }
            java.lang.String r14 = r14.toString()     // Catch:{ all -> 0x0029 }
            r13.<init>(r14)     // Catch:{ all -> 0x0029 }
            throw r13     // Catch:{ all -> 0x0029 }
        L_0x0029:
            r13 = move-exception
            monitor-exit(r18)     // Catch:{ all -> 0x0029 }
            throw r13
        L_0x002c:
            java.lang.Thread r13 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0029 }
            r0 = r18
            r0.selectorthread = r13     // Catch:{ all -> 0x0029 }
            r0 = r18
            java.util.concurrent.atomic.AtomicBoolean r13 = r0.isclosed     // Catch:{ all -> 0x0029 }
            boolean r13 = r13.get()     // Catch:{ all -> 0x0029 }
            if (r13 == 0) goto L_0x0040
            monitor-exit(r18)     // Catch:{ all -> 0x0029 }
        L_0x003f:
            return
        L_0x0040:
            monitor-exit(r18)     // Catch:{ all -> 0x0029 }
            r0 = r18
            java.lang.Thread r13 = r0.selectorthread
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "WebsocketSelector"
            java.lang.StringBuilder r14 = r14.append(r15)
            r0 = r18
            java.lang.Thread r15 = r0.selectorthread
            long r16 = r15.getId()
            r0 = r16
            java.lang.StringBuilder r14 = r14.append(r0)
            java.lang.String r14 = r14.toString()
            r13.setName(r14)
            java.nio.channels.ServerSocketChannel r13 = java.nio.channels.ServerSocketChannel.open()     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            r0.server = r13     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            java.nio.channels.ServerSocketChannel r13 = r0.server     // Catch:{ IOException -> 0x00ec }
            r14 = 0
            r13.configureBlocking(r14)     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            java.nio.channels.ServerSocketChannel r13 = r0.server     // Catch:{ IOException -> 0x00ec }
            java.net.ServerSocket r11 = r13.socket()     // Catch:{ IOException -> 0x00ec }
            int r13 = dji.thirdparty.org.java_websocket.WebSocketImpl.RCVBUF     // Catch:{ IOException -> 0x00ec }
            r11.setReceiveBufferSize(r13)     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            java.net.InetSocketAddress r13 = r0.address     // Catch:{ IOException -> 0x00ec }
            r11.bind(r13)     // Catch:{ IOException -> 0x00ec }
            java.nio.channels.Selector r13 = java.nio.channels.Selector.open()     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            r0.selector = r13     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            java.nio.channels.ServerSocketChannel r13 = r0.server     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            java.nio.channels.Selector r14 = r0.selector     // Catch:{ IOException -> 0x00ec }
            r0 = r18
            java.nio.channels.ServerSocketChannel r15 = r0.server     // Catch:{ IOException -> 0x00ec }
            int r15 = r15.validOps()     // Catch:{ IOException -> 0x00ec }
            r13.register(r14, r15)     // Catch:{ IOException -> 0x00ec }
        L_0x00a5:
            r0 = r18
            java.lang.Thread r13 = r0.selectorthread     // Catch:{ RuntimeException -> 0x0140 }
            boolean r13 = r13.isInterrupted()     // Catch:{ RuntimeException -> 0x0140 }
            if (r13 != 0) goto L_0x003f
            r9 = 0
            r5 = 0
            r0 = r18
            java.nio.channels.Selector r13 = r0.selector     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r13.select()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r18
            java.nio.channels.Selector r13 = r0.selector     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.util.Set r10 = r13.selectedKeys()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.util.Iterator r8 = r10.iterator()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        L_0x00c4:
            boolean r13 = r8.hasNext()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x01c0
            java.lang.Object r13 = r8.next()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r13
            java.nio.channels.SelectionKey r0 = (java.nio.channels.SelectionKey) r0     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r9 = r0
            boolean r13 = r9.isValid()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x00c4
            boolean r13 = r9.isAcceptable()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x0149
            r0 = r18
            boolean r13 = r0.onConnect(r9)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 != 0) goto L_0x00f5
            r9.cancel()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            goto L_0x00c4
        L_0x00ea:
            r13 = move-exception
            goto L_0x00a5
        L_0x00ec:
            r7 = move-exception
            r13 = 0
            r0 = r18
            r0.handleFatal(r13, r7)
            goto L_0x003f
        L_0x00f5:
            r0 = r18
            java.nio.channels.ServerSocketChannel r13 = r0.server     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.nio.channels.SocketChannel r4 = r13.accept()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r13 = 0
            r4.configureBlocking(r13)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r18
            dji.thirdparty.org.java_websocket.server.WebSocketServer$WebSocketServerFactory r13 = r0.wsf     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r18
            java.util.List<dji.thirdparty.org.java_websocket.drafts.Draft> r14 = r0.drafts     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.net.Socket r15 = r4.socket()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r18
            dji.thirdparty.org.java_websocket.WebSocketImpl r12 = r13.createWebSocket(r0, r14, r15)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r18
            java.nio.channels.Selector r13 = r0.selector     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r14 = 1
            java.nio.channels.SelectionKey r13 = r4.register(r13, r14, r12)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r12.key = r13     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r18
            dji.thirdparty.org.java_websocket.server.WebSocketServer$WebSocketServerFactory r13 = r0.wsf     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.nio.channels.SelectionKey r14 = r12.key     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.nio.channels.ByteChannel r13 = r13.wrapChannel(r4, r14)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r12.channel = r13     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r8.remove()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r18
            r0.allocateBuffers(r12)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            goto L_0x00c4
        L_0x0133:
            r7 = move-exception
            if (r9 == 0) goto L_0x0139
            r9.cancel()     // Catch:{ RuntimeException -> 0x0140 }
        L_0x0139:
            r0 = r18
            r0.handleIOException(r9, r5, r7)     // Catch:{ RuntimeException -> 0x0140 }
            goto L_0x00a5
        L_0x0140:
            r6 = move-exception
            r13 = 0
            r0 = r18
            r0.handleFatal(r13, r6)
            goto L_0x003f
        L_0x0149:
            boolean r13 = r9.isReadable()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x0187
            java.lang.Object r13 = r9.attachment()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r13
            dji.thirdparty.org.java_websocket.WebSocketImpl r0 = (dji.thirdparty.org.java_websocket.WebSocketImpl) r0     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r5 = r0
            java.nio.ByteBuffer r2 = r18.takeBuffer()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.nio.channels.ByteChannel r13 = r5.channel     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            boolean r13 = dji.thirdparty.org.java_websocket.SocketChannelIOHelper.read(r2, r5, r13)     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x01ac
            java.util.concurrent.BlockingQueue<java.nio.ByteBuffer> r13 = r5.inQueue     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            r13.put(r2)     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            r0 = r18
            r0.queue(r5)     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            r8.remove()     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            java.nio.channels.ByteChannel r13 = r5.channel     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            boolean r13 = r13 instanceof dji.thirdparty.org.java_websocket.WrappedByteChannel     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x0187
            java.nio.channels.ByteChannel r13 = r5.channel     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            dji.thirdparty.org.java_websocket.WrappedByteChannel r13 = (dji.thirdparty.org.java_websocket.WrappedByteChannel) r13     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            boolean r13 = r13.isNeedRead()     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x0187
            r0 = r18
            java.util.List<dji.thirdparty.org.java_websocket.WebSocketImpl> r13 = r0.iqueue     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            r13.add(r5)     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        L_0x0187:
            boolean r13 = r9.isWritable()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x00c4
            java.lang.Object r13 = r9.attachment()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r13
            dji.thirdparty.org.java_websocket.WebSocketImpl r0 = (dji.thirdparty.org.java_websocket.WebSocketImpl) r0     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r5 = r0
            java.nio.channels.ByteChannel r13 = r5.channel     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            boolean r13 = dji.thirdparty.org.java_websocket.SocketChannelIOHelper.batch(r5, r13)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x00c4
            boolean r13 = r9.isValid()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 == 0) goto L_0x00c4
            r13 = 1
            r9.interestOps(r13)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            goto L_0x00c4
        L_0x01a9:
            r6 = move-exception
            goto L_0x003f
        L_0x01ac:
            r0 = r18
            r0.pushBuffer(r2)     // Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
            goto L_0x0187
        L_0x01b2:
            r6 = move-exception
            r0 = r18
            r0.pushBuffer(r2)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            throw r6     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        L_0x01b9:
            r6 = move-exception
            r0 = r18
            r0.pushBuffer(r2)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            throw r6     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        L_0x01c0:
            r0 = r18
            java.util.List<dji.thirdparty.org.java_websocket.WebSocketImpl> r13 = r0.iqueue     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            boolean r13 = r13.isEmpty()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            if (r13 != 0) goto L_0x00a5
            r0 = r18
            java.util.List<dji.thirdparty.org.java_websocket.WebSocketImpl> r13 = r0.iqueue     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r14 = 0
            java.lang.Object r13 = r13.remove(r14)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r0 = r13
            dji.thirdparty.org.java_websocket.WebSocketImpl r0 = (dji.thirdparty.org.java_websocket.WebSocketImpl) r0     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            r5 = r0
            java.nio.channels.ByteChannel r3 = r5.channel     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            dji.thirdparty.org.java_websocket.WrappedByteChannel r3 = (dji.thirdparty.org.java_websocket.WrappedByteChannel) r3     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            java.nio.ByteBuffer r2 = r18.takeBuffer()     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            boolean r13 = dji.thirdparty.org.java_websocket.SocketChannelIOHelper.readMore(r2, r5, r3)     // Catch:{ all -> 0x01fc }
            if (r13 == 0) goto L_0x01ec
            r0 = r18
            java.util.List<dji.thirdparty.org.java_websocket.WebSocketImpl> r13 = r0.iqueue     // Catch:{ all -> 0x01fc }
            r13.add(r5)     // Catch:{ all -> 0x01fc }
        L_0x01ec:
            java.util.concurrent.BlockingQueue<java.nio.ByteBuffer> r13 = r5.inQueue     // Catch:{ all -> 0x01fc }
            r13.put(r2)     // Catch:{ all -> 0x01fc }
            r0 = r18
            r0.queue(r5)     // Catch:{ all -> 0x01fc }
            r0 = r18
            r0.pushBuffer(r2)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            goto L_0x01c0
        L_0x01fc:
            r13 = move-exception
            r0 = r18
            r0.pushBuffer(r2)     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
            throw r13     // Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.org.java_websocket.server.WebSocketServer.run():void");
    }

    /* access modifiers changed from: protected */
    public void allocateBuffers(WebSocket c) throws InterruptedException {
        if (this.queuesize.get() < (this.decoders.size() * 2) + 1) {
            this.queuesize.incrementAndGet();
            this.buffers.put(createBuffer());
        }
    }

    /* access modifiers changed from: protected */
    public void releaseBuffers(WebSocket c) throws InterruptedException {
    }

    public ByteBuffer createBuffer() {
        return ByteBuffer.allocate(WebSocketImpl.RCVBUF);
    }

    private void queue(WebSocketImpl ws) throws InterruptedException {
        if (ws.workerThread == null) {
            ws.workerThread = this.decoders.get(this.queueinvokes % this.decoders.size());
            this.queueinvokes++;
        }
        ws.workerThread.put(ws);
    }

    private ByteBuffer takeBuffer() throws InterruptedException {
        return this.buffers.take();
    }

    /* access modifiers changed from: private */
    public void pushBuffer(ByteBuffer buf) throws InterruptedException {
        if (this.buffers.size() <= this.queuesize.intValue()) {
            this.buffers.put(buf);
        }
    }

    private void handleIOException(SelectionKey key, WebSocket conn, IOException ex) {
        SelectableChannel channel;
        onWebsocketError(conn, ex);
        if (conn != null) {
            conn.closeConnection(1006, ex.getMessage());
        } else if (key != null && (channel = key.channel()) != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (IOException e) {
            }
            if (WebSocketImpl.DEBUG) {
                System.out.println("Connection closed because of" + ex);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFatal(WebSocket conn, Exception e) {
        onError(conn, e);
        try {
            stop();
        } catch (IOException e1) {
            onError(null, e1);
        } catch (InterruptedException e12) {
            Thread.currentThread().interrupt();
            onError(null, e12);
        }
    }

    /* access modifiers changed from: protected */
    public String getFlashSecurityPolicy() {
        return "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + getPort() + "\" /></cross-domain-policy>";
    }

    public final void onWebsocketMessage(WebSocket conn, String message) {
        onMessage(conn, message);
    }

    public final void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
        onMessage(conn, blob);
    }

    public final void onWebsocketOpen(WebSocket conn, Handshakedata handshake) {
        if (addConnection(conn)) {
            onOpen(conn, (ClientHandshake) handshake);
        }
    }

    public final void onWebsocketClose(WebSocket conn, int code, String reason, boolean remote) {
        this.selector.wakeup();
        try {
            if (removeConnection(conn)) {
                onClose(conn, code, reason, remote);
            }
            try {
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } finally {
            try {
                releaseBuffers(conn);
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean removeConnection(WebSocket ws) {
        boolean remove;
        synchronized (this.connections) {
            remove = this.connections.remove(ws);
        }
        return remove;
    }

    /* access modifiers changed from: protected */
    public boolean addConnection(WebSocket ws) {
        boolean add;
        synchronized (this.connections) {
            add = this.connections.add(ws);
        }
        return add;
    }

    public final void onWebsocketError(WebSocket conn, Exception ex) {
        onError(conn, ex);
    }

    public final void onWriteDemand(WebSocket w) {
        WebSocketImpl conn = (WebSocketImpl) w;
        try {
            conn.key.interestOps(5);
        } catch (CancelledKeyException e) {
            conn.outQueue.clear();
        }
        this.selector.wakeup();
    }

    public void onWebsocketCloseInitiated(WebSocket conn, int code, String reason) {
        onCloseInitiated(conn, code, reason);
    }

    public void onWebsocketClosing(WebSocket conn, int code, String reason, boolean remote) {
        onClosing(conn, code, reason, remote);
    }

    public void onCloseInitiated(WebSocket conn, int code, String reason) {
    }

    public void onClosing(WebSocket conn, int code, String reason, boolean remote) {
    }

    public final void setWebSocketFactory(WebSocketServerFactory wsf2) {
        this.wsf = wsf2;
    }

    public final WebSocketFactory getWebSocketFactory() {
        return this.wsf;
    }

    /* access modifiers changed from: protected */
    public boolean onConnect(SelectionKey key) {
        return true;
    }

    private Socket getSocket(WebSocket conn) {
        return ((SocketChannel) ((WebSocketImpl) conn).key.channel()).socket();
    }

    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
        return (InetSocketAddress) getSocket(conn).getLocalSocketAddress();
    }

    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
        return (InetSocketAddress) getSocket(conn).getRemoteSocketAddress();
    }

    public void onMessage(WebSocket conn, ByteBuffer message) {
    }

    public class WebSocketWorker extends Thread {
        static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketServer.class.desiredAssertionStatus());
        private BlockingQueue<WebSocketImpl> iqueue = new LinkedBlockingQueue();

        public WebSocketWorker() {
            setName("WebSocketWorker-" + getId());
            setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(WebSocketServer.this) {
                /* class dji.thirdparty.org.java_websocket.server.WebSocketServer.WebSocketWorker.AnonymousClass1 */

                public void uncaughtException(Thread t, Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, e);
                }
            });
        }

        public void put(WebSocketImpl ws) throws InterruptedException {
            this.iqueue.put(ws);
        }

        public void run() {
            ByteBuffer buf;
            WebSocketImpl ws = null;
            while (true) {
                try {
                    ws = this.iqueue.take();
                    buf = ws.inQueue.poll();
                    if ($assertionsDisabled || buf != null) {
                        ws.decode(buf);
                        WebSocketServer.this.pushBuffer(buf);
                    } else {
                        throw new AssertionError();
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (RuntimeException e2) {
                    WebSocketServer.this.handleFatal(ws, e2);
                    return;
                } catch (Throwable th) {
                    WebSocketServer.this.pushBuffer(buf);
                    throw th;
                }
            }
        }
    }
}
