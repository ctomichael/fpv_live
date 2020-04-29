package dji.component.flysafe.inject;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FlyForbidInjectManager {
    private static FlyForbidInjectManager instance = null;
    private FlyForbidToP3Injectable flyForbidToP3Injectable;

    public static synchronized FlyForbidInjectManager getInstance() {
        FlyForbidInjectManager flyForbidInjectManager;
        synchronized (FlyForbidInjectManager.class) {
            if (instance == null) {
                instance = new FlyForbidInjectManager();
            }
            flyForbidInjectManager = instance;
        }
        return flyForbidInjectManager;
    }

    private FlyForbidInjectManager() {
    }

    public static FlyForbidToP3Injectable getFlyForbidToP3Injectable() {
        return getInstance().flyForbidToP3Injectable;
    }

    public static void setFlyForbidToP3Injectable(FlyForbidToP3Injectable flyForbidToP3Injectable2) {
        getInstance().flyForbidToP3Injectable = flyForbidToP3Injectable2;
    }
}
