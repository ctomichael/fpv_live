package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.CmdIdRc;
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
public class DataRcSetCalibration extends DataBase implements DJIDataSyncListener {
    private static DataRcSetCalibration instance = null;
    private MODE mode;

    public static synchronized DataRcSetCalibration getInstance() {
        DataRcSetCalibration dataRcSetCalibration;
        synchronized (DataRcSetCalibration.class) {
            if (instance == null) {
                instance = new DataRcSetCalibration();
            }
            dataRcSetCalibration = instance;
        }
        return dataRcSetCalibration;
    }

    public DataRcSetCalibration setMode(MODE mode2) {
        this.mode = mode2;
        return this;
    }

    public MODE getMode() {
        if (this._recData != null) {
            return MODE.find(this._recData[0]);
        }
        return MODE.OTHER;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mode.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.Mammoth) {
            pack.receiverType = DeviceType.WIFI_G.value();
        } else if (type == ProductType.WM240 || type == ProductType.WM245 || DJIComponentManager.getInstance().isRcRM500()) {
            pack.receiverType = DeviceType.RC.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetCalibration.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum MODE {
        Normal(0),
        Middle(1),
        Limits(2),
        Quit(3),
        TimeOut(4),
        OTHER(6),
        Disconnect(255);
        
        private int data;

        private MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MODE find(int b) {
            MODE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
