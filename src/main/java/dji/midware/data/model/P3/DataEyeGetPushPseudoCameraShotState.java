package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.midware.data.model.base.DJICommonDataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushPseudoCameraShotState extends DJICommonDataBase {
    private static DataEyeGetPushPseudoCameraShotState instance = null;

    public static synchronized DataEyeGetPushPseudoCameraShotState getInstance() {
        DataEyeGetPushPseudoCameraShotState dataEyeGetPushPseudoCameraShotState;
        synchronized (DataEyeGetPushPseudoCameraShotState.class) {
            if (instance == null) {
                instance = new DataEyeGetPushPseudoCameraShotState();
            }
            dataEyeGetPushPseudoCameraShotState = instance;
        }
        return dataEyeGetPushPseudoCameraShotState;
    }

    public DataEyeSetPseudoCameraControl.PseudoCameraCmdResult getPseudoCameraCmdResult() {
        return DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }
}
