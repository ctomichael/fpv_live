package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycSetParams extends DataBase implements DJIDataSyncListener {
    private String[] indexs = null;
    private ParamInfo paramInfo;
    private Number value;
    private Number[] values = null;

    public void setIndexs(String... indexs2) {
        this.indexs = indexs2;
    }

    public void setValues(Number... values2) {
        this.values = values2;
    }

    public DataFlycSetParams setInfo(String index, Number value2) {
        this.paramInfo = DJIFlycParamInfoManager.read(index);
        this.value = value2;
        return this;
    }

    public DataFlycSetParams setInfo(String index) {
        this.paramInfo = DJIFlycParamInfoManager.read(index);
        this.value = this.paramInfo.setvalue;
        return this;
    }

    public void onRecvPackSeted(RecvPack recvPack) {
        super.onRecvPackSeted(recvPack);
        if (recvPack != null && recvPack.ccode == Ccode.OK.value()) {
            if (this.indexs != null) {
                for (int i = 0; i < this.indexs.length; i++) {
                    DJIFlycParamInfoManager.write(this.indexs[i], this.values[i]);
                }
                return;
            }
            DJIFlycParamInfoManager.write(this.paramInfo.name, this.value);
        }
    }

    public void setRecData(byte[] data) {
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int tmp;
        byte[] valuebytes;
        int tmp2;
        byte[] valuebytes2;
        int i;
        int tmp3 = 0;
        if (this.indexs != null) {
            int length = 0;
            for (int i2 = 0; i2 < this.indexs.length; i2++) {
                this.paramInfo = DJIFlycParamInfoManager.read(this.indexs[i2]);
                if (DJIFlycParamInfoManager.isNew()) {
                    i = this.paramInfo.size + 4;
                } else {
                    i = this.paramInfo.size + 2;
                }
                length += i;
            }
            this._sendData = new byte[length];
            for (int i3 = 0; i3 < this.indexs.length; i3++) {
                this.paramInfo = DJIFlycParamInfoManager.read(this.indexs[i3]);
                if (DJIFlycParamInfoManager.isNew()) {
                    BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.paramInfo.hash), this._sendData, tmp3);
                    tmp2 = tmp3 + 4;
                } else {
                    BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.paramInfo.index), this._sendData, tmp3);
                    tmp2 = tmp3 + 2;
                }
                this.value = this.values[i3];
                switch (this.paramInfo.typeId) {
                    case INT08S:
                    case INT16S:
                    case INT32S:
                    case INT08U:
                    case INT16U:
                        valuebytes2 = BytesUtil.getBytes(this.value.intValue());
                        break;
                    case INT64S:
                    case INT32U:
                    case INT64U:
                        valuebytes2 = BytesUtil.getBytes(this.value.longValue());
                        break;
                    case BYTE:
                        valuebytes2 = BytesUtil.getBytes((short) this.value.byteValue());
                        break;
                    case DOUBLE:
                        valuebytes2 = BytesUtil.getBytes(this.value.doubleValue());
                        break;
                    default:
                        valuebytes2 = BytesUtil.getBytes(this.value.floatValue());
                        break;
                }
                System.arraycopy(valuebytes2, 0, this._sendData, tmp2, this.paramInfo.size);
                tmp3 = tmp2 + this.paramInfo.size;
            }
            return;
        }
        if (DJIFlycParamInfoManager.isNew()) {
            this._sendData = new byte[(this.paramInfo.size + 4)];
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.paramInfo.hash), this._sendData, 0);
            tmp = 0 + 4;
        } else {
            this._sendData = new byte[(this.paramInfo.size + 2)];
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.paramInfo.index), this._sendData, 0);
            tmp = 0 + 2;
        }
        switch (this.paramInfo.typeId) {
            case INT08S:
            case INT16S:
            case INT32S:
            case INT08U:
            case INT16U:
                valuebytes = BytesUtil.getBytes(this.value.intValue());
                break;
            case INT64S:
            case INT32U:
            case INT64U:
                valuebytes = BytesUtil.getBytes(this.value.longValue());
                break;
            case BYTE:
                valuebytes = BytesUtil.getBytes((short) this.value.byteValue());
                break;
            case DOUBLE:
                valuebytes = BytesUtil.getBytes(this.value.doubleValue());
                break;
            default:
                valuebytes = BytesUtil.getBytes(this.value.floatValue());
                break;
        }
        System.arraycopy(valuebytes, 0, this._sendData, tmp, this.paramInfo.size);
        int tmp4 = tmp + this.paramInfo.size;
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
            pack.cmdId = CmdIdFlyc.CmdIdType.SetParamsByHash.value();
        } else {
            pack.cmdId = CmdIdFlyc.CmdIdType.SetParamsByIndex.value();
        }
        pack.timeOut = 1000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
