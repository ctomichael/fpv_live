package dji.internal.mission.abstraction.hotpoint;

import dji.common.mission.hotpoint.HotpointMission;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@EXClassNullAway
public class HotpointAbstractionCacheData {
    private static final double MISSION_CACHE_EXPIRATION = 5000.0d;
    private DataOsdGetPushCommon.FLYC_STATE fcMode;
    private HotpointMission mission;
    private DataFlycGetPushWayPointMissionInfo missionStatus;
    private Date missionValidDate;
    private Boolean navigationEnabled;
    private DataOsdGetPushCommon.RcModeChannel rcMode;

    public HotpointAbstractionCacheData() {
        reset();
    }

    public void reset() {
        this.rcMode = null;
        this.fcMode = null;
        this.navigationEnabled = null;
        this.missionValidDate = null;
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

    public void replaceMission(HotpointMission data) {
        this.mission = data;
        this.missionValidDate = null;
    }

    public void renewMissionDate() {
        if (this.mission != null) {
            this.missionValidDate = Calendar.getInstance().getTime();
        }
    }

    public boolean isMissionValid() {
        if (this.mission == null || this.missionValidDate == null || ((double) Math.abs(this.missionValidDate.getTime() - System.currentTimeMillis())) >= 5000.0d) {
            return false;
        }
        return true;
    }

    public DataOsdGetPushCommon.RcModeChannel getRcMode() {
        return this.rcMode;
    }

    public void setRcMode(DataOsdGetPushCommon.RcModeChannel rcMode2) {
        this.rcMode = rcMode2;
    }

    public DataOsdGetPushCommon.FLYC_STATE getFcMode() {
        return this.fcMode;
    }

    public void setFcMode(DataOsdGetPushCommon.FLYC_STATE fcMode2) {
        this.fcMode = fcMode2;
    }

    public Boolean isNavigationEnabled() {
        return this.navigationEnabled;
    }

    public void setNavigationEnabled(boolean navigationEnabled2) {
        this.navigationEnabled = Boolean.valueOf(navigationEnabled2);
    }

    public DataFlycGetPushWayPointMissionInfo getMissionStatus() {
        return this.missionStatus;
    }

    public void setMissionStatus(DataFlycGetPushWayPointMissionInfo missionStatus2) {
        this.missionStatus = missionStatus2;
    }
}
