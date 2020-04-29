package dji.sdksharedlib.listener;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIActionCallback {
    void onFails(DJIError dJIError);

    void onSuccess(Object obj);
}
