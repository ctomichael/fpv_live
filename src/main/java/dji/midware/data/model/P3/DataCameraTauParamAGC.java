package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamAGC extends DataCameraTauParamer {
    public DataCameraTauParamAGC() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.AGC;
    }

    public DataCameraTauParamAGC setType(AGCType type) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) type.value();
        return this;
    }

    public AGCType getType() {
        if (this._recData == null || this._recData.length <= 0) {
            return AGCType.LINEAR;
        }
        return AGCType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum AGCType {
        LINEAR(0),
        DEFAULT(1),
        SEASKY(2),
        OUTDOOR(3),
        INDOOR(4),
        MANUAL(5),
        USER1(6),
        USER2(7),
        USER3(8),
        OTHER(100);
        
        private int data;

        private AGCType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static AGCType find(int b) {
            AGCType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
