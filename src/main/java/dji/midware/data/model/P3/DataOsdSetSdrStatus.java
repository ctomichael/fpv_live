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
public class DataOsdSetSdrStatus extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetSdrStatus instance = null;
    private int deviceType = 0;
    private int isOpen = 0;

    public static synchronized DataOsdSetSdrStatus getInstance() {
        DataOsdSetSdrStatus dataOsdSetSdrStatus;
        synchronized (DataOsdSetSdrStatus.class) {
            if (instance == null) {
                instance = new DataOsdSetSdrStatus();
            }
            dataOsdSetSdrStatus = instance;
        }
        return dataOsdSetSdrStatus;
    }

    public DataOsdSetSdrStatus setIsOpen(boolean isOpen2) {
        this.isOpen = isOpen2 ? 1 : 0;
        return this;
    }

    public DataOsdSetSdrStatus setDeviceType(int deviceType2) {
        this.deviceType = deviceType2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.isOpen;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (this.deviceType == 0) {
            pack.receiverType = DeviceType.OFDM.value();
        } else {
            pack.receiverType = DeviceType.OSD.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetSdrStatus.value();
        start(pack, callBack);
    }
}
