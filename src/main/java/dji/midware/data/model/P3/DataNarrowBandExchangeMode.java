package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdNarrowBand;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataNarrowBandExchangeMode extends DataBase implements DJIDataSyncListener {
    private static DataNarrowBandExchangeMode instance = null;
    private int newSlaveCharacter;
    private int originalSlaveCharacter;

    public static synchronized DataNarrowBandExchangeMode getInstance() {
        DataNarrowBandExchangeMode dataNarrowBandExchangeMode;
        synchronized (DataNarrowBandExchangeMode.class) {
            if (instance == null) {
                instance = new DataNarrowBandExchangeMode();
            }
            dataNarrowBandExchangeMode = instance;
        }
        return dataNarrowBandExchangeMode;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public boolean isExchanging() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 1;
    }

    public int getFromSlaveMode() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getDesireSlaveMode() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getBeforeSlaveMode() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = 0;
        this._sendData[1] = (byte) this.originalSlaveCharacter;
        this._sendData[2] = (byte) this.newSlaveCharacter;
        this._sendData[3] = 0;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.NarrowBand.value();
        pack.cmdId = CmdIdNarrowBand.CmdIdType.ExchangeMode.value();
        pack.data = getSendData();
        pack.timeOut = 5000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    public DataNarrowBandExchangeMode setTransformation(int originalSlaveCharacter2, int newSlaveCharacter2) {
        this.originalSlaveCharacter = originalSlaveCharacter2;
        this.newSlaveCharacter = newSlaveCharacter2;
        return this;
    }
}
