package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraTauParamOptizate extends DataCameraTauParamer {
    public DataCameraTauParamOptizate() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.SCENE_OPTIMIZATE;
    }

    public DataCameraTauParamOptizate setValue(int value) {
        this.mParams = BytesUtil.getBytes((short) value);
        return this;
    }

    public int getValue() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }
}
