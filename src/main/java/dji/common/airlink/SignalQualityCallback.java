package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface SignalQualityCallback {
    void onUpdate(int i);
}
