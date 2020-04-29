package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public interface DJIDataCallBack {
    void onFailure(Ccode ccode);

    void onSuccess(Object obj);
}
