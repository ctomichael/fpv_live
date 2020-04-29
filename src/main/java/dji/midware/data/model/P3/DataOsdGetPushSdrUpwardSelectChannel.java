package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushSdrUpwardSelectChannel extends DataBase {
    private static DataOsdGetPushSdrUpwardSelectChannel instance = null;
    private int mSelectChannelCnt = 0;

    public static synchronized DataOsdGetPushSdrUpwardSelectChannel getInstance() {
        DataOsdGetPushSdrUpwardSelectChannel dataOsdGetPushSdrUpwardSelectChannel;
        synchronized (DataOsdGetPushSdrUpwardSelectChannel.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSdrUpwardSelectChannel();
            }
            dataOsdGetPushSdrUpwardSelectChannel = instance;
        }
        return dataOsdGetPushSdrUpwardSelectChannel;
    }

    public float getSelectChannelType() {
        return ((Float) get(0, 4, Float.class)).floatValue();
    }

    public int getSelectChannelCount() {
        this.mSelectChannelCnt = ((Integer) get(4, 4, Integer.class)).intValue();
        return this.mSelectChannelCnt;
    }

    public float[] getSelectChannelList() {
        float[] resultList = null;
        if (this._recData != null) {
            if (this.mSelectChannelCnt == 0) {
                this.mSelectChannelCnt = ((Integer) get(4, 4, Integer.class)).intValue();
            }
            if (this.mSelectChannelCnt != 0) {
                resultList = new float[this.mSelectChannelCnt];
                for (int i = 0; i < this.mSelectChannelCnt; i++) {
                    resultList[i] = ((Float) get((i * 4) + 8, 4, Float.class)).floatValue();
                }
            }
        }
        return resultList;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
