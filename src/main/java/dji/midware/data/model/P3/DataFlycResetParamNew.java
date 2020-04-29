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
public class DataFlycResetParamNew extends DataBase implements DJIDataSyncListener {
    private String[] indexs = null;

    public DataFlycResetParamNew setInfos(String[] indexs2) {
        this.indexs = indexs2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (DJIFlycParamInfoManager.isNew()) {
            this._sendData = new byte[(this.indexs.length * 4)];
            for (int i = 0; i < this.indexs.length; i++) {
                ParamInfo paramInfo = DJIFlycParamInfoManager.read(this.indexs[i]);
                if (paramInfo != null) {
                    BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(paramInfo.hash), this._sendData, i * 4);
                }
            }
            return;
        }
        this._sendData = new byte[(this.indexs.length * 2)];
        for (int i2 = 0; i2 < this.indexs.length; i2++) {
            ParamInfo paramInfo2 = DJIFlycParamInfoManager.read(this.indexs[i2]);
            if (paramInfo2 != null) {
                BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(paramInfo2.index), this._sendData, i2 * 2);
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
