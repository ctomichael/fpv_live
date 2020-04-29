package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;

@Keep
public class DataRecognizeGetPushIsSupportDJIGO extends DataBase {
    private static DataRecognizeGetPushIsSupportDJIGO instance = null;

    public static synchronized DataRecognizeGetPushIsSupportDJIGO getInstance() {
        DataRecognizeGetPushIsSupportDJIGO dataRecognizeGetPushIsSupportDJIGO;
        synchronized (DataRecognizeGetPushIsSupportDJIGO.class) {
            if (instance == null) {
                instance = new DataRecognizeGetPushIsSupportDJIGO();
            }
            dataRecognizeGetPushIsSupportDJIGO = instance;
        }
        return dataRecognizeGetPushIsSupportDJIGO;
    }

    private DataRecognizeGetPushIsSupportDJIGO() {
        this.isNeedPushLosed = true;
    }

    /* access modifiers changed from: protected */
    public void setPushLose() {
        super.setPushLose();
        clear();
        post();
    }

    public boolean isSupportDJIGO() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 0;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
