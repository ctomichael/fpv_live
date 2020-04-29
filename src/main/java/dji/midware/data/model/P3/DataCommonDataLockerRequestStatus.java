package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.model.P3.DataCommonDataLocker;

@Keep
public class DataCommonDataLockerRequestStatus extends DataCommonDataLockerCommonRequest {
    public DataCommonDataLockerRequestStatus() {
        super(DataCommonDataLocker.DataLockerCmdType.REQUEST_STATUS);
    }

    public int getEnabledState() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean isverified() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getUsernameLength() {
        return ((Integer) get(2, 4, Integer.class)).intValue();
    }

    public String getUsername() {
        int length = getUsernameLength();
        if (this._recData == null || length <= 0) {
            return "";
        }
        return getUTF8(6, length);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        super.doPack();
    }
}
