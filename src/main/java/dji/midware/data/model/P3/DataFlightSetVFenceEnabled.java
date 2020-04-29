package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlight;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataFlightSetVFenceEnabled extends DataBase implements DJIDataSyncListener {
    private static DataFlightSetVFenceEnabled mInstance = null;
    private boolean isVFenceEnabled;

    public static synchronized DataFlightSetVFenceEnabled getInstance() {
        DataFlightSetVFenceEnabled dataFlightSetVFenceEnabled;
        synchronized (DataFlightSetVFenceEnabled.class) {
            if (mInstance == null) {
                mInstance = new DataFlightSetVFenceEnabled();
            }
            dataFlightSetVFenceEnabled = mInstance;
        }
        return dataFlightSetVFenceEnabled;
    }

    public DataFlightSetVFenceEnabled setVFenceEnabled(boolean enabled) {
        this.isVFenceEnabled = enabled;
        return this;
    }

    public int getRspResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.isVFenceEnabled) {
            this._sendData[0] = Byte.MIN_VALUE;
        } else {
            this._sendData[0] = 0;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 5;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.Flight.value();
        pack.cmdId = CmdIdFlight.CmdIdType.SetVFenceEnabled.value();
        super.start(pack, callBack);
    }
}
