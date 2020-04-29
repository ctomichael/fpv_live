package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.data.config.P3.CmdIdSpecial;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataFlycSetJoyStickParams;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataNewSpecialControl extends DataSpecialControl implements DJIDataSyncListener {
    private static DataNewSpecialControl instance = null;
    protected static volatile int mSequence = 0;
    protected int mDataLength = 12;
    protected DataSpecialControl.FlyCtrlCmd mFlyCtrlCmd = DataSpecialControl.FlyCtrlCmd.INIT;
    protected DataSpecialControl.FlyGoHomeStaus mFlyGoHomeStatus = DataSpecialControl.FlyGoHomeStaus.INIT;
    protected DataFlycSetJoyStickParams.FlycMode mFlycMode = DataFlycSetJoyStickParams.FlycMode.OTHER;
    protected byte mGimbalByte = 0;
    protected final byte[] mKeyData = {0, 0};
    protected final byte[] mMultiData = {0, 0, 0, 0};
    protected boolean mNeedAck = false;
    protected byte mSecondaryGimbalByte = 0;
    protected SubCmd mSubMode = SubCmd.COMMON;
    protected int mVersion = 0;
    protected WorkView mWorkView = WorkView.LiveView;

    public static synchronized DataNewSpecialControl getInstance() {
        DataNewSpecialControl dataNewSpecialControl;
        synchronized (DataNewSpecialControl.class) {
            if (instance == null) {
                instance = new DataNewSpecialControl();
            }
            dataNewSpecialControl = instance;
        }
        return dataNewSpecialControl;
    }

    /* access modifiers changed from: protected */
    public void _reset() {
        this.mSubMode = SubCmd.COMMON;
        this.mVersion = 0;
        this.mNeedAck = false;
        this.mDataLength = 12;
        this.mFlyGoHomeStatus = DataSpecialControl.FlyGoHomeStaus.INIT;
        this.mFlyCtrlCmd = DataSpecialControl.FlyCtrlCmd.INIT;
        this.mGimbalByte = 0;
        this.mSecondaryGimbalByte = 0;
        Arrays.fill(this.mKeyData, (byte) 0);
        Arrays.fill(this.mMultiData, (byte) 0);
    }

    /* access modifiers changed from: protected */
    public DataNewSpecialControl reset() {
        _reset();
        return this;
    }

    public DataNewSpecialControl init() {
        _reset();
        return this;
    }

    public DataNewSpecialControl setWorkView(WorkView view) {
        this.mWorkView = view;
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | (view.value() << 6));
        return this;
    }

    public DataNewSpecialControl setCameraMode(DataCameraGetMode.MODE mode) {
        _reset();
        setWorkView(WorkView.LiveView);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | 32);
        byte[] bArr2 = this.mMultiData;
        bArr2[1] = (byte) (bArr2[1] | (mode.value() << 4));
        return this;
    }

    public DataNewSpecialControl setPhotoType(DataCameraSetPhoto.TYPE type) {
        _reset();
        setWorkView(WorkView.LiveView);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | Tnaf.POW_2_WIDTH);
        byte[] bArr2 = this.mMultiData;
        bArr2[1] = (byte) (bArr2[1] | type.value());
        return this;
    }

    public DataNewSpecialControl setPhotoType(DataCameraSetPhoto.TYPE type, int number, int interval) {
        _reset();
        setWorkView(WorkView.LiveView);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | Tnaf.POW_2_WIDTH);
        byte[] bArr2 = this.mMultiData;
        bArr2[1] = (byte) (bArr2[1] | type.value());
        if (type == DataCameraSetPhoto.TYPE.TIME) {
            this.mMultiData[2] = (byte) interval;
            this.mMultiData[3] = (byte) number;
        } else {
            this.mMultiData[2] = (byte) number;
        }
        return this;
    }

    public DataNewSpecialControl setRecordType(boolean isStart) {
        byte b;
        _reset();
        setWorkView(WorkView.LiveView);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | 8);
        byte[] bArr2 = this.mMultiData;
        byte b2 = bArr2[0];
        if (isStart) {
            b = 128;
        } else {
            b = 0;
        }
        bArr2[0] = (byte) (b | b2);
        return this;
    }

    public DataNewSpecialControl setEnterPlayBack(boolean isStart) {
        byte b;
        _reset();
        setWorkView(WorkView.LiveView);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | 4);
        byte[] bArr2 = this.mMultiData;
        byte b2 = bArr2[0];
        if (isStart) {
            b = 64;
        } else {
            b = 0;
        }
        bArr2[0] = (byte) (b | b2);
        return this;
    }

    public DataNewSpecialControl setRecordType(boolean isStart, int type, int interval) {
        byte b;
        _reset();
        setWorkView(WorkView.LiveView);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | 8);
        byte[] bArr2 = this.mMultiData;
        byte b2 = bArr2[0];
        if (isStart) {
            b = 128;
        } else {
            b = 0;
        }
        bArr2[0] = (byte) (b | b2);
        byte[] bArr3 = this.mMultiData;
        bArr3[2] = (byte) (bArr3[2] | (interval & 255));
        byte[] bArr4 = this.mMultiData;
        bArr4[3] = (byte) (bArr4[3] | (type << 5) | (interval >> 8));
        return this;
    }

    public DataNewSpecialControl setPlayBackType(boolean isStart) {
        byte b;
        _reset();
        setWorkView(WorkView.LiveView);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | 4);
        byte[] bArr2 = this.mMultiData;
        byte b2 = bArr2[0];
        if (isStart) {
            b = 64;
        } else {
            b = 0;
        }
        bArr2[0] = (byte) (b | b2);
        return this;
    }

    public DataNewSpecialControl setPlayBackPlayCtr(DataSpecialControl.PlayCtrType type, byte data) {
        _reset();
        setWorkView(WorkView.PlayBack);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | Tnaf.POW_2_WIDTH);
        byte[] bArr2 = this.mMultiData;
        bArr2[1] = (byte) (bArr2[1] | (type.value() << 5));
        byte[] bArr3 = this.mMultiData;
        bArr3[2] = (byte) (bArr3[2] | data);
        return this;
    }

    public DataNewSpecialControl setPlayBackBrowserType(DataSpecialControl.PlayBrowseType type, byte data1, byte data2) {
        _reset();
        setWorkView(WorkView.PlayBack);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | 8);
        byte[] bArr2 = this.mMultiData;
        bArr2[1] = (byte) (bArr2[1] | (type.value() << 1));
        byte[] bArr3 = this.mMultiData;
        bArr3[2] = (byte) (bArr3[2] | data1);
        byte[] bArr4 = this.mMultiData;
        bArr4[3] = (byte) (bArr4[3] | data2);
        return this;
    }

    public DataNewSpecialControl setPlayBackBrowserScaleType(short scale) {
        _reset();
        setWorkView(WorkView.PlayBack);
        byte[] bArr = this.mKeyData;
        bArr[1] = (byte) (bArr[1] | 8);
        byte[] bArr2 = this.mMultiData;
        bArr2[1] = (byte) (bArr2[1] | (DataSpecialControl.PlayBrowseType.SCALE.value() << 1));
        byte[] bArr3 = this.mMultiData;
        bArr3[2] = (byte) (bArr3[2] | (scale & 255));
        byte[] bArr4 = this.mMultiData;
        bArr4[3] = (byte) (bArr4[3] | (scale >> 8));
        return this;
    }

    public DataNewSpecialControl setGimbalMode(DataGimbalControl.MODE gimbalMode) {
        _reset();
        setWorkView(WorkView.LiveView);
        this.mGimbalByte = (byte) (gimbalMode.value() << 2);
        this.mGimbalByte = (byte) (this.mGimbalByte | 2);
        return this;
    }

    public DataNewSpecialControl setGimbalMode(DataGimbalControl.MODE gimbalMode, boolean isAhead) {
        _reset();
        setWorkView(WorkView.LiveView);
        this.mGimbalByte = (byte) (gimbalMode.value() << 2);
        this.mGimbalByte = (byte) (this.mGimbalByte | 2);
        this.mGimbalByte = (byte) ((isAhead ? (byte) 1 : 0) | this.mGimbalByte);
        return this;
    }

    public DataNewSpecialControl resetGimbal() {
        _reset();
        setWorkView(WorkView.LiveView);
        this.mGimbalByte = 1;
        return this;
    }

    public DataNewSpecialControl selfieGimbal() {
        _reset();
        setWorkView(WorkView.LiveView);
        this.mGimbalByte = Tnaf.POW_2_WIDTH;
        return this;
    }

    public DataNewSpecialControl setFlyGoHomeStatus(DataSpecialControl.FlyGoHomeStaus status) {
        _reset();
        setWorkView(WorkView.LiveView);
        this.mFlyGoHomeStatus = status;
        return this;
    }

    public DataNewSpecialControl setFlycMode(DataFlycSetJoyStickParams.FlycMode mode) {
        _reset();
        setWorkView(WorkView.LiveView);
        this.mFlycMode = mode;
        return this;
    }

    public void start(long period) {
        start((DJIDataCallBack) null);
    }

    public void stop() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        ProductType type = DJIProductManager.getInstance().getType();
        if (DJIProductSupportUtil.isLonganSeries(null) || DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type)) {
            pack.receiverType = DeviceType.OFDM.value();
        } else {
            pack.receiverType = DeviceType.OSD.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SPECIAL.value();
        pack.cmdId = CmdIdSpecial.CmdIdType.NewControl.value();
        pack.repeatTimes = 2;
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        start(pack, callBack);
    }

    private int getSequence() {
        int seq = mSequence + 1;
        if (seq > 255) {
            seq = 0;
        }
        mSequence = seq;
        return seq;
    }

    private void fillArray(byte[] data, int offset, byte value) {
        if (data != null && data.length != 0 && offset < data.length) {
            int length = data.length;
            for (int i = offset; i < length; i++) {
                data[i] = value;
            }
        }
    }

    private void doCommonPack(byte[] sendData, int offset, boolean reset) {
        sendData[offset + 0] = (byte) this.mSubMode.value();
        int i = offset + 1;
        sendData[i] = (byte) (sendData[i] | this.mDataLength);
        int i2 = offset + 1;
        sendData[i2] = (byte) ((this.mNeedAck ? (byte) 32 : 0) | sendData[i2]);
        int i3 = offset + 1;
        sendData[i3] = (byte) (sendData[i3] | (this.mVersion << 6));
        sendData[offset + 2] = (byte) getSequence();
        if (reset) {
            sendData[offset + 3] = (byte) (DataSpecialControl.FlyGoHomeStaus.INIT.value() | (DataSpecialControl.FlyCtrlCmd.INIT.value() << 2));
            fillArray(sendData, offset + 4, (byte) 0);
            return;
        }
        sendData[offset + 3] = (byte) (this.mFlyGoHomeStatus.value() | (this.mFlyCtrlCmd.value() << 2));
        sendData[offset + 4] = this.mGimbalByte;
        sendData[offset + 5] = this.mSecondaryGimbalByte;
        System.arraycopy(this.mKeyData, 0, sendData, offset + 6, 2);
        System.arraycopy(this.mMultiData, 0, sendData, offset + 8, 4);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        boolean z = true;
        if (SubCmd.COMMON == this.mSubMode) {
            this.mDataLength = 12;
            this._sendData = new byte[(this.mDataLength * 2)];
            boolean firstReset = (DataSpecialControl.FlyGoHomeStaus.INIT == this.mFlyGoHomeStatus && DataSpecialControl.FlyCtrlCmd.INIT == this.mFlyCtrlCmd) ? false : true;
            doCommonPack(this._sendData, 0, firstReset);
            byte[] bArr = this._sendData;
            int i = this.mDataLength;
            if (firstReset) {
                z = false;
            }
            doCommonPack(bArr, i, z);
        }
    }

    @Keep
    public enum SubCmd {
        COMMON(0),
        FLYC(1),
        GIMBAL(2),
        CAMERA(3),
        OTHER(100);
        
        private final int data;

        private SubCmd(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SubCmd find(int b) {
            SubCmd result = COMMON;
            SubCmd[] values = values();
            for (SubCmd tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum WorkView {
        LiveView(0),
        PlayBack(1),
        Library(2),
        Rc(3),
        OTHER(100);
        
        private final int data;

        private WorkView(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static WorkView find(int b) {
            WorkView result = LiveView;
            WorkView[] values = values();
            for (WorkView tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
