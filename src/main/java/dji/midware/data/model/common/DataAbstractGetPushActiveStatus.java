package dji.midware.data.model.common;

import com.secneo.ProtectMeVmpMethod;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.base.DJICommonDataBase;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import dji.publics.utils.LanguageUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DataAbstractGetPushActiveStatus extends DJICommonDataBase implements DJIDataSyncListener {
    protected String TAG = getClass().getSimpleName();
    protected String ac = LanguageUtils.CN_DJI_LANG_CODE;
    protected int activeStatus;
    protected int day;
    protected String getSN = "";
    protected int hour;
    protected int min;
    protected int month;
    protected String pushSN = "";
    protected int second;
    protected String sn;
    protected int snLen = 10;
    protected int timeOffset = 0;
    protected TYPE type;
    protected DJIActiveVersion version = DJIActiveVersion.Ver1_0;
    protected int year;

    public boolean isGetted() {
        return this._recData != null && this._recData.length >= 11;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        if (data != null && data.length >= 11 && this.pack.cmdType != 1 && data.length <= 18) {
            setRecData(data);
            DJILogHelper.getInstance().LOGD(this.TAG, "** senderType=" + this.pack.senderType + " len=" + data.length + " " + BytesUtil.byte2hex(data), false, false);
            int pushType = ((Integer) get(0, 1, Integer.class)).intValue();
            if (pushType == TYPE.PUSH.value(DJIActiveVersion.Ver1_0)) {
                this.version = DJIActiveVersion.Ver1_0;
                if (DeviceType.BATTERY._equals(this.pack.receiverType)) {
                    this.pushSN = getInvertSn(1, 10);
                } else {
                    this.pushSN = getRevertSn(1, 10);
                }
            } else if (pushType == TYPE.PUSH.value(DJIActiveVersion.Ver1_1)) {
                this.version = DJIActiveVersion.Ver1_1;
                this.snLen = ((Integer) get(1, 1, Integer.class)).intValue();
                DJILogHelper.getInstance().LOGD(this.TAG, getClass().getSimpleName() + " snLen=" + this.snLen, false, true);
                if (this.snLen > 16) {
                    this.pushSN = "";
                } else {
                    this.pushSN = getRevertSn(2, this.snLen);
                }
                DJILogHelper.getInstance().LOGD(this.TAG, getClass().getSimpleName() + " pushSN=" + this.pushSN, false, true);
            }
            EventBus.getDefault().post(this);
        }
    }

    public void clear() {
        super.clear();
        this.pushSN = "";
    }

    public DataAbstractGetPushActiveStatus setVersion(DJIActiveVersion ver) {
        this.version = ver;
        return this;
    }

    public DJIActiveVersion getVersion() {
        return this.version;
    }

    public String getPushSN() {
        if (this.pushSN.equals("")) {
            this.pushSN = getSN();
        }
        return this.pushSN;
    }

    public boolean isPushSnAvailable() {
        for (int i = 1; i < this.snLen + 1; i++) {
            if (this._recData != null && this._recData.length > i && this._recData[i] != 0) {
                return true;
            }
        }
        return false;
    }

    @ProtectMeVmpMethod
    public boolean getActiveStatus() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getYear() {
        return ((Integer) get(this.timeOffset + 1, 2, Integer.class)).intValue();
    }

    public int getMonth() {
        return ((Integer) get(this.timeOffset + 3, 1, Integer.class)).intValue();
    }

    public int getDay() {
        return ((Integer) get(this.timeOffset + 4, 1, Integer.class)).intValue();
    }

    public int getHour() {
        return ((Integer) get(this.timeOffset + 5, 1, Integer.class)).intValue();
    }

    public int getMin() {
        return ((Integer) get(this.timeOffset + 6, 1, Integer.class)).intValue();
    }

    public int getSecond() {
        return ((Integer) get(this.timeOffset + 7, 1, Integer.class)).intValue();
    }

    public long getTime() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.ENGLISH).parse(getYear() + "-" + getMonth() + "-" + getDay() + "-" + getHour() + "-" + getMin() + "-" + getSecond()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getSN() {
        if (this.version != DJIActiveVersion.Ver1_0) {
            int snLen2 = ((Integer) get(8, 1, Integer.class)).intValue();
            if (snLen2 > 16) {
                this.getSN = "";
            } else {
                this.getSN = getRevertSn(9, snLen2);
            }
        } else if (this.pack == null || !DeviceType.BATTERY._equals(this.pack.receiverType)) {
            this.getSN = getRevertSn(8, 10);
        } else {
            this.getSN = "";
            if (this._recData == null || this._recData.length < 18) {
                return "";
            }
            this.getSN = getInvertSn(8, 10);
        }
        return this.getSN;
    }

    /* access modifiers changed from: protected */
    public String getInvertSn(int from, int len) {
        StringBuilder sb = new StringBuilder(len);
        if (this._recData != null && from + len <= this._recData.length) {
            int i = (from + len) - 1;
            while (i >= from && 255 != this._recData[i]) {
                if (BytesUtil.isNumberOrLetter(this._recData[i])) {
                    sb.append((char) this._recData[i]);
                }
                i--;
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getRevertSn(int from, int len) {
        StringBuilder sb = new StringBuilder(len);
        if (this._recData != null && from + len <= this._recData.length) {
            int i = from;
            while (i < from + len && 255 != this._recData[i]) {
                if (BytesUtil.isNumberOrLetter(this._recData[i])) {
                    sb.append((char) this._recData[i]);
                }
                i++;
            }
        }
        return sb.toString();
    }

    protected static String getRevertSn(byte[] data, int from, int len) {
        StringBuilder sb = new StringBuilder(len);
        if (data != null && from + len <= data.length) {
            int i = from;
            while (i < from + len && 255 != data[i]) {
                if (BytesUtil.isNumberOrLetter(data[i])) {
                    sb.append((char) data[i]);
                }
                i++;
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public String getInvertSnOld(int from, int len) {
        String sn2 = "";
        if (this._recData == null || from + len > this._recData.length) {
            return sn2;
        }
        int i = (from + len) - 1;
        while (i >= from && this._recData[i] != 255) {
            sn2 = sn2 + ((int) this._recData[i]);
            i--;
        }
        return sn2;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public String getRevertSnOld(int from, int len) {
        if (this._recData == null || from + len > this._recData.length) {
            return "";
        }
        return get(from, len);
    }

    public DataAbstractGetPushActiveStatus setAc(String ac2) {
        this.ac = ac2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setType(TYPE type2) {
        this.type = type2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setActiveStatus(boolean activeStatus2) {
        this.activeStatus = activeStatus2 ? 1 : 0;
        return this;
    }

    public DataAbstractGetPushActiveStatus setYear(int year2) {
        this.year = year2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setMonth(int month2) {
        this.month = month2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setDay(int day2) {
        this.day = day2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setHour(int hour2) {
        this.hour = hour2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setMin(int min2) {
        this.min = min2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setSecond(int second2) {
        this.second = second2;
        return this;
    }

    public DataAbstractGetPushActiveStatus setSN(String sn2) {
        this.sn = sn2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 10;
        if (this.type == TYPE.GET) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) this.type.value(this.version);
        } else if (this.type == TYPE.SET) {
            byte[] snbytes = BytesUtil.getBytes(this.sn);
            DJILogHelper.getInstance().LOGD("", "active set sn=" + this.sn, true, true);
            DJILogHelper.getInstance().LOGD("", "active set sn len=" + snbytes.length, true, true);
            if (this.version == DJIActiveVersion.Ver1_0) {
                this._sendData = new byte[19];
            } else {
                this._sendData = new byte[(snbytes.length + 10)];
                DJILogHelper.getInstance().LOGD("", "active set sn2 len=" + this._sendData.length, true, true);
            }
            this._sendData[0] = (byte) this.type.value(this.version);
            this._sendData[1] = (byte) this.activeStatus;
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.year), this._sendData, 2);
            this._sendData[4] = (byte) this.month;
            this._sendData[5] = (byte) this.day;
            this._sendData[6] = (byte) this.hour;
            this._sendData[7] = (byte) this.min;
            this._sendData[8] = (byte) this.second;
            if (this.version != DJIActiveVersion.Ver1_0) {
                int len = snbytes.length < 16 ? snbytes.length : 16;
                DJILogHelper.getInstance().LOGD("", "active set len=" + len, false, true);
                this._sendData[9] = (byte) len;
                if (snbytes.length > 0) {
                    System.arraycopy(snbytes, 0, this._sendData, 10, len);
                }
            } else if (snbytes.length > 0) {
                byte[] bArr = this._sendData;
                if (snbytes.length < 10) {
                    i = snbytes.length;
                }
                System.arraycopy(snbytes, 0, bArr, 9, i);
            }
        } else if (this.type == TYPE.FAIL) {
            byte[] snbytes2 = BytesUtil.getBytes(this.sn);
            if (this.version == DJIActiveVersion.Ver1_0) {
                this._sendData = new byte[11];
                this._sendData[0] = (byte) this.type.value(this.version);
                System.arraycopy(snbytes2, 0, this._sendData, 1, 10);
                return;
            }
            this._sendData = new byte[(snbytes2.length + 2)];
            this._sendData[0] = (byte) this.type.value(this.version);
            this._sendData[1] = (byte) snbytes2.length;
            System.arraycopy(snbytes2, 0, this._sendData, 2, snbytes2.length);
        }
    }

    public void start(DJIDataCallBack callBack) {
    }

    public enum DJIActiveVersion {
        Ver1_0(0),
        Ver1_1(1),
        Ver1_2(2),
        Ver1_3(3),
        OTHER(100);
        
        private int data;

        private DJIActiveVersion(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIActiveVersion find(int b) {
            DJIActiveVersion result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public TYPE getType() {
        return TYPE.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public enum TYPE {
        GetVer(0),
        GET(1, 17, 33),
        SET(2, 18, 34),
        PUSH(3, 19, 35),
        FAIL(4, 20, 36),
        EagleGet(49),
        EagleModuleNum(51),
        EagleUserInfo(53),
        EaglePush(54),
        EagleSet(55),
        EagleRestart(56),
        OTHER(100);
        
        private int data;
        private int data1;
        private int data2;

        private TYPE(int data3) {
            this.data = data3;
        }

        private TYPE(int data3, int data12) {
            this.data = data3;
            this.data1 = data12;
        }

        private TYPE(int data3, int data12, int data22) {
            this.data = data3;
            this.data1 = data12;
            this.data2 = data22;
        }

        public int value() {
            return this.data;
        }

        public int value(DJIActiveVersion version) {
            int relValue = this.data;
            if (this.data1 == 0) {
                return this.data;
            }
            switch (AnonymousClass1.$SwitchMap$dji$midware$data$model$common$DataAbstractGetPushActiveStatus$DJIActiveVersion[version.ordinal()]) {
                case 1:
                    relValue = this.data;
                    break;
                case 2:
                    relValue = this.data1;
                    break;
                case 3:
                    relValue = this.data2;
                    break;
                case 4:
                    relValue = this.data;
                    break;
            }
            return relValue;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TYPE find(int b) {
            TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static TYPE findV3(int b) {
            TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i].data == b) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    /* renamed from: dji.midware.data.model.common.DataAbstractGetPushActiveStatus$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$dji$midware$data$model$common$DataAbstractGetPushActiveStatus$DJIActiveVersion = new int[DJIActiveVersion.values().length];

        static {
            try {
                $SwitchMap$dji$midware$data$model$common$DataAbstractGetPushActiveStatus$DJIActiveVersion[DJIActiveVersion.Ver1_0.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$dji$midware$data$model$common$DataAbstractGetPushActiveStatus$DJIActiveVersion[DJIActiveVersion.Ver1_1.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$dji$midware$data$model$common$DataAbstractGetPushActiveStatus$DJIActiveVersion[DJIActiveVersion.Ver1_2.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$dji$midware$data$model$common$DataAbstractGetPushActiveStatus$DJIActiveVersion[DJIActiveVersion.Ver1_3.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }
}
