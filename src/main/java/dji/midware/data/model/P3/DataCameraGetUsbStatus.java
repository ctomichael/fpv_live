package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraGetUsbStatus extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetUsbStatus instance = null;

    public static synchronized DataCameraGetUsbStatus getInstance() {
        DataCameraGetUsbStatus dataCameraGetUsbStatus;
        synchronized (DataCameraGetUsbStatus.class) {
            if (instance == null) {
                instance = new DataCameraGetUsbStatus();
            }
            dataCameraGetUsbStatus = instance;
        }
        return dataCameraGetUsbStatus;
    }

    public USBSTATUS getUSBStatus() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1 ? USBSTATUS.CONNECT : USBSTATUS.DISCONNECT;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetUSB.value();
        start(pack, callBack);
    }

    @Keep
    public enum USBSTATUS {
        DISCONNECT(0),
        CONNECT(1);
        
        private int value;

        private USBSTATUS(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }
    }
}
