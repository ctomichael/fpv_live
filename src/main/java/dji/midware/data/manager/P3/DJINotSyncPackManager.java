package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;

@EXClassNullAway
public class DJINotSyncPackManager extends DJIPackManagerBase {
    private static DJINotSyncPackManager instance = null;
    private int connectLosedelayMillis = 1000;

    public static synchronized DJINotSyncPackManager getInstance() {
        DJINotSyncPackManager dJINotSyncPackManager;
        synchronized (DJINotSyncPackManager.class) {
            if (instance == null) {
                instance = new DJINotSyncPackManager();
            }
            dJINotSyncPackManager = instance;
        }
        return dJINotSyncPackManager;
    }

    private DJINotSyncPackManager() {
        this.enabledSetDataEvent = true;
    }

    /* access modifiers changed from: protected */
    public void handleAirConnection(RecvPack pack) {
        if (this.isCheck && (pack.cmdSet == 2 || pack.senderType == DeviceType.OFDM.value() || pack.cmdSet == 3 || pack.cmdSet == 4 || pack.cmdSet == 12)) {
            if (this.curCameraEvent != DataCameraEvent.ConnectOK) {
                setCurCameraEvent(DataCameraEvent.ConnectOK);
                DataSpecialControl.getInstance().init().start(20);
            }
            removeDisconnectMsg();
            long delay = (long) this.connectLosedelayMillis;
            if (DJIProductManager.getInstance().getType() == ProductType.WM230 && DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.AOA) {
                delay = (long) (this.connectLosedelayMillis * 2);
            }
            sendDisconnectMsg(delay, 1);
        }
        if (pack.cmdSet != CmdSet.COMMON.value() || pack.cmdId == CmdIdCommon.CmdIdType.TranslateData.value()) {
        }
    }
}
