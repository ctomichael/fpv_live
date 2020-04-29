package dji.midware.reflect;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.record.RecorderBase;

@EXClassNullAway
public interface MidwareToVideoLibInjectable {
    String getDownloadDirectoryPath();

    String getVideoCacheDirectoryPath();

    void startRecordFlightAnalytics(RecorderBase recorderBase, String str);

    void stopRecordFlightAnalytics(String str);
}
