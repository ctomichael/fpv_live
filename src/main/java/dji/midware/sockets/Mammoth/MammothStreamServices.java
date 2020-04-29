package dji.midware.sockets.Mammoth;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoUtil;
import dji.midware.natives.FPVController;
import dji.midware.sockets.pub.SocketTcpClient;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamSaver;
import java.net.Socket;

@EXClassNullAway
public class MammothStreamServices extends SocketTcpClient {
    private static MammothStreamServices instance;
    private static String ip = "192.168.1.1";
    private static int port = 19005;
    private byte[] extraBufForParsing;
    private StreamDataObserver needPackObserver;
    private StreamDataObserver noNeedPackObserver;

    private MammothStreamServices() {
        super(ip, port);
    }

    /* access modifiers changed from: protected */
    public void initBufferAttrs() {
        this.mBufferType = 3;
        this.mMinBufferLength = 1024;
        this.buffer = new byte[8192];
    }

    public static synchronized MammothStreamServices getInstance() {
        MammothStreamServices mammothStreamServices;
        synchronized (MammothStreamServices.class) {
            if (instance == null) {
                instance = new MammothStreamServices();
            }
            mammothStreamServices = instance;
        }
        return mammothStreamServices;
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

    /* access modifiers changed from: protected */
    public void connectSocket(Socket socket) throws Exception {
        super.connectSocket(socket);
        socket.setReceiveBufferSize(4194304);
    }

    /* access modifiers changed from: protected */
    public void parse(byte[] buffer, int offset, int length) {
        if ((ServiceManager.getInstance().isNeedRawData() || ServiceManager.getInstance().getDecoder() != null) && ServiceManager.getInstance().isRemoteOK()) {
            if (StreamSaver.SAVE_videoUsb_Open) {
                StreamSaver.getInstance(StreamSaver.SAVE_videoWifi_Name).write(buffer, 0, length);
            }
            if (StreamSaver.SAVE_WM230VideoDebug_Open) {
                StreamSaver.getInstance(StreamSaver.Save_wm230VideoDebug_Name).write(buffer, 0, length);
            }
            if (ServiceManager.getInstance().isNeedPacked()) {
                if (this.needPackObserver == null) {
                    this.needPackObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.MammothService);
                }
                this.needPackObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) length);
                if (ServiceManager.getInstance().isNeedRawData()) {
                    DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
                } else {
                    FPVController.native_transferVideoData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.MammothStreamService, buffer, offset, length), length);
                }
            } else if (ServiceManager.getInstance().getDecoder() != null) {
                DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean getHeartStatus() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void resetHeartStatus() {
    }

    public void onDisconnect() {
        Log.d("checkState", "stream onDisconnect : " + DJILog.getCurrentStack());
    }

    public void onConnect() {
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
