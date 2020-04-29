package com.loc;

import android.text.TextUtils;
import com.loc.bg;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

/* compiled from: HttpUrlUtil */
public final class bi {
    private int a;
    private int b;
    private boolean c;
    private SSLContext d;
    private Proxy e;
    private volatile boolean f;
    private long g;
    private long h;
    private String i;
    private a j;
    private bg.a k;

    /* compiled from: HttpUrlUtil */
    private static class a {
        private Vector<b> a;
        private volatile b b;

        private a() {
            this.a = new Vector<>();
            this.b = new b((byte) 0);
        }

        /* synthetic */ a(byte b2) {
            this();
        }

        public final b a() {
            return this.b;
        }

        public final b a(String str) {
            if (TextUtils.isEmpty(str)) {
                return this.b;
            }
            for (int i = 0; i < this.a.size(); i++) {
                b bVar = this.a.get(i);
                if (bVar != null && bVar.a().equals(str)) {
                    return bVar;
                }
            }
            b bVar2 = new b((byte) 0);
            bVar2.b(str);
            this.a.add(bVar2);
            return bVar2;
        }

        public final void b(String str) {
            if (!TextUtils.isEmpty(str)) {
                this.b.a(str);
            }
        }
    }

    /* compiled from: HttpUrlUtil */
    private static class b implements HostnameVerifier {
        private String a;
        private String b;

        private b() {
        }

        /* synthetic */ b(byte b2) {
            this();
        }

        public final String a() {
            return this.b;
        }

        public final void a(String str) {
            this.a = str;
        }

        public final void b(String str) {
            this.b = str;
        }

        public final boolean verify(String str, SSLSession sSLSession) {
            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            return !TextUtils.isEmpty(this.a) ? this.a.equals(str) : !TextUtils.isEmpty(this.b) ? defaultHostnameVerifier.verify(this.b, sSLSession) : defaultHostnameVerifier.verify(str, sSLSession);
        }
    }

    bi(int i2, int i3, Proxy proxy, boolean z) {
        this(i2, i3, proxy, z, (byte) 0);
    }

