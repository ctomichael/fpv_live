package dji.midware.data.manager.P3;

import android.content.Context;
import android.util.Log;
import com.dji.frame.util.V_FileUtil;
import com.dji.frame.util.V_JsonUtil;
import com.google.gson.reflect.TypeToken;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.R;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.data.params.P3.ParamInfoBean;
import java.util.HashMap;
import java.util.List;

@EXClassNullAway
public class DJIFlycParamInfoManager {
    /* access modifiers changed from: private */
    public static Context context;
    private static HashMap<String, ParamInfo> infos = new HashMap<>();
    private static DJIFlycParamInfoManager instance = null;

    protected static void setContext(Context ctx) {
        context = ctx;
        getInstance();
    }

    public static DJIFlycParamInfoManager getInstance() {
        if (instance == null) {
            instance = new DJIFlycParamInfoManager();
        }
        return instance;
    }

    public static boolean isNew() {
        return true;
    }

    public static boolean isNewForOsd() {
        return DataOsdGetPushCommon.getInstance().getFlycVersion() >= 1;
    }

    public static ParamInfo read(String index) {
        return infos.get(index);
    }

    public static ParamInfo readByIndex(int index) {
        for (String key : infos.keySet()) {
            ParamInfo paramInfo = infos.get(key);
            if (paramInfo.index == index) {
                return paramInfo;
            }
        }
        return null;
    }

    public static ParamInfo readByHash(long hash) {
        for (String key : infos.keySet()) {
            ParamInfo paramInfo = infos.get(key);
            if (paramInfo.hash == hash) {
                return paramInfo;
            }
        }
        return null;
    }

    public static String getNameByIndex(int index) {
        for (String key : infos.keySet()) {
            ParamInfo paramInfo = infos.get(key);
            if (paramInfo.index == index) {
                return paramInfo.name;
            }
        }
        return "";
    }

    public static String getNameByHash(long hash) {
        for (String key : infos.keySet()) {
            ParamInfo paramInfo = infos.get(key);
            if (paramInfo.hash == hash) {
                return paramInfo.name;
            }
        }
        return "";
    }

    public static Number valueOf(String index) {
        if (infos.get(index) == null) {
            return 0;
        }
        return infos.get(index).value;
    }

    public static boolean isInited() {
        return infos != null && infos.size() > 0;
    }

    public static void write(String index, Number value) {
        ParamInfo paramInfo = infos.get(index);
        paramInfo.value = value;
        infos.put(index, paramInfo);
    }

    public static void writeNewParamInfo(String index, ParamInfo paramInfo) {
        infos.put(index, paramInfo);
    }

    public static void writeSetValue(String index, String from) {
        ParamInfo paramInfo = infos.get(index);
        paramInfo.setvalue = infos.get(from).value;
        infos.put(index, paramInfo);
    }

    public static void writeSetValue(String index, Number value) {
        ParamInfo paramInfo = infos.get(index);
        paramInfo.setvalue = value;
        infos.put(index, paramInfo);
    }

    public DJIFlycParamInfoManager() {
        init();
    }

    private void init() {
        new Thread(new Runnable() {
            /* class dji.midware.data.manager.P3.DJIFlycParamInfoManager.AnonymousClass1 */

            public void run() {
                DJIFlycParamInfoManager.this.readToMemory(V_FileUtil.rawfileGetContent(DJIFlycParamInfoManager.context, R.raw.flyc_param_infos));
            }
        }, "flycParam").start();
    }

    /* access modifiers changed from: private */
    public void readToMemory(String string) {
        try {
            List<ParamInfoBean> beans = V_JsonUtil.getList(string, new TypeToken<List<ParamInfoBean>>() {
                /* class dji.midware.data.manager.P3.DJIFlycParamInfoManager.AnonymousClass2 */
            });
            if (beans != null && beans.size() > 0) {
                for (ParamInfoBean paramInfoBean : beans) {
                    ParamInfo info = paramInfoBean.getParamInfo();
                    if (info != null) {
                        infos.put(info.name, info);
                    }
                }
                Log.d("DJIFlycParamInfoManager", "readToMemory size = " + infos.size());
            }
        } catch (Throwable e) {
            DJILogHelper.getInstance().LOGE("gsonerror", "flyc read to mem" + e.toString());
        }
    }
}
