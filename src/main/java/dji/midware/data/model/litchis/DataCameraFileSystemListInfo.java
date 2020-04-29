package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.litchis.FileRecvPack;

@EXClassNullAway
public class DataCameraFileSystemListInfo extends DataBase {
    private static DataCameraFileSystemListInfo instance = null;
    private FileRecvPack recvPack;

    public static synchronized DataCameraFileSystemListInfo getInstance() {
        DataCameraFileSystemListInfo dataCameraFileSystemListInfo;
        synchronized (DataCameraFileSystemListInfo.class) {
            if (instance == null) {
                instance = new DataCameraFileSystemListInfo();
            }
            dataCameraFileSystemListInfo = instance;
        }
        return dataCameraFileSystemListInfo;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getFileCount() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public int getDataLength() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public FileRecvPack getRecvPack() {
        return this.recvPack;
    }

    public void setRecvPack(FileRecvPack recvPack2) {
        this.recvPack = recvPack2;
        setPushRecData(recvPack2.data);
    }
}
