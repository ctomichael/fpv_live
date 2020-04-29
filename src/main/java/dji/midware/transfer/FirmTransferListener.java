package dji.midware.transfer;

import dji.midware.data.config.P3.Ccode;
import java.io.File;

public interface FirmTransferListener {
    void initFirmware(int i, File file);

    void onUploadComplete(int i, int i2, float f, float f2);

    void onUploadFailed(String str, Ccode ccode);

    void onUploadProgress(int i);

    void onUploadRate(float f);

    void onUploadStart(int i, int i2);

    void onUploadSuccess(int i);
}
