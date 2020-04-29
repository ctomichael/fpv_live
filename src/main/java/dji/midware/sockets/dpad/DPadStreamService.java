package dji.midware.sockets.dpad;

import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoUtil;
import dji.midware.natives.FPVController;
import dji.midware.sockets.pub.DJIUdpServerSocket;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamSaver;

public class DPadStreamService extends DJIUdpServerSocket {
    private static final String IP_DPAD = "127.0.0.1";
    private static final int PORT_DPAD = 40002;
    private static DPadStreamService instance;
    private StreamDataObserver mNeedPackObserver;

    public DPadStreamService() {
        super(IP_DPAD, 40002);
    }

    public static synchronized DPadStreamService getInstance() {
        DPadStreamService dPadStreamService;
        synchronized (DPadStreamService.class) {
            if (instance == null) {
                instance = new DPadStreamService();
            }
            dPadStreamService = instance;
        }
        return dPadStreamService;
    }

    public void destroy() {
        super.destroy();
        stopStream();
        instance = null;
    }

    /* access modifiers changed from: protected */
    public void parse(byte[] buffer, int offset, int length) {
        if ((ServiceManager.getInstance().isNeedRawData() || ServiceManager.getInstance().getDecoder() != null) && ServiceManager.getInstance().isRemoteOK()) {
            if (StreamSaver.SAVE_videoUsb_Open) {
                StreamSaver.getInstance(StreamSaver.SAVE_videoWifi_Name).write(buffer, 0, length);
            }
            if (ServiceManager.getInstance().isNeedPacked()) {
                if (this.mNeedPackObserver == null) {
                    this.mNeedPackObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.DPadUdpServiceParse);
                }
                this.mNeedPackObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) length);
                if (ServiceManager.getInstance().isNeedRawData()) {
                    DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
                } else {
                    FPVController.native_transferVideoData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.UsbHostRC, buffer, 0, length), length);
                }
            } else if (ServiceManager.getInstance().getDecoder() != null) {
                DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
            }
        }
    }

    public void onDisconnect() {
    }

    public void onConnect() {
    }

    public boolean isRemoteOK() {
        return true;
    }
}
