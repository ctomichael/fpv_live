package dji.midware.data.model.P3;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.data.config.P3.CmdIdSpecial;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataFlycSetJoyStickParams;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.specialcontrol.DJIStateMachineDecorator;
import dji.midware.data.specialcontrol.OldSpecialControlStateMachine;
import dji.midware.data.specialcontrol.SpecialControlRisingTrigger;
import dji.midware.interfaces.DJIDataAsyncListener;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.BytesUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Keep
@EXClassNullAway
public class DataOldSpecialControl extends DataSpecialControl implements DJIDataAsyncListener, SpecialControlRisingTrigger {
    private static final long DELAY_STOP = 100;
    private static final int MSG_ID_RESET = 2;
    private static final int MSG_ID_START = 0;
    private static final int MSG_ID_STOP = 1;
    private static DataOldSpecialControl instance = null;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
        /* class dji.midware.data.model.P3.DataOldSpecialControl.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DataOldSpecialControl.this.sendPack();
                    if (msg.arg1 == 0) {
                        return true;
                    }
                    DataOldSpecialControl.this.handler.sendEmptyMessageDelayed(0, (long) msg.arg1);
                    return true;
                case 1:
                    DataOldSpecialControl.this.handler.removeMessages(0);
                    DataOldSpecialControl.this.reset().start(20);
                    return true;
                case 2:
                    DataOldSpecialControl.this.reset().start(20);
                    return true;
                default:
                    return true;
            }
        }
    });
    protected boolean isReset = false;
    protected byte mCameraByte = 0;
    protected final byte[] mData = new byte[2];
    protected DataSpecialControl.FlyGoHomeStaus mFlyGoHomeStatus = DataSpecialControl.FlyGoHomeStaus.INIT;
    protected DataFlycSetJoyStickParams.FlycMode mFlycMode = DataFlycSetJoyStickParams.FlycMode.OTHER;
    protected byte mGimbalByte = 0;
    protected boolean mInit = false;
    protected DataSpecialControl.PlayBrowseType mPlayBackBrowserType = DataSpecialControl.PlayBrowseType.OTHER;
    protected DataSpecialControl.PlayCtrType mPlayBackVideoCtrlType = DataSpecialControl.PlayCtrType.OTHER;
    protected DeviceType mReceiveType = DeviceType.CAMERA;
    private DJIStateMachineDecorator mStateMachine = new OldSpecialControlStateMachine("old-special-control", BackgroundLooper.getLooper(), this);
    protected boolean streamErrorStatus = false;

    public static synchronized DataOldSpecialControl getInstance() {
        DataOldSpecialControl dataOldSpecialControl;
        synchronized (DataOldSpecialControl.class) {
            if (instance == null) {
                instance = new DataOldSpecialControl();
            }
            dataOldSpecialControl = instance;
        }
        return dataOldSpecialControl;
    }

    private DataOldSpecialControl() {
        this.mStateMachine.start();
    }

    /* access modifiers changed from: protected */
    public void _reset() {
        this.isReset = false;
        this.mCameraByte = 0;
        this.mGimbalByte = 0;
        this.mPlayBackVideoCtrlType = DataSpecialControl.PlayCtrType.OTHER;
        this.mPlayBackBrowserType = DataSpecialControl.PlayBrowseType.OTHER;
        this.mFlyGoHomeStatus = DataSpecialControl.FlyGoHomeStaus.INIT;
        this.streamErrorStatus = false;
        Arrays.fill(this.mData, (byte) 0);
    }

    /* access modifiers changed from: protected */
    public DataOldSpecialControl reset() {
        _reset();
        this.isReset = true;
        return this;
    }

    public DataOldSpecialControl init() {
        _reset();
        this.mInit = true;
        return this;
    }

