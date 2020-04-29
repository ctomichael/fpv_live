package dji.midware.sockets.pub;

import android.os.Handler;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.ContextUtil;
import dji.midware.wifi.DJIMultiNetworkMgr;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

@EXClassNullAway
public abstract class SocketTcpClient extends DJISocket {
    /* access modifiers changed from: private */
    public Timer checkTimer;
    private Handler handler = new Handler(BackgroundLooper.getLooper());

    public SocketTcpClient(String ip, int port) {
        super(ip, port, null, 0);
    }

    public SocketTcpClient(String ip, int port, String localIp, int localPort) {
        super(ip, port, localIp, localPort);
    }

    public void destroy() {
        if (this.checkTimer != null) {
            this.checkTimer.cancel();
            this.checkTimer = null;
        }
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void connect() {
        if (canDo() && !DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
            try {
                resetHeartStatus();
                this.socket = DJIMultiNetworkMgr.getInstance().getSocket();
                this.socket.setKeepAlive(true);
                this.socket.setSoLinger(true, 0);
                this.socket.setReuseAddress(true);
                this.socket.setSoTimeout(5000);
                this.socket.setTcpNoDelay(true);
                this.socket.setOOBInline(true);
                this.socket.setTrafficClass(16);
                this.socket.setPerformancePreferences(1, 2, 3);
                if (this.localPort > 0) {
                    if (this.localIp == null) {
                        String wifiIp = ContextUtil.getWifiIP();
                        if (wifiIp != null) {
                            this.socket.bind(new InetSocketAddress(wifiIp, this.localPort));
                        }
                    } else {
                        this.socket.bind(new InetSocketAddress(this.localIp, this.localPort));
                    }
                }
                connectSocket(this.socket);
                this.socket.connect(this.address, DJIVideoDecoder.connectLosedelay);
                if (this.out != null) {
                    this.out.close();
                }
                if (this.input != null) {
                    this.input.close();
                }
                this.out = this.socket.getOutputStream();
                this.input = this.socket.getInputStream();
                checkReceiveThread();
                LOGD("tcp 连接建立 ip:" + this.ip + "port:" + this.port);
            } catch (Exception e) {
                this.socket = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyConnected() {
        if (!this.isConnected) {
            this.isConnected = true;
            if (DJILinkDaemonService.getInstance().getLinkType() != DJILinkType.AOA) {
                if (this.handler == null) {
                    this.handler = new Handler(BackgroundLooper.getLooper());
                }
                this.handler.post(new Runnable() {
                    /* class dji.midware.sockets.pub.SocketTcpClient.AnonymousClass1 */

                    public void run() {
                        SocketTcpClient.this.onConnect();
                    }
                });
                return;
            }
            LOGD("没有成功执行连接状态");
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkConneted() {
        /*
            r5 = this;
            boolean r2 = r5.canDo()
            if (r2 != 0) goto L_0x0026
            boolean r2 = r5.isConnected
            if (r2 == 0) goto L_0x001d
            java.lang.Object r3 = dji.midware.sockets.pub.SocketTcpClient.LOCK
            monitor-enter(r3)
            java.net.Socket r2 = r5.socket     // Catch:{ IOException -> 0x001e }
            if (r2 == 0) goto L_0x0016
            java.net.Socket r2 = r5.socket     // Catch:{ IOException -> 0x001e }
            r2.close()     // Catch:{ IOException -> 0x001e }
        L_0x0016:
            r2 = 0
            r5.socket = r2     // Catch:{ all -> 0x0023 }
            r5.onDisconnect()     // Catch:{ all -> 0x0023 }
            monitor-exit(r3)     // Catch:{ all -> 0x0023 }
        L_0x001d:
            return
        L_0x001e:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x0023 }
            goto L_0x0016
        L_0x0023:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0023 }
            throw r2
        L_0x0026:
            java.net.Socket r2 = r5.socket
            if (r2 != 0) goto L_0x002e
            r5.connect()
            goto L_0x001d
        L_0x002e:
            java.lang.Object r3 = dji.midware.sockets.pub.SocketTcpClient.LOCK
            monitor-enter(r3)
            java.net.Socket r2 = r5.socket     // Catch:{ IOException -> 0x0094, NullPointerException -> 0x003d }
            r4 = 255(0xff, float:3.57E-43)
            r2.sendUrgentData(r4)     // Catch:{ IOException -> 0x0094, NullPointerException -> 0x003d }
        L_0x0038:
            monitor-exit(r3)     // Catch:{ all -> 0x003a }
            goto L_0x001d
        L_0x003a:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x003a }
            throw r2
        L_0x003d:
            r0 = move-exception
        L_0x003e:
            java.net.Socket r2 = r5.socket     // Catch:{ IOException -> 0x008f }
            if (r2 == 0) goto L_0x0047
            java.net.Socket r2 = r5.socket     // Catch:{ IOException -> 0x008f }
            r2.close()     // Catch:{ IOException -> 0x008f }
        L_0x0047:
            r2 = 0
            r5.socket = r2     // Catch:{ all -> 0x003a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x003a }
            r2.<init>()     // Catch:{ all -> 0x003a }
            java.lang.String r4 = "tcp 断开 Exception = "
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r4 = dji.log.DJILog.exceptionToString(r0)     // Catch:{ all -> 0x003a }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x003a }
            r5.LOGE(r2)     // Catch:{ all -> 0x003a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x003a }
            r2.<init>()     // Catch:{ all -> 0x003a }
            java.lang.String r4 = "tcp 连接断开 ip = "
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r4 = r5.ip     // Catch:{ all -> 0x003a }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r4 = " port = "
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ all -> 0x003a }
            int r4 = r5.port     // Catch:{ all -> 0x003a }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x003a }
            r5.LOGE(r2)     // Catch:{ all -> 0x003a }
            r5.onDisconnect()     // Catch:{ all -> 0x003a }
            goto L_0x0038
        L_0x008f:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x003a }
            goto L_0x0047
        L_0x0094:
            r0 = move-exception
            goto L_0x003e
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.sockets.pub.SocketTcpClient.checkConneted():void");
    }

    public void onDisconnect() {
        this.isConnected = false;
    }

    /* access modifiers changed from: protected */
    public void startTimers() {
        new Thread(new Runnable() {
            /* class dji.midware.sockets.pub.SocketTcpClient.AnonymousClass2 */

            public void run() {
                Timer unused = SocketTcpClient.this.checkTimer = new Timer();
                SocketTcpClient.this.checkTimer.schedule(new TimerTask() {
                    /* class dji.midware.sockets.pub.SocketTcpClient.AnonymousClass2.AnonymousClass1 */

                    public void run() {
                        SocketTcpClient.this.checkConneted();
                    }
                }, 100, 2000);
            }
        }, "tcpDaemon").start();
    }

    /* access modifiers changed from: protected */
    public void sendError() {
    }

    /* access modifiers changed from: protected */
    public void connectSocket(Socket socket) throws Exception {
    }

    public boolean isOK() {
        return isConnected() && getHeartStatus();
    }
}
