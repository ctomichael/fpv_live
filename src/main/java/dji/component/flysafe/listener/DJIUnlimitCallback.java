package dji.component.flysafe.listener;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIUnlimitCallback {
    void onAlreadyVerify();

    void onFailed(String str);

    void onNotVerify();

    void onSuccess();
}
