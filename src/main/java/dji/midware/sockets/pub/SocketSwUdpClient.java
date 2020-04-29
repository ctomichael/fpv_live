package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.manager.P3.DataPortEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.natives.UDT;
import dji.midware.util.WifiStateUtil;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public abstract class SocketSwUdpClient extends DJISwUdpSocket {
    protected static final int PortOccupiedEventMax = 5;
    private Timer checkTimer;
    protected int portEvent;
    private volatile int tryTime = 0;

    /* access modifiers changed from: protected */
    public abstract void parse(int i, byte[] bArr, int i2);

    public SocketSwUdpClient(ArrayList<IpPortConfig> config) {
        super(config);
        startTimers();
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
        if (!DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
            UDT.SwUdpStop();
            IpPortConfig config = (IpPortConfig) this.configList.get(this.tryTime % this.configList.size());
            int result = UDT.SwUdpConnect(config.ip, config.port, config.type.ordinal(), false);
            this.currentConfig = config;
            this.tryTime++;
            LOGE("SwUdpConnect ...result=" + result);
        }
    }

    public boolean isConnected() {
        try {
            return UDT.SwUdpIsConnected();
        } catch (UnsatisfiedLinkError error) {
            error.printStackTrace();
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void closeSocket() {
        UDT.SwUdpClose();
    }

    /* access modifiers changed from: private */
    public void checkConneted() {
        if (ServiceManager.getContext() != null && WifiStateUtil.isWifiActive(ServiceManager.getContext()) && !isConnected()) {
            connect();
            if (this.portEvent < 5 && UDT.SwUdpIsPortOccupied()) {
                EventBus.getDefault().post(new DataPortEvent());
                this.portEvent++;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startTimers() {
        this.checkTimer = new Timer(getClass().getSimpleName() + " timer");
        this.checkTimer.schedule(new TimerTask() {
            /* class dji.midware.sockets.pub.SocketSwUdpClient.AnonymousClass1 */

            public void run() {
                SocketSwUdpClient.this.checkConneted();
            }
        }, 5000, 2000);
    }
}
