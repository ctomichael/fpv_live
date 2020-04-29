package dji.midware.data.config.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.interfaces.CmdIdInterface;

@EXClassNullAway
public class DefaultCmdIdForAll implements CmdIdInterface {
    private static DefaultCmdIdForAll instance = null;

    public static synchronized DefaultCmdIdForAll getInstance() {
        DefaultCmdIdForAll defaultCmdIdForAll;
        synchronized (DefaultCmdIdForAll.class) {
            if (instance == null) {
                instance = new DefaultCmdIdForAll();
            }
            defaultCmdIdForAll = instance;
        }
        return defaultCmdIdForAll;
    }

    public boolean isMix() {
        return false;
    }

    public boolean isBlock() {
        return true;
    }

    public int value() {
        return 0;
    }

    public boolean isNeedCcode() {
        return false;
    }

    public Class<? extends DataBase> getDataModel() {
        return null;
    }

    public DataBase getDataBase() {
        return null;
    }
}
