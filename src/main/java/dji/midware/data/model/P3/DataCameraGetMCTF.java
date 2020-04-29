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
public class DataCameraGetMCTF extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetMCTF instance = null;

    public static synchronized DataCameraGetMCTF getInstance() {
        DataCameraGetMCTF dataCameraGetMCTF;
        synchronized (DataCameraGetMCTF.class) {
            if (instance == null) {
                instance = new DataCameraGetMCTF();
            }
            dataCameraGetMCTF = instance;
        }
        return dataCameraGetMCTF;
    }

    public boolean isEnable() {
        return ((Integer) get(0, 1, Integer.class)).intValue() != 0;
    }

    public int getStrength() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetMCTF.value();
        start(pack, callBack);
    }
}
