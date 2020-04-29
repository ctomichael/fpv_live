package dji.midware.transfer.base;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public interface FileTransferListener {
    void onFailure(FileTransferTask fileTransferTask, String str, Ccode ccode);

    void onProgress(int i, int i2);

    void onRateUpdate(float f);

    void onStart();

    void onSuccess(FileTransferTask fileTransferTask);
}
