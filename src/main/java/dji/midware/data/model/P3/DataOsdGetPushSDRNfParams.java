package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushSDRNfParams extends DataBase {
    private static DataOsdGetPushSDRNfParams instance = null;

    public static synchronized DataOsdGetPushSDRNfParams getInstance() {
        DataOsdGetPushSDRNfParams dataOsdGetPushSDRNfParams;
        synchronized (DataOsdGetPushSDRNfParams.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSDRNfParams();
            }
            dataOsdGetPushSDRNfParams = instance;
        }
        return dataOsdGetPushSDRNfParams;
    }

    public int get1KmOffset() {
        return ((Byte) get(0, 1, Byte.class)).byteValue();
    }

    public int getPathLossOffset() {
        return ((Byte) get(1, 1, Byte.class)).byteValue();
    }

    public int getRcLinkOffset() {
        return ((Byte) get(2, 1, Byte.class)).byteValue();
    }

    public int getTxPowerOffset() {
        return ((Byte) get(3, 1, Byte.class)).byteValue();
    }

    public DisLossEvent getDisLossInd() {
        return DisLossEvent.find(((Integer) get(4, 1, Integer.class)).intValue());
    }

    public int getSigBarInd() {
        return ((Integer) get(5, 1, Integer.class)).intValue();
    }

    public int getDlPwrAccu() {
        return ((Integer) get(6, 1, Integer.class)).intValue();
    }

    public int getMaxNf20M() {
        return ((Integer) get(7, 2, Integer.class)).intValue();
    }

    public int getMinNf20M() {
        return ((Integer) get(9, 2, Integer.class)).intValue();
    }

    public int getMaxNf10M() {
        return ((Integer) get(11, 2, Integer.class)).intValue();
    }

    public int getMinNf10M() {
        return ((Integer) get(13, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DisLossEvent {
        NONE(0),
        GROUND_INTERFERED(1),
        UAV_INTERFERED(2),
        SIGNAL_BLOCK(3);
        
        private int value;

        private DisLossEvent(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static DisLossEvent find(int value2) {
            DisLossEvent result = NONE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
