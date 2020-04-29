package dji.apppublic.reflect;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AppPubInjectManager {
    private static AppPubInjectManager instance = null;
    private AppPubToP3Injectable appPubToP3Injectable;

    public static AppPubInjectManager getInstance() {
        if (instance == null) {
            instance = new AppPubInjectManager();
        }
        return instance;
    }

    private AppPubInjectManager() {
    }

    public static AppPubToP3Injectable getAppPubToP3Injectable() {
        return getInstance().appPubToP3Injectable;
    }

    public static void setAppPubToP3Injectable(AppPubToP3Injectable appPubToP3Injectable2) {
        getInstance().appPubToP3Injectable = appPubToP3Injectable2;
    }
}
