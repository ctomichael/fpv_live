package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.midware.data.model.P3.DataCommonDataLocker;
import dji.midware.util.BytesUtil;
import java.util.Arrays;

@Keep
public class DataCommonDataLockerEnabled extends DataCommonDataLocker {
    private static final int dataLength = 397;
    private byte[] passcodeData;
    private byte[] userNameData;

    public DataCommonDataLockerEnabled(DataCommonDataLocker.DataLockerCmdType type) {
        super(type);
    }

    public DataCommonDataLockerEnabled setUsername(String userName) {
        if (!TextUtils.isEmpty(userName)) {
            this.userNameData = BytesUtil.getBytesUTF8(userName);
        }
        return this;
    }

    public DataCommonDataLockerEnabled setCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            this.passcodeData = BytesUtil.getBytesUTF8(code);
        }
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int userNameLength;
        int passcodeLength;
        if (this.userNameData != null) {
            userNameLength = this.userNameData.length;
        } else {
            userNameLength = 0;
        }
        if (this.passcodeData != null) {
            passcodeLength = this.passcodeData.length;
        } else {
            passcodeLength = 0;
        }
        if (userNameLength > 256) {
            userNameLength = 256;
        }
        if (passcodeLength > 128) {
            passcodeLength = 128;
        }
        this._sendData = new byte[dataLength];
        Arrays.fill(this._sendData, (byte) 0);
        this._sendData[0] = (byte) this.mCmdType.value();
        int index = 0 + 1;
        System.arraycopy(BytesUtil.getBytes(this.mCmdType.value()), 0, this._sendData, index, 4);
        int index2 = index + 4;
        System.arraycopy(BytesUtil.getBytes(userNameLength), 0, this._sendData, index2, 4);
        int index3 = index2 + 4;
        if (this.userNameData != null) {
            System.arraycopy(this.userNameData, 0, this._sendData, index3, userNameLength);
        }
        int index4 = index3 + 256;
        System.arraycopy(BytesUtil.getBytes(passcodeLength), 0, this._sendData, index4, 4);
        int index5 = index4 + 4;
        if (this.passcodeData != null) {
            System.arraycopy(this.passcodeData, 0, this._sendData, index5, passcodeLength);
        }
    }
}
