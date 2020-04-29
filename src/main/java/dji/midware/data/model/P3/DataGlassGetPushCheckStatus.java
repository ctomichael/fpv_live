package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGlassGetPushCheckStatus extends DataBase {
    private static DataGlassGetPushCheckStatus instance = null;

    public static synchronized DataGlassGetPushCheckStatus getInstance() {
        DataGlassGetPushCheckStatus dataGlassGetPushCheckStatus;
        synchronized (DataGlassGetPushCheckStatus.class) {
            if (instance == null) {
                instance = new DataGlassGetPushCheckStatus();
            }
            dataGlassGetPushCheckStatus = instance;
        }
        return dataGlassGetPushCheckStatus;
    }

    private DataGlassGetPushCheckStatus() {
        this.isNeedPushLosed = true;
        this.isRemoteModel = true;
    }

    /* access modifiers changed from: protected */
    public void setPushLose() {
        this.isPushLosed = true;
        post();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
