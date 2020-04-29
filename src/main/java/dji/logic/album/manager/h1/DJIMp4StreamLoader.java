package dji.logic.album.manager.h1;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.litchis.DJIFileStreamLoader;
import dji.logic.album.manager.litchis.DJIFileType;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.model.litchis.DataAppRequest;
import dji.midware.data.model.litchis.DataCameraFileSystemFileData;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.data.model.litchis.DataRequestFile;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.mp4.MP4Parser;
import dji.midware.util.BytesUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIMp4StreamLoader extends DJIFileStreamLoader {
    private Mp4PreReadListener preReadListener;
    private boolean requestHeader = false;
    private boolean requestTail = false;

    public interface Mp4PreReadListener {
        void onPreReadComplete();
    }

    public void startLoadHeader() {
        this.requestHeader = true;
        start(0);
    }

    public void startLoadTail(int offset) {
        this.requestTail = true;
        Log.e(this.TAG, "start load tail");
        start((long) offset);
    }

    public void setPreReadListener(Mp4PreReadListener listener) {
        this.preReadListener = listener;
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

    public void start() {
        start(0);
    }

    public void start(long seekOffset) {
        this.handler.sendEmptyMessage(3);
        this.isPrepared = false;
        this.seekOffset = seekOffset;
        this.tOffset = seekOffset;
        this.isAlive = true;
        this.curSeq = 0;
        this.foffset = (int) seekOffset;
        this.resending = false;
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
        this.cacheManager.seekFile(seekOffset);
        ((DataAppRequest) DataRequestFile.getInstance().setIndex(this.mFileIndex).setNum(1).setSubType(DataConfig.SubType.MP4).setSize(-1).setOffset(this.tOffset).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start(new DJIDataCallBack() {
            /* class dji.logic.album.manager.h1.DJIMp4StreamLoader.AnonymousClass1 */

            public void onSuccess(Object model) {
                Log.e("DJIMp4StreamLoader", "send cmd seek success");
            }

            public void onFailure(Ccode ccode) {
                Log.e("DJIMp4StreamLoader", "send cmd seek fail");
            }
        });
        startMe();
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
                    this.cacheManager.seekFile(this.seekOffset);
                    this.offset = 0;
                    Log.e(this.TAG, "receive eventbus seekOffset: " + this.seekOffset);
                    Log.e(this.TAG, "receive data:" + BytesUtil.byte2hex(recvPack.data));
                    infolen = fileData.getInfoLen();
                    getlen = recvPack.data.length - infolen;
                    if (!this.isPrepared) {
                        this.isPrepared = true;
                        if (this.tOffset == 0) {
                            this.alburmFile.length = (long) (fileData.getSize() - infolen);
                            DJILogHelper.getInstance().LOGD(this.TAG, "alburmFile.length=" + this.alburmFile.length, true, false);
                            if (this.mOnPreparedListener != null) {
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
                    DJILogHelper.getInstance().LOGD(this.TAG, "tOffset=" + this.tOffset + " fileInfo.length=" + this.alburmFile.length, true, true);
                    if (this.tOffset != this.alburmFile.length) {
                        this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.DATA_NOMATCH));
                    } else if (this.requestTail) {
                        this.requestTail = false;
                        abort(false);
                        MP4Parser.getInstance().parseMoov(this.buffer);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.offset = 0;
                        this.preReadListener.onPreReadComplete();
                        return;
                    } else {
                        recvOver();
                    }
                } else {
                    this.curSeq++;
                    if (this.offset > this.buffer.length - 512) {
                        writeFile();
                    }
                }
                if (this.requestHeader && this.offset > 100) {
                    this.requestHeader = false;
                    int moov_offset = MP4Parser.getInstance().findMoovOffset(this.buffer);
                    abort(false);
                    Log.d(this.TAG, "moov offset: " + moov_offset);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    this.offset = 0;
                    startLoadTail(moov_offset);
                }
            } else if (recvPack.seq > this.curSeq) {
                reSend();
            }
        }
    }
}
