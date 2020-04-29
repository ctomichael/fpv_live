package dji.sdksharedlib.keycatalog.extension;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class Utils {
    public static final String TAG = "Utils";

    public static Class[] parseAllClass(Class[] list) {
        if (list == null || list.length == 0) {
            return null;
        }
        List<Class> tmp = new ArrayList<>();
        boolean containsGroup = false;
        for (Class clazz : list) {
            if (IAbstractionGroup.class.isAssignableFrom(clazz)) {
                try {
                    Class[] classes = ((IAbstractionGroup) clazz.newInstance()).getAbstractions();
                    if (classes != null) {
                        for (Class c : classes) {
                            tmp.add(c);
                        }
                    }
                } catch (Exception e) {
                    DJILog.e(TAG, "error IAbstractionGroup", new Object[0]);
                }
                containsGroup = true;
            } else {
                tmp.add(clazz);
            }
        }
        if (!containsGroup) {
            return list;
        }
        Class[] retList = new Class[tmp.size()];
        tmp.toArray(retList);
        return retList;
    }

    public static Key[] parseAllKey(ComplexKey complexKeyAnnotation, Key keyAnnotation) {
        if (complexKeyAnnotation != null && keyAnnotation == null) {
            return complexKeyAnnotation.value();
        }
        if (complexKeyAnnotation == null && keyAnnotation != null) {
            return new Key[]{keyAnnotation};
        } else if (complexKeyAnnotation == null || keyAnnotation == null) {
            return null;
        } else {
            DJILog.e(TAG, "you can't define ComplexKey and Key both, please remove one", new Object[0]);
            return null;
        }
    }
}
