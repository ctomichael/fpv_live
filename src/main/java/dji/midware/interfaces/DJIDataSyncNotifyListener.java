package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIDataSyncNotifyListener {
    void start(DJIDataCallBack dJIDataCallBack, boolean z);
}
