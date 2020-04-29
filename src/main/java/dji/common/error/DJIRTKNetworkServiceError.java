package dji.common.error;

public class DJIRTKNetworkServiceError extends DJIError {
    public static final DJIRTKNetworkServiceError ALREADY_STARTED = new DJIRTKNetworkServiceError("The service is already started. Stop it first to re-start the service.");
    public static final DJIRTKNetworkServiceError AUTHENTICATION_FAILURE = new DJIRTKNetworkServiceError("The authentication failed when trying to access to the server.");
    public static final DJIRTKNetworkServiceError INCORRECT_REFERENCE_STATION_SOURCE = new DJIRTKNetworkServiceError("Please choose correct reference station srouce.");
    public static final DJIRTKNetworkServiceError INVALID_GPS_DATA = new DJIRTKNetworkServiceError("The GPS location of the aircraft is invalid. The RTK network service requires the location of the air system.");
    public static final DJIRTKNetworkServiceError INVALID_SETTINGS = new DJIRTKNetworkServiceError("The network service settings are invalid.");

    protected DJIRTKNetworkServiceError(String desc) {
        super(desc);
    }
}
