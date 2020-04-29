package dji.logic.album.manager.litchis;

import android.util.Pair;
import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumFileInfo;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.model.litchis.DataAppRequest;
import dji.midware.data.model.litchis.DataCameraFileSystemFileData;
import dji.midware.data.model.litchis.DataCameraFileSystemPush;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.data.model.litchis.DataRequestAck;
import dji.midware.data.model.litchis.DataRequestFile;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.data.queue.P3.PackUtil;
import dji.midware.interfaces.DJIDataCallBack;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIFileLoader extends DJILoader<DJIAlbumFile> {
    private static final int ACK_TIMEOUT = 1000;
    private static final String LOG_TAG = "play_back_file_download";
    private static final int MAX_RESEND_RANGES_COUNT = 64;
    private static final int PACKAGE_WRAPPER_BUFFER_SIZE = 10000;
    private static final int _1KB = 1024;
    private static final int _1MB = 1048576;
    private static int sHitPackCount = 0;
    private static int sLossPackCount = 0;
    private static int sLossPackLen = 10;
    private static int sPackLossJumpLen = 99;
    private static int sPackLossLen = 100;
    protected DJIAlbumFile alburmFile = new DJIAlbumFile();
    protected byte[] buffer = new byte[2097152];
    protected DJIAlbumFileInfo fileInfo;
    private boolean isOpenSliceMode = false;
    private boolean isPanoSubimageDownload;
    boolean lastPackageGetted = false;
    int lastSeq;
    long logCount = 0;
    private LinkedBlockingQueue<BufferElement> mBufferQueue = new LinkedBlockingQueue<>();
    private volatile int mLastHitSeq;
    private List<PackageWrapper> mPackageWrapperList;
    private final Object mPackageWrapperListLock = new Object();
    private long mReceivedDataSize;
    private boolean mSupportSegmentRetransmission;
    private boolean mSupportSlidingWindow;
    private BufferElement mWriteBufferElement;
    protected String nameKey;
    int prePackageSeq;
    int recvPackCount;
    private int sliseIndex = 1;
    private int sliseLen = 41943040;
    private int sliseNum = 0;
    protected DataConfig.SubType subType;
    private long tOffset = 0;

    public DJIFileLoader() {
        this.timeOutMax = 15;
        this.checkDelay = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        this.mPackageWrapperList = new ArrayList(10000);
    }

    public void destroy() {
        destroyMe();
        BufferElement.releasePool();
        this.mPackageWrapperList.clear();
    }

    public void setListener(DJIAlbumFileInfo fileInfo2, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        this.fileInfo = fileInfo2;
        this.listener = listener;
        this.alburmFile.index = fileInfo2.index;
        this.alburmFile.length = fileInfo2.length;
        this.alburmFile.fileType = fileInfo2.fileType;
    }

    /* access modifiers changed from: protected */
    public void getCacheKey() {
        this.nameKey = this.fileInfo.getNameKey();
    }

    public void start() {
        boolean z;
        DJILog.d(this.TAG, "Start Download File index=" + this.fileInfo.index, new Object[0]);
        super.start();
        this.timeOutMax = 15;
        this.offset = 0;
        this.tOffset = 0;
        this.offsetTmp = 0;
        this.resending = false;
        ProductType type = DJIProductManager.getInstance().getType();
        this.mSupportSegmentRetransmission = type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM245 || type == ProductType.WM160;
        if (type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM245 || type == ProductType.WM160) {
            z = true;
        } else {
            z = false;
        }
        this.mSupportSlidingWindow = z;
        if (!cacheCheck()) {
            ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            DJIVideoPackManager.getInstance().start();
            if (this.isOpenSliceMode) {
                this.sliseNum = ((int) ((this.fileInfo.length - this.tOffset) / ((long) this.sliseLen))) + 1;
                if ((this.fileInfo.length - this.tOffset) % ((long) this.sliseLen) == 0) {
                    this.sliseNum--;
                }
            }
            startMe();
            clearTimeout();
            resetRetransmissionTask();
            scheduleRetransmissionTask();
            synchronized (this.mPackageWrapperListLock) {
                if (this.mPackageWrapperList.isEmpty()) {
                    for (int i = 0; i < 10000; i++) {
                        this.mPackageWrapperList.add(new PackageWrapper());
                    }
                } else {
                    for (int i2 = 0; i2 < 10000; i2++) {
                        this.mPackageWrapperList.get(i2).release();
                    }
                }
            }
            this.mLastHitSeq = 0;
            this.mBufferQueue.clear();
            this.mWriteBufferElement = BufferElement.obtainBufferElement();
            this.curSeq = 0;
            ((DataAppRequest) DataRequestFile.getInstance().setIndex(this.fileInfo.index).setSubIndex(this.fileInfo.subIndex).setNum(1).setSubType(this.subType).setSize(getSliceSize()).setOffset(this.tOffset).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            this.isAlive = true;
            if (this.mSupportSlidingWindow && !this.mParseHandler.hasMessages(9)) {
                this.mParseHandler.sendEmptyMessage(9);
            }
        }
    }

    private void restart() {
        if (this.sliseNum != 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.isAlive) {
                this.curSeq = 0;
                this.sliseIndex++;
                ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
                ((DataAppRequest) DataRequestFile.getInstance().setIndex(this.fileInfo.index).setNum(1).setSubType(this.subType).setSize(getSliceSize()).setOffset(this.tOffset).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            }
        }
    }

    private void restartWithSlidingWindow() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.isAlive) {
            this.curSeq = 0;
            this.mLastHitSeq = 0;
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            ((DataAppRequest) DataRequestFile.getInstance().setIndex(this.fileInfo.index).setNum(1).setSubType(this.subType).setSize(getSliceSize()).setOffset(this.mReceivedDataSize).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        }
    }

    private long getSliceSize() {
        if (this.sliseNum == 0) {
            return -1;
        }
        return (long) (this.sliseIndex == this.sliseNum ? -1 : this.sliseLen);
    }

    private boolean cacheCheck() {
        this.tOffset = this.cacheManager.getLenCacheInDisk(this.nameKey);
        this.mReceivedDataSize = this.tOffset;
        if (this.tOffset != this.fileInfo.length || this.isPanoSubimageDownload) {
            this.offsetTmp = this.tOffset;
            this.cacheManager.openStream(this.nameKey);
            return false;
        }
        this.alburmFile.bitmap = this.cacheManager.getBitmapFromDisk(this.nameKey);
        this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
        return true;
    }

    public void stop() {
        stopMe();
        if (this.cacheManager != null) {
            this.cacheManager.closeStream(this.fileInfo.createTime);
        }
        if (this.mSupportSlidingWindow) {
            resetRetransmissionTask();
            this.mPackageWrapperList.clear();
            this.mParseHandler.removeMessages(9);
        }
        DJIVideoPackManager.getInstance().stop();
    }

    public void abort(boolean isTimeout) {
        if (this.isAlive) {
            ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            if (!isTimeout) {
                this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.CLIENT_ABORT));
            }
        }
        stop();
    }

    /* access modifiers changed from: protected */
    public void countRate() {
        long rate = this.mReceivedDataSize - this.offsetTmp;
        if (rate < 0) {
            rate = 0;
        }
        this.listener.onRateUpdate(this.alburmFile.length, this.mReceivedDataSize, rate);
        this.offsetTmp = this.mReceivedDataSize;
    }

    /* access modifiers changed from: protected */
    public void countProgress() {
        this.listener.onProgress(this.alburmFile.length, this.tOffset);
    }

    /* access modifiers changed from: protected */
    public final void handleReceivedData(PackageWrapper wrapper) {
        if (this.isAlive && wrapper.getSeq() == this.curSeq) {
            while (wrapper.isHitted()) {
                try {
                    if (this.mWriteBufferElement.willOverFlow(wrapper.getMsgDataLen())) {
                        this.mBufferQueue.put(this.mWriteBufferElement);
                        this.mWriteBufferElement = BufferElement.obtainBufferElement();
                    }
                    this.mWriteBufferElement.arrayCopyFrom(wrapper.getData(), wrapper.getInfoDataLen(), wrapper.getMsgDataLen());
                    this.tOffset += (long) wrapper.getMsgDataLen();
                    this.curSeq++;
                    if (wrapper.isLastPackage()) {
                        DJILog.logWriteD("playback", "handle last package, curSeq=" + this.curSeq + ", total receive bytes=" + this.tOffset + ", fileInfo=" + this.fileInfo + ", flushBuffer id=" + this.mWriteBufferElement.id, "playback", new Object[0]);
                        this.mWriteBufferElement.isLastElement = true;
                        this.mBufferQueue.put(this.mWriteBufferElement);
                        wrapper.release();
                        return;
                    }
                    wrapper.release();
                    wrapper = this.mPackageWrapperList.get(this.curSeq % 10000);
                } catch (InterruptedException e) {
                    DJILog.logWriteE("playback", "handleReceivedData interrupted: " + Thread.currentThread(), "playback", new Object[0]);
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onFileDataReceived(DataCameraFileSystemFileData fileData) {
        FileRecvPack receivedPackage = fileData.getRecvPack();
        if (PackUtil.sessionId() != receivedPackage.sessionId) {
            DJILog.e(this.TAG, "Unexpected SessionId", new Object[0]);
        } else if (this.isAlive) {
            if (this.mSupportSlidingWindow) {
                this.mReceivedDataSize += (long) fileData.getMsgLen();
                if (this.recvPackCount % 100 == 0) {
                    this.handler.sendEmptyMessage(2);
                } else {
                    if (this.recvPackCount >= 2147483646) {
                        this.recvPackCount = 0;
                    }
                    this.recvPackCount++;
                }
                resetRetransmissionTask();
                scheduleRetransmissionTask();
            }
            int infoDataLen = fileData.getInfoLen();
            if (!this.mSupportSlidingWindow) {
                handlePackageReceiveDeprecated(receivedPackage, infoDataLen);
            } else {
                handlePackageReceiveWithSlidingWindow(receivedPackage, infoDataLen);
            }
        }
    }

    private void writeFile() {
        this.cacheManager.writeBuffer(this.buffer, 0, this.offset);
        this.offset = 0;
    }

    /* access modifiers changed from: protected */
    public void flushBuffer() {
        super.flushBuffer();
        while (this.isAlive) {
            try {
                BufferElement readBufferElement = this.mBufferQueue.take();
                if (readBufferElement.isLastElement) {
                    receiveSuccess(readBufferElement);
                } else {
                    this.cacheManager.writeBuffer(readBufferElement.getBuffer(), 0, readBufferElement.getOffset());
                }
                BufferElement.releaseBufferElement(readBufferElement);
            } catch (InterruptedException e) {
                DJILog.logWriteE("playback", "flushBuffer thread interrupted: " + Thread.currentThread(), "playback", new Object[0]);
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void reSend() {
        if (!this.resending) {
            this.resending = true;
            DJIVideoPackManager.getInstance().clearVideoData();
            if (this.curSeq == 0) {
                if (DJIProductManager.getInstance().getType() == ProductType.Mammoth) {
                    ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(1).setMissRanges(null).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
                }
                ((DataAppRequest) DataRequestFile.getInstance().setDoNotChangeSessionId().setIndex(this.fileInfo.index).setSubIndex(this.fileInfo.subIndex).setNum(1).setSubType(this.subType).setSize(getSliceSize()).setOffset(this.tOffset).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            } else {
                ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(1).setMissRanges(null).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            }
            freshPushStatus();
        }
    }

    /* access modifiers changed from: protected */
    public void resendWithSlidingWindow() {
        super.resendWithSlidingWindow();
        if (this.isAlive) {
            DJIVideoPackManager.getInstance().clearVideoData();
            if (this.curSeq == 0) {
                DJILog.e(this.TAG, "First Package UnReceive, Restart Download Session", new Object[0]);
                ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
                ((DataAppRequest) DataRequestFile.getInstance().setDoNotChangeSessionId().setIndex(this.fileInfo.index).setSubIndex(this.fileInfo.subIndex).setNum(1).setSubType(this.subType).setSize(getSliceSize()).setOffset(this.tOffset).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            } else if (this.mSupportSegmentRetransmission) {
                resendMissingRanges();
            } else {
                ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(1).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            }
            scheduleRetransmissionTask();
        }
    }

    /* access modifiers changed from: protected */
    public void recvOver() {
        ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        writeFile();
        stopMe();
        this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
    }

    private void resendMissingRanges() {
        List<Pair<Integer, Integer>> resendRanges;
        List<Pair<Integer, Integer>> missingRanges = calculateMissingRanges();
        if (missingRanges.size() == 0) {
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            return;
        }
        if (missingRanges.size() > 64) {
            resendRanges = missingRanges.subList(0, 63);
        } else {
            resendRanges = missingRanges;
        }
        DJILog.logWriteD("playback", "resendMissingRanges: curSeq=" + this.curSeq + ", mLastHitSeq=" + this.mLastHitSeq + ", missNum=" + resendRanges.size() + ", missRanges=" + resendRanges, "playback", new Object[0]);
        ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(resendRanges.size()).setMissRanges(resendRanges).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
    }

    private void receiveSuccess(BufferElement readBuffer) {
        ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        this.cacheManager.writeBuffer(readBuffer.getBuffer(), 0, readBuffer.getOffset());
        stopMe();
        this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemPush push) {
        if (this.isAlive) {
            if (this.tOffset == this.fileInfo.length) {
                ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            } else if (!this.mSupportSlidingWindow) {
                checkPushStatus();
                ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            } else {
                resendMissingRanges();
            }
        }
    }

    private List<Pair<Integer, Integer>> calculateMissingRanges() {
        int startSeq = this.curSeq;
        int index = startSeq % 10000;
        int offset = this.mLastHitSeq - startSeq;
        int missingPackagesCount = 0;
        int hitPackagesCount = 0;
        List<Pair<Integer, Integer>> missingRanges = new ArrayList<>();
        synchronized (this.mPackageWrapperListLock) {
            for (int i = index; i < index + offset; i++) {
                if (!this.mPackageWrapperList.get(i % 10000).isHitted()) {
                    if (hitPackagesCount != 0) {
                        startSeq += hitPackagesCount;
                        hitPackagesCount = 0;
                    }
                    missingPackagesCount++;
                } else {
                    hitPackagesCount++;
                    if (missingPackagesCount != 0) {
                        missingRanges.add(new Pair<>(Integer.valueOf(startSeq), Integer.valueOf(missingPackagesCount)));
                        startSeq += missingPackagesCount;
                        missingPackagesCount = 0;
                        if (missingRanges.size() == 64) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        if (missingPackagesCount != 0 && missingRanges.size() < 64) {
            missingRanges.add(new Pair<>(Integer.valueOf(startSeq), Integer.valueOf(missingPackagesCount)));
        }
        return missingRanges;
    }

    private void handlePackageReceiveDeprecated(FileRecvPack receivedPackage, int infoDataLen) {
        int msgDataLen;
        if (receivedPackage.seq == this.curSeq) {
            checkPushStatus();
            this.resending = false;
            if (receivedPackage.seq == 0) {
                msgDataLen = receivedPackage.data.length - infoDataLen;
            } else {
                msgDataLen = receivedPackage.data.length;
            }
            this.mReceivedDataSize += (long) msgDataLen;
            System.arraycopy(receivedPackage.data, infoDataLen, this.buffer, this.offset, msgDataLen);
            this.offset += msgDataLen;
            this.tOffset += (long) msgDataLen;
            this.handler.sendEmptyMessage(2);
            if (receivedPackage.isLastFlag != 1) {
                this.curSeq++;
                if (this.offset > this.buffer.length - 2048) {
                    writeFile();
                }
            } else if (this.tOffset == this.fileInfo.length) {
                recvOver();
            } else if (this.tOffset > this.fileInfo.length) {
                recvOver();
            } else {
                restart();
            }
        } else {
            this.lastSeq = receivedPackage.seq;
            reSend();
        }
    }

    private void handlePackageReceiveWithSlidingWindow(FileRecvPack receivedPackage, int infoDataLen) {
        int packageSeq = receivedPackage.seq;
        int index = packageSeq % 10000;
        synchronized (this.mPackageWrapperListLock) {
            PackageWrapper wrapper = this.mPackageWrapperList.get(index);
            if (packageSeq < this.curSeq || wrapper.isHitted()) {
                DJILog.e(this.TAG, "Unexpected Seq=" + packageSeq + "curSeq=" + this.curSeq + " isHit=" + wrapper.isHitted(), new Object[0]);
            } else if (packageSeq > (this.curSeq + 10000) - 1) {
                DJILog.e(this.TAG, "Out of Window Range", new Object[0]);
            } else {
                this.prePackageSeq = packageSeq;
                if (packageSeq > this.mLastHitSeq) {
                    this.mLastHitSeq = packageSeq;
                }
                wrapper.setPackageData(receivedPackage, infoDataLen);
                if (wrapper.isLastPackage()) {
                    DJILog.d(this.TAG, "Last Package Received", new Object[0]);
                }
                handleReceivedData(wrapper);
            }
        }
    }

    private void resetRetransmissionTask() {
        this.ackTimeoutCount = 0;
        this.handler.removeMessages(8);
    }

    private void scheduleRetransmissionTask() {
        if (!this.handler.hasMessages(8)) {
            this.handler.sendEmptyMessageDelayed(8, 1000);
        }
    }

    public void setType(DataConfig.SubType subType2) {
        this.subType = subType2;
        switch (subType2) {
            case Pano:
                this.checkDelay = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
                this.nameKey = this.fileInfo.getFullPanoNameKey();
                this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
                this.isPanoSubimageDownload = true;
                return;
            case ORG:
                this.checkDelay = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
                getCacheKey();
                this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
                this.isPanoSubimageDownload = false;
                return;
            default:
                this.listener.onFailure(DJIAlbumPullErrorType.ERROR_REQ);
                this.isPanoSubimageDownload = false;
                return;
        }
    }

    private static class PackageWrapper {
        private byte[] data = new byte[1024];
        private int dataLen = 0;
        private int infoDataLen = 0;
        private boolean isLastPackage = false;
        private boolean mHit = false;
        private int msgDataLen = 0;
        private int seq = -1;

        PackageWrapper() {
        }

        public void setPackageData(FileRecvPack receivedPackage, int infoDataLen2) {
            boolean z = false;
            byte[] buffer = receivedPackage.buffer;
            if (buffer != null && buffer.length >= 10) {
                int headerLen = buffer[0] & 63;
                this.dataLen = buffer.length - headerLen;
                if (this.dataLen > this.data.length) {
                    this.data = new byte[this.dataLen];
                }
                if (this.dataLen > 0) {
                    System.arraycopy(buffer, headerLen, this.data, 0, this.dataLen);
                }
            }
            this.infoDataLen = infoDataLen2;
            this.msgDataLen = this.dataLen - infoDataLen2;
            this.seq = receivedPackage.seq;
            if (receivedPackage.isLastFlag == 1) {
                z = true;
            }
            this.isLastPackage = z;
            this.mHit = true;
        }

        public void release() {
            this.seq = -1;
            this.infoDataLen = 0;
            this.msgDataLen = 0;
            this.mHit = false;
            this.dataLen = 0;
            this.isLastPackage = false;
        }

        public int getSeq() {
            checkState();
            return this.seq;
        }

        public int getInfoDataLen() {
            checkState();
            return this.infoDataLen;
        }

        public int getMsgDataLen() {
            checkState();
            return this.msgDataLen;
        }

        public boolean isHitted() {
            return this.mHit;
        }

        public int getDataLen() {
            checkState();
            return this.dataLen;
        }

        public byte[] getData() {
            checkState();
            return this.data;
        }

        public boolean isLastPackage() {
            checkState();
            return this.isLastPackage;
        }

        private void checkState() {
            if (!this.mHit) {
                DJILog.logWriteE("playback", "use a invalid wrapper!" + this, new Object[0]);
            }
        }

        public String toString() {
            return "wrapper seq:" + this.seq + ", infoDataLen=" + this.infoDataLen + ", msgDataLen=" + this.msgDataLen + ", isLastPackage=" + this.isLastPackage;
        }
    }

    private static class BufferElement {
        private static AtomicInteger sAvailableId = new AtomicInteger(0);
        private static final List<BufferElement> sBufferElementPool = new ArrayList();
        byte[] buffer = new byte[2097152];
        int id = sAvailableId.getAndIncrement();
        boolean isLastElement = false;
        int offset = 0;

        BufferElement() {
        }

        public boolean willOverFlow(int len) {
            return this.offset + len > this.buffer.length;
        }

        public int getSize() {
            return this.buffer.length;
        }

        public int getOffset() {
            return this.offset;
        }

        public byte[] getBuffer() {
            return this.buffer;
        }

        public void arrayCopyFrom(byte[] data, int pos, int len) {
            if (willOverFlow(len)) {
                throw new ArrayIndexOutOfBoundsException("BufferItem 溢出");
            }
            System.arraycopy(data, pos, this.buffer, this.offset, len);
            this.offset += len;
        }

        static BufferElement obtainBufferElement() {
            synchronized (sBufferElementPool) {
                int size = sBufferElementPool.size();
                if (size <= 0) {
                    return new BufferElement();
                }
                BufferElement bufferElement = sBufferElementPool.remove(size - 1);
                bufferElement.offset = 0;
                bufferElement.isLastElement = false;
                bufferElement.id = sAvailableId.getAndIncrement();
                return bufferElement;
            }
        }

        static void releaseBufferElement(BufferElement bufferElement) {
            bufferElement.offset = 0;
            bufferElement.isLastElement = false;
            synchronized (sBufferElementPool) {
                if (sBufferElementPool.size() < 5) {
                    sBufferElementPool.add(bufferElement);
                }
            }
        }

        static void releasePool() {
            synchronized (sBufferElementPool) {
                sBufferElementPool.clear();
                sAvailableId.set(0);
            }
        }
    }
}
