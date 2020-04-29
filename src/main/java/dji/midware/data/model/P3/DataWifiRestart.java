package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataWifiRestart extends DataBase implements DJIDataSyncListener {
    private static DataWifiRestart mInstance = null;
    private boolean isFromLongan = false;

    public DataWifiRestart setFromLongan(boolean isFromLongan2) {
        this.isFromLongan = isFromLongan2;
        return this;
    }

    public static synchronized DataWifiRestart getInstance() {
        DataWifiRestart dataWifiRestart;
        synchronized (DataWifiRestart.class) {
            if (mInstance == null) {
                mInstance = new DataWifiRestart();
            }
            dataWifiRestart = mInstance;
        }
        return dataWifiRestart;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (!this.isFromLongan) {
            pack.receiverType = DeviceType.WIFI_G.value();
        } else {
            pack.receiverType = DeviceType.WIFI.value();
        }
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.KumquatX || type == ProductType.KumquatS) {
            pack.receiverType = DeviceType.WIFI.value();
        } else if (type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM160) {
            if (DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null) || DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected()) {
                pack.receiverType = DeviceType.WIFI_G.value();
            } else {
                pack.receiverType = DeviceType.WIFI.value();
            }
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.RestartWifi.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
