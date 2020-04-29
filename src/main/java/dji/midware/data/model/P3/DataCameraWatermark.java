package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.BytesUtil;

@Keep
public class DataCameraWatermark extends DataBase implements DJIDataSyncListener {
    private int contentValue = 0;
    private byte displayMode = 0;
    private boolean isGetMode = true;
    private byte[] userCustomInfoBytes = new byte[20];
    private int version = 0;

    public DataCameraWatermark setProtocolVersion(int version2) {
        this.version = version2;
        return this;
    }

    public DataCameraWatermark setGetMode(boolean enable) {
        this.isGetMode = enable;
        return this;
    }

    public DataCameraWatermark reset() {
        this.displayMode = 0;
        return this;
    }

    public DataCameraWatermark setImageWatermark(boolean enable) {
        int i;
        byte b = this.displayMode;
        if (enable) {
            i = 1;
        } else {
            i = 0;
        }
        this.displayMode = BytesUtil.getByte(BytesUtil.setBitValue(b, 0, (byte) i));
        return this;
    }

    public DataCameraWatermark setVideoWatermark(boolean enable) {
        this.displayMode = BytesUtil.getByte(BytesUtil.setBitValue(this.displayMode, 1, (byte) (enable ? 1 : 0)));
        return this;
    }

    public DataCameraWatermark setLiveViewEnabled(boolean enable) {
        this.displayMode = BytesUtil.getByte(BytesUtil.setBitValue(this.displayMode, 2, (byte) (enable ? 1 : 0)));
        return this;
    }

    public DataCameraWatermark setDroneTypeEnabled(boolean enable) {
        int i;
        int i2 = this.contentValue;
        if (enable) {
            i = 1;
        } else {
            i = 0;
        }
        this.contentValue = BytesUtil.setBitValue(i2, 0, (byte) i);
        return this;
    }

    public DataCameraWatermark setDroneSnEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 1, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setCameraTypeEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 2, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setCameraSnEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 3, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setDateTimeEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 4, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setTimeZoneEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 5, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setGPSLonLatEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 6, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setGPSAltitudeEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 7, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setUserCustomEnabled(boolean enable) {
        this.contentValue = BytesUtil.setBitValue(this.contentValue, 31, (byte) (enable ? 1 : 0));
        return this;
    }

    public DataCameraWatermark setUserCustomString(String information) {
        int i = 20;
        if (!TextUtils.isEmpty(information)) {
            byte[] customInfoTemp = BytesUtil.getBytesUTF8(information);
            byte[] bArr = this.userCustomInfoBytes;
            if (customInfoTemp.length < 20) {
                i = customInfoTemp.length;
            }
            System.arraycopy(customInfoTemp, 0, bArr, 0, i);
        }
        return this;
    }

    public boolean isImageWatermarkEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if ((((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isVideoWatermarkEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if ((((Integer) get(0, 1, Integer.class)).intValue() & 2) != 0) {
            return true;
        }
        return false;
    }

    public boolean isLiveViewEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if ((((Integer) get(0, 1, Integer.class)).intValue() & 4) != 0) {
            return true;
        }
        return false;
    }

    public boolean isDronetypenabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if ((((Integer) get(1, 5, Integer.class)).intValue() & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isDroneSnEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 1) & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isCameraTypeEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 2) & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isCameraSnEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 3) & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isDateTimeEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 4) & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isTimeZoneEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 5) & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isGPSLonLatEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 6) & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isGPSAltitudeEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 7) & 1) != 0) {
            return true;
        }
        return false;
    }

    public boolean isUserCustomInfoEnabled() {
        if (this._recData == null || this._recData.length <= 0) {
            return false;
        }
        if (((((Integer) get(1, 5, Integer.class)).intValue() >> 31) & 1) != 0) {
            return true;
        }
        return false;
    }

    public String getUserCustomInfo() {
        if (isUserCustomInfoEnabled()) {
            return getUTF8(6, 20);
        }
        return "";
    }

    public DataCameraWatermark mapToSetterFromGetter() {
        DataCameraWatermark instance = new DataCameraWatermark();
        instance.displayMode = ((Byte) get(0, 1, Byte.class)).byteValue();
        instance.contentValue = ((Integer) get(1, 5, Integer.class)).intValue();
        byte[] customInfoTemp = getRecData(6, 20, -1);
        if (customInfoTemp != null) {
            instance.userCustomInfoBytes = customInfoTemp;
        }
        return instance;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 20;
        if (this.isGetMode) {
            this._sendData = new byte[1];
            this._sendData[0] = 0;
            byte[] bArr = this._sendData;
            bArr[0] = (byte) (bArr[0] | ((this.version & 1) << 4));
            return;
        }
        int dataLength = 2;
        if (this.version == 1) {
            dataLength = 26;
        }
        this._sendData = new byte[dataLength];
        this._sendData[0] = 1;
        byte[] bArr2 = this._sendData;
        bArr2[0] = (byte) (bArr2[0] | ((this.version & 1) << 4));
        this._sendData[1] = this.displayMode;
        if (this.version != 0) {
            System.arraycopy(BytesUtil.getBytes(this.contentValue), 0, this._sendData, 2, 4);
            byte[] bArr3 = this.userCustomInfoBytes;
            byte[] bArr4 = this._sendData;
            if (this.userCustomInfoBytes.length < 20) {
                i = this.userCustomInfoBytes.length;
            }
            System.arraycopy(bArr3, 0, bArr4, 6, i);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.Watermark.value();
        pack.data = getSendData();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        start(pack, callBack);
    }
}
