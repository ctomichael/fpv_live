package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycSetFlyforbidData extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetFlyforbidData instance = null;
    private int mDataType = 0;

    public static synchronized DataFlycSetFlyforbidData getInstance() {
        DataFlycSetFlyforbidData dataFlycSetFlyforbidData;
        synchronized (DataFlycSetFlyforbidData.class) {
            if (instance == null) {
                instance = new DataFlycSetFlyforbidData();
            }
            dataFlycSetFlyforbidData = instance;
        }
        return dataFlycSetFlyforbidData;
    }

    public DataFlycSetFlyforbidData setFlyforbidData(int type) {
        this.mDataType = type;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetFlyforbidData.value();
        super.start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        if (this.mDataType == 1) {
            this._sendData[0] = 64;
        } else {
            this._sendData[0] = 63;
        }
    }
}
