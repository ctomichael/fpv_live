package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.midware.data.model.P3.DataCommonDataLocker;
import dji.midware.util.BytesUtil;
import dji.thirdparty.afinal.core.Arrays;

@Keep
public class DataCommonDataLockerChangeSecurityCode extends DataCommonDataLocker {
    private static final int DATA_LENGTH = 529;
    private byte[] newCode;
    private byte[] oldCode;
    private byte[] userNameData;

    public DataCommonDataLockerChangeSecurityCode(DataCommonDataLocker.DataLockerCmdType type) {
        super(type);
    }

    public DataCommonDataLockerChangeSecurityCode setUsername(String userName) {
        if (!TextUtils.isEmpty(userName)) {
            this.userNameData = BytesUtil.getBytesUTF8(userName);
        }
        return this;
    }

    public DataCommonDataLockerChangeSecurityCode setOldCode(String oldCode2) {
        if (!TextUtils.isEmpty(oldCode2)) {
            this.oldCode = BytesUtil.getBytesUTF8(oldCode2);
        }
        return this;
    }

    public DataCommonDataLockerChangeSecurityCode setNewCode(String newCode2) {
        if (!TextUtils.isEmpty(newCode2)) {
            this.newCode = BytesUtil.getBytesUTF8(newCode2);
        }
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int userNameLength;
        int oldPasscodeLength;
        int newPasscodeLength;
        if (this.userNameData != null) {
            userNameLength = this.userNameData.length;
        } else {
            userNameLength = 0;
        }
        if (this.oldCode != null) {
            oldPasscodeLength = this.oldCode.length;
        } else {
            oldPasscodeLength = 0;
        }
        if (this.newCode != null) {
            newPasscodeLength = this.newCode.length;
        } else {
            newPasscodeLength = 0;
        }
        if (userNameLength > 256) {
            userNameLength = 256;
        }
        if (oldPasscodeLength > 128) {
            oldPasscodeLength = 128;
        }
        if (newPasscodeLength > 128) {
            newPasscodeLength = 128;
        }
        this._sendData = new byte[529];
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
        System.arraycopy(BytesUtil.getBytes(oldPasscodeLength), 0, this._sendData, index4, 4);
        int index5 = index4 + 4;
        if (this.oldCode != null) {
            System.arraycopy(this.oldCode, 0, this._sendData, index5, oldPasscodeLength);
        }
        int index6 = index5 + 128;
        System.arraycopy(BytesUtil.getBytes(newPasscodeLength), 0, this._sendData, index6, 4);
        int index7 = index6 + 4;
        if (this.newCode != null) {
            System.arraycopy(this.newCode, 0, this._sendData, index7, newPasscodeLength);
        }
    }
}
