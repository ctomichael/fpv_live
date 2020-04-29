package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycStartHotPointMissionWithInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycHotPointMissionDownload extends DataBase implements DJIDataSyncListener {
    private static DataFlycHotPointMissionDownload instance = null;

    public static synchronized DataFlycHotPointMissionDownload getInstance() {
        DataFlycHotPointMissionDownload dataFlycHotPointMissionDownload;
        synchronized (DataFlycHotPointMissionDownload.class) {
            if (instance == null) {
                instance = new DataFlycHotPointMissionDownload();
            }
            dataFlycHotPointMissionDownload = instance;
        }
        return dataFlycHotPointMissionDownload;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getVersion() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public double getHotPointLatitude() {
        return ((Double) get(2, 8, Double.class)).doubleValue();
    }

    public double getHotPointLongitude() {
        return ((Double) get(10, 8, Double.class)).doubleValue();
    }

    public double getHotPointAttitude() {
        return ((Double) get(18, 8, Double.class)).doubleValue();
    }

    public double getHotPointRadius() {
        return ((Double) get(26, 8, Double.class)).doubleValue();
    }

    public float getHotPointAngleSpeed() {
        return ((Float) get(34, 4, Float.class)).floatValue();
    }

    public DataFlycStartHotPointMissionWithInfo.ROTATION_DIR getHotPointClockWise() {
        return DataFlycStartHotPointMissionWithInfo.ROTATION_DIR.find(((Integer) get(38, 1, Integer.class)).intValue());
    }

    public DataFlycStartHotPointMissionWithInfo.TO_START_POINT_MODE getHotPointToStartPointMode() {
        return DataFlycStartHotPointMissionWithInfo.TO_START_POINT_MODE.find(((Integer) get(39, 1, Integer.class)).intValue());
    }

    public DataFlycStartHotPointMissionWithInfo.CAMERA_DIR getHotPointCameraDir() {
        return DataFlycStartHotPointMissionWithInfo.CAMERA_DIR.find(((Integer) get(40, 1, Integer.class)).intValue());
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.HotPointMissionDownload.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = null;
    }
}
