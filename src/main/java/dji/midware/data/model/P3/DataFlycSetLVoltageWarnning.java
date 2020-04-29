package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycGetVoltageWarnning;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycSetLVoltageWarnning extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetLVoltageWarnning instance = null;
    private boolean isNeedGoHome;
    private DataFlycGetVoltageWarnning.WarnningLevel level;
    private int value;

    public static synchronized DataFlycSetLVoltageWarnning getInstance() {
        DataFlycSetLVoltageWarnning dataFlycSetLVoltageWarnning;
        synchronized (DataFlycSetLVoltageWarnning.class) {
            if (instance == null) {
                instance = new DataFlycSetLVoltageWarnning();
            }
            dataFlycSetLVoltageWarnning = instance;
        }
        return dataFlycSetLVoltageWarnning;
    }

    public void setWarnningLevel(DataFlycGetVoltageWarnning.WarnningLevel level2) {
        this.level = level2;
    }

    public void setValue(int value2) {
        this.value = value2;
    }

    public void setIsNeedGoHome(boolean isNeedGoHome2) {
        this.isNeedGoHome = isNeedGoHome2;
    }

    public void setIsNeedLanding(boolean isNeedLanding) {
        this.isNeedGoHome = isNeedLanding;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 1;
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.level.value();
        this._sendData[1] = (byte) this.value;
        byte[] bArr = this._sendData;
        if (!this.isNeedGoHome) {
            i = 0;
        }
        bArr[2] = (byte) i;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetVoltageWarnning.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
