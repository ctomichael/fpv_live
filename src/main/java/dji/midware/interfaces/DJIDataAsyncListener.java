package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIDataAsyncListener {
    void start(long j);

    void stop();
}
