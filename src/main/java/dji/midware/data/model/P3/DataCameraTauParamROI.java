package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamROI extends DataCameraTauParamer {
    public DataCameraTauParamROI() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.INTEREST_REGION;
    }

    public DataCameraTauParamROI setType(ROIType type) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) type.value();
        return this;
    }

    public ROIType getType() {
        if (this._recData == null || this._recData.length <= 0) {
            return ROIType.FULL;
        }
        return ROIType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    @Keep
    public enum ROIType {
        FULL(0),
        SKY_EXCLUDED_33(1),
        SKY_EXCLUDED_50(2),
        OTHER(100);
        
        private int data;

        private ROIType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ROIType find(int b) {
            ROIType result = FULL;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
