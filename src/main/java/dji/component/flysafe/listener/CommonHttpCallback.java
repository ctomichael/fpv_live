package dji.component.flysafe.listener;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface CommonHttpCallback {
    public static final int ERROR_UNKNOW = -1;

    void onFailure(int i, String str);

    void onSuccess();
}
