package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataOsdOsmoCalibration extends DataBase implements DJIDataSyncListener {
    private static DataOsdOsmoCalibration instance = null;
    private byte mType;

    public static synchronized DataOsdOsmoCalibration getInstance() {
        DataOsdOsmoCalibration dataOsdOsmoCalibration;
        synchronized (DataOsdOsmoCalibration.class) {
            if (instance == null) {
                instance = new DataOsdOsmoCalibration();
            }
            dataOsdOsmoCalibration = instance;
        }
        return dataOsdOsmoCalibration;
    }

    public void setCalibration(int type) {
        this.mType = (byte) type;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = this.mType;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.OsmoCalibration.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
