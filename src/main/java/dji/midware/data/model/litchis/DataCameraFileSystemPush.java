package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.litchis.FileRecvPack;

@EXClassNullAway
public class DataCameraFileSystemPush extends DataBase {
    private static DataCameraFileSystemPush instance = null;
    private FileRecvPack recvPack;

    public static synchronized DataCameraFileSystemPush getInstance() {
        DataCameraFileSystemPush dataCameraFileSystemPush;
        synchronized (DataCameraFileSystemPush.class) {
            if (instance == null) {
                instance = new DataCameraFileSystemPush();
            }
            dataCameraFileSystemPush = instance;
        }
        return dataCameraFileSystemPush;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public FileRecvPack getRecvPack() {
        return this.recvPack;
    }

    public void setRecvPack(FileRecvPack recvPack2) {
        this.recvPack = recvPack2;
        setPushRecData(recvPack2.buffer);
    }
}
