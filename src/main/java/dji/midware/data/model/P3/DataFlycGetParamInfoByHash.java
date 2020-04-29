package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycGetParamInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.data.params.P3.RangeModel;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycGetParamInfoByHash extends DataBase implements DJIDataSyncListener {
    private static DataFlycGetParamInfoByHash mInstance = null;
    private long hash;
    private String mIndex = null;

    public static DataFlycGetParamInfoByHash getInstance() {
        if (mInstance == null) {
            mInstance = new DataFlycGetParamInfoByHash();
        }
        return mInstance;
    }

    public DataFlycGetParamInfoByHash setIndex(String index) {
        ParamInfo paramInfo = DJIFlycParamInfoManager.read(index);
        this.mIndex = index;
        if (paramInfo != null) {
            this.hash = paramInfo.hash;
        }
        return this;
    }

    private void setRange(RangeModel range, Class<? extends Number> cls) {
        range.minValue = get(7, 4, cls);
        range.maxValue = get(11, 4, cls);
        range.defaultValue = get(15, 4, cls);
    }

    public ParamInfo getParamInfo() {
        DataFlycGetParamInfo.TypeId typeId = DataFlycGetParamInfo.TypeId.find(((Integer) get(1, 2, Integer.class)).intValue());
        ParamInfo paramInfo = new ParamInfo();
        RangeModel range = new RangeModel();
        switch (typeId) {
            case INT08S:
            case INT16S:
            case INT32S:
            case INT08U:
            case INT16U:
                paramInfo.type = Integer.class;
                break;
            case INT64S:
            case INT32U:
            case INT64U:
                paramInfo.type = Long.class;
                break;
            case BYTE:
                paramInfo.type = Byte.class;
                break;
            case DOUBLE:
                paramInfo.type = Double.class;
                break;
            default:
                paramInfo.type = Float.class;
                break;
        }
        paramInfo.typeId = typeId;
        paramInfo.size = ((Integer) get(3, 2, Integer.class)).intValue();
        paramInfo.attribute = DataFlycGetParamInfo.Attribute.find(((Integer) get(5, 2, Integer.class)).intValue());
        setRange(range, paramInfo.type);
        paramInfo.range = range;
        if (this._recData == null || this._recData.length - 19 < 0) {
            paramInfo.name = "";
        } else {
            paramInfo.name = get(19, this._recData.length - 19);
        }
        return paramInfo;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetParamInfoByHash.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.hash), this._sendData, 0);
    }
}
