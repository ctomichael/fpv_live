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
public class DataCameraGetRecordFan extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetRecordFan mInstance = null;

    public static synchronized DataCameraGetRecordFan getInstance() {
        DataCameraGetRecordFan dataCameraGetRecordFan;
        synchronized (DataCameraGetRecordFan.class) {
            if (mInstance == null) {
                mInstance = new DataCameraGetRecordFan();
            }
            dataCameraGetRecordFan = mInstance;
        }
        return dataCameraGetRecordFan;
    }

    public boolean isForceTurnOffFan() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getForceFanOnT() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getForceFanOffT() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetRecordFan.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
