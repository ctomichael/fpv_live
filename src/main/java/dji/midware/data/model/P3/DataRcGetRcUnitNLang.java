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

@Keep
@EXClassNullAway
public class DataRcGetRcUnitNLang extends DataBase implements DJIDataSyncListener {
    private static DataRcGetRcUnitNLang instance = null;

    public static synchronized DataRcGetRcUnitNLang getInstance() {
        DataRcGetRcUnitNLang dataRcGetRcUnitNLang;
        synchronized (DataRcGetRcUnitNLang.class) {
            if (instance == null) {
                instance = new DataRcGetRcUnitNLang();
            }
            dataRcGetRcUnitNLang = instance;
        }
        return dataRcGetRcUnitNLang;
    }

    public int getUnit() {
        if ((((((Integer) get(0, 1, Integer.class)).intValue() & 192) >> 6) & 1) != 0) {
            return 0;
        }
        return 1;
    }

    public int getLang() {
        int res = ((Integer) get(0, 1, Integer.class)).intValue() & 63;
        for (int i = 0; i < 6; i++) {
            if ((res & 1) != 0) {
                return i;
            }
            res >>= 1;
        }
        return 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetRcUnitNLang.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
