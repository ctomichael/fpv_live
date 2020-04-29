package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSetVideoEncode;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraGetVideoEncode extends DataBase implements DJIDataSyncListener {
    public DataCameraSetVideoEncode.VideoEncodeType getPrimaryEncodeType() {
        return DataCameraSetVideoEncode.VideoEncodeType.find(((Integer) get(0, 1, Integer.class)).intValue() & 15);
    }

    public DataCameraSetVideoEncode.VideoEncodeType getSecondaryEncodeType() {
        return DataCameraSetVideoEncode.VideoEncodeType.find((((Integer) get(0, 1, Integer.class)).intValue() & 240) >> 4);
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetVideoEncode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
