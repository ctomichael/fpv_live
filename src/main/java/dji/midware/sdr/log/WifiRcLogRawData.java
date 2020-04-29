package dji.midware.sdr.log;

import android.support.annotation.NonNull;
import java.io.FileOutputStream;

public class WifiRcLogRawData {
    private byte[] mData = new byte[0];
    private FileOutputStream mFos = null;

    /* access modifiers changed from: package-private */
    public void setData(@NonNull FileOutputStream fos, byte[] data) {
        this.mFos = fos;
        this.mData = data;
    }

    /* access modifiers changed from: package-private */
    public byte[] getData() {
        return this.mData;
    }

    public FileOutputStream getFos() {
        return this.mFos;
    }
}
