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
import org.bouncycastle.asn1.eac.CertificateBody;

@Keep
@EXClassNullAway
public class DataCameraGetQuickPlayBack extends DataBase implements DJIDataSyncListener {
    private static final byte FLAG_ENABLE = Byte.MIN_VALUE;
    private static DataCameraGetQuickPlayBack instance = null;

    public static synchronized DataCameraGetQuickPlayBack getInstance() {
        DataCameraGetQuickPlayBack dataCameraGetQuickPlayBack;
        synchronized (DataCameraGetQuickPlayBack.class) {
            if (instance == null) {
                instance = new DataCameraGetQuickPlayBack();
            }
            dataCameraGetQuickPlayBack = instance;
        }
        return dataCameraGetQuickPlayBack;
    }

    public byte getTime() {
        boolean enable = true;
        int param = ((Integer) get(0, 1, Integer.class)).intValue();
        if ((param & -128) == 0) {
            enable = false;
        }
        if (enable) {
            return (byte) (param & CertificateBody.profileType);
        }
        return 0;
    }

    public boolean isEnable() {
        if ((((Integer) get(0, 1, Integer.class)).intValue() & -128) != 0) {
            return true;
        }
        return false;
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetQuickPlayBack.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
