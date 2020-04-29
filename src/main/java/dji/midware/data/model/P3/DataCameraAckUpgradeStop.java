package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

@Keep
@EXClassNullAway
public class DataCameraAckUpgradeStop extends DataBase implements DJIDataAsync2Listener {
    private static DataCameraAckUpgradeStop instance = null;

    public static synchronized DataCameraAckUpgradeStop getInstance() {
        DataCameraAckUpgradeStop dataCameraAckUpgradeStop;
        synchronized (DataCameraAckUpgradeStop.class) {
            if (instance == null) {
                instance = new DataCameraAckUpgradeStop();
            }
            dataCameraAckUpgradeStop = instance;
        }
        return dataCameraAckUpgradeStop;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 0;
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.AckUpgradeStop.value();
        pack.seq = this.pack.seq;
        start(pack);
    }
}
