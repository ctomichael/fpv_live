package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlight;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataFlightDeleteVFenceData extends DataBase implements DJIDataSyncListener {
    private static DataFlightDeleteVFenceData mInstance = null;
    private boolean shouldDelete;

    public static synchronized DataFlightDeleteVFenceData getInstance() {
        DataFlightDeleteVFenceData dataFlightDeleteVFenceData;
        synchronized (DataFlightDeleteVFenceData.class) {
            if (mInstance == null) {
                mInstance = new DataFlightDeleteVFenceData();
            }
            dataFlightDeleteVFenceData = mInstance;
        }
        return dataFlightDeleteVFenceData;
    }

    public DataFlightDeleteVFenceData shouldDelete(boolean shouldDelete2) {
        this.shouldDelete = shouldDelete2;
        return this;
    }

    public boolean getRspResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.shouldDelete) {
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
        pack.cmdId = CmdIdFlight.CmdIdType.DeleteVFenceData.value();
        super.start(pack, callBack);
    }
}
