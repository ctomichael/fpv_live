package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGlassPushParam extends DataBase {
    private static DataGlassPushParam instance = null;

    public static synchronized DataGlassPushParam getInstance() {
        DataGlassPushParam dataGlassPushParam;
        synchronized (DataGlassPushParam.class) {
            if (instance == null) {
                instance = new DataGlassPushParam();
            }
            dataGlassPushParam = instance;
        }
        return dataGlassPushParam;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public GlassType getGlassType() {
        return GlassType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum GlassType {
        Unknown(0, "Unknown"),
        ZV810(1, "goggles"),
        ZV810Adv(2, "gogglesAdv"),
        ZV820(3, "goggles2"),
        OTHER(255, "OTHER");
        
        private static volatile GlassType[] sValues = null;
        private int data;
        public String names;

        private GlassType(int _data, String name) {
            this.data = _data;
            this.names = name;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GlassType find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            GlassType result = OTHER;
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return result;
        }
    }
}