    private bi(int i2, int i3, Proxy proxy, boolean z, byte b2) {
        this.f = false;
        this.g = -1;
        this.h = 0;
        this.a = i2;
        this.b = i3;
        this.e = proxy;
        this.c = z.a().b(z);
        if (z.b()) {
            this.c = false;
        }
        this.k = null;
        try {
            this.i = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        } catch (Throwable th) {
            an.a(th, "ht", "ic");
        }
        if (this.c) {
            try {
                SSLContext instance = SSLContext.getInstance("TLS");
                instance.init(null, null, null);
                this.d = instance;
            } catch (Throwable th2) {
                an.a(th2, "ht", "ne");
            }
        }
        this.j = new a((byte) 0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0101, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0102, code lost:
        com.loc.an.a(r2, "ht", "par");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x010d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x010e, code lost:
        com.loc.an.a(r2, "ht", "par");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0119, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x011a, code lost:
        com.loc.an.a(r1, "ht", "par");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0125, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0126, code lost:
        com.loc.an.a(r1, "ht", "par");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x015d, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x015e, code lost:
        r1 = null;
        r4 = null;
        r5 = null;
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0178, code lost:
        r0 = "";
        r2 = null;
        r4 = null;
        r5 = null;
        r6 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x007f A[SYNTHETIC, Splitter:B:22:0x007f] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0084 A[SYNTHETIC, Splitter:B:25:0x0084] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0089 A[SYNTHETIC, Splitter:B:28:0x0089] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x008e A[SYNTHETIC, Splitter:B:31:0x008e] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x015d A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x0004] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.loc.bk a(java.net.HttpURLConnection r10, boolean r11) throws com.loc.t, java.io.IOException {
        /*
            r9 = this;
            r3 = 0
            java.lang.String r1 = ""
            r10.connect()     // Catch:{ IOException -> 0x0177, all -> 0x015d }
            java.util.Map r4 = r10.getHeaderFields()     // Catch:{ IOException -> 0x0177, all -> 0x015d }
            int r2 = r10.getResponseCode()     // Catch:{ IOException -> 0x0177, all -> 0x015d }
            if (r4 == 0) goto L_0x018f
            java.lang.String r0 = "gsid"
            java.lang.Object r0 = r4.get(r0)     // Catch:{ IOException -> 0x0177, all -> 0x015d }
            java.util.List r0 = (java.util.List) r0     // Catch:{ IOException -> 0x0177, all -> 0x015d }
            if (r0 == 0) goto L_0x018f
            int r5 = r0.size()     // Catch:{ IOException -> 0x0177, all -> 0x015d }
            if (r5 <= 0) goto L_0x018f
            r5 = 0
            java.lang.Object r0 = r0.get(r5)     // Catch:{ IOException -> 0x0177, all -> 0x015d }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ IOException -> 0x0177, all -> 0x015d }
        L_0x0029:
            r1 = 200(0xc8, float:2.8E-43)
            if (r2 == r1) goto L_0x0092
            com.loc.t r1 = new com.loc.t     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.String r5 = "网络异常原因："
            r4.<init>(r5)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.String r5 = r10.getResponseMessage()     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.String r5 = " 网络异常状态码："
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.String r5 = "  "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.String r5 = " "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.String r5 = r9.i     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            r1.<init>(r4, r0)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            r1.a(r2)     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            throw r1     // Catch:{ IOException -> 0x006d, all -> 0x015d }
        L_0x006d:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r3
            r6 = r3
        L_0x0072:
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x007b }
            java.lang.String r3 = "IO 操作异常 - IOException"
            r1.<init>(r3, r0)     // Catch:{ all -> 0x007b }
            throw r1     // Catch:{ all -> 0x007b }
        L_0x007b:
            r0 = move-exception
            r1 = r2
        L_0x007d:
            if (r6 == 0) goto L_0x0082
            r6.close()     // Catch:{ Throwable -> 0x0101 }
        L_0x0082:
            if (r5 == 0) goto L_0x0087
            r5.close()     // Catch:{ Throwable -> 0x010d }
        L_0x0087:
            if (r1 == 0) goto L_0x008c
            r1.close()     // Catch:{ Throwable -> 0x0119 }
        L_0x008c:
            if (r4 == 0) goto L_0x0091
            r4.close()     // Catch:{ Throwable -> 0x0125 }
        L_0x0091:
            throw r0
        L_0x0092:
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            r6.<init>()     // Catch:{ IOException -> 0x006d, all -> 0x015d }
            java.io.InputStream r5 = r10.getInputStream()     // Catch:{ IOException -> 0x017f, all -> 0x0164 }
            java.io.PushbackInputStream r1 = new java.io.PushbackInputStream     // Catch:{ IOException -> 0x0185, all -> 0x016a }
            r2 = 2
            r1.<init>(r5, r2)     // Catch:{ IOException -> 0x0185, all -> 0x016a }
            r2 = 2
            byte[] r2 = new byte[r2]     // Catch:{ IOException -> 0x018a, all -> 0x016f }
            r1.read(r2)     // Catch:{ IOException -> 0x018a, all -> 0x016f }
            r1.unread(r2)     // Catch:{ IOException -> 0x018a, all -> 0x016f }
            r7 = 0
            byte r7 = r2[r7]     // Catch:{ IOException -> 0x018a, all -> 0x016f }
            r8 = 31
            if (r7 != r8) goto L_0x00d4
            r7 = 1
            byte r2 = r2[r7]     // Catch:{ IOException -> 0x018a, all -> 0x016f }
            r7 = -117(0xffffffffffffff8b, float:NaN)
            if (r2 != r7) goto L_0x00d4
            if (r11 != 0) goto L_0x00d4
            java.util.zip.GZIPInputStream r2 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x018a, all -> 0x016f }
            r2.<init>(r1)     // Catch:{ IOException -> 0x018a, all -> 0x016f }
            r3 = r2
        L_0x00c0:
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
        L_0x00c4:
            int r7 = r3.read(r2)     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            r8 = -1
            if (r7 == r8) goto L_0x00d6
            r8 = 0
            r6.write(r2, r8, r7)     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            goto L_0x00c4
        L_0x00d0:
            r2 = move-exception
            r2 = r1
            r4 = r3
            goto L_0x0072
        L_0x00d4:
            r3 = r1
            goto L_0x00c0
        L_0x00d6:
            com.loc.aq.c()     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            com.loc.bk r2 = new com.loc.bk     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            r2.<init>()     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            byte[] r7 = r6.toByteArray()     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            r2.a = r7     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            r2.b = r4     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            java.lang.String r4 = r9.i     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            r2.c = r4     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            r2.d = r0     // Catch:{ IOException -> 0x00d0, all -> 0x0173 }
            if (r6 == 0) goto L_0x00f1
            r6.close()     // Catch:{ Throwable -> 0x0131 }
        L_0x00f1:
            if (r5 == 0) goto L_0x00f6
            r5.close()     // Catch:{ Throwable -> 0x013c }
        L_0x00f6:
            if (r1 == 0) goto L_0x00fb
            r1.close()     // Catch:{ Throwable -> 0x0147 }
        L_0x00fb:
            if (r3 == 0) goto L_0x0100
            r3.close()     // Catch:{ Throwable -> 0x0152 }
        L_0x0100:
            return r2
        L_0x0101:
            r2 = move-exception
            java.lang.String r3 = "ht"
            java.lang.String r6 = "par"
            com.loc.an.a(r2, r3, r6)
            goto L_0x0082
        L_0x010d:
            r2 = move-exception
            java.lang.String r3 = "ht"
            java.lang.String r5 = "par"
            com.loc.an.a(r2, r3, r5)
            goto L_0x0087
        L_0x0119:
            r1 = move-exception
            java.lang.String r2 = "ht"
            java.lang.String r3 = "par"
            com.loc.an.a(r1, r2, r3)
            goto L_0x008c
        L_0x0125:
            r1 = move-exception
            java.lang.String r2 = "ht"
            java.lang.String r3 = "par"
            com.loc.an.a(r1, r2, r3)
            goto L_0x0091
        L_0x0131:
            r0 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r6 = "par"
            com.loc.an.a(r0, r4, r6)
            goto L_0x00f1
        L_0x013c:
            r0 = move-exception
            java.lang.String r4 = "ht"
            java.lang.String r5 = "par"
            com.loc.an.a(r0, r4, r5)
            goto L_0x00f6
        L_0x0147:
            r0 = move-exception
            java.lang.String r1 = "ht"
            java.lang.String r4 = "par"
            com.loc.an.a(r0, r1, r4)
            goto L_0x00fb
        L_0x0152:
            r0 = move-exception
            java.lang.String r1 = "ht"
            java.lang.String r3 = "par"
            com.loc.an.a(r0, r1, r3)
            goto L_0x0100
        L_0x015d:
            r0 = move-exception
            r1 = r3
            r4 = r3
            r5 = r3
            r6 = r3
            goto L_0x007d
        L_0x0164:
            r0 = move-exception
            r1 = r3
            r4 = r3
            r5 = r3
            goto L_0x007d
        L_0x016a:
            r0 = move-exception
            r1 = r3
            r4 = r3
            goto L_0x007d
        L_0x016f:
            r0 = move-exception
            r4 = r3
            goto L_0x007d
        L_0x0173:
            r0 = move-exception
            r4 = r3
            goto L_0x007d
        L_0x0177:
            r0 = move-exception
            r0 = r1
            r2 = r3
            r4 = r3
            r5 = r3
            r6 = r3
            goto L_0x0072
        L_0x017f:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r3
            goto L_0x0072
        L_0x0185:
            r1 = move-exception
            r2 = r3
            r4 = r3
            goto L_0x0072
        L_0x018a:
            r2 = move-exception
            r2 = r1
            r4 = r3
            goto L_0x0072
        L_0x018f:
            r0 = r1
            goto L_0x0029
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bi.a(java.net.HttpURLConnection, boolean):com.loc.bk");
    }

    static String a(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            String str2 = (String) entry.getValue();
            if (str2 == null) {
                str2 = "";
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(str));
            sb.append("=");
            sb.append(URLEncoder.encode(str2));
        }
        return sb.toString();
    }

    private void a(Map<String, String> map, HttpURLConnection httpURLConnection) {
        if (map != null) {
            for (String str : map.keySet()) {
                httpURLConnection.addRequestProperty(str, map.get(str));
            }
        }
        try {
            httpURLConnection.addRequestProperty("csid", this.i);
        } catch (Throwable th) {
            an.a(th, "ht", "adh");
        }
        httpURLConnection.setConnectTimeout(this.a);
        httpURLConnection.setReadTimeout(this.b);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x019d  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x01a0  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002f A[Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163, all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005c A[Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163, all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0069 A[Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163, all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0071 A[Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163, all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x007f A[Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163, all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00c0 A[SYNTHETIC, Splitter:B:41:0x00c0] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00d8 A[SYNTHETIC, Splitter:B:47:0x00d8] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00e6 A[Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163, all -> 0x0118 }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0107 A[SYNTHETIC, Splitter:B:65:0x0107] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.loc.bk a(java.lang.String r8, boolean r9, java.lang.String r10, java.util.Map<java.lang.String, java.lang.String> r11, byte[] r12, boolean r13) throws com.loc.t {
        /*
            r7 = this;
            r3 = 0
            com.loc.x.c()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r11 != 0) goto L_0x000b
            java.util.HashMap r11 = new java.util.HashMap     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r11.<init>()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x000b:
            com.loc.bi$a r1 = r7.j     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            com.loc.bi$b r1 = r1.a()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r9 == 0) goto L_0x01a3
            boolean r2 = android.text.TextUtils.isEmpty(r10)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r2 != 0) goto L_0x01a3
            com.loc.bi$a r1 = r7.j     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            com.loc.bi$b r1 = r1.a(r10)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r4 = r1
        L_0x0020:
            int r2 = com.loc.bg.a     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            java.lang.String r1 = ""
            switch(r2) {
                case 1: goto L_0x00dc;
                default: goto L_0x0028;
            }     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x0028:
            r2 = r1
        L_0x0029:
            boolean r1 = android.text.TextUtils.isEmpty(r2)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r1 != 0) goto L_0x01a0
            android.net.Uri r1 = android.net.Uri.parse(r8)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            java.lang.String r5 = r1.getHost()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            android.net.Uri$Builder r1 = r1.buildUpon()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            android.net.Uri$Builder r1 = r1.encodedAuthority(r2)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            android.net.Uri r1 = r1.build()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            java.lang.String r1 = r1.toString()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r11 == 0) goto L_0x004f
            java.lang.String r6 = "targetHost"
            r11.put(r6, r5)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x004f:
            boolean r5 = r7.c     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r5 == 0) goto L_0x0058
            com.loc.bi$a r5 = r7.j     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r5.b(r2)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x0058:
            boolean r2 = r7.c     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r2 == 0) goto L_0x0060
            java.lang.String r1 = com.loc.z.a(r1)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x0060:
            java.net.URL r2 = new java.net.URL     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r2.<init>(r1)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            com.loc.bg$a r1 = r7.k     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r1 == 0) goto L_0x019d
            com.loc.bg$a r1 = r7.k     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            java.net.URLConnection r1 = r1.a()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x006f:
            if (r1 != 0) goto L_0x007b
            java.net.Proxy r1 = r7.e     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r1 == 0) goto L_0x00e1
            java.net.Proxy r1 = r7.e     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            java.net.URLConnection r1 = r2.openConnection(r1)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x007b:
            boolean r2 = r7.c     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r2 == 0) goto L_0x00e6
            javax.net.ssl.HttpsURLConnection r1 = (javax.net.ssl.HttpsURLConnection) r1     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r0 = r1
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r2 = r0
            javax.net.ssl.SSLContext r5 = r7.d     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            javax.net.ssl.SSLSocketFactory r5 = r5.getSocketFactory()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r2.setSSLSocketFactory(r5)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r0 = r1
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r2 = r0
            r2.setHostnameVerifier(r4)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r2 = r1
        L_0x0096:
            java.lang.String r1 = android.os.Build.VERSION.SDK     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r1 == 0) goto L_0x00a9
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r4 = 13
            if (r1 <= r4) goto L_0x00a9
            java.lang.String r1 = "Connection"
            java.lang.String r4 = "close"
            r2.setRequestProperty(r1, r4)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
        L_0x00a9:
            r7.a(r11, r2)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            java.lang.String r1 = "POST"
            r2.setRequestMethod(r1)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r1 = 0
            r2.setUseCaches(r1)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r1 = 1
            r2.setDoInput(r1)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r1 = 1
            r2.setDoOutput(r1)     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            if (r12 == 0) goto L_0x00d2
            int r1 = r12.length     // Catch:{ ConnectException -> 0x019a, MalformedURLException -> 0x0196, UnknownHostException -> 0x0193, SocketException -> 0x0190, SocketTimeoutException -> 0x018d, InterruptedIOException -> 0x018a, IOException -> 0x0187, t -> 0x0184, Throwable -> 0x0181 }
            if (r1 <= 0) goto L_0x00d2
            java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ ConnectException -> 0x019a, MalformedURLException -> 0x0196, UnknownHostException -> 0x0193, SocketException -> 0x0190, SocketTimeoutException -> 0x018d, InterruptedIOException -> 0x018a, IOException -> 0x0187, t -> 0x0184, Throwable -> 0x0181 }
            java.io.OutputStream r3 = r2.getOutputStream()     // Catch:{ ConnectException -> 0x019a, MalformedURLException -> 0x0196, UnknownHostException -> 0x0193, SocketException -> 0x0190, SocketTimeoutException -> 0x018d, InterruptedIOException -> 0x018a, IOException -> 0x0187, t -> 0x0184, Throwable -> 0x0181 }
            r1.<init>(r3)     // Catch:{ ConnectException -> 0x019a, MalformedURLException -> 0x0196, UnknownHostException -> 0x0193, SocketException -> 0x0190, SocketTimeoutException -> 0x018d, InterruptedIOException -> 0x018a, IOException -> 0x0187, t -> 0x0184, Throwable -> 0x0181 }
            r1.write(r12)     // Catch:{ ConnectException -> 0x019a, MalformedURLException -> 0x0196, UnknownHostException -> 0x0193, SocketException -> 0x0190, SocketTimeoutException -> 0x018d, InterruptedIOException -> 0x018a, IOException -> 0x0187, t -> 0x0184, Throwable -> 0x0181 }
            r1.close()     // Catch:{ ConnectException -> 0x019a, MalformedURLException -> 0x0196, UnknownHostException -> 0x0193, SocketException -> 0x0190, SocketTimeoutException -> 0x018d, InterruptedIOException -> 0x018a, IOException -> 0x0187, t -> 0x0184, Throwable -> 0x0181 }
        L_0x00d2:
            com.loc.bk r1 = r7.a(r2, r13)     // Catch:{ ConnectException -> 0x019a, MalformedURLException -> 0x0196, UnknownHostException -> 0x0193, SocketException -> 0x0190, SocketTimeoutException -> 0x018d, InterruptedIOException -> 0x018a, IOException -> 0x0187, t -> 0x0184, Throwable -> 0x0181 }
            if (r2 == 0) goto L_0x00db
            r2.disconnect()     // Catch:{ Throwable -> 0x00ea }
        L_0x00db:
            return r1
        L_0x00dc:
            java.lang.String r1 = com.loc.bg.b     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r2 = r1
            goto L_0x0029
        L_0x00e1:
            java.net.URLConnection r1 = r2.openConnection()     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            goto L_0x007b
        L_0x00e6:
            java.net.HttpURLConnection r1 = (java.net.HttpURLConnection) r1     // Catch:{ ConnectException -> 0x00f5, MalformedURLException -> 0x010b, UnknownHostException -> 0x011a, SocketException -> 0x0127, SocketTimeoutException -> 0x0134, InterruptedIOException -> 0x0141, IOException -> 0x014b, t -> 0x0158, Throwable -> 0x0163 }
            r2 = r1
            goto L_0x0096
        L_0x00ea:
            r2 = move-exception
            java.lang.String r3 = "ht"
            java.lang.String r4 = "mPt"
            com.loc.an.a(r2, r3, r4)
            goto L_0x00db
        L_0x00f5:
            r1 = move-exception
            r2 = r3
        L_0x00f7:
            r1.printStackTrace()     // Catch:{ all -> 0x0103 }
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0103 }
            java.lang.String r3 = "http连接失败 - ConnectionException"
            r1.<init>(r3)     // Catch:{ all -> 0x0103 }
            throw r1     // Catch:{ all -> 0x0103 }
        L_0x0103:
            r1 = move-exception
            r3 = r2
        L_0x0105:
            if (r3 == 0) goto L_0x010a
            r3.disconnect()     // Catch:{ Throwable -> 0x0176 }
        L_0x010a:
            throw r1
        L_0x010b:
            r1 = move-exception
        L_0x010c:
            r1.printStackTrace()     // Catch:{ all -> 0x0118 }
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0118 }
            java.lang.String r2 = "url异常 - MalformedURLException"
            r1.<init>(r2)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x0118:
            r1 = move-exception
            goto L_0x0105
        L_0x011a:
            r1 = move-exception
        L_0x011b:
            r1.printStackTrace()     // Catch:{ all -> 0x0118 }
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0118 }
            java.lang.String r2 = "未知主机 - UnKnowHostException"
            r1.<init>(r2)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x0127:
            r1 = move-exception
        L_0x0128:
            r1.printStackTrace()     // Catch:{ all -> 0x0118 }
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0118 }
            java.lang.String r2 = "socket 连接异常 - SocketException"
            r1.<init>(r2)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x0134:
            r1 = move-exception
        L_0x0135:
            r1.printStackTrace()     // Catch:{ all -> 0x0118 }
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0118 }
            java.lang.String r2 = "socket 连接超时 - SocketTimeoutException"
            r1.<init>(r2)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x0141:
            r1 = move-exception
        L_0x0142:
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0118 }
            java.lang.String r2 = "未知的错误"
            r1.<init>(r2)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x014b:
            r1 = move-exception
        L_0x014c:
            r1.printStackTrace()     // Catch:{ all -> 0x0118 }
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0118 }
            java.lang.String r2 = "IO 操作异常 - IOException"
            r1.<init>(r2)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x0158:
            r1 = move-exception
        L_0x0159:
            java.lang.String r2 = "ht"
            java.lang.String r4 = "mPt"
            com.loc.an.a(r1, r2, r4)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x0163:
            r1 = move-exception
        L_0x0164:
            java.lang.String r2 = "ht"
            java.lang.String r4 = "mPt"
            com.loc.an.a(r1, r2, r4)     // Catch:{ all -> 0x0118 }
            com.loc.t r1 = new com.loc.t     // Catch:{ all -> 0x0118 }
            java.lang.String r2 = "未知的错误"
            r1.<init>(r2)     // Catch:{ all -> 0x0118 }
            throw r1     // Catch:{ all -> 0x0118 }
        L_0x0176:
            r2 = move-exception
            java.lang.String r3 = "ht"
            java.lang.String r4 = "mPt"
            com.loc.an.a(r2, r3, r4)
            goto L_0x010a
        L_0x0181:
            r1 = move-exception
            r3 = r2
            goto L_0x0164
        L_0x0184:
            r1 = move-exception
            r3 = r2
            goto L_0x0159
        L_0x0187:
            r1 = move-exception
            r3 = r2
            goto L_0x014c
        L_0x018a:
            r1 = move-exception
            r3 = r2
            goto L_0x0142
        L_0x018d:
            r1 = move-exception
            r3 = r2
            goto L_0x0135
        L_0x0190:
            r1 = move-exception
            r3 = r2
            goto L_0x0128
        L_0x0193:
            r1 = move-exception
            r3 = r2
            goto L_0x011b
        L_0x0196:
            r1 = move-exception
            r3 = r2
            goto L_0x010c
        L_0x019a:
            r1 = move-exception
            goto L_0x00f7
        L_0x019d:
            r1 = r3
            goto L_0x006f
        L_0x01a0:
            r1 = r8
            goto L_0x0058
        L_0x01a3:
            r4 = r1
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bi.a(java.lang.String, boolean, java.lang.String, java.util.Map, byte[], boolean):com.loc.bk");
    }
}
