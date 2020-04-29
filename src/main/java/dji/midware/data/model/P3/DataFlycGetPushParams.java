package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushParams extends DataBase {
    private static DataFlycGetPushParams instance = null;

    public static synchronized DataFlycGetPushParams getInstance() {
        DataFlycGetPushParams dataFlycGetPushParams;
        synchronized (DataFlycGetPushParams.class) {
            if (instance == null) {
                instance = new DataFlycGetPushParams();
                DataFlycGetPushParamsByHash.getInstance();
                DataFlycGetPushParamsByIndex.getInstance();
            }
            dataFlycGetPushParams = instance;
        }
        return dataFlycGetPushParams;
    }

    public String getFirstIndex() {
        if (DJIFlycParamInfoManager.isNew()) {
            return DataFlycGetPushParamsByHash.getInstance().getFirstIndex();
        }
        return DataFlycGetPushParamsByIndex.getInstance().getFirstIndex();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
