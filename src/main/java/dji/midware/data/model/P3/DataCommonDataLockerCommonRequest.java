package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.model.P3.DataCommonDataLocker;
import dji.midware.util.BytesUtil;

@Keep
public class DataCommonDataLockerCommonRequest extends DataCommonDataLocker {
    public static DataCommonDataLockerCommonRequest getInstance() {
        return SingletonHolder.instance;
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataCommonDataLockerCommonRequest instance = new DataCommonDataLockerCommonRequest(DataCommonDataLocker.DataLockerCmdType.VERIFYING);

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public DataCommonDataLocker.DataLockerCmdType getDataProtectionCmdType() {
        if (this._recData != null) {
            return DataCommonDataLocker.DataLockerCmdType.find(BytesUtil.getInt(this._recData, 0, 1));
        }
        return DataCommonDataLocker.DataLockerCmdType.UNKNOWN;
    }

    protected DataCommonDataLockerCommonRequest(DataCommonDataLocker.DataLockerCmdType type) {
        super(type);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) this.mCmdType.value();
        System.arraycopy(BytesUtil.getBytes(this.mCmdType.value()), 0, this._sendData, 1, 4);
    }
}
