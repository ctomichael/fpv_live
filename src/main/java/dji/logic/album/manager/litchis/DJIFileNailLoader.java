package dji.logic.album.manager.litchis;

import android.graphics.Bitmap;
import android.support.v4.os.EnvironmentCompat;
import com.dji.frame.util.V_FileUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumFileInfo;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.media.exif.utils.DJIExifTools;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.litchis.DataAppRequest;
import dji.midware.data.model.litchis.DataCameraFileSystemFileData;
import dji.midware.data.model.litchis.DataCameraFileSystemPush;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.data.model.litchis.DataRequestAck;
import dji.midware.data.model.litchis.DataRequestFile;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIThumbNailUtils;
import java.io.File;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.msgpack.core.MessagePack;

@EXClassNullAway
public class DJIFileNailLoader extends DJILoader<DJIAlbumFile> {
    protected DJIAlbumFile alburmFile = new DJIAlbumFile();
    protected byte[] buffer;
    private int cttt = 0;
    protected DJIAlbumFileInfo fileInfo;
    private volatile int fileLength;
    protected String nameKey;
    private volatile byte[] recvOverBuffer;
    protected int start;
    protected DataConfig.SubType subType;
    protected int thumbOffset;

    public DJIFileNailLoader() {
        this.timeOutMax = 2;
        this.checkDelay = 1000;
    }

    public void destroy() {
        destroyMe();
    }

    public void setListener(DJIAlbumFileInfo fileInfo2, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        this.fileInfo = fileInfo2;
        this.listener = listener;
        this.alburmFile.index = fileInfo2.index;
        this.alburmFile.fileType = fileInfo2.fileType;
    }

    public void setType(DataConfig.SubType subType2) {
        this.subType = subType2;
        switch (subType2) {
            case THM:
                this.nameKey = this.fileInfo.getThumbNameKey();
                this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
                return;
            case Pano_SCR:
                this.nameKey = this.fileInfo.getPanoNameKey();
                this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
                return;
            case TIMELAPSE:
            case SCR:
                this.nameKey = this.fileInfo.getScreenNameKey();
                this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
                return;
            case Pano_THM:
                this.nameKey = this.fileInfo.getPanoThumbNameKey();
                this.alburmFile.cachPath = this.cacheManager.getPath(this.nameKey);
                return;
            case BOKEH:
                this.subType = DataConfig.SubType.ORG;
                this.nameKey = this.fileInfo.getBokehNameKey();
                return;
            case BOKEH_SCR:
                this.subType = DataConfig.SubType.ORG;
                this.nameKey = this.fileInfo.getScreenNameKey();
                return;
            case BOKEH_THM:
                this.subType = DataConfig.SubType.ORG;
                this.nameKey = this.fileInfo.getThumbNameKey();
                return;
            case Pano:
                this.subType = DataConfig.SubType.SCR;
                this.nameKey = this.fileInfo.getPanoramaNameKey();
                return;
            case EXIF:
            case EXIF_NEW:
                if (DJIProductManager.getInstance().getType() == ProductType.Mammoth) {
                    this.subType = DataConfig.SubType.EXIF;
                }
                this.nameKey = this.fileInfo.getExifNameKey();
                return;
            default:
                this.listener.onFailure(DJIAlbumPullErrorType.ERROR_REQ);
                return;
        }
    }

    private boolean cacheCheck() {
        if (this.cacheManager.isBitmapExistInMemory(this.nameKey)) {
            this.alburmFile.bitmap = this.cacheManager.getBitmapFromMemory(this.nameKey);
            this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
            return true;
        } else if (!this.cacheManager.isBitmapExistInDisk(this.nameKey)) {
            return false;
        } else {
            this.alburmFile.bitmap = this.cacheManager.getBitmapFromDisk(this.nameKey);
            if (this.alburmFile.bitmap == null) {
                return false;
            }
            this.cacheManager.addBitmapToMemory(this.nameKey, this.alburmFile.bitmap);
            this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
            return true;
        }
    }

