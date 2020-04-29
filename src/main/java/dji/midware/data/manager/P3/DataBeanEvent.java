package dji.midware.data.manager.P3;

import com.lmax.disruptor.EventFactory;
import dji.midware.data.manager.P3.DataBaseProcessor;

public class DataBeanEvent {
    private DataBaseProcessor.DataBean mDataBean;
    private int mKey;

    public void setDataBean(DataBaseProcessor.DataBean dataBean) {
        this.mDataBean = dataBean;
    }

    public void setKey(int key) {
        this.mKey = key;
    }

    public int getKey() {
        return this.mKey;
    }

    public DataBaseProcessor.DataBean getDataBean() {
        return this.mDataBean;
    }

    public static class DataBeanEventFactory implements EventFactory<DataBeanEvent> {
        public DataBeanEvent newInstance() {
            return new DataBeanEvent();
        }
    }
}
