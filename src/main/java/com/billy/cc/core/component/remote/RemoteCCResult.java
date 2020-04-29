package com.billy.cc.core.component.remote;

import android.os.Parcel;
import android.os.Parcelable;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import dji.pilot.fpv.util.DJIFlurryReport;
import java.util.Map;
import org.json.JSONObject;

public class RemoteCCResult implements Parcelable {
    public static final Parcelable.Creator<RemoteCCResult> CREATOR = new Parcelable.Creator<RemoteCCResult>() {
        /* class com.billy.cc.core.component.remote.RemoteCCResult.AnonymousClass1 */

        public RemoteCCResult createFromParcel(Parcel in2) {
            return new RemoteCCResult(in2);
        }

        public RemoteCCResult[] newArray(int size) {
            return new RemoteCCResult[size];
        }
    };
    private int code;
    private Map<String, Object> data;
    private String errorMessage;
    private boolean success;

    public RemoteCCResult(CCResult result) {
        setCode(result.getCode());
        setErrorMessage(result.getErrorMessage());
        setSuccess(result.isSuccess());
        this.data = RemoteParamUtil.toRemoteMap(result.getDataMap());
    }

    public CCResult toCCResult() {
        CCResult result = new CCResult();
        result.setCode(getCode());
        result.setErrorMessage(getErrorMessage());
        result.setSuccess(isSuccess());
        result.setDataMap(RemoteParamUtil.toLocalMap(this.data));
        return result;
    }

    public String toString() {
        JSONObject json = new JSONObject();
        CCUtil.put(json, DJIFlurryReport.FlightRecord.V2_EVENT_FLIGHTDATA_SYNCHRONOUS_SUBKEY, Boolean.valueOf(this.success));
        CCUtil.put(json, "code", Integer.valueOf(this.code));
        CCUtil.put(json, "errorMessage", this.errorMessage);
        CCUtil.put(json, "data", CCUtil.convertToJson(this.data));
        return json.toString();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success2) {
        this.success = success2;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage2) {
        this.errorMessage = errorMessage2;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.success ? 1 : 0));
        dest.writeString(this.errorMessage);
        dest.writeInt(this.code);
        dest.writeMap(this.data);
    }

    private RemoteCCResult(Parcel in2) {
        this.success = in2.readByte() != 0;
        this.errorMessage = in2.readString();
        this.code = in2.readInt();
        this.data = in2.readHashMap(getClass().getClassLoader());
    }
}
