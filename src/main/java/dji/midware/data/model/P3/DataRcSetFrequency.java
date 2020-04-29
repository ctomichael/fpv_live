package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcSetFrequency extends DataBase implements DJIDataSyncListener {
    private static DataRcSetFrequency instance = null;
    private FreqMode mode;

    public static synchronized DataRcSetFrequency getInstance() {
        DataRcSetFrequency dataRcSetFrequency;
        synchronized (DataRcSetFrequency.class) {
            if (instance == null) {
                instance = new DataRcSetFrequency();
            }
            dataRcSetFrequency = instance;
        }
        return dataRcSetFrequency;
    }

    public DataRcSetFrequency setMode(FreqMode mode2) {
        this.mode = mode2;
        return this;
    }

    public FreqCcode getCcode() {
        return FreqCcode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mode.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetFrequency.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum FreqMode {
        Current(0),
        Enter(1),
        Cancel(2),
        MasterEnter(5),
        SlaveEnter(6),
        SubEnter(7),
        OTHER(10);
        
        private int data;

        private FreqMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FreqMode find(int b) {
            FreqMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FreqCcode {
        Idle(0),
        Progress(1),
        Finish(2),
        OTHER(6);
        
        private int data;

        private FreqCcode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FreqCcode find(int b) {
            FreqCcode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
