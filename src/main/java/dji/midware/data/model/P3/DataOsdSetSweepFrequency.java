package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataOsdSetSweepFrequency extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetSweepFrequency instance = null;
    private int isOpen;
    private int range = 0;
    private int receiverType = 0;

    public static synchronized DataOsdSetSweepFrequency getInstance() {
        DataOsdSetSweepFrequency dataOsdSetSweepFrequency;
        synchronized (DataOsdSetSweepFrequency.class) {
            if (instance == null) {
                instance = new DataOsdSetSweepFrequency();
            }
            dataOsdSetSweepFrequency = instance;
        }
        return dataOsdSetSweepFrequency;
    }

    public DataOsdSetSweepFrequency setSdrType(boolean isOpen2) {
        this.isOpen = isOpen2 ? 1 : 0;
        return this;
    }

    public DataOsdSetSweepFrequency setSdrRange(int range2) {
        this.range = range2;
        return this;
    }

    public DataOsdSetSweepFrequency setReceiverType(int type) {
        this.receiverType = type;
        return this;
    }

    public DataOsdSetSweepFrequency setType(boolean isOpen2) {
        this.isOpen = isOpen2 ? 0 : 1;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.isOpen;
        this._sendData[1] = (byte) this.range;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (this.receiverType == 1) {
            pack.receiverType = DeviceType.OFDM.value();
        } else {
            pack.receiverType = DeviceType.OSD.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetSweepFrequency.value();
        start(pack, callBack);
    }
}
