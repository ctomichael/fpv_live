package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
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
public class DataFlycGetParams extends DataBase implements DJIDataSyncListener {
    private static final Object INDEXES_LOCK = new Object();
    private static DataFlycGetParams instance = null;
    private String[] indexs = new String[0];

    public static synchronized DataFlycGetParams getInstance() {
        DataFlycGetParams dataFlycGetParams;
        synchronized (DataFlycGetParams.class) {
            if (instance == null) {
                instance = new DataFlycGetParams();
            }
            dataFlycGetParams = instance;
        }
        return dataFlycGetParams;
    }

    public DataFlycGetParams setInfos(String[] indexs2) {
        synchronized (INDEXES_LOCK) {
            this.indexs = indexs2;
        }
        return this;
    }

    public void setRecData(byte[] data) {
        int tmp;
        super.setRecData(data);
        if (DJIFlycParamInfoManager.isNew()) {
            tmp = 1 + 4;
        } else {
            tmp = 1 + 2;
        }
        String[] indexesCloned = getIndexesCloned();
        for (int i = 0; i < indexesCloned.length; i++) {
            ParamInfo paramInfo = DJIFlycParamInfoManager.read(indexesCloned[i]);
            if (paramInfo != null) {
                long hashValue = 0;
                try {
                    hashValue = BytesUtil.getUInt(this._recData, tmp - 4);
                } catch (Exception e) {
                }
                if (hashValue == paramInfo.hash) {
                    DJIFlycParamInfoManager.write(indexesCloned[i], get(tmp, paramInfo.size, paramInfo.type));
                    if (DJIFlycParamInfoManager.isNew()) {
                        tmp += paramInfo.size + 4;
                    } else {
                        tmp += paramInfo.size + 2;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        String[] indexsCloned = getIndexesCloned();
        if (DJIFlycParamInfoManager.isNew()) {
            this._sendData = new byte[(indexsCloned.length * 4)];
            for (int i = 0; i < indexsCloned.length; i++) {
                ParamInfo paramInfo = DJIFlycParamInfoManager.read(indexsCloned[i]);
                if (paramInfo == null) {
                    DJILog.save("DataFlycGetParams", "missing key : " + indexsCloned[i]);
                } else {
                    BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(paramInfo.hash), this._sendData, i * 4);
                }
            }
            return;
        }
        this._sendData = new byte[(indexsCloned.length * 2)];
        for (int i2 = 0; i2 < indexsCloned.length; i2++) {
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(DJIFlycParamInfoManager.read(indexsCloned[i2]).index), this._sendData, i2 * 2);
        }
    }

    private String[] getIndexesCloned() {
        String[] indexsCloned;
        synchronized (INDEXES_LOCK) {
            indexsCloned = (String[]) this.indexs.clone();
        }
        return indexsCloned;
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
            pack.cmdId = CmdIdFlyc.CmdIdType.GetParamsByHash.value();
        } else {
            pack.cmdId = CmdIdFlyc.CmdIdType.GetParamsByIndex.value();
        }
        start(pack, callBack);
    }
}
