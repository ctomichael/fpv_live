package dji.sdksharedlib.hardware.abstractions.flightcontroller.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.params.P3.ParamInfo;

@EXClassNullAway
public interface ParamInfoCallBack {
    void onFailure(Ccode ccode);

    void onSuccess(ParamInfo paramInfo);
}
