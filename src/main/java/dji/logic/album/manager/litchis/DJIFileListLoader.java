package dji.logic.album.manager.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.accessory.DJIAudioFileInfo;
import dji.logic.album.model.DJIAlbumDirInfo;
import dji.logic.album.model.DJIAlbumFileInfo;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.logic.album.model.ExtExif;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.model.litchis.DataAppRequest;
import dji.midware.data.model.litchis.DataCameraFileSystemListInfo;
import dji.midware.data.model.litchis.DataCameraFileSystemPush;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.data.model.litchis.DataRequestAck;
import dji.midware.data.model.litchis.DataRequestList;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.data.queue.P3.PackUtil;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIFileListLoader extends DJILoader<DJIAlbumDirInfo> {
    private static final String TAG = DJIFileListLoader.class.getSimpleName();
    private volatile byte[] buffer;
    private volatile DJIAlbumDirInfo mDirInfo = new DJIAlbumDirInfo();
    private DataCameraSetStorageInfo.Storage mStorage = DataCameraSetStorageInfo.Storage.SDCARD;
    private int startIndex;

    public DJIFileListLoader() {
        this.timeOutMax = 2;
        this.checkDelay = 3000;
    }

    public void destroy() {
        destroyMe();
    }

    public void setListener(DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumDirInfo> listener) {
        this.listener = listener;
    }

    public void setStorage(DataCameraSetStorageInfo.Storage storage) {
        this.mStorage = storage;
    }

    public void start() {
        start(1);
    }

    public void start(int index) {
        if (this.isAlive) {
            this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.CLIENT_ABORT));
            return;
        }
        this.startIndex = index;
        super.start();
        this.isAlive = true;
        this.curSeq = 0;
        this.offset = 0;
        this.resending = false;
        sendStartCMD(index);
    }

    /* access modifiers changed from: protected */
    public void sendStartCMD(int index) {
        abortRequest();
        DJILog.logWriteD("playback-file-list", "DJIFileListLoader start() index: " + index + " abort last session: " + PackUtil.sessionId(), "playback", new Object[0]);
        DJIVideoPackManager.getInstance().start();
        requestList(index);
        startMe();
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
            abortRequest();
            if (!isTimeout) {
                DJILog.logWriteD("playback", "file list abort(not timeout)", new Object[0]);
                this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.CLIENT_ABORT));
            }
        }
        stop();
    }

    /* access modifiers changed from: protected */
    public void countRate() {
        this.listener.onRateUpdate((long) this.mDirInfo.dataLength, (long) this.offset, ((long) this.offset) - this.offsetTmp);
        this.offsetTmp = (long) this.offset;
    }

    /* access modifiers changed from: protected */
    public void countProgress() {
        this.listener.onProgress((long) this.mDirInfo.dataLength, (long) this.offset);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemListInfo listInfo) {
        if (this.isAlive) {
            FileRecvPack recvPack = listInfo.getRecvPack();
            if (recvPack.seq == 0 && this.curSeq == 0) {
                this.mDirInfo.fileCount = listInfo.getFileCount();
                this.mDirInfo.dataLength = listInfo.getDataLength();
                DJILog.logWriteD("playback-file-list", "first pack: fileCount=" + this.mDirInfo.fileCount + ", dataLength=" + this.mDirInfo.dataLength + ", recvPack.dataLen=" + recvPack.dataLen + ", isLast=" + recvPack.isLastFlag, "playback", new Object[0]);
                this.buffer = new byte[Math.max(this.mDirInfo.dataLength, recvPack.dataLen)];
            }
            if (recvPack.seq == this.curSeq) {
                checkPushStatus();
                this.resending = false;
                this.curSeq++;
                if (this.offset + recvPack.dataLen > this.buffer.length) {
                    byte[] temp = new byte[(this.offset + recvPack.dataLen)];
                    System.arraycopy(this.buffer, 0, temp, 0, this.offset);
                    this.buffer = temp;
                }
                System.arraycopy(recvPack.data, 0, this.buffer, this.offset, recvPack.dataLen);
                this.offset += recvPack.dataLen;
                this.handler.sendEmptyMessage(2);
                if (recvPack.isLastFlag != 1) {
                    return;
                }
                if (this.offset == this.buffer.length) {
                    this.mParseHandler.sendEmptyMessage(6);
                    return;
                }
                if (this.offset != 0) {
                    byte[] temp2 = new byte[this.offset];
                    System.arraycopy(this.buffer, 0, temp2, 0, this.offset);
                    this.buffer = temp2;
                }
                this.mParseHandler.sendEmptyMessage(6);
            } else if (recvPack.seq > this.curSeq) {
                reSend();
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void reSend() {
        if (!this.resending) {
            this.resending = true;
            DJILogHelper.getInstance().LOGD(TAG, "重发 curSeq=" + this.curSeq, true, false);
            DJIVideoPackManager.getInstance().clearVideoData();
            requestACK(this.curSeq, 1);
            freshPushStatus();
        }
    }

    /* access modifiers changed from: protected */
    public void resendMe() {
        DJILogHelper.getInstance().LOGD(TAG, "DJIFileListLoader timeout resendMe index:" + this.startIndex, true, false);
        freshPushStatus();
        requestList(this.startIndex);
    }

    /* access modifiers changed from: protected */
    public void recvOver() {
        int start;
        requestACK(this.curSeq, 0);
        stop();
        int start2 = 8;
        this.mDirInfo.fileInfoList.clear();
        int i = 0;
        while (i < this.mDirInfo.fileCount && start2 < this.buffer.length) {
            try {
                DJIAlbumFileInfo fileInfo = new DJIAlbumFileInfo();
                fileInfo.parseTime(BytesUtil.getUInt(this.buffer, start2));
                int start3 = start2 + 4;
                fileInfo.length = BytesUtil.getUInt(this.buffer, start3);
                int start4 = start3 + 4;
                fileInfo.index = BytesUtil.getInt(this.buffer, start4);
                int start5 = start4 + 4;
                fileInfo.duration = BytesUtil.getShort(this.buffer, start5);
                int start6 = start5 + 2;
                if (DJIProductManager.getInstance().getType().equals(ProductType.KumquatS) || DJIProductManager.getInstance().getType().equals(ProductType.KumquatX)) {
                    fileInfo.rotation = (this.buffer[start6] >> 6) & 3;
                    fileInfo.frameRate = this.buffer[start6] & 63;
                    start = start6 + 1;
                } else {
                    fileInfo.frameRate = this.buffer[start6];
                    start = start6 + 1;
                }
                if (((float) fileInfo.frameRate) > 30.1f) {
                    fileInfo.frameRate = (int) (((float) fileInfo.frameRate) * 0.5f);
                }
                fileInfo.resolution = DJIFileResolution.find(this.buffer[start]);
                int start7 = start + 1;
                fileInfo.fileType = DJIFileType.find(this.buffer[start7]);
                int start8 = start7 + 1;
                fileInfo.pathLength = this.buffer[start8];
                start2 = start8 + 1;
                if (fileInfo.pathLength > 0) {
                    if (fileInfo.pathLength >= 6) {
                        if (fileInfo.pathLength + start2 <= this.buffer.length) {
                            byte[] ext = new byte[fileInfo.pathLength];
                            System.arraycopy(this.buffer, start2, ext, 0, fileInfo.pathLength);
                            start2 += fileInfo.pathLength;
                            fileInfo.hasEXT = true;
                            int offset = 0;
                            while (offset < fileInfo.pathLength) {
                                DJIAlbumFileInfo.EXT_TYPE type = DJIAlbumFileInfo.EXT_TYPE.find(ext[offset]);
                                int offset2 = offset + 1;
                                switch (type) {
                                    case VideoGUID:
                                        fileInfo.fileGuid = BytesUtil.getUInt(ext, offset2);
                                        offset = offset2 + 4;
                                        break;
                                    case PhotoInfo:
                                        fileInfo.captureType = DataCameraSetPhoto.TYPE.find(ext[offset2]);
                                        int offset3 = offset2 + 1;
                                        fileInfo.photoGroupId = BytesUtil.getUShort(ext, offset3);
                                        offset = offset3 + 2;
                                        break;
                                    case FileTag:
                                        fileInfo.starTag = ext[offset2] != 0;
                                        offset = offset2 + 1;
                                        break;
                                    case VideoInfo:
                                        fileInfo.videoTpye = ext[offset2];
                                        int offset4 = offset2 + 1;
                                        fileInfo.subVideoType = ext[offset4];
                                        int offset5 = offset4 + 1;
                                        fileInfo.encodeType = DJIAlbumFileInfo.EncodeType.find(ext[offset5]);
                                        int offset6 = offset5 + 1;
                                        fileInfo.frameRateScale = ext[offset6];
                                        offset = offset6 + 1;
                                        break;
                                    case GroupParam:
                                        fileInfo.groupType = DJIAlbumFileInfo.GroupType.find(ext[offset2]);
                                        int offset7 = offset2 + 1;
                                        boolean hasResult = isProductHasGroupResult(DJIProductManager.getInstance().getType());
                                        fileInfo.groupNum = getGroupNum(ext, offset7, hasResult);
                                        fileInfo.groupResult = getGroupResult(ext, offset7, hasResult);
                                        offset = offset7 + 2;
                                        break;
                                    case SyncTag:
                                        fileInfo.isSync = ext[offset2] == 1;
                                        offset = offset2 + 1;
                                        break;
                                    case origInfo:
                                        fileInfo.hasOrigPhoto = ext[offset2] == 1;
                                        offset = offset2 + 1;
                                        break;
                                    case exifInfo:
                                        fileInfo.extExif = ExtExif.dataFromBytes(ext, offset2);
                                        offset2 += 35;
                                    case AudioInfo:
                                        offset = writeAudioInfo(fileInfo, ext, offset2);
                                        break;
                                    default:
                                        offset = fileInfo.pathLength;
                                        break;
                                }
                            }
                        } else {
                            return;
                        }
                    } else {
                        fileInfo.pathStr = BytesUtil.getStringUTF8(this.buffer, start2, fileInfo.pathLength);
                        start2 += fileInfo.pathLength;
                    }
                } else {
                    fileInfo.pathStr = "";
                }
                this.mDirInfo.fileInfoList.add(fileInfo);
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                DJILog.logWriteE(TAG, "fetch file list failure. parse error:\n" + BytesUtil.byte2hex(this.buffer), "playback", new Object[0]);
                this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.DATA_NOMATCH));
                return;
            }
        }
        if (this.mDirInfo.fileInfoList.size() > 0 && this.mDirInfo.fileInfoList.get(0).length == 0) {
            DJILog.logWriteE(TAG, "fetch file list parse error. " + BytesUtil.byte2hex(this.buffer), "playback", new Object[0]);
        }
        if (this.mDirInfo.fileInfoList.size() == this.mDirInfo.fileCount) {
            DJILog.logWriteI(TAG, "fetch file list successful. " + this.mDirInfo, "playback", new Object[0]);
            this.handler.sendMessage(this.handler.obtainMessage(0, DJIAlbumDirInfo.copyFrom(this.mDirInfo)));
            return;
        }
        DJILog.logWriteE(TAG, "fetch file list failure. " + this.mDirInfo, "playback", new Object[0]);
        this.handler.sendMessage(this.handler.obtainMessage(1, DJIAlbumPullErrorType.DATA_NOMATCH));
    }

    private int getGroupNum(byte[] buffer2, int offset, boolean hasResult) {
        int groupNumAndResult = BytesUtil.getUShort(buffer2, offset);
        if (hasResult) {
            return groupNumAndResult & 4095;
        }
        return groupNumAndResult;
    }

    private int getGroupResult(byte[] buffer2, int offset, boolean hasResult) {
        int groupNumAndResult = BytesUtil.getUShort(buffer2, offset);
        if (hasResult) {
            return groupNumAndResult >> 14;
        }
        return 0;
    }

    private boolean isProductHasGroupResult(ProductType productType) {
        return productType == ProductType.WM230 || productType == ProductType.WM240 || productType == ProductType.WM245 || productType == ProductType.WM160;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraFileSystemPush push) {
        if (this.isAlive) {
            requestACK(this.curSeq, 0);
        }
    }

    private void abortRequest() {
        ((DataAppRequest) DataRequestAbort.getInstance().setCmdId(DataConfig.CmdId.List).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).setDeviceType(getDeviceType()).start((DJIDataCallBack) null);
    }

    private void requestList(int startIndex2) {
        ((DataAppRequest) DataRequestList.getInstance().setStorage(this.mStorage).setIndex(startIndex2).setNum(-1).setSubType(DataConfig.SubType.ORG).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).setDeviceType(getDeviceType()).start((DJIDataCallBack) null);
    }

    private void requestACK(int seq, int missNum) {
        ((DataAppRequest) DataRequestAck.newInstance().setCmdId(DataConfig.CmdId.List).setSeq(seq).setMissNum(missNum).setReceiverId(getReceiverIdInProtocol(), DataAppRequest.class)).setDeviceType(getDeviceType()).start((DJIDataCallBack) null);
    }

    private int writeAudioInfo(DJIAlbumFileInfo info, byte[] extBytes, int offset) {
        if (!isAudioInfoValid(extBytes, offset)) {
            return info.pathLength;
        }
        byte b = extBytes[offset];
        info.audioType = DJIAudioFileInfo.AudioType.find(b & 31);
        info.dataSource = (b >> 6) & 3;
        int offset2 = offset + 1;
        byte b2 = extBytes[offset2];
        info.samplingFrequency = DJIAudioFileInfo.SamplingFrequency.find(b2 & 31);
        info.samplingBit = DJIAudioFileInfo.SamplingBit.find(b2 >> 5);
        int offset3 = offset2 + 1;
        info.duration = BytesUtil.getUShort(extBytes, offset3);
        int offset4 = offset3 + 2;
        byte b3 = extBytes[offset4];
        int offset5 = offset4 + 1;
        byte[] fileName = new byte[b3];
        System.arraycopy(extBytes, offset5, fileName, 0, b3);
        info.fileName = BytesUtil.getStringUTF8(fileName);
        return offset5 + b3;
    }

    private boolean isAudioInfoValid(byte[] extBytes, int offset) {
        int offset2 = offset + 4;
        if (extBytes.length - 1 < offset2) {
            return false;
        }
        if (extBytes.length - 1 >= offset2 + extBytes[offset2]) {
            return true;
        }
        return false;
    }
}
