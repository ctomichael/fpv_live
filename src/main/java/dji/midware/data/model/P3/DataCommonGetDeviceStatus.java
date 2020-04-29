package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCommonGetDeviceStatus extends DataBase implements DJIDataSyncListener {
    private static DataCommonGetDeviceStatus instance = null;
    private int mFirst = 0;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.RC;
    private int mSecond = 0;

    public static synchronized DataCommonGetDeviceStatus getInstance() {
        DataCommonGetDeviceStatus dataCommonGetDeviceStatus;
        synchronized (DataCommonGetDeviceStatus.class) {
            if (instance == null) {
                instance = new DataCommonGetDeviceStatus();
            }
            dataCommonGetDeviceStatus = instance;
        }
        return dataCommonGetDeviceStatus;
    }

    public DataCommonGetDeviceStatus setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonGetDeviceStatus setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public DataCommonGetDeviceStatus setVersioin(int first, int second) {
        this.mFirst = first;
        this.mSecond = second;
        return this;
    }

    public int getMode() {
        return ((Integer) get(1, 4, Integer.class)).intValue() & 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.GetDeviceStatus.value();
        pack.timeOut = 5000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}
