package dji.pilot.configs;

import dji.apppublic.reflect.AppPubInjectManager;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ICommonConfig {
    public static final boolean CHECK_RCTYPE_NEWAPP = true;
    public static final boolean USE_SPECIALCMD_GOHOME = true;
    public static final boolean isForDeveloper = false;
    public static final boolean isForFactory = AppPubInjectManager.getAppPubToP3Injectable().isFactoryPackage();
    public static final int isForRcUpgrade = 0;
}
