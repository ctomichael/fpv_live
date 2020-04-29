package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.litchis.FileRecvPack;

@EXClassNullAway
public class DataCameraFileSystemStreamData extends DataBase {
    private static DataCameraFileSystemStreamData instance = null;
    private FileRecvPack recvPack;

    public static synchronized DataCameraFileSystemStreamData getInstance() {
        DataCameraFileSystemStreamData dataCameraFileSystemStreamData;
        synchronized (DataCameraFileSystemStreamData.class) {
            if (instance == null) {
                instance = new DataCameraFileSystemStreamData();
            }
            dataCameraFileSystemStreamData = instance;
        }
        return dataCameraFileSystemStreamData;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public FileRecvPack getRecvPack() {
        return this.recvPack;
    }

    public void setRecvPack(FileRecvPack recvPack2) {
        this.recvPack = recvPack2;
        setPushRecData(recvPack2.data);
    }

    public int getTimeScale() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public int getFrameDuration() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public long getStartTime() {
        return ((Long) get(8, 4, Long.class)).longValue();
    }

    public long getDuration() {
        return ((Long) get(12, 4, Long.class)).longValue();
    }

    public int getInfoLen() {
        return 32;
    }
}
