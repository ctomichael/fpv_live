package dji.midware.data.manager.P3;

import android.os.Handler;
import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.DJILogUtils;
import dji.midware.data.config.P3.CmdIdSmartBattery;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DJICmdSetBase;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.packplugin.DJIPackMockPlugin;
import dji.midware.data.manager.packplugin.DJIPackWatchPlugin;
import dji.midware.data.manager.packplugin.record.DJIPackRecordPlugin;
import dji.midware.data.model.common.ByteObject;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.interfaces.DJIDataAsyncListener;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.natives.GroudStation;
import dji.midware.reflect.MidwareInjectManager;
import dji.midware.reflect.MidwareToP3Injectable;
import dji.midware.usb.P3.AoaReportHelper;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.BytesUtil;
import dji.midware.util.SimpleComsumer;
import dji.publics.DJIExecutor;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public abstract class DJIPackManagerBase {
    private static final int CHECK_MODULE_CAMERA_LOSE = 10;
    private static final int DELAY_RC_CONNECT_CHECK = 1500;
    public static final int DRONE_DISCONNECT = 1;
    public static boolean ENABLE_RECEIVE_AIR_DATA_FOR_INNER = true;
    private static final int EVENTBUS_CHECK_DELAY_TIME = 100;
    private static final int MSG_EVENTBUS_POST_TOO_LONG = 1;
    private static final int RC_CONNECT_ABNORMAL = 2;
    private static final String TAG = "DJIPackManagerBase";
    /* access modifiers changed from: private */
    public int byteOffset;
    private SimpleComsumer.Callback callback = new SimpleComsumer.Callback() {
        /* class dji.midware.data.manager.P3.DJIPackManagerBase.AnonymousClass3 */

        public void invoke(Message msg) {
            ByteObject byteObject = (ByteObject) msg.obj;
            int len = msg.arg2;
            System.arraycopy(byteObject.getBytes(), 0, DJIPackManagerBase.this.mbuffer, DJIPackManagerBase.this.count, len);
            byteObject.recycle();
            int unused = DJIPackManagerBase.this.count = DJIPackManagerBase.this.count + len;
            do {
            } while (DJIPackManagerBase.this.parseOne());
            System.arraycopy(DJIPackManagerBase.this.mZerobuffer, 0, DJIPackManagerBase.this.mTempbuffer, 0, DJIPackManagerBase.this.count);
            System.arraycopy(DJIPackManagerBase.this.mbuffer, DJIPackManagerBase.this.byteOffset, DJIPackManagerBase.this.mTempbuffer, 0, DJIPackManagerBase.this.count);
            DJIPackManagerBase.this.mCopybuffer = DJIPackManagerBase.this.mbuffer;
            DJIPackManagerBase.this.mbuffer = DJIPackManagerBase.this.mTempbuffer;
            DJIPackManagerBase.this.mTempbuffer = DJIPackManagerBase.this.mCopybuffer;
            int unused2 = DJIPackManagerBase.this.byteOffset = 0;
        }

        public void invoke(ByteObject byteObject) {
            byte[] temp = byteObject.getBytes();
            int len = byteObject.getValidLen();
            System.arraycopy(temp, 0, DJIPackManagerBase.this.mbuffer, DJIPackManagerBase.this.count, len);
            byteObject.recycle();
            int unused = DJIPackManagerBase.this.count = DJIPackManagerBase.this.count + len;
            do {
            } while (DJIPackManagerBase.this.parseOne());
            System.arraycopy(DJIPackManagerBase.this.mZerobuffer, 0, DJIPackManagerBase.this.mTempbuffer, 0, DJIPackManagerBase.this.count);
            System.arraycopy(DJIPackManagerBase.this.mbuffer, DJIPackManagerBase.this.byteOffset, DJIPackManagerBase.this.mTempbuffer, 0, DJIPackManagerBase.this.count);
            DJIPackManagerBase.this.mCopybuffer = DJIPackManagerBase.this.mbuffer;
            DJIPackManagerBase.this.mbuffer = DJIPackManagerBase.this.mTempbuffer;
            DJIPackManagerBase.this.mTempbuffer = DJIPackManagerBase.this.mCopybuffer;
            int unused2 = DJIPackManagerBase.this.byteOffset = 0;
        }
    };
    protected ReentrantLock connectCheckLock = new ReentrantLock();
    /* access modifiers changed from: private */
    public int count;
    protected DataCameraEvent curCameraEvent = DataCameraEvent.ConnectLose;
    private DataBaseProcessor dataBaseProcessor;
    protected boolean enabledSetDataEvent;
    private DJIEncryManager encryManager;
    protected Handler handler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
        /* class dji.midware.data.manager.P3.DJIPackManagerBase.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            DJIPackManagerBase.this.connectCheckLock.lock();
            try {
                switch (msg.what) {
                    case 0:
                    default:
                        return false;
                    case 1:
                        DJILog.saveConnectDebug("DRONE_DISCONNECT:  " + msg.arg1 + ", isCheck: " + DJIPackManagerBase.this.isCheck);
                        if (DJIPackManagerBase.this.isCheck) {
                            DJILogHelper.getInstance().LOGE(DJIPackManagerBase.TAG, "DataCameraEvent debug lose " + DJIPackManagerBase.this.handler + " " + DJIPackManagerBase.this.name, false, false);
                            DJIPackManagerBase.this.setCurCameraEvent(DataCameraEvent.ConnectLose);
                        }
                        return false;
                    case 2:
                        DJILog.saveConnectDebug("RC_CONNECT_ABNORMAL, isCheck: " + DJIPackManagerBase.this.isCheck + ", pos: " + getClass().getSimpleName());
                        if (DJIPackManagerBase.this.isCheck) {
                            DJIPackManagerBase.this.setRcReceiveDataState(RcReceiveDataState.ABNORMAL);
                        }
                        return false;
                    case 10:
                        DJIPackManagerBase.this.setCurCameraModuleEvent(DataModuleEvent.CameraLose);
                        return false;
                }
            } catch (Exception e) {
                DJILog.saveConnectDebug("DJIPackManager post curCameraEvent 7 exption : " + e.getMessage());
            } finally {
                DJIPackManagerBase.this.connectCheckLock.unlock();
            }
        }
    });
    byte[] header = new byte[4];
    protected boolean isCheck = true;
    protected DJIAirPackIdentifier mAirPackIdentifier = new DJIAirPackIdentifier();
    byte[] mCopybuffer;
    /* access modifiers changed from: private */
    public DataModuleEvent mCurCameraAlive = DataModuleEvent.CameraLose;
    private Handler mEventBusConsumHandler = new Handler(DJIExecutor.getLooper(), new Handler.Callback() {
        /* class dji.midware.data.manager.P3.DJIPackManagerBase.AnonymousClass2 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (DJIPackManagerBase.this.simpleComsumer != null) {
                        DJILog.logWriteD(AoaReportHelper.TAG_CONNECT_DEBUG, "eventbus consume time too long, name: " + DJIPackManagerBase.this.simpleComsumer.getName() + "\nstack trace: " + DJILogUtils.getThreadStack(DJIPackManagerBase.this.simpleComsumer), AoaReportHelper.TAG_CONNECT_DEBUG, new Object[0]);
                        break;
                    }
                    break;
            }
            return false;
        }
    });
    private DJIPackMockPlugin mPackPlugin = DJIPackMockPlugin.getInstance();
    protected RcReceiveDataState mRcReceiveDataState = RcReceiveDataState.ABNORMAL;
    byte[] mTempbuffer = new byte[81920];
    private MidwareToP3Injectable mToP3Injectable = MidwareInjectManager.getMidwareToP3Injectable();
    byte[] mZerobuffer = new byte[81920];
    byte[] mbuffer = new byte[81920];
    protected String name;
    protected boolean needCheckEncrypt = true;
    /* access modifiers changed from: private */
    public SimpleComsumer simpleComsumer;

    /* access modifiers changed from: protected */
    public abstract void handleAirConnection(RecvPack recvPack);

    /* access modifiers changed from: protected */
    public void sendDisconnectMsg(long delayTime, int index) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = index;
        this.handler.sendMessageDelayed(msg, delayTime);
    }

    /* access modifiers changed from: protected */
    public void removeDisconnectMsg() {
        this.handler.removeMessages(1);
    }

    public DJIPackManagerBase() {
        if (!this.enabledSetDataEvent) {
            this.dataBaseProcessor = DataBaseProcessor.getInstance();
            this.encryManager = DJIEncryManager.getInstance();
        }
        this.simpleComsumer = new SimpleComsumer(this.callback, getClass().getSimpleName());
    }

    public SimpleComsumer getSimpleComsumer() {
        return this.simpleComsumer;
    }

    public int getConsumerQueueSize() {
        return this.simpleComsumer.getQueueSize();
    }

    public synchronized void parse(byte[] buffer, int offset, int len) {
        AoaReportHelper.getInstance().reciveCmdChanel(buffer, offset, len);
        ByteObject byteObject = ByteObject.obtain(len, TAG);
        System.arraycopy(buffer, offset, byteObject.getBytes(), 0, len);
        this.simpleComsumer.put(byteObject);
    }

    /* access modifiers changed from: private */
    public boolean parseOne() {
        if (this.count < 13 || !findHead() || this.count < 4) {
            return false;
        }
        System.arraycopy(this.mbuffer, this.byteOffset, this.header, 0, 4);
        if (!GroudStation.native_verifyCrc8(this.header)) {
            this.byteOffset++;
            this.count--;
            return true;
        }
        int length = getLength();
        if (length > this.count) {
            return false;
        }
        byte[] buffer = new byte[length];
        System.arraycopy(this.mbuffer, this.byteOffset, buffer, 0, length);
        if (length < 13 || !GroudStation.native_verifyCrc16(buffer)) {
            this.byteOffset += 4;
            this.count -= 4;
            if (!this.enabledSetDataEvent) {
                DJILogHelper.getInstance().LOGD("PackManager", "fullBuffer crc16=fail=" + BytesUtil.byte2hex(buffer));
            }
        } else {
            this.byteOffset += length;
            this.count -= length;
            if (this.needCheckEncrypt) {
                if (this.enabledSetDataEvent && this.encryManager.isEncryptData(buffer)) {
                    this.encryManager.setIsOldFirmware(false);
                    byte[] seqBytes = new byte[2];
                    System.arraycopy(buffer, 6, seqBytes, 0, 2);
                    buffer = this.encryManager.simpleDecrypt(buffer, BytesUtil.getInt(seqBytes));
                } else if (this.encryManager.isNeedEncrypt(buffer)) {
                    this.encryManager.setIsOldFirmware(true);
                }
            }
            RecvPack pack = RecvPack.obtain(buffer);
            if (ENABLE_RECEIVE_AIR_DATA_FOR_INNER) {
                handleAirConnection(pack);
            }
            checkModuleAlive(pack);
            setMsg(pack);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void checkModuleAlive(RecvPack pack) {
        if (pack.senderType == DeviceType.CAMERA.value()) {
            if (this.handler != null && this.handler.hasMessages(10)) {
                this.handler.removeMessages(10);
            }
            if (this.mCurCameraAlive == DataModuleEvent.CameraLose) {
                setCurCameraModuleEvent(DataModuleEvent.CameraOK);
            }
            this.handler.sendEmptyMessageDelayed(10, 1000);
        }
    }

    private boolean findHead() {
        boolean result;
        int now = indexOf((byte) 85);
        if (now >= 0) {
            result = true;
        } else {
            result = false;
        }
        if (result) {
            this.byteOffset += now;
            this.count -= now;
        } else {
            this.byteOffset = 0;
            this.count = 0;
        }
        return result;
    }

    private int indexOf(byte value) {
        for (int i = 0; i < this.count; i++) {
            if (this.mbuffer[this.byteOffset + i] == value) {
                return i;
            }
        }
        return -1;
    }

    private int getLength() {
        return BytesUtil.getShort(new byte[]{this.mbuffer[this.byteOffset + 1], this.mbuffer[this.byteOffset + 2]}) & 1023;
    }

    /* access modifiers changed from: protected */
    public void handleRcConnectState() {
        if (this.isCheck) {
            this.connectCheckLock.lock();
            try {
                if (this.handler != null && this.handler.hasMessages(2)) {
                    this.handler.removeMessages(2);
                }
                if (this.mRcReceiveDataState != RcReceiveDataState.NORMAL) {
                    setRcReceiveDataState(RcReceiveDataState.NORMAL);
                }
                if (this.handler != null && DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.AOA) {
                    this.handler.sendEmptyMessageDelayed(2, 1500);
                }
            } finally {
                this.connectCheckLock.unlock();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeCheckRcConnectStateMsg() {
        if (this.handler != null) {
            this.handler.removeMessages(2);
        }
    }

    public void setRcReceiveDataState(RcReceiveDataState rcReceiveDataState) {
        this.connectCheckLock.lock();
        try {
            this.mRcReceiveDataState = rcReceiveDataState;
            DJILog.saveConnectDebug("DJIPackManager post RcReceiveDataState:  " + this.mRcReceiveDataState + ", invoke: \r\n" + DJILog.getCurrentStack());
            BackgroundLooper.post(new Runnable() {
                /* class dji.midware.data.manager.P3.DJIPackManagerBase.AnonymousClass4 */

                public void run() {
                    EventBus.getDefault().post(DJIPackManagerBase.this.mRcReceiveDataState);
                }
            });
        } finally {
            this.connectCheckLock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void setCurCameraEvent(DataCameraEvent event) {
        this.connectCheckLock.lock();
        try {
            this.curCameraEvent = event;
            DJILog.saveConnectDebug("DJIPackManager post curCameraEvent:  " + this.curCameraEvent + ", invoke: \r\n" + DJILog.getCurrentStack());
            BackgroundLooper.post(new Runnable() {
                /* class dji.midware.data.manager.P3.DJIPackManagerBase.AnonymousClass5 */

                public void run() {
                    EventBus.getDefault().post(DJIPackManagerBase.this.curCameraEvent);
                }
            });
        } finally {
            this.connectCheckLock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void setCurCameraModuleEvent(DataModuleEvent event) {
        this.connectCheckLock.lock();
        try {
            this.mCurCameraAlive = event;
            DJILog.saveConnectDebug("DJIPackManager post curCameraModuleEvent:  " + this.mCurCameraAlive + ", invoke: \r\n" + DJILog.getCurrentStack());
            BackgroundLooper.post(new Runnable() {
                /* class dji.midware.data.manager.P3.DJIPackManagerBase.AnonymousClass6 */

                public void run() {
                    EventBus.getDefault().post(DJIPackManagerBase.this.mCurCameraAlive);
                }
            });
        } finally {
            this.connectCheckLock.unlock();
        }
    }

    private void setMsg(RecvPack pack) {
        if ((ENABLE_RECEIVE_AIR_DATA_FOR_INNER || !this.mAirPackIdentifier.isAirPack(pack)) && pack.cmdSetObj != null && pack.cmdSetObj.cmdIdClass() != null) {
            DJICmdSetBase cls = pack.cmdSetObj.cmdIdClass();
            boolean isBlock = cls.isBlock(pack.cmdId);
            boolean isMix = cls.isMix(pack.cmdId);
            if ((isBlock || isMix) && pack.cmdType == DataConfig.CMDTYPE.ACK.value()) {
                this.dataBaseProcessor.setMsg(pack);
                if (isInnerOrDebug()) {
                    DJIPackRecordPlugin.getInstance().savePack(pack, DJIPackRecordPlugin.PackType4Plugin.ACK);
                    DJIPackWatchPlugin.getInstance().onCmdCome(pack, DJIPackRecordPlugin.PackType4Plugin.ACK);
                }
            }
            if (!isBlock && !isNeedFilterAckPackForPush(pack)) {
                if (isInnerOrDebug()) {
                    DJIPackRecordPlugin.getInstance().savePack(pack, DJIPackRecordPlugin.PackType4Plugin.PUSH);
                    DJIPackWatchPlugin.getInstance().onCmdCome(pack, DJIPackRecordPlugin.PackType4Plugin.PUSH);
                }
                if (!isInnerOrDebug() || !this.mPackPlugin.isCmdMocking(pack.cmdSet, pack.cmdId)) {
                    try {
                        DataBase dataBase = DJICmdSetBase.getDataBaseObject(pack.cmdSet, pack.cmdId, pack.senderType, pack.senderId);
                        if (dataBase instanceof DJIDataAsyncListener) {
                            ((DJIDataAsyncListener) dataBase).stop();
                        } else if (dataBase != null) {
                            if (isInnerOrDebug()) {
                                this.mEventBusConsumHandler.sendEmptyMessageDelayed(1, 100);
                            }
                            dataBase.setPushRecPack(pack);
                            if (isInnerOrDebug()) {
                                this.mEventBusConsumHandler.removeMessages(1);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            } else if (!isBlock && !isMix && pack.cmdType == DataConfig.CMDTYPE.ACK.value() && isInnerOrDebug()) {
                DJIPackRecordPlugin.getInstance().savePack(pack, DJIPackRecordPlugin.PackType4Plugin.UNKNOWN);
                DJILog.logWriteD(TAG, "ack cmd isBlock=false, cmdset: " + pack.cmdSet + ", cmdid: " + pack.cmdId, TAG, new Object[0]);
            }
        }
    }

    private boolean isNeedFilterAckPackForPush(RecvPack pack) {
        return pack.cmdType == DataConfig.CMDTYPE.ACK.value() && pack.cmdSet == CmdSet.BATTERY.value() && pack.cmdId == CmdIdSmartBattery.CmdIdType.GetPushDynamicData.value();
    }

    /* access modifiers changed from: protected */
    public boolean isInnerOrDebug() {
        return this.mToP3Injectable != null && (this.mToP3Injectable.isDevelopPackage() || this.mToP3Injectable.isInnerPackage());
    }
}
