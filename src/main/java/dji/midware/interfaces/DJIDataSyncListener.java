package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIDataSyncListener {
    void start(DJIDataCallBack dJIDataCallBack);
}
