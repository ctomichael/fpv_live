package com.dji.permission.checker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

class PhoneStateReadTest implements PermissionTest {
    private Context mContext;

    PhoneStateReadTest(Context context) {
        this.mContext = context;
    }

    @SuppressLint({"HardwareIds"})
    public boolean test() throws Throwable {
        TelephonyManager service = (TelephonyManager) this.mContext.getSystemService("phone");
        if (service.getPhoneType() != 0 && TextUtils.isEmpty(service.getDeviceId()) && TextUtils.isEmpty(service.getSubscriberId())) {
            return false;
        }
        return true;
    }
}
