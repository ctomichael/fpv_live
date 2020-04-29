package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraSetMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetMode instance = null;
    private DataCameraGetMode.MODE mode;

    public static synchronized DataCameraSetMode getInstance() {
        DataCameraSetMode dataCameraSetMode;
        synchronized (DataCameraSetMode.class) {
            if (instance == null) {
                instance = new DataCameraSetMode();
            }
            dataCameraSetMode = instance;
        }
        return dataCameraSetMode;
    }

    public DataCameraSetMode setMode(DataCameraGetMode.MODE mode2) {
        this.mode = mode2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mode.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetMode.value();
        pack.repeatTimes = 4;
        pack.timeOut = 3000;
        start(pack, callBack);
    }
}
