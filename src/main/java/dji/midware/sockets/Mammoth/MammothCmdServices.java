package dji.midware.sockets.Mammoth;

import android.util.Log;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.sockets.P3.WifiService;
import dji.midware.sockets.pub.SocketTcpClient;
import java.util.Locale;

@EXClassNullAway
public class MammothCmdServices extends SocketTcpClient {
    private static MammothCmdServices instance;
    private static String ip = "192.168.1.1";
    private static int port = 19003;
    private long count = 0;
    private long lastT = 0;
    private DJIPackManager packManager = DJIPackManager.getInstance();

    private MammothCmdServices() {
        super(ip, port);
    }

    /* access modifiers changed from: protected */
    public void initBufferAttrs() {
        this.mBufferType = 3;
        this.mMinBufferLength = 64;
        this.buffer = new byte[SonyType1MakernoteDirectory.TAG_CONTRAST];
    }

    public static synchronized MammothCmdServices getInstance() {
        MammothCmdServices mammothCmdServices;
        synchronized (MammothCmdServices.class) {
            if (instance == null) {
                instance = new MammothCmdServices();
            }
            mammothCmdServices = instance;
        }
        return mammothCmdServices;
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
        boolean lastState = this.isConnected;
        super.onDisconnect();
        if (lastState) {
            WifiService.getInstance().onDisconnect();
            DJILog.saveConnectDebug("MammothCmdServices onDisconnect invoke: " + DJILog.getCurrentStack());
        }
    }

    public void onConnect() {
        WifiService.getInstance().onConnect();
        DJILog.saveConnectDebug("MammothCmdServices onConnect invoke: " + DJILog.getCurrentStack());
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
