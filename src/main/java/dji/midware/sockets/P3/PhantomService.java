package dji.midware.sockets.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.natives.FPVController;
import dji.midware.sockets.pub.SocketServer;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class PhantomService extends SocketServer {
    private static PhantomService instance;
    private static String ip = "0.0.0.0";
    private static int port = 22345;
    private DJIPackManager packManager = DJIPackManager.getInstance();

    private PhantomService() {
        super(ip, port);
        startStream();
    }

    public static synchronized PhantomService getInstance() {
        PhantomService phantomService;
        synchronized (PhantomService.class) {
            if (instance == null) {
                instance = new PhantomService();
            }
            phantomService = instance;
        }
        return phantomService;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public void startStream() {
        FPVController.native_startRecvThread();
    }

    public void stopStream() {
        FPVController.native_stopRecvThread();
    }

    public void destroy() {
        super.destroy();
        stopStream();
        instance = null;
    }

    public void onDisconnect() {
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.NON);
        DJILog.saveConnectDebug("phantom service post connect lose!");
        EventBus.getDefault().post(DataEvent.ConnectLose);
    }

    public void onConnect() {
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.ADB);
        EventBus.getDefault().post(DataEvent.ConnectOK);
    }

    public void parse(byte[] buffer, int offset, int len) {
        this.packManager.parse(buffer, offset, len);
    }

    /* access modifiers changed from: protected */
    public boolean getHeartStatus() {
        return true;
    }

    public boolean isConnected() {
        return super.isConnected();
    }

    /* access modifiers changed from: protected */
    public void resetHeartStatus() {
    }

    public void LOGD(String s) {
        DJILogHelper.getInstance().LOGD(this.TAG, s, false, false);
    }

    public void LOGE(String s) {
        DJILogHelper.getInstance().LOGE(this.TAG, s, false, false);
    }

    public boolean isRemoteOK() {
        return this.packManager.isRemoteConnected();
    }

    public void setDataMode(boolean dataMode) {
        FPVController.native_setDataMode(dataMode);
    }

    public void pauseService(boolean isPause) {
    }

    public void pauseRecvThread() {
        FPVController.native_pauseRecvThread(true);
    }

    public void resumeRecvThread() {
        FPVController.native_pauseRecvThread(false);
    }

    public void pauseParseThread() {
    }

    public void resumeParseThread() {
    }
}
