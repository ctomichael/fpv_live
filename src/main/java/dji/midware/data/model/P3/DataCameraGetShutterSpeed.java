package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSetShutterSpeed;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraGetShutterSpeed extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetShutterSpeed instance = null;

    public static synchronized DataCameraGetShutterSpeed getInstance() {
        DataCameraGetShutterSpeed dataCameraGetShutterSpeed;
        synchronized (DataCameraGetShutterSpeed.class) {
            if (instance == null) {
                instance = new DataCameraGetShutterSpeed();
            }
            dataCameraGetShutterSpeed = instance;
        }
        return dataCameraGetShutterSpeed;
    }

    public DataCameraSetShutterSpeed.TYPE getAuto() {
        return DataCameraSetShutterSpeed.TYPE.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean isReciprocal() {
        return (((Integer) get(1, 2, Integer.class)).intValue() >> 15) == 1;
    }

    public float getValue() {
        int integral = ((Integer) get(1, 2, Integer.class)).intValue() & -32769;
        return Float.valueOf(integral + "." + ((Integer) get(3, 1, Integer.class)).intValue()).floatValue();
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetShutterSpeed.value();
        start(pack, callBack);
    }
}
