package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
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
public class DataGimbalSetUserParams extends DataBase implements DJIDataSyncListener {
    private static DataGimbalSetUserParams instance = null;
    private int mRepeatTimes = -1;
    private int mTimeOut = -1;
    private int modelId = 0;
    private ParamInfo paramInfo;
    private String strValue;
    private Number value;

    public static synchronized DataGimbalSetUserParams getInstance() {
        DataGimbalSetUserParams dataGimbalSetUserParams;
        synchronized (DataGimbalSetUserParams.class) {
            if (instance == null) {
                instance = new DataGimbalSetUserParams();
            }
            dataGimbalSetUserParams = instance;
        }
        return dataGimbalSetUserParams;
    }

    public DataGimbalSetUserParams setTimeOut(int millisecond) {
        this.mTimeOut = millisecond;
        return this;
    }

    public void setRepeatTimes(int n) {
        this.mRepeatTimes = n;
    }

    public DataGimbalSetUserParams setInfo(String index, Number value2) {
        this.paramInfo = DJIGimbalParamInfoManager.read(index);
        this.value = value2;
        return this;
    }

    public DataGimbalSetUserParams setInfo(String index) {
        this.paramInfo = DJIGimbalParamInfoManager.read(index);
        this.value = this.paramInfo.setvalue;
        return this;
    }

    public DataGimbalSetUserParams setStrValueInfo(String index, String strValue2) {
        this.paramInfo = DJIGimbalParamInfoManager.read(index);
        this.strValue = strValue2;
        return this;
    }

    public int getModelId() {
        return this.modelId;
    }

    public void setModelId(int modelId2) {
        this.modelId = modelId2;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        DJIGimbalParamInfoManager.write(this.paramInfo.name, this.value);
        if (this.strValue != null && this.strValue.length() > 0) {
            DJIGimbalParamInfoManager.writeStrValue(this.paramInfo.name, this.strValue);
            this.strValue = null;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte[] valuebytes;
        if (this.paramInfo != null) {
            this._sendData = new byte[(this.paramInfo.size + 2)];
            if (DJIGimbalParamInfoManager.isConfigNameKey(this.paramInfo.name)) {
                this._sendData[0] = 45;
            } else {
                this._sendData[0] = (byte) this.paramInfo.index;
            }
            this._sendData[1] = (byte) this.paramInfo.size;
            int tmp = 0 + 2;
            byte[] bArr = new byte[0];
            if (this.value != null) {
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
            }
            if (this.strValue != null && this.strValue.length() > 0) {
                byte[] valuebytes2 = BytesUtil.getBytesUTF8(this.strValue);
                System.arraycopy(valuebytes2, 0, this._sendData, tmp, valuebytes2.length);
            }
            DJILogHelper.getInstance().LOGE("DJIPackManager", "send =" + BytesUtil.byte2hex(this._sendData), false, true);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.receiverId = this.modelId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.SetUserParams.value();
        if (this.mTimeOut > 0) {
            pack.timeOut = this.mTimeOut;
        }
        if (this.mRepeatTimes > 0) {
            pack.repeatTimes = this.mRepeatTimes;
        }
        start(pack, callBack);
    }
}
