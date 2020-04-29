package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdGimbal;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;

@Keep
@EXClassNullAway
public class DataGimbalGetPushTimelapseStatus extends DataBase {
    private static final String TAG = "DataGimbalGetPushTimelapseStatus";
    private static final DataGimbalGetPushTimelapseStatus mInstance = new DataGimbalGetPushTimelapseStatus();

    public static DataGimbalGetPushTimelapseStatus getInstance() {
        return mInstance;
    }

    public int getTimelapseStatus() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 3;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        start();
        return super.isChanged(data);
    }

    /* access modifiers changed from: protected */
    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.GIMBAL.value();
        pack.cmdId = CmdIdGimbal.CmdIdType.GetPushTimelapseStatus.value();
        super.start(pack);
    }
}
