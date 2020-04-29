package dji.internal.mission.abstraction.intelligenthotpoint;

import dji.common.flightcontroller.flightassistant.IntelligentHotpointMissionMode;
import dji.common.mission.intelligenthotpoint.IntelligentHotpointMission;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataEyeGetPushPOIExecutionParams;
import dji.midware.data.model.P3.DataEyeGetPushPOITargetInformation;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import java.util.Calendar;
import java.util.Date;

@EXClassNullAway
public class IntelligentHotpointAbstractionCacheData {
    private static final double MISSION_CACHE_EXPIRATION = 5000.0d;
    private IntelligentHotpointMission mission;
    private IntelligentHotpointMissionMode missionMode;
    private DataEyeGetPushPOIExecutionParams missionStatus;
    private DataEyeGetPushPOITargetInformation missionTargetInfo;
    private Date missionValidDate;
    private boolean poi2Enabled;
    private DataOsdGetPushCommon.RcModeChannel rcMode;

    public IntelligentHotpointAbstractionCacheData() {
        reset();
    }

    /* access modifiers changed from: protected */
    public void reset() {
        this.mission = null;
        this.missionValidDate = null;
        this.missionStatus = null;
        this.missionTargetInfo = null;
        this.poi2Enabled = false;
        this.missionMode = null;
        this.rcMode = null;
    }

    /* access modifiers changed from: protected */
    public void replaceMission(IntelligentHotpointMission data) {
        this.mission = data;
        this.missionValidDate = null;
    }

    /* access modifiers changed from: protected */
    public boolean isMissionValid() {
        if (this.mission == null || this.missionValidDate == null || ((double) Math.abs(this.missionValidDate.getTime() - System.currentTimeMillis())) >= 5000.0d) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void renewMissionDate() {
        if (this.mission != null) {
            this.missionValidDate = Calendar.getInstance().getTime();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isMissionStatusInitialized() {
        return this.missionStatus != null;
    }

    /* access modifiers changed from: protected */
    public boolean isMissionTargetInfoInitialized() {
        return this.missionTargetInfo != null;
    }

    /* access modifiers changed from: protected */
    public boolean isMissionStatusChanged(DataEyeGetPushPOIExecutionParams status) {
        if (this.missionStatus == null && status != null) {
            return true;
        }
        if (status == null && this.missionStatus != null) {
            return true;
        }
        if (this.missionStatus == null && status == null) {
            return false;
        }
        if (this.missionStatus.getState() == status.getState()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMissionTargetInfoChanged(DataEyeGetPushPOITargetInformation info) {
        if (this.missionTargetInfo == null && info != null) {
            return true;
        }
        if (info == null && this.missionTargetInfo != null) {
            return true;
        }
        if (this.missionTargetInfo == null && info == null) {
            return false;
        }
        if (this.missionTargetInfo.getStatus() == info.getStatus()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public DataEyeGetPushPOIExecutionParams getMissionStatus() {
        return this.missionStatus;
    }

    /* access modifiers changed from: protected */
    public void setMissionStatus(DataEyeGetPushPOIExecutionParams missionStatus2) {
        this.missionStatus = missionStatus2;
    }

    public DataEyeGetPushPOITargetInformation getMissionTargetInfo() {
        return this.missionTargetInfo;
    }

    public void setMissionTargetInfo(DataEyeGetPushPOITargetInformation missionInfo) {
        this.missionTargetInfo = missionInfo;
    }

    /* access modifiers changed from: protected */
    public boolean isPoi2Enabled() {
        return this.poi2Enabled;
    }

    /* access modifiers changed from: protected */
    public void setPoi2Enabled(boolean poi2Enabled2) {
        this.poi2Enabled = poi2Enabled2;
    }

    /* access modifiers changed from: protected */
    public IntelligentHotpointMissionMode getMissionMode() {
        return this.missionMode;
    }

    /* access modifiers changed from: protected */
    public void setMissionMode(IntelligentHotpointMissionMode missionMode2) {
        this.missionMode = missionMode2;
    }

    /* access modifiers changed from: protected */
    public DataOsdGetPushCommon.RcModeChannel getRcMode() {
        return this.rcMode;
    }

    /* access modifiers changed from: protected */
    public void setRcMode(DataOsdGetPushCommon.RcModeChannel rcMode2) {
        this.rcMode = rcMode2;
    }
}
