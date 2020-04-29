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
public class DataCameraSetDustReduction extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetDustReduction instance = null;
    private int state;

    @Keep
    public enum State {
        ENTER,
        START,
        EXIT
    }

    public static synchronized DataCameraSetDustReduction getInstance() {
        DataCameraSetDustReduction dataCameraSetDustReduction;
        synchronized (DataCameraSetDustReduction.class) {
            if (instance == null) {
                instance = new DataCameraSetDustReduction();
            }
            dataCameraSetDustReduction = instance;
        }
        return dataCameraSetDustReduction;
    }

    public DataCameraSetDustReduction setState(State state2) {
        this.state = state2.ordinal() + 1;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.state;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetDustReductionState.value();
        pack.doPack();
        start(pack, callBack);
    }
}
