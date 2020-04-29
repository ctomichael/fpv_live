package dji.component.flysafe.model;

import android.support.annotation.Keep;
import dji.component.flysafe.util.NFZLogUtil;
import dji.component.flysafe.util.ProtobufHelper;
import dji.flysafe.SupervisorRecordContext;

@Keep
public class FlyfrbStepInfo {
    public String mAppDynamicDbUuid = "";
    public String mFlightDynamicDBUid = "";
    public long mTFRUploadStep = 0;
    public int mUploadNum = -1;

    public void reset() {
        this.mTFRUploadStep = 0;
        this.mAppDynamicDbUuid = "";
        this.mFlightDynamicDBUid = "";
        this.mUploadNum = -1;
    }

    public void copyFromValuedField(FlyfrbStepInfo other) {
        if (other.mTFRUploadStep != 0) {
            this.mTFRUploadStep = other.mTFRUploadStep;
        }
        if (other.mAppDynamicDbUuid != null && !other.mAppDynamicDbUuid.equals("")) {
            this.mAppDynamicDbUuid = other.mAppDynamicDbUuid;
        }
        if (other.mFlightDynamicDBUid != null && !other.mFlightDynamicDBUid.equals("")) {
            this.mFlightDynamicDBUid = other.mFlightDynamicDBUid;
        }
        if (other.mUploadNum != -1) {
            this.mUploadNum = other.mUploadNum;
        }
    }

    public void convertFromJniModel(SupervisorRecordContext recordContext) {
        this.mTFRUploadStep = (long) ProtobufHelper.toInt(recordContext.tfr_update_state);
        if (recordContext.app_dynamic_db_uuid != null) {
            this.mAppDynamicDbUuid = recordContext.app_dynamic_db_uuid;
            NFZLogUtil.LOGD("supervisor mAppDynamicDbUuid: " + this.mAppDynamicDbUuid);
        }
        if (recordContext.dji_flight_dynamic_db_uuid != null) {
            this.mFlightDynamicDBUid = recordContext.dji_flight_dynamic_db_uuid;
            NFZLogUtil.LOGD("supervisor mFlightDynamicDBUid: " + this.mFlightDynamicDBUid);
        }
        this.mUploadNum = ProtobufHelper.toInt(recordContext.upload_number);
    }

    public FlyfrbStepInfo setAppDynamicDbUuid(String appDynamicDbUuid) {
        this.mAppDynamicDbUuid = appDynamicDbUuid;
        return this;
    }

    public FlyfrbStepInfo setFlightDynamicDBUid(String flightDynamicDBUid) {
        this.mFlightDynamicDBUid = flightDynamicDBUid;
        return this;
    }

    public FlyfrbStepInfo setUploadNum(int uploadNum) {
        this.mUploadNum = uploadNum;
        return this;
    }
}
