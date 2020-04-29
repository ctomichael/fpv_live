package dji.logic.album.manager.litchis;

import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.h1.DJIMp4StreamLoader;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.model.litchis.DataAppRequest;
import dji.midware.data.model.litchis.DataCameraFileSystemFileData;
import dji.midware.data.model.litchis.DataCameraFileSystemPush;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.data.model.litchis.DataRequestAck;
import dji.midware.data.model.litchis.DataRequestFile;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.interfaces.DJIDataCallBack;
import java.io.File;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIFileStreamLoader extends DJILoader<DJIAlbumFile> {
    public static final int bufferSize = 307200;
    protected DJIAlbumFile alburmFile = new DJIAlbumFile();
    protected byte[] buffer = new byte[bufferSize];
    protected int foffset = 0;
    public boolean hasSeek = false;
    protected boolean isCached = false;
    protected boolean isPrepared = false;
    protected int mDuration;
    protected int mFileIndex;
    protected DJIFileType mFileType;
    protected OnCacheFileChangeListener mOnCacheFileChangeListener;
    protected OnCacheRenameListener mOnCacheRenameListener;
    protected OnPreparedListener mOnPreparedListener;
    protected OnSeekCompleteListener mOnSeekCompleteListener;
    protected String nameKey;
    protected String nameKeyOver;
    protected long seekOffset = 0;
    protected long tOffset = 0;
    protected int ttt = 0;

    public interface OnCacheRenameListener {
        void onChange();
    }

    public interface OnCacheFileChangeListener {
        void onChange(long j);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(DJIAlbumFile dJIAlbumFile);
    }

    public interface OnPreparedListener {
        void onPrepared(DJIAlbumFile dJIAlbumFile);
    }

    public DJIFileStreamLoader() {
        this.timeOutMax = 5;
        this.checkDelay = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
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

    public long getSeekOffset() {
        return this.seekOffset;
    }

    public boolean isCached() {
        return this.isCached;
    }

    public void onSeek() {
        this.hasSeek = true;
    }

    public void start() {
        start(0);
    }

    public void start(long seekOffset2) {
        super.start();
        this.isPrepared = false;
        this.seekOffset = seekOffset2;
        this.tOffset = seekOffset2;
        this.isAlive = true;
        this.curSeq = 0;
        this.foffset = 0;
        this.resending = false;
        this.offset = 0;
        if (this.isCached) {
            this.alburmFile.length = this.cacheManager.getLenCacheInDisk(this.nameKey);
            this.mOnPreparedListener.onPrepared(this.alburmFile);
            this.mOnCacheFileChangeListener.onChange(this.alburmFile.length);
            this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
            return;
        }
        ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        DJIVideoPackManager.getInstance().start();
        this.cacheManager.openStreamCover(this.nameKey);
        ((DataAppRequest) DataRequestFile.getInstance().setIndex(this.mFileIndex).setNum(1).setSubType(DataConfig.SubType.SCR).setSize(-1).setOffset(this.tOffset).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        startMe();
    }

    public void restart() {
        start(this.seekOffset);
    }

    public void stop() {
        if (this.isAlive) {
            stopMe();
            this.cacheManager.closeStream();
            DJIVideoPackManager.getInstance().stop();
        }
    }

    public void abort(boolean isTimeout) {
        DJILogHelper.getInstance().LOGD(this.TAG, "will abort " + this.isAlive, true, true);
        if (this.isAlive) {
            ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
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
        this.listener.onProgress(this.alburmFile.length, this.tOffset);
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
    public void onEvent3BackgroundThread(DataCameraFileSystemFileData fileData) {
        int getlen;
        if (this.isAlive) {
            FileRecvPack recvPack = fileData.getRecvPack();
            this.ttt++;
            if (this.ttt % 100 == 0) {
                DJILogHelper.getInstance().LOGD(this.TAG, "seq=" + this.curSeq + " 实际=" + recvPack.seq, true, false);
            }
            if (recvPack.seq == this.curSeq) {
                checkPushStatus();
                this.resending = false;
                int infolen = 0;
                if (recvPack.seq == 0) {
                    infolen = fileData.getInfoLen();
                    getlen = recvPack.data.length - infolen;
                    if (!this.isPrepared) {
                        this.isPrepared = true;
                        if (this.tOffset == 0) {
                            this.alburmFile.length = (long) (fileData.getSize() - infolen);
                            DJILogHelper.getInstance().LOGD(this.TAG, "alburmFile.length=" + this.alburmFile.length, true, false);
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
                if (!(this instanceof DJIMp4StreamLoader)) {
                    this.handler.sendEmptyMessage(5);
                }
                if (recvPack.isLastFlag == 1) {
                    DJILogHelper.getInstance().LOGD(this.TAG, "tOffset=" + this.tOffset + " fileInfo.length=" + this.alburmFile.length, true, true);
                    if (this.tOffset == this.alburmFile.length) {
                        recvOver();
                    } else {
                        this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.DATA_NOMATCH));
                    }
                } else {
                    this.curSeq++;
                    if (this.offset > this.buffer.length - 512) {
                        writeFile();
                    }
                }
            } else if (recvPack.seq > this.curSeq) {
                reSend();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writeFile() {
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
            DJILogHelper.getInstance().LOGD(this.TAG, "重发 curSeq=" + this.curSeq, true, false);
            DJIVideoPackManager.getInstance().clearVideoData();
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(1).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            freshPushStatus();
        }
    }

    /* access modifiers changed from: protected */
    public void recvOver() {
        ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq + 1).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        DJILogHelper.getInstance().LOGD(this.TAG, "recvOver ", true, true);
        writeFile();
        if (this.alburmFile.length == this.cacheManager.getLenCacheInDisk(this.nameKey) && !this.hasSeek && copyToFile()) {
            this.isCached = true;
            this.nameKey = this.nameKeyOver;
            this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
            this.mOnCacheRenameListener.onChange();
        }
        stopMe();
        this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
    }

    /* access modifiers changed from: protected */
    public boolean copyToFile() {
        return new File(this.cacheManager.getPath(this.nameKey)).renameTo(new File(this.cacheManager.getPath(this.nameKeyOver)));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemPush push) {
        if (this.isAlive && this.tOffset == this.alburmFile.length) {
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq + 1).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            stop();
        }
    }
}
