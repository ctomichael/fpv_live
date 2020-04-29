package dji.midware.sockets.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoUtil;
import dji.midware.natives.FPVController;
import dji.midware.sockets.pub.SocketUdtClient;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamSaver;

@EXClassNullAway
public class P3CLiveStreamService extends SocketUdtClient {
    private static P3CLiveStreamService instance;
    private static String ip = "192.168.1.3";
    private static String ipWM220 = "192.168.2.1";
    private static String localIp = null;
    private static String localPort = null;
    private static String port = "9000";
    private static String portWM220 = "9003";
    private StreamDataObserver parseObserver;

    public static void setWM220() {
        ip = ipWM220;
        port = portWM220;
    }

    public static void setLocIp(String newLocalIp, int newLocalPort) {
        localIp = newLocalIp;
        localPort = "" + newLocalPort;
    }

    public static void setIp(String newIp, int newPort) {
        ip = newIp;
        port = "" + newPort;
    }

    private P3CLiveStreamService() {
        super(ip, port, localIp, localPort);
        this.sleepTime = 900;
    }

    public static synchronized P3CLiveStreamService getInstance() {
        P3CLiveStreamService p3CLiveStreamService;
        synchronized (P3CLiveStreamService.class) {
            if (instance == null) {
                instance = new P3CLiveStreamService();
            }
            p3CLiveStreamService = instance;
        }
        return p3CLiveStreamService;
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

    public void parse(byte[] buffer, int length) {
        if (ServiceManager.getInstance().getDecoder() != null || ServiceManager.getInstance().isNeedRawData()) {
            if (StreamSaver.SAVE_videoUsb_Open) {
                StreamSaver.getInstance(StreamSaver.SAVE_videoWifi_Name).write(buffer, 0, length);
            }
            if (!ServiceManager.getInstance().isNeedPacked()) {
                DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
            } else if (ServiceManager.getInstance().isNeedRawData()) {
                DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
            } else {
                if (this.parseObserver == null) {
                    this.parseObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.P3cService);
                }
                this.parseObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) length);
                FPVController.native_transferVideoData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.P3CLiveStreamService, buffer, 0, length), length);
            }
        }
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
