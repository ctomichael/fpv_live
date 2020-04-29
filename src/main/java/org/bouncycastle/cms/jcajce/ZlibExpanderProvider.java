package org.bouncycastle.cms.jcajce;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.InputExpander;
import org.bouncycastle.operator.InputExpanderProvider;

public class ZlibExpanderProvider implements InputExpanderProvider {
    /* access modifiers changed from: private */
    public final long limit;

    private static class LimitedInputStream extends FilterInputStream {
        private long remaining;

        public LimitedInputStream(InputStream inputStream, long j) {
            super(inputStream);
            this.remaining = j;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0019, code lost:
            if (r2 >= 0) goto L_0x001b;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int read() throws java.io.IOException {
            /*
                r8 = this;
                r6 = 0
                long r0 = r8.remaining
                int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
                if (r0 < 0) goto L_0x001c
                java.io.InputStream r0 = r8.in
                int r0 = r0.read()
                if (r0 < 0) goto L_0x001b
                long r2 = r8.remaining
                r4 = 1
                long r2 = r2 - r4
                r8.remaining = r2
                int r1 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r1 < 0) goto L_0x001c
            L_0x001b:
                return r0
            L_0x001c:
                org.bouncycastle.util.io.StreamOverflowException r0 = new org.bouncycastle.util.io.StreamOverflowException
                java.lang.String r1 = "expanded byte limit exceeded"
                r0.<init>(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.cms.jcajce.ZlibExpanderProvider.LimitedInputStream.read():int");
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (i2 < 1) {
                return super.read(bArr, i, i2);
            }
            if (this.remaining < 1) {
                read();
                return -1;
            }
            if (this.remaining <= ((long) i2)) {
                i2 = (int) this.remaining;
            }
            int read = this.in.read(bArr, i, i2);
            if (read <= 0) {
                return read;
            }
            this.remaining -= (long) read;
            return read;
        }
    }

    public ZlibExpanderProvider() {
        this.limit = -1;
    }

    public ZlibExpanderProvider(long j) {
        this.limit = j;
    }

    public InputExpander get(final AlgorithmIdentifier algorithmIdentifier) {
        return new InputExpander() {
            /* class org.bouncycastle.cms.jcajce.ZlibExpanderProvider.AnonymousClass1 */

            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return algorithmIdentifier;
            }

            public InputStream getInputStream(InputStream inputStream) {
                InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);
                return ZlibExpanderProvider.this.limit >= 0 ? new LimitedInputStream(inflaterInputStream, ZlibExpanderProvider.this.limit) : inflaterInputStream;
            }
        };
    }
}
