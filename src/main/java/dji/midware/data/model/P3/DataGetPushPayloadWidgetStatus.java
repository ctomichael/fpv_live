package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DataBase;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Keep
@EXClassNullAway
public class DataGetPushPayloadWidgetStatus extends DataBase {
    private static final String TAG = "DataGetPushPayloadWidgetStatus";
    private static DataGetPushPayloadWidgetStatus instance = null;
    private boolean isChanged = false;
    private StringBuilder sb = new StringBuilder();
    private ConcurrentHashMap<String, Integer> widgetValues = new ConcurrentHashMap<>(10);

    public static synchronized DataGetPushPayloadWidgetStatus getInstance() {
        DataGetPushPayloadWidgetStatus dataGetPushPayloadWidgetStatus;
        synchronized (DataGetPushPayloadWidgetStatus.class) {
            if (instance == null) {
                instance = new DataGetPushPayloadWidgetStatus();
            }
            dataGetPushPayloadWidgetStatus = instance;
        }
        return dataGetPushPayloadWidgetStatus;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        if (this.pack != null) {
            this.recDatas.put(this.pack.senderId, data);
        }
        setRecData(data);
        if (isChanged(data) && isWantPush() && this.isRegist) {
            post();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return this.isChanged;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getTotalNumOfWidget() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public void setRecData(byte[] data) {
        this._recData = data;
        this.isChanged = false;
        int widgetSize = ((Integer) get(1, 1, Integer.class)).intValue();
        for (int i = 0; i < widgetSize; i++) {
            this.sb.delete(0, this.sb.length());
            this.sb.append(((Integer) get((i * 6) + 2, 1, Integer.class)).intValue()).append(":");
            this.sb.append(((Integer) get((i * 6) + 3, 1, Integer.class)).intValue());
            int widgetValue = ((Integer) get((i * 6) + 4, 4, Integer.class)).intValue();
            Integer oldValue = this.widgetValues.get(this.sb.toString());
            if (oldValue == null || oldValue.intValue() != widgetValue) {
                this.isChanged = true;
                this.widgetValues.put(this.sb.toString(), Integer.valueOf(widgetValue));
            }
        }
    }

    public Integer getValue(int widgetType, int widgetIndex) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(widgetType).append(":");
        sb2.append(widgetIndex);
        if (this.widgetValues.containsKey(sb2.toString())) {
            return this.widgetValues.get(sb2.toString());
        }
        return Integer.MAX_VALUE;
    }

    public Map<String, Integer> getAllWidgetValue() {
        return this.widgetValues;
    }

    public int getGotWidgetSize() {
        return this.widgetValues.size();
    }

    public void clear() {
        super.clear();
        this.widgetValues.clear();
        DJILog.d(TAG, "clear data in DataGetPushPayloadWidgetStatus : recDatas" + this.recDatas, new Object[0]);
    }
}
