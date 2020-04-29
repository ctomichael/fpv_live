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
public class DataRcSetName extends DataBase implements DJIDataSyncListener {
    private static DataRcSetName instance = null;
    private String name;

    public static synchronized DataRcSetName getInstance() {
        DataRcSetName dataRcSetName;
        synchronized (DataRcSetName.class) {
            if (instance == null) {
                instance = new DataRcSetName();
            }
            dataRcSetName = instance;
        }
        return dataRcSetName;
    }

    public DataRcSetName setName(String name2) {
        this.name = name2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        byte[] namebytes = BytesUtil.getBytesUTF8(this.name);
        System.arraycopy(namebytes, 0, this._sendData, 0, namebytes.length);
        DJILogHelper.getInstance().LOGD("", "setrcname=" + BytesUtil.byte2hex(this._sendData), false, true);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetName.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
