package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.litchis.DataCameraFileSystemAbort;
import dji.midware.data.model.litchis.DataCameraFileSystemFileData;
import dji.midware.data.model.litchis.DataCameraFileSystemListInfo;
import dji.midware.data.model.litchis.DataCameraFileSystemPush;
import dji.midware.data.model.litchis.DataCameraFileSystemStreamData;
import dji.midware.data.packages.litchis.FileRecvPack;
import dji.midware.data.queue.P3.PackUtil;

@Keep
@EXClassNullAway
public class DataCameraGetPushFile extends DataBase {
    private static DataCameraGetPushFile instance = null;
    private long curNano;
    private long lastNano;

    public static synchronized DataCameraGetPushFile getInstance() {
        DataCameraGetPushFile dataCameraGetPushFile;
        synchronized (DataCameraGetPushFile.class) {
            if (instance == null) {
                instance = new DataCameraGetPushFile();
            }
            dataCameraGetPushFile = instance;
        }
        return dataCameraGetPushFile;
    }

    public void setPushRecData(byte[] data) {
        FileRecvPack fileRecvPack = new FileRecvPack(data);
        if (fileRecvPack.sessionId == PackUtil.sessionId()) {
            switch (DataConfig.CmdType.find(fileRecvPack.cmdType)) {
                case PUSH:
                    DataCameraFileSystemPush.getInstance().setRecvPack(fileRecvPack);
                    return;
                case ABORT:
                    DataCameraFileSystemAbort.getInstance().setRecvPack(fileRecvPack);
                    return;
                case DATA:
                    switch (DataConfig.CmdId.find(fileRecvPack.cmdId)) {
                        case List:
                            DataCameraFileSystemListInfo.getInstance().setRecvPack(fileRecvPack);
                            return;
                        case File:
                            DataCameraFileSystemFileData.getInstance().setRecvPack(fileRecvPack);
                            return;
                        case Stream:
                            DataCameraFileSystemStreamData.getInstance().setRecvPack(fileRecvPack);
                            return;
                        default:
                            return;
                    }
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
