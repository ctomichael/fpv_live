package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataDm368_gGetPushCheckStatus extends DataBase {
    private static DataDm368_gGetPushCheckStatus instance = null;

    public static synchronized DataDm368_gGetPushCheckStatus getInstance() {
        DataDm368_gGetPushCheckStatus dataDm368_gGetPushCheckStatus;
        synchronized (DataDm368_gGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataDm368_gGetPushCheckStatus();
            }
            dataDm368_gGetPushCheckStatus = instance;
        }
        return dataDm368_gGetPushCheckStatus;
    }

    public boolean isOK() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.KumquatX || type == ProductType.KumquatS) {
            return false;
        }
        if (getEncryptStatus() || get68013ConnectStatus()) {
            return true;
        }
        return false;
    }

    public int getVideoBps() {
        return ((Integer) get(0, 4, Integer.class)).intValue() >> 15;
    }

    public boolean get68013ConnectStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 16) & 1) == 0;
    }

    public boolean getHDMIConnectStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 17) & 1) == 0;
    }

    public boolean getAppConnectStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 18) & 1) == 0;
    }

    public boolean getEncryptStatus() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 19) & 1) == 0;
    }

    public boolean getHDMIExist() {
        return ((((Integer) get(0, 4, Integer.class)).intValue() >> 20) & 1) == 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
