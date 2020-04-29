package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIGimbalParamInfoManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataGimbalRoninSetUserParams extends DataBase implements DJIDataSyncListener {
    private static DataGimbalRoninSetUserParams instance = null;
    private String[] indexs = null;
    private ParamInfo paramInfo;
    private Number value;
    private Number[] values = null;

    public static synchronized DataGimbalRoninSetUserParams getInstance() {
        DataGimbalRoninSetUserParams dataGimbalRoninSetUserParams;
        synchronized (DataGimbalRoninSetUserParams.class) {
            if (instance == null) {
                instance = new DataGimbalRoninSetUserParams();
            }
            dataGimbalRoninSetUserParams = instance;
        }
        return dataGimbalRoninSetUserParams;
    }

    public DataGimbalRoninSetUserParams setIndexs(String... indexs2) {
        this.indexs = indexs2;
        return this;
    }

    public DataGimbalRoninSetUserParams setValues(Number... values2) {
        this.values = values2;
        return this;
    }

    public DataGimbalRoninSetUserParams setInfo(String index, Number value2) {
        this.paramInfo = DJIGimbalParamInfoManager.read(index);
        this.value = value2;
        return this;
    }

    public DataGimbalRoninSetUserParams setInfo(String index) {
        this.paramInfo = DJIGimbalParamInfoManager.read(index);
        this.value = this.paramInfo.setvalue;
        return this;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        DJIGimbalParamInfoManager.write(this.paramInfo.name, this.value);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte[] valuebytes;
        byte[] valuebytes2;
        int tmp = 0;
        if (this.indexs != null) {
            int length = 0;
            for (int i = 0; i < this.indexs.length; i++) {
                this.paramInfo = DJIGimbalParamInfoManager.read(this.indexs[i]);
                length += this.paramInfo.size + 2;
            }
            this._sendData = new byte[length];
            for (int i2 = 0; i2 < this.indexs.length; i2++) {
                this.paramInfo = DJIGimbalParamInfoManager.read(this.indexs[i2]);
                BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.paramInfo.index), this._sendData, tmp);
                int tmp2 = tmp + 1;
                this._sendData[tmp] = (byte) this.paramInfo.index;
                int tmp3 = tmp2 + 1;
                this._sendData[tmp2] = (byte) this.paramInfo.size;
                this.value = this.values[i2];
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
                System.arraycopy(valuebytes2, 0, this._sendData, tmp3, this.paramInfo.size);
                tmp = tmp3 + this.paramInfo.size;
            }
            this.indexs = null;
        } else if (this.paramInfo != null) {
            this._sendData = new byte[(this.paramInfo.size + 2)];
            this._sendData[0] = (byte) this.paramInfo.index;
            this._sendData[1] = (byte) this.paramInfo.size;
            int tmp4 = 0 + 2;
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
            System.arraycopy(valuebytes, 0, this._sendData, tmp4, this.paramInfo.size);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.RobinSetParams.value();
        start(pack, callBack);
    }
}
