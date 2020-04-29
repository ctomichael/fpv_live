package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.packages.P3.RecvPack;

@EXClassNullAway
public class DJIFlightLogPackManager extends DJIPackManagerBase {
    private static DJIFlightLogPackManager instance = null;
    private DJIPackManager packManager = DJIPackManager.getInstance();

    public static synchronized DJIFlightLogPackManager getInstance() {
        DJIFlightLogPackManager dJIFlightLogPackManager;
        synchronized (DJIFlightLogPackManager.class) {
            if (instance == null) {
                instance = new DJIFlightLogPackManager();
            }
            dJIFlightLogPackManager = instance;
        }
        return dJIFlightLogPackManager;
    }

    public DJIFlightLogPackManager() {
        this.needCheckEncrypt = false;
    }

    /* access modifiers changed from: protected */
    public void handleAirConnection(RecvPack pack) {
        this.packManager.resumeConnectCheck();
    }
}
