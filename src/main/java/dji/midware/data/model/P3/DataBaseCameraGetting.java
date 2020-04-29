package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataBaseCameraGetting extends DataBase implements DJIDataSyncListener {
    protected String cmdId;
    protected CmdIdCamera.CmdIdType cmdIdType;
    protected int value;

    public DataBaseCameraGetting setCmdId(CmdIdCamera.CmdIdType cmdIdType2) {
        this.cmdIdType = cmdIdType2;
        return this;
    }

    public void setCmdId(String cmdId2) {
        this.cmdId = cmdId2;
    }

    public int getValue() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        if (this.cmdIdType != null) {
            pack.cmdId = this.cmdIdType.value();
        } else {
            pack.cmdId = CmdIdCamera.CmdIdType.valueOf("Get" + this.cmdId).value();
        }
        start(pack, callBack);
    }
}
