package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
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
public class DataRcSetPassword extends DataBase implements DJIDataSyncListener {
    private static DataRcSetPassword instance = null;
    private int pw;

    public static synchronized DataRcSetPassword getInstance() {
        DataRcSetPassword dataRcSetPassword;
        synchronized (DataRcSetPassword.class) {
            if (instance == null) {
                instance = new DataRcSetPassword();
            }
            dataRcSetPassword = instance;
        }
        return dataRcSetPassword;
    }

    public DataRcSetPassword setPw(int pw2) {
        this.pw = pw2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.pw), 0, this._sendData, 0, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetPassword.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
