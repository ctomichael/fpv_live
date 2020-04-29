package dji.p4upgrade.reflect;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class P4InjectManager {
    private static P4InjectManager instance = null;
    private P4ToP3Injectable p4ToP3Injectable;

    public static P4InjectManager getInstance() {
        if (instance == null) {
            instance = new P4InjectManager();
        }
        return instance;
    }

    private P4InjectManager() {
    }

    public static P4ToP3Injectable getP4ToP3Injectable() {
        return getInstance().p4ToP3Injectable;
    }

    public static void setP4ToP3Injectable(P4ToP3Injectable p4ToP3Injectable2) {
        getInstance().p4ToP3Injectable = p4ToP3Injectable2;
    }
}
