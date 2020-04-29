package dji.midware.usb.P3;

import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.MidWare;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.packplugin.DJIPackWatchPlugin;
import dji.midware.data.manager.packplugin.record.DJIPackRecordPlugin;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.media.DJIAudioUtil;
import dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser;
import dji.midware.parser.plugins.DJIRingBufferModel;
import dji.midware.reflect.MidwareInjectManager;
import dji.midware.reflect.MidwareToP3Injectable;
import dji.midware.usb.P3.AoaRawDataReceiver;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamSaver;
import dji.midware.wsbridge.BridgeWSConnectionManager;
import java.io.InputStream;
import java.io.OutputStream;
import org.msgpack.core.MessagePack;

@Keep
@EXClassNullAway
public class UsbAccessoryService implements DJIServiceInterface {
    public static boolean ENABLE_SEND_DATA_FOR_INNER = true;
    public static boolean STREAM_DEBUG = false;
    private static final boolean USE_DISRUPTOR_RINGBUFFER = false;
    private static final int _1KB = 1024;
    private static UsbAccessoryService instance = null;
    /* access modifiers changed from: private */
    public static DJIUsbAccessoryReceiver mUsbReceiver;
    private final String TAG = getClass().getSimpleName();
    private boolean isPauseRarseThread = false;
    private boolean isPauseService = false;
    /* access modifiers changed from: private */
    public volatile InputStream mAoaInputStream;
    /* access modifiers changed from: private */
    public OutputStream mAoaOutputStream;
    private AoaRawDataReceiver mAoaRawDataReceiver;
    private VideoRawBufferReceiver mFpvCameraReceiver = new VideoRawBufferReceiver(VideoStreamSource.Fpv, this, "parseVideoFromFpvThread");
    private VideoRawBufferReceiver mMainCameraReceiver = new VideoRawBufferReceiver(VideoStreamSource.Camera, this, "parseVideoFromCamThread");
    private AoaRawChannelHandler mRingBufferParserListener;
    private StreamDataObserver mSendMessageObserver;
    private VideoDataTransferor mVideoDataTransferor = new VideoDataTransferor(this.mMainCameraReceiver, this.mFpvCameraReceiver);
    private int sendIoError = 0;
    private Object startParseThreadLock = new Object();

    @Keep
    public static class VideoBufferInfo {
        public byte[] buffer;
        public int length = -1;
    }

