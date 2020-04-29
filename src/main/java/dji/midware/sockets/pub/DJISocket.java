package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.util.WifiStateUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EXClassNullAway
public abstract class DJISocket implements DJIServiceInterface {
    protected static final Object LOCK = new Object();
    private static final int TYPE_READ_ANY = 0;
    private static final int TYPE_READ_FULL = 1;
    private static final int TYPE_READ_MIN = 2;
    protected static final int TYPE_READ_NEWMIN = 3;
    protected String TAG = getClass().getSimpleName();
    protected InetSocketAddress address;
    protected byte[] buffer = null;
    protected InputStream input;
    protected String ip;
    protected volatile boolean isConnected = false;
    protected boolean isConnecting = false;
    boolean isServer = false;
    protected String localIp;
    protected int localPort;
    protected int mBufferType = 0;
    protected int mMinBufferLength = 1024;
    public volatile long mRecvLength = 0;
    protected OutputStream out;
    protected int port;
    private Thread receiveThread;
    private Runnable runnable = new Runnable() {
        /* class dji.midware.sockets.pub.DJISocket.AnonymousClass2 */

        /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x0070=Splitter:B:22:0x0070, B:39:0x00f7=Splitter:B:39:0x00f7} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r14 = this;
                r6 = 0
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                byte[] r8 = r9.buffer     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r4 = r8.length     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r5 = r9.mMinBufferLength     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r9 = r9.mBufferType     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                switch(r9) {
                    case 0: goto L_0x019b;
                    case 1: goto L_0x006f;
                    case 2: goto L_0x00f6;
                    case 3: goto L_0x0138;
                    default: goto L_0x0011;
                }
            L_0x0011:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this
                java.net.Socket r9 = r9.socket
                if (r9 == 0) goto L_0x0066
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this
                boolean r9 = r9.isServer
                if (r9 != 0) goto L_0x0066
                java.lang.Object r10 = dji.midware.sockets.pub.DJISocket.LOCK
                monitor-enter(r10)
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ Exception -> 0x01ca }
                java.net.Socket r9 = r9.socket     // Catch:{ Exception -> 0x01ca }
                if (r9 == 0) goto L_0x002d
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ Exception -> 0x01ca }
                java.net.Socket r9 = r9.socket     // Catch:{ Exception -> 0x01ca }
                r9.close()     // Catch:{ Exception -> 0x01ca }
            L_0x002d:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ all -> 0x01d0 }
                r11 = 0
                r9.socket = r11     // Catch:{ all -> 0x01d0 }
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ all -> 0x01d0 }
                java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d0 }
                r11.<init>()     // Catch:{ all -> 0x01d0 }
                java.lang.String r12 = "tcp 连接断开 by receive ip:"
                java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x01d0 }
                dji.midware.sockets.pub.DJISocket r12 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ all -> 0x01d0 }
                java.lang.String r12 = r12.ip     // Catch:{ all -> 0x01d0 }
                java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x01d0 }
                java.lang.String r12 = "port:"
                java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x01d0 }
                dji.midware.sockets.pub.DJISocket r12 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ all -> 0x01d0 }
                int r12 = r12.port     // Catch:{ all -> 0x01d0 }
                java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x01d0 }
                java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x01d0 }
                r9.LOGE(r11)     // Catch:{ all -> 0x01d0 }
                if (r6 == 0) goto L_0x0065
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ all -> 0x01d0 }
                r9.onDisconnect()     // Catch:{ all -> 0x01d0 }
            L_0x0065:
                monitor-exit(r10)     // Catch:{ all -> 0x01d0 }
            L_0x0066:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this
                java.lang.String r10 = "socket recv thread over"
                r9.LOGE(r10)
                return
            L_0x006f:
                r7 = 0
            L_0x0070:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                boolean r9 = r9.isConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 == 0) goto L_0x0011
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 != 0) goto L_0x00ac
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.lang.String r10 = "cmd buffer input null"
                r9.LOGD(r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x0070
            L_0x0087:
                r2 = move-exception
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this
                java.lang.StringBuilder r10 = new java.lang.StringBuilder
                r10.<init>()
                java.lang.String r11 = ""
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r11 = r2.getMessage()
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r10 = r10.toString()
                r9.LOGE(r10)
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this
                r9.sendError()
                goto L_0x0011
            L_0x00ac:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r10 = r4 - r7
                int r9 = r9.read(r8, r7, r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r7 = r7 + r9
                if (r7 <= 0) goto L_0x00bf
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r9.notifyConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r6 = 1
            L_0x00bf:
                if (r7 != r4) goto L_0x0070
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r10 = 0
                r9.parse(r8, r10, r7)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r10 = r9.mRecvLength     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r12 = (long) r7     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r10 = r10 + r12
                r9.mRecvLength = r10     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r7 = 0
                r10 = 1
                java.lang.Thread.sleep(r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x0070
            L_0x00d6:
                r2 = move-exception
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this
                java.lang.StringBuilder r10 = new java.lang.StringBuilder
                r10.<init>()
                java.lang.String r11 = ""
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r11 = r2.getMessage()
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r10 = r10.toString()
                r9.LOGE(r10)
                goto L_0x0011
            L_0x00f6:
                r7 = 0
            L_0x00f7:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                boolean r9 = r9.isConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 == 0) goto L_0x0011
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 != 0) goto L_0x010e
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.lang.String r10 = "cmd buffer input null"
                r9.LOGD(r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x00f7
            L_0x010e:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r10 = r4 - r7
                int r9 = r9.read(r8, r7, r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r7 = r7 + r9
                if (r7 <= 0) goto L_0x0121
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r9.notifyConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r6 = 1
            L_0x0121:
                if (r7 < r5) goto L_0x00f7
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r10 = 0
                r9.parse(r8, r10, r7)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r10 = r9.mRecvLength     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r12 = (long) r7     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r10 = r10 + r12
                r9.mRecvLength = r10     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r7 = 0
                r10 = 1
                java.lang.Thread.sleep(r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x00f7
            L_0x0138:
                r7 = 0
                r0 = 0
                r1 = 0
            L_0x013b:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                boolean r9 = r9.isConnected     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 != 0) goto L_0x0149
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                boolean r9 = r9.isConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 == 0) goto L_0x0011
            L_0x0149:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 != 0) goto L_0x0158
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.lang.String r10 = "cmd buffer input null"
                r9.LOGD(r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x013b
            L_0x0158:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r0 = r9.available()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r0 <= r5) goto L_0x0192
                int r1 = r4 - r7
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r10 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r0 <= r1) goto L_0x0190
                r9 = r1
            L_0x016b:
                int r9 = r10.read(r8, r7, r9)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r7 = r7 + r9
                if (r7 <= 0) goto L_0x017e
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                boolean r9 = r9.isConnected     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 != 0) goto L_0x017d
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r9.notifyConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
            L_0x017d:
                r6 = 1
            L_0x017e:
                if (r7 < r5) goto L_0x013b
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r10 = 0
                r9.parse(r8, r10, r7)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r10 = r9.mRecvLength     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r12 = (long) r7     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                long r10 = r10 + r12
                r9.mRecvLength = r10     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r7 = 0
                goto L_0x013b
            L_0x0190:
                r9 = r0
                goto L_0x016b
            L_0x0192:
                r10 = 0
                r9 = 200000(0x30d40, float:2.8026E-40)
                java.lang.Thread.sleep(r10, r9)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x013b
            L_0x019b:
                r7 = 0
            L_0x019c:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                boolean r9 = r9.isConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 == 0) goto L_0x0011
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r9 != 0) goto L_0x01b3
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.lang.String r10 = "cmd buffer input null"
                r9.LOGD(r10)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x019c
            L_0x01b3:
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                java.io.InputStream r9 = r9.input     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                int r7 = r9.read(r8)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                if (r7 <= 0) goto L_0x019c
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r9.notifyConnected()     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r6 = 1
                dji.midware.sockets.pub.DJISocket r9 = dji.midware.sockets.pub.DJISocket.this     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                r10 = 0
                r9.parse(r8, r10, r7)     // Catch:{ IOException -> 0x0087, Exception -> 0x00d6 }
                goto L_0x019c
            L_0x01ca:
                r3 = move-exception
                r3.printStackTrace()     // Catch:{ all -> 0x01d0 }
                goto L_0x002d
            L_0x01d0:
                r9 = move-exception
                monitor-exit(r10)     // Catch:{ all -> 0x01d0 }
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.midware.sockets.pub.DJISocket.AnonymousClass2.run():void");
        }
    };
    private ExecutorService sendThreadPool;
    protected Socket socket;

    public abstract void LOGD(String str);

    public abstract void LOGE(String str);

    /* access modifiers changed from: protected */
    public abstract void connect();

    /* access modifiers changed from: protected */
    public abstract boolean getHeartStatus();

    /* access modifiers changed from: protected */
    public abstract void parse(byte[] bArr, int i, int i2);

    /* access modifiers changed from: protected */
    public abstract void resetHeartStatus();

    /* access modifiers changed from: protected */
    public abstract void sendError();

    /* access modifiers changed from: protected */
    public abstract void startTimers();

    public DJISocket(String ip2, int port2, String localIp2, int localPort2) {
        this.ip = ip2;
        this.port = port2;
        this.localIp = localIp2;
        this.localPort = localPort2;
        this.sendThreadPool = Executors.newSingleThreadExecutor();
        this.address = new InetSocketAddress(ip2, port2);
        initBufferAttrs();
        this.receiveThread = new Thread(this.runnable);
        startTimers();
    }

    /* access modifiers changed from: protected */
    public boolean canDo() {
        if (ServiceManager.getContext() == null || !WifiStateUtil.isWifiActive(ServiceManager.getContext())) {
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        try {
            if (this.socket == null || this.socket.isClosed() || !this.socket.isConnected()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendmessage(final SendPack data) {
        if (this.sendThreadPool != null && !this.sendThreadPool.isShutdown() && !this.sendThreadPool.isTerminated()) {
            this.sendThreadPool.execute(new Runnable() {
                /* class dji.midware.sockets.pub.DJISocket.AnonymousClass1 */

                public void run() {
                    if (!DJISocket.this.isConnected() || DJISocket.this.out == null) {
                        data.bufferObject.noUsed();
                        return;
                    }
                    try {
                        DJISocket.this.out.write(data.buffer, 0, data.getLength());
                        DJISocket.this.out.flush();
                    } catch (IOException e) {
                        DJISocket.this.LOGE("" + e.getMessage());
                        DJISocket.this.sendError();
                    } catch (Exception e2) {
                        DJISocket.this.LOGE("" + e2.getMessage());
                        e2.printStackTrace();
                    }
                    data.bufferObject.noUsed();
                }
            });
        }
    }

    public void destroy() {
        LOGE("djisocket destroy");
        if (this.sendThreadPool != null) {
            this.sendThreadPool.shutdown();
            this.sendThreadPool = null;
        }
        closeSocket();
    }

    /* access modifiers changed from: protected */
    public void closeSocket() {
        if (this.input != null) {
            try {
                this.input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.out != null) {
            try {
                this.out.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        synchronized (LOCK) {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void checkReceiveThread() {
        if (this.receiveThread == null) {
            receiveMessage();
        } else if (!this.receiveThread.isAlive() && isConnected()) {
            LOGD("receiveThread restart");
            this.receiveThread.interrupt();
            this.receiveThread = null;
            receiveMessage();
        }
    }

    private void receiveMessage() {
        this.receiveThread = new Thread(this.runnable);
        this.receiveThread.setPriority(9);
        this.receiveThread.start();
    }

    /* access modifiers changed from: protected */
    public void notifyConnected() {
    }

    /* access modifiers changed from: protected */
    public void initBufferAttrs() {
        this.mBufferType = 0;
        this.buffer = new byte[this.mMinBufferLength];
    }

    public boolean isOK() {
        return isConnected() && getHeartStatus();
    }
}
