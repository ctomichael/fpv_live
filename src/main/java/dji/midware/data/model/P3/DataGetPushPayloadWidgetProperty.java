package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;
import java.util.List;

@Keep
public class DataGetPushPayloadWidgetProperty extends DataBase {
    private static final String TAG = "DataGetPushPayloadWidgetProperty";
    public static final int WIDTET_TYPE_BUTTON = 1;
    public static final int WIDTET_TYPE_INPUT = 5;
    public static final int WIDTET_TYPE_LIST = 4;
    public static final int WIDTET_TYPE_RANGE = 3;
    public static final int WIDTET_TYPE_SWITCH = 2;
    private static DataGetPushPayloadWidgetProperty instance = null;
    private boolean isChanged = false;
    private SparseArray<String> widgetHintMsg = new SparseArray<>(10);
    private SparseArray<SparseArray<String>> widgetNames = new SparseArray<>(10);
    private SparseArray<List<String>> widgetSubList = new SparseArray<>(10);

    public static synchronized DataGetPushPayloadWidgetProperty getInstance() {
        DataGetPushPayloadWidgetProperty dataGetPushPayloadWidgetProperty;
        synchronized (DataGetPushPayloadWidgetProperty.class) {
            if (instance == null) {
                instance = new DataGetPushPayloadWidgetProperty();
            }
            dataGetPushPayloadWidgetProperty = instance;
        }
        return dataGetPushPayloadWidgetProperty;
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

    public void setRecData(byte[] data) {
        this._recData = data;
        this.isChanged = false;
        int widgetType = ((Integer) get(0, 1, Integer.class)).intValue();
        int widgetIndex = ((Integer) get(1, 1, Integer.class)).intValue();
        String widgetName = getUTF8(2, 32);
        if (this.widgetNames.indexOfKey(widgetType) < 0) {
            this.widgetNames.put(widgetType, new SparseArray(10));
            this.isChanged = true;
        }
        SparseArray<String> indexNameMap = this.widgetNames.get(widgetType);
        if (indexNameMap.indexOfKey(widgetIndex) < 0 || !((String) indexNameMap.get(widgetIndex)).equals(widgetName)) {
            indexNameMap.put(widgetIndex, widgetName);
            this.isChanged = true;
        }
        if (widgetType == 5) {
            String hintMsg = getUTF8(34, 32);
            if (this.widgetHintMsg.indexOfKey(widgetIndex) < 0 || !this.widgetHintMsg.get(widgetIndex).equals(hintMsg)) {
                this.isChanged = true;
                this.widgetHintMsg.put(widgetIndex, hintMsg);
            }
        } else if (widgetType == 4) {
            int subListSize = ((Integer) get(34, 1, Integer.class)).intValue();
            if (this.widgetSubList.indexOfKey(widgetIndex) < 0 || this.widgetSubList.get(widgetIndex).size() != subListSize) {
                List<String> subItems = new ArrayList<>(subListSize);
                for (int index = 0; index < subListSize; index++) {
                    subItems.add(index, getUTF8((index * 16) + 35, 16));
                }
                this.widgetSubList.put(widgetIndex, subItems);
                this.isChanged = true;
            }
        }
    }

    public String getWidgetName(int widgetType, int widgetIndex) {
        if (this.widgetNames.indexOfKey(widgetType) >= 0) {
            SparseArray<String> indexNameMap = this.widgetNames.get(widgetType);
            if (indexNameMap.indexOfKey(widgetIndex) >= 0) {
                return (String) indexNameMap.get(widgetIndex);
            }
        }
        return "UnknownName";
    }

    public String getHintMsgOfWidget(int widgetType, int widgetIndex) {
        if (widgetType != 5) {
            return "";
        }
        return this.widgetHintMsg.get(widgetIndex);
    }

    public List<String> getSubListOfWidget(int widgetType, int widgetIndex) {
        if (widgetType != 4) {
            return null;
        }
        return this.widgetSubList.get(widgetIndex);
    }

    public int getWidgetNamesSize() {
        int sum = 0;
        int size = this.widgetNames.size();
        for (int i = 0; i < size; i++) {
            sum += this.widgetNames.valueAt(i).size();
        }
        return sum;
    }

    public void clear() {
        super.clear();
        DJILog.d(TAG, "clear data in DataGetPushPayloadWidgetProperty : recDatas" + this.recDatas, new Object[0]);
        this.widgetNames.clear();
    }
}
