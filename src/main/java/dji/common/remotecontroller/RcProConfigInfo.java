package dji.common.remotecontroller;

import com.dji.frame.util.V_FileUtil;
import com.dji.frame.util.V_JsonUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.ContextUtil;
import dji.sdkcache.R;
import java.util.List;

@EXClassNullAway
public class RcProConfigInfo {
    private static RcProConfigInfo mInstance;
    private ApplicationBean application;
    private List<FunctionsBean> functions;
    private List<ProductlistBean> productlist;

    public static class ApplicationBean {
    }

    private RcProConfigInfo() {
    }

    public static synchronized RcProConfigInfo getInstance() {
        RcProConfigInfo rcProConfigInfo;
        synchronized (RcProConfigInfo.class) {
            if (mInstance == null) {
                mInstance = (RcProConfigInfo) V_JsonUtil.toBean(V_FileUtil.rawfileGetContent(ContextUtil.getContext(), R.raw.rc_pro), RcProConfigInfo.class);
            }
            rcProConfigInfo = mInstance;
        }
        return rcProConfigInfo;
    }

    public ApplicationBean getApplication() {
        return this.application;
    }

    public void setApplication(ApplicationBean application2) {
        this.application = application2;
    }

    public List<FunctionsBean> getFunctions() {
        return this.functions;
    }

    public void setFunctions(List<FunctionsBean> functions2) {
        this.functions = functions2;
    }

    public List<ProductlistBean> getProductlist() {
        return this.productlist;
    }

    public ProductlistBean findProductFuncListByName(String productName) {
        List<ProductlistBean> list = getProductlist();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProduct().equals(productName)) {
                return list.get(i);
            }
        }
        return list.get(0);
    }

    public void setProductlist(List<ProductlistBean> productlist2) {
        this.productlist = productlist2;
    }

    public static class FunctionsBean {
        private int function_class;
        private String id;
        private int value;
        private String zh_cn;

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value2) {
            this.value = value2;
        }

        public String getZh_cn() {
            return this.zh_cn;
        }

        public void setZh_cn(String zh_cn2) {
            this.zh_cn = zh_cn2;
        }

        public int getFunction_class() {
            return this.function_class;
        }

        public void setFunction_class(int function_class2) {
            this.function_class = function_class2;
        }
    }

    public static class ProductlistBean {
        private List<String> button_map_list;
        private String date;
        private List<String> ldrd_map_list;
        private List<ListBean> list;
        private List<String> lwrw_map_list;
        private String min_rcver;
        private String priority;
        private String product;
        private List<String> td_map_list;

        public String getProduct() {
            return this.product;
        }

        public void setProduct(String product2) {
            this.product = product2;
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String date2) {
            this.date = date2;
        }

        public String getMin_rcver() {
            return this.min_rcver;
        }

        public void setMin_rcver(String min_rcver2) {
            this.min_rcver = min_rcver2;
        }

        public String getPriority() {
            return this.priority;
        }

        public void setPriority(String priority2) {
            this.priority = priority2;
        }

        public List<ListBean> getList() {
            return this.list;
        }

        public void setList(List<ListBean> list2) {
            this.list = list2;
        }

        public List<String> getButton_map_list() {
            return this.button_map_list;
        }

        public List<String> getTd_map_list() {
            return this.td_map_list;
        }

        public List<String> getLdrd_map_list() {
            return this.ldrd_map_list;
        }

        public List<String> getLwrw_map_list() {
            return this.lwrw_map_list;
        }

        public static class ListBean {
            private String id;
            private List<String> include;

            public String getId() {
                return this.id;
            }

            public void setId(String id2) {
                this.id = id2;
            }

            public List<String> getInclude() {
                return this.include;
            }

            public void setInclude(List<String> include2) {
                this.include = include2;
            }
        }
    }

    public String getFunctionByValue(int value) {
        for (FunctionsBean bean : this.functions) {
            if (bean.getValue() == value) {
                return bean.getId();
            }
        }
        return null;
    }

    public int getFunctionValueByName(String func) {
        for (FunctionsBean bean : this.functions) {
            if (bean.getId().equals(func)) {
                return bean.getValue();
            }
        }
        return -1;
    }

    public int getFunctionClassValueByName(String func) {
        for (FunctionsBean bean : this.functions) {
            if (bean.getId().equals(func)) {
                return bean.getFunction_class();
            }
        }
        return -1;
    }
}
