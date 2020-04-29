package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataOsdSwitchSdrOrLteLink extends DataBase implements DJIDataSyncListener {
    private boolean isAuto;
    private boolean isSetRTK;
    private LinkMode mLinkMode;
    private DataRcSetMaster.MODE mRcModel;

    public DataOsdSwitchSdrOrLteLink setAuto(boolean auto) {
        this.isAuto = auto;
        return this;
    }

    public DataOsdSwitchSdrOrLteLink setRcMode(DataRcSetMaster.MODE model) {
        this.mRcModel = model;
        return this;
    }

    public DataOsdSwitchSdrOrLteLink setLinkMode(LinkMode mode) {
        this.mLinkMode = mode;
        return this;
    }

    public DataOsdSwitchSdrOrLteLink isRTK2Drone(boolean is2RTK) {
        this.isSetRTK = is2RTK;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        this._sendData = new byte[3];
        this._sendData[0] = (byte) (this.isSetRTK ? 2 : this.mRcModel == DataRcSetMaster.MODE.Master ? 0 : 1);
        byte[] bArr = this._sendData;
        if (!this.isAuto) {
            i = 1;
        }
        bArr[1] = (byte) i;
        if (!this.isAuto) {
            this._sendData[2] = this.mLinkMode.value;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SwitchSetSdrOrLte.value();
        start(pack, callBack);
    }

    public enum LinkMode {
        NONE(0),
        SDR(1),
        LTE(2),
        UNKNOWN(255);
        
        byte value;

        private LinkMode(int value2) {
            this.value = (byte) value2;
        }

        public static LinkMode find(int value2) {
            LinkMode target = UNKNOWN;
            LinkMode[] values = values();
            for (LinkMode mode : values) {
                if (value2 == mode.value) {
                    return mode;
                }
            }
            return target;
        }
    }
}
