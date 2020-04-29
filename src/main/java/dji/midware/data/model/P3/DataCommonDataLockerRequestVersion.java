package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.model.P3.DataCommonDataLocker;

@Keep
public class DataCommonDataLockerRequestVersion extends DataCommonDataLockerCommonRequest {
    public DataCommonDataLockerRequestVersion() {
        super(DataCommonDataLocker.DataLockerCmdType.REQUEST_VERSION);
    }

    public int getVersion() {
        if (this._recData == null || this._recData.length == 0) {
            return 0;
        }
        return ((Integer) get(0, this._recData.length, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        super.doPack();
    }
}
