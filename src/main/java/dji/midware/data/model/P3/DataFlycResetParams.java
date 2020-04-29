package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycResetParams extends DataBase implements DJIDataSyncListener {
    private static DataFlycResetParams instance = null;
    private String[] indexs = null;
    private ParamInfo paramInfo;

    public static synchronized DataFlycResetParams getInstance() {
        DataFlycResetParams dataFlycResetParams;
        synchronized (DataFlycResetParams.class) {
            if (instance == null) {
                instance = new DataFlycResetParams();
            }
            dataFlycResetParams = instance;
        }
        return dataFlycResetParams;
    }

    public DataFlycResetParams setIndexs(String... indexs2) {
        this.indexs = indexs2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.indexs != null) {
            int tmp = 0;
            int length = 0;
            for (int i = 0; i < this.indexs.length; i++) {
                this.paramInfo = DJIFlycParamInfoManager.read(this.indexs[i]);
                if (DJIFlycParamInfoManager.isNew()) {
                    length += 4;
                } else {
                    length += 2;
                }
            }
            this._sendData = new byte[length];
            for (int i2 = 0; i2 < this.indexs.length; i2++) {
                this.paramInfo = DJIFlycParamInfoManager.read(this.indexs[i2]);
                if (DJIFlycParamInfoManager.isNew()) {
                    BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.paramInfo.hash), this._sendData, tmp);
                    tmp += 4;
                } else {
                    BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.paramInfo.index), this._sendData, tmp);
                    tmp += 2;
                }
            }
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        if (DJIFlycParamInfoManager.isNew()) {
            pack.cmdId = CmdIdFlyc.CmdIdType.ResetParamsByHash.value();
        } else {
            pack.cmdId = CmdIdFlyc.CmdIdType.ResetParamsByIndex.value();
        }
        start(pack, callBack);
    }
}
