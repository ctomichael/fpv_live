package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraTauParamIsothermValue extends DataCameraTauParamer {
    public DataCameraTauParamIsothermValue setType(DataCameraTauParamer.ParamCmd type) {
        this.mParamCmd = type;
        return this;
    }

    public DataCameraTauParamIsothermValue setValue(short value) {
        this.mParams = BytesUtil.getBytes(value);
        return this;
    }

    public short getValue() {
        return ((Short) get(0, 2, Short.class)).shortValue();
    }
}
