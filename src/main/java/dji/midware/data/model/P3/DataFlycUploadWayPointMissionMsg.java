package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
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
public class DataFlycUploadWayPointMissionMsg extends DataBase implements DJIDataSyncListener {
    private static DataFlycUploadWayPointMissionMsg instance = null;
    private ACTION_ON_RC_LOST actionOnRCLost = ACTION_ON_RC_LOST.EXIT_WP;
    private float cmdSpeed = 3.0f;
    private FINISH_ACTION finishAction;
    private GIMBAL_PITCH_MODE gimbalPitchMode = GIMBAL_PITCH_MODE.PITCH_SMOOTH;
    private GOTO_FIRST_POINT_ACTION gotoFirstFlag = GOTO_FIRST_POINT_ACTION.MAX_HEIGHT;
    private double hpHeight = 0.0d;
    private double hpLat = 0.0d;
    private double hpLng = 0.0d;
    private float idleSpeed = 10.0f;
    private int missionID = 0;
    private int repeatNum;
    private TRACE_MODE traceMode;
    private int wayPointsCount = 0;
    private YAW_MODE yawMode;

    public static synchronized DataFlycUploadWayPointMissionMsg getInstance() {
        DataFlycUploadWayPointMissionMsg dataFlycUploadWayPointMissionMsg;
        synchronized (DataFlycUploadWayPointMissionMsg.class) {
            if (instance == null) {
                instance = new DataFlycUploadWayPointMissionMsg();
            }
            dataFlycUploadWayPointMissionMsg = instance;
        }
        return dataFlycUploadWayPointMissionMsg;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycUploadWayPointMissionMsg setWayPointCount(int wayPointsCount2) {
        this.wayPointsCount = wayPointsCount2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setMissionID(int id) {
        this.missionID = id;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setCmdSpeed(float cmdSpeed2) {
        this.cmdSpeed = cmdSpeed2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setIdleSpeed(float idleSpeed2) {
        this.idleSpeed = idleSpeed2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setFinishAction(FINISH_ACTION finishAction2) {
        this.finishAction = finishAction2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setRepeatNum(int repeatNum2) {
        this.repeatNum = repeatNum2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setYawMode(YAW_MODE yawMode2) {
        this.yawMode = yawMode2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setTraceMOde(TRACE_MODE traceMode2) {
        this.traceMode = traceMode2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg setActionOnRCLost(ACTION_ON_RC_LOST actionOnRCLost2) {
        this.actionOnRCLost = actionOnRCLost2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg seGimbalPitchMode(GIMBAL_PITCH_MODE gimbalPitchMode2) {
        this.gimbalPitchMode = gimbalPitchMode2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg seHPLat(double hpLat2) {
        this.hpLat = hpLat2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg seHPLng(double hpLng2) {
        this.hpLng = hpLng2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg seHPHeight(double hpHeight2) {
        this.hpHeight = hpHeight2;
        return this;
    }

    public DataFlycUploadWayPointMissionMsg seGotoFirstFlag(GOTO_FIRST_POINT_ACTION gotoFirstFlag2) {
        this.gotoFirstFlag = gotoFirstFlag2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.UploadWayPointMissionMsg.value();
        pack.data = getSendData();
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    public byte[] getSendDataForRecord() {
        return super.getSendData();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[51];
        this._sendData[0] = BytesUtil.getByte(this.wayPointsCount);
        System.arraycopy(BytesUtil.getBytes(this.cmdSpeed), 0, this._sendData, 1, 4);
        System.arraycopy(BytesUtil.getBytes(this.idleSpeed), 0, this._sendData, 5, 4);
        this._sendData[9] = (byte) this.finishAction.value();
        this._sendData[10] = (byte) this.repeatNum;
        this._sendData[11] = (byte) this.yawMode.value();
        this._sendData[12] = (byte) this.traceMode.value();
        this._sendData[13] = (byte) this.actionOnRCLost.value();
        this._sendData[14] = (byte) this.gimbalPitchMode.value();
        System.arraycopy(BytesUtil.getBytes(this.hpLat), 0, this._sendData, 15, 8);
        System.arraycopy(BytesUtil.getBytes(this.hpLng), 0, this._sendData, 23, 8);
        System.arraycopy(BytesUtil.getBytes(this.hpHeight), 0, this._sendData, 31, 4);
        this._sendData[35] = (byte) this.gotoFirstFlag.value();
        System.arraycopy(BytesUtil.getUnsignedBytes(this.missionID), 0, this._sendData, 39, 2);
    }

    @Keep
    public enum FINISH_ACTION {
        NONE(0),
        GOHOME(1),
        LAND(2),
        BACK_TO_FIRST_WAY_POINT(3),
        NO_LIMIT(4);
        
        private int data;

        private FINISH_ACTION(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FINISH_ACTION find(int b) {
            FINISH_ACTION result = NONE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum YAW_MODE {
        AUTO_COURSE(0),
        FREE_COURSE(1),
        REMOTE_CONTROL(2),
        PATH_COURSE(3),
        HP_MODE(4);
        
        private int data;

        private YAW_MODE(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static YAW_MODE find(int b) {
            YAW_MODE result = AUTO_COURSE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum TRACE_MODE {
        EXEC_MESSION(0),
        SMOOTH_PATH(1);
        
        private int data;

        private TRACE_MODE(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TRACE_MODE find(int b) {
            TRACE_MODE result = EXEC_MESSION;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ACTION_ON_RC_LOST {
        EXIT_WP(0),
        CONTINUE_WP(1);
        
        private int data;

        private ACTION_ON_RC_LOST(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ACTION_ON_RC_LOST find(int b) {
            ACTION_ON_RC_LOST result = EXIT_WP;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum GIMBAL_PITCH_MODE {
        PITCH_FREE(0),
        PITCH_SMOOTH(1);
        
        private int data;

        private GIMBAL_PITCH_MODE(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GIMBAL_PITCH_MODE find(int b) {
            GIMBAL_PITCH_MODE result = PITCH_SMOOTH;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum GOTO_FIRST_POINT_ACTION {
        MAX_HEIGHT(0),
        POINT_TO_POINT(1);
        
        private int data;

        private GOTO_FIRST_POINT_ACTION(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GOTO_FIRST_POINT_ACTION find(int b) {
            GOTO_FIRST_POINT_ACTION result = MAX_HEIGHT;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
