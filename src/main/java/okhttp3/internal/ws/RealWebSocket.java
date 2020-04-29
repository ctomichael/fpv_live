package okhttp3.internal.ws;

import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.ws.WebSocketReader;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;

public final class RealWebSocket implements WebSocket, WebSocketReader.FrameCallback {
    static final /* synthetic */ boolean $assertionsDisabled = (!RealWebSocket.class.desiredAssertionStatus());
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000;
    private static final long MAX_QUEUE_SIZE = 16777216;
    private static final List<Protocol> ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
    private boolean awaitingPong;
    private Call call;
    private ScheduledFuture<?> cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    final WebSocketListener listener;
    private final ArrayDeque<Object> messageAndCloseQueue = new ArrayDeque<>();
    private final Request originalRequest;
    private final long pingIntervalMillis;
    private final ArrayDeque<ByteString> pongQueue = new ArrayDeque<>();
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private int receivedCloseCode = -1;
    private String receivedCloseReason;
    private int receivedPingCount;
    private int receivedPongCount;
    private int sentPingCount;
    private Streams streams;
    private WebSocketWriter writer;
    private final Runnable writerRunnable;

    public RealWebSocket(Request request, WebSocketListener listener2, Random random2, long pingIntervalMillis2) {
        if (!"GET".equals(request.method())) {
            throw new IllegalArgumentException("Request must be GET: " + request.method());
        }
        this.originalRequest = request;
        this.listener = listener2;
        this.random = random2;
        this.pingIntervalMillis = pingIntervalMillis2;
        byte[] nonce = new byte[16];
        random2.nextBytes(nonce);
        this.key = ByteString.of(nonce).base64();
        this.writerRunnable = new Runnable() {
            /* class okhttp3.internal.ws.RealWebSocket.AnonymousClass1 */

            public void run() {
                do {
                    try {
                    } catch (IOException e) {
                        RealWebSocket.this.failWebSocket(e, null);
                        return;
                    }
                } while (RealWebSocket.this.writeOneFrame());
            }
        };
    }

    public Request request() {
        return this.originalRequest;
    }

    public synchronized long queueSize() {
        return this.queueSize;
    }

    public void cancel() {
        this.call.cancel();
    }

