package dji.internal.mission.abstraction.waypoint;

import dji.common.mission.waypoint.WaypointExecutionProgress;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointUploadProgress;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@EXClassNullAway
public class WaypointAbstractionCacheData {
    private static final double MISSION_CACHE_EXPIRATION = 5000.0d;
    public DataOsdGetPushCommon.FLYC_STATE fcMode;
    public Boolean isBeginnerModeEnabled;
    public Boolean isMultipleModeEnabled;
    public Boolean isNavigationEnabled;
    public Boolean isSimulatorOn;
    public WaypointExecutionProgress lastProgress;
    public WaypointMission.Builder missionBuilder;
    public Integer missionEvent;
    public DataFlycGetPushWayPointMissionInfo missionStatus;
    public Date missionValidDate;
    public Integer prevReachedIndex;
    public Integer prevTargetIndex;
    public DataOsdGetPushCommon.RcModeChannel rcMode;
    public Integer reachedWaypointIndex;
    public Integer targetIndex;
    public WaypointUploadProgress uploadProgress;

    public WaypointAbstractionCacheData() {
        reset();
    }

    public void reset() {
        this.rcMode = null;
        this.fcMode = null;
        this.isSimulatorOn = null;
        this.isBeginnerModeEnabled = null;
        this.isMultipleModeEnabled = null;
        this.isNavigationEnabled = null;
        this.missionEvent = null;
        this.uploadProgress = null;
        this.targetIndex = null;
        this.reachedWaypointIndex = null;
        this.missionValidDate = null;
        this.missionStatus = new DataFlycGetPushWayPointMissionInfo();
    }

    public void resetMissionStatus() {
        this.missionStatus = new DataFlycGetPushWayPointMissionInfo();
    }

    public boolean isMissionStatusInited() {
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

    public void replaceMission(WaypointMission.Builder mission) {
        this.missionBuilder = mission;
        this.missionValidDate = null;
    }

    public void renewMissionDate() {
        if (this.missionBuilder != null) {
            this.missionValidDate = Calendar.getInstance().getTime();
        }
    }

    public boolean isMissionValid() {
        if (this.missionBuilder == null || this.missionValidDate == null || ((double) Math.abs(this.missionValidDate.getTime() - System.currentTimeMillis())) >= 5000.0d) {
            return false;
        }
        return true;
    }
}
