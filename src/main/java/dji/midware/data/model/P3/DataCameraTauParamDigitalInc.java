package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraTauParamDigitalInc extends DataCameraTauParamer {
    public DataCameraTauParamDigitalInc() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.DIGITAL_INC;
    }

    public DataCameraTauParamDigitalInc setValue(int value) {
        this.mParams = BytesUtil.getBytes((short) value);
        return this;
    }

    public int getValue() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }
}
