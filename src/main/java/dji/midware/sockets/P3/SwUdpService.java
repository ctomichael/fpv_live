package dji.midware.sockets.P3;

import android.support.annotation.Keep;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.DJIVideoDecoderInterface;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoUtil;
import dji.midware.natives.UDT;
import dji.midware.sockets.pub.IpPortConfig;
import dji.midware.sockets.pub.SocketSwUdpClient;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamSaver;
import dji.midware.util.save.VideoFrameObserver;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class SwUdpService extends SocketSwUdpClient {
    private static final IpPortConfig CONFIG_DRONE = new IpPortConfig("192.168.2.1", 9003, IpPortConfig.ConnectType.DRONE);
    private static final IpPortConfig CONFIG_RC = new IpPortConfig("192.168.1.1", 9003, IpPortConfig.ConnectType.RC);
    private static SwUdpService instance = null;
    private static final boolean isRcUseSw = true;
    private SwUdpConnectListener connectListener;
    private byte[] extraBufForParsing;
    private boolean isConnected = false;
    private long lastRecvCmd = 0;
    private StreamDataObserver needPackObserver;
    private StreamDataObserver noNeedPackObserver;
    private DJIPackManager packManager = DJIPackManager.getInstance();
    private StreamDataObserver parseObserver;

    public interface SwUdpConnectListener {
        void onConnect();

        void onDisconnect();
    }

    private static ArrayList<IpPortConfig> getConfig() {
        ArrayList<IpPortConfig> config = new ArrayList<>();
        config.add(CONFIG_DRONE);
        config.add(CONFIG_RC);
        return config;
    }

    public boolean isRcUseSw() {
        return true;
    }

    public void setConnectListener(SwUdpConnectListener connectListener2) {
        this.connectListener = connectListener2;
    }

    public void removeConnectListener() {
        this.connectListener = null;
    }

    public int enReverseSw() {
        UDT.SwUdpStop();
        return UDT.SwUdpConnect(CONFIG_RC.ip, CONFIG_RC.port, CONFIG_RC.type.ordinal(), true);
    }

    public int dnReverseSw() {
        UDT.SwUdpStop();
        return UDT.SwUdpConnect(CONFIG_RC.ip, CONFIG_RC.port, CONFIG_RC.type.ordinal(), false);
    }

    private SwUdpService() {
        super(getConfig());
        UDT.setSwRecver(this);
    }

    public static synchronized SwUdpService getInstance() {
        SwUdpService swUdpService;
        synchronized (SwUdpService.class) {
            if (instance == null) {
                instance = new SwUdpService();
            }
            swUdpService = instance;
        }
        return swUdpService;
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

    public boolean isOK() {
        return false;
    }

    public void onDisconnect() {
        DJILogHelper.getInstance().LOGE(this.TAG, "sw onDisconnect");
        DJILog.saveConnectDebug("sw onDisconnect invoke: " + DJILog.getCurrentStack());
        if (this.currentConfig.isDrone()) {
            EventBus.getDefault().post(DataCameraEvent.ConnectLose);
        }
        WifiService.getInstance().onDisconnect();
        if (this.connectListener != null) {
            this.connectListener.onDisconnect();
        }
    }

    public void onConnect() {
        DJILogHelper.getInstance().LOGE(this.TAG, "sw onConnect");
        DJILog.saveConnectDebug("sw onConnect invoke: " + DJILog.getCurrentStack());
        if (this.currentConfig.isDrone()) {
            EventBus.getDefault().post(DataCameraEvent.ConnectOK);
        }
        this.portEvent = 5;
        WifiService.getInstance().onConnect();
        if (this.connectListener != null) {
            this.connectListener.onConnect();
        }
    }

    private void onRecvCmd() {
        this.lastRecvCmd = System.currentTimeMillis();
    }

    private void onRecvVideo() {
        if (System.currentTimeMillis() - this.lastRecvCmd > 3000) {
            DJILog.saveConnectDebug("3s not recv cmd data");
            this.lastRecvCmd = System.currentTimeMillis();
        }
    }

    public void parse(int type, byte[] buffer, int length) {
        ServiceManager.getInstance().changeTo(WifiService.getInstance());
        if (type == 1) {
            onRecvCmd();
            this.packManager.parse(buffer, 0, length);
            return;
        }
        onRecvVideo();
        if (StreamSaver.SAVE_videoUsb_Open) {
            StreamSaver.getInstance(StreamSaver.SAVE_videoWifi_Name).write(buffer, 0, length);
        }
        if (StreamSaver.SAVE_WM230VideoDebug_Open) {
            StreamSaver.getInstance(StreamSaver.Save_wm230VideoDebug_Name).write(buffer, 0, length);
        }
        DJIVideoDecoderInterface decoderInterface = DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface();
        if (decoderInterface != null) {
            VideoFrameObserver.getInstance().saveTimeStamp(VideoFrameObserver.TimeSavingPoint.UsbGetBody, buffer, 0, false);
            if (ServiceManager.getInstance().isNeedPacked()) {
                if (this.needPackObserver == null) {
                    this.needPackObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.SwUdpServiceParse);
                }
                this.needPackObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) length);
                if (ServiceManager.getInstance().isNeedRawData()) {
                    DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
                } else {
                    decoderInterface.getParseController().feedData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.SwUdpService, buffer, 0, length), 0, length);
                }
            } else if (ServiceManager.getInstance().getDecoder() != null || ServiceManager.getInstance().isNeedRawData()) {
                DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
            }
        }
    }

    public boolean isRcConnect() {
        return this.currentConfig != null && this.currentConfig.isRc() && isConnected();
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
        if (this.currentConfig == null || !this.currentConfig.isDrone()) {
            return this.packManager.isRemoteConnected();
        }
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
