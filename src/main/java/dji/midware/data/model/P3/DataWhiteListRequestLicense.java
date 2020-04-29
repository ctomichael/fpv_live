package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdADS_B;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataWhiteListRequestLicense extends DataBase implements DJIDataSyncListener {
    private static final int DATA_START_POSITION = 36;
    private static final int DESCRIPTION_START_POSITION = 0;
    private static final int END_TIME_START_POSITION = 24;
    private static final int FACTOR = 1000000;
    private static final int HEADER_LENGTH = 4;
    private static final int LEVEL_START_POSITION = 31;
    private static final int LICENSE_ID_START_POSITION = 32;
    private static final int START_TIME_START_POISITION = 20;
    private static final int TYPE_START_POSITION = 30;
    private static DataWhiteListRequestLicense instance;
    private int requestId = -1;

    public static synchronized DataWhiteListRequestLicense getInstance() {
        DataWhiteListRequestLicense dataWhiteListRequestLicense;
        synchronized (DataWhiteListRequestLicense.class) {
            if (instance == null) {
                instance = new DataWhiteListRequestLicense();
            }
            dataWhiteListRequestLicense = instance;
        }
        return dataWhiteListRequestLicense;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getTotalNumberOfLicense() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public byte[] getLicenseBytes() {
        int size = this._recData.length;
        byte[] license = new byte[(size - 4)];
        System.arraycopy(this._recData, 4, license, 0, size - 4);
        return license;
    }

    public DataWhiteListRequestLicense setRequestId(int requestId2) {
        this.requestId = requestId2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.requestId;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.RequestLicense.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    public boolean isEnable() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public boolean isValid() {
        return ((Integer) get(3, 1, Integer.class)).intValue() == 0;
    }

    public byte[] getDescription() {
        byte[] description = new byte[20];
        System.arraycopy(getLicenseBytes(), 0, description, 0, 20);
        return description;
    }

    public byte[] getStartTime() {
        byte[] startTime = new byte[4];
        System.arraycopy(getLicenseBytes(), 20, startTime, 0, 4);
        return startTime;
    }

    public byte[] getEndTime() {
        byte[] endTime = new byte[4];
        System.arraycopy(getLicenseBytes(), 24, endTime, 0, 4);
        return endTime;
    }

    public int getType() {
        return ((Integer) get(34, 1, Integer.class)).intValue();
    }

    public byte[] getData() {
        byte[] data = new byte[40];
        System.arraycopy(getLicenseBytes(), 36, data, 0, 40);
        return data;
    }

    public int getLevel() {
        return ((Integer) get(35, 1, Integer.class)).intValue();
    }

    public int getLicenseId() {
        return ((Integer) get(36, 4, Integer.class)).intValue();
    }

    public List<Integer> getGEOAreaIds() {
        List<Integer> areaIds = new ArrayList<>();
        byte[] idBytes = new byte[4];
        byte[] data = getData();
        for (int i = 0; i < 10; i++) {
            System.arraycopy(data, i * 4, idBytes, 0, 4);
            int curId = BytesUtil.getInt(idBytes);
            if (curId == 0) {
                break;
            }
            areaIds.add(Integer.valueOf(curId));
        }
        return areaIds;
    }

    public float getCircleUnlockAreaLatitude() {
        return (((float) ((Integer) get(40, 4, Integer.class)).intValue()) * 1.0f) / 1000000.0f;
    }

    public float getCircleUnlockAreaLongitude() {
        return (((float) ((Integer) get(44, 4, Integer.class)).intValue()) * 1.0f) / 1000000.0f;
    }

    public int getCircleUnlockAreaRadius() {
        return ((Integer) get(48, 4, Integer.class)).intValue();
    }

    public int getCircleUnlockAreaLimitedHeight() {
        return ((Integer) get(52, 4, Integer.class)).intValue();
    }

    public int getCountryUnlockId() {
        return ((Integer) get(40, 2, Integer.class)).intValue();
    }

    public int getParameterConfigurationType() {
        return ((Integer) get(40, 1, Integer.class)).intValue();
    }

    public int[] getParameterConfigurationValues() {
        int[] values = new int[4];
        for (int i = 0; i < 4; i++) {
            values[i] = ((Integer) get(i + 41, 1, Integer.class)).intValue();
        }
        return values;
    }

    public float[][] getPentagonUnlockAreaPoints() {
        float[][] points = new float[5][];
        for (int i = 0; i < 5; i++) {
            points[i] = new float[2];
            for (int j = 0; j < 2; j++) {
                points[i][j] = (((float) ((Integer) get(((i * 8) + 40) + (j * 4), 4, Integer.class)).intValue()) * 1.0f) / 1000000.0f;
            }
        }
        return points;
    }
}
