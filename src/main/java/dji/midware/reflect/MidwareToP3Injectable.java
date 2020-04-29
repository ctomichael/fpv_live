package dji.midware.reflect;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface MidwareToP3Injectable {
    void endRecordInfo();

    boolean isDevelopPackage();

    boolean isInnerPackage();

    void startRecordInfo();
}
