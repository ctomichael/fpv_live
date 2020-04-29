package dji.midware.sockets.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJINotSyncPackManager;
import dji.midware.sockets.pub.SocketTcpClient;

@EXClassNullAway
public class P3CGroundService extends SocketTcpClient {
    private static P3CGroundService instance;
    private static String ip = "192.168.1.1";
    private static String localIp = null;
    private static int localPort = 0;
    private static int port = 2345;
    private DJINotSyncPackManager packManager = DJINotSyncPackManager.getInstance();

    public static void setLocIp(String newLocalIp, int newLocalPort) {
        localIp = newLocalIp;
        localPort = newLocalPort;
    }

    public static void setIp(String newIp, int newPort) {
        ip = newIp;
        port = newPort;
    }

    private P3CGroundService() {
        super(ip, port, localIp, localPort);
    }

    public static synchronized P3CGroundService getInstance() {
        P3CGroundService p3CGroundService;
        synchronized (P3CGroundService.class) {
            if (instance == null) {
                instance = new P3CGroundService();
            }
            p3CGroundService = instance;
        }
        return p3CGroundService;
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
        stopStream();
        instance = null;
    }

    public void onDisconnect() {
        WifiService.getInstance().onDisconnect();
    }

    public void onConnect() {
        WifiService.getInstance().onConnect();
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
        return false;
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
