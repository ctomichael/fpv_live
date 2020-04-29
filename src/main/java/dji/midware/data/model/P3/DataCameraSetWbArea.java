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
public class DataCameraSetWbArea extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetWbArea instance = null;
    private int mIndex;

    public static synchronized DataCameraSetWbArea getInstance() {
        DataCameraSetWbArea dataCameraSetWbArea;
        synchronized (DataCameraSetWbArea.class) {
            if (instance == null) {
                instance = new DataCameraSetWbArea();
            }
            dataCameraSetWbArea = instance;
        }
        return dataCameraSetWbArea;
    }

    public DataCameraSetWbArea setIndex(int index) {
        this.mIndex = index;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mIndex;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetWhiteBalanceArea.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