    public DataOldSpecialControl setPhotoType(DataCameraSetPhoto.TYPE type) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        this.mCameraByte = (byte) ((type.value() << 5) | 16);
        return this;
    }

    public DataOldSpecialControl setPhotoType(DataCameraSetPhoto.TYPE type, int number, int interval) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        this.mCameraByte = (byte) ((type.value() << 5) | 16);
        if (type == DataCameraSetPhoto.TYPE.TIME) {
            this.mData[0] = (byte) interval;
            this.mData[1] = (byte) number;
        } else {
            this.mData[0] = (byte) number;
        }
        return this;
    }

    public DataOldSpecialControl setRecordType(boolean isStart) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        if (isStart) {
            this.mCameraByte = 12;
        } else {
            this.mCameraByte = 4;
        }
        return this;
    }

    public DataOldSpecialControl setRecordType(boolean isStart, int type, int interval) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        if (isStart) {
            this.mCameraByte = 12;
        } else {
            this.mCameraByte = 4;
        }
        this.mData[0] = (byte) (interval & 255);
        this.mData[1] = (byte) (interval >> 8);
        byte[] bArr = this.mData;
        bArr[1] = (byte) (bArr[1] | ((byte) (type << 5)));
        return this;
    }

    public DataOldSpecialControl setPlayBackType(boolean isStart) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        if (isStart) {
            this.mCameraByte = 3;
        } else {
            this.mCameraByte = 1;
        }
        return this;
    }

    public DataOldSpecialControl setPlayBackPlayCtr(DataSpecialControl.PlayCtrType type, byte data) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        this.mPlayBackVideoCtrlType = type;
        this.mData[0] = data;
        return this;
    }

    public DataOldSpecialControl setPlayBackBrowserType(DataSpecialControl.PlayBrowseType type, byte data1, byte data2) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        this.mPlayBackBrowserType = type;
        this.mData[0] = data1;
        this.mData[1] = data2;
        return this;
    }

    public DataOldSpecialControl setPlayBackBrowserScaleType(short scale) {
        _reset();
        this.mReceiveType = DeviceType.CAMERA;
        this.mPlayBackBrowserType = DataSpecialControl.PlayBrowseType.SCALE;
        System.arraycopy(BytesUtil.getBytes(scale), 0, this.mData, 0, 2);
        return this;
    }

    public DataOldSpecialControl setGimbalMode(DataGimbalControl.MODE gimbalMode) {
        _reset();
        this.mReceiveType = DeviceType.GIMBAL;
        this.mGimbalByte = (byte) (gimbalMode.value() << 2);
        this.mGimbalByte = (byte) (this.mGimbalByte | 2);
        return this;
    }

    public DataOldSpecialControl setGimbalMode(DataGimbalControl.MODE gimbalMode, boolean isAhead) {
        _reset();
        this.mReceiveType = DeviceType.GIMBAL;
        this.mGimbalByte = (byte) (gimbalMode.value() << 2);
        this.mGimbalByte = (byte) (this.mGimbalByte | 2);
        this.mGimbalByte = (byte) ((isAhead ? (byte) 1 : 0) | this.mGimbalByte);
        return this;
    }

    public DataOldSpecialControl resetGimbal() {
        _reset();
        this.mReceiveType = DeviceType.GIMBAL;
        this.mGimbalByte = 1;
        return this;
    }

    public DataOldSpecialControl selfieGimbal() {
        _reset();
        this.mReceiveType = DeviceType.GIMBAL;
        this.mGimbalByte = Tnaf.POW_2_WIDTH;
        return this;
    }

    public DataOldSpecialControl setFlyGoHomeStatus(DataSpecialControl.FlyGoHomeStaus status) {
        _reset();
        this.mReceiveType = DeviceType.FLYC;
        this.mFlyGoHomeStatus = status;
        return this;
    }

    public DataOldSpecialControl setFlycMode(DataFlycSetJoyStickParams.FlycMode mode) {
        _reset();
        this.mReceiveType = DeviceType.FLYC;
        this.mFlycMode = mode;
        return this;
    }

    public boolean isStreamErrorStatus() {
        return this.streamErrorStatus;
    }

    public DataOldSpecialControl setStreamErrorStatus(boolean streamErrorStatus2) {
        this.streamErrorStatus = streamErrorStatus2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 1;
        this._sendData = new byte[10];
        this._sendData[0] = this.mCameraByte;
        if (this.mPlayBackVideoCtrlType != DataSpecialControl.PlayCtrType.OTHER) {
            this._sendData[1] = (byte) (1 << this.mPlayBackVideoCtrlType.value());
        }
        if (this.mPlayBackBrowserType != DataSpecialControl.PlayBrowseType.OTHER) {
            System.arraycopy(BytesUtil.getBytes((short) (1 << this.mPlayBackBrowserType.value())), 0, this._sendData, 2, 2);
        }
        this._sendData[4] = this.mGimbalByte;
        byte[] bArr = this._sendData;
        byte value = this.mFlyGoHomeStatus.value() | (this.mFlycMode.value() << 2);
        if (!this.streamErrorStatus) {
            i = 0;
        }
        bArr[5] = (byte) ((i << 5) | value);
        System.arraycopy(this.mData, 0, this._sendData, 7, 2);
        byte checkSum = this._sendData[0];
        for (int i2 = 1; i2 < 9; i2++) {
            checkSum = (byte) (this._sendData[i2] ^ checkSum);
        }
        this._sendData[9] = checkSum;
    }

    public void start(long period) {
        this.mStateMachine.sendMessage(OldSpecialControlStateMachine.MSG_START);
    }

    public Disposable sendHighLevel() {
        logISpecialCtrl("trigger send high level, camera_byte: " + ((int) this.mCameraByte) + ", \ngimbal byte: " + ((int) this.mGimbalByte) + ", \nplayback video ctrl byte: " + this.mPlayBackVideoCtrlType + ", \nplayback browse byte: " + this.mPlayBackBrowserType + ", \ngo home status: " + this.mFlyGoHomeStatus + ", \nflyc mode: " + this.mFlycMode + ", \nstream error status: " + this.streamErrorStatus);
        return Observable.interval(50, TimeUnit.MILLISECONDS).take(50).observeOn(Schedulers.computation()).subscribe(new DataOldSpecialControl$$Lambda$0(this), new DataOldSpecialControl$$Lambda$1(this), new DataOldSpecialControl$$Lambda$2(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$sendHighLevel$0$DataOldSpecialControl(Long aLong) throws Exception {
        sendPack();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$sendHighLevel$1$DataOldSpecialControl(Throwable throwable) throws Exception {
        logESpecialCtrl("send high level error: " + throwable);
        this.mStateMachine.sendMessage(OldSpecialControlStateMachine.MSG_TIMOUT);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$sendHighLevel$2$DataOldSpecialControl() throws Exception {
        logESpecialCtrl("send high level time out!!");
        this.mStateMachine.sendMessage(OldSpecialControlStateMachine.MSG_TIMOUT);
    }

    public Disposable sendLowLevel() {
        logISpecialCtrl("trigger send low level.");
        return Observable.interval(25, TimeUnit.MILLISECONDS).take(50).observeOn(Schedulers.computation()).subscribe(new DataOldSpecialControl$$Lambda$3(this), new DataOldSpecialControl$$Lambda$4(this), new DataOldSpecialControl$$Lambda$5(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$sendLowLevel$3$DataOldSpecialControl(Long aLong) throws Exception {
        reset();
        sendPack();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$sendLowLevel$4$DataOldSpecialControl(Throwable throwable) throws Exception {
        logESpecialCtrl("send low-- level error: " + throwable);
        this.mStateMachine.sendMessage(OldSpecialControlStateMachine.MSG_TIMOUT);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$sendLowLevel$5$DataOldSpecialControl() throws Exception {
        logESpecialCtrl("send low-- level time out!!");
        this.mStateMachine.sendMessage(OldSpecialControlStateMachine.MSG_TIMOUT);
    }

    public void start(DJIDataCallBack cb) {
        logISpecialCtrl("request start from outside!");
        start(20);
    }

    /* access modifiers changed from: private */
    public void sendPack() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        ProductType type = DJIProductManager.getInstance().getType();
        if (!DJIProductSupportUtil.isLonganSeries(null) && !DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type) && ProductType.Mammoth != type) {
            pack.receiverType = DeviceType.OSD.value();
        } else if (ProductType.Mammoth == type) {
            pack.receiverType = this.mReceiveType.value();
        } else if (ProductType.LonganMobile == type || ProductType.LonganMobile2 == type) {
            pack.receiverType = this.mReceiveType.value();
        } else {
            pack.receiverType = DeviceType.OFDM.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SPECIAL.value();
        pack.cmdId = CmdIdSpecial.CmdIdType.Control.value();
        start(pack);
    }

    public void stop() {
        logISpecialCtrl("recv ack pack...");
        this.mStateMachine.sendMessage(OldSpecialControlStateMachine.MSG_RECV_ACK);
    }

    private void logISpecialCtrl(String content) {
        DJILog.logWriteI(OldSpecialControlStateMachine.TAG_OLD_SPECIAL_CTRL, content, OldSpecialControlStateMachine.TAG_OLD_SPECIAL_CTRL, new Object[0]);
    }

    private void logESpecialCtrl(String content) {
        DJILog.logWriteE(OldSpecialControlStateMachine.TAG_OLD_SPECIAL_CTRL, content, OldSpecialControlStateMachine.TAG_OLD_SPECIAL_CTRL, new Object[0]);
    }
}
