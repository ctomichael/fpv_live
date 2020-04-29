package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DataFlycDetection extends DataBase implements DJIDataSyncListener {
    private static final int CUSTOM_CONTENT_VERSION = 0;
    private static DataFlycDetection instance;
    private int appLat;
    private int appLng;
    private SubCmdId curCmd = null;
    private String customContent;
    private String flyPlan;
    private String license;
    private int personLat;
    private int personLng;
    private byte[][] receDataList = new byte[SubCmdId.values().length][];
    private boolean[] switchList = new boolean[Switch.values().length];
    String uuid;

    @Keep
    public enum Switch {
        Sn,
        GPS,
        HomeGPS,
        DroneID,
        FlyPlan,
        UUID,
        APPGPS,
        CustomContent
    }

    @Keep
    public enum SubCmdId {
        SetFlyPlan(1),
        GetFlyPlan(2),
        SetLicense(3),
        GetLicense(4),
        SetSwitch(5),
        GetSwitch(6),
        SetUUID(7),
        GetUUID(8),
        GetIsSetUUID(9),
        SetDJIAppFlag(10),
        GetAllUUID(11),
        SetAppLocation(13),
        GetDJIAppFlag(12),
        SetCustomContent(14),
        GetCustomContent(15),
        PushOsd(16),
        PushLicense(17),
        Other(255);
        
        int data;

        private SubCmdId(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SubCmdId find(int b) {
            SubCmdId[] items = values();
            SubCmdId result = Other;
            for (int i = 0; i < items.length; i++) {
                if (items[i]._equals(b)) {
                    return items[i];
                }
            }
            return result;
        }
    }

    public static synchronized DataFlycDetection getInstance() {
        DataFlycDetection dataFlycDetection;
        synchronized (DataFlycDetection.class) {
            if (instance == null) {
                instance = new DataFlycDetection(null);
            }
            dataFlycDetection = instance;
        }
        return dataFlycDetection;
    }

    public DataFlycDetection(SubCmdId cmd) {
        this.curCmd = cmd;
    }

    public DataFlycDetection() {
    }

    public DataFlycDetection setSubCmdId(SubCmdId cmd) {
        this.curCmd = cmd;
        return this;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        if (data != null && data.length > 1) {
            SubCmdId cmd = SubCmdId.find(data[0]);
            this.receDataList[cmd.ordinal()] = data;
            EventBus.getDefault().post(cmd);
        }
    }

    public void setFlyPlan(String str, double lat, double lng) {
        this.flyPlan = str;
        this.personLat = convertToInt(lat);
        this.personLng = convertToInt(lng);
        Log.d("flyPlan", "set lat : " + lat + ", lng : " + lng);
        this.curCmd = SubCmdId.SetFlyPlan;
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    public String getFlyPlan() {
        String contentStr;
        if (this._recData == null || this._recData.length <= 1) {
            return "";
        }
        byte b = this._recData[2];
        int length = b;
        if (b > this._recData.length - 3) {
            length = this._recData.length - 3;
        }
        if (length < 10) {
            return "";
        }
        if (this._recData[3] != 35 || this._recData[4] != 36) {
            return new String(this._recData, 3, length, Charset.forName("UTF-8"));
        }
        if (length == 10) {
            contentStr = "";
        } else {
            contentStr = new String(this._recData, 13, length - 10, Charset.forName("UTF-8"));
        }
        Log.d("flyPlan", "get lat : " + convertToDouble(((Integer) get(5, 4, Integer.class)).intValue()) + ", lng : " + convertToDouble(((Integer) get(9, 4, Integer.class)).intValue()));
        return contentStr;
    }

    public double convertToDouble(int value) {
        return ((((double) value) / 1.0E7d) * 180.0d) / 3.141592653589793d;
    }

    public int convertToInt(double value) {
        return (int) (((1.0E7d * value) * 3.141592653589793d) / 180.0d);
    }

    public boolean[] getEnable() {
        boolean z;
        int len = Switch.values().length;
        if (this._recData.length < 6) {
            return new boolean[len];
        }
        boolean[] list = new boolean[len];
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                if ((((Integer) get(2, 4, Integer.class)).intValue() & 1) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                list[i] = z;
            } else {
                list[i] = ((((Integer) get(2, 4, Integer.class)).intValue() >> i) & 1) == 1;
            }
        }
        return list;
    }

    public DataFlycDetection setEnable(boolean[] list) {
        this.switchList = list;
        return this;
    }

    public void setLicense(String license2) {
        this.license = license2;
        this.curCmd = SubCmdId.SetLicense;
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    public String getDroneId() {
        byte b;
        if (this._recData == null || this._recData.length <= 1 || (b = this._recData[2]) <= 0) {
            return "";
        }
        int length = b;
        if (b > this._recData.length - 3) {
            length = this._recData.length - 3;
        }
        return new String(this._recData, 3, length, Charset.forName("UTF-8"));
    }

    public DataFlycDetection setCustomContent(String content) {
        this.customContent = content;
        this.curCmd = SubCmdId.SetCustomContent;
        return this;
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    public String getCustomContent() {
        if (this._recData == null || this._recData.length <= 2) {
            return "";
        }
        byte b = this._recData[2];
        DJILog.d("CustomContent", "getCustomContent ：" + BytesUtil.byte2hex(this._recData), new Object[0]);
        if (b <= 0) {
            return "";
        }
        int length = b;
        if (b > this._recData.length - 3) {
            length = this._recData.length - 3;
        }
        return new String(this._recData, 4, length - 1, Charset.forName("UTF-8"));
    }

    public int getCustomContentVersion() {
        if (this._recData == null || this._recData.length <= 3) {
            return 0;
        }
        return this._recData[3];
    }

    public DataFlycDetection setAppLocation(double lat, double lng) {
        this.appLat = convertToInt(lat);
        this.appLng = convertToInt(lng);
        this.curCmd = SubCmdId.SetAppLocation;
        return this;
    }

    public double getLongitude() {
        return (((Double) get(22, 4, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public double getLatitude() {
        return (((Double) get(26, 4, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public int getRelativeHeight() {
        return ((Integer) get(30, 2, Integer.class)).intValue();
    }

    public int getAbsoluteHeight() {
        return ((Integer) get(32, 2, Integer.class)).intValue();
    }

    public int getXSpeed() {
        return ((Short) get(34, 2, Short.class)).shortValue();
    }

    public int getYSpeed() {
        return ((Short) get(36, 2, Short.class)).shortValue();
    }

    public int getZSpeed() {
        return ((Short) get(38, 2, Short.class)).shortValue();
    }

    public int getPitch() {
        return ((Short) get(40, 2, Short.class)).shortValue();
    }

    public int getRoll() {
        return ((Short) get(42, 2, Short.class)).shortValue();
    }

    public int getYaw() {
        return ((Short) get(44, 2, Short.class)).shortValue();
    }

    public double getHomeLongitude() {
        return (((Double) get(46, 4, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public double getHomeLatitude() {
        return (((Double) get(50, 4, Double.class)).doubleValue() * 180.0d) / 3.141592653589793d;
    }

    public DataOsdGetPushCommon.DroneType getDroneType() {
        return DataOsdGetPushCommon.DroneType.find(((Integer) get(54, 1, Integer.class)).intValue());
    }

    public String getUuid() {
        byte b;
        if (this._recData == null || this._recData.length <= 55 || (b = this._recData[55]) <= 0) {
            return "";
        }
        return new String(this._recData, 56, b, Charset.forName("UTF-8"));
    }

    public String getBoardSn() {
        return getBoardSn(6);
    }

    public String get2BoardSn() {
        return getBoardSn(1);
    }

    public String get2DroneId() {
        byte b;
        if (this._recData == null || this._recData.length <= 28 || (b = this._recData[17]) <= 0) {
            return "";
        }
        return new String(this._recData, 18, b, Charset.forName("UTF-8"));
    }

    public String get2License() {
        byte b;
        if (this._recData == null || this._recData.length <= 1 || (b = this._recData[28]) <= 0) {
            return "";
        }
        return new String(this._recData, 29, b, Charset.forName("UTF-8"));
    }

    public String getBoardSn(int start) {
        byte[] data = this._recData;
        String sn = null;
        if (data == null || data.length < start + 16) {
            return null;
        }
        int count = 0;
        int i = 0;
        while (i < 16) {
            try {
                if (data[i + start] == 0) {
                    break;
                }
                count++;
                i++;
            } catch (Exception e) {
            }
        }
        sn = BytesUtil.getStringUTF8(data, start, count);
        return sn;
    }

    public DataFlycDetection setUUID(String uuid2) {
        this.uuid = uuid2;
        return this;
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    public String getUUID() {
        if (this._recData == null || this._recData.length <= 2) {
            return "";
        }
        byte b = this._recData[2];
        int length = b;
        if (b > this._recData.length - 3) {
            length = this._recData.length - 3;
        }
        return new String(this._recData, 3, length, Charset.forName("UTF-8"));
    }

    public boolean getIsSetUUID() {
        return this._recData != null && this._recData.length > 2 && this._recData[2] == 1;
    }

    public boolean getDJIAppFlag() {
        return this._recData != null && this._recData.length > 2 && this._recData[2] == 1;
    }

    public String[] getAllUUID() {
        byte b;
        if (this._recData == null || this._recData.length <= 2 || (b = this._recData[2]) == 0) {
            return null;
        }
        String[] res = new String[b];
        int pos = 3;
        for (int i = 0; i < b; i++) {
            int year = ((Integer) get(pos, 2, Integer.class)).intValue();
            int pos2 = pos + 2;
            int month = ((Integer) get(pos2, 1, Integer.class)).intValue();
            int pos3 = pos2 + 1;
            int day = ((Integer) get(pos3, 1, Integer.class)).intValue();
            int pos4 = pos3 + 1;
            int hour = ((Integer) get(pos4, 1, Integer.class)).intValue();
            int pos5 = pos4 + 1;
            int min = ((Integer) get(pos5, 1, Integer.class)).intValue();
            int pos6 = pos5 + 1;
            int sec = ((Integer) get(pos6, 1, Integer.class)).intValue();
            int pos7 = pos6 + 1;
            int len = ((Integer) get(pos7, 1, Integer.class)).intValue();
            int pos8 = pos7 + 1;
            String u = new String(this._recData, pos8, len, Charset.forName("UTF-8"));
            pos = pos8 + len;
            res[i] = year + " " + month + " " + day + " " + hour + " " + min + " " + sec + "@" + u;
        }
        return res;
    }

    public Ccode getCcode() {
        byte[] data = this._recData;
        if (data == null || data.length < 2) {
            return Ccode.INVALID_PARAM;
        }
        return Ccode.find(data[1]);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.curCmd == SubCmdId.SetFlyPlan) {
            if (this.flyPlan == null || this.flyPlan.isEmpty()) {
                this._sendData = new byte[12];
                this._sendData[0] = (byte) SubCmdId.SetFlyPlan.value();
                this._sendData[1] = 10;
            } else {
                byte[] byteData = this.flyPlan.getBytes();
                this._sendData = new byte[(byteData.length + 12)];
                this._sendData[0] = (byte) SubCmdId.SetFlyPlan.value();
                this._sendData[1] = (byte) (byteData.length + 10);
                System.arraycopy(byteData, 0, this._sendData, 12, byteData.length);
            }
            this._sendData[2] = 35;
            this._sendData[3] = 36;
            this._sendData[4] = (byte) (this.personLat & 255);
            this._sendData[5] = (byte) ((this.personLat & 65280) >> 8);
            this._sendData[6] = (byte) ((this.personLat & 16711680) >> 16);
            this._sendData[7] = (byte) ((this.personLat & ViewCompat.MEASURED_STATE_MASK) >> 24);
            this._sendData[8] = (byte) (this.personLng & 255);
            this._sendData[9] = (byte) ((this.personLng & 65280) >> 8);
            this._sendData[10] = (byte) ((this.personLng & 16711680) >> 16);
            this._sendData[11] = (byte) ((this.personLng & ViewCompat.MEASURED_STATE_MASK) >> 24);
        } else if (this.curCmd == SubCmdId.GetFlyPlan) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetFlyPlan.value();
        } else if (this.curCmd == SubCmdId.SetLicense) {
            if (this.license == null || this.license.isEmpty()) {
                this._sendData = new byte[2];
                this._sendData[0] = (byte) SubCmdId.SetLicense.value();
                this._sendData[1] = 0;
                return;
            }
            byte[] byteData2 = this.license.getBytes();
            this._sendData = new byte[(byteData2.length + 2)];
            this._sendData[0] = (byte) SubCmdId.SetLicense.value();
            this._sendData[1] = (byte) byteData2.length;
            System.arraycopy(byteData2, 0, this._sendData, 2, byteData2.length);
        } else if (this.curCmd == SubCmdId.GetLicense) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetLicense.value();
        } else if (this.curCmd == SubCmdId.SetCustomContent) {
            if (this.customContent == null || this.customContent.isEmpty()) {
                this._sendData = new byte[2];
                this._sendData[0] = (byte) SubCmdId.SetCustomContent.value();
                this._sendData[1] = 0;
                return;
            }
            byte[] byteData3 = this.customContent.getBytes();
            this._sendData = new byte[(byteData3.length + 3)];
            this._sendData[0] = (byte) SubCmdId.SetCustomContent.value();
            this._sendData[1] = (byte) (byteData3.length + 1);
            this._sendData[2] = 0;
            System.arraycopy(byteData3, 0, this._sendData, 3, byteData3.length);
            DJILog.d("CustomContent", "SetCustomContent ：" + BytesUtil.byte2hex(this._sendData), new Object[0]);
        } else if (this.curCmd == SubCmdId.GetCustomContent) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetCustomContent.value();
        } else if (this.curCmd == SubCmdId.SetSwitch) {
            this._sendData = new byte[5];
            this._sendData[0] = (byte) SubCmdId.SetSwitch.value();
            int value = 0;
            for (int i = 0; i < this.switchList.length; i++) {
                if (this.switchList[i]) {
                    value |= (int) Math.pow(2.0d, (double) i);
                }
            }
            for (int i2 = 0; i2 < 4; i2++) {
                this._sendData[i2 + 1] = (byte) (value & 255);
                value >>= 8;
            }
        } else if (this.curCmd == SubCmdId.GetSwitch) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetSwitch.value();
        } else if (this.curCmd == SubCmdId.GetUUID) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetUUID.value();
        } else if (this.curCmd == SubCmdId.SetUUID) {
            byte[] uuidBytes = this.uuid.getBytes();
            int len = uuidBytes.length;
            this._sendData = new byte[(len + 9)];
            this._sendData[0] = (byte) SubCmdId.SetUUID.value();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(calendar.get(1)), this._sendData, 1);
            this._sendData[3] = (byte) (calendar.get(2) + 1);
            this._sendData[4] = (byte) calendar.get(5);
            this._sendData[5] = (byte) calendar.get(11);
            this._sendData[6] = (byte) calendar.get(12);
            this._sendData[7] = (byte) calendar.get(13);
            this._sendData[8] = (byte) len;
            BytesUtil.arraycopy(uuidBytes, this._sendData, 9);
        } else if (this.curCmd == SubCmdId.GetIsSetUUID) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetIsSetUUID.value();
        } else if (this.curCmd == SubCmdId.SetDJIAppFlag) {
            this._sendData = new byte[2];
            this._sendData[0] = (byte) SubCmdId.SetDJIAppFlag.value();
            this._sendData[1] = 1;
        } else if (this.curCmd == SubCmdId.GetAllUUID) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetAllUUID.value();
        } else if (this.curCmd == SubCmdId.SetAppLocation) {
            this._sendData = new byte[17];
            this._sendData[0] = (byte) SubCmdId.SetAppLocation.value();
            System.arraycopy(BytesUtil.getBytes(System.currentTimeMillis()), 0, this._sendData, 1, 8);
            System.arraycopy(BytesUtil.getBytes(this.appLat), 0, this._sendData, 9, 4);
            System.arraycopy(BytesUtil.getBytes(this.appLng), 0, this._sendData, 13, 4);
        } else if (this.curCmd == SubCmdId.GetDJIAppFlag) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) SubCmdId.GetDJIAppFlag.value();
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.Detection.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}
