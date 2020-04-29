package dji.midware.media.HD;

import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.litchis.DJILoader;
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
import dji.midware.media.MediaLogger;
import java.util.Locale;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIClipFileLoader extends DJILoader<DJIClipFile> {
    protected byte[] buffer = new byte[5242880];
    protected DJIClipFile clipFile = new DJIClipFile();
    protected DJIClipInfo clipInfo;
    private int cttt = 0;
    private boolean isOpenSliceMode = false;
    protected String nameKey;
    private int sliseIndex = 1;
    private int sliseLen = 41943040;
    private int sliseNum = 0;
    protected long tOffset = 0;

    public DJIClipFileLoader() {
        this.timeOutMax = 3;
        this.checkDelay = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
    }

    public void destroy() {
        destroyMe();
    }

    public void setListener(DJIClipInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIClipFile> listener) {
        this.clipInfo = fileInfo;
        this.listener = listener;
        this.clipFile.fileType = fileInfo.fileType;
        getCacheKey();
        this.clipFile.cachPath = this.cacheManager.getPath(this.nameKey);
    }

    /* access modifiers changed from: protected */
    public void getCacheKey() {
        this.nameKey = this.clipInfo.getNameKey();
    }

    public void start() {
        super.start();
        this.isAlive = true;
        this.curSeq = 0;
        this.offset = 0;
        this.tOffset = 0;
        this.offsetTmp = 0;
        this.resending = false;
        if (!cacheCheck()) {
            ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            DJIVideoPackManager.getInstance().start();
            sendCommand();
            startMe();
        }
    }

    private void sendCommand() {
        MediaLogger.show(String.format(Locale.US, "send command: subVideoType=clip, index=%d, subindex=%d num=%d, size=%d, offset=%d", Integer.valueOf(this.clipInfo.commitID), Integer.valueOf(this.clipInfo.clipNo), -1, -1, 0));
        ((DataAppRequest) DataRequestFile.getInstance().setSubType(DataConfig.SubType.CLIP).setIndex(this.clipInfo.commitID).setSubIndex(this.clipInfo.clipNo).setNum(-1).setSize(-1).setOffset(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
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
                sendCommand();
            }
        }
    }

    private long getSliceSize() {
        if (this.sliseNum == 0) {
            return -1;
        }
        return (long) (this.sliseIndex == this.sliseNum ? -1 : this.sliseLen);
    }

    /* access modifiers changed from: protected */
    public boolean cacheCheck() {
        this.tOffset = this.cacheManager.getLenCacheInDisk(this.nameKey);
        this.cacheManager.openStream(this.nameKey);
        return false;
    }

    public void stop() {
        stopMe();
        this.cacheManager.closeStream();
        DJIVideoPackManager.getInstance().stop();
        DJILogHelper.getInstance().LOGD(this.TAG, "停止", true, true);
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
        long rate = this.tOffset - this.offsetTmp;
        if (rate < 0) {
            rate = 0;
        }
        this.listener.onRateUpdate(this.clipFile.length, this.tOffset, rate);
        this.offsetTmp = this.tOffset;
    }

    /* access modifiers changed from: protected */
    public void countProgress() {
        this.listener.onProgress(this.clipFile.length, this.tOffset);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemFileData fileData) {
        int getlen;
        if (this.isAlive) {
            try {
                FileRecvPack recvPack = fileData.getRecvPack();
                this.cttt++;
                if (this.cttt % 200 == 0) {
                    DJILogHelper.getInstance().LOGD(this.TAG, "seq=" + this.curSeq + " 实际=" + recvPack.seq + " " + System.currentTimeMillis(), false, false);
                }
                if (recvPack.seq == this.curSeq) {
                    checkPushStatus();
                    this.resending = false;
                    int infolen = 0;
                    if (recvPack.seq == 0) {
                        infolen = fileData.getInfoLen();
                        getlen = recvPack.data.length - infolen;
                        MediaLogger.show("file size=" + fileData.getSize());
                        this.clipFile.length = (long) (fileData.getSize() - infolen);
                    } else {
                        getlen = recvPack.data.length;
                    }
                    System.arraycopy(recvPack.data, infolen, this.buffer, this.offset, getlen);
                    this.offset += getlen;
                    this.tOffset += (long) getlen;
                    this.handler.sendEmptyMessage(2);
                    if (recvPack.isLastFlag == 1) {
                        DJILogHelper.getInstance().LOGD(this.TAG, "tOffset=" + this.tOffset + " fileInfo.length=" + this.clipFile.length, true, false);
                        if (this.tOffset == this.clipFile.length) {
                            recvOver();
                        } else if (this.tOffset <= this.clipFile.length) {
                            restart();
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
            } catch (Exception e) {
                MediaLogger.show(e);
            }
        }
    }

    private void writeFile() {
        MediaLogger.show("recieved " + this.offset + " bytes for the file " + this.clipInfo.targetPath);
        this.cacheManager.writeBuffer(this.buffer, 0, this.offset);
        this.offset = 0;
    }

    /* access modifiers changed from: protected */
    public synchronized void reSend() {
        if (!this.resending) {
            this.resending = true;
            DJILogHelper.getInstance().LOGD(this.TAG, "重发 curSeq=" + this.curSeq, false, false);
            DJIVideoPackManager.getInstance().clearVideoData();
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(1).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            freshPushStatus();
        }
    }

    /* access modifiers changed from: protected */
    public void recvOver() {
        ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        DJILogHelper.getInstance().LOGD(this.TAG, "recvOver ", true, true);
        writeFile();
        stopMe();
        this.handler.sendMessage(this.handler.obtainMessage(0, this.clipFile));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemPush push) {
        if (this.isAlive && this.tOffset == this.clipFile.length) {
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            stop();
        }
    }
}
