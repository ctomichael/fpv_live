package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.manager.litchis.DJIFileType;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushRecordingName extends DataBase {
    private static DataCameraGetPushRecordingName instance = null;

    public static synchronized DataCameraGetPushRecordingName getInstance() {
        DataCameraGetPushRecordingName dataCameraGetPushRecordingName;
        synchronized (DataCameraGetPushRecordingName.class) {
            if (instance == null) {
                instance = new DataCameraGetPushRecordingName();
            }
            dataCameraGetPushRecordingName = instance;
        }
        return dataCameraGetPushRecordingName;
    }

    public DJIFileType getType() {
        return DJIFileType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getIndex() {
        return ((Integer) get(1, 4, Integer.class)).intValue();
    }

    public long getTime() {
        return ((Long) get(13, 4, Long.class)).longValue();
    }

    public int getFileType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public long getSize() {
        return ((Long) get(5, 8, Long.class)).longValue();
    }

    public int getSubIndex() {
        return ((Integer) get(19, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
