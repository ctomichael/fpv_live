package dji.midware.data.forbid;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class DJISetFlightLimitAreaModel {
    public int contryCode;
    public int latitude;
    public int longitude;
    public int radius;
    public int revers;
    public int type;
}
