package dji.pilot.publics.util;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ILogInterface {
    public static final byte[] LOG_DATA_MAGIC_NUMBER = {119, -1};
    public static final int TYPE_LOG = 0;
    public static final int VERSION_LOG = 0;

    public interface ILogDataType {
        public static final int TYPE_LOG_RC_AIRCRAFTTYPE = 1;
    }
}
