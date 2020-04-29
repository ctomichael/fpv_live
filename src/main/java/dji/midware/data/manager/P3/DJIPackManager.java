package dji.midware.data.manager.P3;

import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.pilot.fpv.model.IEventObjects;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIPackManager extends DJIPackManagerBase {
    private static final String UP_ACK = "UP_ACK";
    private static DJIPackManager instance = null;
    public static boolean isPostPack = false;
    public static int logCmdId;
    public static int logCmdSet;
    private int connectLosedelayMillis = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
    private DJIConnectType connectType = DJIConnectType.RC;
    private boolean isPaused = false;
    private final DJILinkDaemonService linkDaemonService;
    private int packCount = 0;

    public static synchronized DJIPackManager getInstance() {
        DJIPackManager dJIPackManager;
        synchronized (DJIPackManager.class) {
            if (instance == null) {
                instance = new DJIPackManager("Main");
            }
            dJIPackManager = instance;
        }
        return dJIPackManager;
    }

    public DJIPackManager(String name) {
        this.name = name;
        this.enabledSetDataEvent = true;
        this.linkDaemonService = DJILinkDaemonService.getInstance();
    }

    public void setIsCheckConnect(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void delayConnectLose(int delay) {
        DJILogHelper.getInstance().LOGE("DJIPackManager", "ConnectCheck delayConnectLose " + delay);
        if (this.curCameraEvent == DataCameraEvent.ConnectOK) {
            removeDisconnectMsg();
            sendDisconnectMsg((long) delay, 2);
        }
    }

    public void pauseConnectCheck() {
        this.isPaused = true;
        removeCheckRcConnectStateMsg();
        removeDisconnectMsg();
    }

    public void resumeConnectCheck() {
        this.isPaused = false;
        if (this.curCameraEvent == DataCameraEvent.ConnectOK) {
            removeDisconnectMsg();
            sendDisconnectMsg((long) (this.connectLosedelayMillis * 2), 3);
        }
    }

    public void setConnectTypeDirectLy(DJIConnectType type) {
        DJILogHelper.getInstance().LOGE("DJIPackManager", "DataCameraEvent setConnectTypeDirectLy type" + type, false, false);
        this.connectType = type;
        if (type == DJIConnectType.MC) {
            removeDisconnectMsg();
            setCurCameraEvent(DataCameraEvent.ConnectOK);
        } else if (type == DJIConnectType.IDLE) {
            setCurCameraEvent(DataCameraEvent.ConnectLose);
        }
    }

    public void setConnectType(DJIConnectType type) {
        this.connectType = type;
        DJILogHelper.getInstance().LOGE("DJIPackManager", "DataCameraEvent setConnectType cmdSet=" + type, false, false);
        if (type == DJIConnectType.MC) {
            removeDisconnectMsg();
            if (this.curCameraEvent != DataCameraEvent.ConnectOK) {
                setCurCameraEvent(DataCameraEvent.ConnectOK);
            }
        } else if (type == DJIConnectType.IDLE && this.curCameraEvent != DataCameraEvent.ConnectLose) {
            setCurCameraEvent(DataCameraEvent.ConnectLose);
        }
    }

    public boolean isRemoteConnected() {
        return this.curCameraEvent == DataCameraEvent.ConnectOK;
    }

    public boolean isRcConnectNormal() {
        return this.mRcReceiveDataState == RcReceiveDataState.NORMAL;
    }

    /* access modifiers changed from: protected */
    public boolean filterInvalidPack(RecvPack pack) {
        return CmdSet.COMMON.value() == pack.cmdSet && CmdIdCommon.CmdIdType.CmdNeedEncrypt.value() == pack.cmdId;
    }

    /* access modifiers changed from: protected */
    public void handleAirConnection(RecvPack pack) {
        if (this.packCount % SQLiteDatabase.SQLITE_MAX_LIKE_PATTERN_LENGTH == 0) {
            DJILogHelper.getInstance().LOGE("DJIPackManager", "pack senderType=" + pack.senderType + " cmdSet=" + pack.cmdSet, false, false);
        }
        this.packCount++;
        if (this.packCount == Integer.MAX_VALUE) {
            this.packCount = 0;
        }
        if (!this.isPaused) {
            handleRcConnectState();
        }
        if (isPostPack && logCmdSet == pack.cmdSet && logCmdId == pack.cmdId) {
            EventBus.getDefault().post(pack);
        }
        if (this.packCount % 5 == 0 && this.isCheck && !filterInvalidPack(pack)) {
            this.connectCheckLock.lock();
            try {
                if (this.mAirPackIdentifier.isAirPack(pack)) {
                    if (this.curCameraEvent != DataCameraEvent.ConnectOK) {
                        setCurCameraEvent(DataCameraEvent.ConnectOK);
                        DataSpecialControl.getInstance().init().start(20);
                        DJILogHelper.getInstance().LOGE("DJIPackManager", "post DataCameraEvent.ConnectOK " + this.connectType);
                    }
                    if (this.connectType == DJIConnectType.RC || this.connectType == DJIConnectType.IDLE) {
                        removeDisconnectMsg();
                        if (!this.isPaused) {
                            sendDisconnectMsg(getDisconnectDelay(), 4);
                        }
                    }
                }
            } catch (Exception e) {
                DJILog.saveConnectDebug("DJIPackManager post curCameraEvent 5 exption : " + e.getMessage());
            } finally {
                this.connectCheckLock.unlock();
            }
        }
    }

    private long getDisconnectDelay() {
        long delay = (long) this.connectLosedelayMillis;
        ProductType productType = DJIProductManager.getInstance().getType();
        DJILinkType linkType = this.linkDaemonService.getLinkType();
        if (linkType != DJILinkType.WIFI) {
            if (linkType != DJILinkType.AOA) {
                return delay;
            }
            if (productType == ProductType.WM230 || productType == ProductType.WM160) {
                return IEventObjects.PopViewItem.DURATION_DISAPPEAR;
            }
            return delay;
        }
        return IEventObjects.PopViewItem.DURATION_DISAPPEAR;
    }
}
