package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamIsothermUnit extends DataCameraTauParamer {
    public DataCameraTauParamIsothermUnit() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.ISOTHERM_UNIT;
    }

    public DataCameraTauParamIsothermUnit setUnit(int unit) {
        this.mParams = new byte[1];
        this.mParams[0] = (byte) unit;
        return this;
    }

    public int getUnit() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }
}
