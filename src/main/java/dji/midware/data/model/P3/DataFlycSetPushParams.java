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
public class DataFlycSetPushParams extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetPushParams instance = null;
    private int frequency;
    private int group;
    private ParamInfo[] infolist;
    private int size;
    private int startIndex;

    public static synchronized DataFlycSetPushParams getInstance() {
        DataFlycSetPushParams dataFlycSetPushParams;
        synchronized (DataFlycSetPushParams.class) {
            if (instance == null) {
                instance = new DataFlycSetPushParams();
            }
            dataFlycSetPushParams = instance;
        }
        return dataFlycSetPushParams;
    }

    public DataFlycSetPushParams setGroup(int group2) {
        this.group = group2;
        return this;
    }

    public DataFlycSetPushParams setFrequency(int frequency2) {
        this.frequency = frequency2;
        return this;
    }

    public DataFlycSetPushParams setStartIndex(int startIndex2) {
        this.startIndex = startIndex2;
        return this;
    }

    public DataFlycSetPushParams setList(String[] list) {
        if (list == null) {
            this.size = 0;
        } else {
            this.size = list.length;
            this.infolist = new ParamInfo[this.size];
            for (int i = 0; i < this.size; i++) {
                this.infolist[i] = DJIFlycParamInfoManager.read(list[i]);
            }
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[((this.size * 4) + 4)];
        this._sendData[0] = (byte) this.group;
        this._sendData[1] = (byte) this.frequency;
        this._sendData[2] = (byte) this.startIndex;
        this._sendData[3] = (byte) this.size;
        for (int i = 0; i < this.size; i++) {
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.infolist[i].hash), this._sendData, (i * 4) + 4);
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
        pack.cmdId = CmdIdFlyc.CmdIdType.SetPushParams.value();
        start(pack, callBack);
    }
}
