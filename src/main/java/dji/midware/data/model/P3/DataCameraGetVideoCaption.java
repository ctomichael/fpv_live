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
public class DataCameraGetVideoCaption extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetVideoCaption mInstance = null;

    public static synchronized DataCameraGetVideoCaption getInstance() {
        DataCameraGetVideoCaption dataCameraGetVideoCaption;
        synchronized (DataCameraGetVideoCaption.class) {
            if (mInstance == null) {
                mInstance = new DataCameraGetVideoCaption();
            }
            dataCameraGetVideoCaption = mInstance;
        }
        return dataCameraGetVideoCaption;
    }

    public boolean isGenerateVideoCaptionEnable() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 128) != 0;
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetVideoCaption.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
