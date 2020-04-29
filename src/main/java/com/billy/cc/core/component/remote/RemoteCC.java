package com.billy.cc.core.component.remote;

import android.os.Parcel;
import android.os.Parcelable;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCUtil;
import java.util.Map;
import org.json.JSONObject;

public class RemoteCC implements Parcelable {
    public static final Parcelable.Creator<RemoteCC> CREATOR = new Parcelable.Creator<RemoteCC>() {
        /* class com.billy.cc.core.component.remote.RemoteCC.AnonymousClass1 */

        public RemoteCC createFromParcel(Parcel in2) {
            return new RemoteCC(in2);
        }

        public RemoteCC[] newArray(int size) {
            return new RemoteCC[size];
        }
    };
    private String actionName;
    private String callId;
    private String componentName;
    private boolean isMainThreadSyncCall;
    private Map<String, Object> localParams;
    private Map<String, Object> params;

    public RemoteCC(CC cc) {
        this(cc, false);
    }

    public RemoteCC(CC cc, boolean isMainThreadSyncCall2) {
        this.componentName = cc.getComponentName();
        this.actionName = cc.getActionName();
        this.callId = cc.getCallId();
        this.params = RemoteParamUtil.toRemoteMap(cc.getParams());
        this.isMainThreadSyncCall = isMainThreadSyncCall2;
    }

    public Map<String, Object> getParams() {
        if (this.localParams == null) {
            this.localParams = RemoteParamUtil.toLocalMap(this.params);
        }
        return this.localParams;
    }

    protected RemoteCC(Parcel in2) {
        this.componentName = in2.readString();
        this.actionName = in2.readString();
        this.callId = in2.readString();
        this.isMainThreadSyncCall = in2.readByte() != 0;
        this.params = in2.readHashMap(getClass().getClassLoader());
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.componentName);
        dest.writeString(this.actionName);
        dest.writeString(this.callId);
        dest.writeByte((byte) (this.isMainThreadSyncCall ? 1 : 0));
        dest.writeMap(this.params);
    }

    public String toString() {
        JSONObject json = new JSONObject();
        CCUtil.put(json, "componentName", this.componentName);
        CCUtil.put(json, "actionName", this.actionName);
        CCUtil.put(json, "callId", this.callId);
        CCUtil.put(json, "isMainThreadSyncCall", Boolean.valueOf(this.isMainThreadSyncCall));
        CCUtil.put(json, "params", CCUtil.convertToJson(this.params));
        return json.toString();
    }

    public int describeContents() {
        return 0;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public void setComponentName(String componentName2) {
        this.componentName = componentName2;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName2) {
        this.actionName = actionName2;
    }

    public String getCallId() {
        return this.callId;
    }

    public void setCallId(String callId2) {
        this.callId = callId2;
    }

    public boolean isMainThreadSyncCall() {
        return this.isMainThreadSyncCall;
    }

    public void setMainThreadSyncCall(boolean mainThreadSyncCall) {
        this.isMainThreadSyncCall = mainThreadSyncCall;
    }
}
