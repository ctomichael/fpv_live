package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.natives.UDT;
import dji.midware.sockets.P3.P3CRemoteService;
import dji.midware.util.ContextUtil;
import dji.midware.util.WifiStateUtil;
import java.util.Timer;
import java.util.TimerTask;

@EXClassNullAway
public abstract class SocketUdtClient extends DJIUdtSocket {
    private Timer checkTimer;

    public SocketUdtClient(String ip, String port) {
        super(ip, port, null, null);
    }

    public SocketUdtClient(String ip, String port, String localIp, String localPort) {
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
        if (P3CRemoteService.getInstance().isOK() && !DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
            if (this.port.equals("9001")) {
            }
            if (this.socket == -1) {
                this.socket = UDT.socket();
            }
            if (this.port.equals("9001")) {
            }
            int mConnectResult = -1;
            try {
                if (this.localPort == null) {
                    mConnectResult = UDT.connect(this.socket, this.ip, this.port);
                } else if (this.localIp == null) {
                    String wifiIp = ContextUtil.getWifiIP();
                    if (wifiIp != null) {
                        mConnectResult = UDT.connectWithLocal(this.socket, this.ip, this.port, wifiIp, this.localPort);
                    }
                } else {
                    mConnectResult = UDT.connectWithLocal(this.socket, this.ip, this.port, this.localIp, this.localPort);
                }
                if (this.port.equals("9001")) {
                }
                if (mConnectResult < 0) {
                    closeSocket();
                } else {
                    this.isConnecting = true;
                    onConnect();
                    checkReceiveThread();
                    LOGE("udt 连接建立 ip:" + this.ip + "port:" + this.port);
                }
            } catch (Exception e) {
            }
            if (this.port.equals("9001")) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkConneted() {
        if (ServiceManager.getContext() != null && WifiStateUtil.isWifiActive(ServiceManager.getContext()) && !isConnected()) {
            connect();
        }
    }

    /* access modifiers changed from: protected */
    public void startTimers() {
        this.checkTimer = new Timer(getClass().getSimpleName() + " timer");
        this.checkTimer.schedule(new TimerTask() {
            /* class dji.midware.sockets.pub.SocketUdtClient.AnonymousClass1 */

            public void run() {
                SocketUdtClient.this.checkConneted();
            }
        }, 500, 1000);
    }

    /* access modifiers changed from: protected */
    public void sendError() {
    }

    public boolean isOK() {
        return isConnected();
    }
}
