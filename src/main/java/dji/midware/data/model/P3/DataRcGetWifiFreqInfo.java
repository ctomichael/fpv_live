package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.AreaCodeUtil;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataRcGetWifiFreqInfo extends DataBase implements DJIDataSyncListener {
    private static DataRcGetWifiFreqInfo mCcInstance = null;
    private static DataRcGetWifiFreqInfo mSkyCcInstance = null;
    private boolean isRemote = false;
    private CommonType mCommonType = CommonType.Support;
    public DeviceType mDeviceType;

    @Keep
    public enum CommonType {
        COUNTRY_CODE,
        Support,
        FM_24_RANGE,
        FM_57_RANGE,
        FM_58_RANGE
    }

    public static synchronized DataRcGetWifiFreqInfo getCcInstance() {
        DataRcGetWifiFreqInfo dataRcGetWifiFreqInfo;
        synchronized (DataRcGetWifiFreqInfo.class) {
            if (mCcInstance == null) {
                mCcInstance = new DataRcGetWifiFreqInfo();
                mCcInstance.mCommonType = CommonType.COUNTRY_CODE;
            }
            dataRcGetWifiFreqInfo = mCcInstance;
        }
        return dataRcGetWifiFreqInfo;
    }

    public static synchronized DataRcGetWifiFreqInfo getSkyCcInstance() {
        DataRcGetWifiFreqInfo dataRcGetWifiFreqInfo;
        synchronized (DataRcGetWifiFreqInfo.class) {
            if (mSkyCcInstance == null) {
                mSkyCcInstance = new DataRcGetWifiFreqInfo();
                mSkyCcInstance.isRemote = true;
                mSkyCcInstance.mCommonType = CommonType.COUNTRY_CODE;
            }
            dataRcGetWifiFreqInfo = mSkyCcInstance;
        }
        return dataRcGetWifiFreqInfo;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = AreaCodeUtil.getGetAreaCodeDeviceType(this.isRemote).value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetFreqModeInfo.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.mCommonType.equals(CommonType.COUNTRY_CODE)) {
            this._sendData[0] = 1;
        } else if (this.mCommonType.equals(CommonType.Support)) {
            this._sendData[0] = 2;
        } else if (this.mCommonType.equals(CommonType.FM_24_RANGE)) {
            this._sendData[0] = 3;
        } else if (this.mCommonType.equals(CommonType.FM_57_RANGE)) {
            this._sendData[0] = 4;
        } else if (this.mCommonType.equals(CommonType.FM_58_RANGE)) {
            this._sendData[0] = 5;
        }
    }

    public DataRcGetWifiFreqInfo setCommonType(CommonType commonType) {
        this.mCommonType = commonType;
        return this;
    }

    public CommonType getCommonType() {
        return this.mCommonType;
    }

    public int[] getFreqModeRange() {
        return new int[]{((Integer) get(1, 1, Integer.class)).intValue(), ((Integer) get(2, 1, Integer.class)).intValue()};
    }

    public boolean isGetted() {
        return this._recData != null;
    }

    public String getCountryCode() {
        if (this._recData == null || this._recData.length == 0) {
            return "";
        }
        return BytesUtil.getStringUTF8(this._recData, 1, this._recData.length - 1);
    }

    public ArrayList<Integer> getFreqModeSupport() {
        ArrayList<Integer> mAvailableMode = new ArrayList<>();
        int curFreqMask = ((Integer) get(1, 3, Integer.class)).intValue();
        int i = 0;
        while (curFreqMask != 0) {
            if ((curFreqMask & 1) == 1) {
                mAvailableMode.add(Integer.valueOf(i));
            }
            curFreqMask >>= 1;
            i++;
        }
        return mAvailableMode;
    }
}