    public static synchronized UsbAccessoryService getInstance() {
        UsbAccessoryService usbAccessoryService;
        synchronized (UsbAccessoryService.class) {
            if (instance == null) {
                instance = new UsbAccessoryService();
            }
            usbAccessoryService = instance;
        }
        return usbAccessoryService;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public static void DestroyFinal() {
        if (instance != null) {
            if (mUsbReceiver != null) {
                mUsbReceiver.clearTimer();
            }
            instance.destroy();
        }
    }

    public UsbAccessoryService() {
        this.mMainCameraReceiver.setVideoDataTransferor(this.mVideoDataTransferor);
        this.mFpvCameraReceiver.setVideoDataTransferor(this.mVideoDataTransferor);
        this.mRingBufferParserListener = new AoaRawChannelHandler(this.mVideoDataTransferor);
        startStream();
        DJIRingBufferModel bufferModel = new DJIRingBufferModel();
        bufferModel.header = new byte[]{85, MessagePack.Code.UINT8};
        bufferModel.secondHeaderLen = 6;
        this.mAoaRawDataReceiver = new AoaRawDataReceiver(new DJIPluginRingBufferAsyncParser(DJIAudioUtil.AAC_PACKET_MAX_SIZE, bufferModel, this.mRingBufferParserListener), this, new AoaRawDataReceiver.OnParseEndListener() {
            /* class dji.midware.usb.P3.UsbAccessoryService.AnonymousClass1 */

            public void onReceiverEnd() {
                InputStream unused = UsbAccessoryService.this.mAoaInputStream = null;
                OutputStream unused2 = UsbAccessoryService.this.mAoaOutputStream = null;
                UsbAccessoryService.mUsbReceiver.triggerDisconnect();
            }
        });
    }

    public DJIPackManager getPackManager() {
        return this.mRingBufferParserListener.getCmdDataReceiver();
    }

    public static void registerAoaReceiver(Context ctx) {
        Context context = ctx.getApplicationContext();
        mUsbReceiver = new DJIUsbAccessoryReceiver();
        mUsbReceiver.start(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DJIUsbAccessoryReceiver.ACTION_USB_PERMISSION);
        intentFilter.addAction("android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_ACCESSORY_DETACHED");
        intentFilter.addAction(DJIUsbAccessoryReceiver.ACTION_USB_ACCESSORY_ATTACHED);
        intentFilter.addAction("com.dji.accessory.USB_ACCESSORY_ATTACHED");
        intentFilter.addAction(DJIUsbAccessoryReceiver.ACTION_USB_STATE);
        context.registerReceiver(mUsbReceiver, intentFilter);
    }

    public void destroy() {
        this.mAoaInputStream = null;
        this.mAoaOutputStream = null;
        instance = null;
        this.mAoaRawDataReceiver.stopParse();
        Log.e(this.TAG, "final destroy() 71");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        Log.e(this.TAG, "final destroy() 72");
        if (mUsbReceiver != null) {
            mUsbReceiver.destroySession();
            mUsbReceiver.unregisterReceiver();
        }
        Log.e(this.TAG, "final destroy() 73");
        if (StreamSaver.SAVE_videoUsb_Open) {
            StreamSaver.getInstance(StreamSaver.SAVE_videoUsb_Name).destroy();
            VideoStreamSource[] values = VideoStreamSource.values();
            int length = values.length;
            for (int i = 0; i < length; i++) {
                StreamSaver.getInstance("dji_video_usbaccessary_" + values[i].name()).destroy();
            }
        }
        Log.e(this.TAG, "final destroy() 75");
        stopParseVideoThread();
        Log.e(this.TAG, "final destroy() 76");
    }

    public void startStream() {
        this.mRingBufferParserListener.setStartStream(true);
        Log.d(this.TAG, "accessory startStream");
    }

    public void stopStream() {
        this.mRingBufferParserListener.setStartStream(false);
        Log.d(this.TAG, "accessory stopStream");
    }

    public void startThreads() {
        onConnect();
        if (MidWare.isBridgeEnabled()) {
            this.mAoaInputStream = BridgeWSConnectionManager.getInstance().getInputStream();
            this.mAoaOutputStream = BridgeWSConnectionManager.getInstance().getOutputStream();
        } else {
            this.mAoaInputStream = mUsbReceiver.getInputStream();
            this.mAoaOutputStream = mUsbReceiver.getOutputStream();
        }
        if (this.mAoaInputStream != null) {
            startRecvBufferThread();
            startParseVideoThread();
        }
    }

    public void startWSBridge() {
        if (MidWare.isBridgeEnabled()) {
            this.mAoaInputStream = BridgeWSConnectionManager.getInstance().getInputStream();
            this.mAoaOutputStream = BridgeWSConnectionManager.getInstance().getOutputStream();
        }
    }

    private void startRecvBufferThread() {
        print("recvBufferThread.start");
        this.mAoaRawDataReceiver.startParse(this.mAoaInputStream);
    }

    public void startParseVideoThread() {
        synchronized (this.startParseThreadLock) {
            print("parseVideoFromCamThread.start");
            this.mMainCameraReceiver.start();
            print("parseVideoFromFpvThread.start");
            this.mFpvCameraReceiver.start();
        }
    }

    public void stopParseVideoThread() {
        synchronized (this.startParseThreadLock) {
            if (this.mMainCameraReceiver != null) {
                this.mMainCameraReceiver.stopAndNotify();
            }
            if (this.mFpvCameraReceiver != null) {
                this.mFpvCameraReceiver.stopAndNotify();
            }
        }
    }

    @Keep
    public enum VideoStreamSource {
        Camera(0),
        Fpv(1),
        SecondaryCamera(2),
        Unknown(255);
        
        private int index;

        private VideoStreamSource(int index2) {
            this.index = index2;
        }

        public int getIndex() {
            return this.index;
        }

        public static VideoStreamSource find(int index2) {
            VideoStreamSource[] values = values();
            for (VideoStreamSource source : values) {
                if (source.getIndex() == index2) {
                    return source;
                }
            }
            return Unknown;
        }
    }

    public void sendmessage(byte[] buffer) {
        if (this.mAoaOutputStream != null) {
            try {
                this.mAoaOutputStream.write(buffer, 0, buffer.length);
                this.mAoaOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isInnerOrDebug() {
        MidwareToP3Injectable toP3Injectable = MidwareInjectManager.getMidwareToP3Injectable();
        return toP3Injectable != null && (toP3Injectable.isDevelopPackage() || toP3Injectable.isInnerPackage());
    }

    public synchronized void sendmessage(SendPack buffer) {
        if (this.mAoaOutputStream == null || !ENABLE_SEND_DATA_FOR_INNER) {
            buffer.bufferObject.noUsed();
        } else {
            if (isInnerOrDebug()) {
                DJIPackRecordPlugin.getInstance().savePack(buffer, DJIPackRecordPlugin.PackType4Plugin.SEND);
                DJIPackWatchPlugin.getInstance().onCmdCome(buffer, DJIPackRecordPlugin.PackType4Plugin.SEND);
            }
            try {
                this.mAoaOutputStream.write(buffer.buffer, 0, buffer.getLength());
                this.mAoaOutputStream.flush();
                this.sendIoError = 0;
                if (this.mSendMessageObserver == null) {
                    this.mSendMessageObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.SendMessage);
                }
                this.mSendMessageObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) buffer.getLength());
            } catch (Exception e) {
                e.printStackTrace();
                this.sendIoError++;
                if (this.sendIoError >= 10) {
                    this.sendIoError = 0;
                    AoaReportHelper.getInstance().writeError();
                }
            }
            buffer.bufferObject.noUsed();
        }
    }

    public boolean isConnected() {
        return isConnectedToProduct();
    }

    public boolean isOK() {
        return isConnectedToProduct();
    }

    public static boolean isConnectedToProduct() {
        return (mUsbReceiver != null && mUsbReceiver.isGetedConnection()) || MidWare.isBridgeEnabled();
    }

    public static void disconnect() {
        if (mUsbReceiver != null) {
            mUsbReceiver.triggerDisconnect();
        }
    }

    public static boolean isWaitingForDisconnect() {
        return mUsbReceiver != null && mUsbReceiver.isWaitingForDisconnect();
    }

    public static void connect() {
        if (mUsbReceiver != null) {
            mUsbReceiver.toConnect(true);
        }
    }

    private void print(String s) {
        DJILogHelper.getInstance().LOGD(this.TAG, s);
        DJILog.saveConnectDebug(this.TAG + " " + s);
    }

    public DJIUsbAccessoryReceiver.UsbModel getUsbModel() {
        return mUsbReceiver.getCurrentModel();
    }

    public boolean isRemoteOK() {
        return this.mRingBufferParserListener.getCmdDataReceiver().isRemoteConnected();
    }

    public void setDataMode(boolean dataMode) {
        this.mVideoDataTransferor.setDataMode(dataMode);
    }

    public void pauseService(boolean isPause) {
        if (this.isPauseService != isPause) {
            this.isPauseService = isPause;
            this.mAoaRawDataReceiver.setPauseService(this.isPauseService);
            if (this.isPauseService) {
                DJIPackManager.getInstance().pauseConnectCheck();
            } else {
                DJIPackManager.getInstance().resumeConnectCheck();
            }
        }
    }

    public void pauseRecvThread() {
        this.mRingBufferParserListener.setPauseRecvThread(true);
    }

    public void resumeRecvThread() {
        this.mRingBufferParserListener.setPauseRecvThread(false);
    }

    public void pauseParseThread() {
        this.isPauseRarseThread = true;
    }

    public void resumeParseThread() {
        this.isPauseRarseThread = false;
    }

    public void onDisconnect() {
        this.mAoaInputStream = null;
        this.mAoaOutputStream = null;
        this.mAoaRawDataReceiver.stopParse();
        stopParseVideoThread();
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.NON);
    }

    public void onConnect() {
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.AOA);
    }
}
