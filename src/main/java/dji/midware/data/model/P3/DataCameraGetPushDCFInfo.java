package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushDCFInfo extends DJICameraDataBase {
    private static DataCameraGetPushDCFInfo instance = null;

    public static synchronized DataCameraGetPushDCFInfo getInstance() {
        DataCameraGetPushDCFInfo dataCameraGetPushDCFInfo;
        synchronized (DataCameraGetPushDCFInfo.class) {
            if (instance == null) {
                instance = new DataCameraGetPushDCFInfo();
            }
            dataCameraGetPushDCFInfo = instance;
        }
        return dataCameraGetPushDCFInfo;
    }

    public String getFileListMD5() {
        String md5 = get(0, this._recData.length - 1);
        Log.e(DataCameraGetPushDCFInfo.class.getName(), "FileListMD5: " + md5);
        return md5;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
