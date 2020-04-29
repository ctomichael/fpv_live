package dji.midware.reflect;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MidwareInjectManager {
    private static MidwareInjectManager instance = null;
    private MidwareToVideoLibInjectable mMidwareToVideoLibInjectable;
    private MidwareToP3Injectable midwareToP3Injectable;

    public static synchronized MidwareInjectManager getInstance() {
        MidwareInjectManager midwareInjectManager;
        synchronized (MidwareInjectManager.class) {
            if (instance == null) {
                instance = new MidwareInjectManager();
            }
            midwareInjectManager = instance;
        }
        return midwareInjectManager;
    }

    private MidwareInjectManager() {
    }

    public static MidwareToP3Injectable getMidwareToP3Injectable() {
        return getInstance().midwareToP3Injectable;
    }

    public static void setMidwareToP3Injectable(MidwareToP3Injectable midwareToP3Injectable2) {
        getInstance().midwareToP3Injectable = midwareToP3Injectable2;
    }

    public static MidwareToVideoLibInjectable getMidwareToVideoLibInjectable() {
        return getInstance().mMidwareToVideoLibInjectable;
    }

    public static void setMidwareToVideoLibInjectable(MidwareToVideoLibInjectable midwareToVideoLibInjectable) {
        getInstance().mMidwareToVideoLibInjectable = midwareToVideoLibInjectable;
    }
}
