package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamThermometricEnable extends DataCameraTauParamer {
    public DataCameraTauParamThermometricEnable() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.THERMOMETRIC_TYPE;
    }

    public DataCameraTauParamThermometricEnable setType(ThermometricType type) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) type.value();
        return this;
    }

    public ThermometricType getThermometricType() {
        return ThermometricType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum ThermometricType {
        DISABLED(0),
        SPOT(1),
        AREA(2),
        OTHER(99);
        
        private final int _value;

        private ThermometricType(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return value == this._value;
        }

        public static ThermometricType find(int value) {
            ThermometricType[] values = values();
            for (ThermometricType tmp : values) {
                if (tmp._equals(value)) {
                    return tmp;
                }
            }
            return DISABLED;
        }
    }
}
