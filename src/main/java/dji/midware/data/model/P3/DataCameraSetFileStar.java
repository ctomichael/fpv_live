package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraSetFileStar extends DataBase {
    private static DataCameraSetFileStar ourInstance = new DataCameraSetFileStar();
    private int mFileIndex = -1;
    private int[] mFilesIndex = null;
    boolean mIsAddStar = true;

    public static DataCameraSetFileStar getInstance() {
        return ourInstance;
    }

    public DataCameraSetFileStar setFilesIndex(int[] filesIndex) {
        this.mFilesIndex = filesIndex;
        return this;
    }

    public DataCameraSetFileStar setFileIndex(int fileIndex) {
        this.mFileIndex = fileIndex;
        return this;
    }

    public DataCameraSetFileStar isAddStar(boolean isStar) {
        this.mIsAddStar = isStar;
        return this;
    }

    public int getFailNum() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int[] getFailList() {
        int failNum = getFailNum();
        if (failNum == 0) {
            return null;
        }
        int[] res = new int[failNum];
        for (int i = 0; i < failNum; i++) {
            res[i] = ((Integer) get((i * 4) + 1, 4, Integer.class)).intValue();
        }
        return res;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int isStar;
        if (this.mIsAddStar) {
            isStar = 1;
        } else {
            isStar = 0;
        }
        if (this.mFilesIndex == null) {
            this._sendData = new byte[6];
            this._sendData[0] = 1;
            this._sendData[1] = (byte) isStar;
            System.arraycopy(BytesUtil.getBytes(this.mFileIndex), 0, this._sendData, 2, 4);
            return;
        }
        int size = this.mFilesIndex.length;
        this._sendData = new byte[((size * 4) + 2)];
        this._sendData[0] = (byte) size;
        this._sendData[1] = (byte) isStar;
        for (int i = 0; i < size; i++) {
            System.arraycopy(BytesUtil.getBytes(this.mFilesIndex[i]), 0, this._sendData, (i * 4) + 2, 4);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetFileStar.value();
        pack.timeOut = 1000;
        start(pack, callBack);
    }
}
