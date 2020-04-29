package dji.logic.album.manager.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.model.DJIAlbumFile;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.model.litchis.DataAppRequest;
import dji.midware.data.model.litchis.DataCameraFileSystemPush;
import dji.midware.data.model.litchis.DataCameraFileSystemStreamData;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.data.model.litchis.DataRequestAck;
import dji.midware.data.model.litchis.DataRequestStream;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.interfaces.DJIDataCallBack;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIStreamLoader extends DJILoader<DJIAlbumFile> {
    public static final int bufferSize = 819200;
    protected DJIAlbumFile alburmFile = new DJIAlbumFile();
    protected byte[] buffer = new byte[bufferSize];
    private int foffset = 0;
    private boolean isCached = false;
    private boolean isPrepared = false;
    private boolean isSeekd = false;
    private boolean isresend = false;
    protected int mDuration;
    protected int mFileIndex;
    protected DJIFileType mFileType;
    private OnCacheFileChangeListener mOnCacheFileChangeListener;
    private OnCacheRenameListener mOnCacheRenameListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private Timer mTimer;
    protected String nameKey;
    protected String nameKeyOver;
    int persize = 262144;
    private long seekOffset = 0;
    protected long tOffset = 0;
    private int tmpSeq = 0;
    private long tmpTime = 0;
    private int ttt = 0;

    public interface OnCacheFileChangeListener {
        void onChange(long j);
    }

    public interface OnCacheRenameListener {
        void onChange();
    }

    public interface OnPreparedListener {
        void onPrepared(DJIAlbumFile dJIAlbumFile);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(DJIAlbumFile dJIAlbumFile);
    }

    public DJIStreamLoader() {
        this.timeOutMax = 3;
        this.checkDelay = 4000;
    }

    public void destroy() {
        this.buffer = null;
        destroyMe();
    }

    public void setListener(int index, DJIFileType fileType, String fileName, int duration, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        this.listener = listener;
        this.isCached = false;
        this.mFileIndex = index;
        this.mFileType = fileType;
        this.nameKey = fileName;
        this.mDuration = duration;
        this.nameKeyOver = this.nameKey + "_over";
        if (this.cacheManager.isBitmapExistInDisk(this.nameKeyOver)) {
            this.isCached = true;
            this.nameKey = this.nameKeyOver;
        }
        this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
    }

    public String getCachePath() {
        return this.alburmFile.cachPath;
    }

    public boolean isCached() {
        return this.isCached;
    }

    public void start() {
        this.isSeekd = false;
        start(0);
    }

    public void start(long offset) {
        doStart(offset);
    }

    public void seek(long offset) {
        this.isSeekd = true;
        doStart(offset);
    }

    public void restart() {
        start(this.seekOffset);
    }

    private void doStart(long seekOffset2) {
        super.start();
        this.isPrepared = false;
        this.tOffset = (seekOffset2 / 1000) * ((long) this.persize);
        this.foffset = 0;
        this.isAlive = true;
        this.curSeq = 0;
        this.nextSeq = 0;
        this.resending = false;
        if (this.isCached) {
            this.alburmFile.length = this.cacheManager.getLenCacheInDisk(this.nameKey);
            this.mOnPreparedListener.onPrepared(this.alburmFile);
            this.mOnCacheFileChangeListener.onChange(this.alburmFile.length);
            this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
            return;
        }
        DJIVideoPackManager.getInstance().start();
        this.cacheManager.openStreamCover(this.nameKey);
        ((DataAppRequest) DataRequestStream.getInstance().setIndex(this.mFileIndex).setTimeOffset(seekOffset2).setTimeLen(-1).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        startMe();
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        countRate();
        this.mTimer = new Timer("DJIStreamLoader");
        this.mTimer.schedule(new TimerTask() {
            /* class dji.logic.album.manager.litchis.DJIStreamLoader.AnonymousClass1 */

            public void run() {
                ((DataRequestAck) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.Stream).setSeq(DJIStreamLoader.this.nextSeq).setMissNum(0).setReceiverId(DJIStreamLoader.this.getReceiverIdInProtocol(), DataRequestAck.class)).start((DJIDataCallBack) null);
            }
        }, 0, 100);
    }

    public void stop() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.isAlive) {
            stopMe();
            this.cacheManager.closeStream();
            DJIVideoPackManager.getInstance().stop();
        }
    }

    public void abort(boolean isTimeout) {
        abort(DataRequestAbort.AbortReason.Force);
    }

    public void abort(DataRequestAbort.AbortReason reason) {
        DJILogHelper.getInstance().LOGD(this.TAG, "will abort " + this.isAlive, true, true);
        if (this.isAlive) {
            ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.Stream).setReason(reason).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        }
        stop();
    }

    /* access modifiers changed from: protected */
    public void countRate() {
        this.listener.onRateUpdate(this.alburmFile.length, this.tOffset, this.tOffset - this.offsetTmp);
        this.offsetTmp = this.tOffset;
    }

    /* access modifiers changed from: protected */
    public void countProgress() {
    }

    public boolean isPrepared() {
        return this.tOffset > ((long) this.buffer.length) || this.tOffset == this.alburmFile.length;
    }

    public void setPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    public void setOnCacheFileChangeListener(OnCacheFileChangeListener listener) {
        this.mOnCacheFileChangeListener = listener;
    }

    public void setOnCacheRenameListener(OnCacheRenameListener listener) {
        this.mOnCacheRenameListener = listener;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemStreamData fileData) {
        int getlen;
        if (this.isAlive) {
            FileRecvPack recvPack = fileData.getRecvPack();
            this.ttt++;
            if (this.ttt % 200 == 0) {
                DJILogHelper.getInstance().LOGD(this.TAG, "seq=" + this.nextSeq + " 实际=" + recvPack.seq, false, false);
            }
            if (recvPack.seq == this.nextSeq) {
                this.curSeq = this.nextSeq;
                this.nextSeq = recvPack.seq + 1;
                checkPushStatus();
                this.resending = false;
                int infolen = 0;
                if (recvPack.seq == 0) {
                    this.isresend = false;
                    this.tmpTime = System.currentTimeMillis();
                    infolen = fileData.getInfoLen();
                    getlen = recvPack.data.length - infolen;
                    if (!this.isPrepared) {
                        this.isPrepared = true;
                        if (this.tOffset == 0) {
                            if (this.mDuration == 0) {
                                this.alburmFile.duration = fileData.getDuration() / 1000;
                            } else {
                                this.alburmFile.duration = (long) this.mDuration;
                            }
                            this.alburmFile.length = this.alburmFile.duration * ((long) this.persize);
                            DJILogHelper.getInstance().LOGD(this.TAG, "alburmFile.duration=" + this.alburmFile.duration, false, false);
                            if (this.mOnPreparedListener != null) {
                                this.mOnPreparedListener.onPrepared(this.alburmFile);
                            }
                        } else if (this.mOnSeekCompleteListener != null) {
                            this.mOnSeekCompleteListener.onSeekComplete(this.alburmFile);
                        }
                    }
                } else {
                    getlen = recvPack.data.length;
                }
                System.arraycopy(recvPack.data, infolen, this.buffer, this.offset, getlen);
                this.offset += getlen;
                this.tOffset += (long) getlen;
                this.handler.sendEmptyMessage(2);
                if (recvPack.isLastFlag == 1) {
                    this.alburmFile.length = this.tOffset;
                    recvOver();
                } else if (this.offset > this.buffer.length - 2048) {
                    writeFile();
                }
            } else if (recvPack.seq > this.curSeq + 1) {
                this.tmpSeq = recvPack.seq;
                reSend();
            }
        }
    }

    private void writeFile() {
        this.cacheManager.writeBuffer(this.buffer, 0, this.offset);
        this.foffset += this.offset;
        this.offset = 0;
        if (this.mOnCacheFileChangeListener != null) {
            this.mOnCacheFileChangeListener.onChange((long) this.foffset);
        }
    }

    /* access modifiers changed from: protected */
    public void reSend() {
        if (!this.resending) {
            this.resending = true;
            DJILogHelper.getInstance().LOGD(this.TAG, "重发 nextSeq=" + this.nextSeq + " 实际=" + this.tmpSeq, true, false);
            DJIVideoPackManager.getInstance().clearVideoData();
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.Stream).setSeq(this.nextSeq).setMissNum(1).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            freshPushStatus();
        }
    }

    /* access modifiers changed from: protected */
    public void recvOver() {
        ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.Stream).setSeq(this.nextSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        DJILogHelper.getInstance().LOGD(this.TAG, "recvOver foffset=" + this.foffset, true, true);
        writeFile();
        if (!this.isSeekd && copyToFile()) {
            this.isCached = true;
            this.nameKey = this.nameKeyOver;
            this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
            this.mOnCacheRenameListener.onChange();
        }
        stopMe();
        this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
    }

    private boolean copyToFile() {
        return new File(this.cacheManager.getPath(this.nameKey)).renameTo(new File(this.cacheManager.getPath(this.nameKeyOver)));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemPush push) {
        if (this.isAlive && this.tOffset == this.alburmFile.length) {
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.Stream).setSeq(this.nextSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            stop();
        }
    }
}
