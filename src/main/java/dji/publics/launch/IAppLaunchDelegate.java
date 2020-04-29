package dji.publics.launch;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IAppLaunchDelegate {
    public static final boolean APPLAUNCH_OPTIMIZE_EVENTBUS = true;
    public static final boolean APPLAUNCH_OPTIMIZE_FRLIB = true;
    public static final boolean APPLAUNCH_OPTIMIZE_PRODUCTCFG = true;

    void dispatchAppCreate(int i);

    void dispatchAppDestroy(int i);

    void dispatchLaunchPageCreated(int i);

    void dispatchMainPageCreated(int i);
}
