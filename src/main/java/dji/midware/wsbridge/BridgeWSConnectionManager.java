package dji.midware.wsbridge;

import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.MidWare;
import dji.thirdparty.org.java_websocket.WebSocketImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class BridgeWSConnectionManager {
    private static final String DEFAULT_IP = "10.128.129.92";
    private static final String HOST_PREFIX = "ws://";
    private static final String HOST_SUFFIX = ":9007";
    private static final int MAX_BUFFER_SIZE = 100;
    private static final int TRANSFER_SIZE_WITHOUT_VIDEO = 5;
    private static final int TRANSFER_SIZE_WITH_VIDEO = 2048;
    private ByteArrayOutputStream buffer;
    private InputStream inStream;
    /* access modifiers changed from: private */
    public StringBuilder ipAddress;
    /* access modifiers changed from: private */
    public boolean isReconnecting;
    private OutputStream outStream;
    ScheduledExecutorService reconnectExecutor;
    /* access modifiers changed from: private */
    public DJIWebSocketClient socketClient;

    private BridgeWSConnectionManager() {
        this.buffer = new ByteArrayOutputStream();
        this.outStream = new OutputStream() {
            /* class dji.midware.wsbridge.BridgeWSConnectionManager.AnonymousClass1 */

            public void write(int b) throws IOException {
                if (b != -1) {
                    BridgeWSConnectionManager.this.writeAfterFilter(new byte[]{(byte) b}, 0, 1);
                }
            }

            public void write(byte[] b) throws IOException {
                BridgeWSConnectionManager.this.writeAfterFilter(b, 0, b.length);
            }

            public void write(byte[] b, int off, int len) throws IOException {
                if (len != -1) {
                    BridgeWSConnectionManager.this.writeAfterFilter(b, off, len);
                }
            }
        };
        this.inStream = new InputStream() {
            /* class dji.midware.wsbridge.BridgeWSConnectionManager.AnonymousClass2 */

            public synchronized int read() throws IOException {
                byte b;
                if (BridgeWSConnectionManager.this.isConnected()) {
                    b = BridgeWSConnectionManager.this.socketClient.read().get();
                } else {
                    b = 0;
                }
                return b;
            }

            public synchronized int read(byte[] b) throws IOException {
                int len = 0;
                synchronized (this) {
                    if (BridgeWSConnectionManager.this.isConnected()) {
                        ByteBuffer buffer = BridgeWSConnectionManager.this.socketClient.read();
                        len = Math.min(buffer.remaining(), b.length);
                        buffer.get(b, 0, len);
                    }
                }
                return len;
            }

            public synchronized int read(byte[] b, int off, int len) throws IOException {
                int i;
                if (BridgeWSConnectionManager.this.isConnected()) {
                    ByteBuffer buffer = BridgeWSConnectionManager.this.socketClient.read();
                    int len2 = Math.min(buffer.remaining(), b.length);
                    buffer.get(b, off, len2);
                    i = len2;
                } else {
                    i = 0;
                }
                return i;
            }
        };
        try {
            this.isReconnecting = false;
            getCurrentBridgeIPAddress();
            DJILog.d("WSOCKET", "ipAddress " + this.ipAddress.toString(), new Object[0]);
            synchronized (this) {
                this.socketClient = new DJIWebSocketClient(new URI(this.ipAddress.toString()));
                this.socketClient.connect();
            }
        } catch (Exception e) {
            DJILog.d("WSOCKET", "onError " + e, new Object[0]);
        }
    }

    public static BridgeWSConnectionManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    /* access modifiers changed from: private */
    public void getCurrentBridgeIPAddress() {
        this.ipAddress = new StringBuilder();
        this.ipAddress.append(HOST_PREFIX);
        if (!TextUtils.isEmpty("")) {
            this.ipAddress.append("");
        } else if (TextUtils.isEmpty(MidWare.bridgeIP)) {
            this.ipAddress.append(DEFAULT_IP);
        } else {
            this.ipAddress.append(MidWare.bridgeIP);
        }
        this.ipAddress.append(HOST_SUFFIX);
    }

    /* access modifiers changed from: private */
    public void writeAfterFilter(byte[] b, int off, int len) {
        this.buffer.write(b, off, len);
        sendIfReady();
    }

    private void sendIfReady() {
        if (this.buffer.size() > 5) {
            byte[] byteArray = this.buffer.toByteArray();
            if (send(byteArray, byteArray.length)) {
                this.buffer.reset();
            }
        }
    }

    private synchronized boolean send(byte[] b, int length) {
        boolean z;
        byte[] toBeSent = Arrays.copyOfRange(b, 0, length);
        if (this.socketClient == null || !this.socketClient.getConnection().isOpen()) {
            reconnect();
            z = false;
        } else if (!(this.socketClient.getConnection() instanceof WebSocketImpl) || ((WebSocketImpl) this.socketClient.getConnection()).outQueue.size() <= 100) {
            this.socketClient.send(toBeSent);
            z = true;
        } else {
            this.socketClient.getConnection().close();
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public synchronized void reconnect() {
        if (!this.isReconnecting) {
            this.isReconnecting = true;
            this.reconnectExecutor = new ScheduledThreadPoolExecutor(1);
            this.reconnectExecutor.scheduleAtFixedRate(new Runnable() {
                /* class dji.midware.wsbridge.BridgeWSConnectionManager.AnonymousClass3 */

                /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    /*
                        r6 = this;
                        r4 = 0
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this
                        r1.getCurrentBridgeIPAddress()
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this
                        boolean r1 = r1.isConnected()
                        if (r1 != 0) goto L_0x0074
                        dji.midware.wsbridge.BridgeWSConnectionManager r2 = dji.midware.wsbridge.BridgeWSConnectionManager.this
                        monitor-enter(r2)
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this     // Catch:{ Exception -> 0x0052 }
                        dji.midware.wsbridge.DJIWebSocketClient r1 = r1.socketClient     // Catch:{ Exception -> 0x0052 }
                        if (r1 == 0) goto L_0x0022
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this     // Catch:{ Exception -> 0x0052 }
                        dji.midware.wsbridge.DJIWebSocketClient r1 = r1.socketClient     // Catch:{ Exception -> 0x0052 }
                        r1.close()     // Catch:{ Exception -> 0x0052 }
                    L_0x0022:
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this     // Catch:{ Exception -> 0x0052 }
                        dji.midware.wsbridge.DJIWebSocketClient r3 = new dji.midware.wsbridge.DJIWebSocketClient     // Catch:{ Exception -> 0x0052 }
                        java.net.URI r4 = new java.net.URI     // Catch:{ Exception -> 0x0052 }
                        dji.midware.wsbridge.BridgeWSConnectionManager r5 = dji.midware.wsbridge.BridgeWSConnectionManager.this     // Catch:{ Exception -> 0x0052 }
                        java.lang.StringBuilder r5 = r5.ipAddress     // Catch:{ Exception -> 0x0052 }
                        java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0052 }
                        r4.<init>(r5)     // Catch:{ Exception -> 0x0052 }
                        r3.<init>(r4)     // Catch:{ Exception -> 0x0052 }
                        dji.midware.wsbridge.DJIWebSocketClient unused = r1.socketClient = r3     // Catch:{ Exception -> 0x0052 }
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this     // Catch:{ Exception -> 0x0052 }
                        dji.midware.wsbridge.DJIWebSocketClient r1 = r1.socketClient     // Catch:{ Exception -> 0x0052 }
                        r1.connect()     // Catch:{ Exception -> 0x0052 }
                        java.lang.String r1 = "WSOCKET"
                        java.lang.String r3 = "Reconnecting "
                        r4 = 0
                        java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0052 }
                        dji.log.DJILog.d(r1, r3, r4)     // Catch:{ Exception -> 0x0052 }
                    L_0x0050:
                        monitor-exit(r2)     // Catch:{ all -> 0x0071 }
                    L_0x0051:
                        return
                    L_0x0052:
                        r0 = move-exception
                        java.lang.String r1 = "WSOCKET"
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0071 }
                        r3.<init>()     // Catch:{ all -> 0x0071 }
                        java.lang.String r4 = "onError "
                        java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0071 }
                        java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ all -> 0x0071 }
                        java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0071 }
                        r4 = 0
                        java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0071 }
                        dji.log.DJILog.d(r1, r3, r4)     // Catch:{ all -> 0x0071 }
                        goto L_0x0050
                    L_0x0071:
                        r1 = move-exception
                        monitor-exit(r2)     // Catch:{ all -> 0x0071 }
                        throw r1
                    L_0x0074:
                        java.lang.String r1 = "WSOCKET"
                        java.lang.String r2 = "Reconnected, stop retrying"
                        java.lang.Object[] r3 = new java.lang.Object[r4]
                        dji.log.DJILog.d(r1, r2, r3)
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this
                        java.util.concurrent.ScheduledExecutorService r1 = r1.reconnectExecutor
                        boolean r1 = r1.isShutdown()
                        if (r1 != 0) goto L_0x0051
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this
                        java.util.concurrent.ScheduledExecutorService r1 = r1.reconnectExecutor
                        r1.shutdown()
                        dji.midware.wsbridge.BridgeWSConnectionManager r1 = dji.midware.wsbridge.BridgeWSConnectionManager.this
                        boolean unused = r1.isReconnecting = r4
                        goto L_0x0051
                    */
                    throw new UnsupportedOperationException("Method not decompiled: dji.midware.wsbridge.BridgeWSConnectionManager.AnonymousClass3.run():void");
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
        }
    }

    /* access modifiers changed from: private */
    public synchronized boolean isConnected() {
        return (this.socketClient == null || this.socketClient.getConnection() == null || !this.socketClient.getConnection().isOpen()) ? false : true;
    }

    public InputStream getInputStream() {
        return this.inStream;
    }

    public OutputStream getOutputStream() {
        return this.outStream;
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final BridgeWSConnectionManager INSTANCE = new BridgeWSConnectionManager();

        private LazyHolder() {
        }
    }
}
