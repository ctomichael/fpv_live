package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushRawNewParam extends DJICameraDataBase {
    private static DataCameraGetPushRawNewParam instance = null;

    public static synchronized DataCameraGetPushRawNewParam getInstance() {
        DataCameraGetPushRawNewParam dataCameraGetPushRawNewParam;
        synchronized (DataCameraGetPushRawNewParam.class) {
            if (instance == null) {
                instance = new DataCameraGetPushRawNewParam();
            }
            dataCameraGetPushRawNewParam = instance;
        }
        return dataCameraGetPushRawNewParam;
    }

    @Keep
    public enum DJIShootingMode {
        RESERVED(0),
        EI_OFF(1),
        EI_ON(2);
        
        private int cmd = 0;

        private DJIShootingMode(int cmd2) {
            this.cmd = cmd2;
        }

        public int getCmd() {
            return this.cmd;
        }

        public static DJIShootingMode find(int cmd2) {
            DJIShootingMode[] values = values();
            for (DJIShootingMode mode : values) {
                if (mode.getCmd() == cmd2) {
                    return mode;
                }
            }
            return RESERVED;
        }
    }

    public DJIShootingMode getShootingMode() {
        return DJIShootingMode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getEIValue() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    public int getEIState() {
        return ((Integer) get(3, 2, Integer.class)).intValue();
    }

    public int getYScalerMin() {
        return ((Integer) get(5, 2, Integer.class)).intValue();
    }

    public int getYScalerMax() {
        return ((Integer) get(7, 2, Integer.class)).intValue();
    }

    public int getProxyLooksInt() {
        return ((Integer) get(9, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
