package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
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
public class DataRcGetName extends DataBase implements DJIDataSyncListener {
    private static DataRcGetName instance = null;

    public static synchronized DataRcGetName getInstance() {
        DataRcGetName dataRcGetName;
        synchronized (DataRcGetName.class) {
            if (instance == null) {
                instance = new DataRcGetName();
            }
            dataRcGetName = instance;
        }
        return dataRcGetName;
    }

    public String getName() {
        DJILogHelper.getInstance().LOGD("", "getrcname=" + BytesUtil.byte2hex(this._recData), false, true);
        return BytesUtil.getStringUTF8(this._recData);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetName.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
