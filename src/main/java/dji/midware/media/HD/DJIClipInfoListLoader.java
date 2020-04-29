package dji.midware.media.HD;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.litchis.DJILoader;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.model.litchis.DataAppRequest;
import dji.midware.data.model.litchis.DataCameraFileSystemListInfo;
import dji.midware.data.model.litchis.DataCameraFileSystemPush;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.data.model.litchis.DataRequestAck;
import dji.midware.data.model.litchis.DataRequestList;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.HD.DJIClipInfo;
import dji.midware.media.MediaLogger;
import dji.midware.util.BytesUtil;
import dji.thirdparty.afinal.core.Arrays;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIClipInfoListLoader extends DJILoader<DJIClipInfoList> {
    private static final String TAG = "DJIClipInfoListLoader";
    private byte[] buffer;
    private DJIClipInfoList dirInfo = new DJIClipInfoList();
    private int groupID;

    public DJIClipInfoListLoader() {
        this.checkDelay = 1000;
        this.timeOutMax = 10;
    }

    public void destroy() {
        destroyMe();
    }

    public void setListener(DJIAlbumInterface.DJIAlbumPullListener<DJIClipInfoList> listener) {
        this.listener = listener;
    }

    public void start() {
        super.start();
        this.isAlive = true;
        this.curSeq = 0;
        this.offset = 0;
        this.resending = false;
        ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.List).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        DJIVideoPackManager.getInstance().start();
        sendCommand();
        startMe();
    }

    private void sendCommand() {
        MediaLogger.i(TAG, "send command index=" + this.groupID + " num=-1, subtype=Clip");
        ((DataAppRequest) DataRequestList.getInstance().setIndex(this.groupID).setNum(-1).setSubType(DataConfig.SubType.CLIP).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
    }

    public void testStart() {
        DJIVideoPackManager.getInstance().start();
    }

    public void stop() {
        stopMe();
        DJIVideoPackManager.getInstance().stop();
    }

    public void abort(boolean isTimeout) {
        if (this.isAlive) {
            ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.List).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            if (!isTimeout) {
                this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.CLIENT_ABORT));
            }
        }
        stop();
    }

    /* access modifiers changed from: protected */
    public void countRate() {
        this.listener.onRateUpdate((long) this.dirInfo.dataLength, (long) this.offset, ((long) this.offset) - this.offsetTmp);
        this.offsetTmp = (long) this.offset;
    }

    /* access modifiers changed from: protected */
    public void countProgress() {
        this.listener.onProgress((long) this.dirInfo.dataLength, (long) this.offset);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemListInfo listInfo) {
        MediaLogger.e(TAG, "here DataCameraFileSystemListInfo");
        if (this.isAlive) {
            try {
                FileRecvPack recvPack = listInfo.getRecvPack();
                MediaLogger.i(TAG, "received DataCameraFileSystemListInfo at DJIClipListLoader: recevPack.seq=" + recvPack.seq + " data=" + Arrays.toString(recvPack.data) + " buffer=" + Arrays.toString(this.buffer));
                if (recvPack.seq == 0) {
                    this.dirInfo.commitNo = BytesUtil.getShort(recvPack.data, 0);
                    this.dirInfo.clipCount = BytesUtil.getShort(recvPack.data, 2);
                    this.dirInfo.dataLength = (this.dirInfo.clipCount * 2) + 4;
                    this.buffer = new byte[this.dirInfo.dataLength];
                }
                if (recvPack.seq == this.curSeq) {
                    checkPushStatus();
                    this.resending = false;
                    System.arraycopy(recvPack.data, 0, this.buffer, this.offset, recvPack.dataLen);
                    this.offset += recvPack.dataLen;
                    this.handler.sendEmptyMessage(2);
                    if (recvPack.isLastFlag == 1) {
                        DJILogHelper.getInstance().LOGD(TAG, "recvPack.isLastFlag=" + recvPack.isLastFlag, true, false);
                        DJILogHelper.getInstance().LOGD(TAG, "offset=" + this.offset + " dataLength=" + this.buffer.length, true, false);
                        if (this.offset == this.buffer.length) {
                            recvOver();
                        } else {
                            this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.DATA_NOMATCH));
                        }
                    } else {
                        this.curSeq++;
                    }
                    if (recvPack.seq > 0 && recvPack.seq % 50 == 0) {
                    }
                } else if (recvPack.seq > this.curSeq) {
                    reSend();
                }
            } catch (Exception e) {
                MediaLogger.show(e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void reSend() {
        if (!this.resending) {
            this.resending = true;
            DJILogHelper.getInstance().LOGD(TAG, "重发 curSeq=" + this.curSeq, true, false);
            DJIVideoPackManager.getInstance().clearVideoData();
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.List).setSeq(this.curSeq).setMissNum(1).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            freshPushStatus();
        }
    }

    /* access modifiers changed from: protected */
    public void resendMe() {
        freshPushStatus();
        MediaLogger.i(TAG, "resendMe()");
        sendCommand();
    }

    /* access modifiers changed from: protected */
    public void recvOver() {
        try {
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.List).setSeq(this.curSeq).setMissNum(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            stop();
            int start = 4;
            this.dirInfo.clipList.clear();
            for (int i = 0; i < this.dirInfo.clipCount; i++) {
                DJIClipInfo fileInfo = new DJIClipInfo();
                fileInfo.clipNo = this.buffer[start];
                int start2 = start + 1;
                fileInfo.transcodeState = DJIClipInfo.TranscodeState.find(this.buffer[start2]);
                start = start2 + 1;
                this.dirInfo.clipList.add(fileInfo);
            }
            this.handler.sendMessage(this.handler.obtainMessage(0, this.dirInfo));
        } catch (Exception e) {
            MediaLogger.show(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemPush push) {
        if (this.isAlive) {
            ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.List).setSeq(this.curSeq).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            stop();
        }
    }

    public void setGroupID(int groupID2) {
        this.groupID = groupID2;
    }
}
