package com.dji.scan.qr;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface RotationCallback {
    void onRotationChanged(int i);
}
