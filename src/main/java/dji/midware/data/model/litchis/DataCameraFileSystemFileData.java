package dji.midware.data.model.litchis;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.litchis.FileRecvPack;
import java.io.File;
import java.io.FileOutputStream;

@EXClassNullAway
public class DataCameraFileSystemFileData extends DataBase {
    private static DataCameraFileSystemFileData instance = null;
    private File file = new File("/sdcard/testwrite.bin");
    private int i = 0;
    private boolean isDebug = false;
    private long nanos;
    private FileOutputStream outputStream;
    private FileRecvPack recvPack;

    public static synchronized DataCameraFileSystemFileData getInstance() {
        DataCameraFileSystemFileData dataCameraFileSystemFileData;
        synchronized (DataCameraFileSystemFileData.class) {
            if (instance == null) {
                instance = new DataCameraFileSystemFileData();
            }
            dataCameraFileSystemFileData = instance;
        }
        return dataCameraFileSystemFileData;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public DataCameraFileSystemFileData setTime(long nanos2) {
        this.nanos = nanos2;
        return this;
    }

    public long getTime() {
        return this.nanos;
    }

    public FileRecvPack getRecvPack() {
        return this.recvPack;
    }

    public void setRecvPack(FileRecvPack recvPack2) {
        this.recvPack = recvPack2;
        setPushRecData(recvPack2.data);
        if (this.isDebug) {
            try {
                if (this.outputStream == null) {
                    this.outputStream = new FileOutputStream(this.file);
                }
                if (this.i % 20 == 0) {
                    this.outputStream.write(recvPack2.data);
                }
                this.i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getOffset() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public int getSize() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(8, 4, Integer.class)).intValue();
    }

    public int getPathLen() {
        return ((Integer) get(12, 1, Integer.class)).intValue();
    }

    public String getPath() {
        return get(13, getPathLen());
    }

    public int getInfoLen() {
        if (this.recvPack.seq == 0) {
            return getPathLen() + 13;
        }
        return 0;
    }

    public int getMsgLen() {
        return this.recvPack.dataLen - getInfoLen();
    }
}
