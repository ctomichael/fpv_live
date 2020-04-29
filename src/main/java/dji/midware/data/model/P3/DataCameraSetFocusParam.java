package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import dji.sdksharedlib.keycatalog.CameraKeys;

@Keep
@EXClassNullAway
public class DataCameraSetFocusParam extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFocusParam instance = null;
    private boolean mDigitalConZoomIn = true;
    private int mDigitalConZoomSpeed = 1;
    private ZoomMode mDigitalMode = ZoomMode.POSITION;
    private float mDigitalPosScale = 0.0f;
    private float mDigitalStepScale = 0.0f;
    private boolean mDigitalStepZoomIn = true;
    private boolean mOpticalConZoomIn = true;
    private int mOpticalConZoomSpeed = 1;
    private ZoomMode mOpticalMode = ZoomMode.POSITION;
    private float mOpticalPosScale = 0.0f;
    private float mOpticalStepScale = 0.0f;
    private boolean mOpticalStepZoomIn = true;
    private boolean mbDigitalZoom = false;
    private boolean mbOpticalZoom = false;

    public static synchronized DataCameraSetFocusParam getInstance() {
        DataCameraSetFocusParam dataCameraSetFocusParam;
        synchronized (DataCameraSetFocusParam.class) {
            if (instance == null) {
                instance = new DataCameraSetFocusParam();
            }
            dataCameraSetFocusParam = instance;
        }
        return dataCameraSetFocusParam;
    }

    public DataCameraSetFocusParam setOpticalZoom(boolean enable) {
        this.mbOpticalZoom = enable;
        return this;
    }

    public DataCameraSetFocusParam setOpticalMode(ZoomMode mode) {
        this.mOpticalMode = mode;
        return this;
    }

    public DataCameraSetFocusParam setOpticalStepZoomIn(boolean zoom) {
        this.mOpticalStepZoomIn = zoom;
        return this;
    }

    public DataCameraSetFocusParam setOpticalStepScale(float scale) {
        this.mOpticalStepScale = scale;
        return this;
    }

    public DataCameraSetFocusParam setOpticalPosScale(float scale) {
        this.mOpticalPosScale = scale;
        return this;
    }

    public DataCameraSetFocusParam setOpticalConZoom(boolean zoom) {
        this.mOpticalConZoomIn = zoom;
        return this;
    }

    public DataCameraSetFocusParam setOpticalConSpeed(int speed) {
        this.mOpticalConZoomSpeed = speed;
        return this;
    }

    public DataCameraSetFocusParam setDigitalZoom(boolean enable) {
        this.mbDigitalZoom = enable;
        return this;
    }

    public DataCameraSetFocusParam setDigitalMode(ZoomMode mode) {
        this.mDigitalMode = mode;
        return this;
    }

    public DataCameraSetFocusParam setDigitalStepZoomIn(boolean zoom) {
        this.mDigitalStepZoomIn = zoom;
        return this;
    }

    public DataCameraSetFocusParam setDigitalStepScale(float scale) {
        this.mDigitalStepScale = scale;
        return this;
    }

    public DataCameraSetFocusParam setDigitalPosScale(float scale) {
        this.mDigitalPosScale = scale;
        return this;
    }

    public DataCameraSetFocusParam setDigitalConZoom(boolean zoom) {
        this.mDigitalConZoomIn = zoom;
        return this;
    }

    public DataCameraSetFocusParam setDigitalConSpeed(int speed) {
        this.mDigitalConZoomSpeed = speed;
        return this;
    }

    public float getOpticalScale() {
        return (((float) ((Integer) get(0, 2, Integer.class)).intValue()) * 1.0f) / 100.0f;
    }

    public float getDigitalScale() {
        return (((float) ((Integer) get(2, 2, Integer.class)).intValue()) * 1.0f) / 100.0f;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte b;
        this._sendData = new byte[5];
        if (this.mbOpticalZoom) {
            byte[] bArr = this._sendData;
            bArr[0] = (byte) (bArr[0] | 128);
            byte[] bArr2 = this._sendData;
            bArr2[0] = (byte) (bArr2[0] | ((byte) ((this.mOpticalMode.value() << 4) & 255)));
            if (this.mOpticalMode == ZoomMode.STEP) {
                this._sendData[1] = (byte) ((int) (this.mOpticalStepScale * 100.0f));
                byte[] bArr3 = this._sendData;
                bArr3[2] = (byte) ((this.mOpticalStepZoomIn ? (byte) 1 : 0) | bArr3[2]);
            } else if (this.mOpticalMode == ZoomMode.POSITION) {
                System.arraycopy(BytesUtil.getBytes((short) ((int) (this.mOpticalPosScale * 100.0f))), 0, this._sendData, 1, 2);
            } else if (this.mOpticalMode == ZoomMode.CONTINUOUS) {
                this._sendData[1] = (byte) this.mOpticalConZoomSpeed;
                byte[] bArr4 = this._sendData;
                bArr4[2] = (byte) ((this.mOpticalConZoomIn ? (byte) 1 : 0) | bArr4[2]);
            }
        }
        if (this.mbDigitalZoom) {
            byte[] bArr5 = this._sendData;
            bArr5[0] = (byte) (bArr5[0] | 8);
            byte[] bArr6 = this._sendData;
            bArr6[0] = (byte) (bArr6[0] | ((byte) (this.mDigitalMode.value() & 255)));
            if (this.mDigitalMode == ZoomMode.STEP) {
                this._sendData[3] = (byte) ((int) (this.mDigitalStepScale * 100.0f));
                byte[] bArr7 = this._sendData;
                byte b2 = bArr7[4];
                if (this.mDigitalStepZoomIn) {
                    b = 1;
                } else {
                    b = 0;
                }
                bArr7[4] = (byte) (b | b2);
            } else if (this.mDigitalMode == ZoomMode.POSITION) {
                System.arraycopy(BytesUtil.getBytes((short) ((int) (this.mDigitalPosScale * 100.0f))), 0, this._sendData, 3, 2);
            } else if (this.mDigitalMode == ZoomMode.CONTINUOUS) {
                this._sendData[3] = (byte) this.mDigitalConZoomSpeed;
                byte[] bArr8 = this._sendData;
                bArr8[4] = (byte) ((this.mDigitalConZoomIn ? (byte) 1 : 0) | bArr8[4]);
            }
        }
        DJILogHelper.getInstance().LOGD(CameraKeys.COMPONENT_KEY, "Focus Param[" + BytesUtil.byte2hex(this._sendData) + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetZoomParams.value();
        start(pack, callBack);
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetZoomParams.value();
        super.start(pack);
    }

    @Keep
    public enum ZoomMode {
        STEP(0),
        POSITION(1),
        CONTINUOUS(2);
        
        private int data;

        private ZoomMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ZoomMode find(int b) {
            ZoomMode result = POSITION;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
