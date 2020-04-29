package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Protocol;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okhttp3.internal.http2.Http2Reader;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public final class Http2Connection implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
    static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    /* access modifiers changed from: private */
    public static final ExecutorService listenerExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Http2Connection", true));
    /* access modifiers changed from: private */
    public boolean awaitingPong;
    long bytesLeftInWriteWindow;
    final boolean client;
    final Set<Integer> currentPushRequests = new LinkedHashSet();
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    int nextStreamId;
    Settings okHttpSettings = new Settings();
    final Settings peerSettings = new Settings();
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean receivedInitialPeerSettings = false;
    boolean shutdown;
    final Socket socket;
    final Map<Integer, Http2Stream> streams = new LinkedHashMap();
    long unacknowledgedBytesRead = 0;
    final Http2Writer writer;
    /* access modifiers changed from: private */
    public final ScheduledExecutorService writerExecutor;

    static {
        boolean z;
        if (!Http2Connection.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    Http2Connection(Builder builder) {
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        this.nextStreamId = builder.client ? 1 : 2;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.writerExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", this.hostname), false));
        if (builder.pingIntervalMillis != 0) {
            this.writerExecutor.scheduleAtFixedRate(new PingRunnable(false, 0, 0), (long) builder.pingIntervalMillis, (long) builder.pingIntervalMillis, TimeUnit.MILLISECONDS);
        }
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, 65535);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = (long) this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client));
    }

    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    public synchronized int openStreamCount() {
        return this.streams.size();
    }

    /* access modifiers changed from: package-private */
    public synchronized Http2Stream getStream(int id) {
        return this.streams.get(Integer.valueOf(id));
    }

    /* access modifiers changed from: package-private */
    public synchronized Http2Stream removeStream(int streamId) {
        Http2Stream stream;
        stream = this.streams.remove(Integer.valueOf(streamId));
        notifyAll();
        return stream;
    }

    public synchronized int maxConcurrentStreams() {
        return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateConnectionFlowControl(long read) {
        this.unacknowledgedBytesRead += read;
        if (this.unacknowledgedBytesRead >= ((long) (this.okHttpSettings.getInitialWindowSize() / 2))) {
            writeWindowUpdateLater(0, this.unacknowledgedBytesRead);
            this.unacknowledgedBytesRead = 0;
        }
    }

    public Http2Stream pushStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        if (!this.client) {
            return newStream(associatedStreamId, requestHeaders, out);
        }
        throw new IllegalStateException("Client cannot push requests.");
    }

    public Http2Stream newStream(List<Header> requestHeaders, boolean out) throws IOException {
        return newStream(0, requestHeaders, out);
    }

    private Http2Stream newStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        int streamId;
        Http2Stream stream;
        boolean flushHeaders;
        boolean outFinished = !out;
        synchronized (this.writer) {
            synchronized (this) {
                if (this.nextStreamId > 1073741823) {
                    shutdown(ErrorCode.REFUSED_STREAM);
                }
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                streamId = this.nextStreamId;
                this.nextStreamId += 2;
                stream = new Http2Stream(streamId, this, outFinished, false, null);
                flushHeaders = !out || this.bytesLeftInWriteWindow == 0 || stream.bytesLeftInWriteWindow == 0;
                if (stream.isOpen()) {
                    this.streams.put(Integer.valueOf(streamId), stream);
                }
            }
            if (associatedStreamId == 0) {
                this.writer.synStream(outFinished, streamId, associatedStreamId, requestHeaders);
            } else if (this.client) {
                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
            } else {
                this.writer.pushPromise(associatedStreamId, streamId, requestHeaders);
            }
        }
        if (flushHeaders) {
            this.writer.flush();
        }
        return stream;
    }

    /* access modifiers changed from: package-private */
    public void writeSynReply(int streamId, boolean outFinished, List<Header> alternating) throws IOException {
        this.writer.synReply(outFinished, streamId, alternating);
    }

    public void writeData(int streamId, boolean outFinished, Buffer buffer, long byteCount) throws IOException {
        int toWrite;
        boolean z;
        if (byteCount == 0) {
            this.writer.data(outFinished, streamId, buffer, 0);
            return;
        }
        while (byteCount > 0) {
            synchronized (this) {
                while (this.bytesLeftInWriteWindow <= 0) {
                    try {
                        if (!this.streams.containsKey(Integer.valueOf(streamId))) {
                            throw new IOException("stream closed");
                        }
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException();
                    }
                }
                toWrite = Math.min((int) Math.min(byteCount, this.bytesLeftInWriteWindow), this.writer.maxDataLength());
                this.bytesLeftInWriteWindow -= (long) toWrite;
            }
            byteCount -= (long) toWrite;
            Http2Writer http2Writer = this.writer;
            if (!outFinished || byteCount != 0) {
                z = false;
            } else {
                z = true;
            }
            http2Writer.data(z, streamId, buffer, toWrite);
        }
    }

    /* access modifiers changed from: package-private */
    public void writeSynResetLater(int streamId, ErrorCode errorCode) {
        try {
            final int i = streamId;
            final ErrorCode errorCode2 = errorCode;
            this.writerExecutor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
                /* class okhttp3.internal.http2.Http2Connection.AnonymousClass1 */

                public void execute() {
                    try {
                        Http2Connection.this.writeSynReset(i, errorCode2);
                    } catch (IOException e) {
                        Http2Connection.this.failConnection();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
        }
    }

    /* access modifiers changed from: package-private */
    public void writeSynReset(int streamId, ErrorCode statusCode) throws IOException {
        this.writer.rstStream(streamId, statusCode);
    }

    /* access modifiers changed from: package-private */
    public void writeWindowUpdateLater(int streamId, long unacknowledgedBytesRead2) {
        try {
            final int i = streamId;
            final long j = unacknowledgedBytesRead2;
            this.writerExecutor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
                /* class okhttp3.internal.http2.Http2Connection.AnonymousClass2 */

                public void execute() {
                    try {
                        Http2Connection.this.writer.windowUpdate(i, j);
                    } catch (IOException e) {
                        Http2Connection.this.failConnection();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
        }
    }

    final class PingRunnable extends NamedRunnable {
        final int payload1;
        final int payload2;
        final boolean reply;

        PingRunnable(boolean reply2, int payload12, int payload22) {
            super("OkHttp %s ping %08x%08x", Http2Connection.this.hostname, Integer.valueOf(payload12), Integer.valueOf(payload22));
            this.reply = reply2;
            this.payload1 = payload12;
            this.payload2 = payload22;
        }

        public void execute() {
            Http2Connection.this.writePing(this.reply, this.payload1, this.payload2);
        }
    }

    /* access modifiers changed from: package-private */
    public void writePing(boolean reply, int payload1, int payload2) {
        boolean failedDueToMissingPong;
        if (!reply) {
            synchronized (this) {
                failedDueToMissingPong = this.awaitingPong;
                this.awaitingPong = true;
            }
            if (failedDueToMissingPong) {
                failConnection();
                return;
            }
        }
        try {
            this.writer.ping(reply, payload1, payload2);
        } catch (IOException e) {
            failConnection();
        }
    }

    /* access modifiers changed from: package-private */
    public void writePingAndAwaitPong() throws InterruptedException {
        writePing(false, 1330343787, -257978967);
        awaitPong();
    }

    /* access modifiers changed from: package-private */
    public synchronized void awaitPong() throws InterruptedException {
        while (this.awaitingPong) {
            wait();
        }
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void shutdown(ErrorCode statusCode) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (!this.shutdown) {
                    this.shutdown = true;
                    int lastGoodStreamId2 = this.lastGoodStreamId;
                    this.writer.goAway(lastGoodStreamId2, statusCode, Util.EMPTY_BYTE_ARRAY);
                }
            }
        }
    }

    public void close() throws IOException {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    /* JADX WARN: Type inference failed for: r5v13, types: [java.lang.Object[]], assign insn: 0x002c: INVOKE  (r5v13 ? I:java.lang.Object[]) = (r5v12 java.util.Collection<okhttp3.internal.http2.Http2Stream>), (r6v3 okhttp3.internal.http2.Http2Stream[]) type: INTERFACE call: java.util.Collection.toArray(java.lang.Object[]):java.lang.Object[] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close(okhttp3.internal.http2.ErrorCode r8, okhttp3.internal.http2.ErrorCode r9) throws java.io.IOException {
        /*
            r7 = this;
            boolean r5 = okhttp3.internal.http2.Http2Connection.$assertionsDisabled
            if (r5 != 0) goto L_0x0010
            boolean r5 = java.lang.Thread.holdsLock(r7)
            if (r5 == 0) goto L_0x0010
            java.lang.AssertionError r5 = new java.lang.AssertionError
            r5.<init>()
            throw r5
        L_0x0010:
            r4 = 0
            r7.shutdown(r8)     // Catch:{ IOException -> 0x0048 }
        L_0x0014:
            r3 = 0
            monitor-enter(r7)
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r5 = r7.streams     // Catch:{ all -> 0x004b }
            boolean r5 = r5.isEmpty()     // Catch:{ all -> 0x004b }
            if (r5 != 0) goto L_0x0039
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r5 = r7.streams     // Catch:{ all -> 0x004b }
            java.util.Collection r5 = r5.values()     // Catch:{ all -> 0x004b }
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r6 = r7.streams     // Catch:{ all -> 0x004b }
            int r6 = r6.size()     // Catch:{ all -> 0x004b }
            okhttp3.internal.http2.Http2Stream[] r6 = new okhttp3.internal.http2.Http2Stream[r6]     // Catch:{ all -> 0x004b }
            java.lang.Object[] r5 = r5.toArray(r6)     // Catch:{ all -> 0x004b }
            r0 = r5
            okhttp3.internal.http2.Http2Stream[] r0 = (okhttp3.internal.http2.Http2Stream[]) r0     // Catch:{ all -> 0x004b }
            r3 = r0
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r5 = r7.streams     // Catch:{ all -> 0x004b }
            r5.clear()     // Catch:{ all -> 0x004b }
        L_0x0039:
            monitor-exit(r7)     // Catch:{ all -> 0x004b }
            if (r3 == 0) goto L_0x0053
            int r6 = r3.length
            r5 = 0
        L_0x003e:
            if (r5 >= r6) goto L_0x0053
            r2 = r3[r5]
            r2.close(r9)     // Catch:{ IOException -> 0x004e }
        L_0x0045:
            int r5 = r5 + 1
            goto L_0x003e
        L_0x0048:
            r1 = move-exception
            r4 = r1
            goto L_0x0014
        L_0x004b:
            r5 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x004b }
            throw r5
        L_0x004e:
            r1 = move-exception
            if (r4 == 0) goto L_0x0045
            r4 = r1
            goto L_0x0045
        L_0x0053:
            okhttp3.internal.http2.Http2Writer r5 = r7.writer     // Catch:{ IOException -> 0x006a }
            r5.close()     // Catch:{ IOException -> 0x006a }
        L_0x0058:
            java.net.Socket r5 = r7.socket     // Catch:{ IOException -> 0x006f }
            r5.close()     // Catch:{ IOException -> 0x006f }
        L_0x005d:
            java.util.concurrent.ScheduledExecutorService r5 = r7.writerExecutor
            r5.shutdown()
            java.util.concurrent.ExecutorService r5 = r7.pushExecutor
            r5.shutdown()
            if (r4 == 0) goto L_0x0072
            throw r4
        L_0x006a:
            r1 = move-exception
            if (r4 != 0) goto L_0x0058
            r4 = r1
            goto L_0x0058
        L_0x006f:
            r1 = move-exception
            r4 = r1
            goto L_0x005d
        L_0x0072:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.close(okhttp3.internal.http2.ErrorCode, okhttp3.internal.http2.ErrorCode):void");
    }

    /* access modifiers changed from: private */
    public void failConnection() {
        try {
            close(ErrorCode.PROTOCOL_ERROR, ErrorCode.PROTOCOL_ERROR);
        } catch (IOException e) {
        }
    }

    public void start() throws IOException {
        start(true);
    }

    /* access modifiers changed from: package-private */
    public void start(boolean sendConnectionPreface) throws IOException {
        if (sendConnectionPreface) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            int windowSize = this.okHttpSettings.getInitialWindowSize();
            if (windowSize != 65535) {
                this.writer.windowUpdate(0, (long) (windowSize - 65535));
            }
        }
        new Thread(this.readerRunnable).start();
    }

    public void setSettings(Settings settings) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
            }
            this.writer.settings(settings);
        }
    }

    public synchronized boolean isShutdown() {
        return this.shutdown;
    }

    public static class Builder {
        boolean client;
        String hostname;
        Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        int pingIntervalMillis;
        PushObserver pushObserver = PushObserver.CANCEL;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;

        public Builder(boolean client2) {
            this.client = client2;
        }

        public Builder socket(Socket socket2) throws IOException {
            return socket(socket2, ((InetSocketAddress) socket2.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket2)), Okio.buffer(Okio.sink(socket2)));
        }

        public Builder socket(Socket socket2, String hostname2, BufferedSource source2, BufferedSink sink2) {
            this.socket = socket2;
            this.hostname = hostname2;
            this.source = source2;
            this.sink = sink2;
            return this;
        }

        public Builder listener(Listener listener2) {
            this.listener = listener2;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver2) {
            this.pushObserver = pushObserver2;
            return this;
        }

        public Builder pingIntervalMillis(int pingIntervalMillis2) {
            this.pingIntervalMillis = pingIntervalMillis2;
            return this;
        }

        public Http2Connection build() {
            return new Http2Connection(this);
        }
    }

    class ReaderRunnable extends NamedRunnable implements Http2Reader.Handler {
        final Http2Reader reader;

        ReaderRunnable(Http2Reader reader2) {
            super("OkHttp %s", Http2Connection.this.hostname);
            this.reader = reader2;
        }

        /* access modifiers changed from: protected */
        public void execute() {
            ErrorCode connectionErrorCode = ErrorCode.INTERNAL_ERROR;
            ErrorCode streamErrorCode = ErrorCode.INTERNAL_ERROR;
            try {
                this.reader.readConnectionPreface(this);
                do {
                } while (this.reader.nextFrame(false, this));
                try {
                    Http2Connection.this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
                } catch (IOException e) {
                }
                Util.closeQuietly(this.reader);
            } catch (IOException e2) {
                connectionErrorCode = ErrorCode.PROTOCOL_ERROR;
                try {
                    Http2Connection.this.close(connectionErrorCode, ErrorCode.PROTOCOL_ERROR);
                } catch (IOException e3) {
                }
                Util.closeQuietly(this.reader);
            } catch (Throwable th) {
                try {
                    Http2Connection.this.close(connectionErrorCode, streamErrorCode);
                } catch (IOException e4) {
                }
                Util.closeQuietly(this.reader);
                throw th;
            }
        }

        public void data(boolean inFinished, int streamId, BufferedSource source, int length) throws IOException {
            if (Http2Connection.this.pushedStream(streamId)) {
                Http2Connection.this.pushDataLater(streamId, source, length, inFinished);
                return;
            }
            Http2Stream dataStream = Http2Connection.this.getStream(streamId);
            if (dataStream == null) {
                Http2Connection.this.writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
                Http2Connection.this.updateConnectionFlowControl((long) length);
                source.skip((long) length);
                return;
            }
            dataStream.receiveData(source, length);
            if (inFinished) {
                dataStream.receiveFin();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0078, code lost:
            r6.receiveHeaders(r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x007b, code lost:
            if (r11 == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x007d, code lost:
            r6.receiveFin();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void headers(boolean r11, int r12, int r13, java.util.List<okhttp3.internal.http2.Header> r14) {
            /*
                r10 = this;
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this
                boolean r1 = r1.pushedStream(r12)
                if (r1 == 0) goto L_0x000e
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this
                r1.pushHeadersLater(r12, r14, r11)
            L_0x000d:
                return
            L_0x000e:
                okhttp3.internal.http2.Http2Connection r7 = okhttp3.internal.http2.Http2Connection.this
                monitor-enter(r7)
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                okhttp3.internal.http2.Http2Stream r6 = r1.getStream(r12)     // Catch:{ all -> 0x0021 }
                if (r6 != 0) goto L_0x0077
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                boolean r1 = r1.shutdown     // Catch:{ all -> 0x0021 }
                if (r1 == 0) goto L_0x0024
                monitor-exit(r7)     // Catch:{ all -> 0x0021 }
                goto L_0x000d
            L_0x0021:
                r1 = move-exception
                monitor-exit(r7)     // Catch:{ all -> 0x0021 }
                throw r1
            L_0x0024:
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                int r1 = r1.lastGoodStreamId     // Catch:{ all -> 0x0021 }
                if (r12 > r1) goto L_0x002c
                monitor-exit(r7)     // Catch:{ all -> 0x0021 }
                goto L_0x000d
            L_0x002c:
                int r1 = r12 % 2
                okhttp3.internal.http2.Http2Connection r2 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                int r2 = r2.nextStreamId     // Catch:{ all -> 0x0021 }
                int r2 = r2 % 2
                if (r1 != r2) goto L_0x0038
                monitor-exit(r7)     // Catch:{ all -> 0x0021 }
                goto L_0x000d
            L_0x0038:
                okhttp3.Headers r5 = okhttp3.internal.Util.toHeaders(r14)     // Catch:{ all -> 0x0021 }
                okhttp3.internal.http2.Http2Stream r0 = new okhttp3.internal.http2.Http2Stream     // Catch:{ all -> 0x0021 }
                okhttp3.internal.http2.Http2Connection r2 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                r3 = 0
                r1 = r12
                r4 = r11
                r0.<init>(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x0021 }
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                r1.lastGoodStreamId = r12     // Catch:{ all -> 0x0021 }
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r1 = r1.streams     // Catch:{ all -> 0x0021 }
                java.lang.Integer r2 = java.lang.Integer.valueOf(r12)     // Catch:{ all -> 0x0021 }
                r1.put(r2, r0)     // Catch:{ all -> 0x0021 }
                java.util.concurrent.ExecutorService r1 = okhttp3.internal.http2.Http2Connection.listenerExecutor     // Catch:{ all -> 0x0021 }
                okhttp3.internal.http2.Http2Connection$ReaderRunnable$1 r2 = new okhttp3.internal.http2.Http2Connection$ReaderRunnable$1     // Catch:{ all -> 0x0021 }
                java.lang.String r3 = "OkHttp %s stream %d"
                r4 = 2
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0021 }
                r8 = 0
                okhttp3.internal.http2.Http2Connection r9 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0021 }
                java.lang.String r9 = r9.hostname     // Catch:{ all -> 0x0021 }
                r4[r8] = r9     // Catch:{ all -> 0x0021 }
                r8 = 1
                java.lang.Integer r9 = java.lang.Integer.valueOf(r12)     // Catch:{ all -> 0x0021 }
                r4[r8] = r9     // Catch:{ all -> 0x0021 }
                r2.<init>(r3, r4, r0)     // Catch:{ all -> 0x0021 }
                r1.execute(r2)     // Catch:{ all -> 0x0021 }
                monitor-exit(r7)     // Catch:{ all -> 0x0021 }
                goto L_0x000d
            L_0x0077:
                monitor-exit(r7)     // Catch:{ all -> 0x0021 }
                r6.receiveHeaders(r14)
                if (r11 == 0) goto L_0x000d
                r6.receiveFin()
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.headers(boolean, int, int, java.util.List):void");
        }

        public void rstStream(int streamId, ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(streamId)) {
                Http2Connection.this.pushResetLater(streamId, errorCode);
                return;
            }
            Http2Stream rstStream = Http2Connection.this.removeStream(streamId);
            if (rstStream != null) {
                rstStream.receiveRstStream(errorCode);
            }
        }

        /* JADX WARN: Type inference failed for: r8v24, types: [java.lang.Object[]], assign insn: 0x005c: INVOKE  (r8v24 ? I:java.lang.Object[]) = (r8v23 java.util.Collection<okhttp3.internal.http2.Http2Stream>), (r10v4 okhttp3.internal.http2.Http2Stream[]) type: INTERFACE call: java.util.Collection.toArray(java.lang.Object[]):java.lang.Object[] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void settings(boolean r16, okhttp3.internal.http2.Settings r17) {
            /*
                r15 = this;
                r2 = 0
                r7 = 0
                okhttp3.internal.http2.Http2Connection r9 = okhttp3.internal.http2.Http2Connection.this
                monitor-enter(r9)
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0094 }
                int r5 = r8.getInitialWindowSize()     // Catch:{ all -> 0x0094 }
                if (r16 == 0) goto L_0x0017
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0094 }
                r8.clear()     // Catch:{ all -> 0x0094 }
            L_0x0017:
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0094 }
                r0 = r17
                r8.merge(r0)     // Catch:{ all -> 0x0094 }
                r0 = r17
                r15.applyAndAckSettings(r0)     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0094 }
                int r4 = r8.getInitialWindowSize()     // Catch:{ all -> 0x0094 }
                r8 = -1
                if (r4 == r8) goto L_0x0064
                if (r4 == r5) goto L_0x0064
                int r8 = r4 - r5
                long r2 = (long) r8     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                boolean r8 = r8.receivedInitialPeerSettings     // Catch:{ all -> 0x0094 }
                if (r8 != 0) goto L_0x0040
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                r10 = 1
                r8.receivedInitialPeerSettings = r10     // Catch:{ all -> 0x0094 }
            L_0x0040:
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r8 = r8.streams     // Catch:{ all -> 0x0094 }
                boolean r8 = r8.isEmpty()     // Catch:{ all -> 0x0094 }
                if (r8 != 0) goto L_0x0064
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r8 = r8.streams     // Catch:{ all -> 0x0094 }
                java.util.Collection r8 = r8.values()     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Http2Connection r10 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r10 = r10.streams     // Catch:{ all -> 0x0094 }
                int r10 = r10.size()     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Http2Stream[] r10 = new okhttp3.internal.http2.Http2Stream[r10]     // Catch:{ all -> 0x0094 }
                java.lang.Object[] r8 = r8.toArray(r10)     // Catch:{ all -> 0x0094 }
                r0 = r8
                okhttp3.internal.http2.Http2Stream[] r0 = (okhttp3.internal.http2.Http2Stream[]) r0     // Catch:{ all -> 0x0094 }
                r7 = r0
            L_0x0064:
                java.util.concurrent.ExecutorService r8 = okhttp3.internal.http2.Http2Connection.listenerExecutor     // Catch:{ all -> 0x0094 }
                okhttp3.internal.http2.Http2Connection$ReaderRunnable$2 r10 = new okhttp3.internal.http2.Http2Connection$ReaderRunnable$2     // Catch:{ all -> 0x0094 }
                java.lang.String r11 = "OkHttp %s settings"
                r12 = 1
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0094 }
                r13 = 0
                okhttp3.internal.http2.Http2Connection r14 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0094 }
                java.lang.String r14 = r14.hostname     // Catch:{ all -> 0x0094 }
                r12[r13] = r14     // Catch:{ all -> 0x0094 }
                r10.<init>(r11, r12)     // Catch:{ all -> 0x0094 }
                r8.execute(r10)     // Catch:{ all -> 0x0094 }
                monitor-exit(r9)     // Catch:{ all -> 0x0094 }
                if (r7 == 0) goto L_0x009a
                r8 = 0
                int r8 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r8 == 0) goto L_0x009a
                int r9 = r7.length
                r8 = 0
            L_0x0088:
                if (r8 >= r9) goto L_0x009a
                r6 = r7[r8]
                monitor-enter(r6)
                r6.addBytesToWriteWindow(r2)     // Catch:{ all -> 0x0097 }
                monitor-exit(r6)     // Catch:{ all -> 0x0097 }
                int r8 = r8 + 1
                goto L_0x0088
            L_0x0094:
                r8 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x0094 }
                throw r8
            L_0x0097:
                r8 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0097 }
                throw r8
            L_0x009a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.settings(boolean, okhttp3.internal.http2.Settings):void");
        }

        private void applyAndAckSettings(final Settings peerSettings) {
            try {
                Http2Connection.this.writerExecutor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[]{Http2Connection.this.hostname}) {
                    /* class okhttp3.internal.http2.Http2Connection.ReaderRunnable.AnonymousClass3 */

                    public void execute() {
                        try {
                            Http2Connection.this.writer.applyAndAckSettings(peerSettings);
                        } catch (IOException e) {
                            Http2Connection.this.failConnection();
                        }
                    }
                });
            } catch (RejectedExecutionException e) {
            }
        }

        public void ackSettings() {
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void ping(boolean r5, int r6, int r7) {
            /*
                r4 = this;
                if (r5 == 0) goto L_0x0015
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this
                monitor-enter(r1)
                okhttp3.internal.http2.Http2Connection r0 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0012 }
                r2 = 0
                boolean unused = r0.awaitingPong = r2     // Catch:{ all -> 0x0012 }
                okhttp3.internal.http2.Http2Connection r0 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0012 }
                r0.notifyAll()     // Catch:{ all -> 0x0012 }
                monitor-exit(r1)     // Catch:{ all -> 0x0012 }
            L_0x0011:
                return
            L_0x0012:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0012 }
                throw r0
            L_0x0015:
                okhttp3.internal.http2.Http2Connection r0 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ RejectedExecutionException -> 0x0027 }
                java.util.concurrent.ScheduledExecutorService r0 = r0.writerExecutor     // Catch:{ RejectedExecutionException -> 0x0027 }
                okhttp3.internal.http2.Http2Connection$PingRunnable r1 = new okhttp3.internal.http2.Http2Connection$PingRunnable     // Catch:{ RejectedExecutionException -> 0x0027 }
                okhttp3.internal.http2.Http2Connection r2 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ RejectedExecutionException -> 0x0027 }
                r3 = 1
                r1.<init>(r3, r6, r7)     // Catch:{ RejectedExecutionException -> 0x0027 }
                r0.execute(r1)     // Catch:{ RejectedExecutionException -> 0x0027 }
                goto L_0x0011
            L_0x0027:
                r0 = move-exception
                goto L_0x0011
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.ping(boolean, int, int):void");
        }

        public void goAway(int lastGoodStreamId, ErrorCode errorCode, ByteString debugData) {
            Http2Stream[] streamsCopy;
            if (debugData.size() > 0) {
            }
            synchronized (Http2Connection.this) {
                streamsCopy = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
            }
            for (Http2Stream http2Stream : streamsCopy) {
                if (http2Stream.getId() > lastGoodStreamId && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    Http2Connection.this.removeStream(http2Stream.getId());
                }
            }
        }

        public void windowUpdate(int streamId, long windowSizeIncrement) {
            if (streamId == 0) {
                synchronized (Http2Connection.this) {
                    Http2Connection.this.bytesLeftInWriteWindow += windowSizeIncrement;
                    Http2Connection.this.notifyAll();
                }
                return;
            }
            Http2Stream stream = Http2Connection.this.getStream(streamId);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(windowSizeIncrement);
                }
            }
        }

        public void priority(int streamId, int streamDependency, int weight, boolean exclusive) {
        }

        public void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) {
            Http2Connection.this.pushRequestLater(promisedStreamId, requestHeaders);
        }

        public void alternateService(int streamId, String origin, ByteString protocol, String host, int port, long maxAge) {
        }
    }

    /* access modifiers changed from: package-private */
    public boolean pushedStream(int streamId) {
        return streamId != 0 && (streamId & 1) == 0;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void pushRequestLater(int r7, java.util.List<okhttp3.internal.http2.Header> r8) {
        /*
            r6 = this;
            monitor-enter(r6)
            java.util.Set<java.lang.Integer> r0 = r6.currentPushRequests     // Catch:{ all -> 0x003e }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x003e }
            boolean r0 = r0.contains(r1)     // Catch:{ all -> 0x003e }
            if (r0 == 0) goto L_0x0014
            okhttp3.internal.http2.ErrorCode r0 = okhttp3.internal.http2.ErrorCode.PROTOCOL_ERROR     // Catch:{ all -> 0x003e }
            r6.writeSynResetLater(r7, r0)     // Catch:{ all -> 0x003e }
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
        L_0x0013:
            return
        L_0x0014:
            java.util.Set<java.lang.Integer> r0 = r6.currentPushRequests     // Catch:{ all -> 0x003e }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x003e }
            r0.add(r1)     // Catch:{ all -> 0x003e }
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
            okhttp3.internal.http2.Http2Connection$3 r0 = new okhttp3.internal.http2.Http2Connection$3     // Catch:{ RejectedExecutionException -> 0x003c }
            java.lang.String r2 = "OkHttp %s Push Request[%s]"
            r1 = 2
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ RejectedExecutionException -> 0x003c }
            r1 = 0
            java.lang.String r4 = r6.hostname     // Catch:{ RejectedExecutionException -> 0x003c }
            r3[r1] = r4     // Catch:{ RejectedExecutionException -> 0x003c }
            r1 = 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r7)     // Catch:{ RejectedExecutionException -> 0x003c }
            r3[r1] = r4     // Catch:{ RejectedExecutionException -> 0x003c }
            r1 = r6
            r4 = r7
            r5 = r8
            r0.<init>(r2, r3, r4, r5)     // Catch:{ RejectedExecutionException -> 0x003c }
            r6.pushExecutorExecute(r0)     // Catch:{ RejectedExecutionException -> 0x003c }
            goto L_0x0013
        L_0x003c:
            r0 = move-exception
            goto L_0x0013
        L_0x003e:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.pushRequestLater(int, java.util.List):void");
    }

    /* access modifiers changed from: package-private */
    public void pushHeadersLater(int streamId, List<Header> requestHeaders, boolean inFinished) {
        try {
            final int i = streamId;
            final List<Header> list = requestHeaders;
            final boolean z = inFinished;
            pushExecutorExecute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
                /* class okhttp3.internal.http2.Http2Connection.AnonymousClass4 */

                public void execute() {
                    boolean cancel = Http2Connection.this.pushObserver.onHeaders(i, list, z);
                    if (cancel) {
                        try {
                            Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                        } catch (IOException e) {
                            return;
                        }
                    }
                    if (cancel || z) {
                        synchronized (Http2Connection.this) {
                            Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i));
                        }
                    }
                }
            });
        } catch (RejectedExecutionException e) {
        }
    }

    /* access modifiers changed from: package-private */
    public void pushDataLater(int streamId, BufferedSource source, int byteCount, boolean inFinished) throws IOException {
        final Buffer buffer = new Buffer();
        source.require((long) byteCount);
        source.read(buffer, (long) byteCount);
        if (buffer.size() != ((long) byteCount)) {
            throw new IOException(buffer.size() + " != " + byteCount);
        }
        final int i = streamId;
        final int i2 = byteCount;
        final boolean z = inFinished;
        pushExecutorExecute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
            /* class okhttp3.internal.http2.Http2Connection.AnonymousClass5 */

            public void execute() {
                try {
                    boolean cancel = Http2Connection.this.pushObserver.onData(i, buffer, i2, z);
                    if (cancel) {
                        Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                    }
                    if (cancel || z) {
                        synchronized (Http2Connection.this) {
                            Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i));
                        }
                    }
                } catch (IOException e) {
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void pushResetLater(int streamId, ErrorCode errorCode) {
        final int i = streamId;
        final ErrorCode errorCode2 = errorCode;
        pushExecutorExecute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
            /* class okhttp3.internal.http2.Http2Connection.AnonymousClass6 */

            public void execute() {
                Http2Connection.this.pushObserver.onReset(i, errorCode2);
                synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i));
                }
            }
        });
    }

    private synchronized void pushExecutorExecute(NamedRunnable namedRunnable) {
        if (!isShutdown()) {
            this.pushExecutor.execute(namedRunnable);
        }
    }

    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener() {
            /* class okhttp3.internal.http2.Http2Connection.Listener.AnonymousClass1 */

            public void onStream(Http2Stream stream) throws IOException {
                stream.close(ErrorCode.REFUSED_STREAM);
            }
        };

        public abstract void onStream(Http2Stream http2Stream) throws IOException;

        public void onSettings(Http2Connection connection) {
        }
    }
}
