package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataFlycGetPushBoardRecv extends DataBase {
    private static DataFlycGetPushBoardRecv instance = null;

    public static synchronized DataFlycGetPushBoardRecv getInstance() {
        DataFlycGetPushBoardRecv dataFlycGetPushBoardRecv;
        synchronized (DataFlycGetPushBoardRecv.class) {
            if (instance == null) {
                instance = new DataFlycGetPushBoardRecv();
            }
            dataFlycGetPushBoardRecv = instance;
        }
        return dataFlycGetPushBoardRecv;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public byte[] getRecvData() {
        return this._recData;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
