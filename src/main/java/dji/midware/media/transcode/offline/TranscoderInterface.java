package dji.midware.media.transcode.offline;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface TranscoderInterface {
    int getCurProgress();

    String getInputFileName();

    boolean isTranscoding();

    void onDestroy();

    void rebindListener(TranscoderListener transcoderListener);

    void start(String str, String str2, TranscoderListener transcoderListener);

    void stop();
}
