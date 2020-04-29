package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.model.P3.DataCommonDataLocker;

@Keep
public class DataCommonDataLockerRequestFormatState extends DataCommonDataLockerCommonRequest {
    public DataCommonDataLockerRequestFormatState() {
        super(DataCommonDataLocker.DataLockerCmdType.REQUEST_FORMAT_STATE);
    }

    public int getFormattingState() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getErrorCode() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }
}
