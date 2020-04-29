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
public class DataCameraGetCustomInformation extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetCustomInformation instance = null;

    public static synchronized DataCameraGetCustomInformation getInstance() {
        DataCameraGetCustomInformation dataCameraGetCustomInformation;
        synchronized (DataCameraGetCustomInformation.class) {
            if (instance == null) {
                instance = new DataCameraGetCustomInformation();
            }
            dataCameraGetCustomInformation = instance;
        }
        return dataCameraGetCustomInformation;
    }

    public String getCustomInformation() {
        return getUTF8(1, ((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetUserCustomInfo.value();
        start(pack, callBack);
    }
}
