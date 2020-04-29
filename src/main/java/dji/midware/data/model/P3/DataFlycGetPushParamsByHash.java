package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.params.P3.ParamInfo;

@Keep
@EXClassNullAway
public class DataFlycGetPushParamsByHash extends DataBase {
    private static DataFlycGetPushParamsByHash instance = null;

    public static synchronized DataFlycGetPushParamsByHash getInstance() {
        DataFlycGetPushParamsByHash dataFlycGetPushParamsByHash;
        synchronized (DataFlycGetPushParamsByHash.class) {
            if (instance == null) {
                instance = new DataFlycGetPushParamsByHash();
            }
            dataFlycGetPushParamsByHash = instance;
        }
        return dataFlycGetPushParamsByHash;
    }

    public String getFirstIndex() {
        return DJIFlycParamInfoManager.getNameByHash(((Long) get(1, 4, Long.class)).longValue());
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
        int tmp = 1;
        while (tmp < data.length) {
            ParamInfo paramInfo = DJIFlycParamInfoManager.readByHash(((Long) get(tmp, 4, Long.class)).longValue());
            int tmp2 = tmp + 4;
            DJIFlycParamInfoManager.write(paramInfo.name, get(tmp2, paramInfo.size, paramInfo.type));
            tmp = tmp2 + paramInfo.size;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
