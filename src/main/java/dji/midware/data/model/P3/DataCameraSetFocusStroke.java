package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraSetFocusStroke extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFocusStroke instance = null;
    private int mRepeatTime = 2;
    private int mStroke = 0;

    public static synchronized DataCameraSetFocusStroke getInstance() {
        DataCameraSetFocusStroke dataCameraSetFocusStroke;
        synchronized (DataCameraSetFocusStroke.class) {
            if (instance == null) {
                instance = new DataCameraSetFocusStroke();
            }
            dataCameraSetFocusStroke = instance;
        }
        return dataCameraSetFocusStroke;
    }

    public DataCameraSetFocusStroke setStroke(int stroke) {
        this.mStroke = stroke;
        return this;
    }

    public DataCameraSetFocusStroke setRepeatTime(int repeatTime) {
        this.mRepeatTime = repeatTime;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        System.arraycopy(BytesUtil.getUnsignedBytes(this.mStroke), 0, this._sendData, 0, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetFocusStroke.value();
        pack.timeOut = LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE;
        pack.repeatTimes = this.mRepeatTime;
        start(pack, callBack);
    }
}
