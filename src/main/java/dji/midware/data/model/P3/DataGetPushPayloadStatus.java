package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DataBase;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DataGetPushPayloadStatus extends DataBase {
    private static final String TAG = "DataGetPushPayloadStatus";
    private static DataGetPushPayloadStatus instance = null;

    @Keep
    public enum PayloadNeedReloadEvent {
        ConectChanged
    }

    public static synchronized DataGetPushPayloadStatus getInstance() {
        DataGetPushPayloadStatus dataGetPushPayloadStatus;
        synchronized (DataGetPushPayloadStatus.class) {
            if (instance == null) {
                instance = new DataGetPushPayloadStatus();
                instance.isNeedPushLosed = true;
            }
            dataGetPushPayloadStatus = instance;
        }
        return dataGetPushPayloadStatus;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public void notifyPushLoseIndexChanged(boolean fromGetPush) {
        EventBus.getDefault().post(PayloadNeedReloadEvent.ConectChanged);
    }

    public int getConnectionStatus() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean isConnected() {
        return isConnected(-1);
    }

    public boolean isConnected(int index) {
        return ((Integer) get(0, 1, Integer.class, index)).intValue() == 1;
    }

    public String getPayloadName() {
        return getUTF8(1, 32);
    }

    public boolean hasSecondaryRecData() {
        return (this.recDatas == null || this.recDatas.get(2) == null) ? false : true;
    }

    public boolean hasPrimaryRecData() {
        return (this.recDatas == null || this.recDatas.get(0) == null) ? false : true;
    }

    public void clear() {
        super.clear();
        DJILog.d(TAG, "clear data in DataGetPushPayloadStatus : recDatas" + this.recDatas, new Object[0]);
    }
}
