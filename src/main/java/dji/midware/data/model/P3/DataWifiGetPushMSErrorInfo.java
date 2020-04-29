package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcSetMaster;

@Keep
@EXClassNullAway
public class DataWifiGetPushMSErrorInfo extends DataBase {
    private static DataWifiGetPushMSErrorInfo mInstance = null;

    public static synchronized DataWifiGetPushMSErrorInfo getInstance() {
        DataWifiGetPushMSErrorInfo dataWifiGetPushMSErrorInfo;
        synchronized (DataWifiGetPushMSErrorInfo.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushMSErrorInfo();
            }
            dataWifiGetPushMSErrorInfo = mInstance;
        }
        return dataWifiGetPushMSErrorInfo;
    }

    public DataRcSetMaster.MODE getRcType() {
        return DataRcSetMaster.MODE.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public boolean hasError() {
        return !isWifiAuthSuccess() || !isWifiMacSetted() || !isWifiFreqChannelSetted() || !isCountryCodeSetted() || !isSupportedMSCurCountry() || !isSlaveRelatedMaster();
    }

    public boolean isWifiAuthSuccess() {
        return (((Integer) get(1, 2, Integer.class)).intValue() & 1) == 0;
    }

    public boolean isWifiMacSetted() {
        return (((Integer) get(1, 2, Integer.class)).intValue() & 2) == 0;
    }

    public boolean isWifiFreqChannelSetted() {
        return (((Integer) get(1, 2, Integer.class)).intValue() & 4) == 0;
    }

    public boolean isCountryCodeSetted() {
        return (((Integer) get(1, 2, Integer.class)).intValue() & 8) == 0;
    }

    public boolean isSupportedMSCurCountry() {
        return (((Integer) get(1, 2, Integer.class)).intValue() & 16) == 0;
    }

    public boolean isSlaveRelatedMaster() {
        return (((Integer) get(1, 2, Integer.class)).intValue() & 32) == 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
