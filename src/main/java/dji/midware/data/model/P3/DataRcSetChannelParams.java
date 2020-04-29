package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcGetChannelParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcSetChannelParams extends DataBase implements DJIDataSyncListener {
    private static DataRcSetChannelParams instance = null;
    private SparseArray<DataRcGetChannelParams.DJIChannel> list;

    public static synchronized DataRcSetChannelParams getInstance() {
        DataRcSetChannelParams dataRcSetChannelParams;
        synchronized (DataRcSetChannelParams.class) {
            if (instance == null) {
                instance = new DataRcSetChannelParams();
            }
            dataRcSetChannelParams = instance;
        }
        return dataRcSetChannelParams;
    }

    public DataRcSetChannelParams setList(SparseArray<DataRcGetChannelParams.DJIChannel> list2) {
        this.list = list2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int size = this.list.size();
        this._sendData = new byte[size];
        for (int i = 0; i < size; i++) {
            DataRcGetChannelParams.DJIChannel channel = this.list.get(this.list.keyAt(i));
            this._sendData[i] = (byte) ((channel.direction ? 1 : 0) | channel.name);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetChannelParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
