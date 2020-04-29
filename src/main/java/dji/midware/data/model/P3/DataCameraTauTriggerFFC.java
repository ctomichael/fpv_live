package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauTriggerFFC extends DataCameraTauParamer {
    public DataCameraTauTriggerFFC() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.TRIGGER_FFC;
        this.mbGet = false;
    }
}
