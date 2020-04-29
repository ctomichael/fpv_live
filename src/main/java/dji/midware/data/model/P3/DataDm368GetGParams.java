package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdDm368;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataDm368SetGParams;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.HashMap;

@Keep
@EXClassNullAway
public class DataDm368GetGParams extends DataBase implements DJIDataSyncListener {
    private static DataDm368GetGParams instance = null;
    private DataDm368SetGParams.CmdId cmdId;
    private boolean isGetPhoneCharge = false;
    private boolean isLb2 = false;
    private HashMap<DataDm368SetGParams.CmdId, Integer> mDm368GHm = new HashMap<>();

    public static synchronized DataDm368GetGParams getInstance() {
        DataDm368GetGParams dataDm368GetGParams;
        synchronized (DataDm368GetGParams.class) {
            if (instance == null) {
                instance = new DataDm368GetGParams();
            }
            dataDm368GetGParams = instance;
        }
        return dataDm368GetGParams;
    }

    public DataDm368GetGParams setType(boolean isLb22) {
        this.isLb2 = isLb22;
        return this;
    }

    public DataDm368GetGParams setGetPhoneCharge(boolean getPhoneCharge) {
        this.isGetPhoneCharge = getPhoneCharge;
        return this;
    }

    public boolean getIsShowOsd() {
        return toInt(this.mDm368GHm.get(DataDm368SetGParams.CmdId.ShowOsd)) == 1;
    }

    public boolean getIsDouble() {
        return toInt(this.mDm368GHm.get(DataDm368SetGParams.CmdId.ShowDouble)) == 1;
    }

    public boolean getUnit() {
        return ((Integer) get(8, 1, Integer.class)).intValue() == 1;
    }

    public int get720PFps() {
        return toInt(this.mDm368GHm.get(DataDm368SetGParams.CmdId.Set720PFps));
    }

    public int getOutputDevice() {
        return ((Integer) get(14, 1, Integer.class)).intValue();
    }

    public int getHDMIFormat() {
        return ((Integer) get(17, 1, Integer.class)).intValue();
    }

    public int getOutputMode() {
        return toInt(this.mDm368GHm.get(DataDm368SetGParams.CmdId.SetOutputMode));
    }

    public int getSDIFormat() {
        return ((Integer) get(23, 1, Integer.class)).intValue();
    }

    public int getOsdMarginTop() {
        return ((Integer) get(26, 1, Integer.class)).intValue();
    }

    public int getOsdMarginLeft() {
        return ((Integer) get(29, 1, Integer.class)).intValue();
    }

    public int getOsdMarginBottom() {
        return ((Integer) get(32, 1, Integer.class)).intValue();
    }

    public int getOsdMarginRight() {
        return ((Integer) get(35, 1, Integer.class)).intValue();
    }

    public int getOutputLocation() {
        return ((Integer) get(38, 1, Integer.class)).intValue();
    }

    public boolean getOutputEnable() {
        return ((Integer) get(41, 1, Integer.class)).intValue() == 1;
    }

    public int getChargingMode() {
        return ((Integer) get(44, 1, Integer.class)).intValue();
    }

    public boolean getIsDisableUpgradeVoice() {
        return toInt(this.mDm368GHm.get(DataDm368SetGParams.CmdId.DisableUpgradeSound)) == 1;
    }

    public DataDm368SetGParams.PhoneChargeConfig getPhoneChargeConfig() {
        return DataDm368SetGParams.PhoneChargeConfig.find(((Integer) get(2, 1, Integer.class)).intValue());
    }

    public int toInt(Object value) {
        if (value == null || !(value instanceof Integer)) {
            return 0;
        }
        return ((Integer) value).intValue();
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        if (data != null && data.length >= 2) {
            int length = ((Integer) get(1, 1, Integer.class)).intValue();
            int i = 0;
            while (i + length + 1 < data.length) {
                DataDm368SetGParams.CmdId id = DataDm368SetGParams.CmdId.find(((Integer) get(i, 1, Integer.class)).intValue());
                length = ((Integer) get(i + 1, 1, Integer.class)).intValue();
                this.mDm368GHm.put(id, Integer.valueOf(((Integer) get(i + 2, length, Integer.class)).intValue()));
                i += length + 2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.isLb2) {
            this._sendData = new byte[14];
            this._sendData[0] = (byte) DataDm368SetGParams.CmdId.ShowOsd.value();
            this._sendData[1] = (byte) DataDm368SetGParams.CmdId.ShowDouble.value();
            this._sendData[2] = (byte) DataDm368SetGParams.CmdId.ShowUnit.value();
            this._sendData[3] = (byte) DataDm368SetGParams.CmdId.Set720PFps.value();
            this._sendData[4] = (byte) DataDm368SetGParams.CmdId.SetOutputDevice.value();
            this._sendData[5] = (byte) DataDm368SetGParams.CmdId.SetHDMIFormat.value();
            this._sendData[6] = (byte) DataDm368SetGParams.CmdId.SetOutputMode.value();
            this._sendData[7] = (byte) DataDm368SetGParams.CmdId.SetSDIFormat.value();
            this._sendData[8] = (byte) DataDm368SetGParams.CmdId.SetOsdTop.value();
            this._sendData[9] = (byte) DataDm368SetGParams.CmdId.SetOsdLeft.value();
            this._sendData[10] = (byte) DataDm368SetGParams.CmdId.SetOsdBottom.value();
            this._sendData[11] = (byte) DataDm368SetGParams.CmdId.SetOsdRight.value();
            this._sendData[12] = (byte) DataDm368SetGParams.CmdId.SetOutputLoc.value();
            this._sendData[13] = (byte) DataDm368SetGParams.CmdId.SetOutputEnable.value();
        } else if (this.isGetPhoneCharge) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) DataDm368SetGParams.CmdId.AndroidPhoneCharge.value();
        } else {
            this._sendData = new byte[4];
            this._sendData[0] = (byte) DataDm368SetGParams.CmdId.ShowOsd.value();
            this._sendData[1] = (byte) DataDm368SetGParams.CmdId.ShowDouble.value();
            this._sendData[2] = (byte) DataDm368SetGParams.CmdId.ShowUnit.value();
            this._sendData[3] = (byte) DataDm368SetGParams.CmdId.Set720PFps.value();
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368_G.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.GetGParams.value();
        start(pack, callBack);
    }
}
