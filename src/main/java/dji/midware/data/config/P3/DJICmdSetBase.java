package dji.midware.data.config.P3;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.interfaces.CmdIdInterface;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@EXClassNullAway
public abstract class DJICmdSetBase {
    SparseArray<CmdIdInterface> mIdCmdSpArray = new SparseArray<>();

    /* access modifiers changed from: protected */
    public abstract CmdIdInterface[] getCurCmdIdValues();

    public abstract String getDataModelName(int i, int i2, int i3);

    DJICmdSetBase() {
        CmdIdInterface[] cmdIds = getCurCmdIdValues();
        for (CmdIdInterface cmdId : cmdIds) {
            this.mIdCmdSpArray.put(cmdId.value(), cmdId);
        }
    }

    public CmdIdInterface getCmdId(int value) {
        CmdIdInterface[] cmdIds = getCurCmdIdValues();
        for (CmdIdInterface cmdId : cmdIds) {
            if (cmdId.value() == value) {
                return cmdId;
            }
        }
        return null;
    }

    public boolean isMix(int cmdId) {
        return this.mIdCmdSpArray.get(cmdId, DefaultCmdIdForAll.getInstance()).isMix();
    }

    public boolean isBlock(int cmdId) {
        return this.mIdCmdSpArray.get(cmdId, DefaultCmdIdForAll.getInstance()).isBlock();
    }

    public boolean isNeedCcode(int cmdId) {
        return this.mIdCmdSpArray.get(cmdId, DefaultCmdIdForAll.getInstance()).isNeedCcode();
    }

    public Class<? extends DataBase> getDataModel(int cmdId) {
        return this.mIdCmdSpArray.get(cmdId, DefaultCmdIdForAll.getInstance()).getDataModel();
    }

    public DataBase getDataBase(int cmdId) {
        return this.mIdCmdSpArray.get(cmdId, DefaultCmdIdForAll.getInstance()).getDataBase();
    }

    @Nullable
    public static DataBase getDataBaseObject(int cmdSet, int cmdId, int senderType, int senderId) {
        DJICmdSetBase cls = CmdSet.find(cmdSet).cmdIdClass();
        if (cls == null) {
            return null;
        }
        DataBase dataBase = cls.getDataBase(cmdId);
        if (dataBase != null) {
            return dataBase;
        }
        Class dataModel = cls.getDataModel(cmdId);
        if (dataModel == null) {
            try {
                dataModel = Class.forName(cls.getDataModelName(senderType, senderId, cmdId));
            } catch (ClassNotFoundException e) {
            }
        }
        if (dataModel == null) {
            return dataBase;
        }
        try {
            Method getInstance = dataModel.getMethod("getInstance", new Class[0]);
            getInstance.setAccessible(true);
            return (DataBase) getInstance.invoke(dataModel, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
            return dataBase;
        }
    }
}
