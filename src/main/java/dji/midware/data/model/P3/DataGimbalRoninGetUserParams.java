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

@Keep
@EXClassNullAway
public class DataGimbalRoninGetUserParams extends DataBase implements DJIDataSyncListener {
    private static DataGimbalRoninGetUserParams instance = null;
    private String[] indexs = null;

    public static synchronized DataGimbalRoninGetUserParams getInstance() {
        DataGimbalRoninGetUserParams dataGimbalRoninGetUserParams;
        synchronized (DataGimbalRoninGetUserParams.class) {
            if (instance == null) {
                instance = new DataGimbalRoninGetUserParams();
            }
            dataGimbalRoninGetUserParams = instance;
        }
        return dataGimbalRoninGetUserParams;
    }

    public DataGimbalRoninGetUserParams setInfos(String[] indexs2) {
        this.indexs = indexs2;
        return this;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        int tmp = 0;
        for (int i = 0; i < this.indexs.length; i++) {
            ParamInfo paramInfo = DJIGimbalParamInfoManager.read(this.indexs[i]);
            if (paramInfo != null && paramInfo.index == ((Integer) get(tmp, 1, Integer.class)).intValue()) {
                int tmp2 = tmp + 2;
                DJIGimbalParamInfoManager.write(this.indexs[i], get(tmp2, paramInfo.size, paramInfo.type));
                tmp = tmp2 + paramInfo.size;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[this.indexs.length];
        for (int i = 0; i < this.indexs.length; i++) {
            this._sendData[i] = (byte) DJIGimbalParamInfoManager.read(this.indexs[i]).index;
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
        pack.cmdId = CmdIdGimbal.CmdIdType.RobinGetParams.value();
        start(pack, callBack);
    }
}
