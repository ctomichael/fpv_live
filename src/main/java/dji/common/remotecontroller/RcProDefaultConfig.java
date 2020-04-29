package dji.common.remotecontroller;

import com.dji.frame.util.V_FileUtil;
import com.dji.frame.util.V_JsonUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.ContextUtil;
import dji.sdkcache.R;
import java.util.List;

@EXClassNullAway
public class RcProDefaultConfig {
    private static volatile RcProDefaultConfig Instance;
    private List<ListBean> list;

    public List<ListBean> getList() {
        return this.list;
    }

    public void setList(List<ListBean> list2) {
        this.list = list2;
    }

    public String getFunc(String s) {
        for (ListBean bean : this.list) {
            if (bean.getId().equals(s)) {
                return bean.getFunc();
            }
        }
        return null;
    }

    public static RcProDefaultConfig getInstance() {
        if (Instance == null) {
            synchronized (RcProDefaultConfig.class) {
                if (Instance == null) {
                    Instance = (RcProDefaultConfig) V_JsonUtil.toBean(V_FileUtil.rawfileGetContent(ContextUtil.getContext(), R.raw.rc_pro_default), RcProDefaultConfig.class);
                }
            }
        }
        return Instance;
    }

    public static class ListBean {
        private String func;
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getFunc() {
            return this.func;
        }

        public void setFunc(String func2) {
            this.func = func2;
        }
    }
}
