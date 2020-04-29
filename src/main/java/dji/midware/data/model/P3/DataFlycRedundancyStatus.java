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
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataFlycRedundancyStatus extends DataBase implements DJIDataSyncListener {
    public static final int acc_err_version = 1;
    public static final int baro_err_version = 1;
    public static final int comm_version = 1;
    public static final int compass_err_version = 1;
    public static final int gps_err_version = 1;
    public static final int gyro_err_version = 1;
    public static final int imu_err_version = 1;
    private static DataFlycRedundancyStatus instance = null;
    public static final int radar_err_version = 1;
    public static final int rtk_err_version = 1;
    public static final int us_err_version = 1;
    public static final int vo_err_version = 1;
    private RS_CMD_TYPE mCmdID = RS_CMD_TYPE.ASK_VERSION;

    @Keep
    public static class IMUStatus {
        public boolean canProduction;
        public boolean canRepairForFree;
        public int colorStatus;
        public int devErrCode;
        public int devIndex;
        public int devType;
        public int id;
        public int imuIndex;
        public boolean isCtrlUsed;
        public boolean isNSUsed;
        public boolean isNeedAnalyseByApp;
        public boolean isNeedRefreshLed;
        public boolean isNeedShowAtStatusBar;
        public boolean isRealInAir;
        public long time;
        public int userAction;
    }

    public static synchronized DataFlycRedundancyStatus getInstance() {
        DataFlycRedundancyStatus dataFlycRedundancyStatus;
        synchronized (DataFlycRedundancyStatus.class) {
            if (instance == null) {
                instance = new DataFlycRedundancyStatus();
            }
            dataFlycRedundancyStatus = instance;
        }
        return dataFlycRedundancyStatus;
    }

    public DataFlycRedundancyStatus setCmdType(RS_CMD_TYPE cmdType) {
        this.mCmdID = cmdType;
        return this;
    }

    public RS_CMD_TYPE getCmdType() {
        return RS_CMD_TYPE.ofValue(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public VersionResult getVersionResult() {
        VersionResult res = new VersionResult();
        res.COMM = ((Integer) get(1, 1, Integer.class)).intValue();
        res.IMU = ((Integer) get(2, 1, Integer.class)).intValue();
        res.ACC = ((Integer) get(3, 1, Integer.class)).intValue();
        res.GYRO = ((Integer) get(4, 1, Integer.class)).intValue();
        res.BARO = ((Integer) get(5, 1, Integer.class)).intValue();
        res.COMPASS = ((Integer) get(6, 1, Integer.class)).intValue();
        res.GPS = ((Integer) get(7, 1, Integer.class)).intValue();
        res.RTK = ((Integer) get(8, 1, Integer.class)).intValue();
        res.VO = ((Integer) get(9, 1, Integer.class)).intValue();
        res.US = ((Integer) get(10, 1, Integer.class)).intValue();
        res.RADAR = ((Integer) get(11, 1, Integer.class)).intValue();
        return res;
    }

    public List<IMUStatus> getIMUStatus() {
        List<IMUStatus> lists = new ArrayList<>();
        if (RS_CMD_TYPE.ofValue(((Integer) get(0, 1, Integer.class)).intValue()) == RS_CMD_TYPE.ASK_ERR_STATE) {
            for (int i = 0; i < 3; i++) {
                IMUStatus status = new IMUStatus();
                status.colorStatus = ((Integer) get((i * 8) + 1, 1, Integer.class)).intValue();
                long err_code = ((Long) get((i * 8) + 2, 4, Long.class)).longValue();
                status.isRealInAir = ((err_code >> 2) & 1) == 1;
                status.imuIndex = (int) ((err_code >> 4) & 15);
                status.devType = (int) ((err_code >> 8) & 255);
                status.devIndex = (int) ((err_code >> 12) & 15);
                status.devErrCode = (int) ((err_code >> 24) & 255);
                int inter_code = ((Integer) get((i * 8) + 6, 2, Integer.class)).intValue();
                status.isNeedRefreshLed = (inter_code & 1) == 1;
                status.isNeedShowAtStatusBar = (inter_code & 2) == 2;
                status.canRepairForFree = (inter_code & 4) == 4;
                status.isNeedAnalyseByApp = (inter_code & 8) == 8;
                status.canProduction = (inter_code & 16) == 16;
                status.userAction = ((Integer) get((i * 8) + 8, 1, Integer.class)).intValue();
                lists.add(status);
            }
        }
        return lists;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.RedundancyStatus.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.mCmdID == RS_CMD_TYPE.ASK_VERSION) {
            this._sendData = new byte[12];
            this._sendData[1] = 1;
            this._sendData[2] = 1;
            this._sendData[3] = 1;
            this._sendData[4] = 1;
            this._sendData[5] = 1;
            this._sendData[6] = 1;
            this._sendData[7] = 1;
            this._sendData[8] = 1;
            this._sendData[9] = 1;
            this._sendData[10] = 1;
            this._sendData[11] = 1;
        } else {
            this._sendData = new byte[1];
        }
        this._sendData[0] = (byte) this.mCmdID.value();
    }

    @Keep
    public enum RS_CMD_TYPE {
        ASK_VERSION(1),
        ASK_ERR_STATE(2),
        SEND_ERR_STATE(3),
        SEND_SWITCH_STATE(4);
        
        private int mValue = 0;

        private RS_CMD_TYPE(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean belongs(int value) {
            return this.mValue == value;
        }

        public static RS_CMD_TYPE ofValue(int value) {
            RS_CMD_TYPE[] values = values();
            for (RS_CMD_TYPE ht : values) {
                if (ht.belongs(value)) {
                    return ht;
                }
            }
            return ASK_VERSION;
        }
    }

    @Keep
    public static class VersionResult {
        public int ACC;
        public int BARO;
        public int COMM;
        public int COMPASS;
        public int GPS;
        public int GYRO;
        public int IMU;
        public int RADAR;
        public int RTK;
        public int US;
        public int VO;

        public boolean isVersionMatch() {
            return this.COMM == 0 && this.IMU == 0 && this.ACC == 0 && this.GYRO == 0 && this.BARO == 0 && this.COMPASS == 0 && this.GPS == 0 && this.RTK == 0 && this.VO == 0 && this.US == 0 && this.RADAR == 0;
        }
    }

    @Keep
    public enum COLOR_STATUS {
        GRAY(0),
        GREEN(1),
        YELLOW(2),
        RED(3),
        GREEN_FLASH(17),
        YELLOW_FLASH(18),
        RED_FLASH(19);
        
        private int mValue = 0;

        private COLOR_STATUS(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean belongs(int value) {
            return this.mValue == value;
        }

        public static COLOR_STATUS ofValue(int value) {
            COLOR_STATUS[] values = values();
            for (COLOR_STATUS ht : values) {
                if (ht.belongs(value)) {
                    return ht;
                }
            }
            return GRAY;
        }
    }

    @Keep
    public enum USER_ACTION {
        None(0),
        CALC_IMU(1),
        CALC_COMPASS(2),
        WAIT_WARM_UP(3),
        FILL_INSTALL(4),
        CHECK_INSTALL(5),
        STAY_GROUND(6),
        CHECK_CONNECTION(7),
        REBOOT_PILOT(8),
        UPGRATE(9),
        LANDING_FOR_CHECKING(10),
        STABLE_FLY(11),
        SWITCH_ATTI_MODE(12),
        REPAIR(13),
        WAIT_FILOT_COLD(14),
        SWITCH_IMU(15),
        FIND_LARGE_PLACE(16),
        REQUEST_PERMISSION(17),
        UNLOCK(18),
        FILL_RIGHT_PARAMS(19);
        
        private int mValue = 0;

        private USER_ACTION(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean belongs(int value) {
            return this.mValue == value;
        }

        public static USER_ACTION ofValue(int value) {
            USER_ACTION[] values = values();
            for (USER_ACTION ht : values) {
                if (ht.belongs(value)) {
                    return ht;
                }
            }
            return None;
        }
    }
}
