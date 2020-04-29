package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCenter;
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
public class DataCenterSetSelfDischarge extends DataBase implements DJIDataSyncListener {
    private static DataCenterSetSelfDischarge instance;
    private int mDays = 7;
    private int mEncrypt = 0;

    public static synchronized DataCenterSetSelfDischarge getInstance() {
        DataCenterSetSelfDischarge dataCenterSetSelfDischarge;
        synchronized (DataCenterSetSelfDischarge.class) {
            if (instance == null) {
                instance = new DataCenterSetSelfDischarge();
            }
            dataCenterSetSelfDischarge = instance;
        }
        return dataCenterSetSelfDischarge;
    }

    public DataCenterSetSelfDischarge setEncrypt(int encrypt) {
        this.mEncrypt = encrypt;
        return this;
    }

    public DataCenterSetSelfDischarge setDays(int days) {
        this.mDays = days;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        this._sendData[0] = (byte) this.mEncrypt;
        System.arraycopy(BytesUtil.getBytes(this.mDays), 0, this._sendData, 1, 4);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CENTER.value();
        pack.cmdId = CmdIdCenter.CmdIdType.SetSelfDischarge.value();
        pack.timeOut = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        start(pack, callBack);
    }
}
