package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.params.P3.ParamInfo;

@Keep
@EXClassNullAway
public class DataFlycGetPushParamsByIndex extends DataBase {
    private static DataFlycGetPushParamsByIndex instance = null;

    public static synchronized DataFlycGetPushParamsByIndex getInstance() {
        DataFlycGetPushParamsByIndex dataFlycGetPushParamsByIndex;
        synchronized (DataFlycGetPushParamsByIndex.class) {
            if (instance == null) {
                instance = new DataFlycGetPushParamsByIndex();
            }
            dataFlycGetPushParamsByIndex = instance;
        }
        return dataFlycGetPushParamsByIndex;
    }

    public String getFirstIndex() {
        return DJIFlycParamInfoManager.getNameByIndex(((Integer) get(1, 2, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
        int tmp = 1;
        while (tmp < data.length) {
            ParamInfo paramInfo = DJIFlycParamInfoManager.readByIndex(((Integer) get(tmp, 2, Integer.class)).intValue());
            int tmp2 = tmp + 2;
            DJIFlycParamInfoManager.write(paramInfo.name, get(tmp2, paramInfo.size, paramInfo.type));
            tmp = tmp2 + paramInfo.size;
        }
    }

    public ParamInfo getInfo(ParamInfo paramInfo) {
        paramInfo.value = get(3, paramInfo.size, paramInfo.type);
        return paramInfo;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
