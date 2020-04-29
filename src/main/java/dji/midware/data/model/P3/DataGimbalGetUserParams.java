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
import dji.midware.data.params.P3.GimbalParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataGimbalGetUserParams extends DataBase implements DJIDataSyncListener {
    public static final int CONFIG_NAME_SUB_CMDID = 45;
    private static DataGimbalGetUserParams instance = null;
    private String[] indexs = null;
    private String[] mGimbalNameIndexs = new String[0];
    private int mRepeatTimes = -1;
    private int mTimeOut = -1;
    private int modelId = 0;

    public DataGimbalGetUserParams setTimeOut(int millisecond) {
        this.mTimeOut = millisecond;
        return this;
    }

    public void setRepeatTimes(int n) {
        this.mRepeatTimes = n;
    }

    public static synchronized DataGimbalGetUserParams getInstance() {
        DataGimbalGetUserParams dataGimbalGetUserParams;
        synchronized (DataGimbalGetUserParams.class) {
            if (instance == null) {
                instance = new DataGimbalGetUserParams();
            }
            dataGimbalGetUserParams = instance;
        }
        return dataGimbalGetUserParams;
    }

    public int getModelId() {
        return this.modelId;
    }

    public void setModelId(int modelId2) {
        this.modelId = modelId2;
    }

    public DataGimbalGetUserParams setInfos(String[] indexs2) {
        this.indexs = indexs2;
        return this;
    }

    public DataGimbalGetUserParams setGimbalNameIndexs(String[] gimbalNameIndexs) {
        this.mGimbalNameIndexs = gimbalNameIndexs;
        return this;
    }

    public void setRecData(byte[] data) {
        if (this.recvPack != null && this.recvPack.buffer != null) {
            super.setRecData(data);
            int tmp = 0;
            int i = 0;
            while (i < this.indexs.length) {
                try {
                    GimbalParamInfo paramInfo = (GimbalParamInfo) DJIGimbalParamInfoManager.read(this.indexs[i]);
                    DJILogHelper.getInstance().LOGD("", i + " index=" + paramInfo.index + " index=" + get(tmp, 1, Integer.class), false, true);
                    if (paramInfo != null && paramInfo.index == ((Integer) get(tmp, 1, Integer.class)).intValue()) {
                        int tmp2 = tmp + 2;
                        DJIGimbalParamInfoManager.write(this.indexs[i], get(tmp2, paramInfo.size, paramInfo.type));
                        tmp = tmp2 + paramInfo.size;
                    }
                    i++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return;
                }
            }
            boolean isReceiveGimbalName = false;
            for (int i2 = 0; i2 != this.mGimbalNameIndexs.length; i2++) {
                GimbalParamInfo paramInfo2 = (GimbalParamInfo) DJIGimbalParamInfoManager.read(this.mGimbalNameIndexs[i2]);
                if (paramInfo2.index == ((Integer) get(tmp, 1, Integer.class)).intValue()) {
                    isReceiveGimbalName = true;
                    int tmp3 = tmp + 3;
                    DJIGimbalParamInfoManager.writeStrValue(this.mGimbalNameIndexs[i2], getUTF8(tmp3, paramInfo2.size));
                    tmp = tmp3 + paramInfo2.size;
                }
            }
            if (isReceiveGimbalName) {
                this.mGimbalNameIndexs = new String[0];
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[(this.indexs.length + (this.mGimbalNameIndexs.length * 2))];
        for (int i = 0; i < this.indexs.length; i++) {
            this._sendData[i] = (byte) DJIGimbalParamInfoManager.read(this.indexs[i]).index;
        }
        for (int i2 = 0; i2 != this.mGimbalNameIndexs.length; i2++) {
            this._sendData[this.indexs.length + (i2 * 2)] = (byte) DJIGimbalParamInfoManager.read(this.mGimbalNameIndexs[i2]).index;
            this._sendData[this.indexs.length + (i2 * 2) + 1] = 45;
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
        pack.cmdId = CmdIdGimbal.CmdIdType.GetUserParams.value();
        if (this.mTimeOut > 0) {
            pack.timeOut = this.mTimeOut;
        }
        if (this.mRepeatTimes > 0) {
            pack.repeatTimes = this.mRepeatTimes;
        }
        start(pack, callBack);
    }
}
