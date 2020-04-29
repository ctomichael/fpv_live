package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.util.BytesUtil;
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataCameraGetPushShutterCmd extends DataBase {
    private static DataCameraGetPushShutterCmd instance = null;
    private final String TAG = DataCameraGetPushShutterCmd.class.getSimpleName();

    public static synchronized DataCameraGetPushShutterCmd getInstance() {
        DataCameraGetPushShutterCmd dataCameraGetPushShutterCmd;
        synchronized (DataCameraGetPushShutterCmd.class) {
            if (instance == null) {
                instance = new DataCameraGetPushShutterCmd();
            }
            dataCameraGetPushShutterCmd = instance;
        }
        return dataCameraGetPushShutterCmd;
    }

    public int getShutterType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        if (this.pack.cmdSet == 2 && this.pack.cmdId == 124) {
            Log.d(this.TAG, "isChanged: recData" + BytesUtil.byte2hex(this.pack.buffer));
            start();
            return true;
        } else if (Arrays.equals(this._recData, data)) {
            return false;
        } else {
            return true;
        }
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.ResponseRc.value();
        super.start(pack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[]{0, 0};
    }
}
