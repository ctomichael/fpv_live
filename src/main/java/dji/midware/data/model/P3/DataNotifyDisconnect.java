package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

@Keep
@EXClassNullAway
public class DataNotifyDisconnect extends DataBase implements DJIDataAsync2Listener {
    private static DataNotifyDisconnect instance = null;

    public static synchronized DataNotifyDisconnect getInstance() {
        DataNotifyDisconnect dataNotifyDisconnect;
        synchronized (DataNotifyDisconnect.class) {
            if (instance == null) {
                instance = new DataNotifyDisconnect();
            }
            dataNotifyDisconnect = instance;
        }
        return dataNotifyDisconnect;
    }

    public RebootReason getRebootReason() {
        return RebootReason.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getTimeout() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) Ccode.SUCCEED.value();
    }

    public void start() {
        SendPack pack = new SendPack();
        if (this.pack != null) {
            pack.senderType = DeviceType.APP.value();
            pack.receiverType = this.pack.senderType;
            pack.receiverId = this.pack.senderId;
            pack.cmdType = DataConfig.CMDTYPE.ACK.value();
            pack.isNeedAck = DataConfig.NEEDACK.NO.value();
            pack.encryptType = DataConfig.EncryptType.NO.value();
            pack.cmdSet = CmdSet.COMMON.value();
            pack.cmdId = this.pack.cmdId;
            start(pack);
        }
    }

    @Keep
    public enum RebootReason {
        For1860(1),
        ForAircraft(2),
        OTHER(7);
        
        private int data;

        private RebootReason(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RebootReason find(int b) {
            RebootReason result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
