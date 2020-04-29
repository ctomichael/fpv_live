package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
public class DataCommonSetStartModuleChecking extends DataBase implements DJIDataSyncListener {
    private static DataCommonSetStartModuleChecking instance = null;

    public static DataCommonSetStartModuleChecking getInstance() {
        if (instance == null) {
            instance = new DataCommonSetStartModuleChecking();
        }
        return instance;
    }

    public boolean isCameraBPCPushing() {
        return ((Integer) get(0, 4, Integer.class)).intValue() == 10;
    }

    public int getCameraBPSState() {
        if (isCameraBPCPushing()) {
            return ((Integer) get(6, 1, Integer.class)).intValue();
        }
        return -1;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[7];
        System.arraycopy(BytesUtil.getBytes(9), 0, this._sendData, 0, 4);
        System.arraycopy(BytesUtil.getBytes(1), 0, this._sendData, 4, 2);
        this._sendData[6] = 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES_BY_PUSH.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.SetStartModuleChecking.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
