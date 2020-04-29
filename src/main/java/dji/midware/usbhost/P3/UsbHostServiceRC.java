package dji.midware.usbhost.P3;

import android.os.Environment;
import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.Dpad.DpadProductType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIArPackManager;
import dji.midware.data.manager.P3.DJIFlightLogPackManager;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoUtil;
import dji.midware.natives.FPVController;
import dji.midware.parser.plugins.DJIPluginLBChanneParser;
import dji.midware.util.save.StreamDataObserver;
import java.io.FileOutputStream;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class UsbHostServiceRC implements DJIServiceInterface {
    static boolean firstRecv = true;
    private static UsbHostServiceRC instance = null;
    private static final String saveVideoPath = (Environment.getExternalStorageDirectory().getPath() + "usbhost.264");
    private final boolean IS_PRINT_RATE = true;
    private final String TAG = getClass().getSimpleName();
    private byte[] boxbuffer = new byte[16384];
    private int count = 0;
    private boolean dataMode;
    private FileOutputStream fileOutputStream = null;
    private int getVideoIndex = 0;
    private boolean isPauseRecvThread = false;
    private boolean isPauseService = false;
    private boolean isPaused = false;
    private final boolean isSaveVideoToFile = false;
    private boolean isStartStream;
    private long lastT = 0;
    private boolean mOsdRun;
    private boolean mParseRun;
    private boolean mParseVideoRun;
    private boolean mVodRun;
    private boolean m_cmd_serial = true;
    private StreamDataObserver needPackObserver;
    private byte[] osdbuffer = new byte[4096];
    private DJIPackManager packManager;
    private Thread parseVideoThread;
    private int setVideoIndex = 0;
    private ArrayList<byte[]> videoList = new ArrayList<>(50);
    private DJIVideoPackManager videoPackManager;
    private ArrayList<Integer> videoSizeList = new ArrayList<>(50);

    public static synchronized UsbHostServiceRC getInstance() {
        UsbHostServiceRC usbHostServiceRC;
        synchronized (UsbHostServiceRC.class) {
            if (instance == null) {
                instance = new UsbHostServiceRC();
            }
            usbHostServiceRC = instance;
        }
        return usbHostServiceRC;
    }

    public static void Pause() {
        if (instance != null) {
            instance.pause();
        }
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public UsbHostServiceRC() {
        System.out.println("xxxxxxxxxxxxxx UsbHostServiceRC construct");
        startStream();
        this.packManager = DJIPackManager.getInstance();
        this.videoPackManager = DJIVideoPackManager.getInstance();
        NativeRcController.rc_init();
        NativeRcController.rc_set_cb_obj(this);
    }

    public void start() {
        NativeRcController.rc_init();
    }

    public void stop() {
        NativeRcController.rc_exit();
    }

    public void startStream() {
        this.isStartStream = true;
        Log.d("", "xx usb host startStream");
    }

    public void stopStream() {
        this.isStartStream = false;
        Log.d("", "usb host stopStream");
        this.mVodRun = false;
    }

    /* access modifiers changed from: protected */
    public void startThreads() {
    }

    public void onSerialRecv(byte[] serialBuffer, int size) {
        if (!this.isPaused) {
            if (firstRecv) {
                firstRecv = false;
                onConnect();
            }
            printRate(size);
            this.packManager.parse(serialBuffer, 0, size);
        }
    }

    @Deprecated
    public void onVideoRecv(byte[] buffer, int size) {
    }

    @Deprecated
    public void onVideoRecv(byte[] videoBuffer, int size, int frameNum, int isKeyFrame, int spsPos, int spsLen, int ppsPos, int ppsLen, int width, int height) {
    }

    public void onVideoRecv(byte[] videoBuffer, int size, int frameNum, int isKeyFrame, int spsPos, int spsLen, int ppsPos, int ppsLen, int width, int height, int channelID) {
        if (!this.isPaused) {
            if (firstRecv) {
                firstRecv = false;
                onConnect();
            }
            if (channelID == DJIPluginLBChanneParser.DJILBChannelID.LiveView.value() || channelID == DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value() || channelID == DJIPluginLBChanneParser.DJILBChannelID.FourthLiveViewXT.value()) {
                if (DpadProductType.Pomato == DpadProductManager.getInstance().getProductType()) {
                    DJIVideoDataRecver.getInstance().onVideoRecv(videoBuffer, size, frameNum, isKeyFrame != 0, spsPos, spsLen, ppsPos, ppsLen, width, height, false);
                } else {
                    transferVideoData(videoBuffer, size);
                }
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.FileDownload.value()) {
                printRate(size);
                this.videoPackManager.parse(videoBuffer, 0, size);
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.ARPush.value()) {
                DJIArPackManager.getInstance().parse(videoBuffer, 0, size);
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.FlightLog.value()) {
                DJIFlightLogPackManager.getInstance().parse(videoBuffer, 0, size);
            }
        }
    }

    private void transferVideoData(byte[] videoBuffer, int size) {
        if ((!ServiceManager.getInstance().isNeedRawData() && ServiceManager.getInstance().getDecoder() == null) || !ServiceManager.getInstance().isRemoteOK()) {
            return;
        }
        if (ServiceManager.getInstance().isNeedPacked()) {
            if (this.needPackObserver == null) {
                this.needPackObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetBody);
            }
            this.needPackObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) size);
            if (ServiceManager.getInstance().isNeedRawData()) {
                DJIVideoDataRecver.getInstance().onVideoRecv(videoBuffer, size, true);
            } else {
                FPVController.native_transferVideoData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.UsbHostRC, videoBuffer, 0, size), size);
            }
        } else if (ServiceManager.getInstance().getDecoder() != null) {
            DJIVideoDataRecver.getInstance().onVideoRecv(videoBuffer, size, true);
        }
    }

    private void startRecvVodThread() {
    }

    private void handleOldMethod(int dataLen) {
        if (this.dataMode) {
            DJIVideoPackManager.getInstance().parseData(this.boxbuffer, 0, dataLen);
        } else {
            putVideoBuffer(this.boxbuffer, dataLen);
        }
    }

    public void handleNewMethod(byte[] buffer, int offset, int count2) {
    }

    @Keep
    private class RecvVodRunnable implements Runnable {
        private RecvVodRunnable() {
        }

        public void run() {
        }
    }

    private void putVideoBuffer(byte[] buffer, int length) {
        this.videoSizeList.set(this.setVideoIndex, Integer.valueOf(length));
        System.arraycopy(buffer, 0, this.videoList.get(this.setVideoIndex), 0, length);
        if (this.setVideoIndex == this.videoList.size() - 1) {
            this.setVideoIndex = 0;
        } else {
            this.setVideoIndex++;
        }
    }

    @Keep
    private class ParseVideoRunnable implements Runnable {
        public static final String TAG = "VideoStream_Parse_Thread";
        private int aoa_log_update = 0;
        private long last_frame_num_packet = 0;
        private long last_frame_size = 0;
        private long last_frame_start_time = -1;

        private ParseVideoRunnable() {
        }

        public void run() {
            UsbHostServiceRC.this.print("ParseVideoRunnable.end");
        }
    }

    public synchronized void sendmessage(SendPack buffer) {
        if (!this.isPaused) {
            if (buffer.cmdId != 1 || buffer.cmdSet == 0) {
            }
            NativeRcController.rc_sendto_serial(buffer.buffer, buffer.getLength());
            buffer.bufferObject.noUsed();
        }
    }

    public boolean isConnected() {
        return true;
    }

    public boolean isOK() {
        return isConnected();
    }

    public void resume() {
        this.isPaused = false;
    }

    private void pause() {
        this.isPaused = true;
        firstRecv = true;
    }

    public void destroy() {
        this.mVodRun = false;
        this.mOsdRun = false;
        this.mParseRun = false;
        instance = null;
    }

    private void printRate(int length) {
        this.count += length;
        if (getTickCount() - this.lastT > 1000) {
            this.lastT = getTickCount();
            this.count = 0;
        }
    }

    private long getTickCount() {
        return System.currentTimeMillis();
    }

    private void printUI(String s) {
        DJILogHelper.getInstance().LOGE(this.TAG, s, false, true);
    }

    /* access modifiers changed from: private */
    public void print(String s) {
        DJILogHelper.getInstance().LOGE(this.TAG, s, false, false);
    }

    public boolean isRemoteOK() {
        return this.packManager.isRemoteConnected();
    }

    public void setDataMode(boolean dataMode2) {
    }

    public void pauseRecvThread() {
        this.isPauseRecvThread = true;
    }

    public void resumeRecvThread() {
        this.isPauseRecvThread = false;
    }

    public void pauseParseThread() {
    }

    public void resumeParseThread() {
    }

    public void pauseService(boolean isPause) {
        if (this.isPauseService != isPause) {
            this.isPauseService = isPause;
            if (this.isPauseService) {
                DJIPackManager.getInstance().pauseConnectCheck();
            } else {
                DJIPackManager.getInstance().resumeConnectCheck();
            }
        }
    }

    public void onDisconnect() {
        this.mVodRun = false;
        this.mOsdRun = false;
        this.mParseRun = false;
        firstRecv = true;
        instance = null;
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.NON);
    }

    public void onConnect() {
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.HOSTRC);
    }
}
