package dji.midware.sockets.dpad;

import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.DJIVideoDecoderInterface;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoProcessTimeTest;
import dji.midware.media.DJIVideoUtil;
import dji.midware.sockets.pub.DJIStreamLocalSocket;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamSaver;

public class DpadStreamNewService extends DJIStreamLocalSocket {
    private static final String SOCKET_NAME = "relay_fpv_sock";
    private static DpadStreamNewService instance;
    private StreamDataObserver mNeedPackObserver;

    private DpadStreamNewService() {
        super(SOCKET_NAME);
    }

    public static synchronized DpadStreamNewService getInstance() {
        DpadStreamNewService dpadStreamNewService;
        synchronized (DpadStreamNewService.class) {
            if (instance == null) {
                instance = new DpadStreamNewService();
            }
            dpadStreamNewService = instance;
        }
        return dpadStreamNewService;
    }

    public void destroy() {
        super.destroy();
        stopStream();
        instance = null;
    }

    public void sendmessage(SendPack buffer) {
    }

    /* access modifiers changed from: protected */
    public void parse(byte[] buffer, int offset, int length) {
        DJIVideoDecoderInterface decoderInterface = DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface();
        if (((ServiceManager.getInstance().isNeedRawData() || ServiceManager.getInstance().getDecoder() != null) && ServiceManager.getInstance().isRemoteOK()) || !(decoderInterface == null || decoderInterface.getDJIVideoDecoder() == null || decoderInterface.getParseController() == null)) {
            if (StreamSaver.SAVE_videoUsb_Open) {
                StreamSaver.getInstance(StreamSaver.SAVE_videoWifi_Name).write(buffer, 0, length);
            }
            if (ServiceManager.getInstance().isNeedPacked()) {
                if (ServiceManager.getInstance().isNeedRawData()) {
                    DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
                    return;
                }
                DJIVideoProcessTimeTest.getInstance().onVideoDataProcessing(DJIVideoProcessTimeTest.VideoProcessPoint.BeforeParse, buffer, offset);
                decoderInterface.getParseController().feedData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.UsbHostRC, buffer, 0, length), 0, length);
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
