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
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetEquipInfo extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetEquipInfo instance = null;
    private int mClipId;
    private char mEquipLabel;
    private int mRealName;

    public static synchronized DataCameraSetEquipInfo getInstance() {
        DataCameraSetEquipInfo dataCameraSetEquipInfo;
        synchronized (DataCameraSetEquipInfo.class) {
            if (instance == null) {
                instance = new DataCameraSetEquipInfo();
            }
            dataCameraSetEquipInfo = instance;
        }
        return dataCameraSetEquipInfo;
    }

    public DataCameraSetEquipInfo setEquipLabel(char label) {
        this.mEquipLabel = label;
        return this;
    }

    public DataCameraSetEquipInfo setRealName(int name) {
        this.mRealName = name;
        return this;
    }

    public DataCameraSetEquipInfo setClipId(int id) {
        this.mClipId = id;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.mEquipLabel;
        this._sendData[1] = (byte) (this.mRealName & 255);
        this._sendData[2] = (byte) (((this.mRealName >> 8) & 3) | (this.mClipId << 2));
        this._sendData[3] = (byte) (this.mClipId >> 6);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetRawEquipInfo.value();
        pack.timeOut = 6000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
