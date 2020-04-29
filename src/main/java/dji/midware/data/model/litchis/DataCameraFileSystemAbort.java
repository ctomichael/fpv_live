package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.litchis.FileRecvPack;

@EXClassNullAway
public class DataCameraFileSystemAbort extends DataBase {
    private static DataCameraFileSystemAbort instance = null;
    private FileRecvPack recvPack;

    public static synchronized DataCameraFileSystemAbort getInstance() {
        DataCameraFileSystemAbort dataCameraFileSystemAbort;
        synchronized (DataCameraFileSystemAbort.class) {
            if (instance == null) {
                instance = new DataCameraFileSystemAbort();
            }
            dataCameraFileSystemAbort = instance;
        }
        return dataCameraFileSystemAbort;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public FileRecvPack getRecvPack() {
        return this.recvPack;
    }

    public void setRecvPack(FileRecvPack recvPack2) {
        this.recvPack = recvPack2;
        setPushRecData(recvPack2.buffer);
    }
}
