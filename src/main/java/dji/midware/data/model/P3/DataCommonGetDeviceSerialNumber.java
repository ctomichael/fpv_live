package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCommonGetDeviceSerialNumber extends DataAbstractGetPushActiveStatus implements DJIDataSyncListener {
    private DeviceIndex mDeviceIndex = DeviceIndex.DeviceNum;

    @Keep
    public enum DeviceIndex {
        BoardNum(1),
        ChipId(2),
        ModuleNum(3),
        DeviceNum(4);
        
        byte data;

        private DeviceIndex(int i) {
            this.data = (byte) i;
        }
    }

    public DataCommonGetDeviceSerialNumber setReceiveId(DeviceIndex id) {
        this.mDeviceIndex = id;
        return this;
    }

    public String getSerialNum() {
        int headLen = 2;
        int len = ((Integer) get(0, 2, Integer.class)).intValue();
        int flycOffset = 4;
        if (DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.WM160) {
            flycOffset = 0;
        }
        if (this.mDeviceIndex == DeviceIndex.DeviceNum) {
            headLen = 2 + flycOffset;
            len -= flycOffset;
        }
        return getRevertSn(headLen, len);
    }

    /* access modifiers changed from: protected */
    public String getRevertSn(int from, int len) {
        StringBuilder sb = new StringBuilder(len);
        if (this._recData != null && from + len <= this._recData.length) {
            int i = from;
            while (i < from + len && -1 != this._recData[i]) {
                if (BytesUtil.isNumberOrLetter(this._recData[i])) {
                    sb.append((char) this._recData[i]);
                }
                i++;
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = this.mDeviceIndex.data;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.WM160) {
            pack.receiverType = DeviceType.FLYC.value();
        } else {
            pack.receiverType = DeviceType.DM368.value();
            pack.receiverId = 3;
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.GetSerialNum.value();
        start(pack, callBack);
    }
}
