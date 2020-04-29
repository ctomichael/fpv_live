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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycSetPlaneName extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetPlaneName instance = null;
    private String name;

    public static synchronized DataFlycSetPlaneName getInstance() {
        DataFlycSetPlaneName dataFlycSetPlaneName;
        synchronized (DataFlycSetPlaneName.class) {
            if (instance == null) {
                instance = new DataFlycSetPlaneName();
            }
            dataFlycSetPlaneName = instance;
        }
        return dataFlycSetPlaneName;
    }

    public DataFlycSetPlaneName setName(String name2) {
        this.name = name2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 32;
        this._sendData = new byte[32];
        byte[] namebytes = BytesUtil.getBytesUTF8(this.name);
        byte[] bArr = this._sendData;
        if (namebytes.length < 32) {
            i = namebytes.length;
        }
        System.arraycopy(namebytes, 0, bArr, 0, i);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetPlaneName.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 4;
        start(pack, callBack);
    }
}
