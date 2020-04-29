package dji.midware.sockets.dpad;

import dji.log.DJILog;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import org.greenrobot.eventbus.EventBus;

public class DPadWifiService implements DJIServiceInterface {
    private static final boolean USE_LOCALSOCKET = true;
    private static DPadWifiService instance = null;
    private int connectCount;
    private DPadCmdService mDpadCmdService;
    private DpadStreamNewService mDpadNewStreamService;
    private DPadStreamService mDpadStreamService;

    public static synchronized DPadWifiService getInstance() {
        DPadWifiService dPadWifiService;
        synchronized (DPadWifiService.class) {
            if (instance == null) {
                instance = new DPadWifiService();
            }
            dPadWifiService = instance;
        }
        return dPadWifiService;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    private DPadWifiService() {
        this.mDpadStreamService = null;
        this.mDpadNewStreamService = null;
        this.mDpadCmdService = null;
        this.connectCount = 0;
        this.mDpadCmdService = DPadCmdService.getInstance();
        this.mDpadNewStreamService = DpadStreamNewService.getInstance();
    }

    public void destroy() {
        if (this.mDpadStreamService != null) {
            this.mDpadStreamService.destroy();
            this.mDpadStreamService = null;
        }
        if (this.mDpadNewStreamService != null) {
            this.mDpadNewStreamService.destroy();
            this.mDpadNewStreamService = null;
        }
        if (this.mDpadCmdService != null) {
            this.mDpadCmdService.destroy();
            this.mDpadCmdService = null;
        }
        instance = null;
    }

    public void sendmessage(SendPack buffer) {
        if (this.mDpadCmdService != null && this.mDpadCmdService.isConnected()) {
            this.mDpadCmdService.sendmessage(buffer);
        }
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    public boolean isConnected() {
        if (this.mDpadCmdService == null || !this.mDpadCmdService.isConnected()) {
            return false;
        }
        return true;
    }

    public boolean isOK() {
        return isConnected();
    }

    public boolean isRemoteOK() {
        if (this.mDpadCmdService == null || !this.mDpadCmdService.isConnected()) {
            return false;
        }
        return DJIPackManager.getInstance().isRemoteConnected();
    }

    public synchronized void onDisconnect() {
        if (ServiceManager.isAlive() && this.connectCount > 0) {
            this.connectCount--;
            if (this.connectCount == 0) {
                DJILinkDaemonService.getInstance().setLinkType(DJILinkType.NON);
                if (ServiceManager.isAlive()) {
                    DJILog.saveConnectDebug("dpad wifi service post connect lose!");
                    EventBus.getDefault().post(DataEvent.ConnectLose);
                }
            }
        }
    }

    public synchronized void onConnect() {
        if (ServiceManager.isAlive()) {
            if (this.connectCount == 0) {
                DJILinkDaemonService.getInstance().setLinkType(DJILinkType.USB_WIFI);
                EventBus.getDefault().post(DataEvent.ConnectOK);
            }
            this.connectCount++;
        }
    }

    public void setDataMode(boolean dataMode) {
    }

    public void pauseRecvThread() {
    }

    public void resumeRecvThread() {
    }

    public void pauseParseThread() {
    }

    public void resumeParseThread() {
    }

    public void pauseService(boolean isPause) {
    }
}
