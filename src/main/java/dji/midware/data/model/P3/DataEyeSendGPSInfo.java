package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataEyeSendGPSInfo extends DataBase implements DJIDataSyncListener {
    private static DataEyeSendGPSInfo instance = null;
    private int mAccuracy = 0;
    private double mLantitue = 0.0d;
    private double mLongtitue = 0.0d;

    public static synchronized DataEyeSendGPSInfo getInstance() {
        DataEyeSendGPSInfo dataEyeSendGPSInfo;
        synchronized (DataEyeSendGPSInfo.class) {
            if (instance == null) {
                instance = new DataEyeSendGPSInfo();
            }
            dataEyeSendGPSInfo = instance;
        }
        return dataEyeSendGPSInfo;
    }

    public DataEyeSendGPSInfo setLocation(double longtitue, double lantitue, int accuracy) {
        this.mLongtitue = longtitue;
        this.mLantitue = lantitue;
        this.mAccuracy = accuracy;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this._sendData == null) {
            this._sendData = new byte[22];
        }
        System.arraycopy(BytesUtil.getBytes(this.mLongtitue), 0, this._sendData, 0, 8);
        System.arraycopy(BytesUtil.getBytes(this.mLantitue), 0, this._sendData, 8, 8);
        System.arraycopy(BytesUtil.getBytes((short) this.mAccuracy), 0, this._sendData, 16, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SendGPSInfo.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
