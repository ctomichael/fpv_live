package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJILocationCallback {
    void onFailed();

    void onSuccess(String str, String str2);
}