    public void connect(OkHttpClient client) {
        OkHttpClient client2 = client.newBuilder().eventListener(EventListener.NONE).protocols(ONLY_HTTP1).build();
        final Request request = this.originalRequest.newBuilder().header("Upgrade", "websocket").header(DJISDKCacheKeys.CONNECTION, "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        this.call = Internal.instance.newWebSocketCall(client2, request);
        this.call.timeout().clearTimeout();
        this.call.enqueue(new Callback() {
            /* class okhttp3.internal.ws.RealWebSocket.AnonymousClass2 */

            public void onResponse(Call call, Response response) {
                try {
                    RealWebSocket.this.checkResponse(response);
                    StreamAllocation streamAllocation = Internal.instance.streamAllocation(call);
                    streamAllocation.noNewStreams();
                    Streams streams = streamAllocation.connection().newWebSocketStreams(streamAllocation);
                    try {
                        RealWebSocket.this.listener.onOpen(RealWebSocket.this, response);
                        RealWebSocket.this.initReaderAndWriter("OkHttp WebSocket " + request.url().redact(), streams);
                        streamAllocation.connection().socket().setSoTimeout(0);
                        RealWebSocket.this.loopReader();
                    } catch (Exception e) {
                        RealWebSocket.this.failWebSocket(e, null);
                    }
                } catch (ProtocolException e2) {
                    RealWebSocket.this.failWebSocket(e2, response);
                    Util.closeQuietly(response);
                }
            }

            public void onFailure(Call call, IOException e) {
                RealWebSocket.this.failWebSocket(e, null);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void checkResponse(Response response) throws ProtocolException {
        if (response.code() != 101) {
            throw new ProtocolException("Expected HTTP 101 response but was '" + response.code() + " " + response.message() + "'");
        }
        String headerConnection = response.header(DJISDKCacheKeys.CONNECTION);
        if (!"Upgrade".equalsIgnoreCase(headerConnection)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + headerConnection + "'");
        }
        String headerUpgrade = response.header("Upgrade");
        if (!"websocket".equalsIgnoreCase(headerUpgrade)) {
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + headerUpgrade + "'");
        }
        String headerAccept = response.header("Sec-WebSocket-Accept");
        String acceptExpected = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
        if (!acceptExpected.equals(headerAccept)) {
            throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + acceptExpected + "' but was '" + headerAccept + "'");
        }
    }

    public void initReaderAndWriter(String name, Streams streams2) throws IOException {
        synchronized (this) {
            this.streams = streams2;
            this.writer = new WebSocketWriter(streams2.client, streams2.sink, this.random);
            this.executor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(name, false));
            if (this.pingIntervalMillis != 0) {
                this.executor.scheduleAtFixedRate(new PingRunnable(), this.pingIntervalMillis, this.pingIntervalMillis, TimeUnit.MILLISECONDS);
            }
            if (!this.messageAndCloseQueue.isEmpty()) {
                runWriter();
            }
        }
        this.reader = new WebSocketReader(streams2.client, streams2.source, this);
    }

    public void loopReader() throws IOException {
        while (this.receivedCloseCode == -1) {
            this.reader.processNextFrame();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean processNextFrame() throws IOException {
        try {
            this.reader.processNextFrame();
            if (this.receivedCloseCode == -1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            failWebSocket(e, null);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void awaitTermination(int timeout, TimeUnit timeUnit) throws InterruptedException {
        this.executor.awaitTermination((long) timeout, timeUnit);
    }

    /* access modifiers changed from: package-private */
    public void tearDown() throws InterruptedException {
        if (this.cancelFuture != null) {
            this.cancelFuture.cancel(false);
        }
        this.executor.shutdown();
        this.executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    /* access modifiers changed from: package-private */
    public synchronized int sentPingCount() {
        return this.sentPingCount;
    }

    /* access modifiers changed from: package-private */
    public synchronized int receivedPingCount() {
        return this.receivedPingCount;
    }

    /* access modifiers changed from: package-private */
    public synchronized int receivedPongCount() {
        return this.receivedPongCount;
    }

    public void onReadMessage(String text) throws IOException {
        this.listener.onMessage(this, text);
    }

    public void onReadMessage(ByteString bytes) throws IOException {
        this.listener.onMessage(this, bytes);
    }

    public synchronized void onReadPing(ByteString payload) {
        if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
            this.pongQueue.add(payload);
            runWriter();
            this.receivedPingCount++;
        }
    }

    public synchronized void onReadPong(ByteString buffer) {
        this.receivedPongCount++;
        this.awaitingPong = false;
    }

    public void onReadClose(int code, String reason) {
        if (code == -1) {
            throw new IllegalArgumentException();
        }
        Streams toClose = null;
        synchronized (this) {
            if (this.receivedCloseCode != -1) {
                throw new IllegalStateException("already closed");
            }
            this.receivedCloseCode = code;
            this.receivedCloseReason = reason;
            if (this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
                toClose = this.streams;
                this.streams = null;
                if (this.cancelFuture != null) {
                    this.cancelFuture.cancel(false);
                }
                this.executor.shutdown();
            }
        }
        try {
            this.listener.onClosing(this, code, reason);
            if (toClose != null) {
                this.listener.onClosed(this, code, reason);
            }
        } finally {
            Util.closeQuietly(toClose);
        }
    }

    public boolean send(String text) {
        if (text != null) {
            return send(ByteString.encodeUtf8(text), 1);
        }
        throw new NullPointerException("text == null");
    }

    public boolean send(ByteString bytes) {
        if (bytes != null) {
            return send(bytes, 2);
        }
        throw new NullPointerException("bytes == null");
    }

    private synchronized boolean send(ByteString data, int formatOpcode) {
        boolean z = false;
        synchronized (this) {
            if (!this.failed && !this.enqueuedClose) {
                if (this.queueSize + ((long) data.size()) > MAX_QUEUE_SIZE) {
                    close(1001, null);
                } else {
                    this.queueSize += (long) data.size();
                    this.messageAndCloseQueue.add(new Message(formatOpcode, data));
                    runWriter();
                    z = true;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean pong(ByteString payload) {
        boolean z;
        if (this.failed || (this.enqueuedClose && this.messageAndCloseQueue.isEmpty())) {
            z = false;
        } else {
            this.pongQueue.add(payload);
            runWriter();
            z = true;
        }
        return z;
    }

    public boolean close(int code, String reason) {
        return close(code, reason, 60000);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean close(int code, String reason, long cancelAfterCloseMillis) {
        boolean z = true;
        synchronized (this) {
            WebSocketProtocol.validateCloseCode(code);
            ByteString reasonBytes = null;
            if (reason != null) {
                reasonBytes = ByteString.encodeUtf8(reason);
                if (((long) reasonBytes.size()) > 123) {
                    throw new IllegalArgumentException("reason.size() > 123: " + reason);
                }
            }
            if (this.failed || this.enqueuedClose) {
                z = false;
            } else {
                this.enqueuedClose = true;
                this.messageAndCloseQueue.add(new Close(code, reasonBytes, cancelAfterCloseMillis));
                runWriter();
            }
        }
        return z;
    }

    private void runWriter() {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.executor != null) {
            this.executor.execute(this.writerRunnable);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0046, code lost:
        if (r5 == null) goto L_0x0073;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r10.writePong(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        okhttp3.internal.Util.closeQuietly(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0075, code lost:
        if ((r4 instanceof okhttp3.internal.ws.RealWebSocket.Message) == false) goto L_0x00ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0077, code lost:
        r3 = ((okhttp3.internal.ws.RealWebSocket.Message) r4).data;
        r8 = okio.Okio.buffer(r10.newMessageSink(((okhttp3.internal.ws.RealWebSocket.Message) r4).formatOpcode, (long) r3.size()));
        r8.write(r3);
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0094, code lost:
        monitor-enter(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r16.queueSize -= (long) r3.size();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a3, code lost:
        monitor-exit(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00a8, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a9, code lost:
        okhttp3.internal.Util.closeQuietly(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00ac, code lost:
        throw r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00af, code lost:
        if ((r4 instanceof okhttp3.internal.ws.RealWebSocket.Close) == false) goto L_0x00c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b1, code lost:
        r2 = (okhttp3.internal.ws.RealWebSocket.Close) r4;
        r10.writeClose(r2.code, r2.reason);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00bc, code lost:
        if (r9 == null) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00be, code lost:
        r16.listener.onClosed(r16, r6, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00cd, code lost:
        throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean writeOneFrame() throws java.io.IOException {
        /*
            r16 = this;
            r11 = 0
            r4 = 0
            r6 = -1
            r7 = 0
            r9 = 0
            monitor-enter(r16)
            r0 = r16
            boolean r12 = r0.failed     // Catch:{ all -> 0x006c }
            if (r12 == 0) goto L_0x000e
            monitor-exit(r16)     // Catch:{ all -> 0x006c }
        L_0x000d:
            return r11
        L_0x000e:
            r0 = r16
            okhttp3.internal.ws.WebSocketWriter r10 = r0.writer     // Catch:{ all -> 0x006c }
            r0 = r16
            java.util.ArrayDeque<okio.ByteString> r12 = r0.pongQueue     // Catch:{ all -> 0x006c }
            java.lang.Object r5 = r12.poll()     // Catch:{ all -> 0x006c }
            okio.ByteString r5 = (okio.ByteString) r5     // Catch:{ all -> 0x006c }
            if (r5 != 0) goto L_0x0045
            r0 = r16
            java.util.ArrayDeque<java.lang.Object> r12 = r0.messageAndCloseQueue     // Catch:{ all -> 0x006c }
            java.lang.Object r4 = r12.poll()     // Catch:{ all -> 0x006c }
            boolean r12 = r4 instanceof okhttp3.internal.ws.RealWebSocket.Close     // Catch:{ all -> 0x006c }
            if (r12 == 0) goto L_0x006f
            r0 = r16
            int r6 = r0.receivedCloseCode     // Catch:{ all -> 0x006c }
            r0 = r16
            java.lang.String r7 = r0.receivedCloseReason     // Catch:{ all -> 0x006c }
            r11 = -1
            if (r6 == r11) goto L_0x0050
            r0 = r16
            okhttp3.internal.ws.RealWebSocket$Streams r9 = r0.streams     // Catch:{ all -> 0x006c }
            r11 = 0
            r0 = r16
            r0.streams = r11     // Catch:{ all -> 0x006c }
            r0 = r16
            java.util.concurrent.ScheduledExecutorService r11 = r0.executor     // Catch:{ all -> 0x006c }
            r11.shutdown()     // Catch:{ all -> 0x006c }
        L_0x0045:
            monitor-exit(r16)     // Catch:{ all -> 0x006c }
            if (r5 == 0) goto L_0x0073
            r10.writePong(r5)     // Catch:{ all -> 0x00a8 }
        L_0x004b:
            r11 = 1
            okhttp3.internal.Util.closeQuietly(r9)
            goto L_0x000d
        L_0x0050:
            r0 = r16
            java.util.concurrent.ScheduledExecutorService r12 = r0.executor     // Catch:{ all -> 0x006c }
            okhttp3.internal.ws.RealWebSocket$CancelRunnable r13 = new okhttp3.internal.ws.RealWebSocket$CancelRunnable     // Catch:{ all -> 0x006c }
            r0 = r16
            r13.<init>()     // Catch:{ all -> 0x006c }
            r0 = r4
            okhttp3.internal.ws.RealWebSocket$Close r0 = (okhttp3.internal.ws.RealWebSocket.Close) r0     // Catch:{ all -> 0x006c }
            r11 = r0
            long r14 = r11.cancelAfterCloseMillis     // Catch:{ all -> 0x006c }
            java.util.concurrent.TimeUnit r11 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ all -> 0x006c }
            java.util.concurrent.ScheduledFuture r11 = r12.schedule(r13, r14, r11)     // Catch:{ all -> 0x006c }
            r0 = r16
            r0.cancelFuture = r11     // Catch:{ all -> 0x006c }
            goto L_0x0045
        L_0x006c:
            r11 = move-exception
            monitor-exit(r16)     // Catch:{ all -> 0x006c }
            throw r11
        L_0x006f:
            if (r4 != 0) goto L_0x0045
            monitor-exit(r16)     // Catch:{ all -> 0x006c }
            goto L_0x000d
        L_0x0073:
            boolean r11 = r4 instanceof okhttp3.internal.ws.RealWebSocket.Message     // Catch:{ all -> 0x00a8 }
            if (r11 == 0) goto L_0x00ad
            r0 = r4
            okhttp3.internal.ws.RealWebSocket$Message r0 = (okhttp3.internal.ws.RealWebSocket.Message) r0     // Catch:{ all -> 0x00a8 }
            r11 = r0
            okio.ByteString r3 = r11.data     // Catch:{ all -> 0x00a8 }
            okhttp3.internal.ws.RealWebSocket$Message r4 = (okhttp3.internal.ws.RealWebSocket.Message) r4     // Catch:{ all -> 0x00a8 }
            int r11 = r4.formatOpcode     // Catch:{ all -> 0x00a8 }
            int r12 = r3.size()     // Catch:{ all -> 0x00a8 }
            long r12 = (long) r12     // Catch:{ all -> 0x00a8 }
            okio.Sink r11 = r10.newMessageSink(r11, r12)     // Catch:{ all -> 0x00a8 }
            okio.BufferedSink r8 = okio.Okio.buffer(r11)     // Catch:{ all -> 0x00a8 }
            r8.write(r3)     // Catch:{ all -> 0x00a8 }
            r8.close()     // Catch:{ all -> 0x00a8 }
            monitor-enter(r16)     // Catch:{ all -> 0x00a8 }
            r0 = r16
            long r12 = r0.queueSize     // Catch:{ all -> 0x00a5 }
            int r11 = r3.size()     // Catch:{ all -> 0x00a5 }
            long r14 = (long) r11     // Catch:{ all -> 0x00a5 }
            long r12 = r12 - r14
            r0 = r16
            r0.queueSize = r12     // Catch:{ all -> 0x00a5 }
            monitor-exit(r16)     // Catch:{ all -> 0x00a5 }
            goto L_0x004b
        L_0x00a5:
            r11 = move-exception
            monitor-exit(r16)     // Catch:{ all -> 0x00a5 }
            throw r11     // Catch:{ all -> 0x00a8 }
        L_0x00a8:
            r11 = move-exception
            okhttp3.internal.Util.closeQuietly(r9)
            throw r11
        L_0x00ad:
            boolean r11 = r4 instanceof okhttp3.internal.ws.RealWebSocket.Close     // Catch:{ all -> 0x00a8 }
            if (r11 == 0) goto L_0x00c8
            r0 = r4
            okhttp3.internal.ws.RealWebSocket$Close r0 = (okhttp3.internal.ws.RealWebSocket.Close) r0     // Catch:{ all -> 0x00a8 }
            r2 = r0
            int r11 = r2.code     // Catch:{ all -> 0x00a8 }
            okio.ByteString r12 = r2.reason     // Catch:{ all -> 0x00a8 }
            r10.writeClose(r11, r12)     // Catch:{ all -> 0x00a8 }
            if (r9 == 0) goto L_0x004b
            r0 = r16
            okhttp3.WebSocketListener r11 = r0.listener     // Catch:{ all -> 0x00a8 }
            r0 = r16
            r11.onClosed(r0, r6, r7)     // Catch:{ all -> 0x00a8 }
            goto L_0x004b
        L_0x00c8:
            java.lang.AssertionError r11 = new java.lang.AssertionError     // Catch:{ all -> 0x00a8 }
            r11.<init>()     // Catch:{ all -> 0x00a8 }
            throw r11     // Catch:{ all -> 0x00a8 }
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.ws.RealWebSocket.writeOneFrame():boolean");
    }

    private final class PingRunnable implements Runnable {
        PingRunnable() {
        }

        public void run() {
            RealWebSocket.this.writePingFrame();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        if (r1 == -1) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        failWebSocket(new java.net.SocketTimeoutException("sent ping but didn't receive pong within " + r9.pingIntervalMillis + "ms (after " + (r1 - 1) + " successful ping/pongs)"), null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r2.writePing(okio.ByteString.EMPTY);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005c, code lost:
        failWebSocket(r0, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return;
     */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writePingFrame() {
        /*
            r9 = this;
            r8 = 0
            r3 = -1
            monitor-enter(r9)
            boolean r4 = r9.failed     // Catch:{ all -> 0x0052 }
            if (r4 == 0) goto L_0x0009
            monitor-exit(r9)     // Catch:{ all -> 0x0052 }
        L_0x0008:
            return
        L_0x0009:
            okhttp3.internal.ws.WebSocketWriter r2 = r9.writer     // Catch:{ all -> 0x0052 }
            boolean r4 = r9.awaitingPong     // Catch:{ all -> 0x0052 }
            if (r4 == 0) goto L_0x0050
            int r1 = r9.sentPingCount     // Catch:{ all -> 0x0052 }
        L_0x0011:
            int r4 = r9.sentPingCount     // Catch:{ all -> 0x0052 }
            int r4 = r4 + 1
            r9.sentPingCount = r4     // Catch:{ all -> 0x0052 }
            r4 = 1
            r9.awaitingPong = r4     // Catch:{ all -> 0x0052 }
            monitor-exit(r9)     // Catch:{ all -> 0x0052 }
            if (r1 == r3) goto L_0x0055
            java.net.SocketTimeoutException r3 = new java.net.SocketTimeoutException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "sent ping but didn't receive pong within "
            java.lang.StringBuilder r4 = r4.append(r5)
            long r6 = r9.pingIntervalMillis
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r5 = "ms (after "
            java.lang.StringBuilder r4 = r4.append(r5)
            int r5 = r1 + -1
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = " successful ping/pongs)"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            r9.failWebSocket(r3, r8)
            goto L_0x0008
        L_0x0050:
            r1 = r3
            goto L_0x0011
        L_0x0052:
            r3 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0052 }
            throw r3
        L_0x0055:
            okio.ByteString r3 = okio.ByteString.EMPTY     // Catch:{ IOException -> 0x005b }
            r2.writePing(r3)     // Catch:{ IOException -> 0x005b }
            goto L_0x0008
        L_0x005b:
            r0 = move-exception
            r9.failWebSocket(r0, r8)
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.ws.RealWebSocket.writePingFrame():void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r3.listener.onFailure(r3, r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002f, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0030, code lost:
        okhttp3.internal.Util.closeQuietly(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0033, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void failWebSocket(java.lang.Exception r4, @javax.annotation.Nullable okhttp3.Response r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r1 = r3.failed     // Catch:{ all -> 0x002c }
            if (r1 == 0) goto L_0x0007
            monitor-exit(r3)     // Catch:{ all -> 0x002c }
        L_0x0006:
            return
        L_0x0007:
            r1 = 1
            r3.failed = r1     // Catch:{ all -> 0x002c }
            okhttp3.internal.ws.RealWebSocket$Streams r0 = r3.streams     // Catch:{ all -> 0x002c }
            r1 = 0
            r3.streams = r1     // Catch:{ all -> 0x002c }
            java.util.concurrent.ScheduledFuture<?> r1 = r3.cancelFuture     // Catch:{ all -> 0x002c }
            if (r1 == 0) goto L_0x0019
            java.util.concurrent.ScheduledFuture<?> r1 = r3.cancelFuture     // Catch:{ all -> 0x002c }
            r2 = 0
            r1.cancel(r2)     // Catch:{ all -> 0x002c }
        L_0x0019:
            java.util.concurrent.ScheduledExecutorService r1 = r3.executor     // Catch:{ all -> 0x002c }
            if (r1 == 0) goto L_0x0022
            java.util.concurrent.ScheduledExecutorService r1 = r3.executor     // Catch:{ all -> 0x002c }
            r1.shutdown()     // Catch:{ all -> 0x002c }
        L_0x0022:
            monitor-exit(r3)     // Catch:{ all -> 0x002c }
            okhttp3.WebSocketListener r1 = r3.listener     // Catch:{ all -> 0x002f }
            r1.onFailure(r3, r4, r5)     // Catch:{ all -> 0x002f }
            okhttp3.internal.Util.closeQuietly(r0)
            goto L_0x0006
        L_0x002c:
            r1 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x002c }
            throw r1
        L_0x002f:
            r1 = move-exception
            okhttp3.internal.Util.closeQuietly(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.ws.RealWebSocket.failWebSocket(java.lang.Exception, okhttp3.Response):void");
    }

    static final class Message {
        final ByteString data;
        final int formatOpcode;

        Message(int formatOpcode2, ByteString data2) {
            this.formatOpcode = formatOpcode2;
            this.data = data2;
        }
    }

    static final class Close {
        final long cancelAfterCloseMillis;
        final int code;
        final ByteString reason;

        Close(int code2, ByteString reason2, long cancelAfterCloseMillis2) {
            this.code = code2;
            this.reason = reason2;
            this.cancelAfterCloseMillis = cancelAfterCloseMillis2;
        }
    }

    public static abstract class Streams implements Closeable {
        public final boolean client;
        public final BufferedSink sink;
        public final BufferedSource source;

        public Streams(boolean client2, BufferedSource source2, BufferedSink sink2) {
            this.client = client2;
            this.source = source2;
            this.sink = sink2;
        }
    }

    final class CancelRunnable implements Runnable {
        CancelRunnable() {
        }

        public void run() {
            RealWebSocket.this.cancel();
        }
    }
}
