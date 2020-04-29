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
public class DataCameraSetPushChart extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetPushChart instance = null;
    private boolean mEnable = false;

    public static synchronized DataCameraSetPushChart getInstance() {
        DataCameraSetPushChart dataCameraSetPushChart;
        synchronized (DataCameraSetPushChart.class) {
            if (instance == null) {
                instance = new DataCameraSetPushChart();
            }
            dataCameraSetPushChart = instance;
        }
        return dataCameraSetPushChart;
    }

    public DataCameraSetPushChart setEnable(boolean enable) {
        this.mEnable = enable;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.mEnable) {
            this._sendData[0] = 1;
        } else {
            this._sendData[0] = 0;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetPushChart.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}