    public synchronized void start() {
        if (this.isAlive) {
            this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.CLIENT_ABORT));
        } else {
            super.start();
            this.isAlive = true;
            this.curSeq = 0;
            this.offset = 0;
            this.resending = false;
            this.alburmFile.length = 0;
            if (!cacheCheck()) {
                ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
                DJIVideoPackManager.getInstance().start();
                ((DataAppRequest) DataRequestFile.getInstance().setIndex(this.fileInfo.index).setSubIndex(this.fileInfo.subIndex).setNum(1).setSubType(this.subType).setSize(-1).setOffset(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
                startMe();
            }
        }
    }

    public void stop() {
        stopMe();
        DJIVideoPackManager.getInstance().stop();
    }

    public void abort(boolean isTimeout) {
        if (this.isAlive) {
            ((DataAppRequest) ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            if (!isTimeout) {
                this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.CLIENT_ABORT));
            }
            DJILog.logWriteD("playback", "DJIFileNailLoader abort() fileInfo.index=" + (this.fileInfo != null ? String.valueOf(this.fileInfo.index) : EnvironmentCompat.MEDIA_UNKNOWN), "playback", new Object[0]);
        }
        stop();
    }

    /* access modifiers changed from: protected */
    public void countRate() {
        this.listener.onRateUpdate(this.alburmFile.length, (long) this.offset, ((long) this.offset) - this.offsetTmp);
        this.offsetTmp = (long) this.offset;
    }

    /* access modifiers changed from: protected */
    public void countProgress() {
        this.listener.onProgress(this.alburmFile.length, (long) this.offset);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemFileData fileData) {
        int getlen;
        if (this.isAlive) {
            FileRecvPack recvPack = fileData.getRecvPack();
            this.cttt++;
            if (recvPack.seq == this.curSeq) {
                checkPushStatus();
                this.resending = false;
                this.curSeq++;
                int infolen = 0;
                if (recvPack.seq == 0) {
                    infolen = fileData.getInfoLen();
                    this.alburmFile.length = (long) (fileData.getSize() - infolen);
                    this.buffer = new byte[(((int) this.alburmFile.length) + 2)];
                    getlen = recvPack.data.length - infolen;
                } else {
                    getlen = recvPack.data.length;
                }
                System.arraycopy(recvPack.data, infolen, this.buffer, this.offset, getlen);
                this.offset += getlen;
                this.handler.sendEmptyMessage(2);
                if (((long) this.offset) == this.alburmFile.length) {
                    this.recvOverBuffer = ByteUtils.clone(this.buffer);
                    this.fileLength = this.offset;
                    this.mParseHandler.sendEmptyMessage(6);
                }
            } else if (recvPack.seq > this.curSeq) {
                reSend();
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void reSend() {
        if (!this.resending) {
            this.resending = true;
            DJILog.logWriteD("playback", "重发 curSeq=" + this.curSeq + " fileInfo.index＝" + (this.fileInfo != null ? String.valueOf(this.fileInfo.index) : EnvironmentCompat.MEDIA_UNKNOWN), "playback", new Object[0]);
            DJIVideoPackManager.getInstance().clearVideoData();
            if (this.curSeq == 0) {
                ((DataAppRequest) DataRequestFile.getInstance().setDoNotChangeSessionId().setIndex(this.fileInfo.index).setNum(1).setSubType(this.subType).setSize(-1).setOffset(0).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            } else {
                ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(1).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
            }
            freshPushStatus();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void recvOver() {
        Bitmap bitmap;
        ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.File).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).start((DJIDataCallBack) null);
        stopMe();
        if (this.fileLength == 0) {
            this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.DATA_NOMATCH));
        } else {
            if (!(((short) this.recvOverBuffer[this.fileLength - 1]) == 217 || this.fileInfo.fileType == DJIFileType.TIF)) {
                this.recvOverBuffer[this.fileLength] = -1;
                this.recvOverBuffer[this.fileLength + 1] = MessagePack.Code.STR8;
                this.fileLength += 2;
            }
            DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(this.receiverIdInProtocol);
            if ((this.fileInfo.fileType == DJIFileType.MOV || this.fileInfo.fileType == DJIFileType.MP4) && canGetThumbNailTHM(cameraType)) {
                bitmap = DJIThumbNailUtils.getThumbNailThm(this.recvOverBuffer);
            } else if (this.fileInfo.fileType == DJIFileType.TIF || this.fileInfo.fileType == DJIFileType.TIFF_SEQ || this.fileInfo.fileType == DJIFileType.SEQ) {
                V_FileUtil.fileWrite(this.cacheManager.getPath(this.nameKey.replace(".jpg", ".tif")), this.recvOverBuffer, 0, this.fileLength);
                bitmap = DJIThumbNailUtils.covertTIFtoJPG(this.recvOverBuffer, 0, this.fileLength);
            } else if (this.fileInfo.fileType == DJIFileType.BOKEH || this.fileInfo.fileType == DJIFileType.PANO) {
                bitmap = DJIThumbNailUtils.getThumbNail(this.recvOverBuffer, 0, this.fileLength);
                this.cacheManager.addBitmapToDiskNoDecorder(this.nameKey, this.recvOverBuffer, 0, this.fileLength);
            } else {
                bitmap = DJIThumbNailUtils.getThumbNail(this.recvOverBuffer, 0, this.fileLength);
            }
            this.alburmFile.bitmap = bitmap;
            if (bitmap != null) {
                if (!(this.fileInfo.fileType == DJIFileType.BOKEH || this.fileInfo.fileType == DJIFileType.PANO)) {
                    this.cacheManager.addBitmapToDisk(this.nameKey, bitmap);
                }
                if (this.fileInfo.fileType == DJIFileType.TIF && this.subType == DataConfig.SubType.SCR) {
                    DJIExifTools.copyExifData(new File(this.cacheManager.getPath(this.nameKey.replace(".jpg", ".tif"))), new File(this.cacheManager.getPath(this.nameKey)), null);
                }
                this.cacheManager.addBitmapToMemory(this.nameKey, bitmap);
            } else if (this.subType == DataConfig.SubType.EXIF || this.subType == DataConfig.SubType.EXIF_NEW) {
                this.cacheManager.addBitmapToDiskNoDecorder(this.nameKey, this.recvOverBuffer, 0, this.fileLength);
            } else {
                this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.CLIENT_ABORT));
            }
            this.handler.sendMessage(this.handler.obtainMessage(0, this.alburmFile));
        }
    }

    private boolean canGetThumbNailTHM(DataCameraGetPushStateInfo.CameraType cameraType) {
        return (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau336 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau640 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC230 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC2403 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC245_IMX477 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC160) ? false : true;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemPush push) {
        if (this.isAlive) {
            DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.File).setSeq(this.curSeq).setMissNum(0).start((DJIDataCallBack) null);
        }
    }
}
