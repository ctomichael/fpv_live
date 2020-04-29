package dji.midware.usbhost.P3;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.MidWare;
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
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.BytesUtil;
import dji.midware.util.save.StreamDelaySaver;
import dji.midware.util.save.StreamSaver;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

@EXClassNullAway
public class UsbHostService implements DJIServiceInterface {
    private static UsbHostService instance = null;
    private static final String saveVideoPath = (Environment.getExternalStorageDirectory().getPath() + "usbhost.264");
    private final boolean IS_PRINT_RATE = true;
    private final String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public byte[] boxbuffer = new byte[16384];
    /* access modifiers changed from: private */
    public UsbDeviceConnection connection;
    private int count = 0;
    private boolean dataMode;
    private DJIPluginLBChanneParser djiPluginLBChanneParser;
    private FileOutputStream fileOutputStream = null;
    /* access modifiers changed from: private */
    public int getVideoIndex = 0;
    private boolean isPauseRecvThread = false;
    private boolean isPauseService = false;
    private final boolean isSaveVideoToFile = false;
    private boolean isStartStream;
    private long lastT = 0;
    private boolean mOsdRun;
    private boolean mParseRun;
    /* access modifiers changed from: private */
    public boolean mParseVideoRun;
    /* access modifiers changed from: private */
    public boolean mVodRun;
    private UsbEndpoint osdEndpoint;
    private byte[] osdbuffer = new byte[4096];
    private UsbEndpoint outEndpoint;
    private DJIPackManager packManager;
    private Thread parseOsdThread;
    /* access modifiers changed from: private */
    public Thread parseVideoThread;
    private Thread recvOsdThread;
    private Thread recvVodThread;
    /* access modifiers changed from: private */
    public int setVideoIndex = 0;
    /* access modifiers changed from: private */
    public DJIUsbReceiver usbReceiver;
    /* access modifiers changed from: private */
    public ArrayList<byte[]> videoList = new ArrayList<>(50);
    /* access modifiers changed from: private */
    public ArrayList<Integer> videoSizeList = new ArrayList<>(50);
    /* access modifiers changed from: private */
    public UsbEndpoint vodEndpoint;

    static /* synthetic */ int access$908(UsbHostService x0) {
        int i = x0.getVideoIndex;
        x0.getVideoIndex = i + 1;
        return i;
    }

