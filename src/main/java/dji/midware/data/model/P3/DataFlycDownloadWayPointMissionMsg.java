package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycUploadWayPointMissionMsg;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycDownloadWayPointMissionMsg extends DataBase implements DJIDataSyncListener {
    private static int VALID_REQUIRE_ID_LENGTH = 42;
    private static DataFlycDownloadWayPointMissionMsg instance = null;

    public static synchronized DataFlycDownloadWayPointMissionMsg getInstance() {
        DataFlycDownloadWayPointMissionMsg dataFlycDownloadWayPointMissionMsg;
        synchronized (DataFlycDownloadWayPointMissionMsg.class) {
            if (instance == null) {
                instance = new DataFlycDownloadWayPointMissionMsg();
            }
            dataFlycDownloadWayPointMissionMsg = instance;
        }
        return dataFlycDownloadWayPointMissionMsg;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getWayPointCount() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public float getCmdSpeed() {
        return ((Float) get(2, 4, Float.class)).floatValue();
    }

    public float getIdleSpeed() {
        return ((Float) get(6, 4, Float.class)).floatValue();
    }

    public DataFlycUploadWayPointMissionMsg.FINISH_ACTION getFinishAction() {
        return DataFlycUploadWayPointMissionMsg.FINISH_ACTION.find(((Integer) get(10, 1, Integer.class)).intValue());
    }

    public int getRepeatNum() {
        return ((Integer) get(11, 1, Integer.class)).intValue();
    }

    public DataFlycUploadWayPointMissionMsg.YAW_MODE getYawMode() {
        return DataFlycUploadWayPointMissionMsg.YAW_MODE.find(((Integer) get(12, 1, Integer.class)).intValue());
    }

    public DataFlycUploadWayPointMissionMsg.TRACE_MODE getTraceMode() {
        return DataFlycUploadWayPointMissionMsg.TRACE_MODE.find(((Integer) get(13, 1, Integer.class)).intValue());
    }

    public DataFlycUploadWayPointMissionMsg.ACTION_ON_RC_LOST getActionOnRCLost() {
        return DataFlycUploadWayPointMissionMsg.ACTION_ON_RC_LOST.find(((Integer) get(14, 1, Integer.class)).intValue());
    }

    public DataFlycUploadWayPointMissionMsg.GIMBAL_PITCH_MODE getGimbalPitchMode() {
        return DataFlycUploadWayPointMissionMsg.GIMBAL_PITCH_MODE.find(((Integer) get(15, 1, Integer.class)).intValue());
    }

    public double getHPLat() {
        return ((Double) get(16, 8, Double.class)).doubleValue();
    }

    public double getHPLng() {
        return ((Double) get(24, 8, Double.class)).doubleValue();
    }

    public float getHPHeight() {
        return ((Float) get(32, 4, Float.class)).floatValue();
    }

    public int getGotoFirstFlag() {
        return ((Integer) get(36, 1, Integer.class)).intValue();
    }

    public int getMissionID() {
        if (this._recData == null || this._recData.length < VALID_REQUIRE_ID_LENGTH) {
            return -1;
        }
        return BytesUtil.getUShort(this._recData, 40);
    }

    @Nullable
    public byte[] getBodyData() {
        if (this._recData == null || this._recData.length < 1) {
            return null;
        }
        byte[] buffer = new byte[(this._recData.length - 1)];
        System.arraycopy(this._recData, 1, buffer, 0, buffer.length);
        return buffer;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.DownloadWayPointMissionMsg.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = 0;
    }
}
