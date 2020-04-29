package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
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
public class DataCommonGetPushAppGpsConfig extends DataBase implements DJIDataSyncListener {
    private static DataCommonGetPushAppGpsConfig instance = null;
    private Ccode ResponseCode = Ccode.UNDEFINED;
    private boolean isStart = false;
    private int pushInterval = 0;

    public static DataCommonGetPushAppGpsConfig getInstance() {
        if (instance == null) {
            instance = new DataCommonGetPushAppGpsConfig();
        }
        return instance;
    }

    public boolean isStart() {
        boolean z;
        if (((Integer) get(0, 1, Integer.class)).intValue() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.isStart = z;
        if (!this.isStart) {
            this.pushInterval = 0;
        }
        return this.isStart;
    }

    public int getPushInterval() {
        this.pushInterval = ((Integer) get(1, 4, Integer.class)).intValue();
        return this.pushInterval;
    }

    public void setResponseCode(boolean isOpenGps) {
        if (!this.isStart) {
            this.ResponseCode = Ccode.OK;
        } else if (!isOpenGps) {
            this.ResponseCode = Ccode.NOGPS;
        } else {
            this.ResponseCode = Ccode.OK;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.GetPushAppGpsConfig.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.ResponseCode.value();
    }
}
