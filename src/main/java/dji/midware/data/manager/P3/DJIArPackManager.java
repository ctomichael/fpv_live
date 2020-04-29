package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.packages.P3.RecvPack;

@EXClassNullAway
public class DJIArPackManager extends DJIPackManagerBase {
    private static DJIArPackManager instance = null;
    private DJIPackManager packManager = DJIPackManager.getInstance();

    public static synchronized DJIArPackManager getInstance() {
        DJIArPackManager dJIArPackManager;
        synchronized (DJIArPackManager.class) {
            if (instance == null) {
                instance = new DJIArPackManager();
            }
            dJIArPackManager = instance;
        }
        return dJIArPackManager;
    }

    public DJIArPackManager() {
        this.needCheckEncrypt = false;
    }

    /* access modifiers changed from: protected */
    public void handleAirConnection(RecvPack pack) {
        this.packManager.resumeConnectCheck();
    }
}
