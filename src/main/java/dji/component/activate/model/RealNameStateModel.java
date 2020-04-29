package dji.component.activate.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import dji.midware.data.model.P3.DataFlycSetGetVerPhone;

public class RealNameStateModel {
    public String deviceSn;
    public String phoneNum;
    public DataFlycSetGetVerPhone.FlycPhoneStatus status = DataFlycSetGetVerPhone.FlycPhoneStatus.Unknown;

    public boolean isNeedRealName() {
        return this.status == DataFlycSetGetVerPhone.FlycPhoneStatus.NeedBind;
    }

    @NonNull
    public String toString() {
        return "phone=" + TextUtils.isEmpty(this.phoneNum) + " sn=" + TextUtils.isEmpty(this.deviceSn) + " status=" + this.status;
    }
}
