package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetMaster;

@Keep
@EXClassNullAway
public class DataWifiGetPushMasterSlaveStatus extends DataBase {
    private static DataWifiGetPushMasterSlaveStatus mInstance = null;
    private String mAuthCode;
    private String mConnectState;
    private String mFreqPoint;
    private String mMasterId;
    private String mRecStatus;
    private String mRecvFreq;
    private String mRssi;
    private String mSendFreq;
    private String mSlaveId;
    private String mStatusMode;

    public static synchronized DataWifiGetPushMasterSlaveStatus getInstance() {
        DataWifiGetPushMasterSlaveStatus dataWifiGetPushMasterSlaveStatus;
        synchronized (DataWifiGetPushMasterSlaveStatus.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushMasterSlaveStatus();
            }
            dataWifiGetPushMasterSlaveStatus = mInstance;
        }
        return dataWifiGetPushMasterSlaveStatus;
    }

    public DataRcSetMaster.MODE getStatusMode() {
        if (TextUtils.isEmpty(this.mStatusMode)) {
            return DataRcSetMaster.MODE.OTHER;
        }
        int mode = DataRcSetMaster.MODE.Master.value();
        try {
            mode = Integer.valueOf(this.mStatusMode).intValue();
        } catch (NumberFormatException e) {
        }
        return DataRcSetMaster.MODE.find(mode);
    }

    public String getMasterId() {
        return this.mMasterId;
    }

    public String getSlaveId() {
        return this.mSlaveId;
    }

    public String getConnectState() {
        return this.mConnectState;
    }

    public String getFreqPoint() {
        return this.mFreqPoint;
    }

    public String getRssi() {
        return this.mRssi;
    }

    public String getSendFreq() {
        return this.mSendFreq;
    }

    public String getRecvFreq() {
        return this.mRecvFreq;
    }

    public String getAuthCode() {
        return this.mAuthCode;
    }

    /* access modifiers changed from: protected */
    public void post() {
        extractInfo();
        super.post();
    }

    private void extractInfo() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        this.mRecStatus = getStatusString();
        this.mRecStatus = this.mRecStatus.substring(0, this.mRecStatus.length() - 1);
        if (!"".equals(this.mRecStatus)) {
            String[] strArray = this.mRecStatus.split(",");
            int i = 0;
            if (0 < strArray.length) {
                str = strArray[0];
                i = 0 + 1;
            } else {
                str = "";
            }
            this.mStatusMode = str;
            if (i < strArray.length) {
                str2 = strArray[i];
                i++;
            } else {
                str2 = "";
            }
            this.mMasterId = str2;
            if (i < strArray.length) {
                str3 = strArray[i];
                i++;
            } else {
                str3 = "";
            }
            this.mSlaveId = str3;
            if (i < strArray.length) {
                str4 = strArray[i];
                i++;
            } else {
                str4 = "";
            }
            this.mConnectState = str4;
            if (i < strArray.length) {
                str5 = strArray[i];
                i++;
            } else {
                str5 = "";
            }
            this.mFreqPoint = str5;
            if (i < strArray.length) {
                str6 = strArray[i];
                i++;
            } else {
                str6 = "";
            }
            this.mRssi = str6;
            if (i < strArray.length) {
                str7 = strArray[i];
                i++;
            } else {
                str7 = "";
            }
            this.mSendFreq = str7;
            if (i < strArray.length) {
                str8 = strArray[i];
                i++;
            } else {
                str8 = "";
            }
            this.mRecvFreq = str8;
            if (i < strArray.length) {
                int i2 = i + 1;
                str9 = strArray[i];
            } else {
                str9 = "";
            }
            this.mAuthCode = str9;
        }
    }

    private String getStatusString() {
        if (this._recData == null || this._recData.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte a_recData : this._recData) {
            sb.append((char) a_recData);
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
