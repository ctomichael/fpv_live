package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycUploadWayPointMsgByIndex;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataFlycDownloadWayPointMsgByIndex extends DataBase implements DJIDataSyncListener {
    private static DataFlycDownloadWayPointMsgByIndex instance = null;
    private int index;

    public static synchronized DataFlycDownloadWayPointMsgByIndex getInstance() {
        DataFlycDownloadWayPointMsgByIndex dataFlycDownloadWayPointMsgByIndex;
        synchronized (DataFlycDownloadWayPointMsgByIndex.class) {
            if (instance == null) {
                instance = new DataFlycDownloadWayPointMsgByIndex();
            }
            dataFlycDownloadWayPointMsgByIndex = instance;
        }
        return dataFlycDownloadWayPointMsgByIndex;
    }

    public DataFlycDownloadWayPointMsgByIndex setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getIndex() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public double getLatitude() {
        return ((Double) get(2, 8, Double.class)).doubleValue();
    }

    public double getLongitude() {
        return ((Double) get(10, 8, Double.class)).doubleValue();
    }

    public float getAltitude() {
        return ((Float) get(18, 4, Float.class)).floatValue();
    }

    public float getDampingDis() {
        return ((Float) get(22, 4, Float.class)).floatValue();
    }

    public short getTgtYaw() {
        return ((Short) get(26, 2, Short.class)).shortValue();
    }

    public short getTgtPitch() {
        return ((Short) get(28, 2, Short.class)).shortValue();
    }

    public DataFlycUploadWayPointMsgByIndex.TURNMODE getTurnMode() {
        return DataFlycUploadWayPointMsgByIndex.TURNMODE.find(((Short) get(30, 1, Short.class)).shortValue());
    }

    public boolean hasSpeed() {
        return ((Integer) get(31, 1, Integer.class)).intValue() == 1;
    }

    public int getSpeed() {
        return ((Integer) get(32, 2, Integer.class)).intValue();
    }

    public int getCameraActionType() {
        return ((Integer) get(34, 1, Integer.class)).intValue();
    }

    public int getCameraActionInterval() {
        return ((Integer) get(35, 2, Integer.class)).intValue();
    }

    public boolean getHasAction() {
        return ((Integer) get(39, 1, Integer.class)).intValue() == 1;
    }

    public short getMaxReachTime() {
        return ((Short) get(40, 2, Short.class)).shortValue();
    }

    public int getRepeatNum() {
        int result = (((Integer) get(42, 1, Integer.class)).intValue() << 28) >> 28;
        return 0;
    }

    public int getActionNum() {
        return ((Integer) get(42, 1, Integer.class)).intValue() >> 4;
    }

    public ArrayList<DataFlycUploadWayPointMsgByIndex.ACTION> getAction() {
        ArrayList<DataFlycUploadWayPointMsgByIndex.ACTION> result = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            result.add(DataFlycUploadWayPointMsgByIndex.ACTION.find(((Integer) get(i + 43, 1, Integer.class)).intValue()));
        }
        return result;
    }

    public ArrayList<Integer> getParameter() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            result.add(Integer.valueOf(((Short) get((i * 2) + 59, 2, Short.class)).shortValue()));
        }
        return result;
    }

    @Nullable
    public byte[] getBodyData() {
        if (this._recData == null || this._recData.length < 1) {
            return null;
        }
        byte[] buffer = new byte[(this._recData.length - 1)];
        System.arraycopy(this._recData, 1, buffer, 0, buffer.length);
        return buffer;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.DownloadWayPointMsgByIndex.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = BytesUtil.getByte(this.index);
    }
}
