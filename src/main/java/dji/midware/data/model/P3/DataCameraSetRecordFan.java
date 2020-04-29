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
import dji.midware.media.DJIVideoDecoder;

@Keep
@EXClassNullAway
public class DataCameraSetRecordFan extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetRecordFan mInstance = null;
    private int mFanOffT = 0;
    private int mFanOnT = 0;
    private byte mValue = 0;

    public static synchronized DataCameraSetRecordFan getInstance() {
        DataCameraSetRecordFan dataCameraSetRecordFan;
        synchronized (DataCameraSetRecordFan.class) {
            if (mInstance == null) {
                mInstance = new DataCameraSetRecordFan();
            }
            dataCameraSetRecordFan = mInstance;
        }
        return dataCameraSetRecordFan;
    }

    public DataCameraSetRecordFan reset() {
        this.mValue = 0;
        return this;
    }

    public DataCameraSetRecordFan setIsForceTurnOffFan(boolean enable) {
        if (enable) {
            this.mValue = 1;
        } else {
            this.mValue = 0;
        }
        return this;
    }

    public DataCameraSetRecordFan setFanOnT(int t) {
        this.mFanOnT = t;
        return this;
    }

    public DataCameraSetRecordFan setFanOffT(int t) {
        this.mFanOffT = t;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = this.mValue;
        this._sendData[1] = (byte) this.mFanOnT;
        this._sendData[2] = (byte) this.mFanOffT;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetRecordFan.value();
        pack.data = getSendData();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        start(pack, callBack);
    }
}
