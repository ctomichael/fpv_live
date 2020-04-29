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
public class DataCommonSetMultiParam extends DataBase implements DJIDataSyncListener {
    private static DataCommonSetMultiParam mInstance = null;
    private byte[] mParams = null;
    private DeviceType mReceiverType = DeviceType.CAMERA;

    public static synchronized DataCommonSetMultiParam getInstance() {
        DataCommonSetMultiParam dataCommonSetMultiParam;
        synchronized (DataCommonSetMultiParam.class) {
            if (mInstance == null) {
                mInstance = new DataCommonSetMultiParam();
            }
            dataCommonSetMultiParam = mInstance;
        }
        return dataCommonSetMultiParam;
    }

    public int getErrorCmdId() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataCommonSetMultiParam setMultiParam(byte[] cmds) {
        this.mParams = cmds;
        return this;
    }

    public DataCommonSetMultiParam setReceiverType(DeviceType device) {
        this.mReceiverType = device;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = this.mParams;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.SetMultiParam.value();
        pack.data = getSendData();
        pack.timeOut = this.mParams[0] * 1000;
        start(pack, callBack);
    }
}
