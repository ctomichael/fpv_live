package dji.midware.data.config.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import java.lang.reflect.Method;
import java.util.Locale;

@EXClassNullAway
public class DataUtil {
    public static String getDataModelName(String deviceName, String cmdName) {
        return "dji.midware.data.model.P3.Data" + deviceName.substring(0, 1).toUpperCase(Locale.ENGLISH) + deviceName.substring(1).toLowerCase(Locale.ENGLISH) + cmdName;
    }

    public static String getDataModelNameExtra(String deviceName, String cmdName) {
        return "com.dji.midware.extension.Data" + deviceName.substring(0, 1).toUpperCase(Locale.ENGLISH) + deviceName.substring(1).toLowerCase(Locale.ENGLISH) + cmdName;
    }

    public static String getDataModelNameNon(String deviceName, String cmdName) {
        return "dji.midware.data.model.P3.Data" + deviceName + cmdName;
    }

    public static DataBase getDataBaseInstRefl(Class<? extends DataBase> cls) {
        if (cls == null) {
            return null;
        }
        try {
            Method getInstance = cls.getMethod("getInstance", new Class[0]);
            getInstance.setAccessible(true);
            return (DataBase) getInstance.invoke(cls, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
