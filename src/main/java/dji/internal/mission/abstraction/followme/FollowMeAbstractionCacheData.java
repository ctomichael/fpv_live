package dji.internal.mission.abstraction.followme;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import java.util.Arrays;

@EXClassNullAway
public class FollowMeAbstractionCacheData {
    private DataOsdGetPushCommon.FLYC_STATE fcMode;
    private DataFlycGetPushWayPointMissionInfo missionStatus;
    private Boolean navigationEnabled;
    private DataOsdGetPushCommon.RcModeChannel rcMode;

    public FollowMeAbstractionCacheData() {
        reset();
    }

    public void reset() {
        this.rcMode = null;
        this.fcMode = null;
        this.navigationEnabled = null;
        this.missionStatus = null;
    }

    public boolean isMissionStatusInitialized() {
        return this.missionStatus != null;
    }

    public boolean isMissionStatusChanged(DataFlycGetPushWayPointMissionInfo status) {
        if (this.missionStatus == null && status != null) {
            return true;
        }
        if (status == null && this.missionStatus != null) {
            return true;
        }
        if (this.missionStatus == null && status == null) {
            return false;
        }
        if (Arrays.equals(this.missionStatus.getRecData(), status.getRecData())) {
            return false;
        }
        return true;
    }

    public void setRcMode(DataOsdGetPushCommon.RcModeChannel rcMode2) {
        this.rcMode = rcMode2;
    }

    public DataOsdGetPushCommon.RcModeChannel getRcMode() {
        return this.rcMode;
    }

    public void setFcMode(DataOsdGetPushCommon.FLYC_STATE fcMode2) {
        this.fcMode = fcMode2;
    }

    public DataOsdGetPushCommon.FLYC_STATE getFcMode() {
        return this.fcMode;
    }

    public void setNavigationEnabled(boolean navigationEnabled2) {
        this.navigationEnabled = Boolean.valueOf(navigationEnabled2);
    }

    public Boolean isNavigationEnabled() {
        return this.navigationEnabled;
    }

    public void setMissionStatus(DataFlycGetPushWayPointMissionInfo missionStatus2) {
        this.missionStatus = missionStatus2;
    }

    public DataFlycGetPushWayPointMissionInfo getMissionStatus() {
        return this.missionStatus;
    }
}
