package dji.common.error;

import dji.diagnostics.model.DJIDiagnosticsError;

public class DJIFlightHubError extends DJIError {
    public static final DJIFlightHubError AIRCRAFT_ALREADY_BOUND_IN_OTHER_TEAM = new DJIFlightHubError("The aircraft has already been bound in the other team. Please unbind it first.");
    public static final DJIFlightHubError AIRCRAFT_ALREADY_BOUND_IN_TEAM_CHOSEN = new DJIFlightHubError("The aircraft has already been bound in chosen team.");
    public static final DJIFlightHubError AIRCRAFT_SERIAL_NUMBER_IS_NOT_AVAILABLE = new DJIFlightHubError("Could not get serial number.");
    public static final DJIFlightHubError AUTHORIZATION_EXPIRED = new DJIFlightHubError("Authorization information has expired. Please authorize again.");
    public static final DJIFlightHubError BOUND_DEVICE_LIMITATION_REACHED = new DJIFlightHubError("Bound device number has reached the limit of your service package.");
    public static final DJIFlightHubError NOT_LOGGED_IN = new DJIFlightHubError("You are not logged in.");
    public static final DJIFlightHubError NO_AIRCRAFT_FOUND = new DJIFlightHubError("No aircraft found.");
    public static final DJIFlightHubError NO_AUTHORIZATION_INFORMATION_FOUND = new DJIFlightHubError("No authorization information found.");
    public static final DJIFlightHubError NO_RECORD_FOUND = new DJIFlightHubError("No record found.");
    public static final DJIFlightHubError NO_RIGHT_TO_ACCESS = new DJIFlightHubError("You have no right to access the data.");
    public static final DJIFlightHubError NO_SDK_PERMISSION_FOR_SERVICE_PACKAGE = new DJIFlightHubError("Service package does not have permission to access SDK service.");
    public static final DJIFlightHubError NO_TEAM_FOUND = new DJIFlightHubError("No team found.");
    public static final DJIFlightHubError PARAMETERS_INVALID = new DJIFlightHubError("Invalid parameter(s).");
    public static final DJIFlightHubError SERVICE_PACKAGE_EXPIRED = new DJIFlightHubError("Your service package has expired.");
    public static final DJIFlightHubError SERVICE_PACKAGE_LIMITATION_REACHED = new DJIFlightHubError("Your service package has reached the limit of device number. Please check if your service package has expired.");
    public static final DJIFlightHubError SIGNATURE_INVALID = new DJIFlightHubError("Invalid signature.");
    public static final DJIFlightHubError USER_NOT_FOUND_IN_FLIGHT_HUB = new DJIFlightHubError("User not found in FlightHub.");
    public static final DJIFlightHubError USER_NOT_FOUND_IN_USER_CENTER = new DJIFlightHubError("The account information is not found.");

    protected DJIFlightHubError(String desc) {
        super(desc);
    }

    public static DJIError getDJIError(int errorCode) {
        switch (errorCode) {
            case -1:
            case 500:
            case 1015:
                return COMMON_UNKNOWN;
            case 401:
                return SIGNATURE_INVALID;
            case 1005:
                return SERVICE_PACKAGE_EXPIRED;
            case 1006:
                return SERVICE_PACKAGE_LIMITATION_REACHED;
            case 1018:
            case 6003:
                return USER_NOT_FOUND_IN_FLIGHT_HUB;
            case 2001:
                return NO_RIGHT_TO_ACCESS;
            case 3001:
                return AIRCRAFT_ALREADY_BOUND_IN_TEAM_CHOSEN;
            case 3003:
                return AIRCRAFT_ALREADY_BOUND_IN_OTHER_TEAM;
            case 3006:
                return NO_RECORD_FOUND;
            case 3009:
                return BOUND_DEVICE_LIMITATION_REACHED;
            case 4001:
                return PARAMETERS_INVALID;
            case 4041:
                return NO_TEAM_FOUND;
            case 4042:
                return NO_AIRCRAFT_FOUND;
            case 6000:
                return NO_AUTHORIZATION_INFORMATION_FOUND;
            case DJIDiagnosticsError.VideoDecoder.ENCRYPTION_ERROR /*6001*/:
                return USER_NOT_FOUND_IN_USER_CENTER;
            case DJIDiagnosticsError.VideoDecoder.CONNECT_TO_DESERIALIZER_ERROR /*6002*/:
                return AUTHORIZATION_EXPIRED;
            case 6006:
                return NO_SDK_PERMISSION_FOR_SERVICE_PACKAGE;
            default:
                return COMMON_UNKNOWN;
        }
    }
}
