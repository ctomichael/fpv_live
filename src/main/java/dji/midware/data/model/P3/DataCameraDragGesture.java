package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraDragGesture extends DataBase implements DJIDataAsync2Listener {
    private static DataCameraDragGesture instance = null;
    private int x = 0;
    private int y = 0;

    public static synchronized DataCameraDragGesture getInstance() {
        DataCameraDragGesture dataCameraDragGesture;
        synchronized (DataCameraDragGesture.class) {
            if (instance == null) {
                instance = new DataCameraDragGesture();
            }
            dataCameraDragGesture = instance;
        }
        return dataCameraDragGesture;
    }

    public DataCameraDragGesture setX(int x2) {
        this.x = x2;
        return this;
    }

    public DataCameraDragGesture setY(int y2) {
        this.y = y2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[8];
        BytesUtil.arraycopy(BytesUtil.getBytes(this.x), this._sendData);
        System.arraycopy(BytesUtil.getBytes(this.y), 0, this._sendData, 4, 4);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.DragGesture.value();
        pack.data = getSendData();
        start(pack);
    }
}
