package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauFFCMode extends DataCameraTauParamer {
    public DataCameraTauFFCMode() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.FFC_MODE;
    }

    public DataCameraTauFFCMode setMode(FFCMode mode) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) mode.value();
        return this;
    }

    public FFCMode getMode() {
        if (this._recData == null || this._recData.length <= 0) {
            return FFCMode.AUTO;
        }
        return FFCMode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum FFCMode {
        MANUAL(0),
        AUTO(1),
        OTHER(100);
        
        private int data;

        private FFCMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FFCMode find(int b) {
            FFCMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
