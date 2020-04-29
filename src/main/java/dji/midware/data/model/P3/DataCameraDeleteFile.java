package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataCameraDeleteFile extends DataBase implements DJIDataSyncListener {
    private static DataCameraDeleteFile instance = null;
    private ArrayList<Integer> indexs;
    private int num;
    private DataCameraSetStorageInfo.Storage storage = DataCameraSetStorageInfo.Storage.SDCARD;

    public static synchronized DataCameraDeleteFile getInstance() {
        DataCameraDeleteFile dataCameraDeleteFile;
        synchronized (DataCameraDeleteFile.class) {
            if (instance == null) {
                instance = new DataCameraDeleteFile();
            }
            dataCameraDeleteFile = instance;
        }
        return dataCameraDeleteFile;
    }

    public DataCameraDeleteFile setNum(int num2) {
        this.num = num2;
        return this;
    }

    public DataCameraDeleteFile setIndexs(ArrayList<Integer> indexs2) {
        this.indexs = indexs2;
        return this;
    }

    public DataCameraDeleteFile setStorageLoaction(DataCameraSetStorageInfo.Storage storage2) {
        this.storage = storage2;
        return this;
    }

    public int getNum() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public ArrayList<Integer> getIndexs() {
        int num2 = ((Integer) get(0, 1, Integer.class)).intValue();
        if (num2 <= 0) {
            return null;
        }
        ArrayList<Integer> indexs2 = new ArrayList<>(num2);
        for (int i = 0; i < num2; i++) {
            indexs2.add(get((i * 4) + 1, 4, Integer.class));
        }
        return indexs2;
    }

    public ArrayList<Integer> getFailNum() {
        int num2 = ((Integer) get(0, 1, Integer.class)).intValue();
        if (num2 <= 0) {
            return null;
        }
        ArrayList<Integer> indexs2 = new ArrayList<>(num2);
        for (int i = 0; i < num2; i++) {
            indexs2.add(get((i * 4) + 1, 4, Integer.class));
        }
        return indexs2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.num > 0) {
            this._sendData = new byte[((this.num * 4) + 1)];
            this._sendData[0] = (byte) this.num;
            for (int i = 0; i < this.indexs.size(); i++) {
                System.arraycopy(BytesUtil.getBytes(this.indexs.get(i).intValue()), 0, this._sendData, (i * 4) + 1, 4);
            }
            return;
        }
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM245) {
            this._sendData = new byte[2];
            this._sendData[0] = (byte) this.num;
            this._sendData[1] = (byte) this.storage.value();
            return;
        }
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.num;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.DeleteFile.value();
        pack.timeOut = 4000;
        start(pack, callBack);
    }
}
