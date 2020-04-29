package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.model.P3.DataCommonDataLocker;

@Keep
public class DataCommonDataLockerResetting extends DataCommonDataLockerCommonRequest {
    public DataCommonDataLockerResetting(DataCommonDataLocker.DataLockerCmdType type) {
        super(type);
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }
}
