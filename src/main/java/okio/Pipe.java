package okio;

import java.io.IOException;

public final class Pipe {
    final Buffer buffer = new Buffer();
    final long maxBufferSize;
    private final Sink sink = new PipeSink();
    boolean sinkClosed;
    private final Source source = new PipeSource();
    boolean sourceClosed;

    public Pipe(long maxBufferSize2) {
        if (maxBufferSize2 < 1) {
            throw new IllegalArgumentException("maxBufferSize < 1: " + maxBufferSize2);
        }
        this.maxBufferSize = maxBufferSize2;
    }

    public final Source source() {
        return this.source;
    }

    public final Sink sink() {
        return this.sink;
    }

    final class PipeSink implements Sink {
        final Timeout timeout = new Timeout();

        PipeSink() {
        }

        /* JADX WARNING: CFG modification limit reached, blocks count: 127 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void write(okio.Buffer r13, long r14) throws java.io.IOException {
            /*
                r12 = this;
                r10 = 0
                okio.Pipe r4 = okio.Pipe.this
                okio.Buffer r5 = r4.buffer
                monitor-enter(r5)
                okio.Pipe r4 = okio.Pipe.this     // Catch:{ all -> 0x0016 }
                boolean r4 = r4.sinkClosed     // Catch:{ all -> 0x0016 }
                if (r4 == 0) goto L_0x0034
                java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0016 }
                java.lang.String r6 = "closed"
                r4.<init>(r6)     // Catch:{ all -> 0x0016 }
                throw r4     // Catch:{ all -> 0x0016 }
            L_0x0016:
                r4 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0016 }
                throw r4
            L_0x0019:
                okio.Pipe r4 = okio.Pipe.this     // Catch:{ all -> 0x0016 }
                long r6 = r4.maxBufferSize     // Catch:{ all -> 0x0016 }
                okio.Pipe r4 = okio.Pipe.this     // Catch:{ all -> 0x0016 }
                okio.Buffer r4 = r4.buffer     // Catch:{ all -> 0x0016 }
                long r8 = r4.size()     // Catch:{ all -> 0x0016 }
                long r0 = r6 - r8
                int r4 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
                if (r4 != 0) goto L_0x0047
                okio.Timeout r4 = r12.timeout     // Catch:{ all -> 0x0016 }
                okio.Pipe r6 = okio.Pipe.this     // Catch:{ all -> 0x0016 }
                okio.Buffer r6 = r6.buffer     // Catch:{ all -> 0x0016 }
                r4.waitUntilNotified(r6)     // Catch:{ all -> 0x0016 }
            L_0x0034:
                int r4 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
                if (r4 <= 0) goto L_0x005b
                okio.Pipe r4 = okio.Pipe.this     // Catch:{ all -> 0x0016 }
                boolean r4 = r4.sourceClosed     // Catch:{ all -> 0x0016 }
                if (r4 == 0) goto L_0x0019
                java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x0016 }
                java.lang.String r6 = "source is closed"
                r4.<init>(r6)     // Catch:{ all -> 0x0016 }
                throw r4     // Catch:{ all -> 0x0016 }
            L_0x0047:
                long r2 = java.lang.Math.min(r0, r14)     // Catch:{ all -> 0x0016 }
                okio.Pipe r4 = okio.Pipe.this     // Catch:{ all -> 0x0016 }
                okio.Buffer r4 = r4.buffer     // Catch:{ all -> 0x0016 }
                r4.write(r13, r2)     // Catch:{ all -> 0x0016 }
                long r14 = r14 - r2
                okio.Pipe r4 = okio.Pipe.this     // Catch:{ all -> 0x0016 }
                okio.Buffer r4 = r4.buffer     // Catch:{ all -> 0x0016 }
                r4.notifyAll()     // Catch:{ all -> 0x0016 }
                goto L_0x0034
            L_0x005b:
                monitor-exit(r5)     // Catch:{ all -> 0x0016 }
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: okio.Pipe.PipeSink.write(okio.Buffer, long):void");
        }

        public void flush() throws IOException {
            synchronized (Pipe.this.buffer) {
                if (Pipe.this.sinkClosed) {
                    throw new IllegalStateException("closed");
                } else if (Pipe.this.sourceClosed && Pipe.this.buffer.size() > 0) {
                    throw new IOException("source is closed");
                }
            }
        }

        public void close() throws IOException {
            synchronized (Pipe.this.buffer) {
                if (!Pipe.this.sinkClosed) {
                    if (!Pipe.this.sourceClosed || Pipe.this.buffer.size() <= 0) {
                        Pipe.this.sinkClosed = true;
                        Pipe.this.buffer.notifyAll();
                        return;
                    }
                    throw new IOException("source is closed");
                }
            }
        }

        public Timeout timeout() {
            return this.timeout;
        }
    }

    final class PipeSource implements Source {
        final Timeout timeout = new Timeout();

        PipeSource() {
        }

        public long read(Buffer sink, long byteCount) throws IOException {
            long read;
            synchronized (Pipe.this.buffer) {
                if (!Pipe.this.sourceClosed) {
                    while (true) {
                        if (Pipe.this.buffer.size() != 0) {
                            read = Pipe.this.buffer.read(sink, byteCount);
                            Pipe.this.buffer.notifyAll();
                            break;
                        } else if (Pipe.this.sinkClosed) {
                            read = -1;
                            break;
                        } else {
                            this.timeout.waitUntilNotified(Pipe.this.buffer);
                        }
                    }
                } else {
                    throw new IllegalStateException("closed");
                }
            }
            return read;
        }

        public void close() throws IOException {
            synchronized (Pipe.this.buffer) {
                Pipe.this.sourceClosed = true;
                Pipe.this.buffer.notifyAll();
            }
        }

        public Timeout timeout() {
            return this.timeout;
        }
    }
}
