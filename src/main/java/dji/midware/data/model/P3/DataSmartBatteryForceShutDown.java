package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdSmartBattery;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

@Keep
@EXClassNullAway
public class DataSmartBatteryForceShutDown extends DataBase implements DJIDataSyncListener {
    private static DataSmartBatteryForceShutDown mInstance = null;
    private int index = 0;

    public static DataSmartBatteryForceShutDown getInstance() {
        if (mInstance == null) {
            mInstance = new DataSmartBatteryForceShutDown();
        }
        return mInstance;
    }

    public DataSmartBatteryForceShutDown setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SMARTBATTERY.value();
        pack.cmdId = CmdIdSmartBattery.CmdIdType.ForceShutDown.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
        this._sendData[0] = (byte) this.index;
        this._sendData[1] = 70;
        this._sendData[2] = TarHeader.LF_CONTIG;
        this._sendData[3] = 40;
        this._sendData[4] = 25;
        this._sendData[5] = -17;
        this._sendData[6] = -66;
        this._sendData[7] = -83;
        this._sendData[8] = MessagePack.Code.MAP16;
    }
}
