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
public class DataOsdSetDebugVideo extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetDebugVideo instance = null;
    private boolean isEnabled;
    private int type;

    public DataOsdSetDebugVideo setType(int type2) {
        this.type = type2;
        return this;
    }

    public DataOsdSetDebugVideo setEnabled(boolean isEnabled2) {
        this.isEnabled = isEnabled2;
        return this;
    }

    public boolean isEnabled() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 1 || ((Integer) get(1, 1, Integer.class)).intValue() == 1;
    }

    public static synchronized DataOsdSetDebugVideo getInstance() {
        DataOsdSetDebugVideo dataOsdSetDebugVideo;
        synchronized (DataOsdSetDebugVideo.class) {
            if (instance == null) {
                instance = new DataOsdSetDebugVideo();
            }
            dataOsdSetDebugVideo = instance;
        }
        return dataOsdSetDebugVideo;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetDebugVideo.value();
        pack.repeatTimes = 3;
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.type;
        byte[] bArr = this._sendData;
        if (!this.isEnabled) {
            i = 1;
        }
        bArr[1] = (byte) i;
    }
}
