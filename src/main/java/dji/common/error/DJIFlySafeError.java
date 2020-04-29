package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;

@EXClassNullAway
public class DJIFlySafeError extends DJIError {
    public static final DJIFlySafeError ACCOUNT_NOT_LOGGED_IN_OR_NOT_AUTHORIZED = new DJIFlySafeError("No logged in account or account did not get authorization.");
    public static final DJIFlySafeError COULD_NOT_CONNECT_TO_INTERNET_FOR_PULLING_DATA = new DJIFlySafeError("Could not connect to the Internet while SDK try to pull the latest cached data from server.");
    public static final DJIFlySafeError COULD_NOT_ENABLE_OR_DISABLE_GEO_SYSTEM_WHILE_AIRCRAFT_IS_IN_THE_SKY = new DJIFlySafeError("Could not enable or disable the GEO system while the aircraft is flying.");
    public static final DJIFlySafeError COULD_NOT_FIND_UNLOCKED_RECORD_IN_THE_SERVER = new DJIFlySafeError("Could not find unlocked record in the server.");
    public static final DJIFlySafeError FLIGHT_CONTROLLER_SERIAL_NUMBER_IS_NOT_READY = new DJIFlySafeError("The flight controller SN is not ready, could not start to execute next step, please try again later.");
    public static final DJIFlySafeError INVALID_SIMULATED_LOCATION = new DJIFlySafeError("INVALID simulation location.");
    public static final DJIFlySafeError NOT_LOGGED_IN = new DJIFlySafeError("No logged in account.");
    public static final DJIFlySafeError NO_DATA_IN_DATABASE = new DJIFlySafeError("No data in database");
    public static final DJIFlySafeError UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT = new DJIFlySafeError("Could not find unlocked record on the aircraft.");
    public static final DJIFlySafeError USER_MISMATCH = new DJIFlySafeError("User mismatch.");

    private DJIFlySafeError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(Ccode ccode) {
        if (COMMON_UNKNOWN != DJIError.getDJIError(ccode)) {
            return DJIError.getDJIError(ccode);
        }
        switch (ccode) {
            case TIMEOUT:
                return COMMON_TIMEOUT;
            default:
                return COMMON_UNKNOWN;
        }
    }
}