    public static synchronized UsbHostService getInstance() {
        UsbHostService usbHostService;
        synchronized (UsbHostService.class) {
            if (instance == null) {
                instance = new UsbHostService();
            }
            usbHostService = instance;
        }
        return usbHostService;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public UsbHostService() {
        System.out.println("xxxxxxxxxxxxxx UsbHostService construct");
        this.djiPluginLBChanneParser = new DJIPluginLBChanneParser(new DJIPluginLBChanneParser.DJILBChannelListener() {
            /* class dji.midware.usbhost.P3.UsbHostService.AnonymousClass1 */

            public void onRecv(int channelID, byte[] buffer, int offset, int count) {
                FPVController.native_transferVideoData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.UsbHostServiceLBChanneParser, buffer, offset, count), count);
            }
        });
        startStream();
        this.packManager = DJIPackManager.getInstance();
    }

    public void start(Context ctx) {
        Context context = ctx.getApplicationContext();
        this.usbReceiver = new DJIUsbReceiver();
        this.usbReceiver.start(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DJIUsbReceiver.ACTION_USB_PERMISSION);
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        context.registerReceiver(this.usbReceiver, intentFilter);
    }

    public void stop(Context ctx) {
        ctx.getApplicationContext().unregisterReceiver(this.usbReceiver);
        if (this.usbReceiver != null) {
            this.usbReceiver.destroy();
            this.usbReceiver = null;
        }
    }

    public void startStream() {
        this.isStartStream = true;
        Log.d("", "xx usb host startStream");
    }

    public void stopStream() {
        this.isStartStream = false;
        Log.d("", "usb host stopStream");
        this.mVodRun = false;
        this.recvVodThread = null;
    }

    /* access modifiers changed from: protected */
    public void startThreads() {
        onConnect();
        this.connection = this.usbReceiver.getConnection();
        this.vodEndpoint = this.usbReceiver.getVodEndpoint();
        this.osdEndpoint = this.usbReceiver.getOsdEndpoint();
        this.outEndpoint = this.usbReceiver.getOutEndpoint();
        if (this.osdEndpoint != null) {
        }
        if (this.vodEndpoint != null) {
            startRecvVodThread();
        }
    }

    private void startRecvVodThread() {
        if (this.recvVodThread == null) {
            this.recvVodThread = new Thread(new RecvVodRunnable(), "startRecvVodThread");
            this.recvVodThread.start();
            print("recvVodThread.start");
        }
    }

    /* access modifiers changed from: private */
    public void handleOldMethod(int dataLen) {
        if (this.dataMode) {
            DJIVideoPackManager.getInstance().parseData(this.boxbuffer, 0, dataLen);
        } else {
            putVideoBuffer(this.boxbuffer, dataLen);
        }
    }

    public void handleNewMethod(byte[] buffer, int offset, int count2) {
        if (this.djiPluginLBChanneParser != null) {
            this.djiPluginLBChanneParser.parse(buffer, offset, count2);
        }
    }

    private class RecvVodRunnable implements Runnable {
        private RecvVodRunnable() {
        }

        public void run() {
            boolean unused = UsbHostService.this.mVodRun = true;
            while (UsbHostService.this.usbReceiver.isGetedConnection() && UsbHostService.this.mVodRun) {
                int length = UsbHostService.this.connection.bulkTransfer(UsbHostService.this.vodEndpoint, UsbHostService.this.boxbuffer, UsbHostService.this.boxbuffer.length, 0);
                if (length > 0) {
                    UsbHostService.this.handleOldMethod(length);
                }
            }
            UsbHostService.this.print("recvVodThread.end");
        }
    }

    private void putVideoBuffer(byte[] buffer, int length) {
        this.videoSizeList.set(this.setVideoIndex, Integer.valueOf(length));
        byte[] bytes = this.videoList.get(this.setVideoIndex);
        if (bytes == null || length + 1024 > bytes.length) {
            bytes = new byte[(length + 1024)];
            this.videoList.set(this.setVideoIndex, bytes);
        } else {
            Arrays.fill(bytes, (byte) 0);
        }
        System.arraycopy(buffer, 0, bytes, 0, length);
        if (this.setVideoIndex == this.videoList.size() - 1) {
            this.setVideoIndex = 0;
        } else {
            this.setVideoIndex++;
        }
    }

    private class ParseVideoRunnable implements Runnable {
        public static final String TAG = "VideoStream_Parse_Thread";
        private int aoa_log_update = 0;
        private long last_frame_num_packet = 0;
        private long last_frame_size = 0;
        private long last_frame_start_time = -1;

        private ParseVideoRunnable() {
        }

        public void run() {
            boolean unused = UsbHostService.this.mParseVideoRun = true;
            UsbHostService.this.print("ParseVideoRunnable " + (UsbHostService.this.usbReceiver != null && UsbHostService.this.usbReceiver.isGetedConnection() && UsbHostService.this.mParseVideoRun));
            long last_log_time = -1;
            while (true) {
                if (MidWare.isBridgeEnabled() || (UsbHostService.this.usbReceiver != null && UsbHostService.this.usbReceiver.isGetedConnection() && UsbHostService.this.mParseVideoRun)) {
                    if (UsbAccessoryService.STREAM_DEBUG) {
                        if (last_log_time == -1) {
                            last_log_time = SystemClock.uptimeMillis();
                        }
                        if (SystemClock.uptimeMillis() - last_log_time > 1000) {
                            last_log_time = SystemClock.uptimeMillis();
                        }
                    }
                    if (UsbHostService.this.getVideoIndex == UsbHostService.this.setVideoIndex) {
                        try {
                            Thread.sleep(0, 500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        byte[] buffer = (byte[]) UsbHostService.this.videoList.get(UsbHostService.this.getVideoIndex);
                        int length = ((Integer) UsbHostService.this.videoSizeList.get(UsbHostService.this.getVideoIndex)).intValue();
                        if (ServiceManager.getInstance().isNeedPacked()) {
                            FPVController.native_transferVideoData(buffer, length);
                            if (StreamDelaySaver.IS_SAVE_PACKET_DELAY) {
                                try {
                                    if (this.last_frame_start_time == -1) {
                                        this.last_frame_start_time = SystemClock.uptimeMillis();
                                    }
                                    String text = String.format(Locale.US, "[After Sending To FFMpeg] word 0: %X word 1: %X word 2: %X size=%d time=%d \n", Integer.valueOf(BytesUtil.getInt(buffer, 0)), Integer.valueOf(BytesUtil.getInt(buffer, 4)), Integer.valueOf(BytesUtil.getInt(buffer, 8)), Integer.valueOf(length), Long.valueOf(System.currentTimeMillis()));
                                    this.last_frame_size += (long) length;
                                    this.last_frame_num_packet++;
                                    if (length != 2048) {
                                        text = text + String.format(Locale.US, "frameReceiveDelay=%d frame_size=%d num_packet=%d", Long.valueOf(SystemClock.uptimeMillis() - this.last_frame_start_time), Long.valueOf(this.last_frame_size), Long.valueOf(this.last_frame_num_packet));
                                        this.last_frame_start_time = -1;
                                        this.last_frame_size = 0;
                                        this.last_frame_num_packet = 0;
                                    }
                                    StreamDelaySaver.getInstance().packetDelayFile.append((CharSequence) (text + "\n"));
                                    this.aoa_log_update = (this.aoa_log_update + 1) % 100;
                                    if (this.aoa_log_update == 0) {
                                        StreamDelaySaver.getInstance().packetDelayFile.flush();
                                    }
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            if (StreamSaver.SAVE_videoUsb_Open) {
                                StreamSaver.getInstance(StreamSaver.SAVE_videoUsb_Name).write(buffer, 0, length);
                            }
                        } else {
                            DJIVideoDataRecver.getInstance().onVideoRecv(buffer, length, true);
                        }
                        if (UsbHostService.this.getVideoIndex == UsbHostService.this.videoList.size() - 1) {
                            int unused2 = UsbHostService.this.getVideoIndex = 0;
                        } else {
                            UsbHostService.access$908(UsbHostService.this);
                        }
                        try {
                            Thread.sleep(0, 100);
                        } catch (InterruptedException e3) {
                            e3.printStackTrace();
                        }
                    }
                }
            }
            Thread unused3 = UsbHostService.this.parseVideoThread = null;
            UsbHostService.this.print("ParseVideoRunnable.end");
        }
    }

    public synchronized void sendmessage(SendPack buffer) {
        if (this.outEndpoint == null) {
            buffer.bufferObject.noUsed();
        } else {
            this.connection.bulkTransfer(this.outEndpoint, buffer.buffer, buffer.getLength(), 100);
            buffer.bufferObject.noUsed();
        }
    }

    public boolean isConnected() {
        return this.usbReceiver.isGetedConnection();
    }

    public boolean isOK() {
        return isConnected();
    }

    public void destroy() {
        this.mVodRun = false;
        this.mOsdRun = false;
        this.mParseRun = false;
        this.recvVodThread = null;
        this.recvOsdThread = null;
        this.parseOsdThread = null;
        instance = null;
    }

    private void printRate(int length) {
        this.count += length;
        if (getTickCount() - this.lastT > 1000) {
            System.out.println(String.format(Locale.US, "video rate %d KB\n", Integer.valueOf(this.count / 1024)));
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
        return true;
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
        this.recvVodThread = null;
        this.recvOsdThread = null;
        this.parseOsdThread = null;
        instance = null;
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.NON);
    }

    public void onConnect() {
        System.out.println("serial USBHost DJILinkDaemonService.getInstance().setLinkType(DJILinkType.HOST) ");
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.HOST);
    }
}
