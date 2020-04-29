package dji.midware.sockets.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.sockets.pub.SocketUdtClient;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class P3CCameraService extends SocketUdtClient {
    private static P3CCameraService instance;
    private static String ip = "192.168.1.3";
    private static String localIp = null;
    private static String localPort = null;
    private static String port = "9001";

    public enum DJIFileUdtConnectStatus {
        Connected,
        Disconnected
    }

    private P3CCameraService() {
        super(ip, port, localIp, localPort);
    }

    public static void setLocIp(String newLocalIp, int newLocalPort) {
        localIp = newLocalIp;
        localPort = "" + newLocalPort;
    }

    public static void setIp(String newIp, int newPort) {
        ip = newIp;
        port = "" + newPort;
    }

    public static synchronized P3CCameraService getInstance() {
        P3CCameraService p3CCameraService;
        synchronized (P3CCameraService.class) {
            if (instance == null) {
                instance = new P3CCameraService();
            }
            p3CCameraService = instance;
        }
        return p3CCameraService;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    public void destroy() {
        super.destroy();
        instance = null;
    }

    public void onDisconnect() {
        WifiService.getInstance().onDisconnect();
    }

    public void onConnect() {
        WifiService.getInstance().onConnect();
        EventBus.getDefault().post(DJIFileUdtConnectStatus.Connected);
    }

    public void parse(byte[] buffer, int length) {
        DJIVideoPackManager.getInstance().parseData(buffer, 0, length);
    }

    public boolean isConnected() {
        return super.isConnected();
    }

    public void LOGD(String s) {
        DJILogHelper.getInstance().LOGD(this.TAG, s, false, false);
    }

    public void LOGE(String s) {
        DJILogHelper.getInstance().LOGE(this.TAG, s, false, false);
    }

    public boolean isRemoteOK() {
        return true;
    }

    public void setDataMode(boolean dataMode) {
    }

    public void pauseService(boolean isPause) {
    }

    public void pauseRecvThread() {
    }

    public void resumeRecvThread() {
    }

    public void pauseParseThread() {
    }

    public void resumeParseThread() {
    }
}
