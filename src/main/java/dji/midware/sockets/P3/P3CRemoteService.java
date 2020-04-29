package dji.midware.sockets.P3;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.sockets.pub.SocketTcpClient;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class P3CRemoteService extends SocketTcpClient {
    private static P3CRemoteService instance;
    private static String ip = "192.168.1.2";
    private static String localIp = null;
    private static int localPort = 0;
    private static int port = 5678;
    private long count = 0;
    private long lastT = 0;
    private DJIPackManager packManager = DJIPackManager.getInstance();

    public static void setLocIp(String newLocalIp, int newLocalPort) {
        localIp = newLocalIp;
        localPort = newLocalPort;
    }

    public static void setIp(String newIp, int newPort) {
        ip = newIp;
        port = newPort;
    }

    private P3CRemoteService() {
        super(ip, port, localIp, localPort);
    }

    public static synchronized P3CRemoteService getInstance() {
        P3CRemoteService p3CRemoteService;
        synchronized (P3CRemoteService.class) {
            if (instance == null) {
                instance = new P3CRemoteService();
            }
            p3CRemoteService = instance;
        }
        return p3CRemoteService;
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
        EventBus.getDefault().post(DataCameraEvent.ConnectLose);
        WifiService.getInstance().onDisconnect();
    }

    public void onConnect() {
        EventBus.getDefault().post(DataCameraEvent.ConnectOK);
        WifiService.getInstance().onConnect();
    }

    public void parse(byte[] buffer, int offset, int len) {
        this.packManager.parse(buffer, offset, len);
    }

    private void printRate(int length) {
        this.count += (long) length;
        if (getTickCount() - this.lastT > 1000) {
            float rate = (((float) this.count) * 1.0f) / 1024.0f;
            if (rate > 1024.0f) {
                Log.d("download", String.format(Locale.US, "rate %.2f MB\n", Float.valueOf(rate / 1024.0f)));
            } else {
                Log.d("download", String.format(Locale.US, "rate %.2f KB\n", Float.valueOf(rate)));
            }
            this.lastT = getTickCount();
            this.count = 0;
        }
    }

    private long getTickCount() {
        return System.currentTimeMillis();
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
