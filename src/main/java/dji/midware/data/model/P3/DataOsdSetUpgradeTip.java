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
public class DataOsdSetUpgradeTip extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetUpgradeTip instance = null;
    private UPGRADETIP mUpgradeTip = UPGRADETIP.START;

    public static synchronized DataOsdSetUpgradeTip getInstance() {
        DataOsdSetUpgradeTip dataOsdSetUpgradeTip;
        synchronized (DataOsdSetUpgradeTip.class) {
            if (instance == null) {
                instance = new DataOsdSetUpgradeTip();
            }
            dataOsdSetUpgradeTip = instance;
        }
        return dataOsdSetUpgradeTip;
    }

    public DataOsdSetUpgradeTip setUpgradeTip(UPGRADETIP tip) {
        this.mUpgradeTip = tip;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mUpgradeTip.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetUpgradeTip.value();
        pack.repeatTimes = 1;
        pack.timeOut = 300;
        start(pack, callBack);
    }

    @Keep
    public enum UPGRADETIP {
        START(1),
        SUCCESS(2),
        FAIL(3);
        
        private int data;

        private UPGRADETIP(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static UPGRADETIP find(int b) {
            UPGRADETIP result = START;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
