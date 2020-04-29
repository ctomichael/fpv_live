package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import org.bouncycastle.asn1.eac.CertificateBody;

@Keep
@EXClassNullAway
public class DataRcGetChannelParams extends DataBase implements DJIDataSyncListener {
    private static DataRcGetChannelParams instance = null;

    @Keep
    public static class DJIChannel {
        public boolean direction;
        public int name;
        public int value;
    }

    public static synchronized DataRcGetChannelParams getInstance() {
        DataRcGetChannelParams dataRcGetChannelParams;
        synchronized (DataRcGetChannelParams.class) {
            if (instance == null) {
                instance = new DataRcGetChannelParams();
            }
            dataRcGetChannelParams = instance;
        }
        return dataRcGetChannelParams;
    }

    public SparseArray<DJIChannel> getList() {
        SparseArray<DJIChannel> result = new SparseArray<>();
        if (this._recData != null) {
            int num = this._recData.length / 3;
            for (int i = 0; i < num; i++) {
                DJIChannel channel = new DJIChannel();
                int baseValue = ((Integer) get(i * 3, 3, Integer.class)).intValue();
                channel.name = baseValue & CertificateBody.profileType;
                channel.value = baseValue >> 8;
                channel.direction = (baseValue & 128) == 1;
                result.put(i, channel);
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetChannelParams.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
