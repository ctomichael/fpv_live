package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;

@Keep
public class DataEyeGetPushTimeLapseKeyFrame extends DataBase {
    private static DataEyeGetPushTimeLapseKeyFrame instance = null;

    public static synchronized DataEyeGetPushTimeLapseKeyFrame getInstance() {
        DataEyeGetPushTimeLapseKeyFrame dataEyeGetPushTimeLapseKeyFrame;
        synchronized (DataEyeGetPushTimeLapseKeyFrame.class) {
            if (instance == null) {
                instance = new DataEyeGetPushTimeLapseKeyFrame();
            }
            dataEyeGetPushTimeLapseKeyFrame = instance;
        }
        return dataEyeGetPushTimeLapseKeyFrame;
    }

    public int getCount() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public ArrayList<Long> getIds() {
        ArrayList<Long> arrayList = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            arrayList.add(get((i * 8) + 1, 8, Long.class));
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
