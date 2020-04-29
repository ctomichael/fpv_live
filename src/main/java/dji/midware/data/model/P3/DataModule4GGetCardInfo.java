package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdModule4G;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataModule4GGetCardInfo extends DataBase implements DJIDataSyncListener {
    public static final int SEARCH_4G_TYPE = 1;
    public static final int SEARCH_MCC = 3;
    public static final int SEARCH_MNC = 4;
    public static final int SEARCH_VERSION = 2;
    public static final int SEARCH_WIDTH_BAND = 5;
    private DeviceType mReceiver = DeviceType.OSD;
    private int mRequestCmd;

    @interface CardInfoCmd {
    }

    public DataModule4GGetCardInfo setRequestCmd(@CardInfoCmd int cmd) {
        this.mRequestCmd = cmd;
        return this;
    }

    public DataModule4GGetCardInfo setReceiverType(DeviceType device) {
        this.mReceiver = device;
        return this;
    }

    public int getRequestId() {
        return this.mRequestCmd;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mRequestCmd;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiver == null ? DeviceType.OSD.value() : this.mReceiver.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.Module4G.value();
        pack.cmdId = CmdIdModule4G.CmdIdType.Get4GCardInfo.value();
        start(pack, callBack);
    }

    public long getVersion() {
        if (this.mRequestCmd != 2 || !isGetted()) {
            return -1;
        }
        return ((Long) get(0, 8, Long.class)).longValue();
    }

    public int getMCC() {
        if (this.mRequestCmd != 3 || !isGetted()) {
            return -1;
        }
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public int getMNC() {
        if (this.mRequestCmd != 4 || !isGetted()) {
            return -1;
        }
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public DataModule4GCardType get4GCardType() {
        if (this.mRequestCmd != 1 || !isGetted()) {
            return DataModule4GCardType.UNKNOWN;
        }
        return DataModule4GCardType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public long getRxWidthBand() {
        if (this.mRequestCmd != 5 || !isGetted()) {
            return -1;
        }
        return ((Long) get(4, 4, Long.class)).longValue();
    }

    public long getTxWidthBand() {
        if (this.mRequestCmd != 5 || !isGetted()) {
            return -1;
        }
        return ((Long) get(0, 4, Long.class)).longValue();
    }
}
