package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamRotate extends DataCameraTauParamer {
    public DataCameraTauParamRotate() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.PICTURE_ROTATE;
    }

    public DataCameraTauParamRotate setRotateType(RotateType type) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) type.value();
        return this;
    }

    public RotateType getRotateType() {
        if (this._recData == null || this._recData.length <= 0) {
            return RotateType.ANGLE_0;
        }
        return RotateType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum RotateType {
        ANGLE_0(0),
        ANGLE_90(1),
        ANGLE_180(2),
        ANGLE_270(3),
        OTHER(100);
        
        private int data;

        private RotateType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RotateType find(int b) {
            RotateType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
