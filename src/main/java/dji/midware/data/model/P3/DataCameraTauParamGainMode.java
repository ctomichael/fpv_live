package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamGainMode extends DataCameraTauParamer {
    public DataCameraTauParamGainMode() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.GAIN_MODE;
    }

    public DataCameraTauParamGainMode setMode(GainMode mode) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) mode.value();
        return this;
    }

    public GainMode getGainMode() {
        return GainMode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum GainMode {
        AUTO(0),
        LOW(1),
        HIGH(2),
        OTHER(100);
        
        private int data;

        private GainMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GainMode find(int b) {
            GainMode result = AUTO;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
