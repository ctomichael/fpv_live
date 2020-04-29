package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdDm368;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataDm368SetGParams extends DataBase implements DJIDataSyncListener {
    private CmdId cmdId;
    private int value;

    public DataDm368SetGParams set(CmdId cmdId2, int value2) {
        this.cmdId = cmdId2;
        this.value = value2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.cmdId.value();
        this._sendData[1] = 1;
        this._sendData[2] = (byte) this.value;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368_G.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.SetGParams.value();
        start(pack, callBack);
    }

    @Keep
    public enum CmdId {
        ShowOsd(1),
        Set720PFps(3),
        SetOsdLeft(4),
        SetOsdRight(5),
        SetOsdTop(6),
        SetOsdBottom(7),
        ShowUnit(9),
        ShowDouble(10),
        SetOutputDevice(12),
        SetHDMIFormat(13),
        SetSDIFormat(15),
        SetOutputMode(16),
        SetOutputLoc(17),
        SetOutputEnable(18),
        AndroidPhoneCharge(23),
        DisableUpgradeSound(26),
        OTHER(100);
        
        private int data;

        private CmdId(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdId find(int b) {
            CmdId result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum PhoneChargeConfig {
        SMALL_CURRENT(0),
        HIGH_CURRENT(1),
        OTHER(100);
        
        private int data;

        private PhoneChargeConfig(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PhoneChargeConfig find(int b) {
            PhoneChargeConfig result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
