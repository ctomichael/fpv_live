package dji.midware.media.transcode.offline;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface TranscoderListener {
    void onFailure(Exception exc);

    void onProgress(int i);

    void onStart();

    void onSuccess();
}
