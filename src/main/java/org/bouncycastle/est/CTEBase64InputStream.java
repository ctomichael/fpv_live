package org.bouncycastle.est;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class CTEBase64InputStream extends InputStream {
    protected final byte[] data = new byte[768];
    protected final OutputStream dataOutputStream;
    protected boolean end;
    protected final Long max;
    protected final byte[] rawBuf = new byte[1024];
    protected long read;
    protected int rp;
    protected final InputStream src;
    protected int wp;

    public CTEBase64InputStream(InputStream inputStream, Long l) {
        this.src = inputStream;
        this.dataOutputStream = new OutputStream() {
            /* class org.bouncycastle.est.CTEBase64InputStream.AnonymousClass1 */

            public void write(int i) throws IOException {
                byte[] bArr = CTEBase64InputStream.this.data;
                CTEBase64InputStream cTEBase64InputStream = CTEBase64InputStream.this;
                int i2 = cTEBase64InputStream.wp;
                cTEBase64InputStream.wp = i2 + 1;
                bArr[i2] = (byte) i;
            }
        };
        this.max = l;
    }

    public void close() throws IOException {
        this.src.close();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0056 A[SYNTHETIC, Splitter:B:20:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int pullFromSrc() throws java.io.IOException {
        /*
            r12 = this;
            r10 = 1
            r8 = 10
            r0 = 0
            r1 = -1
            long r2 = r12.read
            java.lang.Long r4 = r12.max
            long r4 = r4.longValue()
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 < 0) goto L_0x0014
            r0 = r1
        L_0x0013:
            return r0
        L_0x0014:
            java.io.InputStream r2 = r12.src
            int r3 = r2.read()
            r2 = 33
            if (r3 >= r2) goto L_0x0024
            r2 = 13
            if (r3 == r2) goto L_0x0024
            if (r3 != r8) goto L_0x0061
        L_0x0024:
            byte[] r2 = r12.rawBuf
            int r2 = r2.length
            if (r0 < r2) goto L_0x0032
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Content Transfer Encoding, base64 line length > 1024"
            r0.<init>(r1)
            throw r0
        L_0x0032:
            byte[] r4 = r12.rawBuf
            int r2 = r0 + 1
            byte r5 = (byte) r3
            r4[r0] = r5
            long r4 = r12.read
            long r4 = r4 + r10
            r12.read = r4
            r0 = r2
        L_0x003f:
            if (r3 <= r1) goto L_0x0054
            byte[] r2 = r12.rawBuf
            int r2 = r2.length
            if (r0 >= r2) goto L_0x0054
            if (r3 == r8) goto L_0x0054
            long r4 = r12.read
            java.lang.Long r2 = r12.max
            long r6 = r2.longValue()
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 < 0) goto L_0x0014
        L_0x0054:
            if (r0 <= 0) goto L_0x0084
            byte[] r1 = r12.rawBuf     // Catch:{ Exception -> 0x0069 }
            r2 = 0
            java.io.OutputStream r3 = r12.dataOutputStream     // Catch:{ Exception -> 0x0069 }
            org.bouncycastle.util.encoders.Base64.decode(r1, r2, r0, r3)     // Catch:{ Exception -> 0x0069 }
        L_0x005e:
            int r0 = r12.wp
            goto L_0x0013
        L_0x0061:
            if (r3 < 0) goto L_0x003f
            long r4 = r12.read
            long r4 = r4 + r10
            r12.read = r4
            goto L_0x003f
        L_0x0069:
            r0 = move-exception
            java.io.IOException r1 = new java.io.IOException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Decode Base64 Content-Transfer-Encoding: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            throw r1
        L_0x0084:
            if (r3 != r1) goto L_0x005e
            r0 = r1
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.est.CTEBase64InputStream.pullFromSrc():int");
    }

    public int read() throws IOException {
        if (this.rp == this.wp) {
            this.rp = 0;
            this.wp = 0;
            int pullFromSrc = pullFromSrc();
            if (pullFromSrc == -1) {
                return pullFromSrc;
            }
        }
        byte[] bArr = this.data;
        int i = this.rp;
        this.rp = i + 1;
        return bArr[i] & 255;
    }
}
