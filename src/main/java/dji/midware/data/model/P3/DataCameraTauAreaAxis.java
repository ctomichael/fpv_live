package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraTauAreaAxis extends DataCameraTauParamer {
    public DataCameraTauAreaAxis() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.AREA_AXIS;
    }

    public DataCameraTauAreaAxis setAxis(short left, short top, short right, short bottom) {
        this.mParams = new byte[8];
        System.arraycopy(BytesUtil.getBytes(left), 0, this.mParams, 0, 2);
        System.arraycopy(BytesUtil.getBytes(top), 0, this.mParams, 2, 2);
        System.arraycopy(BytesUtil.getBytes(right), 0, this.mParams, 4, 2);
        System.arraycopy(BytesUtil.getBytes(bottom), 0, this.mParams, 6, 2);
        return this;
    }
}
