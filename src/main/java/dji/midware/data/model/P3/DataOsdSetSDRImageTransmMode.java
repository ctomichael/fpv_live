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
public class DataOsdSetSDRImageTransmMode extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetSDRImageTransmMode instance = null;
    private SDRImageTransmMode mImageTransmMode = SDRImageTransmMode.SMOOTH;

    public static synchronized DataOsdSetSDRImageTransmMode getInstance() {
        DataOsdSetSDRImageTransmMode dataOsdSetSDRImageTransmMode;
        synchronized (DataOsdSetSDRImageTransmMode.class) {
            if (instance == null) {
                instance = new DataOsdSetSDRImageTransmMode();
            }
            dataOsdSetSDRImageTransmMode = instance;
        }
        return dataOsdSetSDRImageTransmMode;
    }

    public DataOsdSetSDRImageTransmMode setMode(SDRImageTransmMode _mode) {
        this.mImageTransmMode = _mode;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetSDRImageTransmissionMode.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mImageTransmMode.value();
    }

    @Keep
    public enum SDRImageTransmMode {
        SMOOTH(0),
        HD(1),
        NONE(10);
        
        private int data;

        private SDRImageTransmMode(int i) {
            this.data = i;
        }

        public int value() {
            return this.data;
        }

        public static SDRImageTransmMode find(int v) {
            SDRImageTransmMode[] items = values();
            int len = items.length;
            for (int i = 0; i != len; i++) {
                if (v == items[i].value()) {
                    return items[i];
                }
            }
            return NONE;
        }
    }
}
