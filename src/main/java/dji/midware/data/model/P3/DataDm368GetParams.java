package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdDm368;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataDm368SetParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataDm368GetParams extends DataBase implements DJIDataSyncListener {
    private static DataDm368GetParams instance = null;
    private DataDm368SetParams.DM368CmdId cmdId;
    private int mPercent = 0;

    public static synchronized DataDm368GetParams getInstance() {
        DataDm368GetParams dataDm368GetParams;
        synchronized (DataDm368GetParams.class) {
            if (instance == null) {
                instance = new DataDm368GetParams();
            }
            dataDm368GetParams = instance;
        }
        return dataDm368GetParams;
    }

    public DataDm368GetParams set(DataDm368SetParams.DM368CmdId cmdId2) {
        this.cmdId = cmdId2;
        return this;
    }

    public int getResult() {
        if (this.cmdId == DataDm368SetParams.DM368CmdId.BandwidthPercentage) {
            this.mPercent = ((Integer) get(2, 1, Integer.class)).intValue();
        }
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getBandWidthPercent() {
        return this.mPercent;
    }

    public int getBandWidthPercentInstant() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getCmdId() {
        return this.cmdId.value();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.cmdId.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverId = 1;
        pack.receiverType = DeviceType.DM368.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.GetParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
