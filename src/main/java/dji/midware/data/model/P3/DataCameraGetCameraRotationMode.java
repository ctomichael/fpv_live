package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSetCameraRotationMode;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraGetCameraRotationMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetCameraRotationMode instance;

    public static synchronized DataCameraGetCameraRotationMode getInstance() {
        DataCameraGetCameraRotationMode dataCameraGetCameraRotationMode;
        synchronized (DataCameraGetCameraRotationMode.class) {
            if (instance == null) {
                instance = new DataCameraGetCameraRotationMode();
            }
            dataCameraGetCameraRotationMode = instance;
        }
        return dataCameraGetCameraRotationMode;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetCameraRotationMode.value();
        pack.data = getSendData();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getRotationMode() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataCameraSetCameraRotationMode.RotationAngleType getRotationAngleType() {
        return getRotationAngleType(-1);
    }

    public DataCameraSetCameraRotationMode.RotationAngleType getRotationAngleType(int index) {
        return DataCameraSetCameraRotationMode.RotationAngleType.find(((Integer) get(1, 1, Integer.class, index)).intValue());
    }
}
