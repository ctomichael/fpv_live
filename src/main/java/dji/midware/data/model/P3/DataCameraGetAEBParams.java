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
public class DataCameraGetAEBParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetAEBParams instance = null;

    public static synchronized DataCameraGetAEBParams getInstance() {
        DataCameraGetAEBParams dataCameraGetAEBParams;
        synchronized (DataCameraGetAEBParams.class) {
            if (instance == null) {
                instance = new DataCameraGetAEBParams();
            }
            dataCameraGetAEBParams = instance;
        }
        return dataCameraGetAEBParams;
    }

    public int getExposureValue() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getAEBNumber() {
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetAEBParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
