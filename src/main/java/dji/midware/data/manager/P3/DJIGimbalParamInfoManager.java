package dji.midware.data.manager.P3;

import android.content.Context;
import com.dji.frame.util.V_FileUtil;
import com.dji.frame.util.V_JsonUtil;
import com.google.gson.reflect.TypeToken;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.R;
import dji.midware.data.params.P3.GimbalParamInfo;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.data.params.P3.ParamInfoBean;
import java.util.HashMap;
import java.util.List;

@EXClassNullAway
public class DJIGimbalParamInfoManager {
    public static final String[] CONFIG_NAME_KEYS = {"gimbal_config_name_0", "gimbal_config_name_1", "gimbal_config_name_2"};
    /* access modifiers changed from: private */
    public static Context context;
    private static HashMap<String, GimbalParamInfo> infos = new HashMap<>();
    private static DJIGimbalParamInfoManager instance = null;

    protected static void setContext(Context ctx) {
        context = ctx;
        getInstance();
    }

    public static DJIGimbalParamInfoManager getInstance() {
        if (instance == null) {
            instance = new DJIGimbalParamInfoManager();
        }
        return instance;
    }

    public static ParamInfo read(String index) {
        return infos.get(index);
    }

    public static Number valueOf(String index) {
        return infos.get(index).value;
    }

    public static boolean isConfigNameKey(String keyName) {
        for (int i = 0; i != CONFIG_NAME_KEYS.length; i++) {
            if (keyName.equals(CONFIG_NAME_KEYS[i])) {
                return true;
            }
        }
        return false;
    }

    public static void write(String index, Number value) {
        GimbalParamInfo paramInfo = infos.get(index);
        paramInfo.value = value;
        infos.put(index, paramInfo);
    }

    public static void writeStrValue(String index, String strValue) {
        GimbalParamInfo paramInfo = infos.get(index);
        paramInfo.strValue = strValue;
        infos.put(index, paramInfo);
    }

    public static void writeSetValue(String index, String from) {
        GimbalParamInfo paramInfo = infos.get(index);
        paramInfo.setvalue = infos.get(from).value;
        infos.put(index, paramInfo);
    }

    public static void writeSetValue(String index, Number value) {
        GimbalParamInfo paramInfo = infos.get(index);
        paramInfo.setvalue = value;
        infos.put(index, paramInfo);
    }

    public DJIGimbalParamInfoManager() {
        init();
    }

    private void init() {
        new Thread(new Runnable() {
            /* class dji.midware.data.manager.P3.DJIGimbalParamInfoManager.AnonymousClass1 */

            public void run() {
                DJIGimbalParamInfoManager.this.readToMemory(V_FileUtil.rawfileGetContent(DJIGimbalParamInfoManager.context, R.raw.gimbal_param_infos));
            }
        }, "gimbalParam").start();
    }

    /* access modifiers changed from: private */
    public void readToMemory(String string) {
        try {
            List<ParamInfoBean> beans = V_JsonUtil.getList(string, new TypeToken<List<ParamInfoBean>>() {
                /* class dji.midware.data.manager.P3.DJIGimbalParamInfoManager.AnonymousClass2 */
            });
            if (beans != null && beans.size() > 0) {
                for (ParamInfoBean paramInfoBean : beans) {
                    GimbalParamInfo info = GimbalParamInfo.copyOf(paramInfoBean.getParamInfo());
                    if (info != null) {
                        infos.put(info.name, info);
                    }
                }
            }
        } catch (Throwable e) {
            DJILogHelper.getInstance().LOGE("gsonerror", "gimbal read to mem" + e.toString());
        }
    }
}
