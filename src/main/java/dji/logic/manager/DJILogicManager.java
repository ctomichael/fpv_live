package dji.logic.manager;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.camera.DJILogicCameraInfo;
import dji.logic.mc.DJILogicHomePoint;
import dji.logic.mc.DJIMcHelper;
import dji.logic.ofdm.DJILogicMaxMcs;
import dji.logic.vision.DJIVisionHelper;
import dji.logic.wifi.DJIWifiHelper;

@EXClassNullAway
public class DJILogicManager {
    private static DJILogicManager mInstance = null;

    public static synchronized DJILogicManager getInstance() {
        DJILogicManager dJILogicManager;
        synchronized (DJILogicManager.class) {
            if (mInstance == null) {
                mInstance = new DJILogicManager();
            }
            dJILogicManager = mInstance;
        }
        return dJILogicManager;
    }

    private DJILogicManager() {
        DJIMcHelper.getInstance();
        DJILogicHomePoint.getInstance();
        DJILogicCameraInfo.getInstance();
        DJILogicMaxMcs.getInstance();
        DJIVisionHelper.getInstance();
        DJIWifiHelper.getInstance();
    }

    public void destroy() {
        DJIMcHelper.getInstance().destroy();
        DJILogicHomePoint.getInstance().destroy();
        DJILogicCameraInfo.getInstance().destroy();
        DJIVisionHelper.getInstance().destroy();
        DJIWifiHelper.getInstance().destroy();
    }
}
