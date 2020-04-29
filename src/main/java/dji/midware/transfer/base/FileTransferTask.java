package dji.midware.transfer.base;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.dji.frame.util.MD5;
import dji.log.DJILogUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonTransferFileData;
import dji.midware.data.model.P3.DataCommonTransferFileRequest;
import dji.midware.data.model.P3.DataCommonTransferFileVerify;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataRcGetPushFlowControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.natives.GroudStation;
import dji.midware.transfer.FileTransferLog;
import dji.midware.transfer.base.ITransferFileObject;
import dji.midware.util.BytesUtil;
import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FileTransferTask {
    private static final int MILLISECOND_TO_NANOSECOND = 1000000;
    private static final int MSG_ACK_RECEIVE = 3;
    private static final int MSG_RATE_TICK = 5;
    private static final int MSG_REQUEST_SUCCESS = 2;
    private static final int MSG_REWIND_TRANSFER = 6;
    private static final int MSG_START = 1;
    private static final int MSG_VERIFY = 4;
    private static final int PACK_RATE_TICK_MILLISECOND = 1000;
    private static final int SLEEP_MILLISECOND_MAX = 999999;
    boolean isFullQueue = false;
    long mAckCount;
    int mCurrentNum = 0;
    long mExceptionAckCount;
    /* access modifiers changed from: private */
    public File mFile;
    private String mFileName;
    private byte[] mFileParameter;
    private int mFileSize = 0;
    private ITransferFileObject.CommonTransferFileType mFileType = ITransferFileObject.CommonTransferFileType.OTHER;
    AtomicInteger mFreeQueueNum;
    /* access modifiers changed from: private */
    public TransferHandler mHandler;
    private HandlerThread mHandlerThread = new HandlerThread("file_transfer_check_thread");
    /* access modifiers changed from: private */
    public volatile boolean mIsDestroyed = false;
    /* access modifiers changed from: private */
    public volatile boolean mIsFinish = false;
    int[] mPackReSendArray;
    volatile int mPackSendPeriod = 0;
    /* access modifiers changed from: private */
    public int mPackageLength = 40;
    /* access modifiers changed from: private */
    public IPacketHandler mPacketHandler;
    /* access modifiers changed from: private */
    public ITransferFileObject.CommonTransferProtocolType mProtocolType = ITransferFileObject.CommonTransferProtocolType.OTHER;
    DeviceType mReceiveType = DeviceType.CAMERA;
    int mReceiverId = 0;
    long mResendCount;
    long mSendCount;
    int mSendSequence = 0;
    boolean[] mSendStatus;
    int mTotalNum = 0;
    FileTransferListener mTransferListener;
    FileTransferThread mTransferThread = new FileTransferThread();
    /* access modifiers changed from: private */
    public int mTransferVerifyTimes;
    /* access modifiers changed from: private */
    public ITransferFileObject.CommonTransferVerifyType mVerifyType = ITransferFileObject.CommonTransferVerifyType.OTHER;
    volatile int mWindowSequence = 0;
    int mWindowSize = 5;
    FileTransferStrategies strategy = FileTransferStrategies.APP_AOA_RC_WIFI_UAV;

    static /* synthetic */ int access$1108(FileTransferTask x0) {
        int i = x0.mTransferVerifyTimes;
        x0.mTransferVerifyTimes = i + 1;
        return i;
    }

    private class TransferHandler extends Handler {
        private TransferHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FileTransferTask.this.startTransfer(false);
                    return;
                case 2:
                    FileTransferTask.this.initWindow();
                    return;
                case 3:
                    FileTransferTask.this.handlePackAckReceive(msg.arg1, msg.arg2, (List) msg.obj);
                    return;
                case 4:
                    FileTransferTask.this.verifyFile();
                    return;
                case 5:
                    FileTransferTask.this.calculateRate(msg.arg1);
                    return;
                case 6:
                    FileTransferTask.this.rewindTransfer();
                    return;
                default:
                    return;
            }
        }
    }

    public FileTransferTask() {
        this.mHandlerThread.start();
        this.mHandler = new TransferHandler(this.mHandlerThread.getLooper());
    }

    /* access modifiers changed from: package-private */
    public void setPackSendPeriod(int packSendPeriod) {
        if (DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Mammoth || DJILinkDaemonService.getInstance().getLinkType() != DJILinkType.AOA) {
            this.mPackSendPeriod = packSendPeriod;
        }
    }

    public FileTransferTask setReceiver(@NonNull DeviceType receiveType) {
        return setReceiver(receiveType, 0);
    }

    public FileTransferTask setReceiver(@NonNull DeviceType receiveType, @NonNull int receiveId) {
        this.mReceiveType = receiveType;
        this.mReceiverId = receiveId;
        return this;
    }

    public FileTransferTask setTransferData(@NonNull byte[] data, @NonNull String fileName, @NonNull ITransferFileObject.CommonTransferFileType fileType, @Nullable byte[] parameter) {
        if (!(data == null || data.length == 0 || TextUtils.isEmpty(fileName))) {
            this.mFileName = fileName;
            this.mFileSize = data.length;
            this.mFileType = fileType;
            this.mFileParameter = parameter;
            this.mTransferThread.setData();
        }
        return this;
    }

    public FileTransferTask setTransferData(@NonNull File file, @NonNull ITransferFileObject.CommonTransferFileType fileType, @Nullable byte[] parameter) {
        if (file != null && file.exists()) {
            this.mFile = file;
            this.mFileName = this.mFile.getName();
            this.mFileSize = (int) this.mFile.length();
            this.mFileType = fileType;
            this.mFileParameter = parameter;
        }
        return this;
    }

    public FileTransferTask setTransferListener(FileTransferListener listener) {
        this.mTransferListener = listener;
        return this;
    }

    public FileTransferTask setFileTransferStrategies(FileTransferStrategies strategy2) {
        this.strategy = strategy2;
        return this;
    }

    public int[] getPackReSendMap() {
        return this.mPackReSendArray;
    }

    public boolean isFinish() {
        return this.mIsFinish;
    }

    public void start() {
        if (this.mHandler != null) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
        }
    }

    /* access modifiers changed from: private */
    public void startTransfer(boolean rewind) {
        LOGD("\n----------------------------------------------------- start transfer file -----------------------------------------------------" + (rewind ? "retry" : ""));
        if (!rewind && this.mTransferListener != null) {
            this.mTransferListener.onStart();
        }
        if (!this.mTransferThread.isDataSettled()) {
            loadFile();
        }
        if (this.mTransferThread.isDataSettled()) {
            requestFileTransfer();
        } else if (this.mTransferListener != null) {
            this.mTransferListener.onFailure(this, "handler transferListener is null", Ccode.PARAM_NOT_AVAILABLE);
        }
    }

    /* access modifiers changed from: private */
    public void rewindTransfer() {
        LOGD("rewind transfer retry times:" + this.mTransferVerifyTimes + " max times:" + this.strategy.transferVerifyMaxTimes);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (!this.mTransferThread.isInterrupted()) {
            this.mTransferThread.quit();
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
        if (this.mPacketHandler != null) {
            this.mPacketHandler.destroy();
        }
        this.mSendSequence = 0;
        this.mWindowSequence = 0;
        this.mAckCount = 0;
        this.mExceptionAckCount = 0;
        this.mSendCount = 0;
        this.mResendCount = 0;
        this.mTransferThread = new FileTransferThread();
        startTransfer(true);
    }

    public synchronized void stop(String reason) {
        LOGD("task stop reason=" + reason + " mTransferListener=" + this.mTransferListener);
        if (this.mPacketHandler != null) {
            this.mPacketHandler.destroy();
        }
        if (this.mTransferListener != null) {
            this.mTransferListener.onFailure(this, reason + " ", Ccode.USER_CANCEL);
            this.mTransferListener = null;
        }
    }

    public synchronized void destroy() {
        if (this.mIsDestroyed) {
            LOGD("task is destroyed, exception stack:");
            LOGD(DJILogUtils.getCurrentStack());
        } else {
            this.mIsDestroyed = true;
            LOGD("Task Destroy");
            if (!this.mTransferThread.isInterrupted()) {
                this.mTransferThread.quit();
            }
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages(null);
                this.mHandler = null;
            }
            this.mHandlerThread.quit();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushFlowControl flowControl) {
        setSendFlow(!flowControl.canSendFlow());
    }

    /* access modifiers changed from: protected */
    public void setSendFlow(boolean needPause) {
        if (needPause) {
            setPackSendPeriod(this.strategy.packSendPeriodMaxNanosecond);
        }
        this.mTransferThread.isPause = needPause && !isFinish();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCommonTransferFileData transferFile) {
        int ackSequence = transferFile.getAckSequence();
        int nextConsequentSequence = isNewProtocol() ? 0 : transferFile.getNextConsequentSequence();
        List<Integer> packetLoss = isNewProtocol() ? transferFile.getLostSegment() : null;
        if (this.mHandler != null) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, ackSequence, nextConsequentSequence, packetLoss));
        }
    }

    private boolean isNewProtocol() {
        return ITransferFileObject.CommonTransferProtocolType.PROTOCOL_2.equals(this.mProtocolType);
    }

    private void loadFile() {
        if (this.mFile == null || !this.mFile.exists()) {
            LOGD("check file not exists, load file failed");
            return;
        }
        try {
            this.mTransferThread.setData();
        } catch (OutOfMemoryError e) {
            LOGD("load file OOM, GC, sleep, try again");
            System.gc();
            SystemClock.sleep(100);
            this.mTransferThread.setData();
        }
    }

    private void requestFileTransfer() {
        LOGD("文件传输配置信息\n @接收模块：" + this.mReceiveType.name() + "\n @接收Id：" + this.mReceiverId + "\n @文件名：" + this.mFileName + "\n @文件大小：" + this.mFileSize + "\n @文件类型：" + this.mFileType.name());
        final DataCommonTransferFileRequest request = DataCommonTransferFileRequest.newInstance(this.mReceiveType, this.mReceiverId);
        request.setFileName(this.mFileName).setFileSize(this.mFileSize).setFileType(this.mFileType).setParameter(this.mFileParameter).start(new DJIDataCallBack() {
            /* class dji.midware.transfer.base.FileTransferTask.AnonymousClass1 */

            public void onSuccess(Object model) {
                int unused = FileTransferTask.this.mPackageLength = request.getPackageLength();
                FileTransferTask.this.mWindowSize = request.getWindowSize();
                ITransferFileObject.CommonTransferVerifyType unused2 = FileTransferTask.this.mVerifyType = request.getVerifyType();
                ITransferFileObject.CommonTransferProtocolType unused3 = FileTransferTask.this.mProtocolType = request.getProtocolType();
                if (FileTransferTask.this.mHandler != null) {
                    FileTransferTask.this.mHandler.sendMessage(FileTransferTask.this.mHandler.obtainMessage(2));
                }
            }

            public void onFailure(Ccode ccode) {
                FileTransferTask.this.LOGE("请求文件传输失败：" + ccode.name());
                if (FileTransferTask.this.mTransferListener != null) {
                    FileTransferTask.this.mTransferListener.onFailure(FileTransferTask.this, "request file transfer failed", ccode);
                }
            }
        });
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.fill(boolean[], boolean):void}
     arg types: [boolean[], int]
     candidates:
      ClspMth{java.util.Arrays.fill(double[], double):void}
      ClspMth{java.util.Arrays.fill(byte[], byte):void}
      ClspMth{java.util.Arrays.fill(long[], long):void}
      ClspMth{java.util.Arrays.fill(char[], char):void}
      ClspMth{java.util.Arrays.fill(short[], short):void}
      ClspMth{java.util.Arrays.fill(java.lang.Object[], java.lang.Object):void}
      ClspMth{java.util.Arrays.fill(int[], int):void}
      ClspMth{java.util.Arrays.fill(float[], float):void}
      ClspMth{java.util.Arrays.fill(boolean[], boolean):void} */
    /* access modifiers changed from: private */
    public void initWindow() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        LOGD("传输窗口配置信息\n @传输包长度：" + this.mPackageLength + "\n @窗口大小：" + this.mWindowSize + "\n @校验方法：" + this.mVerifyType.name() + "\n @协议类型：" + this.mProtocolType.name());
        switch (this.mProtocolType) {
            case PROTOCOL_1:
                this.mPacketHandler = new BasicPacketHandler(this);
                break;
            case PROTOCOL_2:
                this.mPacketHandler = new ExtendedPacketHandler(this);
                break;
            default:
                this.mPacketHandler = new BasicPacketHandler(this);
                break;
        }
        this.mPackSendPeriod = this.strategy.packSendPeriodMinNanosecond;
        this.mFreeQueueNum = new AtomicInteger(this.mWindowSize);
        this.mSendStatus = new boolean[this.mWindowSize];
        Arrays.fill(this.mSendStatus, false);
        this.mPackReSendArray = new int[this.mWindowSize];
        this.mTransferThread.start();
        if (this.mHandler != null) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(5, this.mCurrentNum, 0), 1000);
        }
    }

    public long getAckCount() {
        return this.mAckCount;
    }

    public long getExceptionAckCount() {
        return this.mExceptionAckCount;
    }

    public long getSendCount() {
        return this.mSendCount;
    }

    public long getResendCount() {
        return this.mResendCount;
    }

    /* access modifiers changed from: private */
    public void verifyFile() {
        byte[] verifyData;
        LOGD("校验文件信息");
        switch (this.mVerifyType) {
            case MD5:
                verifyData = MD5.getMD5(this.mFile);
                break;
            case CRC16:
                verifyData = BytesUtil.getBytes(GroudStation.native_calcCrc16(this.mTransferThread.getData()));
                break;
            default:
                if (this.mTransferListener != null) {
                    this.mTransferListener.onFailure(this, "invalid verify type", Ccode.FM_CRC_WRONG);
                    return;
                }
                return;
        }
        DataCommonTransferFileVerify.newInstance(this.mReceiveType, this.mReceiverId).setVerifyType(this.mVerifyType).setVerifyData(verifyData).start(new DJIDataCallBack() {
            /* class dji.midware.transfer.base.FileTransferTask.AnonymousClass2 */

            public void onSuccess(Object model) {
                FileTransferTask.this.LOGD("校验文件成功");
                if (FileTransferTask.this.mTransferListener != null) {
                    FileTransferTask.this.mTransferListener.onSuccess(FileTransferTask.this);
                }
            }

            public void onFailure(Ccode ccode) {
                FileTransferTask.this.LOGD("校验文件失败");
                FileTransferTask.access$1108(FileTransferTask.this);
                if (FileTransferTask.this.mTransferVerifyTimes < FileTransferTask.this.strategy.transferVerifyMaxTimes && FileTransferTask.this.mHandler != null) {
                    FileTransferTask.this.mHandler.sendEmptyMessageDelayed(6, 1000);
                } else if (FileTransferTask.this.mTransferListener != null) {
                    FileTransferTask.this.mTransferListener.onFailure(FileTransferTask.this, "verify file failed", ccode);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void calculateRate(int lastSequence) {
        float rate = ((1.0f * ((float) (this.mCurrentNum - lastSequence))) * ((float) this.mPackageLength)) / 1000.0f;
        if (this.mHandler != null) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(5, this.mCurrentNum, 0), 1000);
        }
        if (this.mTransferListener != null) {
            this.mTransferListener.onRateUpdate((float) Math.round(rate));
        }
    }

    /* access modifiers changed from: private */
    public void handlePackAckReceive(int ackSequence, int nextConsequentSequence, List<Integer> packetLoss) {
        if (this.mPacketHandler != null) {
            this.mPacketHandler.handlePackAckReceive(this, ackSequence, nextConsequentSequence, packetLoss);
        }
    }

    /* access modifiers changed from: package-private */
    public void handleTransferOver() {
        LOGD("传输完成");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(5);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(4));
        }
    }

    /* access modifiers changed from: package-private */
    public void speedControl(int nanoSecond) throws InterruptedException {
        if (nanoSecond > SLEEP_MILLISECOND_MAX) {
            Thread.sleep((long) (nanoSecond / 1000000), nanoSecond % 1000000);
        } else {
            Thread.sleep(0, nanoSecond);
        }
    }

    class FileTransferThread extends Thread {
        public volatile boolean isPause = false;
        private MappedByteBuffer mappedByteBuffer;
        private long totalSize;

        FileTransferThread() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:23:0x008e A[SYNTHETIC, Splitter:B:23:0x008e] */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x0093 A[SYNTHETIC, Splitter:B:26:0x0093] */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x00a4 A[SYNTHETIC, Splitter:B:34:0x00a4] */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x00a9 A[SYNTHETIC, Splitter:B:37:0x00a9] */
        /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setData() {
            /*
                r9 = this;
                r7 = 0
                r0 = 0
                java.io.RandomAccessFile r8 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x006b }
                dji.midware.transfer.base.FileTransferTask r1 = dji.midware.transfer.base.FileTransferTask.this     // Catch:{ IOException -> 0x006b }
                java.io.File r1 = r1.mFile     // Catch:{ IOException -> 0x006b }
                java.lang.String r2 = "rw"
                r8.<init>(r1, r2)     // Catch:{ IOException -> 0x006b }
                java.nio.channels.FileChannel r0 = r8.getChannel()     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                long r2 = r0.size()     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                r9.totalSize = r2     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                long r2 = r9.totalSize     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                r4 = 2147483647(0x7fffffff, double:1.060997895E-314)
                int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r1 <= 0) goto L_0x002b
                dji.midware.transfer.base.FileTransferTask r1 = dji.midware.transfer.base.FileTransferTask.this     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                java.lang.String r2 = "文件过大"
                r1.LOGE(r2)     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
            L_0x002b:
                java.nio.channels.FileChannel$MapMode r1 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                r2 = 0
                long r4 = r9.totalSize     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                java.nio.MappedByteBuffer r1 = r0.map(r1, r2, r4)     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                r9.mappedByteBuffer = r1     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                dji.midware.transfer.base.FileTransferTask r1 = dji.midware.transfer.base.FileTransferTask.this     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                r2.<init>()     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                java.lang.String r3 = "load file success, check isDataSettled="
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                boolean r3 = r9.isDataSettled()     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                r1.LOGD(r2)     // Catch:{ IOException -> 0x00ba, all -> 0x00b7 }
                if (r8 == 0) goto L_0x0059
                r8.close()     // Catch:{ IOException -> 0x0060 }
            L_0x0059:
                if (r0 == 0) goto L_0x00bd
                r0.close()     // Catch:{ IOException -> 0x0065 }
                r7 = r8
            L_0x005f:
                return
            L_0x0060:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0059
            L_0x0065:
                r6 = move-exception
                r6.printStackTrace()
                r7 = r8
                goto L_0x005f
            L_0x006b:
                r6 = move-exception
            L_0x006c:
                r6.printStackTrace()     // Catch:{ all -> 0x00a1 }
                dji.midware.transfer.base.FileTransferTask r1 = dji.midware.transfer.base.FileTransferTask.this     // Catch:{ all -> 0x00a1 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a1 }
                r2.<init>()     // Catch:{ all -> 0x00a1 }
                java.lang.String r3 = "load file exception "
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00a1 }
                java.lang.String r3 = r6.getMessage()     // Catch:{ all -> 0x00a1 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00a1 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00a1 }
                r1.LOGD(r2)     // Catch:{ all -> 0x00a1 }
                if (r7 == 0) goto L_0x0091
                r7.close()     // Catch:{ IOException -> 0x009c }
            L_0x0091:
                if (r0 == 0) goto L_0x005f
                r0.close()     // Catch:{ IOException -> 0x0097 }
                goto L_0x005f
            L_0x0097:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x005f
            L_0x009c:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0091
            L_0x00a1:
                r1 = move-exception
            L_0x00a2:
                if (r7 == 0) goto L_0x00a7
                r7.close()     // Catch:{ IOException -> 0x00ad }
            L_0x00a7:
                if (r0 == 0) goto L_0x00ac
                r0.close()     // Catch:{ IOException -> 0x00b2 }
            L_0x00ac:
                throw r1
            L_0x00ad:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x00a7
            L_0x00b2:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x00ac
            L_0x00b7:
                r1 = move-exception
                r7 = r8
                goto L_0x00a2
            L_0x00ba:
                r6 = move-exception
                r7 = r8
                goto L_0x006c
            L_0x00bd:
                r7 = r8
                goto L_0x005f
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.midware.transfer.base.FileTransferTask.FileTransferThread.setData():void");
        }

        public boolean isDataSettled() {
            return this.mappedByteBuffer != null && this.totalSize > 0;
        }

        public byte[] getData() {
            byte[] data = new byte[((int) this.totalSize)];
            this.mappedByteBuffer.rewind();
            this.mappedByteBuffer.get(data, 0, (int) this.totalSize);
            return data;
        }

        public void quit() {
            FileTransferTask.this.LOGD("transfer thread quit");
            interrupt();
            try {
                join(10000);
                FileTransferTask.this.LOGD("线程被终止");
            } catch (InterruptedException e) {
                e.printStackTrace();
                FileTransferTask.this.LOGD("transfer thread quit exception e=" + e.getMessage());
            }
        }

        public void run() {
            byte[] unitData;
            super.run();
            if (!isDataSettled()) {
                FileTransferTask.this.LOGE("未配置文件数据");
                return;
            }
            boolean unused = FileTransferTask.this.mIsFinish = false;
            int unitLength = FileTransferTask.this.mPackageLength;
            FileTransferTask.this.mTotalNum = (int) Math.ceil((double) ((1.0f * ((float) this.totalSize)) / ((float) unitLength)));
            FileTransferTask.this.LOGD("本次传输总包数：" + FileTransferTask.this.mTotalNum + " 文件总长度：" + this.totalSize);
            while (!FileTransferTask.this.mIsDestroyed && !Thread.currentThread().isInterrupted() && this.mappedByteBuffer.hasRemaining()) {
                try {
                    int position = this.mappedByteBuffer.position();
                    if (this.isPause) {
                        FileTransferTask.this.LOGE("因遥控器流控暂停发送");
                        Thread.sleep(200);
                    } else {
                        long time = System.nanoTime();
                        if (this.mappedByteBuffer.remaining() >= unitLength) {
                            unitData = new byte[unitLength];
                            this.mappedByteBuffer.get(unitData);
                            this.mappedByteBuffer.position(position + unitLength);
                        } else {
                            int remainLength = this.mappedByteBuffer.remaining();
                            unitData = new byte[remainLength];
                            this.mappedByteBuffer.get(unitData);
                            this.mappedByteBuffer.position(position + remainLength);
                        }
                        while (FileTransferTask.this.mFreeQueueNum.get() == 0) {
                            if (Thread.currentThread().isInterrupted()) {
                                FileTransferTask.this.LOGE("transfer thread isInterrupted, break!!!");
                                boolean unused2 = FileTransferTask.this.mIsFinish = true;
                                return;
                            } else if (!FileTransferTask.this.isFullQueue) {
                                FileTransferTask.this.LOGE("send queue free number is 0");
                                FileTransferTask.this.isFullQueue = true;
                                FileTransferTask.this.mPacketHandler.handleFullQueue();
                            }
                        }
                        FileTransferTask.this.isFullQueue = false;
                        FileTransferTask.this.mPackReSendArray[FileTransferTask.this.mSendSequence % FileTransferTask.this.mWindowSize] = 0;
                        FileTransferTask.this.sendPack(FileTransferTask.this.mSendSequence, unitData);
                        FileTransferTask.this.mFreeQueueNum.decrementAndGet();
                        FileTransferTask.this.mSendSequence++;
                        if (FileTransferTask.this.mSendSequence == FileTransferTask.this.mTotalNum) {
                            FileTransferTask.this.mPacketHandler.handleFullQueue();
                        }
                        long time2 = System.nanoTime() - time;
                        if (time2 < ((long) FileTransferTask.this.mPackSendPeriod)) {
                            int nanoTime = (int) (((long) FileTransferTask.this.mPackSendPeriod) - time2);
                            if (nanoTime > 0) {
                                FileTransferTask.this.speedControl(nanoTime);
                            } else {
                                FileTransferTask.this.LOGD("speed control exception nanoTime=" + nanoTime);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    FileTransferTask.this.stop("stop reason:InterruptedException e=" + e.getMessage());
                    e.printStackTrace();
                    return;
                } finally {
                    boolean unused3 = FileTransferTask.this.mIsFinish = true;
                }
            }
            boolean unused4 = FileTransferTask.this.mIsFinish = true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isWindowFull() {
        return this.mFreeQueueNum != null && this.mFreeQueueNum.get() == 0;
    }

    /* access modifiers changed from: private */
    public void sendPack(int sequence, byte[] data) {
        if (this.mPacketHandler != null) {
            this.mPacketHandler.sendPack(this, sequence, data, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void LOGD(String str) {
        FileTransferLog.d(str + " windowSequence=" + this.mWindowSequence);
    }

    /* access modifiers changed from: package-private */
    public void LOGE(String str) {
        FileTransferLog.e(str + " windowSequence=" + this.mWindowSequence);
    }
}
