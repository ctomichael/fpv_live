package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRTK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

public class NRTKGGASender extends DataBase implements DJIDataSyncListener {
    private double latitude;
    private double longitude;

    public static NRTKGGASender getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static NRTKGGASender INSTANCE = new NRTKGGASender();

        private Holder() {
        }
    }

    public NRTKGGASender setGPS(double latitude2, double longitude2) {
        this.latitude = latitude2;
        this.longitude = longitude2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[16];
        System.arraycopy(BytesUtil.getBytes(this.latitude), 0, this._sendData, 0, 8);
        System.arraycopy(BytesUtil.getBytes(this.longitude), 0, this._sendData, 8, 8);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RTK.value();
        pack.cmdId = CmdIdRTK.CmdIdType.SendNRTKGGA.value();
        start(pack, callBack);
    }
}
