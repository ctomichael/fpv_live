package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamTempUnit extends DataCameraTauParamer {
    public DataCameraTauParamTempUnit() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.TEMP_UNIT_IN_SPOT;
    }

    public DataCameraTauParamTempUnit setMode(TempUnit mode) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) mode.value();
        return this;
    }

    public TempUnit getGainMode() {
        return TempUnit.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum TempUnit {
        Fahrenheit(1),
        Celsius(2),
        OTHER(100);
        
        private int data;

        private TempUnit(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TempUnit find(int b) {
            TempUnit result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
