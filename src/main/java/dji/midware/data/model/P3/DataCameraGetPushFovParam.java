package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.model.base.DJICameraDataBase;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataCameraGetPushFovParam extends DJICameraDataBase {
    private static DataCameraGetPushFovParam instance = null;
    private float mFovH = 0.0f;
    private float mFovV = 0.0f;

    public static synchronized DataCameraGetPushFovParam getInstance() {
        DataCameraGetPushFovParam dataCameraGetPushFovParam;
        synchronized (DataCameraGetPushFovParam.class) {
            if (instance == null) {
                instance = new DataCameraGetPushFovParam();
            }
            dataCameraGetPushFovParam = instance;
        }
        return dataCameraGetPushFovParam;
    }

    public void setRecData(byte[] data) {
        if (!Arrays.equals(this._recData, data)) {
            super.setRecData(data);
            if (hasFovData()) {
                this.mFovH = (float) Math.toDegrees(Math.atan2((double) (((float) getImageWidth()) * 0.5f), (double) getLensFocalLength()) * 2.0d);
                this.mFovV = (float) Math.toDegrees(Math.atan2((double) (((float) getImageHeight()) * 0.5f), (double) getLensFocalLength()) * 2.0d);
            }
            DJILogHelper.getInstance().LOGD(CameraKeys.COMPONENT_KEY, "Fovh-" + this.mFovH + ";" + this.mFovV, false, true);
        }
    }

    public boolean hasFovData() {
        return this._recData != null && this._recData.length > 0;
    }

    public float getFovH() {
        if (isGetted()) {
            return this.mFovH;
        }
        return 0.0f;
    }

    public float getFovV() {
        if (isGetted()) {
            return this.mFovV;
        }
        return 0.0f;
    }

    public int getImageWidth() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public int getImageHeight() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public int getImageRatio() {
        return ((Integer) get(8, 4, Integer.class)).intValue();
    }

    public int getLensFocalLength() {
        return ((Integer) get(12, 4, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
