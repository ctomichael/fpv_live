package org.bouncycastle.est;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.bouncycastle.est.HttpUtil;
import org.bouncycastle.util.Properties;
import org.bouncycastle.util.Strings;

public class ESTResponse {
    private static final Long ZERO = 0L;
    private String HttpVersion;
    private Long absoluteReadLimit;
    /* access modifiers changed from: private */
    public Long contentLength;
    private final HttpUtil.Headers headers;
    private InputStream inputStream;
    private final byte[] lineBuffer;
    private final ESTRequest originalRequest;
    /* access modifiers changed from: private */
    public long read = 0;
    private final Source source;
    private int statusCode;
    private String statusMessage;

    private class PrintingInputStream extends InputStream {
        private final InputStream src;

        private PrintingInputStream(InputStream inputStream) {
            this.src = inputStream;
        }

        public int available() throws IOException {
            return this.src.available();
        }

        public void close() throws IOException {
            this.src.close();
        }

        public int read() throws IOException {
            int read = this.src.read();
            System.out.print(String.valueOf((char) read));
            return read;
        }
    }

    public ESTResponse(ESTRequest eSTRequest, Source source2) throws IOException {
        this.originalRequest = eSTRequest;
        this.source = source2;
        if (source2 instanceof LimitedSource) {
            this.absoluteReadLimit = ((LimitedSource) source2).getAbsoluteReadLimit();
        }
        Set<String> asKeySet = Properties.asKeySet("org.bouncycastle.debug.est");
        if (asKeySet.contains("input") || asKeySet.contains("all")) {
            this.inputStream = new PrintingInputStream(source2.getInputStream());
        } else {
            this.inputStream = source2.getInputStream();
        }
        this.headers = new HttpUtil.Headers();
        this.lineBuffer = new byte[1024];
        process();
    }

    static /* synthetic */ long access$108(ESTResponse eSTResponse) {
        long j = eSTResponse.read;
        eSTResponse.read = 1 + j;
        return j;
    }

    private void process() throws IOException {
        this.HttpVersion = readStringIncluding(' ');
        this.statusCode = Integer.parseInt(readStringIncluding(' '));
        this.statusMessage = readStringIncluding(10);
        String readStringIncluding = readStringIncluding(10);
        while (readStringIncluding.length() > 0) {
            int indexOf = readStringIncluding.indexOf(58);
            if (indexOf > -1) {
                this.headers.add(Strings.toLowerCase(readStringIncluding.substring(0, indexOf).trim()), readStringIncluding.substring(indexOf + 1).trim());
            }
            readStringIncluding = readStringIncluding(10);
        }
        this.contentLength = getContentLength();
        if (this.statusCode == 204 || this.statusCode == 202) {
            if (this.contentLength == null) {
                this.contentLength = 0L;
            } else if (this.statusCode == 204 && this.contentLength.longValue() > 0) {
                throw new IOException("Got HTTP status 204 but Content-length > 0.");
            }
        }
        if (this.contentLength == null) {
            throw new IOException("No Content-length header.");
        }
        if (this.contentLength.equals(ZERO)) {
            this.inputStream = new InputStream() {
                /* class org.bouncycastle.est.ESTResponse.AnonymousClass1 */

                public int read() throws IOException {
                    return -1;
                }
            };
        }
        if (this.contentLength != null) {
            if (this.contentLength.longValue() < 0) {
                throw new IOException("Server returned negative content length: " + this.absoluteReadLimit);
            } else if (this.absoluteReadLimit != null && this.contentLength.longValue() >= this.absoluteReadLimit.longValue()) {
                throw new IOException("Content length longer than absolute read limit: " + this.absoluteReadLimit + " Content-Length: " + this.contentLength);
            }
        }
        this.inputStream = wrapWithCounter(this.inputStream, this.absoluteReadLimit);
        if ("base64".equalsIgnoreCase(getHeader("content-transfer-encoding"))) {
            this.inputStream = new CTEBase64InputStream(this.inputStream, getContentLength());
        }
    }

    public void close() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
        this.source.close();
    }

    public Long getContentLength() {
        String firstValue = this.headers.getFirstValue("Content-Length");
        if (firstValue == null) {
            return null;
        }
        try {
            return Long.valueOf(Long.parseLong(firstValue));
        } catch (RuntimeException e) {
            throw new RuntimeException("Content Length: '" + firstValue + "' invalid. " + e.getMessage());
        }
    }

    public String getHeader(String str) {
        return this.headers.getFirstValue(str);
    }

    public HttpUtil.Headers getHeaders() {
        return this.headers;
    }

    public String getHttpVersion() {
        return this.HttpVersion;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public ESTRequest getOriginalRequest() {
        return this.originalRequest;
    }

    public Source getSource() {
        return this.source;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0038  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String readStringIncluding(char r8) throws java.io.IOException {
        /*
            r7 = this;
            r1 = 0
            r6 = -1
            r0 = r1
        L_0x0003:
            java.io.InputStream r2 = r7.inputStream
            int r3 = r2.read()
            byte[] r4 = r7.lineBuffer
            int r2 = r0 + 1
            byte r5 = (byte) r3
            r4[r0] = r5
            byte[] r0 = r7.lineBuffer
            int r0 = r0.length
            if (r2 < r0) goto L_0x0032
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Server sent line > "
            java.lang.StringBuilder r1 = r1.append(r2)
            byte[] r2 = r7.lineBuffer
            int r2 = r2.length
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0032:
            if (r3 == r8) goto L_0x0036
            if (r3 > r6) goto L_0x004a
        L_0x0036:
            if (r3 != r6) goto L_0x003e
            java.io.EOFException r0 = new java.io.EOFException
            r0.<init>()
            throw r0
        L_0x003e:
            java.lang.String r0 = new java.lang.String
            byte[] r3 = r7.lineBuffer
            r0.<init>(r3, r1, r2)
            java.lang.String r0 = r0.trim()
            return r0
        L_0x004a:
            r0 = r2
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.est.ESTResponse.readStringIncluding(char):java.lang.String");
    }

    /* access modifiers changed from: protected */
    public InputStream wrapWithCounter(final InputStream inputStream2, final Long l) {
        return new InputStream() {
            /* class org.bouncycastle.est.ESTResponse.AnonymousClass2 */

            public void close() throws IOException {
                if (ESTResponse.this.contentLength != null && ESTResponse.this.contentLength.longValue() - 1 > ESTResponse.this.read) {
                    throw new IOException("Stream closed before limit fully read, Read: " + ESTResponse.this.read + " ContentLength: " + ESTResponse.this.contentLength);
                } else if (inputStream2.available() > 0) {
                    throw new IOException("Stream closed with extra content in pipe that exceeds content length.");
                } else {
                    inputStream2.close();
                }
            }

            public int read() throws IOException {
                int read = inputStream2.read();
                if (read > -1) {
                    ESTResponse.access$108(ESTResponse.this);
                    if (l != null && ESTResponse.this.read >= l.longValue()) {
                        throw new IOException("Absolute Read Limit exceeded: " + l);
                    }
                }
                return read;
            }
        };
    }
}
