package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycRedundancyStatus;

@Keep
@EXClassNullAway
public class DataFlycPushRedundancyStatus extends DataBase {
    private static DataFlycPushRedundancyStatus mInstance = null;

    @Keep
    public static class RedundancySwitchInfo {
        public long dstErrCode;
        public int dstImuIndex;
        public int id;
        public int reqID;
        public int resultCode;
        public long srcErrCode;
        public int srcImuIndex;
        public long time;
    }

    public static class NavigationSystemState {
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

    public static synchronized DataFlycPushRedundancyStatus getInstance() {
        DataFlycPushRedundancyStatus dataFlycPushRedundancyStatus;
        synchronized (DataFlycPushRedundancyStatus.class) {
            if (mInstance == null) {
                mInstance = new DataFlycPushRedundancyStatus();
            }
            dataFlycPushRedundancyStatus = mInstance;
        }
        return dataFlycPushRedundancyStatus;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public DataFlycRedundancyStatus.RS_CMD_TYPE getCmdType() {
        return DataFlycRedundancyStatus.RS_CMD_TYPE.ofValue(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public DataFlycRedundancyStatus.IMUStatus getIMUStatus() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6 = false;
        DataFlycRedundancyStatus.RS_CMD_TYPE cmdType = DataFlycRedundancyStatus.RS_CMD_TYPE.ofValue(((Integer) get(0, 1, Integer.class)).intValue());
        DataFlycRedundancyStatus.IMUStatus status = new DataFlycRedundancyStatus.IMUStatus();
        if (cmdType == DataFlycRedundancyStatus.RS_CMD_TYPE.SEND_ERR_STATE) {
            status.colorStatus = ((Integer) get(1, 1, Integer.class)).intValue();
            long err_code = ((Long) get(2, 4, Long.class)).longValue();
            if (((err_code >> 2) & 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            status.isRealInAir = z;
            status.imuIndex = (int) ((err_code >> 4) & 15);
            status.devType = (int) ((err_code >> 8) & 255);
            status.devIndex = (int) ((err_code >> 12) & 15);
            status.devErrCode = (int) ((err_code >> 24) & 255);
            int inter_code = ((Integer) get(6, 2, Integer.class)).intValue();
            if ((inter_code & 1) == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            status.isNeedRefreshLed = z2;
            if ((inter_code & 2) == 2) {
                z3 = true;
            } else {
                z3 = false;
            }
            status.isNeedShowAtStatusBar = z3;
            if ((inter_code & 4) == 4) {
                z4 = true;
            } else {
                z4 = false;
            }
            status.canRepairForFree = z4;
            if ((inter_code & 8) == 8) {
                z5 = true;
            } else {
                z5 = false;
            }
            status.isNeedAnalyseByApp = z5;
            if ((inter_code & 16) == 16) {
                z6 = true;
            }
            status.canProduction = z6;
            status.userAction = ((Integer) get(8, 1, Integer.class)).intValue();
        }
        return status;
    }

    public RedundancySwitchInfo getSwitchInfo() {
        RedundancySwitchInfo info = new RedundancySwitchInfo();
        info.reqID = ((Integer) get(1, 1, Integer.class)).intValue();
        info.srcImuIndex = ((Integer) get(2, 1, Integer.class)).intValue();
        info.dstImuIndex = ((Integer) get(3, 1, Integer.class)).intValue();
        info.resultCode = ((Integer) get(4, 1, Integer.class)).intValue();
        info.srcErrCode = ((Long) get(5, 4, Long.class)).longValue();
        info.dstErrCode = ((Long) get(9, 4, Long.class)).longValue();
        return info;
    }

    public NavigationSystemState getNavigationSystemState() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6 = false;
        DataFlycRedundancyStatus.RS_CMD_TYPE cmdType = DataFlycRedundancyStatus.RS_CMD_TYPE.ofValue(((Integer) get(0, 1, Integer.class)).intValue());
        NavigationSystemState state = new NavigationSystemState();
        if (cmdType == DataFlycRedundancyStatus.RS_CMD_TYPE.SEND_ERR_STATE) {
            state.colorStatus = ((Integer) get(1, 1, Integer.class)).intValue();
            long err_code = ((Long) get(2, 4, Long.class)).longValue();
            if (((err_code >> 2) & 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            state.isRealInAir = z;
            state.imuIndex = (int) ((err_code >> 4) & 15);
            state.devType = (int) ((err_code >> 8) & 255);
            state.devIndex = (int) ((err_code >> 12) & 15);
            state.devErrCode = (int) ((err_code >> 24) & 255);
            int inter_code = ((Integer) get(6, 2, Integer.class)).intValue();
            if ((inter_code & 1) == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            state.isNeedRefreshLed = z2;
            if ((inter_code & 2) == 2) {
                z3 = true;
            } else {
                z3 = false;
            }
            state.isNeedShowAtStatusBar = z3;
            if ((inter_code & 4) == 4) {
                z4 = true;
            } else {
                z4 = false;
            }
            state.canRepairForFree = z4;
            if ((inter_code & 8) == 8) {
                z5 = true;
            } else {
                z5 = false;
            }
            state.isNeedAnalyseByApp = z5;
            if ((inter_code & 16) == 16) {
                z6 = true;
            }
            state.canProduction = z6;
            state.userAction = ((Integer) get(8, 1, Integer.class)).intValue();
        }
        return state;
    }
}
