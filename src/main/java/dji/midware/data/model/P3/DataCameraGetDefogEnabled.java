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
public class DataCameraGetDefogEnabled extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetDefogEnabled instance = null;

    public static synchronized DataCameraGetDefogEnabled getInstance() {
        DataCameraGetDefogEnabled dataCameraGetDefogEnabled;
        synchronized (DataCameraGetDefogEnabled.class) {
            if (instance == null) {
                instance = new DataCameraGetDefogEnabled();
            }
            dataCameraGetDefogEnabled = instance;
        }
        return dataCameraGetDefogEnabled;
    }

    public boolean getEnabled() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 1;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetDefogEnabled.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
