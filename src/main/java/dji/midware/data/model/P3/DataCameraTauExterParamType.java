package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauExterParamType extends DataCameraTauParamer {
    public DataCameraTauExterParamType() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.EXTER_PARAM_TYPE;
    }

    public DataCameraTauExterParamType setType(ExterParamType type) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) type.value();
        return this;
    }

    public ExterParamType getExterParamType() {
        return ExterParamType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum ExterParamType {
        USER1(0),
        USER2(1),
        USER3(2),
        OTHER(99);
        
        private final int _value;

        private ExterParamType(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return value == this._value;
        }

        public static ExterParamType find(int value) {
            ExterParamType[] values = values();
            for (ExterParamType tmp : values) {
                if (tmp._equals(value)) {
                    return tmp;
                }
            }
            return USER1;
        }
    }
}
