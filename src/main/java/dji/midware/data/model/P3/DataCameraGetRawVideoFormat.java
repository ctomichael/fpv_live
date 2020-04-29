package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushRawParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import org.bouncycastle.asn1.eac.CertificateBody;

@Keep
@EXClassNullAway
public class DataCameraGetRawVideoFormat extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetRawVideoFormat mInstance = null;

    public static synchronized DataCameraGetRawVideoFormat getInstance() {
        DataCameraGetRawVideoFormat dataCameraGetRawVideoFormat;
        synchronized (DataCameraGetRawVideoFormat.class) {
            if (mInstance == null) {
                mInstance = new DataCameraGetRawVideoFormat();
            }
            dataCameraGetRawVideoFormat = mInstance;
        }
        return dataCameraGetRawVideoFormat;
    }

    public int getVideoResolution() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getVideoFrameRate() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getFOVType() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getPlaySpeedValue() {
        return ((Integer) get(3, 1, Integer.class)).intValue() & 63;
    }

    public int getPlaySpeedType() {
        return (((Integer) get(3, 1, Integer.class)).intValue() >> 6) & 3;
    }

    public int getSecondVideoResolution() {
        return ((Integer) get(4, 1, Integer.class)).intValue() & CertificateBody.profileType;
    }

    public int getSecondVideoSetting() {
        return ((Integer) get(4, 1, Integer.class)).intValue() & 1;
    }

    public DataCameraGetPushRawParams.RawMode getRawMode() {
        return DataCameraGetPushRawParams.RawMode.find(((Integer) get(4, 1, Integer.class)).intValue());
    }

    public int getRawResolution() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public int getRawFrameRate() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetRawVideoFormat.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
