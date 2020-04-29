package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataEyeFixedWingControl extends DataBase implements DJIDataSyncListener {
    private static DataEyeFixedWingControl instance = null;
    private FixedWingCtrlType mRequestType = FixedWingCtrlType.OTHER;

    public static synchronized DataEyeFixedWingControl getInstance() {
        DataEyeFixedWingControl dataEyeFixedWingControl;
        synchronized (DataEyeFixedWingControl.class) {
            if (instance == null) {
                instance = new DataEyeFixedWingControl();
            }
            dataEyeFixedWingControl = instance;
        }
        return dataEyeFixedWingControl;
    }

    public DataEyeFixedWingControl setRequestType(FixedWingCtrlType type) {
        this.mRequestType = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mRequestType.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.FixedWingCtrl.value();
        start(pack, callBack);
    }

    @Keep
    public enum FixedWingCtrlType {
        READY(0),
        ENTER(1),
        EXIT(2),
        OTHER(100);
        
        private final int data;

        private FixedWingCtrlType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FixedWingCtrlType find(int b) {
            FixedWingCtrlType result = READY;
            FixedWingCtrlType[] values = values();
            for (FixedWingCtrlType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
