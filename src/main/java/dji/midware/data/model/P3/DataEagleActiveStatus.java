package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Pair;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import java.util.HashMap;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DataEagleActiveStatus extends DataAbstractGetPushActiveStatus {
    private static DataEagleActiveStatus instance = null;
    public static HashMap<DeviceType, Pair<byte[], String>> mActivePushModuleInfo = new HashMap<>();
    public static HashMap<DeviceType, Boolean> mActiveStatus = new HashMap<>();
    public static String mActiveTimeForTest = "";
    public static boolean mIsDeviceActived = true;
    byte[] mData;
    private int mModuleNum = 0;
    byte[] mNonce;

    public static synchronized DataEagleActiveStatus getInstance() {
        DataEagleActiveStatus dataEagleActiveStatus;
        synchronized (DataEagleActiveStatus.class) {
            if (instance == null) {
                instance = new DataEagleActiveStatus();
            }
            dataEagleActiveStatus = instance;
        }
        return dataEagleActiveStatus;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        if (getType() == DataAbstractGetPushActiveStatus.TYPE.EaglePush) {
            byte[] eagleData = getEaglePushData();
            mActivePushModuleInfo.put(getPushDeviceType(), new Pair<>(eagleData, getPushSN()));
        } else if (getType() == DataAbstractGetPushActiveStatus.TYPE.EagleModuleNum) {
            this.mModuleNum = ((Integer) get(21, 1, Integer.class)).intValue();
        }
        EventBus.getDefault().post(this);
    }

    public void setRecData(byte[] data) {
        boolean z;
        boolean isActive;
        super.setRecData(data);
        if (this.type == DataAbstractGetPushActiveStatus.TYPE.EagleGet) {
            this.timeOffset = 10;
            if ((((Integer) get(0, 1, Integer.class)).intValue() & 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            mIsDeviceActived = z;
            mActiveTimeForTest = getYear() + "-" + getMonth() + "-" + getDay() + "-" + getHour() + "-" + getMin() + "-" + getSecond();
            if (data != null && data[0] != 0 && data.length >= 59) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if ((data[51 + i] >> j) % 2 == 1) {
                            isActive = true;
                        } else {
                            isActive = false;
                        }
                        mActiveStatus.put(DeviceType.find((i * 8) + j), Boolean.valueOf(isActive));
                    }
                }
            }
        }
    }

    public void clear() {
        mActiveStatus.clear();
        mActivePushModuleInfo.clear();
        mIsDeviceActived = true;
        this.mModuleNum = 0;
    }

    public DataAbstractGetPushActiveStatus.TYPE getType() {
        return DataAbstractGetPushActiveStatus.TYPE.findV3(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int getModuleNum() {
        return this.mModuleNum;
    }

    public byte[] getNonce() {
        byte[] res = new byte[16];
        if (this._recData != null && this._recData.length > 21) {
            System.arraycopy(this._recData, 5, res, 0, 16);
        }
        return res;
    }

    public void setNonce(byte[] nonce) {
        this.mNonce = nonce;
    }

    public DeviceType getPushDeviceType() {
        return DeviceType.find(((Integer) get(38, 1, Integer.class)).intValue());
    }

    public void setData(byte[] data) {
        this.mData = data;
    }

    public byte[] getEaglePushData() {
        return this._recData;
    }

    public String getPushSN() {
        return getRevertSn(41, ((Integer) get(40, 1, Integer.class)).intValue());
    }

    public static DeviceType getActiveDeviceType(byte[] data) {
        if (data == null || data.length < 39) {
            return DeviceType.WHO;
        }
        return DeviceType.find(data[38]);
    }

    public static String getActiveDeviceSn(byte[] data) {
        if (data == null || data.length < 39) {
            return "";
        }
        byte b = data[39];
        if (data.length < 39 + b) {
            return "";
        }
        return getRevertSn(data, 40, b);
    }

    public boolean getActiveStatus() {
        return mIsDeviceActived;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.type == DataAbstractGetPushActiveStatus.TYPE.EagleUserInfo) {
            this._sendData = new byte[135];
            this._sendData[0] = (byte) this.type.value();
            System.arraycopy(BytesUtil.getBytes(this.type.value()), 0, this._sendData, 1, 4);
            if (this.mNonce != null) {
                System.arraycopy(this.mNonce, 0, this._sendData, 38, this.mNonce.length);
            }
        } else if (this.type == DataAbstractGetPushActiveStatus.TYPE.EagleSet) {
            this._sendData = this.mData;
        } else {
            this._sendData = new byte[5];
            this._sendData[0] = (byte) this.type.value();
            this._sendData[1] = (byte) this.type.value();
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (DJIProductManager.getInstance().getType() == ProductType.WM160) {
            pack.receiverType = DeviceType.FLYC.value();
        } else {
            pack.receiverType = DeviceType.DM368.value();
            pack.receiverId = 4;
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.ActiveStatus.value();
        pack.timeOut = 5000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
