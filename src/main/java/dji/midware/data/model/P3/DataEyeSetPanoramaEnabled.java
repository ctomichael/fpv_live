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

@Keep
@EXClassNullAway
public class DataEyeSetPanoramaEnabled extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetPanoramaEnabled instance = null;
    private int flag;
    private int type;

    public static synchronized DataEyeSetPanoramaEnabled getInstance() {
        DataEyeSetPanoramaEnabled dataEyeSetPanoramaEnabled;
        synchronized (DataEyeSetPanoramaEnabled.class) {
            if (instance == null) {
                instance = new DataEyeSetPanoramaEnabled();
            }
            dataEyeSetPanoramaEnabled = instance;
        }
        return dataEyeSetPanoramaEnabled;
    }

    public DataEyeSetPanoramaEnabled setFlag(int flag2) {
        this.flag = flag2;
        return this;
    }

    public DataEyeSetPanoramaEnabled setType(int type2) {
        this.type = type2;
        return this;
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
        pack.cmdId = CmdIdEYE.CmdIdType.SetPanoramaEnabled.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.flag;
        this._sendData[1] = (byte) this.type;
    }
}
